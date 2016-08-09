<%@ page import="org.elsiklab.AlternativeLoci" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>LSAA - Alternative Loci</title>
    </head>
    <body>
    <g:render template="../layouts/reportHeader"/>
        <h3>Alternative loci</h3>
        <div id="list-availableStatus" class="content scaffold-list" role="main">
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <table>
                <thead>
                    <tr>
                        <g:sortableColumn property="id" title="Delete"/>
                        <g:sortableColumn property="id" title="Edit"/>
                        <g:sortableColumn property="lastUpdated" title="Last updated" params="${filters}"/>
                        <g:sortableColumn property="organism" title="Organism" params="${filters}"/>
                        <g:sortableColumn property="sequencename" title="Sequence name" params="${filters}"/>
                        <g:sortableColumn property="name" title="Name" params="${filters}"/>
                        <g:sortableColumn property="owners" title="Owner" params="${filters}"/>
                        <g:sortableColumn property="cvterm" title="CV Term" params="${filters}"/>
                        <g:sortableColumn property="description" title="Description" params="${filters}"/>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${features}" status="i" var="feature">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                            <td>
                                <g:if test="${feature.class.name == 'org.elsiklab.AlternativeLoci'}">
                                    <g:link action="delete" id="${feature.id}">Delete</g:link>
                                </g:if>
                                <g:if test="${feature.class.name == 'org.elsiklab.AlternativeRegion'}">
                                    <g:link action="deleteRegion" id="${feature.id}">Delete</g:link>
                                </g:if>
                            </td>
                            <td>
                                <g:if test="${feature.class.name == 'org.elsiklab.AlternativeLoci'}">
                                    <g:link action="edit" id="${feature.id}">Edit</g:link>
                                </g:if>
                                <g:if test="${feature.class.name == 'org.elsiklab.AlternativeRegion'}">
                                    <g:link action="editRegion" id="${feature.id}">Edit</g:link>
                                </g:if>
                            </td>
                            <td>
                                <g:formatDate format="E dd-MMM-yy" date="${feature.lastUpdated}"/>
                            </td>
                            <td>
                                ${feature.featureLocation?.sequence?.organism?.commonName}
                            </td>
                            <td>
                                ${feature.featureLocation?.sequence?.name}:${feature.featureLocation.fmin}..${feature.featureLocation.fmax}
                            </td>
                            <td>
                                ${feature.name}
                                <g:if env="development">
                                    <g:if test="${feature.class.name == 'org.elsiklab.AlternativeLoci'}">
                                        <a href="${g.createLink(absolute:true, base: 'http://localhost/apollo', uri: '/' + feature.featureLocation.sequence.organism.commonName+'/jbrowse/?loc=' + feature.name + '&organism='+feature.featureLocation.sequence.organism.id)}">alt locus</a>
                                    </g:if>
                                    <g:if test="${feature.class.name == 'org.elsiklab.AlternativeRegion'}">
                                        <a href="${g.createLink(absolute:true, base: 'http://localhost/apollo', uri: '/' + feature.featureLocation.sequence.organism.commonName+'/jbrowse/?loc=' + feature.featureLocation?.sequence?.name + ':' + feature.featureLocation.fmin + '..' + feature.featureLocation.fmax+ '&organism='+feature.featureLocation.sequence.organism.id)}">locus</a>
                                    </g:if>
                                </g:if>
                                <g:if env="production">
                                    <g:if test="${feature.class.name == 'org.elsiklab.AlternativeLoci'}">
                                        <a href="${g.createLink(absolute:true, uri: '/' + feature.featureLocation.sequence.organism.commonName+'/jbrowse/?loc=' + feature.name + '&organism='+feature.featureLocation.sequence.organism.id)}">alt locus</a>
                                    </g:if>
                                    <g:if test="${feature.class.name == 'org.elsiklab.AlternativeRegion'}">
                                        <a href="${g.createLink(absolute:true, uri: '/' + feature.featureLocation.sequence.organism.commonName+'/jbrowse/?loc=' + feature.featureLocation?.sequence?.name + ':' + feature.featureLocation.fmin + '..' + feature.featureLocation.fmax+ '&organism='+feature.featureLocation.sequence.organism.id)}">locus</a>
                                    </g:if>
                                </g:if>
                            </td>

                            <td>
                                ${feature.owner?.username}
                            </td>
                            <td>
                                ${feature.cvTerm}
                            </td>
                            <td>
                                ${feature.description}
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
