<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Verify Recovery List</title>

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
		<jsp:include page="../layout/header.jsp" />
  	<!-- <jsp:include page="../layout/confirmation_popup.jsp"/> -->
		<jsp:include page="../hq/raisequery_popup.jsp" />
			<jsp:include page="../hq/approve_popup.jsp" />
		<jsp:include page="../layout/sidebar.jsp" />
		<div class="content-wrapper">
			<section class="content-header">
				<div class="container-fluid">
					<div class="row mb-2">
						<div class="col-sm-6">
							<h1>Verify Recovery List</h1>
						</div>
						<div class="col-sm-6">
							<ol class="breadcrumb float-sm-right">
								<li class="breadcrumb-item"><a>Home</a></li>
								<li class="breadcrumb-item active">Verify Recovery List</li>
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
									<h3 class="card-title">Verify Recovery List</h3>
								</div>
								<div class="card-body card">
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
									<table id="dataListTable" class="table table-bordered table-striped table-responsive">
									<thead>
										<tr>
											<th style="text-align: center; vertical-align: middle;">GSTIN</th>
											<th style="text-align: center; vertical-align: middle;">Taxpayer Name</th>
											<th style="text-align: center; vertical-align: middle;">Jurisdiction</th>
											<th style="text-align: center; vertical-align: middle;">Period</th>
											<th style="text-align: center; vertical-align: middle;">Reporting Date<br>(DD-MM-YYYY)</th>
											<th style="text-align: center; vertical-align: middle;">Indicative Value(₹)</th>
											<th style="text-align: center; vertical-align: middle;">Action Status</th>
											<th style="text-align: center; vertical-align: middle;">Case ID</th>
											<th style="text-align: center; vertical-align: middle;">Case Stage</th>
											<th style="text-align: center; vertical-align: middle;">Case Stage ARN</th>
											<th style="text-align: center; vertical-align: middle;">Amount(₹)</th>
											<th style="text-align: center; vertical-align: middle;">Recovery Stage</th>
											<th style="text-align: center; vertical-align: middle;">Recovery Stage ARN</th>
											<th style="text-align: center; vertical-align: middle;">Recovery Via DRC03(₹)</th>
											<th style="text-align: center; vertical-align: middle;">Recovery Against Demand(₹)</th>
											<th style="text-align: center; vertical-align: middle;">Supporting file</th>
											<th style="text-align: center; vertical-align: middle;">Action</th>
										</tr>
									</thead>
									<tbody>
									    <c:forEach items="${listofCases}" var="item">             
										<tr>
											<td>${item.GSTIN_ID}</td>
											<td>${item.taxpayerName}</td>
											<td>${item.circle}</td>
											<td>${item.period_ID}</td>
											<td><fmt:formatDate value="${item.date}"  pattern="dd-MM-yyyy" /></td>
											<td>${item.indicativeTaxValue}</td>
											<td>${item.actionStatusName}</td>
											<td>${item.caseId}</td>
											<td>${item.caseStageName}</td>
											<td>${item.caseStageArn}</td>
											<td>${item.demand}</td>
											<td>${item.recoveryStageName}</td>
											<td>${item.recoveryStageArnStr}</td>
											<td>${item.recoveryByDRC3}</td>
											<td>${item.recoveryAgainstDemand}</td>
											<td>
												<c:if test="${not empty item.uploadedFileName}">
													<a href="/hq/downloadFOFile?fileName=${item.uploadedFileName}"><button type="button" onclick="" class="btn btn-primary"><i class="fas fa-download" style="font-size:20px"></i></button></a>
												</c:if>
												<c:if test="${empty item.uploadedFileName}">
													<button type="button" onclick="" class="btn btn-primary" disabled><i class="fas fa-download" style="font-size:20px"></i></button>
												</c:if>
											</td>
											<td>
											
										    <button type="button" class="btn btn-primary"  onclick="approveCase('${item.GSTIN_ID}' , '${item.caseReportingDate_ID}' , '${item.period_ID}')" > Approve </button> 
											      
											<button type="button" class="btn btn-danger mt-1"  onclick="raiseQueryCase('${item.GSTIN_ID}' , '${item.caseReportingDate_ID}' , '${item.period_ID}')" > Raise Query </button> 
								             
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
		<jsp:include page="../layout/footer.jsp" />
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


		function approveCase(gstno, date, period) {

			$("#app_gstno").val(gstno);
			$("#app_date").val(date);
			$("#app_period").val(period);

			$("#confirmationApproveModal").modal('show');
		}
		
        
		function raiseQueryCase(gstno, date, period) {

			$("#gstno").val(gstno);
			$("#date").val(date);
			$("#period").val(period);

			$("#raiseQueryModal").modal('show');
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

		$(function() {
			$("#transferBtn").on("click", function() {
				$("#transferModal").modal('show');
			});

			$("#okayBtn").on("click", function() {
				console.log("save");
				$("#confirmationModal").modal('hide');
			});

			$("#cancelBtn").on("click", function() {
				console.log("failed");
				$("#confirmationModal").modal('hide');
			});

		});

		
		$('#raiseQueryModal').on('hidden.bs.modal', function () {
		    
		    $('#recoveryForm')[0].reset();
		    
		});


		$('#recoveryForm').on('submit', function(oEvent) {
			
			oEvent.preventDefault();
			
			$.confirm({
				title : 'Confirm!',
				content : 'Do you want to proceed ahead with Raise Query the case?',
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


	    $('#approveForm').on('submit', function(oEvent) {
			
			oEvent.preventDefault();
			
			$.confirm({
				title : 'Confirm!',
				content : 'Do you want to proceed ahead with Approve the case?',
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

		
		
	</script>
</body>
</html>
