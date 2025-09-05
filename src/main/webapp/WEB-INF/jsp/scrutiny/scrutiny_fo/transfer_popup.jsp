<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
<script src="/static/dist/js/jquery-confirm.min.js"></script>
<style>
  .btn-outline-custom { color:#495057; border:1px solid #ced4da; text-align:left; }
</style>

<script>
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => { if (e.ctrlKey && e.key === 'u') e.preventDefault(); });
  document.addEventListener('keydown', e => { if (e.key === 'F12') e.preventDefault(); });
  $(function () {
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = e => { if (e.persisted) disableBack(); }
  });
  document.onkeydown = function(e){
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) e.preventDefault();
  };

  function showHideOtherRemark(){
    var selectedValue = $('input[name="remarkOptions"]:checked').val();
    if(selectedValue == 1){ $("#remarksId").show(); } else { $("#remarksId").hide(); }
  }

  function centerCase(val){
    var circle = $("#jurisdiction").val();
    if(circle == val){
      alert("Please choose different jurisdiction");
      $("#caseAssignedTo").val('');
    }else{
      if(val == 'NC'){
        $("#file").show();
        $("#uploadedFile").prop("required", true);
        $("input[value='2']").parent().show();
        $("input[value='1']").parent().hide();
        $("input[value='3']").parent().hide();
      }else{
        $("#file").hide();
        $("#uploadedFile").prop("required", false);
        $("input[value='2']").parent().hide();
        $("input[value='1']").parent().show();
        $("input[value='3']").parent().show();
      }
    }
  }
</script>

<!-- Transfer Case Modal -->
<div class="modal fade" id="transferModal" tabindex="-1" aria-labelledby="transferModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">

      <div class="modal-header">
        <h5 class="modal-title" id="transferModalTitle">Transfer case</h5>
        <!-- BS5 close button -->
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <form method="POST" name="TransferForm" id="TransferForm" action="request_for_transfer" enctype="multipart/form-data">
        <div class="modal-body">

          <div class="form-group">
            <label for="caseAssignedTo" class="col-form-label">Case Assigned To
              <span style="color:red;"> *</span>
            </label>
            <select class="form-control selectpicker" id="caseAssignedTo" name="caseAssignedTo"
                    data-live-search="true" onChange="centerCase(this.value)" title="Please Select" required>
              <c:forEach items="${circls}" var="circls">
                <option value="${circls.locationId}">${circls.locationName}</option>
              </c:forEach>
            </select>
          </div>

          <input type="hidden" id="gstno" name="gstno">
          <input type="hidden" id="date" name="date">
          <input type="hidden" id="period" name="period">
          <input type="hidden" id="jurisdiction" name="jurisdiction">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

          <div class="form-group mb-0">
            <label class="col-form-label">Remarks <span style="color:red;">*</span></label>
          </div>

          <div class="btn-group btn-group-vertical w-100" role="group" data-toggle="buttons">
            <c:forEach items="${transferRemarks}" var="transferRemarks">
              <label class="btn btn-outline-custom" style="font-weight:400;">
                <input type="radio" name="remarkOptions" id="${transferRemarks.id}"
                       onclick="showHideOtherRemark()" value="${transferRemarks.id}" required>
                ${transferRemarks.name}
              </label>
            </c:forEach>
          </div>

          <textarea class="form-control" id="remarksId" name="otherRemarks" style="display:none" placeholder="Remarks"></textarea>

          <div class="form-group" id="file" style="display:none;">
            <label for="uploadedFile" class="col-form-label">
              Upload File <span style="color:red;">*</span>
              <span>(Upload only pdf file with max file size of 10 MB)</span>
            </label>
            <input type="file" class="form-control" id="uploadedFile" name="uploadedFile" accept=".pdf"/>
          </div>

        </div>

        <!-- Confirmation Nested Modal (kept; just BS5 close) -->
        <div class="modal fade" id="confirmationTransferModal" tabindex="-1"
             aria-labelledby="confirmationTransferModalTitle" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">

              <div class="modal-header">
                <h5 class="modal-title" id="confirmationTransferModalTitle">Are you sure want to submit</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
              </div>

              <div class="modal-footer">
                <button type="submit" class="btn btn-primary" id="okBtn">Okay</button>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" id="cancelBtn">Cancel</button>
              </div>

            </div>
          </div>
        </div>

        <div class="modal-footer">
          <!-- keeps existing submit behavior -->
          <button type="submit" class="btn btn-primary" id="transferCaseBtn">Transfer</button>
        </div>

      </form>
    </div>
  </div>
</div>
