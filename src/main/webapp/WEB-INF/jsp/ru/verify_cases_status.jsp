<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Verify Case Status</title>

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

  <style>
    :root{
      --hp-sidebar-width: 210px;
      --hp-header-height: 56px;
    }
    .app-header{ height: var(--hp-header-height); }
    .app-content{ padding: 1rem; }
    .content-header{ padding: .5rem 0 .75rem; }

    .app-sidebar{ width: var(--hp-sidebar-width) !important; }
    .app-main{ margin-left: 0 !important; transition: margin-left .2s ease; }
    body.sidebar-collapse .app-main{ margin-left: 0 !important; }

    @media (max-width: 991.98px){
      .app-main{ margin-left: 0 !important; }
    }
    .app-content > .container,
    .app-content > .container-sm,
    .app-content > .container-md,
    .app-content > .container-lg,
    .app-content > .container-xl,
    .app-content > .container-xxl,
    .app-content > .container-fluid{
      max-width:100% !important; width:100% !important;
    }
  </style>
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- Header / Sidebar -->
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <!-- Modals you already use -->
  <jsp:include page="../ru/verifier_recommended.jsp"/>
  <jsp:include page="../ru/verifier_raisequery.jsp"/>
  <jsp:include page="../ru/appeal_revision_popup.jsp"/>

  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6"><h1 class="m-0">Verify Case Status</h1></div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/ru/dashboard">Home</a></li>
                <li class="breadcrumb-item active">Verify Case Status</li>
              </ol>
            </div>
          </div>
        </div>

        <div class="card card-primary">
          <div class="card-header"><h3 class="card-title mb-0">Verify Cases</h3></div>
          <div class="card-body">

            <!-- Alerts (unchanged) -->
            <div class="alert alert-success d-none" id="commonBoostrapAlertSuccess">
              <div id="caseRecommendedTagLine" class="d-none">Case Recommended Successfully !</div>
            </div>
            <div class="alert alert-success d-none" id="commonBoostrapAlertFail">
              <div id="raisedQueryTagLine" class="d-none">Query Raised Successfully !</div>
            </div>
            <div class="alert alert-success d-none" id="caseAppealedAlertSuccess">
              <div id="caseAppealedTagLine" class="d-none">Case recommended for appeal successfully !</div>
            </div>
            <div class="alert alert-success d-none" id="caseRevisionAlertSuccess">
              <div id="caseRevisionTagLine" class="d-none">Case recommended for revision successfully !</div>
            </div>

            <!-- Filter -->
            <div class="row g-3 align-items-end">
              <div class="col-md-6">
                <label class="form-label">Category</label>
                <select id="category" name="category"
                        class="selectpicker form-select"
                        data-live-search="true" title="Please Select Category">
                  <option value="NA">Select All</option>
                  <c:forEach items="${categoryNameList}" var="category">
                    <c:choose>
                      <c:when test="${category eq categoryName}">
                        <option value="${category}" selected="selected">${category}</option>
                      </c:when>
                      <c:otherwise>
                        <option value="${category}">${category}</option>
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </select>
              </div>
            </div>

            <!-- Table -->
            <div class="table-responsive mt-3">
              <table id="example1" class="table table-bordered table-striped w-100">
                <thead>
                <tr>
                  <th class="text-center align-middle">GSTIN</th>
                  <th class="text-center align-middle">Taxpayer Name</th>
                  <th class="text-center align-middle">Jurisdiction</th>
                  <th class="text-center align-middle">Period</th>
                  <th class="text-center align-middle">Reporting Date (DD-MM-YYYY)</th>
                  <th class="text-center align-middle">Indicative Value(₹)</th>
                  <th class="text-center align-middle">Case Category</th>
                  <th class="text-center align-middle">Updating Date</th>
                  <th class="text-center align-middle">Action Status</th>
                  <th class="text-center align-middle">Case Id</th>
                  <th class="text-center align-middle">Case Stage</th>
                  <th class="text-center align-middle">Case Stage ARN</th>
                  <th class="text-center align-middle">Amount(₹)</th>
                  <th class="text-center align-middle">Recovery Stage</th>
                  <th class="text-center align-middle">Recovery Stage ARN</th>
                  <th class="text-center align-middle">Recovery Via DRC03(₹)</th>
                  <th class="text-center align-middle">Recovery Against Demand(₹)</th>
                  <th class="text-center align-middle">Parameter</th>
                  <th class="text-center align-middle">Verifier Remarks</th>
                  <th class="text-center align-middle">Approver Remarks</th>
                  <th class="text-center align-middle">Supporting File</th>
                  <th class="text-center align-middle">Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${verifierCaseList}" var="object">
                  <tr>
                    <td><c:out value="${object.id.GSTIN}"/></td>
                    <td><c:out value="${object.taxpayerName}"/></td>
                    <td><c:out value="${object.locationDetails.locationName}"/></td>
                    <td><c:out value="${object.id.period}"/></td>
                    <td><fmt:formatDate value="${object.id.caseReportingDate}" pattern="dd-MM-yyyy"/></td>
                    <td><c:out value="${object.indicativeTaxValue}"/></td>
                    <td><c:out value="${object.category}"/></td>
                    <td><fmt:formatDate value="${object.caseUpdateDate}" pattern="dd-MM-yyyy HH:mm"/></td>
                    <td><c:out value="${object.actionStatus.name}"/></td>
                    <td><c:out value="${object.caseId}"/></td>
                    <td><c:out value="${object.caseStage.name}"/></td>
                    <td><c:out value="${object.caseStageArn}"/></td>
                    <td><c:out value="${object.demand}"/></td>
                    <td><c:out value="${object.recoveryStage.name}"/></td>
                    <td><c:out value="${object.recoveryStageArn}"/></td>
                    <td><c:out value="${object.recoveryByDRC3}"/></td>
                    <td><c:out value="${object.recoveryAgainstDemand}"/></td>
                    <td><c:out value="${object.parameter}"/></td>
                    <td>
                      <c:forEach items="${object.remark}" var="remark">
                        <i class="fas fa-pen fa-xs me-1"></i><c:out value="${remark}"/><br/>
                      </c:forEach>
                    </td>
                    <td>
                      <c:forEach items="${object.apRemarks}" var="apRemark">
                        <i class="fas fa-pen fa-xs me-1"></i><c:out value="${apRemark}"/><br/>
                      </c:forEach>
                    </td>
                    <td class="text-center">
                      <a href="/ru/downloadFile?fileName=${object.fileName}" class="btn btn-primary">
                        <i class="fas fa-download"></i>
                      </a>
                    </td>
                    <td>
                      <button type="button"
                              class="btn btn-primary mb-1"
                              onclick="callRecommended('${object.id.GSTIN}','${object.locationDetails.locationName}','${object.id.caseReportingDate}','${object.id.period}');">
                        Recommend
                      </button>
                      <button type="button"
                              class="btn btn-primary mb-1"
                              onclick="callRaiseQuery('${object.id.GSTIN}','${object.locationDetails.locationName}','${object.id.caseReportingDate}','${object.id.period}');">
                        Raise Query
                      </button>
                      <button type="button"
                              class="btn btn-primary"
                              onclick="callAppealOrRevision('${object.id.GSTIN}','${object.id.caseReportingDate}','${object.id.period}');">
                        Appeal/Revision
                      </button>
                    </td>
                  </tr>
                </c:forEach>
                </tbody>
              </table>
            </div>

          </div>
        </div>

      </div>
    </div>
  </main>

  <jsp:include page="../layout/footer.jsp"/>

