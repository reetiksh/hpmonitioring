<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<style>
  .inputDiv {
    margin: 5px;
  }
</style>
<script>
  $(function () {
    bsCustomFileInput.init();
  });

  
  $(document).ready(function () {
		var counter = $("#arnLength").val();

    checkUserSession();
    loadTheInputfieldJspPage(counter);  
    counter++;

    $("#myTable").on('input', '.inputDiv', function () {
        calculateSum(counter);
    });

    $("#addrow").on("click", function () {

      checkUserSession();

      // console.log("checkUserSession called!");

      var newRow = $("<tr>");
      var cols = '<td style="border-top: 0px">';

      $.ajax({
          url: 'loadNilDarNOInputFields',
          type: 'GET',
          data: { counter: counter },
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

      counter++;
      $("#arnLength").val(counter);
      $("#totalPara").html(" Total: " + counter).css({"color": "#3d3d4a5e"});

      if (counter === 1) {
        $(".ibtnDel").prop("disabled", true);
        }
    });


		$("table.order-list").on("click", ".ibtnDel", function (event) {
      if (counter > 1) {
        $(this).closest("tr").remove();
        counter -= 1
        $("#arnLength").val(counter);
        if(counter!=0){
          $("#totalPara").html(" Total: " + counter).css({"color": "#3d3d4a5e"});
        } else {
          $("#totalPara").html("");
        }

        // Enable the last delete button
        $(".ibtnDel").last().prop("disabled", false);

        if(counter == 1){
          $(".ibtnDel").prop("disabled", true);
        }
      }

      calculateSum(counter);

		});

    $(".pdf-file-upload").on("input", function() {
      console.log("In pdf checking!");
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

  function loadTheInputfieldJspPage(counter){
    var newRow = $("<tr>");
    var cols = '<td style="border-top: 0px">';

    $.ajax({
        url: 'loadNilDarNOInputFields',
        type: 'GET',
        data: { counter: counter },
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

    counter++;
    $("#arnLength").val(counter);
    $("#totalPara").html(" Total: " + counter).css({"color": "#3d3d4a5e"});

    if (counter === 1) {
      $(".ibtnDel").prop("disabled", true);
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

<input type="hidden" id="arnLength" value="${fn:length(lst)}">

<div class="col-md-12">
  <div class="form-group">
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
    <br>
    <!-- Para code Start -->
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
          <input type="number" id="totalInvolvedAmount" name="totalInvolvedAmount" placeholder="Grand total involved amount" class="form-control" readonly />
        </div>
      </div>
    </div>
    <table id="myTable" class="table order-list">
      <tfoot>
        <c:if test="${fn:length(lst) > 0}">
          <c:forEach items="${lst}" varStatus="loop" var="lst">
            <tr>
              <td><input type="text" class="form-control recoveryclass" value="${fn:trim(lst)}"
                  id="para" name="para[${loop.index}]"
                  placeholder="Please enter Recovery Stage ARN" maxlength="15" pattern="[A-Za-z0-9]{15}"
                  title="Please enter 15-digits alphanumeric Recovery Stage ARN" required /></td>
              <td><input type="button" class="ibtnDel btn btn-md btn-danger" value="Delete"></td>
            </tr>
          </c:forEach>
        </c:if>
        <tr>
          <td colspan="5" style="text-align: left;">
            <input type="button" class="btn btn-primary" id="addrow" value="Add Para" />
          </td>
        </tr>
      </tfoot>
    </table>
  </div>
</div>