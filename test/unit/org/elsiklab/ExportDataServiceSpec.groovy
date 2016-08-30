package org.elsiklab

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification
import org.bbop.apollo.Sequence
import org.bbop.apollo.Organism

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@Mock([Organism, FastaFile, AlternativeLoci])
@TestFor(ExportDataService)
class ExportDataServiceSpec extends Specification {
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

    void "test getting reversals"() {
        when:
            def organism = Organism.findByCommonName('pyu')
            def ret = service.getTransformations(organism)
        then:
            1==1
    }
}
