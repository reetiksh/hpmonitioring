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
		
	    var counter = $("#arnLength").val();
		
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
	        //counter -= 1
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


$(document).ready(function() {
    $('#recoveryStage').on('change', function() {
   	 debugger;
        var selectedOption = $(this).val();
        if (selectedOption === '3') {
       	 $('#recoveryAgainstDemand').val('');
        }
    });
});


</script>



<script>

$('#caseUpdateForm').on('submit', function(oEvent) {
	debugger;
	var recoveryAgainstDemand = $("#recoveryAgainstDemand").val();
	var isValid = /^\d+$/.test(recoveryAgainstDemand);
	if(!isValid){
		$('#submitUpdateCaseTagLine').css('display','block');
		event.preventDefault(); // Prevent the form from submitting
		return;
	}
	$('#submitUpdateCaseTagLine').css('display','none');
	
	oEvent.preventDefault();

	var recoveryStage = $("#recoveryStage").val();

	var recoveryStageArn = $("#recoveryStageArn").val(); 

	var contentMessage = (recoveryStage == 3) ? 'Do you want to proceed ahead with closing the case?' : 'Do you want to proceed ahead with updating case details?';
	
	$.confirm({
	title : 'Confirm!',		
	content : contentMessage,
	buttons : {
			submit : function() {                     
                   
				   if(recoveryStage == 2 || recoveryStage == 3){

				   if (typeof recoveryStageArn === 'undefined' || recoveryStageArn === null) {
					
                      $.alert("Please add Recovery Stage ARN/Reference no");
				   		
				   }else{
				   
			        var fileName = document.querySelector('#uploadedFile').value;

			        var extension = fileName.slice((fileName.lastIndexOf(".") - 1 >>> 0) + 2);
			        
			        var input = document.getElementById('uploadedFile');
			        
			     	if (input.files && input.files[0]) {
			        
			     	var maxAllowedSize = 10 * 1024 * 1024;
                    
			     	if(extension == 'pdf'){
			             
			        if(input.files[0].size > maxAllowedSize) {

			        	$.alert('Please upload max 10MB file');
			         	input.value = '';
			        }else{
					       
			        	oEvent.currentTarget.submit();
			 	    }

			     	}else{

			          $.alert("Please upload only pdf file");
			          document.querySelector('#uploadedFile').value = '';
			        }

			     	}else{

			     		 $.alert("Please upload pdf file");
			     	}

				 }

			  }else{

				    var fileName = document.querySelector('#uploadedFile').value;

			        var extension = fileName.slice((fileName.lastIndexOf(".") - 1 >>> 0) + 2);
			        
			        var input = document.getElementById('uploadedFile');
			        
			     	if (input.files && input.files[0]) {
			        
			     	var maxAllowedSize = 10 * 1024 * 1024;
                  
			     	if(extension == 'pdf'){
			             
			        if(input.files[0].size > maxAllowedSize) {

			        	$.alert('Please upload max 10MB file');
			         	input.value = '';
			        }else{
					       
			        	oEvent.currentTarget.submit();
			 	    }

			     	}else{

			          $.alert("Please upload only pdf file");
			          document.querySelector('#uploadedFile').value = '';
			        }

			     	}else{

			     		 $.alert("Please upload pdf file");
			     	}

			  }

			},
			close : function() {
				$.alert('Canceled!');
			}
		}
	});



	
});


function onAction() {
	
        var recoveryId = $("#recoveryStage").val();

        var recoveryStatus = ${submittedData.recoveryStage};

        if(recoveryId < recoveryStatus){
        
         alert("Action not allowed: You can not select previous recovery status/stage");

         $("#recoveryStage").val(recoveryStatus);
          	
        }
}


