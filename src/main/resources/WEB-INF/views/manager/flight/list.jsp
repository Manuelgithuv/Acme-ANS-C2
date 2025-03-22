<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.manager.list.label.tag" path="tag"/>
	<acme:list-column code="authenticated.manager.list.label.indication" path="indication"/>
	<acme:list-column code="authenticated.manager.list.label.cost" path="cost"/>
	<acme:list-column code="authenticated.manager.list.label.published" path="published"/>
	<acme:list-column code="authenticated.manager.list.label.description" path="description"/>
	<acme:list-column code="authenticated.manager.list.label.identity" path="manager.identity.fullName"/>
</acme:list>
<jstl:if test="${_command == 'list'}">
	<acme:button code="authenticated.manager.list.button.flight.create" action="/manager/flight/create"/>
</jstl:if>	