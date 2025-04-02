<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code ="technician.maintenance-record.list.label.moment" path ="moment" width ="15%"/>
	<acme:list-column code ="technician.maintenance-record.list.label.status" path ="status" width ="15%"/>
	<acme:list-column code ="technician.maintenance-record.list.label.inspectionDueDate" path ="inspectionDueDate" width ="15%"/>
	<acme:list-column code ="technician.maintenance-record.list.label.estimatedCost" path ="estimatedCost" width ="15%"/>
	<acme:list-payload path="payload"/>	
</acme:list>