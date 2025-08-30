<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="en">

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>HP GST | Dashboard</title>
<!-- <link rel="stylesheet" href="/static/dist/css/googleFront/googleFrontFamilySourceSansPro.css"> -->
<link rel="stylesheet"
	href="/static/plugins/fontawesome-free/css/all.min.css">
<link rel="stylesheet"
	href="/static/dist/ionicons-2.0.1/css/ionicons.min.css">
<link rel="stylesheet"
	href="/static/plugins/tempusdominus-bootstrap-4/css/tempusdominus-bootstrap-4.min.css">
<link rel="stylesheet"
	href="/static/plugins/icheck-bootstrap/icheck-bootstrap.min.css">
<link rel="stylesheet" href="/static/plugins/jqvmap/jqvmap.min.css">
<link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
<link rel="stylesheet"
	href="/static/plugins/overlayScrollbars/css/OverlayScrollbars.min.css">
<link rel="stylesheet"
	href="/static/plugins/daterangepicker/daterangepicker.css">
<link rel="stylesheet"
	href="/static/plugins/summernote/summernote-bs4.min.css">
<link rel="stylesheet"
	href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
<link rel="stylesheet"
	href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
<link rel="stylesheet"
	href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
<link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
<link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">

<style>

/* Modal container */
.modal {
  width: 100%;
  height: 100%;
  overflow: auto;
}

/* Modal content */
.modal-content {
  width: 90%;
  max-height: 90vh;
  overflow: auto;
}


</style>

</head>
<body class="hold-transition sidebar-mini layout-fixed">
	<div class="wrapper">
		<div
			class="preloader flex-column justify-content-center align-items-center">
			<img class="animation__shake" src="/static/dist/img/AdminLTELogo.png"
				alt="AdminLTELogo" height="60" width="60">
		</div>
		<jsp:include page="../layout/header.jsp" />
		<jsp:include page="../layout/sidebar.jsp" />
		<div class="content-wrapper">
			<div class="content-header">
				<div class="container-fluid">
					<div class="row mb-2">
						<div class="col-sm-6">
							<h1 class="m-0">Period Wise Status</h1>
						</div>
						<div class="col-sm-6">
							<ol class="breadcrumb float-sm-right">
								<li class="breadcrumb-item"><a href="/vw/dashboard">Home</a></li>
				                <li class="breadcrumb-item"><a href="/vw/mis">MIS</a></li>
				                <li class="breadcrumb-item">Report</li>
							</ol>
						</div>
					</div>
				</div>
			</div>
			
			<section class="content">
				<div class="container-fluid">
		
		    <table id="foCaseStageList" class="table table-bordered table-striped"
			style="width: 100%;">
			<thead>
				<tr>
					<th rowspan="2" style="text-align: center;">Period</th>
					<th rowspan="2" style="text-align: center;">Yet to be Acknowledge</th>
					<th rowspan="2" style="text-align: center;">Yet to Be Initiated</th>
					
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
				<c:forEach items="${consolidateCategoryWiseDataList}" var="object"
					varStatus="loop">
					<tr>
					
						<td style="text-align: center;" ><c:out value="${object.period}" /></td>
						<td style="text-align: center;" ><a class="fa fa-eye"
											aria-hidden="true" style="cursor: pointer; color: #4682b4"
											onclick="drillDown('notAcknowledge', '${loop.count}')" >${object.notAcknowledge}</a></td>
											
						<td style="text-align: center;" ><a class="fa fa-eye"
											aria-hidden="true" style="cursor: pointer; color: #4682b4"
											onclick="drillDown('notApplicable', '${loop.count}')">${object.notApplicable}</a></td>
						
						<td style="text-align: center;" > <a class="fa fa-eye"
											aria-hidden="true" style="cursor: pointer; color: #4682b4"
											onclick="drillDown('dRC01AIssued', '${loop.count}')">${object.dRC01AIssued}</a></td>
						
						<td style="text-align: center;" > <a class="fa fa-eye"
											aria-hidden="true" style="cursor: pointer; color: #4682b4"
											onclick="drillDown('dRC01Issued', '${loop.count}')">${object.dRC01Issued}</a></td>
						
						<td style="text-align: center;" ><a class="fa fa-eye"
											aria-hidden="true" style="cursor: pointer; color: #4682b4"
											onclick="drillDown('aSMT10Issued', '${loop.count}')">${object.aSMT10Issued}</a></td>
						
						<td style="text-align: center;" ><a class="fa fa-eye"
											aria-hidden="true" style="cursor: pointer; color: #4682b4"
											onclick="drillDown('caseDropped', '${loop.count}')">${object.caseDropped}</a></td>
						
						<td style="text-align: center;" ><a class="fa fa-eye"
											aria-hidden="true" style="cursor: pointer; color: #4682b4"
											onclick="drillDown('demandCreatedByDrc07', '${loop.count}')">${object.demandCreatedByDrc07}</a></td>
								
						<td style="text-align: center;" ><a class="fa fa-eye"
											aria-hidden="true" style="cursor: pointer; color: #4682b4"
											onclick="drillDown('partialVoluntaryPaymentRemainingDemand', '${loop.count}')">${object.partialVoluntaryPaymentRemainingDemand}</a></td>
						
						<td style="text-align: center;" ><a class="fa fa-eye"
											aria-hidden="true" style="cursor: pointer; color: #4682b4"
											onclick="drillDown('demandCreatedHoweverDischargedViaDrc03', '${loop.count}')">${object.demandCreatedHoweverDischargedViaDrc03}</a></td>
						
					</tr>
				</c:forEach>

			</tbody>
		</table> 

        <div class="modal fade" id="modal1" tabindex="-1"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="max-width: 2000px;">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel">Detailed View</h5>
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body" id="exampleModal"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-dismiss="modal">Close</button>
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
	<script
		src="/static/plugins/tempusdominus-bootstrap-4/js/tempusdominus-bootstrap-4.min.js"></script>
	<script src="/static/plugins/summernote/summernote-bs4.min.js"></script>
	<script
		src="/static/plugins/overlayScrollbars/js/jquery.overlayScrollbars.min.js"></script>
	<script src="/static/dist/js/adminlte.js"></script>
	<script src="/static/dist/js/googleCharts/googleChartsLoader.js"></script>
	<script src="/static/dist/js/pages/dashboard.js"></script>
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

	<script type="text/javascript">
  document.addEventListener('contextmenu', function(e) {
		e.preventDefault();
	});
	document.addEventListener('keydown', function(e) {
		if (e.ctrlKey && e.key === 'u') {
			e.preventDefault();
		}
	});
  </script>
    
  <script>
    
    function drillDown(category, index) {
     
     var link = 'landingDrillDownPeriodWiseList?category=' + category + '&index=' + index;
		
	    $.ajax({url: '/checkLoginStatus',
			method: 'get',
			async: false,
			success: function(result){
				const myJSON = JSON.parse(result);
				if(result=='true'){

					$('#exampleModal').load(link, function(response, status, xhr){
						if(status == 'success'){
								$('#modal1').modal('show');
								console.log("showing");
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
    $("#foCaseStageList").DataTable({   
      "responsive": true,
      "lengthChange": false,
      "autoWidth": false
    });


	$(function() {
		$("#example1").DataTable({
			"responsive" : true,
			"lengthChange" : false,
			"autoWidth" : true,
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


	function checkForm(){

        alert("dashboard");
		
		$("#category").prop("required", true);
		$("#financialyear").prop("required", false);
		$("#view").prop("required", true);
		
	}   
	
	
  </script>
</body>

</html>