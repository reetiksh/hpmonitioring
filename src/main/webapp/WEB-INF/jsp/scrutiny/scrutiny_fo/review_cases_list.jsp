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

  <!-- Layout fixes: keep content aligned with sidebar and remove fixed center width -->
  <style>
    :root{ --hp-sidebar-width: 210px; --hp-header-height: 56px; }

    /* keep sidebar + content perfectly aligned */
    .app-sidebar{ width: var(--hp-sidebar-width) !important; }
    .app-header{ height: var(--hp-header-height); }
    .app-main{ margin-left: 0 !important; transition: margin-left .2s ease; }
    body.sidebar-collapse .app-main{ margin-left: 0 !important; }

    /* on < lg screens the sidebar is overlay, so content should be full width */
    @media (max-width: 991.98px){ .app-main{ margin-left: 0 !important; } }

    /* Make any .container inside .app-content full width */
    .app-content > .container,
    .app-content > .container-fluid{
      max-width:100% !important; width:100% !important;
    }
  </style>
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- Header & Sidebar -->
  <jsp:include page="../../layout/header.jsp"/>
  <jsp:include page="../../layout/sidebar.jsp"/>

  <!-- (kept, if you still use it elsewhere) -->
  <jsp:include page="../../layout/confirmation_popup.jsp"/>

  <!-- ====================== MAIN ====================== -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page header -->
        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6">
              <h1 class="m-0">Review Cases</h1>
            </div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="#">Home</a></li>
                <li class="breadcrumb-item active">Review Cases</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- Alerts -->
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

        <!-- Card -->
        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title mb-0">Review Cases</h3>
          </div>

          <div class="card-body">
            <!-- Filter -->
            <div class="row g-3">
              <div class="col-md-6">
                <label class="form-label">Status <span class="text-danger">*</span></label>
                <select id="category" name="category" class="form-select">
                  <option value="">Please Select</option>
                  <c:forEach items="${caseStatusList}" var="caseStatusSolo">
                    <option value="${caseStatusSolo.id}">${caseStatusSolo.name}</option>
                  </c:forEach>
                </select>
                <c:if test="${formResult.hasFieldErrors('category')}">
                  <div class="text-danger small mt-1">
                    ${formResult.getFieldError('category').defaultMessage}
                  </div>
                </c:if>
              </div>
            </div>

            <!-- Loader -->
            <div id="loader" class="text-center my-4" style="display:none;">
              <i class="fa fa-spinner fa-spin" style="font-size:52px;color:#0d6efd;"></i>
            </div>

            <!-- AJAX target -->
            <div id="dataListDiv"></div>
          </div>
        </div>

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
  /* ===== Keep content margin synced with real sidebar width so toggle works ===== */
  function setSidebarVar(){
    const sb = document.querySelector('.app-sidebar');
    const collapsed = document.body.classList.contains('sidebar-collapse');
    const px = collapsed ? 0 : (sb ? sb.offsetWidth : 210);
    document.documentElement.style.setProperty('--hp-sidebar-width', px + 'px');
  }
  setSidebarVar();
  new MutationObserver(setSidebarVar).observe(document.body, {attributes:true, attributeFilter:['class']});
  window.addEventListener('resize', setSidebarVar);

  /* ===== Common page niceties ===== */
  $(function(){
    // fade-out alerts
    $("#message, #message2").each(function(){
      const $t = $(this);
      if ($t.length) {
        setTimeout(() => { $t.fadeTo(500, 0).slideUp(500, () => $t.remove()); }, 2500);
      }
    });

    // protect keys/context menu (if you keep this convention)
    document.addEventListener('contextmenu', e => e.preventDefault());
    document.addEventListener('keydown', e => {
      const k = e.key.toLowerCase();
      if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
    });
  });

  /* ===== DataTables initializer for any tables that arrive via AJAX ===== */
  function initTablesIn($container){
    const $tbl = $container.find('table#example1');
    if ($tbl.length && !$tbl.hasClass('dataTable-initialized')){
      $tbl.addClass('dataTable-initialized').DataTable({
        responsive: false,
        lengthChange: false,
        autoWidth: true,
        scrollX: true,
        buttons: ["excel","pdf","print"]
      }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
    }
  }

  /* ===== Load list based on dropdown ===== */
  $(function () {
    $("#category").on('change', function() {
      const selectedValue = $(this).val();
      if (!selectedValue) {
        $("#dataListDiv").empty();
        return;
      }

      $.ajax({
        url: '/checkLoginStatus',
        method: 'get',
        async: false,
        success: function(result){
          const isLoggedIn = (result === 'true' || result === true);
          if (!isLoggedIn) { window.location.reload(); return; }

          $("#dataListDiv").empty();
          $("#loader").show();

          setTimeout(function(){
            $("#dataListDiv").load('/scrutiny_fo/review_cases_list/' + encodeURIComponent(selectedValue) + '/', function(response, status){
              $("#loader").hide();
              if (status === 'success') {
                initTablesIn($("#dataListDiv"));
              } else {
                console.log("Failed to load list view");
              }
            });
          }, 600);
        }
      });
    });
  });
</script>

</body>
</html>
