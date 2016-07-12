package org.elsiklab

import grails.test.spock.IntegrationSpec
import grails.test.mixin.TestFor
import org.bbop.apollo.Sequence
/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(EditScaffoldsController)
class EditScaffoldsControllerSpec extends IntegrationSpec {

    def setup() {
        new Sequence(
            length: 10000,
            seqChunkSize: 0,
            start: 0,
            end: 10000,
            name:"GK000015.2" 
        ).save()
    }

    def cleanup() {
    }

    void "test something"() {
        when:
            controller.createReversal("GK000015.2",1500,3000,"description");
        then:
            def res = controller.getReversals(); 
            controller.convertToYaml(); 
            res.size() == 1
    }
}
