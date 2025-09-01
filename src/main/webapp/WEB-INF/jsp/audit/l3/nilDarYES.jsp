<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script>
  $(function () {
    bsCustomFileInput.init();
  });

  $(document).ready(function() {
    $(".pdf-file-upload").on("input", function() {
      var pdfFile = $(this).val();
      if(!VerifyUploadSizeIsOK("pdfFile", 10485760)){
        $("#pdf_alert").html("Please upload pdf file upto 10MB").css("color","red");
        $(this).val('');
        $("#pdf_alert").fadeIn().delay(5000).fadeOut();
        return;
      }
    });
    $(".reply-pdf-file-upload").on("input", function() {
      console.log("In reply pdf checking!");
      var pdfFileReply = $(this).val();
      if(!VerifyUploadSizeIsOK("pdfFileReply", 10485760)){
        $("#reply_pdf_alert").html("Please upload pdf file upto 10MB").css("color","red");
        $(this).val('');
        $("#reply_pdf_alert").fadeIn().delay(5000).fadeOut();
        return;
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
</script>
<div class="form-group">
  <c:if test="${not empty dateDocumentDetails.actionFilePath}">
    <div class="col-lg-12" style="padding: 0px 0px 10px 0px;">
      <div id="pdfFileDownloadDiv">
        <div class="row">
          <div class="col-lg-6 row">
            <div class="col-lg-12 row">
              <label class="col-lg-6" for="previous_document">Previous Uploaded Document </label>
              <c:if test="${darRejected eq 'true'}">
                <div class="col-md-6">
                  <span>Query Raised <i class="fa fa-exclamation-triangle" style="color:red"></i></span>
                </div>
              </c:if>
            </div>
            <a href="/l3/downloadFile?fileName=${dateDocumentDetails.actionFilePath}">
              <img class="animation__shake" src="/static/image/PDF_file_icon.png" alt="pdf" height="40" width="34">  <span id="pdfFileName">${dateDocumentDetails.actionFilePath}</span>
            </a>
          </div>
          <c:if test="${not empty fn:trim(dateDocumentDetails.otherFilePath) and fn:length(fn:trim(dateDocumentDetails.otherFilePath)) > 0}">
            <div class="col-lg-6">
              <label class="col-lg-12" for="previous_document">Uploaded Taxpayer Reply </label>
              <a href="/l3/downloadFile?fileName=${dateDocumentDetails.otherFilePath}">
                <img class="animation__shake" src="/static/image/PDF_file_icon.png" alt="pdf" height="40" width="34">  <span id="pdfFileName">${dateDocumentDetails.otherFilePath}</span>
              </a>
            </div>
          </c:if>
        </div>
      </div>
    </div>
    <c:if test="${not empty dateDocumentDetails.commentFromL2Officer}">
      <label for="commentL2"> Remarks of Allocating Officer</label>
      <input type="text" class="col-lg-12 form-control commentL2"
        value="${dateDocumentDetails.commentFromL2Officer}" readonly />
        <br>
    </c:if>
  </c:if>
  <c:if test="${empty nonEditable}">
    <div class="row">
      <div class="col-md-6">
        <label for="pdfFile">Supporting Document<span style="color: red;"> *</span><span id="pdf_alert"></span></label>
        <div class="input-group">
          <div class="custom-file">
            <input type="file" class="custom-file-input pdf-file-upload" id="pdfFile" name="pdfData.pdfFile" accept=".pdf" required>
            <label class="custom-file-label" for="pdfFile">Choose PDF (size upto 10MB)</label> 
          </div>
        </div>
      </div>
      <div class="col-md-6">
        <label for="pdfFileReply">Taxpayer Reply <span id="reply_pdf_alert"></span></label>
        <div class="input-group">
          <div class="custom-file">
            <input type="file" class="custom-file-input reply-pdf-file-upload" id="pdfFileReply" name="pdfDataReply.pdfFile" accept=".pdf">
            <label class="custom-file-label" for="pdfFileReply">Choose PDF (size upto 10MB)</label> 
          </div>
        </div>
      </div>
    </div>
  </c:if>
</div>