<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-moment code="authenticated.assistance-agent.list.label.lastUpdateMoment" path="lastUpdateMoment" readonly="true"/>
	<acme:input-moment code="authenticated.assistance-agent.list.label.creationMoment" path="creationMoment" readonly="true"/>
    <acme:input-textbox code="authenticated.assistance-agent.list.label.stepUndergoing" path="stepUndergoing" />
	<acme:input-select code="authenticated.assistance-agent.list.label.status" path="status" choices="${statuses}"/>
    <acme:input-double code="authenticated.assistance-agent.list.label.resolutionPercentage" path="resolutionPercentage"/>
    <acme:input-textbox code="authenticated.assistance-agent.list.label.resolutionDescription" path="resolutionDescription" />
    <acme:input-money code="authenticated.assistance-agent.list.label.compensation" path="compensation" />
    <acme:input-checkbox code="authenticated.assistance-agent.list.label.claimAcepted" path="claimAcepted" readonly="true"/>
    <acme:input-checkbox code="authenticated.assistance-agent.list.label.published" path="published" readonly="true"/>
    <acme:input-select code="authenticated.assistance-agent.list.label.claim" path="claim" choices="${claims}"/>
    	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && published == false}">
			<acme:submit code="authenticated.assistance-agent.form.button.claim.publish" action="/assistance-agent/claim-tracking-log/publish"/>
			<acme:submit code="authenticated.assistance-agent.form.button.claim.update" action="/assistance-agent/claim-tracking-log/update"/>
			<acme:submit code="authenticated.assistance-agent.form.button.claim.delete" action="/assistance-agent/claim-tracking-log/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="authenticated.assistance-agent.form.button.claim.create" action="/assistance-agent/claim-tracking-log/create"/>
		</jstl:when>	
	</jstl:choose>
</acme:form>