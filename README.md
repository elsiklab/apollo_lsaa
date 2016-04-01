# apollo-lsaa

A plugin for [Apollo](http://github.com/GMOD/Apollo) to annotate alternative loci (locus specific alternate assemblies)

This uses the Grails plugin framework

## Installation

Run the build inside of this project

    grails refresh-dependencies && grails maven-install


This will add the project to the local grails plugin repository


Then add the plugin declaration of this project to *Apollo's* grails-app/conf/BuildConfig.groovy


    grails.project.dependency.resolution = {
        ...
        plugins {
            compile ":apollo-lsaa:1.0.0-SNAPSHOT"
        }
    }

Apollo will then use this plugin declaration and the API's for apollo-lsaa will be setup automatically


You will also want to install the client code client/LSAA as a JBrowse plugin



## Run tests

    grails test-app
