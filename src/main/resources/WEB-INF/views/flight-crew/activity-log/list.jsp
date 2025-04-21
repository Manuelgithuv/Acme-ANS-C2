<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.crew.label.registrationMoment" path="registrationMoment"/>
	<acme:list-column code="authenticated.crew.label.incidentType" path="incidentType"/>
	<acme:list-column code="authenticated.crew.label.severity" path="severity"/>
	<acme:list-column code="authenticated.crew.label.leg.flightCode" path="leg.flightCode"/>
	<acme:list-column code="authenticated.crew.label.published" path="published"/>
	<acme:list-payload path="payload"/>
</acme:list>
<jstl:if test="${_command == 'list'}">
	<acme:button code="authenticated.crew.button.log.create" action="/flight-crew/activity-log/create"/>
</jstl:if>	