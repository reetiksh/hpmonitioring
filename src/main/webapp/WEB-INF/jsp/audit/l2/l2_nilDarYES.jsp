<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script>
  $(function () {
    bsCustomFileInput.init();
  });


  $(document).ready(function () {
    if('${nonEditable}'==='true'){
      $('input').attr('readonly', true);
      $('button').attr('disabled', true); 
      $('input[type="checkbox"]').attr('disabled', true);
    } 

    //If MCM approve the DAR then L2 can't reject the DAR
    if('${auditMaster.action.status}' == 'recommendedForApproval'){
      $('.form-check-input').prop('disabled', true);
      $("#submit_button_reject").hide();
    }
    //If MCM reject the DAR then L2 can't approve the DAR
    if('${auditMaster.action.status}' == 'recommendedForRaiseQuery'){
      $("#submit_button_approve").hide();
    }
  });

  var darRejected = '${darRejected}';
  if(darRejected){
    $('input[type="checkbox"]').attr('checked', true);
    $("#submit_button").removeClass("btn-primary").addClass("btn-danger").text("Reject").attr('value', 'rejection');
    $('input[type="checkbox"]').attr('disabled', true);
  }
  
  // document.querySelectorAll('.form-check-input').forEach(function(checkbox) {
  //   checkbox.addEventListener('change', function() {
  //     let commentInput = document.getElementById('comment');
  //     if (commentInput) {
  //       if (this.checked) {
  //         $("#submit_button").removeClass("btn-primary").addClass("btn-danger").text("Reject").attr('value', 'rejection');
  //         // document.getElementById('comment_span').style.display = 'block';
  //         commentInput.setAttribute('required', 'required');
  //       } else {
  //         $("#submit_button").removeClass("btn-danger").addClass("btn-primary").text("Approve").attr('value', 'approval');
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
  <div class="col-md-5">
    &nbsp;<label for="pdfFile">Supporting Document<span id="pdf_alert"></span></label>
    <div class="input-group col-md-12">
      <div class="custom-file">
        <a href="/l2/downloadFile?fileName=${documentDetails.actionFilePath}">
          <img class="animation__shake" src="/static/image/PDF_file_icon.png" alt="pdf" height="40" width="34">  <span id="pdfFileName">${documentDetails.actionFilePath}</span>
        </a>
      </div>
    </div>
  </div>
  <div class="row col-md-5">
    <c:if test="${not empty fn:trim(documentDetails.otherFilePath) and fn:length(fn:trim(documentDetails.otherFilePath)) > 0}">
      <label class="col-lg-12" for="previous_document">Taxpayer Reply </label>
      <a href="/l2/downloadFile?fileName=${documentDetails.otherFilePath}">
        <img class="animation__shake" src="/static/image/PDF_file_icon.png" alt="pdf" height="40" width="34">  <span id="pdfFileName">${documentDetails.otherFilePath}</span>
      </a>
    </c:if>
  </div>
  <div class="col-md-2">
    <c:if test="${darRejected eq 'true'}">
        <span>Query Raised <i class="fa fa-exclamation-triangle" style="color:red"></i></span>
    </c:if>
  </div>
  <div class="col-md-12" style="margin: 15px 0px 0px 0px;">
    <label for="comment"> Remarks<span style="color: red;"> *</span><span id="comment_span" style="color: red; float: right; display: none;">&nbsp;*</span></label>
    <input type="text" class="form-control comment" 
      id="comment"
      name="comment" 
      value="${darDetails.comment}"
      placeholder="Please enter your remarks"
      maxlength="100"
      title="Remarks Should be with in 100 letters" required/>
  </div>
  <!-- <div class="col-md-2" style="padding: 40px; margin: 15px 0px 0px 0px;">
    <div class="form-check">
      <label class="form-check-label" for="darApproval">Raise Query</label>
        <input class="form-check-input" style="margin: 7px 0px 0px 4px;" type="checkbox" value="true" name="darApproval" placeholder="" id="darApproval">
        <input type="hidden" value="${darRejected}" name="darApproval">
    </div>
  </div> -->
</div>