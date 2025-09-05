<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>HP GST | Dashboard</title>

	<!-- AdminLTE 4 (Bootstrap 5) + FontAwesome -->
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
	<link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.5.2/css/all.min.css" rel="stylesheet">
	<link href="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/css/adminlte.min.css" rel="stylesheet">

	<!-- DataTables (Bootstrap 5 build) + Buttons -->
	<link href="https://cdn.datatables.net/v/bs5/dt-2.0.8/r-3.0.2/b-3.0.2/fh-4.0.1/datatables.min.css" rel="stylesheet"/>

	<!-- (kept) FusionCharts -->
	<script type="text/javascript" src="https://cdn.fusioncharts.com/fusioncharts/latest/fusioncharts.js"></script>
	<script type="text/javascript" src="https://cdn.fusioncharts.com/fusioncharts/latest/themes/fusioncharts.theme.fusion.js"></script>

	<!-- (kept) Google Charts -->
	<script src="https://www.gstatic.com/charts/loader.js"></script>

	<style>
		.small-kpi .card-body { min-height: 110px; }
		.chart-box { height: 400px; }
	</style>
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

	<!-- Header -->
	<nav class="app-header navbar navbar-expand bg-body shadow-sm">
		<div class="container-fluid">
			<a class="navbar-brand d-flex align-items-center" href="/hq/dashboard">
				<i class="fa-solid fa-scale-balanced me-2"></i>
				<span>HP GST</span>
			</a>
			<ul class="navbar-nav ms-auto">
				<li class="nav-item d-none d-sm-inline-block"><a class="nav-link" href="/hq/dashboard">Home</a></li>
			</ul>
		</div>
	</nav>

	<!-- Sidebar -->
	<aside class="app-sidebar bg-body-secondary shadow" data-bs-theme="light">
		<div class="sidebar-brand">
			<a href="/hq/dashboard" class="brand-link">
				<img src="/static/dist/img/AdminLTELogo.png" alt="Logo" class="brand-image opacity-75 shadow">
				<span class="brand-text fw-light">Dashboard</span>
			</a>
		</div>
		<div class="sidebar-wrapper">
			<nav class="mt-2">
				<ul class="nav sidebar-menu flex-column" role="menu">
					<li class="nav-item">
						<a href="/hq/dashboard" class="nav-link active">
							<i class="nav-icon fa-solid fa-gauge-high"></i>
							<p>Dashboard</p>
						</a>
					</li>
					<!-- add more items if needed -->
				</ul>
			</nav>
		</div>
	</aside>

	<!-- Main -->
	<main class="app-main">
		<div class="app-content-header">
			<div class="container-fluid">
				<div class="row">
					<div class="col-sm-6">
						<h3 class="mb-0">Dashboard</h3>
					</div>
					<div class="col-sm-6">
						<ol class="breadcrumb float-sm-end mb-0">
							<li class="breadcrumb-item"><a href="/hq/dashboard">Home</a></li>
							<li class="breadcrumb-item active">Dashboard</li>
						</ol>
					</div>
				</div>
			</div>
		</div>

		<div class="app-content">
			<div class="container-fluid">

				<!-- KPI cards (replacing small-box, same data) -->
				<div class="row g-3">
					<div class="col-lg-3 col-6">
						<div class="card small-kpi text-white bg-info">
							<div class="card-body d-flex justify-content-between align-items-center">
								<div>
									<h3 class="mb-1">${dashBoardTotalCases}</h3>
									<p class="mb-0 small">Total Cases</p>
								</div>
								<i class="ion ion-bag fa-2x opacity-75"></i>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-6">
						<div class="card small-kpi text-white bg-success">
							<div class="card-body d-flex justify-content-between align-items-center">
								<div>
									<h3 class="mb-1">${dashBoardTotalAcknowledgedCases}</h3>
									<p class="mb-0 small">Cases Acknowledged</p>
								</div>
								<i class="ion ion-stats-bars fa-2x opacity-75"></i>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-6">
						<div class="card small-kpi text-white bg-primary">
							<div class="card-body d-flex justify-content-between align-items-center">
								<div>
									<h3 class="mb-1">${dashBoardTotalInitiatedCases}</h3>
									<p class="mb-0 small">Cases Initiated</p>
								</div>
								<i class="ion ion-stats-bars fa-2x opacity-75"></i>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-6">
						<div class="card small-kpi text-dark bg-warning">
							<div class="card-body d-flex justify-content-between align-items-center">
								<div>
									<h3 class="mb-1">${dashBoardTotalCasesClosedByFo}</h3>
									<p class="mb-0 small">Cases Closed by FO</p>
								</div>
								<i class="ion ion-person-add fa-2x opacity-75"></i>
							</div>
						</div>
					</div>

					<div class="col-lg-3 col-6">
						<div class="card small-kpi text-white bg-danger">
							<div class="card-body d-flex justify-content-between align-items-center">
								<div>
									<h3 class="mb-1">${dashBoardTotalSuspectedIndicativeAmount}</h3>
									<p class="mb-0 small">Total Suspected Indicative Amount</p>
								</div>
								<i class="ion ion-pie-graph fa-2x opacity-75"></i>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-6">
						<div class="card small-kpi text-white bg-dark">
							<div class="card-body d-flex justify-content-between align-items-center">
								<div>
									<h3 class="mb-1">${dashBoardTotalAmount}</h3>
									<p class="mb-0 small">Total Amount</p>
								</div>
								<i class="ion ion-pie-graph fa-2x opacity-75"></i>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-6">
						<div class="card small-kpi text-white bg-secondary">
							<div class="card-body d-flex justify-content-between align-items-center">
								<div>
									<h3 class="mb-1">${dashBoardTotalDemand}</h3>
									<p class="mb-0 small">Total Demand</p>
								</div>
								<i class="ion ion-pie-graph fa-2x opacity-75"></i>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-6">
						<div class="card small-kpi text-dark bg-light">
							<div class="card-body d-flex justify-content-between align-items-center">
								<div>
									<h3 class="mb-1">${dashBoardTotalRecovery}</h3>
									<p class="mb-0 small">Total Recovery</p>
								</div>
								<i class="ion ion-pie-graph fa-2x opacity-75"></i>
							</div>
						</div>
					</div>
				</div>

				<!-- Filters (same form/ids/logic, just BS5 classes) -->
				<div class="card card-body mt-3">
					<form method="GET" action="dashboard" name="dashboard" id="dashboard" modelAttribute="dashboard">
						<div class="row">
							<div class="col-md-11 row">
								<div class="col-md-3">
									<div class="mb-3">
										<label class="form-label">Category<span style="color: red;"> *</span></label>
										<select id="category" name="category" class="form-select" required>
											<option value="">Please Select</option>
											<c:forEach items="${categories}" var="var">
												<c:choose>
													<c:when test="${var.id eq category}">
														<option value="${var.id}" selected="selected">${var.name}</option>
													</c:when>
													<c:otherwise>
														<option value="${var.id}">${var.name}</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="col-md-3">
									<div class="mb-3">
										<label class="form-label">View<span style="color: red;"> *</span></label>
										<select id="view" name="view" class="form-select" required>
											<option value="">Please Select</option>
											<option value="circleWise" <c:if test="${viewtype eq 'circleWise'}">selected</c:if>>Circle-wise</option>
											<!-- <option value="zoneWise" <c:if test="${viewtype eq 'zoneWise'}">selected</c:if>>Zone-wise</option> -->
										</select>
									</div>
								</div>
								<div class="col-md-3">
									<div class="mb-3">
										<label class="form-label">Financial Year</label>
										<select id="financialyear" name="financialyear" class="form-select">
											<option value="">Please Select</option>
											<c:forEach items="${financialyear}" var="financialyear">
												<option value="${financialyear}" <c:if test="${financialyear == year}">selected</c:if>>${financialyear}</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="col-md-3">
									<div class="mb-3">
										<label class="form-label" for="parameter">Paramter</label>
										<select id="parameter" name="parameter" class="form-select" title="Please Select the Parameter">
											<option value="0">Please Select</option>
											<c:forEach items="${parameterList}" var="parameter">
												<c:choose>
													<c:when test="${parameter.id eq parameterId}">
														<option value="${parameter.id}" selected="selected">${parameter.paramName}</option>
													</c:when>
													<c:otherwise>
														<option value="${parameter.id}">${parameter.paramName}</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-1">
								<div class="text-center">
									<button type="submit" class="btn btn-primary" style="margin-top: 30px; float: right;">Submit</button>
								</div>
							</div>
						</div>
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					</form>

					<!-- circleWise table (unchanged data/logic) -->
					<c:if test="${not empty viewtype && viewtype == 'circleWise'}">
						<div class="row">
							<table id="example1" class="table table-bordered table-striped">
								<thead style="background-color: #C0C0C0">
									<tr>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">Circle</th>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">No. of Cases</th>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">Suspected Indicative Tax
											Value (₹)</th>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">Demand Created (₹)</th>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">Amount Recovered (₹)
										</th>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">Yet to be
											Acknowledge</th>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">Yet to be
											Initiated</th>
										<!-- <th style="text-align: center; vertical-align: middle;" rowspan="2">Total Closed</th> -->
										<th style="text-align: center; vertical-align: middle;" colspan="7">Case Stage</th>
									</tr>
									<tr>
										<th style="text-align: center; vertical-align: middle;">DRC-01A
											issued</th>
										<th style="text-align: center; vertical-align: middle;">ASMT-10
											issued</th>
										<th style="text-align: center; vertical-align: middle;">DRC01
											issued</th>
										<th style="text-align: center; vertical-align: middle;">Demand
											Created via DRC07</th>
										<th style="text-align: center; vertical-align: middle;">Case
											Dropped</th>
										<th style="text-align: center; vertical-align: middle;">Partial
											Voluntary Payment Remaining Demand</th>
										<th style="text-align: center; vertical-align: middle;">Demand
											Created However Discharged via DRC-03</th>
									</tr>
								</thead>
								<tbody>
									<c:set var="totalnoOfCase" value="${0}" />
									<c:set var="totalindicativeValue" value="${0}" />
									<c:set var="totaldemand" value="${0}" />
									<c:set var="totalrecoveredAmount" value="${0}" />
									<c:set var="totaldrc01aissued" value="${0}" />
									<c:set var="totalamst10issued" value="${0}" />
									<c:set var="totaldrc01issued" value="${0}" />
									<c:set var="totalnotAcknowledege" value="${0}" />
									<c:set var="totaldemandByDrc07" value="${0}" />
									<c:set var="totalcaseDroped" value="${0}" />
									<c:set var="totalnotApplicable" value="${0}" />
									<c:set var="totalpartialVoluentryDemand" value="${0}" />
									<c:set var="totaldemandDischageByDrc03" value="${0}" />
									<!-- <c:set var="totalClosed" value="${0}" /> -->
									<c:forEach items="${circleList}" var="circleList">
										<tr>
											<td>${circleList.juristiction}</td>
											<td>${circleList.noOfCase}</td>
											<c:set var="totalnoOfCase" value="${totalnoOfCase + circleList.noOfCase}" />
											<c:set var="totalindicativeValue" value="${totalindicativeValue + circleList.indicativeValue}" />
											<c:set var="totaldemand" value="${totaldemand + circleList.demand}" />
											<c:set var="totalrecoveredAmount" value="${totalrecoveredAmount + circleList.recoveredAmount}" />
											<c:set var="totaldrc01aissued" value="${totaldrc01aissued + circleList.drc01aissued}" />
											<c:set var="totalamst10issued" value="${totalamst10issued + circleList.amst10issued}" />
											<c:set var="totaldrc01issued" value="${totaldrc01issued + circleList.drc01issued}" />
											<c:set var="totalnotAcknowledege" value="${totalnotAcknowledege + circleList.notAcknowledege}" />
											<c:set var="totaldemandByDrc07" value="${totaldemandByDrc07 + circleList.demandByDrc07}" />
											<c:set var="totalcaseDroped" value="${totalcaseDroped + circleList.caseDroped}" />
											<c:set var="totalnotApplicable" value="${totalnotApplicable + circleList.notApplicable}" />
											<c:set var="totalpartialVoluentryDemand" value="${totalpartialVoluentryDemand + circleList.partialVoluentryDemand}" />
											<c:set var="totaldemandDischageByDrc03" value="${totaldemandDischageByDrc03 + circleList.demandDischageByDrc03}" />
											<td>${circleList.indicativeValue}</td>
											<td>${circleList.demand}</td>
											<td>${circleList.recoveredAmount}</td>
											<td>${circleList.notAcknowledege}</td>
											<td>${circleList.notApplicable}</td>
											<!-- <td>${circleList.totalClosed}</td> -->
											<td>${circleList.drc01aissued}</td>
											<td>${circleList.amst10issued}</td>
											<td>${circleList.drc01issued}</td>
											<td>${circleList.demandByDrc07}</td>
											<td>${circleList.caseDroped}</td>
											<td>${circleList.partialVoluentryDemand}</td>
											<td>${circleList.demandDischageByDrc03}</td>
										</tr>
									</c:forEach>
								</tbody>
								<tbody>
									<tr>
										<td>Grand Total</td>
										<td>${totalnoOfCase}</td>
										<td>${totalindicativeValue}</td>
										<td>${totaldemand}</td>
										<td>${totalrecoveredAmount}</td>
										<td>${totalnotAcknowledege}</td>
										<td>${totalnotApplicable}</td>
										<!-- <td>${totalClosed}</td> -->
										<td>${totaldrc01aissued}</td>
										<td>${totalamst10issued}</td>
										<td>${totaldrc01issued}</td>
										<td>${totaldemandByDrc07}</td>
										<td>${totalcaseDroped}</td>
										<td>${totalpartialVoluentryDemand}</td>
										<td>${totaldemandDischageByDrc03}</td>
									</tr>
								</tbody>
							</table>
						</div>
					</c:if>

					<br><br>

					<!-- default (empty viewtype) table block (unchanged) -->
					<c:if test="${empty viewtype}">
						<div class="row">
							<table id="example1" class="table table-bordered table-striped">
								<thead style="background-color: #C0C0C0">
									<tr>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">Circle</th>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">No. of Cases</th>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">Suspected Indicative
											TaxValue (₹)</th>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">Demand Created (₹)</th>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">Amount Recovered (₹)
										</th>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">Yet to be Acknowledge
										</th>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">Yet to be Initiated</th>
										<!-- 	<th style="text-align: center; vertical-align: middle;" rowspan="2">Total Closed</th> -->
										<th style="text-align: center; vertical-align: middle;" colspan="7">Case Stage</th>
									</tr>
									<tr>
										<th style="text-align: center; vertical-align: middle;">DRC-01A
											issued</th>
										<th style="text-align: center; vertical-align: middle;">ASMT-10
											issued</th>
										<th style="text-align: center; vertical-align: middle;">DRC01
											issued</th>
										<th style="text-align: center; vertical-align: middle;">Demand
											Created via DRC07</th>
										<th style="text-align: center; vertical-align: middle;">Case
											Dropped</th>
										<th style="text-align: center; vertical-align: middle;">Partial
											Voluntary Payment Remaining Demand</th>
										<th style="text-align: center; vertical-align: middle;">Demand
											Created However Discharged via DRC-03</th>
									</tr>
								</thead>
								<tbody>
									<c:set var="totalnoOfCase" value="${0}" />
									<c:set var="totalindicativeValue" value="${0}" />
									<c:set var="totaldemand" value="${0}" />
									<c:set var="totalrecoveredAmount" value="${0}" />
									<c:set var="totaldrc01aissued" value="${0}" />
									<c:set var="totalamst10issued" value="${0}" />
									<c:set var="totaldrc01issued" value="${0}" />
									<c:set var="totalnotAcknowledege" value="${0}" />
									<c:set var="totaldemandByDrc07" value="${0}" />
									<c:set var="totalcaseDroped" value="${0}" />
									<c:set var="totalnotApplicable" value="${0}" />
									<c:set var="totalpartialVoluentryDemand" value="${0}" />
									<c:set var="totaldemandDischageByDrc03" value="${0}" />
									<!-- <c:set var="totalClosed" value="${0}" /> -->
									<c:forEach items="${circleList}" var="circleList">
										<tr>
											<td>${circleList.juristiction}</td>
											<td>${circleList.noOfCase}</td>
											<c:set var="totalnoOfCase" value="${totalnoOfCase + circleList.noOfCase}" />
											<c:set var="totalindicativeValue" value="${totalindicativeValue + circleList.indicativeValue}" />
											<c:set var="totaldemand" value="${totaldemand + circleList.demand}" />
											<c:set var="totalrecoveredAmount" value="${totalrecoveredAmount + circleList.recoveredAmount}" />
											<c:set var="totaldrc01aissued" value="${totaldrc01aissued + circleList.drc01aissued}" />
											<c:set var="totalamst10issued" value="${totalamst10issued + circleList.amst10issued}" />
											<c:set var="totaldrc01issued" value="${totaldrc01issued + circleList.drc01issued}" />
											<c:set var="totalnotAcknowledege" value="${totalnotAcknowledege + circleList.notAcknowledege}" />
											<c:set var="totaldemandByDrc07" value="${totaldemandByDrc07 + circleList.demandByDrc07}" />
											<c:set var="totalcaseDroped" value="${totalcaseDroped + circleList.caseDroped}" />
											<c:set var="totalnotApplicable" value="${totalnotApplicable + circleList.notApplicable}" />
											<c:set var="totalpartialVoluentryDemand" value="${totalpartialVoluentryDemand + circleList.partialVoluentryDemand}" />
											<c:set var="totaldemandDischageByDrc03" value="${totaldemandDischageByDrc03 + circleList.demandDischageByDrc03}" />
											<td>${circleList.indicativeValue}</td>
											<td>${circleList.demand}</td>
											<td>${circleList.recoveredAmount}</td>
											<td>${circleList.notAcknowledege}</td>
											<td>${circleList.notApplicable}</td>
											<!-- <td>${circleList.totalClosed}</td> -->
											<td>${circleList.drc01aissued}</td>
											<td>${circleList.amst10issued}</td>
											<td>${circleList.drc01issued}</td>
											<td>${circleList.demandByDrc07}</td>
											<td>${circleList.caseDroped}</td>
											<td>${circleList.partialVoluentryDemand}</td>
											<td>${circleList.demandDischageByDrc03}</td>
										</tr>
									</c:forEach>
								</tbody>
								<tbody>
									<tr>
										<td>Grand Total</td>
										<td>${totalnoOfCase}</td>
										<td>${totalindicativeValue}</td>
										<td>${totaldemand}</td>
										<td>${totalrecoveredAmount}</td>
										<td>${totalnotAcknowledege}</td>
										<td>${totalnotApplicable}</td>
										<td>${totaldrc01aissued}</td>
										<td>${totalamst10issued}</td>
										<td>${totaldrc01issued}</td>
										<td>${totaldemandByDrc07}</td>
										<td>${totalcaseDroped}</td>
										<td>${totalpartialVoluentryDemand}</td>
										<td>${totaldemandDischageByDrc03}</td>
									</tr>
								</tbody>
							</table>
						</div>
					</c:if>

					<!-- zoneWise block (unchanged data/logic) -->
					<c:if test="${not empty viewtype && viewtype == 'zoneWise'}">
						<div class="row">
							<table id="example1" class="table table-bordered table-striped">
								<thead style="background-color: #C0C0C0">
									<tr>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">Zone</th>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">No. of Cases</th>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">Suspected Indicative Tax
											Value (₹)</th>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">Demand Created (₹)</th>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">Amount Recovered (₹)
										</th>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">Yet to be Acknowledge
										</th>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">Yet to be Initiated</th>
										<th style="text-align: center; vertical-align: middle;" colspan="7">Case Stage</th>
									</tr>
									<tr>
										<th style="text-align: center; vertical-align: middle;">DRC-01A
											issued</th>
										<th style="text-align: center; vertical-align: middle;">ASMT-10
											issued</th>
										<th style="text-align: center; vertical-align: middle;">DRC01
											issued</th>
										<th style="text-align: center; vertical-align: middle;">Demand
											Created via DRC07</th>
										<th style="text-align: center; vertical-align: middle;">Case
											Dropped</th>
										<th style="text-align: center; vertical-align: middle;">Partial
											Voluntary Payment Remaining Demand</th>
										<th style="text-align: center; vertical-align: middle;">Demand
											Created However Discharged via DRC-03</th>
									</tr>
								</thead>
								<tbody>
									<c:set var="totalnoOfCaseZone" value="${0}" />
									<c:set var="totalindicativeValueZone" value="${0}" />
									<c:set var="totaldemandZone" value="${0}" />
									<c:set var="totalrecoveredAmountZone" value="${0}" />
									<c:set var="totaldrc01aissuedZone" value="${0}" />
									<c:set var="totalamst10issuedZone" value="${0}" />
									<c:set var="totaldrc01issuedZone" value="${0}" />
									<c:set var="totalnotAcknowledegeZone" value="${0}" />
									<c:set var="totaldemandByDrc07Zone" value="${0}" />
									<c:set var="totalcaseDropedZone" value="${0}" />
									<c:set var="totalnotApplicableZone" value="${0}" />
									<c:set var="totalpartialVoluentryDemandZone" value="${0}" />
									<c:set var="totaldemandDischageByDrc03Zone" value="${0}" />
									<c:forEach items="${zoneList}" var="zoneList">
										<tr>
											<td>${zoneList.juristiction}</td>
											<td>${zoneList.noOfCase}</td>
											<c:set var="totalnoOfCaseZone" value="${totalnoOfCaseZone + zoneList.noOfCase}" />
											<c:set var="totalindicativeValueZone" value="${totalindicativeValueZone + zoneList.indicativeValue}" />
											<c:set var="totaldemandZone" value="${totaldemandZone + zoneList.demand}" />
											<c:set var="totalrecoveredAmountZone" value="${totalrecoveredAmountZone + zoneList.recoveredAmount}" />
											<c:set var="totaldrc01aissuedZone" value="${totaldrc01aissuedZone + zoneList.drc01aissued}" />
											<c:set var="totalamst10issuedZone" value="${totalamst10issuedZone + zoneList.amst10issued}" />
											<c:set var="totaldrc01issuedZone" value="${totaldrc01issuedZone + zoneList.drc01issued}" />
											<c:set var="totalnotAcknowledegeZone" value="${totalnotAcknowledegeZone + zoneList.notAcknowledege}" />
											<c:set var="totaldemandByDrc07Zone" value="${totaldemandByDrc07Zone + zoneList.demandByDrc07}" />
											<c:set var="totalcaseDropedZone" value="${totalcaseDropedZone + zoneList.caseDroped}" />
											<c:set var="totalnotApplicableZone" value="${totalnotApplicableZone + zoneList.notApplicable}" />
											<c:set var="totalpartialVoluentryDemandZone" value="${totalpartialVoluentryDemandZone + zoneList.partialVoluentryDemand}" />
											<c:set var="totaldemandDischageByDrc03Zone" value="${totaldemandDischageByDrc03Zone + zoneList.demandDischageByDrc03}" />
											<td>${zoneList.indicativeValue}</td>
											<td>${zoneList.demand}</td>
											<td>${zoneList.recoveredAmount}</td>
											<td>${zoneList.notAcknowledege}</td>
											<td>${zoneList.notApplicable}</td>
											<td>${zoneList.drc01aissued}</td>
											<td>${zoneList.amst10issued}</td>
											<td>${zoneList.drc01issued}</td>
											<td>${zoneList.demandByDrc07}</td>
											<td>${zoneList.caseDroped}</td>
											<td>${zoneList.partialVoluentryDemand}</td>
											<td>${zoneList.demandDischageByDrc03}</td>
										</tr>
									</c:forEach>
								</tbody>
								<tbody>
									<tr>
										<td>Grand Total</td>
										<td>${totalnoOfCaseZone}</td>
										<td>${totalindicativeValueZone}</td>
										<td>${totaldemandZone}</td>
										<td>${totalrecoveredAmountZone}</td>
										<td>${totalnotAcknowledegeZone}</td>
										<td>${totalnotApplicableZone}</td>
										<td>${totaldrc01aissuedZone}</td>
										<td>${totalamst10issuedZone}</td>
										<td>${totaldrc01issuedZone}</td>
										<td>${totaldemandByDrc07Zone}</td>
										<td>${totalcaseDropedZone}</td>
										<td>${totalpartialVoluentryDemandZone}</td>
										<td>${totaldemandDischageByDrc03Zone}</td>
									</tr>
								</tbody>
							</table>
						</div>
					</c:if>
				</div>

				<br>
				<hr>
				<br>

				<!-- (kept) Charts containers (IDs used by your existing scripts) -->
				<div class="row">
					<div class="col-lg-12"><div id="pieChart1" style="height: 400px"></div></div>
				</div>
				<br>
				<div class="row">
					<div class="col-lg-12"><div id="pieChart2" style="height: 400px"></div></div>
				</div>
				<br>
				<div class="row">
					<div class="col-lg-12"><div id="pieChart3" style="height: 400px"></div></div>
				</div>
				<br><br>

				<table id="foCaseStageList" class="table table-bordered table-striped" style="width: 100%;">
					<thead>
						<tr>
							<th rowspan="2" style="text-align: center;">Period</th>
							<th rowspan="2" style="text-align: center;">Yet to be Acknowledge</th>
							<th rowspan="2" style="text-align: center;">Yet to be Initiated</th>
							<th colspan="3" style="text-align: center;">Initiated</th>
							<th colspan="4" style="text-align: center;">Concluded</th>
						</tr>
						<tr>
							<th style="text-align: center;">DRC-01A Issued</th>
							<th style="text-align: center;">DRC01 Issued</th>
							<th style="text-align: center;">ASMT-10 Issued</th>
							<th style="text-align: center;">Case Dropped</th>
							<th style="text-align: center;">Demand Created Via DRC07</th>
							<th style="text-align: center;">Partial Voluntary Payment Remaining Demand</th>
							<th style="text-align: center;">Demand Created However Discharged via DRC-03</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${consolidateCategoryWiseDataList}" var="object" varStatus="loop">
							<tr>
								<td style="text-align: center;"><c:out value="${object.period}" /></td>
								<td style="text-align: center;"><c:out value="${object.notAcknowledge}" /></td>
								<td style="text-align: center;"><c:out value="${object.notApplicable}" /></td>
								<td style="text-align: center;"><c:out value="${object.dRC01AIssued}" /></td>
								<td style="text-align: center;"><c:out value="${object.dRC01Issued}" /></td>
								<td style="text-align: center;"><c:out value="${object.aSMT10Issued}" /></td>
								<td style="text-align: center;"><c:out value="${object.caseDropped}" /></td>
								<td style="text-align: center;"><c:out value="${object.demandCreatedByDrc07}" /></td>
								<td style="text-align: center;"><c:out value="${object.partialVoluntaryPaymentRemainingDemand}" /></td>
								<td style="text-align: center;"><c:out value="${object.demandCreatedHoweverDischargedViaDrc03}" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>

			</div><!-- /.container-fluid -->
		</div><!-- /.app-content -->
	</main>

	<!-- Footer -->
	<footer class="app-footer">
		<div class="float-end d-none d-sm-inline">AdminLTE 4</div>
		<strong>© <fmt:formatDate value="<%= new java.util.Date() %>" pattern="yyyy"/> HP GST</strong> — All rights reserved.
	</footer>
