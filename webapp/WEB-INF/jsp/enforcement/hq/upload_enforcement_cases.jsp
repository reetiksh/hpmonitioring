<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST | Upload Enforcement Cases</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png"/>

  <!-- AdminLTE 4 / Bootstrap 5 -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">

  <!-- DataTables (Bootstrap 5 builds) -->
  <link rel="stylesheet" href="/static/plugins/datatables-bs5/css/dataTables.bootstrap5.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap5.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap5.min.css">

  <!-- Optional -->
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">

  <!-- Layout tweaks: keep content snug to sidebar; sync on toggle -->
  <style>
    :root{
      --hp-sidebar-width: 210px;   /* will be overwritten by JS with the real measured width */
      --hp-header-height: 56px;
    }

    .app-header{ height: var(--hp-header-height); }
    .app-content{ padding: 1rem; }
    .content-header{ padding: .5rem 0 .75rem; }

    /* Keep sidebar + content aligned */
    .app-sidebar{ width: var(--hp-sidebar-width) !important; }
    .app-main   { margin-left: 0px !important; transition: margin-left .2s ease; }

    /* When collapsed on desktop, content should shift to 0 */
    body.sidebar-collapse .app-main{ margin-left: 0 !important; }

    /* On < lg screens the sidebar is overlay, so content is full width */
    @media (max-width: 991.98px){
      .app-main{ margin-left: 0 !important; }
    }

    /* Ensure any .container inside .app-content spans full width */
    .app-content > .container,
    .app-content > .container-sm,
    .app-content > .container-md,
    .app-content > .container-lg,
    .app-content > .container-xl,
    .app-content > .container-xxl,
    .app-content > .container-fluid{
      max-width: 100% !important;
      width: 100% !important;
    }
  </style>
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- ========== HEADER (AdminLTE 4) ========== -->
  <header class="app-header navbar navbar-expand bg-dark border-bottom">
    <!-- Sidebar toggle -->
    <div class="navbar-nav">
      <a class="nav-link" data-lte-toggle="sidebar" href="#" role="button" aria-label="Toggle sidebar">
        <i class="fas fa-bars text-white"></i>
      </a>
    </div>

    <!-- Brand -->
    <div class="navbar-brand ms-2 text-white fw-bold d-none d-sm-inline">
      <img src="/static/files/hp_logo.png" alt="HP GST" style="height:28px;width:28px;border-radius:50%;margin-right:.5rem;vertical-align:middle;">
      HP GST
    </div>

    <!-- Right-side icons (optional) -->
    <ul class="navbar-nav ms-auto">
      <li class="nav-item"><a class="nav-link text-white" href="#"><i class="far fa-user"></i></a></li>
      <li class="nav-item"><a class="nav-link text-white" href="#"><i class="far fa-bell"></i></a></li>
    </ul>
  </header>

  <!-- ========== SIDEBAR (AdminLTE 4) ========== -->
  <aside class="app-sidebar bg-dark text-white elevation-4"
         style="background-image:url(/static/dist/img/pattern_h.png); background-color:#15283c;">
    <div class="sidebar-brand d-flex align-items-center p-3 border-bottom">
      <a href="/" class="d-flex align-items-center text-decoration-none text-white">
        <img src="/static/files/hp_logo.png" alt="HP GST Logo"
             class="brand-image img-circle elevation-3 me-2" style="opacity:.85; width:44px; height:44px;">
        <span class="brand-text fw-bold">HP GST</span>
      </a>
    </div>

    <div class="app-sidebar-menu p-2">
      <nav>
        <ul class="nav nav-pills flex-column" role="menu">
          <li class="nav-item">
            <a href="/welcome" class="nav-link d-flex align-items-center">
              <i class="nav-icon fas fa-home me-2"></i>
              <span>Home <c:out value="${user}"/></span>
            </a>
          </li>
          <c:forEach items="${MenuList}" var="data">
            <li class="nav-item">
              <a href="/${data.userType}/${data.url}"
                 class="nav-link d-flex align-items-center <c:if test='${data.url == activeMenu}'>active</c:if>">
                <i class="nav-icon fas ${data.icon} me-2"></i>
                <span><c:out value="${data.name}"/></span>
              </a>
            </li>
          </c:forEach>
        </ul>
      </nav>
    </div>
  </aside>

  <!-- ========== MAIN CONTENT ========== -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page header -->
        <div class="content-header">
          <div class="row align-items-center">
            <div class="col">
              <h1 class="m-0">Upload Enforcement Cases</h1>
            </div>
            <div class="col-auto">
              <ol class="breadcrumb mb-0">
                <li class="breadcrumb-item"><a href="/enforcement_hq/dashboard">Home</a></li>
                <li class="breadcrumb-item active">Upload Enforcement Cases</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- Card -->
        <div class="card card-primary">
          <div class="card-header d-flex justify-content-between align-items-center">
            <h3 class="card-title mb-0">Upload Enforcement Cases</h3>
            <a href="/static/files/enforcement_cases_upload_format.xlsx" class="btn btn-success btn-sm" download>
              <i class="fas fa-download me-1"></i> Sample <i class="fas fa-file-excel ms-1"></i>
            </a>
          </div>

          <form method="POST" id="uploadForm" name="uploadForm"
                action="<c:url value='/enforcement_hq/upload_enforcement_cases' />"
                enctype="multipart/form-data">
            <div class="card-body">
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
              <div id="errorMessage" class="text-danger"></div>

              <div class="row g-3">
                <div class="col-md-6">
                  <label class="form-label">Dispatch No. <span class="text-danger">*</span>
                    <span id="dispatch_alert"></span>
                  </label>
                  <input type="text" class="form-control" id="extensionNo" name="extensionNo" placeholder="Dispatch Number">
                  <c:if test="${formResult.hasFieldErrors('extensionNo')}">
                    <span class="text-danger">${formResult.getFieldError('extensionNo').defaultMessage}</span>
                  </c:if>
                </div>

                <div class="col-md-6">
                  <label class="form-label">Category <span class="text-danger">*</span>
                    <span id="category_alert"></span>
                  </label>
                  <select id="category" name="category" class="form-select selectpicker" data-live-search="true">
                    <option value="" disabled selected>Please Select Category</option>
                    <c:forEach items="${categories}" var="categories">
                      <option value="${categories.id}">${categories.name}</option>
                    </c:forEach>
                  </select>
                  <c:if test="${formResult.hasFieldErrors('category')}">
                    <span class="text-danger">${formResult.getFieldError('category').defaultMessage}</span>
                  </c:if>
                </div>

                <div class="col-md-6">
                  <label class="form-label">Dispatch Document <span class="text-danger">*</span>
                    <span id="pdf_alert"></span>
                  </label>
                  <input type="file" class="form-control" id="pdfFile" name="pdfData.pdfFile" accept=".pdf">
                  <small class="form-text text-muted">Choose PDF (size up to 100 MB)</small>
                </div>

                <div class="col-md-6">
                  <label class="form-label">Case File <span class="text-danger">*</span>
                    <span id="excel_alert"></span>
                  </label>
                  <input type="file" class="form-control" id="excelFile" name="excelData.excelFile" accept=".xls,.xlsx">
                  <small class="form-text text-muted">Choose Excel (size up to 2 MB)</small>
                </div>
              </div>
            </div>

            <div class="card-footer text-end">
              <button type="button" class="btn btn-primary" onclick="submitForm()">Submit</button>
            </div>
          </form>
        </div>

        <!-- Success / Error areas + table (if present) -->
        <c:if test="${not empty successMessage}">
          <div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
            <strong>${successMessage}</strong>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
          </div>
          <nav class="navbar navbar-dark bg-primary rounded-top mb-2">
            Recently uploaded cases in Category : ${selectedCategory}
          </nav>
        </c:if>

        <c:if test="${not empty errorList}">
          <div class="alert alert-danger alert-dismissible fade show" role="alert" style="max-height:300px; overflow-y:auto;">
            <c:forEach items="${errorList}" var="excelError">
              <strong>${excelError}</strong><br/>
            </c:forEach>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
          </div>
          <c:if test="${!empty uploadData}">
            <nav class="navbar navbar-dark bg-primary rounded-top mb-2">
              Correct rows from uploaded excel
            </nav>
          </c:if>
        </c:if>

        <c:if test="${!empty uploadData}">
          <div class="card p-2">
            <table id="example1" class="table table-bordered table-striped w-100">
              <thead>
              <tr>
                <th class="text-center align-middle">GSTIN</th>
                <th class="text-center align-middle">Taxpayer Name</th>
                <th class="text-center align-middle">Case Reporting Date</th>
                <th class="text-center align-middle">Suspected Indicative Tax Value (₹)</th>
                <th class="text-center align-middle">Period</th>
                <th class="text-center align-middle">Assigned To Circle</th>
                <th class="text-center align-middle">Parameter_1</th>
                <th class="text-center align-middle">Parameter_2</th>
                <th class="text-center align-middle">Parameter_3</th>
                <th class="text-center align-middle">Parameter_4</th>
                <th class="text-center align-middle">Parameter_5</th>
              </tr>
              </thead>
              <tbody>
              <c:forEach items="${uploadData}" var="row">
                <tr>
                  <c:forEach items="${row}" var="cell">
                    <td class="text-center align-middle"><c:out value="${cell}"/></td>
                  </c:forEach>
                </tr>
              </c:forEach>
              </tbody>
            </table>
          </div>
        </c:if>

      </div>
    </div>
  </main>

  <!-- ========== FOOTER ========== -->
  <footer class="main-footer">
    <strong>Copyright &copy; 2023-2024
      <a href="/">Govt of Himachal Pradesh</a>.
    </strong>
    All rights reserved.
  </footer>
