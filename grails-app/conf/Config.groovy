// configuration for plugin testing - will not be included in the plugin zip
grails.config.locations = [
        "file:src/groovy/${appName}-config.groovy"        // dev only
        , "classpath:${appName}-config.groovy"    // for production deployment
        , "classpath:${appName}-config.properties"
]


log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
    debug 'org.elsiklab'
    debug 'grails.app'
}

lsaa {
    appStoreDirectory = '/tmp/'
    sequence_search_tools = [
        blat_nuc: [
            search_exe: '/usr/local/bin/blat',
            search_class: 'org.elsiklab.sequence.search.blat.BlatCommandLineNucleotideToNucleotide',
            name: 'Blat nucleotide',
            params: '',
            tmp_dir: '/opt/apollo/tmp',
            removeTmpDir: false
        ],
        blat_prot: [
            search_exe: '/usr/local/bin/blat',
            search_class: 'org.elsiklab.sequence.search.blat.BlatCommandLineProteinToNucleotide',
            name: 'Blat protein',
            params: '',
            tmp_dir: '/opt/apollo/tmp',
            removeTmpDir: false
        ],
        blast_prot: [
            search_exe: '/usr/local/ncbi/blast/bin/tblastn',
            formatter_exe: '/usr/local/ncbi/blast/bin/blast_formatter',
            gff_exe: 'bp_search2gff.pl',
            search_class: 'org.elsiklab.sequence.search.blast.BlastCommandLine',
            name: 'Blast protein',
            params: ''
        ]
    ]
}

redis {
    timeout = 2000
    port = 6379
    host = "localhost"
}


grails.resources.work.dir = "grails-app/jbrowse"
