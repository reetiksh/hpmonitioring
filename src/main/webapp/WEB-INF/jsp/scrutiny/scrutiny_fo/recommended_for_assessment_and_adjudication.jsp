<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

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
	    
	    document.getElementById('recommendedOnDate').value = '';
    /************ add row for scrutiny proceedingg dropped end ****************/
	
    /**************** Check for selected date shold not be less than from case reporting date start *****************/
        $('#recommendedOnDate').on('change', function() {
        	$("#assesmentFileAttachTagLine").css("display", "none");
    		$("#assesmentfileMaxSizeTagLine").css("display", "none");
    		$("#dateSelectTagLine").css("display", "none");
	    	debugger;
	    	var inputValue = $(this).val();
	        var convertReportingDate = $('#asmtCaseReportingDate').val();
	        var convertedReportingDate = returnConvertedDate(convertReportingDate);
	        if(inputValue < convertedReportingDate){
	        	alert("Selected date should not be earlier than case reporting date.");
	        	$('#recommendedOnDate').val('');
      		 }
        	});
		});
	/**************** Check for selected date shold not be less than from case reporting date end *****************/
	
	
	/******************* convert date type start ***********************/
  	function returnConvertedDate(convertDate) {
  		var parts = convertDate.split(" ");
        var day = parts[2];
        var month = parts[1]; // e.g., "Nov"
        var year = parts[5];
        var monthIndex = new Date(Date.parse(month + " 1, 2020")).getMonth(); // Get month index
        
        // Create a new Date object
        var dateObject = new Date(year, monthIndex, day);
        
        // Extract year, month, and day
        var formattedYear = dateObject.getFullYear();
        var formattedMonth = String(dateObject.getMonth() + 1).padStart(2, '0');
        var formattedDay = String(dateObject.getDate()).padStart(2, '0');

        // Format as yyyy-mm-dd
       return formattedYear + '-' + formattedMonth + '-' + formattedDay;
    }
	/******************* convert date type end ***********************/
	
	
	
	function recommendedForAssesment(){
		
		$("#assesmentFileAttachTagLine").css("display", "none");
		$("#assesmentfileMaxSizeTagLine").css("display", "none");
		$("#dateSelectTagLine").css("display", "none");
		
		var assessmentFileSize;
		var maxSizeInBytes = 10 * 1024 * 1024; // 10 MB
		var recoveryStageVal = $("#recoveryStage").val();
		var gstin = "${mstScrutinyCases.id.GSTIN}";
		var period = "${mstScrutinyCases.id.period}";
		var caseReportingDate = "${mstScrutinyCases.id.caseReportingDate}";
		var dateValue = $('#recommendedOnDate').val();
		
		 if(dateValue == ""){
			   $("#dateSelectTagLine").css("display", "block");
			   return;
		  }
		
		
		var assessmentAttachFile = $('#recommendedForAssessAndAdjudicationFile')[0].files.length;
		if(assessmentAttachFile != 0){
			var fileInput = $('#recommendedForAssessAndAdjudicationFile')[0];
			assessmentFileSize = fileInput.files[0].size; // Size in bytes
		}
		else{
			assessmentFileSize = 0;
		}
		
		 if(assessmentFileSize == 0){
			   $("#assesmentFileAttachTagLine").css("display", "block");
			   return;
		  }
		 
		 if(assessmentFileSize > maxSizeInBytes){
				$('#assesmentfileMaxSizeTagLine').css('display', 'block');
				return;
			}
		
		$("#asmtGstin").val(gstin);
		$("#asmtPeriod").val(period);
		$("#asmtCaseReportingDate").val(caseReportingDate);
		
		   $.confirm({
				title : 'Confirm!',
				content : 'Do you want to proceed ahead with recommendation of this case to Assessment & Adjudication?',
				buttons : {
					submit : function() {
						//$('#scrutinyProceedingDroppedModal').hide();
						setTimeout(function() {
         document.getElementById("recommendedAssesmentForm").submit(); 
       }, 00);
					},
					close : function() {
						$.alert('Canceled!');
					}
				}
			});
		
	}
	
	 // Get the current date in the format YYYY-MM-DD
    var today = new Date().toISOString().split('T')[0];
    // Set the max attribute to today's date
    document.getElementById("recommendedOnDate").setAttribute("max", today);
    
    document.getElementById("recommendedOnDate").value = today;
			 
