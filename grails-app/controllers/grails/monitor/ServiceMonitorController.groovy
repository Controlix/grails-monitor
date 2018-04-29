package grails.monitor

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class ServiceMonitorController {

    ServiceMonitorService serviceMonitorService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond serviceMonitorService.list(params), model:[serviceMonitorCount: serviceMonitorService.count()]
    }

    def show(Long id) {
        respond serviceMonitorService.get(id)
    }

    def create() {
        respond new ServiceMonitor(params)
    }

    def save(ServiceMonitor serviceMonitor) {
        if (serviceMonitor == null) {
            notFound()
            return
        }

        try {
            serviceMonitorService.save(serviceMonitor)
        } catch (ValidationException e) {
            respond serviceMonitor.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'serviceMonitor.label', default: 'ServiceMonitor'), serviceMonitor.id])
                redirect serviceMonitor
            }
            '*' { respond serviceMonitor, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond serviceMonitorService.get(id)
    }

    def update(ServiceMonitor serviceMonitor) {
        if (serviceMonitor == null) {
            notFound()
            return
        }

        try {
            serviceMonitorService.save(serviceMonitor)
        } catch (ValidationException e) {
            respond serviceMonitor.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'serviceMonitor.label', default: 'ServiceMonitor'), serviceMonitor.id])
                redirect serviceMonitor
            }
            '*'{ respond serviceMonitor, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        serviceMonitorService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'serviceMonitor.label', default: 'ServiceMonitor'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'serviceMonitor.label', default: 'ServiceMonitor'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
