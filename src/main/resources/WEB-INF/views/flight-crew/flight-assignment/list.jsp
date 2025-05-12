<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.crew.label.assignee" path="assignee"/>
	<acme:list-column code="authenticated.crew.label.leg" path="leg"/>
	<acme:list-column code="authenticated.crew.label.duty" path="duty"/>
	<acme:list-column code="authenticated.crew.label.lastUpdate" path="lastUpdate"/>
	<acme:list-column code="authenticated.crew.label.status" path="status"/>
	<acme:list-column code="authenticated.crew.label.published" path="published"/>	
	<acme:list-payload path="payload"/>
</acme:list>
<jstl:if test="${_command == 'list'}">
	<acme:button code="authenticated.crew.button.create" action="/flight-crew/flight-assignment/create"/>
</jstl:if>