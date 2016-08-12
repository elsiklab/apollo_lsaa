import org.bbop.apollo.Sequence
import org.bbop.apollo.Organism
import org.elsiklab.FastaFile

class BootStrap {
    def grailsApplication
    def init = { servletContext ->
        log.info 'Initializing...'
        def dataSource = grailsApplication.config.dataSource
        log.info 'Datasource'
        log.info "Url: ${dataSource.url}"
        log.info "Driver: ${dataSource.driverClassName}"
        log.info "Dialect: ${dataSource.dialect}"
        if(grailsApplication.config.lsaa.bootstrap) {
            log.info 'Bootstrapping database'
            def organism = new Organism(
                commonName: 'pyu',
                directory: 'test/integration/data/pyu_data'
            ).save(flush: true, failOnError: true)
            new Sequence(
                name: 'scf1117875582023',
                organism: organism,
                length: 1683196,
                seqChunkSize: 20000,
                start: 0,
                end: 1683196
            ).save(flush: true, failOnError: true)
            new FastaFile(
                filename: 'test/integration/resources/pyu_data/scf1117875582023.fa',
                lastUpdated: new Date(),
                dateCreated: new Date(),
                originalname: 'scf1117875582023.fa',
                username: 'admin',
                organism: organism
            ).save(flush: true, failOnError: true)
        }
    }
    def destroy = {
    }
}
