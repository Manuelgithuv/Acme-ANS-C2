<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<form method="get">
    <label>
        <input type="checkbox" name="ongoing" value="true" ${param.ongoing == 'true' ? 'checked' : ''} onchange="this.form.submit()" />
        'PENDING'
    </label>
</form>

<acme:list>
	<acme:list-column code="authenticated.assistanceAgent.list.label.claimNameId" path="claimNameId"/>
	<acme:list-column code="authenticated.assistanceAgent.list.label.registrationMoment" path="registrationMoment"/>
	<acme:list-column code="authenticated.assistanceAgent.list.label.status" path="status"/>
	<acme:list-column code="authenticated.assistanceAgent.list.label.published" path="published"/>
	<acme:list-column code="authenticated.assistanceAgent.list.label.leg" path="legId"/>
	<acme:list-payload path="payload"/>
</acme:list>
<jstl:if test="${_command == 'list'}">
	<acme:button code="authenticated.assistanceAgent.list.button.booking.create" action="/assistance-agent/claim/create"/>
</jstl:if>