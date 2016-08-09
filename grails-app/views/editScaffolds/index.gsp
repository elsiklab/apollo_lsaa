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
                <g:form name="generate" url="${grailsApplication.config.lsaa.taskrunnerURL}">
                    <g:submitButton name="Generate scaffolds"></g:submitButton>
                </g:form>
                <g:form name="load" action="loadFromAltLoci">
                    <g:submitButton name="Load yaml from alternate loci"></g:submitButton>
                </g:form>
            </div>
            <div class="right">
            </div>
        </div>





        <p id="output"></p>
    </body>
</html>
