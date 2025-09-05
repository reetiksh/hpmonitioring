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

	<link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png"/>

	<!-- AdminLTE 4 (Bootstrap 5) + FontAwesome -->
	<link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6/css/all.min.css" rel="stylesheet">
	<link href="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/css/adminlte.min.css" rel="stylesheet">

	<!-- (Optional) Keep your existing local plugins if needed -->
	<link rel="stylesheet" href="/static/plugins/tempusdominus-bootstrap-4/css/tempusdominus-bootstrap-4.min.css">
	<link rel="stylesheet" href="/static/plugins/icheck-bootstrap/icheck-bootstrap.min.css">
	<link rel="stylesheet" href="/static/plugins/jqvmap/jqvmap.min.css">
	<link rel="stylesheet" href="/static/plugins/daterangepicker/daterangepicker.css">
	<link rel="stylesheet" href="/static/plugins/summernote/summernote-bs4.min.css">
	<link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
	<link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">

	<!-- DataTables (Bootstrap 5) + Buttons (CDN) -->
	<link href="https://cdn.datatables.net/v/bs5/dt-2.0.7/b-3.0.2/r-3.0.2/datatables.min.css" rel="stylesheet"/>

	<!-- FusionCharts (kept as-is) -->
	<script src="https://cdn.fusioncharts.com/fusioncharts/latest/fusioncharts.js"></script>
	<script src="https://cdn.fusioncharts.com/fusioncharts/latest/themes/fusioncharts.theme.fusion.js"></script>

	<style>
		/* Replace AdminLTE v3 small-box with simple KPI cards for v4 */
		.kpi-card .icon-wrap{font-size:32px;opacity:.65}
		.table thead th, .table tbody td{vertical-align:middle;text-align:center}
	</style>
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

	<!-- Header & Sidebar (unchanged includes) -->
	<jsp:include page="../layout/header.jsp" />
	<jsp:include page="../layout/sidebar.jsp" />

	<main class="app-main">
		<!-- Content Header -->
		<div class="app-content-header">
			<div class="container-fluid">
				<div class="row mb-2">
					<div class="col-sm-6"><h1 class="m-0">Dashboard</h1></div>
					<div class="col-sm-6">
						<ol class="breadcrumb float-sm-end">
							<li class="breadcrumb-item"><a href="/fo/dashboard">Home</a></li>
							<li class="breadcrumb-item active" aria-current="page">Dashboard</li>
						</ol>
					</div>
				</div>
			</div>
		</div>

		<!-- Main Content -->
		<div class="app-content">
			<div class="container-fluid">

				<!-- KPI cards (AdminLTE 4 cards) -->
				<div class="row g-3">
					<div class="col-lg-3 col-6">
						<div class="card text-bg-info kpi-card h-100">
							<div class="card-body d-flex justify-content-between align-items-center">
								<div>
									<h3 class="mb-1">${dashBoardTotalCases}</h3>
									<p class="mb-0">Total Cases</p>
								</div>
								<div class="icon-wrap"><i class="fa-solid fa-briefcase"></i></div>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-6">
						<div class="card text-bg-success kpi-card h-100">
							<div class="card-body d-flex justify-content-between align-items-center">
								<div>
									<h3 class="mb-1">${dashBoardTotalAcknowledgedCases}</h3>
									<p class="mb-0">Cases Acknowledged</p>
								</div>
								<div class="icon-wrap"><i class="fa-solid fa-square-check"></i></div>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-6">
						<div class="card text-bg-primary kpi-card h-100">
							<div class="card-body d-flex justify-content-between align-items-center">
								<div>
									<h3 class="mb-1">${dashBoardTotalInitiatedCases}</h3>
									<p class="mb-0">Cases Initiated</p>
								</div>
								<div class="icon-wrap"><i class="fa-solid fa-play"></i></div>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-6">
						<div class="card text-bg-warning kpi-card h-100">
							<div class="card-body d-flex justify-content-between align-items-center">
								<div>
									<h3 class="mb-1">${dashBoardTotalCasesClosedByFo}</h3>
									<p class="mb-0">Cases Closed by FO</p>
								</div>
								<div class="icon-wrap"><i class="fa-solid fa-circle-check"></i></div>
							</div>
						</div>
					</div>

					<div class="col-lg-3 col-6">
						<div class="card text-bg-danger kpi-card h-100">
							<div class="card-body d-flex justify-content-between align-items-center">
								<div>
									<h3 class="mb-1">${dashBoardTotalSuspectedIndicativeAmount}</h3>
									<p class="mb-0">Total Suspected Indicative Amount</p>
								</div>
								<div class="icon-wrap"><i class="fa-solid fa-indian-rupee-sign"></i></div>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-6">
						<div class="card text-bg-dark kpi-card h-100">
							<div class="card-body d-flex justify-content-between align-items-center">
								<div>
									<h3 class="mb-1">${dashBoardTotalAmount}</h3>
									<p class="mb-0">Total Amount</p>
								</div>
								<div class="icon-wrap"><i class="fa-solid fa-sack-dollar"></i></div>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-6">
						<div class="card text-bg-secondary kpi-card h-100">
							<div class="card-body d-flex justify-content-between align-items-center">
								<div>
									<h3 class="mb-1">${dashBoardTotalDemand}</h3>
									<p class="mb-0">Total Demand</p>
								</div>
								<div class="icon-wrap"><i class="fa-solid fa-file-invoice"></i></div>
							</div>
						</div>
					</div>
					<div class="col-lg-3 col-6">
						<div class="card text-bg-light kpi-card h-100">
							<div class="card-body d-flex justify-content-between align-items-center">
								<div>
									<h3 class="mb-1">${dashBoardTotalRecovery}</h3>
									<p class="mb-0">Total Recovery</p>
								</div>
								<div class="icon-wrap"><i class="fa-solid fa-coins"></i></div>
							</div>
						</div>
					</div>
				</div>

				<!-- Filters (logic unchanged) -->
				<div class="card card-primary card-outline mt-4">
					<div class="card-header"><h3 class="card-title mb-0">Filters</h3></div>
					<div class="card-body">
						<form method="GET" action="dashboard" name="dashboard" id="dashboard" modelAttribute="dashboard">
							<div class="row g-3">
								<div class="col-md-3">
									<label class="form-label">Category<span class="text-danger"> *</span></label>
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
								<div class="col-md-3">
									<label class="form-label">View<span class="text-danger"> *</span></label>
									<select id="view" name="view" class="form-select" required>
										<option value="">Please Select</option>
										<option value="circleWise" <c:if test="${viewtype eq 'circleWise'}">selected</c:if>>Circle-wise</option>
										<!-- <option value="zoneWise" <c:if test="${viewtype eq 'zoneWise'}">selected</c:if>>Zone-wise</option> -->
									</select>
								</div>
								<div class="col-md-3">
									<label class="form-label">Financial Year</label>
									<select id="financialyear" name="financialyear" class="form-select">
										<option value="">Please Select</option>
										<c:forEach items="${financialyear}" var="financialyear">
											<option value="${financialyear}" <c:if test="${financialyear == year}">selected</c:if>>${financialyear}</option>
										</c:forEach>
									</select>
								</div>
								<div class="col-md-3">
									<label for="parameter" class="form-label">Paramter</label>
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
								<div class="col-12 text-end">
									<button type="submit" class="btn btn-primary">Submit</button>
								</div>
							</div>
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						</form>
					</div>
				</div>

				<!-- circleWise table (logic/data unchanged) -->
				<c:if test="${not empty viewtype && viewtype == 'circleWise'}">
					<div class="card card-outline card-primary mt-3">
						<div class="card-body">
							<table id="example1" class="table table-bordered table-striped w-100">
								<thead style="background-color:#C0C0C0">
									<tr>
										<th rowspan="2">Circle</th>
										<th rowspan="2">No. of Cases</th>
										<th rowspan="2">Suspected Indicative Tax Value (₹)</th>
										<th rowspan="2">Demand Created (₹)</th>
										<th rowspan="2">Amount Recovered (₹)</th>
										<th rowspan="2">Yet to be Acknowledge</th>
										<th rowspan="2">Yet to be Initiated</th>
										<th colspan="7">Case Stage</th>
									</tr>
									<tr>
										<th>DRC-01A issued</th>
										<th>ASMT-10 issued</th>
										<th>DRC01 issued</th>
										<th>Demand Created via DRC07</th>
										<th>Case Dropped</th>
										<th>Partial Voluntary Payment Remaining Demand</th>
										<th>Demand Created However Discharged via DRC-03</th>
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
									<tr class="table-light fw-semibold">
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
					</div>
				</c:if>

				<!-- default view (empty viewtype) -->
				<c:if test="${empty viewtype}">
					<div class="card card-outline card-primary mt-3">
						<div class="card-body">
							<table id="example1" class="table table-bordered table-striped w-100">
								<thead style="background-color:#C0C0C0">
									<tr>
										<th rowspan="2">Circle</th>
										<th rowspan="2">No. of Cases</th>
										<th rowspan="2">Suspected Indicative TaxValue (₹)</th>
										<th rowspan="2">Demand Created (₹)</th>
										<th rowspan="2">Amount Recovered (₹)</th>
										<th rowspan="2">Yet to be Acknowledge</th>
										<th rowspan="2">Yet to be Initiated</th>
										<th colspan="7">Case Stage</th>
									</tr>
									<tr>
										<th>DRC-01A issued</th>
										<th>ASMT-10 issued</th>
										<th>DRC01 issued</th>
										<th>Demand Created via DRC07</th>
										<th>Case Dropped</th>
										<th>Partial Voluntary Payment Remaining Demand</th>
										<th>Demand Created However Discharged via DRC-03</th>
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
									<tr class="table-light fw-semibold">
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
					</div>
				</c:if>

				<!-- zoneWise table (logic/data unchanged) -->
				<c:if test="${not empty viewtype && viewtype == 'zoneWise'}">
					<div class="card card-outline card-primary mt-3">
						<div class="card-body">
							<table id="example1" class="table table-bordered table-striped w-100">
								<thead style="background-color:#C0C0C0">
									<tr>
										<th rowspan="2">Zone</th>
										<th rowspan="2">No. of Cases</th>
										<th rowspan="2">Suspected Indicative Tax Value (₹)</th>
										<th rowspan="2">Demand Created (₹)</th>
										<th rowspan="2">Amount Recovered (₹)</th>
										<th rowspan="2">Yet to be Acknowledge</th>
										<th rowspan="2">Yet to be Initiated</th>
										<th colspan="7">Case Stage</th>
									</tr>
									<tr>
										<th>DRC-01A issued</th>
										<th>ASMT-10 issued</th>
										<th>DRC01 issued</th>
										<th>Demand Created via DRC07</th>
										<th>Case Dropped</th>
										<th>Partial Voluntary Payment Remaining Demand</th>
										<th>Demand Created However Discharged via DRC-03</th>
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
									<tr class="table-light fw-semibold">
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
					</div>
				</c:if>

				<br><hr><br>

				<!-- Chart containers (IDs unchanged to preserve logic) -->
				<div class="row">
					<div class="col-lg-12"><div id="pieChart1" style="height:400px"></div></div>
				</div><br>
				<div class="row">
					<div class="col-lg-12"><div id="pieChart2" style="height:400px"></div></div>
				</div><br>
				<div class="row">
					<div class="col-lg-12"><div id="pieChart3" style="height:400px"></div></div>
				</div>

			</div>
		</div>

		<jsp:include page="../layout/footer.jsp" />
	</main>
