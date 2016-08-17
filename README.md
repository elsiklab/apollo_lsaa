# apollo-lsaa

[![](https://travis-ci.org/elsiklab/apollo_lsaa.svg?branch=master)](https://travis-ci.org/elsiklab/apollo_lsaa)

A plugin for [Apollo](http://github.com/GMOD/Apollo) to annotate alternative loci (locus specific alternate assemblies)


# Setup

## Bootstrap you data

By default the bootstrapping should be setup for your organism in grails-app/conf/BootStrap.groovy

## Run in development


In dev, you can install jbrowse into web-app/jbrowse with this script

    scripts/install_dev.sh

The app can also be run as a stand-alone app without running Apollo2 proper

    grails run-app

If something is on port 8080 use `grails run-app -Dserver.port=8085` instead

## Run in production

Install the plugin to your maven cache

    grails refresh-dependencies
    grails maven-install

After that, add the plugin to Apollo's BuildConfig.groovy

    grails.project.dependency.resolution = {
        ...
        plugins {
            compile ":apollo-domain-classes:1.0.10"
            compile ":apollo-lsaa:1.0.4"
        }
    }

Apollo will then use this plugin for apollo-lsaa automatically. The plugins can be installed using

    scripts/copy_client.sh

This copies the jbrowse plugins into ../../web-app/jbrowse, which would be the location of Apollo in production mode


# Tests

## Run tests/lint

    grails test-app
    grails codenarc


# Notes

Note: we are currently using the "apollo-domain-classes" modification from https://github.com/GMOD/Apollo/pull/962



