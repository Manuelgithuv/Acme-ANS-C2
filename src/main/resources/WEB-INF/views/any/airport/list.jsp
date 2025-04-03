<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.airport.list.label.name" path="name"/>
	<acme:list-column code="any.airport.list.label.iataCode" path="iataCode"/>
	<acme:list-column code="any.airport.list.label.scope" path="scope"/>
	<acme:list-column code="any.airport.list.label.city" path="city"/>
	<acme:list-column code="any.airport.list.label.country" path="country"/>
	<acme:list-column code="any.airport.list.label.webSite" path="webSite"/>
	<acme:list-column code="any.airport.list.label.email" path="email"/>
	<acme:list-column code="any.airport.list.label.phoneNumber" path="phoneNumber"/>	
</acme:list>