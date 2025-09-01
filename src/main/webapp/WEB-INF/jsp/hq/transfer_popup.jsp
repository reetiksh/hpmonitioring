<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
  .btn-outline-custom {
    color: #495057;
    border: 1px solid #ced4da;
    text-align:left;
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
	function showHideOtherRemark(){
		var selectedValue = $('input[name="remarkOptions"]:checked').val();
		if(selectedValue == 4){
			$("#remarksId").show();
		}else{
			$("#remarksId").hide();
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
</script>
<script>
	function approve(){
		var x = document.getElementById("rejectRemarkDiv");

		if(x.style.display === "block"){
			x.style.display = "none";
			return;
		}

    var gstIn = $("#gstIn").val();
    var caseReportingDate = $("#caseReportingDate").val();
    var period = $("#period").val();
    var assignedTo = $("#locationId").val();

		// console.log(gstIn, caseReportingDate, period, assignedTo);

		const hidden_form = document.createElement('form'); 
		hidden_form.method = 'post'; 
			
		hidden_form.action = "/hq/request_for_transfer"; 
			
		const hidden_input1 = document.createElement('input'); 
		hidden_input1.type = 'hidden'; 
		hidden_input1.name = 'gstIn';  
		hidden_input1.value = gstIn; 

		const hidden_input2 = document.createElement('input'); 
		hidden_input2.type = 'hidden'; 
		hidden_input2.name = 'caseReportingDate'; 
		hidden_input2.value = caseReportingDate; 

		const hidden_input3 = document.createElement('input'); 
		hidden_input3.type = 'hidden'; 
		hidden_input3.name = 'period'; 
		hidden_input3.value = period; 

    const hidden_input4 = document.createElement('input'); 
		hidden_input4.type = 'hidden'; 
		hidden_input4.name = 'assignedTo'; 
		hidden_input4.value = assignedTo; 

		const hidden_input5 = document.createElement('input'); 
		hidden_input5.type = 'hidden'; 
		hidden_input5.name = '${_csrf.parameterName}'; 
		hidden_input5.value = '${_csrf.token}'; 

		hidden_form.appendChild(hidden_input1); 
		hidden_form.appendChild(hidden_input2);
		hidden_form.appendChild(hidden_input3); 
    hidden_form.appendChild(hidden_input4); 
		hidden_form.appendChild(hidden_input5);

		document.body.appendChild(hidden_form); 
		$.confirm({
			title : 'Confirm!',
			content : 'Do you want to proceed ahead with approval of case transfer?',
			buttons : {
				submit : function() {
					hidden_form.submit(); 
				},
				close : function() {
					$.alert('Canceled!');
				}
			}
		});
	}

  function reject(){
		var x = document.getElementById("rejectRemarkDiv");

		if(x.style.display === "none"){
			x.style.display = "block";
			return;
		}

    var gstIn = $("#gstIn").val();
    var caseReportingDate = $("#caseReportingDate").val();
    var period = $("#period").val();
    var assignedTo = $("#assignedFromLocationId").val();
		var rejectRemark = $("#rejectRemark").val();

		// console.log("rejectRemark : " + rejectRemark);
		if(rejectRemark === '' || rejectRemark == null){
			$("#rejectRemark_alert").html("Please enter remark !").css("color","red");
			return;
		}

		// console.log(gstIn, caseReportingDate, period, assignedTo);

		const hidden_form = document.createElement('form'); 
		hidden_form.method = 'post'; 
			
		hidden_form.action = "/hq/request_for_transfer"; 
			
		const hidden_input1 = document.createElement('input'); 
		hidden_input1.type = 'hidden'; 
		hidden_input1.name = 'gstIn';  
		hidden_input1.value = gstIn; 

		const hidden_input2 = document.createElement('input'); 
		hidden_input2.type = 'hidden'; 
		hidden_input2.name = 'caseReportingDate'; 
		hidden_input2.value = caseReportingDate; 

		const hidden_input3 = document.createElement('input'); 
		hidden_input3.type = 'hidden'; 
		hidden_input3.name = 'period'; 
		hidden_input3.value = period; 

    const hidden_input4 = document.createElement('input'); 
		hidden_input4.type = 'hidden'; 
		hidden_input4.name = 'assignedTo'; 
		hidden_input4.value = assignedTo; 

		const hidden_input5 = document.createElement('input'); 
		hidden_input5.type = 'hidden'; 
		hidden_input5.name = '${_csrf.parameterName}'; 
		hidden_input5.value = '${_csrf.token}';

		const hidden_input6 = document.createElement('input'); 
		hidden_input6.type = 'hidden'; 
		hidden_input6.name = 'rejectRemark'; 
		hidden_input6.value = rejectRemark;
 
		hidden_form.appendChild(hidden_input1); 
		hidden_form.appendChild(hidden_input2);
		hidden_form.appendChild(hidden_input3); 
    hidden_form.appendChild(hidden_input4); 
		hidden_form.appendChild(hidden_input5);
		hidden_form.appendChild(hidden_input6);

		document.body.appendChild(hidden_form); 
		$.confirm({
			title : 'Confirm!',
			content : 'Do you want to proceed ahead with rejection of case transfer?',
			buttons : {
				submit : function() {
					hidden_form.submit(); 
				},
				close : function() {
					$.alert('Canceled!');
				}
			}
		}); 
	}
</script>
<script>
	$('form').on('submit', function(oEvent) {
		oEvent.preventDefault();
		$.confirm({
			title : 'Confirm!',
			content : 'Are you sure you want to submit!',
			buttons : {
				submit : function() {
					oEvent.currentTarget.submit();
				},
				close : function() {
					$.alert('Canceled!');
				}
			}
		});
	}) 
</script>

<div class="modal fade bd-example-modal-lg" id="transferModal" tabindex="-1" role="dialog" aria-labelledby="transferModalTitle" aria-hidden="true" >
  <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="transferModalTitle">Transfer Case</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">    
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      
      <form method="POST" name="TransferForm" action="">
        <div class="modal-body">

          <div class="form-group">
            <label for="recipient-name" class="col-form-label">Remarks: </label>
            <input type="text" class="form-control" id="remark" name="remark"  value="" readonly/>
          </div>
          
          <input type="hidden" id="gstIn" name="gstIn" >
          <input type="hidden" id="caseReportingDate" name="caseReportingDate" >
          <input type="hidden" id="period" name="period" >
          <input type="hidden" id="assignedFromLocationId" name="assignedFromLocationId" >
          
          <div class="form-group" style="margin-bottom: 0rem;">
            <label for="message-text" class="col-form-label">Jurisdiction to which case will be assigned: <span style="color: red;"> *</span></label>
            <select style="width: 100%;" id="locationId" name="locationId"></select>
          </div>
          <div class="btn-group btn-group-vertical" role="group" data-toggle="buttons" style="width: 100%;"></div>
					
					<div class="form-group" style="display: none;" id="rejectRemarkDiv" title="Please enter your remark with in 250 letters">
						<label for="rejectRemark">Reason(s) for Rejection <span style="color: red;"> *</span><span id="rejectRemark_alert"></span></label>
						<input type="text" class="form-control" id="rejectRemark" name="rejectRemark" maxlength="250" placeholder="Please Enter Your Remarks">
					</div>

          <div class="modal-footer">
            <button type="button" class="btn btn-primary" Style="width: 80px;" onclick="approve();">Approve</button>
            <button type="button" class="btn btn-danger" Style="width: 80px;" onclick="reject()">Reject</button>
          </div>
        </div>
      </form>
    </div>
  </div>
</div>
