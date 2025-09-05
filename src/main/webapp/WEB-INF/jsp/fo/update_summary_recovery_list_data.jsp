<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<style>
  .modal-lg, .modal-xl { max-width: 1400px; }
  .table-responsive { overflow: scroll; max-height: 800px; }
  .btn-outline-custom {
    color: #495057;
    border: 1px solid #ced4da;
    text-align: left;
  }
</style>

<script>
  // no logic changes — just grouped the hardening handlers
  document.addEventListener('contextmenu', function(e){ e.preventDefault(); });
  document.addEventListener('keydown', function(e){
    if ((e.ctrlKey && e.key === 'u') || e.key === 'F12') e.preventDefault();
  });
  $(document).ready(function(){
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = function(evt){ if (evt.persisted) disableBack(); }
  });
  document.onkeydown = function(e){
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) e.preventDefault();
  };

  // unchanged logic: toggle free-text remarks only when value == 1
  function showHideOtherRemark(){
    var selectedValue = $('input[name="remarks"]:checked').val();
    if (selectedValue == 1){
      $("#remarksId").show();
    } else {
      $("#remarksId").hide();
    }
  }

  // unchanged logic: load modal after login status check
  function view(gst, date, period){
    $.ajax({
      url: '/checkLoginStatus',
      method: 'get',
      async: false,
      success: function(result){
        const myJSON = JSON.parse(result);
        if (result == 'true'){
          $("#updateSummaryViewBody").load(
            '/fo/view_recovery_case/id?gst=' + gst + '&date=' + date + '&period=' + period,
            function(response, status){
              if (status == 'success'){
                $("#updateSummaryViewModal").modal('show');
              } else {
                console.log('failed');
              }
            }
          );
        } else if (result == 'false'){
          window.location.reload();
        }
      }
    });
  }

  // unchanged helpers for other (unrelated) modal flows
  $(function(){
    $('#mycheckbox').change(function(){
      if (this.checked) $("#checked").show(); else $("#checked").hide();
    });

    $("#updateSummaryClosedCaseBtn").on("click", function(){
      $("#confirmationModal").modal('show');
    });

    $("#updateSummaryViewBtn").on("click", function(){
      $.ajax({
        url: '/checkLoginStatus',
        method: 'get',
        async: false,
        success: function(result){
          const myJSON = JSON.parse(result);
          if (result == 'true'){
            $("#updateSummaryViewBody").load('//fo/view_case', function(_, status){
              if (status == 'success'){
                $("#updateSummaryViewModal").modal('show');
              } else {
                console.log('failed');
              }
            });
          } else if (result == 'false'){
            window.location.reload();
          }
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

<div class="row">
  <table id="dataListTable" class="table table-bordered table-striped table-responsive">
    <thead>
      <tr>
        <th style="text-align:center;vertical-align:middle;">GSTIN</th>
        <th style="text-align:center;vertical-align:middle;">Taxpayer Name</th>
        <th style="text-align:center;vertical-align:middle;">Jurisdiction</th>
        <th style="text-align:center;vertical-align:middle;">Period</th>
        <th style="text-align:center;vertical-align:middle;">Reporting Date<br>(DD-MM-YYYY)</th>
        <th style="text-align:center;vertical-align:middle;">Indicative Value(₹)</th>
        <th style="text-align:center;vertical-align:middle;">Action Status</th>
        <th style="text-align:center;vertical-align:middle;">Case ID</th>
        <th style="text-align:center;vertical-align:middle;">Case Stage</th>
        <th style="text-align:center;vertical-align:middle;">Case Stage ARN</th>
        <th style="text-align:center;vertical-align:middle;">Amount(₹)</th>
        <th style="text-align:center;vertical-align:middle;">Recovery Stage</th>
        <th style="text-align:center;vertical-align:middle;">Recovery Stage ARN</th>
        <th style="text-align:center;vertical-align:middle;">Recovery Via DRC03(₹)</th>
        <th style="text-align:center;vertical-align:middle;">Recovery Against Demand(₹)</th>
        <th style="text-align:center;vertical-align:middle;">Parameter</th>
        <th style="text-align:center;vertical-align:middle;">Case File</th>
        <th style="text-align:center;vertical-align:middle;">Action</th>
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
          <td>
            <c:if test="${item.uploadedFileName != null}">
              <a href="/fo/downloadUploadedPdfFile?fileName=${item.uploadedFileName}">
                <button type="button" class="btn btn-primary" aria-label="Download case PDF">
                  <i class="fa fa-download"></i>
                </button>
              </a>
            </c:if>
          </td>
          <td>
            <button type="button" style="margin-bottom:5px;" class="btn btn-primary viewbtn"
                    onclick="view('${item.GSTIN_ID}','${item.caseReportingDate_ID}','${item.period_ID}')">
              Update
            </button>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
</div>

<!-- Update modal (body loaded via AJAX) -->
<div class="modal fade" id="updateSummaryViewModal" tabindex="-1" role="dialog"
     aria-labelledby="updateSummaryViewTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-lg" role="document" aria-modal="true">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="updateSummaryViewModalTitle">Update Recovery Case</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body" id="updateSummaryViewBody"></div>
    </div>
  </div>
</div>
