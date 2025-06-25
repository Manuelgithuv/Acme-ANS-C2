<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<acme:form>
    <acme:input-moment code="authenticated.assistance-agent.list.label.registrationMoment" path="registrationMoment" readonly="true"/>
    <acme:input-textbox code="authenticated.assistance-agent.list.label.passengerEmail" path="passengerEmail" />
    <acme:input-textbox code="authenticated.assistance-agent.list.label.description" path="description" />
    <acme:input-textbox code="authenticated.assistance-agent.list.label.status" path="status" readonly="true"/>
    <acme:input-select code="authenticated.assistance-agent.list.label.type" path="type" choices="${types}"/>
    <acme:input-checkbox code="authenticated.assistance-agent.list.label.published" path="published" readonly="true"/>
    <acme:input-select code="authenticated.assistance-agent.list.label.leg" path="legId" choices="${legIds}"/>
    <acme:button code="administrator.logs.form.button.list" action="/administrator/claim-tracking-log/list?claimId=${id}"/>
</acme:form>