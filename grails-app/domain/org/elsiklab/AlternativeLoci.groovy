package org.elsiklab

import org.bbop.apollo.BiologicalRegion

import groovy.transform.EqualsAndHashCode
@EqualsAndHashCode
class AlternativeLoci extends BiologicalRegion {

    static String ontologyId = "SO:0001525"
    static String cvTerm = "assembly_error_correction"
    static String alternateCvTerm = "alternative_loci"


    static constraints = {
        reversed nullable: true
        description nullable: true
    }

    String description
    Boolean reversed

    Integer start_file
    Integer end_file
    FastaFile fasta_file


    static mapping = {
        description type: 'text'
    }
}
