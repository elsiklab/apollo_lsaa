package org.elsiklab

import grails.converters.JSON
import org.bbop.apollo.Organism

class AltLociSequenceController {

    def editScaffoldsService

    def index() { }

    def features() {
        def map = editScaffoldsService.getTransformedSequence(Organism.findByCommonName(params.organism))
        def start = Integer.parseInt(params.start) >= 0 ? Integer.parseInt(params.start) : 0
        def end = Integer.parseInt(params.end)
        render ([features: [[seq: map.substring(start, end), start: start, end: end]]] as JSON)
    }
}
