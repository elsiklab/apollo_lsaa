package org.elsiklab

import static org.springframework.http.HttpStatus.*

import grails.converters.JSON
import org.ho.yaml.Yaml
import org.ho.yaml.exception.YamlException
import org.bbop.apollo.FeatureLocation
import org.bbop.apollo.Sequence

class EditScaffoldsController {

    def grailsApplication

    def index() {
        def yamlfile = new File("${grailsApplication.config.lsaa.appStoreDirectory}/out.yaml")
        try {
            def ret = Yaml.load(yamlfile)
            render view: 'index', model: [yaml: yamlfile.text]
        }
        catch (YamlException e) {
            e.printStackTrace()
            render view: 'index', model: [yaml: yamlfile.text, flash: [message: 'Error parsing YAML']]
        }
        catch (FileNotFoundException e) {
            e.printStackTrace()
            render view: 'index', model: [yaml: '']
        }
    }

    def editScaffold() {
        new File("${grailsApplication.config.lsaa.appStoreDirectory}/out.yaml").withWriter { out ->
            out.write params.scaffoldEditor
        }
        redirect(action: 'index')
    }

    def generateScaffolds() {
        new File("${grailsApplication.config.lsaa.appStoreDirectory}/temp.fa").withWriter { temp ->
            FastaFile.all.each { it ->
                new File(it.filename).withReader { input ->
                    temp << input
                }
            }
            def ap = grailsApplication.config.lsaa.appStoreDirectory

            new File("${grailsApplication.config.lsaa.appStoreDirectory}/out.fa").withWriter { out ->
                log.debug "${grailsApplication.config.lsaa.scaffolder.path} sequence ${grailsApplication.config.lsaa.appStoreDirectory}/out.yaml ${grailsApplication.config.lsaa.appStoreDirectory}/temp.fa"
                ("${grailsApplication.config.lsaa.scaffolder.path} sequence ${grailsApplication.config.lsaa.appStoreDirectory}/out.yaml ${grailsApplication.config.lsaa.appStoreDirectory}/temp.fa").execute().waitForProcessOutput(out, System.err)
            }
        }
        redirect(action: 'downloadFasta')
    }

    def downloadFasta() {
        new File("${grailsApplication.config.lsaa.appStoreDirectory}/out.fa").withReader { stream ->
            response.setHeader 'Content-disposition', 'attachment;filename=output.fa'
            response.contentType = 'application/octet-stream'
            response.outputStream << stream
            response.outputStream.flush()
        }
    }

    def loadFromAltLoci() {
        new File("${grailsApplication.config.lsaa.appStoreDirectory}/out.yaml").withWriter { temp ->
            temp << convertToYaml()
        }
        redirect(action: 'index')
    }

    def createReversal(String sequence, Integer start, Integer end, String description) {
        String name = UUID.randomUUID()
        Sequence s = Sequence.findByName(sequence)

        AlternativeLoci altloci = new AlternativeLoci(
            name: sequence,
            uniqueName: name,
            description: description,
            reversed: true
        ).save(flush: true, failOnError: true)

        FeatureLocation featureLoc = new FeatureLocation(
            fmin: start,
            fmax: end,
            feature: altloci,
            sequence: s
        ).save(flush:true)
        altloci.addToFeatureLocations(featureLoc)

        render ([success: true] as JSON)
    }

    def createCorrection() {
        String name = UUID.randomUUID()
        Sequence seq = Sequence.findByName(params.sequence)

        AlternativeLoci altloci = new AlternativeLoci(
            name: name,
            uniqueName: name,
            description: params.description
        ).save(flush: true, failOnError: true)

        FeatureLocation featureLoc = new FeatureLocation(
            fmin: params.start,
            fmax: start.end,
            feature: altloci,
            sequence: seq
        ).save(flush:true)
        altloci.addToFeatureLocations(featureLoc)

        def file = File.createTempFile('fasta', null, new File(grailsApplication.config.lsaa.appStoreDirectory))

        file.withWriter { temp ->
            temp << ">${name}"
            temp << params.sequencedata
            editscaf = new FastaFile(
                filename: file.absolutePath,
                username: 'admin',
                dateCreated: new Date(),
                lastModified: new Date(),
                originalname: 'admin-' + new Date()
            ).save(flush:true)
        }


        render ([success: true] as JSON)
    }

    def getReversals() {
         return AlternativeLoci.createCriteria().list {
             eq('reversed', true)
         }
    }

    def convertToMap() {
        def res = AlternativeLoci.createCriteria().list {
            featureLocations {
                order('fmin', 'ascending')
            }
        }
        def map = []
        def prevstart = 1

        def s = Sequence.findByName(res[0].featureLocation.sequence.name)
        def current

        res.eachWithIndex { it, i ->

            def fmin = it.featureLocation.fmin
            def fmax = it.featureLocation.fmax

            if (i > 0) map << current

            if (it.reversed) {
                map << [
                    sequence: [
                        source: it.name,
                        start: prevstart,
                        stop: fmin - 1
                    ]
                ]
                map << [
                    sequence: [
                        source: it.name,
                        start: fmin,
                        stop: fmax,
                        reverse: it.reversed
                    ]
                ]
                map << [
                    sequence: [
                        source: it.name,
                        start: fmax + 1,
                        stop: i == res.size() - 1 ? s.length - 1 : res[i + 1].featureLocation.fmin
                    ]
                ]
            }

            prevstart = fmin
        }
        return map
    }

    def convertToYaml() {
        return Yaml.dump(this.convertToMap())
    }

    def downloadYaml() {
        render this.convertToMap()
    }
}
