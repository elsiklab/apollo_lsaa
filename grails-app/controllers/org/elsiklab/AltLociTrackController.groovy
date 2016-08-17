package org.elsiklab

import grails.converters.JSON
import groovy.json.JsonBuilder
import org.bbop.apollo.Sequence

class AltLociTrackController {

    def index() { }

    def features() {
        Sequence sequence = Sequence.findByName(params.id)
        def features = AlternativeLoci.createCriteria().list {
            featureLocations {
                eq('sequence', sequence)
            }   
        }   
        JsonBuilder json = new JsonBuilder ()
        json.features features, { it ->
            start it.featureLocation.fmin
            uniqueID it.uniqueName
            end it.featureLocation.fmax
            ref it.featureLocation.sequence.name
            description it.description
            color 'rgba(50,50,190,0.2)'
            seqName it.uniqueName
        }   

        render json.toString()
    }

    def globalStats() {
        render ([] as JSON)
    }
}
