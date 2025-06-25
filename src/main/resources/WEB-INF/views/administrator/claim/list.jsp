<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.assistanceAgent.list.label.registrationMoment" path="registrationMoment"/>
	<acme:list-column code="authenticated.assistanceAgent.list.label.passengerEmail" path="passengerEmail"/>
	<acme:list-column code="authenticated.assistanceAgent.list.label.status" path="status"/>
	<acme:list-column code="authenticated.assistanceAgent.list.label.published" path="published"/>
	<acme:list-column code="authenticated.assistanceAgent.list.label.leg" path="legId"/>
    <acme:list-payload path="payload"/>
</acme:list>