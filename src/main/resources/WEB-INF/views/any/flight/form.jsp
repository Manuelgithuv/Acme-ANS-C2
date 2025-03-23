<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="true"> 
	<acme:input-textbox code="any.flight.list.label.tag" path="tag" />
	<acme:input-checkbox code="any.flight.list.label.indication" path="indication"/>
	<acme:input-money code="any.flight.list.label.cost" path="cost" />
	<acme:input-textarea code="any.flight.list.label.description" path="description" />
	<acme:input-moment code="any.flight.list.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:input-moment code="any.flight.list.label.scheduledArrival" path="scheduledArrival"/>
	<acme:input-textbox code="any.flight.list.label.originCity" path="originCity"/>
	<acme:input-textbox code="any.flight.list.label.destinationCity" path="destinationCity"/>
	<acme:input-integer code="any.flight.list.label.layovers" path="layovers"/>
	<acme:button code="any.flight.list.button.legs" action="/any/leg/list?flightId=${id}"/>
</acme:form>