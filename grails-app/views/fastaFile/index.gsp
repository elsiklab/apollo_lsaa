<%@ page import="org.elsiklab.AlternativeLoci" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>LSAA - Add scaffold</title>
        <style>
        .fastaFile {
            width: 100%;
            height: 400px;
        }
        .formcontainer {
            display: flex;
        }
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
        }
        .container {
            margin-left: 20px;
        }
        </style>
    </head>
    <body>
    <g:render template="../layouts/reportHeader"/>
    <div class="container">
        <h3 class="header">Submit sequence</h3>

        <g:if test="${flash.message}">
            <div class="message row col-sm-12" role="status">${flash.message}</div>
        </g:if>

        <div class="formcontainer">
            <div class="left">
                <p>Submit sequence in FASTA format</p>
                <g:form name="scaffold" action="uploadText">
                    <g:textArea name="fastaFile" class="fastaFile"></g:textArea><br />
                    <g:submitButton name="Submit"></g:submitButton>
                </g:form>
            </div>
            <div class="center">
            </div>
            <div class="right"> 
                <p>Upload a FASTA file</p>
                <g:form action="uploadFile" method="post" enctype="multipart/form-data">
                    <input type="file" name="fastaFile" />
                    <g:submitButton name="Submit"></g:submitButton>
                </g:form>
            </div>
        </div>

        <p id="output"></p>


        <div id="list-feature" class="content scaffold-list" role="main">
            <table>
                <thead>
                    <tr>
                        <g:sortableColumn property="filename" title="Filename" params="${filters}"/>
                        <g:sortableColumn property="user" title="Username" params="${filters}"/>
                        <g:sortableColumn property="dateCreated" title="Date created" params="${filters}"/>
                        <g:sortableColumn property="lastUpdated" title="Last updated" params="${filters}"/>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${features}" status="i" var="feature">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                            <td>
                                <g:link action="delete" id="${feature.id}">Delete</g:link>
                            </td>
                            <td>
                                <g:link action="edit" id="${feature.id}">Edit</g:link>
                            </td>
                            <td>
                                ${feature.filename}
                            </td>
                            <td>
                                ${feature.username}
                            </td>
                            <td>
                                <g:formatDate format="E dd-MMM-yy" date="${feature.lastUpdated}"/>
                            </td>
                            <td>
                                <g:formatDate format="E dd-MMM-yy" date="${feature.dateCreated}"/>
                            </td>
                        </tr>
                    </g:each>
                </tbody>
            </table>

            <div class="pagination">
                <g:paginate total="${featureCount ?: 0}" params="${params}"/>
            </div>
        </div>
        </div>
    </body>
</html>
