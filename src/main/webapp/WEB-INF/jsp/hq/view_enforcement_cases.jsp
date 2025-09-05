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
  <title>HP GST | Review Enforcement Cases</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png" />
  <!-- Plugins & theme (paths unchanged) -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css" />
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css" />
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css" />
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css" />
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css" />
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css" />
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">
  <jsp:include page="../layout/header.jsp" />
  <jsp:include page="../layout/sidebar.jsp" />

  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page header -->
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1 class="m-0">Review Enforcement Cases</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="/hq/dashboard">Home</a></li>
              <li class="breadcrumb-item active">Review Enforcement Cases</li>
            </ol>
          </div>
        </div>

        <!-- Search Card -->
        <div class="row">
          <div class="col-12">
            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title">Review Cases List</h3>
              </div>

              <form method="GET" id="searchEnforcementCases" name="searchEnforcementCases"
                    action="<c:url value='/hq/view_enforcement_cases/search_enforcement_cases' />"
                    enctype="multipart/form-data">
                <div class="card-body">
                  <div class="form-group col-md-12">
                    <div class="row">
                      <div class="col-md-4">
                        <div class="row">
                          <div class="col-md-12">
                            <label for="GSTIN">GSTIN <span style="color:red;"> *</span><span id="GSTIN_alert"></span></label>
                          </div>
                          <div class="col-md-12">
                            <input type="text" class="form-control" id="GSTIN" name="GSTIN" placeholder="Please Enter GSTIN" required>
                          </div>
                        </div>
                      </div>
                      <div class="col-md-1 d-flex align-items-end justify-content-center">
                        <button type="submit" class="btn btn-primary mt-2">
                          <i class="fa fa-search" aria-hidden="true"></i>
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </form>

              <!-- Results -->
              <c:if test="${not empty caseList}">
                <div class="card-body">
                  <table id="example1" class="table table-bordered table-striped w-100">
                    <thead>
                      <tr>
                        <th style="text-align:center; vertical-align:middle;">GSTIN</th>
                        <th style="text-align:center; vertical-align:middle;">Taxpayer Name</th>
                        <th style="text-align:center; vertical-align:middle;">Jurisdiction</th>
                        <th style="text-align:center; vertical-align:middle;">Period</th>
                        <th style="text-align:center; vertical-align:middle;">Dispatch No.</th>
                        <th style="text-align:center; vertical-align:middle;">Reporting Date<br>(DD-MM-YYYY)</th>
                        <th style="text-align:center; vertical-align:middle;">Indicative Value(₹)</th>
                        <th style="text-align:center; vertical-align:middle;">Currently With</th>
                        <th style="text-align:center; vertical-align:middle;">Action Status</th>
                        <th style="text-align:center; vertical-align:middle;">Case ID</th>
                        <th style="text-align:center; vertical-align:middle;">Case Stage</th>
                        <th style="text-align:center; vertical-align:middle;">Case Stage ARN</th>
                        <th style="text-align:center; vertical-align:middle;">Amount(₹)</th>
                        <th style="text-align:center; vertical-align:middle;">Recovery Stage</th>
                        <th style="text-align:center; vertical-align:middle;">Recovery Stage ARN</th>
                        <th style="text-align:center; vertical-align:middle;">Recovery Via DRC03(₹)</th>
                        <th style="text-align:center; vertical-align:middle;">Recovery Against Demand(₹)</th>
                        <th style="text-align:center; vertical-align:middle;">Parameters</th>
                        <th style="text-align:center; vertical-align:middle;">Supporting File</th>
                      </tr>
                    </thead>
                    <tbody>
                      <c:forEach items="${caseList}" var="object">
                        <tr>
                          <td>
                            <a href="/hq/view_enforcement_case_history?GSTIN=${object.id.GSTIN}&period=${object.id.period}&caseReportingDate=${object.id.caseReportingDate}">
                              <c:out value="${object.id.GSTIN}" />
                            </a>
                          </td>
                          <td><c:out value="${object.taxpayerName}" /></td>
                          <td><c:out value="${object.locationDetails.locationName}" /></td>
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
                          <td class="text-center">
                            <c:if test="${not empty object.extensionNoDocument.extensionFileName}">
                              <a href="/hq/downloadFile?fileName=${object.extensionNoDocument.extensionFileName}">
                                <button type="button" class="btn btn-primary">
                                  <i class="fas fa-download"></i>
                                </button>
                              </a>
                            </c:if>
                            <c:if test="${empty object.extensionNoDocument.extensionFileName}">
                              <button type="button" class="btn btn-primary" disabled>
                                <i class="fas fa-download"></i>
                              </button>
                            </c:if>
                          </td>
                        </tr>
                      </c:forEach>
                    </tbody>
                  </table>
                </div>
              </c:if>
            </div>
          </div>
        </div>

      </div>
    </div>
  </main>

  <jsp:include page="../layout/footer.jsp" />
</div>

<!-- Scripts -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>

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

<script src="/static/dist/js/adminlte.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<!-- AdminLTE 4 sidebar toggle helper -->
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

<!-- Hardening / prevention (unchanged behavior) -->
<script>
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => { if (e.ctrlKey && e.key === 'u') e.preventDefault(); });
  document.addEventListener('keydown', e => { if (e.key === 'F12') e.preventDefault(); });
  $(document).ready(function () {
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack;
    window.onpageshow = function (evt) { if (evt.persisted) disableBack(); }
  });
  document.onkeydown = function (e) {
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) e.preventDefault();
  };
</script>

<!-- DataTables init: keep your Buttons and horizontal scroll -->
<script>
  $(function () {
    const dt = $("#example1").DataTable({
      responsive: false,
      lengthChange: false,
      autoWidth: true,
      scrollX: true,
      buttons: ["excel", "pdf", "print"]
    });
    dt.buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
  });
</script>

<!-- Guarded snippet to avoid JS errors if elements/datasets aren’t present -->
<script>
  (function () {
    const tableBody = document.getElementById("tableBody");
    if (!tableBody) return; // no client-side re-render target, so bail out
    // If you later pass a JSON array to the page, you can safely parse & render here.
    // const data = JSON.parse('${fn:escapeXml(caseListJson)}');
  })();
</script>

</body>
</html>
