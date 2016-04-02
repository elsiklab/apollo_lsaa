
<%@ page import="org.elsiklab.AlternativeLoci" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>LSAA - Alternative Loci</title>
    </head>
    <body>
    <g:render template="../layouts/reportHeader"/>
        <a href="#list-availableStatus" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
            </ul>
        </div>
        <div id="list-availableStatus" class="content scaffold-list" role="main">
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <table>
            <thead>
                    <tr>

                    <g:sortableColumn property="lastUpdated" title="Last updated" params="${filters}"/>
                    <g:sortableColumn property="organism" title="Organism" params="${filters}"/>
                    <g:sortableColumn property="sequencename" title="Sequence name" params="${filters}"/>
                    <g:sortableColumn property="name" title="Name" params="${filters}"/>
                    <g:sortableColumn property="owners" title="Owner" params="${filters}"/>

                    </tr>
                </thead>
                <tbody>

                <g:each in="${features}" status="i" var="feature">
                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                    <td>
                        <g:formatDate format="E dd-MMM-yy" date="${feature.lastUpdated}"/>
                    </td>
                    <td>
                        ${feature.featureLocation?.sequence.organism.commonName}
                    </td>
                    <td>
                        ${feature.featureLocation?.sequence.name}
                    </td>
                    <td>
                        <g:link target="_blank" controller="annotator" action="loadLink"
                                params="[loc: feature.featureLocation?.sequence.name + ':' + feature.featureLocation.fmin + '..' + feature.featureLocation.fmax, organism: feature.featureLocation?.sequence.organism.id]">
                            ${feature.name}
                        </g:link>
                    </td>

                    <td>
                        ${feature.owner?.username}
                    </td>
                </tr>
                </g:each>


                </tbody>
            </table>
            <div class="pagination">
                <g:paginate total="${alternativeLociInstanceCount ?: 0}" />
            </div>
        </div>
    </body>
</html>
