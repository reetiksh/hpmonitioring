<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>HP GST | Dashboard</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png" />

  <!-- AdminLTE 4 / Bootstrap 5 core -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css" />
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css" />

  <!-- Optional (only keep what you use) -->
  <link rel="stylesheet" href="/static/dist/ionicons-2.0.1/css/ionicons.min.css" />
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css" />

  <style>
    :root{
      /* Will be synced to real sidebar width by JS so content lines up when toggling */
      --hp-sidebar-width: 210px;
    }
    .app-sidebar{ width: var(--hp-sidebar-width) !important; }
    .app-main{ margin-left: 0 !important; transition: margin-left .2s ease; }
    body.sidebar-collapse .app-main{ margin-left: 0 !important; }
    @media (max-width: 991.98px){ .app-main{ margin-left: 0 !important; } }

    /* Make any .container inside .app-content full width */
    .app-content > .container,
    .app-content > .container-fluid{ max-width:100% !important; width:100% !important; }
  </style>
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- Preloader -->
  <div class="preloader flex-column justify-content-center align-items-center">
    <img class="animation__shake" src="/static/dist/img/AdminLTELogo.png" alt="AdminLTELogo" height="60" width="60" />
  </div>

  <!-- Header & Sidebar -->
  <jsp:include page="../../layout/header.jsp"/>
  <jsp:include page="../../layout/sidebar.jsp"/>

  <!-- ====================== MAIN ====================== -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page header -->
        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6"><h1 class="m-0">Dashboard</h1></div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/hq/dashboard">Home</a></li>
                <li class="breadcrumb-item active">Dashboard</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- Small boxes -->
        <section class="content">
          <div class="row g-3">
            <div class="col-lg-3 col-6">
              <div class="small-box bg-info">
                <div class="inner">
                  <h3>150 cr.</h3>
                  <p>Total Demand</p>
                </div>
                <div class="icon"><i class="ion ion-bag"></i></div>
                <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
              </div>
            </div>

            <div class="col-lg-3 col-6">
              <div class="small-box bg-success">
                <div class="inner">
                  <h3>15 cr.</h3>
                  <p>Total Recovery</p>
                </div>
                <div class="icon"><i class="ion ion-stats-bars"></i></div>
                <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
              </div>
            </div>

            <div class="col-lg-3 col-6">
              <div class="small-box bg-primary">
                <div class="inner">
                  <h3>53<sup style="font-size:20px">%</sup></h3>
                  <p>Total Cases</p>
                </div>
                <div class="icon"><i class="ion ion-stats-bars"></i></div>
                <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
              </div>
            </div>

            <div class="col-lg-3 col-6">
              <div class="small-box bg-warning">
                <div class="inner">
                  <h3>44</h3>
                  <p>Action Taken On</p>
                </div>
                <div class="icon"><i class="ion ion-person-add"></i></div>
                <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
              </div>
            </div>

            <div class="col-lg-3 col-6">
              <div class="small-box bg-danger">
                <div class="inner">
                  <h3>65</h3>
                  <p>Pending Cases</p>
                </div>
                <div class="icon"><i class="ion ion-pie-graph"></i></div>
                <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
              </div>

              <!-- Example dynamic text -->
              <div><h3>${nameFromTableAppRole}</h3></div>
            </div>
          </div>
        </section>

      </div><!-- /.container-fluid -->
    </div><!-- /.app-content -->
  </main>

  <!-- Footer -->
  <jsp:include page="../../layout/footer.jsp"/>

</div><!-- /.app-wrapper -->

<!-- overlay for mobile sidebar (tap outside to close) -->
<div class="sidebar-overlay" data-lte-dismiss="sidebar"></div>

<!-- ====================== SCRIPTS ====================== -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  /* Keep content margin synced with real sidebar width so toggle works */
  function setSidebarVar(){
    const sb = document.querySelector('.app-sidebar');
    const collapsed = document.body.classList.contains('sidebar-collapse');
    const px = collapsed ? 0 : (sb ? sb.offsetWidth : 210);
    document.documentElement.style.setProperty('--hp-sidebar-width', px + 'px');
  }
  setSidebarVar();
  new MutationObserver(setSidebarVar).observe(document.body, {attributes:true, attributeFilter:['class']});
  window.addEventListener('resize', setSidebarVar);

  /* Optional hardening (kept consistent with your other pages) */
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });

  function myFunction(){
    alert("Inside MyFunction!");
    document.forms["form1"]?.submit();
  }
</script>
</body>
</html>
