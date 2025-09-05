<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Recommend For Closure</title>

  <!-- AdminLTE 4 / Bootstrap 5 -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">

  <!-- DataTables (Bootstrap 5 builds) -->
  <link rel="stylesheet" href="/static/plugins/datatables-bs5/css/dataTables.bootstrap5.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap5.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap5.min.css">

  <!-- Optional -->
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">

  <!-- Keep sidebar + content perfectly aligned -->
  <style>
    :root{ --hp-sidebar-width: 210px; }
    .app-sidebar{ width: var(--hp-sidebar-width) !important; }
    .app-main{ margin-left: 0px !important; transition: margin-left .2s ease; }
    body.sidebar-collapse .app-main{ margin-left: 0 !important; }
    @media (max-width: 991.98px){ .app-main{ margin-left: 0 !important; } }

    /* Make any .container inside .app-content full width */
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

  <!-- Header / Sidebar (AdminLTE 4 versions) -->
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <!-- (These modals are not used on this list, but kept if you need them elsewhere) -->
  <jsp:include page="../ru/verifier_recommended.jsp"/>
  <jsp:include page="../ru/verifier_raisequery.jsp"/>

  <!-- ====================== MAIN ====================== -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page header -->
        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6"><h1 class="m-0">Recommend For Closure</h1></div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/ru/dashboard">Home</a></li>
                <li class="breadcrumb-item active">Recommend For Closure</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- Card -->
        <div class="card card-primary">
          <div class="card-header"><h3 class="card-title mb-0">Recommend For Closure</h3></div>

          <div class="card-body">
            <div class="table-responsive">
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
                  <th class="text-center align-middle">Status</th>
                  <th class="text-center align-middle">Supporting File</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${recommendedeByVerifierForClosure}" var="object">
                  <tr>
                    <td><c:out value="${object.id.GSTIN}"/></td>
                    <td><c:out value="${object.taxpayerName}"/></td>
                    <td><c:out value="${object.locationDetails.locationName}"/></td>
                    <td><c:out value="${object.id.period}"/></td>
                    <td><fmt:formatDate value="${object.id.caseReportingDate}" pattern="dd-MM-yyyy"/></td>
                    <td><c:out value="${object.indicativeTaxValue}"/></td>
                    <td><c:out value="${object.category}"/></td>
                    <td><fmt:formatDate value="${object.caseUpdateDate}" pattern="dd-MM-yyyy HH:mm:ss"/></td>
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
                    <td>Recommended</td>
                    <td class="text-center">
                      <a href="/ru/downloadFile?fileName=${object.fileName}" class="btn btn-primary">
                        <i class="fas fa-download"></i>
                      </a>
                    </td>
                  </tr>
                </c:forEach>
                </tbody>
              </table>
            </div>
          </div>
        </div>

      </div><!-- /.container-fluid -->
    </div><!-- /.app-content -->
  </main>

  <!-- Footer -->
  <jsp:include page="../layout/footer.jsp"/>

</div><!-- /.app-wrapper -->

<!-- overlay for mobile “tap outside to close” -->
<div class="sidebar-overlay" data-lte-dismiss="sidebar"></div>

<!-- ====================== SCRIPTS ====================== -->
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
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  /* Sync content margin with real sidebar width so toggle works */
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

  /* Hardening */
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });

  /* Optional: if you keep old buttons somewhere, use BS5 modal API (safe no-op if IDs don’t exist) */
  const rb = document.getElementById('recommendedBtn');
  if (rb) rb.addEventListener('click', () => {
    const m = bootstrap.Modal.getOrCreateInstance(document.getElementById('recommendedModal'));
    m && m.show();
  });
  const rq = document.getElementById('raiseQueryBtn');
  if (rq) rq.addEventListener('click', () => {
    const m = bootstrap.Modal.getOrCreateInstance(document.getElementById('raisequeryModal'));
    m && m.show();
  });
</script>
</body>
</html>
