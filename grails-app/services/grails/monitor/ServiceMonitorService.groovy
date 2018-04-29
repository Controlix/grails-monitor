package grails.monitor

import grails.events.annotation.Publisher
import grails.gorm.services.Service

@Service(ServiceMonitor)
interface ServiceMonitorService {

    ServiceMonitor get(Serializable id)

    List<ServiceMonitor> list(Map args)

    Long count()

	@Publisher
    void delete(Serializable id)

	@Publisher
    ServiceMonitor save(ServiceMonitor serviceMonitor)

}