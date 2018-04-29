package grails.monitor

import javax.inject.Named

import org.springframework.context.annotation.Scope

import akka.actor.AbstractActor
import akka.actor.AbstractActor.Receive

@Named("MonitorActor")
@Scope("prototype")
class MonitorActor extends AbstractActor {

	@Override
	public Receive createReceive() {
		receiveBuilder().build()
	}

}
