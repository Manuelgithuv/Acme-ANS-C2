<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="authenticated.customer.list.label.locatorCode" path="locatorCode" />
	<acme:input-moment code="authenticated.customer.list.label.purchaseMoment" path="purchaseMoment"/>
	<acme:input-select code="authenticated.customer.list.label.travelClass" path="travelClass" choices="${travelClasses}" />
	<acme:input-money code="authenticated.customer.list.label.price" path="price" />
	<acme:input-textbox code="authenticated.customer.list.label.lastCardNibble" path="lastCardNibble" />
	<acme:input-select code="authenticated.customer.list.label.flight" path="flight" choices="${flights}"/>
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish') && published == false}">
		
			<acme:button code="authenticated.customer.list.button.passengers" action="/customer/passenger/listPassengerByBooking?bookingId=${id}"/>
		
			<acme:submit code="authenticated.customer.form.button.flight.publish" action="/customer/booking/publish"/>
			<acme:submit code="authenticated.customer.form.button.flight.update" action="/customer/booking/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'show'&& published== true}">
				<acme:button code="authenticated.customer.list.button.passengers" action="/customer/passenger/listPassengerByBooking?bookingId=${id}"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="authenticated.customer.form.button.flight.create" action="/customer/booking/create"/>
		</jstl:when>	
	</jstl:choose>
</acme:form>	
	
