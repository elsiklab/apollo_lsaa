package org.elsiklab



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class AltFastaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "GET"]


    def index(Integer max) {
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

    @Transactional
    def create() {
        def altFasta
        if(params.addFasta) {
            def f = File.createTempFile("fasta", null, new File(grailsApplication.config.lsaa.appStoreDirectory))
            f.withWriter { out ->
                out << params.addFasta
            }
            altFasta = new AltFasta(filename: f.getAbsolutePath(), username: "admin", dateCreated: new Date(), lastUpdated: new Date()).save()
        }
 
        else if(params.addFile) {
            if(new File(params.addFile).exists()) {
                altFasta = new AltFasta(filename: params.addFile, username: "admin", dateCreated: new Date(), lastUpdated: new Date()).save()
            }
            else {
                respond "Error: file does not exist", view: 'index', error: "Error"
            }
        }

        if (altFasta.hasErrors()) {
            respond altFasta.errors, view:'create'
            return
        }

        altFasta.save flush:true

        redirect(action: "index")
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
        log.debug "delete"

        if (altFasta == null) {
            notFound()
            return
        }

        altFasta.delete flush:true

        redirect(action: "index")
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
