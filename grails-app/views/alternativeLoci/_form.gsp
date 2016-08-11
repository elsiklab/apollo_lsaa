<%@ page import="org.elsiklab.AlternativeLoci" %>
<%@ page import="org.elsiklab.FastaFile" %>
<%@ page import="org.bbop.apollo.Sequence" %>

<div class="fieldcontain ${hasErrors(bean: alternativeLociInstance, field: 'value', 'error')} required">

    <label for="name">
        <g:message code="alternativeLoci.value.label" default="Name" />
        <span class="required-indicator">*</span>
    </label>
    <g:select name="name" required="" value="${alternativeLociInstance?.featureLocation?.sequence?.name}" from="${Sequence.list()}" optionValue="name" optionKey="id" />
    <br />
    <label for="start">
        <g:message code="alternativeLoci.value.label" default="Start" />
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="start" required="" value="${alternativeLociInstance?.featureLocation?.fmin}"/>
    <br />
    <label for="end">
        <g:message code="alternativeLoci.value.label" default="End" />
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="end" required="" value="${alternativeLociInstance?.featureLocation?.fmax}"/>
    <br />
    <label for="fasta_file">
        <g:message code="alternativeLoci.value.fasta_file" default="Fasta file" />
        <span class="required-indicator">*</span>
    </label>
    <g:select name="fasta_file" required="" value="${alternativeLociInstance?.fasta_file?.originalname}" from="${FastaFile.list()}" optionKey="id" optionValue="originalname" />
    <br />

    <label for="start_file">
        <g:message code="alternativeLoci.value.start_file" default="Start (fasta)" />
    </label>
    <g:textField name="start_file" value="${alternativeLociInstance?.start_file}"/>
    <br />
    <label for="end_file">
        <g:message code="alternativeLoci.value.end_file" default="End (fasta)" />
    </label>
    <g:textField name="end_file" value="${alternativeLociInstance?.end_file}"/>
    <br />
    <label for="description">
        <g:message code="alternativeLoci.value.label" default="Description" />
    </label>
    <g:textField name="description" value="${alternativeLociInstance?.description}"/>
    <br />
    <label for="reversed">
        <g:message code="alternativeLoci.value.label" default="Reverse complement?" />
    </label>
    <g:checkBox name="reversed" value="${alternativeLociInstance?.reversed}"/>



    <br />
</div>

