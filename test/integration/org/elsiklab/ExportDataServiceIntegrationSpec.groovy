package org.elsiklab

import grails.test.spock.IntegrationSpec
import org.bbop.apollo.Organism

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
class ExportDataServiceIntegrationSpec extends IntegrationSpec {
    def exportDataService

    def setup() {
        def organism = new Organism(
            commonName: 'pyu',
            directory: 'test/integration/data/pyu_data/'
        ).save(flush: true, failOnError: true)
    }

    def cleanup() {
    }

    void "test empty altloci"() {
        when:
            def organism = Organism.findByCommonName('pyu')
            def ret = exportDataService.getAltLoci(organism)
        then:
            ret.size == 0
    }

    void "test adding altloci"() {
        when:
            def organism = Organism.findByCommonName('pyu')
            def ret = exportDataService.getAltLoci(organism)
        then:
            ret.size == 0
    }
}
