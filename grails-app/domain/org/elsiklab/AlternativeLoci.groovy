package org.elsiklab

import org.bbop.apollo.BiologicalRegion

import groovy.transform.EqualsAndHashCode
@EqualsAndHashCode
class AlternativeLoci extends BiologicalRegion {

    static String ontologyId = "SO:0000702" //possible_assembly_error
    static String cvTerm = "possible_assembly_error" // may have a link
    static String alternateCvTerm = "alternative_loci"
    static constraints = {
        residues nullable: true
    }
    String residues
    String description
    static mapping = {
        residues type: 'text'
    }
}
