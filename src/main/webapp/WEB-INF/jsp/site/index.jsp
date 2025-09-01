<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST | Dashboard</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png"/>

  <!-- AdminLTE 4 / Bootstrap 5 -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">

  <style>
    :root{
      --hp-header-height: 56px;
    }

    /* Make wrapper a column so footer sits at the bottom naturally */
    .app-wrapper{
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      background-color: var(--bs-body-bg);
    }

    .app-header{ height: var(--hp-header-height); }

    .app-main{
      flex: 1 1 auto;        /* fills available space */
      margin: 0;             /* no sidebar, no left margin */
    }

    .app-content{
      padding: 1rem;
    }

    .content-header{
      padding: .5rem 0 .75rem;
    }

    /* Full width containers inside app-content */
    .app-content > .container,
    .app-content > .container-fluid,
    .app-content > [class^="container-"]{
      max-width: 100% !important;
      width: 100% !important;
    }

    /* Hover effect you had */
    .hoverEffect1:hover{
      background-color: #07243b;
      color: #fff;
      font-weight: bold;
      border-radius: 3px;
      box-shadow: 2px 3px 3px 3px #494949;
    }

    /* Footer—no fixed positioning; spans full width */
    .main-footer{
      margin: 0;
      padding: .75rem 1rem;
    }
  </style>
</head>
<body class="layout-fixed bg-body-tertiary">
<div class="app-wrapper"><!-- flex column -->

  <!-- ================= HEADER (no sidebar) ================= -->
  <header class="app-header navbar navbar-expand bg-dark border-bottom">
    <div class="navbar-brand ms-2 text-white fw-bold">
      <img src="/static/files/hp_logo.png" alt="HP GST"
           style="height:28px;width:28px;border-radius:50%;margin-right:.5rem;vertical-align:middle;">
      HP GST
    </div>

    <ul class="navbar-nav ms-auto">
      <!-- User dropdown -->
      <li class="nav-item dropdown">
        <a class="nav-link" data-bs-toggle="dropdown" href="#"><i class="far fa-user text-white"></i></a>
        <div class="dropdown-menu dropdown-menu-end">
          <span class="dropdown-item dropdown-header">${UserLoginName}</span>
          <div class="dropdown-divider"></div>
          <a href="${commonUserDetails}" class="dropdown-item"><i class="fas fa-id-card me-2"></i> User Details</a>
          <div class="dropdown-divider"></div>
          <a href="${changeUserPassword}" class="dropdown-item"><i class="fas fa-key me-2"></i> Change Password</a>
          <div class="dropdown-divider"></div>
          <a href="/logout" class="dropdown-item dropdown-footer text-danger"><i class="fas fa-sign-out-alt me-2"></i> Logout</a>
        </div>
      </li>
      <!-- Fullscreen -->
      <li class="nav-item">
        <a class="nav-link text-white" data-lte-toggle="fullscreen" href="#" role="button" aria-label="Fullscreen">
          <i class="fas fa-expand-arrows-alt"></i>
        </a>
      </li>
    </ul>
  </header>

  <!-- ================= MAIN ================= -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page header -->
        <div class="content-header">
          <div class="text-center">
            <img src="static/image/logo_HP.png" alt="HPGST_LOGO" style="width:200px;">
            <h4 class="mt-1 mb-3">Government of Himachal Pradesh</h4>
          </div>
        </div>

        <!-- Functions grid -->
        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title mb-0">Functions <i class="fas fa-tag nav-icon"></i></h3>
          </div>
          <div class="card-body">
            <div class="row g-3">
              <c:forEach items="${LoggedInUserRolesV2}" var="object">
                <div class="col-12 col-md-6 col-xl-4">
                  <div class="card text-white h-100"
                       style="background-image:url(/static/dist/img/pattern_h.png); background-color:#15283c;">
                    <div class="card-header">${object.key}</div>
                    <div class="card-body" style="max-height: 220px; overflow-y: auto; background-color:#fafafa65;">
                      <c:forEach items="${object.value}" var="object1">
                        <a href="/${object1.value.url}" class="text-decoration-none">
                          <div class="card border-primary mb-2"
                               style="background-image:url(/static/dist/img/pattern_h.png); background-color:#15283c;">
                            <div class="card-header text-center text-white hoverEffect1">
                              ${object1.key}
                            </div>
                          </div>
                        </a>
                      </c:forEach>
                    </div>
                  </div>
                </div>
              </c:forEach>
            </div>
          </div>
        </div>

      </div>
    </div>
  </main>

  <!-- ================= FOOTER (stays at bottom) ================= -->
  <footer class="main-footer text-center">
    <strong>Copyright &copy; 2023-2024 <a href="/">Govt of Himachal Pradesh</a>.</strong>
    All rights reserved.
  </footer>
</div><!-- /.app-wrapper -->

<!-- ================= SCRIPTS ================= -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  // Hardening (optional)
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
</script>
</body>
</html>
