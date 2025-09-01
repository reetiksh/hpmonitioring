<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>HP GST | Upload Review Cases</title>
		<link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">
		<link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
		<link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
		<link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
		<link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
		<link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
		<link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
		<link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
	</head>
<body class="hold-transition sidebar-mini layout-fixed">
<div class="wrapper">
	<div class="preloader flex-column justify-content-center align-items-center">
		<img class="animation__shake" src="/static/dist/img/AdminLTELogo.png" alt="AdminLTELogo" height="60" width="60">
	</div>
	<jsp:include page="../../layout/header.jsp"/>
	<jsp:include page="../../layout/sidebar.jsp"/>
	<div class="content-wrapper">
		<div class="content-header">
			<div class="container-fluid">
				<div class="row mb-2">
					<div class="col-sm-6">
						<h1 class="m-0">Upload Audit Cases</h1>
					</div>
					<div class="col-sm-6">
						<ol class="breadcrumb float-sm-right">
							<li class="breadcrumb-item"><a href="/l1/dashboard">Home</a></li>
							<li class="breadcrumb-item active">Upload Audit Cases</li>
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
									<h3 class="card-title">Upload Audit Cases</h3>
								</div>
								<br>
								<div class="col-md-12">
									<a href="/static/files/audit_cases_upload_format.xlsx" class="btn btn-success" style="float: right;" download><i class="fas fa-download"></i> Sample <i class="fas fa-file-excel"></i></a>
								</div>
								<form method="POST" id="uploadForm" name="uploadForm" action="<c:url value='/l1/upload_audit_cases' />" enctype="multipart/form-data">
									<div class="card-body">
										<div class="row">
											<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
												<div id="errorMessage" style="color: red;"></div>
												<div class="col-md-6">
													<div class="form-group"  title="Dispatch Number Should be with in 40 character">
														<label for="Extension Number">Dispatch No. <span style="color: red;"> *</span><span id="dispatch_alert"></span></label>
														<input type="extensionNo" class="form-control" id="extensionNo" name="extensionNo" placeholder="Dispatch Number"/>
													</div>
													<c:if test="${formResult.hasFieldErrors('extensionNo')}">
															<span style="color: red;" class="text-danger">${formResult.getFieldError('extensionNo').defaultMessage}</span>
													</c:if>
												</div>
												<div class="col-md-6">
														<div class="form-group">
															<label>Category<span style="color: red;"> *</span><span id="category_alert"></span></label>
															<select id="category" name="category" class="selectpicker col-md-12" data-live-search="true">
																<option value="" disabled selected>Please Select Category</option>
																<c:forEach items="${categories}" var="categories">
																	<option value="${categories.id}">${categories.name}</option>
																</c:forEach>
															</select>
														</div>
							              <c:if test="${formResult.hasFieldErrors('category')}">
								              <span style="color: red;" class="text-danger">${formResult.getFieldError('category').defaultMessage}</span>
							              </c:if>
												</div>
												<div class="col-md-6">
														<div class="form-group">
														<label for="pdfFile">Dispatch Document<span style="color: red;"> *</span><span id="pdf_alert"></span></label>
														<div class="input-group">
																<div class="custom-file">
																<input type="file" class="custom-file-input" id="pdfFile" name="pdfData.pdfFile" accept=".pdf"/>
																<label class="custom-file-label" for="pdfFile">Choose PDF (size upto 100MB)</label> 
																</div>
														</div>
														</div>
												</div>
												<div class="col-md-6">
														<div class="form-group">
														<label for="excelFile">Case File<span style="color: red;"> *</span><span id="excel_alert"></span></label>
														<div class="input-group">
																<div class="custom-file">
																<input type="file" class="custom-file-input" id="excelFile"  name="excelData.excelFile" accept=".xls, .xlsx"/>
																<label class="custom-file-label" for="excelFile">Choose Excel (size upto 2MB)</label>
																</div>
														</div>
														</div>
												</div>
												<br/>
											 <br/>
										</div>
									</div>
									<div class="card-footer">
										<button type="button" class="btn btn-primary" onclick="submitForm()">Submit</button>
									</div>
								</form>
							</div>
							<c:if test="${not empty successMessage}">
								<div class="col-12 alert alert-success alert-dismissible fade show" id="message"  role="alert">
									<strong>${successMessage}</strong><br>
									<button type="button" class="close" data-dismiss="alert" aria-label="Close">
										<span aria-hidden="true">&times;</span>
									</button>
								</div>
								<nav class="navbar navbar-dark bg-primary" style="margin-bottom: 5px; border-top-left-radius: .25rem; border-top-right-radius: .25rem;">
									Recently uploaded cases in Category : ${selectedCategory}
								</nav>
							</c:if>
							<c:if test="${not empty errorList}">
								<div class="col-12 alert alert-danger alert-dismissible fade show" role="alert" style="max-height: 300px; overflow-y: auto;">
									<c:forEach items="${errorList}" var="excelError">
										<strong>${excelError}</strong><br>
									</c:forEach>
									<button type="button" class="close" data-dismiss="alert" aria-label="Close">
										<span aria-hidden="true">&times;</span>
									</button>
								</div>
								<c:if test="${!empty uploadData}">
									<nav class="navbar navbar-dark bg-primary" style="margin-bottom: 5px; border-top-left-radius: .25rem; border-top-right-radius: .25rem;">
										Correct rows from uploaded excel
									</nav>
								</c:if>
							</c:if>
							<c:if test="${!empty uploadData}">
								<div class="card" style="padding: 3px;">
									<table id="example1" class="table table-bordered table-striped">
										<thead>
											<tr>
												<th style="text-align: center; vertical-align: middle;">Case Id</th>
												<th style="text-align: center; vertical-align: middle;">GSTIN</th>
												<th style="text-align: center; vertical-align: middle;">Taxpayer Name</th>
												<th style="text-align: center; vertical-align: middle;">Case Reporting Date</th>
												<!-- <th style="text-align: center; vertical-align: middle;">Suspected Indicative Tax Value (₹)</th> -->
												<th style="text-align: center; vertical-align: middle;">Period</th>
												<th style="text-align: center; vertical-align: middle;">Assigned To Circle</th>
												<th style="text-align: center; vertical-align: middle;">Parameter_1</th>
												<th style="text-align: center; vertical-align: middle;">Parameter_2</th>
												<th style="text-align: center; vertical-align: middle;">Parameter_3</th>
												<th style="text-align: center; vertical-align: middle;">Parameter_4</th>
												<th style="text-align: center; vertical-align: middle;">Parameter_5</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${uploadData}" var="row">
												<tr>
													<c:forEach items="${row}" var="cell">
													<td style="text-align: center; vertical-align: middle;"><c:out value="${cell}" /></td>
													</c:forEach>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</c:if>
						</div>
					</div>
				</div>
			</section>
	</div>
	<jsp:include page="../../layout/footer.jsp"/>
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
	$(document).ready(function() {
		$("#message").fadeTo(5000, 500).slideUp(500, function() {
			$("#message").slideUp(500);
		});
	});
