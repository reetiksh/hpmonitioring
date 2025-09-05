<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Rejected / Update Summary</title>

  <!-- Styles -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">

  <style>
    .modal-lg,.modal-xl{ max-width:1200px; }
    .table-responsive{ overflow:scroll; max-height:800px; }
    .btn-outline-custom{ color:#495057; border:1px solid #ced4da; text-align:left; }
  </style>
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <div class="content-wrapper">
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6"><h1>Rejected / Update Summary</h1></div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a>Home</a></li>
              <li class="breadcrumb-item active">Rejected / Update Summary</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <!-- Filters -->
        <div class="row">
          <c:if test="${!empty parameters}">
            <div class="col-md-6 form-group">
              <label class="col-md-2">Parameter<span style="color:red;"> *</span></label>
              <select id="parameter" name="parameter" class="selectpicker col-md-8" data-live-search="true" title="Please Select Paramater">
                <option value="0">Select All Parameter</option>
                <c:forEach items="${parameters}" var="parameter">
                  <c:choose>
                    <c:when test="${parameter.id eq parameterId}">
                      <option value="${parameter.id}" selected="selected">${parameter.paramName}</option>
                    </c:when>
                    <c:otherwise>
                      <option value="${parameter.id}">${parameter.paramName}</option>
                    </c:otherwise>
                  </c:choose>
                </c:forEach>
              </select>
            </div>
          </c:if>
        </div>

        <!-- Loader + List -->
        <div id="loader" style="display:none; text-align:center;">
          <i class="fa fa-spinner fa-spin" style="font-size:52px;color:#007bff;"></i>
        </div>

        <div id="dataListDiv">
          <div class="table-responsive">
            <table id="dataListTable" class="table table-bordered table-striped">
              <thead>
              <tr>
                <th style="text-align:center;">GSTIN</th>
                <th style="text-align:center;">Taxpayer Name</th>
                <th style="text-align:center;">Jurisdiction</th>
                <th style="text-align:center;">Period</th>
                <th style="text-align:center;">Reporting Date<br>(DD-MM-YYYY)</th>
                <th style="text-align:center;">Indicative Value(₹)</th>
                <th style="text-align:center;">Action Status</th>
                <th style="text-align:center;">Case Id</th>
                <th style="text-align:center;">Case Stage</th>
                <th style="text-align:center;">Case Stage ARN</th>
                <th style="text-align:center;">Amount(₹)</th>
                <th style="text-align:center;">Recovery Stage</th>
                <th style="text-align:center;">Recovery Stage ARN</th>
                <th style="text-align:center;">Recovery Via DRC03(₹)</th>
                <th style="text-align:center;">Recovery Against Demand(₹)</th>
                <th style="text-align:center;">Parameter</th>
                <th style="text-align:center;">Remarks</th>
                <th style="text-align:center;">Case File</th>
                <th style="text-align:center;">Action</th>
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
                  <td><i class="fas fa-pen nav-icon fa-xs"></i> ${item.remarks}</td>
                  <td>
                    <c:if test="${item.uploadedFileName != null}">
                      <a href="/fo/downloadUploadedPdfFile?fileName=${item.uploadedFileName}" class="btn btn-primary">
                        <i class="fa fa-download"></i>
                      </a>
                    </c:if>
                  </td>
                  <td>
                    <button type="button" style="margin-bottom:5px;" class="btn btn-primary"
                            onclick="view('${item.GSTIN_ID}','${item.caseReportingDate_ID}','${item.period_ID}')">Update</button>

                    <c:choose>
                      <c:when test="${item.actionStatus eq '3' && item.caseStage eq '5'}">
                        <button type="button" class="btn btn-primary"
                                onclick="closeCase('${item.GSTIN_ID}','${item.caseReportingDate_ID}','${item.period_ID}')">Close</button>
                      </c:when>
                      <c:when test="${item.actionStatus eq '3' && item.caseStage eq '6'}">
                        <button type="button" class="btn btn-primary"
                                onclick="closeCase('${item.GSTIN_ID}','${item.caseReportingDate_ID}','${item.period_ID}')">Close</button>
                      </c:when>
                      <c:when test="${item.actionStatus eq '3' && item.caseStage eq '8'}">
                        <button type="button" class="btn btn-primary"
                                onclick="closeCase('${item.GSTIN_ID}','${item.caseReportingDate_ID}','${item.period_ID}')">Close</button>
                      </c:when>
                      <c:when test="${item.actionStatus eq '3' && item.caseStage eq '9'}">
                        <button type="button" class="btn btn-primary"
                                onclick="closeCase('${item.GSTIN_ID}','${item.caseReportingDate_ID}','${item.period_ID}')">Close</button>
                      </c:when>
                      <c:otherwise>
                        <button type="button" class="btn btn-primary" disabled>Close</button>
                      </c:otherwise>
                    </c:choose>

                    <c:if test="${item.caseIdUpdate eq 'yes'}">
                      <button type="button" style="margin:5px 0 0;" class="btn btn-primary"
                              onclick="updateCaseId('${item.GSTIN_ID}','${item.caseReportingDate_ID}','${item.period_ID}','${item.caseId}')">
                        Update Case ID
                      </button>
                    </c:if>
                  </td>
                </tr>
              </c:forEach>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </section>
  </div>

  <jsp:include page="../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- ===== Modals ===== -->

<!-- Update modal -->
<div class="modal fade" id="updateSummaryViewModal" tabindex="-1" role="dialog" aria-labelledby="updateSummaryViewTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="updateSummaryViewModalTitle">Update Case</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      </div>
      <div class="modal-body" id="updateSummaryViewBody"></div>
    </div>
  </div>
</div>

<!-- Close case modal -->
<div class="modal fade" id="closeCaseModal" tabindex="-1" role="dialog" aria-labelledby="closeCaseModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <form method="POST" action="fo_close_rejected_cases" name="closeCaseForm">
        <div class="modal-header">
          <h5 class="modal-title" id="confirmationModalTitle">Do you want to proceed ahead with closure of the case?</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

        <div class="modal-body">
          <p>I hereby undertake that;</p>
          <p><i class="fa fa-check"></i> All the points raised by EIU pertaining to this Taxpayer have been dealt carefully in the light of the provisions of the GST laws</p>
          <p><i class="fa fa-check"></i> Applicable Tax has been levied as per the provisions of GST acts/rules 2017</p>
          <p><i class="fa fa-check"></i> Applicable Interest has been levied as per the provisions of GST acts/rules 2017</p>
          <p><i class="fa fa-check"></i> Applicable Penalty has been levied as per the provisions of GST acts/rules 2017</p>
          <p><input type="checkbox" id="mycheckbox" name="checkbox"> I hereby declare that above information is true and correct to the best of my knowledge</p>
        </div>

        <input type="hidden" id="gstno" name="gstno">
        <input type="hidden" id="date" name="date">
        <input type="hidden" id="period" name="period">

        <div class="modal-footer">
          <div id="checked" style="display:none;">
            <button type="submit" class="btn btn-primary" id="okayBtn">Okay</button>
          </div>
          <button type="button" class="btn btn-secondary" data-dismiss="modal" id="closeBtn">Cancel</button>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- Update Case ID modal -->
<div class="modal fade" id="updateCaseidModal" tabindex="-1" role="dialog" aria-labelledby="updateCaseidModal" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="modalTitle">Update Case ID</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      </div>

      <form method="POST" name="updatecaseidForm" id="updatecaseidForm" action="update_rejected_caseid" enctype="multipart/form-data">
        <div class="modal-body">
          <input type="hidden" id="gstnocaseid" name="gstnocaseid">
          <input type="hidden" id="datecaseid" name="datecaseid">
          <input type="hidden" id="periodcaseid" name="periodcaseid">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

          <div class="row">
            <div class="col-md-12">
              <div class="form-group">
                <label>Old Case ID</label>
                <input class="form-control" type="text" id="caseid" readonly />
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-md-12">
              <div class="form-group">
                <label>New Case ID Proposed<span style="color:red;"> *</span></label>
                <input class="form-control" id="newcaseid" name="caseid" maxlength="15" pattern="[A-Za-z0-9]{15}" title="Please enter 15-digits alphanumeric Case Id" required />
              </div>
            </div>
          </div>

          <div class="form-group" style="margin-bottom:0;">
            <label class="col-form-label">Remarks<span style="color:red;"> *</span></label>
          </div>
          <div class="btn-group btn-group-vertical" role="group" data-toggle="buttons" style="width:100%;">
            <c:forEach items="${listRemarks}" var="item">
              <label class="btn btn-outline-custom">
                <input type="radio" name="remarks" id="${item.id}" onclick="showHideOtherRemark()" value="${item.id}" required> ${item.name}
              </label>
            </c:forEach>
          </div>
          <textarea class="form-control" id="remarksId" name="otherRemarks" style="display:none" placeholder="Remarks"></textarea>

          <div class="form-group" style="margin-bottom:0;">
            <label class="col-form-label">Upload File<span style="color:red;"> *</span></label>
          </div>
          <input type="file" id="filePath" name="filePath" accept=".pdf" required />
        </div>

        <!-- inline confirm (kept for parity, shown by code that triggers it) -->
        <div class="modal fade" id="confirmationModal" tabindex="-1" role="dialog" aria-labelledby="confirmationModalTitle" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="confirmationTransferModalTitle">Are you sure want to submit</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              </div>
              <div class="modal-footer">
                <button type="submit" class="btn btn-primary" id="okBtn">Okay</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal" id="cancelBtn">Cancel</button>
              </div>
            </div>
          </div>
        </div>

        <div class="modal-footer">
          <button type="submit" class="btn btn-primary" id="transferCaseBtn">Submit</button>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- ===== Scripts ===== -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

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
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  // Keep existing behavior
  function showHideOtherRemark(){
    var selectedValue = $('input[name="remarks"]:checked').val();
    if(selectedValue == 1){ $("#remarksId").show(); } else { $("#remarksId").hide(); }
  }
  $(function(){ $('.selectpicker').selectpicker(); });

  $('#updatecaseidForm').on('submit', function(oEvent){
    oEvent.preventDefault();
    var oldCaseId = document.querySelector('#caseid').value.trim();
    var newCaseId = document.querySelector('#newcaseid').value.trim();

    $.confirm({
      title:'Confirm!',
      content:'Do you want to proceed ahead to request case id updation?',
      buttons:{
        submit:function(){
          var fileName = document.querySelector('#filePath').value;
          var extension = fileName.slice((fileName.lastIndexOf(".") - 1 >>> 0) + 2);
          var input = document.getElementById('filePath');

          if(input.files && input.files[0]){
            var maxAllowedSize = 10 * 1024 * 1024;
            if(extension == 'pdf'){
              if(input.files[0].size > maxAllowedSize){
                $.alert('Please upload max 10MB file');
                input.value = '';
              }else{
                if(oldCaseId == newCaseId){
                  $.alert("New Case ID can not be same as Old Case ID");
                  $("#newcaseid").val("");
                }else{
                  oEvent.currentTarget.submit();
                }
              }
            }else{
              $.alert("Please upload only pdf file");
              document.querySelector('#filePath').value = '';
            }
          }else{
            $.alert("Please upload pdf file");
          }
        },
        close:function(){ $.alert('Canceled!'); }
      }
    });
  });

  // Hardening (unchanged)
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    if (e.ctrlKey && e.key === 'u') e.preventDefault();
    if (e.key === 'F12') e.preventDefault();
  });
  $(document).ready(function(){
    function disableBack(){ window.history.forward() }
    window.onload = disableBack();
    window.onpageshow = function(evt){ if(evt.persisted) disableBack(); }
  });
  document.onkeydown = function(e){
    if(e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116){ e.preventDefault(); }
  };

  function view(gst, date, period){
    $.ajax({
      url:'/checkLoginStatus',
      method:'get',
      async:false,
      success:function(result){
        const myJSON = JSON.parse(result);
        if(result=='true'){
          $("#updateSummaryViewBody").load('/fo/rejected_view_case/id?gst='+gst+'&date='+date+'&period='+period, function(response, status){
            if(status=='success'){ $("#updateSummaryViewModal").modal('show'); } else { console.log("failed"); }
          });
        } else if(result=='false'){ window.location.reload(); }
      }
    });
  }

  function closeCase(gstno, date, period){
    $("#gstno").val(gstno);
    $("#date").val(date);
    $("#period").val(period);
    $("#closeCaseModal").modal('show');
  }

  function updateCaseId(gstno, date, period, caseid){
    $("#gstnocaseid").val(gstno);
    $("#datecaseid").val(date);
    $("#periodcaseid").val(period);
    $("#caseid").val(caseid);
    $("#updateCaseidModal").modal('show');
  }

  $(function(){
    $('#mycheckbox').change(function(){ if(this.checked){ $("#checked").show(); } else { $("#checked").hide(); }});
    $("#updateSummaryClosedCaseBtn").on("click", function(){ $("#confirmationModal").modal('show'); });

    $("#updateSummaryViewBtn").on("click", function(){
      $.ajax({
        url:'/checkLoginStatus',
        method:'get',
        async:false,
        success:function(result){
          const myJSON = JSON.parse(result);
          if(result=='true'){
            $("#updateSummaryViewBody").load('//fo/rejected_view_case', function(response, status){
              if(status=='success'){ $("#updateSummaryViewModal").modal('show'); } else { console.log("failed"); }
            });
          } else if(result=='false'){ window.location.reload(); }
        }
      });
    });

    $("#okayBtn").on("click", function(){ $("#closeCaseModal").modal('hide'); });
    $("#closeBtn").on("click", function(){ $("#closeCaseModal").modal('hide'); });
  });

  // Parameter filter -> reload list
  $(function(){
    $("#parameter").on('change', function(){
      var selectedValue = $(this).val();
      var categoryId = '${categoryId}';
      $.ajax({
        url:'/checkLoginStatus',
        method:'get',
        async:false,
        success:function(result){
          const myJSON = JSON.parse(result);
          if(result=='true'){
            $("#dataListDiv").empty();
            $("#loader").show();
            setTimeout(function(){
              $("#dataListDiv").load('/fo/rejected_update_summary_data_list?id='+categoryId+'&parameterId='+selectedValue, function(response, status){
                $("#loader").hide();
                if(status=='success'){ console.log("success"); } else { console.log("failed"); }
              });
            }, 1000);
          } else if(result=='false'){ window.location.reload(); }
        }
      });
    });
  });

  // DataTable (presentation only; no logic changes)
  $(function(){
    $("#dataListTable").DataTable({
      responsive:true,
      lengthChange:false,
      autoWidth:false,
      buttons:["excel","pdf","print","colvis"]
    }).buttons().container().appendTo('#dataListTable_wrapper .col-md-6:eq(0)');
  });
</script>
</body>
</html>
