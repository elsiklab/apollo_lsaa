<%@ page import="org.elsiklab.AlternativeLoci" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>LSAA - Edit scaffolds</title>
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

        <g:form name="scaffold">
            <g:textArea name="scaffoldEditor"></g:textArea>
        </g:form>
    </body>
</html>