</script>
<div class="card-body"
	style="background-color: #f1f1f1; border-radius: 3px; box-shadow: 2px 2px 4px 1px #8888884d;">
	
	<div id="assesmentFileAttachTagLine" style="color: red;display: none;padding-left:0px;padding-top:0px;margin-bottom: 10px;">Please attach the file.</div>
	<div id="assesmentfileMaxSizeTagLine" style="color: red;display: none;padding-left:0px;padding-top:0px;margin-bottom: 10px;">Maximum allowable file size is 10 MB.</div>
	
	<div id="dateSelectTagLine" style="color: red;display: none;padding-left:0px;padding-top:0px;margin-bottom: 10px;">Please select date.</div>
	
	<form method="POST" name="recommendedAssesmentForm"
		id="recommendedAssesmentForm" action="recommend_assesment"
		enctype="multipart/form-data">
		<h4>
			<b><u>Recommended for Assessment & Adjudication:</u></b>
		</h4>
		<br>
		<div class="row">
			<div class="col-md-3" id="span4">
				<div class="form-group">
					<label for="caseId">Case Id <span style="color: red">*</span></label>
					<input class="form-control" id="caseId" name="caseId"
						placeholder="Please enter Case Id" maxlength="15"
						pattern="[A-Za-z0-9]{15}"
						title="Please enter 15-digits alphanumeric Case Id" readonly />
				</div>
			</div>

			<div class="col-md-3" id="span6">
				<div class="form-group">
					<label for="caseStageARN">Case Stage<span
						style="color: red">*</span></label> <input class="form-control"
						id="caseStage" name="caseStage"
						placeholder="Please select Case Stage" value="ASMT-10 Issued"
						title="ASMT-10 Issued" readonly />
				</div>
			</div>

			<div class="col-md-3" id="span6">
				<div class="form-group">
					<label for="caseStageARN">Case Stage ARN/Reference no<span
						style="color: red">*</span></label> <input class="form-control"
						id="caseStageArn" name="caseStageArn"
						placeholder="Please enter Case Stage ARN" maxlength="15"
						pattern="[A-Za-z0-9]{15}" value="${submittedData.caseStageArn}"
						title="Please enter 15-digits alphanumeric Case Stage ARN"
						readonly />
				</div>
			</div>

			<div class="col-md-3" id="span2">
				<div class="form-group">
					<label for="demand">Amount(₹)<span style="color: red">*</span></label>
					<input class="form-control" type="text" maxlength="11" id="demand"
						name="demand"
						onkeypress='return event.charCode >= 48 && event.charCode <= 57'
						value="${submittedData.demand}" title="Please enter Amount"
						readonly />
				</div>
			</div>


			<div class="col-lg-12 alert alert-danger alert-dismissible fade show"
				role="alert" id="message1"
				style="max-height: 500px; overflow-y: auto; display: none;">
				<span id="alertMessage1"></span>
				<button type="button" class="close" data-dismiss="alert"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />

		</div>

		<!-- recoveryStageList -->
		<div class="row">

			<div class="col-md-3">
				<div class="form-group">
					<label for="recoveryStage"> Recovery Stage <span
						style="color: red">*</span></label> <select
						class="form-control selectpicker recoveryStage" id="recoveryStage"
						name="recoveryStage" data-live-search="true"
						title="Please select Recovery Stage" disabled>
						<c:forEach items="${recoveryStageList}" var="soloRecovery">
							<c:choose>
								<c:when
									test="${soloRecovery.id eq mstScrutinyCases.recoveryStage.id}">
									<option value="${soloRecovery.id}" selected="selected" readonly>${soloRecovery.name}</option>
								</c:when>
								<c:otherwise>
									<option value="${soloRecovery.id}">${soloRecovery.name}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</div>
			</div>




			<div class="col-md-3">
				<div class="form-group">
					<label for="recoveryByDRC">Recovery Via DRC03(₹)<span
						style="color: red">*</span></label> <input class="form-control"
						type="text" maxlength="11" id="recoveryByDRC3"
						name="recoveryByDRC3"
						onkeypress='return event.charCode >= 48 && event.charCode <= 57'
						value="${submittedData.recoveryByDRC3}"
						title="Please enter Recovery Via DRC03" readonly />
				</div>
			</div>

			<%-- <div class="col-md-3">
				<div class="form-group">
					<label for="excelFile">File Upload <span style="color: red">*</span></label><span>
						(upload only pdf file with max file size of 10MB ) </span> <input
						class="form-control" type="file" id="uploadedFile"
						name="uploadedFile" accept=".pdf" disabled>
					<c:if test="${submittedData.sum eq 'fileexist'}">
						<a
							href="/fo/downloadUploadedFile?fileName=${submittedData.remarks}"><button
								type="button" class="btn btn-primary">
								<i class="fa fa-download"></i>
							</button></a>
					</c:if>
				</div>
			</div> --%>


			<c:if test="${not empty submittedData.recoveryStageArnStr}">
				<c:set var="strlst" value="${submittedData.recoveryStageArnStr}"></c:set>
				<c:set var="lst" value="${fn:split(strlst,',')}"></c:set>
			</c:if>

			<input type="hidden" id="arnLength" value="${fn:length(lst)}">
			<input type="hidden" value="" id="asmtGstin" name="asmtGstin"
				readonly /> <input type="hidden" value="" id="asmtPeriod"
				name="asmtPeriod" readonly /> <input type="hidden" value=""
				id="asmtCaseReportingDate" name="asmtCaseReportingDate" readonly />


		<div class="col-md-3">
				<div class="form-group">
					<label for="workingDate">Date <span style="color: red;">
							*</span><span id="date_alert"></span></label> <input type="date"
						class="form-control" id="recommendedOnDate" name="recommendedOnDate" required />
				</div>
			</div>
			
			<div class="col-md-3">
				<div class="form-group">
					<label for="downloadButton">Download<span
						style="color: red">*</span></label><span> (Attached ASMT-10 file.)
					</span>
					<div>
						<a href="/scrutiny_fo/downloadUploadedPdfFile?fileName=${amstTenFilePath}">
							<button type="button" class="btn btn-primary">
								<i class="fa fa-download"></i>
							</button>
						</a>
					</div>
				</div>
			</div>

		
			<div class="col-md-3">
				<div class="form-group">
					<label for="recommendedForAssessAndAdjudicationFile">File
						Upload <span style="color: red">*</span>
					</label><span> (upload only pdf file with max file size of 10MB ) </span> <input
						class="form-control" type="file"
						id="recommendedForAssessAndAdjudicationFile"
						name="recommendedForAssessAndAdjudicationFile" accept=".pdf">
				</div>
			</div>




			<div class="col-md-3">
				<div class="form-group">
					<label for="recoveryByDRC">Recovery Stage ARN/Reference no<span
						style="color: red">*</span></label>
					<table id="myTable" class=" table order-list">
						<thead>
						</thead>
						<tbody>
						</tbody>
						<tfoot>

							<c:if test="${fn:length(listOfArn) > 0}">
								<c:forEach items="${listOfArn}" varStatus="loop" var="lst">
									<tr>
										<td><input type="text" class="form-control recoveryclass"
											value="${fn:trim(lst)}" id="recoveryStageArn"
											name="recoveryStageArn[${loop.index}]"
											placeholder="Please enter Recovery Stage ARN" maxlength="15"
											pattern="[A-Za-z0-9]{15}"
											title="Please enter 15-digits alphanumeric Recovery Stage ARN"
											readonly /></td>
											<td><input type="button"
											class="ibtnDel btn btn-md btn-danger" value="Delete" disabled></td>
									</tr>
								</c:forEach>
							</c:if>

							<tr>
								<td colspan="5" style="text-align: left;"><input
									type="button" class="btn btn-primary" id="addrow"
									value="Add Row" disabled /></td>
							</tr>

						</tfoot>
					</table>
				</div>
			</div>





		</div>


		<hr>
		<div class="row">
			<div class="col-lg-12">
				<div class="float-right">
					<button type="button" class="btn btn-primary"
						onclick="recommendedForAssesment();">Recommend</button>
				</div>
			</div>
		</div>
	</form>
</div>
