package org.elsiklab

import grails.test.spock.IntegrationSpec
import org.bbop.apollo.Sequence
import org.bbop.apollo.Organism


class AlternativeLociControllerIntegrationSpec extends IntegrationSpec {

    def setup() {
        Organism organism = new Organism(
                commonName: "UMD3.1",
                directory: "test/integration/resources/umd31"
        ).save(flush: true)
        new Sequence(
                start: 0,
                end: 1405242,
                length: 1405242,
                seqChunkSize: 20000,
                name: "Group1.10",
                organism: organism
        ).save()
    }

    void "test something"() {
        expect:
              1 == 1
    }
}
