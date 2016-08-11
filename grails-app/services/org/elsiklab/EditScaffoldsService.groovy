package apollo.lsaa

import grails.transaction.Transactional

@Transactional
class EditScaffoldsService {

    def serviceMethod() {

    }
    def getTranformedSequence(ArrayList<AlternativeLoci> list) {
        def map = []
        int prevstart = 1 
        for(int i = 0; i < list.size(); i++) {
            AlternativeLoci curr = list[i];
            AlternativeLoci next = list[i + 1]; 
            boolean last = (i == list.size() - 1)
            map << [
                sequence: [
                    source: curr.name,
                    start: prevstart,
                    stop: curr.featureLocation.fmin-1
                ]   
            ]   
            map << [
                sequence: [
                    source: curr.name,
                    start: curr.featureLocation.fmin,
                    stop: curr.featureLocation.fmax,
                    reverse: curr.reversed ?: false
                ]   
            ]   
            map << [
                sequence: [
                    source: curr.name,
                    start: curr.featureLocation.fmax + 1,
                    stop: last ? (curr.end_file - curr.start_file) - 1 : next.featureLocation.fmin
                ]   
            ]   
            prevstart = curr.featureLocation.fmin
        }
        return map
    }
}
