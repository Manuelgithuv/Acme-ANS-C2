<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="authenticated.manager.list.label.tag" path="tag" />
	<acme:input-checkbox code="authenticated.manager.list.label.indication" path="indication"/>
	<acme:input-money code="authenticated.manager.list.label.cost" path="cost" />
	<acme:input-textarea code="authenticated.manager.list.label.description" path="description" />
	<acme:input-moment code="authenticated.manager.list.label.scheduledDeparture" path="scheduledDeparture"  readonly="true"/>
	<acme:input-moment code="authenticated.manager.list.label.scheduledArrival" path="scheduledArrival"  readonly="true"/>
	<acme:input-textbox code="authenticated.manager.list.label.originCity" path="originCity" readonly="true"/>
	<acme:input-textbox code="authenticated.manager.list.label.destinationCity" path="destinationCity" readonly="true"/>
	<acme:input-integer code="authenticated.manager.list.label.numberOfLayovers" path="numberOfLayovers" readonly="true"/>
	<acme:button code="authenticated.manager.list.button.legs" action="/manager/leg/list?flightId=${id}"/>
</acme:form>