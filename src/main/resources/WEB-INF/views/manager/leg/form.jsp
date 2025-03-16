<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="authenticated.manager.list.label.flightCode" path="flightCode" />
	<acme:input-moment code="authenticated.manager.list.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:input-moment code="authenticated.manager.list.label.scheduledArrival" path="scheduledArrival" />
	<acme:input-textbox code="authenticated.manager.list.label.status" path="status"/>
	<acme:input-double code="authenticated.manager.list.label.hours" path="hours"/>
	<acme:input-select code="authenticated.manager.list.label.flight" path="flight" choices="${flights}" />
	<acme:input-select code="authenticated.manager.list.label.departureAirport" path="departureAirport" choices="${departureAirports}" />
	<acme:input-select code="authenticated.manager.list.label.arrivalAirport" path="arrivalAirport" choices="${arrivalAirports}"/>
	<acme:input-select code="authenticated.manager.list.label.aircraft" path="aircraft" choices="${aircrafts}"/>
</acme:form>