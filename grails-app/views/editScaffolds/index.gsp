<%@ page import="org.elsiklab.AlternativeLoci" %>
<%@ page import="org.bbop.apollo.Organism" %>
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
            margin-left: 20px;
        }
        .header {
            padding: 20px;
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
        <h3 class="header">Scaffold editor</h3>
        
        <div class="container">
            <g:if test="${error}">
                <div class="message" role="status">${error}</div>
            </g:if>
            <div class="left">
                <g:form action="export">
                    <g:select name="organism" required="" from="${Organism.list()}" optionValue="commonName" optionKey="id" />
                    <select name="type">
                        <option>JSON</option>
                        <option>YAML</option>
                        <option>FASTA</option>
                    </select>
                    <g:submitButton name="Submit"></g:submitButton>
                </g:form>
            </div>
        </div>
    </body>
</html>
