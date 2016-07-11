package org.elsiklab

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class EditScaffold {
    static constraints = {
        filename nullable: true
        username nullable: true
        dateCreated nullable: true
        lastUpdated nullable: true
    }
    String filename
    String username
    Date dateCreated
    Date lastUpdated
}