</div>

<!-- Scripts: Bootstrap & AdminLTE 4 -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/js/adminlte.min.js"></script>

<!-- jQuery (required by DataTables) -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<!-- DataTables (Bootstrap 5 build) + Buttons -->
<script src="https://cdn.datatables.net/v/bs5/dt-2.0.8/r-3.0.2/b-3.0.2/fh-4.0.1/datatables.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/pdfmake.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/vfs_fonts.js"></script>

<script>
	document.addEventListener('contextmenu', function (e) { e.preventDefault(); });
	document.addEventListener('keydown', function (e) { if (e.ctrlKey && e.key === 'u') { e.preventDefault(); } });
</script>

<!-- (kept) Google Charts scripts exactly as provided -->
<script>
	google.charts.load('current', { 'packages': ['corechart'] });
	google.charts.setOnLoadCallback(drawChart);
	function drawChart() {
		var data = google.visualization.arrayToDataTable(${indicativeTaxValue});
		var options = {
			title: 'Category Wise Total Indicative Tax Value',
			vAxis: { minValue: 0, logScale: true }
		};
		var chart = new google.visualization.ColumnChart(document.getElementById('pieChart1'));
		chart.draw(data, options);
	}
</script>
<script>
	google.charts.load('current', { 'packages': ['corechart'] });
	google.charts.setOnLoadCallback(drawChart);
	function drawChart() {
		var data = google.visualization.arrayToDataTable(${demandValue});
		var options = {
			title: 'Category Wise Total Demand',
			vAxis: { minValue: 0, logScale: true }
		};
		var chart = new google.visualization.ColumnChart(document.getElementById('pieChart2'));
		chart.draw(data, options);
	}
