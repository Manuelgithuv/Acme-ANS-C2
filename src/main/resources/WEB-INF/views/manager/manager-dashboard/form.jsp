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
	<acme:print code="manager.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.rankingPosition"/>
		</th>
		<td>
			<acme:print value="${rankingPosition}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.average-yearsToRetire"/>
		</th>
		<td>
			<acme:print value="${yearsToRetire}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.ratioOnTimeLegs"/>
		</th>
		<td>
			<acme:print value="${empty ratioOnTimeLegs ? 'NaN' : ratioOnTimeLegs}"/>

		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.ratioDelayedLegs"/>
		</th>
		<td>
			<acme:print value="${empty ratioDelayedLegs ? 'NaN' : ratioDelayedLegs}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.avgDesviationOfCost"/>
		</th>
		<td>
			<acme:print value="${empty avgDesviationOfCost ? 'NaN' : avgDesviationOfCost}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.minDesviationOfCost"/>
		</th>
		<td>
			<acme:print value="${empty minDesviationOfCost ? 'NaN' : minDesviationOfCost}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.maxDesviationOfCost"/>
		</th>
		<td>
			<acme:print value="${empty maxDesviationOfCost ? 'NaN' : maxDesviationOfCost}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.standardDesviationOfCost"/>
		</th>
		<td>
			<acme:print value="${empty standardDesviationOfCost ? 'NaN' : standardDesviationOfCost}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.mostPopularAirport"/>
		</th>
		<td>
			<acme:print value="${empty mostPopularAirport ? 'Null' : mostPopularAirport}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.leastPopularAirport"/>
		</th>
		<td>
			<acme:print value="${empty leastPopularAirport ? 'Null' : leastPopularAirport}"/>
		</td>
	</tr>	
</table>

<h2>
	<acme:print code="manager.dashboard.form.title.leg-statuses"/>
</h2>

<div>
	<canvas id="canvas"></canvas>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		var data = {
			labels : [
					"ON_TIME", "DELAYED", "LANDED", "CANCELLED"
			],
			datasets : [
				{
					data : [
						<jstl:out value="${numberOfLegsOnTime}"/>, 
						<jstl:out value="${numberOfLegsDelayed}"/>, 
						<jstl:out value="${numberOfLegsLanded}"/>,
						<jstl:out value="${numberOfLegsCancelled}"/>
					]
				}
			]
		};
		var options = {
			scales : {
				yAxes : [
					{
						ticks : {
							suggestedMin : 0,
							suggestedMax : 15
						}
					}
				]
			},
			legend : {
				display : false
			}
		};
	
		var canvas, context;
	
		canvas = document.getElementById("canvas");
		context = canvas.getContext("2d");
		new Chart(context, {
			type : "bar",
			data : data,
			options : options
		});
	});
</script>

<acme:return/>

