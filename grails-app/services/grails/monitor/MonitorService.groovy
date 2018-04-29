package grails.monitor

import static conf.SpringExtension.SpringExtProvider

import akka.actor.ActorSystem
import akka.actor.Props
import grails.events.Event
import grails.events.annotation.Subscriber

class MonitorService {

	ActorSystem actorSystem

	def monitors = [:]

	@Subscriber
	def onSave(ServiceMonitor serviceMonitor) {
		if (! monitors[serviceMonitor.id]) {
			def requestId = UUID.randomUUID().toString()

			// describe the properties of the desired actor
			def actorType = "MonitorActor"
			Props props = SpringExtProvider.get(actorSystem).props(actorType)

			// ask akka to create the actor
			// use unique actor name because this will be an ephemeral, stateless actor
			// managed as a prototype bean in the spring context
			def actorName = "${actorType}-${requestId}"
			
			monitors[serviceMonitor.id] = actorSystem.actorOf(props, actorName)
		}

		log.info "Saved {}", serviceMonitor
		log.info "All monitors {}", monitors
	}

	@Subscriber
	def onDelete(Event event) {
		actorSystem.stop monitors[event.parameters.id]
		monitors.remove(event.parameters.id)
		
		log.info "Deleted {}", event.parameters.id
		log.info "All monitors {}", monitors
	}
}