</script>
	<div id="submitUpdateCaseTagLine" style="color: red;display: none;">Please enter amount in required format (e.g. :- 6591 ).</div>
	<div class="card-body">
	
	<form method="POST" action="fo_update_recovery_cases" name="caseUpdateForm" id="caseUpdateForm" enctype="multipart/form-data" >
		<div class="row">
			<div class="col-md-3">
				<div class="form-group">
					<label for="gstin">GSTIN </label> 
					<input class="form-control" id="gstin" name="GSTIN_ID" value="${viewItem.GSTIN_ID}" name="gstin" readonly />
				</div>

			</div>
			<div class="col-md-3">
				<div class="form-group">
					<label for="taxpayername">Taxpayer Name </label> 
					<input class="form-control" id="taxpayerName" name="taxpayerName" value="${viewItem.taxpayerName}" name="taxpayerName" readonly />
				</div>

			</div>
			
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			
			<div class="col-md-3">
				<div class="form-group">
					<label for="circle">Jurisdiction</label> 
					<input class="form-control" id="circle" name="circle" value="${viewItem.circle}" name="circle" readonly />
				</div>
			</div>
			<div class="col-md-3">
				<div class="form-group">
					<label for="casecategory">Case Category </label>
					<input class="form-control" id="caseCategory" value="${viewItem.category}" name="category" readonly />
				</div>
			</div>
			
		</div>
		<div class="row">
			
			<div class="col-md-3">
				<div class="form-group">
					<label for="casereportingdate">Period</label>
					<input class="form-control" value="${viewItem.period_ID}" readonly />
				</div>

			</div>
			
			<div class="col-md-3">
				<div class="form-group">
					<label for="casereportingdate">Reporting Date(DD-MM-YYYY)</label>
					<input class="form-control" value="<fmt:formatDate value='${viewItem.date}' pattern='dd-MM-yyyy' />" readonly />
				</div>

			</div>

			<input type="hidden" class="form-control" id="caseReportingDate" value="${viewItem.caseReportingDate_ID}" name="caseReportingDate_ID" />
			
			<input type="hidden" name="period_ID" value="${viewItem.period_ID}" >
			
			<div class="col-md-3">
				<div class="form-group">
					<label for="suspectedIndicative">Indicative Value(₹)</label>
					<input class="form-control" value="${viewItem.indicativeTaxValue}"  readonly />
				</div>
			</div>
			
			<div class="col-md-3">
				<div class="form-group">
					<label for="actionstatus">Action Status <span style="color: red">*</span></label>
                    <input class="form-control" value="${submittedData.actionStatusName}" readonly />
				</div>	
	    </div>
			
		</div>
		
		<div class="row">
		
		
		   
		<div class="col-md-3" id="span4"  >
				<div class="form-group">
				<label for="actionstatus">Case Id </label>
					<input class="form-control" value="${viewItem.caseId}" readonly />
				</div>
		 </div>
		 	
		 <div class="col-md-3" id="span5"  >
				<div class="form-group">
					<label for="caseStage">Case Stage </label>
                    <input class="form-control" value="${submittedData.caseStageName}" readonly />
		 		</div>
		 </div>
		 <div class="col-md-3" id="span6"  >
				<div class="form-group">
					<label for="caseStageARN">Case Stage ARN/Reference no</label>
					<input class="form-control" value="${submittedData.caseStageArn}" readonly />
				</div>
			</div>
			
		<div class="col-md-3" id="span2">
				<div class="form-group">
					<label for="demand">Amount(₹)</label>
					<input class="form-control" type="text"  value="${submittedData.demand}"  readonly />
				</div>
	    </div> 		
			
		</div>		
		
		<div class="row" >
		   
		    <div class="col-md-3">
				<div class="form-group">
				<label for="recoveryStage">	Recovery Stage <span style="color: red">*</span></label>  
					<select class="form-control selectpicker recoveryStage" id="recoveryStage" name="recoveryStage" data-live-search="true"  onchange="onAction()"  title="Please select Recovery Stage"  >
					        <option data-tokens="recoverystage" value="0" disabled>Select</option>
					        <c:forEach items="${listRecovery}" var="categories">
                                   <option data-tokens="" value="${categories.id}"  <c:if test="${categories.id == '4'}" >disabled</c:if> <c:if test="${categories.id == submittedData.recoveryStage}"> selected </c:if>   >${categories.name}</option>
                            </c:forEach>
                    </select>
				</div>
			</div>
			
		    
			<div class="col-md-3">
				<div class="form-group">
					<label for="recoveryByDRC">Recovery Via DRC03(₹)</label> 
					<input class="form-control" type="text" id="recoveryByDRC3" value="${submittedData.recoveryByDRC3}"  readonly />
				</div>
            
			</div>
			
			<div class="col-md-3">
				<div class="form-group">
					<label for="recovery">Recovery Against Demand(₹)<span style="color: red">*</span></label>
					<input class="form-control" type="text" maxlength="11" id="recoveryAgainstDemand" name="recoveryAgainstDemand" onkeypress='return event.charCode >= 48 && event.charCode <= 57'  value="${submittedData.recoveryAgainstDemand}"  title="Please enter Recovery Against Demand"  required />
				</div>
			</div>
			
			
		</div>
		
		<c:if test="${not empty submittedData.recoveryStageArnStr}">
		    <c:set var="strlst" value="${submittedData.recoveryStageArnStr}"></c:set>
		    <c:set var="lst" value="${fn:split(strlst,',')}"></c:set>
		</c:if>
		
		<input type="hidden" id="arnLength" value="${fn:length(lst)}" >
		
		<div class="row">
		
		<div class="col-md-3"  >
				<div class="form-group">
					<label for="excelFile">File Upload <span style="color: red">*</span></label><span> (upload only pdf file with max file size of 10MB ) </span>
                    <input class="form-control" type="file" id="uploadedFile"  name="uploadedFile" accept=".pdf"  required >
                    <c:if test="${submittedData.sum eq 'fileexist'}">
                    <br>
                    <a href="/fo/downloadUploadedFile?fileName=${submittedData.remarks}"><button type="button" class="btn btn-primary"><i class="fa fa-download"></i></button></a>
                    </c:if>
				</div>
		    </div>
		
		<div class="col-md-3">
		  <div class="form-group">
		   <label for="recoveryByDRC">Recovery Stage ARN/Reference no<span style="color: red">*</span></label>
		   <table id="myTable" class=" table order-list">
			<thead>
			    </thead>
			    <tbody>
			    </tbody>
			    <tfoot>
			    
			        <c:if test="${fn:length(lst) > 0}">
			        <c:forEach items="${lst}"  varStatus="loop"  var="lst">
			         <tr>
			            <td><input type="text" class="form-control recoveryclass" value="${fn:trim(lst)}" id="recoveryStageArn" name="recoveryStageArn[${loop.index}]" placeholder="Please enter Recovery Stage ARN" maxlength="15" pattern="[A-Za-z0-9]{15}" title="Please enter 15-digits alphanumeric Recovery Stage ARN"  required /></td>
	                    <td><input type="button" class="ibtnDel btn btn-md btn-danger"  value="Delete"></td>
			        </tr>
			        </c:forEach>
                    </c:if>
                    
			        <tr>
			            <td colspan="5" style="text-align: left;">
			                <input type="button" class="btn btn-primary" id="addrow"  value="Add Row" />
			            </td>
			        </tr>
			        
			    </tfoot>
			</table>
		   </div>
		 </div>  
		   
		</div>  
		
		<hr>
		
		<div class="row">
			<div class="col-md-12">
				<div class="form-group float-right">
					  <button type="submit" class="btn btn-primary" id="submitCase" >Submit</button>
				</div>
			</div>
		</div>
		</form>
		
	</div>






