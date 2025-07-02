<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:print code="assistance-agent.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.resolvedClaimsRatio"/>
		</th>
		<td>
			<acme:print value="${resolvedClaimsRatio}"/>
		</td>
	</tr>
    <tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.rejectedClaimsRatio"/>
		</th>
		<td>
			<acme:print value="${rejectedClaimsRatio}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.topThreeMonthsWithMostClaim"/>
		</th>
		<td>
			<acme:print value="${topThreeMonthsWithMostClaims}"/>
		</td>
	</tr>
    <tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.averageLogsPerClaim"/>
		</th>
		<td>
			<acme:print value="${averageLogsPerClaim}"/>
		</td>
	</tr>
    <tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.minimumLogsPerClaim"/>
		</th>
		<td>
			<acme:print value="${minimumLogsPerClaim}"/>
		</td>
	</tr>
    <tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.maximumLogsPerClaim"/>
		</th>
		<td>
			<acme:print value="${maximumLogsPerClaim}"/>
		</td>
	</tr>
    <tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.standardDeviationLogsPerClaim"/>
		</th>
		<td>
			<acme:print value="${standardDeviationLogsPerClaim}"/>
		</td>
	</tr>
    <tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.averageClaimsLastMonth"/>
		</th>
		<td>
			<acme:print value="${averageClaimsLastMonth}"/>
		</td>
	</tr>
    <tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.minimumClaimsLastMonth"/>
		</th>
		<td>
			<acme:print value="${minimumClaimsLastMonth}"/>
		</td>
	</tr>
    <tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.maximumClaimsLastMonth"/>
		</th>
		<td>
			<acme:print value="${maximumClaimsLastMonth}"/>
		</td>
	</tr>
    <tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.standardDeviationClaimsLastMonth"/>
		</th>
		<td>
			<acme:print value="${standardDeviationClaimsLastMonth}"/>
		</td>
	</tr>
		
</table>