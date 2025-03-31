<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column sortable="false" code="authenticated.customer.list.label.fullName" path="fullName"/>
	<acme:list-column sortable="true" code="authenticated.customer.list.label.email" path="email"/>
	<acme:list-column sortable="false" code="authenticated.customer.list.label.passportNumber" path="passportNumber"/>
	<acme:list-column sortable="false" code="authenticated.customer.list.label.dateOfBirth" path="dateOfBirth"/>
	<acme:list-column sortable="false" code="authenticated.customer.list.label.specialNeeds" path="specialNeeds"/>
</acme:list>
<jstl:if test="${_command == 'list' && isBookingPublished==false}">
	<acme:button code="authenticated.customer.list.button.passenger.create" action="/customer/passenger/create?bookingId=${bookingId}"/>
</jstl:if>	