</div>

<!-- Core JS (AdminLTE 4 requires Bootstrap 5) -->
<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/js/adminlte.min.js"></script>

<!-- Keep your existing local plugins if used anywhere in your logic -->
<script src="/static/plugins/jquery-ui/jquery-ui.min.js"></script>
<script>$.widget.bridge('uibutton', $.ui.button)</script>
<script src="/static/plugins/chart.js/Chart.min.js"></script>
<script src="/static/plugins/sparklines/sparkline.js"></script>
<script src="/static/plugins/jqvmap/jquery.vmap.min.js"></script>
<script src="/static/plugins/jqvmap/maps/jquery.vmap.usa.js"></script>
<script src="/static/plugins/jquery-knob/jquery.knob.min.js"></script>
<script src="/static/plugins/moment/moment.min.js"></script>
<script src="/static/plugins/daterangepicker/daterangepicker.js"></script>
<script src="/static/plugins/tempusdominus-bootstrap-4/js/tempusdominus-bootstrap-4.min.js"></script>
<script src="/static/plugins/summernote/summernote-bs4.min.js"></script>
<script src="/static/dist/js/googleCharts/googleChartsLoader.js"></script>
<script src="/static/dist/js/pages/dashboard.js"></script>

<!-- DataTables (Bootstrap 5) + Buttons -->
<script src="https://cdn.datatables.net/v/bs5/dt-2.0.7/b-3.0.2/r-3.0.2/datatables.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/jszip@3.10.1/dist/jszip.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/pdfmake@0.2.7/build/pdfmake.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/pdfmake@0.2.7/build/vfs_fonts.js"></script>

