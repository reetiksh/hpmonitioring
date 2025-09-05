<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
.btn-outline-custom { color:#495057; border:1px solid #ced4da; text-align:left; }
.popuprq { display:none; position:fixed; top:50%; left:50%; transform:translate(-50%,-50%); padding:20px; background:#fff; border:1px solid #ccc; box-shadow:0 4px 8px rgba(0,0,0,.1); z-index:999; }
.overlayrq { display:none; position:fixed; inset:0; background:rgba(0,0,0,.5); z-index:998; }
</style>

<head>
  <!-- Keep your local jQuery; Bootstrap 5 is loaded globally by AdminLTE 4 layout -->
  <script src="/static/dist/js/jquery-3.6.4.min.js"></script>
</head>

<div class="modal fade" id="raisequeryModal" tabindex="-1" aria-labelledby="raisequeryModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">

      <div class="modal-header">
        <h5 class="modal-title" id="raisequeryModalTitle"><b>Raise Query</b></h5>
        <!-- Bootstrap 5 close button -->
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <!-- (Legacy inline popup — kept intact) -->
      <div class="popuprq" id="myPopuprq">
        <p>Are You Sure To Raise Query For The Following Case ?</p>
        <button onclick="onOkrq()">OK</button>
        <button onclick="onCancelrq()">Cancel</button>
      </div>

      <div class="modal-body">
        <form method="POST" id="verifierRaiseQueryRemarksDetails" action="recommended_closure_with_raise_query_remarks">
          <input type="hidden" id="gstinnorq" name="gstinno">
          <input type="hidden" id="circlerq" name="circle">
          <input type="hidden" id="reportingdaterq" name="reportingdate">
          <input type="hidden" id="periodrq" name="period">
          <input type="hidden" id="remarksRaiseQuery" name="remarksRaiseQuery">
          <input type="hidden" id="otherRemarksTextValue" name="otherRemarksTextValue">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
          
          <div id="selectRaiseQueryRemarksValueTagLine" style="color:red;display:none;">Please Enter the Remarks !</div>
          <div id="selectRaiseQueryRemarksTagLine" style="color:red;display:none;">Please enter remarks.</div>

          <div class="form-group" style="margin-bottom:0;">
            <label for="message-text" class="col-form-label">Remarks <span style="color:red;">*</span></label>
          </div>

          <!-- Kept your vertical radio list & logic; Bootstrap 5 doesn’t need data-toggle -->
          <div class="btn-group btn-group-vertical w-100" role="group">
            <c:forEach items="${raiseQueryRemarks}" var="object">
              <label class="btn btn-outline-custom" style="font-weight:400;">
                <input type="radio" name="remarkOptions" id="${object.name}" onclick="showHideOtherRemark()" value="${object.id}"> ${object.name}
              </label>
            </c:forEach>
          </div>

          <textarea class="form-control mt-2" id="remarksRaiseQueryId" name="otherRemarks" style="display:none" placeholder="Remarks"></textarea>
        </form>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="transferCaseBtn" onclick="submitVerifierRaiseQueryRemarksForm()">Submit</button>
      </div>

    </div>
  </div>
</div>

<script>
  // Security/UI behavior — unchanged
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => { if ((e.ctrlKey && e.key === 'u') || e.key === 'F12') e.preventDefault(); });
  $(document).ready(function () {
    function disableBack() { window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = function (evt) { if (evt.persisted) disableBack(); }
  });
  document.onkeydown = function (e) { if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) e.preventDefault(); };

  function showHideOtherRemark(){
    $('#selectRaiseQueryRemarksTagLine').hide();
    $('#selectRaiseQueryRemarksValueTagLine').hide();

    var selectedValue = $('input[name="remarkOptions"]:checked').val();
    $("#remarksRaiseQuery").val(selectedValue);
    if(selectedValue == 1){
      $("#remarksRaiseQueryId").show();
    }else{
      $("#remarksRaiseQueryId").hide();
    }
  }

  // Helper: Bootstrap 5 modal hide (no jQuery plugin in BS5)
  function hideBsModalById(id){
    const el = document.getElementById(id);
    if (!el) return;
    const inst = bootstrap.Modal.getInstance(el) || new bootstrap.Modal(el);
    inst.hide();
  }

  function openPopuprq() {
    $.confirm({
      title: 'Confirm!',
      content: 'Do you want to proceed ahead with raising a query ?',
      buttons: {
        submit: function () {
          hideBsModalById('raisequeryModal');
          // show top-level alert placeholders on the host page (kept original IDs/flow)
          document.getElementById("commonBoostrapAlertFail").style.display = "block";
          document.getElementById("raisedQueryTagLine").style.display = "block";
          setTimeout(function() {
            document.getElementById("verifierRaiseQueryRemarksDetails").submit();
          }, 300);
        },
        close: function () { $.alert('Canceled!'); }
      }
    });
  }

  function closePopuprq(){ $("#myPopuprq").hide(); }

  function onOkrq(){
    document.getElementById("commonBoostrapAlertFail").style.display = "block";
    document.getElementById("raisedQueryTagLine").style.display = "block";
    setTimeout(function() {
      document.getElementById("verifierRaiseQueryRemarksDetails").submit();
    }, 500);
    hideBsModalById('raisequeryModal');
    const popupContainer = document.getElementById('raisequeryModal');
    if (popupContainer) popupContainer.style.display = 'none';
    closePopuprq();
  }

  function onCancelrq(){ closePopuprq(); }

  function submitVerifierRaiseQueryRemarksForm(){
    var otherRemarksValue = $('#remarksRaiseQueryId').val();
    var selectedValue = $('input[name="remarkOptions"]:checked').val();
    $("#otherRemarksTextValue").val(otherRemarksValue);

    if ((selectedValue == 1) && (!otherRemarksValue || otherRemarksValue.trim() === '')) {
      $('#selectRaiseQueryRemarksValueTagLine').show();
      return;
    }

    if (selectedValue === undefined) {
      $('#myPopuprq').hide();
      $('#selectRaiseQueryRemarksTagLine').show();
    } else {
      openPopuprq();
    }
  }
</script>
