<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<style>
  .inputDiv {
    margin: 5px;
  }
</style>
<script>
  $(document).ready(function () {
    if('${nonEditable}'==='true'){
      $('input').attr('readonly', true);
      $('select').attr('disabled', true);
      $('button').attr('disabled', true); 
    } 

		var counter = $("#paraCount").val();

    //load all the existing submitted paras
    for(let i = 0; i<=counter ; i++){
      addRow(i, '${auditMaster.caseId}');
    }
    //save rest of the details for later use
    counter++;
    $("#paraCount").val(counter);
    $("#totalPara").html(" Total: " + counter).css({"color": "#3d3d4a5e"});

    //checking session is expired or active
    checkUserSession();

    //Calculate the grand total of the inputfields
    $("#myTable").on('input', '.inputDiv', function () {
        calculateSum(counter);
    });

    //add row button
    $("#addrow").on("click", function () {
      checkUserSession();
      addRow(counter, '');
      counter++;
      $("#paraCount").val(counter);
      $("#totalPara").html(" Total: " + counter).css({"color": "#3d3d4a5e"});
    });

    //delete para action button
		$("table.order-list").on("click", ".ibtnDel", function (event) {
        $(this).closest("tr").remove();
        counter -= 1
        $("#paraCount").val(counter);
        if(counter!=0){
          $("#totalPara").html(" Total: " + counter).css({"color": "#3d3d4a5e"});
        } else {
          $("#totalPara").html("");
        }

        // Enable the last delete button
        $(".ibtnDel").last().prop("disabled", false);

        //If no para exists then add a fresh one para
        if(counter == 0){
          checkUserSession();
          addRow(counter, '');
          counter++;
          $("#paraCount").val(counter);
          $("#totalPara").html(" Total: " + counter).css({"color": "#3d3d4a5e"});

          if (counter === 1) {
            $(".ibtnDel").prop("disabled", true);
          }
        }

      calculateSum(counter);
		});

    $(".pdf-file-upload").on("input", function() {
      var pdfFile = $(this).val();
      if(!VerifyUploadSizeIsOK("pdfFile", 10485760)){
        $("#pdf_alert").html("Please upload pdf file upto 10MB").css("color","red");
        $(this).val('');
        $("#pdf_alert").fadeIn().delay(5000).fadeOut();
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
  
  $(function () {
    bsCustomFileInput.init();
  });

  function addRow(counter, caseId){
    var newRow = $("<tr>");
    var cols = '<td style="border-top: 0px">';

    $.ajax({
        url: 'loadNilFinalAmountRecoveryNOInputFields',
        type: 'GET',
        data: { counter: counter, caseId: caseId },
        async: false,
        success: function (data) {
            cols += data;
        }
    });

    cols += '</td>';
    newRow.append(cols);
    $("table.order-list").append(newRow);

    // Disable previous delete buttons
    $(".ibtnDel").not(":last").prop("disabled", true);
    
  }

  function calculateSum(counter){
    if(counter > 0){
        let sum  = 0;
        for (let i = 0; i < counter; i++) {
          let amountInvolved = parseFloat($("#amount_involved_" + i).val()) || 0;
          sum += amountInvolved;
        }
        $("#totalInvolvedAmount").val(sum);
      }
  }

  function checkUserSession(){
    $.ajax({url: "/checkUserSession", async: false, success: function(result){

      // console.log("UserSession : " + result);
      if(result == 'No'){
        window.location.assign('/logout');
      }
		}});

  }
</script>
<c:if test="${not empty submittedData.paraStr}">
  <c:set var="strlst" value="${submittedData.paraStr}"></c:set>
  <c:set var="lst" value="${fn:split(strlst,',')}"></c:set>
</c:if>

<input type="hidden" id="paraCount" value="${paraCount}">

<div class="col-md-12">
  <div class="form-group">
    <c:if test="${not empty dateDocumentDetails.actionFilePath}">
      <div class="col-lg-12" style="padding: 0px 0px 10px 0px;">
        <div id="pdfFileDownloadDiv">
          <div class="col-lg-12 row">
            <label class="col-lg-3" for="previous_document">Uploaded Document </label>
          </div>
          <a href="/l3/downloadFile?fileName=${dateDocumentDetails.actionFilePath}">
            <img class="animation__shake" src="/static/image/PDF_file_icon.png" alt="pdf" height="40" width="34">  <span id="pdfFileName">${dateDocumentDetails.actionFilePath}</span>
          </a>
        </div>
      </div>
    </c:if>
    <c:if test="${empty nonEditable}">
      <label for="pdfFile">Supporting Document<span style="color: red;"> *</span><span id="pdf_alert"></span></label>
      <div class="input-group">
        <div class="custom-file">
          <input type="file" class="custom-file-input pdf-file-upload" id="pdfFile" name="pdfData.pdfFile" accept=".pdf" required>
          <label class="custom-file-label" for="pdfFile">Choose PDF (size upto 10MB)</label> 
        </div>
      </div>
    </c:if>
    <br>
    <div class="col-md-12" style="background-color: #d3d3d3;
      border-radius: 5px 5px 5px 5px;
      margin-bottom: 10px;">
      <label for="recoveryByDRC">Para(s):<span style="color: red">*</span> <span id="totalPara"></span></label>
    </div>
    <div class="row">
      <div class="col-md-2">
        <label>Total Involved Amount:</label>
      </div>
      <div class="col-md-4">
        <div class="form-group">
          <input type="number" id="totalInvolvedAmount" name="totalInvolvedAmount" value="${auditMaster.totalInvolvedAmount}" placeholder="Grand total involved amount" class="form-control" readonly />
        </div>
      </div>
    </div>
    <table id="myTable" class="table order-list">
      <tfoot>
        
        <!-- <tr>
          <td colspan="5" style="text-align: left;">
            <button type="button" class="btn btn-primary" id="addrow" value="Add Para" >Add Para</button>
          </td>
        </tr> -->
      </tfoot>
    </table>
  </div>
</div>