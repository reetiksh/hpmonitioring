<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST | Dashboard</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">

  <!-- CSS Dependencies -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/dist/ionicons-2.0.1/css/ionicons.min.css">
  <link rel="stylesheet" href="/static/plugins/tempusdominus-bootstrap-4/css/tempusdominus-bootstrap-4.min.css">
  <link rel="stylesheet" href="/static/plugins/icheck-bootstrap/icheck-bootstrap.min.css">
  <link rel="stylesheet" href="/static/plugins/jqvmap/jqvmap.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="/static/plugins/overlayScrollbars/css/OverlayScrollbars.min.css">
  <link rel="stylesheet" href="/static/plugins/daterangepicker/daterangepicker.css">
  <link rel="stylesheet" href="/static/plugins/summernote/summernote-bs4.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
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
    <!-- Dashboard Header -->
    <div class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6"><h1 class="m-0">Dashboard</h1></div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="#">Home</a></li>
              <li class="breadcrumb-item active">Dashboard</li>
            </ol>
          </div>
        </div>
      </div>
    </div>

    <!-- Dashboard Boxes -->
    <section class="content">
      <div class="container-fluid">
        <div class="row">
          <!-- Box 1 -->
          <div class="col-lg-3 col-6">
            <div class="small-box bg-info">
              <div class="inner">
                <h3>${allotted_cases}</h3>
                <p>Total Cases Allotted</p>
              </div>
              <div class="icon"><i class="ion ion-bag"></i></div>
              
            </div>
          </div>

          <!-- Box 2 -->
          <div class="col-lg-3 col-6">
            <div class="small-box bg-success">
              <div class="inner">
                <h3>${assigned_cases}</h3>
                <p>Total Assigned Cases</p>
              </div>
              <div class="icon"><i class="ion ion-stats-bars"></i></div>
            </div>
          </div>

          <!-- Box 3 -->
          <div class="col-lg-3 col-6">
            <div class="small-box bg-primary">
              <div class="inner">
                <h3>${audit_cases_completed}</h3>
                <p>Total Cases Completed</p>
              </div>
              <div class="icon"><i class="ion ion-stats-bars"></i></div>
            </div>
          </div>

          <!-- Box 4 -->
          <div class="col-lg-3 col-6">
            <div class="small-box bg-warning">
              <div class="inner">
                <h3>${pending_audit_plan}</h3>
                <p>Cases Pending Audit Plan</p>
              </div>
              <div class="icon"><i class="ion ion-person-add"></i></div>
              
            </div>
          </div>

          <!-- Box 5 -->
          <div class="col-lg-3 col-6">
            <div class="small-box bg-danger">
              <div class="inner">
                <h3>${pending_DAR}</h3>
                <p>Cases With Pending DAR</p>
              </div>
              <div class="icon"><i class="ion ion-pie-graph"></i></div>
            </div>
            <div><h3>${nameFromTableAppRole}</h3></div>
          </div>
        </div>

        <!-- Annexure Table -->
        <div class="card card-primary mt-4">
          <div class="card-header">
            <h3 class="card-title">Audit Status by Zone and Circle (Annexure VII)</h3>
          </div>
          <div class="card-body">
            <table id="annexureTable" class="table table-bordered table-striped" style="width:100%">
              <thead>
                <tr>
                  <th rowspan="2">Zone</th>
                  <th rowspan="2">Circle</th>
                  <th rowspan="2">Allotted Cases</th>
                  <th rowspan="2">Audit Cases Completed</th>
                  <th rowspan="2">Pending at Desk Review Stage</th>
                  <th rowspan="2">Pending at Audit Plan Approval Stage</th>
                  <th rowspan="2">Pending at Books Examination Stage</th>
                  <th rowspan="2">Pending at DAR Completion Stage</th>
                  <th rowspan="2">Pending at FAR Preparation Stage</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="row" items="${reportData}">
                  <tr>
                    <td><c:out value="${row.zoneName}"/></td>
                    <td><c:out value="${row.circleName}"/></td>
                    <td><c:out value="${row.allottedCases}"/></td>
                    <td><c:out value="${row.auditCasesCompleted}"/></td>
                    <td><c:out value="${row.pendingDeskReview}"/></td>
                    <td><c:out value="${row.pendingApprovalAuditPlan}"/></td>
                    <td><c:out value="${row.pendingExaminationBooks}"/></td>
                    <td><c:out value="${row.pendingDAR}"/></td>
                    <td><c:out value="${row.pendingFAR}"/></td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </section>
  </div>

  <jsp:include page="../../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- JS Dependencies -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/jquery-ui/jquery-ui.min.js"></script>
<script>$.widget.bridge('uibutton', $.ui.button)</script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/plugins/chart.js/Chart.min.js"></script>
<script src="/static/plugins/sparklines/sparkline.js"></script>
<script src="/static/plugins/jqvmap/jquery.vmap.min.js"></script>
<script src="/static/plugins/jqvmap/maps/jquery.vmap.usa.js"></script>
<script src="/static/plugins/jquery-knob/jquery.knob.min.js"></script>
<script src="/static/plugins/moment/moment.min.js"></script>
<script src="/static/plugins/daterangepicker/daterangepicker.js"></script>
<script src="/static/plugins/tempusdominus-bootstrap-4/js/tempusdominus-bootstrap-4.min.js"></script>
<script src="/static/plugins/summernote/summernote-bs4.min.js"></script>
<script src="/static/plugins/overlayScrollbars/js/jquery.overlayScrollbars.min.js"></script>
<script src="/static/dist/js/adminlte.js"></script>
<script src="/static/dist/js/pages/dashboard.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<!-- DataTables -->
<script src="/static/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="/static/plugins/datatables-bs4/js/dataTables.bootstrap4.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/dataTables.buttons.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.bootstrap4.min.js"></script>
<script src="/static/plugins/jszip/jszip.min.js"></script>
<script src="/static/plugins/pdfmake/pdfmake.min.js"></script>
<script src="/static/plugins/pdfmake/vfs_fonts.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.html5.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.print.min.js"></script>

<script>
  $(function () {
    $("#annexureTable").DataTable({
      "responsive": true,
      "lengthChange": false,
      "autoWidth": false,
      "ordering": false,
      "scrollX": true,
      "buttons": ["excel", "pdf", "print"]
    }).buttons().container().appendTo('#annexureTable_wrapper .col-md-6:eq(0)');
  });

  document.addEventListener('contextmenu', function(e) { e.preventDefault(); });
  document.addEventListener('keydown', function(e) {
    if (e.ctrlKey && e.key === 'u') { e.preventDefault(); }
    if (e.key === 'F12') { e.preventDefault(); }
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) { e.preventDefault(); }
  });
  $(document).ready(function () {
    function disableBack() { window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = function (evt) { if (evt.persisted) disableBack(); }
  });
</script>
</body>
</html>
