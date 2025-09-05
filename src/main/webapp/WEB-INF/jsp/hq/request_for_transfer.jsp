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
  <title>HP GST |Review Case List</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png"/>

  <!-- Bootstrap 5 + AdminLTE 4 + Font Awesome -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/css/adminlte.min.css" rel="stylesheet"/>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet"/>

  <!-- DataTables (Bootstrap 5) -->
  <link href="https://cdn.datatables.net/1.13.8/css/dataTables.bootstrap5.min.css" rel="stylesheet"/>
  <link href="https://cdn.datatables.net/responsive/2.5.0/css/responsive.bootstrap5.min.css" rel="stylesheet"/>
  <link href="https://cdn.datatables.net/buttons/2.4.2/css/buttons.bootstrap5.min.css" rel="stylesheet"/>

  <!-- Select2 (kept) -->
  <link rel="stylesheet" href="/static/plugins/select2/select2.min.css"/>

  <!-- jQuery Confirm (kept) -->
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css"/>

  <style>
    :root { --hp-sidebar-width: 250px; }
    .app-sidebar { width: var(--hp-sidebar-width) !important; }
    @media (min-width: 992px) {
      body.sidebar-expand-lg:not(.sidebar-collapse) .app-wrapper .app-main { margin-left: var(--hp-sidebar-width); }
      body.sidebar-collapse .app-wrapper .app-main { margin-left: 0 !important; }
    }
    @media (max-width: 991.98px) {
      .app-wrapper .app-main { margin-left: 0 !important; }
      body.sidebar-open .app-wrapper .app-main { margin-left: 0 !important; }
    }
  </style>
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../hq/transfer_popup.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Transfer Request</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="/hq/dashboard">Home</a></li>
              <li class="breadcrumb-item active">Request For Transfer</li>
            </ol>
          </div>
        </div>

        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title">Transfer Request</h3>
          </div>

          <div class="card-body">
            <c:if test="${not empty successMessage}">
              <div class="col-12 alert alert-success alert-dismissible fade show" id="message" role="alert" style="max-height: 500px; overflow-y: auto;">
                <strong>${successMessage}</strong><br>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
              </div>
            </c:if>

            <c:if test="${!empty hqTransferList}">
              <table id="example1" class="table table-bordered table-striped w-100">
                <thead>
                  <tr>
                    <th style="text-align: center; vertical-align: middle;">GSTIN</th>
                    <th style="text-align: center; vertical-align: middle;">Taxpayer Name</th>
                    <th style="text-align: center; vertical-align: middle;">Category</th>
                    <th style="text-align: center; vertical-align: middle;">Reporting Date<br>(DD-MM-YYYY)</th>
                    <th style="text-align: center; vertical-align: middle;">Case Period</th>
                    <th style="text-align: center; vertical-align: middle;">Indicative Value(₹)</th>
                    <th style="text-align: center; vertical-align: middle;">Case Assigned To</th>
                    <!-- <th style="text-align: center; vertical-align: middle;">Remark</th> -->
                    <th style="text-align: center; vertical-align: middle;">Suggested Jurisdiction</th>
                    <th style="text-align: center; vertical-align: middle;">Requested on</th>
                    <th style="text-align: center; vertical-align: middle;">Action</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach items="${hqTransferList}" var="object">
                    <tr>
                      <td><c:out value="${object.GSTIN}" /></td>
                      <td><c:out value="${object.taxpayerName}" /></td>
                      <td><c:out value="${object.category}" /></td>
                      <td><fmt:formatDate value="${object.caseReportingDate}" pattern="dd-MM-yyyy" /></td>
                      <td><c:out value="${object.period}" /></td>
                      <td><fmt:formatNumber value="${object.indicativeTaxValue}" pattern="#,##,##0"/></td>
                      <td><c:out value="${object.assignedFromLocationName}" /></td>
                      <!-- <td><c:out value="${object.remark}" /></td> -->
                      <td><c:out value="${object.suggestedJurisdictionName}" /></td>
                      <td><fmt:formatDate value="${object.updatingDate}" pattern="dd-MM-yyyy" /></td>
                      <td style="text-align: center;">
                        <button type="button" style="margin: 3px;" class="btn btn-info"
                          onclick="transferBtn('${object.remark}' , '${object.suggestedJurisdictionId}' , '${object.assignedFromLocationId}', '${object.GSTIN}', '${object.caseReportingDate}', '${object.period}');">
                          <i class="fa fa-pen-square" style="font-size:25px"></i>
                        </button>
                        <c:if test="${not empty object.transferFilePath}">
                          <a href="/hq/downloadFOFile?fileName=${object.transferFilePath}">
                            <button type="button" class="btn btn-primary">
                              <i class="fas fa-download" style="font-size:20px"></i>
                            </button>
                          </a>
                        </c:if>
                      </td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </c:if>

            <c:if test="${empty hqTransferList}">
              <div class="col-12 text-center">
                <i class="fa fa-info-circle" style="font-size:100px;color:rgb(97, 97, 97)" aria-hidden="true"></i><br>
                <span style="font-size:35px;color:rgb(97, 97, 97)">No Transfer Request Available</span>
              </div>
            </c:if>
          </div>
        </div>

      </div>
    </div>
  </main>

  <jsp:include page="../layout/footer.jsp"/>

  <!-- Control Sidebar (kept for compatibility) -->
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- Core JS: jQuery -> Bootstrap 5 bundle -> AdminLTE 4 -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/js/adminlte.min.js"></script>

