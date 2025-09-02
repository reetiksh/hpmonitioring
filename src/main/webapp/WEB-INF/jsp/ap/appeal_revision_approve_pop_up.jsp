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

        /* Style for the overlay/background */
        .overlay {
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
<head>
  <!-- <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script> -->
  <script src="/static/dist/js/jquery-3.6.4.min.js"></script>
</head>

<div class="modal fade" id="appealRevisonApproveModal" tabindex="-1" role="dialog" aria-labelledby="appealRevisionApproveModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
    	<div id="appealRevisionApproveRemarksMissingTagLine" style="color: red;display: none;padding-left:10px;padding-top:10px;">Please enter remarks.</div>
    	<div id="appealRevisionApproveRadioOptionTagLine" style="color: red;display: none;padding-left:10px;padding-top:10px;">Please select appeal or revision.</div>
    	<div id="allFieldsEmptyApproveTagLine" style="color: red;display: none;padding-left:10px;padding-top:10px;">Please select the fields.</div>
    	  
       <div class="modal-header">
        <h5 class="modal-title" id="appealRevisionApproveModalTitle"><b>Appeal/Revision</b></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
       </div>
	    
      <div class="modal-body">
        <form method="POST" name="caseUpdateForm" id="appealRevisionApproveDetails" action="approve_appeal_revision_case">
	  <div class="card-body">
	  	<div class="row">
	  		<div class="col-md-3">
				<div class="form-group">
					<label for="appeal">Appeal
						<input type="radio" name="appealRevisionApproved" value="apApproveAppeal" id="appealSelected"> 
					 </label>
				</div>
			</div>
			<div class="col-md-3">
				<div class="form-group">
					<label for="revision">Revision
						<input type="radio" name="appealRevisionApproved" value="apApproveRevision" id="revisionSelected"> 
					 </label>
				</div>
			</div>
		</div>
		
		<div class="row">
	  		<div class="col-md-12">
				<div class="form-group">
					<label for="message-text" >Remarks <span style="color: red">*</span></label>
            		<textarea class="form-control" id="appealRevisionApproveRemarks" name="appealRevisionApproveRemarks"></textarea>
				</div>
			</div>
		</div>
		
		</div> 
      
      <input type="hidden" id="appRevApproveGstiNo" name="appRevApproveGstiNo" >
      <input type="hidden" id="appRevApproveReportingdate" name="appRevApproveReportingdate" >
      <input type="hidden" id="appRevApprovePeriod" name="appRevApprovePeriod" >
      <input type="hidden" id="appealRevisionApproveFilePath" name="appealRevisionApproveFilePath">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
      
      </form>
        
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="appealRevisionApproveCase"  onclick="submitAppealRevisionApproveDetails()" >Submit</button>
      </div>
    </div>
  </div>
</div>

<script>

function submitAppealRevisionApproveDetails(){
	
	var appealRejectionApproveRadioOptionSelected = $("input[name='appealRevisionApproved']:checked").val();
	var appealRevisionApproveRemarksVal = $("#appealRevisionApproveRemarks").val();
	
	if((appealRejectionApproveRadioOptionSelected === undefined) && (appealRevisionApproveRemarksVal == "")){ 
		$("#appealRevisionApproveRadioOptionTagLine").css("display", "none");  // appealRevisionRadioOptionTagLine
		$("#appealRevisionApproveRemarksMissingTagLine").css("display", "none"); // appealRevisionRemarksMissingTagLine
		$("#allFieldsEmptyApproveTagLine").css("display", "block");	// allFieldsEmptyTagLine
    	return ;
	}
	if(appealRejectionApproveRadioOptionSelected === undefined){
		$("#allFieldsEmptyApproveTagLine").css("display", "none");
		$("#appealRevisionApproveRemarksMissingTagLine").css("display", "none");
		$("#appealRevisionApproveRadioOptionTagLine").css("display", "block");
		return ;
	}
	if(appealRevisionApproveRemarksVal == ""){
		$("#allFieldsEmptyApproveTagLine").css("display", "none");
		$("#appealRevisionApproveRadioOptionTagLine").css("display", "none");
		$("#appealRevisionApproveRemarksMissingTagLine").css("display", "block");
		return;
	}
	
	
	
	
	
	if(appealRejectionApproveRadioOptionSelected == "apApproveAppeal"){
    	  $.confirm({
  			title : 'Confirm!',
  			content : 'Do you want to proceed ahead with recommendation of this case for appeal?',
  			buttons : {
  				submit : function() {
  	    $('#appealRevisonApproveModal').modal('hide');
  	    document.getElementById("appealRevisionCaseApprovedAlertSuccess").style.display = "block";
  	    document.getElementById("appealRevisionCaseApprovedTagLine").style.display = "block";
      
  		    setTimeout(function() {
  		    document.getElementById("appealRevisionApproveDetails").submit();
  		}, 300);
  				},
  				close : function() {
  					$.alert('Canceled!');
  				}
  			}
  		});
      }else{
    	  $.confirm({
    			title : 'Confirm!',
    			content : 'Do you want to proceed ahead with recommendation of this case for revision?',
    			buttons : {
    				submit : function() {
    	    $('#appealRevisonApproveModal').modal('hide');
    	    document.getElementById("appealRevisionCaseApprovedAlertSuccess").style.display = "block";
		    document.getElementById("appealRevisionCaseApprovedTagLine").style.display = "block";
        
    		    setTimeout(function() {
    		    document.getElementById("appealRevisionApproveDetails").submit();
    		}, 300);
    				},
    				close : function() {
    					$.alert('Canceled!');
    				}
    			}
    		});
      }
}
</script>




