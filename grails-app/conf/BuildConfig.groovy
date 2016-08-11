grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir = 'target/test-reports'
grails.enable.native2ascii = false

grails.project.fork = [
    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = 'maven'
grails.project.dependency.resolution = {
    inherits('global') {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log 'warn'
    repositories {
        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()
    }
    dependencies {
        runtime 'org.postgresql:postgresql:9.3-1101-jdbc41'
        compile 'com.github.samtools:htsjdk:jar:2.6.0'
        compile 'org.biojava:biojava-core:4.2.4'
        compile group: 'org.jyaml', name: 'jyaml', version: '1.3'
        compile group: 'commons-io', name: 'commons-io', version: '2.5'
    }

    plugins {
        build ':tomcat:7.0.70'
        build(':release:3.1.0',
              ':rest-client-builder:2.1.0',
              ':scaffolding:2.1.2',
              ':codenarc:0.25.2') {
            export = false
        }
        compile ':shiro:1.2.1'
        compile ':apollo-domain-classes:1.0.10'
        runtime ':hibernate4:4.3.5.5'
        runtime ':cors:1.3.0'
        compile ':asset-pipeline:2.8.0'
    }
}

codenarc.ruleSetFiles = 'file:.codenarc'
codenarc.processViews = true