</div>

<!-- Overlay for mobile (tap outside to close) -->
<div class="sidebar-overlay" data-lte-dismiss="sidebar"></div>

<!-- ========== SCRIPTS (order matters) ========== -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<!-- DataTables (Bootstrap 5 builds) -->
<script src="/static/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="/static/plugins/datatables-bs5/js/dataTables.bootstrap5.min.js"></script>
<script src="/static/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="/static/plugins/datatables-responsive/js/responsive.bootstrap5.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/dataTables.buttons.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.bootstrap5.min.js"></script>
<script src="/static/plugins/jszip/jszip.min.js"></script>
<script src="/static/plugins/pdfmake/pdfmake.min.js"></script>
<script src="/static/plugins/pdfmake/vfs_fonts.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.html5.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.print.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.colVis.min.js"></script>

<!-- Optional -->
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  // --- Sync content margin with actual sidebar width (fixes the gap & keeps it in sync on toggle) ---
  function setSidebarVar(){
    const sb = document.querySelector('.app-sidebar');
    const collapsed = document.body.classList.contains('sidebar-collapse');
    const px = collapsed ? 0 : (sb ? sb.offsetWidth : 210);
    document.documentElement.style.setProperty('--hp-sidebar-width', px + 'px');
  }
  setSidebarVar();
  new MutationObserver(setSidebarVar).observe(document.body, {attributes:true, attributeFilter:['class']});
  window.addEventListener('resize', setSidebarVar);

  // Fade out success alert (if present)
  $(function(){
    $("#message").fadeTo(5000, 0.5).slideUp(500, function(){ $(this).remove(); });
  });

  // DataTable (only if table exists)
  $(function(){
    if ($('#example1').length){
      $('#example1').DataTable({
        responsive: false,
        lengthChange: false,
        autoWidth: true,
        scrollX: true,
        buttons: [{ extend:'excel', title:null }]
      }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
    }
  });

  // Form validations + confirm
  function validateDocumentType(file, expected){
    return expected.includes(file.type);
  }
  function VerifyUploadSizeIsOK(id, maxBytes){
    const fld = document.getElementById(id);
    return !(fld.files && fld.files.length === 1 && fld.files[0].size > maxBytes) ;
  }

  function submitForm(){
    const extensionNo = $("#extensionNo").val();
    const category    = $("#category").val();
    const pdfFile     = $("#pdfFile").val();
    const excelFile   = $("#excelFile").val();
    let hasErr = false;

    $("#dispatch_alert,#category_alert,#pdf_alert,#excel_alert").html("").css("color","transparent");

    if(!extensionNo){
      $("#dispatch_alert").html("Please provide dispatch number").css("color","red"); hasErr = true;
    } else if(extensionNo.length > 40){
      $("#dispatch_alert").html("Please provide correct dispatch number").css("color","red"); hasErr = true;
    }
    if(!category){
      $("#category_alert").html("Please select category").css("color","red"); hasErr = true;
    }
    if(!pdfFile){
      $("#pdf_alert").html("Please upload pdf file").css("color","red"); hasErr = true;
    }
    if(!excelFile){
      $("#excel_alert").html("Please upload excel sheet").css("color","red"); hasErr = true;
    }

    if(!VerifyUploadSizeIsOK("pdfFile", 104857600)){
      $("#pdf_alert").html("Please upload pdf file upto 100MB").css("color","red"); hasErr = true;
    }
    if(!VerifyUploadSizeIsOK("excelFile", 2097152)){
      $("#excel_alert").html("Please upload the excel within 2MB").css("color","red"); hasErr = true;
    }

    const p = document.getElementById('pdfFile')?.files?.[0];
    const e = document.getElementById('excelFile')?.files?.[0];
    if (p && !validateDocumentType(p, 'application/pdf')){
      $("#pdf_alert").html("Please upload the pdf").css("color","red"); hasErr = true;
    }
    // Accept both xls and xlsx MIME types
    if (e && !( /application\/vnd\.ms-excel|application\/vnd\.openxmlformats-officedocument\.spreadsheetml\.sheet/.test(e.type) )){
      $("#excel_alert").html("Please upload excel sheet").css("color","red"); hasErr = true;
    }

    if (hasErr) return;

    $.confirm({
      title : 'Confirm!',
      content : 'Do you want to proceed ahead with uploading of case(s)?',
      buttons : {
        submit : function(){ document.getElementById('uploadForm').submit(); },
        close  : function(){ $.alert('Canceled!'); }
      }
    });
  }

  // Hardening
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
</script>
</body>
</html>
