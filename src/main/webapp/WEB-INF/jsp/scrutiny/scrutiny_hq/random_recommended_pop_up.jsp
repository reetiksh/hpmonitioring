<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
  .btn-outline-custom {
    color: #495057;
    border: 1px solid #ced4da;
    text-align: left;
  }
</style>

<div class="modal fade" id="randomRecommendScrutinyCaseModal" tabindex="-1"
     role="dialog" aria-labelledby="randomRecommendScrutinyCaseModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">

      <div class="modal-header">
        <h5 class="modal-title" id="randomRecommendScrutinyCaseModalTitle">
          <b>Recommend to Verifier for Mandatory Scrutiny</b>
        </h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>

      <div id="appealRevisionRejectRemarksMissingTagLine"
           style="color:red; display:none; padding-left:10px; padding-top:10px;">
        Please enter remarks.
      </div>

      <div class="modal-body">
        <form method="POST" id="recommendCaseForScrutinyDetails" action="random_recommend_for_scrutiny">
          <input type="hidden" id="recommendScrutinyGstin" name="recommendScrutinyGstin">
          <input type="hidden" id="recommendScrutinyPeriod" name="recommendScrutinyPeriod">
          <input type="hidden" id="recommendScrutinyCaseReportingDate" name="recommendScrutinyCaseReportingDate">
          <input type="hidden" id="recommendScrutinyRemark" name="recommendScrutinyRemark">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

          <div class="form-group" style="margin-bottom:0;">
            <label for="appealRevisionRejectRemarksValue" class="col-form-label">
              Remarks <span style="color:red;">*</span>
            </label>
          </div>
          <textarea class="form-control" id="appealRevisionRejectRemarksValue" name="appealRevisionRejectRemarksValue"
                    placeholder="Remarks" maxlength="1000"></textarea>
        </form>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="recommendSubmitBtn"
                onclick="submitAppealRevisionRejectDetails()">Submit</button>
      </div>
    </div>
  </div>
</div>

<script>
  // Optional helper to open & seed the modal
  function openRecommendScrutinyModal(gstin, period, caseReportingDate) {
    $('#recommendScrutinyGstin').val(gstin);
    $('#recommendScrutinyPeriod').val(period);
    $('#recommendScrutinyCaseReportingDate').val(caseReportingDate);
    $('#appealRevisionRejectRemarksValue').val('');
    $('#appealRevisionRejectRemarksMissingTagLine').hide();
    $('#randomRecommendScrutinyCaseModal').modal('show');
  }

  // Hide error once user starts typing
  $('#appealRevisionRejectRemarksValue').on('input', function () {
    $('#appealRevisionRejectRemarksMissingTagLine').hide();
  });

  function submitAppealRevisionRejectDetails() {
    const $btn = $('#recommendSubmitBtn');
    const val = ($('#appealRevisionRejectRemarksValue').val() || '').trim();

    if (!val) {
      $('#appealRevisionRejectRemarksMissingTagLine').show();
      return;
    }

    // Pass trimmed value to hidden field the server uses
    $('#recommendScrutinyRemark').val(val);

    $.confirm({
      title: 'Confirm!',
      content: 'Do you want to proceed ahead with recommending this case for mandatory scrutiny?',
      buttons: {
        submit: function () {
          $btn.prop('disabled', true);
          $('#randomRecommendScrutinyCaseModal').modal('hide'); // correct modal id
          setTimeout(function () {
            document.getElementById('recommendCaseForScrutinyDetails').submit();
          }, 300);
        },
        close: function () {
          $.alert('Canceled!');
        }
      }
    });
  }
</script>
