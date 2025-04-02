<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list navigable="false">
	<acme:list-column code ="technician.task.list.label.type" path ="type" width ="15%"/>
	<acme:list-column code ="technician.task.list.label.estimatedDuration" path ="estimatedDuration" width ="15%"/>
	<acme:list-column code ="technician.task.list.label.priority" path ="priority" width ="15%"/>
	<acme:list-column code ="technician.task.list.label.description" path ="description" width ="15%"/>
	<acme:list-payload path="payload"/>	
</acme:list>