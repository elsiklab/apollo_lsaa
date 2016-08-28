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

    def generateScaffolds() {

        def ap = grailsApplication.config.lsaa.appStoreDirectory

        new File("${grailsApplication.config.lsaa.appStoreDirectory}/out.fa").withWriter { out ->
            out << 'hello'
        }
        new File("${grailsApplication.config.lsaa.appStoreDirectory}/out.fa").withReader { stream ->
            response.setHeader 'Content-disposition', 'attachment;filename=output.fa'
            response.contentType = 'application/octet-stream'
            response.outputStream << stream
            response.outputStream.flush()
        }
    }


    def getTransformedYaml() {
        def organism = Organism.findById(params.organism);
        def map = exportDataService.getTransformations(organism)
        render text: Yaml.dump(map)
    }

    def getTransformedJSON() {
        def organism = Organism.findById(params.organism);
        def map = exportDataService.getTransformations(organism)
        render text: map as JSON
    }

    def getTransformedSequence() {
        def organism = Organism.findById(params.organism);
        def map = exportDataService.getTransformedSequence(organism)
        render text: map
    }

    def export() {
        if(params.type == "YAML") {
            getTransformedYaml()
        }
        else if(params.type == "JSON") {
            getTransformedJSON()
        }
        else if(params.type == "FASTA") {
            getTransformedSequence()
        }
        else {
            render text: "hello"
        }
    }

    
}
