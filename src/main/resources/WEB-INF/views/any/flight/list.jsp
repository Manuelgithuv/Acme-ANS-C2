<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="any.flight.list.label.tag" path="tag"/>
	<acme:list-column code="any.flight.list.label.indication" path="indication"/>
	<acme:list-column code="any.flight.list.label.cost" path="cost"/>
	<acme:list-column code="any.flight.list.label.description" path="description"/>
	<acme:list-column code="any.manager.list.flight.label.managerIdentity" path="manager.identity.fullName"/>
</acme:list>