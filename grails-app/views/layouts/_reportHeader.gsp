<%@ page import="org.codehaus.groovy.grails.web.json.JSONArray" %>
<nav class="navbar navbar-default">
    <style>
    .menubox {
        border: 1px solid black;
        padding: 10px;
        background-color: #fff;
    }
    </style>
    <div class="apollo-header row">
        <g:link uri="/"><asset:image src="ApolloLogo_100x36.png"/></g:link>
        <div class="btn btn-group">
            <button class="btn dropdown-toggle glyphicon glyphicon-list-alt " data-toggle="dropdown">
                Reports
                <span class="caret"></span>
            </button>
            <ul class="dropdown-menu apollo-dropdown">
                <g:each in="${grailsApplication.config.apollo.administrativePanel}" var="report">
                    <g:if test="${report.type == 'report'}">
                        <li><g:link uri="${report.link}">${report.label}</g:link></li>
                    </g:if>
                </g:each>
            </ul>
        </div>
        <div class="menubox">
            <p>Scaffold editor menu</p>
            <ul>
                <li>
                    <g:link controller="addFasta" action="index">Add fasta</g:link>
                </li>
                <li>
                    <g:link controller="editScaffolds" action="index">Edit scaffolds</g:link>
                </li>
                <li>
                    <g:link controller="alternativeLoci" action="index">Alternative loci</g:link>
                </li>
            </ul>
        </div>
    </div>
</nav>
