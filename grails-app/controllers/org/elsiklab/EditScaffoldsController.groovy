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
        def text = new File("out.yaml").text
        try {
            def reference = new File("output.fasta").text
            def ret = Yaml.load(new File("out.yaml"))
            render view: "index", model: [yaml: text, reference: reference]
        }
        catch(YamlException e) {
            e.printStackTrace()
            render view: "index", model: [yaml: text, flash: [message: "Error parsing YAML"]]
        }
        catch(FileNotFoundException e) {
            e.printStackTrace()
            render view: "index", model: [yaml: text]
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
            EditScaffold.getAll().each { it ->
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

    def addFasta(Integer max) {
        if(params.addFasta) {
            def f = File.createTempFile("fasta", null, null)
            f.withWriter { out ->
                out << params.addFasta
            }
            new EditScaffold(filename: f.getAbsolutePath(), username: "admin", dateCreated: new Date(), lastUpdated: new Date()).save()
        }

        else if(params.addFile) {
            new EditScaffold(filename: params.addFile, username: "admin", dateCreated: new Date(), lastUpdated: new Date()).save()
        }

        params.max = Math.min(max ?: 15, 100)

        def list = EditScaffold.createCriteria().list(max: params.max, offset:params.offset) {
            if(params.sort=="username") {
                order('username', params.order)
            }
            if(params.sort=="filename") {
                order('filename', params.order)
            }
            else if(params.sort=="lastUpdated") {
                order('lastUpdated', params.order)
            }
            else if(params.sort=="dateCreated") {
                order('dateCreated', params.order)
            }
            else if(params.sort=="organism") {
                organism {
                    order('commonName',params.order)
                }
            }
        }

        render view: "addFasta", model: [features: list, featureCount: list.totalCount, sort: params.sort]
    }

    @Transactional
    def delete(EditScaffold editScaffold) {
        if (editScaffold == null) {
            notFound()
            return
        }

        editScaffold.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'EditScaffold.label', default: 'EditScaffold'), editScaffold.id
])
                redirect action:'index', method:'GET'
            }
            '*' { render status: NO_CONTENT }
        }
    }
    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'availableStatus.label', default: 'AlternativeLoci'), params.id])
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NOT_FOUND }
        }
    }



    def createReversal(String sequence, Integer start, Integer end, String description) {
        String name = UUID.randomUUID()
        System.err.println sequence
        Sequence s = Sequence.findByName(sequence)
        System.err.println s

        AlternativeLoci altloci = new AlternativeLoci(
            name: name,
            uniqueName: name,
            description: description ?: ""
        ).save(flush: true)
        System.err.println altloci

        FeatureLocation featureLoc = new FeatureLocation(
            fmin: start,
            fmax: end,
            feature: altloci,
            sequence: s
        ).save(flush:true)
        System.err.println featureLoc
        altloci.addToFeatureLocations(featureLoc)
        render ([success: true] as JSON)
    }
    def getReversals() {
         return AlternativeLoci.getAll()
    }
}
