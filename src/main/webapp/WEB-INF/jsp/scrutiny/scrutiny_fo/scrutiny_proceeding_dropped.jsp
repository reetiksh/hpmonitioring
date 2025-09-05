<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<style>
  .modal-lg, .modal-xl { max-width: 1000px; }
  .table-responsive { overflow: scroll; max-height: 800px; }
  .btn-outline-custom { color:#495057; border:1px solid #ced4da; text-align:left; }
</style>

<script>
  // --- Hardening (unchanged) ---
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
  $(function(){ function disableBack(){ window.history.forward(); }
    window.onload = disableBack; window.onpageshow = e => { if (e.persisted) disableBack(); };
  });

  // --- Helpers (BS5 modal) ---
  function hideModal(id){ const el=document.getElementById(id); const m=el && bootstrap.Modal.getInstance(el); if(m) m.hide(); }
  // format dd-mm-yyyy
  function formatTheDate(inputDate){
    const d=new Date(inputDate); const dd=String(d.getDate()).padStart(2,'0'); const mm=String(d.getMonth()+1).padStart(2,'0'); return `${dd}-${mm}-${d.getFullYear()}`;
  }
  // copy DRC-03 -> Amount
  function copyInputValueToAmount(){ document.getElementById("demand").value = document.getElementById("recoveryByDRC3").value; }

  // --- dynamic ARN rows ---
  $(function(){
    let counter = Number($("#arnLength").val() || 0);

    $("#addrow").on("click", function(){
      const idx = counter++;
      const $tr = $(`
        <tr>
          <td>
            <input type="text"
                   class="form-control recovery-arn"
                   name="recoveryStageArn[${idx}]"
                   placeholder="Please enter Recovery Stage ARN"
                   maxlength="15"
                   pattern="[A-Za-z0-9]{15}"
                   title="Please enter 15-digits alphanumeric Recovery Stage ARN"
                   required />
          </td>
          <td><input type="button" class="ibtnDel btn btn-md btn-danger" value="Delete"></td>
        </tr>
      `);
      $("table.order-list tbody").append($tr);
    });

    $("table.order-list").on("click", ".ibtnDel", function(){
      $(this).closest("tr").remove();
    });
  });

  // --- submit / validation ---
  function submitScrutinyCaseDropped(){
    const maxSize = 10 * 1024 * 1024; // 10MB
    const stage = $("#recoveryStage").val();
    const drc3  = $("#recoveryByDRC3").val().trim();
    const fileInput = document.getElementById('uploadedFile');
    const file = fileInput.files[0];

    // clear messages
    $("#recoveryStageTagLine,#drc03TagLine,#fileNotAttachedTagLine,#fileMaxSizeTagLine,#recveryStageArnTagLine").hide();

    // basic checks
    if(!stage){ $("#recoveryStageTagLine").show(); return; }
    if(!drc3){ $("#drc03TagLine").show(); return; }
    if(!file){ $("#fileNotAttachedTagLine").show(); return; }
    if(file.size > maxSize){ $("#fileMaxSizeTagLine").show(); return; }

    // ARN required when stage 2 or 3
    if(stage === '2' || stage === '3'){
      let anyEmpty = false;
      $(".recovery-arn").each(function(){ if(!$(this).val().trim()){ anyEmpty = true; } });
      if(anyEmpty){ $("#recveryStageArnTagLine").show(); return; }
    }

    $.confirm({
      title:'Confirm!',
      content:'Do you want to proceed ahead with dropping scrutiny proceedings?',
      buttons:{
        submit: function(){
          hideModal('scrutinyProceedingDroppedModal');
          // slight delay so modal animates out
          setTimeout(() => document.getElementById("scrutinyDroppedData").submit(), 100);
        },
        close: function(){ $.alert('Canceled!'); }
      }
    });
  }
</script>

<div class="modal fade" id="scrutinyProceedingDroppedModal" tabindex="-1" aria-labelledby="scrutinyProceedingDroppedModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-lg">
    <div class="modal-content">

      <div class="modal-header">
        <h5 class="modal-title" id="scrutinyProceedingDroppedModalTitle">Drop Scrutiny Proceedings</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <!-- inline validation messages -->
      <div id="recoveryStageTagLine" class="text-danger px-3 pt-2" style="display:none;">Please select recovery stage.</div>
      <div id="drc03TagLine" class="text-danger px-3 pt-2" style="display:none;">Please fill recovery via DRC03.</div>
      <div id="fileNotAttachedTagLine" class="text-danger px-3 pt-2" style="display:none;">Please select the file.</div>
      <div id="fileMaxSizeTagLine" class="text-danger px-3 pt-2" style="display:none;">Maximum allowable file size is 10 MB.</div>
      <div id="recveryStageArnTagLine" class="text-danger px-3 pt-2" style="display:none;">Please provide recovery stage ARN/reference no.</div>

      <div class="modal-body" id="updateSummaryViewBody">
        <form method="POST" action="scrutiny_initiated_dropped" name="scrutinyDroppedData" id="scrutinyDroppedData" enctype="multipart/form-data">

          <div class="row">
            <div class="col-md-4">
              <label class="form-label" for="gstin">GSTIN</label>
              <input class="form-control" id="gstin" name="gstin" value="${viewItem.GSTIN_ID}" readonly>
            </div>
            <div class="col-md-4">
              <label class="form-label" for="taxpayerName">Taxpayer Name</label>
              <input class="form-control" id="taxpayerName" name="taxpayerName" value="${viewItem.taxpayerName}" readonly>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <div class="col-md-4">
              <label class="form-label" for="circle">Jurisdiction</label>
              <input class="form-control" id="circle" name="circle" value="${viewItem.circle}" readonly>
            </div>
          </div>

          <div class="row mt-2">
            <div class="col-md-4">
              <label class="form-label" for="caseCategory">Case Category</label>
              <input class="form-control" id="caseCategory" name="caseCategory" value="${viewItem.category.name}" readonly>
            </div>
            <div class="col-md-4">
              <label class="form-label" for="period">Period</label>
              <input class="form-control" id="period" name="period" value="${viewItem.period_ID}" readonly>
            </div>
            <div class="col-md-4">
              <label class="form-label" for="casereportingdate">Reporting Date (DD-MM-YYYY)</label>
              <input class="form-control" id="casereportingdate" name="casereportingdate" value="<fmt:formatDate value='${viewItem.date}' pattern='dd-MM-yyyy' />" readonly>
            </div>

            <input type="hidden" id="caseReportingDate" name="caseReportingDate_ID" value="${viewItem.caseReportingDate_ID}">
            <input type="hidden" name="period_ID" value="${viewItem.period_ID}">
          </div>

          <div class="row mt-2">
            <div class="col-md-4">
              <label class="form-label" for="susIndicativeTax">Indicative Value(₹)</label>
              <input class="form-control" id="susIndicativeTax" name="indicativeTaxValue" value="${viewItem.indicativeTaxValue}" readonly>
            </div>

            <div class="col-md-4">
              <label class="form-label" for="recoveryStage">Recovery Stage <span class="text-danger">*</span></label>
              <select class="form-select" id="recoveryStage" name="recoveryStage" title="Please select Recovery Stage">
                <option value="" disabled selected>Select</option>
                <c:forEach items="${listRecovery}" var="categories">
                  <option value="${categories.id}" <c:if test="${categories.id == '4'}">disabled</c:if> <c:if test="${categories.id == submittedData.recoveryStage}">selected</c:if>>
                    ${categories.name}
                  </option>
                </c:forEach>
              </select>
            </div>

            <div class="col-md-4">
              <label class="form-label" for="demand">Amount(₹) <span class="text-danger">*</span></label>
              <input class="form-control" id="demand" name="demand" maxlength="11" inputmode="numeric" pattern="[0-9]*" value="${submittedData.demand}" readonly>
            </div>
          </div>

          <div class="row mt-2">
            <div class="col-md-4">
              <label class="form-label" for="recoveryByDRC3">Recovery Via DRC03(₹) <span class="text-danger">*</span></label>
              <input class="form-control" id="recoveryByDRC3" name="recoveryByDRC3" maxlength="11" inputmode="numeric" pattern="[0-9]*" value="${submittedData.recoveryByDRC3}" oninput="copyInputValueToAmount()">
            </div>

            <div class="col-md-4">
              <label class="form-label" for="uploadedFile">File Upload <span class="text-danger">*</span></label>
              <span class="ms-1 small">(upload only PDF up to 10MB)</span>
              <input class="form-control" type="file" id="uploadedFile" name="uploadedFile" accept=".pdf">
              <c:if test="${submittedData.sum eq 'fileexist'}">
                <a href="/fo/downloadUploadedFile?fileName=${submittedData.remarks}" class="btn btn-sm btn-primary mt-2"><i class="fa fa-download"></i></a>
              </c:if>
            </div>

            <c:if test="${not empty submittedData.recoveryStageArnStr}">
              <c:set var="strlst" value="${submittedData.recoveryStageArnStr}" />
              <c:set var="lst" value="${fn:split(strlst,',')}" />
            </c:if>
            <input type="hidden" id="arnLength" value="${fn:length(lst)}" />

            <div class="col-md-4">
              <label class="form-label">Recovery Stage ARN/Reference No <span class="text-danger">*</span></label>
              <table id="myTable" class="table order-list">
                <tbody>
                  <c:if test="${fn:length(lst) > 0}">
                    <c:forEach items="${lst}" varStatus="loop" var="lstItem">
                      <tr>
                        <td>
                          <input type="text"
                                 class="form-control recovery-arn"
                                 name="recoveryStageArn[${loop.index}]"
                                 value="${fn:trim(lstItem)}"
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
                </tbody>
                <tfoot>
                  <tr>
                    <td colspan="5" class="text-start">
                      <input type="button" class="btn btn-primary" id="addrow" value="Add Row" />
                    </td>
                  </tr>
                </tfoot>
              </table>
            </div>
          </div>

          <hr>

          <div class="d-flex justify-content-end">
            <button type="button" class="btn btn-primary" id="submitCase" onclick="submitScrutinyCaseDropped()">Submit</button>
          </div>
        </form>
      </div><!-- /.modal-body -->

    </div>
  </div>
</div>
