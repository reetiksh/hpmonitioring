<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<script type="text/javascript">
  $('.selectpicker').selectpicker();
  
  $(function () {
    bsCustomFileInput.init();
  });

  $('form').on('submit', function(oEvent) {
    oEvent.preventDefault();

    var pdfFile = $("#pdfFile").val();
    if(!VerifyUploadSizeIsOK("pdfFile", 10485760)){
      $("#pdf_alert").html("Please upload pdf file upto 10MB").css("color","red");
      setTimeout(function() {
        $("#pdf_alert").fadeOut();
      }, 5000);
      return;
    }

    var pdfFileClosureReport = $("#pdfFileClosureReport").val();
    if(!VerifyUploadSizeIsOK("pdfFileClosureReport", 10485760)){
      $("#pdfClosureReport_alert").html("Please upload pdf file upto 10MB").css("color","red");
      setTimeout(function() {
        $("#pdfClosureReport_alert").fadeOut();
      }, 5000);
      return;
    }

    $.confirm({
      title : 'Confirm!',
      content : 'Do you want to proceed ahead with recommendation of this case for Assessment & Adjudication?',
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

  $(document).ready(function () {
    if('${nonEditable}'==='true'){
      $('input').attr('readonly', true);
      $('select').attr('disabled', true);
      $('button').attr('disabled', true); 
    }
  });

  $(document).ready(function() {
		$("#workingDate").on("change", function() {
			var effectiveDate = $("#workingDate").val();
			var now = new Date();
      var previousStatusDate = new Date("${previousStatusDate}");
      now.setHours(23, 59, 59, 999);
			const givenDate = new Date(effectiveDate);

      var effectiveDate1 = $("#workingDateClosureReport").val();
			const givenDate1 = new Date(effectiveDate1);

      if(givenDate > givenDate1){
        $("#workingDateClosureReport").val('');
      }
			
			if (givenDate > now) {
				$("#workingDate").val('');
        $("#workingDateClosureReport").val('');

				var x = document.getElementById("message1");
				if(x.style.display === "none"){
					$("#alertMessage1").html("Future date is not allowed").css("color","#fffff");
					x.style.display = "block";
					showDiv();
				}
			} else if (givenDate < previousStatusDate) {
        $("#workingDate").val('');
        $("#workingDateClosureReport").val('');

        var x = document.getElementById("message1");
				if(x.style.display === "none"){
					$("#alertMessage1").html("The entered date cannot be before " +  formatDate(previousStatusDate)).css("color","#fffff");
					x.style.display = "block";
					showDiv();
				}
      }
		});
	});

  $(document).ready(function() {
		$("#workingDateClosureReport").on("change", function() {
			var effectiveDate = $("#workingDateClosureReport").val();
			var now = new Date();
      var previousStatusDate = new Date("${previousStatusDate}");
      now.setHours(23, 59, 59, 999);
			const givenDate = new Date(effectiveDate);

      var recommendedWorkingDate = $("#workingDate").val();
      const recommendedWorkingDate1 = new Date(recommendedWorkingDate);
			
			if (givenDate > now) {
				$("#workingDateClosureReport").val('');

				var x = document.getElementById("message1");
				if(x.style.display === "none"){
					$("#alertMessage1").html("Future date is not allowed").css("color","#fffff");
					x.style.display = "block";
					showDiv();
				}
			} else if (givenDate < previousStatusDate) {
        $("#workingDateClosureReport").val('');

        var x = document.getElementById("message1");
				if(x.style.display === "none"){
					$("#alertMessage1").html("The closure report date cannot be before " +  formatDate(previousStatusDate)).css("color","#fffff");
					x.style.display = "block";
					showDiv();
				}
      } else if (givenDate < recommendedWorkingDate1) {
        $("#workingDateClosureReport").val('');

        var x = document.getElementById("message1");
				if(x.style.display === "none"){
					$("#alertMessage1").html("The closure report date cannot be before " +  formatDate(recommendedWorkingDate1)).css("color","#fffff");
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
</script>
<div class="card-body"  style="background-color: #f1f1f1; border-radius: 3px; box-shadow: 2px 2px 4px 1px #8888884d;">
  <form method="POST" name="caseUpdationActionForm" id="caseUpdationActionForm" action="/l3/update_audit_case_details" enctype="multipart/form-data" >
    <div class="row">
      <div class="col-lg-12">
        <div class="card-body"  style="background-color: #e9e9e9; border-radius: 3px; box-shadow: 2px 2px 4px 1px #8888884d; margin: 0px 0px 10px 0px;">
          <p class="h5">Recommended for Assessment/Adjudication</p>
          <hr>
            <div class="row">
              <div class="col-lg-12">
                  <div class="row">
                    <div class="col-lg-2">
                      <div class="form-group">
                        <label for="workingDate">Date<span style="color: red;"> *</span></label>
                        <c:set var="isoDate"><fmt:formatDate value='${amdDocument.actionDate}' pattern='yyyy-MM-dd'/></c:set>
                        <input type="date" class="form-control" id="workingDate" name="workingDate" value="${isoDate}" required/>
                      </div>
                    </div>
                    <div class="form-group col-lg-7 row">
                      <div class="col-lg-12">
                        <label>Select FO User<span style="color: red;"> *</span><span id="category_alert"></span></label>
                      </div>
                      <select id="foUserId" name="foUserId" class="selectpicker col-lg-12" data-live-search="true" title="Please Select FO User Name" required>
                        <c:forEach items="${foUserList}" var="value">
                          <c:choose>
                            <c:when test="${value.userId eq amdDocument.caseId.foUserDetailsForShowCauseNotice.userId}">
                              <option value="${value.userId}" selected>${value.userName} (${value.designation}) : ${value.loginId}</option>
                            </c:when>
                            <c:otherwise>
                              <option value="${value.userId}">${value.userName} (${value.designation}) : ${value.loginId}</option>
                            </c:otherwise>
                          </c:choose>
                        </c:forEach>
                      </select>
                    </div>
                    <div class="form-group col-lg-3">
                      <label for="workingDate">ARN Number<span style="color: red;"> *</span></label>
                      <input type="text" class="form-control" id="arn" name="arn" maxlength="15" minlength="15" placeholder="Please enter ARN number" value="${amdDocument.caseId.arnNumber}"
                        onkeydown="return /[A-Za-z0-9]/.test(event.key)" title="ARN Number should be alphanumeric(uppercase) and 15 digits long" required/>
                    </div>
                    <div class="col-lg-12">
                      <div class="form-group">
                        <c:if test="${empty nonEditable}">
                          <label for="pdfFile">Supporting Document<span style="color: red;"> *</span><span id="pdf_alert"></span></label>
                          <div class="input-group">
                              <div class="custom-file">
                                <input type="file" class="custom-file-input" id="pdfFile" name="pdfData.pdfFile" accept=".pdf" required/>
                                <label class="custom-file-label" for="pdfFile">Choose PDF (size upto 10MB)</label> 
                              </div>
                          </div>
                        </c:if>
                        <c:if test="${not empty amdDocument.actionFilePath}">
                          <div class="col-lg-12" style="padding: 0px 0px 10px 0px;">
                            <div id="pdfFileDownloadDiv">
                              <div class="col-lg-12 row">
                                <label class="col-lg-3" for="previous_document">Uploaded Document </label>
                                <c:if test="${darRejected eq 'true'}">
                                  <div class="col-md-3">
                                    <span>Query Raised <i class="fa fa-exclamation-triangle" style="color:red"></i></span>
                                  </div>
                                </c:if>
                              </div>
                              <a href="/l3/downloadFile?fileName=${amdDocument.actionFilePath}">
                                <img class="animation__shake" src="/static/image/PDF_file_icon.png" alt="pdf" height="40" width="34">  <span id="pdfFileName">${amdDocument.actionFilePath}</span>
                              </a>
                            </div>
                          </div>
                        </c:if>
                      </div>
                    </div>
                  </div>
              </div>
            </div>
        </div>
      </div>
      <c:if test="${empty amdDocument}">
        <div class="col-lg-12">
          <div class="card-body"  style="background-color: #e9e9e9; border-radius: 3px; box-shadow: 2px 2px 4px 1px #8888884d;">
            <p class="h5">Closure Report</p>
            <hr>
              <div class="row">
                <div class="col-lg-12">
                    <div class="row">
                      <div class="col-lg-2">
                        <div class="form-group">
                          <label for="workingDateClosureReport">Date<span style="color: red;"> *</span></label>
                          <input type="date" class="form-control" id="workingDateClosureReport" name="workingDateClosureReport" required/>
                        </div>
                      </div>
                      <div class="col-lg-10">
                        <div class="form-group">
                          <label for="pdfFileClosureReport">Supporting Document<span style="color: red;"> *</span><span id="pdfClosureReport_alert"></span></label>
                          <div class="input-group">
                              <div class="custom-file">
                                <input type="file" class="custom-file-input" id="pdfFileClosureReport" name="pdfDataClosureReport.pdfFile" accept=".pdf" required/>
                                <label class="custom-file-label" for="pdfFile">Choose PDF (size upto 10MB)</label> 
                              </div>
                          </div>
                        </div>
                      </div>
                    </div>
                </div>
              </div>
          </div>
        </div>
      </c:if>
      <br>
      <div class="col-lg-12 alert alert-danger alert-dismissible fade show" role="alert" id="message1" style="max-height: 500px; overflow-y: auto; display: none;">
        <span id="alertMessage1"></span>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
      <input type="hidden" name="transferId" id="transferId" value="${transferRole.id}" />
      <input type="hidden" name="caseId" id="caseId" value="${caseId}" />
      <input type="hidden" name="updateStatusId" id="updateStatusId" value="${activeStatusId}" />
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