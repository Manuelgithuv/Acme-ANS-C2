<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list navigable="false">
	<acme:list-column sortable="false" code="authenticated.administrator.list.label.fullName" path="fullName"/>
	<acme:list-column sortable="true" code="authenticated.administrator.list.label.email" path="email"/>
	<acme:list-column sortable="false" code="authenticated.administrator.list.label.passportNumber" path="passportNumber"/>
	<acme:list-column sortable="false" code="authenticated.administrator.list.label.dateOfBirth" path="dateOfBirth"/>
	<acme:list-column sortable="false" code="authenticated.administrator.list.label.specialNeeds" path="specialNeeds"/>
</acme:list>
	