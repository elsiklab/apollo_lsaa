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
    void 'test reversal'() {
        when:
            params.sequence = 'scf1117875582023'
            params.start = 1000
            params.end = 2000
            params.description = 'inversion'
            params.organism = 'pyu'
            controller.createReversal(params)
            def res = controller.getReversals()
            def map = controller.convertToMap()
        then:
            res.size() == 1
            map.size == 3
            map[1].sequence.source == 'scf1117875582023'
    }
    void 'test badseq'() {
        when:
            params.sequence = 'bad sequence'
            params.start = 1000
            params.end = 2000
            params.description = 'inversion'
            params.organism = 'pyu'
            def ret = controller.createReversal(params)
        then:
            controller.response.status == 500
    }
}
