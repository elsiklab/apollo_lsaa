package org.elsiklab

import grails.converters.JSON

class LsaaController {

    def index() { }


    def createLSAA() {
        log.debug "create LSAA"
        render ([success: true] as JSON)
    }
}
