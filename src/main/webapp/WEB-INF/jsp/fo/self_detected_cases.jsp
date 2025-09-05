<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST | Upload Self Detected Cases</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">

  <!-- AdminLTE / Bootstrap 4 -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">

  <!-- Plugins -->
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">

  <!-- (Shown only if recent rows table is rendered) -->
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
</head>

<body class="hold-transition sidebar-mini layout-fixed">
<div class="wrapper">
  <div class="preloader flex-column justify-content-center align-items-center">
    <img class="animation__shake" src="/static/dist/img/AdminLTELogo.png" alt="AdminLTELogo" height="60" width="60">
  </div>

  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <div class="content-wrapper">
    <!-- Header -->
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6"><h1 class="m-0">Upload Self Detected Cases</h1></div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="/fo/dashboard">Home</a></li>
              <li class="breadcrumb-item active">Upload Self Detected Cases</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <!-- Content -->
    <section class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-md-12">

            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title">Upload Self Detected Cases</h3>
              </div>

              <form method="POST"
                    id="uploadSelfDetectedCaseForm"
                    name="uploadSelfDetectedCaseForm"
                    action="/fo/self_detected_cases"
                    enctype="multipart/form-data">
                <div class="card-body">
                  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                  <div class="row">
                    <div class="col-md-4">
                      <div class="form-group" title="GSTIN must be 15 characters, uppercase alphanumeric.">
                        <label for="gstIn">GSTIN <span class="text-danger">*</span><span id="gstIn_alert"></span></label>
                        <input type="text" class="form-control" id="gstIn" name="gstIn"
                               placeholder="Enter GSTIN"
                               minlength="15" maxlength="15"
                               pattern="[A-Z0-9]{15}"
                               oninput="this.value=this.value.toUpperCase().replace(/[^A-Z0-9]/g,'')"
                               required>
                      </div>
                    </div>

                    <div class="col-md-4">
                      <div class="form-group" title="Taxpayer Name (max 200 chars)">
                        <label for="taxpayerName">Taxpayer Name <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="taxpayerName" name="taxpayerName"
                               placeholder="Enter Taxpayer Name" maxlength="200" required>
                      </div>
                    </div>

                    <div class="col-md-4">
                      <div class="form-group" title="Case ID must be 15 characters, uppercase alphanumeric.">
                        <label for="caseId">Case ID <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="caseId" name="caseId"
                               placeholder="Enter Case ID"
                               minlength="15" maxlength="15"
                               pattern="[A-Z0-9]{15}"
                               oninput="this.value=this.value.toUpperCase().replace(/[^A-Z0-9]/g,'')"
                               required>
                      </div>
                    </div>

                    <div class="col-md-4">
                      <div class="form-group" title="Only digits, up to 12.">
                        <label for="suspectedIndicativeTaxValue">Suspected Indicative Tax Value (₹)
                          <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="suspectedIndicativeTaxValue" name="suspectedIndicativeTaxValue"
                               placeholder="Enter Indicative Value"
                               inputmode="numeric" pattern="^[0-9]{1,12}$"
                               oninput="this.value=this.value.replace(/[^0-9]/g,'').slice(0,12)"
                               required>
                      </div>
                    </div>

                    <div class="col-md-4">
                      <div class="form-group" title="Must not be a future date.">
                        <label for="caseReportingDate">Case Reporting Date <span class="text-danger">*</span></label>
                        <input type="date" class="form-control" id="caseReportingDate" name="caseReportingDate" required>
                      </div>
                    </div>

                    <div class="col-md-4">
                      <div class="form-group">
                        <label for="assignedToCircle">Jurisdiction <span class="text-danger">*</span></label>
                        <select id="assignedToCircle" name="assignedToCircle"
                                class="selectpicker col-md-12" data-live-search="true"
                                title="Please Select Jurisdiction" required>
                          <c:forEach items="${jurisdictions}" var="jurisdiction">
                            <option value="${jurisdiction}">${jurisdiction}</option>
                          </c:forEach>
                        </select>
                      </div>
                    </div>

                    <div class="col-md-4">
                      <div class="form-group">
                        <label for="period">Period <span class="text-danger">*</span></label>
                        <select id="period" name="period"
                                class="selectpicker col-md-12" data-live-search="true"
                                title="Please Select Period" required>
                          <c:forEach items="${periods}" var="period">
                            <option value="${period}">${period}</option>
                          </c:forEach>
                        </select>
                      </div>
                    </div>

                    <div class="col-md-4">
                      <div class="form-group">
                        <label for="category">Category <span class="text-danger">*</span></label>
                        <select id="category" name="category"
                                class="selectpicker col-md-12" data-live-search="true"
                                title="Please Select Category" required>
                          <c:forEach items="${categories}" var="cat">
                            <c:choose>
                              <c:when test="${cat eq 'Self Detected Cases'}">
                                <option value="${cat}" selected>${cat}</option>
                              </c:when>
                              <c:otherwise>
                                <option value="${cat}">${cat}</option>
                              </c:otherwise>
                            </c:choose>
                          </c:forEach>
                        </select>
                      </div>
                    </div>

                    <div class="col-md-4">
                      <div class="form-group">
                        <label for="remark">Remarks <span class="text-danger">*</span></label>
                        <select id="remark" name="remark"
                                class="selectpicker col-md-12" data-live-search="true"
                                title="Please Select Remarks" multiple required
                                onchange="toggleOtherRemarks()">
                          <c:forEach items="${remarks}" var="r">
                            <option value="${r.paramName}">${r.paramName}</option>
                          </c:forEach>
                        </select>
                      </div>
                    </div>

                    <div class="col-md-4" id="otherRemarkDiv" style="display:none;">
                      <div class="form-group">
                        <label for="otherRemarks">Other Remarks <span class="text-danger">*</span></label>
                        <input type="text" class="form-control" id="otherRemarks" name="otherRemarks"
                               placeholder="Enter Other Remarks" maxlength="250">
                      </div>
                    </div>
                  </div>
                </div>

                <div class="card-footer">
                  <button type="submit" class="btn btn-primary">Submit</button>
                </div>
              </form>
            </div>

            <!-- Success / Errors -->
            <c:if test="${not empty successMessage}">
              <div class="col-12 alert alert-success alert-dismissible fade show" id="message" role="alert">
                <strong>${successMessage}</strong>
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <nav class="navbar navbar-dark bg-primary mb-2 rounded-top">
                Recently uploaded cases
              </nav>
            </c:if>

            <c:if test="${not empty errorList}">
              <div class="col-12 alert alert-danger alert-dismissible fade show" role="alert" style="max-height:300px; overflow-y:auto;">
                <c:forEach items="${errorList}" var="excelError">
                  <strong>${excelError}</strong><br/>
                </c:forEach>
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
              <c:if test="${!empty uploadData}">
                <nav class="navbar navbar-dark bg-primary mb-2 rounded-top">
                  Correct rows from uploaded excel
                </nav>
              </c:if>
            </c:if>

            <!-- Recently uploaded table -->
            <c:if test="${!empty uploadData}">
              <table id="example1" class="table table-bordered table-striped">
                <thead>
                  <tr>
                    <th class="text-center align-middle">GSTIN</th>
                    <th class="text-center align-middle">Taxpayer Name</th>
                    <th class="text-center align-middle">Case Reporting Date</th>
                    <th class="text-center align-middle">Suspected Indicative Tax Value (₹)</th>
                    <th class="text-center align-middle">Period</th>
                    <th class="text-center align-middle">Assigned To Circle</th>
                    <th class="text-center align-middle">Remarks</th>
                    <th class="text-center align-middle">Other Remarks</th>
                    <th class="text-center align-middle">Case ID</th>
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
            </c:if>

          </div>
        </div>
      </div>
    </section>
  </div>

  <jsp:include page="../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- Core -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<!-- Plugins -->
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<!-- DataTables (used only when table exists) -->
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
  // Light hardening (kept per your pattern)
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
  $(function(){
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = evt => { if (evt.persisted) disableBack(); };
  });

  // Bootstrap-select
  $(function(){ $('.selectpicker').selectpicker(); });

  // Cap reporting date to today
  (function limitDateToToday(){
    const el = document.getElementById('caseReportingDate');
    if (!el) return;
    const d = new Date();
    const pad = n => String(n).padStart(2,'0');
    el.max = `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}`;
  })();

  // Fade success message
  $(function () {
    $("#message").each(function(){
      $(this).fadeTo(5000, .5).slideUp(500, () => $(this).remove());
    });
  });

  // DataTable only if table exists
  $(function () {
    if ($("#example1").length) {
      $("#example1").DataTable({
        responsive: true,
        lengthChange: false,
        autoWidth: false,
        buttons: [{ extend:'excel', title:null }]
      }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
    }
  });

  // Remarks: show/hide "Other Remarks" when "Others" is selected (multi-select)
  function toggleOtherRemarks(){
    const sel = document.getElementById('remark');
    const show = Array.from(sel.options).some(o => o.selected && o.value === 'Others');
    const div = document.getElementById('otherRemarkDiv');
    const input = document.getElementById('otherRemarks');
    if (show) {
      div.style.display = 'block';
      input.required = true;
    } else {
      div.style.display = 'none';
      input.required = false;
      input.value = '';
    }
  }
  window.toggleOtherRemarks = toggleOtherRemarks;

  // Confirm on submit (single handler, scoped to this form)
  $('#uploadSelfDetectedCaseForm').on('submit', function (e) {
    e.preventDefault();
    $.confirm({
      title: 'Confirm!',
      content: 'Do you want to proceed ahead with uploading this case?',
      buttons: {
        Yes: () => e.currentTarget.submit(),
        Cancel: () => $.alert('Canceled!')
      }
    });
  });
</script>
</body>
</html>
