package org.elsiklab

import grails.transaction.Transactional
import grails.converters.JSON
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.bbop.apollo.Organism
import org.bbop.apollo.TabDelimitedAlignment
import org.codehaus.groovy.grails.web.json.JSONObject

class SequenceSearchController {

    def configWrapperService

    def searchSequence() {
        Organism organism = Organism.findById(request.JSON.organism)

        def search = request.JSON.search
        StringBuilder map = new StringBuilder()
        def searchUtils = searchTools().get(search.key)

        searchUtils.put("database", search.blast ? organism.fasta : organism.blatdb)
        searchUtils.put("output_dir", organism.directory)

        // dynamically allocate a search_class
        def searcher = this.class.classLoader.loadClass( searchUtils.search_class )?.newInstance()

        searcher.parseConfiguration(searchUtils as JSONObject)

        def results = searcher.search('searchid', search.residues, search.database_id ?: "", map)

        def slurper = new JsonSlurper()
        def filetext = new File(organism.directory+"/trackList.json").text
        def trackList = slurper.parseText(filetext)

        def newtrack = [:]
        trackList.tracks.each { result ->
            if(result.label == map.toString()) {
                newtrack = result
            }
        }


        JsonBuilder json = new JsonBuilder ()
        json {
            matches results.collect { TabDelimitedAlignment result ->
                [
                    "identity": result.percentId,
                    "significance": result.eValue,
                    "subject": ({
                        "location" ({
                            "fmin" result.subjectStart
                            "fmax" result.subjectEnd
                            "strand" result.subjectStrand
                        })
                        "feature" ({
                            "uniquename" result.subjectId
                            "type"({
                                "name" "region"
                                "cv" ({
                                    "name" "sequence"
                                })
                            })
                        })
                    }),
                    "query": ({
                        "location" ({
                            "fmin" result.queryStart
                            "fmax" result.queryEnd
                            "strand" result.queryStrand
                        })
                        "feature" ({
                            "uniquename" result.queryId
                            "type" ({
                                "name" "region"
                                "cv"({
                                    "name" "sequence"
                                })
                            })
                        })
                    }),
                    "rawscore": result.bitscore
                ]
            }
            track newtrack
        }

        
        render json
    }

    def searchTools() {
        return [
            blat_nuc: [
                search_exe: "/usr/local/bin/blat",
                search_class: "org.elsiklab.sequence.search.blat.BlatCommandLineNucleotideToNucleotide",
                name: "Blat nucleotide",
                params: "",
                tmp_dir: "/opt/apollo/tmp",
                removeTmpDir: false
            ],
            blat_prot: [
                search_exe: "/usr/local/bin/blat",
                search_class: "org.elsiklab.sequence.search.blat.BlatCommandLineProteinToNucleotide",
                name: "Blat protein",
                params: "",
                tmp_dir: "/opt/apollo/tmp",
                removeTmpDir: false
            ],
            blast_prot: [
                search_exe: "/usr/local/ncbi/blast/bin/tblastn",
                formatter_exe: "/usr/local/ncbi/blast/bin/blast_formatter",
                gff_exe: "bp_search2gff.pl",
                search_class: "org.elsiklab.sequence.search.blast.BlastCommandLine",
                name: "Blast protein",
                params: ""
            ]
        ]
    }

    def getSequenceSearchTools() {
        def jre = ["sequence_search_tools": searchTools()]
        render jre as JSON
    }
}
