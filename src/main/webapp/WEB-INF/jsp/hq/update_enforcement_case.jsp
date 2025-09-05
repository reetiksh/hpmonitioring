<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST |Review Case List</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png"/>

  <!-- AdminLTE 4 / Bootstrap 5 / Font Awesome 6 -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.5.2/css/all.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/css/adminlte.min.css" rel="stylesheet"/>

  <!-- DataTables (Bootstrap 5 build + Responsive + Buttons) -->
  <link href="https://cdn.datatables.net/v/bs5/dt-2.0.8/r-3.0.2/b-3.0.2/datatables.min.css" rel="stylesheet"/>

  <!-- bootstrap-select (BS5-compatible) -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.14.0-beta3/dist/css/bootstrap-select.min.css" rel="stylesheet"/>

  <!-- jquery-confirm (kept for your existing usage) -->
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page header -->
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Update Enforcement Case</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-end">
              <li class="breadcrumb-item"><a href="/hq/dashboard">Home</a></li>
              <li class="breadcrumb-item active">Update Enforcement Case</li>
            </ol>
          </div>
        </div>

        <!-- Search Card -->
        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title">Search for Enforcement Case</h3>
          </div>

          <form method="GET" id="searchEnforcementCases" name="searchEnforcementCases" action="<c:url value='/hq/search_enforcement_cases' />" enctype="multipart/form-data">
            <div class="card-body">
              <div class="form-group col-md-12">
                <div class="row g-3">
                  <div class="col-md-4">
                    <label for="GSTIN">GSTIN <span style="color: red;"> *</span><span id="GSTIN_alert"></span></label>
                    <input type="text" class="form-control" id="GSTIN" name="GSTIN" placeholder="Please Enter GSTIN" required>
                  </div>
                  <div class="col-md-4">
                    <label for="period">Period <span id="period_alert"></span></label>
                    <select id="period" name="period" class="selectpicker w-100" data-live-search="true" title="Please Select Period">
                      <c:forEach items="${periods}" var="period">
                        <c:choose>
                          <c:when test="${period eq selectPeriod}">
                            <option value="${period}" selected="selected">${period}</option>
                          </c:when>
                          <c:otherwise>
                            <option value="${period}">${period}</option>
                          </c:otherwise>
                        </c:choose>
                      </c:forEach>
                    </select>
                  </div>
                  <div class="col-md-3">
                    <label for="caseReportingDate">Case Reporting Date <span id="caseReportingDate_alert"></span></label>
                    <input type="date" class="form-control" id="caseReportingDate" name="caseReportingDate" placeholder="Please enter Case Reporting Date"/>
                  </div>
                  <div class="col-md-1 d-flex align-items-end">
                    <button type="submit" class="btn btn-primary w-100"><i class="fa fa-search" aria-hidden="true"></i></button>
                  </div>
                </div>
              </div>
            </div>
          </form>
        </div>

        <c:if test="${not empty successMessage}">
          <div class="col-12 alert alert-success alert-dismissible fade show" id="message" role="alert" style="max-height: 500px; overflow-y: auto;">
            <strong>${successMessage}</strong><br>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
          </div>
        </c:if>

        <c:if test="${not empty caseList}">
          <div class="card card-primary">
            <div class="card-header">
              <h3 class="card-title">Enforcement Cases</h3>
            </div>
            <div class="card-body">
              <div class="form-group col-md-12">
                <table id="example1" class="table table-bordered table-striped w-100">
                  <thead>
                  <tr>
                    <th style="text-align: center; vertical-align: middle;">GSTIN</th>
                    <th style="text-align: center; vertical-align: middle;">Taxpayer Name</th>
                    <th style="text-align: center; vertical-align: middle;">Jurisdiction</th>
                    <th style="text-align: center; vertical-align: middle;">Category</th>
                    <th style="text-align: center; vertical-align: middle;">Period</th>
                    <th style="text-align: center; vertical-align: middle;">Dispatch No.</th>
                    <th style="text-align: center; vertical-align: middle;">Reporting Date<br>(DD-MM-YYYY)</th>
                    <th style="text-align: center; vertical-align: middle;">Indicative Value(₹)</th>
                    <th style="text-align: center; vertical-align: middle;">Currently With</th>
                    <th style="text-align: center; vertical-align: middle;">Action Status</th>
                    <th style="text-align: center; vertical-align: middle;">Case ID</th>
                    <th style="text-align: center; vertical-align: middle;">Case Stage</th>
                    <th style="text-align: center; vertical-align: middle;">Case Stage ARN</th>
                    <th style="text-align: center; vertical-align: middle;">Amount(₹)</th>
                    <th style="text-align: center; vertical-align: middle;">Recovery Stage</th>
                    <th style="text-align: center; vertical-align: middle;">Recovery Stage ARN</th>
                    <th style="text-align: center; vertical-align: middle;">Recovery Via DRC03(₹)</th>
                    <th style="text-align: center; vertical-align: middle;">Recovery Against Demand(₹)</th>
                    <th style="text-align: center; vertical-align: middle;">Parameters</th>
                    <th style="text-align: center; vertical-align: middle;">Supporting File</th>
                    <th style="text-align: center; vertical-align: middle;">Action</th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach items="${caseList}" var="object">
                    <tr>
                      <td><c:out value="${object.id.GSTIN}" /></td>
                      <td><c:out value="${object.taxpayerName}" /></td>
                      <td><c:out value="${object.locationDetails.locationName}"/></td>
                      <td><c:out value="${object.category}"/></td>
                      <td><c:out value="${object.id.period}" /></td>
                      <td><c:out value="${object.extensionNo}" /></td>
                      <td><fmt:formatDate value="${object.id.caseReportingDate}" pattern="dd-MM-yyyy" /></td>
                      <td><fmt:formatNumber value="${object.indicativeTaxValue}" pattern="#,##,##0" /></td>
                      <td><c:out value="${object.assignedTo}" /></td>
                      <td><c:out value="${object.actionStatus.name}" /></td>
                      <td><c:out value="${object.caseId}" /></td>
                      <td><c:out value="${object.caseStage.name}" /></td>
                      <td><c:out value="${object.caseStageArn}" /></td>
                      <td><fmt:formatNumber value="${object.demand}" pattern="#,##,##0" /></td>
                      <td><c:out value="${object.recoveryStage.name}" /></td>
                      <td><c:out value="${object.recoveryStageArn}" /></td>
                      <td><fmt:formatNumber value="${object.recoveryByDRC3}" pattern="#,##,##0" /></td>
                      <td><fmt:formatNumber value="${object.recoveryAgainstDemand}" pattern="#,##,##0" /></td>
                      <td><c:out value="${object.parameter}" /></td>
                      <td style="text-align: center;">
                        <a href="/hq/downloadFile?fileName=${object.extensionNoDocument.extensionFileName}">
                          <button type="button" class="btn btn-primary">
                            <i class="fas fa-download"></i>
                          </button>
                        </a>
                      </td>
                      <td style="text-align: center;">
                        <c:if test="${object.assignedTo eq 'FO'}">
                          <button type="button" title="Edit User" class="btn btn-info" onclick="viewEnforcementCases('${object.id.GSTIN}', '${object.id.period}', '${object.id.caseReportingDate}')"><i class="fas fa-edit nav-icon"></i></button>
                        </c:if>
                        <c:if test="${object.assignedTo ne 'FO'}">
                          <button type="button" title="Edit User" class="btn btn-info" onclick="viewEnforcementCases('${object.id.GSTIN}', '${object.id.period}', '${object.id.caseReportingDate}')" disabled><i class="fas fa-edit nav-icon"></i></button>
                        </c:if>
                      </td>
                    </tr>
                  </c:forEach>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </c:if>

      </div>
    </div>
  </main>

  <jsp:include page="../layout/footer.jsp"/>
