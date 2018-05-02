package grails.monitor

class ServiceMonitor {

    String name
    String url
    String method = 'GET'

    static constraints = {
        name blank: false, maxSize: 20, unique: true
        url blank: false
        method inList: ["GET", "POST"]
    }
}
