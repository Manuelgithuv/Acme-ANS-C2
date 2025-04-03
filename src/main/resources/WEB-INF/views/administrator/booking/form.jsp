<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="authenticated.administrator.list.label.locatorCode" path="locatorCode" />
	<acme:input-moment code="authenticated.administrator.list.label.purchaseMoment" path="purchaseMoment"/>
	<acme:input-select code="authenticated.administrator.list.label.travelClass" path="travelClass" choices="${travelClasses}" />
	<acme:input-money code="authenticated.administrator.list.label.price" path="price" />
	<acme:input-textbox code="authenticated.administrator.list.label.lastCardNibble" path="lastCardNibble" />
	<acme:input-select code="authenticated.administrator.list.label.flight" path="flight" choices="${flights}"/>
	<jstl:choose>
		<jstl:when test="${_command == 'show'&& published== true}">
				<acme:button code="authenticated.administrator.list.button.passengers" action="/administrator/passenger/listPassengerByBooking?bookingId=${id}"/>
		</jstl:when>	
	</jstl:choose>
</acme:form>	
	
