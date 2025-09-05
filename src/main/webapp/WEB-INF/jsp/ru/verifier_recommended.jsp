<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
  .btn-outline-custom {
    color: #495057;
    border: 1px solid #ced4da;
    text-align: left;
  }
  /* Lightweight confirm popup + overlay (kept as-is, logic unchanged) */
  .popup {
    display: none; position: fixed; top: 50%; left: 50%;
    transform: translate(-50%, -50%);
    padding: 20px; background-color: #fff; border: 1px solid #ccc;
    box-shadow: 0 4px 8px rgba(0,0,0,.1); z-index: 1056; /* above BS modal backdrop */
    width: min(480px, 90vw); border-radius: .5rem;
  }
  .popup .actions { display:flex; gap:.5rem; justify-content:flex-end; margin-top:1rem; }
  .overlay {
    display: none; position: fixed; inset: 0; background-color: rgba(0,0,0,.5); z-index: 1055;
  }
</style>

<!-- Recommend Modal (AdminLTE 4 / Bootstrap 5 markup) -->
<div class="modal fade" id="recommendedModal" tabindex="-1" aria-labelledby="recommendedModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">

      <div class="modal-header">
        <h5 class="modal-title" id="recommendedModalTitle"><b>Recommend</b></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <!-- Lightweight inline confirm popup (kept by request) -->
      <div class="overlay" id="overlay"></div>
      <div class="popup" id="myPopup">
        <p class="mb-0">Are you sure you want to recommend the following case?</p>
        <div class="actions">
          <button type="button" class="btn btn-primary btn-sm" onclick="onOk()">OK</button>
          <button type="button" class="btn btn-secondary btn-sm" onclick="onCancel()">Cancel</button>
        </div>
      </div>

      <div class="modal-body">
        <form method="POST" id="verifierRemarksDetails" action="recommended_closure_with_remarks">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
          <input type="hidden" id="gstinno" name="gstinno">
          <input type="hidden" id="circle" name="circle">
          <input type="hidden" id="reportingdate" name="reportingdate">
          <input type="hidden" id="period" name="period">
          <input type="hidden" id="remarks" name="remarks" value="1">

          <div id="selectRemarksValueTagLine" class="text-danger" style="display:none;">Please enter the remarks!</div>
          <div id="selectRemarksTagLine" class="text-danger" style="display:none;">Please select the remarks!</div>

          <div class="form-group mb-2">
            <label class="col-form-label">Remarks <span class="text-danger">*</span></label>
          </div>

          <div class="btn-group-vertical w-100" role="group" data-toggle="buttons">
            <c:forEach items="${verifierRemarksModelList}" var="object">
              <label class="btn btn-outline-custom mb-2" style="font-weight:400;">
                <input type="radio"
                       name="remarkOptions"
                       id="${object.name}"
                       onclick="showHideOtherRecommendedRemark()"
                       value="${object.id}"> ${object.name}
              </label>
            </c:forEach>
          </div>

          <textarea class="form-control"
                    id="remarksIdForRecommended"
                    name="otherRemarks"
                    placeholder="Remarks"
                    style="display:none"></textarea>
        </form>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="transferCaseBtn" onclick="submitVerifierRemarksForm()">Submit</button>
      </div>
    </div>
  </div>
</div>

<script>
  /* Hardening (kept) */
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
  (function () {
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack;
    window.onpageshow = function (evt){ if (evt.persisted) disableBack(); };
  })();

  /* Popup helpers (unchanged logic) */
  function openPopup(){
    document.getElementById("myPopup").style.display = "block";
    document.getElementById("overlay").style.display = "block";
  }
  function closePopup(){
    document.getElementById("myPopup").style.display = "none";
    document.getElementById("overlay").style.display = "none";
  }

  function onOk(){
    // Show success tags on parent page (ids expected to exist elsewhere)
    const ok1 = document.getElementById("commonBoostrapAlertSuccess");
    const ok2 = document.getElementById("caseRecommendedTagLine");
    if (ok1) ok1.style.display = "block";
    if (ok2) ok2.style.display = "block";

    // Submit shortly after UI feedback
    setTimeout(function(){ document.getElementById("verifierRemarksDetails").submit(); }, 300);

    // Hide BS5 modal
    const modalEl = document.getElementById('recommendedModal');
    const instance = bootstrap.Modal.getOrCreateInstance(modalEl);
    instance.hide();

    closePopup();
  }
  function onCancel(){ closePopup(); }

  function submitVerifierDeclaration(){
    // Kept as-is: show success and submit
    const ok1 = document.getElementById("commonBoostrapAlertSuccess");
    const ok2 = document.getElementById("caseRecommendedTagLine");
    if (ok1) ok1.style.display = "block";
    if (ok2) ok2.style.display = "block";
    setTimeout(function(){ document.getElementById("verifierRemarksDetails").submit(); }, 300);
  }

  function showHideOtherRecommendedRemark(){
    document.getElementById('selectRemarksTagLine').style.display = 'none';
    document.getElementById('selectRemarksValueTagLine').style.display = 'none';

    const selected = document.querySelector('input[name="remarkOptions"]:checked');
    const remarksHidden = document.getElementById("remarks");
    const otherBox = document.getElementById("remarksIdForRecommended");

    if (selected){
      remarksHidden.value = selected.value;
      if (selected.value == '4'){ // keep original condition
        otherBox.style.display = 'block';
      } else {
        otherBox.value = '';
        otherBox.style.display = 'none';
      }
    }
  }

  function validateForm(){
    // Original logic kept; ids referenced here are preserved
    const caseAssignedTo = document.getElementById("caseAssignedTo") ? document.getElementById("caseAssignedTo").value : '';
    const selected = document.querySelector('input[name="remarkOptions"]:checked');
    const remarkOptions = selected ? selected.value : '';
    const other = document.getElementById("remarksId") ? document.getElementById("remarksId").value.trim() : '';

    if (caseAssignedTo !== '' & remarkOptions > 0){
      if (remarkOptions == 4){
        if (other !== '') return true;
        alert("Please fill require information"); return false;
      }
      return true;
    }
    alert("Please fill require information"); return false;
  }

  function submitVerifierRemarksForm(){
    const selected = document.querySelector('input[name="remarkOptions"]:checked');
    const otherBox = document.getElementById('remarksIdForRecommended');

    // Require text when "Other" selected
    if (selected && selected.value == '4' && otherBox.value.trim() === ''){
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
