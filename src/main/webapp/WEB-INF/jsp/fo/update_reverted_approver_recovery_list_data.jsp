<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST | Recovery Cases</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">

  <!-- Styles -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">

  <style>
    .modal-lg,.modal-xl{ max-width:1400px; }
    .table-responsive{ overflow:auto; max-height:800px; }
    .btn-outline-custom{ color:#495057; border:1px solid #ced4da; text-align:left; }
  </style>
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
  <!-- Header + Sidebar -->
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <!-- Content -->
  <div class="content-wrapper">
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6"><h1>Recovery Cases (Query Raised)</h1></div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="/fo/dashboard">Home</a></li>
              <li class="breadcrumb-item active">Recovery Cases</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">

        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title mb-0">Cases requiring recovery updates</h3>
          </div>

          <div class="card-body">
            <div class="table-responsive">
              <table id="dataListTable" class="table table-bordered table-striped">
                <thead>
                  <tr>
                    <th class="text-center align-middle">GSTIN</th>
                    <th class="text-center align-middle">Taxpayer Name</th>
                    <th class="text-center align-middle">Jurisdiction</th>
                    <th class="text-center align-middle">Period</th>
                    <th class="text-center align-middle">Reporting Date<br>(DD-MM-YYYY)</th>
                    <th class="text-center align-middle">Indicative Value (₹)</th>
                    <th class="text-center align-middle">Action Status</th>
                    <th class="text-center align-middle">Case ID</th>
                    <th class="text-center align-middle">Case Stage</th>
                    <th class="text-center align-middle">Case Stage ARN</th>
                    <th class="text-center align-middle">Amount (₹)</th>
                    <th class="text-center align-middle">Recovery Stage</th>
                    <th class="text-center align-middle">Recovery Stage ARN</th>
                    <th class="text-center align-middle">Recovery Via DRC03 (₹)</th>
                    <th class="text-center align-middle">Recovery Against Demand (₹)</th>
                    <th class="text-center align-middle">Parameter</th>
                    <th class="text-center align-middle">Remarks</th>
                    <th class="text-center align-middle">Action</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach items="${listofCases}" var="item">
                    <tr>
                      <td>${item.GSTIN_ID}</td>
                      <td>${item.taxpayerName}</td>
                      <td>${item.circle}</td>
                      <td>${item.period_ID}</td>
                      <td><fmt:formatDate value="${item.date}" pattern="dd-MM-yyyy"/></td>
                      <td>${item.indicativeTaxValue}</td>
                      <td>${item.actionStatusName}</td>
                      <td>${item.caseId}</td>
                      <td>${item.caseStageName}</td>
                      <td>${item.caseStageArn}</td>
                      <td>${item.demand}</td>
                      <td>${item.recoveryStageName}</td>
                      <td>${item.recoveryStageArnStr}</td>
                      <td>${item.recoveryByDRC3}</td>
                      <td>${item.recoveryAgainstDemand}</td>
                      <td>${item.parameter}</td>
                      <td>${item.remarks}</td>
                      <td class="text-nowrap">
                        <button type="button"
                                class="btn btn-primary btn-sm"
                                onclick="view('${item.GSTIN_ID}','${item.caseReportingDate_ID}','${item.period_ID}')">
                          Update
                        </button>
                      </td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
          </div>
        </div>

      </div>
    </section>
  </div>

  <!-- Footer -->
  <jsp:include page="../layout/footer.jsp"/>

  <!-- Modal -->
  <div class="modal fade" id="updateSummaryViewModal" tabindex="-1" aria-labelledby="updateSummaryViewTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="updateSummaryViewModalTitle">Update Recovery Case</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span>&times;</span></button>
        </div>
        <div class="modal-body" id="updateSummaryViewBody"></div>
      </div>
    </div>
  </div>

  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- Scripts -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
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
<script src="/static/dist/js/adminlte.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  // Hardening (as supplied)
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key;
    if ((e.ctrlKey && k === 'u') || k === 'F12') e.preventDefault();
  });
  // Disable refresh
  document.onkeydown = function (e) {
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) e.preventDefault();
  };
  // BFC/Back-forward cache guard
  $(function(){
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = function(evt){ if (evt.persisted) disableBack(); }
  });

  // Table
  $(function(){
    $("#dataListTable").DataTable({
      responsive: true,
      lengthChange: false,
      autoWidth: false,
      buttons: ["excel", "pdf", "print", "colvis"]
    }).buttons().container().appendTo('#dataListTable_wrapper .col-md-6:eq(0)');
  });

  // Modal loader
  function view(gst, date, period){
    $.ajax({
      url: '/checkLoginStatus',
      method: 'get',
      async: false,
      success: function(result){
        // server returns "true"/"false" string; compare directly
        if (result === 'true') {
          $("#updateSummaryViewBody").load('/fo/view_raise_query_recovery_case/id?gst='+encodeURIComponent(gst)+'&date='+encodeURIComponent(date)+'&period='+encodeURIComponent(period),
            function(_, status){
              if (status === 'success') {
                $("#updateSummaryViewModal").modal('show');
              } else {
                $.alert('Failed to load case details.');
              }
            });
        } else {
          // session expired or not logged in -> refresh
          window.location.reload();
        }
      },
      error: function(){
        $.alert('Unable to verify session. Please try again.');
      }
    });
  }
</script>
</body>
</html>
