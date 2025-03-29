<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="authenticated.customer.list.label.fullName" path="fullName" />
	<acme:input-textbox code="authenticated.customer.list.label.email" path="email"/>
	<acme:input-textbox code="authenticated.customer.list.label.passportNumber" path="passportNumber" />
	<acme:input-moment code="authenticated.customer.list.label.dateOfBirth" path="dateOfBirth"/>
	<acme:input-textarea code="authenticated.customer.list.label.specialNeeds" path="specialNeeds"/>
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|publish') && published == false}">
			<acme:submit code="authenticated.customer.form.button.passenger.publish" action="/customer/passenger/publish"/>
			<acme:submit code="authenticated.customer.form.button.passenger.update" action="/customer/passenger/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="authenticated.customer.form.button.passenger.create" action="/customer/passenger/create?bookingId=${bookingId}"/>
		</jstl:when>	
	</jstl:choose>
</acme:form>