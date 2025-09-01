<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
<script src="/static/dist/js/jquery-confirm.min.js"></script>
<style>
.modal-lg, .modal-xl {
    max-width: 1000px;
}

.table-responsive {
 overflow: scroll;
 max-height: 800px;
 }

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
        
        if(selectedValue == 1){
          $("#remarksId").show();
        }else{
          $("#remarksId").hide();
        }
        
	}


	function centerCase(val){
       
       var circle = $("#jurisdiction").val();

       if(circle == val){
           
    	   alert("Please choose different jurisdiction");
           
    	   $("#caseAssignedTo").val('');
    	    
       }else{
           
    	   if(val == 'NC'){

        	   var div = document.getElementById("file");
        	   
        	   div.style.display = "block";
        	   
        	   $("#uploadedFile").prop("required", true);

        	   $("input[value='1']").parent().hide();

        	   $("input[value='3']").parent().hide();
        	   
           }else{

        	   var div = document.getElementById("file");

        	   div.style.display = "none";
        	   
        	   $("#uploadedFile").prop("required", false);

        	   $("input[value='1']").parent().show();

        	   $("input[value='3']").parent().show();
        	   
           } 
  
        }
	   
	}

	
	/************ add row for scrutiny proceedingg dropped start ****************/
	$(document).ready(function () {
	    var counter = $("#arnLength").val();
	    
	    $("#addrow").on("click", function () {
	        var newRow = $("<tr>");
	        var cols = "";
	
	        cols += '<td><input type="text" class="form-control recoveryclass"  id="recoveryStageArn" name="recoveryStageArn[' + counter + ']" placeholder="Please enter Recovery Stage ARN" maxlength="15" pattern="[A-Za-z0-9]{15}" title="Please enter 15-digits alphanumeric Recovery Stage ARN" required /></td>';
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
	/************ add row for scrutiny proceedingg dropped end ****************/
	
	
	/********** convert to dd-mm-yyyy format start **********/	
	  function formatTheDate(inputDate) {
		
            // Parsing the input date string into a JavaScript Date object
            var date = new Date(inputDate);

            // Extracting day, month, and year
            var day = date.getDate();
            var month = date.getMonth() + 1; // Month is zero-based
            var year = date.getFullYear();

            // Adding leading zeros if necessary
            if (day < 10) {
                day = '0' + day;
            }
            if (month < 10) {
                month = '0' + month;
            }

            // Combining into the desired format
            return day + '-' + month + '-' + year;
        } 
	  /********** convert to dd-mm-yyyy format end **********/
	  
	   function copyInputValueToAmount() {
            var input1 = document.getElementById("recoveryByDRC3").value;
            document.getElementById("demand").value = input1;
        }
	
	  
	   function submitScrutinyCaseDropped(){
		   
		   var maxSizeInBytes = 10 * 1024 * 1024; // 10 MB
		   var recoveryStageVal = $("#recoveryStage").val();
		   var recoveryByDRC3Val = $("#recoveryByDRC3").val();
		   var scrutinyDroppedFileAttachedVal = $("#uploadedFile")[0].files.length;
		   var recoveryStageArnVal = $("#recoveryStageArn").val(); 
		   
		   var scrutinyDroppedFileAttachedSize;
		   
		   if(scrutinyDroppedFileAttachedVal != 0){
				var fileInput = $('#uploadedFile')[0];
				scrutinyDroppedFileAttachedSize = fileInput.files[0].size; // Size in bytes
			}
			else{
				scrutinyDroppedFileAttachedSize = 0;
			}
		   
		   $("#recoveryStageTagLine").css("display", "none");
		   $("#drc03TagLine").css("display", "none");
		   $("#fileNotAttachedTagLine").css("display", "none");
		   $("#fileMaxSizeTagLine").css("display", "none");
		   $("#recveryStageArnTagLine").css("display", "none");
		   
		   
		   var isEmpty = false;
		    $(".recoveryclass").each(function () {
		        if ($(this).val().trim() === "") {
		            isEmpty = true;
		            recoveryStageArnVal = "";
		        }
		    });
		   
		   if(recoveryStageVal == ""){
			   $("#recoveryStageTagLine").css("display", "block");
			   return;
		   }
		   
		   if(recoveryByDRC3Val == ""){
			   $("#drc03TagLine").css("display", "block");
			   return;
		   }
		   
		   if(scrutinyDroppedFileAttachedSize == 0){ 
			   $("#fileNotAttachedTagLine").css("display", "block");
			   return;
		   }
		   
		   if(scrutinyDroppedFileAttachedSize > maxSizeInBytes){
				$('#fileMaxSizeTagLine').css('display', 'block');
				return;
			}
		   if(recoveryStageVal == '2' || recoveryStageVal == '3'){
			   if(recoveryStageArnVal == "" || recoveryStageArnVal == undefined ){
				   $('#recveryStageArnTagLine').css('display', 'block');
					return;	   
			   }
			}
		   
		   
		   
		   
		   
		   $("#recoveryStageTagLine").css("display", "none");
		   $("#drc03TagLine").css("display", "none");
		   $("#fileNotAttachedTagLine").css("display", "none");
		   
		   
		   $.confirm({
				title : 'Confirm!',
				content : 'Do you want to proceed ahead with dropping scrutiny proceedings?',
				buttons : {
					submit : function() {
						$('#scrutinyProceedingDroppedModal').hide();
						setTimeout(function() {
         document.getElementById("scrutinyDroppedData").submit();
        }, 00);
					},
					close : function() {
						$.alert('Canceled!');
					}
				}
			});
	   }
	  
	  
</script>

<div class="modal fade" id="scrutinyProceedingDroppedModal" tabindex="-1" role="dialog" aria-labelledby="scrutinyProceedingDroppedModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-lg"  role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="scrutinyProceedingDroppedModalTitle">Drop Scrutiny Proceedings</h5>
        <button type="button" class="close" href="/acknowledge_cases"  data-dismiss="modal" aria-label="Close">    
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div id="recoveryStageTagLine" style="color: red;display: none;padding-left:10px;padding-top:10px;">Please select recovery stage.</div>
      <div id="drc03TagLine" style="color: red;display: none;padding-left:10px;padding-top:10px;">Please fill recovery via DRC03.</div>
      <div id="fileNotAttachedTagLine" style="color: red;display: none;padding-left:10px;padding-top:10px;">Please select the file.</div>
   	  <div id="fileMaxSizeTagLine" style="color: red;display: none;padding-left:10px;padding-top:10px;">Maximum allowable file size is 10 MB.</div>
   	  <div id="recveryStageArnTagLine" style="color: red;display: none;padding-left:10px;padding-top:10px;">Please provide recovery stage arn/reference no.</div>  
      
      
       <div class="modal-body" id="updateSummaryViewBody" >
       
      
      
      <form method="POST" action="scrutiny_initiated_dropped" name="scrutinyDroppedData" id="scrutinyDroppedData" enctype="multipart/form-data" >
		<div class="row">
			<div class="col-md-4">
				<div class="form-group">
					<label for="gstin">GSTIN </label> 
					<input class="form-control" id="gstin"  value="${viewItem.GSTIN_ID}" name="gstin" readonly />
				</div>
			</div>
			
			<div class="col-md-4">
				<div class="form-group">
					<label for="taxpayername">Taxpayer Name </label> 
					<input class="form-control" id="taxpayerName" name="taxpayerName" value="${viewItem.taxpayerName}" name="taxpayerName" readonly />
				</div>

			</div>
			
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			
			<div class="col-md-4">
				<div class="form-group">
					<label for="circle">Jurisdiction</label> 
					<input class="form-control" id="circle" name="circle" value="${viewItem.circle}" name="circle" readonly />
				</div>
			</div>
			
			
		</div>
		<div class="row">
			
			<div class="col-md-4">
				<div class="form-group">
					<label for="casecategory">Case Category </label>
					<input class="form-control" id="caseCategory" value="${viewItem.category.name}" name="caseCategory" readonly />
				</div>
			</div>
			
			
			<div class="col-md-4">
				<div class="form-group">
					<label for="period">Period</label>
					<input class="form-control" id="period" value="${viewItem.period_ID}" name="period" readonly />
				</div>

			</div>
			
			<div class="col-md-4">
				<div class="form-group">
					<label for="casereportingdate">Reporting Date(DD-MM-YYYY)</label>
					<input class="form-control" id="casereportingdate" value="<fmt:formatDate value='${viewItem.date}' pattern='dd-MM-yyyy' />" name="casereportingdate" readonly />
				</div>

			</div>

			<input type="hidden" class="form-control" id="caseReportingDate" value="${viewItem.caseReportingDate_ID}" name="caseReportingDate_ID" />
			
			<input type="hidden" name="period_ID" value="${viewItem.period_ID}" >
			
			
			
		</div>
		
			
				
		<span id="span1"  >		
		
		<div class="row" >
		
		    <div class="col-md-4">
				<div class="form-group">
					<label for="suspectedIndicative">Indicative Value(₹)</label>
					<input class="form-control" id="susIndicativeTax" value="${viewItem.indicativeTaxValue}" name="indicativeTaxValue" readonly />
				</div>
			</div>
		    
		    <div class="col-md-4">
				<div class="form-group">
				<label for="recoveryStage">	Recovery Stage <span style="color: red">*</span></label>  
					<select class="form-control selectpicker recoveryStage" id="recoveryStage" name="recoveryStage" data-live-search="true"  title="Please select Recovery Stage"  >
					        <option data-tokens="recoverystage" value="0" disabled>Select</option>
					        <c:forEach items="${listRecovery}" var="categories">
                                   <option data-tokens="" value="${categories.id}"  <c:if test="${categories.id == '4'}" >disabled</c:if> <c:if test="${categories.id == submittedData.recoveryStage}"> selected </c:if>   >${categories.name}</option>
                            </c:forEach>
                    </select>
				</div>
			</div>
			
		     <div class="col-md-4" id="span2">
				<div class="form-group">
					<label for="demand">Amount(₹)<span style="color: red">*</span></label>
					<input class="form-control" type="text" maxlength="11" id="demand" name="demand"  onchange="amountChange()" onkeypress='return event.charCode >= 48 && event.charCode <= 57'  value="${submittedData.demand}"  title="Please enter Amount" readonly  />
				</div>
		    </div> 
		    
		</div>
		
		<div class="row">
		<div class="col-md-4">
				<div class="form-group">
					<label for="recoveryByDRC">Recovery Via DRC03(₹)<span style="color: red">*</span></label> 
					<input class="form-control" type="text" maxlength="11" id="recoveryByDRC3" name="recoveryByDRC3" onkeypress='return event.charCode >= 48 && event.charCode <= 57' oninput="copyInputValueToAmount()" value="${submittedData.recoveryByDRC3}"  title="Please enter Recovery Via DRC03" />
				</div>
       	</div>
		
		<div class="col-md-4"  >
				<div class="form-group">
					<label for="excelFile">File Upload <span style="color: red">*</span></label><span> (upload only pdf file with max file size of 10MB ) </span>
                    <input class="form-control" type="file" id="uploadedFile"  name="uploadedFile" accept=".pdf"  >
                    <c:if test="${submittedData.sum eq 'fileexist'}">
                    <a href="/fo/downloadUploadedFile?fileName=${submittedData.remarks}"><button type="button" class="btn btn-primary"><i class="fa fa-download"></i></button></a>
                    </c:if>
				</div>
		</div>
		
		<c:if test="${not empty submittedData.recoveryStageArnStr}">
		    <c:set var="strlst" value="${submittedData.recoveryStageArnStr}"></c:set>
		    <c:set var="lst" value="${fn:split(strlst,',')}"></c:set>
		</c:if>
		
		<input type="hidden" id="arnLength" value="${fn:length(lst)}" >
		
		<div class="col-md-4">
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
		
		</span>
		
		<hr>
		
		<div class="row">
			<div class="col-md-12">
				<div class="form-group float-right">
					  <button type="button" class="btn btn-primary" id="submitCase" onclick="submitScrutinyCaseDropped()">Submit</button>
				</div>
			</div>
		</div>
		</form>
		
		</div>
      
      
    </div>
  </div>
</div>
