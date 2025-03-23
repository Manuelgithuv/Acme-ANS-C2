<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list navigable="false">
	<acme:list-column code="any.manager.list.leg.label.flightCode" path="flightCode"/>
	<acme:list-column code="any.manager.list.leg.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:list-column code="any.manager.list.leg.label.scheduledArrival" path="scheduledArrival"/>
	<acme:list-column code="any.manager.list.leg.label.status" path="status"/>
	<acme:list-column code="any.manager.list.leg.label.hours" path="hours"/>
	<acme:list-column code="any.manager.list.leg.label.managerIdentity" path="manager.identity.fullName"/>
</acme:list>