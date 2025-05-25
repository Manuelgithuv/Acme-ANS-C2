<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="authenticated.crew.label.registrationMoment" path="registrationMoment" placeholder="crew.registrationMoment.placeholder" />
	<acme:input-textbox code="authenticated.crew.label.incidentType" path="incidentType" placeholder="crew.incidentType.placeholder" />
	<acme:input-textarea code="authenticated.crew.label.description" path="description" placeholder="crew.description.placeholder" />
	<acme:input-integer code="authenticated.crew.label.severity" path="severity" placeholder="crew.severity.placeholder" />
	<acme:input-select code="authenticated.crew.label.leg" path="leg" choices="${legs}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && canPublish}">
			<acme:submit code="authenticated.crew.button.delete" action="/flight-crew/activity-log/delete"/>
			<acme:submit code="authenticated.crew.button.update" action="/flight-crew/activity-log/update"/>
			<acme:submit code="authenticated.crew.button.publish" action="/flight-crew/activity-log/publish"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && !published}">
			<acme:submit code="authenticated.crew.button.delete" action="/flight-crew/activity-log/delete"/>
			<acme:submit code="authenticated.crew.button.update" action="/flight-crew/activity-log/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="authenticated.crew.button.create" action="/flight-crew/activity-log/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>	
	
