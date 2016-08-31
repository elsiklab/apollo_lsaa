<%@ page import="org.elsiklab.AlternativeLoci" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>LSAA - Add scaffold</title>
        <style>
        .left {
            flex: 1;
        }
        .center {
            flex: 0.4;
        }
        .right {
            flex: 1;
        }
        .header {
            padding: 20px;
            display: block;
        }
        .container {
            margin-left: 20px;
            display: flex;
        }
        </style>
    </head>
    <body>
        <g:render template="../layouts/homeReportHeader"/>
        <div class="container">
            <div class="left">
                <h3 class="header">ApolloLSAA</h3>
                <p>Locus specific alternate assembly editor</p>

                <g:if test="${flash.message}">
                    <div class="message row col-sm-12" role="status">${flash.message}</div>
                </g:if>
            </div>
        </div>
    </body>
</html>