<!-- DataTables (Bootstrap 5) -->
<script src="https://cdn.datatables.net/1.13.8/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.13.8/js/dataTables.bootstrap5.min.js"></script>
<script src="https://cdn.datatables.net/responsive/2.5.0/js/dataTables.responsive.min.js"></script>
<script src="https://cdn.datatables.net/responsive/2.5.0/js/responsive.bootstrap5.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.2/js/dataTables.buttons.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.2/js/buttons.bootstrap5.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.10.1/jszip.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/pdfmake.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.2.7/vfs_fonts.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.2/js/buttons.html5.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.2/js/buttons.print.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.2/js/buttons.colVis.min.js"></script>

<!-- Select2 (kept) -->
<script src="/static/plugins/select2/select2.min.js"></script>

<!-- jQuery Confirm (kept) -->
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<!-- jQuery ↔ Bootstrap 5 Modal bridge so $('#id').modal('show') keeps working -->
<script>
  (function ($) {
    $.fn.modal = function (action) {
      return this.each(function () {
        const inst = bootstrap.Modal.getOrCreateInstance(this);
        if (action === 'show') inst.show();
        else if (action === 'hide') inst.hide();
      });
    };
  })(jQuery);
</script>

<!-- Sidebar toggle helper to mimic ALTE3 pushmenu behavior -->
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

<!-- Your original scripts (logic unchanged) -->
<script>
  document.addEventListener('contextmenu', function(e) { e.preventDefault(); });
  document.addEventListener('keydown', function(e) {
    if (e.ctrlKey && e.key === 'u') e.preventDefault();
  });
  document.addEventListener('keydown', function(e) {
    if (e.key === 'F12') e.preventDefault();
  });
  $(document).ready(function () {
    function disableBack() {window.history.forward()}
    window.onload = disableBack();
    window.onpageshow = function (evt) {if (evt.persisted) disableBack()}
  });
  document.onkeydown = function (e) {
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) e.preventDefault();
  };

  $('select').select2();
  $(document).ready(function() {
    $("#message").fadeTo(5000, 500).slideUp(500, function() { $("#message").slideUp(500); });
  });
</script>

<script>
  $(function(){
    $('select').select2({ dropdownParent: $('#transferModal') });
  });
</script>

<script>
  $(function () {
    $("#example1").DataTable({
      responsive: true,
      lengthChange: false,
      autoWidth: false,
      buttons: ["excel","pdf","print"]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');

    $('#example2').DataTable({
      paging: true,
      lengthChange: false,
      searching: false,
      ordering: true,
      info: true,
      autoWidth: false,
      responsive: true
    });
  });
</script>

<script>
  function transferBtn(remark , suggestedJurisdictionId , assignedFromLocationId, gstIn, caseReportingDate, period){
    $("#remark").val(remark);
    $("#assignedFromLocationId").val(assignedFromLocationId);
    $("#gstIn").val(gstIn);
    $("#caseReportingDate").val(caseReportingDate);
    $("#period").val(period);

    var selectValues = JSON.parse('${locatoinMap}');
    const selectElement = document.getElementById('locationId');

    $("#locationId").val(suggestedJurisdictionId);

    // reset options
    $('#locationId').children().remove().end().append('<option disabled selected value="">Select Jurisdiction</option>');

    $.each(selectValues, function(key, value) {
      if(key != assignedFromLocationId){
        if (key == suggestedJurisdictionId) {
          $('#locationId').append('<option selected value="' + key + '">' + selectValues[key] + '</option>');
        } else {
          $('#locationId').append('<option value="' + key + '">' + selectValues[key] + '</option>');
        }
      }
    });

    $("#transferModal").modal('show');
  }
</script>
</body>
</html>
