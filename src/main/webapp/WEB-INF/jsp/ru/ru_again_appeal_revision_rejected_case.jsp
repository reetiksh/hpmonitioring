<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
  .btn-outline-custom {
    color: #495057;
    border: 1px solid #ced4da;
    text-align:left;
  }

  .popup {
    display: none; position: fixed; top: 50%; left: 50%;
    transform: translate(-50%, -50%); padding: 20px; background-color: #fff;
    border: 1px solid #ccc; box-shadow: 0 4px 8px rgba(0,0,0,.1); z-index: 999;
  }
  .overlay {
    display: none; position: fixed; inset: 0; background-color: rgba(0,0,0,.5); z-index: 998;
  }
</style>

<head>
  <script src="/static/dist/js/jquery-3.6.4.min.js"></script>
</head>

<!-- Bootstrap 5 / AdminLTE 4 modal markup -->
<div class="modal fade" id="appealRevisonAfterRejectionModal" tabindex="-1" aria-labelledby="appealRevisionRejectedModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">

      <div id="appealRevisionRejectedRemarksMissingTagLine" class="px-3 pt-3" style="color: red; display: none;">Please enter remarks.</div>
      <div id="appealRevisionRejectedRadioOptionTagLine" class="px-3 pt-3" style="color: red; display: none;">Please select appeal or revision.</div>
      <div id="allFieldsEmptyRejectedTagLine" class="px-3 pt-3" style="color: red; display: none;">Please select the fields.</div>
      <div id="fileNotAttachedRejectedTagLine" class="px-3 pt-3" style="color: red; display: none;">Please select the file.</div>
      <div id="fileMaxSizeRejectedTagLine" class="px-3 pt-3" style="color: red; display: none;">File max size can be 10 MB.</div>

      <div class="modal-header">
        <h5 class="modal-title" id="appealRevisionRejectedModalTitle"><b>Appeal/Revision</b></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <div class="modal-body">
        <form method="POST" name="caseUpdateForm" id="appealRevisionRejectedDetails" action="ru_once_again_appeal_revision_rejected_case" enctype="multipart/form-data">
          <div class="card-body">
            <div class="row g-3 align-items-center">
              <div class="col-md-3">
                <div class="form-check">
                  <input class="form-check-input" type="radio" name="appealRevisionRejected" value="ruAppeal" id="appealRejectedSelected">
                  <label class="form-check-label" for="appealRejectedSelected">Appeal</label>
                </div>
              </div>
              <div class="col-md-3">
                <div class="form-check">
                  <input class="form-check-input" type="radio" name="appealRevisionRejected" value="ruRevision" id="revisionRejectedSelected">
                  <label class="form-check-label" for="revisionRejectedSelected">Revision</label>
                </div>
              </div>
            </div>

            <div class="row mt-3">
              <div class="col-md-12">
                <label class="form-label" for="appealRevisionRejectedRemarks">Remarks <span style="color: red">*</span></label>
                <textarea class="form-control" id="appealRevisionRejectedRemarks" name="appealRevisionRejectedRemarks"></textarea>
              </div>
            </div>

            <div class="row mt-3">
              <div class="col-md-12">
                <label class="form-label" for="appealRevisionRejectedFile">File Upload <span style="color: red">*</span></label>
                <span> (Upload only pdf file with max file size of 10 MB)</span>
                <input class="form-control" type="file" id="appealRevisionRejectedFile" name="appealRevisionRejectedFile" accept=".pdf">
              </div>
            </div>
          </div>

          <input type="hidden" id="appRevRejGstiNo" name="appRevRejGstiNo">
          <input type="hidden" id="appRevRejReportingdate" name="appRevRejReportingdate">
          <input type="hidden" id="appRevRejPeriod" name="appRevRejPeriod">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        </form>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="appealRevisionRejectedCase" onclick="submitAppealRevisionRejectedCase()">Submit</button>
      </div>

    </div>
  </div>
</div>

