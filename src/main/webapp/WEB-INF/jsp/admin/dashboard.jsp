<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST | Dashboard</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png"/>

  <!-- AdminLTE 4 / Bootstrap 5 -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css"/>
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css"/>

  <!-- Optional (load only if you use them on this page) -->
  <link rel="stylesheet" href="/static/plugins/overlayScrollbars/css/OverlayScrollbars.min.css"/>
  <link rel="stylesheet" href="/static/plugins/daterangepicker/daterangepicker.css"/>
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css"/>

  <!-- Fix the left GAP when sidebar is collapsed -->
  <style>
    :root { --app-sidebar-width: 250px; } /* keep in sync with your sidebar width */

    /* Desktop: content pushed by sidebar */
    .sidebar-expand-lg .app-main { margin-left: 0px; transition: margin-left .3s; }
    /* Desktop: collapsed */
    .sidebar-collapse .app-main { margin-left: 0 !important; }
    /* Mobile/offcanvas: never push */
    @media (max-width: 991.98px) {
      .sidebar-open .app-main { margin-left: 0 !important; }
    }
  </style>
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- Header & Sidebar (imported) -->
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <!-- ============== MAIN CONTENT (AdminLTE 4) ============== -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page Header -->
        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6">
              <h1 class="m-0">Dashboard</h1>
            </div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="#">Home</a></li>
                <li class="breadcrumb-item active">Dashboard</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- Small Boxes -->
        <div class="row g-3">
          <div class="col-lg-3 col-6">
            <div class="small-box bg-info text-white">
              <div class="inner">
                <h3>150 cr.</h3>
                <p>Total Demand</p>
              </div>
              <div class="icon"><i class="fas fa-bag-shopping"></i></div>
              <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
            </div>
          </div>

          <div class="col-lg-3 col-6">
            <div class="small-box bg-success text-white">
              <div class="inner">
                <h3>15 cr.</h3>
                <p>Total Recovery</p>
              </div>
              <div class="icon"><i class="fas fa-chart-column"></i></div>
              <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
            </div>
          </div>

          <div class="col-lg-3 col-6">
            <div class="small-box bg-primary text-white">
              <div class="inner">
                <h3>53<sup style="font-size: 20px">%</sup></h3>
                <p>Total Cases</p>
              </div>
              <div class="icon"><i class="fas fa-chart-line"></i></div>
              <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
            </div>
          </div>

          <div class="col-lg-3 col-6">
            <div class="small-box bg-warning text-dark">
              <div class="inner">
                <h3>44</h3>
                <p>Action Taken On</p>
              </div>
              <div class="icon"><i class="fas fa-user-plus"></i></div>
              <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
            </div>
          </div>
        </div>

      </div>
    </div>
  </main>

  <!-- Footer (imported) -->
  <jsp:include page="../layout/footer.jsp"/>

</div><!-- /.app-wrapper -->

<!-- ======= SCRIPTS: jQuery → Bootstrap → AdminLTE (v4 pair) ======= -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<!-- Optional (load only if used) -->
<script src="/static/plugins/moment/moment.min.js"></script>
<script src="/static/plugins/daterangepicker/daterangepicker.js"></script>
<script src="/static/plugins/overlayScrollbars/js/jquery.overlayScrollbars.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  // (Optional) basic hardening
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
</script>
</body>
</html>
