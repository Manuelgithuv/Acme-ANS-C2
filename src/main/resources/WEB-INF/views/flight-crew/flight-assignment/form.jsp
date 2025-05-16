<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-select code="authenticated.crew.label.duty" path="duty" choices="${duties}"/>
	<acme:input-textbox code="authenticated.crew.label.lastUpdate" path="lastUpdate" placeholder="crew.lastUpdate.placeholder" />
	<acme:input-select code="authenticated.crew.label.status" path="status" choices="${statuses}"/>
	<acme:input-textarea code="authenticated.crew.label.remarks" path="remarks" placeholder="crew.remarks.placeholder" />
	<acme:input-select code="authenticated.crew.label.leg" path="leg" choices="${legs}"/>
	<acme:input-select code="authenticated.crew.label.assignee" path="assignee" choices="${assignees}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && authorised }">
			<acme:submit code="authenticated.crew.button.delete" action="/flight-crew/flight-assignment/delete"/>
			<acme:submit code="authenticated.crew.button.update" action="/flight-crew/flight-assignment/update"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && canPublish }">
			<acme:submit code="authenticated.crew.button.publish" action="/flight-crew/flight-assignment/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="authenticated.crew.button.create" action="/flight-crew/flight-assignment/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>	
	
