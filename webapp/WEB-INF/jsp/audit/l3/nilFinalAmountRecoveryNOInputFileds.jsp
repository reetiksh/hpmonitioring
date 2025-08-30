<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
<script type="text/javascript">
  $('.selectpicker').selectpicker();

  $(document).ready(function() {
    if('${nonEditable}'==='true'){
      $('input').attr('readonly', true);
      $('select').attr('disabled', true);
      $('button').attr('disabled', true); 
    }

    if('${raisedQuery}'!= null && '${raisedQuery}'!=''){
      var row = document.getElementById("row-" + '${counter}');
      if (row) {
        row.style.backgroundColor = '#ebd3d2';
      }
    }

    function calculateAmountToBeRecovered(counter) {
      let recoveredAmount = parseFloat($("#amount_recovered_" + counter).val()) || 0;
      let droppedAmount = parseFloat($("#dropped_amount_" + counter).val()) || 0;
      let amountInvolved = parseFloat($("#amount_involved_" + counter).val()) || 0;

      let amountToBeRecovered = amountInvolved - droppedAmount - recoveredAmount;
      $("#amountToBe_recovered_" + counter).val(amountToBeRecovered);
    }

    // Attach event listeners to input fields
    $(".recovered-amount").on("input", function() {
      let counter = $(this).attr('id').split('_')[2]; // Get the counter from the ID
      calculateAmountToBeRecovered(counter);

      $("#amountToBe_recovered_alert_" + counter).html("Updated").css("color","red");
      $("#amountToBe_recovered_alert_" + counter).fadeIn().delay(2000).fadeOut();
    });
  });

  function isNumberKey(event) {
    const key = event.key;
    const value = event.target.value;

    // Allow: backspace, delete, tab, escape, enter, and arrow keys
    if (
        key === 'Backspace' || 
        key === 'Delete' || 
        key === 'Tab' || 
        key === 'Escape' || 
        key === 'Enter' || 
        key === 'ArrowLeft' || 
        key === 'ArrowRight' || 
        key === 'ArrowUp' || 
        key === 'ArrowDown' ||
        // Allow: Ctrl+A, Ctrl+C, Ctrl+V, Ctrl+X
        (event.ctrlKey === true && (key === 'a' || key === 'c' || key === 'v' || key === 'x'))
    ) {
        // let it happen, don't do anything
        return true;
    }

    // Ensure that it is a number and stop the keypress if it isn't
    if (!/[0-9]/.test(key)) {
        event.preventDefault();
        return false;
    }

    // Check if the length is within limit
    if (value.length > 12) {
        event.preventDefault();
        return false;
    }

    return true;
  }

  function disableCopyPaste(e) {
      e.preventDefault();
  }
</script>

<div class="row" id="row-${counter}" style="background-color: #e3e7ef; border-radius: 10px; padding: 15px; box-shadow: 2px 2px 4px 1px #5a57574d;">
  <div class="col-md-12 row">
    <div class="col-md-8" style="margin-bottom: 10px;">
      <label>Para Category<span style="color: red;"> *</span></label>
      <select name="para[${counter}][0]" class="selectpicker col-md-6" data-live-search="true" title="Please Select Para Category" required>
        <c:forEach items="${paraCategoryList}" var="para">
          <c:choose>
            <c:when test="${para.id eq paraCategory}">
              <option value="${para.id}" selected="selected">${para.name}</option>
            </c:when>
            <c:otherwise>
              <option value="${para.id}" disabled>${para.name}</option>
            </c:otherwise>
          </c:choose>
        </c:forEach>
      </select>
    </div>
    <c:if test="${not empty raisedQuery}">
      <div class="col-md-3">
        <span>Query Raised <i class="fa fa-exclamation-triangle" style="color:red"></i></span>
      </div>
    </c:if>
  </div>
  <div class="col-md-3">
    <label for="amount_involved_${counter}"> Amount Involved<span style="color: red;"> *</span></label>
    <input type="number" class="form-control recoveryclass inputDiv involved-amount" 
      id="amount_involved_${counter}"
      name="para[${counter}][1]" 
      value="${amount_involved}"
      placeholder="Total involved amount" readonly />
  </div>
  <div class="col-md-3">
    <label for="dropped_amount_${counter}"> Amount Dropped<span style="color: red;"> *</span></label>
    <input type="number" class="form-control recoveryclass inputDiv dropped-amount" 
      id="dropped_amount_${counter}"
      name="para[${counter}][3]" 
      value="${dropped_amount}"
      placeholder="Please enter dropped amount" 
      maxlength="11" 
      pattern="[0-9]{11}"
      title="Maximum 11 digits and without decimal" readonly />
  </div>
  <div class="col-md-3">
    <label for="amountToBe_recovered_${counter}"> Amount To Be Recovered<span style="color: red;"> *</span>&nbsp;<span id="amountToBe_recovered_alert_${counter}"></span></label>
    <input type="number" class="form-control recoveryclass inputDiv amount-to-be-recovered" 
      id="amountToBe_recovered_${counter}"
      name="para[${counter}][4]"
      value="${amountToBe_recovered}" 
      placeholder="Please enter the amount to be recovered" 
      maxlength="11" 
      pattern="[0-9]{11}"
      title="Maximum 11 digits and without decimal" readonly />
  </div>
  <div class="col-md-3">
    <label for="amount_recovered_${counter}"> Amount Recovered<span style="color: red;"> *</span></label>
    <input type="text" class="form-control recoveryclass inputDiv recovered-amount" 
      id="amount_recovered_${counter}"
      name="para[${counter}][2]" 
      value="${amount_recovered}"
      placeholder="Please enter recovered amount" 
      maxlength="11" 
      onkeydown="return isNumberKey(event)"
      onpaste="disableCopyPaste(event)" 
      oncopy="disableCopyPaste(event)"
      title="Maximum 11 digits and without decimal" required />
  </div>
  <!-- <input type="hidden" id="para_id_${counter}" name="para[${counter}][5]" value="${dar_Details_Id}"/>
  <input type="hidden" value="${counter}"/>
  <div class="col-md-12">
    <button type="button" class="ibtnDel btn btn-md btn-danger inputDiv" value="Delete">Delete</button>
  </div> -->
</div>