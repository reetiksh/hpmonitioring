<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">

<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>HP GST | Dashboard</title>
	<!-- <link rel="stylesheet" href="/static/dist/css/googleFront/googleFrontFamilySourceSansPro.css"> -->
	<link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
	<link rel="stylesheet" href="/static/dist/ionicons-2.0.1/css/ionicons.min.css">
	<link rel="stylesheet" href="/static/plugins/tempusdominus-bootstrap-4/css/tempusdominus-bootstrap-4.min.css">
	<link rel="stylesheet" href="/static/plugins/icheck-bootstrap/icheck-bootstrap.min.css">
	<link rel="stylesheet" href="/static/plugins/jqvmap/jqvmap.min.css">
	<link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
	<link rel="stylesheet" href="/static/plugins/overlayScrollbars/css/OverlayScrollbars.min.css">
	<link rel="stylesheet" href="/static/plugins/daterangepicker/daterangepicker.css">
	<link rel="stylesheet" href="/static/plugins/summernote/summernote-bs4.min.css">
	<link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
	<link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
	<link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
	<link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
	<link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
	<!-- Step 1 - Include the fusioncharts core library -->
	<script type="text/javascript"
		src="https://cdn.fusioncharts.com/fusioncharts/latest/fusioncharts.js"></script>
	<!-- Step 2 - Include the fusion theme -->
	<script type="text/javascript"
		src="https://cdn.fusioncharts.com/fusioncharts/latest/themes/fusioncharts.theme.fusion.js"></script>

