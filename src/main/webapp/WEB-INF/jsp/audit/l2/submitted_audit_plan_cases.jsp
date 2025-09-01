<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Acknowledge Case List</title>

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
		<jsp:include page="../../fo/transfer_popup.jsp" />
		<jsp:include page="../../layout/sidebar.jsp" />
		<div class="content-wrapper">
			<section class="content-header">
				<div class="container-fluid">
					<div class="row mb-2">
						<div class="col-sm-6">
							<h1>Submittted Audit Plan</h1>
						</div>
						<div class="col-sm-6">
							<ol class="breadcrumb float-sm-right">
								<li class="breadcrumb-item"><a href="/l2/dashboard">Home</a></li>
								<li class="breadcrumb-item active">Verification for Audit Plan</li>
							</ol>
						</div>
					</div>
				</div>
			</section>
			<section class="content">
				<div class="container-fluid">
					<div class="row">
						<div class="col-12">
							<div class="card card-primary">
								<div class="card-header">
									<h3 class="card-title">Audit Plan (Submitted by team lead officer(s))</h3>
								</div>
								<div class="card-body">
									<c:if test="${not empty errorMessage}">
										<div class="alert alert-danger alert-dismissible fade show" id="message" role="alert">
											<strong>${errorMessage}</strong>
											<button type="button" class="close" data-dismiss="alert" aria-label="Close">
												<span aria-hidden="true">&times;</span>
											</button>
										</div>
									</c:if>
									<c:if test="${not empty successMessage}">
										<div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
											<strong>${successMessage}</strong>
											<button type="button" class="close" data-dismiss="alert" aria-label="Close">
												<span aria-hidden="true">&times;</span>
											</button>
										</div>
									</c:if>
									<c:if test="${not empty categories}">
										<div class="col-md-6">
											<div class="row">
													<div class="col-md-2">
														<label>Category</label>
													</div>
													<div class="col-md-6">
														<select id="category" name="category" class="selectpicker col-md-12" data-live-search="true">
															<option value="0" selected>All Categories</option>
															<c:forEach items="${categories}" var="category">
																<c:choose>
																	<c:when test="${category.id eq categoryId}">
																			<option value="${category.id}" selected="selected">${category.name}</option>
																	</c:when>
																	<c:otherwise>
																		<option value="${category.id}">${category.name}</option>
																	</c:otherwise>
																</c:choose>
															</c:forEach>
														</select>
													</div>
											</div>
										</div>
										<br>
									</c:if>
									<c:if test="${not empty auditCases}">
										<table id="example1" class="table table-bordered table-striped">
											<thead>
												<tr>
													<th style="text-align: center; vertical-align: middle;">Case Category</th>
													<th style="text-align: center; vertical-align: middle;">GSTIN</th>
													<th style="text-align: center; vertical-align: middle;">Taxpayer Name</th>
													<th style="text-align: center; vertical-align: middle;">Jurisdiction</th>
													<th style="text-align: center; vertical-align: middle;">Case Id</th>
													<th style="text-align: center; vertical-align: middle;">Period</th>
													<th style="text-align: center; vertical-align: middle;">Reporting Date (DD-MM-YYYY)</th>
													<th style="text-align: center; vertical-align: middle;">Indicative Value(₹)</th>
													<th style="text-align: center; vertical-align: middle;">Parameters</th>
													<th style="text-align: center; vertical-align: middle;">Audit Plan Submission date <br>(DD-MM-YYYY)</th>
													<th style="text-align: center; vertical-align: middle;">File Uploaded By Headquater</th>
													<th style="text-align: center; vertical-align: middle;">Action</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${auditCases}" var="object">
													<tr>
														<td><c:out value="${object.category.name}" /></td>
														<td><c:out value="${object.GSTIN}" /></td>
														<td><c:out value="${object.taxpayerName}" /></td>
														<td><c:out value="${object.locationDetails.locationName}"/></td>
														<td><c:out value="${object.caseId}" /></td>
														<td><c:out value="${object.period}" /></td>
														<td><fmt:formatDate value="${object.caseReportingDate}" pattern="dd-MM-yyyy" /></td>
														<td><fmt:formatNumber value="${object.indicativeTaxValue}" pattern="#,##,##0" /></td>
														<td>
															<c:forEach items="${object.parametersNameList}" var="parameter">
																<c:out value="${parameter}, "/>
															</c:forEach>
														</td>
														<td><fmt:formatDate value="${object.currentStatusLastWorkingDate}" pattern="dd-MM-yyyy" /></td>
														<td style="text-align: center;">
															<c:if test="${not empty object.auditExtensionNoDocument.extensionFileName}">
																<a href="/l2/downloadHqPdfFile?fileName=${object.auditExtensionNoDocument.extensionFileName}">
																	<button type="button" onclick="" class="btn btn-primary">
																		<i class="fas fa-download"></i>
																	</button>
																</a>
															</c:if>
															<c:if test="${empty object.auditExtensionNoDocument.extensionFileName}">
																	<button type="button" class="btn btn-primary" disabled>
																		<i class="fas fa-download"></i>
																	</button>
															</c:if>
														</td>
														<td style="text-align: center;">
															<button type="button" onclick="showAssignmentTab('${object.caseId}')" class="btn btn-primary"><i class="fas fa-edit"></i></button>
														</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</c:if>
									<c:if test="${empty auditCases}">
										<div class="col-12" style="text-align: center;">
											<i class="fa fa-info-circle" style="font-size:100px;color:rgb(97, 97, 97)" aria-hidden="true"></i><br>
											<span style="font-size:35px;color:rgb(97, 97, 97)">No Audit Cases Available for Your Jurisdiction(s)</span>
										</div>
									</c:if>
								</div>
							</div>
						</div>
					</div>
				</div>
			</section>
		</div>
		<div class="modal fade" id="showAssignmentModal" tabindex="-1" role="dialog" aria-labelledby="showAssignmentModalTitle" aria-hidden="true" >
			<div class="modal-dialog modal-dialog-centered modal-lg" role="document" >
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="showAssignmentModalTitle">Case Details</h5>
						<button type="button" class="close" data-dismiss="modal" aria-label="Close">    
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
					<div class="modal-body" id="showCaseAssignmentDetails" >
					</div>
				</div>
			</div>
		</div>
		<jsp:include page="../../layout/footer.jsp" />
		<aside class="control-sidebar control-sidebar-dark"></aside>
	</div>

	<script src="/static/plugins/jquery/jquery.min.js"></script>
	<script src="/static/dist/js/jquery-confirm.min.js"></script>
	<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
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
	<script src="/static/dist/js/adminlte.min.js"></script>

	<script>
		$(document).ready(function() {
			$("#message").fadeTo(5000, 500).slideUp(500, function() {
				$("#message").slideUp(500);
			});
		});


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
	</script>

	<script>
		document.getElementById("category").addEventListener("change", function() {
			var selectedValue = this.value;
			var url = "/l2/submitted_audit_plan_cases?categoryId=" + selectedValue;
			window.location.href = url;
		});

		function showAssignmentTab(caseId){
			console.log("Case Id: " + caseId);

			var link = '/l2/update_audit_plan_cases?caseId=' + caseId;

			$.ajax({url: '/checkLoginStatus',
				method: 'get',
				async: false,
				success: function(result){
					const myJSON = JSON.parse(result);
					if(result=='true'){
						$("#showCaseAssignmentDetails").load(link, function(response, status, xhr){
							if(status == 'success'){
									$("#showAssignmentModal").modal('show');
							}else{
								console.log("failed");
							}
						});
					} else if(result=='false'){
						window.location.reload();
					}
				}         
			});
		}
	</script>
		
</body>
</html>