<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.manager.list.label.flightCode" path="flightCode"/>
	<acme:list-column code="authenticated.manager.list.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:list-column code="authenticated.manager.list.label.scheduledArrival" path="scheduledArrival"/>
	<acme:list-column code="authenticated.manager.list.label.status" path="status"/>
	<acme:list-column code="authenticated.manager.list.label.hours" path="hours"/>
	<acme:list-column code="authenticated.manager.list.label.managerIdentity" path="manager.identity.fullName"/>
</acme:list>