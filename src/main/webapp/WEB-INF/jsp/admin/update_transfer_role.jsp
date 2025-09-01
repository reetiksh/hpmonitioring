<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>HP GST | Transfer User Roles</title>
		<link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">
		<!-- <link rel="stylesheet" href="/static/dist/css/googleFront/googleFrontFamilySourceSansPro.css"> -->
		<link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
		<link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
		<link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
		<link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
		<link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
		<link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
		<link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
		<link rel="stylesheet" href="/static/plugins/select2/select2.min.css">
		<link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
		<link rel="stylesheet" href="/static/dist/css/sweetalert2.min.css">
		<style>
			.bootstrap-select .dropdown-menu {
				max-height: 350px;
			}
		</style>
	</head>
<body class="hold-transition sidebar-mini layout-fixed">
<div class="wrapper">
	<div class="preloader flex-column justify-content-center align-items-center">
		<img class="animation__shake" src="/static/dist/img/AdminLTELogo.png" alt="AdminLTELogo" height="60" width="60">
	</div>
	<jsp:include page="../layout/header.jsp"/>
	<jsp:include page="../hq/transfer_popup.jsp"/>
	<jsp:include page="../layout/sidebar.jsp"/>
	<div class="content-wrapper">
		<div class="content-header">
			<div class="container-fluid">
				<div class="row mb-2">
					<div class="col-sm-6">
						<h1 class="m-0">Transfers Scheduled</h1>
					</div>
					<div class="col-sm-6">
						<ol class="breadcrumb float-sm-right">
							<li class="breadcrumb-item"><a href="/admin/dashboard">Home</a></li>
							<li class="breadcrumb-item active">Update Transfer Role</li>
						</ol>
					</div>
				</div>
			</div>
		</div>
		<section class="content">
				<div class="container-fluid">
					<div class="row">
						<div class="col-12 alert alert-danger alert-dismissible fade show" role="alert" id="message" style="max-height: 500px; overflow-y: auto; display: none;">
							<span id="alertMessage"></span>
							<button type="button" class="close" data-dismiss="alert" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<!-- <c:if test="${not empty uploadDataError}">
							<div class="col-12 alert alert-danger alert-dismissible fade show" id="messageOutput"  role="alert" style="max-height: 500px; overflow-y: auto;">
								<c:forEach items="${uploadDataError}" var="row">
									<strong>${row}</strong><br>
								</c:forEach>
								<button type="button" class="close" data-dismiss="alert" aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
							</div>
						</c:if> -->
						<c:if test="${not empty uploadDataInfo}">
							<div class="col-12 alert alert-success alert-dismissible fade show" id="messageOutput"  role="alert" style="max-height: 500px; overflow-y: auto;">
								<c:forEach items="${uploadDataInfo}" var="row">
									<strong>${row}</strong><br>
								</c:forEach>
								<button type="button" class="close" data-dismiss="alert" aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
							</div>
						</c:if>
						<div class="col-12">
							<div class="card card-primary">
								<div class="card-header">
									<h3 class="card-title">Transfers Scheduled <i class="fas fa-receipt nav-icon"></i></h3>
								</div>
								<c:if test="${allTransferRolesList.size() gt 0}">
									<div class="card-body">
										<table id="example1" class="table table-bordered table-striped">
											<thead>
											<tr>
												<th>Transfer From</th>
												<th>Role : Location</th>
												<th>Effective Date</th>
												<th>Transfer To</th>
												<th>Requested By</th>
												<th>Action</th>
											</tr>
											</thead>
											<tbody>
												<c:forEach items="${allTransferRolesList}" var="row" varStatus="status">
													<tr>
														<td>
															<c:out value="${row.userRoleMapping.userDetails.firstName}" />
															<c:out value="${ row.userRoleMapping.userDetails.middleName}" />
															<c:out value="${ row.userRoleMapping.userDetails.lastName}" />
															<br>
															<c:out value="${[row.userRoleMapping.userDetails.loginName]}" />
															<c:out value="${ row.userRoleMapping.userDetails.designation.designationAcronym}" />
														</td>
														<td>
															<c:out value="${row.userRoleMapping.userRole.roleName}" /> : 
															<c:if test = "${row.userRoleMapping.circleDetails.circleId ne 'NA'}">
																<c:out value="${row.userRoleMapping.circleDetails.circleName}" /> (Circle)
															</c:if>
															<c:if test = "${row.userRoleMapping.zoneDetails.zoneId ne 'NA'}">
																<c:out value="${row.userRoleMapping.zoneDetails.zoneName}" /> (Zone)
															</c:if>
															<c:if test = "${row.userRoleMapping.stateDetails.stateId ne 'NA'}">
																<c:out value="${row.userRoleMapping.stateDetails.stateName}" /> (State)
															</c:if>
														</td>
														<td>
															<fmt:formatDate value="${row.actionDate}" pattern="dd-MM-yyyy" />
														</td>
														<td>
															<c:out value="${row.transferToUser.firstName}" />
															<c:out value="${ row.transferToUser.middleName}" />
															<c:out value="${ row.transferToUser.lastName}" />
															<br>
															<c:out value="${[row.transferToUser.loginName]}" />
															<c:out value="${ row.transferToUser.designation.designationAcronym}" />
														</td>
														<td>
															<c:out value="${row.requestedByUserId.firstName}" />
															<c:out value="${ row.requestedByUserId.middleName}" />
															<c:out value="${ row.requestedByUserId.lastName}" />
															<br>
															<c:out value="${[row.requestedByUserId.loginName]}" />
															<c:out value="${ row.requestedByUserId.designation.designationAcronym}" />
														</td>
														<td style="text-align: center; vertical-align: middle;">
															<button type="button" title="Edit User" class="btn btn-info" onclick="updateTransferRole('${row.id}')"><i class="fas fa-edit nav-icon"></i></button>
															<button type="button" title="Edit User" class="btn btn-danger" onclick="deleteTransferRole('${row.id}')"><i class="fas fa-trash nav-icon"></i></button>
														</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
										<br>
									</div>
								</c:if>
								<c:if test="${empty allTransferRolesList}">
									<br>
									<div class="col-12" style="text-align: center;">
										<i class="fa fa-info-circle" style="font-size:100px;color:rgb(97, 97, 97)" aria-hidden="true"></i><br>
										<span style="font-size:35px;color:rgb(97, 97, 97)">No Transfer Request Available</span>
									</div>
								</c:if>
							</div>
						</div>
					</div>
				</div>
			</section>
	</div>
	<div class="modal fade" id="viewTransferRoleModal" tabindex="-1" role="dialog" aria-labelledby="viewTransferRoleModalTitle" aria-hidden="true" >
		<div class="modal-dialog modal-dialog-centered modal-lg" role="document" >
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="updateSummaryViewModalTitle">Update Transfer Role </h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">    
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body" id="viewTransferRole" >
				</div>
			</div>
		</div>
	</div>

	<jsp:include page="../layout/footer.jsp"/>
	<aside class="control-sidebar control-sidebar-dark">
	</aside>
