<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
  .btn-outline-custom {
    color: #495057;
    border: 1px solid #ced4da;
    text-align: left;
  }
  /* keep modal sizing consistent with your app if needed
  .modal-lg, .modal-xl { max-width: 900px; } */
</style>

<div class="modal fade" id="randomRecommendScrutinyCaseModal" tabindex="-1" role="dialog" aria-labelledby="randomRecommendScrutinyCaseModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">

      <!-- error tagline (hidden by default) -->
      <div id="appealRevisionRejectRemarksMissingTagLine"
           style="color: red; display: none; padding: 10px;">
        Please enter remarks.
      </div>

      <div class="modal-header">
        <h5 class="modal-title" id="appealRevisonRejectModalTitle">
          <b>Recommend to FO for Mandatory Scrutiny</b>
        </h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>

      <div class="modal-body">
        <form method="POST" id="recommendCaseForScrutinyDetails" action="random_recommend_for_scrutiny">
          <input type="hidden" id="recommendScrutinyGstin" name="recommendScrutinyGstin">
          <input type="hidden" id="recommendScrutinyPeriod" name="recommendScrutinyPeriod">
          <input type="hidden" id="recommendScrutinyCaseReportingDate" name="recommendScrutinyCaseReportingDate">
          <input type="hidden" id="recommendScrutinyRemark" name="recommendScrutinyRemark">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

          <div class="form-group mb-1">
            <label for="appealRevisionRejectRemarksValue" class="col-form-label">
              Remarks <span style="color: red;">*</span>
            </label>
          </div>

          <textarea class="form-control" id="appealRevisionRejectRemarksValue"
                    name="appealRevisionRejectRemarksValue" placeholder="Remarks"></textarea>
        </form>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="recommendScrutinySubmitBtn"
                onclick="submitAppealRevisionRejectDetails()">Submit</button>
      </div>
    </div>
  </div>
</div>

<script>
  // live hide error as user types
  (function () {
    const $ta = $('#appealRevisionRejectRemarksValue');
    const $err = $('#appealRevisionRejectRemarksMissingTagLine');
    $ta.on('input', function () {
      const v = ($ta.val() || '').trim();
      if (v.length > 0) $err.hide();
    });
  })();

  function submitAppealRevisionRejectDetails(){
    const $btn = $('#recommendScrutinySubmitBtn');
    const $ta  = $('#appealRevisionRejectRemarksValue');
    const val  = ($ta.val() || '').trim();

    if (!val) {
      $('#appealRevisionRejectRemarksMissingTagLine').show();
      $ta.focus();
      return;
    }

    // set hidden field with trimmed value once
    $('#recommendScrutinyRemark').val(val);

    $.confirm({
      title : 'Confirm!',
      content : 'Do you want to proceed ahead with recommending this case for mandatory scrutiny?',
      buttons : {
        submit : function () {
          // prevent double-submit
          $btn.prop('disabled', true);
          $('#randomRecommendScrutinyCaseModal').modal('hide');
          setTimeout(function () {
            document.getElementById('recommendCaseForScrutinyDetails').submit();
          }, 300);
        },
        close : function () { $.alert('Canceled!'); }
      }
    });
  }
</script>
