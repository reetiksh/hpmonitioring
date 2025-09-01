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

<link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
<link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
<link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
<link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
<link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
<link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
<link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
<link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">

</head>
<body class="hold-transition sidebar-mini">
	<div class="wrapper">
		<jsp:include page="../../layout/header.jsp" />
		<jsp:include page="../../layout/sidebar.jsp" />
		<div class="content-wrapper">
			<section class="content-header">
				<div class="container-fluid">
					<div class="row mb-2">
						<div class="col-sm-6 row">
							<div class="col-sm-1">
								<a href="/l3/update_audit_case">
									<div class="btn btn-info">
										<i class="fas fa-arrow-left nav-icon"></i>
									</div>
								</a>
							</div>
							<div class="col-sm-11">
								<h1>Case Assignment</h1>
							</div>
						</div>
						<div class="col-sm-6">
							<ol class="breadcrumb float-sm-right">
								<li class="breadcrumb-item"><a href="/l3/dashboard">Home</a></li>
								<li class="breadcrumb-item"><a href="/l3/update_audit_case">Update Audit Case</a></li>
								<li class="breadcrumb-item active">Update Audit Case Details</li>
							</ol>
						</div>
					</div>
				</div>
			</section>
			<section class="content">
				<div class="container-fluid">
					<div class="row">
						<div class="col-12">
							<div class="card card-primary" style="height: 100%;">
								<div class="card-header">
									<h3 class="card-title">Case Details</h3>
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
									<div class="col-lg-12">
										<jsp:include page="../l3/auditCaseStatusHeaderPannel.jsp" />
										<div class="col-lg-12">
											<jsp:include page="../l3/auditCaseStatusPannel2.jsp" />
										</div> 
										<div class="row">
											<!-- <div class="col-lg-2">
												<jsp:include page="../l3/auditCaseStatusPannel.jsp" />
											</div> -->
											<div class="col-lg-12" id="actionPannelDiv">
											</div>
										</div>
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
	<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
	<script src="/static/plugins/bs-custom-file-input/bs-custom-file-input.min.js"></script>
	<script src="/static/dist/js/adminlte.min.js"></script>
	<script src="/static/dist/js/bootstrap-select.min.js"></script>
	<script src="/static/dist/js/jquery-confirm.min.js"></script>
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
			showActionPannel();
		});

		function showActionPannel(){
			var caseId = '${auditCaseDetails.caseId}';
			var activeActionPannelId = '${activeActionPannelId}'

			console.log("Case Id: " + caseId + " | Active Pannel : " + activeActionPannelId);

			var link = '/l3/update_audit_case_details/activePannel?caseId=' + caseId + '&activeActionPannelId=' + activeActionPannelId;

			$.ajax({url: '/checkLoginStatus',
				method: 'get',
				async: false,
				success: function(result){
					const myJSON = JSON.parse(result);
					if(result=='true'){
						$("#actionPannelDiv").load(link, function(response, status, xhr){
							if(status == 'success'){
								console.log("success");	
								// $("#showAssignmentModal").modal('show');
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

		$('form').on('submit', function(oEvent) {
			oEvent.preventDefault();
			$.confirm({
				title : 'Confirm!',
				content : 'Do you want to proceed ahead to update the case?',
				buttons : {
					submit : function() {
						oEvent.currentTarget.submit();
					},
					close : function() {
						$.alert('Canceled!');
					}
				}
			});
		}) 
	</script>
		
</body>
</html>