package org.elsiklab

import grails.converters.JSON


class EditScaffoldsController {

    def index() {
        def text = new File("out.yaml").text
        render view: "index", model: [yaml: text]
    }

    def editScaffold() {
        new File("out.yaml").withWriter { out ->
            out.write params.scaffoldEditor
        }
        redirect(action: "index")
    }
}
