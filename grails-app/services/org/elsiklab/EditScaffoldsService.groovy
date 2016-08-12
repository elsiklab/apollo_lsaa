package org.elsiklab

import grails.transaction.Transactional
import org.ho.yaml.Yaml
import org.ho.yaml.exception.YamlException

@Transactional
class EditScaffoldsService {

    def fastaFileService

    def serviceMethod() {

    }

    def getTransformations(def list, def organism) {
        def map = []
        def prevstart = 1 
        def fastaFile = FastaFile.findByOrganism(organism)
        log.debug fastaFile

        for(int i = 0; i < list.size(); i++) {
            AlternativeLoci curr = list[i];
            AlternativeLoci next = list[i + 1]; 
            map << [
                sequence: [
                    source: curr.name,
                    start: prevstart,
                    stop: curr.featureLocation.fmin - 1,
                    filename: fastaFile.filename,
                    reverse: false
                ]   
            ]   
            map << [
                sequence: [
                    source: curr.name,
                    start: curr.featureLocation.fmin,
                    stop: curr.featureLocation.fmax,
                    reverse: curr.reversed ?: false,
                    filename: curr.fasta_file.filename
                ]   
            ]   
            map << [
                sequence: [
                    source: curr.name,
                    start: curr.featureLocation.fmax + 1,
                    stop: curr == list.last() ? (curr.end_file - curr.start_file) - 1 : next.featureLocation.fmin,
                    filename: fastaFile.filename,
                    reverse: false
                ]   
            ]   
            prevstart = curr.featureLocation.fmin
        }
        return map
    }

    def getReversals() {
        return AlternativeLoci.createCriteria().list {
            featureLocations {
                order('fmin', 'ascending')
            }
        }
    }
    def getTransformedSequence(def list, def organism) {
        String string = ''
        log.debug organism
        def ret = this.getTransformations(this.getReversals(), organism)
        log.debug ret
        ret.each { it ->
            string += fastaFileService.readSequence(it.sequence.filename, it.sequence.source, it.sequence.start, it.sequence.stop, it.sequence.reverse)
        }
        return string
    }

}
