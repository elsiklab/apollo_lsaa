package org.elsiklab

import org.bbop.apollo.BiologicalRegion

import groovy.transform.EqualsAndHashCode
@EqualsAndHashCode
class AlternativeRegion extends BiologicalRegion {

    static String ontologyId = "SO:0001525" //possible_assembly_error
    static String cvTerm = "assembly_error_correction" // may have a link
    static String alternateCvTerm = "alternative_region"
    String description
    static mapping = {
        residues type: 'text'
    }
}
