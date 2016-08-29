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
        def organism = Organism.findById(params.organism)
        def map = exportDataService.getTransformations(organism)
        response.contentType = 'text/plain'
        if(params.download == 'Download') {
            response.setHeader 'Content-disposition', 'attachment;filename=output.yaml'
        }
        render text: Yaml.dump(map)
    }

    def getTransformedJSON() {
        def organism = Organism.findById(params.organism)
        def map = exportDataService.getTransformations(organism)
        response.contentType = 'application/json'
        if(params.download == 'Download') {
            response.setHeader 'Content-disposition', 'attachment;filename=output.json'
        }
        def json = map as JSON
        json.prettyPrint = true
        render text: json
    }

    def getTransformedSequence() {
        def organism = Organism.findById(params.organism)
        def map = exportDataService.getTransformedSequence(organism)
        if(params.download == 'Download') {
            response.setHeader 'Content-disposition', 'attachment;filename=output.fa'
        }
        response.contentType = 'text/plain'
        render text: map
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
