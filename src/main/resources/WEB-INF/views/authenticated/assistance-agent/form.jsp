<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<acme:form>
	<acme:input-textbox code="authenticated.assistance-agent.form.label.employeCode" path="employeCode"/>
	<acme:input-textbox code="authenticated.assistance-agent.form.label.spokenLanguages" path="spokenLanguages"/>
	<acme:input-moment code="authenticated.assistance-agent.form.label.dateStartingWorking" path="dateStartingWorking"/>
	<acme:input-textbox code="authenticated.assistance-agent.form.label.bio" path="bio"/>
	<acme:input-integer code="authenticated.assistance-agent.form.label.salary" path="salary"/>
	<acme:input-textarea code="authenticated.assistance-agent.form.label.picture" path="picture"/>
	<acme:input-select code="authenticated.assistance-agent.list.label.airline" path="airlineCode" choices="${airlineCodes}"/>
	<jstl:if test="${_command == 'create'}">
		<acme:submit  code="authenticated.assistance-agent.form.button.create" action="/authenticated/assistance-agent/create"/>
	</jstl:if>
	<jstl:if test="${_command == 'update'}">
		<acme:submit code="authenticated.assistance-agent.form.button.update" action="/authenticated/assistance-agent/update"/>
	</jstl:if>
</acme:form>