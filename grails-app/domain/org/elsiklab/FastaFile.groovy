package org.elsiklab

import org.bbop.apollo.Organism

class FastaFile {

    static constraints = {
        organism nullable: true
    }



    String filename
    String originalname
    String username
    Date dateCreated
    Date lastUpdated
    Organism organism
}
