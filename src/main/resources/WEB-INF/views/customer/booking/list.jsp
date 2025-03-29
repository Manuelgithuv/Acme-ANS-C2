<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.customer.list.label.locatorCode" path="locatorCode"/>
	<acme:list-column code="authenticated.customer.list.label.travelClass" path="travelClass"/>
	<acme:list-column code="authenticated.customer.list.label.price" path="price"/>
	<acme:list-column code="authenticated.customer.list.label.published" path="published"/>
	<acme:list-column code="authenticated.customer.list.label.lastCardNibble" path="lastCardNibble"/>
</acme:list>
<jstl:if test="${_command == 'list'}">
	<acme:button code="authenticated.customer.list.button.booking.create" action="/customer/booking/create"/>
</jstl:if>	