<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">

<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Acknowledge Case List</title>

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
		<jsp:include page="../../cag/fo/transfer_popup.jsp" />
		<jsp:include page="../../layout/sidebar.jsp" />
		<div class="content-wrapper">
			<section class="content-header">
				<div class="container-fluid">
					<div class="row mb-2">
						<div class="col-sm-6">
							<h1>Acknowledge/Transfer Cases</h1>
						</div>
						<div class="col-sm-6">
							<ol class="breadcrumb float-sm-right">
								<li class="breadcrumb-item"><a>Home</a></li>
								<li class="breadcrumb-item active">Acknowledge Cases </li>
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
									<h3 class="card-title">Pending for Acknowledge/Transfer
										List</h3>
								</div>
								<div class="card-body card">
									<c:if test="${not empty message}">
										<div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
											<strong>${message}</strong>
											<button type="button" class="close" data-dismiss="alert" aria-label="Close">
												<span aria-hidden="true">&times;</span>
											</button>
										</div>
									</c:if>
									<c:if test="${not empty acknowlegdemessage}">
										<div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
											<strong>${acknowlegdemessage}</strong>
											<button type="button" class="close" data-dismiss="alert" aria-label="Close">
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
												<th style="text-align: center; vertical-align: middle;">Parameter</th>
												<th style="text-align: center; vertical-align: middle;">Period</th>
												<th style="text-align: center; vertical-align: middle;">Reporting Date (DD-MM-YYYY)</th>
												<th style="text-align: center; vertical-align: middle;">Indicative Value(₹)</th>
												<th style="text-align: center; vertical-align: middle;">Supporting File</th>
												<th style="text-align: center; vertical-align: middle;">Reasons</th>
												<th style="text-align: center; vertical-align: middle;">Action</th>

											</tr>
										</thead>
										<tbody>
											<c:forEach items="${listofCases}" var="total">
												<tr>
													<td style="vertical-align: middle;">${total.GSTIN_ID}</td>
													<td style="vertical-align: middle;">${total.taxpayerName}</td>
													<td style="vertical-align: middle;">${total.circle}</td>
													<td style="vertical-align: middle;">${total.category}</td>
													<td style="vertical-align: middle;">${total.parameter}</td>
													<td style="vertical-align: middle;">${total.period_ID}</td>
													<td style="vertical-align: middle;">
														<fmt:formatDate value="${total.date}" pattern="dd-MM-yyyy" />
													</td>
													<td style="vertical-align: middle;">${total.indicativeTaxValue}</td>
													<td style="text-align: center; vertical-align: middle;">
														<c:if test="${total.uploadedFileName != null}">
															<a href="/cag_fo/downloadPdfUploadedFile?fileName=${total.uploadedFileName}"><button
																	type="button" onclick="" class="btn btn-primary"><i
																		class="fas fa-download"></i></button></a>
														</c:if>
													</td>
													<td style="vertical-align: middle;">${total.remarks}</td>
													<td style="text-align: center; vertical-align: middle;">
														<c:if test="${total.actionStatus eq 0}">
															<button class="btn btn-primary acknowledgeBtn btn-block" data-gstnid="${total.GSTIN_ID}"
																data-reportingdateid="${total.caseReportingDate_ID}"
																data-periodid="${total.period_ID}"
																data-parameterid="${total.parameter}">Acknowledge</button>
														</c:if>
														<c:if test="${total.actionStatus eq 1}">
															<button class="btn btn-primary acknowledgeBtn btn-block"
																onclick="acknowledge( '${total.GSTIN_ID}' , '${total.caseReportingDate_ID}' , '${total.period_ID}' );"
																disabled>Acknowledge</button>
														</c:if>
														<button class="btn btn-primary transferBtn btn-block"
															onclick="transferBtn( '${total.GSTIN_ID}' , '${total.caseReportingDate_ID}' , '${total.period_ID}' , '${total.caseId}' , '${total.parameter}' );">Transfer</button>
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

		$(document).ready(function () {
			$(document).on('click', '.acknowledgeBtn', function (event) {
				var gstno = $(this).data('gstnid');
				var date = $(this).data('reportingdateid');
				var period = $(this).data('periodid');
				var parameter = $(this).data('parameterid');
				var row = $(this).closest('tr');
				$.confirm({
					title: 'Confirm!',
					content: 'Do you want to proceed ahead with the acknowledgement of case?',
					buttons: {
						submit: function () {

							$.ajax({
								url: '/cag_fo/acknowledge_cases',
								method: 'post',
								data: {
									gstno: gstno,
									date: date,
									period: period,
									parameter: parameter
								},
								beforeSend: function (xhr) {
									xhr.setRequestHeader('${_csrf.headerName}',
										'${_csrf.token}');
								},
								success: function (result) {

									$.alert(result);

									row.find('.acknowledgeBtn').prop('disabled', true);

								}
							});


						},
						close: function () {
							$.alert('Canceled!');
						}

					}

				});

			});

		});


		function transferBtn(gstno, date, period, jurisdiction, parameter) {

			$("#gstno").val(gstno);
			$("#date").val(date);
			$("#period").val(period);
			$("#jurisdiction").val(jurisdiction);
			$("#parameter").val(parameter);

			$("#transferModal").modal('show');
		}
	</script>

	<script>
		$(document).ready(function () {
			$("#message").fadeTo(2000, 500).slideUp(500, function () {
				$("#message").slideUp(500);
			});
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

		$(function () {
			$("#transferBtn").on("click", function () {
				$("#transferModal").modal('show');
			});

			$("#okayBtn").on("click", function () {
				console.log("save");
				$("#confirmationModal").modal('hide');
			});

			$("#cancelBtn").on("click", function () {
				console.log("failed");
				$("#confirmationModal").modal('hide');
			});

		});


		$('#transferCaseBtn').on('click', function (oEvent) {

			oEvent.preventDefault();

			debugger;

			var gstno = $("#gstno").val();
			var date = $("#date").val();
			var period = $("#period").val();
			var parameter = $("#parameter").val();
			var caseAssignedTo = $("#caseAssignedTo").val();
			var otherRemarks = $("#remarksId").val();
			var remarkOptions = $('input[name="remarkOptions"]:checked').val();

			if (caseAssignedTo === null || caseAssignedTo == "") {

				$.alert('Please choose Case Assigned To');

			} else if (remarkOptions == null) {

				$.alert('Please choose Remarks');

			} else {

				$.confirm({
					title: 'Confirm!',
					content: 'Do you want to proceed ahead with transfer of this case?',
					buttons: {
						submit: function () {
							var remarkOptions = $('input[name="remarkOptions"]:checked').val();
							var otherRemarks = $("#remarksId").val().trim();

							var caseAssign = $("#caseAssignedTo").val();

							if (caseAssign == 'NC') {

								var fileName = document.querySelector('#uploadedFile').value;

								var extension = fileName.slice((fileName.lastIndexOf(".") - 1 >>> 0) + 2);

								var input = document.getElementById('uploadedFile');

								if (input.files && input.files[0]) {

									var maxAllowedSize = 10 * 1024 * 1024;

									if (extension == 'pdf') {

										if (input.files[0].size > maxAllowedSize) {

											$.alert('Please upload max 10MB file');
											input.value = '';
										} else {

											if (remarkOptions == 2) {

												var formData = new FormData();
												formData.append('uploadedFile', $('#uploadedFile')[0].files[0]);
												formData.append('gstno', gstno);
												formData.append('date', date);
												formData.append('period', period);
												formData.append('caseAssignedTo', caseAssignedTo);
												formData.append('remarkOptions', remarkOptions);
												formData.append('parameter', parameter);

												$.ajax({
													url: '/cag_fo/transfer_cases',
													method: 'post',
													data: formData,
													processData: false,
													contentType: false,
													beforeSend: function (xhr) {
														xhr.setRequestHeader('${_csrf.headerName}',
															'${_csrf.token}');
													},
													success: function (result) {

														$("#transferModal").modal('hide');
														$.alert(result);

													}
												});

											} else {

												alert("Please choose correct Remarks");

											}

										}

									} else {

										$.alert("Please upload only pdf file");
										document.querySelector('#uploadedFile').value = '';

									}

								} else {

									$.alert("Please upload pdf file");
								}

							} else {

								if (remarkOptions == 1) {
									if (otherRemarks != '') {

										$.ajax({
											url: '/cag_fo/transfer_cases',
											method: 'post',
											data: {
												gstno: gstno,
												date: date,
												period: period,
												caseAssignedTo: caseAssignedTo,
												otherRemarks: otherRemarks,
												remarkOptions: remarkOptions,
												parameter: parameter
											},
											beforeSend: function (xhr) {
												xhr.setRequestHeader('${_csrf.headerName}',
													'${_csrf.token}');
											},
											success: function (result) {

												$("#transferModal").modal('hide');
												$.alert(result);

											}

										});

									} else {
										$.alert('Please enter Others remarks');
									}
								} else {

									$.ajax({
										url: '/cag_fo/transfer_cases',
										method: 'post',
										data: {
											gstno: gstno,
											date: date,
											period: period,
											caseAssignedTo: caseAssignedTo,
											remarkOptions: remarkOptions,
											parameter: parameter
										},
										beforeSend: function (xhr) {
											xhr.setRequestHeader('${_csrf.headerName}',
												'${_csrf.token}');
										},
										success: function (result) {

											$("#transferModal").modal('hide');
											$.alert(result);

										}

									});

								}

							}

						},
						close: function () {
							$.alert('Canceled!');
						}
					}
				});

			}

		});



		$('#transferModal').on('hidden.bs.modal', function () {

			$('#TransferForm')[0].reset();
			$('#caseAssignedTo').val('');
			$('#caseAssignedTo').selectpicker('val', '');
		});


	</script>
</body>

</html>