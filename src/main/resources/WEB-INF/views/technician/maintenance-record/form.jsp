<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<acme:form>
	
	<acme:input-select code = "technician.maintenance-record.form.label.status" path = "status" choices="${status}"/>
	<acme:input-moment code = "technician.maintenance-record.form.label.inspectionDueDate" path = "inspectionDueDate"/>
	<acme:input-money code = "technician.maintenance-record.form.label.estimatedCost" path = "estimatedCost"/>
	<acme:input-textbox code = "technician.maintenance-record.form.label.notes" path = "notes"/>
	<acme:input-select code = "technician.maintenance-record.form.label.aircraft" path = "aircraft" choices="${aircraft}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<jstl:if test="${_command == 'show'}">
				<acme:button code="technician.maintenance-record.form.button.listByMaintenanceRecord" action="/technician/task/listByMaintenanceRecord?maintenanceRecordId=${id}"/>
			</jstl:if>
			<acme:submit code="technician.maintenance-record.form.button.publish" action="/technician/maintenance-record/publish?id=${id}"/>
			<acme:submit code="technician.maintenance-record.form.button.update" action="/technician/maintenance-record/update?id=${id}"/>	
			<acme:submit code="technician.maintenance-record.form.button.delete" action="/technician/maintenance-record/delete?id=${id}"/>	
		</jstl:when>
		<jstl:when test="${_command == 'show' && draftMode== false}">
				<acme:button code="technician.maintenance-record.form.button.listByMaintenanceRecord" action="/technician/task/listByMaintenanceRecord?maintenanceRecordId=${id}"/>
		</jstl:when>
		<jstl:when  test="${acme:anyOf(_command,'create')}">
			<acme:submit code="technician.maintenance-record.form.button.create" action="/technician/maintenance-record/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>