</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
	<div class="app-wrapper">
		<div class="preloader flex-column justify-content-center align-items-center">
			<img class="animation__shake" src="/static/dist/img/AdminLTELogo.png" alt="AdminLTELogo" height="60"
				width="60">
		</div>
		<jsp:include page="../layout/header.jsp" />
		<jsp:include page="../layout/sidebar.jsp" />
		<main class="app-main">
    <div class="app-content">
			<div class="content-header">
				<div class="container-fluid">
					<div class="row mb-2">
						<div class="col-sm-6">
							<h1 class="m-0">Dashboard</h1>
						</div>
						<div class="col-sm-6">
							<ol class="breadcrumb float-sm-right">
								<li class="breadcrumb-item"><a href="/ap/dashboard">Home</a></li>
								<li class="breadcrumb-item active">Dashboard</li>
							</ol>
						</div>
					</div>
				</div>
			</div>
			<section class="content">
				<div class="container-fluid">
					<div class="row">
						<div class="col-sm-12">
							Approver/Verifier Case Status (Other than Old Cases)
						</div>
						<div class="col-lg-3 col-6">
							<div class="small-box bg-info">
								<div class="inner">
									<h3>${approvalTotalCases}<sup style="font-size: 20px"></sup>
									</h3>
									<p>Total Cases recevied for Approval</p>
								</div>
								<div class="icon">
									<i class="ion ion-bag"></i>
								</div>
								<a href="#" class="small-box-footer"> &nbsp</a>
							</div>
						</div>
						<div class="col-lg-3 col-6">
							<div class="small-box bg-success">
								<div class="inner">
									<h3>${pendingApprovalTotalCases}<sup style="font-size: 20px"></sup>
									</h3>
									<p>Cases Closed by Approver</p>
								</div>
								<div class="icon">
									<i class="ion ion-stats-bars"></i>
								</div>
								<a href="#" class="small-box-footer"> &nbsp</a>
							</div>
						</div>
						<div class="col-lg-3 col-6">
							<div class="small-box bg-primary">
								<div class="inner">
									<h3>${verificationTotalCases}<sup style="font-size: 20px"></sup>
									</h3>
									<p>Total Cases recevied for verifications</p>
								</div>
								<div class="icon">
									<i class="ion ion-bag"></i>
								</div>
								<a href="#" class="small-box-footer"> &nbsp</a>
							</div>
						</div>
						<div class="col-lg-3 col-6">
							<div class="small-box bg-warning">
								<div class="inner">
									<h3>${pendingVerificationTotalCases}<sup style="font-size: 20px"></sup>
									</h3>
									<p>Cases Recommended by verifier</p>
								</div>
								<div class="icon">
									<i class="ion ion-stats-bars"></i>
								</div>
								<a href="#" class="small-box-footer"> &nbsp</a>
							</div>
						</div>
						<div class="col-sm-12">
							Field Officer Case Status (Other than Old Cases)
						</div>
						<div class="col-lg-3 col-6">
							<div class="small-box bg-info">
								<div class="inner">
									<h3>${dashBoardTotalCases}<sup style="font-size: 20px"></sup>
									</h3>
									<p>Total Cases</p>
								</div>
								<div class="icon">
									<i class="ion ion-bag"></i>
								</div>
								<a href="#" class="small-box-footer"> &nbsp</a>
							</div>
						</div>
						<div class="col-lg-3 col-6">
							<div class="small-box bg-success">
								<div class="inner">
									<h3>${dashBoardTotalAcknowledgedCases}<sup style="font-size: 20px"></sup>
									</h3>
									<p>Cases Acknowledged</p>
								</div>
								<div class="icon">
									<i class="ion ion-stats-bars"></i>
								</div>
								<a href="#" class="small-box-footer"> &nbsp</a>
							</div>
						</div>
						<div class="col-lg-3 col-6">
							<div class="small-box bg-primary">
								<div class="inner">
									<h3>
										${dashBoardTotalInitiatedCases}<sup style="font-size: 20px"></sup>
									</h3>
									<p>Cases Initiated</p>
								</div>
								<div class="icon">
									<i class="ion ion-stats-bars"></i>
								</div>
								<a href="#" class="small-box-footer"> &nbsp</a>
							</div>
						</div>
						<div class="col-lg-3 col-6">
							<div class="small-box bg-warning">
								<div class="inner">
									<h3>${dashBoardTotalCasesClosedByFo}<sup style="font-size: 20px">
									</h3>
									<p>Cases Closed by FO</p>
								</div>
								<div class="icon">
									<i class="ion ion-person-add"></i>
								</div>
								<a href="#" class="small-box-footer">&nbsp</a>
							</div>
						</div>
						<div class="col-lg-3 col-6">
							<div class="small-box bg-danger">
								<div class="inner">
									<h3>${dashBoardTotalSuspectedIndicativeAmount}<supstyle="font-size: 20px">
									</h3>
									<p>Total Suspected Indicative Amount</p>
								</div>
								<div class="icon">
									<i class="ion ion-pie-graph"></i>
								</div>
								<a href="#" class="small-box-footer"> &nbsp</a>
							</div>
						</div>
						<div class="col-lg-3 col-6">
							<div class="small-box bg-dark">
								<div class="inner">
									<h3>${dashBoardTotalAmount}<sup style="font-size: 20px">
									</h3>
									<p>Total Amount</p>
								</div>
								<div class="icon">
									<i class="ion ion-pie-graph"></i>
								</div>
								<a href="#" class="small-box-footer"> &nbsp</a>
							</div>
						</div>
						<div class="col-lg-3 col-6">
							<div class="small-box bg-secondary">
								<div class="inner">
									<h3>${dashBoardTotalDemand}<sup style="font-size: 20px">
									</h3>
									<p>Total Demand</p>
								</div>
								<div class="icon">
									<i class="ion ion-pie-graph"></i>
								</div>
								<a href="#" class="small-box-footer"> &nbsp </a>
							</div>
						</div>
						<div class="col-lg-3 col-6">
							<div class="small-box bg-light">
								<div class="inner">
									<h3>${dashBoardTotalRecovery}<sup style="font-size: 20px">
									</h3>
									<p>Total Recovery</p>
								</div>
								<div class="icon">
									<i class="ion ion-pie-graph"></i>
								</div>
								<a href="#" class="small-box-footer"> &nbsp </a>
							</div>
						</div>
					</div>


					<form method="GET" action="dashboard" name="dashboard" id="dashboard" modelAttribute="dashboard">
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label>Category<span style="color: red;"> *</span></label> <select id="category"
										name="category" class="custom-select" required>
										<option value="">Please Select</option>
										<c:forEach items="${categories}" var="categories">
											<option value="${categories.id}" <c:if test="${categories.id eq category}">selected</c:if>
												>${categories.name}</option>
										</c:forEach>
									</select>
								</div>
							</div>

							<div class="col-md-6">
								<div class="form-group">
									<label>Financial Year</label> <select id="financialyear" name="financialyear"
										class="custom-select">
										<option value="">Please Select</option>
										<c:forEach items="${financialyear}" var="financialyear">
											<option value="${financialyear}" <c:if test="${financialyear == year}">selected</c:if>
												>${financialyear}</option>
										</c:forEach>
									</select>
								</div>
							</div>

						</div>

						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

						<div class="row">

							<div class="col-md-6">
								<div class="form-group">
									<label>View<span style="color: red;"> *</span></label> <select id="view" name="view"
										class="custom-select" required>
										<option value="">Please Select</option>
										<option value="circleWise" <c:if test="${viewtype eq 'circleWise'}">selected</c:if>
											>Circle-wise</option>
										<!-- <option value="zoneWise" <c:if test="${viewtype eq 'zoneWise'}">selected</c:if>>Zone-wise
										</option> -->
									</select>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label for="parameter">Paramter</label>
									<select id="parameter" name="parameter" class="selectpicker col-md-12"
										title="Please Select the Parameter">
										<option value="0">Select All Parameter</option>
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

						<div class="row">
							<div class="col-md-12">
								<div class="text-center">
									<button type="submit" class="btn btn-primary">Submit</button>
								</div>
							</div>
						</div>
					</form>


					<br>


					<c:if test="${not empty viewtype && viewtype == 'circleWise'}">

						<div class="row">
							<table id="example1" class="table table-bordered table-striped table-responsive">
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
										<!-- <th style="text-align: center; vertical-align: middle;"
				rowspan="2">Total Closed</th> -->
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

									<%-- <c:set var="totalClosed" value="${0}" /> --%>

									<c:forEach items="${circleList}" var="circleList">
										<tr>
											<td>${circleList.juristiction}</td>
											<td>${circleList.noOfCase}</td>
											<c:set var="totalnoOfCase" value="${totalnoOfCase + circleList.noOfCase}" />
											<c:set var="totalindicativeValue"
												value="${totalindicativeValue + circleList.indicativeValue}" />
											<c:set var="totaldemand" value="${totaldemand + circleList.demand}" />
											<c:set var="totalrecoveredAmount"
												value="${totalrecoveredAmount + circleList.recoveredAmount}" />
											<c:set var="totaldrc01aissued" value="${totaldrc01aissued + circleList.drc01aissued}" />
											<c:set var="totalamst10issued" value="${totalamst10issued + circleList.amst10issued}" />
											<c:set var="totaldrc01issued" value="${totaldrc01issued + circleList.drc01issued}" />
											<c:set var="totalnotAcknowledege"
												value="${totalnotAcknowledege + circleList.notAcknowledege}" />
											<c:set var="totaldemandByDrc07"
												value="${totaldemandByDrc07 + circleList.demandByDrc07}" />
											<c:set var="totalcaseDroped" value="${totalcaseDroped + circleList.caseDroped}" />
											<c:set var="totalnotApplicable"
												value="${totalnotApplicable + circleList.notApplicable}" />
											<c:set var="totalpartialVoluentryDemand"
												value="${totalpartialVoluentryDemand + circleList.partialVoluentryDemand}" />
											<c:set var="totaldemandDischageByDrc03"
												value="${totaldemandDischageByDrc03 + circleList.demandDischageByDrc03}" />

											<%-- <c:set var="totalClosed" value="${totalClosed + circleList.totalClosed}" /> --%>


											<td>${circleList.indicativeValue}</td>
											<td>${circleList.demand}</td>
											<td>${circleList.recoveredAmount}</td>
											<td>${circleList.notAcknowledege}</td>
											<td>${circleList.notApplicable}</td>

											<%-- <td>${circleList.totalClosed}</td> --%>

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

										<%-- <td>${totalClosed}</td> --%>

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

					<br> <br>

					<c:if test="${empty viewtype}">

						<div class="row">
							<table id="example1" class="table table-bordered table-striped table-responsive">
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
										<!-- 		<th style="text-align: center; vertical-align: middle;"
				rowspan="2">Total Closed</th> -->
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
									<%-- <c:set var="totalClosed" value="${0}" /> --%>


									<c:forEach items="${circleList}" var="circleList">
										<tr>
											<td>${circleList.juristiction}</td>
											<td>${circleList.noOfCase}</td>
											<c:set var="totalnoOfCase" value="${totalnoOfCase + circleList.noOfCase}" />
											<c:set var="totalindicativeValue"
												value="${totalindicativeValue + circleList.indicativeValue}" />
											<c:set var="totaldemand" value="${totaldemand + circleList.demand}" />
											<c:set var="totalrecoveredAmount"
												value="${totalrecoveredAmount + circleList.recoveredAmount}" />
											<c:set var="totaldrc01aissued" value="${totaldrc01aissued + circleList.drc01aissued}" />
											<c:set var="totalamst10issued" value="${totalamst10issued + circleList.amst10issued}" />
											<c:set var="totaldrc01issued" value="${totaldrc01issued + circleList.drc01issued}" />
											<c:set var="totalnotAcknowledege"
												value="${totalnotAcknowledege + circleList.notAcknowledege}" />
											<c:set var="totaldemandByDrc07"
												value="${totaldemandByDrc07 + circleList.demandByDrc07}" />
											<c:set var="totalcaseDroped" value="${totalcaseDroped + circleList.caseDroped}" />
											<c:set var="totalnotApplicable"
												value="${totalnotApplicable + circleList.notApplicable}" />
											<c:set var="totalpartialVoluentryDemand"
												value="${totalpartialVoluentryDemand + circleList.partialVoluentryDemand}" />
											<c:set var="totaldemandDischageByDrc03"
												value="${totaldemandDischageByDrc03 + circleList.demandDischageByDrc03}" />

											<%-- <c:set var="totalClosed" value="${totalClosed + circleList.totalClosed}" /> --%>

											<td>${circleList.indicativeValue}</td>
											<td>${circleList.demand}</td>
											<td>${circleList.recoveredAmount}</td>
											<td>${circleList.notAcknowledege}</td>
											<td>${circleList.notApplicable}</td>
											<%-- <td>${circleList.totalClosed}</td> --%>
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

					<c:if test="${not empty viewtype && viewtype == 'zoneWise'}">

						<div class="row">
							<table id="example1" class="table table-bordered table-striped table-responsive">
								<thead style="background-color: #C0C0C0">
									<tr>
										<th style="text-align: center; vertical-align: middle;" rowspan="2">Zone</th>
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
											<c:set var="totalindicativeValueZone"
												value="${totalindicativeValueZone + zoneList.indicativeValue}" />
											<c:set var="totaldemandZone" value="${totaldemandZone + zoneList.demand}" />
											<c:set var="totalrecoveredAmountZone"
												value="${totalrecoveredAmountZone + zoneList.recoveredAmount}" />
											<c:set var="totaldrc01aissuedZone"
												value="${totaldrc01aissuedZone + zoneList.drc01aissued}" />
											<c:set var="totalamst10issuedZone"
												value="${totalamst10issuedZone + zoneList.amst10issued}" />
											<c:set var="totaldrc01issuedZone"
												value="${totaldrc01issuedZone + zoneList.drc01issued}" />
											<c:set var="totalnotAcknowledegeZone"
												value="${totalnotAcknowledegeZone + zoneList.notAcknowledege}" />
											<c:set var="totaldemandByDrc07Zone"
												value="${totaldemandByDrc07Zone + zoneList.demandByDrc07}" />
											<c:set var="totalcaseDropedZone" value="${totalcaseDropedZone + zoneList.caseDroped}" />
											<c:set var="totalnotApplicableZone"
												value="${totalnotApplicableZone + zoneList.notApplicable}" />
											<c:set var="totalpartialVoluentryDemandZone"
												value="${totalpartialVoluentryDemandZone + zoneList.partialVoluentryDemand}" />
											<c:set var="totaldemandDischageByDrc03Zone"
												value="${totaldemandDischageByDrc03Zone + zoneList.demandDischageByDrc03}" />

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
					<br>
					<hr>
					<br>
					<!-- <div class="row">
						<div class="col-lg-12">
							<div id="pieChart1" style="height: 400px"></div>
						</div>
					</div>
					<br>
					<div class="row">
						<div class="col-lg-12">
							<div id="pieChart2" style="height: 400px"></div>
						</div>
					</div>
					<br>
					<div class="row">
						<div class="col-lg-12">
							<div id="pieChart3" style="height: 400px"></div>
						</div>
					</div>
					<br> <br>
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
									<td style="text-align: center;">
										<c:out value="${object.period}" />
									</td>
									<td style="text-align: center;">
										<c:out value="${object.notAcknowledge}" />
									</td>
									<td style="text-align: center;">
										<c:out value="${object.notApplicable}" />
									</td>

									<td style="text-align: center;">
										<c:out value="${object.dRC01AIssued}" />
									</td>
									<td style="text-align: center;">
										<c:out value="${object.dRC01Issued}" />
									</td>
									<td style="text-align: center;">
										<c:out value="${object.aSMT10Issued}" />
									</td>
									<td style="text-align: center;">
										<c:out value="${object.caseDropped}" />
									</td>
									<td style="text-align: center;">
										<c:out value="${object.demandCreatedByDrc07}" />
									</td>
									<td style="text-align: center;">
										<c:out value="${object.partialVoluntaryPaymentRemainingDemand}" />
									</td>
									<td style="text-align: center;">
										<c:out value="${object.demandCreatedHoweverDischargedViaDrc03}" />
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table> -->
				</div>
			</section>
    </div>
  </div>
