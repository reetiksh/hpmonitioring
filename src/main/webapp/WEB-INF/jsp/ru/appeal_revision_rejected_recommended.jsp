<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
  .btn-outline-custom { color:#495057; border:1px solid #ced4da; text-align:left; }

  .popup {
    display:none; position:fixed; top:50%; left:50%; transform:translate(-50%,-50%);
    padding:20px; background:#fff; border:1px solid #ccc; box-shadow:0 4px 8px rgba(0,0,0,.1); z-index:999;
  }
  .overlay {
    display:none; position:fixed; inset:0; background:rgba(0,0,0,.5); z-index:998;
  }
</style>

<script>
  // --- Cross-compatible modal hide (Bootstrap 5 or jQuery plugin) ---
  function hideModal(modalId) {
    var el = document.getElementById(modalId);
    if (window.bootstrap && bootstrap.Modal) {
      bootstrap.Modal.getOrCreateInstance(el).hide();
    } else if (window.jQuery && $(el).modal) {
      $(el).modal('hide');
    } else {
      el.classList.remove('show');
      el.style.display = 'none';
      document.body.classList.remove('modal-open');
      document.querySelectorAll('.modal-backdrop').forEach(b=>b.remove());
    }
  }

  function openPopup() {
    document.getElementById("myPopup").style.display = "block";
    document.getElementById("overlay").style.display = "block";
  }
  function closePopup() {
    document.getElementById("myPopup").style.display = "none";
    document.getElementById("overlay").style.display = "none";
  }

  function onOk() {
    document.getElementById("commonBoostrapAlertSuccess")?.style && (document.getElementById("commonBoostrapAlertSuccess").style.display = "block");
    document.getElementById("caseRecommendedTagLine")?.style && (document.getElementById("caseRecommendedTagLine").style.display = "block");
    setTimeout(function(){ document.getElementById("verifierRemarksDetails").submit(); }, 300);

    hideModal('recommendedModal');
    closePopup();
  }
  function onCancel(){ closePopup(); }

  function submitVerifierDeclaration(){
    hideModal('closeCaseModal');
    document.getElementById("commonBoostrapAlertSuccess")?.style && (document.getElementById("commonBoostrapAlertSuccess").style.display = "block");
    document.getElementById("caseRecommendedTagLine")?.style && (document.getElementById("caseRecommendedTagLine").style.display = "block");
    setTimeout(function(){ document.getElementById("verifierRemarksDetails").submit(); }, 300);
  }

  function showHideOtherRecommendedRemark(){
    document.getElementById('selectRemarksTagLine').style.display = 'none';
    document.getElementById('selectRemarksValueTagLine').style.display = 'none';

    var selectedValue = document.querySelector('input[name="remarkOptions"]:checked')?.value;
    document.getElementById("remarks").value = selectedValue || '';
    var other = document.getElementById("remarksIdForRecommended");
    if (selectedValue === '4') {
      other.style.display = 'block';
    } else {
      other.style.display = 'none';
      other.value = '';
    }
  }

  function submitVerifierRemarksForm(){
    var otherRemarksValue = document.getElementById('remarksIdForRecommended').value;
    var selected = document.querySelector('input[name="remarkOptions"]:checked');

    if (selected && selected.value === '4' && !otherRemarksValue.trim()){
      document.getElementById('selectRemarksValueTagLine').style.display = 'block';
      return;
    }

    if (!selected){
      document.getElementById('myPopup').style.display = 'none';
      document.getElementById('selectRemarksTagLine').style.display = 'block';
      return;
    }
    document.getElementById('selectRemarksTagLine').style.display = 'none';
    openPopup();
  }
</script>

<!-- Custom confirm overlay for your existing logic -->
<div class="overlay" id="overlay"></div>

<div class="modal fade" id="recommendedModal" tabindex="-1" aria-labelledby="recommendedModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="recommendedModalTitle"><b>Recommend</b></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <!-- Your custom popup confirmation -->
      <div class="popup" id="myPopup">
        <p>Are You Sure to Recommend The Following Case ?</p>
        <button type="button" class="btn btn-primary btn-sm" onclick="onOk()">OK</button>
        <button type="button" class="btn btn-secondary btn-sm" onclick="onCancel()">Cancel</button>
      </div>

      <div class="modal-body">
        <form method="POST" id="verifierRemarksDetails" action="appeal_revision_recommended_case">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
          <input type="hidden" id="gstinno" name="gstinno">
          <input type="hidden" id="circle" name="circle">
          <input type="hidden" id="reportingdate" name="reportingdate">
          <input type="hidden" id="period" name="period">
          <input type="hidden" id="remarks" name="remarks" value="1">

          <div id="selectRemarksValueTagLine" style="color:red; display:none;">Please Enter the Remarks !</div>
          <div id="selectRemarksTagLine" style="color:red; display:none;">Please select the Remarks !</div>

          <div class="form-group mb-2">
            <label class="col-form-label">Remarks <span style="color:red;">*</span></label>
          </div>

          <div class="btn-group btn-group-vertical w-100" role="group" data-toggle="buttons">
            <c:forEach items="${verifierRemarksModelList}" var="object">
              <label class="btn btn-outline-custom" style="font-weight:400;">
                <input type="radio" name="remarkOptions" id="${object.name}" value="${object.id}" onclick="showHideOtherRecommendedRemark()"> ${object.name}
              </label>
            </c:forEach>
          </div>

          <textarea class="form-control mt-2" id="remarksIdForRecommended" name="otherRemarks" style="display:none" placeholder="Remarks"></textarea>
        </form>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="transferCaseBtn" onclick="submitVerifierRemarksForm()">Submit</button>
      </div>
    </div>
  </div>
</div>
