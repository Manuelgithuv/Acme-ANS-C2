<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="administrator.airline.list.label.name" path="name" width="33%"/>
	<acme:list-column code="administrator.airline.list.label.iataCode" path="iataCode" width="34%"/>
	<acme:list-column code="administrator.airport.list.label.scope" path="scope" width="33%"/>
	
	<acme:list-payload path="payload"/>	
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="administrator.airline.list.button.create" action="/administrator/airport/create"/>
</jstl:if>