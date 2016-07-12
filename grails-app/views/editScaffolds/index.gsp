<%@ page import="org.elsiklab.AlternativeLoci" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>LSAA - Edit scaffolds</title>
        <style>
        .scaffoldEditor {
            width: 100%;
            height: 400px
        }
        .container {
            display: flex;
        }
        .left {
            flex: 1;
        }
        .right {
            flex: 1;
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

        <h3>Scaffold editor</h3>
        <p>Use the scaffolder syntax to edit the genome arrangement</p>
        <g:if test="${error}">
            <div class="message" role="status">${error}</div>
        </g:if>
        <div class="container">
            <div class="left">
                <g:form name="scaffold" action="editScaffold">
                    <g:textArea name="scaffoldEditor" class="scaffoldEditor" value="${yaml}"></g:textArea><br />
                    <g:submitButton name="Submit"></g:submitButton>
                </g:form>
            </div>

            <div class="right">
            </div>
        </div>


        <g:form name="generate" action="generateScaffolds">
            <g:submitButton name="Generate scaffolds"></g:submitButton>
        </g:form>
        <g:form name="load" action="loadFromAltLoci">
            <g:submitButton name="Load yaml from alternate loci"></g:submitButton>
        </g:form>



        <p id="output"></p>
    </body>
</html>
