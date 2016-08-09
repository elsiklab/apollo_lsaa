
<%@ page import="org.elsiklab.AlternativeLoci" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'availableStatus.label', default: 'AlternativeLoci')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <g:render template="../layouts/reportHeader"/>
        <div id="show-availableStatus" class="content scaffold-show" role="main">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <ol class="property-list availableStatus">
            
                <g:if test="${alternativeLociInstance?.name}">
                <li class="fieldcontain">
                    <span id="value-label" class="property-label"><g:message code="availableStatus.name.label" default="Properties" /></span>
                    
                        <span class="property-value" aria-labelledby="value-label">Name: ${alternativeLociInstance.name}</span>
                        <span class="property-value" aria-labelledby="value-label">Start: ${alternativeLociInstance.featureLocation.fmin}</span>
                        <span class="property-value" aria-labelledby="value-label">End: ${alternativeLociInstance.featureLocation.fmax}</span>
                        <span class="property-value" aria-labelledby="value-label">Description: ${alternativeLociInstance.description}</span>
                    
                </li>
                </g:if>
            
            </ol>
            <g:form url="[resource:alternativeLociInstance, action:'delete']" method="DELETE">
                <fieldset class="buttons">
                    <g:link class="edit" action="edit" resource="${alternativeLociInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                    <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
