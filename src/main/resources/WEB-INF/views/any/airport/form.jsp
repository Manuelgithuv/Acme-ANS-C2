<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true"> 
	<acme:input-textbox code="any.airport.list.label.name" path="name" />
	<acme:input-textbox code="any.airport.list.label.iataCode" path="iataCode" />
	<acme:input-textbox code="any.airport.list.label.scope" path="scope" />
	<acme:input-textbox code="any.airport.list.label.city" path="city" />
	<acme:input-textbox code="any.airport.list.label.country" path="country" />
	<acme:input-textbox code="any.airport.list.label.webSite" path="webSite" />
	<acme:input-textbox code="any.airport.list.label.email" path="email" />
	<acme:input-textbox code="any.airport.list.label.phoneNumber" path="phoneNumber" />
</acme:form>