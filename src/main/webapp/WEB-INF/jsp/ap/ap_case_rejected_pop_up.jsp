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

$(document).ready(function() {
		
				
  $.ajax({
    url: '/ap/get_all_ap_revert_remarks', // Change this to your actual endpoint
    method: 'GET', // Change this to the appropriate HTTP method
    success: function(response) {
        // Assuming response is an array of objects with 'id' and 'name' properties
        var apRejectRemarksDiv = $('#apRejectRemarksDiv');
        var apRejectRemarks = $('#apRejectRemarks');
        apRejectRemarks.empty(); // Clear existing options

        // Loop through the response and append radio buttons
        $.each(response, function(index, object) {
            // Create a new label and input for each remark
            var label = $('<label>', {
              class: 'btn btn-outline-custom',
                style: 'font-weight: 400; display: block;',
                text: object.name
            });

            var input = $('<input>', {
                type: 'radio',
                name: 'remarkOptions',
                style: 'margin-right: 10px;',
                id: object.name,
                value: object.id,
                onclick: 'showHideOtherRemark()'
            });

            // Append the input to the label, and the label to the container
            label.prepend(input);
            apRejectRemarks.append(label);
        });

        apRejectRemarksDiv.show(); // Show the div with the radio buttons
    },
    error: function(xhr, status, error) {
        console.error('Error:', error);
        // Handle error if needed
    }
});

		
	});


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
  function showHideOtherRemark() {
    $('#apRejectRemarksTagLine').css('display', 'none');
    $('#apRejectRemarksValueTagLine').css('display', 'none');


    var selectedValue = $('input[name="remarkOptions"]:checked').val();
    $("#apRejectedRemarks").val(selectedValue);
    if (selectedValue == 1) {
      $("#remarksRaiseQueryId").show();
    } else {
      $("#remarksRaiseQueryId").hide();
    }
  }

  function openPopuprej() {
    // Display the popup and overlay
    // document.getElementById("myPopuprej").style.display = "block";
    // document.getElementById("overlayrqrej").style.display = "block";


    $.confirm({
      title: 'Confirm!',
      content: 'Do you want to proceed ahead with reverting the case?',
      buttons: {
        submit: function () {
          // oEvent.currentTarget.submit();

          // document.getElementById("raisequeryModal").style.display = "none";
          $('#raisequeryModal').modal('hide');
          document.getElementById("commonBoostrapAlertFail").style.display = "block";
          document.getElementById("caseRejectedTagLine").style.display = "block";
          setTimeout(function () {
            document.getElementById("approverRemarksDetailsForRejectedCase").submit();
          }, 300);
        },
        close: function () {
          $.alert('Canceled!');
        }
      }
    });
  }

  function closePopuprej() {
    // Hide the popup and overlay
    document.getElementById("myPopuprej").style.display = "none";
    document.getElementById("overlayrej").style.display = "none";
  }

  function onOkrej() {
    // Handle OK button click
    document.getElementById("commonBoostrapAlertFail").style.display = "block";
    document.getElementById("caseRejectedTagLine").style.display = "block";
    setTimeout(function () {
      document.getElementById("approverRemarksDetailsForRejectedCase").submit();
    }, 500);

    $('#raisequeryModal').modal('hide');
    var popupContainer = document.getElementById('raisequeryModal');
    if (popupContainer) {
      // Hide the popup by setting its display property to 'none'
      popupContainer.style.display = 'none';
    }
    closePopuprej();
  }

  function onCancelrq() {
    // Handle Cancel button click
    closePopuprej();
  }

  function submitVerifierRaiseQueryRemarksForm() {

    var selectedValue = $('input[name="remarkOptions"]:checked').val();
    var otherRemarksValue = $('#remarksRaiseQueryId').val();
    $('#apRejectedTextValueRemarks').val(otherRemarksValue);
    
    if(selectedValue === undefined){
    	$('#apRejectRemarksValueTagLine').css('display', 'none');
		$('#apRevertCasefileMaxSizeTagLine').css('display', 'none');
    	$('#apRejectRemarksTagLine').css('display', 'block');
    	
    	return;
    }
    
    if ((selectedValue == 1) && (otherRemarksValue == '')) {
    	$('#apRevertCasefileMaxSizeTagLine').css('display', 'none');
    	$('#apRejectRemarksTagLine').css('display', 'none');
    	$('#apRevertCaseFileTagLine').css('display', 'none');
      $('#apRejectRemarksValueTagLine').css('display', 'block');
      return;
    }
    var revertCaseFileAttached = $("#fileAttachedWhileRevert")[0].files.length;
	var maxSizeInBytes = 10 * 1024 * 1024; // 2 MB
	var revertCaseFileAttachedSize;
	
	if(revertCaseFileAttached != 0){
		var fileInput = $('#fileAttachedWhileRevert')[0];
		revertCaseFileAttachedSize = fileInput.files[0].size; // Size in bytes
	}
	else{
		revertCaseFileAttachedSize = 0;
	}
	
	if(revertCaseFileAttachedSize == 0){
		$('#apRejectRemarksTagLine').css('display', 'none');
		$('#apRejectRemarksValueTagLine').css('display', 'none');
		$('#apRevertCasefileMaxSizeTagLine').css('display', 'none');
		$('#apRevertCaseFileTagLine').css('display', 'block');
		return;
	}
	
	if(revertCaseFileAttachedSize > maxSizeInBytes){
		$('#apRejectRemarksTagLine').css('display', 'none');
		$('#apRejectRemarksValueTagLine').css('display', 'none');
		$('#apRevertCaseFileTagLine').css('display', 'none');
		$('#apRevertCasefileMaxSizeTagLine').css('display', 'block');
		
		return;
	}
	
	if (selectedValue === undefined) {
      $('#myPopuprej').hide();
      $('#apRejectRemarksTagLine').css('display', 'block');
    } else {
    	$('#apRejectRemarksTagLine').css('display', 'none');
		$('#apRejectRemarksValueTagLine').css('display', 'none');
		$('#apRevertCaseFileTagLine').css('display', 'none');
		$('#apRevertCasefileMaxSizeTagLine').css('display', 'none');
      openPopuprej();
    }



    <!-- $("#approverRemarksDetailsForRejectedCase").submit();-->
  }

