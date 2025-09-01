<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
	<jsp:include page="../layout/sidebar.jsp"/>
	<div class="content-wrapper">
		<div class="content-header">
			<div class="container-fluid">
				<div class="row mb-2">
					<div class="col-sm-6">
						<h1 class="m-0">Transfer User Role</h1>
					</div>
					<div class="col-sm-6">
						<ol class="breadcrumb float-sm-right">
							<li class="breadcrumb-item"><a href="/admin/dashboard">Home</a></li>
							<li class="breadcrumb-item active">Transfer User Role</li>
						</ol>
					</div>
				</div>
			</div>
		</div>
		<section class="content">
				<div class="container-fluid">
					<div class="row">
						<div class="col-12">
							<div class="card card-primary">
								<div class="card-header">
									<h3 class="card-title">Transfer User Role <i class="fas fa-exchange-alt nav-icon"></i></h3>
								</div>
								<div class="card-body">
									<div class="row">
										<div class="col-md-4">
											<label>Select Officer Name <span style="color: red;"> *</span><span id="select_user_error"></span></label>
											<select class="form-control selectpicker" data-live-search="true" id="userId" name="userId">
												<option value="" disabled selected>Select officer name</option>
												<c:forEach items="${userList}" var="user">
													<c:if test="${userId eq user.userId}">
														<option value="${user.userId}" data-subtext="${user.designation.designationAcronym}" selected>${user.firstName} ${user.middleName} ${user.lastName} [${user.loginName}]</option>
													</c:if>
													<c:if test="${userId ne user.userId}">
														<option value="${user.userId}" data-subtext="${user.designation.designationAcronym}">${user.firstName} ${user.middleName} ${user.lastName} [${user.loginName}]</option>
													</c:if>
												</c:forEach>
											</select>
										</div>
										<div class="col-md-2">
											<label>Select Role<span id="select_user_error"></span></label>
											<select class="form-control selectpicker" id="userRole" name="userRole"  data-live-search="true" title="Select Role" multiple>
												<c:if test="${!empty allUserRole}">
													<c:forEach items="${allUserRole}" var="userRole">
														<option value="${userRole.id}">${userRole.roleName}</option>
													</c:forEach>
												</c:if>
												<c:if test="${empty allUserRole}">
													<option value="" disabled>No Role Found</option>
												</c:if>
											</select>
										</div>
										<div class="col-md-5">
											<label>Select Location <span id="select_user_error"></span></label>
											<select class="form-control selectpicker" id="locationId" name="locationId" data-live-search="true"  title="Select Location" multiple>
												<optgroup label="State">
													<c:forEach items="${stateMap}" var="object">
														<option value="${object.key}">${object.value}</option>
													</c:forEach>
												</optgroup>
												<optgroup label="Zone">
													<c:forEach items="${zoneMap}" var="object">
														<option value="${object.key}">${object.value}</option>
													</c:forEach>
												</optgroup>
												<optgroup label="Circle">
													<c:forEach items="${circleMap}" var="object">
														<option value="${object.key}">${object.value}</option>
													</c:forEach>
												</optgroup>
											</select>
										</div>
										<div class="col-md-1" >
											<button type="button" class="btn btn-primary" style="margin-top: 32px; margin-left: 40px;" onclick="submitSearch()"><i class="fa fa-search" aria-hidden="true"></i></button>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="col-12 alert alert-danger alert-dismissible fade show" role="alert" id="message" style="max-height: 500px; overflow-y: auto; display: none;">
							<span id="alertMessage"></span>
							<button type="button" class="close" data-dismiss="alert" aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<c:if test="${not empty uploadDataError}">
							<div class="col-12 alert alert-danger alert-dismissible fade show" id="messageOutput"  role="alert" style="max-height: 500px; overflow-y: auto;">
								<c:forEach items="${uploadDataError}" var="row">
									<strong>${row}</strong><br>
								</c:forEach>
								<button type="button" class="close" data-dismiss="alert" aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
							</div>
						</c:if>
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
						<c:if test="${userExistingRole.size() gt 0}">
							<div class="col-12">
								<div class="card card-primary">
									<div class="card-header">
										<h3 class="card-title">Officer Roles <i class="fas fa-user-tag nav-icon"></i></h3><div style="float: right;"><i class="fas fa-user nav-icon"></i> ${userName}</div>
									</div>
									<div class="card-body">
										<table id="example1" class="table table-bordered table-striped">
											<thead>
											<tr>
												<th>Role Assigned</th>
												<th>Effected Location</th>
											</tr>
											</thead>
											<tbody>
												<c:forEach items="${userExistingRole}" var="role">
													<tr>
														<td>
															<div class="form-check">
																<input class="form-check-input" type="checkbox" value="${role.key}" name="checkboxRole" id="checkbox">
																<c:out value="${role.key}" />
															</div>
														</td>
														<td>
															<div class="form-check">
																<c:forEach items="${role.value}" var="locationMap">
																	<input class="form-check-input" type="checkbox" value="${locationMap.key}" name="checkboxLocation" id="checkbox">
																	<c:out value="${locationMap.value}" />
																	<br>
																</c:forEach>
															</div>
														</td>
													</tr>
												</c:forEach>
											</tbody>
										</table>
										<br>
										<div class="row">
											<div class="col-md-1">
												<label>Transfer To <span style="color: red;"> *</span><span id="select_transfer_user_error"></span></label>
											</div>
											<div class="col-md-4" id="transferTo" style="border-radius:3px; padding-right: 10px;">
												<select class="form-control selectpicker" data-live-search="true" id="transferToUserId" name="transferToUserId">
													<option value="" disabled selected>Select Officer Name</option>
													<c:forEach items="${userList}" var="user">
														<c:if test="${userId ne user.userId}">
															<option value="${user.userId}" data-subtext="${user.loginName}">${user.firstName} ${user.middleName} ${user.lastName} [${user.loginName}]</option>
														</c:if>
													</c:forEach>
												</select>
											</div>
											<div class="col-md-2" style="text-align: right;">
												<label>Effective Date: <span style="color: red;"> *</span><span id="select_transfer_user_error"></span></label>
											</div>
											<div class="col-md-2">
												<input type="date" class="form-control" id="effectedDate" name="effectedDate"  value="<c:out value='${effectedDate}'/>"/>
											</div>
											<div class="col-md-3" style="text-align: right;">
												<button type="button"  class="btn btn-primary" onclick="submitTransferRequest()">Submit</button>
											</div>
										</div>
									</div>
								</div>
							</div>
						</c:if>
					</div>
				</div>
			</section>
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
<script>
	function showDiv(){
		$("#message").fadeTo(2000, 500).slideUp(500, function() {
			$("#message").slideUp(500);
		});
	}
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
	$(document).ready(function () {
	    function disableBack() {window.history.forward()}
	
	    window.onload = disableBack();
	    window.onpageshow = function (evt) {if (evt.persisted) disableBack()}
	});
	    // Disable refresh
	    document.onkeydown = function (e) {
	        if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) {
	            e.preventDefault();
	           
	        }
	    };
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
	function submitSearch(){
		var userRoles = "";
		$('#userRole :selected').each(function(i, sel){ 
			if(userRoles != ''){
				userRoles = userRoles + ",";
			}
			userRoles =  userRoles + $(sel).val()
		});
		console.log(userRoles);

		var locationIds = "";
		$('#locationId :selected').each(function(i, sel){ 
			if(locationIds != ''){
				locationIds = locationIds + ",";
			}
			locationIds =  locationIds + $(sel).val()
		});
		console.log(locationIds);

		var userId = $("#userId").val();
		if(userId === '' || userId == null){
			$("#select_user_error").html("Please Select officer name !").css("color","red");
			return;
		}

		const hidden_form = document.createElement('form'); 

		hidden_form.method = 'get'; 
		
		hidden_form.action = "/admin/transfer_role"; 
			
		const hidden_input1 = document.createElement('input'); 
		hidden_input1.type = 'hidden'; 
		hidden_input1.name = 'userRoles';  
		hidden_input1.value = userRoles; 

		const hidden_input2 = document.createElement('input'); 
		hidden_input2.type = 'hidden'; 
		hidden_input2.name = 'locationIds'; 
		hidden_input2.value = locationIds; 

		const hidden_input3 = document.createElement('input'); 
		hidden_input3.type = 'hidden'; 
		hidden_input3.name = 'userId'; 
		hidden_input3.value = userId;

		const hidden_input6 = document.createElement('input'); 
		hidden_input6.type = 'hidden'; 
		hidden_input6.name = '${_csrf.parameterName}'; 
		hidden_input6.value = '${_csrf.token}'; 

		hidden_form.appendChild(hidden_input1); 
		hidden_form.appendChild(hidden_input2);
		hidden_form.appendChild(hidden_input3); 
		hidden_form.appendChild(hidden_input6); 
		document.body.appendChild(hidden_form); 

		hidden_form.submit();

	}
