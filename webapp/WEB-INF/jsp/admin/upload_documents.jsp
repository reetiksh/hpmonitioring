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
							<h1 class="m-0">Upload General Documentations</h1>
						</div>
						<div class="col-sm-6">
							<ol class="breadcrumb float-sm-right">
								<li class="breadcrumb-item"><a href="/admin/dashboard">Home</a></li>
								<li class="breadcrumb-item active">Upload General Documentations</li>
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
										Documentations Upload Form  <i class="fas fa-file-pdf"></i>
									</h3>
								</div>
								<br>
								<form method="POST" id="uploadDocDetails"
									action="upload_documents"
									enctype="multipart/form-data">
									<div class="card-body">
										<div class="row">
											<input type="hidden" name="${_csrf.parameterName}"
												value="${_csrf.token}" />
											<div id="errorMessage" style="color: red;"></div>
										<div class="col-md-6">
																<div class="form-group">
																	<label for="Extension Number">Document Category<span
														style="color: red;"> *</span><span id="categorys_error"></span></label>
																	<select id="category" name="category"
																		class="custom-select" onchange="submitDropdown()">
																		<option value="">Please Select Category</option>
																		<c:forEach items="${categoryList}" var="year">
																			<option value="${year}"
																				<c:if test="${year == category}">selected</c:if>>${year}</option>
																		</c:forEach>
																	</select>
																</div>
															</div>
											
											<div class="col-md-6">
												<div class="form-group">
													<label for="Extension Number">Document Type<span
														style="color: red;"> *</span><span id="type_error"></span></label>
													<select class="form-control" id="type" name="type" required>
														<option value="" disabled selected>Select
															Type</option>

														<c:forEach items="${typeList}" var="designation">
															<option value="${designation}">${designation}</option>
														</c:forEach>
													</select>
												</div>
											</div>
											<div class="col-md-6">
										        <div class="form-group">
										            <label for="year">Choose Year<span id="year_error"style="color: red;"> *</span></label>
										            <select id="year" name="year" class="form-control">
										                <option value="" disabled selected>Select Year</option>
										                <!-- Options from yearList in descending order -->
										                <c:forEach items="${yearList}" var="year">
										                    <option value="${year}">${year}</option>
										                </c:forEach>
										            </select>
										        </div>
										    </div>

											<div class="form-group">
												<label for="file">Upload PDF  <span style="color: red;"> * </span><span>(upload only pdf file with max file size of 25MB)</span></label> <input type="file"
													id="file" name="file" accept=".pdf"
													class="form-control-file">
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
									<div class="card-footer">
										<button type="button" class="btn btn-primary"
											onclick="submitForm()">Submit</button>
										
										
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
		$('select').select2();
	</script>
	<script>
		$(function() {
			bsCustomFileInput.init();
		});

		
	</script>
	<script>
		$('form').on('submit', function(oEvent) {
			oEvent.preventDefault();
			$.confirm({
				title : 'Confirm!',
				content : 'Do you want to proceed ahead with document upload?',
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
	<script>
		$(document).ready(function() {
			$("#resetBtn").click(function(e) {
				$.confirm({
					title : 'Confirm!',
					content : 'Are you sure you want to clear the form!',
					buttons : {
						submit : function() {
							clear_all_error();
							$("#uploadDocDetails").trigger("reset");
						},
						close : function() {
							$.alert('Canceled!');
						}
					}
				});
			});
		});
		function clear_all_error(){console.log(1);
		
			$("#year_error").html("");
			$("#category_error").html("");
			$("#type_error").html("");
		}
	</script>
	
<script>
    function submitForm() {
        var category = document.getElementById("category").value;
        var type = document.getElementById("type").value;
        var year = document.getElementById("year").value;
        var fileInput = document.getElementById("file");
        var file = fileInput.value;

        var maxAllowedSize = 25 * 1024 * 1024;
        
        if (category === "" || type === "" || year === "" || file === "") {
            alert("Please fill in all required fields and upload a PDF file.");
        } else {
            var fileExtension = file.substring(file.lastIndexOf('.') + 1).toLowerCase();
            if (fileExtension !== 'pdf') {
                
                alert("Please upload PDF files only.");
                
            }else if(fileInput.files[0].size > maxAllowedSize){

            	alert('Please upload max 25MB file');
            	fileInput.value = '';

            }else {
                document.getElementById("uploadDocDetails").submit();
            }
        }
    }
    
</script>
<script>
    // Function to submit the form
    function submitDropdown() {
      var selectedCategory = document.getElementById("category").value;
      document.getElementById("type").value = "";
        document.getElementById("uploadDocDetails").submit();
    }
</script>


</body>
</html>
