<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-select code="authenticated.customer.list.label.booking" path="booking" choices="${bookings}" />
	<acme:input-select code="authenticated.customer.list.label.passenger" path="passenger" choices="${passengers}"/>
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish') && published == false}">
			<acme:submit code="authenticated.customer.form.button.flight.publish" action="/customer/bookingPassenger/publish"/>
			<acme:submit code="authenticated.customer.form.button.flight.update" action="/customer/bookingPassenger/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="authenticated.customer.form.button.flight.create" action="/customer/bookingPassenger/create"/>
		</jstl:when>	
	</jstl:choose>
</acme:form>	
	
