<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<%-- 
<acme:form>
	<acme:input-textbox code="administrator.airline.form.label.name" path="name"/>	
	<acme:input-textbox code="administrator.airline.form.label.iataCode" path="iataCode"/>
	<acme:input-textbox code="administrator.airline.form.label.webSite" path="webSite"/>
	<acme:input-textbox code="administrator.airline.form.label.foundationMoment" path="foundationMoment"/>
	<acme:input-select code="administrator.airline.form.label.type" path="type" choices= "${types}"/>
	<acme:input-textbox code="administrator.airline.form.label.email" path="email"/>
	<acme:input-textbox code="administrator.airline.form.label.phoneNumber" path="phoneNumber"/>
	<%-- 
	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update')}">
			<acme:input-checkbox code="administrator.airline.form.label.confirmation" path="confirmation"/>
			<acme:submit code="administrator.airline.form.button.update" action="/administrator/airline/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="administrator.airline.form.label.confirmation" path="confirmation"/>
			<acme:submit code="administrator.airline.form.button.create" action="/administrator/airline/create"/>
		</jstl:when>		
	</jstl:choose>	
	y
</acme:form>
--%>