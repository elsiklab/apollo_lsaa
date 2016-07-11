package org.elsiklab



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class AltFastaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]


    def index(Integer max) {
        if(params.addFasta) {
            def f = File.createTempFile("fasta", null, null)
            f.withWriter { out ->
                out << params.addFasta
            }
            new AltFasta(filename: f.getAbsolutePath(), username: "admin", dateCreated: new Date(), lastUpdated: new Date()).save()
        }
 
        else if(params.addFile) {
            new AltFasta(filename: params.addFile, username: "admin", dateCreated: new Date(), lastUpdated: new Date()).save()
        }

        params.max = Math.min(max ?: 15, 100)
 
        def list = AltFasta.createCriteria().list(max: params.max, offset:params.offset) {
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
 
        render view: "index", model: [features: list, featureCount: list.totalCount, sort: params.sort]
    }


    def show(AltFasta altFasta) {
        respond altFasta
    }

    def create() {
        respond new AltFasta(params)
    }

    @Transactional
    def save(AltFasta altFasta) {
        if (altFasta == null) {
            notFound()
            return
        }

        if (altFasta.hasErrors()) {
            respond altFasta.errors, view:'create'
            return
        }

        altFasta.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'altFasta.label', default: 'AltFasta'), altFasta.id])
                redirect altFasta
            }
            '*' { respond altFasta, [status: CREATED] }
        }
    }

    def edit(AltFasta altFasta) {
        respond altFasta
    }

    @Transactional
    def update(AltFasta altFasta) {
        if (altFasta == null) {
            notFound()
            return
        }

        if (altFasta.hasErrors()) {
            respond altFasta.errors, view:'edit'
            return
        }

        altFasta.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'AltFasta.label', default: 'AltFasta'), altFasta.id])
                redirect altFasta
            }
            '*'{ respond altFasta, [status: OK] }
        }
    }

    @Transactional
    def delete(AltFasta altFasta) {

        if (altFasta == null) {
            notFound()
            return
        }

        altFasta.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'AltFasta.label', default: 'AltFasta'), altFasta.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'altFasta.label', default: 'AltFasta'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