</script>
<script>
	google.charts.load('current', { 'packages': ['corechart'] });
	google.charts.setOnLoadCallback(drawChart);
	function drawChart() {
		var data = google.visualization.arrayToDataTable(${recoveryValue});
		var options = {
			title: 'Category Wise Total Recovery',
			vAxis: { minValue: 0, logScale: true }
		};
		var chart = new google.visualization.ColumnChart(document.getElementById('pieChart3'));
		chart.draw(data, options);
	}
</script>

<!-- (kept) Chart.js example (unchanged) -->
<script src="/static/plugins/chart.js/Chart.min.js"></script>
<script>
	const ctx = document.getElementById('myChart');
	if (ctx) {
		new Chart(ctx, {
			type: 'doughnut',
			data: {
				labels: ['Red','Blue','Yellow'],
				datasets: [{
					label: 'My First Dataset',
					data: [300, 50, 100],
					backgroundColor: ['rgb(255, 99, 132)','rgb(54, 162, 235)','rgb(255, 205, 86)'],
					hoverOffset: 4
				}]
			},
			options: { scales: { y: { beginAtZero: true } } }
		});
	}
</script>

<!-- (kept) DataTables init exactly as your logic -->
<script>
	$("#foCaseStageList").DataTable({
		"responsive": true,
		"lengthChange": false,
		"autoWidth": false
	});

	$(function () {
		$("#example1").DataTable({
			"responsive": true, "lengthChange": false, "autoWidth": false, "scrollX": true,
			"buttons": ["excel","pdf","print"]
		}).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
	});

	function checkForm() {
		alert("dashboard");
		$("#category").prop("required", true);
		$("#financialyear").prop("required", false);
		$("#view").prop("required", true);
	}
