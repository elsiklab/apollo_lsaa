package org.elsiklab

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(FastaFileService)
class FastaFileServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
        when:
            def a = service.readIndexedFasta('test/resources/1.plain/sequence.fa','A')
            def b = service.readIndexedFasta('test/resources/1.plain/sequence.fa','B')
            def c = service.readIndexedFasta('test/resources/1.plain/sequence.fa','C')
        then:
            a == 'GGGGGGGG'
            b == 'TTTTTTTT'
            c == 'AAAAAAAA'
    }
}
