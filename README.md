# apollo-lsaa

[![](https://travis-ci.org/elsiklab/apollo_lsaa.svg?branch=master)](https://travis-ci.org/elsiklab/apollo_lsaa)

A plugin for [Apollo](http://github.com/GMOD/Apollo) to annotate alternative loci (locus specific alternate assemblies)


# Installation

## Install client plugin

    scripts/copy_client.sh

This copies the jbrowse plugins into ../../web-app/jbrowse/plugins

## Install ruby tools

    gem install scaffolder scaffolder-tools

This needs ruby 2.0.0

# Tests

## Run tests

    grails test-app

## Run codenarc

    grails codenarc


# Deploy

## Run in development

To develop the plugin, it is valuable to run the plugin in isolation "like a microservice" separately from the main application

    grails run-app -reloading -Dserver.port=8085 --stacktrace

You can then run Apollo on port 8080 or similar

Also launch the nodejs server

    node taskrunner/index.js

## Run in production

Run the maven-install target to build and install to the system's local maven cache

    grails refresh-dependencies && grails maven-install

After it is installed to the local maven cache, add this to Apollo's BuildConfig.groovy

    grails.project.dependency.resolution = {
        ...
        plugins {
            compile ":apollo-domain-classes:1.0.9"
            compile ":apollo-lsaa:1.0.2"
        }
    }

Apollo will then use this plugin declaration and the API's for apollo-lsaa will be setup automatically.

You can then also use the taskrunner/index.js using Phusion Passenger or just as a isolated app as in development mode.

# Notes

Note: we are currently using the "apollo-domain-classes" modification from https://github.com/GMOD/Apollo/pull/962


