<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>Recommend Mandatory Scrutiny</title>

  <!-- AdminLTE 4 / Bootstrap 5 -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css"/>
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css"/>

  <!-- DataTables (optional; remove if not listing cases here) -->
  <link rel="stylesheet" href="/static/plugins/datatables-bs5/css/dataTables.bootstrap5.min.css"/>
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap5.min.css"/>
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap5.min.css"/>

  <!-- jQuery Confirm (for the confirm dialog) -->
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css"/>

  <style>
    :root{ --hp-sidebar-width:210px; }
    .app-sidebar{ width:var(--hp-sidebar-width)!important; }
    .app-main{ margin-left:0!important; transition:margin-left .2s ease; }
    body.sidebar-collapse .app-main{ margin-left:0!important; }
    @media (max-width: 991.98px){ .app-main{ margin-left:0!important; } }
    .app-content > .container, .app-content > .container-fluid{ max-width:100%!important; width:100%!important; }

    .btn-outline-custom{ color:#495057; border:1px solid #ced4da; text-align:left; }

    /* (Optional legacy styles kept from your snippet) */
    .popuprej{ display:none; position:fixed; top:50%; left:50%; transform:translate(-50%,-50%); padding:20px; background:#fff; border:1px solid #ccc; box-shadow:0 4px 8px rgba(0,0,0,.1); z-index:999; }
    .overlayrej{ display:none; position:fixed; inset:0; background:rgba(0,0,0,.5); z-index:998; }
  </style>
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- Header & Sidebar -->
  <jsp:include page="../../layout/header.jsp"/>
  <jsp:include page="../../layout/sidebar.jsp"/>

  <!-- ====================== MAIN ====================== -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6"><h1 class="m-0">Recommend Mandatory Scrutiny</h1></div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/hq/dashboard">Home</a></li>
                <li class="breadcrumb-item active">Recommend Scrutiny</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- Example table (hook your list here; or remove the table & use your own trigger buttons) -->
        <div class="card">
          <div class="card-header"><h3 class="card-title mb-0">Cases</h3></div>
          <div class="card-body">
            <div class="table-responsive">
              <table id="example1" class="table table-bordered table-striped w-100">
                <thead>
                <tr>
                  <th class="text-center align-middle">GSTIN</th>
                  <th class="text-center align-middle">Period</th>
                  <th class="text-center align-middle">Reporting Date</th>
                  <th class="text-center align-middle">Action</th>
                </tr>
                </thead>
                <tbody>
                <!-- Replace 'caseList' with your server variable; fields: gstin, period, reportingDate -->
                <c:forEach items="${caseList}" var="c">
                  <tr>
                    <td>${c.gstin}</td>
                    <td>${c.period}</td>
                    <td>${c.reportingDate}</td>
                    <td class="text-center">
                      <button type="button" class="btn btn-primary"
                              onclick="openRecommendModal('${c.gstin}','${c.period}','${c.reportingDate}')">
                        Recommend
                      </button>
                    </td>
                  </tr>
                </c:forEach>
                </tbody>
              </table>
            </div>

            <!-- Demo trigger if you’re not rendering a list yet -->
            <button class="btn btn-outline-primary mt-3"
                    onclick="openRecommendModal('24ABCDE1234F1Z5','062024','2024-06-30')">
              Demo: Open Modal
            </button>
          </div>
        </div>

      </div>
    </div>
  </main>

  <!-- Footer -->
  <jsp:include page="../../layout/footer.jsp"/>

</div>

<!-- overlay for mobile sidebar close -->
<div class="sidebar-overlay" data-lte-dismiss="sidebar"></div>

<!-- ====================== MODAL ====================== -->
<div class="modal fade" id="randomRecommendScrutinyCaseModal" tabindex="-1"
     aria-labelledby="randomRecommendScrutinyCaseModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">

      <!-- inline alert for missing remarks -->
      <div id="appealRevisionRejectRemarksMissingTagLine"
           style="color:red; display:none; padding:10px;">
        Please enter remarks.
      </div>

      <div class="modal-header">
        <h5 class="modal-title" id="randomRecommendScrutinyCaseModalTitle">
          <b>Recommend to FO for Mandatory Scrutiny</b>
        </h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"
                aria-label="Close"></button>
      </div>

      <div class="modal-body">
        <form method="POST" id="recommendCaseForScrutinyDetails" action="recommended_from_hq_for_scrutiny">
          <input type="hidden" id="recommendScrutinyGstin" name="recommendScrutinyGstin">
          <input type="hidden" id="recommendScrutinyPeriod" name="recommendScrutinyPeriod">
          <input type="hidden" id="recommendScrutinyCaseReportingDate" name="recommendScrutinyCaseReportingDate">
          <input type="hidden" id="recommendScrutinyRemark" name="recommendScrutinyRemark">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

          <!-- (unused duplicate line kept for compatibility if referenced elsewhere) -->
          <div id="appealRevisionRejectRemarksValueTagLine" style="color:red; display:none;">Please enter remarks !</div>

          <div class="mb-2">
            <label class="form-label">Remarks <span class="text-danger">*</span></label>
          </div>
          <textarea class="form-control" id="appealRevisionRejectRemarksValue"
                    name="appealRevisionRejectRemarksValue"
                    placeholder="Remarks" rows="4"></textarea>
        </form>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="transferCaseBtn"
                onclick="submitAppealRevisionRejectDetails()">Submit</button>
      </div>

    </div>
  </div>
</div>

<!-- ====================== SCRIPTS ====================== -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<!-- DataTables (optional) -->
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

<!-- jQuery Confirm -->
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  /* Keep content aligned with real sidebar width */
  function setSidebarVar(){
    const sb = document.querySelector('.app-sidebar');
    const collapsed = document.body.classList.contains('sidebar-collapse');
    const px = collapsed ? 0 : (sb ? sb.offsetWidth : 210);
    document.documentElement.style.setProperty('--hp-sidebar-width', px + 'px');
  }
  setSidebarVar();
  new MutationObserver(setSidebarVar).observe(document.body, {attributes:true, attributeFilter:['class']});
  window.addEventListener('resize', setSidebarVar);

  /* Optional table init */
  $(function () {
    if ($('#example1').length){
      $("#example1").DataTable({
        responsive: false,
        lengthChange: false,
        autoWidth: true,
        scrollX: true,
        buttons: ["excel","pdf","print"]
      }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
    }
  });

  /* Hardening (as in your other pages) */
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
  $(document).ready(function () {
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = e => { if (e.persisted) disableBack(); };
  });

  /* ========= Modal helpers ========= */
  function openRecommendModal(gstin, period, reportingDate){
    $("#recommendScrutinyGstin").val(gstin);
    $("#recommendScrutinyPeriod").val(period);
    $("#recommendScrutinyCaseReportingDate").val(reportingDate);
    $("#appealRevisionRejectRemarksMissingTagLine").hide();
    $("#appealRevisionRejectRemarksValue").val('');
    $("#randomRecommendScrutinyCaseModal").modal('show');
  }

  function submitAppealRevisionRejectDetails(){
    const remarks = ($("#appealRevisionRejectRemarksValue").val() || '').trim();
    if (remarks){
      $("#recommendScrutinyRemark").val(remarks);
      $.confirm({
        title : 'Confirm!',
        content : 'Do you want to proceed ahead with recommending this case for mandatory scrutiny?',
        buttons : {
          submit : function() {
            // Hide the correct modal id (fixed from original snippet)
            $('#randomRecommendScrutinyCaseModal').modal('hide');
            setTimeout(function(){
              document.getElementById("recommendCaseForScrutinyDetails").submit();
            }, 300);
          },
          close : function(){ $.alert('Canceled!'); }
        }
      });
    } else {
      $("#appealRevisionRejectRemarksMissingTagLine").show();
    }
  }
</script>
</body>
</html>
