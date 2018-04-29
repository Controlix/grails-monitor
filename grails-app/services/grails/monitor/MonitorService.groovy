package grails.monitor

import grails.events.Event
import grails.events.annotation.Subscriber

class MonitorService {
	
	def monitors = [:]

	@Subscriber
	def onSave(ServiceMonitor serviceMonitor) {
		monitors[serviceMonitor.id] = serviceMonitor
		log.info "Saved {}", serviceMonitor
		log.info "All monitors {}", monitors
	}
	
	@Subscriber
	def onDelete(Event event) {
		monitors.remove(event.parameters.id)
		log.info "Deleted {}", event.parameters.id
		log.info "All monitors {}", monitors
	}
}
