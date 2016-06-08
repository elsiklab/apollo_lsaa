package org.elsiklab

import org.bbop.apollo.BiologicalRegion

import groovy.transform.EqualsAndHashCode
@EqualsAndHashCode
class AlternativeRegion extends BiologicalRegion {

    static String ontologyId = "SO:0000702"
    static String cvTerm = "possible_assembly_error"
    static String alternateCvTerm = "alternative_region"
    String description
    static mapping = {
        residues type: 'text'
    }
}