</div>
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/plugins/bs-custom-file-input/bs-custom-file-input.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>
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
<script src="/static/dist/js/jquery-confirm.min.js"></script>
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script src="/static/plugins/select2/select2.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>
<script src="/static/dist/js/sweetalert2.all.min.js"></script>

<script>
	function showDiv(){
		$("#message").fadeTo(2000, 500).slideUp(500, function() {
			$("#message").slideUp(500);
		});
	}
</script>
<script>
	$(document).ready(function() {
		$("#messageOutput").fadeTo(10000, 500).slideUp(500, function() {
			$("#messageOutput").slideUp(500);
		});
	});
</script>

<script>
  $(function () {
		$("#example1").DataTable({
		"responsive": true, "lengthChange": false, "autoWidth": false
		}).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
	});
</script>

<script>
	function updateTransferRole(transferId){
	  $.ajax({url: '/checkLoginStatus',
			method: 'get',
			async: false,
			success: function(result){
				const myJSON = JSON.parse(result);
				if(result=='true'){
					$("#viewTransferRole").load('/admin/view_update_transfer_role?id='+transferId, function(response, status, xhr){
						if(status == 'success'){
								$("#viewTransferRoleModal").modal('show');
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

	function deleteTransferRole(transferId){

		$.confirm({
			title : 'Confirm!',
			content : 'Do you want to proceed ahead with transfer role deletion?',
			buttons : {
				submit : function() {
					const hidden_form = document.createElement('form'); 
					hidden_form.method = 'post'; 
					hidden_form.action = "/admin/delete_transfer_role"; 
						
					const hidden_input1 = document.createElement('input'); 
					hidden_input1.type = 'hidden'; 
					hidden_input1.name = 'trnasferId';  
					hidden_input1.value = transferId; 

					const hidden_input2 = document.createElement('input'); 
					hidden_input2.type = 'hidden'; 
					hidden_input2.name = '${_csrf.parameterName}'; 
					hidden_input2.value = '${_csrf.token}'; 

					hidden_form.appendChild(hidden_input1); 
					hidden_form.appendChild(hidden_input2); 
					document.body.appendChild(hidden_form); 

					hidden_form.submit();
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
