package org.elsiklab

import static org.springframework.http.HttpStatus.*

import grails.converters.JSON
import org.bbop.apollo.FeatureLocation
import org.bbop.apollo.Sequence
import org.bbop.apollo.Organism

class EditScaffoldsController {

    def grailsApplication
    def editScaffoldsService

    def index() {
        render view: 'index'
    }

    def generateScaffolds() {
        new File("${grailsApplication.config.lsaa.appStoreDirectory}/temp.fa").withWriter { temp ->
            FastaFile.all.each { it ->
                new File(it.filename).withReader { input ->
                    temp << input
                }
            }
            def ap = grailsApplication.config.lsaa.appStoreDirectory

            new File("${grailsApplication.config.lsaa.appStoreDirectory}/out.fa").withWriter { out ->
                out << "Hello World!"
            }
        }
        redirect(action: 'downloadFasta')
    }

    def downloadFasta() {
        new File("${grailsApplication.config.lsaa.appStoreDirectory}/out.fa").withReader { stream ->
            response.setHeader 'Content-disposition', 'attachment;filename=output.fa'
            response.contentType = 'application/octet-stream'
            response.outputStream << stream
            response.outputStream.flush()
        }
    }


    def createReversal() {
        String name = UUID.randomUUID()
        Organism organism = Organism.findByCommonName(params.organism)
        if(!organism) {
            render text: ([error: 'No organism found'] as JSON), status: 500
        }
        else {
            Sequence seq = Sequence.findByNameAndOrganism(params.sequence, organism)
            FastaFile fastaFile = FastaFile.findByOrganism(organism)

            if(!seq) {
                render text: ([error: 'No sequence found'] as JSON), status: 500
            }
            else if(!fastaFile) {
                render text: ([error: 'No representative FASTA found for organism'] as JSON), status: 500
            }
            else {
                AlternativeLoci altloci = new AlternativeLoci(
                    name: params.sequence,
                    uniqueName: name,
                    description: params.description,
                    reversed: true,
                    start_file: 0,
                    end_file: new File(fastaFile.filename).length(),
                    fasta_file: fastaFile
                ).save(flush: true)

                FeatureLocation featureLoc = new FeatureLocation(
                    fmin: params.start,
                    fmax: params.end,
                    feature: altloci,
                    sequence: seq
                ).save(flush:true)

                altloci.addToFeatureLocations(featureLoc)

                render ([success: true] as JSON)
            }
        }
    }

    def createCorrection() {
        String name = UUID.randomUUID()
        Organism organism = Sequence.findByNameAndOrganism(params.organism)
        if(!organism) {
            render text: ([error: 'No organism found'] as JSON), status: 500
        }
        else {
            Sequence seq = Sequence.findByNameAndOrganism(params.sequence, organism)
            if(!seq) {
                render text: ([error: 'No sequence found'] as JSON), status: 500 
            } 
            else {
                def file = File.createTempFile('fasta', null, new File(grailsApplication.config.lsaa.appStoreDirectory))
                file.withWriter { temp ->
                    filename = temp.absolutePath
                    temp << ">${name}"
                    temp << params.sequencedata
                    fastaFile = new FastaFile(
                        filename: file.absolutePath,
                        username: 'admin',
                        dateCreated: new Date(),
                        lastModified: new Date(),
                        originalname: 'admin-' + new Date()
                    ).save(flush: true)
                }

                AlternativeLoci altloci = new AlternativeLoci(
                    name: name,
                    uniqueName: name,
                    description: params.description,
                    start_file: 0,
                    end_file: new File(fastaFile).length(),
                    fasta_file: fastaFile
                ).save(flush: true)

                FeatureLocation featureLoc = new FeatureLocation(
                    fmin: params.start,
                    fmax: start.end,
                    feature: altloci,
                    sequence: seq
                ).save(flush: true)

                altloci.addToFeatureLocations(featureLoc)

                render ([success: true] as JSON)
            }
        }
    }

    def getTransformedYaml() {
        def map = editScaffoldsService.getTransformedSequence(editScaffoldsService.getReversals())
        render text: Yaml.dump(map)
    }

    def getTransformedSequence() {
        def map = editScaffoldsService.getTransformedSequence(editScaffoldsService.getReversals())
        render text: map as JSON
    } 
}
