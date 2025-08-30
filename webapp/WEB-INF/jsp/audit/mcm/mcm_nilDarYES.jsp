<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script>
  $(document).ready(function () {
    if('${nonEditable}'==='true'){
      $('input').attr('readonly', true);
      $('select').attr('disabled', true);
      $('button').attr('disabled', true); 
      $('input[type="checkbox"]').attr('disabled', true);
    } 
  });
  
  // document.querySelectorAll('.form-check-input').forEach(function(checkbox) {
  //   checkbox.addEventListener('change', function() {
  //     let commentInput = document.getElementById('comment');
  //     if (commentInput) {
  //       if (this.checked) {
  //         $("#submit_button").removeClass("btn-primary").addClass("btn-danger").text("Raise Query").attr('value', 'recommendation for raising query');
  //         // document.getElementById('comment_span').style.display = 'block';
  //         commentInput.setAttribute('required', 'required');
  //       } else {
  //         $("#submit_button").removeClass("btn-danger").addClass("btn-primary").text("Approval Recommended").attr('value', 'recommendation for approval');
  //         // document.getElementById('comment_span').style.display = 'none';
  //         commentInput.removeAttribute('required');
  //       }
  //     } else {
  //       console.error("Comment input not found for Row ID:", rowId);
  //     }
  //   });
  // });
</script>
<div class="form-group row">
  <div class="col-md-6">
    <label class="col-lg-12" for="pdfFile">Supporting Document<span style="color: red;"> *</span><span id="pdf_alert"></span></label>
    <a href="/mcm/downloadFile?fileName=${amdDocument.actionFilePath}">
      <img class="animation__shake" src="/static/image/PDF_file_icon.png" alt="pdf" height="40" width="34">  <span id="pdfFileName">${documentDetails.actionFilePath}</span>
    </a>
  </div>
  <div class="col-md-6">
    <c:if test="${not empty fn:trim(documentDetails.otherFilePath) and fn:length(fn:trim(documentDetails.otherFilePath)) > 0}">
      <label class="col-lg-12" for="previous_document">Taxpayer Reply </label>
      <a href="/l2/downloadFile?fileName=${documentDetails.otherFilePath}">
        <img class="animation__shake" src="/static/image/PDF_file_icon.png" alt="pdf" height="40" width="34">  <span id="pdfFileName">${documentDetails.otherFilePath}</span>
      </a>
    </c:if>
  </div>
  <!-- <div class="col-md-2" style="padding: 40px; margin: 15px 0px 0px 0px;">
    <div class="form-check">
      <label class="form-check-label" for="darApproval">Raise Query</label>
      <input class="form-check-input" style="margin: 7px 0px 0px 4px;" type="checkbox" value="darReject" name="darApproval" placeholder="" id="darApproval">
    </div>
  </div> -->
</div>