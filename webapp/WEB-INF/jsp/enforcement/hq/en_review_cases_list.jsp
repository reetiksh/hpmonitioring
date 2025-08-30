<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Review Cases</title>

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

  <!-- Layout fixes (kept inline on purpose) -->
  <style>
    :root{
      /* default desktop width; JS will sync this to the real measured width */
      --hp-sidebar-width: 210px;
      --hp-header-height: 56px;
    }

    /* compact spacing */
    .app-header{ height: var(--hp-header-height); }
    .app-content{ padding: 1rem; }
    .content-header{ padding: .5rem 0 .75rem; }

    /* keep sidebar + content perfectly aligned */
    .app-sidebar{ width: var(--hp-sidebar-width) !important; }
    .app-main{ margin-left: 0px !important; transition: margin-left .2s ease; }

    /* when collapsed on desktop */
    body.sidebar-collapse .app-main{ margin-left: 0 !important; }

    /* on < lg screens the sidebar is overlay, so content should be full width */
    @media (max-width: 991.98px){
      .app-main{ margin-left: 0 !important; }
    }

    /* ensure any .container inside .app-content spans full width */
    .app-content > .container,
    .app-content > .container-sm,
    .app-content > .container-md,
    .app-content > .container-lg,
    .app-content > .container-xl,
    .app-content > .container-xxl,
    .app-content > .container-fluid{
      max-width: 100% !important;
      width: 100% !important;
    }
  </style>
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- ========== HEADER (AdminLTE 4) ========== -->
  <header class="app-header navbar navbar-expand bg-dark border-bottom">
    <!-- Sidebar toggle (left) -->
    <div class="navbar-nav">
      <a class="nav-link" data-lte-toggle="sidebar" href="#" role="button" aria-label="Toggle sidebar">
        <i class="fas fa-bars text-white"></i>
      </a>
    </div>

    <!-- Brand (center-left) -->
    <div class="navbar-brand ms-2 text-white fw-bold d-none d-sm-inline">
      <img src="/static/files/hp_logo.png" alt="HP GST" style="height:28px;width:28px;border-radius:50%;margin-right:.5rem;vertical-align:middle;">
      HP GST
    </div>

    <!-- Right navbar -->
    <ul class="navbar-nav ms-auto">
      <!-- (sample) icons on the right for parity with your layout -->
      <li class="nav-item"><a class="nav-link text-white" href="#"><i class="far fa-user"></i></a></li>
      <li class="nav-item dropdown">
        <a class="nav-link text-white" data-bs-toggle="dropdown" href="#"><i class="far fa-bell"></i></a>
        <div class="dropdown-menu dropdown-menu-lg dropdown-menu-end" style="max-height:300px; overflow-y:auto;">
          <span class="dropdown-item dropdown-header">Notifications</span>
          <div class="dropdown-divider"></div>
          <c:choose>
            <c:when test="${not empty loginedUserNotificationList}">
              <c:forEach items="${loginedUserNotificationList}" var="object">
                <button class="dropdown-item text-wrap" onclick="showHighlightedNotification('${object.description}');">
                  ${object.description}
                </button>
                <div class="dropdown-divider"></div>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <span class="dropdown-item dropdown-header">No notifications available</span>
            </c:otherwise>
          </c:choose>
        </div>
      </li>
    </ul>
  </header>

  <!-- ========== SIDEBAR (AdminLTE 4) ========== -->
  <aside class="app-sidebar bg-dark text-white elevation-4" style="background-image:url(/static/dist/img/pattern_h.png); background-color:#15283c;">
    <!-- Brand block inside sidebar -->
    <div class="sidebar-brand d-flex align-items-center p-3 border-bottom">
      <a href="/" class="d-flex align-items-center text-decoration-none text-white">
        <img src="/static/files/hp_logo.png" alt="HP GST Logo"
             class="brand-image img-circle elevation-3 me-2" style="opacity:.85; width:44px; height:44px;">
        <span class="brand-text fw-bold">HP GST</span>
      </a>
    </div>

    <!-- Menu -->
    <div class="app-sidebar-menu p-2">
      <nav>
        <ul class="nav nav-pills flex-column" role="menu">
          <li class="nav-item">
            <a href="/welcome" class="nav-link d-flex align-items-center">
              <i class="nav-icon fas fa-home me-2"></i>
              <span>Home <c:out value="${user}"/></span>
            </a>
          </li>

          <!-- Dynamic menu -->
          <c:forEach items="${MenuList}" var="data">
            <li class="nav-item">
              <a href="/${data.userType}/${data.url}"
                 class="nav-link d-flex align-items-center <c:if test='${data.url == activeMenu}'>active</c:if>">
                <i class="nav-icon fas ${data.icon} me-2"></i>
                <span><c:out value="${data.name}"/></span>
              </a>
            </li>
          </c:forEach>
        </ul>
      </nav>
    </div>
  </aside>

  <!-- ========== MAIN CONTENT ========== -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page header (compact) -->
        <div class="content-header">
          <div class="row align-items-center">
            <div class="col">
              <h1 class="m-0">Review Cases</h1>
            </div>
            <div class="col-auto">
              <ol class="breadcrumb mb-0">
                <li class="breadcrumb-item"><a>Home</a></li>
                <li class="breadcrumb-item active">Review Cases</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- Card -->
        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title mb-0">Review Cases</h3>
          </div>

          <div class="card-body">
            <!-- Success messages -->
            <c:if test="${not empty closeclasemessage}">
              <div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
                <strong>${closeclasemessage}</strong>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
              </div>
            </c:if>
            <c:if test="${not empty message}">
              <div class="alert alert-success alert-dismissible fade show" id="message2" role="alert">
                <strong>${message}</strong>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
              </div>
            </c:if>

            <!-- Filters -->
            <div class="row g-3">
              <div class="col-md-6">
                <label class="form-label">Status <span class="text-danger">*</span></label>
                <select id="category" name="category" class="form-select">
                  <option value="">Please Select</option>
                  <c:forEach items="${caseStatusList}" var="caseStatusSolo">
                    <option value="${caseStatusSolo.id}">${caseStatusSolo.status}</option>
                  </c:forEach>
                </select>
                <c:if test="${formResult.hasFieldErrors('category')}">
                  <span class="text-danger">${formResult.getFieldError('category').defaultMessage}</span>
                </c:if>
              </div>
            </div>

            <!-- Loader -->
            <div id="loader" class="py-4 text-center" style="display:none;">
              <i class="fa fa-spinner fa-spin" style="font-size:52px;color:#0d6efd;"></i>
            </div>

            <!-- Dynamic list container -->
            <div id="dataListDiv"></div>
          </div>
        </div>

      </div>
    </div>
  </main>

  <!-- ========== FOOTER ========== -->
  <footer class="main-footer">
    <strong>Copyright &copy; 2023-2024
      <a href="/">Govt of Himachal Pradesh</a>.
    </strong>
    All rights reserved.
  </footer>
