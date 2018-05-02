package grails.monitor

import akka.http.javadsl.Http
import akka.http.javadsl.model.HttpMessage
import akka.http.javadsl.model.HttpMethods
import akka.http.javadsl.model.HttpRequest
import akka.http.javadsl.model.StatusCode
import akka.http.javadsl.model.StatusCodes
import akka.stream.ActorMaterializer
import groovy.util.logging.Slf4j

import java.time.Duration

import javax.annotation.PostConstruct
import javax.inject.Named

import org.springframework.context.annotation.Scope

import com.codahale.metrics.Gauge
import com.codahale.metrics.MetricRegistry

import akka.actor.AbstractActorWithTimers
import akka.actor.AbstractActor.Receive
import grails.plugin.dropwizard.ast.MetricRegistryAware

import java.util.concurrent.CompletionStage
import java.util.concurrent.TimeUnit

import static akka.pattern.PatternsCS.pipe

@Slf4j
@Named("MonitorActor")
@Scope("prototype")
class MonitorActor extends AbstractActorWithTimers implements MetricRegistryAware {

    private static final String TIMER_KEY = "monitor-timer#"

    ServiceMonitor serviceMonitor
    Boolean status = false

    Http http = Http.get(context().system())
    def dispatcher = context().dispatcher()
    def materializer = ActorMaterializer.create(context())

    static class StartChecking {}

    static class CheckNow {}

    static class CheckResult {
        StatusCode status
    }

    MonitorActor(ServiceMonitor serviceMonitor) {
        this.serviceMonitor = serviceMonitor
        getTimers().startSingleTimer(TIMER_KEY + serviceMonitor.id, new StartChecking(), Duration.ofMillis(200L))
    }

    @PostConstruct
    def registerStatusGauge() {
        def name = MetricRegistry.name(MonitorActor, serviceMonitor.name, serviceMonitor.method, "status")
        metricRegistry.register(name, new Gauge<Boolean>() {
            Boolean getValue() {
                status
            }
        })
        log.debug "gauge {} registered", name
    }

    @Override
    public Receive createReceive() {
        receiveBuilder()
                .match(StartChecking, { getTimers().startPeriodicTimer(TIMER_KEY + serviceMonitor.id, new CheckNow(), Duration.ofSeconds(5L)) })
                .match(CheckNow, { pipe(check(), dispatcher).to(self()) })
                .match(CheckResult, { status = it.status.isSuccess() })
                .build()
    }

    def check() {
        def request = HttpRequest.create(serviceMonitor.url).withMethod(HttpMethods.lookup(serviceMonitor.method).get())
        log.debug "Check {} with request {}", serviceMonitor, request

        http.singleRequest(request).thenApply( {println it; new CheckResult(status: it.status)})
    }
}
