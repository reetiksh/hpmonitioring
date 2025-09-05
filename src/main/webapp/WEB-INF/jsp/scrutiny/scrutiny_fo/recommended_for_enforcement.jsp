<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>HP GST | Recommended for Enforcement</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />

  <!-- AdminLTE 4 / Bootstrap 5 -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">

  <!-- Plugins you already use on this page (kept exactly as-is) -->
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">

  <style>
    .btn-outline-custom{ color:#495057;border:1px solid #ced4da;text-align:left; }
    .hp-card-bg{ background:#f1f1f1;border-radius:3px;box-shadow:2px 2px 4px 1px #8888884d; }
    /* Make any .container within content full width */
    .app-content>.container,.app-content>.container-fluid{max-width:100%!important;width:100%!important;}
  </style>
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- Header / Sidebar includes (unchanged) -->
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <!-- ===================== MAIN ===================== -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page header -->
        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6">
              <h1 class="m-0">Recommended for Enforcement</h1>
            </div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/fo/dashboard">Home</a></li>
                <li class="breadcrumb-item active">Recommended for Enforcement</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- Card -->
        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title mb-0">Recommended for Enforcement</h3>
          </div>

          <div class="card-body hp-card-bg">

            <!-- ========= YOUR ORIGINAL CONTENT (dataset/logic untouched) ========= -->

<link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
<script src="/static/dist/js/bootstrap-select.min.js"></script>

<script>
  $(function(){ if (window.bsCustomFileInput) bsCustomFileInput.init(); });
  $('.selectpicker').selectpicker();

  /************ add row for scrutiny proceedingg dropped start ****************/
  $(document).ready(function () {
    var counter = $("#arnLength").val();

    $("#addrow").on("click", function () {
      var newRow = $("<tr>");
      var cols = "";
      cols += '<td><input type="text" class="form-control recoveryclass" id="recoveryStageArn" name="recoveryStageArn[' + counter + ']" placeholder="Please enter Recovery Stage ARN" maxlength="15" pattern="[A-Za-z0-9]{15}" title="Please enter 15-digits alphanumeric Recovery Stage ARN" required /></td>';
      cols += '<td><input type="button" class="ibtnDel btn btn-md btn-danger" value="Delete"></td>';
      newRow.append(cols);
      $("table.order-list").append(newRow);
      counter++;
    });

    $("table.order-list").on("click", ".ibtnDel", function (event) {
      $(this).closest("tr").remove();
    });
  });
  /************ add row for scrutiny proceedingg dropped end ****************/

  /* $('form').on('submit', function(oEvent) {
    oEvent.preventDefault();
    $.confirm({
      title : 'Confirm!',
      content : 'Do you want to proceed ahead with scrutiny initiate?',
      buttons : {
        submit : function() { oEvent.currentTarget.submit(); },
        close : function() { $.alert('Canceled!'); }
      }
    });
  }) */
</script>

<div class="card-body" style="background-color:#f1f1f1;border-radius:3px;box-shadow:2px 2px 4px 1px #8888884d;">
  <form method="POST" name="asmtTenForm" id="asmtTenForm" action="submit_asmt_ten" enctype="multipart/form-data">
    <h4><b><u>Recommended for Enforcement:</u></b></h4><br>

    <%-- The following blocks are intentionally kept (commented) to preserve existing logic/dataset exactly as provided. Do not remove. --%>
    <%-- 
    <div class="row">
      <div class="col-md-3" id="span4">
        <div class="form-group">
          <label for="caseId">Case Id <span style="color:red">*</span></label>
          <input class="form-control" id="caseId" name="caseId" placeholder="Please enter Case Id" maxlength="15" pattern="[A-Za-z0-9]{15}" title="Please enter 15-digits alphanumeric Case Id" />
        </div>
      </div>
      <div class="col-md-3" id="span6">
        <div class="form-group">
          <label for="caseStageARN">Case Stage<span style="color:red">*</span></label>
          <input class="form-control" id="caseStage" name="caseStage" placeholder="Please select Case Stage" value="ASMT-10 Issued" title="ASMT-10 Issued" readonly />
        </div>
      </div>
      <div class="col-md-3" id="span6">
        <div class="form-group">
          <label for="caseStageARN">Case Stage ARN/Reference no<span style="color:red">*</span></label>
          <input class="form-control" id="caseStageArn" name="caseStageArn" placeholder="Please enter Case Stage ARN" maxlength="15" pattern="[A-Za-z0-9]{15}" value="${submittedData.caseStageArn}" title="Please enter 15-digits alphanumeric Case Stage ARN" />
        </div>
      </div>
      <div class="col-md-3" id="span2">
        <div class="form-group">
          <label for="demand">Amount(₹)<span style="color:red">*</span></label>
          <input class="form-control" type="text" maxlength="11" id="demand" name="demand" onkeypress='return event.charCode >= 48 && event.charCode <= 57' value="${submittedData.demand}" title="Please enter Amount" />
        </div>
      </div>

      <div class="col-lg-12 alert alert-danger alert-dismissible fade show" role="alert" id="message1" style="max-height:500px;overflow-y:auto;display:none;">
        <span id="alertMessage1"></span>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      </div>
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </div>

    <div class="row">
      <div class="col-md-3">
        <div class="form-group">
          <label for="recoveryStage"> Recovery Stage <span style="color:red">*</span></label>
          <select class="form-control selectpicker recoveryStage" id="recoveryStage" name="recoveryStage" data-live-search="true" title="Please select Recovery Stage">
            <option data-tokens="recoverystage" value="0" disabled>Select</option>
            <c:forEach items="${recoveryStageList}" var="categories">
              <option value="${categories.id}" <c:if test="${categories.id == '4'}">disabled</c:if> <c:if test="${categories.id == submittedData.recoveryStage}">selected</c:if>>${categories.name}</option>
            </c:forEach>
          </select>
        </div>
      </div>

      <div class="col-md-3">
        <div class="form-group">
          <label for="recoveryByDRC">Recovery Via DRC03(₹)<span style="color:red">*</span></label>
          <input class="form-control" type="text" maxlength="11" id="recoveryByDRC3" name="recoveryByDRC3" onkeypress='return event.charCode >= 48 && event.charCode <= 57' value="${submittedData.recoveryByDRC3}" title="Please enter Recovery Via DRC03" />
        </div>
      </div>

      <div class="col-md-3">
        <div class="form-group">
          <label for="excelFile">File Upload <span style="color:red">*</span></label><span> (upload only pdf file with max file size of 10MB ) </span>
          <input class="form-control" type="file" id="uploadedFile" name="uploadedFile" accept=".pdf">
          <c:if test="${submittedData.sum eq 'fileexist'}">
            <a href="/fo/downloadUploadedFile?fileName=${submittedData.remarks}"><button type="button" class="btn btn-primary"><i class="fa fa-download"></i></button></a>
          </c:if>
        </div>
      </div>

      <c:if test="${not empty submittedData.recoveryStageArnStr}">
        <c:set var="strlst" value="${submittedData.recoveryStageArnStr}"/>
        <c:set var="lst" value="${fn:split(strlst,',')}"/>
      </c:if>

      <input type="hidden" id="arnLength" value="${fn:length(lst)}">
      <input type="hidden" value="" id="asmtGstin" name="asmtGstin" readonly />
      <input type="hidden" value="" id="asmtPeriod" name="asmtPeriod" readonly />
      <input type="hidden" value="" id="asmtCaseReportingDate" name="asmtCaseReportingDate" readonly />

      <div class="col-md-3">
        <div class="form-group">
          <label for="recoveryByDRC">Recovery Stage ARN/Reference no<span style="color:red">*</span></label>
          <table id="myTable" class="table order-list">
            <thead></thead>
            <tbody></tbody>
            <tfoot>
              <c:if test="${fn:length(lst) > 0}">
                <c:forEach items="${lst}" varStatus="loop" var="lst">
                  <tr>
                    <td><input type="text" class="form-control recoveryclass" value="${fn:trim(lst)}" id="recoveryStageArn" name="recoveryStageArn[${loop.index}]" placeholder="Please enter Recovery Stage ARN" maxlength="15" pattern="[A-Za-z0-9]{15}" title="Please enter 15-digits alphanumeric Recovery Stage ARN" required /></td>
                    <td><input type="button" class="ibtnDel btn btn-md btn-danger" value="Delete"></td>
                  </tr>
                </c:forEach>
              </c:if>
              <tr>
                <td colspan="5" style="text-align:left;">
                  <input type="button" class="btn btn-primary" id="addrow" value="Add Row" />
                </td>
              </tr>
            </tfoot>
          </table>
        </div>
      </div>
    </div>
    --%>

    <hr>
    <div class="col-12 text-center">
      <img class="animation__shake" src="/static/image/under_development.png" alt="pdf" height="150" width="150"><br>
      <span style="font-size:35px;color:rgb(97,97,97)">This feature will be available shortly</span>
    </div>

    <%-- 
    <div class="row">
      <div class="col-lg-12">
        <div class="float-right">
          <button type="button" class="btn btn-primary" onclick="recommendedForAudit();">Submit</button>
        </div>
      </div>
    </div>
    --%>
  </form>
</div>

            <!-- ========= /YOUR ORIGINAL CONTENT ========= -->

          </div><!-- /.card-body -->
        </div><!-- /.card -->

      </div><!-- /.container-fluid -->
    </div><!-- /.app-content -->
  </main>

  <!-- Footer -->
  <jsp:include page="../layout/footer.jsp"/>

</div><!-- /.app-wrapper -->

<!-- overlay for mobile “tap outside to close” -->
<div class="sidebar-overlay" data-lte-dismiss="sidebar"></div>

<!-- =============== SCRIPTS =============== -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<!-- Plugins used above -->
<script src="/static/plugins/bs-custom-file-input/bs-custom-file-input.min.js"></script>
<script src="/static/dist/js/bootstrap-select.min.js"></script>

<script>
  // Hardening (kept from your pattern)
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
  // Optional: if you rely on sidebar width syncing in your project, you can keep this minimal helper
  function setSidebarVar(){
    const sb = document.querySelector('.app-sidebar');
    const collapsed = document.body.classList.contains('sidebar-collapse');
    const px = collapsed ? 0 : (sb ? sb.offsetWidth : 210);
    document.documentElement.style.setProperty('--hp-sidebar-width', px + 'px');
  }
  setSidebarVar();
  new MutationObserver(setSidebarVar).observe(document.body, {attributes:true, attributeFilter:['class']});
  window.addEventListener('resize', setSidebarVar);
</script>
</body>
</html>
