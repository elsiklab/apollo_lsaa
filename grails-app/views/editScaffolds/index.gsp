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
                <p><g:link action="getTransformedSequence">Get transformed sequence(JSON)</g:link></p>
                <p><g:link action="getTransformedYaml">Get transformed sequence (YAML)</g:link></p>
            </div>
        </div>
    </body>
</html>
