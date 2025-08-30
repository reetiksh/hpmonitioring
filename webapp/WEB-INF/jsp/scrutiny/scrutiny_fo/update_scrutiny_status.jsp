<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Update Scrutiny Staus</title>

<!-- <link rel="stylesheet" href="/static/dist/css/googleFront/googleFrontFamilySourceSansPro.css"> -->
<link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
<link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
<link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
<link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
<link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
<link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
<link rel="stylesheet" href="/static/dist/css/adminlte.min.css"> 

</head>
<body class="hold-transition sidebar-mini">
	<div class="wrapper">
		<jsp:include page="../../layout/header.jsp" />
  	<!-- <jsp:include page="../../layout/confirmation_popup.jsp"/> -->
		<jsp:include page="../scrutiny_fo/scrutiny_proceeding_dropped.jsp" />
		<jsp:include page="../../layout/sidebar.jsp" />
		<div class="content-wrapper">
			<section class="content-header">
				<div class="container-fluid">
					<div class="row mb-2">
						<div class="col-sm-6">
							<h1>Update Scrutiny Status</h1>
						</div>
						<div class="col-sm-6">
							<ol class="breadcrumb float-sm-right">
								<li class="breadcrumb-item"><a>Home</a></li>
								<li class="breadcrumb-item active">Update Scrutiny Status</li>
							</ol>
						</div>
					</div>
				</div>
			</section>
			<section class="content">
				<div class="container-fluid">
					<div class="row">
						<div class="col-12">
							<div class="card-primary">
								<div class="card-header">
									<h3 class="card-title">Update Scrutiny Status</h3>
								</div>

								<div class="card-body">
									<c:if test="${not empty message}">
										<div class="alert alert-success alert-dismissible fade show"
											id="message" role="alert">
											<strong>${message}</strong>
											<button type="button" class="close" data-dismiss="alert"
												aria-label="Close">
												<span aria-hidden="true">&times;</span>
											</button>
										</div>
									</c:if>
									<c:if test="${not empty acknowlegdemessage}">
										<div class="alert alert-success alert-dismissible fade show"
											id="message" role="alert">
											<strong>${acknowlegdemessage}</strong>
											<button type="button" class="close" data-dismiss="alert"
												aria-label="Close">
												<span aria-hidden="true">&times;</span>
											</button>
										</div>
									</c:if>
									<table id="example1" class="table table-bordered table-striped">
										<thead>
											<tr>
												<th style="text-align: center; vertical-align: middle;">GSTIN</th>
												<th style="text-align: center; vertical-align: middle;">Taxpayer Name</th>
												<th style="text-align: center; vertical-align: middle;">Jurisdiction</th>
												<th style="text-align: center; vertical-align: middle;">Case Category</th>
												<th style="text-align: center; vertical-align: middle;">Period</th>
												<th style="text-align: center; vertical-align: middle;">Reporting Date (DD-MM-YYYY)</th>
												<th style="text-align: center; vertical-align: middle;">ASMT-10 Issued Or Not ?</th>
												<th style="text-align: center; vertical-align: middle;">Indicative Value(₹)</th>
												<th style="text-align: center; vertical-align: middle;">Parameter(s)</th>
												<!-- <th style="text-align: center; vertical-align: middle;">Reasons</th> -->
												<th style="text-align: center; vertical-align: middle;">Action</th>
												
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${mstScrutinyCasesList}" var="mstScrutinyCase">
												<tr>
													<td style="text-align: center; vertical-align: middle;">${mstScrutinyCase.id.GSTIN}</td>
													<td style="text-align: center; vertical-align: middle;">${mstScrutinyCase.taxpayerName}</td>
													<td style="text-align: center; vertical-align: middle;">${mstScrutinyCase.locationDetails.locationName}</td>
													<td style="text-align: center; vertical-align: middle;">${mstScrutinyCase.category.name}</td>
													<td style="text-align: center; vertical-align: middle;">${mstScrutinyCase.id.period}</td>
													<td style="text-align: center; vertical-align: middle;"><fmt:formatDate value="${mstScrutinyCase.id.caseReportingDate}" pattern="dd-MM-yyyy" /></td>
													<td style="text-align: center; vertical-align: middle;">Issued</td>
													<td style="text-align: center; vertical-align: middle;">${mstScrutinyCase.indicativeTaxValue}</td>
													<td style="text-align: center; vertical-align: middle;">${mstScrutinyCase.allConcatParametersValue}</td>
													<td style="text-align: center; vertical-align: middle;">
														<button class="btn btn-primary" onclick="updateScrutinyStaus('${mstScrutinyCase.id.GSTIN}' , '${mstScrutinyCase.id.caseReportingDate}' , '${mstScrutinyCase.id.period}');"><i class="fas fa-edit"></i></button>														
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
									
								</div>
							</div>
						</div>
					</div>
				</div>
			</section>
		</div>
		<jsp:include page="../../layout/footer.jsp" />
		<aside class="control-sidebar control-sidebar-dark"></aside>
	</div>

	<script src="/static/plugins/jquery/jquery.min.js"></script>
	<script src="/static/dist/js/jquery-confirm.min.js"></script>
	<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
	<script src="/static/plugins/datatables/jquery.dataTables.min.js"></script>
	<script
		src="/static/plugins/datatables-bs4/js/dataTables.bootstrap4.min.js"></script>
	<script
		src="/static/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
	<script
		src="/static/plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
	<script
		src="/static/plugins/datatables-buttons/js/dataTables.buttons.min.js"></script>
	<script
		src="/static/plugins/datatables-buttons/js/buttons.bootstrap4.min.js"></script>
	<script src="/static/plugins/jszip/jszip.min.js"></script>
	<script src="/static/plugins/pdfmake/pdfmake.min.js"></script>
	<script src="/static/plugins/pdfmake/vfs_fonts.js"></script>
	<script
		src="/static/plugins/datatables-buttons/js/buttons.html5.min.js"></script>
	<script
		src="/static/plugins/datatables-buttons/js/buttons.print.min.js"></script>
	<script
		src="/static/plugins/datatables-buttons/js/buttons.colVis.min.js"></script>
	<script src="/static/dist/js/bootstrap-select.min.js"></script>
	<script src="/static/dist/js/adminlte.min.js"></script>
	<script>
		document.addEventListener('contextmenu', function(e) {
			e.preventDefault();
		});
		document.addEventListener('keydown', function(e) {
			if (e.ctrlKey && e.key === 'u') {
				e.preventDefault();
			}
		});
		document.addEventListener('keydown', function(e) {
			if (e.key === 'F12') {
				e.preventDefault();
			}
		});
		// Disable back and forward cache
		$(document).ready(function() {
			function disableBack() {
				window.history.forward()
			}

			window.onload = disableBack();
			window.onpageshow = function(evt) {
				if (evt.persisted)
					disableBack()
			}
		});
		// Disable refresh
		document.onkeydown = function(e) {
			if (e.key === 'F5' || (e.ctrlKey && e.key === 'r')
					|| e.keyCode === 116) {
				e.preventDefault();

			}
		};
		
		
	function updateScrutinyStaus(gstin,reportingDate,period){
			var url = '/scrutiny_fo/scrutiny_status_updated?gstin=' + gstin +"&reportingDate=" + reportingDate + "&period=" + period;
			window.location.href = url;
		}
		
	</script>

	<script>
		$(document).ready(function() {
			$("#message").fadeTo(2000, 500).slideUp(500, function() {
				$("#message").slideUp(500);
			});
		});

		$(function() {
			$("#example1").DataTable({
				"responsive" : true,
				"lengthChange" : false,
				"autoWidth" : false,
				"buttons" : [ "excel", "pdf", "print" ]
			}).buttons().container().appendTo(
					'#example1_wrapper .col-md-6:eq(0)');
			$('#example2').DataTable({
				"paging" : true,
				"lengthChange" : false,
				"searching" : false,
				"ordering" : true,
				"info" : true,
				"autoWidth" : false,
				"responsive" : true,
			});
		});

		

		
		


		
		
		
		
	</script>
</body>
</html>
