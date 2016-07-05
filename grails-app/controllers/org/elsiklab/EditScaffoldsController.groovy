package org.elsiklab

import grails.converters.JSON
import org.ho.yaml.Yaml;
import org.ho.yaml.exception.YamlException;


class EditScaffoldsController {

    def index() {
        def text = new File("out.yaml").text
        try {
            def reference = new File("output.fasta").text
            def ret = Yaml.load(new File("out.yaml"))
            log.debug ret
            render view: "index", model: [yaml: text, reference: reference]
        }
        catch(YamlException e) {
            log.debug "Error"
            render view: "index", model: [yaml: text, error: "Error parsing YAML"]
        }
        catch(FileNotFoundException e) {
            render view: "index", model: [yaml: text, error: params.error]
        }
    }

    def editScaffold() {
        new File("out.yaml").withWriter { out ->
            out.write params.scaffoldEditor
        }
        redirect(action: "index")
    }

    def generateScaffolds() {
        new File("out.fasta").withWriter { out ->
            ("scaffolder sequence out.yaml scf1117875582023.fa").execute().waitForProcessOutput(out, System.err)
            log.debug "done"
        }
        redirect(action: "downloadFasta")
    }

    def downloadFasta() {
        log.debug "downloading"
        new File("out.fasta").withReader { stream ->
            response.setHeader "Content-disposition", "attachment;filename=output.fa"
            response.contentType = 'application/octet-stream'
            response.outputStream << stream
            response.outputStream.flush()
        }
    }
}