<!-- (Optional) bootstrap-select (BS5-compatible) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.14.0-beta3/dist/js/bootstrap-select.min.js"></script>

<!-- Your original scripts & logic preserved -->
<script type="text/javascript">
	document.addEventListener('contextmenu', function (e) { e.preventDefault(); });
	document.addEventListener('keydown', function (e) { if (e.ctrlKey && e.key === 'u') { e.preventDefault(); } });
</script>

<!-- Google Charts logic (unchanged) -->
<script>
	google.charts.load('current', { 'packages': ['corechart'] });
	google.charts.setOnLoadCallback(function() {
		try {
			var data1 = google.visualization.arrayToDataTable(${ indicativeTaxValue });
			var opts1 = { title: 'Category Wise Total Indicative Tax Value', vAxis: { minValue: 0, logScale: true } };
			new google.visualization.ColumnChart(document.getElementById('pieChart1')).draw(data1, opts1);
		}catch(e){}
		try {
			var data2 = google.visualization.arrayToDataTable(${ demandValue });
			var opts2 = { title: 'Category Wise Total Demand', vAxis: { minValue: 0, logScale: true } };
			new google.visualization.ColumnChart(document.getElementById('pieChart2')).draw(data2, opts2);
		}catch(e){}
		try {
			var data3 = google.visualization.arrayToDataTable(${ recoveryValue });
			var opts3 = { title: 'Category Wise Total Recovery', vAxis: { minValue: 0, logScale: true } };
			new google.visualization.ColumnChart(document.getElementById('pieChart3')).draw(data3, opts3);
		}catch(e){}
	});
