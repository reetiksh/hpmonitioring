<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script>
  $(function () {
    bsCustomFileInput.init();
  });
  
  $('.selectpicker').selectpicker();
  
  
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
	
	
	
	
	/* $('form').on('submit', function(oEvent) {
				oEvent.preventDefault();
				$.confirm({
					title : 'Confirm!',
					content : 'Do you want to proceed ahead with scrutiny initiate?',
					buttons : {
						submit : function() {
							oEvent.currentTarget.submit();
						},
						close : function() {
							$.alert('Canceled!');
						}
					}
				});
			})  */
			
			
		function recommendedForAudit(){
			$("#caseIdTagLine").css("display", "none");
			$("#demandTagLine").css("display", "none");
			$("#auditFileAttachTagLine").css("display", "none");
			$("#auditfileMaxSizeTagLine").css("display", "none");
			
			
		
			var gstin = "${mstScrutinyCases.id.GSTIN}";
			var period = "${mstScrutinyCases.id.period}";
			var caseReportingDate = "${mstScrutinyCases.id.caseReportingDate}";
			
			$("#asmtGstin").val(gstin);
			$("#asmtPeriod").val(period);
			$("#asmtCaseReportingDate").val(caseReportingDate);
			
			var maxSizeInBytes = 10 * 1024 * 1024; // 10 MB
			var caseId = $("#caseId").val();
			var demand  = $("#demand").val();
			var auditAttachFile = $('#recommendedForAuditFile')[0].files.length;
			
			if(auditAttachFile != 0){
				var fileInput = $('#recommendedForAuditFile')[0];
				auditAttachFileSize = fileInput.files[0].size; // Size in bytes
			}
			else{
				auditAttachFileSize = 0;
			}
			
			if(caseId == ""){
				   $("#caseIdTagLine").css("display", "block");
				   return;
	   		 }
			
			 if(demand == ""){
				   $("#demandTagLine").css("display", "block");
				   return;
			   }
			
			
			if(auditAttachFileSize == 0){
				   $("#auditFileAttachTagLine").css("display", "block");
				   return;
			  }
			 
			 if(auditAttachFileSize > maxSizeInBytes){
				$('#auditfileMaxSizeTagLine').css('display', 'block');
				return;
			}
			
			
			$.confirm({
				title : 'Confirm!',
				content : 'Do you want to proceed ahead with recommendation of this case to Audit?',
				buttons : {
					submit : function() {
						//$('#scrutinyProceedingDroppedModal').hide();
						setTimeout(function() {
         document.getElementById("recommendedAuditForm").submit(); 
       }, 00);
					},
					close : function() {
						$.alert('Canceled!');
					}
				}
			});
		}			
			
  
  
