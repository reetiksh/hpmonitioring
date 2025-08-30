<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
  .btn-outline-custom {
    color: #495057;
    border: 1px solid #ced4da;
    text-align: left;
  }

  .popuprej {
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

  /* Style for the overlay/background */
  .overlayrej {
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


</script>

<div class="modal fade" id="appealRevisonRejectModal" tabindex="-1" role="dialog" aria-labelledby="appealRevisonRejectTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
    <div id="appealRevisionRejectRemarksMissingTagLine" style="color: red;display: none;padding-left:10px;padding-top:10px;">Please enter remarks.</div>
      <div class="modal-header">
        <h5 class="modal-title" id="appealRevisonRejectModalTitle"><b>Revert</b></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      
      <div class="modal-body">
        <form method="POST" id="appealRevisionRejectCaseDetails" action="reject_appeal_revision_case">
          <input type="hidden" id="appealRevisionRejectGstin" name="appealRevisionRejectGstin">
          <input type="hidden" id="appealRevisionRejectPeriod" name="appealRevisionRejectPeriod">
          <input type="hidden" id="appealRevisionRejectCaseReportingDate" name="appealRevisionRejectCaseReportingDate">
          <input type="hidden" id="appealRevisionRejectFilePath" name="appealRevisionRejectFilePath">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
          
          <div id="appealRevisionRejectRemarksValueTagLine" style="color: red;display: none;">Please enter remarks !</div>
          
          <div class="form-group" style="margin-bottom: 0rem;">
            <label for="message-text" class="col-form-label">Remarks
              <span style="color: red;"> *</span>
            </label>
          </div>
          <textarea class="form-control" id="appealRevisionRejectRemarksValue" name="appealRevisionRejectRemarksValue"  placeholder="Remarks"></textarea>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="transferCaseBtn"
          onclick="submitAppealRevisionRejectDetails()">Submit</button>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
function submitAppealRevisionRejectDetails(){
	var appealRevisionRejectRemarksVal = $("#appealRevisionRejectRemarksValue").val();
	if(appealRevisionRejectRemarksVal != ""){
		$("#appealRevisionRejectRemarksValue").val(appealRevisionRejectRemarksVal);
	   	  $.confirm({
	  			title : 'Confirm!',
	  			content : "Do you want to proceed ahead with rejection of verifier's recommendation ?", 
	  			buttons : {
	  				submit : function() {
	  	    $('#appealRevisonRejectModal').modal('hide');
	  	  document.getElementById("appealRevisionCaseRejectedAlertFail").style.display = "block";
	  	    document.getElementById("appealRevisionCaseRejectedTagLine").style.display = "block";
	  	  	    setTimeout(function() {
	  		    document.getElementById("appealRevisionRejectCaseDetails").submit();
	  		}, 300);
	  				},
	  				close : function() {
	  					$.alert('Canceled!');
	  				}
	  			}
	  		});
	}
	else{
		$("#appealRevisionRejectRemarksMissingTagLine").css("display", "block");
		return;
	}
}
</script>
