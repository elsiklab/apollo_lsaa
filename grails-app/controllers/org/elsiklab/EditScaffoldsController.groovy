package org.elsiklab

import grails.converters.JSON
import org.ho.yaml.Yaml;
import org.ho.yaml.exception.YamlException;


class EditScaffoldsController {

    def index() {
        def text = new File("out.yaml").text
        try {
            def ret = Yaml.load(new File("out.yaml"))
            log.debug ret
            render view: "index", model: [yaml: text]
        }
        catch(YamlException e) {
            log.debug "Error"
            render view: "index", model: [yaml: text, error: "Error parsing YAML"]
        }
    }

    def editScaffold() {
        new File("out.yaml").withWriter { out ->
            out.write params.scaffoldEditor
        }
        redirect(action: "index")
    }
}
