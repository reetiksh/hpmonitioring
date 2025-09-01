<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<script>
  $(function () {
    bsCustomFileInput.init();
  });

  $(document).ready(function() {
    var workingDate = '${enforcementCaseDateDocumentDetails.actionDate}';
    var pdfFile = '${enforcementCaseDateDocumentDetails.actionFileName}';
    var recoveryStageARN ='${enforcementCaseDateDocumentDetails.recoveryStageArn}';
       var recoveryStageid = $('#recoveryStageId').val();
    var updateButton = document.querySelector(".btn.btn-primary");
    var pdfFileDiv = document.getElementById("pdfFileDiv");
    var divElement = document.getElementById("pdfFileDownloadDiv");
    var spanElement = document.getElementById("pdfFileName");
    if(workingDate.length > 0) {
        $("#workingDate").prop("readonly", true);
        setTimeout(function () {
      if (recoveryStageid) {
        $('#recoveryStage').val(recoveryStageid).selectpicker('refresh').prop("disabled", true);
      }
    }, 100);
        $(".recoveryclass").prop("disabled", true);
        $(".ibtnDel").prop("disabled", true);
        updateButton.disabled = true;
     
 
    }
    
  
    if(pdfFile.length > 0) {
      pdfFileDiv.style.display = "none";
      divElement.style.display = "block";
      spanElement.textContent = pdfFile;
    }
    
  });

   	$(function () {
		$('.selectpicker').selectpicker();
	});

  $('form').on('submit', function(oEvent) {
    oEvent.preventDefault();

    var pdfFile = $("#pdfFile").val();
    if(!VerifyUploadSizeIsOK("pdfFile", 10485760)){
      // $("#pdf_alert").html("Please upload pdf file upto 10MB").css("color","red");
      // setTimeout(function() {
      //   $("#pdf_alert").fadeOut();
      // }, 5000);
      var x = document.getElementById("message1");
      if(x.style.display === "none"){
        $("#alertMessage1").html("Please upload pdf file upto 10MB").css("color","#fffff");
        x.style.display = "block";
        showDiv();
      }
      return;
    }

    $.confirm({
      title : 'Confirm!',
      content : 'Do you want to proceed ahead with updating Panchnama details?',
      buttons : {
        submit : function() {
          oEvent.currentTarget.submit();
        },
        close : function() {
          $.alert('Canceled!');
        }
      }
    });
  });

  function VerifyUploadSizeIsOK(UploadFieldID, MaxSizeInBytes){
	  var fld = document.getElementById(UploadFieldID);
	  if( fld.files && fld.files.length == 1 && fld.files[0].size > MaxSizeInBytes ){
			return false;
	  }
	  return true;
	}
 


  $(document).ready(function() {
		$("#workingDate").on("change", function() {
			var effectiveDate = $("#workingDate").val();
			var now = new Date();
      var previousStatusDate = new Date("${previousStatusDate}");
			now.setHours(23, 59, 59, 999);
			const givenDate = new Date(effectiveDate);
			
			if (givenDate > now) {
				$("#workingDate").val('');

				var x = document.getElementById("message1");
				if(x.style.display === "none"){
					$("#alertMessage1").html("Future date is not allowed").css("color","#fffff");
					x.style.display = "block";
					showDiv();
				}
			} else if (givenDate < previousStatusDate) {
        $("#workingDate").val('');

        var x = document.getElementById("message1");
				if(x.style.display === "none"){
					$("#alertMessage1").html("The entered date cannot be before " + formatDate(previousStatusDate)).css("color","#fffff");
					x.style.display = "block";
					showDiv();
				}
      }
		});
	});

  function showDiv(){
		$("#message1").fadeTo(2000, 500).slideUp(500, function() {
			$("#message1").slideUp(500);
		});
	}

  function formatDate(date) {
    var day = String(date.getDate()).padStart(2, '0');
    var month = String(date.getMonth() + 1).padStart(2, '0');
    var year = date.getFullYear();
    return day + "-" + month + "-" + year;
  }

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

