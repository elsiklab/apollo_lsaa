package org.elsiklab



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class AddFastaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "GET"]


    def index(Integer max) {
        params.max = Math.min(max ?: 15, 100)
 
        def list = FastaFile.createCriteria().list(max: params.max, offset:params.offset) {
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


    def show(FastaFile addFasta) {
        respond addFasta
    }

    @Transactional
    def create() {
        def addFasta
        if(params.addFasta) {
            def f = File.createTempFile("fasta", null, new File(grailsApplication.config.lsaa.appStoreDirectory))
            f.withWriter { out ->
                out << params.addFasta
            }
            addFasta = new FastaFile(filename: f.getAbsolutePath(), username: "admin", dateCreated: new Date(), lastUpdated: new Date()).save()
        }
 
        else if(params.addFile) {
            if(new File(params.addFile).exists()) {
                addFasta = new FastaFile(filename: params.addFile, username: "admin", dateCreated: new Date(), lastUpdated: new Date()).save()
            }
            else {
                respond "Error: file does not exist", view: 'index', error: "Error"
            }
        }

        if (addFasta.hasErrors()) {
            respond addFasta.errors, view:'create'
            return
        }

        addFasta.save flush:true

        redirect(action: "index")
    }

    @Transactional
    def save(FastaFile addFasta) {
        if (addFasta == null) {
            notFound()
            return
        }

        if (addFasta.hasErrors()) {
            respond addFasta.errors, view:'create'
            return
        }

        addFasta.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'addFasta.label', default: 'AddFasta'), addFasta.id])
                redirect addFasta
            }
            '*' { respond addFasta, [status: CREATED] }
        }
    }

    def edit(FastaFile addFasta) {
        respond addFasta
    }

    @Transactional
    def update(FastaFile addFasta) {
        if (addFasta == null) {
            notFound()
            return
        }

        if (addFasta.hasErrors()) {
            respond addFasta.errors, view:'edit'
            return
        }

        addFasta.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'AddFasta.label', default: 'AddFasta'), addFasta.id])
                redirect addFasta
            }
            '*'{ respond addFasta, [status: OK] }
        }
    }

    @Transactional
    def delete(FastaFile addFasta) {
        log.debug "delete"

        if (addFasta == null) {
            notFound()
            return
        }

        addFasta.delete flush:true

        redirect(action: "index")
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'addFasta.label', default: 'AddFasta'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
