<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="authenticated.manager.list.label.flightCode" path="flightCode" />
	<acme:input-moment code="authenticated.manager.list.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:input-moment code="authenticated.manager.list.label.scheduledArrival" path="scheduledArrival" />
	<acme:input-select code="authenticated.manager.list.label.status" path="status" choices="${statuses}"/>
	<acme:input-double code="authenticated.manager.list.label.hours" path="hours"/>
	<acme:input-select code="authenticated.manager.list.label.departureAirport" path="departureAirport" choices="${departureAirports}" />
	<acme:input-select code="authenticated.manager.list.label.arrivalAirport" path="arrivalAirport" choices="${arrivalAirports}"/>
	<acme:input-select code="authenticated.manager.list.label.aircraft" path="aircraft" choices="${aircrafts}"/>
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && published == false}">
			<acme:submit code="authenticated.manager.form.button.leg.publish" action="/manager/leg/publish"/>
			<acme:submit code="authenticated.manager.form.button.leg.update" action="/manager/leg/update"/>
			<acme:submit code="authenticated.manager.form.button.leg.delete" action="/manager/leg/delete"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="authenticated.manager.form.button.leg.create" action="/manager/leg/create?flightId=${flightId}"/>
		</jstl:when>	
	</jstl:choose>
</acme:form>