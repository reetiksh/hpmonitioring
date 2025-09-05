<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<style>
  .btn-outline-custom { color:#495057; border:1px solid #ced4da; text-align:left; }
</style>

<script>
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => { if (e.ctrlKey && e.key === 'u') e.preventDefault(); });
  document.addEventListener('keydown', e => { if (e.key === 'F12') e.preventDefault(); });

  $(function () {
    function disableBack(){ window.history.forward() }
    window.onload = disableBack();
    window.onpageshow = function (evt) { if (evt.persisted) disableBack() }
  });

  // Disable refresh
  document.onkeydown = function (e) {
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) e.preventDefault();
  };
</script>

<script>
  // Hook the form submit and show confirm; submit on confirmation
  $(function () {
    $('#recoveryForm').on('submit', function (oEvent) {
      oEvent.preventDefault();
      const form = this;

      $.confirm({
        title: 'Confirm!',
        content: 'Do you want to proceed ahead with Raise Query?',
        buttons: {
          submit: {
            btnClass: 'btn-primary',
            action: function () {
              form.submit(); // <-- actually submit now
            }
          },
          close: function () {
            $.alert('Canceled!');
          }
        }
      });
    });
  });

  // (Optional helper) call this to open the modal and preset hidden fields
  function openRaiseQueryModal(gst, date, period) {
    $('#gstno').val(gst);
    $('#date').val(date);
    $('#period').val(period);
    const modalEl = document.getElementById('raiseQueryModal');
    const modal = new bootstrap.Modal(modalEl);
    modal.show();
  }
</script>

<div class="modal fade" id="raiseQueryModal" tabindex="-1" aria-labelledby="raiseQueryModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">

      <div class="modal-header">
        <h5 class="modal-title" id="raiseQueryModalTitle">Raise Query</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <form method="POST" name="recoveryForm" id="recoveryForm" action="raise_query_recovery_cases">
        <div class="modal-body">
          <input type="hidden" id="gstno" name="gstno">
          <input type="hidden" id="date"  name="date">
          <input type="hidden" id="period" name="period">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

          <div class="form-group" style="margin-bottom:0;">
            <label for="remarksId" class="col-form-label">Remarks<span style="color:red;"> *</span></label>
          </div>
          <textarea class="form-control" id="remarksId" name="otherRemarks" placeholder="Remarks" required></textarea>
        </div>

        <div class="modal-footer">
          <button type="submit" class="btn btn-danger" id="raiseQueryCaseBtn">Raise Query</button>
        </div>
      </form>

    </div>
  </div>
</div>
