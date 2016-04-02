<!DOCTYPE html>
<html>
	<head>
		<title>Grails Runtime Exception</title>
		<meta name="layout" content="main">
		<asset:stylesheet src="errors.css"/>
	</head>
	<body>
        <ul class="errors">
            <g:renderException exception="${exception}" />
        </ul>
	</body>
</html>
