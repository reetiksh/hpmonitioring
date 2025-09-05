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
          <div class="col-sm-6"><h1 class="m-0">Upload Self Detected Cases</h1></div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="/fo/dashboard">Home</a></li>
              <li class="breadcrumb-item active">Upload Self Detected Cases</li>
            </ol>
          </div>
        </div>
      </div>
    </div>

    <section class="content">
      <div class="container-fluid">
        <div class="row"><div class="col-md-12">
          <div class="card card-primary">
            <div class="card-header"><h3 class="card-title">Upload Self Detected Cases</h3></div>

            <form method="POST" id="uploadSelfDetectedCaseForm" name="uploadSelfDetectedCaseForm"
                  action="/scrutiny_fo/self_detected_cases" enctype="multipart/form-data" modelAttribute="selfDetectedCase">
              <div class="card-body">
                <div class="row">
                  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                  <div id="errorMessage" style="color: red;"></div>

                  <div class="col-md-4">
                    <div class="form-group" title="GSTIN should be upper-case alphanumeric (15 chars)">
                      <label for="gstIn">GSTIN <span style="color:red;">*</span><span id="gstIn_alert"></span></label>
                      <input type="text" class="form-control" id="gstIn" name="gstIn"
                             placeholder="Please Enter GSTIN" minlength="15" maxlength="15"
                             oninput="this.value=this.value.toUpperCase().replace(/[^A-Z0-9]/g,'')"
                             required />
                    </div>
                  </div>

                  <div class="col-md-4">
                    <div class="form-group" title="Taxpayer Name should not be more than 200 letters">
                      <label for="taxpayerName">Taxpayer Name <span style="color:red;">*</span><span id="taxpayerName_alert"></span></label>
                      <input type="text" class="form-control" id="taxpayerName" name="taxpayerName"
                             maxlength="200" placeholder="Please Enter Taxpayer Name" required />
                    </div>
                  </div>

                  <div class="col-md-4">
                    <div class="form-group" title="Case Id should be upper-case alphanumeric (15 chars)">
                      <label for="caseId">Case Id <span style="color:red;">*</span><span id="caseId_alert"></span></label>
                      <input type="text" class="form-control" id="caseId" name="caseId"
                             placeholder="Please Enter Case Id" minlength="15" maxlength="15"
                             oninput="this.value=this.value.toUpperCase().replace(/[^A-Z0-9]/g,'')"
                             required />
                    </div>
                  </div>

                  <div class="col-md-4">
                    <div class="form-group" title="Only digits, up to 12">
                      <label for="suspectedIndicativeTaxValue">Suspected Indicative Tax Value (₹)
                        <span style="color:red;">*</span><span id="susIndicative_alert"></span></label>
                      <input type="text" class="form-control" id="suspectedIndicativeTaxValue" name="suspectedIndicativeTaxValue"
                             placeholder="Please Enter Suspected Indicative Tax Value"
                             inputmode="numeric" maxlength="12" pattern="\\d{1,12}"
                             oninput="this.value=this.value.replace(/\\D/g,'')"
                             required />
                    </div>
                  </div>

                  <div class="col-md-4">
                    <div class="form-group" title="Case Reporting Date should not be a future date">
                      <label for="caseReportingDate">Case Reporting Date
                        <span style="color:red;">*</span><span id="caseReportingDate_alert"></span></label>
                      <input type="date" class="form-control" id="caseReportingDate" name="caseReportingDate"
                             placeholder="Please Select Case Reporting Date" required />
                    </div>
                  </div>

                  <div class="col-md-4">
                    <div class="form-group">
                      <label for="assignedToCircle">Jurisdiction <span style="color:red;">*</span>
                        <span id="jurisdiction_alert"></span></label>
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
                      <label for="period">Period <span style="color:red;">*</span><span id="period_alert"></span></label>
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
                      <label for="category">Category <span style="color:red;">*</span><span id="category_only_alert"></span></label>
                      <select id="category" name="category"
                              class="selectpicker col-md-12" data-live-search="true"
                              title="Please Select Category" required>
                        <c:forEach items="${categories}" var="categories">
                          <c:choose>
                            <c:when test="${categories eq 'Self Detected Cases'}">
                              <option value="${categories}" selected="selected">${categories}</option>
                            </c:when>
                            <c:otherwise>
                              <option value="${categories}">${categories}</option>
                            </c:otherwise>
                          </c:choose>
                        </c:forEach>
                      </select>
                    </div>
                  </div>

                  <div class="col-md-4">
                    <div class="form-group">
                      <label for="remark">Remarks <span style="color:red;">*</span><span id="remark_alert"></span></label>
                      <select id="remark" name="remark"
                              class="selectpicker col-md-12" data-live-search="true"
                              title="Please Select Remarks" multiple required onchange="dropdownChanged()">
                        <c:forEach items="${remarks}" var="remark">
                          <option value="${remark.paramName}">${remark.paramName}</option>
                        </c:forEach>
                        <!-- Ensure you have an 'Others' option in data to trigger the text field -->
                      </select>
                    </div>
                  </div>

                  <div class="col-md-4" style="display:none;" id="otherRemarkDiv">
                    <div class="form-group">
                      <label for="otherRemarks">Other Remarks <span style="color:red;">*</span>
                        <span id="otherRemarks_alert"></span></label>
                      <input type="text" class="form-control" id="otherRemarks" name="otherRemarks"
                             placeholder="Please Enter Your Remarks" />
                    </div>
                  </div>

                </div>
              </div>
              <div class="card-footer">
                <button type="submit" class="btn btn-primary">Submit</button>
              </div>
            </form>
          </div>

          <c:if test="${not empty successMessage}">
            <div class="col-12 alert alert-success alert-dismissible fade show" id="message" role="alert">
              <strong>${successMessage}</strong><br>
              <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <nav class="navbar navbar-dark bg-primary" style="margin-bottom:5px;border-top-left-radius:.25rem;border-top-right-radius:.25rem;">
              Recently uploaded cases
            </nav>
          </c:if>

          <c:if test="${not empty errorList}">
            <div class="col-12 alert alert-danger alert-dismissible fade show" role="alert" style="max-height:300px;overflow-y:auto;">
              <c:forEach items="${errorList}" var="excelError">
                <strong>${excelError}</strong><br>
              </c:forEach>
              <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <c:if test="${!empty uploadData}">
              <nav class="navbar navbar-dark bg-primary" style="margin-bottom:5px;border-top-left-radius:.25rem;border-top-right-radius:.25rem;">
                Correct rows from uploaded excel
              </nav>
            </c:if>
          </c:if>

          <c:if test="${!empty uploadData}">
            <table id="example1" class="table table-bordered table-striped">
              <thead>
                <tr>
                  <th style="text-align:center;vertical-align:middle;">GSTIN</th>
                  <th style="text-align:center;vertical-align:middle;">Taxpayer Name</th>
                  <th style="text-align:center;vertical-align:middle;">Case Reporting Date</th>
                  <th style="text-align:center;vertical-align:middle;">Suspected Indicative Tax Value (₹)</th>
                  <th style="text-align:center;vertical-align:middle;">Period</th>
                  <th style="text-align:center;vertical-align:middle;">Assigned To Circle</th>
                  <th style="text-align:center;vertical-align:middle;">Remarks</th>
                  <th style="text-align:center;vertical-align:middle;">Case Id</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach items="${uploadData}" var="row">
                  <tr>
                    <c:forEach items="${row}" var="cell">
                      <td style="text-align:center;vertical-align:middle;"><c:out value="${cell}" /></td>
                    </c:forEach>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </c:if>

        </div></div>
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

