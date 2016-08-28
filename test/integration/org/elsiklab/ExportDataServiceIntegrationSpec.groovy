package org.elsiklab

import grails.test.spock.IntegrationSpec
import grails.test.mixin.TestFor
import org.bbop.apollo.Sequence
import org.bbop.apollo.Organism

@TestFor(ExportDataService)
class ExportDataServiceIntegrationSpec extends IntegrationSpec {

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
        assert 1==1
    }
    void "test getting reversals"() {
        when:
            def map = service.getTransformedSequence(organism)
        then:
            //map[0] == [ sequence: [ source: "scf1117875582023", start: 100, stop: 200, reverse: true, filename: "test/integration/resources/pyu_data/scf1117875582023.fa", external: true ] ]
            1==1
    }
}