</div>

<!-- Modal (BS5 markup) -->
<div class="modal fade" id="viewTransferRoleModal" tabindex="-1" aria-labelledby="viewTransferRoleModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="updateSummaryViewModalTitle">Update Enforcement Review Cases Details</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body" id="viewTransferRole"></div>
    </div>
  </div>
</div>

<!-- Scripts: jQuery -> Bootstrap 5 -> AdminLTE 4 -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/js/adminlte.min.js"></script>

<!-- DataTables bundle (DT + Responsive + Buttons + JSZip + pdfmake) -->
<script src="https://cdn.datatables.net/v/bs5/dt-2.0.8/r-3.0.2/b-3.0.2/jszip-3.10.1/pdfmake-0.2.7/vfs_fonts-0.1.0/datatables.min.js"></script>

<!-- bootstrap-select (BS5) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.14.0-beta3/dist/js/bootstrap-select.min.js"></script>

<!-- jquery-confirm (kept) -->
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  // Keep your original UX/security handlers
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    if ((e.ctrlKey && e.key === 'u') || e.key === 'F12') e.preventDefault();
  });
  $(document).ready(function () {
    function disableBack(){ window.history.forward() }
    window.onload = disableBack();
    window.onpageshow = evt => { if (evt.persisted) disableBack() };
  });
  document.onkeydown = function (e) {
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) e.preventDefault();
  };
</script>

<script>
  // Bootstrap 5 shim so existing $("#...").modal('show') logic keeps working
  (function($){
    if (!$.fn.modal) {
      $.fn.modal = function(action){
        return this.each(function(){
          const modal = bootstrap.Modal.getOrCreateInstance(this);
          if (action === 'show') modal.show();
          else if (action === 'hide') modal.hide();
        });
      };
    }
  })(jQuery);

  // DataTable init (logic preserved)
  $(function () {
    $("#example1").DataTable({
      responsive: false,
      lengthChange: false,
      autoWidth: true,
      scrollX: true,
      buttons: ["excel","pdf","print"]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');

    // bootstrap-select init
    $('.selectpicker').selectpicker();

    // success alert fade/slide (unchanged behavior)
    $("#message").fadeTo(5000, 500).slideUp(500, function(){ $("#message").slideUp(500); });
  });
</script>

<script>
  function viewEnforcementCases(GSTIN, period, caseReportingDate){
    var date = String(caseReportingDate);
    var caseReportingDateStr = date.substring(0, 11);
    var link = '/hq/view_enforcement_case_to_edit?GSTIN=' + GSTIN + '&period=' + period + '&caseReportingDateStr=' + caseReportingDateStr;

    $.ajax({
      url: '/checkLoginStatus',
      method: 'get',
      async: false,
      success: function(result){
        const myJSON = JSON.parse(result);
        if(result=='true'){
          $("#viewTransferRole").load(link, function(response, status, xhr){
            if(status == 'success'){
              $("#viewTransferRoleModal").modal('show');
            } else {
              console.log("failed");
            }
          });
        } else if(result=='false'){
          window.location.reload();
        }
      }
    });
  }
</script>

<script>
  $('form').on('submit', function(oEvent) {
    oEvent.preventDefault();
    $.confirm({
      title : 'Confirm!',
      content : 'Do you want to proceed ahead to search the case(s)?',
      buttons : {
        submit : function() { oEvent.currentTarget.submit(); },
        close  : function() { $.alert('Canceled!'); }
      }
    });
  });
</script>
</body>
</html>
