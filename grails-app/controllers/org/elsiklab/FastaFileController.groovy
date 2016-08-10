package org.elsiklab

import static org.springframework.http.HttpStatus.*

import grails.converters.JSON
import grails.transaction.Transactional
import htsjdk.samtools.reference.IndexedFastaSequenceFile
import org.biojava.nbio.core.sequence.DNASequence
import org.apache.commons.io.FileUtils

@Transactional(readOnly = true)
class FastaFileController {

    static allowedMethods = [save: 'POST', update: 'PUT', delete: 'GET']

    def index(Integer max) {
        params.max = Math.min(max ?: 15, 100)

        def list = FastaFile.createCriteria().list(max: params.max, offset:params.offset) {
            if(params.sort == 'username') {
                order('username', params.order)
            }
            if(params.sort == 'filename') {
                order('filename', params.order)
            }
            else if(params.sort == 'lastUpdated') {
                order('lastUpdated', params.order)
            }
            else if(params.sort == 'dateCreated') {
                order('dateCreated', params.order)
            }
            else if(params.sort == 'organism') {
                organism {
                    order('commonName',params.order)
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
        def fileStream = request.getFile('fastaFile').getInputStream()
        def targetFile = File.createTempFile('fasta', null, new File(grailsApplication.config.lsaa.appStoreDirectory))
        FileUtils.copyInputStreamToFile(fileStream, targetFile)
        log.debug targetFile.getAbsolutePath()
        fastaFile = new FastaFile(
            filename: targetFile.getAbsolutePath(),
            username: 'admin',
            dateCreated: new Date(),
            lastUpdated: new Date()
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

        fastaFile = new FastaFile(
            filename: f.getAbsolutePath(),
            username: 'admin',
            dateCreated: new Date(),
            lastUpdated: new Date()
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


    def getTransformedSequence() {
        def res = AlternativeLoci.createCriteria().list() {
            featureLocations {
                order('fmin','ascending')
            }
        }
        log.debug res

        def map = []
        def prevstart = 1
        def current

        res.eachWithIndex { it, i ->
            log.debug it
            if(i > 0) {
                map << current
            }
            map << [
                sequence: [
                    source: it.name,
                    start: prevstart,
                    stop: it.featureLocation.fmin-1
                ]
            ]
            map << [
                sequence: [
                    source: it.name,
                    start: it.featureLocation.fmin,
                    stop: it.featureLocation.fmax,
                    reverse: it.reversed ?: false
                ]
            ]
            map << [
                sequence: [
                    source: it.name,
                    start: it.featureLocation.fmax + 1,
                    stop: i == res.size() - 1 ? (it.end_file - it.start_file) - 1 : res[i + 1].featureLocation.fmin
                ]
            ]
            prevstart = it.featureLocation.fmin
        }

        log.debug map
        render map as JSON
    }

    def saveVideoFile(){
        
    }
}
