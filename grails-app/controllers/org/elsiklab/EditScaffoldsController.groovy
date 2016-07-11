package org.elsiklab

import grails.converters.JSON
import grails.transaction.Transactional

import org.ho.yaml.Yaml
import org.ho.yaml.exception.YamlException

import org.bbop.apollo.FeatureLocation
import org.bbop.apollo.Sequence


import static org.springframework.http.HttpStatus.*


class EditScaffoldsController {

    def index() {
        def yamlfile = new File("out.yaml")
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
        new File("out.yaml").withWriter { out ->
            out.write params.scaffoldEditor
        }
        redirect(action: "index")
    }

    def generateScaffolds() {
        new File("temp.fa").withWriter { temp ->
            AltFasta.getAll().each { it ->
                new File(it.filename).withReader { input ->
                    temp << input
                }
            }

            new File("out.fa").withWriter { out ->
                ("scaffolder sequence out.yaml temp.fa").execute().waitForProcessOutput(out, System.err)
            }
        }
        redirect(action: "downloadFasta")
    }

    def downloadFasta() {
        log.debug "downloading"
        new File("out.fa").withReader { stream ->
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
