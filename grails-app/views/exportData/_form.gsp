<%@ page import="org.elsiklab.AlternativeLoci" %>



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
    <label for="residues">
        <g:message code="alternativeLoci.value.label" default="Residues" />
    </label>
    <p>${alternativeLociInstance?.residues}</p>
</div>

