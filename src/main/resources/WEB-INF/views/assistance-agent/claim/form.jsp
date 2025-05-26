<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-moment code="authenticated.assistance-agent.list.label.registrationMoment" path="registrationMoment"/>
    <acme:input-textbox code="authenticated.assistance-agent.list.label.passengerEmail" path="passengerEmail" />
    <acme:input-textbox code="authenticated.assistance-agent.list.label.description" path="description" />
    <acme:input-select code="authenticated.assistance-agent.list.label.status" path="status" choices="${statuses}"/>
    <acme:input-select code="authenticated.assistance-agent.list.label.type" path="type" choices="${types}"/>
    <acme:input-checkbox code="authenticated.assistance-agent.list.label.published" path="published" readonly="true"/>
    <acme:input-select code="authenticated.assistance-agent.list.label.leg" path="legId" choices="${legIds}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && published == false}">
			<acme:submit code="authenticated.assistance-agent.form.button.claim.publish" action="/assistance-agent/claim/publish"/>
			<acme:submit code="authenticated.assistance-agent.form.button.claim.update" action="/assistance-agent/claim/update"/>
			<acme:submit code="authenticated.assistance-agent.form.button.claim.delete" action="/assistance-agent/claim/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="authenticated.assistance-agent.form.button.claim.create" action="/assistance-agent/claim/create?flightId=${flightId}"/>
		</jstl:when>	
	</jstl:choose>
</acme:form>