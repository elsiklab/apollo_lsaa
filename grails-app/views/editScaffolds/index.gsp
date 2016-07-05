<%@ page import="org.elsiklab.AlternativeLoci" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>LSAA - Edit scaffolds</title>
        <style>
        .scaffoldEditor {
            width: 700px;
            height: 400px;
        }
        </style>
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
        </div>
        <h3>Scaffold editor</h3>
        <p>Use the scaffolder syntax to edit the genome arrangement</p>
        <g:if test="${error}">
            <div class="message" role="status">${error}</div>
        </g:if>

        <g:form name="scaffold" action="editScaffold">
            <g:textArea name="scaffoldEditor" class="scaffoldEditor" value="${yaml}"></g:textArea><br />
            <g:submitButton name="Submit"></g:submitButton>
        </g:form>


        <g:form name="generate" action="generateScaffolds">
            <g:submitButton name="Generate scaffolds"></g:submitButton>
        </g:form>

        <p id="output"></p>
    </body>
</html>
