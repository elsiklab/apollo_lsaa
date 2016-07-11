<%@ page import="org.elsiklab.AlternativeLoci" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>LSAA - Add scaffold</title>
        <style>
        .container {
            display: flex;
            border: 1px solid #000;
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
        <h3>Submit sequence</h3>

        <g:if test="${flash.message}">
            <div class="message row col-sm-12" role="status">${flash.message}</div>
        </g:if>

        <g:form name="scaffold" action="create" class="container">
            <div class="left">
                <p>Submit sequence in FASTA format</p>
                <g:textArea name="addFasta" class="addFasta"></g:textArea><br />
                <g:submitButton name="Submit"></g:submitButton>
            </div>
            <div class="center">
                <p>--or--</p>
            </div>
            <div class="right"> 
                <p>Submit path to existing FASTA file on filesystem</p>
                <g:textField name="addFile"></g:textField>
            </div>
        </g:form>

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
    </body>
</html>
