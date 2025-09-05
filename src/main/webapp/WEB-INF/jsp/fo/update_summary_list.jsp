<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Update Status List</title>

  <!-- Styles -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
</head>

<body class="hold-transition sidebar-mini">
<div class="wrapper">
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>
  <jsp:include page="../layout/confirmation_popup.jsp"/>

  <div class="content-wrapper">
    <!-- Page header -->
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6"><h1>Update Status</h1></div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a>Home</a></li>
              <li class="breadcrumb-item active">Update Status</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <!-- Page body -->
    <section class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-12">

            <div class="card-primary">
              <div class="card-header">
                <h3 class="card-title">Update Status</h3>
              </div>

              <div class="card-body card">
                <!-- flash messages -->
                <c:if test="${not empty closeclasemessage}">
                  <div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
                    <strong>${closeclasemessage}</strong>
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                      <span aria-hidden="true">&times;</span>
                    </button>
                  </div>
                </c:if>
                <c:if test="${not empty message}">
                  <div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
                    <strong>${message}</strong>
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                      <span aria-hidden="true">&times;</span>
                    </button>
                  </div>
                </c:if>

                <!-- filters -->
                <div class="row">
                  <div class="col-md-6 form-group">
                    <label class="col-md-12">Case Type</label>
                    <select id="caseType" name="caseType"
                            class="selectpicker col-md-12"
                            data-live-search="true"
                            title="Please Select case type">
                      <c:forEach items="${caseTypeList}" var="caseType">
                        <c:choose>
                          <c:when test="${selectedCaseType eq caseType.key}">
                            <option value="${caseType.key}" selected>${caseType.value}</option>
                          </c:when>
                          <c:otherwise>
                            <option value="${caseType.key}">${caseType.value}</option>
                          </c:otherwise>
                        </c:choose>
                      </c:forEach>
                    </select>
                  </div>

                  <c:if test="${not empty categories}">
                    <div class="col-md-6 form-group">
                      <label class="col-md-12">Category <span class="text-danger">*</span></label>
                      <select id="category" name="category"
                              class="selectpicker col-md-12"
                              data-live-search="true"
                              title="Please Select Category">
                        <c:forEach items="${categories}" var="category">
                          <c:choose>
                            <c:when test="${category.id eq categoryId}">
                              <option value="${category.id}" selected>${category.name}</option>
                            </c:when>
                            <c:otherwise>
                              <option value="${category.id}">${category.name}</option>
                            </c:otherwise>
                          </c:choose>
                        </c:forEach>
                      </select>
                    </div>
                  </c:if>
                </div>

                <!-- loader -->
                <div id="loader" class="text-center" style="display:none;">
                  <i class="fa fa-spinner fa-spin" style="font-size:52px;color:#007bff;"></i>
                </div>

                <!-- list target -->
                <div id="dataListDiv"></div>
              </div>
            </div>

          </div>
        </div>
      </div>
    </section>
  </div>

  <jsp:include page="../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- Scripts -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>
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
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<script>
  // --- hardening (per your pattern)
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
  $(function(){
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = evt => { if (evt.persisted) disableBack(); };
  });

  // Flash message fade
  $(function() {
    $("#message").fadeTo(2000, .5).slideUp(500, function(){ $(this).remove(); });
  });

  // Bootstrap select
  $(function(){ $('.selectpicker').selectpicker(); });

  // Helper to check auth and load into the list div
  function authLoad(url){
    $.ajax({
      url: '/checkLoginStatus',
      method: 'get',
      async: false,
      success: function(result){
        if(result === 'true'){
          $("#dataListDiv").empty();
          $("#loader").show();
          setTimeout(function(){
            $("#dataListDiv").load(url, function(_resp, status){
              $("#loader").hide();
              if(status !== 'success'){ console.warn('Load failed:', url); }
            });
          }, 300);
        } else {
          window.location.reload();
        }
      },
      error: function(){ window.location.reload(); }
    });
  }

  // Case type change -> navigate to persist selection
  $("#caseType").on('change', function(){
    const selectedValue = $(this).val();
    window.location.href = "/fo/update_summary_list?selectedCaseType=" + selectedValue;
  });

  // Category change -> load list (special handling for id=13)
  $("#category").on('change', function(){
    const selectedValue   = $(this).val();
    const selectedCaseType = '${selectedCaseType}';
    const extended = (selectedCaseType && Number(selectedCaseType) > 0)
      ? ("&selectedCaseType=" + selectedCaseType)
      : "";
    if(selectedValue === '13'){
      authLoad('/fo/get_scrutiny_data_list?id=' + selectedValue);
    }else{
      authLoad('/fo/update_summary_data_list?id=' + selectedValue + extended);
    }
  });

  // Optional: validation for child forms rendered inside #dataListDiv
  function formValidation(){
    var actionStatusId = $("#actionStatus").val();
    if(actionStatusId == 1){ return true; }
    var fileName = (document.querySelector('#uploadedFile') || {}).value || '';
    var ext = fileName.slice((fileName.lastIndexOf(".") - 1 >>> 0) + 2).toLowerCase();
    var input = document.getElementById('uploadedFile');
    if(input && input.files && input.files[0]){
      var max = 10 * 1024 * 1024;
      if(ext === 'pdf'){
        if(input.files[0].size > max){
          alert('Please upload max 10MB file');
          input.value = '';
          return false;
        }
        return true;
      }else{
        alert("Please upload only pdf file");
        input.value = '';
        return false;
      }
    }else{
      alert("Please upload pdf file");
      return false;
    }
  }
  window.formValidation = formValidation;

  // Submit for approval helper (kept API/signature)
  function submitForApproval(GSTIN, period, caseReportingDate, needApproval, assigneToUserId){
    const payload = {
      GSTIN: GSTIN,
      period: period,
      caseReportingDate: caseReportingDate,
      needApproval: needApproval,
      assigneToUserId: assigneToUserId,
      '${_csrf.parameterName}': '${_csrf.token}'
    };

    if(needApproval !== 'yes'){
      $('#showFoAssignmentModal').modal('hide');
      return;
    }

    $.confirm({
      title: 'Confirm!',
      content: 'Do you want to proceed ahead to submit the case for field officer ?',
      buttons: {
        submit: function(){
          $.ajax({
            url: '/fo/caseForApproval',
            method: 'POST',
            data: payload,
            success: function(resp){
              if(resp && resp.success){
                $.alert({
                  title: 'Success',
                  content: resp.message || 'Submitted successfully',
                  buttons: { ok: function(){ location.reload(); } }
                });
                $(`tr[data-gstin="${GSTIN}"][data-period="${period}"][data-reporting-date="${caseReportingDate}"]`).remove();
              }else{
                $.alert(resp && resp.message ? resp.message : 'Submission failed.');
              }
              $("#showAssignmentModal, #showFoAssignmentModal").modal('hide');
            },
            error: function(){
              $.alert('Error in submission! Please check your connection.');
              $("#showAssignmentModal, #showFoAssignmentModal").modal('hide');
            }
          });
        },
        close: function(){ $.alert('Canceled!'); }
      }
    });
  }
  window.submitForApproval = submitForApproval;
</script>
</body>
</html>