</script>

<!-- Chart.js demo (left intact though no canvas is present in provided markup) -->
<script>
	const ctx = document.getElementById('myChart');
	if (ctx) {
		new Chart(ctx, {
			type: 'doughnut',
			data: { labels: ['Red','Blue','Yellow'], datasets: [{ label: 'My First Dataset', data: [300,50,100], backgroundColor: ['rgb(255,99,132)','rgb(54,162,235)','rgb(255,205,86)'], hoverOffset: 4 }] },
			options: { scales: { y: { beginAtZero: true } } }
		});
	}
</script>

<!-- DataTables init (logic unchanged) -->
<script>
	$("#foCaseStageList").DataTable({ responsive:true, lengthChange:false, autoWidth:false });

	$(function () {
		$("#example1").DataTable({
			responsive: true,
			lengthChange: false,
			autoWidth: true,
			buttons: ["excel","pdf","print"]
		}).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');

		$('#example2').DataTable({
			paging: true,
			lengthChange: false,
			searching: false,
			ordering: true,
			info: true,
			autoWidth: false,
			responsive: true
		});
	});

	function checkForm() {
		alert("dashboard");
		$("#category").prop("required", true);
		$("#financialyear").prop("required", false);
		$("#view").prop("required", true);
	}
</script>

<!-- FusionCharts sample (unchanged data source) -->
<script type="text/javascript">
	const dataSource = {
		colorrange:{gradient:"0",color:[{code:"#6da81e",minvalue:"0",maxvalue:"50",label:"Freezing"},{code:"#f6bc33",minvalue:"50",maxvalue:"70",label:"Warm"},{code:"#e24b1a",minvalue:"70",maxvalue:"85",label:"Hot"}]},
		dataset:[{data:[{rowid:"LA",columnid:"WI",displayvalue:"60.1",colorrangelabel:"Warm"},{rowid:"LA",columnid:"SP",displayvalue:"64.5",colorrangelabel:"Warm"},{rowid:"LA",columnid:"SU",displayvalue:"68.2",colorrangelabel:"Warm"},{rowid:"LA",columnid:"AU",displayvalue:"65.7",colorrangelabel:"Warm"},{rowid:"NY",columnid:"WI",displayvalue:"33.7",colorrangelabel:"Freezing"},{rowid:"NY",columnid:"SP",displayvalue:"57.8",colorrangelabel:"Warm"},{rowid:"NY",columnid:"SU",displayvalue:"74.49",colorrangelabel:"Hot"},{rowid:"NY",columnid:"AU",displayvalue:"57.6",colorrangelabel:"Warm"},{rowid:"CH",columnid:"WI",displayvalue:"22.89",colorrangelabel:"Freezing"},{rowid:"CH",columnid:"SP",displayvalue:"55.7",colorrangelabel:"Warm"},{rowid:"CH",columnid:"SU",displayvalue:"72.2",colorrangelabel:"Hot"},{rowid:"CH",columnid:"AU",displayvalue:"51.6",colorrangelabel:"Warm"},{rowid:"HO",columnid:"WI",displayvalue:"53.0",colorrangelabel:"Warm"},{rowid:"HO",columnid:"SP",displayvalue:"72.7",colorrangelabel:"Hot"},{rowid:"HO",columnid:"SU",displayvalue:"83.3",colorrangelabel:"Hot"},{rowid:"HO",columnid:"AU",displayvalue:"53.0",colorrangelabel:"Warm"}]}],
		columns:{column:[{id:"WI",label:"Winter"},{id:"SU",label:"Summer"},{id:"SP",label:"Spring"},{id:"AU",label:"Autumn"}]},
		rows:{row:[{id:"NY",label:"New York"},{id:"LA",label:"Los Angeles"},{id:"CH",label:"Chicago"},{id:"HO",label:"Houston"}]},
		chart:{theme:"candy",caption:"Average temperature for Top 4 US Cities",subcaption:" Across all seasons (2016-17)",showvalues:"1",mapbycategory:"1",plottooltext:"$rowlabel's average temperature in $columnlabel is $displayvalue °F"}
	};
	FusionCharts.ready(function(){ new FusionCharts({ type:"heatmap", renderAt:"chart-container", width:"100%", height:"100%", dataFormat:"json", dataSource }).render(); });
</script>
</body>
</html>
