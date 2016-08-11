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
            username: 'admin',
            originalname: 'admin-' + new Date()
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
        ).save(flush: true)
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
            'eq'('class', AlternativeLoci.class.name)
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
        respond new AlternativeLoci(params)
    }

    @Transactional
    def save(AlternativeLoci instance) {
        def sequence = Sequence.findById(params.name)
        if(sequence) {
            def fastaFile = FastaFile.findById(params.fasta_file)
            if(fastaFile) {
                def file = new File(fastaFile.filename)
                if(file) {
                    String name = UUID.randomUUID()
                    log.debug "${params.start} ${params.end} ${file.length()}"
                    AlternativeLoci alternativeLociInstance = new AlternativeLoci(
                        description: params.description,
                        name: name,
                        uniqueName: name,
                        start_file: params.start_file == "" ? 0 : params.start_file,
                        end_file: params.end_file == "" ? file.length() : params.end_file,
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
                else {
                    render text: ([error: 'FASTA file path was moved'] as JSON), status: 500
                }
            }
            else {
                render text: ([error: 'No FASTA file found'] as JSON), status: 500
            }
        }
        else {
            render text: ([error: 'No sequence found'] as JSON), status: 500
        }
    }

    def edit(AlternativeLoci alternativeLociInstance) {
        render view: 'edit', model: [alternativeLociInstance: alternativeLociInstance]
    }

    @Transactional
    def update(AlternativeLoci instance) {
        log.debug instance
        def sequence = Sequence.findById(params.name)
        if(sequence) {
            def fastaFile = FastaFile.findById(params.fasta_file)
            if(fastaFile) {
                def file = new File(fastaFile.filename)
                if(file) {
                    AlternativeLoci altloci = AlternativeLoci.findById(params.name)
                    log.debug altloci
                    altloci.description = params.description
                    altloci.start_file = params.start_file ?: 0
                    altloci.end_file = params.end_file ?: file.length()
                    altloci.fasta_file = fastaFile
                    altloci.featureLocation = params.start
                    altloci.featureLocation = params.end
                    altloci.featureLocation.sequence = sequence
                    altloci.save(flush: true, failOnError: true)
                    redirect(action: 'edit')
                }
                else {
                    render text: ([error: 'FASTA file path was moved'] as JSON), status: 500
                }
            }
            else {
                render text: ([error: 'No FASTA file found'] as JSON), status: 500
            }
        }
        else {
            render text: ([error: 'No sequence found'] as JSON), status: 500
        }
    }

    @Transactional
    def delete(AlternativeLoci alternativeLociInstance) {
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
            'eq'('class', AlternativeLoci.class.name)
        }

        render view: 'index', model: [features: list, sort: params.sort, alternativeLociInstanceCount: list.totalCount]
    }
}
