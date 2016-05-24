package org.elsiklab.sequence.search;


import java.util.Collection;
import org.codehaus.groovy.grails.web.json.JSONObject;
import org.bbop.apollo.BlastAlignment;

public abstract class SequenceSearchTool {

    public abstract void parseConfiguration(JSONObject config) throws SequenceSearchToolException;

    public abstract Collection<BlastAlignment> search(String uniqueToken, String query, String databaseId, StringBuilder t) throws SequenceSearchToolException;
}