<script>
  // Helper to hide/show BS5 modal without jQuery plugins
  function hideModalById(id) {
    const el = document.getElementById(id);
    if (!el) return;
    const modal = bootstrap.Modal.getOrCreateInstance(el);
    modal.hide();
  }

  function submitAppealRevisionRejectedCase() {
    var appealRejectionRadioOptionSelected = $("input[name='appealRevisionRejected']:checked").val();
    var appealRevisionRemarksVal = $("#appealRevisionRejectedRemarks").val();
    var appealRevisionFileAttached = $("#appealRevisionRejectedFile")[0].files.length;
    var maxSizeInBytes = 10 * 1024 * 1024; // 10 MB
    var appealRevisionFileAttachedSize;

    if (appealRevisionFileAttached != 0) {
      var fileInput = $('#appealRevisionRejectedFile')[0];
      appealRevisionFileAttachedSize = fileInput.files[0].size;
    } else {
      appealRevisionFileAttachedSize = 0;
    }

    if ((appealRejectionRadioOptionSelected === undefined) && (appealRevisionRemarksVal == "") && (appealRevisionFileAttached == 0)) {
      $("#appealRevisionRejectedRadioOptionTagLine").hide();
      $("#appealRevisionRejectedRemarksMissingTagLine").hide();
      $("#fileNotAttachedRejectedTagLine").hide();
      $("#fileMaxSizeRejectedTagLine").hide();
      $("#allFieldsEmptyRejectedTagLine").show();
      return;
    }
    if (appealRejectionRadioOptionSelected === undefined) {
      $("#allFieldsEmptyRejectedTagLine").hide();
      $("#appealRevisionRejectedRemarksMissingTagLine").hide();
      $("#fileNotAttachedRejectedTagLine").hide();
      $("#fileMaxSizeRejectedTagLine").hide();
      $("#appealRevisionRejectedRadioOptionTagLine").show();
      return;
    }
    if (appealRevisionRemarksVal == "") {
      $("#allFieldsEmptyRejectedTagLine").hide();
      $("#appealRevisionRejectedRadioOptionTagLine").hide();
      $("#fileNotAttachedRejectedTagLine").hide();
      $("#fileMaxSizeRejectedTagLine").hide();
      $("#appealRevisionRejectedRemarksMissingTagLine").show();
      return;
    }
    if (appealRevisionFileAttached == 0) {
      $("#allFieldsEmptyRejectedTagLine").hide();
      $("#appealRevisionRejectedRadioOptionTagLine").hide();
      $("#appealRevisionRejectedRemarksMissingTagLine").hide();
      $("#fileMaxSizeRejectedTagLine").hide();
      $("#fileNotAttachedRejectedTagLine").show();
      return;
    }
    if (appealRevisionFileAttachedSize > maxSizeInBytes) {
      $("#allFieldsEmptyRejectedTagLine").hide();
      $("#appealRevisionRejectedRadioOptionTagLine").hide();
      $("#appealRevisionRejectedRemarksMissingTagLine").hide();
      $("#fileNotAttachedRejectedTagLine").hide();
      $("#fileMaxSizeRejectedTagLine").show();
      return;
    }

    if (appealRejectionRadioOptionSelected == "ruAppeal") {
      $.confirm({
        title: 'Confirm!',
        content: 'Do you want to proceed ahead with appealing the case ?',
        buttons: {
          submit: function () {
            // Close the correct modal (ID matches markup)
            hideModalById('appealRevisonAfterRejectionModal');
            document.getElementById("caseAppealedAlertSuccess").style.display = "block";
            document.getElementById("caseAppealedTagLine").style.display = "block";
            setTimeout(function () {
              document.getElementById("appealRevisionRejectedDetails").submit();
            }, 300);
          },
          close: function () { $.alert('Canceled!'); }
        }
      });
    } else {
      $.confirm({
        title: 'Confirm!',
        content: 'Do you want to proceed ahead with revision the case ?',
        buttons: {
          submit: function () {
            hideModalById('appealRevisonAfterRejectionModal');
            document.getElementById("caseRevisionAlertSuccess").style.display = "block";
            document.getElementById("caseRevisionTagLine").style.display = "block";
            setTimeout(function () {
              document.getElementById("appealRevisionRejectedDetails").submit();
            }, 300);
          },
          close: function () { $.alert('Canceled!'); }
        }
      });
    }
  }
</script>