<script>
  // (FYI: blocking right-click/devtools is largely ineffective and harms accessibility)
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => { if (e.ctrlKey && e.key === 'u') e.preventDefault(); if (e.key === 'F12') e.preventDefault(); });
  $(document).ready(function () {
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = function (evt){ if (evt.persisted) disableBack(); }

    // Bootstrap-select
    $('.selectpicker').selectpicker();

    // Case date: disallow future
    const today = new Date().toISOString().split('T')[0];
    $('#caseReportingDate').attr('max', today);

    // Flash success fade-out
    $("#message").fadeTo(5000, 500).slideUp(500, function(){ $("#message").slideUp(500); });

    // DataTable (only if present)
    if ($('#example1').length) {
      $("#example1").DataTable({
        responsive: true, lengthChange: false, autoWidth: false,
        buttons: [{ extend: 'excel', title: null }]
      }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
    }
  });

  // Remarks: show input if “Others” is selected in multi-select
  function dropdownChanged(){
    const $sel = document.getElementById('remark');
    const show = Array.from($sel.options).some(o => o.selected && o.value === 'Others');
    const box = document.getElementById('otherRemarkDiv');
    const input = document.getElementById('otherRemarks');
    if (show) { box.style.display = 'block'; input.required = true; }
    else { box.style.display = 'none'; input.required = false; input.value=''; }
  }

  // Submit confirm
  $('form').on('submit', function(e){
    e.preventDefault();
    $.confirm({
      title: 'Confirm!',
      content: 'Do you want to proceed ahead with uploading of case(s)?',
      buttons: {
        submit: function(){ e.currentTarget.submit(); },
        close: function(){ $.alert('Canceled!'); }
      }
    });
  });
</script>
</body>
</html>
