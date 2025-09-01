<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>HP GST | Upload Review Cases</title>
<link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">
<!-- <link rel="stylesheet" href="/static/dist/css/googleFront/googleFrontFamilySourceSansPro.css"> -->
<link rel="stylesheet"
	href="/static/plugins/fontawesome-free/css/all.min.css">
<link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
<link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
<link rel="stylesheet" href="/static/plugins/select2/select2.min.css">
<link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
<style>
div[title]:hover::after {
	background-color: red;
}

.select2-selection__rendered {
	line-height: 31px !important;
}

.select2-container .select2-selection--single {
	height: 36px !important;
}

.select2-selection__arrow {
	height: 34px !important;
}
.delete-file {
    color: red;
    cursor: pointer;
    background-color: #f8f9fa;
    padding: 5px 10px; /* Adjust padding as needed for button-like appearance */
    border-radius: 5px; /* Rounded corners for button-like appearance */
    transition: color 0.3s ease, background-color 0.3s ease; /* Smooth transitions */
}

.delete-file:hover {
    color: blue;
}

</style>
<style>
    .btn-delete {
    background-color: red;
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
							<h1 class="m-0">Manage General Documentations</h1>
						</div>
						<div class="col-sm-6">
							<ol class="breadcrumb float-sm-right">
								<li class="breadcrumb-item"><a href="/admin/dashboard">Home</a></li>
								<li class="breadcrumb-item active">Manage General Documentations</li>
							</ol>
						</div>
					</div>
				</div>
			</div>
			<section class="content">
				<div class="container-fluid">
					<div class="row">
						<div class="col-md-12">
							<div class="card card-primary">
								<div class="card-header">
									<h3 class="card-title">
										Documentations Delete Form <i class="fas fa-trash-alt"></i>
									</h3>
								</div>
								<br>
								<form method="POST" id="deleteDocDetails"
									action="manage_documents">
									<div class="card-body">
										<div class="row">
											<input type="hidden" name="${_csrf.parameterName}"
												value="${_csrf.token}" />
											<div id="errorMessage" style="color: red;"></div>

											<div class="col-md-4">
												<div class="form-group">
													<h4>
														Select Financial Year<span style="color: red;"> *</span>
													</h4>
													<select id="financialyearNoti" name="financialyearNoti"
														class="custom-select">
														<option value="">Please Select</option>
														<c:forEach items="${yearList}" var="year">
															<option value="${year}"
																<c:if test="${year == financialyearNoti}">selected</c:if>>${year}</option>
																</c:forEach>
													</select>
												</div>
											</div>
											<div class="col-md-4">
						<div class="form-group">
							<h4>
								Select Category<span style="color: red;"> *</span>
							</h4>
							<select id="type" name="type"
								class="custom-select">
								<option value="">Please Select</option>
								<c:forEach items="${categoryList}" var="year">
									<option value="${year}"
										<c:if test="${year == type}">selected</c:if>>${year}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">
						    <div style="margin-top: 7%;text-align: center;">
							<button type="button" class="btn btn-primary" onclick="submitForm()">Search</button>
							</div>
						</div>
					</div>
											<div class="col-md-12">
														<c:if test="${not empty fileList}">
															<table class="table table-striped table-bordered" id="myTable3"
																style="color: black;">
											
																<thead class="thead-dark">
																	<tr class="header">
																	
																	<th scope="col">Category</th>
																	<th scope="col">Type</th>
																		<th scope="col">Year</th>
																		<th scope="col">Upload Date</th>
																		<th scope="col">File Name</th>
																		<th scope="col">Delete Entry</th>
																	</tr>
																</thead>
																<tbody>
														<c:forEach items="${fileList}" var="circular">
														    <tr>
														        
														        <td>${circular.category}</td>
														        <td>${circular.type}</td>
														        <td>${circular.year}</td>
														        <td>${circular.uploadDate}</td>
<td><a href="/downloadFile?fileName=${circular.fileName}&category=${circular.category}&year=${circular.year}" target="_blank"><i
										class="fas fa-file-pdf"></i> ${circular.fileName}</a></td>														       <td>
														            <button type="button" class="btn btn-secondary btn-delete" 
														                    onclick="confirmDelete('${circular.id},${circular.category},${circular.year}')">
														                <i class="fas fa-trash-alt"></i> Delete File
														            </button>
														        </td>
														    </tr>
														</c:forEach>
													</tbody>
												</table>
											</c:if>
											</div>
											<br />
											<c:forEach items="${HqUploadForm.excelErrors}"
												var="excelError">
												<span style="color: red;" class="error"><c:out
														value="${excelError}" /></span>
											</c:forEach>
											<br />
										</div>
									</div>
								</form>
							</div>
							<c:if test="${not empty successMessage}">
								<div
									class="col-12 alert alert-success alert-dismissible fade show"
									id="message" role="alert">
									<strong>${successMessage}</strong><br>
									<button type="button" class="close" data-dismiss="alert"
										aria-label="Close">
										<span aria-hidden="true">&times;</span>
									</button>
								</div>
							</c:if>
							<c:if test="${not empty errorMessage}">
								<div
									class="col-12 alert alert-danger alert-dismissible fade show"
									id="message" role="alert">
									<strong>${errorMessage}</strong><br>
									<button type="button" class="close" data-dismiss="alert"
										aria-label="Close">
										<span aria-hidden="true">&times;</span>
									</button>
								</div>
							</c:if>
						</div>
					</div>
				</div>
			</section>
		</div>
		<jsp:include page="../layout/footer.jsp" />
		<aside class="control-sidebar control-sidebar-dark"></aside>
	</div>
	<script src="/static/plugins/jquery/jquery.min.js"></script>
	<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
	<script
		src="/static/plugins/bs-custom-file-input/bs-custom-file-input.min.js"></script>
	<script src="/static/dist/js/adminlte.min.js"></script>
	<script src="/static/dist/js/bootstrap-select.min.js"></script>
	<script src="/static/plugins/select2/select2.min.js"></script>
	<script src="/static/dist/js/jquery-confirm.min.js"></script>

	<script>

		function submitForm() {
			$("#deleteDocDetails").submit();
		}
	</script>
<script>
function confirmDelete(id, category, financialyearNoti) {
    const hidden_form = document.createElement('form'); 
    hidden_form.method = 'post'; 
    hidden_form.action = "/admin/manage_documents"; 
    
    const hidden_input_id = document.createElement('input'); 
    hidden_input_id.type = 'hidden'; 
    hidden_input_id.name = 'id'; 
    hidden_input_id.value = id; 

    const hidden_input_category = document.createElement('input'); 
    hidden_input_category.type = 'hidden'; 
    hidden_input_category.name = 'category'; 
    hidden_input_category.value = category; 

    const hidden_input_year = document.createElement('input'); 
    hidden_input_year.type = 'hidden'; 
    hidden_input_year.name = 'financialyearNoti'; 
    hidden_input_year.value = financialyearNoti; 

    const hidden_input_csrf = document.createElement('input'); 
    hidden_input_csrf.type = 'hidden'; 
    hidden_input_csrf.name = '${_csrf.parameterName}'; 
    hidden_input_csrf.value = '${_csrf.token}';

    hidden_form.appendChild(hidden_input_id);
    hidden_form.appendChild(hidden_input_category);
    hidden_form.appendChild(hidden_input_year);
    hidden_form.appendChild(hidden_input_csrf);

    // Log values to ensure they are correct before submitting
    console.log('ID:', id);
    console.log('Category:', category);
    console.log('Financial Year Notification:', financialyearNoti);
    console.log('CSRF Token:', '${_csrf.token}');

    document.body.appendChild(hidden_form); 

    $.confirm({
        title: 'Confirm!',
        content: 'Do you want to proceed with deleting this file?',
        buttons: {
            submit: function() {
                $.post(hidden_form.action, $(hidden_form).serialize())
                .done(function(response) {
                    // Handle success response
                    $.alert({
                        title: 'Success!',
                        content: 'File deleted successfully!',
                        onClose: function() {
                            location.reload(); // Example: Reload the page after deletion
                        }
                    });
                })
                .fail(function(xhr, status, error) {
                    // Handle error response if needed
                    $.alert('Failed to delete file: ' + error);
                });
            },
            close: function() {
                $.alert('Canceled!');
            }
        }
    });
}
</script>



	
</body>
</html>
