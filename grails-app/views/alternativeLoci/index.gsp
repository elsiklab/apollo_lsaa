
<%@ page import="org.elsiklab.AlternativeLoci" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'availableStatus.label', default: 'AlternativeLoci')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
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
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <table>
            <thead>
                    <tr>
                    
                        <g:sortableColumn property="value" title="${message(code: 'availableStatus.name.label', default: 'Name')}" />
                        <g:sortableColumn property="value" title="${message(code: 'availableStatus.name.label', default: 'Description')}" />
                        <g:sortableColumn property="value" title="${message(code: 'availableStatus.name.label', default: 'Refseq')}" />
                        <g:sortableColumn property="value" title="${message(code: 'availableStatus.name.label', default: 'Start')}" />
                        <g:sortableColumn property="value" title="${message(code: 'availableStatus.name.label', default: 'End')}" />
                    
                    </tr>
                </thead>
                <tbody>
                <g:each in="${alternativeLociInstanceList}" status="i" var="alternativeLociInstance">
                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                    
                        <td><g:link action="show" id="${alternativeLociInstance.id}">${alternativeLociInstance.name}</g:link></td>
                        <td><g:link action="show" id="${alternativeLociInstance.id}">${alternativeLociInstance.description}</g:link></td>
                        <td><g:link action="show" id="${alternativeLociInstance.id}">${alternativeLociInstance.featureLocation.sequence.name}</g:link></td>
                        <td><g:link action="show" id="${alternativeLociInstance.id}">${alternativeLociInstance.featureLocation.fmin}</g:link></td>
                        <td><g:link action="show" id="${alternativeLociInstance.id}">${alternativeLociInstance.featureLocation.fmax}</g:link></td>
                    
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
