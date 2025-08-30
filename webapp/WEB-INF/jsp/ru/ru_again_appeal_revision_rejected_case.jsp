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

<div class="modal fade" id="appealRevisonAfterRejectionModal" tabindex="-1" role="dialog" aria-labelledby="appealRevisonAfterRejectionModal" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
    	<div id="appealRevisionRejectedRemarksMissingTagLine" style="color: red;display: none;padding-left:10px;padding-top:10px;">Please enter remarks.</div>
    	<div id="appealRevisionRejectedRadioOptionTagLine" style="color: red;display: none;padding-left:10px;padding-top:10px;">Please select appeal or revision.</div>
    	<div id="allFieldsEmptyRejectedTagLine" style="color: red;display: none;padding-left:10px;padding-top:10px;">Please select the fields.</div>
    	<div id="fileNotAttachedRejectedTagLine" style="color: red;display: none;padding-left:10px;padding-top:10px;">Please select the file.</div>
    	<div id="fileMaxSizeRejectedTagLine" style="color: red;display: none;padding-left:10px;padding-top:10px;">File max size can be 10 MB.</div>   
       <div class="modal-header">
        <h5 class="modal-title" id="appealRevisionRejectedModalTitle"><b>Appeal/Revision</b></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">    
          <span aria-hidden="true">&times;</span>
        </button>
       </div>
	    
      <div class="modal-body">
        <form method="POST" name="caseUpdateForm" id="appealRevisionRejectedDetails" action="ru_once_again_appeal_revision_rejected_case" enctype="multipart/form-data">
	  <div class="card-body">
	  	<div class="row">
	  		<div class="col-md-3">
				<div class="form-group">
					<label for="appeal">Appeal
						<input type="radio" name="appealRevisionRejected" value="ruAppeal" id="appealRejectedSelected"> 
					 </label>
				</div>
			</div>
			<div class="col-md-3">
				<div class="form-group">
					<label for="revision">Revision
						<input type="radio" name="appealRevisionRejected" value="ruRevision" id="revisionRejectedSelected"> 
					 </label>
				</div>
			</div>
		</div>
		
		<div class="row">
	  		<div class="col-md-12">
				<div class="form-group">
					<label for="message-text" >Remarks <span style="color: red">*</span></label>
            		<textarea class="form-control" id="appealRevisionRejectedRemarks" name="appealRevisionRejectedRemarks"></textarea>
				</div>
			</div>
		</div>
		
		
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
				<label for="excelFile">File Upload <span style="color: red">*</span></label><span> (Upload only pdf file with max file size of 10 MB ) </span>
                   <input class="form-control" type="file" id="appealRevisionRejectedFile"  name="appealRevisionRejectedFile" accept=".pdf"  >
				</div>
			</div>
		</div>
		
	  </div> 
      
      <input type="hidden" id="appRevRejGstiNo" name="appRevRejGstiNo" >
      <input type="hidden" id="appRevRejReportingdate" name="appRevRejReportingdate" >
      <input type="hidden" id="appRevRejPeriod" name="appRevRejPeriod" >
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
      
      </form>
        
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="appealRevisionRejectedCase"  onclick="submitAppealRevisionRejectedCase()"  >Submit</button>
      </div>
    </div>
  </div>
</div>

<script>

function submitAppealRevisionRejectedCase(){
	
	var appealRejectionRadioOptionSelected = $("input[name='appealRevisionRejected']:checked").val();
	var appealRevisionRemarksVal = $("#appealRevisionRejectedRemarks").val();
	var appealRevisionFileAttached = $("#appealRevisionRejectedFile")[0].files.length;
	var maxSizeInBytes = 10 * 1024 * 1024; // 2 MB
	var appealRevisionFileAttachedSize;
	
	if(appealRevisionFileAttached != 0){
		var fileInput = $('#appealRevisionRejectedFile')[0];
		appealRevisionFileAttachedSize = fileInput.files[0].size; // Size in bytes
	}
	else{
		appealRevisionFileAttachedSize = 0;
	}
	
	
	if((appealRejectionRadioOptionSelected === undefined) && (appealRevisionRemarksVal == "") && (appealRevisionFileAttached == 0) ){
		$("#appealRevisionRejectedRadioOptionTagLine").css("display", "none");  // appealRevisionRadioOptionTagLine
		$("#appealRevisionRejectedRemarksMissingTagLine").css("display", "none"); // appealRevisionRemarksMissingTagLine
		$("#fileNotAttachedRejectedTagLine").css("display", "none");				// fileNotAttachedTagLine
		$("#fileMaxSizeRejectedTagLine").css("display", "none");		// fileMaxSizeTagLine
		
		 $("#allFieldsEmptyRejectedTagLine").css("display", "block");	// allFieldsEmptyTagLine
	    	return ;
	}
	if(appealRejectionRadioOptionSelected === undefined){
		$("#allFieldsEmptyRejectedTagLine").css("display", "none");
		$("#appealRevisionRejectedRemarksMissingTagLine").css("display", "none");
		$("#fileNotAttachedRejectedTagLine").css("display", "none");
		$("#fileMaxSizeRejectedTagLine").css("display", "none");
		
		$("#appealRevisionRejectedRadioOptionTagLine").css("display", "block");
		return ;
	}
	if(appealRevisionRemarksVal == ""){
		$("#allFieldsEmptyRejectedTagLine").css("display", "none");
		$("#appealRevisionRejectedRadioOptionTagLine").css("display", "none");
		$("#fileNotAttachedRejectedTagLine").css("display", "none");
		$("#fileMaxSizeRejectedTagLine").css("display", "none");
		
		$("#appealRevisionRejectedRemarksMissingTagLine").css("display", "block");
		return;
	}
	if(appealRevisionFileAttached == 0){
		$("#allFieldsEmptyRejectedTagLine").css("display", "none");
		$("#appealRevisionRejectedRadioOptionTagLine").css("display", "none");
		$("#appealRevisionRejectedRemarksMissingTagLine").css("display", "none");
		$("#fileMaxSizeRejectedTagLine").css("display", "none");
		
		$("#fileNotAttachedRejectedTagLine").css("display", "block");
		return;
	}
	if(appealRevisionFileAttachedSize > maxSizeInBytes){
		$("#allFieldsEmptyRejectedTagLine").css("display", "none");
		$("#appealRevisionRejectedRadioOptionTagLine").css("display", "none");
		$("#appealRevisionRejectedRemarksMissingTagLine").css("display", "none");
		$("#fileNotAttachedRejectedTagLine").css("display", "none");
		
		$("#fileMaxSizeRejectedTagLine").css("display", "block");
		return;
	}
	
	
	
	if(appealRejectionRadioOptionSelected == "ruAppeal"){
    	  $.confirm({
  			title : 'Confirm!',
  			content : 'Do you want to proceed ahead with appealing the case ?',
  			buttons : {
  				submit : function() {
  	    $('#appealRevisonModal').modal('hide');
  	    document.getElementById("caseAppealedAlertSuccess").style.display = "block";
  	    document.getElementById("caseAppealedTagLine").style.display = "block";
      
  		    setTimeout(function() {
  		    document.getElementById("appealRevisionRejectedDetails").submit();
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
    			content : 'Do you want to proceed ahead with revision the case ?',
    			buttons : {
    				submit : function() {
    	    $('#appealRevisonModal').modal('hide');
    	    document.getElementById("caseRevisionAlertSuccess").style.display = "block";
		    document.getElementById("caseRevisionTagLine").style.display = "block";
        
    		    setTimeout(function() {
    		    document.getElementById("appealRevisionRejectedDetails").submit();
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




