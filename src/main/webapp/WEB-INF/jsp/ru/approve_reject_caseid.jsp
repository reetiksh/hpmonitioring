<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Acknowledge Case List</title>

  <!-- Bootstrap 5 CSS (required by AdminLTE 4) -->
  <link rel="stylesheet" href="/static/plugins/bootstrap/css/bootstrap.min.css"/>

  <!-- AdminLTE 4 -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">

  <!-- DataTables (Bootstrap 5 builds) -->
  <link rel="stylesheet" href="/static/plugins/datatables-bs5/css/dataTables.bootstrap5.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap5.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap5.min.css">

  <!-- Optional -->
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- Header / Sidebar -->
  <jsp:include page="../layout/header.jsp" />
  <jsp:include page="../fo/transfer_popup.jsp" />
  <jsp:include page="../layout/sidebar.jsp" />

  <!-- ====================== MAIN ====================== -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page header -->
        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6">
              <h1 class="m-0">Approve/Reject Case Id</h1>
            </div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="#">Home</a></li>
                <li class="breadcrumb-item active">Approve/Reject Case Id</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- Card -->
        <div class="row">
          <div class="col-12">
            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title mb-0">Pending for Approval/Rejection of Case Id List</h3>
              </div>

              <div class="card-body">
                <!-- Flash message -->
                <c:if test="${not empty message}">
                  <div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
                    <strong>${message}</strong>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                  </div>
                </c:if>

                <div class="table-responsive">
                  <table id="example1" class="table table-bordered table-striped align-middle w-100">
                    <thead class="table-light">
                      <tr>
                        <th class="text-center">GSTIN</th>
                        <th class="text-center">Taxpayer Name</th>
                        <th class="text-center">Jurisdiction</th>
                        <th class="text-center">Case Category</th>
                        <th class="text-center">Period</th>
                        <th class="text-center">Reporting Date (DD-MM-YYYY)</th>
                        <th class="text-center">Indicative Value(₹)</th>
                        <th class="text-center">Action Status</th>
                        <th class="text-center">Case ID</th>
                        <th class="text-center">Case Stage</th>
                        <th class="text-center">Case Stage ARN</th>
                        <th class="text-center">Amount(₹)</th>
                        <th class="text-center">Recovery Stage</th>
                        <th class="text-center">Recovery Stage ARN</th>
                        <th class="text-center">Recovery Via DRC03(₹)</th>
                        <th class="text-center">Recovery Against Demand(₹)</th>
                        <th class="text-center">Parameter</th>
                        <th class="text-center">Uploaded File</th>
                        <th class="text-center">Action</th>
                      </tr>
                    </thead>
                    <tbody>
                      <c:forEach items="${listofCases}" var="total">
                        <tr>
                          <td>${total.GSTIN_ID}</td>
                          <td>${total.taxpayerName}</td>
                          <td>${total.circle}</td>
                          <td>${total.category}</td>
                          <td>${total.period_ID}</td>
                          <td><fmt:formatDate value="${total.date}" pattern="dd-MM-yyyy" /></td>
                          <td>${total.indicativeTaxValue}</td>
                          <td>${total.actionStatusName}</td>
                          <td>${total.caseId}</td>
                          <td>${total.caseStageName}</td>
                          <td>${total.caseStageArn}</td>
                          <td>${total.demand}</td>
                          <td>${total.recoveryStageName}</td>
                          <td>${total.recoveryStageArnStr}</td>
                          <td>${total.recoveryByDRC3}</td>
                          <td>${total.recoveryAgainstDemand}</td>
                          <td>${total.parameter}</td>
                          <td class="text-center">
                            <c:if test="${total.uploadedFileName != null}">
                              <a href="/ru/downloadFile?fileName=${total.uploadedFileName}" class="btn btn-sm btn-primary" title="Download">
                                <i class="fa fa-download"></i>
                              </a>
                            </c:if>
                          </td>
                          <td class="text-center">
                            <button class="btn btn-sm btn-primary"
                                    onclick="updateCaseId('${total.GSTIN_ID}', '${total.caseReportingDate_ID}', '${total.period_ID}')">
                              Approve/Reject Case Id
                            </button>
                          </td>
                        </tr>
                      </c:forEach>
                    </tbody>
                  </table>
                </div><!-- /.table-responsive -->
              </div><!-- /.card-body -->
            </div>
          </div>
        </div>

      </div><!-- /.container-fluid -->
    </div><!-- /.app-content -->
  </main>

  <jsp:include page="../layout/footer.jsp" />

</div><!-- /.app-wrapper -->

<!-- ================ Modal (Bootstrap 5 markup) ================ -->
<div class="modal fade" id="updateSummaryViewModal" tabindex="-1" aria-labelledby="updateSummaryViewModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="updateSummaryViewModalTitle">Update Case ID</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body" id="updateSummaryViewBody"></div>
    </div>
  </div>
</div>

<!-- ====================== SCRIPTS (order matters) ====================== -->
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
  // Fade out alerts
  $(function () {
    $("#message").each(function () {
      $(this).fadeTo(2000, 0.5).slideUp(500, () => $(this).remove());
    });
  });

  // DataTables init (BS5)
  $(function () {
    const dt = $("#example1").DataTable({
      responsive: false,
      lengthChange: false,
      autoWidth: true,
      scrollX: true,
      buttons: ["excel", "pdf", "print", "colvis"]
    });
    dt.buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
  });

  // Open modal with AJAX-loaded content
  function updateCaseId(gst, date, period){
    $.ajax({
      url: '/checkLoginStatus',
      method: 'get',
      async: false,
      success: function(result){
        if (result === 'true') {
          $("#updateSummaryViewBody").load(
            '/ru/view_case_id/id?gst='+encodeURIComponent(gst)+'&date='+encodeURIComponent(date)+'&period='+encodeURIComponent(period),
            function(response, status){
              if (status === 'success') {
                const modalEl = document.getElementById('updateSummaryViewModal');
                if (modalEl) new bootstrap.Modal(modalEl).show();
              } else {
                console.warn('Failed to load modal content');
              }
            });
        } else if (result === 'false') {
          window.location.reload();
        }
      }
    });
  }

  // Hardening (unchanged)
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
  (function disableBFCache(){
    function f(){ window.history.forward(); }
    window.onload = f;
    window.onpageshow = evt => { if (evt.persisted) f(); };
  })();
</script>
</body>
</html>
