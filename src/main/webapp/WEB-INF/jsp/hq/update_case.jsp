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

  <!-- Bootstrap 5 + AdminLTE 4 + Font Awesome -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/css/adminlte.min.css" rel="stylesheet"/>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet"/>

  <!-- jQuery Confirm (kept) -->
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css"/>
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- (kept) preloader -->
  <div class="preloader d-flex flex-column justify-content-center align-items-center">
    <img class="animation__shake" src="/static/dist/img/AdminLTELogo.png" alt="AdminLTELogo" height="60" width="60">
  </div>

  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page header -->
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

        <!-- Card -->
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
                            <label for="extensionNo">GSTIN<span style="color: red;"> *</span></label>
                            <input type="extensionNo" class="form-control" id="extensionNo" name="extensionNo"
                                   value="<c:out value='${HqUploadForm.extensionNo}'/>" placeholder="Extension Number"/>
                          </div>
                          <c:if test="${formResult.hasFieldErrors('extensionNo')}">
                            <span style="color: red;" class="text-danger">${formResult.getFieldError('extensionNo').defaultMessage}</span>
                          </c:if>
                        </div>

                        <div class="col-md-3">
                          <div class="form-group">
                            <label>Taxpayer Name<span style="color: red;"> *</span></label>
                            <input type="extensionNo" class="form-control" id="extensionNo" name="extensionNo"
                                   value="<c:out value='${HqUploadForm.extensionNo}'/>" placeholder="Extension Number"/>
                          </div>
                          <c:if test="${formResult.hasFieldErrors('category')}">
                            <span style="color: red;" class="text-danger">${formResult.getFieldError('category').defaultMessage}</span>
                          </c:if>
                        </div>

                        <div class="col-md-3">
                          <div class="form-group">
                            <label for="circle">Circle<span style="color: red;"> *</span></label>
                            <input type="extensionNo" class="form-control" id="extensionNo" name="extensionNo"
                                   value="<c:out value='${HqUploadForm.extensionNo}'/>" placeholder="Extension Number"/>
                          </div>
                        </div>

                        <div class="col-md-3">
                          <div class="form-group">
                            <label for="extensionNoField">Extension No.<span style="color: red;"> *</span></label>
                            <input type="extensionNo" class="form-control" id="extensionNo" name="extensionNo"
                                   value="<c:out value='${HqUploadForm.extensionNo}'/>" placeholder="Extension Number"/>
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
                  <div class="alert alert-success" role="alert">
                    <p class="mb-0">${successMessage}</p>
                  </div>
                </c:if>
              </div>
            </div>
          </div>
        </section>

      </div>
    </div>
  </main>

  <jsp:include page="../layout/footer.jsp"/>

  <!-- Control Sidebar (kept for compatibility) -->
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- Core JS: jQuery -> Bootstrap 5 bundle -> AdminLTE 4 -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/js/adminlte.min.js"></script>

<!-- Optional plugins you already used -->
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<!-- Sidebar toggle helper to mimic AdminLTE3 pushmenu behavior if header uses data-widget -->
<script>
  (function () {
    const mqDesktop = window.matchMedia('(min-width: 992px)');
    function toggleSidebar() {
      if (mqDesktop.matches) {
        document.body.classList.toggle('sidebar-collapse');
      } else {
        document.body.classList.toggle('sidebar-open');
      }
    }
    document.addEventListener('click', function (e) {
      const btn = e.target.closest('[data-lte-toggle="sidebar"], [data-widget="pushmenu"]');
      if (!btn) return;
      e.preventDefault();
      toggleSidebar();
    });
  })();
</script>

<!-- Your original page logic (unchanged) -->
<script>
  document.addEventListener('contextmenu', function(e) { e.preventDefault(); });
  document.addEventListener('keydown', function(e) {
    if (e.ctrlKey && e.key === 'u') { e.preventDefault(); }
  });
  document.addEventListener('keydown', function(e) {
    if (e.key === 'F12') { e.preventDefault(); }
  });
  $(document).ready(function () {
    function disableBack() {window.history.forward()}
    window.onload = disableBack();
    window.onpageshow = function (evt) {if (evt.persisted) disableBack()}
  });
  document.onkeydown = function (e) {
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) { e.preventDefault(); }
  };
</script>
</body>
</html>
