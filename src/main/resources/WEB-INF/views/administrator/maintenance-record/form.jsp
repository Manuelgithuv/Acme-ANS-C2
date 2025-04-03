<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<acme:form>
	<acme:input-select code = "administrator.maintenance-record.form.label.status" path = "status" choices="${status}"/>
	<acme:input-moment code = "administrator.maintenance-record.form.label.inspectionDueDate" path = "inspectionDueDate"/>
	<acme:input-money code = "administrator.maintenance-record.form.label.estimatedCost" path = "estimatedCost"/>
	<acme:input-textbox code = "administrator.maintenance-record.form.label.notes" path = "notes"/>
	<acme:input-select code = "administrator.maintenance-record.form.label.aircraft" path = "aircraft" choices="${aircraft}"/>
	<acme:button code="administrator.task.form.button.list" action="/administrator/task/list?maintenanceRecordId=${id}"/>
</acme:form>