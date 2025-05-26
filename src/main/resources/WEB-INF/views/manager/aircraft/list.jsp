<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<acme:list>
	<acme:list-column code="authenticated.manager.list.label.registrationNumber" path="registrationNumber"/>
	<acme:list-column code="authenticated.manager.list.label.iataCode" path="airline.iataCode"/>
</acme:list>