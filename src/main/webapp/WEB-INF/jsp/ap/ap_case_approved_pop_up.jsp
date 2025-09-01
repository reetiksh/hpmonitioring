<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
  .btn-outline-custom {
  color: #495057;
  border: 1px solid #ced4da;
  text-align:left;
  }
  .popup {
  display: none;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  padding: 20px;
  background-color: #fff;
  border: 1px solid #ccc;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 999;
  }
  .overlayap {
    display: none;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 998;
  }
</style>

<script>
document.addEventListener('contextmenu', function(e) {
	e.preventDefault();
});
document.addEventListener('keydown', function(e) {
	if (e.ctrlKey && e.key === 'u') {
		e.preventDefault();
	}
});
document.addEventListener('keydown', function(e) {
    if (e.key === 'F12') {
        e.preventDefault();
    }
});
// Disable back and forward cache
$(document).ready(function () {
    function disableBack() {window.history.forward()}

    window.onload = disableBack();
    window.onpageshow = function (evt) {if (evt.persisted) disableBack()}
});
// Disable refresh
document.onkeydown = function (e) {
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) {
        e.preventDefault();
        
    }
};
  function openPopup() {
    document.getElementById("myPopupap").style.display = "block";
    document.getElementById("overlayap").style.display = "block";
  }

  function closePopupap() {
    document.getElementById("myPopupap").style.display = "none";
    document.getElementById("overlayap").style.display = "none";
  }

  function onOkap() {
    document.getElementById("commonBoostrapAlertSuccess").style.display = "block";
    document.getElementById("caseApprovedTagLine").style.display = "block";
    setTimeout(function() {
      document.getElementById("approverRemarksDetails").submit();
    }, 500);
    
    $('#recommendedModal').modal('hide');
    var popupContainer = document.getElementById('recommendedModal');
    if (popupContainer) {
        popupContainer.style.display = 'none';
    }
    closePopupap();
  }

      function onCancelap() {
          closePopupap();
      }

	function showHideOtherRecommendedRemark(){
    $('#apApprovedRemarksTagLine').css('display', 'none');
    $('#apApprovedRemarksValueTagLine').css('display', 'none');
    var otherRemarksValue = $('#remarksIdForRecommended').val();
		var selectedValue = $('input[name="remarkOptions"]:checked').val();
    $("#apApproveRemarks").val(selectedValue);
        if(selectedValue == 4){
          $("#remarksIdForRecommended").show();
        }else{
          $("#remarksIdForRecommended").hide();
        }
        
	}
	
  function validateForm(){
    var caseAssignedTo = $("#caseAssignedTo").val();
    var remarkOptions = $('input[name="remarkOptions"]:checked').val();
    var otherRemarks = $("#remarksId").val().trim();
    if(caseAssignedTo != '' & remarkOptions > 0){
        if(remarkOptions == 4){
          if(otherRemarks != ''){
            return true;
          }else{
              alert("Please fill require information");
            return false;
          }
        }else{
            return true;
        }
    }else{
      alert("Please fill require information");
        return false;
    }
	}
	
	function submitApproverRemarksForm(){
    var selectedValue = $('input[name="remarkOptions"]:checked').val();
    var otherRemarksValue = $('#remarksIdForRecommended').val();
    if((selectedValue == 4) && otherRemarksValue == ''){
      $('#apApprovedRemarksValueTagLine').css('display', 'block');
      return;
    }
    if(selectedValue === undefined){
      $('#myPopupap').hide();
      $('#apApprovedRemarksTagLine').css('display', 'block');
    }
    else{
      openPopup();
    }
	<!-- $("#verifierRemarksDetails").submit(); -->
	}
	

function submitApproverDeclaration(){
  $(document).ready(function() {
    $("#closeCaseModal").modal('hide');
  });
  document.getElementById("commonBoostrapAlertSuccess").style.display = "block";
  document.getElementById("caseApprovedTagLine").style.display = "block";
  setTimeout(function() {
      document.getElementById("approverRemarksDetails").submit();
    }, 300);
}

</script>


<div class="modal fade" id="recommendedModal" tabindex="-1" role="dialog" aria-labelledby="recommendedModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
       <div class="modal-header">
       <h5 class="modal-title" id="transferModalTitle"><b>Approve</b></h5>
       <button type="button" class="close" data-dismiss="modal" aria-label="Close">    
       <span aria-hidden="true">&times;</span>
       </button>
       </div>
	    <div class="popup" id="myPopupap">
	        <p>Are You Sure to Approve the following case ?</p>
	        <button onclick="onOkap()">OK</button>
	        <button onclick="onCancelap()">Cancel</button>
	    </div>
       <div class="modal-body">
        <form method="POST" id="approverRemarksDetails" action="approved_cases_with_remarks">
          <input type="hidden" id="gstinnoap" name="gstinno" >
          <input type="hidden" id="circleap" name="circle" >
          <input type="hidden" id="reportingdateap" name="reportingdate" >
          <input type="hidden" id="periodap" name="period" >
          <input type="hidden" id="apApproveRemarks" name="apApproveRemarks" >
          <input type="hidden" id="actionStatusAp" name="actionStatus" >
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> 
          <div id="apApprovedRemarksValueTagLine" style="color: red;display: none;">Please Enter the Remarks !</div>
          <div id="apApprovedRemarksTagLine" style="color: red;display: none;">Please select the Remarks !</div>
          <div class="form-group" style="margin-bottom: 0rem;">
            <label for="message-text" class="col-form-label">Remarks
            	<span style="color: red;"> *</span>
            </label>
          </div>
          <div class="btn-group btn-group-vertical" role="group" data-toggle="buttons" style="width: 100%;">
            <c:forEach items="${approverRemarksToApproveTheCaseList}" var="object">
          	<label class="btn btn-outline-custom" style="font-weight: 400;">
			        <input type="radio" name="remarkOptions" id="${object.name}" onclick="showHideOtherRecommendedRemark()" value="${object.id}" > ${object.name}
			      </label>
        	</c:forEach>
        	</div>
        	<textarea class="form-control" id="remarksIdForRecommended" name="otherRemarks" style="display: none" placeholder="Remarks"></textarea>
		    </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="transferCaseBtn" onclick="submitApproverRemarksForm()">Submit</button>
      </div>
    </div>
  </div>
</div>

