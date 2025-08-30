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

<script>

 		function openPopup() {
            document.getElementById("myPopup").style.display = "block";
            document.getElementById("overlay").style.display = "block";
        }

        function closePopup() {
            document.getElementById("myPopup").style.display = "none";
            document.getElementById("overlay").style.display = "none";
        }

        function onOk() {
            document.getElementById("commonBoostrapAlertSuccess").style.display = "block";
            document.getElementById("caseRecommendedTagLine").style.display = "block";
            setTimeout(function() {
            	document.getElementById("verifierRemarksDetails").submit();
            }, 300);
            
            $('#recommendedModal').modal('hide');
            var popupContainer = document.getElementById('recommendedModal');
            if (popupContainer) {
                popupContainer.style.display = 'none';
            }
            closePopup();
        }

        function onCancel() {
            closePopup();
        }


        function submitVerifierDeclaration(){
          $(document).ready(function() {
            $("#closeCaseModal").modal('hide');
          });
          document.getElementById("commonBoostrapAlertSuccess").style.display = "block";
          document.getElementById("caseRecommendedTagLine").style.display = "block";
          setTimeout(function() {
            	document.getElementById("verifierRemarksDetails").submit();
            }, 300);
        }


	function showHideOtherRecommendedRemark(){
    $('#selectRemarksTagLine').css('display', 'none');
    $('#selectRemarksValueTagLine').css('display', 'none');
		var selectedValue = $('input[name="remarkOptions"]:checked').val();
    $("#remarks").val(selectedValue);
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
	
	function submitVerifierRemarksForm(){
    

    var otherRemarksValue = $('#remarksIdForRecommended').val();
    var selectedValue = $('input[name="remarkOptions"]:checked').val();
    
    if((selectedValue == 4) && otherRemarksValue == ''){
      $('#selectRemarksValueTagLine').css('display', 'block');
      return;
    }


    if(selectedValue === undefined){
      $('#myPopup').hide();
      $('#selectRemarksTagLine').css('display', 'block');
    }
    else{
      $('#selectRemarksTagLine').css('display', 'none');
      openPopup();
    }

	
	<!-- $("#verifierRemarksDetails").submit(); -->
	}
	

</script>


<div class="modal fade" id="recommendedModal" tabindex="-1" role="dialog" aria-labelledby="recommendedModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
       <div class="modal-header">
       <h5 class="modal-title" id="transferModalTitle"><b>Recommend</b></h5>
       <button type="button" class="close" data-dismiss="modal" aria-label="Close">    
       <span aria-hidden="true">&times;</span>
       </button>
       </div>
	    <div class="popup" id="myPopup">
	        <p>Are You Sure to Recommend The Following Case ?</p>
	        <button onclick="onOk()">OK</button>
	        <button onclick="onCancel()">Cancel</button>
	    </div>
       
       
       <div class="modal-body">
        <form method="POST" id="verifierRemarksDetails" action="appeal_revision_recommended_case">
         <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> 
         <input type="hidden" id="gstinno" name="gstinno" >
          <input type="hidden" id="circle" name="circle" >
          <input type="hidden" id="reportingdate" name="reportingdate" >
          <input type="hidden" id="period" name="period" >
          <input type="hidden" id="remarks" name="remarks" value="1">
          
          
          <div id="selectRemarksValueTagLine" style="color: red;display: none;">Please Enter the Remarks !</div>
          <div id="selectRemarksTagLine" style="color: red;display: none;">Please select the Remarks !</div>
          <div class="form-group" style="margin-bottom: 0rem;">
            <label for="message-text" class="col-form-label">Remarks
            	<span style="color: red;"> *</span>
            </label>
          </div>
          
          <div class="btn-group btn-group-vertical" role="group" data-toggle="buttons" style="width: 100%;">
            <c:forEach items="${verifierRemarksModelList}" var="object">
          	<label class="btn btn-outline-custom" style="font-weight: 400;">
			    <input type="radio" name="remarkOptions" id="${object.name}" onclick="showHideOtherRecommendedRemark()" value="${object.id}" > ${object.name}
			  </label>
        	</c:forEach>
        	</div>
        	<textarea class="form-control" id="remarksIdForRecommended" name="otherRemarks" style="display: none" placeholder="Remarks"></textarea>
          
		</form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="transferCaseBtn" onclick="submitVerifierRemarksForm()">Submit</button>
      </div>
    </div>
  </div>
</div>

