package org.elsiklab

import grails.converters.JSON
import groovy.json.JsonBuilder
import org.bbop.apollo.Sequence

class AltLociTrackController {

    def features() {
        Sequence sequence = Sequence.findByName(params.id)
        def features = AlternativeLoci.createCriteria().list {
            featureLocations {
                eq('sequence', sequence)
            }
        }
        JsonBuilder json = new JsonBuilder ()
        json.features features, { it ->
            uniqueID it.uniqueName
            start it.featureLocation.fmin - 1
            end it.featureLocation.fmax - 1
            ref it.featureLocation.sequence.name
            description it.description
            color 'rgba(50,50,190,0.2)'
        }

        render json.toString()
    }

    def globalStats() {
        render ([] as JSON)
    }
}