</script>
<script>
	function submitTransferRequest(){
		var checkedValues = [];
		var checkboxes = document.getElementsByName('checkboxRole');

		checkboxes.forEach(function(checkbox) {
			if(checkbox.checked){
				checkedValues.push(checkbox.value);
			}
		});

		var checkedRoles = checkedValues.join(',');
		console.log(checkedRoles)

		checkedValues = [];
		checkboxes = document.getElementsByName('checkboxLocation');

		checkboxes.forEach(function(checkbox) {
			if(checkbox.checked){
				checkedValues.push(checkbox.value);
			}
		});

		var checkedLocations = checkedValues.join(',');
		console.log(checkedLocations);

		if(checkedLocations.length==0 && checkedRoles.length==0){
			var x = document.getElementById("message");
			if(x.style.display === "none"){
				$("#alertMessage").html("Please select role(s) and location(s).").css("color","#fffff");
				x.style.display = "block";
				showDiv();
			}
			return;
		}

		var transferToUserId = $("#transferToUserId").val();
		var userId = '${userId}';

		if(transferToUserId === '' || transferToUserId == null){
			$("#transferTo").css("box-shadow","2px 3px 5px red");
			var x = document.getElementById("message");
			if(x.style.display === "none"){
				$("#alertMessage").html("Please select officer Name to transfer the role and cases.").css("color","#fffff");
				x.style.display = "block";
				showDiv();
			}
			return;
		}

		var effectedDate = $("#effectedDate").val();
		if(effectedDate==='' || effectedDate==null){
			$("#transferTo").css("box-shadow","2px 3px 5px transparent");
			$("#effectedDate").css("box-shadow","2px 3px 5px red");
			var x = document.getElementById("message");
			if(x.style.display === "none"){
				$("#alertMessage").html("Please provide the effective date.").css("color","#fffff");
				x.style.display = "block";
				showDiv();
			}
			return;
		}
		var now = new Date();
		now.setHours(0, 0, 0, 0);
		const givenDate = new Date(effectedDate);
		if(givenDate < now){
			$("#effectedDate").css("box-shadow","2px 3px 5px red");
			var x = document.getElementById("message");
			if(x.style.display === "none"){
				$("#alertMessage").html("Please provide valid effective date.").css("color","#fffff");
				x.style.display = "block";
				showDiv();
			}
			return;
		}

		const hidden_form = document.createElement('form'); 

		hidden_form.method = 'get'; 
			
		hidden_form.action = "/admin/transfer_role_action"; 
			
		const hidden_input1 = document.createElement('input'); 
		hidden_input1.type = 'hidden'; 
		hidden_input1.name = 'checkedRoles';  
		hidden_input1.value = checkedRoles; 

		const hidden_input2 = document.createElement('input'); 
		hidden_input2.type = 'hidden'; 
		hidden_input2.name = 'checkedLocations'; 
		hidden_input2.value = checkedLocations; 

		const hidden_input3 = document.createElement('input'); 
		hidden_input3.type = 'hidden'; 
		hidden_input3.name = 'userId'; 
		hidden_input3.value = userId;

		const hidden_input4 = document.createElement('input'); 
		hidden_input4.type = 'hidden'; 
		hidden_input4.name = 'transferToUserId'; 
		hidden_input4.value = transferToUserId;

		const hidden_input5 = document.createElement('input'); 
		hidden_input5.type = 'hidden'; 
		hidden_input5.name = 'effectedDate'; 
		hidden_input5.value = effectedDate;

		const hidden_input6 = document.createElement('input'); 
		hidden_input6.type = 'hidden'; 
		hidden_input6.name = '${_csrf.parameterName}'; 
		hidden_input6.value = '${_csrf.token}'; 

		hidden_form.appendChild(hidden_input1); 
		hidden_form.appendChild(hidden_input2);
		hidden_form.appendChild(hidden_input3); 
		hidden_form.appendChild(hidden_input4); 
		hidden_form.appendChild(hidden_input5);
		hidden_form.appendChild(hidden_input6); 
		document.body.appendChild(hidden_form); 
		$.confirm({
			title : 'Confirm!',
			content : 'Do you want to proceed ahead with role(s) transfer?',
			buttons : {
				submit : function() {
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
