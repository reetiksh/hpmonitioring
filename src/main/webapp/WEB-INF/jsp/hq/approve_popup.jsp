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

  $(document).ready(function () {
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = function(evt){ if (evt.persisted) disableBack(); };
  });
  document.onkeydown = function (e) {
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) e.preventDefault();
  };
</script>

<!-- Bootstrap 5 / AdminLTE 4 modal markup -->
<div class="modal fade" id="confirmationApproveModal" tabindex="-1"
     aria-labelledby="confirmationApproveModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">

      <div class="modal-header">
        <h5 class="modal-title" id="confirmationApproveModalTitle">Approve Case</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <form method="POST" name="approveForm" id="approveForm"
            action="approve_recovery_cases" onsubmit="return validateForm()">

        <input type="hidden" id="app_gstno" name="gstno">
        <input type="hidden" id="app_date" name="date">
        <input type="hidden" id="app_period" name="period">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

        <div class="modal-footer">
          <button type="submit" class="btn btn-primary" id="okBtn">Yes</button>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" id="cancelBtn">No</button>
        </div>
      </form>

    </div>
  </div>
</div>

<!-- Optional: jQuery -> Bootstrap 5 modal bridge so $('#...').modal('show') still works -->
<script>
  (function ($) {
    if (!window.bootstrap) return;
    $.fn.modal = function (action) {
      return this.each(function () {
        const inst = bootstrap.Modal.getOrCreateInstance(this);
        if (action === 'show') inst.show();
        else if (action === 'hide') inst.hide();
      });
    };
  })(jQuery);
</script>
