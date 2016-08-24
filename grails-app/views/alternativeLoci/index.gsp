<%@ page import="org.elsiklab.AlternativeLoci" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>LSAA - Alternative Loci</title>
        <style>
        .container {
            margin-left: 20px;
        }
        .header {
            padding: 20px;
        }
        </style>
    </head>
    <body>
    <g:render template="../layouts/reportHeader"/>
    <div class="container">
        <h3 class="header">Alternative loci</h3>
        <div id="list-availableStatus" class="content scaffold-list" role="main">

            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>

            <g:link action="create">Create</g:link>
            
            <table>
                <thead>
                    <tr>
                        <g:sortableColumn property="id" title="Delete"/>
                        <g:sortableColumn property="id" title="Edit"/>
                        <g:sortableColumn property="lastUpdated" title="Last updated" params="${filters}"/>
                        <g:sortableColumn property="organism" title="Organism" params="${filters}"/>
                        <g:sortableColumn property="sequencename" title="Sequence name" params="${filters}"/>
                        <g:sortableColumn property="name" title="Name" params="${filters}"/>
                        <g:sortableColumn property="start_file" title="Alternative coordinates" params="${filters}"/>
                        <g:sortableColumn property="owners" title="Owner" params="${filters}"/>
                        <g:sortableColumn property="cvterm" title="CV Term" params="${filters}"/>
                        <g:sortableColumn property="description" title="Description" params="${filters}"/>
                        <g:sortableColumn property="reversed" title="Reversed" params="${filters}"/>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${features}" status="i" var="feature">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                            <td><g:link action="delete" id="${feature.id}">Delete</g:link></td>
                            <td><g:link action="edit" id="${feature.id}">Edit</g:link></td>
                            <td><g:formatDate format="E dd-MMM-yy" date="${feature.lastUpdated}"/></td>
                            <td>${feature.featureLocation?.sequence?.organism?.commonName}</td>
                            <td>${feature.featureLocation?.sequence?.name}:${feature.featureLocation.fmin}..${feature.featureLocation.fmax}</td>
                            <td>
                                <g:if env="development">
                                    <a href="${g.createLink(relativeUri: '../jbrowse/?loc=' + feature.featureLocation?.sequence?.name + ':' + feature.featureLocation.fmin + '..' + feature.featureLocation.fmax + '&organism='+feature.featureLocation.sequence.organism.id)}">alt locus</a>
                                </g:if>
                                <g:if env="production">
                                    <a href="${g.createLink(absolute:true, uri: '/' + feature.featureLocation.sequence.organism.commonName+'/jbrowse/?loc=' + feature.name + '&organism='+feature.featureLocation.sequence.organism.id)}">alt locus</a>
                                </g:if>
                            </td>
                            <td>${feature.name_file}:${feature.start_file}..${feature.end_file}</td>
                            <td>${feature.owner?.username}</td>
                            <td>${feature.cvTerm}</td>
                            <td>${feature.description}</td>
                            <td>${feature.reversed}</td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
            <div class="pagination">
                <g:paginate total="${alternativeLociInstanceCount ?: 0}" />
            </div>
        </div>
        </div>
    </body>
</html>
