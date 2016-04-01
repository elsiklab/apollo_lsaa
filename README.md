# apollo-lsaa

A plugin for [Apollo](http://github.com/GMOD/Apollo) to annotate alternative loci (locus specific alternate assemblies)

This uses the Grails plugin framework


## Build

The build involves running maven-install to create a maven zip package

    grails refresh-dependencies
    grails maven-install

This outputs a bunch of files including something like grails-apollo-lsaa-1.0.0-SNAPSHOT.zip

## Install to Apollo

Add the filesystem path and plugin declaration of this project to grails-app/conf/BuildConfig.groovy


    grails.project.dependency.resolution = {
        ...
        plugins {
            compile ":apollo-lsaa:1.0.0-SNAPSHOT"
        }
    }



If that doesn't work you can also try explicitely setting the filesystem path for finding the plugin (wherever you built it)

    grails.plugin.location.'apollo-lsaa' = "/Users/biocmd/Work/Apollo/plugins/apollo-lsaa"

Or
   
   grails install-plugin grails-apollo-lsaa-1.0.0-SNAPSHOT.zip


## Run tests

    grails test-app