</script>
<script>
	$(function () {
    $("#example1").DataTable({
      "responsive": false, "lengthChange": false, "autoWidth": true, "scrollX": true,
      "buttons": 
			[{
				extend: 'excel',
				title: null
			}]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
  });
</script>
<script>
$(function () {
	bsCustomFileInput.init();
});

function submitForm(){
	var extensionNo = $("#extensionNo").val();
	var category = $("#category").val();
	var pdfFile = $("#pdfFile").val();
	var excelFile = $("#excelFile").val();
	var flag = false;

	$("#dispatch_alert").html("").css("color","transparent");
	$("#category_alert").html("").css("color","transparent");
	$("#pdf_alert").html("").css("color","transparent");
	$("#excel_alert").html("").css("color","transparent");

	if(extensionNo === '' || extensionNo == null){
		$("#dispatch_alert").html("Please provide dispatch number").css("color","red");
		flag = true;
	} else if(extensionNo.length > 40){
		$("#dispatch_alert").html("Please provide correct dispatch number").css("color","red");
		flag = true;
	}

	if(category === '' || category == null){
		$("#category_alert").html("Please select category").css("color","red");
		flag = true;
	}

	if(pdfFile === '' || pdfFile == null){
		$("#pdf_alert").html("Please upload pdf file").css("color","red");
		flag = true;
	}

	if(excelFile === '' || excelFile == null){
		$("#excel_alert").html("Please upload excel sheet").css("color","red");
		flag = true;
	}

	if(!VerifyUploadSizeIsOK("pdfFile", 104857600)){
		$("#pdf_alert").html("Please upload pdf file upto 10MB").css("color","red");
		flag = true;
	}

	if(!VerifyUploadSizeIsOK("excelFile", 2097152)){
		$("#excel_alert").html("Please upload the excel within 2MB").css("color","red");
		flag = true;
	}
	
	var fileInput = document.getElementById('pdfFile');
  	var file = fileInput.files[0];
	if (!validateDocumentType(file, 'application/pdf')) {
		$("#pdf_alert").html("Please upload the pdf").css("color","red");
		flag = true;
	}

	var fileInput = document.getElementById('excelFile');
  	var file = fileInput.files[0];
	if (!validateDocumentType(file, 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet')) {
		$("#excel_alert").html("Please upload excel sheet").css("color","red");
		flag = true;
	}

	if(flag!=true){
		$("#uploadForm").submit();
	}
}
</script>
<script>
	function validateDocumentType(file, filetype) {
		return filetype.includes(file.type);
	}
</script>
<script type="text/javascript">
	function VerifyUploadSizeIsOK(UploadFieldID, MaxSizeInBytes){
	  var fld = document.getElementById(UploadFieldID);
	  if( fld.files && fld.files.length == 1 && fld.files[0].size > MaxSizeInBytes ){
			return false;
	  }
	  return true;
	}
</script>
	
<script>
	function downloadExcel() {
		var filePath = 'C:/Download/gst_upload3.xlsx';
		var link = document.createElement('a');
		link.href = filePath;
		link.download = 'filename.xlsx';
		link.click();
	}
</script>
<script>
	$('form').on('submit', function(oEvent) {
		oEvent.preventDefault();
		$.confirm({
			title : 'Confirm!',
			content : 'Do you want to proceed ahead with uploading of case(s)?',
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
