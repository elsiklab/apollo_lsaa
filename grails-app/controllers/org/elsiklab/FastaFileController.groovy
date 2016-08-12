package org.elsiklab

import static org.springframework.http.HttpStatus.*

import grails.converters.JSON
import grails.transaction.Transactional
import org.apache.commons.io.FileUtils
import java.text.SimpleDateFormat

@Transactional(readOnly = true)
class FastaFileController {

    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'GET']

    def index(Integer max) {
        params.max = Math.min(max ?: 15, 100)

        def list = FastaFile.createCriteria().list(max: params.max, offset:params.offset) {
            if(params.sort == 'username') {
                order ('username', params.order)
            }
            if(params.sort == 'filename') {
                order ('filename', params.order)
            }
            else if(params.sort == 'lastUpdated') {
                order ('lastUpdated', params.order)
            }
            else if(params.sort == 'dateCreated') {
                order ('dateCreated', params.order)
            }
            else if(params.sort == 'organism') {
                organism {
                    order ('commonName',params.order)
                }
            }
        }

        render view: 'index', model: [features: list, featureCount: list.totalCount, sort: params.sort]
    }

    def show(FastaFile fastaFile) {
        respond fastaFile
    }

    @Transactional
    def uploadFile() {
        def fastaFile
        def fileStream = request.getFile('fastaFile').inputStream
        def targetFile = File.createTempFile('fasta', null, new File(grailsApplication.config.lsaa.appStoreDirectory))
        FileUtils.copyInputStreamToFile(fileStream, targetFile)
        fastaFile = new FastaFile(
            filename: targetFile.absolutePath,
            username: 'admin',
            dateCreated: new Date(),
            lastUpdated: new Date(),
            originalname: request.getFile('fastaFile').originalFilename
        ).save(flush: true)

        redirect(action: 'index')
    }
    @Transactional
    def uploadText() {
        def fastaFile
        def targetFile = File.createTempFile('fasta', null, new File(grailsApplication.config.lsaa.appStoreDirectory))
        targetFile.withWriter { out ->
            out << params.fastaFile
        }
        def now = new Date()

        fastaFile = new FastaFile(
            filename: targetFile.absolutePath,
            username: 'admin',
            dateCreated: now,
            lastUpdated: now,
            originalname: 'admin-' + new SimpleDateFormat("E YYMMdd_HHmmss").format(now)
        ).save(flush: true)

        redirect(action: 'index')
    }

    @Transactional
    def save(FastaFile fastaFile) {
        if (fastaFile == null) {
            notFound()
            return
        }

        if (fastaFile.hasErrors()) {
            respond fastaFile.errors, view:'create'
            return
        }

        fastaFile.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'fastaFile.label', default: 'AddFasta'), fastaFile.id])
                redirect fastaFile
            }
            '*' { respond fastaFile, [status: CREATED] }
        }
    }

    def edit(FastaFile fastaFile) {
        respond fastaFile
    }

    @Transactional
    def update(FastaFile fastaFile) {
        if (fastaFile == null) {
            notFound()
            return
        }

        if (fastaFile.hasErrors()) {
            respond fastaFile.errors, view:'edit'
            return
        }

        fastaFile.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'AddFasta.label', default: 'AddFasta'), fastaFile.id])
                redirect fastaFile
            }
            '*'{ respond fastaFile, [status: OK] }
        }
    }

    @Transactional
    def delete(FastaFile fastaFile) {
        if (fastaFile == null) {
            notFound()
            return
        }

        def success = new File(fastaFile.filename).delete()
        if (!success) {
            log.warn 'Error deleting file '+fastaFile.filename
        }


        fastaFile.delete flush:true

        redirect(action: 'index')
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'fastaFile.label', default: 'AddFasta'), params.id])
                redirect action: 'index', method: 'GET'
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
