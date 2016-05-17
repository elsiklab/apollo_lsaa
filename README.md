# apollo-lsaa

A plugin for [Apollo](http://github.com/GMOD/Apollo) to annotate alternative loci (locus specific alternate assemblies)

## Installation


Run the maven-install target to build and install to the system's local maven cache

    grails refresh-dependencies && grails maven-install

After it is installed to the local maven cache, add this to Apollo's BuildConfig.groovy

    grails.project.dependency.resolution = {
        ...
        plugins {
            compile ":apollo-domain-classes:1.0.6"
            compile ":apollo-lsaa:1.0.1"
        }
    }

Apollo will then use this plugin declaration and the API's for apollo-lsaa will be setup automatically.

Note: we are currently using the "apollo-domain-classes" modification from https://github.com/GMOD/Apollo/pull/962


## Install client plugin

Copy the code from client/LSAA to jbrowse-download/plugins/LSAA and include it as a plugin declaration



## Run tests

    grails test-app

## Run codenarc

    grails codenarc