</script>
<div class="card-body"  style="background-color: #f1f1f1; border-radius: 3px; box-shadow: 2px 2px 4px 1px #8888884d;">
	<div id="auditFileAttachTagLine" style="color: red;display: none;padding-left:0px;padding-top:0px;margin-bottom: 10px;">Please attach the file.</div>
  	<div id="auditfileMaxSizeTagLine" style="color: red;display: none;padding-left:0px;padding-top:0px;margin-bottom: 10px;">Maximum allowable file size is 10 MB.</div>
	<div id="caseIdTagLine" style="color: red;display: none;padding-left:0px;padding-top:0px;margin-bottom: 10px;">Please fill caseId.</div>
	<div id="demandTagLine" style="color: red;display: none;padding-left:0px;padding-top:0px;margin-bottom: 10px;">Please fill amount.</div>


  <form method="POST" name="recommendedAuditForm" id="recommendedAuditForm" action="recommend_audit" enctype="multipart/form-data" >
  
  <h4><b><u>Recommended for Audit:</u></b></h4><br>
    <div class="row">
			<div class="col-md-3" id="span4">
			 
				<div class="form-group">
					<label for="caseId">Case Id <span style="color: red">*</span></label>
					<input class="form-control" id="caseId" name="caseId"
						placeholder="Please enter Case Id" maxlength="15"
						pattern="[A-Za-z0-9]{15}"
						title="Please enter 15-digits alphanumeric Case Id" />
				</div>
			</div>

			<%-- <div class="col-md-3" id="span6">
				<div class="form-group">
					<label for="caseStageARN">Case Stage<span style="color: red">*</span></label>
					<input class="form-control" id="caseStage" name="caseStage" placeholder="Please select Case Stage"  value="ASMT-10 Issued"  title="ASMT-10 Issued" readonly />
				</div>
			</div>

			<div class="col-md-3" id="span6"  >
				<div class="form-group">
					<label for="caseStageARN">Case Stage ARN/Reference no<span style="color: red">*</span></label>
					<input class="form-control" id="caseStageArn" name="caseStageArn" placeholder="Please enter Case Stage ARN" maxlength="15" pattern="[A-Za-z0-9]{15}"  value="${submittedData.caseStageArn}"  title="Please enter 15-digits alphanumeric Case Stage ARN" />
				</div>
			</div> --%>
			
			 <div class="col-md-3" id="span2">
				<div class="form-group">
					<label for="demand">Amount(₹)<span style="color: red">*</span></label>
					<input class="form-control" type="text" maxlength="11" id="demand" name="demand" onkeypress='return event.charCode >= 48 && event.charCode <= 57'  value="${submittedData.demand}"  title="Please enter Amount"  />
				</div>
		    </div> 
		    
		    <div class="col-md-3"  >
				<div class="form-group">
					<label for="recommendedForAuditFile">File Upload <span style="color: red">*</span></label><span> (upload only pdf file with max file size of 10MB ) </span>
                    <input class="form-control" type="file" id="recommendedForAuditFile"  name="recommendedForAuditFile" accept=".pdf"  >
                    <c:if test="${submittedData.sum eq 'fileexist'}">
                    <a href="/fo/downloadUploadedFile?fileName=${submittedData.remarks}"><button type="button" class="btn btn-primary"><i class="fa fa-download"></i></button></a>
                    </c:if>
				</div>
			</div>

			
		<div class="col-lg-12 alert alert-danger alert-dismissible fade show" role="alert" id="message1" style="max-height: 500px; overflow-y: auto; display: none;">
        <span id="alertMessage1"></span>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
      
    </div>
    
    <!-- recoveryStageList -->
    <div class="row">
    	  <%-- <div class="col-md-3">
				<div class="form-group">
				<label for="recoveryStage">	Recovery Stage <span style="color: red">*</span></label>  
					<select class="form-control selectpicker recoveryStage" id="recoveryStage" name="recoveryStage" data-live-search="true"  title="Please select Recovery Stage"  >
					        <option data-tokens="recoverystage" value="0" disabled>Select</option>
					        <c:forEach items="${recoveryStageList}" var="categories">
                                   <option data-tokens="" value="${categories.id}"  <c:if test="${categories.id == '4'}" >disabled</c:if> <c:if test="${categories.id == submittedData.recoveryStage}"> selected </c:if>   >${categories.name}</option>
                            </c:forEach>
                    </select>
				</div>
			</div>
			
			<div class="col-md-3">
				<div class="form-group">
					<label for="recoveryByDRC">Recovery Via DRC03(₹)<span style="color: red">*</span></label> 
					<input class="form-control" type="text" maxlength="11" id="recoveryByDRC3" name="recoveryByDRC3" onkeypress='return event.charCode >= 48 && event.charCode <= 57'  value="${submittedData.recoveryByDRC3}"  title="Please enter Recovery Via DRC03" />
				</div>
            </div> --%>
            
            
			
			
			<c:if test="${not empty submittedData.recoveryStageArnStr}">
		    <c:set var="strlst" value="${submittedData.recoveryStageArnStr}"></c:set>
		    <c:set var="lst" value="${fn:split(strlst,',')}"></c:set>
		</c:if>
		
		<input type="hidden" id="arnLength" value="${fn:length(lst)}" >
		<input type="hidden" value="" id="asmtGstin" name="asmtGstin" readonly />
		<input type="hidden" value="" id="asmtPeriod" name="asmtPeriod" readonly />
		<input type="hidden" value="" id="asmtCaseReportingDate" name="asmtCaseReportingDate" readonly />
			
			
			<%-- <div class="col-md-3">
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
		 </div> --%> 
			
			
    
    </div>
    
    
 <!--    <hr>
    <div class="col-12" style="text-align: center;">
    <img class="animation__shake" src="/static/image/under_development.png" alt="pdf" height="150" width="150"><br>
    <span style="font-size:35px;color:rgb(97, 97, 97)">This feature will be available shortly</span>
  </div> -->
    <div class="row">
      <div class="col-lg-12">
        <div class="float-right">
            <button type="button" class="btn btn-primary" onclick="recommendedForAudit();">Recommend</button>
        </div>
      </div>
    </div> 
  </form>
</div>