package org.elsiklab

import grails.test.spock.IntegrationSpec
import grails.test.mixin.TestFor
import org.bbop.apollo.Sequence
import org.bbop.apollo.Organism

@TestFor(EditScaffoldsController)
class EditScaffoldsControllerSpec extends IntegrationSpec {

    def setup() {
        def organism = new Organism(
            commonName: 'pyu',
            directory: 'test/integration/data/pyu_data/'
        ).save(flush: true, failOnError: true)
        new Sequence(
            name: 'scf1117875582023',
            organism: organism,
            length: 1683196,
            seqChunkSize: 20000,
            start: 0,
            end: 1683196
        ).save(flush: true, failOnError: true)
        new Sequence(
            name: 'scf1117875582023',
            organism: organism,
            length: 1683196,
            seqChunkSize: 20000,
            start: 0,
            end: 1683196
        ).save(flush: true, failOnError: true)
        new FastaFile(
            filename: 'test/integration/resources/pyu_data/scf1117875582023.fa',
            lastUpdated: new Date(),
            dateCreated: new Date(),
            originalname: 'scf1117875582023.fa',
            username: 'admin',
            organism: organism
        ).save(flush: true, failOnError: true)
    
    }

    def cleanup() {
    }
    void 'test badseq'() {
        when:
            controller.params.sequence = 'bad sequence'
            controller.params.start = 1000
            controller.params.end = 2000
            controller.params.description = 'inversion'
            controller.params.organism = 'pyu'
            controller.createReversal()
        then:
            controller.response.status == 500
            AlternativeLoci.count == 0
    }
    void 'test reversal'() {
        when:
            controller.params.sequence = 'scf1117875582023'
            controller.params.start = 1000
            controller.params.end = 2000
            controller.params.description = 'inversion'
            controller.params.organism = 'pyu'
            controller.createReversal()
        then:
            controller.response.status == 200
            AlternativeLoci.count == 1
    }
    
}