</script>

<div class="modal fade" id="raisequeryModal" tabindex="-1" role="dialog" aria-labelledby="raisequeryModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="raiseQueryModalTitle"><b>Revert</b></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="popuprej" id="myPopuprej">
        <p>Are You Sure to Reject The Following Case ?</p>
        <button onclick="onOkrej()">OK</button>
        <button onclick="onCancelrq()">Cancel</button>
      </div>
      <div class="modal-body">
        <form method="POST" id="approverRemarksDetailsForRejectedCase" action="rejected_cases_with_remarks" enctype="multipart/form-data">
          <input type="hidden" id="gstinnoaprej" name="gstinno">
          <input type="hidden" id="circleaprej" name="circle">
          <input type="hidden" id="reportingdateaprej" name="reportingdate">
          <input type="hidden" id="periodaprej" name="period">
          <input type="hidden" id="apRejectedRemarks" name="apRejectedRemarks">
          <input type="hidden" id="apRejectedTextValueRemarks" name="apRejectedTextValueRemarks">
          <input type="hidden" id="actionStatusRej" name="actionStatus">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
          <div id="apRejectRemarksValueTagLine" style="color: red;display: none;">Please Enter the Remarks !</div>
          <div id="apRejectRemarksTagLine" style="color: red;display: none;">Please select Remarks !</div>
          <div id="apRevertCaseFileTagLine" style="color: red;display: none;">Please select file !</div>
          <div id="apRevertCasefileMaxSizeTagLine" style="color: red;display: none;padding-top:10px;">Maximum allowable file size is 10 MB.</div>   
          <div class="form-group" style="margin-bottom: 0rem;">
            <label for="message-text" class="col-form-label">Remarks
              <span style="color: red;"> *</span>
            </label>
          </div>
          <!-- <div class="btn-group btn-group-vertical" role="group" data-bs-toggle="buttons" style="width: 100%;">
            <c:forEach items="${approverRemarksToRejectTheCaseList}" var="object">
              <label class="btn btn-outline-custom" style="font-weight: 400;">
                <input type="radio" name="remarkOptions" id="${object.name}" onclick="showHideOtherRemark()"
                  value="${object.id}"> ${object.name}
              </label>
            </c:forEach>
          </div> -->
          <div class="btn-group btn-group-vertical" role="group" data-bs-toggle="buttons" style="width: 100%; display:none; " id="apRejectRemarksDiv">
            
               <div id="apRejectRemarks"></div>
                <!-- <input type="radio" name="remarkOptions" id="${object.name}" onclick="showHideOtherRemark()" value="${object.id}"> ${object.name} -->
              
            
          </div>
          <textarea class="form-control" id="remarksRaiseQueryId" name="otherRemarks" style="display: none" placeholder="Remarks"></textarea>
          <br><br>
          <div class="row">
			<div class="col-md-12">
				<div class="form-group">
				<label for="excelFile">File Upload <span style="color: red">*</span></label><span> (Upload only pdf file with max file size of 10 MB ) </span>
                   <input class="form-control" type="file" id="fileAttachedWhileRevert"  name="fileAttachedWhileRevert" accept=".pdf"  >
				</div>
			</div>
		</div>
          
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="transferCaseBtn"
          onclick="submitVerifierRaiseQueryRemarksForm()">Submit</button>
      </div>
    </div>
  </div>
</div>