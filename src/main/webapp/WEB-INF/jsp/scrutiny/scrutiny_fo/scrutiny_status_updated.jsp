<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Update Scrutiny Status</title>

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
		<jsp:include page="../../fo/transfer_popup.jsp" />
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
								<li class="breadcrumb-item"><a href="/l2/dashboard">Home</a></li>
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
							<div class="card card-primary" style="height: 100%;">
								<div class="card-header">
									<h3 class="card-title">Update Scrutiny Status</h3>
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
										<jsp:include page="../scrutiny_fo/scrutinyCaseHeaderPannel.jsp" />
										<div class="row">
											<div class="col-lg-2">
												<jsp:include page="../scrutiny_fo/updatedScrutinySidePannel.jsp" />
											</div>
											<div class="col-lg-10" id="updatedScrutinyPannelView">
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
			console.log("Hello!");
			displaySidePannelView();
		});

		function displaySidePannelView(){
			var gstin = '${mstScrutinyCases.id.GSTIN}';
			var period = '${mstScrutinyCases.id.period}';
			var dateString = '${mstScrutinyCases.id.caseReportingDate}';
            var dateParts = dateString.split(' ');
            var monthNames = {
                "Jan": 0, "Feb": 1, "Mar": 2, "Apr": 3, "May": 4, "Jun": 5,
                "Jul": 6, "Aug": 7, "Sep": 8, "Oct": 9, "Nov": 10, "Dec": 11
            };
            var month = monthNames[dateParts[1]];
            var day = parseInt(dateParts[2]);
            var year = parseInt(dateParts[5]);
            var timeParts = dateParts[3].split(':');
            var hours = parseInt(timeParts[0]);
            var minutes = parseInt(timeParts[1]);
            var seconds = parseInt(timeParts[2]);

            var reportingDate = new Date(year, month, day, hours, minutes, seconds);
            var reportingDateStr = reportingDate.toISOString();
			
			
			var link = '/scrutiny_fo/update_scrutiny_side_panel_view?pageId=6&gstinStr=' + gstin + '&periodStr=' + period + '&reportingDateStr=' + reportingDateStr;

			$.ajax({url: '/checkLoginStatus',
				method: 'get',
				async: false,
				success: function(result){
					const myJSON = JSON.parse(result);
					if(result=='true'){
						$("#updatedScrutinyPannelView").load(link, function(response, status, xhr){
							if(status == 'success'){
								console.log("success");	
								$("#caseId").val('${mstScrutinyCases.caseId}');
								$("#caseStageArn").val('${mstScrutinyCases.caseStageArn}');
								$("#demand").val('${mstScrutinyCases.amountRecovered}');
								$("#recoveryByDRC3").val('${mstScrutinyCases.recoveryByDRC03}');
								
								
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
		
		
		
		function displayDynamicSidePannelView(sidePannelViewType){
			var pageId = sidePannelViewType;
			
			var gstin = '${mstScrutinyCases.id.GSTIN}';
			var period = '${mstScrutinyCases.id.period}';
			
			var dateString = '${mstScrutinyCases.id.caseReportingDate}';
            var dateParts = dateString.split(' ');
            var monthNames = {
                "Jan": 0, "Feb": 1, "Mar": 2, "Apr": 3, "May": 4, "Jun": 5,
                "Jul": 6, "Aug": 7, "Sep": 8, "Oct": 9, "Nov": 10, "Dec": 11
            };
            var month = monthNames[dateParts[1]];
            var day = parseInt(dateParts[2]);
            var year = parseInt(dateParts[5]);
            var timeParts = dateParts[3].split(':');
            var hours = parseInt(timeParts[0]);
            var minutes = parseInt(timeParts[1]);
            var seconds = parseInt(timeParts[2]);

            var reportingDate = new Date(year, month, day, hours, minutes, seconds);
            var reportingDateStr = reportingDate.toISOString();
			
			
			var link = '/scrutiny_fo/update_scrutiny_side_panel_view?pageId=' +pageId;
			var link = '/scrutiny_fo/update_scrutiny_side_panel_view?pageId=' + pageId + '&gstinStr=' + gstin + '&periodStr=' + period + '&reportingDateStr=' + reportingDateStr;
			
			 $.ajax({url: '/checkLoginStatus',
				method: 'get',
				async: false,
				success: function(result){
					const myJSON = JSON.parse(result);
					if(result=='true'){
						$("#updatedScrutinyPannelView").load(link, function(response, status, xhr){
							if(status == 'success'){
								console.log("success");	
								$("#caseId").val('${mstScrutinyCases.caseId}');
								$("#caseStageArn").val('${mstScrutinyCases.caseStageArn}');
								$("#demand").val('${mstScrutinyCases.amountRecovered}');
								$("#recoveryByDRC3").val('${mstScrutinyCases.recoveryByDRC03}');
								$("#asmtCaseReportingDate").val('${mstScrutinyCases.id.caseReportingDate}');
								console.log('${mstScrutinyCases.id.caseReportingDate}');
								 var fileName = "${mstScrutinyCases.filePath}";
								 /* var fileDownloadLink = document.getElementById("fileDownloadLink"); */
								/* fileDownloadLink.href = "/fo/downloadUploadedPdfFile?fileName=" + encodeURIComponent(fileName);  */
								
								 $('#fileDownloadLink').attr('href', '/scrutiny_fo/downloadUploadedPdfFile?fileName=' + encodeURIComponent(fileName));
								
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
		
		
		
		
		
	</script>
	
	<script>
	function submitAsmtTenIssuance(){
		
		var recoveryStageVal = $("#recoveryStage").val();
		var gstin = "${mstScrutinyCases.id.GSTIN}";
		var period = "${mstScrutinyCases.id.period}";
		var caseReportingDate = "${mstScrutinyCases.id.caseReportingDate}";
		
		$("#asmtGstin").val(gstin);
		$("#asmtPeriod").val(period);
		$("#asmtCaseReportingDate").val(caseReportingDate);
		
		   $.confirm({
				title : 'Confirm!',
				content : 'Are you sure to submit ASMT-10 issuance ?',
				buttons : {
					submit : function() {
						$('#scrutinyProceedingDroppedModal').hide();
						setTimeout(function() {
         document.getElementById("asmtTenForm").submit();  
       }, 00);
					},
					close : function() {
						$.alert('Canceled!');
					}
				}
			});
		
	}
	
	</script>
		
</body>
</html>