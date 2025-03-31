<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code ="technician.task.list.label.type" path ="type" width ="15%"/>
	<acme:list-column code ="technician.task.list.label.estimatedDuration" path ="estimatedDuration" width ="15%"/>
	<acme:list-column code ="technician.task.list.label.priority" path ="priority" width ="15%"/>
	<acme:list-column code ="technician.task.list.label.draftMode" path ="draftMode" width ="15%"/>
	<acme:list-column code ="technician.task.list.label.description" path ="description" width ="15%"/>
	<acme:list-payload path="payload"/>	
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="technician.task.list.button.create" action="/technician/task/create"/>
</jstl:if>

<jstl:if test="${_command != 'list' && isMaintenanceRecordDraftMode == true}">
	<acme:button code="technician.task.list.button.createTask" action="/technician/task/createByMaintenanceRecord?maintenanceRecordId=${maintenanceRecordId}"/>
	<acme:button code="technician.task.list.button.addTask" action="/technician/task/addTask?maintenanceRecordId=${maintenanceRecordId}"/>
</jstl:if>