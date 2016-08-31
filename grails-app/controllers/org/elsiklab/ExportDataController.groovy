package org.elsiklab

import static org.springframework.http.HttpStatus.*

import grails.converters.JSON
import org.bbop.apollo.FeatureLocation
import org.bbop.apollo.Sequence
import org.bbop.apollo.Organism
import org.ho.yaml.Yaml

class ExportDataController {

    def grailsApplication
    def exportDataService

    def index() {
        render view: 'index'
    }

    def getTransformedYaml() {
        def organism = Organism.findByCommonName(params.organism)?:Organism.findById(params.organism)
        if(organism) {
            def map = exportDataService.getTransformations(organism)
            response.contentType = 'text/plain'
            if(params.download == 'Download') {
                response.setHeader 'Content-disposition', 'attachment;filename=output.yaml'
            }
            render text: Yaml.dump(map)
        }
        else {
            render text: Yaml.dump([error: "Organism not found"])
        }
    }

    def getTransformedJSON() {
        def organism = Organism.findByCommonName(params.organism)?:Organism.findById(params.organism)
        if(organism) {
            def map = exportDataService.getTransformations(organism)
            response.contentType = 'application/json'
            if(params.download == 'Download') {
                response.setHeader 'Content-disposition', 'attachment;filename=output.json'
            }
            def json = map as JSON
            json.prettyPrint = true
            render text: json
        }
        else {
            render ([error: "Organism not found"] as JSON)
        }
    }

    def getTransformedSequence() {
        def organism = Organism.findByCommonName(params.organism)?:Organism.findById(params.organism)
        if(organism) {
            def map = exportDataService.getTransformedSequence(organism)
            if(params.download == 'Download') {
                response.setHeader 'Content-disposition', 'attachment;filename=output.fa'
            }
            response.contentType = 'text/plain'
            render text: map
        }
        else {
            render ([error: "Organism not found"] as JSON)
        }
    }

    def export() {
        if(params.type == 'YAML') {
            getTransformedYaml()
        }
        else if(params.type == 'JSON') {
            getTransformedJSON()
        }
        else if(params.type == 'FASTA') {
            getTransformedSequence()
        }
        else {
            render text: 'Unknown download method'
        }
    }
}
