<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- AdminLTE 4 / Bootstrap 5 / Font Awesome -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.5.2/css/all.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/css/adminlte.min.css" rel="stylesheet">

<!-- Bootstrap-Select (BS5 compatible) -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.14.0-beta3/dist/css/bootstrap-select.min.css" rel="stylesheet">

<!-- DataTables (Bootstrap 5 build) -->
<link href="https://cdn.datatables.net/v/bs5/dt-1.13.8/r-2.5.0/b-2.4.2/datatables.min.css" rel="stylesheet"/>

<style>
.modal-lg, .modal-xl { max-width: 1400px; }
.table-responsive { overflow: scroll; max-height: 800px; }
.btn-outline-custom { color:#495057; border:1px solid #ced4da; text-align:left; }
</style>

<script>
    function showHideOtherRemark(){
        var selectedValue = $('input[name="remarks"]:checked').val();
        if(selectedValue == 1){
            $("#remarksId").show();
        }else{
            $("#remarksId").hide();
        }
    }

    $(function() {
        $('.selectpicker').selectpicker();
    });
</script>

<div class="row">
    <c:if test="${!empty parameters}">
        <div class="col-md-6 form-group">
            <label class="col-md-2">Parameter<span style="color: red;"> *</span></label>
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
    <div class="col-md-12">
        <table id="example1" class="table table-bordered table-striped">
            <thead>
                <tr>
                    <th style="text-align: center; vertical-align: middle;">GSTIN</th>
                    <th style="text-align: center; vertical-align: middle;">Taxpayer Name</th>
                    <th style="text-align: center; vertical-align: middle;">Jurisdiction</th>
                    <th style="text-align: center; vertical-align: middle;">Period</th>
                    <th style="text-align: center; vertical-align: middle;">Reporting Date<br>(DD-MM-YYYY)</th>
                    <th style="text-align: center; vertical-align: middle;">Indicative Value(₹)</th>
                    <th style="text-align: center; vertical-align: middle;">Action Status</th>
                    <th style="text-align: center; vertical-align: middle;">Case ID</th>
                    <th style="text-align: center; vertical-align: middle;">Case Stage</th>
                    <th style="text-align: center; vertical-align: middle;">Case Stage ARN</th>
                    <th style="text-align: center; vertical-align: middle;">Amount(₹)</th>
                    <th style="text-align: center; vertical-align: middle;">Recovery Stage</th>
                    <th style="text-align: center; vertical-align: middle;">Recovery Stage ARN</th>
                    <th style="text-align: center; vertical-align: middle;">Recovery Via DRC03(₹)</th>
                    <th style="text-align: center; vertical-align: middle;">Recovery Against Demand(₹)</th>
                    <th style="text-align: center; vertical-align: middle;">Parameter</th>
                    <th style="text-align: center; vertical-align: middle;">Review Comments</th>
                    <th style="text-align: center; vertical-align: middle;">Case File</th>
                    <th style="text-align: center; vertical-align: middle;">Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${listofCases}" var="item">
                <tr>
                    <td>${item.GSTIN_ID}</td>
                    <td>${item.taxpayerName}</td>
                    <td>${item.circle}</td>
                    <td>${item.period_ID}</td>
                    <td><fmt:formatDate value="${item.date}"  pattern="dd-MM-yyyy" /></td>
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
                    <td>
                    <c:if test="${item.uploadedFileName != null}">
                        <a href="/foa/downloadUploadedPdfFile?fileName=${item.uploadedFileName}">
                            <button type="button" class="btn btn-primary"><i class="fa-solid fa-download"></i></button>
                        </a>
                    </c:if>
                    </td>
                    <td>
                        <button type="button" style="margin-bottom:5px;" class="btn btn-primary viewbtn" onclick="view('${item.GSTIN_ID}' , '${item.caseReportingDate_ID}' , '${item.period_ID}')">Update</button>

                        <c:choose>
                            <c:when test="${item.actionStatus eq '3' && item.caseStage eq '5'}">
                                <button type="button" class="btn btn-primary" onclick="closeCase('${item.GSTIN_ID}' , '${item.caseReportingDate_ID}' , '${item.period_ID}')">Close</button>
                            </c:when>
                            <c:when test="${item.actionStatus eq '3' && item.caseStage eq '6'}">
                                <button type="button" class="btn btn-primary" onclick="closeCase('${item.GSTIN_ID}' , '${item.caseReportingDate_ID}' , '${item.period_ID}')">Close</button>
                            </c:when>
                            <c:when test="${item.actionStatus eq '3' && item.caseStage eq '8'}">
                                <button type="button" class="btn btn-primary" onclick="closeCase('${item.GSTIN_ID}' , '${item.caseReportingDate_ID}' , '${item.period_ID}')">Close</button>
                            </c:when>
                            <c:when test="${item.actionStatus eq '3' && item.caseStage eq '9'}">
                                <button type="button" class="btn btn-primary" onclick="closeCase('${item.GSTIN_ID}' , '${item.caseReportingDate_ID}' , '${item.period_ID}')">Close</button>
                            </c:when>
                            <c:otherwise>
                                <button type="button" class="btn btn-primary" disabled onclick="closeCase('${item.GSTIN_ID}' , '${item.caseReportingDate_ID}' , '${item.period_ID}')">Close</button>
                            </c:otherwise>
                        </c:choose>

                        <c:if test="${item.caseIdUpdate eq 'yes'}">
                            <button type="button" style="margin-bottom:5px;margin-top:5px;" class="btn btn-primary" onclick="updateCaseId('${item.GSTIN_ID}' , '${item.caseReportingDate_ID}' , '${item.period_ID}' , '${item.caseId}')">Update Case ID</button>
                        </c:if>
                    </td>
                </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<!-- Update Case Modal (BS5 markup) -->
<div class="modal fade" id="updateSummaryViewModal" tabindex="-1" aria-labelledby="updateSummaryViewModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="updateSummaryViewModalTitle">Update Case </h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body" id="updateSummaryViewBody"></div>
    </div>
  </div>
</div>

<!-- Close Case Modal (BS5 markup) -->
<div class="modal fade" id="closeCaseModal" tabindex="-1" aria-labelledby="closeCaseModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
     <form method="POST" action="fo_close_cases" name="closeCaseForm">
      <div class="modal-header">
        <h5 class="modal-title" id="confirmationModalTitle">Do you want to proceed ahead with closure of the case?</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

      <div class="modal-body">
        <p>I hereby undertake that;</p>
        <p><i class="fa-solid fa-check"></i> All the points raised by EIU pertaining to this Taxpayer have been dealt carefully in the light of the provisions of the GST laws</p>
        <p><i class="fa-solid fa-check"></i> Applicable Tax has been levied as per the provisions of GST acts/rules 2017</p>
        <p><i class="fa-solid fa-check"></i> Applicable Interest has been levied as per the provisions of GST acts/rules 2017</p>
        <p><i class="fa-solid fa-check"></i> Applicable Penalty has been levied as per the provisions of GST acts/rules 2017</p>
        <p><input type="checkbox" id="mycheckbox" name="checkbox"> I hereby declare that above information is true and correct to the best of my knowledge</p>
      </div>

      <input type="hidden" id="gstno" name="gstno">
      <input type="hidden" id="date" name="date">
      <input type="hidden" id="period" name="period">

      <div class="modal-footer">
        <div id="checked" style="display:none">
          <button type="submit" class="btn btn-primary" id="okayBtn">Okay</button>
        </div>
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" id="closeBtn">Cancel</button>
      </div>
     </form>
    </div>
  </div>
</div>

<!-- Update Case ID Modal (BS5 markup) -->
<div class="modal fade" id="updateCaseidModal" tabindex="-1" aria-labelledby="updateCaseidModal" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="modalTitle">Update Case ID</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <form method="POST" name="updatecaseidForm" id="updatecaseidForm" action="update_caseid" enctype="multipart/form-data">
      <div class="modal-body">

          <input type="hidden" id="gstnocaseid" name="gstnocaseid">
          <input type="hidden" id="datecaseid" name="datecaseid">
          <input type="hidden" id="periodcaseid" name="periodcaseid">

          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

          <div class="row">
          <div class="col-md-12">
                <div class="form-group">
                    <label for="gstin">Old Case ID</label>
                    <input class="form-control" type="text" id="caseid" readonly />
                </div>
           </div>
           </div>

           <div class="row">
           <div class="col-md-12">
                <div class="form-group">
                    <label for="gstin">New Case ID Proposed<span style="color: red;"> *</span></label>
                    <input class="form-control" maxlength="15" pattern="[A-Za-z0-9]{15}" id="newcaseid" name="caseid" title="Please enter 15-digits alphanumeric Case Id" required />
                </div>
           </div>
           </div>

           <div class="form-group" style="margin-bottom: 0rem;">
             <label for="message-text" class="col-form-label">Remarks<span style="color: red;"> *</span></label>
           </div>
           <div class="btn-group btn-group-vertical" role="group" data-toggle="buttons" style="width: 100%;">
              <c:forEach items="${listRemarks}" var="item">
                  <label class="btn btn-outline-custom">
                    <input type="radio" name="remarks" id="${item.id}" onclick="showHideOtherRemark()" value="${item.id}" required> ${item.name}
                  </label>
              </c:forEach>
            </div>
            <textarea class="form-control" id="remarksId" name="otherRemarks" style="display: none" placeholder="Remarks"></textarea>

            <div class="form-group" style="margin-bottom: 0rem;">
              <label for="message-text" class="col-form-label">Upload File<span style="color: red;"> *</span><span>(Only pdf files of size upto 10mb)</span></label>
            </div>

            <input type="file" id="filePath" name="filePath" accept="pdf" required />

        </div>

      <div class="modal-footer">
        <button type="submit" class="btn btn-primary" id="transferCaseBtn">Submit</button>
      </div>
      </form>
    </div>
  </div>
</div>

<!-- JS: jQuery -> Bootstrap 5 -> AdminLTE 4 -> DataTables -> bootstrap-select -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/js/adminlte.min.js"></script>

<script src="https://cdn.datatables.net/v/bs5/dt-1.13.8/r-2.5.0/b-2.4.2/datatables.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.14.0-beta3/dist/js/bootstrap-select.min.js"></script>

<!-- jQuery ↔ Bootstrap 5 modal bridge (so $('#id').modal('show') still works) -->
<script>
  (function ($) {
    $.fn.modal = function (action) {
      return this.each(function () {
        const inst = bootstrap.Modal.getOrCreateInstance(this);
        if (action === 'show') inst.show();
        else if (action === 'hide') inst.hide();
      });
    };
  })(jQuery);
</script>

<script>
document.addEventListener('contextmenu', function(e) { e.preventDefault(); });
document.addEventListener('keydown', function(e) { if (e.ctrlKey && e.key === 'u') { e.preventDefault(); }});
document.addEventListener('keydown', function(e) { if (e.key === 'F12') { e.preventDefault(); }});
// Disable back and forward cache
$(document).ready(function () {
    function disableBack() { window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = function (evt) { if (evt.persisted) disableBack(); }
});
// Disable refresh
document.onkeydown = function (e) {
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) { e.preventDefault(); }
};

function view(gst, date, period){
  $.ajax({url: '/checkLoginStatus', method: 'get', async: false,
    success: function(result){
      const myJSON = JSON.parse(result);
      if(result=='true'){
        $("#updateSummaryViewBody").load('/foa/view_case/id?gst='+gst+'&date='+date+'&period='+period,
          function(response, status, xhr){
            if(status == 'success'){ $("#updateSummaryViewModal").modal('show'); }
            else { console.log("failed"); }
          });
      } else if(result=='false'){ window.location.reload(); }
    }
  });
}

function closeCase(gstno , date, period){
  $("#gstno").val(gstno);
  $("#date").val(date);
  $("#period").val(period);
  $("#closeCaseModal").modal('show');
}

function updateCaseId(gstno , date, period, caseid){
  $("#gstnocaseid").val(gstno);
  $("#datecaseid").val(date);
  $("#periodcaseid").val(period);
  $("#caseid").val(caseid);
  $("#updateCaseidModal").modal('show');
}
</script>

<script>
$(function () {

  $('#mycheckbox').change(function(){
    if(this.checked) { $("#checked").show(); } else { $("#checked").hide(); }
  });

  $("#updateSummaryClosedCaseBtn").on("click", function(){
    $("#confirmationModal").modal('show');
  });

  $("#updateSummaryViewBtn").on("click", function(){
    $.ajax({url: '/checkLoginStatus', method: 'get', async: false,
      success: function(result){
        const myJSON = JSON.parse(result);
        if(result=='true'){
          $("#updateSummaryViewBody").load('//foa/view_case', function(response, status, xhr){
            if(status == 'success'){ $("#updateSummaryViewModal").modal('show'); }
            else { console.log("failed"); }
          });
        } else if(result=='false'){ window.location.reload(); }
      }
    });
  });

  $("#okayBtn").on("click", function(){
    console.log("save");
    $("#closeCaseModal").modal('hide');
  });

  $("#closeBtn").on("click", function(){
    console.log("failed");
    $("#closeCaseModal").modal('hide');
  });

});
</script>

<script>
    $('#updatecaseidForm').on('submit', function(oEvent) {
        debugger;
        oEvent.preventDefault();

        var oldCaseId = document.querySelector('#caseid').value.trim();
        var newCaseId = document.querySelector('#newcaseid').value.trim();

        $.confirm({
            title : 'Confirm!',
            content : 'Do you want to proceed ahead to request case id updation?',
            buttons : {
                submit : function() {

                    var fileName = document.querySelector('#filePath').value;
                    var extension = fileName.slice((fileName.lastIndexOf(".") - 1 >>> 0) + 2);
                    var input = document.getElementById('filePath');

                    if (input.files && input.files[0]) {

                        var maxAllowedSize = 10 * 1024 * 1024;

                        if(extension == 'pdf'){

                            if(input.files[0].size > maxAllowedSize) {

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
                close : function() {
                    $.alert('Canceled!');
                }
             }
        });

    });

    $(function() {
        $("#example1").DataTable({
          "responsive": false, "lengthChange": false, "autoWidth": true, "scrollX": true
        }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
    });
</script>

<script>
    // parameter change handler exactly as-is
    $(function() {
        $("#parameter").on('change',function() {
            var selectedValue = $(this).val();
            var categoryId = '${categoryId}';
            if(categoryId == '13'){
                $.ajax({url: '/checkLoginStatus', method: 'get', async: false,
                    success: function(result){const myJSON = JSON.parse(result);
                        if(result=='true'){
                            $("#dataListDiv").empty();
                            $("#loader").show();
                            setTimeout(function() {
                                $("#dataListDiv").load('/foa/get_scrutiny_data_list?id=' + categoryId + '&parameterId=' + selectedValue, function(response, status, xhr) {
                                    $("#loader").hide();
                                    if (status == 'success') {
                                        console.log("success");
                                    } else {
                                        console.log("failed");
                                    }
                                });
                            }, 1000);
                        }
                        else if(result=='false'){
                            window.location.reload();
                        }
                    }
                });
            }
            else{
                $.ajax({url: '/checkLoginStatus', method: 'get', async: false,
                    success: function(result){
                      const myJSON = JSON.parse(result);
                      if(result=='true'){
                        $("#dataListDiv").empty();
                        $("#loader").show();
                        setTimeout(function() {
                          $("#dataListDiv").load('/foa/update_summary_data_list?id=' + categoryId + '&parameterId=' + selectedValue,
                            function(response, status, xhr) {
                              $("#loader").hide();
                              if (status == 'success') { console.log("success"); }
                              else { console.log("failed"); }
                            });
                        }, 1000);
                      }
                      else if(result=='false'){
                        window.location.reload();
                      }
                    }
                });
            }
        });
    });
</script>
