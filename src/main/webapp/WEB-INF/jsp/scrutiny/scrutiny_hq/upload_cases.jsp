<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>HP GST | Upload Review Cases</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />

  <!-- AdminLTE 4 + Bootstrap 5 + FontAwesome (CDN) -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6/css/all.min.css">

  <!-- Bootstrap Select (BS5) -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.14.0-beta3/dist/css/bootstrap-select.min.css">

  <!-- jQuery Confirm -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/jquery-confirm@3.3.4/css/jquery-confirm.min.css" />

  <!-- DataTables (Bootstrap 5) + Buttons -->
  <link rel="stylesheet" href="https://cdn.datatables.net/v/bs5/dt-2.0.7/b-3.0.2/r-3.0.2/datatables.min.css"/>

  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">
  <style>
    .required::after { content:" *"; color:#dc3545; }
    .hide-vis { color: transparent; }
  </style>
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
  <div class="app-wrapper">

    <!-- Header -->
    <nav class="app-header navbar navbar-expand bg-body">
      <div class="container-fluid">
        <a class="navbar-brand d-flex align-items-center gap-2" href="/hq/dashboard">
          <img src="/static/files/hp_logo.png" alt="HP GST" width="28" height="28" class="rounded"/>
          <span class="fw-semibold">HP GST</span>
        </a>
        <ul class="navbar-nav ms-auto">
          <li class="nav-item">
            <a class="nav-link" href="/hq/dashboard" title="Home">
              <i class="fa-solid fa-house"></i>
            </a>
          </li>
        </ul>
      </div>
    </nav>

    <!-- (Optional) Sidebar placeholder -->
    <aside class="app-sidebar bg-body-secondary shadow" id="sidebar">
      <div class="sidebar-brand">
        <a href="/hq/dashboard" class="brand-link">
          <img src="/static/files/hp_logo.png" alt="HP GST" class="brand-image opacity-75 shadow" />
          <span class="brand-text fw-bold">HQ</span>
        </a>
      </div>
      <div class="sidebar-wrapper">
        <nav class="mt-2">
          <ul class="nav nav-pills nav-sidebar flex-column" data-lte-toggle="treeview" role="menu">
            <li class="nav-item">
              <a href="/hq/dashboard" class="nav-link">
                <i class="nav-icon fa-solid fa-gauge"></i><p>Dashboard</p>
              </a>
            </li>
            <li class="nav-item">
              <a href="/scrutiny_hq/upload_cases" class="nav-link active">
                <i class="nav-icon fa-solid fa-file-arrow-up"></i><p>Upload Review Cases</p>
              </a>
            </li>
          </ul>
        </nav>
      </div>
    </aside>

    <!-- Main -->
    <main class="app-main">
      <div class="app-content pt-3 px-3">
        <div class="container-fluid">

          <!-- Page header -->
          <div class="row mb-2">
            <div class="col-sm-6">
              <h1 class="h3 mb-0">Upload Review Cases</h1>
            </div>
            <div class="col-sm-6">
              <ol class="breadcrumb float-sm-end">
                <li class="breadcrumb-item"><a href="/hq/dashboard">Home</a></li>
                <li class="breadcrumb-item active" aria-current="page">Upload Review Cases</li>
              </ol>
            </div>
          </div>

          <!-- Card -->
          <div class="card card-primary card-outline">
            <div class="card-header d-flex align-items-center justify-content-between">
              <h3 class="card-title mb-0">Upload Review Cases</h3>
              <a href="#" class="btn btn-success sampleExcel">
                <i class="fas fa-download me-2"></i>Sample <i class="fas fa-file-excel ms-2"></i>
              </a>
            </div>

            <form method="POST" id="uploadScrutinyForm" name="uploadScrutinyForm"
                  action="<c:url value='/scrutiny_hq/upload_cases'/>" enctype="multipart/form-data">
              <div class="card-body">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <div id="errorMessage" class="text-danger small mb-2"></div>

                <div class="row g-3">
                  <!-- Dispatch No -->
                  <div class="col-md-6">
                    <label for="extensionNo" class="form-label required">Dispatch No.</label>
                    <input type="text" class="form-control" id="extensionNo" name="extensionNo"
                           placeholder="Dispatch Number" maxlength="40" />
                    <div id="dispatch_alert" class="form-text hide-vis">.</div>
                    <c:if test="${formResult.hasFieldErrors('extensionNo')}">
                      <div class="text-danger small">${formResult.getFieldError('extensionNo').defaultMessage}</div>
                    </c:if>
                  </div>

                  <!-- Category -->
                  <div class="col-md-6">
                    <label for="category" class="form-label required">Category</label>
                    <select id="category" name="category" class="selectpicker form-control" data-live-search="true" title="Please Select Category">
                      <c:forEach items="${categories}" var="cat">
                        <option value="${cat}">${cat}</option>
                      </c:forEach>
                    </select>
                    <div id="category_alert" class="form-text hide-vis">.</div>
                    <c:if test="${formResult.hasFieldErrors('category')}">
                      <div class="text-danger small">${formResult.getFieldError('category').defaultMessage}</div>
                    </c:if>
                  </div>

                  <!-- Dispatch Document (PDF) -->
                  <div class="col-md-6">
                    <label for="pdfFile" class="form-label required">Dispatch Document</label>
                    <input type="file" class="form-control" id="pdfFile" name="pdfData.pdfFile" accept=".pdf" />
                    <div class="form-text">Upload PDF (size upto <strong>100MB</strong>)</div>
                    <div id="pdf_alert" class="form-text text-danger"></div>
                  </div>

                  <!-- Case File (Excel) -->
                  <div class="col-md-6">
                    <label for="excelFile" class="form-label required">Case File</label>
                    <input type="file" class="form-control" id="excelFile" name="excelData.excelFile" accept=".xls,.xlsx" />
                    <div class="form-text">Upload Excel (size upto <strong>2MB</strong>)</div>
                    <div id="excel_alert" class="form-text text-danger"></div>
                  </div>
                </div>
              </div>

              <div class="card-footer d-flex justify-content-end gap-2">
                <button type="button" class="btn btn-primary" onclick="submitForm()">
                  <i class="fa-solid fa-cloud-arrow-up me-2"></i>Submit
                </button>
              </div>
            </form>
          </div>

          <!-- Success message -->
          <c:if test="${not empty successMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert" id="message">
              <strong>${successMessage}</strong>
              <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <div class="alert alert-primary py-2">
              <i class="fa-solid fa-clock-rotate-left me-2"></i>Recently uploaded cases
            </div>
          </c:if>

          <!-- Error list -->
          <c:if test="${not empty errorList}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert" style="max-height: 300px; overflow-y:auto;">
              <c:forEach items="${errorList}" var="excelError">
                <div><strong>${excelError}</strong></div>
              </c:forEach>
              <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <c:if test="${!empty uploadData}">
              <div class="alert alert-primary py-2">
                <i class="fa-solid fa-list-check me-2"></i>Correct rows from uploaded excel
              </div>
            </c:if>
          </c:if>

          <!-- Uploaded data table -->
          <c:if test="${!empty uploadData}">
            <div class="card card-outline card-light">
              <div class="card-body">
                <table id="example1" class="table table-striped table-hover table-bordered w-100">
                  <thead class="table-light">
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
                          <td class="text-center align-middle"><c:out value="${cell}" /></td>
                        </c:forEach>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>
              </div>
            </div>
          </c:if>

        </div>
      </div>

      <footer class="app-footer">
        <div class="float-end d-none d-sm-inline">AdminLTE 4</div>
        <strong>HP GST</strong> &copy; <script>document.write(new Date().getFullYear())</script>.
      </footer>
    </main>
  </div>

  <!-- Core JS -->
  <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/js/adminlte.min.js"></script>

  <!-- Bootstrap Select (BS5) -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.14.0-beta3/dist/js/bootstrap-select.min.js"></script>

  <!-- jQuery Confirm -->
  <script src="https://cdn.jsdelivr.net/npm/jquery-confirm@3.3.4/js/jquery-confirm.min.js"></script>

  <!-- DataTables + Buttons (BS5) -->
  <script src="https://cdn.datatables.net/v/bs5/dt-2.0.7/b-3.0.2/r-3.0.2/datatables.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/jszip@3.10.1/dist/jszip.min.js"></script>

  <script>
    // Initialize UI components
    $(function () {
      $('.selectpicker').selectpicker();

      const tbl = $('#example1');
      if (tbl.length) {
        tbl.DataTable({
          responsive: true,
          lengthChange: false,
          autoWidth: false,
          buttons: [{ extend: 'excel', title: null }]
        }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
      }

      // Auto-fade success message
      $("#message").fadeTo(5000, 500).slideUp(500, function() {
        $("#message").slideUp(500);
      });
    });

    // Confirm before any form submit (fallback)
    $(document).on('submit', 'form', function (e) {
      if (this.id !== 'uploadScrutinyForm') return; // our form uses custom trigger
      e.preventDefault();
      $.confirm({
        title: 'Confirm!',
        content: 'Do you want to proceed ahead with uploading of case(s)?',
        buttons: {
          submit: () => this.submit(),
          close: () => $.alert('Canceled!')
        }
      });
    });

    // Sample Excel workflow (optional backend endpoints)
    $(document).on('click', '.sampleExcel', function (e) {
      e.preventDefault();
      $.ajax({
        url: '/scrutiny_hq/updateExcel',
        method: 'POST',
        beforeSend: function(xhr) {
          xhr.setRequestHeader('${_csrf.headerName}', '${_csrf.token}');
        },
        success: function() {
          const fileName = 'upload_scrutiny_cases.xlsx';
          window.location.href = '/scrutiny_hq/downloadUpdatedExcel?fileName=' + encodeURIComponent(fileName);
        },
        error: function(xhr) {
          $.alert('An error occurred: ' + (xhr?.responseText || 'Request failed'));
        }
      });
    });

    // Helpers
    function validateDocumentTypeByExt(filename, allowedExts) {
      if (!filename) return false;
      const ext = filename.split('.').pop().toLowerCase();
      return allowedExts.includes(ext);
    }

    function VerifyUploadSizeIsOK(inputId, maxBytes){
      const fld = document.getElementById(inputId);
      return !(fld.files && fld.files.length === 1 && fld.files[0].size > maxBytes);
    }

    // Submit with client-side validation
    window.submitForm = function () {
      const extensionNo = $("#extensionNo").val()?.trim();
      const category    = $("#category").val();
      const pdfVal      = $("#pdfFile").val();
      const excelVal    = $("#excelFile").val();
      let flag = false;

      $("#dispatch_alert,#category_alert,#pdf_alert,#excel_alert").text('').addClass('hide-vis');

      if (!extensionNo) {
        $("#dispatch_alert").text("Please provide dispatch number").removeClass('hide-vis').addClass('text-danger'); flag = true;
      } else if (extensionNo.length > 40) {
        $("#dispatch_alert").text("Please provide correct dispatch number").removeClass('hide-vis').addClass('text-danger'); flag = true;
      }

      if (!category) {
        $("#category_alert").text("Please select category").removeClass('hide-vis').addClass('text-danger'); flag = true;
      }

      if (!pdfVal) {
        $("#pdf_alert").text("Please upload pdf file").removeClass('hide-vis'); flag = true;
      }

      if (!excelVal) {
        $("#excel_alert").text("Please upload excel sheet").removeClass('hide-vis'); flag = true;
      }

      // Size: PDF 100MB, Excel 2MB
      if (pdfVal && !VerifyUploadSizeIsOK("pdfFile", 100 * 1024 * 1024)) {
        $("#pdf_alert").text("Please upload pdf file upto 100MB").removeClass('hide-vis'); flag = true;
      }
      if (excelVal && !VerifyUploadSizeIsOK("excelFile", 2 * 1024 * 1024)) {
        $("#excel_alert").text("Please upload the excel within 2MB").removeClass('hide-vis'); flag = true;
      }

      // Extension checks
      if (pdfVal && !validateDocumentTypeByExt(pdfVal, ['pdf'])) {
        $("#pdf_alert").text("Please upload a PDF file").removeClass('hide-vis'); flag = true;
      }
      if (excelVal && !validateDocumentTypeByExt(excelVal, ['xls', 'xlsx'])) {
        $("#excel_alert").text("Please upload an Excel file (.xls/.xlsx)").removeClass('hide-vis'); flag = true;
      }

      if (!flag) {
        // Ask for confirmation, then submit
        $.confirm({
          title: 'Confirm!',
          content: 'Do you want to proceed ahead with uploading of case(s)?',
          buttons: {
            submit: () => $("#uploadScrutinyForm")[0].submit(),
            close : () => $.alert('Canceled!')
          }
        });
      }
    };
  </script>
</body>
</html>
