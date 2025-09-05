<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Update Scrutiny Status</title>

  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png"/>

  <!-- AdminLTE / Bootstrap core -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">

  <!-- Plugins used across the app -->
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">

  <style>
    :root{ --hp-sidebar-width: 210px; }
    .app-sidebar{ width: var(--hp-sidebar-width) !important; }
    @media (min-width: 992px){
      body.sidebar-expand-lg:not(.sidebar-collapse) .app-wrapper .app-main{ margin-left: var(--hp-sidebar-width); }
    }
    @media (max-width: 991.98px){ .app-wrapper .app-main{ margin-left: 0 !important; } }
  </style>
</head>

<body class="hold-transition sidebar-mini layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="wrapper app-wrapper">

  <!-- Header / Sidebar -->
  <jsp:include page="../../layout/header.jsp"/>
  <jsp:include page="../../fo/transfer_popup.jsp"/>
  <jsp:include page="../../layout/sidebar.jsp"/>

  <!-- ====================== MAIN ====================== -->
  <main class="app-main content-wrapper">
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6"><h1>Update Scrutiny Status</h1></div>
          <div class="col-sm-6 d-flex justify-content-end">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="/l2/dashboard">Home</a></li>
              <li class="breadcrumb-item active">Update Scrutiny Status</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">

        <c:if test="${not empty errorMessage}">
          <div class="alert alert-danger alert-dismissible fade show" id="message" role="alert">
            <strong>${errorMessage}</strong>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span>&times;</span></button>
          </div>
        </c:if>
        <c:if test="${not empty successMessage}">
          <div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
            <strong>${successMessage}</strong>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span>&times;</span></button>
          </div>
        </c:if>

        <div class="card card-primary h-100">
          <div class="card-header">
            <h3 class="card-title">Update Scrutiny Status</h3>
          </div>
          <div class="card-body">
            <div class="col-lg-12">
              <!-- Header panel about the case (kept as-is) -->
              <jsp:include page="../scrutiny_fo/scrutinyCaseHeaderPannel.jsp"/>

              <!-- Hidden context for JS (avoid brittle client-side date parsing) -->
              <input type="hidden" id="ctxGstin" value="${mstScrutinyCases.id.GSTIN}">
              <input type="hidden" id="ctxPeriod" value="${mstScrutinyCases.id.period}">
              <input type="hidden" id="ctxReportingDateIso"
                     value="<fmt:formatDate value='${mstScrutinyCases.id.caseReportingDate}' pattern=\"yyyy-MM-dd'T'HH:mm:ss'Z'\"/>">

              <div class="row">
                <div class="col-lg-2">
                  <jsp:include page="../scrutiny_fo/updatedScrutinySidePannel.jsp"/>
                </div>
                <div class="col-lg-10" id="updatedScrutinyPannelView"><!-- AJAX loads here --></div>
              </div>
            </div>
          </div>
        </div>

      </div>
    </section>
  </main>

  <jsp:include page="../../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- ====================== SCRIPTS ====================== -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/plugins/bs-custom-file-input/bs-custom-file-input.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<!-- Plugins -->
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>
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

<script>
  // Basic UX niceties
  $(function(){
    $("#message").fadeTo(5000, 500).slideUp(500, function(){ $("#message").slideUp(500); });
    try { bsCustomFileInput.init(); } catch(e){}
    $('.selectpicker').selectpicker();
  });

  // Hardening (kept consistent with your other pages)
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = (e.key || '').toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
  $(document).ready(function () {
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = function (evt){ if (evt.persisted) disableBack(); }
  });

  // ===== Panel loading helpers (uses server-rendered ISO date to avoid brittle parsing) =====
  function buildPanelUrl(pageId){
    const gstin = encodeURIComponent($('#ctxGstin').val() || '');
    const period = encodeURIComponent($('#ctxPeriod').val() || '');
    const reportingDateIso = encodeURIComponent($('#ctxReportingDateIso').val() || '');
    return '/scrutiny_fo/update_scrutiny_side_panel_view'
           + '?pageId=' + encodeURIComponent(pageId)
           + '&gstinStr=' + gstin
           + '&periodStr=' + period
           + '&reportingDateStr=' + reportingDateIso;
  }

  function loadPanel(pageId){
    const link = buildPanelUrl(pageId);
    $.ajax({
      url: '/checkLoginStatus',
      method: 'get',
      async: false,
      success: function(result){
        if (result === 'true'){
          $("#updatedScrutinyPannelView").load(link, function(response, status){
            if (status === 'success'){
              // Optional: seed common fields if present in the loaded markup
              $("#caseId").val('${mstScrutinyCases.caseId}');
              $("#caseStageArn").val('${mstScrutinyCases.caseStageArn}');
              $("#demand").val('${mstScrutinyCases.amountRecovered}');
              $("#recoveryByDRC3").val('${mstScrutinyCases.recoveryByDRC03}');
              const fileName = "${mstScrutinyCases.filePath}";
              if (fileName){
                $('#fileDownloadLink').attr('href', '/scrutiny_fo/downloadUploadedPdfFile?fileName=' + encodeURIComponent(fileName));
              }
            } else {
              console.log('Panel load failed');
            }
          });
        } else if (result === 'false'){
          window.location.reload();
        }
      }
    });
  }

  // Initial load (pageId 6 kept from your original code)
  $(document).ready(function(){
    loadPanel(6);
  });

  // Expose for side-panel buttons to call with different pageIds
  window.displayDynamicSidePannelView = function(pageId){
    loadPanel(pageId);
  };

  // Example: Submitting ASMT-10 from inside the loaded panel (keeping your original function name)
  window.submitAsmtTenIssuance = function(){
    const gstin = $('#ctxGstin').val();
    const period = $('#ctxPeriod').val();
    const caseReportingDate = $('#ctxReportingDateIso').val();
    $('#asmtGstin').val(gstin);
    $('#asmtPeriod').val(period);
    $('#asmtCaseReportingDate').val(caseReportingDate);

    $.confirm({
      title: 'Confirm!',
      content: 'Are you sure to submit ASMT-10 issuance ?',
      buttons: {
        submit: function(){ document.getElementById("asmtTenForm").submit(); },
        close: function(){ $.alert('Canceled!'); }
      }
    });
  };
</script>
</body>
</html>
