package org.elsiklab

import org.bbop.apollo.BiologicalRegion

import groovy.transform.EqualsAndHashCode
@EqualsAndHashCode
class AlternativeLoci extends BiologicalRegion {

    static String ontologyId = "SO:0001525"
    static String cvTerm = "assembly_error_correction"
    static String alternateCvTerm = "alternative_loci"


    static constraints = {
        reverse nullable: true
        description nullable: true
    }

    String description
    Boolean reverse


    static mapping = {
        description type: 'text'
    }
}
