<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	
	<acme:list-column code="authenticated.customer.list.label.booking" path="booking"/>
	<acme:list-column code="authenticated.customer.list.label.passenger" path="passenger"/>
	<acme:list-column code="authenticated.customer.list.label.published" path="published"/>
	
	
	
</acme:list>
<jstl:if test="${_command == 'list'}">
	<acme:button code="authenticated.customer.list.button.bookingPassenger.create" action="/customer/booking-passenger/create"/>
</jstl:if>	