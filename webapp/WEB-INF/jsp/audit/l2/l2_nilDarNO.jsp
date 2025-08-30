<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<style>
  .inputDiv {
    margin: 5px;
  }
</style>
<script>
  $(document).ready(function () {
    buttonValue();
    
    if('${nonEditable}'==='true'){
      $('input').attr('readonly', true);
      $('select').attr('disabled', true);
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
  document.querySelectorAll('.form-check-input').forEach(function(checkbox) {
    checkbox.addEventListener('change', function() {
      let rowId = this.id.split('_')[1];
      let commentInput = document.getElementById('comment_' + rowId);
      if (commentInput) {
        if (this.checked) {
          // $("#submit_button").removeClass("btn-primary").addClass("btn-danger").text("Reject").attr('value', 'rejection');
          $("#submit_button_approve").hide();
          document.getElementById('comment_span_' + rowId).style.display = 'block';
          commentInput.setAttribute('required', 'required');
        } else {
          buttonValue();
          document.getElementById('comment_span_' + rowId).style.display = 'none';
          commentInput.removeAttribute('required');
        }
      } else {
        console.error("Comment input not found for Row ID:", rowId);
      }
    });
  });

  function buttonValue(){
    let size = '${darDetailsList.size()}';
    var raisedQueryFlag = false;
    for(let i=0 ; i<size ; i++){
      if(document.getElementById('flexCheckDefault_' + i).checked){
        raisedQueryFlag = true;
      }
    }

    if(raisedQueryFlag==false){
      // $("#submit_button").removeClass("btn-danger").addClass("btn-primary").text("Approve").attr('value', 'approval');
      
      //If MCM reject the DAR then L2 can't approve the DAR
      if('${auditMaster.action.status}' == 'recommendedForRaiseQuery'){
        $("#submit_button_approve").hide();
      } else {
        $("#submit_button_approve").show();
      }
    } else {
      // var button_value = $("#submit_button").val();
      // if(button_value==='approval'){
      //   $("#submit_button").removeClass("btn-primary").addClass("btn-danger").text("Reject").attr('value', 'rejection');
      // }
      $("#submit_button_approve").hide();
    }
  }
</script>
<div class="col-md-12">
  <div class="form-group">
    <c:if test="${not empty documentDetails.actionFilePath}">
      <div class="col-lg-12" style="padding: 0px 0px 10px 0px;">
        <div id="pdfFileDownloadDiv">
          <div class="row">
            <div class="col-lg-6 row">
              <div class="col-lg-12 row">
                <label class="col-lg-6" for="previous_document">Uploaded Document </label>
                <c:if test="${darRejected eq 'true'}">
                  <div class="col-md-6">
                    <span>Query Raised <i class="fa fa-exclamation-triangle" style="color:red"></i></span>
                  </div>
                </c:if>
              </div>
              <a href="/l2/downloadFile?fileName=${documentDetails.actionFilePath}">
                <img class="animation__shake" src="/static/image/PDF_file_icon.png" alt="pdf" height="40" width="34">  <span id="pdfFileName">${documentDetails.actionFilePath}</span>
              </a>
            </div>
            <c:if test="${not empty fn:trim(documentDetails.otherFilePath) and fn:length(fn:trim(documentDetails.otherFilePath)) > 0}">
              <div class="col-lg-6 row">
                <label class="col-lg-12" for="previous_document">Taxpayer Reply </label>
                <a href="/l2/downloadFile?fileName=${documentDetails.otherFilePath}">
                  <img class="animation__shake" src="/static/image/PDF_file_icon.png" alt="pdf" height="40" width="34">  <span id="pdfFileName">${documentDetails.otherFilePath}</span>
                </a>
              </div>
            </c:if>
          </div>
        </div>
      </div>
    </c:if>
    <div class="col-md-12" style="background-color: #d3d3d3;
      border-radius: 5px 5px 5px 5px;
      margin-bottom: 10px;">
      <label for="recoveryByDRC">Para(s):<span style="color: red">*</span> <span id="totalPara" style="color: #3d3d4a5e">Total: ${darDetailsList.size()}</span></label>
    </div>
    <div class="row">
      <div class="col-md-2">
        <label>Total Involved Amount:</label>
      </div>
      <div class="col-md-4">
        <div class="form-group">
          <input class="form-control" type="number" id="totalInvolvedAmount" name="totalInvolvedAmount" placeholder="Grand total involved amount" value ="${auditMaster.totalInvolvedAmount}" class="form-control" readonly />
        </div>
      </div>
    </div>
    <table id="myTable" class="table order-list">
      <tbody>
        <c:forEach items="${darDetailsList}" var="darDetails" varStatus="counter">
          <c:set var="bgColor" value="#e3e7ef" />
          <c:if test="${darDetails.raiseQuery eq 'true'}">
              <c:set var="bgColor" value="#ebd3d2" />
          </c:if>
          <div class="row" id="row-${counter.index}" style="background-color: ${bgColor}; border-radius: 10px; padding: 15px; box-shadow: 2px 2px 4px 1px #5a57574d; margin: 8px 8px 15px 8px;">
            <div class="col-md-12 row">
              <div class="col-md-8 row"  style="margin-bottom: 10px;">
                <label class="col-md-3 col-form-label"> Para Category</label>
                <input type="text" class="col-md-4 form-control inputDiv para-category" 
                  value="${darDetails.auditParaCategory.name}"
                  placeholder="Para Category" readonly />
              </div>
              <c:if test="${not empty darDetails.raiseQuery}">
                <div class="col-md-3">
                  <span>Query Raised <i class="fa fa-exclamation-triangle" style="color:red"></i></span>
                </div>
              </c:if>
            </div>
            <div class="col-md-3">
              <label for="amount_involved_${counter.index}"> Amount Involved</label>
              <input type="number" class="form-control inputDiv involved-amount" 
                value="${darDetails.amountInvolved}"
                placeholder="Total involved amount" readonly />
            </div>
            <div class="col-md-3">
              <label for="amount_recovered_${counter.index}"> Amount Recovered</label>
              <input type="number" class="form-control inputDiv recovered-amount" 
                placeholder="Please enter recovered amount" 
                maxlength="15" 
                pattern="[0-9]{11}"
                value="${darDetails.amountRecovered}"
                title="Maximum 11 digits and without decimal" readonly />
            </div>
            <div class="col-md-3">
              <label for="dropped_amount_${counter.index}"> Amount Dropped</label>
              <input type="number" class="form-control inputDiv dropped-amount" 
                value="${darDetails.amountDropped}"
                title="Maximum 11 digits and without decimal" readonly />
            </div>
            <div class="col-md-3">
              <label for="amountToBe_recovered_${counter.index}"> Amount To Be Recovered</label>
              <input type="number" class="form-control inputDiv amount-to-be-recovered" 
                value="${darDetails.amountToBeRecovered}"
                title="Maximum 11 digits and without decimal" readonly />
            </div>
            <c:if test="${not empty darDetails.commentL2}">
              <div class="col-md-12">
                <label for="previous_comment_from_l2">Previous Remarks</label>
                <textarea type="text" class="form-control inputDiv comment"
                  placeholder="Please enter your remarks" readonly>${darDetails.commentL2}</textarea>
              </div>
            </c:if>
            <c:if test="${not empty darDetails.commentMcm}">
              <div class="col-md-12">
                <label for="previous_comment_from_mcm">Remarks of MCM</label>
                <textarea type="text" class="form-control inputDiv comment"
                  placeholder="Please enter your remarks" readonly>${darDetails.commentMcm}</textarea>
              </div>
            </c:if>
            <div class="col-md-10">
              <label for="comment_${counter.index}"> Remarks<span id="comment_span_${counter.index}" style="color: red; float: right; display: none;">&nbsp;*</span></label>
              <input type="text" class="form-control inputDiv comment" 
                id="comment_${counter.index}"
                name="para[${counter.index}][1]"
                placeholder="Please enter your remarks"
                maxlength="100"
                title="Remarks Should be with in 100 letters"/>
            </div>
            <div class="col-md-2" style="padding: 40px;">
              <div class="form-check">
                <label class="form-check-label" for="flexCheckDefault_${counter.index}"> Raise Query</label>
                <c:if test="${not empty darDetails.raiseQuery && assignedFromMcm eq 'true'}">
                  <input class="form-check-input inputDiv" type="checkbox" value="true" name="para[${counter.index}][2]" placeholder="" id="flexCheckDefault_${counter.index}" checked disabled>
                  <input type="hidden" value="${darDetails.raiseQuery}" name="para[${counter.index}][2]">
                </c:if>
                <c:if test="${empty darDetails.raiseQuery || assignedFromMcm eq 'false'}">
                  <input class="form-check-input inputDiv" type="checkbox" value="true" name="para[${counter.index}][2]" placeholder="" id="flexCheckDefault_${counter.index}"/>
                </c:if>
              </div>
            </div>
            <input type="hidden" name="para[${counter.index}][0]" id="darId_${counter.index}" value="${darDetails.id}" />
          </div>
        </c:forEach>
        <div class="col-md-12" style="margin: 15px 0px 0px 0px;">
          <label for="comment">Other Remarks<span style="color: red;"> *</span><span id="comment_span" style="color: red; float: right; display: none;">&nbsp;*</span></label>
          <input type="text" class="form-control comment" 
            id="comment"
            name="comment" 
            value="${darDetails.comment}"
            placeholder="Please enter your remarks"
            maxlength="100"
            title="Comment Should be with in 100 letters" required/>
        </div>
      </tbody>
    </table>
  </div>
</div>