<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<style>
  .btn-outline-custom {
    color: #495057;
    border: 1px solid #ced4da;
    text-align:left;
}

  .class {
  border: 1px solid black;
  padding : 10px;
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
	$(document).ready(function () {
	    var counter = 0;
	
	    $("#addrow").on("click", function () {
	        var newRow = $("<tr>");
	        var cols = "";
	        cols += '<td><input type="text" class="form-control recoveryclass" id="recoveryStageArn" name="recoveryStageArn[' + counter + ']" placeholder="Please enter Recovery Stage ARN" maxlength="15" pattern="[A-Za-z0-9]{15}" title="Please enter 15-digits alphanumeric Recovery Stage ARN" required /></td>';
	        cols += '<td><input type="button" class="ibtnDel btn btn-md btn-danger "  value="Delete"></td>';
	        newRow.append(cols);
	        $("table.order-list").append(newRow);
	        counter++;
	    });
	    
	    $("table.order-list").on("click", ".ibtnDel", function (event) {
	        $(this).closest("tr").remove();       
	        counter -= 1
	    });
	
	});
	
	
	function calculateRow(row) {
	    var price = +row.find('input[name^="price"]').val();
	
	}
	
	function calculateGrandTotal() {
	    var grandTotal = 0;
	    $("table.order-list").find('input[name^="price"]').each(function () {
	        grandTotal += +$(this).val();
	    });
	    $("#grandtotal").text(grandTotal.toFixed(2));
	}

</script>


<script>
$(function() {
	  $('.selectpicker').selectpicker();
	});


</script>



<script>

 $('form').on('submit', function(oEvent) {
	
	oEvent.preventDefault();
    
	var status = $("#status").val();

    if(status == 'approve'){

      $.confirm({
  		title : 'Confirm!',
  		content : 'Do you want to proceed ahead with approval of request for case id updation?',
  		buttons : {
  			submit : function() {
                  
  			    oEvent.currentTarget.submit();
  			   
  			},
  			close : function() {
  				$.alert('Canceled!');
  			}
  			
  		}
  	
  	});
      
    }

    if(status == 'reject'){

      $.confirm({
  		title : 'Confirm!',
  		content : 'Do you want to proceed ahead with rejection of request of for case id updation?',
  		buttons : {
  			submit : function() {
                  
  			    oEvent.currentTarget.submit();
  			   
  			},
  			close : function() {
  				$.alert('Canceled!');
  			}
  			
  		  }
  	
  	  });
      
    }
	
});


function rejectCaseId(val){

	 if(val == 'reject'){
	     
		 $("#status").val('reject'); 
	     
	 }
	 
}


function approveCaseId(val){

	 if(val == 'approve'){
		 
		 $("#status").val('approve');
		  
	 }
	 
 }


</script>

	<div class="card-body">
	
	<form method="POST" action="updateCaseId" name="caseUpdateForm" id="caseUpdateForm" enctype="multipart/form-data" >
			
		<div class="row" >
		
		<div class="col-md-12">
			<div class="form-group">
				<label for="recovery">Existing Case ID</label>
				<input class="form-control" type="text"  value="${oldCaseId}" name="caseid"  readonly />
			</div>
		</div>
			
		</div>
		
		<div class="row" >
		
		<div class="col-md-12">
			<div class="form-group">
				<label for="recovery">New Case ID Proposed</label>
				<input class="form-control" type="text"  value="${suggestedCaseId}"  name="otherRemarks" readonly />
			</div>
		</div>
			
		</div>
		
		<input type="hidden" id="status" name="status"  />
		
		<input type="hidden" id="gstnocaseid" name="gstnocaseid"  value="${gst}" />
		
		<input type="hidden" id="datecaseid" name="datecaseid" value="${date}" />
		
		<input type="hidden" id="periodcaseid" name="periodcaseid" value="${period}" />
		
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		
		<div class="row" >
		
		<div class="col-md-12">
			<div class="form-group">
				<label for="recovery">Remarks</label>
				<input class="form-control" type="text"  value="${remarks}"  readonly />
			</div>
		</div>
			
		</div>
		
		<div class="row" >
		
		<div class="col-md-12" id="remarks" >
			<div class="form-group">
				<label for="recovery">Reasons for Approval/Rejection<span style="color: red">*</span></label>
				<input class="form-control" type="text"  id="approvalRemarks" name="approvalRemarks" required />
			</div>
		</div>
			  
		</div>
		
		<hr>
		
		<div class="row">
			<div class="col-md-12">
				<div class="form-group float-right">
				
					  <button class="btn btn-primary" id="submitCaseapprove" onclick="approveCaseId('approve')" >Approve</button>
					  
					  <button class="btn btn-danger" id="submitCaseReject" onclick="rejectCaseId('reject')" >Reject</button>
					  
				</div>
			</div>
			
		</div>
		</form>
		
	</div>


