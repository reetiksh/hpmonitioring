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
    <!-- <link rel="stylesheet" href="/static/dist/css/googleFront/googleFrontFamilySourceSansPro.css"> -->
    <link rel="stylesheet" href="../../plugins/fontawesome-free/css/all.min.css">
    <link rel="stylesheet" href="../../dist/css/adminlte.min.css">
    <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
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
            <h1 class="m-0">Upload Review Cases</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="/hq/dashboard">Home</a></li>
              <li class="breadcrumb-item active">Upload Review Cases</li>
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
                  <h3 class="card-title">Upload Review Cases</h3>
                </div>
                <br>
                <form method="POST" action="<c:url value='/hq/upload_data' />" enctype="multipart/form-data">
                  <div class="card-body">
                    <div class="row">
                      <div class="col-md-3">
                        <div class="form-group">
                          <label for="Extension Number">GSTIN<span style="color: red;"> *</span></label>
                          <input type="extensionNo" class="form-control" id="extensionNo" name="extensionNo"  value="<c:out value='${HqUploadForm.extensionNo}'/>" placeholder="Extension Number"/>
                        </div>
							          <c:if test="${formResult.hasFieldErrors('extensionNo')}">
                          <span style="color: red;" class="text-danger">${formResult.getFieldError('extensionNo').defaultMessage}</span>
                        </c:if>
                      </div>
                      <div class="col-md-3">
                        <div class="form-group">
                          <label>Taxpayer Name<span style="color: red;"> *</span></label>
                          <input type="extensionNo" class="form-control" id="extensionNo" name="extensionNo"  value="<c:out value='${HqUploadForm.extensionNo}'/>" placeholder="Extension Number"/>
                        </div>
                        <c:if test="${formResult.hasFieldErrors('category')}">
                          <span style="color: red;" class="text-danger">${formResult.getFieldError('category').defaultMessage}</span>
                        </c:if>
                      </div>
                      <div class="col-md-3">
                        <div class="form-group">
                        <label for="pdfFile">Circle<span style="color: red;"> *</span></label>
                          <input type="extensionNo" class="form-control" id="extensionNo" name="extensionNo"  value="<c:out value='${HqUploadForm.extensionNo}'/>" placeholder="Extension Number"/>
                        </div>
                      </div>
                      <div class="col-md-3">
                        <div class="form-group">
                        <label for="pdfFile">Extension No.<span style="color: red;"> *</span></label>
                            <input type="extensionNo" class="form-control" id="extensionNo" name="extensionNo"  value="<c:out value='${HqUploadForm.extensionNo}'/>" placeholder="Extension Number"/>
                        </div>
                      </div>
                      <br/>
                      <c:forEach items="${HqUploadForm.excelErrors}" var="excelError">
                        <span style="color: red;" class="error"><c:out value="${excelError}" /></span>
                      </c:forEach>
                      <br/>
                    </div>
                  </div>
                  <div class="card-footer">
                    <button type="submit" class="btn btn-primary">Submit</button>
                  </div>
                </form>
              </div>
              <c:if test="${not empty successMessage}">
                <div class="success-message">
                  <p>${successMessage}</p>
                </div>
              </c:if>
            </div>
          </div>
        </div>
      </section>
  </div>
  <jsp:include page="../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>
<script src="../../plugins/jquery/jquery.min.js"></script>
<script src="../../plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="../../plugins/bs-custom-file-input/bs-custom-file-input.min.js"></script>
<script src="../../dist/js/adminlte.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>
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
$(function () {
  bsCustomFileInput.init();
});
</script>
</body>
</html>
