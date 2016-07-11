package org.elsiklab

import grails.converters.JSON
import grails.transaction.Transactional

import org.ho.yaml.Yaml
import org.ho.yaml.exception.YamlException

import org.bbop.apollo.FeatureLocation
import org.bbop.apollo.Sequence


import static org.springframework.http.HttpStatus.*


class AddFastaController {



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