</script>

<!-- (kept) FusionCharts heatmap example (unchanged dataset/logic) -->
<script type="text/javascript">
	const dataSource = {
		colorrange: {
			gradient: "0",
			color: [
				{ code: "#6da81e", minvalue: "0", maxvalue: "50", label: "Freezing" },
				{ code: "#f6bc33", minvalue: "50", maxvalue: "70", label: "Warm" },
				{ code: "#e24b1a", minvalue: "70", maxvalue: "85", label: "Hot" }
			]
		},
		dataset: [{
			data: [
				{ rowid: "LA", columnid: "WI", displayvalue: "60.1", colorrangelabel: "Warm" },
				{ rowid: "LA", columnid: "SP", displayvalue: "64.5", colorrangelabel: "Warm" },
				{ rowid: "LA", columnid: "SU", displayvalue: "68.2", colorrangelabel: "Warm" },
				{ rowid: "LA", columnid: "AU", displayvalue: "65.7", colorrangelabel: "Warm" },
				{ rowid: "NY", columnid: "WI", displayvalue: "33.7", colorrangelabel: "Freezing" },
				{ rowid: "NY", columnid: "SP", displayvalue: "57.8", colorrangelabel: "Warm" },
				{ rowid: "NY", columnid: "SU", displayvalue: "74.49", colorrangelabel: "Hot" },
				{ rowid: "NY", columnid: "AU", displayvalue: "57.6", colorrangelabel: "Warm" },
				{ rowid: "CH", columnid: "WI", displayvalue: "22.89", colorrangelabel: "Freezing" },
				{ rowid: "CH", columnid: "SP", displayvalue: "55.7", colorrangelabel: "Warm" },
				{ rowid: "CH", columnid: "SU", displayvalue: "72.2", colorrangelabel: "Hot" },
				{ rowid: "CH", columnid: "AU", displayvalue: "51.6", colorrangelabel: "Warm" },
				{ rowid: "HO", columnid: "WI", displayvalue: "53.0", colorrangelabel: "Warm" },
				{ rowid: "HO", columnid: "SP", displayvalue: "72.7", colorrangelabel: "Hot" },
				{ rowid: "HO", columnid: "SU", displayvalue: "83.3", colorrangelabel: "Hot" },
				{ rowid: "HO", columnid: "AU", displayvalue: "53.0", colorrangelabel: "Warm" }
			]
		}],
		columns: { column: [{ id: "WI", label: "Winter" }, { id: "SU", label: "Summer" }, { id: "SP", label: "Spring" }, { id: "AU", label: "Autumn" }] },
		rows: { row: [{ id: "NY", label: "New York" }, { id: "LA", label: "Los Angeles" }, { id: "CH", label: "Chicago" }, { id: "HO", label: "Houston" }] },
		chart: {
			theme: "candy",
			caption: "Average temperature for Top 4 US Cities",
			subcaption: " Across all seasons (2016-17)",
			showvalues: "1",
			mapbycategory: "1",
			plottooltext: "$rowlabel's average temperature in $columnlabel is $displayvalue °F"
		}
	};

	FusionCharts.ready(function () {
		var myChart = new FusionCharts({
			type: "heatmap",
			renderAt: "chart-container",
			width: "100%",
			height: "100%",
			dataFormat: "json",
			dataSource
		}).render();
	});
</script>
</body>
</html>