</div>

<!-- overlay for mobile sidebar (tap to close) -->
<div class="sidebar-overlay" data-lte-dismiss="sidebar"></div>

<!-- ========== SCRIPTS (order matters) ========== -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<!-- DataTables -->
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
  // 1) Keep content margin in sync with real sidebar width
  function setSidebarVar(){
    const sb = document.querySelector('.app-sidebar');
    const collapsed = document.body.classList.contains('sidebar-collapse');
    // 0 on collapsed (desktop), real width otherwise
    const px = collapsed ? 0 : (sb ? sb.offsetWidth : 210);
    document.documentElement.style.setProperty('--hp-sidebar-width', px + 'px');
  }
  setSidebarVar();

  // Recompute when the body class changes (collapse/expand)
  new MutationObserver(setSidebarVar).observe(document.body, {attributes:true, attributeFilter:['class']});
  // Recompute on resize (sidebar can change with breakpoints)
  window.addEventListener('resize', setSidebarVar);

  // 2) Flash messages fade out
  $(function () {
    $("#message, #message2").each(function () {
      $(this).fadeTo(2000, 0.5).slideUp(500, () => $(this).remove());
    });
  });

  // 3) Load list on status change
  $(function() {
    $("#category").on('change', function () {
      const selectedValue = $(this).val();
      $.ajax({
        url: '/checkLoginStatus',
        method: 'get',
        async: false,
        success: function(result){
          if (result === 'true') {
            $("#dataListDiv").empty();
            $("#loader").show();
            setTimeout(function() {
              $("#dataListDiv").load('/enforcement_hq/review_cases_list/' + selectedValue + '/', function(_, status) {
                $("#loader").hide();
                if (status !== 'success') console.warn('List load failed');
              });
            }, 200);
          } else {
            window.location.reload();
          }
        }
      });
    });
  });

  // 4) Hardening (unchanged)
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });

  // (Optional) notification modal helper
  function showHighlightedNotification(txt){
    alert(txt); // swap with your modal if needed
  }
</script>
</body>
</html>
