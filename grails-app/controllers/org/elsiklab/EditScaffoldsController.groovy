package org.elsiklab

import grails.converters.JSON
import grails.transaction.Transactional

import org.ho.yaml.Yaml
import org.ho.yaml.exception.YamlException

import org.bbop.apollo.FeatureLocation
import org.bbop.apollo.Sequence


import static org.springframework.http.HttpStatus.*


class EditScaffoldsController {


    def grailsApplication

    def index() {
        def yamlfile = new File("${grailsApplication.config.appStoreDirectory}/out.yaml")
        try {
            def ret = Yaml.load(yamlfile)
            render view: "index", model: [yaml: yamlfile.text]
        }
        catch(YamlException e) {
            e.printStackTrace()
            render view: "index", model: [yaml: yamlfile.text, flash: [message: "Error parsing YAML"]]
        }
        catch(FileNotFoundException e) {
            e.printStackTrace()
            render view: "index", model: [yaml: ""]
        }
    }

    def editScaffold() {
        new File("${grailsApplication.config.appStoreDirectory}/out.yaml").withWriter { out ->
            out.write params.scaffoldEditor
        }
        redirect(action: "index")
    }

    def generateScaffolds() {
        new File("${grailsApplication.config.appStoreDirectory}/temp.fa").withWriter { temp ->
            AltFasta.getAll().each { it ->
                new File(it.filename).withReader { input ->
                    temp << input
                }
            }
            def ap = grailsApplication.config.appStoreDirectory

            new File("${grailsApplication.config.appStoreDirectory}/out.fa").withWriter { out ->
                ("scaffolder sequence ${grailsApplication.config.appStoreDirectory}/out.yaml ${grailsApplication.config.appStoreDirectory}/temp.fa").execute().waitForProcessOutput(out, System.err)
            }
        }
        redirect(action: "downloadFasta")
    }

    def downloadFasta() {
        new File("${grailsApplication.config.appStoreDirectory}/out.fa").withReader { stream ->
            response.setHeader "Content-disposition", "attachment;filename=output.fa"
            response.contentType = 'application/octet-stream'
            response.outputStream << stream
            response.outputStream.flush()
        }
    }

    def createReversal(String sequence, Integer start, Integer end, String description) {
        String name = UUID.randomUUID()
        Sequence s = Sequence.findByName(sequence)
 
        AlternativeLoci altloci = new AlternativeLoci(
            name: name,
            uniqueName: name,
            description: description ?: "",
            filename: "blah"
        ).save(flush: true,failOnError: true)
 
        FeatureLocation featureLoc = new FeatureLocation(
            fmin: start,
            fmax: end,
            feature: altloci,
            sequence: s
        ).save(flush:true)
        altloci.addToFeatureLocations(featureLoc)
        render ([success: true] as JSON)
    }
    def getReversals() {
         return AlternativeLoci.getAll()
    }
}
