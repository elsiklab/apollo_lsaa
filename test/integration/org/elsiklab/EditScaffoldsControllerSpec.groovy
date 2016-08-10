package org.elsiklab

import grails.test.spock.IntegrationSpec
import grails.test.mixin.TestFor
import org.bbop.apollo.Sequence
import org.bbop.apollo.Organism


@TestFor(EditScaffoldsController)
class EditScaffoldsControllerSpec extends IntegrationSpec {

    def setup() {
        def organism = new Organism(
            commonName: "UMD3.1",
            directory: "test/integration/data/umd31"
        ).save()
        new Sequence(
            length: 10000,
            seqChunkSize: 0,
            start: 0,
            end: 10000,
            name:"GK000015.2",
            organism: organism
        ).save()
    }

    def cleanup() {
    }
    void "test reversal"() {
        when:
            params.sequence = "GK000015.2"
            params.start = 1000
            params.end = 2000
            params.description = "inversion"
            params.organism = "UMD3.1"
            controller.createReversal()
            def res = controller.getReversals()
            def map = controller.convertToMap()
        then:
            res.size() == 1
            map.size == 3
            map[1].sequence.source == "GK000015.2"
    }
}
