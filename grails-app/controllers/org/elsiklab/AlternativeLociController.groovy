package org.elsiklab


import grails.converters.JSON
import groovy.json.JsonBuilder
import grails.transaction.Transactional
import org.bbop.apollo.*


class AlternativeLociController {

    def permissionService
    def nameService
    def featureService


    def addLoci() {

        Sequence sequence = Sequence.findByName(params.sequence)
        if(!sequence) {
            response.status = 500
            render ([error: "No sequence found"] as JSON)
            return
        }

        String name = nameService.generateUniqueName()
        AlternativeLoci altloci = new AlternativeLoci(
            description: params.description,
            name: name,
            uniqueName: name,
            residues: params.sequencedata
        ).save(flush:true)
        FeatureLocation featureLoc = new FeatureLocation(
                fmin: params.start
                ,fmax: params.end
                ,feature: altloci
                ,sequence: sequence
        ).save(flush:true)

        new File(sequence.organism.fasta).withWriterAppend("UTF-8") {
            it.write(">"+name+"\n"+params.sequencedata+"\n")
        }

        // remake fasta index, blat db, blast db
        ('samtools faidx '+sequence.organism.fasta).execute()
        ('faToTwoBit '+sequence.organism.fasta+' '+sequence.organism.blatdb).execute()
        ('makeblastdb -dbtype nucl -in '+sequence.organism.fasta).execute()

        altloci.addToFeatureLocations(featureLoc)
        render ([success: "create loci success"] as JSON)
    }


    def getLoci() {
        Sequence sequence = Sequence.findByName(params.sequence)
        def features = AlternativeLoci.createCriteria().list() {
            featureLocations {
                eq('sequence',sequence)
            }
        }
        JsonBuilder json = new JsonBuilder ()
        json.features features, { result ->
            start result.featureLocation.fmin
            id result.uniqueName
            end result.featureLocation.fmax
            ref result.featureLocation.sequence.name
            description result.description
            if(result.residues) {
                href true
            }
            color "rgba(50,50,190,0.2)"
            seqName result.uniqueName
        }
        
        render json.toString()
    }




    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond AlternativeLoci.list(params), model:[alternativeLociInstanceCount: AlternativeLoci.count()]
    }

    def show(AlternativeLoci alternativeLociInstance) {
        respond alternativeLociInstance
    }

    def create() {
        respond new AlternativeLoci(params)
    }

    @Transactional
    def save(AlternativeLoci alternativeLociInstance) {
        if (alternativeLociInstance == null) {
            notFound()
            return
        }

        if (alternativeLociInstance.hasErrors()) {
            respond alternativeLociInstance.errors, view:'create'
            return
        }

        alternativeLociInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'alternativeLoci.label', default: 'AlternativeLoci'), alternativeLociInstance.id])
                redirect alternativeLociInstance
            }
            '*' { respond alternativeLociInstance, [status: CREATED] }
        }
    }

    def edit(AlternativeLoci alternativeLociInstance) {
        respond alternativeLociInstance
    }

    @Transactional
    def update(AlternativeLoci alternativeLociInstance) {
        if (alternativeLociInstance == null) {
            notFound()
            return
        }

        if (alternativeLociInstance.hasErrors()) {
            respond alternativeLociInstance.errors, view:'edit'
            return
        }

        alternativeLociInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'AlternativeLoci.label', default: 'AlternativeLoci'), alternativeLociInstance.id])
                redirect alternativeLociInstance
            }
            '*'{ respond alternativeLociInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(AlternativeLoci alternativeLociInstance) {

        if (alternativeLociInstance == null) {
            notFound()
            return
        }

        alternativeLociInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'AlternativeLoci.label', default: 'AlternativeLoci'), alternativeLociInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'availableStatus.label', default: 'AlternativeLoci'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
