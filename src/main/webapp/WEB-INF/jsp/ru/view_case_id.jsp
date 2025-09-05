<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST | Approve / Reject Case ID Update</title>

  <!-- Icons / Fonts -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">

  <!-- AdminLTE 4 (Bootstrap 5) -->
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">

  <!-- Plugins used by this page -->
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">

  <style>
    .btn-outline-custom{color:#495057;border:1px solid #ced4da;text-align:left}
    .class{border:1px solid #000;padding:10px}
  </style>
</head>
<body class="sidebar-mini layout-fixed">
<div class="wrapper">
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <div class="content-wrapper">
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6"><h1>Approve / Reject Case ID Update</h1></div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-end">
              <li class="breadcrumb-item"><a href="/hq/dashboard">Home</a></li>
              <li class="breadcrumb-item active">Approve / Reject Case ID</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title">Review Request</h3>
          </div>

          <div class="card-body">
            <form method="POST" action="updateCaseId" name="caseUpdateForm" id="caseUpdateForm" enctype="multipart/form-data">
              <div class="row">
                <div class="col-md-12">
                  <div class="form-group">
                    <label for="recovery">Existing Case ID</label>
                    <input class="form-control" type="text" value="${oldCaseId}" name="caseid" readonly />
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-md-12">
                  <div class="form-group">
                    <label for="recovery">New Case ID Proposed</label>
                    <input class="form-control" type="text" value="${suggestedCaseId}" name="otherRemarks" readonly />
                  </div>
                </div>
              </div>

              <input type="hidden" id="status" name="status" />
              <input type="hidden" id="gstnocaseid" name="gstnocaseid" value="${gst}" />
              <input type="hidden" id="datecaseid" name="datecaseid" value="${date}" />
              <input type="hidden" id="periodcaseid" name="periodcaseid" value="${period}" />
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

              <div class="row">
                <div class="col-md-12">
                  <div class="form-group">
                    <label for="recovery">Remarks</label>
                    <input class="form-control" type="text" value="${remarks}" readonly />
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-md-12" id="remarks">
                  <div class="form-group">
                    <label for="recovery">Reasons for Approval/Rejection <span style="color:red">*</span></label>
                    <input class="form-control" type="text" id="approvalRemarks" name="approvalRemarks" required />
                  </div>
                </div>
              </div>

              <hr>

              <div class="d-flex justify-content-end gap-2">
                <button type="submit" class="btn btn-primary me-2" id="submitCaseapprove" onclick="approveCaseId('approve')">Approve</button>
                <button type="submit" class="btn btn-danger" id="submitCaseReject" onclick="rejectCaseId('reject')">Reject</button>
              </div>
            </form>
          </div>
        </div>

      </div>
    </section>
  </div>

  <jsp:include page="../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- Core JS -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- AdminLTE 4 -->
<script src="/static/dist/js/adminlte.min.js"></script>

<!-- Plugins used by this page -->
<script src="/static/dist/js/jquery-confirm.min.js"></script>
<script src="/static/dist/js/bootstrap-select.min.js"></script>

<script>
// context/keyboard guards (unchanged)
document.addEventListener('contextmenu', e => e.preventDefault());
document.addEventListener('keydown', e => { if (e.ctrlKey && e.key === 'u') e.preventDefault(); });
document.addEventListener('keydown', e => { if (e.key === 'F12') e.preventDefault(); });
$(document).ready(function () {
  function disableBack(){window.history.forward()}
  window.onload = disableBack();
  window.onpageshow = function(evt){ if (evt.persisted) disableBack() }
});
document.onkeydown = function (e) {
  if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) e.preventDefault();
};

// dynamic rows (kept even if not used on this screen)
$(document).ready(function () {
  var counter = 0;
  $("#addrow").on("click", function () {
    var newRow = $("<tr>");
    var cols = "";
    cols += '<td><input type="text" class="form-control recoveryclass" id="recoveryStageArn" name="recoveryStageArn[' + counter + ']" placeholder="Please enter Recovery Stage ARN" maxlength="15" pattern="[A-Za-z0-9]{15}" title="Please enter 15-digits alphanumeric Recovery Stage ARN" required /></td>';
    cols += '<td><input type="button" class="ibtnDel btn btn-sm btn-danger" value="Delete"></td>';
    newRow.append(cols);
    $("table.order-list").append(newRow);
    counter++;
  });
  $("table.order-list").on("click", ".ibtnDel", function () {
    $(this).closest("tr").remove();
    counter -= 1;
  });
});

// bootstrap-select init
$(function(){ $('.selectpicker').selectpicker(); });

// approval flow (unchanged logic)
$('form').on('submit', function(oEvent){
  oEvent.preventDefault();
  var status = $("#status").val();

  if(status === 'approve'){
    $.confirm({
      title: 'Confirm!',
      content: 'Do you want to proceed ahead with approval of request for case id updation?',
      buttons: {
        submit: function(){ oEvent.currentTarget.submit(); },
        close: function(){ $.alert('Canceled!'); }
      }
    });
  }

  if(status === 'reject'){
    $.confirm({
      title: 'Confirm!',
      content: 'Do you want to proceed ahead with rejection of request of for case id updation?',
      buttons: {
        submit: function(){ oEvent.currentTarget.submit(); },
        close: function(){ $.alert('Canceled!'); }
      }
    });
  }
});

function rejectCaseId(val){ if(val === 'reject'){ $("#status").val('reject'); } }
function approveCaseId(val){ if(val === 'approve'){ $("#status").val('approve'); } }
</script>
</body>
</html>
