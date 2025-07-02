<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
    <acme:list-column code="authenticated.assistanceAgent.list.label.claim" path="claim"/>
	<acme:list-column code="authenticated.assistanceAgent.list.label.lastUpdateMoment" path="lastUpdateMoment"/>
	<acme:list-column code="authenticated.assistanceAgent.list.label.resolutionPercentage" path="resolutionPercentage"/>
	<acme:list-column code="authenticated.assistanceAgent.list.label.published" path="published"/>
</acme:list>
<jstl:if test="${_command == 'list'}">
	<acme:button code="authenticated.assistanceAgent.list.button.booking.create" action="/assistance-agent/claim-tracking-log/create"/>
</jstl:if>