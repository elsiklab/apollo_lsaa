package org.elsiklab

import grails.test.spock.IntegrationSpec
import org.bbop.apollo.Sequence
import org.bbop.apollo.Organism


class AlternativeLociControllerIntegrationSpec extends IntegrationSpec {

    def setup() {
        Organism organism = new Organism(
                directory: "test/integration/resources/"
                , commonName: "honeybee"
        ).save(flush: true)
        new Sequence(
                length: 1405242
                , seqChunkSize: 20000
                , start: 0
                , organism: organism
                , end: 1405242
                , name: "Group1.10"
        ).save()
    }

    void "test something"() {
        expect:
              1==1
    }
}
