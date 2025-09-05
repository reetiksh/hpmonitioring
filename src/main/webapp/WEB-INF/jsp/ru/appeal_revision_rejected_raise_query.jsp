<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
  .btn-outline-custom {
    color: #495057;
    border: 1px solid #ced4da;
    text-align:left;
  }
  .popuprq {
    display: none;
    position: fixed;
    top: 50%; left: 50%;
    transform: translate(-50%, -50%);
    padding: 20px; background-color: #fff;
    border: 1px solid #ccc; box-shadow: 0 4px 8px rgba(0,0,0,.1);
    z-index: 1056; /* above .modal (1055) */
  }
  .overlayrq {
    display: none;
    position: fixed; inset: 0;
    background-color: rgba(0,0,0,.5);
    z-index: 1055;
  }
</style>

<head>
  <!-- jQuery (kept for existing logic/plugins) -->
  <script src="/static/dist/js/jquery-3.6.4.min.js"></script>
</head>

<!-- AdminLTE 4 / Bootstrap 5 modal markup -->
<div class="modal fade" id="raisequeryModal" tabindex="-1" aria-labelledby="raisequeryModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">

      <div class="modal-header">
        <h5 class="modal-title" id="raisequeryModalTitle"><b>Raise Query</b></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <!-- Optional inline confirm popup (kept as-is) -->
      <div class="popuprq" id="myPopuprq">
        <p>Are You Sure To Raise Query For The Following Case ?</p>
        <button class="btn btn-primary btn-sm" onclick="onOkrq()">OK</button>
        <button class="btn btn-secondary btn-sm" onclick="onCancelrq()">Cancel</button>
      </div>

      <div class="modal-body">
        <form method="POST" id="verifierRaiseQueryRemarksDetails" action="appeal_revision_raise_query_case">
          <input type="hidden" id="gstinnorq" name="gstinno">
          <input type="hidden" id="circlerq" name="circle">
          <input type="hidden" id="reportingdaterq" name="reportingdate">
          <input type="hidden" id="periodrq" name="period">
          <input type="hidden" id="remarksRaiseQuery" name="remarksRaiseQuery">
          <input type="hidden" id="otherRemarksTextValue" name="otherRemarksTextValue">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

          <div id="selectRaiseQueryRemarksValueTagLine" class="text-danger" style="display:none;">Please Enter the Remarks !</div>
          <div id="selectRaiseQueryRemarksTagLine" class="text-danger" style="display:none;">Please enter remarks.</div>

          <div class="form-group mb-2">
            <label class="col-form-label">Remarks <span class="text-danger">*</span></label>
          </div>

          <!-- Kept dataset/logic intact; only Bootstrap 5-safe markup -->
          <div class="btn-group-vertical w-100" role="group">
            <c:forEach items="${raiseQueryRemarks}" var="object">
              <label class="btn btn-outline-custom fw-normal mb-2">
                <input type="radio" name="remarkOptions" id="${object.name}" onclick="showHideOtherRemark()" value="${object.id}"> ${object.name}
              </label>
            </c:forEach>
          </div>

          <textarea class="form-control mt-2" id="remarksRaiseQueryId" name="otherRemarks" style="display:none;" placeholder="Remarks"></textarea>
        </form>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="transferCaseBtn" onclick="submitVerifierRaiseQueryRemarksForm()">Submit</button>
      </div>
    </div>
  </div>
</div>

<script>
  // Helper for Bootstrap 5 modal control (AdminLTE 4)
  function hideRaiseQueryModal() {
    var el = document.getElementById('raisequeryModal');
    if (!el) return;
    if (window.bootstrap && bootstrap.Modal) {
      var inst = bootstrap.Modal.getOrCreateInstance(el);
      inst.hide();
    } else {
      // Fallback (if BS5 not available yet)
      el.style.display = 'none';
      el.classList.remove('show');
      document.body.classList.remove('modal-open');
      document.querySelectorAll('.modal-backdrop').forEach(b=>b.remove());
    }
  }

  function showHideOtherRemark(){
    $('#selectRaiseQueryRemarksTagLine').hide();
    $('#selectRaiseQueryRemarksValueTagLine').hide();

    var selectedValue = $('input[name="remarkOptions"]:checked').val();
    $("#remarksRaiseQuery").val(selectedValue);

    if (selectedValue == 1) {
      $("#remarksRaiseQueryId").show();
    } else {
      $("#remarksRaiseQueryId").hide();
    }
  }

  function openPopuprq() {
    $.confirm({
      title: 'Confirm!',
      content: 'Do you want to proceed ahead with raising a query ?',
      buttons: {
        submit: function() {
          hideRaiseQueryModal();
          document.getElementById("commonBoostrapAlertFail").style.display = "block";
          document.getElementById("raisedQueryTagLine").style.display = "block";
          setTimeout(function() {
            document.getElementById("verifierRaiseQueryRemarksDetails").submit();
          }, 300);
        },
        close: function() {
          $.alert('Canceled!');
        }
      }
    });
  }

  function closePopuprq() { $("#myPopuprq").hide(); }

  function onOkrq() {
    document.getElementById("commonBoostrapAlertFail").style.display = "block";
    document.getElementById("raisedQueryTagLine").style.display = "block";
    setTimeout(function() {
      document.getElementById("verifierRaiseQueryRemarksDetails").submit();
    }, 500);

    hideRaiseQueryModal();
    var popupContainer = document.getElementById('raisequeryModal');
    if (popupContainer) popupContainer.style.display = 'none';
    closePopuprq();
  }

  function onCancelrq() { closePopuprq(); }

  function submitVerifierRaiseQueryRemarksForm(){
    var otherRemarksValue = $('#remarksRaiseQueryId').val();
    var selectedValue = $('input[name="remarkOptions"]:checked').val();
    $("#otherRemarksTextValue").val(otherRemarksValue);

    if ((selectedValue == 1) && (otherRemarksValue == '')) {
      $('#selectRaiseQueryRemarksValueTagLine').show();
      return;
    }
    if (selectedValue === undefined) {
      $('#myPopuprq').hide();
      $('#selectRaiseQueryRemarksTagLine').show();
    } else {
      openPopuprq();
    }
    // $("#verifierRaiseQueryRemarksDetails").submit(); // intentionally kept commented as in original
  }
</script>
