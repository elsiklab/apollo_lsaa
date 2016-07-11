package org.elsiklab

import grails.converters.JSON
import grails.transaction.Transactional

import org.ho.yaml.Yaml
import org.ho.yaml.exception.YamlException

import org.bbop.apollo.FeatureLocation
import org.bbop.apollo.Sequence


import static org.springframework.http.HttpStatus.*


class AddFastaController {

    def index(Integer max) {
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
}
