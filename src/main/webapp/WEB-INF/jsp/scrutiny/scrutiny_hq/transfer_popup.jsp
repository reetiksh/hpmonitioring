<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>Transfer Case</title>

  <!-- AdminLTE 4 / Bootstrap 5 -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css"/>
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css"/>

  <!-- Optional -->
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css"/>

  <style>
    .btn-outline-custom{ color:#495057; border:1px solid #ced4da; text-align:left; }
    :root{ --hp-sidebar-width:210px; }
    .app-sidebar{ width:var(--hp-sidebar-width)!important; }
    .app-main{ margin-left:0!important; transition:margin-left .2s ease; }
    body.sidebar-collapse .app-main{ margin-left:0!important; }
    @media (max-width: 991.98px){ .app-main{ margin-left:0!important; } }
    .app-content > .container, .app-content > .container-fluid{ max-width:100%!important; width:100%!important; }
  </style>
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- Header & Sidebar (adjust paths as needed) -->
  <jsp:include page="../../layout/header.jsp"/>
  <jsp:include page="../../layout/sidebar.jsp"/>

  <!-- ====================== MAIN ====================== -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6"><h1 class="m-0">Transfer Case</h1></div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/hq/dashboard">Home</a></li>
                <li class="breadcrumb-item active">Transfer Case</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- Demo trigger (optional). In real use, call showTransferModal(...) from your table row buttons -->
        <div class="card">
          <div class="card-body">
            <button type="button" class="btn btn-primary"
                    onclick="showTransferModal('24ABCDE1234F1Z5','2024-06-30','062024','101','Requested by Circle A')">
              Open Transfer Modal (demo)
            </button>
          </div>
        </div>

      </div>
    </div>
  </main>

  <jsp:include page="../../layout/footer.jsp"/>

</div>

<!-- overlay for mobile sidebar -->
<div class="sidebar-overlay" data-lte-dismiss="sidebar"></div>

<!-- =============== Modal =============== -->
<div class="modal fade" id="transferModal" tabindex="-1" aria-labelledby="transferModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="transferModalTitle">Transfer Case</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <!-- No direct action; Approve/Reject build a hidden POST with CSRF -->
      <form method="POST" name="TransferForm" onsubmit="return false;">
        <div class="modal-body">

          <div class="mb-3">
            <label class="form-label">Remarks</label>
            <input type="text" class="form-control" id="remark" name="remark" readonly/>
          </div>

          <!-- Hidden context fields -->
          <input type="hidden" id="gstIn" name="gstIn"/>
          <input type="hidden" id="caseReportingDate" name="caseReportingDate"/>
          <input type="hidden" id="period" name="period"/>
          <input type="hidden" id="assignedFromLocationId" name="assignedFromLocationId"/>

          <div class="mb-3">
            <label class="form-label">Jurisdiction to which case will be assigned <span class="text-danger">*</span></label>
            <select id="locationId" name="locationId" class="form-select" required>
              <option value="">-- Select Jurisdiction --</option>
              <!-- Populate from server: provide a List of locations  (id, locationName) as "locationList" -->
              <c:forEach items="${locationList}" var="loc">
                <option value="${loc.id}">${loc.locationName}</option>
              </c:forEach>
            </select>
          </div>

          <!-- Radio remark options (if you need them; otherwise remove) -->
          <div class="btn-group-vertical w-100 mb-2" role="group" data-toggle="buttons">
            <!-- example options; keep names/values if used elsewhere -->
            <label class="btn btn-outline-custom">
              <input type="radio" name="remarkOptions" value="1"> Data mismatch
            </label>
            <label class="btn btn-outline-custom">
              <input type="radio" name="remarkOptions" value="2"> Out of jurisdiction
            </label>
            <label class="btn btn-outline-custom">
              <input type="radio" name="remarkOptions" value="3"> Workload balancing
            </label>
            <label class="btn btn-outline-custom">
              <input type="radio" name="remarkOptions" value="4" onclick="showHideOtherRemark()"> Other (specify)
            </label>
          </div>

          <textarea class="form-control" id="remarksId" name="otherRemarks" style="display:none" placeholder="Enter remarks"></textarea>

          <!-- Reject reason -->
          <div class="mt-3" id="rejectRemarkDiv" style="display:none" title="Please enter your remark within 250 letters">
            <label for="rejectRemark" class="form-label">
              Reason(s) for Rejection <span class="text-danger">*</span> <span id="rejectRemark_alert"></span>
            </label>
            <input type="text" class="form-control" id="rejectRemark" name="rejectRemark" maxlength="250" placeholder="Please Enter Your Remarks"/>
          </div>

        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" style="width: 100px;" onclick="approve()">Approve</button>
          <button type="button" class="btn btn-danger"  style="width: 100px;" onclick="reject()">Reject</button>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- =============== SCRIPTS =============== -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  /* Sync content with real sidebar width so toggle works */
  function setSidebarVar(){
    const sb = document.querySelector('.app-sidebar');
    const collapsed = document.body.classList.contains('sidebar-collapse');
    const px = collapsed ? 0 : (sb ? sb.offsetWidth : 210);
    document.documentElement.style.setProperty('--hp-sidebar-width', px + 'px');
  }
  setSidebarVar();
  new MutationObserver(setSidebarVar).observe(document.body, {attributes:true, attributeFilter:['class']});
  window.addEventListener('resize', setSidebarVar);

  /* Hardening (kept to match your other pages) */
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

  // ===== Helpers =====
  function showHideOtherRemark(){
    const selectedValue = $('input[name="remarkOptions"]:checked').val();
    if (selectedValue === '4'){ $("#remarksId").show(); } else { $("#remarksId").hide(); }
  }

  function validateForm(){
    const caseAssignedTo = $("#locationId").val();
    const remarkOptions = $('input[name="remarkOptions"]:checked').val();
    const otherRemarks = $("#remarksId").val().trim();

    if (caseAssignedTo && remarkOptions){
      if (remarkOptions === '4'){
        if (otherRemarks !== '') return true;
        alert("Please fill required information"); return false;
      }
      return true;
    }
    alert("Please fill required information");
    return false;
  }

  // Open & seed modal (call this from your table row buttons)
  function showTransferModal(gstIn, reportingDate, period, fromLocationId, requestedRemark){
    $("#gstIn").val(gstIn);
    $("#caseReportingDate").val(reportingDate);
    $("#period").val(period);
    $("#assignedFromLocationId").val(fromLocationId);
    $("#remark").val(requestedRemark || '');
    $("#rejectRemarkDiv").hide();
    $("#rejectRemark").val('');
    $("#locationId").val(''); // reset selection
    $("#transferModal").modal('show');
  }

  // ===== Approve / Reject actions =====
  function approve(){
    // hide reject block if visible
    $("#rejectRemarkDiv").hide();

    const gstIn = $("#gstIn").val();
    const caseReportingDate = $("#caseReportingDate").val();
    const period = $("#period").val();
    const assignedTo = $("#locationId").val();

    if (!assignedTo){
      $.alert("Please select the jurisdiction to assign.");
      return;
    }

    const form = document.createElement('form');
    form.method = 'post';
    form.action = "/scrutiny_hq/request_for_transfer";

    const add = (name, value) => {
      const input = document.createElement('input');
      input.type = 'hidden';
      input.name = name;
      input.value = value;
      form.appendChild(input);
    };

    add('gstIn', gstIn);
    add('caseReportingDate', caseReportingDate);
    add('period', period);
    add('assignedTo', assignedTo);
    add('${_csrf.parameterName}', '${_csrf.token}');

    document.body.appendChild(form);
    $.confirm({
      title: 'Confirm!',
      content: 'Do you want to proceed ahead with approval of case transfer?',
      buttons: {
        submit: function(){ form.submit(); },
        close: function(){ $.alert('Canceled!'); }
      }
    });
  }

  function reject(){
    const $div = $("#rejectRemarkDiv");
    if ($div.is(":hidden")){ $div.show(); return; }

    const gstIn = $("#gstIn").val();
    const caseReportingDate = $("#caseReportingDate").val();
    const period = $("#period").val();
    const assignedTo = $("#assignedFromLocationId").val(); // send back to original (as per your code)
    const rejectRemark = $("#rejectRemark").val().trim();

    if (!rejectRemark){
      $("#rejectRemark_alert").html("Please enter remark !").css("color","red");
      return;
    }

    const form = document.createElement('form');
    form.method = 'post';
    form.action = "/scrutiny_hq/request_for_transfer";

    const add = (name, value) => {
      const input = document.createElement('input');
      input.type = 'hidden';
      input.name = name;
      input.value = value;
      form.appendChild(input);
    };

    add('gstIn', gstIn);
    add('caseReportingDate', caseReportingDate);
    add('period', period);
    add('assignedTo', assignedTo);
    add('rejectRemark', rejectRemark);
    add('${_csrf.parameterName}', '${_csrf.token}');

    document.body.appendChild(form);
    $.confirm({
      title: 'Confirm!',
      content: 'Do you want to proceed ahead with rejection of case transfer?',
      buttons: {
        submit: function(){ form.submit(); },
        close: function(){ $.alert('Canceled!'); }
      }
    });
  }

  // Global submit confirm (kept from your snippet; applies to any <form> on page)
  $('form').on('submit', function(e){
    e.preventDefault();
    $.confirm({
      title: 'Confirm!',
      content: 'Are you sure you want to submit!',
      buttons: {
        submit: () => e.currentTarget.submit(),
        close: () => $.alert('Canceled!')
      }
    });
  });
</script>
</body>
</html>
