<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST | Dashboard</title>

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

  <style>
    /* Wider modal body for large tables */
    .modal-dialog { max-width: 2000px; }
    .modal-content { width: 100%; max-height: 90vh; overflow: auto; }
  </style>
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">
  <div class="preloader flex-column justify-content-center align-items-center">
    <img class="animation__shake" src="/static/dist/img/AdminLTELogo.png" alt="AdminLTELogo" height="60" width="60">
  </div>

  <!-- keep includes exactly as provided -->
  <jsp:include page="../layout/header.jsp" />
  <jsp:include page="../layout/sidebar.jsp" />

  <!-- ====================== MAIN ====================== -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page header -->
        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6"><h1 class="m-0">Period Wise Status</h1></div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/vw/dashboard">Home</a></li>
                <li class="breadcrumb-item"><a href="/vw/mis">MIS</a></li>
                <li class="breadcrumb-item active">Report</li>
              </ol>
            </div>
          </div>
        </div>

        <section class="content">
          <div class="card">
            <div class="card-body">
              <div class="table-responsive">
                <table id="foCaseStageList" class="table table-bordered table-striped w-100">
                  <thead>
                  <tr>
                    <th rowspan="2" style="text-align: center;">Period</th>
                    <th rowspan="2" style="text-align: center;">Yet to be Acknowledge</th>
                    <th rowspan="2" style="text-align: center;">Yet to Be Initiated</th>
                    <th colspan="3" style="text-align: center;">Initiated</th>
                    <th colspan="4" style="text-align: center;">Concluded</th>
                  </tr>
                  <tr>
                    <th style="text-align: center;">DRC-01A Issued</th>
                    <th style="text-align: center;">DRC01 Issued</th>
                    <th style="text-align: center;">ASMT-10 Issued</th>
                    <th style="text-align: center;">Case Dropped</th>
                    <th style="text-align: center;">Demand Created Via DRC07</th>
                    <th style="text-align: center;">Partial Voluntary Payment Remaining Demand</th>
                    <th style="text-align: center;">Demand Created However Discharged via DRC-03</th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach items="${consolidateCategoryWiseDataList}" var="object" varStatus="loop">
                    <tr>
                      <td style="text-align: center;"><c:out value="${object.period}" /></td>

                      <td style="text-align: center;">
                        <a class="fa fa-eye" aria-hidden="true" style="cursor:pointer;color:#4682b4"
                           onclick="drillDown('notAcknowledge','${loop.count}')">${object.notAcknowledge}</a>
                      </td>

                      <td style="text-align: center;">
                        <a class="fa fa-eye" aria-hidden="true" style="cursor:pointer;color:#4682b4"
                           onclick="drillDown('notApplicable','${loop.count}')">${object.notApplicable}</a>
                      </td>

                      <td style="text-align: center;">
                        <a class="fa fa-eye" aria-hidden="true" style="cursor:pointer;color:#4682b4"
                           onclick="drillDown('dRC01AIssued','${loop.count}')">${object.dRC01AIssued}</a>
                      </td>

                      <td style="text-align: center;">
                        <a class="fa fa-eye" aria-hidden="true" style="cursor:pointer;color:#4682b4"
                           onclick="drillDown('dRC01Issued','${loop.count}')">${object.dRC01Issued}</a>
                      </td>

                      <td style="text-align: center;">
                        <a class="fa fa-eye" aria-hidden="true" style="cursor:pointer;color:#4682b4"
                           onclick="drillDown('aSMT10Issued','${loop.count}')">${object.aSMT10Issued}</a>
                      </td>

                      <td style="text-align: center;">
                        <a class="fa fa-eye" aria-hidden="true" style="cursor:pointer;color:#4682b4"
                           onclick="drillDown('caseDropped','${loop.count}')">${object.caseDropped}</a>
                      </td>

                      <td style="text-align: center;">
                        <a class="fa fa-eye" aria-hidden="true" style="cursor:pointer;color:#4682b4"
                           onclick="drillDown('demandCreatedByDrc07','${loop.count}')">${object.demandCreatedByDrc07}</a>
                      </td>

                      <td style="text-align: center;">
                        <a class="fa fa-eye" aria-hidden="true" style="cursor:pointer;color:#4682b4"
                           onclick="drillDown('partialVoluntaryPaymentRemainingDemand','${loop.count}')">${object.partialVoluntaryPaymentRemainingDemand}</a>
                      </td>

                      <td style="text-align: center;">
                        <a class="fa fa-eye" aria-hidden="true" style="cursor:pointer;color:#4682b4"
                           onclick="drillDown('demandCreatedHoweverDischargedViaDrc03','${loop.count}')">${object.demandCreatedHoweverDischargedViaDrc03}</a>
                      </td>
                    </tr>
                  </c:forEach>
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          <!-- Modal (Bootstrap 5) -->
          <div class="modal fade" id="modal1" tabindex="-1" aria-labelledby="modal1Title" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title" id="modal1Title">Detailed View</h5>
                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body" id="exampleModal"></div>
                <div class="modal-footer">
                  <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
              </div>
            </div>
          </div>

        </section>

      </div>
    </div>
  </main>

  <jsp:include page="../layout/footer.jsp" />
</div>

<!-- overlay for mobile sidebar (tap outside to close) -->
<div class="sidebar-overlay" data-lte-dismiss="sidebar"></div>

<!-- Scripts -->
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

<!-- Optional -->
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  // hardening (kept)
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12') e.preventDefault();
  });
  $(document).ready(function(){
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = function(evt){ if (evt.persisted) disableBack(); }
  });
  document.onkeydown = function(e){
    const k = e.key.toLowerCase();
    if (k === 'f5' || (e.ctrlKey && k === 'r') || e.keyCode === 116) e.preventDefault();
  };

  // DataTables
  $("#foCaseStageList").DataTable({
    responsive: true,
    lengthChange: false,
    autoWidth: true
  });

  $("#example1").DataTable({
    responsive: true,
    lengthChange: false,
    autoWidth: true,
    buttons: ["excel","pdf","print"]
  }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');

  $('#example2').DataTable({
    paging: true,
    lengthChange: false,
    searching: false,
    ordering: true,
    info: true,
    autoWidth: true,
    responsive: true
  });

  // drillDown logic (preserved, Bootstrap 5 modal API)
  function drillDown(category, index) {
    var link = 'landingDrillDownPeriodWiseList?category=' + category + '&index=' + index;
    $.ajax({
      url: '/checkLoginStatus',
      method: 'get',
      async: false,
      success: function(result){
        const myJSON = JSON.parse(result);
        if(result=='true'){
          $('#exampleModal').load(link, function(response, status){
            if(status === 'success'){
              const modal = new bootstrap.Modal(document.getElementById('modal1'));
              modal.show();
            }else{
              console.log("failed");
            }
          });
        } else if(result=='false'){
          window.location.reload();
        }
      }
    });
  }
</script>
</body>
</html>
