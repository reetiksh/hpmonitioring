<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<style>
  .btn-outline-custom{
    color:#495057;
    border:1px solid #ced4da;
    text-align:left;
  }
</style>

<script>
  // hardening (unchanged logic)
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    if ((e.ctrlKey && e.key === 'u') || e.key === 'F12') e.preventDefault();
  });
  $(function(){
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = evt => { if (evt.persisted) disableBack(); };
  });
  document.onkeydown = function(e){
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) e.preventDefault();
  };

  // keep exact behavior: show textarea only when radio value is "1"
  function showHideOtherRemark(){
    const sel = $('input[name="remarkOptions"]:checked').val();
    if (sel == 1){
      $("#remarksId").show();
    } else {
      $("#remarksId").hide().val('');
    }
  }

  // keep exact behavior: block same-jurisdiction; toggle file+remarks options when "NC"
  function centerCase(val){
    const circle = $("#jurisdiction").val();

    if (circle == val){
      alert("Please choose different jurisdiction");
      $("#caseAssignedTo").val('').change();
      return;
    }

    const fileDiv = document.getElementById("file");
    if (val === 'NC'){
      fileDiv.style.display = "block";
      $("#uploadedFile").prop("required", true);

      // hide value=1 & 3; show value=2
      $("input[value='1']").parent().hide();
      $("input[value='2']").parent().show();
      $("input[value='3']").parent().hide();
    } else {
      fileDiv.style.display = "none";
      $("#uploadedFile").prop("required", false).val('');

      // show value=1 & 3; hide value=2
      $("input[value='1']").parent().show();
      $("input[value='2']").parent().hide();
      $("input[value='3']").parent().show();
    }
  }

  // optional: initialize bootstrap-select if present (no logic change)
  $(function(){
    if ($.fn.selectpicker){ $('.selectpicker').selectpicker(); }
  });
</script>

<div class="modal fade" id="transferModal" tabindex="-1" role="dialog" aria-labelledby="transferModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document" aria-modal="true">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="transferModalTitle">Transfer case</h5>
        <button type="button" class="close" href="/acknowledge_cases" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>

      <form method="POST" name="TransferForm" id="TransferForm" action="fo_transfer_cases" enctype="multipart/form-data">
        <div class="modal-body">
          <div class="form-group">
            <label for="caseAssignedTo" class="col-form-label">Case Assigned To <span style="color:red;">*</span></label>
            <select class="form-control selectpicker" id="caseAssignedTo" name="caseAssignedTo"
                    data-live-search="true" onchange="centerCase(this.value)" title="Please Select" required>
              <c:forEach items="${circls}" var="circls">
                <option data-tokens="${circls.locationName}" value="${circls.locationId}">${circls.locationName}</option>
              </c:forEach>
            </select>
          </div>

          <!-- hidden fields -->
          <input type="hidden" id="gstno" name="gstno">
          <input type="hidden" id="date" name="date">
          <input type="hidden" id="period" name="period">
          <input type="hidden" id="jurisdiction" name="jurisdiction">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

          <div class="form-group mb-1">
            <label class="col-form-label">Remarks <span style="color:red;">*</span></label>
          </div>

          <!-- remark options -->
          <div class="btn-group btn-group-vertical w-100" role="group" data-toggle="buttons" aria-label="Remarks options">
            <c:forEach items="${transferRemarks}" var="transferRemarks">
              <label class="btn btn-outline-custom" style="font-weight:400;">
                <input type="radio" name="remarkOptions" id="${transferRemarks.id}"
                       onclick="showHideOtherRemark()" value="${transferRemarks.id}" required>
                ${transferRemarks.name}
              </label>
            </c:forEach>
          </div>

          <textarea class="form-control mt-2" id="remarksId" name="otherRemarks" style="display:none" placeholder="Remarks"></textarea>

          <!-- file (only for NC) -->
          <div class="form-group mt-3" id="file" style="display:none;">
            <label for="uploadedFile" class="col-form-label">Upload File <span style="color:red;">*</span></label>
            <input type="file" class="form-control" id="uploadedFile" name="uploadedFile" accept=".pdf">
          </div>
        </div>

        <!-- (kept) confirmation modal markup -->
        <div class="modal fade" id="confirmationTransferModal" tabindex="-1" role="dialog" aria-labelledby="confirmationTransferModalTitle" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="confirmationTransferModalTitle">Are you sure want to submit</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>

              <div class="modal-footer">
                <button type="submit" class="btn btn-primary" id="okBtn">Okay</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal" id="cancelBtn">Cancel</button>
              </div>
            </div>
          </div>
        </div>

        <div class="modal-footer">
          <button type="submit" class="btn btn-primary" id="transferCaseBtn">Transfer</button>
        </div>
      </form>
    </div>
  </div>
</div>
