<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>HP GST | Update Recovery Case</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">

  <!-- AdminLTE / Bootstrap / Plugins -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">

  <style>
    .btn-outline-custom{color:#495057;border:1px solid #ced4da;text-align:left;}
    .tagline-error{color:#c62828;display:none}
    .order-list td{vertical-align:middle}
  </style>
</head>
<body class="hold-transition sidebar-mini layout-fixed">
<div class="wrapper">
  <!-- header + sidebar -->
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <div class="content-wrapper">
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6"><h1>Update Recovery Case</h1></div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="/fo/dashboard">Home</a></li>
              <li class="breadcrumb-item active">Update Recovery Case</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">

        <div id="submitUpdateCaseTagLine" class="tagline-error">
          Please enter amount in required format (e.g. 6591).
        </div>

        <div class="card card-primary">
          <div class="card-header"><h3 class="card-title mb-0">Case Details</h3></div>

          <div class="card-body">
            <form method="POST" action="fo_update_recovery_cases" name="caseUpdateForm" id="caseUpdateForm" enctype="multipart/form-data">
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

              <div class="row">
                <div class="col-md-3">
                  <label for="gstin">GSTIN</label>
                  <input class="form-control" id="gstin" name="GSTIN_ID" value="${viewItem.GSTIN_ID}" readonly />
                </div>
                <div class="col-md-3">
                  <label for="taxpayerName">Taxpayer Name</label>
                  <input class="form-control" id="taxpayerName" name="taxpayerName" value="${viewItem.taxpayerName}" readonly />
                </div>
                <div class="col-md-3">
                  <label for="circle">Jurisdiction</label>
                  <input class="form-control" id="circle" name="circle" value="${viewItem.circle}" readonly />
                </div>
                <div class="col-md-3">
                  <label for="caseCategory">Case Category</label>
                  <input class="form-control" id="caseCategory" name="category" value="${viewItem.category}" readonly />
                </div>
              </div>

              <div class="row mt-3">
                <div class="col-md-3">
                  <label>Period</label>
                  <input class="form-control" value="${viewItem.period_ID}" readonly />
                  <input type="hidden" name="period_ID" value="${viewItem.period_ID}"/>
                </div>
                <div class="col-md-3">
                  <label>Reporting Date (DD-MM-YYYY)</label>
                  <input class="form-control" value="<fmt:formatDate value='${viewItem.date}' pattern='dd-MM-yyyy' />" readonly />
                </div>
                <div class="col-md-3">
                  <label>Indicative Value (₹)</label>
                  <input class="form-control" value="${viewItem.indicativeTaxValue}" readonly />
                </div>
                <div class="col-md-3">
                  <label>Action Status</label>
                  <input class="form-control" value="${submittedData.actionStatusName}" readonly />
                </div>

                <input type="hidden" id="caseReportingDate" name="caseReportingDate_ID" value="${viewItem.caseReportingDate_ID}" />
              </div>

              <div class="row mt-3">
                <div class="col-md-3">
                  <label>Case Id</label>
                  <input class="form-control" value="${viewItem.caseId}" readonly />
                </div>
                <div class="col-md-3">
                  <label>Case Stage</label>
                  <input class="form-control" value="${submittedData.caseStageName}" readonly />
                </div>
                <div class="col-md-3">
                  <label>Case Stage ARN / Reference No</label>
                  <input class="form-control" value="${submittedData.caseStageArn}" readonly />
                </div>
                <div class="col-md-3">
                  <label>Amount (₹)</label>
                  <input class="form-control" value="${submittedData.demand}" readonly />
                </div>
              </div>

              <div class="row mt-3">
                <div class="col-md-3">
                  <label for="recoveryStage">Recovery Stage <span class="text-danger">*</span></label>
                  <select class="form-control selectpicker" id="recoveryStage" name="recoveryStage" data-live-search="true" title="Please select Recovery Stage" required>
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

                <div class="col-md-3">
                  <label>Recovery Via DRC03 (₹)</label>
                  <input class="form-control" id="recoveryByDRC3" value="${submittedData.recoveryByDRC3}" readonly />
                </div>

                <div class="col-md-3">
                  <label for="recoveryAgainstDemand">Recovery Against Demand (₹) <span class="text-danger">*</span></label>
                  <input class="form-control" id="recoveryAgainstDemand" name="recoveryAgainstDemand"
                         maxlength="11" inputmode="numeric" pattern="^\\d+$"
                         onkeypress="return event.charCode>=48 && event.charCode<=57"
                         value="${submittedData.recoveryAgainstDemand}" required />
                </div>
              </div>

              <!-- Recovery Stage ARN table -->
              <c:if test="${not empty submittedData.recoveryStageArnStr}">
                <c:set var="strlst" value="${submittedData.recoveryStageArnStr}"/>
                <c:set var="lst" value="${fn:split(strlst,',')}"/>
              </c:if>
              <input type="hidden" id="arnLength" value="${fn:length(lst)}"/>

              <div class="row mt-3">
                <div class="col-md-6">
                  <label>Recovery Stage ARN / Reference No <span class="text-danger">*</span></label>
                  <table id="myTable" class="table order-list">
                    <thead></thead>
                    <tbody></tbody>
                    <tfoot>
                      <c:if test="${fn:length(lst) > 0}">
                        <c:forEach items="${lst}" varStatus="loop" var="lst">
                          <tr>
                            <td style="width:85%;">
                              <input type="text" class="form-control recoveryclass"
                                     value="${fn:trim(lst)}"
                                     name="recoveryStageArn[${loop.index}]"
                                     placeholder="Please enter Recovery Stage ARN"
                                     maxlength="15" pattern="[A-Za-z0-9]{15}"
                                     title="Please enter 15-digits alphanumeric Recovery Stage ARN"
                                     required />
                            </td>
                            <td style="width:15%;">
                              <input type="button" class="ibtnDel btn btn-md btn-danger" value="Delete">
                            </td>
                          </tr>
                        </c:forEach>
                      </c:if>
                      <tr>
                        <td colspan="2">
                          <input type="button" class="btn btn-primary" id="addrow" value="Add Row"/>
                        </td>
                      </tr>
                    </tfoot>
                  </table>
                </div>

                <div class="col-md-6">
                  <label>File Upload <span class="text-danger">*</span></label>
                  <span> (upload only PDF, max 10MB)</span>
                  <input class="form-control" type="file" id="uploadedFile" name="uploadedFile" accept=".pdf" required />
                  <c:if test="${submittedData.sum eq 'fileexist'}">
                    <div class="mt-2">
                      <a href="/fo/downloadUploadedFile?fileName=${submittedData.remarks}" class="btn btn-primary">
                        <i class="fa fa-download"></i> Download current file
                      </a>
                    </div>
                  </c:if>
                </div>
              </div>

              <hr>
              <div class="d-flex justify-content-end">
                <button type="submit" class="btn btn-primary" id="submitCase">Submit</button>
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

<!-- Scripts -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  // Hardening (same as your snippet)
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    if ((e.ctrlKey && e.key === 'u') || e.key === 'F12' ||
        e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) e.preventDefault();
  });
  $(document).ready(function(){
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = function(evt){ if (evt.persisted) disableBack(); }
  });

  // selectpicker
  $(function(){ $('.selectpicker').selectpicker(); });

  // Row add/remove for ARNs
  $(function(){
    var counter = parseInt($("#arnLength").val() || "0", 10);
    $("#addrow").on("click", function(){
      var $tr = $("<tr>");
      var cols = "";
      cols += '<td style="width:85%;"><input type="text" class="form-control recoveryclass" ' +
              'name="recoveryStageArn[' + counter + ']" ' +
              'placeholder="Please enter Recovery Stage ARN" maxlength="15" ' +
              'pattern="[A-Za-z0-9]{15}" title="Please enter 15-digits alphanumeric Recovery Stage ARN" required /></td>';
      cols += '<td style="width:15%;"><input type="button" class="ibtnDel btn btn-md btn-danger" value="Delete"></td>';
      $tr.append(cols);
      $("table.order-list").append($tr);
      counter++;
    });
    $("table.order-list").on("click", ".ibtnDel", function(){ $(this).closest("tr").remove(); });
  });

  // RecoveryStage specific tweak: if "3" selected, clear Recovery Against Demand (as in your code)
  $(function(){
    $('#recoveryStage').on('change', function(){
      if ($(this).val() === '3') $('#recoveryAgainstDemand').val('');
    });
  });

  // Shared utility: validate PDF size and type
  function validatePdfInput(inputEl, maxBytes){
    if (!inputEl.files || !inputEl.files[0]) return false; // no file yet
    var f = inputEl.files[0];
    var name = f.name || '';
    var ext = name.split('.').pop().toLowerCase();
    if (ext !== 'pdf'){ $.alert("Please upload only PDF file"); inputEl.value=''; return false; }
    if (f.size > maxBytes){ $.alert("Please upload max 10MB file"); inputEl.value=''; return false; }
    return true;
  }

  // Submit handler (cleaned up, same logic as yours)
  $('#caseUpdateForm').on('submit', function(oEvent){
    oEvent.preventDefault();

    // numeric check for Recovery Against Demand
    var rad = $("#recoveryAgainstDemand").val();
    if (!/^\d+$/.test(rad || "")){
      $('#submitUpdateCaseTagLine').show();
      return;
    }
    $('#submitUpdateCaseTagLine').hide();

    var recoveryStage = $("#recoveryStage").val();
    // At least one ARN input must exist when stage 2 or 3
    var arnInputsCount = $('input[name^="recoveryStageArn"]').length;

    var contentMessage = (recoveryStage === '3')
      ? 'Do you want to proceed ahead with closing the case?'
      : 'Do you want to proceed ahead with updating case details?';

    $.confirm({
      title: 'Confirm!',
      content: contentMessage,
      buttons: {
        submit: () => {
          if (recoveryStage === '2' || recoveryStage === '3'){
            if (arnInputsCount === 0){
              $.alert("Please add Recovery Stage ARN/Reference no");
              return;
            }
            if (!validatePdfInput(document.getElementById('uploadedFile'), 10 * 1024 * 1024)) return;
            oEvent.currentTarget.submit();
          } else {
            if (!validatePdfInput(document.getElementById('uploadedFile'), 10 * 1024 * 1024)) return;
            oEvent.currentTarget.submit();
          }
        },
        close: () => $.alert('Canceled!')
      }
    });
  });

  // Prevent moving to a *previous* recovery stage
  function onAction(){
    var chosen = parseInt($("#recoveryStage").val() || "0", 10);
    var current = parseInt(${submittedData.recoveryStage}, 10);
    if (chosen < current){
      alert("Action not allowed: You cannot select a previous recovery status/stage.");
      $("#recoveryStage").val(String(current)).change();
      $('.selectpicker').selectpicker('refresh');
    }
  }
  $('#recoveryStage').on('change', onAction);
  // initialize once (in case select rendered with different default)
  $(onAction);
</script>
</body>
</html>
