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
<title>MCM DAR case</title>
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
		<jsp:include page="../../fo/transfer_popup.jsp" />
		<jsp:include page="../../layout/sidebar.jsp" />
		<div class="content-wrapper">
			<section class="content-header">
				<div class="container-fluid">
					<div class="row mb-2">
						<div class="col-sm-6 row">
							<div class="col-sm-1">
								<a href="/mcm/${tabUrl}">
									<div class="btn btn-info">
										<i class="fas fa-arrow-left nav-icon"></i>
									</div>
								</a>
							</div>
							<div class="col-sm-11">
								<h1>Submittted DAR Details</h1>
							</div>
						</div>
						<div class="col-sm-6">
							<ol class="breadcrumb float-sm-right">
								<li class="breadcrumb-item"><a href="/mcm/dashboard">Home</a></li>
								<li class="breadcrumb-item"><a href="/mcm/${tabUrl}">${tabName}</a></li>
								<li class="breadcrumb-item active">Submittted DAR Details</li>
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
									<h3 class="card-title">Case Details</h3>
								</div>
								<div class="card-body">
									<jsp:include page="../l3/auditCaseStatusHeaderPannel.jsp" />

									<div style="background-color: #f1f1f1; border-radius: 3px; box-shadow: 2px 2px 4px 1px #8888884d; padding: 10px;">
										<p class="h5">DAR</p>
										<hr>
										<form method="POST" name="caseUpdationActionForm" id="caseUpdationActionForm" action="/mcm/update_dar_case" enctype="multipart/form-data" >
											<div class="row">
												<div class="col-lg-12">
													<div class="row">
														<div class="col-lg-6">
															<div class="form-group row">
																<label for="workingDate" class="col-form-label col-sm-3">Date</label>
																<div class="col-sm-4">
																	<!-- <c:set var="isoDate"><fmt:formatDate value='${darDetails.actionDate}' pattern='yyyy-MM-dd'/></c:set> -->
																	<input type="date" class="form-control" id="workingDate" name="workingDate" required/>
																</div>
															</div>
														</div>
														<div class="col-lg-6">
															<div class="form-group row">
																	<label class="col-form-label col-lg-2">Nil DAR</label>
																	<input type="text" id="nilDar" name="nilDar" class="form-control col-md-4" value="${darDetails.nilDar}" readonly>
															</div>
														</div>
														<div class="col-lg-12" id="inputDiv"></div>
														<div class="col-lg-12">
															<div class="form-group">
																	<label class="col-form-label">Other remarks<span style="color: red;"> *</span><!--<span id="comment_span" style="color: red; float: right; display: none;">&nbsp;*</span> --></label>
																	<input type="text" id="comment" name="comment" class="form-control col-md-12" placeholder="Please enter your other remarks on this DAR" maxlength="100" title="Please Enter you remarks within 100 letters" required>
															</div>
														</div>
														<div class="col-lg-12">
															<c:if test="${not empty darDetails.commentFromMcmOfficer}">
																<div class="col-md-12">
																	<label for="previous_comment_from_mcm">Previously submitted other remarks</label>
																	<textarea type="text" class="form-control inputDiv comment"
																		placeholder="Please enter your remarks" readonly>${darDetails.commentFromMcmOfficer}</textarea>
																</div>
															</c:if>
														</div>
														<div class="col-lg-12">
															<c:if test="${not empty darDetails.commentFromL2Officer}">
																<div class="col-md-12">
																	<label for="previous_comment_from_mcm">Remarks from allocation officer</label>
																	<textarea type="text" class="form-control inputDiv comment"
																		placeholder="Please enter your remarks" readonly>${darDetails.commentFromL2Officer}</textarea>
																</div>
															</c:if>
														</div>
													</div>
												</div>
												<div class="col-lg-12 alert alert-danger alert-dismissible fade show" role="alert" id="message1" style="max-height: 500px; overflow-y: auto; display: none;">
													<span id="alertMessage1"></span>
													<button type="button" class="close" data-dismiss="alert" aria-label="Close">
														<span aria-hidden="true">&times;</span>
													</button>
												</div>
												<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
												<input type="hidden" name="caseId" id="caseId" value="${auditCaseDetails.caseId}" />
												<input type="hidden" name="updateStatusId" id="updateStatusId" value="${darDetails.action.id}" />
											</div>
											<hr>
											<div class="row">
												<div class="col-lg-12">
													<div class="float-right">
														<button type="submit" id="submit_button_approve" value="recommendation for approval" class="btn btn-primary">Approval Recommended</button>
														<button type="submit" id="submit_button_reject" value="recommendation for raising query" class="btn btn-danger">Raise Query</button>
														<!-- <button type="submit" id="submit_button" value="recommendation for approval" class="btn btn-primary">Approval Recommended</button> -->
													</div>
												</div>
											</div>
											<input type="hidden" id="darApproval" name="darApproval"/>
										</form>
									</div>
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
		$(document).ready(function() {
				var selectedOption = '${darDetails.nilDar}';

				var link = '/mcm/nil_dar_option?nilDar=' + selectedOption + "&caseId=" + '${auditCaseDetails.caseId}';

				$.ajax({url: '/checkLoginStatus',
					method: 'get',
					async: false,
					success: function(result){
						const myJSON = JSON.parse(result);
						if(result=='true'){
							$("#inputDiv").load(link, function(response, status, xhr){
								if(status == 'success'){
									// console.log("success");
								}else{
									alert("We are facing some internal error. Please refresh the page!");
								}
							});
						} else if(result=='false'){
							window.location.reload();
						}
					}
				});
		});

		// Assign button value to input field
		$(document).ready(function () {
			$("button[type='submit']").on("click", function () {
				$("#darApproval").val($(this).val());
			});
		});

		$('form').on('submit', function(oEvent) {
			oEvent.preventDefault();
			$.confirm({
				title : 'Confirm!',
				content : 'Do you want to proceed ahead with ' + $("#darApproval").val() +' on the submitted DAR details?',
				buttons : {
					submit : function() {
						oEvent.currentTarget.submit();
					},
					close : function() {
						$.alert('Canceled!');
					}
				}
			});
		});

		$(document).ready(function () {
			if ('${auditCaseDetails.assignedFrom}' == 'MCM' && '${auditCaseDetails.assignTo}' == 'L2') {
				var actionDate = new Date("${darDetails.actionDate}");

				var formattedDate = actionDate.getFullYear() + '-' + 
					('0' + (actionDate.getMonth() + 1)).slice(-2) + '-' + 
					('0' + actionDate.getDate()).slice(-2);
				document.getElementById("workingDate").value = formattedDate;
			}

			$("#workingDate").on("change", function () {
				var effectiveDate = $("#workingDate").val();
				var now = new Date();
				var previousStatusDate = new Date("${previousStatusDate}");
				now.setHours(23, 59, 59, 999);
				const givenDate = new Date(effectiveDate);

				if (givenDate > now) {
					$("#workingDate").val('');

					var x = document.getElementById("message1");
					if (x.style.display === "none") {
						$("#alertMessage1").html("Future date is not allowed").css("color", "#fffff");
						x.style.display = "block";
						showDiv();
					}
				} else if (givenDate < previousStatusDate) {
					$("#workingDate").val('');

					var x = document.getElementById("message1");
					if (x.style.display === "none") {
						$("#alertMessage1").html("The entered date cannot be before " + formatDate(previousStatusDate)).css("color", "#fffff");
						x.style.display = "block";
						showDiv();
					}
				}
			});
		});

		function showDiv() {
			$("#message1").fadeTo(2000, 500).slideUp(500, function () {
				$("#message1").slideUp(500);
			});
		}

		function formatDate(date) {
			var day = String(date.getDate()).padStart(2, '0');
			var month = String(date.getMonth() + 1).padStart(2, '0');
			var year = date.getFullYear();
			return day + "-" + month + "-" + year;
		}
	</script>
		
</body>
</html>