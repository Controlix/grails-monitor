package grails.monitor

import java.time.Duration

import javax.annotation.PostConstruct
import javax.inject.Named

import org.springframework.context.annotation.Scope

import com.codahale.metrics.Gauge
import com.codahale.metrics.MetricRegistry

import akka.actor.AbstractActorWithTimers
import akka.actor.AbstractActor.Receive
import grails.plugin.dropwizard.ast.MetricRegistryAware

@Named("MonitorActor")
@Scope("prototype")
class MonitorActor extends AbstractActorWithTimers implements MetricRegistryAware {

	private static final String TIMER_KEY = "monitor-timer#"

	ServiceMonitor serviceMonitor
	Boolean status = false

	static class StartChecking {}
	static class CheckNow {}

	MonitorActor(ServiceMonitor serviceMonitor) {
		this.serviceMonitor = serviceMonitor
		getTimers().startSingleTimer(TIMER_KEY + serviceMonitor.id, new StartChecking(), Duration.ofMillis(200L))
	}
	
	@PostConstruct
	def registerStatusGauge() {
		def name = MetricRegistry.name(MonitorActor, serviceMonitor.url, serviceMonitor.method, "status")
		metricRegistry.register(name, new Gauge<Boolean>() {
			Boolean getValue() {
				status
			}
		})
		println "gauge ${name} registered"
	}

	@Override
	public Receive createReceive() {
		receiveBuilder()
				.match(StartChecking, { getTimers().startPeriodicTimer(TIMER_KEY + serviceMonitor.id, new CheckNow(), Duration.ofSeconds(5L)) } )
				.match(CheckNow, { status = !status })
				.build()
	}
}
