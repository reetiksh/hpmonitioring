<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>HP GST | Update Recovery (Raised Query)</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <!-- AdminLTE / Bootstrap / Plugins -->
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">

  <style>
    .btn-outline-custom{color:#495057;border:1px solid #ced4da;text-align:left}
    .class{border:1px solid #000;padding:10px}
  </style>
</head>
<body class="hold-transition sidebar-mini layout-fixed">
<div class="wrapper">
  <!-- Preloader (optional) -->
  <div class="preloader flex-column justify-content-center align-items-center">
    <img class="animation__shake" src="/static/dist/img/AdminLTELogo.png" alt="AdminLTELogo" height="60" width="60">
  </div>

  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <div class="content-wrapper">
    <!-- Page header -->
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6"><h1>Update Recovery (Raised Query)</h1></div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="/fo/dashboard">Home</a></li>
              <li class="breadcrumb-item active">Update Recovery (Raised Query)</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <!-- Page content -->
    <section class="content">
      <div class="container-fluid">
        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title">Case Details & Update</h3>
          </div>

          <div class="card-body">
            <div id="submitUpdateCaseTagLine" style="color: red; display:none;">
              Please enter amount in required format (e.g. :- 6591).
            </div>

            <!-- ========== FORM ========== -->
            <form method="POST"
                  action="fo_update_raised_query_recovery_cases"
                  name="caseUpdateForm"
                  id="caseUpdateForm"
                  enctype="multipart/form-data">

              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

              <!-- Row 1 -->
              <div class="row">
                <div class="col-md-3">
                  <div class="form-group">
                    <label>GSTIN</label>
                    <input class="form-control" id="gstin" name="GSTIN_ID"
                           value="${viewItem.GSTIN_ID}" readonly />
                  </div>
                </div>

                <div class="col-md-3">
                  <div class="form-group">
                    <label>Taxpayer Name</label>
                    <input class="form-control" id="taxpayerName" name="taxpayerName"
                           value="${viewItem.taxpayerName}" readonly />
                  </div>
                </div>

                <div class="col-md-3">
                  <div class="form-group">
                    <label>Jurisdiction</label>
                    <input class="form-control" id="circle" name="circle"
                           value="${viewItem.circle}" readonly />
                  </div>
                </div>

                <div class="col-md-3">
                  <div class="form-group">
                    <label>Case Category</label>
                    <input class="form-control" id="caseCategory"
                           name="category" value="${viewItem.category}" readonly />
                  </div>
                </div>
              </div>

              <!-- Row 2 -->
              <div class="row">
                <div class="col-md-3">
                  <div class="form-group">
                    <label>Period</label>
                    <input class="form-control" value="${viewItem.period_ID}" readonly />
                  </div>
                </div>

                <div class="col-md-3">
                  <div class="form-group">
                    <label>Reporting Date (DD-MM-YYYY)</label>
                    <input class="form-control"
                           value="<fmt:formatDate value='${viewItem.date}' pattern='dd-MM-yyyy' />"
                           readonly />
                  </div>
                </div>

                <input type="hidden" class="form-control"
                       id="caseReportingDate" name="caseReportingDate_ID"
                       value="${viewItem.caseReportingDate_ID}" />
                <input type="hidden" name="period_ID" value="${viewItem.period_ID}" />

                <div class="col-md-3">
                  <div class="form-group">
                    <label>Indicative Value (₹)</label>
                    <input class="form-control" value="${viewItem.indicativeTaxValue}" readonly />
                  </div>
                </div>

                <div class="col-md-3">
                  <div class="form-group">
                    <label>Action Status</label>
                    <input class="form-control" value="${submittedData.actionStatusName}" readonly />
                  </div>
                </div>
              </div>

              <!-- Row 3 -->
              <div class="row">
                <div class="col-md-3" id="span4">
                  <div class="form-group">
                    <label>Case Id</label>
                    <input class="form-control" value="${viewItem.caseId}" readonly />
                  </div>
                </div>

                <div class="col-md-3" id="span5">
                  <div class="form-group">
                    <label>Case Stage</label>
                    <input class="form-control" value="${submittedData.caseStageName}" readonly />
                  </div>
                </div>

                <div class="col-md-3" id="span6">
                  <div class="form-group">
                    <label>Case Stage ARN/Reference no</label>
                    <input class="form-control" value="${submittedData.caseStageArn}" readonly />
                  </div>
                </div>

                <div class="col-md-3" id="span2">
                  <div class="form-group">
                    <label>Amount (₹)</label>
                    <input class="form-control" type="text" value="${submittedData.demand}" readonly />
                  </div>
                </div>
              </div>

              <!-- Row 4 -->
              <div class="row">
                <div class="col-md-3">
                  <div class="form-group">
                    <label>Recovery Stage</label>
                    <select class="form-control selectpicker"
                            id="recoveryStage"
                            name="recoveryStage"
                            data-live-search="true"
                            title="Please select Recovery Stage"
                            onchange="onAction()">
                      <option value="0" disabled>Select</option>
                      <c:forEach items="${listRecovery}" var="categories">
                        <option value="${categories.id}"
                                <c:if test="${categories.id == '4'}">disabled</c:if>
                                <c:if test="${categories.id == submittedData.recoveryStage}">selected</c:if>>
                          ${categories.name}
                        </option>
                      </c:forEach>
                    </select>
                  </div>
                </div>

                <div class="col-md-3">
                  <div class="form-group">
                    <label>Recovery Via DRC03 (₹)</label>
                    <input class="form-control" type="text" id="recoveryByDRC3"
                           value="${submittedData.recoveryByDRC3}" readonly />
                  </div>
                </div>

                <div class="col-md-3">
                  <div class="form-group">
                    <label>Recovery Against Demand (₹) <span class="text-danger">*</span></label>
                    <input class="form-control"
                           type="text"
                           maxlength="11"
                           id="recoveryAgainstDemand"
                           name="recoveryAgainstDemand"
                           onkeypress='return event.charCode >= 48 && event.charCode <= 57'
                           value="${submittedData.recoveryAgainstDemand}"
                           title="Please enter Recovery Against Demand"
                           required />
                  </div>
                </div>
              </div>

              <!-- Recovery ARN rows -->
              <c:if test="${not empty submittedData.recoveryStageArnStr}">
                <c:set var="strlst" value="${submittedData.recoveryStageArnStr}" />
                <c:set var="lst" value="${fn:split(strlst,',')}" />
              </c:if>
              <input type="hidden" id="arnLength" value="${fn:length(lst)}" />

              <div class="row">
                <div class="col-md-3">
                  <div class="form-group">
                    <label>Recovery Stage ARN/Reference no <span class="text-danger">*</span></label>
                    <table id="myTable" class="table order-list">
                      <thead></thead>
                      <tbody></tbody>
                      <tfoot>
                      <c:if test="${fn:length(lst) > 0}">
                        <c:forEach items="${lst}" varStatus="loop" var="lst">
                          <tr>
                            <td>
                              <input type="text"
                                     class="form-control recoveryclass"
                                     value="${fn:trim(lst)}"
                                     id="recoveryStageArn"
                                     name="recoveryStageArn[${loop.index}]"
                                     placeholder="Please enter Recovery Stage ARN"
                                     maxlength="15"
                                     pattern="[A-Za-z0-9]{15}"
                                     title="Please enter 15-digits alphanumeric Recovery Stage ARN"
                                     required />
                            </td>
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

                <div class="col-md-3">
                  <div class="form-group">
                    <label>File Upload <span class="text-danger">*</span></label>
                    <span> (upload only pdf file with max file size of 10MB)</span>
                    <input class="form-control" type="file" id="uploadedFile" name="uploadedFile" accept=".pdf" required />
                    <c:if test="${submittedData.sum eq 'fileexist'}">
                      <br>
                      <a href="/fo/downloadUploadedFile?fileName=${submittedData.remarks}">
                        <button type="button" class="btn btn-primary"><i class="fa fa-download"></i></button>
                      </a>
                    </c:if>
                  </div>
                </div>
              </div>

              <hr>

              <!-- Submit -->
              <div class="row">
                <div class="col-md-12">
                  <div class="form-group float-right">
                    <button type="submit" class="btn btn-primary" id="submitCase">Submit</button>
                  </div>
                </div>
              </div>

            </form>
            <!-- ========== /FORM ========== -->
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
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  // Hardening
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

  // Selectpicker
  $(function(){ $('.selectpicker').selectpicker(); });

  // Dynamic Recovery ARN rows
  $(document).ready(function () {
    var counter = Number($("#arnLength").val() || 0);

    $("#addrow").on("click", function () {
      var newRow = $("<tr>");
      var cols = "";
      cols += '<td><input type="text" class="form-control recoveryclass" id="recoveryStageArn" ' +
              'name="recoveryStageArn[' + counter + ']" placeholder="Please enter Recovery Stage ARN" ' +
              'maxlength="15" pattern="[A-Za-z0-9]{15}" title="Please enter 15-digits alphanumeric Recovery Stage ARN" required /></td>';
      cols += '<td><input type="button" class="ibtnDel btn btn-md btn-danger" value="Delete"></td>';
      newRow.append(cols);
      $("table.order-list").append(newRow);
      counter++;
    });

    $("table.order-list").on("click", ".ibtnDel", function () {
      $(this).closest("tr").remove();
    });

    // Clear RAD when stage=3 (Closed via DRC-03)
    $('#recoveryStage').on('change', function() {
      var selectedOption = $(this).val();
      if (selectedOption === '3') {
        $('#recoveryAgainstDemand').val('');
      }
    });
  });

  // Prevent selecting earlier recovery stage
  function onAction() {
    var recoveryId = Number($("#recoveryStage").val() || 0);
    var recoveryStatus = Number(${submittedData.recoveryStage});
    if (recoveryId < recoveryStatus) {
      alert("Action not allowed: You can not select previous recovery status/stage");
      $("#recoveryStage").val(String(recoveryStatus)).change();
    }
  }

  // Submit handler
  $('#caseUpdateForm').on('submit', function(oEvent) {
    var recoveryAgainstDemand = $("#recoveryAgainstDemand").val();
    var isValid = /^\d+$/.test(recoveryAgainstDemand);
    if (!isValid) {
      $('#submitUpdateCaseTagLine').show();
      oEvent.preventDefault();
      return;
    }
    $('#submitUpdateCaseTagLine').hide();
    oEvent.preventDefault();

    var recoveryStage = $("#recoveryStage").val();
    var recoveryStageArn = $("#recoveryStageArn").val();
    var contentMessage = (recoveryStage == '3')
      ? 'Do you want to proceed ahead with closing the case?'
      : 'Do you want to proceed ahead with updating case details?';

    $.confirm({
      title: 'Confirm!',
      content: contentMessage,
      buttons: {
        submit: function () {
          // If recovery needs ARN, ensure at least one input exists
          if (recoveryStage == '2' || recoveryStage == '3') {
            if (typeof recoveryStageArn === 'undefined' || recoveryStageArn === null) {
              $.alert("Please add Recovery Stage ARN/Reference no");
              return;
            }
          }

          var fileName = (document.querySelector('#uploadedFile') || {}).value || '';
          var ext = fileName.slice((fileName.lastIndexOf(".") - 1 >>> 0) + 2).toLowerCase();
          var input = document.getElementById('uploadedFile');

          if (input && input.files && input.files[0]) {
            var maxAllowedSize = 10 * 1024 * 1024;
            if (ext === 'pdf') {
              if (input.files[0].size > maxAllowedSize) {
                $.alert('Please upload max 10MB file');
                input.value = '';
              } else {
                // OK submit
                oEvent.currentTarget.submit();
              }
            } else {
              $.alert("Please upload only pdf file");
              input.value = '';
            }
          } else {
            $.alert("Please upload pdf file");
          }
        },
        close: function () {
          $.alert('Canceled!');
        }
      }
    });
  });
</script>
</body>
</html>
