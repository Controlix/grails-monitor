package grails.monitor

import grails.events.annotation.Subscriber

class MonitorService {
	
	def monitors = [:]

	@Subscriber
	def onSave(ServiceMonitor serviceMonitor) {
		monitors[serviceMonitor.id] = serviceMonitor
		println "Saved ${serviceMonitor}"
		println "All monitors ${monitors}"
	}
}
