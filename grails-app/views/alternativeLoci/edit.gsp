<%@ page import="org.elsiklab.AlternativeLoci" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <g:set var="entityName" value="${message(code: 'alternativeLoci.name', default: 'AlternativeLoci')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <g:render template="../layouts/reportHeader"/>
        <div id="edit-alternativeLoci" class="content scaffold-edit" role="main">
            <h1><g:message code="Edit AlternativeLoci" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${alternativeLociInstance}">
            <ul class="errors" role="alert">
                <g:eachError bean="${alternativeLociInstance}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form url="[resource:alternativeLociInstance, action:'update']" method="PUT" >
                <g:hiddenField name="version" value="${alternativeLociInstance?.name}" />
                <fieldset class="form">
                    <g:render template="form"/>
                </fieldset>
                <fieldset class="buttons">
                    <g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
