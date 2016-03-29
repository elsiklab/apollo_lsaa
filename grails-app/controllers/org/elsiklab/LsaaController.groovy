package org.elsiklab

class LsaaController {

    def index() { }


    def createLSAA() {
        log.debug "create LSAA"
        render ([success: "true"] as JSON)
    }
}
