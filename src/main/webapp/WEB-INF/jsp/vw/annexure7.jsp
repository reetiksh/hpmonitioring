<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Annexure VII Report</title>

  <!-- AdminLTE 4 / Bootstrap 5 -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">

  <!-- DataTables (Bootstrap 5 builds) -->
  <link rel="stylesheet" href="/static/plugins/datatables-bs5/css/dataTables.bootstrap5.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap5.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap5.min.css">

  <style>
    /* Right-align numeric columns */
    #annexureTable th:nth-child(n+3),
    #annexureTable td:nth-child(n+3) { text-align: right; }
    #annexureTable thead th { white-space: nowrap; }

    /* Keep content aligned with real sidebar width */
    :root{ --hp-sidebar-width: 210px; }
    .app-sidebar{ width: var(--hp-sidebar-width) !important; }
    .app-main{ margin-left: 0 !important; transition: margin-left .2s ease; }
    @media (max-width: 991.98px){ .app-main{ margin-left: 0 !important; } }
    .app-content > .container, .app-content > .container-fluid{ max-width:100% !important; width:100% !important; }
  </style>
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- Use your local includes exactly as requested -->
  <jsp:include page="hedar.jsp"/>
  <jsp:include page="sidebar.jsp"/>

  <!-- ============== MAIN ============== -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page header -->
        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6"><h1 class="m-0">Annexure VII</h1></div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/hq/dashboard">Home</a></li>
                <li class="breadcrumb-item active">Annexure VII</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- Card -->
        <div class="card card-primary">
          <div class="card-header"><h3 class="card-title mb-0">Audit Status by Zone and Circle</h3></div>
          <div class="card-body">

            <!-- Totals -->
            <c:set var="totAllotted" value="0" />
            <c:set var="totCompleted" value="0" />
            <c:set var="totDesk" value="0" />
            <c:set var="totPlan" value="0" />
            <c:set var="totBooks" value="0" />
            <c:set var="totDAR" value="0" />
            <c:set var="totFAR" value="0" />

            <div class="table-responsive">
              <table id="annexureTable" class="table table-bordered table-striped w-100">
                <thead>
                  <tr>
                    <th>Zone</th>
                    <th>Circle</th>
                    <th>Allotted Cases</th>
                    <th>Audit Cases Completed</th>
                    <th>Pending at Desk Review Stage</th>
                    <th>Pending at Audit Plan Approval Stage</th>
                    <th>Pending at Books Examination Stage</th>
                    <th>Pending at DAR Completion Stage</th>
                    <th>Pending at FAR Preparation Stage</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach var="row" items="${reportData}">
                    <tr>
                      <td><c:out value="${row.zoneName}"/></td>
                      <td><c:out value="${row.circleName}"/></td>
                      <td><fmt:formatNumber value="${row.allottedCases}" pattern="#,##,##0"/></td>
                      <td><fmt:formatNumber value="${row.auditCasesCompleted}" pattern="#,##,##0"/></td>
                      <td><fmt:formatNumber value="${row.pendingDeskReview}" pattern="#,##,##0"/></td>
                      <td><fmt:formatNumber value="${row.pendingApprovalAuditPlan}" pattern="#,##,##0"/></td>
                      <td><fmt:formatNumber value="${row.pendingExaminationBooks}" pattern="#,##,##0"/></td>
                      <td><fmt:formatNumber value="${row.pendingDAR}" pattern="#,##,##0"/></td>
                      <td><fmt:formatNumber value="${row.pendingFAR}" pattern="#,##,##0"/></td>
                    </tr>

                    <!-- accumulate totals -->
                    <c:set var="totAllotted"  value="${totAllotted  + (row.allottedCases             != null ? row.allottedCases             : 0)}"/>
                    <c:set var="totCompleted" value="${totCompleted + (row.auditCasesCompleted      != null ? row.auditCasesCompleted      : 0)}"/>
                    <c:set var="totDesk"      value="${totDesk      + (row.pendingDeskReview        != null ? row.pendingDeskReview        : 0)}"/>
                    <c:set var="totPlan"      value="${totPlan      + (row.pendingApprovalAuditPlan != null ? row.pendingApprovalAuditPlan : 0)}"/>
                    <c:set var="totBooks"     value="${totBooks     + (row.pendingExaminationBooks  != null ? row.pendingExaminationBooks  : 0)}"/>
                    <c:set var="totDAR"       value="${totDAR       + (row.pendingDAR               != null ? row.pendingDAR               : 0)}"/>
                    <c:set var="totFAR"       value="${totFAR       + (row.pendingFAR               != null ? row.pendingFAR               : 0)}"/>
                  </c:forEach>
                </tbody>
                <tfoot>
                  <tr>
                    <th colspan="2" class="text-end">Grand Total</th>
                    <th><fmt:formatNumber value="${totAllotted}"  pattern="#,##,##0"/></th>
                    <th><fmt:formatNumber value="${totCompleted}" pattern="#,##,##0"/></th>
                    <th><fmt:formatNumber value="${totDesk}"      pattern="#,##,##0"/></th>
                    <th><fmt:formatNumber value="${totPlan}"      pattern="#,##,##0"/></th>
                    <th><fmt:formatNumber value="${totBooks}"     pattern="#,##,##0"/></th>
                    <th><fmt:formatNumber value="${totDAR}"       pattern="#,##,##0"/></th>
                    <th><fmt:formatNumber value="${totFAR}"       pattern="#,##,##0"/></th>
                  </tr>
                </tfoot>
              </table>
            </div>

          </div>
        </div>

      </div><!-- /.container-fluid -->
    </div><!-- /.app-content -->
  </main>

  <!-- Footer (if you keep it separate, include here; otherwise remove this line) -->
  <!-- <jsp:include page="footer.jsp" /> -->

</div><!-- /.app-wrapper -->

<!-- overlay for mobile sidebar (tap outside to close) -->
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

<script>
  // Keep content margin synced with real sidebar width so toggle works
  function setSidebarVar(){
    const sb = document.querySelector('.app-sidebar');
    const collapsed = document.body.classList.contains('sidebar-collapse');
    const px = collapsed ? 0 : (sb ? sb.offsetWidth : 210);
    document.documentElement.style.setProperty('--hp-sidebar-width', px + 'px');
  }
  setSidebarVar();
  new MutationObserver(setSidebarVar).observe(document.body, {attributes:true, attributeFilter:['class']});
  window.addEventListener('resize', setSidebarVar);

  $(function () {
    const title = 'Annexure_VII_Report';
    $("#annexureTable").DataTable({
      responsive: true,
      lengthChange: false,
      autoWidth: true,
      ordering: false,
      scrollX: true,
      dom: 'Bfrtip',
      buttons: [
        { extend: 'excelHtml5', title },
        { extend: 'pdfHtml5',   title, orientation: 'landscape', pageSize: 'A4' },
        { extend: 'print',      title }
      ]
    }).buttons().container().appendTo('#annexureTable_wrapper .col-md-6:eq(0)');
  });
</script>
</body>
</html>
