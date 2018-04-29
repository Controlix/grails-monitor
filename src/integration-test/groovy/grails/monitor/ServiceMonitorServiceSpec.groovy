package grails.monitor

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class ServiceMonitorServiceSpec extends Specification {

    ServiceMonitorService serviceMonitorService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new ServiceMonitor(...).save(flush: true, failOnError: true)
        //new ServiceMonitor(...).save(flush: true, failOnError: true)
        //ServiceMonitor serviceMonitor = new ServiceMonitor(...).save(flush: true, failOnError: true)
        //new ServiceMonitor(...).save(flush: true, failOnError: true)
        //new ServiceMonitor(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //serviceMonitor.id
    }

    void "test get"() {
        setupData()

        expect:
        serviceMonitorService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<ServiceMonitor> serviceMonitorList = serviceMonitorService.list(max: 2, offset: 2)

        then:
        serviceMonitorList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        serviceMonitorService.count() == 5
    }

    void "test delete"() {
        Long serviceMonitorId = setupData()

        expect:
        serviceMonitorService.count() == 5

        when:
        serviceMonitorService.delete(serviceMonitorId)
        sessionFactory.currentSession.flush()

        then:
        serviceMonitorService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        ServiceMonitor serviceMonitor = new ServiceMonitor()
        serviceMonitorService.save(serviceMonitor)

        then:
        serviceMonitor.id != null
    }
}
