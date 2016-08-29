package org.elsiklab

import grails.transaction.Transactional
import org.ho.yaml.Yaml
import org.ho.yaml.exception.YamlException

@Transactional
class ExportDataService {

    def fastaFileService

    def serviceMethod() {

    }

    def getTransformations(def organism) {
        def map = []
        def prevstart = 1
        def fastaFile = FastaFile.findByOrganism(organism)
        def list = this.getAltLoci(organism)
        AlternativeLoci curr = list[0]
        AlternativeLoci next = list[1]
        map << [
            sequence: [
                source: curr.featureLocation.sequence.name,
                start: prevstart,
                stop: curr.featureLocation.fmin - 1,
                filename: fastaFile.filename,
                reverse: false
            ]
        ]
        for(int i = 0; i < list.size(); i++) {
            curr = list[i]
            next = list[i + 1]
            if(curr.reversed) {
                
                map << [
                    sequence: [
                        source: curr.name_file,
                        start: curr.featureLocation.fmin,
                        stop: curr.featureLocation.fmax,
                        reverse: curr.reversed ?: false,
                        filename: curr.fasta_file.filename
                    ]
                ]

                prevstart = (i == list.size() - 1) ? (curr.featureLocation.sequence.length) - 1 : next.featureLocation.fmin
                map << [
                    sequence: [
                        source: curr.featureLocation.sequence.name,
                        start: curr.featureLocation.fmax + 1,
                        stop: prevstart,
                        filename: fastaFile.filename,
                        reverse: false
                    ]
                ]
            }
        }
        return map
    }

    def getAltLoci(def organism) {
        return AlternativeLoci.createCriteria().list {
            featureLocations {
                order('fmin', 'ascending')
                sequence {
                    eq('organism', organism)
                }
            }
        }
    }

    def getTransformedSequence(def organism) {
        String string = ''
        def ret = this.getTransformations(organism)
        ret.each { it ->
            string += fastaFileService.readSequence(it.sequence.filename, it.sequence.source, it.sequence.start, it.sequence.stop, it.sequence.reverse)
        }
        return string
    }
}
