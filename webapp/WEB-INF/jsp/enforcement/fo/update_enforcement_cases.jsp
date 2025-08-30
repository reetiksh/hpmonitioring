<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html lang="en">

<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>HP GST | Uploaded Enforcement Cases</title>
	<link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">
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
		<jsp:include page="../../layout/sidebar.jsp" />
		<div class="content-wrapper">
			<section class="content-header">
				<div class="container-fluid">
					<div class="row mb-2">
						<div class="col-sm-6">
							<h1>Uploaded Enforcement Cases</h1>
						</div>
						<div class="col-sm-6">
							<ol class="breadcrumb float-sm-right">
								<li class="breadcrumb-item"><a href="enforcement_fo/dashboard">Home</a></li>
								<li class="breadcrumb-item active">Uploaded Enforcement Cases</li>
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
									<h3 class="card-title">Uploaded Enforcement Cases</h3>
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
													<select id="category" name="category" class="selectpicker col-md-12"
														data-live-search="true">
														<option value="0" selected>All Categories</option>
														<c:forEach items="${categories}" var="category">
															<c:choose>
																<c:when test="${category.id eq categoryId}">
																	<option value="${category.id}" selected="selected">${category.name}
																	</option>
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
									<c:if test="${not empty enforcementCases}">
										<table id="example1" class="table table-bordered table-striped">
											<thead>
												<tr>
													<th style="text-align: center; vertical-align: middle;">GSTIN</th>
													<th style="text-align: center; vertical-align: middle;">Taxpayer Name</th>
													<th style="text-align: center; vertical-align: middle;">Jurisdiction</th>
													<th style="text-align: center; vertical-align: middle;">Case Category</th>
													<th style="text-align: center; vertical-align: middle;">Period</th>
													<th style="text-align: center; vertical-align: middle;">Reporting Date
														(DD-MM-YYYY)</th>
													<th style="text-align: center; vertical-align: middle;">Indicative Value(₹)</th>
													<th style="text-align: center; vertical-align: middle;">Parameter</th>
													<th style="text-align: center; vertical-align: middle;">Assigned on (DD-MM-YYYY)
													</th>
													<th style="text-align: center; vertical-align: middle;">Supporting File</th>
													<th style="text-align: center; vertical-align: middle;">Action</th>
												</tr>
											</thead>
											<tbody>
												<c:forEach items="${enforcementCases}" var="object">
													<tr data-gstin="${object.id.GSTIN}" data-period="${object.id.period}" data-reporting-date="${object.id.caseReportingDate}">
														<td>
															<c:out value="${object.id.GSTIN}" />
														</td>
														<td>
															<c:out value="${object.taxpayerName}" />
														</td>
														<td>
															<c:out value="${object.caseLocation.locationName}" />
														</td>
														<td>
															<c:out value="${object.category.name}" />
														</td>
														<td>
															<c:out value="${object.id.period}" />
														</td>
														<td style="text-align: center;">
															<fmt:formatDate value="${object.id.caseReportingDate}" pattern="dd-MM-yyyy" />
														</td>
														<td>
															<fmt:formatNumber value="${object.indicativeTaxValue}" pattern="#,##,##0" />
														</td>
														<td>
															<c:out value="${object.parametersNameList}" />
														</td>
														<td style="text-align: center;">
															<fmt:formatDate value="${object.caseUpdatingTimestamp}"
																pattern="dd-MM-yyyy" />
														</td>
														<td style="text-align: center;">
															<c:if test="${not empty object.extensionNoDocumentId.extensionFileName}">
																<a
																	href="/enforcement_fo/downloadHqPdfFile?fileName=${object.extensionNoDocumentId.extensionFileName}">
																	<button type="button" onclick="" class="btn btn-primary">
																		<i class="fas fa-download"></i>
																	</button>
																</a>
															</c:if>
															<c:if test="${empty object.extensionNoDocumentId.extensionFileName}">
																<button type="button" class="btn btn-primary" disabled>
																	<i class="fas fa-download"></i>
																</button>
															</c:if>
														</td>
														<td style="text-align: center;">
															<button type="button"
																onclick="showCaseDetails('${object.id.GSTIN}', '${object.id.period}', '${object.id.caseReportingDate}')"
																class="btn btn-primary"><i class="fas fa-edit"></i></button>
														</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
									</c:if>
									<c:if test="${empty enforcementCases}">
										<div class="col-12" style="text-align: center;">
											<i class="fa fa-info-circle" style="font-size:100px;color:rgb(97, 97, 97)"
												aria-hidden="true"></i><br>
											<span style="font-size:35px;color:rgb(97, 97, 97)">No Audit Cases Available for Your
												Jurisdiction(s)</span>
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
			<div class="modal-dialog modal-dialog-centered" role="document" >
				<div class="modal-content">
					<div class="modal-header">
						<h5 class="modal-title" id="showAssignmentModalTitle">Update Case Information</h5>
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

		$(document).ready(function () {
			$("#message").fadeTo(5000, 500).slideUp(500, function () {
				$("#message").slideUp(500);
			});
		});

		document.addEventListener('contextmenu', function (e) {
			e.preventDefault();
		});
		document.addEventListener('keydown', function (e) {
			if (e.ctrlKey && e.key === 'u') {
				e.preventDefault();
			}
		});
		document.addEventListener('keydown', function (e) {
			if (e.key === 'F12') {
				e.preventDefault();
			}
		});
		// Disable back and forward cache
		$(document).ready(function () {
			function disableBack() {
				window.history.forward()
			}

			window.onload = disableBack();
			window.onpageshow = function (evt) {
				if (evt.persisted)
					disableBack()
			}
		});
		// Disable refresh
		document.onkeydown = function (e) {
			if (e.key === 'F5' || (e.ctrlKey && e.key === 'r')
				|| e.keyCode === 116) {
				e.preventDefault();

			}
		};
	</script>

	<script>
		document.getElementById("category").addEventListener("change", function () {
			var selectedValue = this.value;
			var url = "/enforcement_fo/update_enforcement_cases?categoryId=" + selectedValue;
			window.location.href = url;
		});

		$(function () {
			$("#example1").DataTable({
				"responsive": false, "lengthChange": false, "autoWidth": true, "scrollX": true,
				"buttons":
					[
						"excel",
						"pdf",
						"print",
					]
			}).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
		});
		// function showCaseIdSubmissionAndApprovalRequestFormTab(GSTIN, period, caseReportingDate){
		// 	var link = '/enforcement_fo/update_enforcement_case?GSTIN='+ GSTIN + '&period=' + period + '&caseReportingDate=' + caseReportingDate;
		// 	$.ajax({url: '/checkLoginStatus',
		// 		method: 'get',
		// 		async: false,
		// 		success: function(result){
		// 			const myJSON = JSON.parse(result);
		// 			if(result=='true'){
		// 				$("#showCaseAssignmentDetails").load(link, function(response, status, xhr){
		// 					if(status == 'success'){
		// 							$("#showAssignmentModal").modal('show');
		// 					}else{
		// 						console.log("failed");
		// 					}
		// 				});
		// 			} else if(result=='false'){
		// 				window.location.reload();
		// 			}
		// 		}         
		// 	});
		// }

		function showCaseDetails(GSTIN, period, caseReportingDate) {
			const hidden_form = document.createElement('form');
			hidden_form.method = 'POST';

			hidden_form.action = '/enforcement_fo/update_enforcement_case';

			const hidden_input1 = document.createElement('input');
			hidden_input1.type = 'hidden';
			hidden_input1.name = 'GSTIN';
			hidden_input1.value = GSTIN;

			const hidden_input2 = document.createElement('input');
			hidden_input2.type = 'hidden';
			hidden_input2.name = 'period';
			hidden_input2.value = period;

			const hidden_input3 = document.createElement('input');
			hidden_input3.type = 'hidden';
			hidden_input3.name = 'caseReportingDate';
			hidden_input3.value = caseReportingDate;

			const hidden_input4 = document.createElement('input');
			hidden_input4.type = 'hidden';
			hidden_input4.name = '${_csrf.parameterName}';
			hidden_input4.value = '${_csrf.token}';

			hidden_form.appendChild(hidden_input1);
			hidden_form.appendChild(hidden_input2);
			hidden_form.appendChild(hidden_input3);
			hidden_form.appendChild(hidden_input4);

			document.body.appendChild(hidden_form);

			$.confirm({
				title: 'Confirm!',
				content: 'Do you want to proceed ahead to view the case details?',
				buttons: {
					submit: function () {
						hidden_form.submit();
					},
					close: function () {
						$.alert('Canceled!');
					}
				}
			});
		} 

		function submitForApproval(GSTIN, period, caseReportingDate, needApproval, caseId) {
			const hidden_form_data = {
				GSTIN: GSTIN,
				period: period,
				caseReportingDate: caseReportingDate,
				needApproval: needApproval,
				caseId: caseId,
				'${_csrf.parameterName}': '${_csrf.token}'
			};
			var contenttext;
			if(needApproval == 'yes'){
				contenttext = 'Do you want to proceed ahead to submit the case for Supervisory Officer permission?';
			} else {
				contenttext = 'Do you want to proceed ahead for investigation without Supervisory Officer permission?';
			}

			$.confirm({
				title: 'Confirm!',
				content: contenttext,
				buttons: {
					submit: function () {
						$.ajax({
							url: '/enforcement_fo/caseForApproval',
							method: 'POST',
							data: hidden_form_data,
							success: function (response) {
								if (response.success) {
									$.alert(response.message);
									// Removing the row
									$(`tr[data-gstin=` + GSTIN + `][data-period=` + period + `][data-reporting-date=` + caseReportingDate + `]`).remove();
								} else {
									$.alert(response.message);
								}
								$("#showAssignmentModal").modal('hide');
							},
							error: function () {
								$.alert('Error in submission! Please check your connection.');
								$("#showAssignmentModal").modal('hide');
							}
						});
					},
					close: function () {
						$.alert('Canceled!');
					}
				}
			});
		}
	</script>
</body>
</html>