<%@ page import="org.elsiklab.AlternativeLoci" %>
<%@ page import="org.elsiklab.FastaFile" %>

<div class="fieldcontain ${hasErrors(bean: alternativeLociInstance, field: 'value', 'error')} required">
    <label for="name">
        <g:message code="alternativeLoci.value.label" default="Name" />
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="name" required="" value="${alternativeLociInstance?.name}"/>
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
    <label for="description">
        <g:message code="alternativeLoci.value.label" default="Description" />
    </label>
    <g:textField name="description" required="" value="${alternativeLociInstance?.description}"/>
    <br />
    <label for="start_fasta">
        <g:message code="alternativeLoci.value.start_fasta" default="Start (fasta)" />
    </label>
    <g:textField name="start_fasta" required="" value="${alternativeLociInstance?.start_fasta}"/>
    <br />
    <label for="end_fasta">
        <g:message code="alternativeLoci.value.end_fasta" default="End (fasta)" />
    </label>
    <g:textField name="end_fasta" required="" value="${alternativeLociInstance?.end_fasta}"/>
    <br />
    <label for="fasta_file">
        <g:message code="alternativeLoci.value.fasta_file" default="Fasta file" />
    </label>
    <g:select name="fasta_file" required="" value="${alternativeLociInstance?.fasta_file}" from="${FastaFile.list()}" optionValue="filename" noSelection="[none:'N/A']" />

    <br />
</div>

