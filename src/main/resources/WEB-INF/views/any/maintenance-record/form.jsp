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
		<jstl:when test="${_command == 'show' && draftMode== false}">
				<acme:button code="any.maintenance-record.form.button.list" action="/any/task/list?id=${id}"/>
		</jstl:when>
	</jstl:choose>
</acme:form>