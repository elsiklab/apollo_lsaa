package org.elsiklab

import grails.test.spock.IntegrationSpec
import org.bbop.apollo.Organism
import org.bbop.apollo.Sequence

class EditScaffoldsIntegrationSpec extends IntegrationSpec {
    def editScaffoldsService
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

    void "test something"() {
    }

    void "test getting reversals"() {
        when:
            def map = editScaffoldsService.getTransformedSequence(editScaffoldsService.getReversals(), organism)
        then:
            map[0] == [ sequence: [ source: "scf1117875582023", start: 100, stop: 200, reverse: true, filename: "test/integration/resources/pyu_data/scf1117875582023.fa", external: true ] ]
    }
}
