<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST | Transfer Request</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">

  <!-- AdminLTE 3 (Bootstrap 4) + Plugins -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/select2/css/select2.min.css">
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
  <jsp:include page="../../layout/header.jsp"/>
  <!-- The modal UI used by transferBtn lives in this include -->
  <jsp:include page="../scrutiny_hq/transfer_popup.jsp"/>
  <jsp:include page="../../layout/sidebar.jsp"/>

  <div class="content-wrapper">
    <section class="content-header">
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
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-12">

            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title">Transfer Request</h3>
              </div>

              <div class="card-body">
                <c:if test="${not empty successMessage}">
                  <div class="col-12 alert alert-success alert-dismissible fade show" id="message" role="alert" style="max-height: 500px; overflow-y: auto;">
                    <strong>${successMessage}</strong>
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                      <span aria-hidden="true">&times;</span>
                    </button>
                  </div>
                </c:if>

                <c:if test="${!empty mstScrutinyCasesList}">
                  <div class="table-responsive">
                    <table id="example1" class="table table-bordered table-striped w-100">
                      <thead>
                        <tr>
                          <th class="text-center align-middle">GSTIN</th>
                          <th class="text-center align-middle">Taxpayer Name</th>
                          <th class="text-center align-middle">Category</th>
                          <th class="text-center align-middle">Reporting Date<br>(DD-MM-YYYY)</th>
                          <th class="text-center align-middle">Case Period</th>
                          <th class="text-center align-middle">Indicative Value(₹)</th>
                          <th class="text-center align-middle">Case Assigned To</th>
                          <th class="text-center align-middle">Suggested Jurisdiction</th>
                          <th class="text-center align-middle">Requested on</th>
                          <th class="text-center align-middle">Action</th>
                        </tr>
                      </thead>
                      <tbody>
                        <c:forEach items="${mstScrutinyCasesList}" var="object">
                          <tr>
                            <td class="text-center align-middle"><c:out value="${object.id.GSTIN}"/></td>
                            <td class="text-center align-middle"><c:out value="${object.taxpayerName}"/></td>
                            <td class="text-center align-middle"><c:out value="${object.category.name}"/></td>
                            <td class="text-center align-middle"><fmt:formatDate value="${object.id.caseReportingDate}" pattern="dd-MM-yyyy"/></td>
                            <td class="text-center align-middle"><c:out value="${object.id.period}"/></td>
                            <td class="text-center align-middle"><fmt:formatNumber value="${object.indicativeTaxValue}" pattern="#,##,##0"/></td>
                            <td class="text-center align-middle"><c:out value="${object.locationDetails.locationName}"/></td>
                            <td class="text-center align-middle"><c:out value="${object.suggestedJurisdictionName}"/></td>
                            <td class="text-center align-middle"><fmt:formatDate value="${object.caseUpdateDate}" pattern="dd-MM-yyyy"/></td>
                            <td class="text-center align-middle">
                              <button type="button" class="btn btn-info" style="margin:3px;"
                                      title="Edit transfer"
                                      onclick="transferBtn('${fn:escapeXml(object.remark)}',
                                                           '${fn:escapeXml(object.suggestedJurisdictionId)}',
                                                           '${fn:escapeXml(object.locationDetails.locationId)}',
                                                           '${fn:escapeXml(object.id.GSTIN)}',
                                                           '${fn:escapeXml(object.id.caseReportingDate)}',
                                                           '${fn:escapeXml(object.id.period)}');">
                                <i class="fa fa-pen-square" style="font-size:20px"></i>
                              </button>
                              <c:if test="${not empty object.filePath}">
                                <a class="btn btn-primary" title="Download attachment"
                                   href="/scrutiny_hq/downloadUploadedPdfFile?fileName=${object.filePath}">
                                  <i class="fas fa-download" style="font-size:16px"></i>
                                </a>
                              </c:if>
                            </td>
                          </tr>
                        </c:forEach>
                      </tbody>
                    </table>
                  </div>
                </c:if>

                <c:if test="${empty mstScrutinyCasesList}">
                  <div class="col-12 text-center">
                    <i class="fa fa-info-circle" style="font-size:100px;color:#616161" aria-hidden="true"></i><br>
                    <span style="font-size:35px;color:#616161">No Transfer Request Available</span>
                  </div>
                </c:if>
              </div>
            </div>

          </div>
        </div>
      </div>
    </section>
  </div>

  <jsp:include page="../../layout/footer.jsp"/>

  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- Safely embed the jurisdiction map as JSON -->
<script id="locationJson" type="application/json"><c:out value="${locatoinMap}" /></script>

<!-- Scripts -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- DataTables -->
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

<!-- Select2 + misc -->
<script src="/static/plugins/select2/js/select2.full.min.js"></script>
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<script>
  // DataTable
  $(function () {
    const dt = $("#example1").DataTable({
      responsive: true,
      lengthChange: false,
      autoWidth: false,
      buttons: ["excel", "pdf", "print"]
    });
    dt.buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');

    // Flash fade
    $("#message").fadeTo(5000, 0.5).slideUp(500, function(){ $(this).remove(); });
  });

  // Initialize Select2 globally (the popup also rebinds with dropdownParent)
  $('select').select2();

  // Helper: open transfer modal and hydrate fields/options
  function transferBtn(remark, suggestedJurisdictionId, assignedFromLocationId, gstIn, caseReportingDate, period) {
    // Basic fields
    $("#remark").val(remark || "");
    $("#assignedFromLocationId").val(assignedFromLocationId || "");
    $("#gstIn").val(gstIn || "");
    $("#caseReportingDate").val(caseReportingDate || "");
    $("#period").val(period || "");

    // Read JSON safely from script tag
    let selectValues = {};
    try {
      const raw = document.getElementById('locationJson').textContent || "{}";
      selectValues = JSON.parse(raw);
    } catch(e){ selectValues = {}; }

    // Rebuild options, excluding current assignedFromLocationId
    const $loc = $('#locationId');
    $loc.empty().append('<option disabled selected value="">Select Jurisdiction</option>');

    Object.keys(selectValues).forEach(function(key){
      if (String(key) === String(assignedFromLocationId)) return;
      const isSel = String(key) === String(suggestedJurisdictionId);
      const opt = new Option(selectValues[key], key, isSel, isSel);
      $loc.append(opt);
    });

    // Ensure Select2 reflects new options inside modal
    if ($loc.hasClass("select2-hidden-accessible")) {
      $loc.trigger('change.select2'); // refresh
    }
    $loc.select2({ dropdownParent: $('#transferModal') });

    // Show modal
    $("#transferModal").modal('show');
  }

  // Hardening controls (kept consistent with other pages)
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
  $(document).ready(function () {
    function disableBack() { window.history.forward(); }
    window.onload = disableBack;
    window.onpageshow = evt => { if (evt.persisted) disableBack(); };
  });
</script>
</body>
</html>