</div>

<div class="sidebar-overlay" data-lte-dismiss="sidebar"></div>

<!-- Scripts -->
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
  /* Sync content with real sidebar width */
  function setSidebarVar(){
    const sb = document.querySelector('.app-sidebar');
    const collapsed = document.body.classList.contains('sidebar-collapse');
    const px = collapsed ? 0 : (sb ? sb.offsetWidth : 210);
    document.documentElement.style.setProperty('--hp-sidebar-width', px + 'px');
  }
  setSidebarVar();
  new MutationObserver(setSidebarVar).observe(document.body, {attributes:true, attributeFilter:['class']});
  window.addEventListener('resize', setSidebarVar);

  /* DataTables */
  $(function () {
    $("#example1").DataTable({
      responsive: false,
      lengthChange: false,
      autoWidth: true,
      scrollX: true,
      buttons: ["excel","pdf","print"]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
  });

  /* Category change (logic unchanged) */
  $(function () {
    $("#category").on('change', function(){
      const selectedValue = $(this).val();
      window.location.href = '/ru/verify_cases_status?categoryName=' + encodeURIComponent(selectedValue);
    });
  });

  /* Modal helpers updated to Bootstrap 5 API (no logic change) */
  function callRecommended(gstin, circle, reportingDate, period) {
    $("#gstinno").val(gstin);
    $("#circle").val(circle);
    $("#reportingdate").val(reportingDate);
    $("#period").val(period);
    const m = bootstrap.Modal.getOrCreateInstance(document.getElementById('closeCaseModal'));
    m.show();
  }
  function callAppealOrRevision(gstin, reportingDate, period) {
    $("#appRegGstiNo").val(gstin);
    $("#appRegReportingdate").val(reportingDate);
    $("#appRegPeriod").val(period);
    const m = bootstrap.Modal.getOrCreateInstance(document.getElementById('appealRevisonModal'));
    m.show();
  }
  function callRaiseQuery(gstin, circle, reportingDate, period) {
    $("#gstinnorq").val(gstin);
    $("#circlerq").val(circle);
    $("#reportingdaterq").val(reportingDate);
    $("#periodrq").val(period);
    const m = bootstrap.Modal.getOrCreateInstance(document.getElementById('raisequeryModal'));
    m.show();
  }

  /* Hardening (unchanged) */
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
</script>

<!-- Existing confirmation modal -->
<div class="modal fade" id="closeCaseModal" tabindex="-1" aria-labelledby="closeCaseModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div>
        <div class="modal-header">
          <h5 class="modal-title" id="confirmationModalTitle">Do you want to proceed ahead with case recommendation ?</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <p>I hereby undertake that;</p>
          <p><i class="fa fa-check"></i> All the points raised by EIU pertaining to this Taxpayer have been dealt carefully in the light of the provisions of the GST laws</p>
          <p><i class="fa fa-check"></i> Applicable Tax has been levied as per the provisions of GST acts/rules 2017</p>
          <p><i class="fa fa-check"></i> Applicable Interest has been levied as per the provisions of GST acts/rules 2017</p>
          <p><i class="fa fa-check"></i> Applicable Penalty has been levied as per the provisions of GST acts/rules 2017</p>
          <p><input type="checkbox" id="mycheckbox" name="checkbox"> I hereby declare that above information is true and correct to the best of my knowledge</p>
        </div>
        <input type="hidden" id="gstno" name="gstno">
        <input type="hidden" id="date" name="date">
        <input type="hidden" id="period" name="period">
        <div class="modal-footer">
          <div id="checked" style="display:none">
            <button onclick="submitVerifierDeclaration()" class="btn btn-primary" id="okayBtn">Okay</button>
          </div>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" id="closeBtn">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</div>
<script>
  $('#mycheckbox').on('change', function () {
    document.getElementById('checked').style.display = this.checked ? 'block' : 'none';
  });
</script>
</body>
</html>
