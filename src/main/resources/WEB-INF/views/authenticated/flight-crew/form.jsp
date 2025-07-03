<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<acme:form>
	<acme:input-textbox code="authenticated.flight_crew.form.label.identifier" path="identifier"/>
	<acme:input-textbox code="authenticated.flight_crew.form.label.phone" path="phone"/>
	<acme:input-textarea code="authenticated.flight_crew.form.label.languageSkills" path="languageSkills"/>
	<acme:input-select code="authenticated.flight_crew.form.label.availability" path="availability" choices="${availabilities}"/>
	<acme:input-integer code="authenticated.flight_crew.form.label.experience" path="experience"/>
	<acme:input-textbox code="authenticated.flight_crew.form.label.salary" path="salary"/>
	<acme:input-select code="authenticated.flight_crew.form.label.airline" path="airline" choices="${airlines}"/>
	<jstl:if test="${_command == 'create'}">
		<acme:submit  code="authenticated.flight_crew.form.button.create" action="/authenticated/flight_crew/create"/>
	</jstl:if>
	<jstl:if test="${_command == 'update'}">
		<acme:submit code="authenticated.flight_crew.form.button.update" action="/authenticated/flight_crew/update"/>
	</jstl:if>
</acme:form>
