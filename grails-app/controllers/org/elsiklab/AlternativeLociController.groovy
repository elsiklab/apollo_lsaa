package org.elsiklab

import static org.springframework.http.HttpStatus.*

import groovy.json.JsonBuilder
import grails.converters.JSON
import grails.util.Environment
import grails.transaction.Transactional
import org.bbop.apollo.FeatureLocation
import org.bbop.apollo.Sequence
import org.bbop.apollo.BiologicalRegion
import org.bbop.apollo.OrganismProperty
import org.bbop.apollo.User
import org.apache.shiro.SecurityUtils


class AlternativeLociController {

    def permissionService
    def nameService
    def featureService

    def addLoci() {

        Sequence sequence = Sequence.findByName(params.sequence)
        if(!sequence) {
            response.status = 500
            render ([error: 'No sequence found'] as JSON)
            return
        }

        if(!params.sequencedata.length()) {
            response.status = 500
            render ([error: 'No sequencedata provided'] as JSON)
            return
        }

        new Sequence(
            name: name,
            organism: sequence.organism,
            start: 0,
            end: params.sequencedata.length(),
            length: params.sequencedata.length(),
            seqChunkSize: 20000
        ).save()

        def filename

        new File("${sequence.organism.directory}/${name}.fa").with {
            write('>' + name + '\n' + params.sequencedata + '\n')
            filename = absolutePath
            ("prepare-refseqs.pl --fasta ${absolutePath} --out ${sequence.organism.directory}").execute()
            ("generate-names.pl --completionLimit 0 --out ${sequence.organism.directory}").execute()

            new OrganismProperty(key: 'blatdb', organism: sequence.organism, value: name).save()
            new OrganismProperty(key: 'blatdbpath', organism: sequence.organism, value: absolutePath).save()

            // remake fasta index, blat db, blast db
            ("makeblastdb -dbtype nucl -in ${absolutePath} -title ${name}").execute()
        }
        String name = UUID.randomUUID()
        def fastaFile = new FastaFile(
            filename: filename,
            dateCreated: new Date(),
            dateModified: new Date(),
            username: "admin",
            originalname: "admin-" + new Date()
        ).save(flush: true)

        AlternativeLoci altloci = new AlternativeLoci(
            description: params.description,
            name: name,
            uniqueName: name,
            start_file: 0,
            end_file: params.sequencedata.length(),
            fastaFile: fastaFile
        ).save(flush: true)

        FeatureLocation featureLoc = new FeatureLocation(
            fmin: params.start,
            fmax: params.end,
            feature: altloci,
            sequence: sequence
        ).save(flush:true)
        altloci.addToFeatureLocations(featureLoc)

        def owner = User.findByUsername(SecurityUtils.subject.principal ?: 'admin')
        if (!owner && Environment.current != Environment.PRODUCTION) {
            owner = new User(username: 'admin', passwordHash: 'admin', firstName: 'admin', lastName: 'admin')
            owner.save(flush: true)
        }
        altloci.addToOwners(owner)

        render ([success: 'create loci success'] as JSON)
    }

    def addRegion() {

        Sequence sequence = Sequence.findByName(params.sequence)
        if(!sequence) {
            response.status = 500
            render ([error: 'No sequence found'] as JSON)
            return
        }

        String name = UUID.randomUUID()
        AlternativeRegion altloci = new AlternativeRegion(
            description: params.description,
            name: name,
            uniqueName: name
        ).save(flush: true)

        FeatureLocation featureLoc = new FeatureLocation(
            fmin: params.start,
            fmax: params.end,
            feature: altloci,
            sequence: sequence
        ).save(flush:true)
        altloci.addToFeatureLocations(featureLoc)

        def owner = User.findByUsername(SecurityUtils.subject.principal ?: 'admin')
        if (!owner && Environment.current != Environment.PRODUCTION) {
            owner = new User(username: 'admin', passwordHash: 'admin', firstName: 'admin', lastName: 'admin')
            owner.save(flush: true)
        }
        altloci.addToOwners(owner)

        render ([success: 'create loci success'] as JSON)
    }

    def getLoci() {
        Sequence sequence = Sequence.findByName(params.sequence)
        def features = BiologicalRegion.createCriteria().list {
            featureLocations {
                eq('sequence', sequence)
            }
            'in'('class', [AlternativeRegion.class.name, AlternativeLoci.class.name])
        }
        JsonBuilder json = new JsonBuilder ()
        json.features features, { it ->
            start it.featureLocation.fmin
            id it.uniqueName
            end it.featureLocation.fmax
            ref it.featureLocation.sequence.name
            description it.description
            color 'rgba(50,50,190,0.2)'
            seqName it.uniqueName
        }

        render json.toString()
    }

