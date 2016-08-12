# apollo-lsaa

[![](https://travis-ci.org/elsiklab/apollo_lsaa.svg?branch=master)](https://travis-ci.org/elsiklab/apollo_lsaa)

A plugin for [Apollo](http://github.com/GMOD/Apollo) to annotate alternative loci (locus specific alternate assemblies)


# Installation

## Install client plugin to root apollo
    
    scripts/copy_client.sh

This copies the jbrowse plugins into ../../web-app/jbrowse, which would be the location of Apollo in production mode

## Bootstrap you data

By default the bootstrapping should be setup for your organism in grails-app/conf/BootStrap.groovy

# Deploy

## Run in development


In dev, you can install jbrowse into web-app/jbrowse with this script

    scripts/install_dev.sh

The app can also be run stand-alone without Apollo2

    grails run-app -reloading -Dserver.port=8085 --stacktrace

## Run in production

Run this to install to the system's local maven cache

    grails refresh-dependencies && grails maven-install

After that, add apollo-lsaa as a dependency Apollo's BuildConfig.groovy

    grails.project.dependency.resolution = {
        ...
        plugins {
            compile ":apollo-domain-classes:1.0.10"
            compile ":apollo-lsaa:1.0.3"
        }
    }

Apollo will then use this plugin declaration and the API's for apollo-lsaa will be setup automatically.

# Tests

## Run tests/lint

    grails test-app
    grails codenarc


# Notes

Note: we are currently using the "apollo-domain-classes" modification from https://github.com/GMOD/Apollo/pull/962


