# apollo-lsaa

[![](https://travis-ci.org/elsiklab/apollo_lsaa.svg?branch=master)](https://travis-ci.org/elsiklab/apollo_lsaa)

A plugin for [Apollo](http://github.com/GMOD/Apollo) to annotate alternative loci (locus specific alternate assemblies)

# Prerequisites

* Grails 2.5.5
* Java 8 (minimum for htsjdk)
* Samtools (to perform faidx)
* JBrowse scripts (e.g. cpanm git://github.com/gmod/jbrowse.git) used for SequenceSearch
* BioPerl (e.g. cpanm BioPerl) used for SequenceSearch


# Setup

## Configuration

By default the bootstrapping should be setup for your organism in grails-app/conf/BootStrap.groovy

The configuration of the database can be setup in apollo-lsaa-groovy.config (if running apollo-lsaa standalone) or from apollo-config.groovy (if using apollo-lsaa as an Apollo plugin)

## Domain classes

Note: we are currently using the apollo-domain-classes plugin from https://github.com/elsiklab/apollo_domain_classes

Follow setup steps for apollo-domain-classes before installing apollo-lsaa

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
            compile ":apollo-domain-classes:1.0.12"
            compile ":apollo-lsaa:1.0.5"
        }
    }

Apollo will then use this plugin for apollo-lsaa automatically. The plugins can be installed using

    scripts/copy_client.sh

This copies the jbrowse plugins into ../../web-app/jbrowse, which would be the location of Apollo in production mode


# Tests

## Run tests/lint

    grails test-app
    grails codenarc


