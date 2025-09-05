<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
  .btn-outline-custom{color:#495057;border:1px solid #ced4da;text-align:left;}
</style>

<script>
// basic hardening (same as yours)
document.addEventListener('contextmenu', e=>e.preventDefault());
document.addEventListener('keydown', e=>{ if((e.ctrlKey&&e.key==='u')||e.key==='F12') e.preventDefault(); });
document.onkeydown = e => { if (e.key==='F5'||(e.ctrlKey&&e.key==='r')||e.keyCode===116) e.preventDefault(); };
$(document).ready(function(){ function disableBack(){window.history.forward()} window.onload=disableBack; window.onpageshow=e=>{if(e.persisted)disableBack()} });

// toggle "Other" textarea
function showHideOtherRemark(){
  const selected = $('input[name="remarkOptions"]:checked').val();
  const $ta = $("#remarksId");
  $ta.val('');
  if(selected==='other'){ $ta.show().prop('required',true).focus(); }
  else { $ta.hide().prop('required',false); }
}

// optional opener to prefill hidden fields before showing the modal
function openRecommendModal(gst, date, period){
  $("#rec_gst").val(gst || '');
  $("#rec_date").val(date || '');
  $("#rec_period").val(period || '');
  $("#transferModal").modal('show');
}

$(function(){
  // submit with basic validation
  $("#transferCaseBtn").on("click", function(){
    const $form = $("#recommendForm");
    const selected = $('input[name="remarkOptions"]:checked').val();
    const otherTxt = ($("#remarksId").is(":visible") ? $("#remarksId").val().trim() : "");
    if(!selected){
      alert("Please select a remark option.");
      return;
    }
    if(selected==='other' && otherTxt.length===0){
      alert("Please enter remarks in the text area.");
      $("#remarksId").focus();
      return;
    }
    // prevent double submit
    $(this).prop("disabled", true);
    $form.trigger("submit");
  });

  // enforce max length as you type (fallback for older browsers)
  $("#remarksId").on("input", function(){
    const max = 500;
    if(this.value.length > max) this.value = this.value.slice(0,max);
  });
});
</script>

<div class="modal fade" id="transferModal" tabindex="-1" role="dialog" aria-labelledby="transferModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">

      <form id="recommendForm" method="POST" action="/fo/recommend_case">
        <div class="modal-header">
          <h5 class="modal-title" id="transferModalTitle">Recommend</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span>&times;</span></button>
        </div>

        <div class="modal-body">
          <div class="form-group mb-2">
            <label class="col-form-label">Action:</label>
            <input type="text" class="form-control" value="Recommend" readonly>
          </div>

          <div class="form-group mb-1">
            <label class="col-form-label">Remarks <span style="color:red">*</span></label>
          </div>

          <!-- Preset remark options -->
          <div class="btn-group btn-group-vertical w-100" role="group" data-toggle="buttons">
            <c:choose>
              <c:when test="${not empty listRemarks}">
                <c:forEach items="${listRemarks}" var="r">
                  <label class="btn btn-outline-custom text-start" style="font-weight:400;">
                    <input type="radio" name="remarkOptions" value="${r.id}" autocomplete="off" onclick="showHideOtherRemark()"> ${r.name}
                  </label>
                </c:forEach>
              </c:when>
              <c:otherwise>
                <!-- Fallback static options if listRemarks is empty -->
                <label class="btn btn-outline-custom text-start"><input type="radio" name="remarkOptions" value="sufficient" onclick="showHideOtherRemark()"> Information sufficient</label>
                <label class="btn btn-outline-custom text-start"><input type="radio" name="remarkOptions" value="need-followup" onclick="showHideOtherRemark()"> Needs follow-up</label>
              </c:otherwise>
            </c:choose>

            <!-- "Other" option -->
            <label class="btn btn-outline-custom text-start">
              <input type="radio" name="remarkOptions" value="other" onclick="showHideOtherRemark()"> Other (specify)
            </label>
          </div>

          <!-- Free-text remarks (hidden until "Other" is chosen) -->
          <textarea class="form-control mt-2" id="remarksId" name="remarksText" placeholder="Enter remarks (max 500 chars)" maxlength="500" style="display:none"></textarea>
        </div>

        <!-- Hidden context + CSRF -->
        <input type="hidden" id="rec_gst" name="gstno" value="">
        <input type="hidden" id="rec_date" name="date" value="">
        <input type="hidden" id="rec_period" name="period" value="">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
          <button type="button" class="btn btn-primary" id="transferCaseBtn">Submit</button>
        </div>
      </form>

    </div>
  </div>
</div>
