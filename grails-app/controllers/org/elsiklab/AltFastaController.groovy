package org.elsiklab



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class AltFastaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond AltFasta.list(params), model:[altFastaCount: AltFasta.count()]
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
