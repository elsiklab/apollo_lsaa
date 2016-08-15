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
            <p>Use the scaffolder syntax to edit the genome arrangement</p>
            <div class="left">
                <p><g:link action="getTransformedJSON">Get transformed sequence (JSON)</g:link></p>
                <p><g:link action="getTransformedYaml">Get transformed sequence (YAML)</g:link></p>
                <p><g:link action="getTransformedSequence">Get transformed sequence (FASTA)</g:link></p>
            </div>
        </div>
    </body>
</html>
