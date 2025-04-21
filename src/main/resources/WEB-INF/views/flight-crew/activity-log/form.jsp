<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="authenticated.crew.label.registrationMoment" path="registrationMoment" placeholder="crew.registrationMoment.placeholder" />
	<acme:input-textbox code="authenticated.crew.label.incidentType" path="incidentType" placeholder="crew.incidentType.placeholder" />
	<acme:input-textarea code="authenticated.crew.label.description" path="description" placeholder="crew.description.placeholder" />
	<acme:input-integer code="authenticated.crew.label.severity" path="severity" placeholder="crew.severity.placeholder" />
	<acme:input-select code="authenticated.crew.label.leg.flightCode" path="leg.flightCode" choices="${legs}"/>	
	<jstl:choose>
		<jstl:when test="${_command == 'show' && published == false}">
			<acme:submit code="authenticated.crew.button.log.publish" action="/flight-crew/activity-log/publish"/>
			<acme:submit code="authenticated.crew.button.log.update" action="/flight-crew/activity-log/update"/>
			<acme:submit code="authenticated.crew.button.log.delete" action="/flight-crew/activity-log/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'update' && published == false}">
			<acme:submit code="authenticated.crew.button.log.update" action="/flight-crew/activity-log/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="authenticated.crew.button.log.create" action="/flight-crew/activity-log/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>	
	