</script>
<div class="card-body" style="background-color: #f1f1f1; border-radius: 3px; box-shadow: 2px 2px 4px 1px #8888884d;">
  <p class="h5">Panchnama</p>
  <hr>
  <form method="POST" name="caseUpdationActionForm" id="caseUpdationActionForm" action="/enforcement_fo/update_enforcement_case_details" enctype="multipart/form-data" >
    <div class="row">
      <div class="col-lg-12">
          <div class="row">
            <div class="col-lg-2">
              <div class="form-group">
                <label for="workingDate">Date<span style="color: red;"> *</span></label>
                <c:set var="isoDate"><fmt:formatDate value='${enforcementCaseDateDocumentDetails.actionDate}' pattern='yyyy-MM-dd'/></c:set>
                <input type="date" class="form-control" onchange="checkdatereporting(this)" id="workingDate" name="workingDate" value="${isoDate}"  required/>
              </div>
            </div>
            <div class="col-lg-10">
              <div class="form-group">
                <label for="pdfFile">Supporting Document<span style="color: red;"> *</span><span id="pdf_alert"></span></label>
                <div class="input-group" id="pdfFileDiv">
                    <div class="custom-file">
                      <input type="file" class="custom-file-input" id="pdfFile" name="pdfData.pdfFile" accept=".pdf" required/>
                      <label class="custom-file-label" for="pdfFile">Choose PDF (size upto 10MB)</label> 
                    </div>
                </div>
                <div id="pdfFileDownloadDiv" style="display: none;">
                  <a href="/enforcement_fo/downloadHqPdfFile?fileName=${enforcementCaseDateDocumentDetails.actionFileName}">
                    <img class="animation__shake" src="/static/image/PDF_file_icon.png" alt="pdf" height="40" width="34">  <span id="pdfFileName"></span>
                  </a>
                </div>
              </div>
            </div>
            <!-- <div class="col-lg-12">
              <c:if test="${not empty amdDocument.commentFromL2Officer}">
                <div class="form-group">
                  <label for="comment">Remarks from Allocating Officer </label>
                  <input type="text" class="form-control" id="comment" name="comment" value="${amdDocument.commentFromL2Officer}" readonly/>
                </div>
              </c:if>
            </div> -->
          </div>
            <div class="row">
              
						<c:if test="${not empty enforcementCaseDateDocumentDetails.recoveryStageArn}">
					<c:set var="strlst" value="${enforcementCaseDateDocumentDetails.recoveryStageArn}"></c:set>
					<c:set var="lst" value="${fn:split(strlst,',')}"></c:set>
				</c:if>
				<input type="hidden" id="arnLength" value="${fn:length(lst)}">

	<div class="col-md-3">
  <div class="form-group">
    <c:if test="${not empty enforcementCaseDateDocumentDetails.recoveryStage}">
      <input type="hidden" id="recoveryStageId" value="${enforcementCaseDateDocumentDetails.recoveryStage.id}" />
    </c:if>

    <label for="recoveryStage">Recovery Stage <span style="color: red">*</span></label>
    <select id="recoveryStage" class="form-control selectpicker recoveryclass" name="recoveryStage" data-live-search="true" title="Please select Recovery Stage">
      <option data-tokens="recoverystage" value="0" disabled>Select</option>
      <c:forEach items="${listRecovery}" var="categories">
        <option data-tokens="${categories.name}" value="${categories.id}">${categories.name}</option>
      </c:forEach>
    </select>
  </div>
</div>

           <div class="form-group">
                <label >Recovery Stage ARN<span style="color: red;"> *</span></label>
                <table id="myTable" class="table order-list">
							<thead>
							</thead>
							<tbody>
							</tbody>
              		<tfoot>

								<c:if test="${fn:length(lst) > 0}">
									<c:forEach items="${lst}" varStatus="loop" var="lst">
										<tr>
											<td><input type="text" class="form-control recoveryclass" value="${fn:trim(lst)}"
													id="recoveryStageARN" name="recoveryStageARN[${loop.index}]"
													placeholder="Please enter Recovery Stage ARN" maxlength="15" pattern="[A-Za-z0-9]{15}"
													title="Please enter 15-digits alphanumeric Recovery Stage ARN" required /></td>
											<td><input type="button" class="ibtnDel btn btn-md btn-danger" value="Delete"></td>
										</tr>
									</c:forEach>
								</c:if>

								<tr>
									<td colspan="5" style="text-align: left;">
										<input type="button" class="btn btn-primary" id="addrow" value="Add Row" />
									</td>
								</tr>

							</tfoot>
						</table>
              </div>
          </div>
      </div>
      <div class="col-lg-12 alert alert-danger alert-dismissible fade show" role="alert" id="message1" style="max-height: 500px; overflow-y: auto; display: none;">
        <span id="alertMessage1"></span>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
      <input type="hidden" name="GSTIN" id="GSTIN" value="${enforcementCaseDetails.id.GSTIN}" />
      <input type="hidden" name="period" id="period" value="${enforcementCaseDetails.id.period}" />
      <c:set var="formattedCaseReportingDate"><fmt:formatDate value='${enforcementCaseDetails.id.caseReportingDate}' pattern='yyyy-MM-dd'/></c:set>
      <input type="hidden" name="caseReportingDate" id="caseReportingDate" value="${formattedCaseReportingDate}" />
      <input type="hidden" name="updateStatusId" id="updateStatusId" value="${activeActionPannelId}" />
    </div>
    <hr>
    <div class="row">
      <div class="col-lg-12">
        <div class="float-right">
            <button type="submit" class="btn btn-primary" id="updateButton">Update</button>
        </div>
      </div>
    </div>
  </form>
</div>