</main>
  <jsp:include page="../layout/footer.jsp" />
	</div>
	<script src="/static/plugins/jquery/jquery.min.js"></script>
	<script src="/static/plugins/jquery-ui/jquery-ui.min.js"></script>
	<script>
		$.widget.bridge('uibutton', $.ui.button)
	</script>
	<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
	<script src="/static/plugins/chart.js/Chart.min.js"></script>
	<script src="/static/plugins/sparklines/sparkline.js"></script>
	<script src="/static/plugins/jqvmap/jquery.vmap.min.js"></script>
	<script src="/static/plugins/jqvmap/maps/jquery.vmap.usa.js"></script>
	<script src="/static/plugins/jquery-knob/jquery.knob.min.js"></script>
	<script src="/static/plugins/moment/moment.min.js"></script>
	<script src="/static/plugins/daterangepicker/daterangepicker.js"></script>
	<script src="/static/plugins/tempusdominus-bootstrap-4/js/tempusdominus-bootstrap-4.min.js"></script>
	<script src="/static/plugins/summernote/summernote-bs4.min.js"></script>
	<script src="/static/plugins/overlayScrollbars/js/jquery.overlayScrollbars.min.js"></script>
	<script src="/static/dist/js/adminlte.js"></script>
	<script src="/static/dist/js/googleCharts/googleChartsLoader.js"></script>
	<script src="/static/dist/js/pages/dashboard.js"></script>
	<script src="/static/plugins/datatables/jquery.dataTables.min.js"></script>
	<script src="/static/plugins/datatables-bs4/js/dataTables.bootstrap4.min.js"></script>
	<script src="/static/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
	<script src="/static/plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
	<script src="/static/plugins/datatables-buttons/js/dataTables.buttons.min.js"></script>
	<script src="/static/plugins/datatables-buttons/js/buttons.bootstrap4.min.js"></script>
	<script src="/static/plugins/jszip/jszip.min.js"></script>
	<script src="/static/plugins/pdfmake/pdfmake.min.js"></script>
	<script src="/static/plugins/pdfmake/vfs_fonts.js"></script>
	<script src="/static/plugins/datatables-buttons/js/buttons.html5.min.js"></script>
	<script src="/static/plugins/datatables-buttons/js/buttons.print.min.js"></script>
	<script src="/static/plugins/datatables-buttons/js/buttons.colVis.min.js"></script>
	<script src="/static/dist/js/bootstrap-select.min.js"></script>
	<script type="text/javascript"
		src="https://cdn.fusioncharts.com/fusioncharts/latest/fusioncharts.js"></script>
	<!-- Step 2 - Include the fusion theme -->
	<script type="text/javascript"
		src="https://cdn.fusioncharts.com/fusioncharts/latest/themes/fusioncharts.theme.fusion.js"></script>

	<script type="text/javascript">
		document.addEventListener('contextmenu', function (e) {
			e.preventDefault();
		});
		document.addEventListener('keydown', function (e) {
			if (e.ctrlKey && e.key === 'u') {
				e.preventDefault();
			}
		});

	</script>
	<script type="text/javascript">

		/*    google.charts.load('current', { 'packages': ['corechart'] });
				google.charts.setOnLoadCallback(drawChart);
				function drawChart() {
					var data = google.visualization.arrayToDataTable(${ indicativeTaxValue });
					var options = {
						title: 'Category Wise Total Indicative Tax Value',
						pieHole: 0.4,
						legend: { position: 'bottom' }
					};
					//var chart = new google.visualization.PieChart(document.getElementById('pieChart1'));
					chart.draw(data, options);
				} */

		/*   google.charts.load('current', { 'packages': ['corechart'] });
			google.charts.setOnLoadCallback(drawChart);
			function drawChart() {
				var data = google.visualization.arrayToDataTable(${ demandValue });
				var options = {
					title: 'Category Wise Total Demand',
					titleposition: { position: 'center' },
					pieHole: 0.4,
					legend: { position: 'bottom' }
				};
				//var chart = new google.visualization.PieChart(document.getElementById('pieChart2'));
				chart.draw(data, options);
				} */

		/*  google.charts.load('current', { 'packages': ['corechart'] });
			google.charts.setOnLoadCallback(drawChart);
			function drawChart() {
				var data = google.visualization.arrayToDataTable(${ recoveryValue });
				var options = {
					title: 'Category Wise Total Recovery',
					pieHole: 0.4,
					legend: { position: 'bottom' }
				};
				//var chart = new google.visualization.PieChart(document.getElementById('pieChart3'));
				chart.draw(data, options);
				} */

	</script>

	<script>

		google.charts.load('current', { 'packages': ['corechart'] });
		google.charts.setOnLoadCallback(drawChart);

		function drawChart() {
			var data = google.visualization.arrayToDataTable(${ indicativeTaxValue });

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
			var data = google.visualization.arrayToDataTable(${ demandValue });

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
			var data = google.visualization.arrayToDataTable(${ recoveryValue });

			var options = {
				title: 'Category Wise Total Recovery',
				vAxis: { minValue: 0, logScale: true }
			};

			var chart = new google.visualization.ColumnChart(document.getElementById('pieChart3'));
			chart.draw(data, options);
		}


	</script>

	<script>
		const ctx = document.getElementById('myChart');
		new Chart(ctx, {
			type: 'doughnut',
			data: {
				labels: [
					'Red',
					'Blue',
					'Yellow'
				],
				datasets: [{
					label: 'My First Dataset',
					data: [300, 50, 100],
					backgroundColor: [
						'rgb(255, 99, 132)',
						'rgb(54, 162, 235)',
						'rgb(255, 205, 86)'
					],
					hoverOffset: 4
				}]
			},
			options: {
				scales: {
					y: {
						beginAtZero: true
					}
				}
			}
		});
	</script>
	<script>
		$("#foCaseStageList").DataTable({
			"responsive": true,
			"lengthChange": false,
			"autoWidth": false
		});


		$(function () {
			$("#example1").DataTable({
				"responsive": true,
				"lengthChange": false,
				"autoWidth": true,
				"buttons": ["excel", "pdf", "print"]
			}).buttons().container().appendTo(
				'#example1_wrapper .col-md-6:eq(0)');
			$('#example2').DataTable({
				"paging": true,
				"lengthChange": false,
				"searching": false,
				"ordering": true,
				"info": true,
				"autoWidth": false,
				"responsive": true,
			});
		});


		function checkForm() {

			alert("dashboard");

			$("#category").prop("required", true);
			$("#financialyear").prop("required", false);
			$("#view").prop("required", true);

		}


	</script>
	<script type="text/javascript">
		const dataSource = {
			colorrange: {
				gradient: "0",
				color: [
					{
						code: "#6da81e",
						minvalue: "0",
						maxvalue: "50",
						label: "Freezing"
					},
					{
						code: "#f6bc33",
						minvalue: "50",
						maxvalue: "70",
						label: "Warm"
					},
					{
						code: "#e24b1a",
						minvalue: "70",
						maxvalue: "85",
						label: "Hot"
					}
				]
			},
			dataset: [
				{
					data: [
						{
							rowid: "LA",
							columnid: "WI",
							displayvalue: "60.1",
							colorrangelabel: "Warm"
						},
						{
							rowid: "LA",
							columnid: "SP",
							displayvalue: "64.5",
							colorrangelabel: "Warm"
						},
						{
							rowid: "LA",
							columnid: "SU",
							displayvalue: "68.2",
							colorrangelabel: "Warm"
						},
						{
							rowid: "LA",
							columnid: "AU",
							displayvalue: "65.7",
							colorrangelabel: "Warm"
						},
						{
							rowid: "NY",
							columnid: "WI",
							displayvalue: "33.7",
							colorrangelabel: "Freezing"
						},
						{
							rowid: "NY",
							columnid: "SP",
							displayvalue: "57.8",
							colorrangelabel: "Warm"
						},
						{
							rowid: "NY",
							columnid: "SU",
							displayvalue: "74.49",
							colorrangelabel: "Hot"
						},
						{
							rowid: "NY",
							columnid: "AU",
							displayvalue: "57.6",
							colorrangelabel: "Warm"
						},
						{
							rowid: "CH",
							columnid: "WI",
							displayvalue: "22.89",
							colorrangelabel: "Freezing"
						},
						{
							rowid: "CH",
							columnid: "SP",
							displayvalue: "55.7",
							colorrangelabel: "Warm"
						},
						{
							rowid: "CH",
							columnid: "SU",
							displayvalue: "72.2",
							colorrangelabel: "Hot"
						},
						{
							rowid: "CH",
							columnid: "AU",
							displayvalue: "51.6",
							colorrangelabel: "Warm"
						},
						{
							rowid: "HO",
							columnid: "WI",
							displayvalue: "53.0",
							colorrangelabel: "Warm"
						},
						{
							rowid: "HO",
							columnid: "SP",
							displayvalue: "72.7",
							colorrangelabel: "Hot"
						},
						{
							rowid: "HO",
							columnid: "SU",
							displayvalue: "83.3",
							colorrangelabel: "Hot"
						},
						{
							rowid: "HO",
							columnid: "AU",
							displayvalue: "53.0",
							colorrangelabel: "Warm"
						}
					]
				}
			],
			columns: {
				column: [
					{
						id: "WI",
						label: "Winter"
					},
					{
						id: "SU",
						label: "Summer"
					},
					{
						id: "SP",
						label: "Spring"
					},
					{
						id: "AU",
						label: "Autumn"
					}
				]
			},
			rows: {
				row: [
					{
						id: "NY",
						label: "New York"
					},
					{
						id: "LA",
						label: "Los Angeles"
					},
					{
						id: "CH",
						label: "Chicago"
					},
					{
						id: "HO",
						label: "Houston"
					}
				]
			},
			chart: {
				theme: "candy",
				caption: "Average temperature for Top 4 US Cities",
				subcaption: " Across all seasons (2016-17)",
				showvalues: "1",
				mapbycategory: "1",
				plottooltext:
					"$rowlabel's average temperature in $columnlabel is $displayvalue °F"
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



		/*  google.charts.load('current', {'packages':['corechart']});
		google.charts.setOnLoadCallback(drawChart);
	
		function drawChart() {
			var data = google.visualization.arrayToDataTable([
				['Year', 'Sales'],
				['2010',  11111111111111],
				['2011',  11111170],
				['2012',  6644444444444440],
				['2013',  1030],
				['2014',  5]  // Small value example
			]);
	
			var options = {
				title: 'Company Performance',
				vAxis: { minValue: 0, logScale: true, viewWindow: {min: 0}},
				series: {
				0: { targetAxisIndex: 0 },
				1: { targetAxisIndex: 1 }
				}
			};
	
			var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
			chart.draw(data, options);
		} */


	</script>
</body>

</html>