    def show(AlternativeLoci alternativeLociInstance) {
        respond alternativeLociInstance
    }

    def create() {
        log.debug params
        respond new AlternativeLoci(params)
    }

    @Transactional
    def save() {
        Sequence sequence = Sequence.findById(params.name)
        if(!sequence) {
            response.status = 500
            render ([error: 'No sequence found'] as JSON)
            return
        }
        def fastaFile = FastaFile.findById(params.fasta_file)

        String name = UUID.randomUUID()
        AlternativeLoci alternativeLociInstance = new AlternativeLoci(
            description: params.description,
            name: name,
            uniqueName: name,
            start_file: params.start_file ?: 0,
            end_file: params.end_file ?: new File(fastaFile.filename).length(),
            fasta_file: fastaFile
        ).save(flush: true, failOnError: true)

        FeatureLocation featureLoc = new FeatureLocation(
            fmin: params.start,
            fmax: params.end,
            feature: alternativeLociInstance,
            sequence: sequence
        ).save(flush:true, failOnError: true)
        alternativeLociInstance.addToFeatureLocations(featureLoc)

        redirect(action: 'index')
    }

    def edit(AlternativeLoci alternativeLociInstance) {
        render view: 'edit', model: [alternativeLociInstance: alternativeLociInstance]
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
                redirect action:'index', method:'GET'
            }
            '*' { respond alternativeLociInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(AlternativeLoci alternativeLociInstance) {
        if (alternativeLociInstance == null) {
            notFound()
            return
        }

        def success = new File(alternativeLociInstance.fasta_file.filename).delete()
        if(!success) {
            log.warn "Error deleting file"
        }


        alternativeLociInstance.delete flush:true

        redirect(action: 'index')
    }

    def showRegion(AlternativeRegion alternativeLociInstance) {
        respond alternativeLociInstance
    }

    def createRegion() {
        respond new AlternativeRegion(params)
    }

    @Transactional
    def saveRegion(AlternativeRegion alternativeLociInstance) {
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
                flash.message = message(code: 'default.created.message', args: [message(code: 'alternativeLoci.label', default: 'AlternativeRegion'), alternativeLociInstance.id])
                redirect alternativeLociInstance
            }
            '*' { respond alternativeLociInstance, [status: CREATED] }
        }
    }

    def editRegion(AlternativeRegion alternativeLociInstance) {
        render view: 'edit', model: [alternativeLociInstance: alternativeLociInstance]
    }

    @Transactional
    def updateRegion(AlternativeRegion alternativeLociInstance) {
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
                flash.message = message(code: 'default.updated.message', args: [message(code: 'AlternativeRegion.label', default: 'AlternativeRegion'), alternativeLociInstance.id])
                redirect action:'index', method:'GET'
            }
            '*' { respond alternativeLociInstance, [status: OK] }
        }
    }

    @Transactional
    def deleteRegion(AlternativeRegion alternativeLociInstance) {

        if (alternativeLociInstance == null) {
            notFound()
            return
        }

        alternativeLociInstance.delete flush:true

        redirect(action: 'index')
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'availableStatus.label', default: 'AlternativeLoci'), params.id])
                redirect action: 'index', method: 'GET'
            }
            '*' { render status: NOT_FOUND }
        }
    }

    def index(Integer max) {
        params.max = Math.min(max ?: 15, 100)

        def c = BiologicalRegion.createCriteria()

        def list = c.list(max: params.max, offset:params.offset) {

            if(params.sort == 'owners') {
                owners {
                    order('username', params.order)
                }
            }
            if(params.sort == 'sequencename') {
                featureLocations {
                    sequence {
                        order('name', params.order)
                    }
                }
            }
            else if(params.sort == 'name') {
                order('name', params.order)
            }
            else if(params.sort == 'cvTerm') {
                order('class', params.order)
            }
            else if(params.sort == 'organism') {
                featureLocations {
                    sequence {
                        organism {
                            order('commonName', params.order)
                        }
                    }
                }
            }
            else if(params.sort == 'lastUpdated') {
                order('lastUpdated', params.order)
            }

            if(params.ownerName != null && params.ownerName != '') {
                owners {
                    ilike('username', '%' + params.ownerName + '%')
                }
            }
            if(params.organismName != null && params.organismName != '') {
                featureLocations {
                    sequence {
                        organism {
                            ilike('commonName', '%' + params.organismName + '%')
                        }
                    }
                }
            }
            'in'('class', [AlternativeRegion.class.name, AlternativeLoci.class.name])
        }

        render view: 'index', model: [features: list, sort: params.sort, alternativeLociInstanceCount: list.totalCount]
    }
}
