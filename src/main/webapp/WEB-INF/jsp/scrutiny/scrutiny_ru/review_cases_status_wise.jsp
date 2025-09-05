<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<style>
  .modal-lg, .modal-xl { max-width: 1400px; }
  .btn-outline-custom {
    color:#495057; border:1px solid #ced4da; text-align:left;
  }
</style>

<script>
  function showHideOtherRemark(){
    const v = $('input[name="remarks"]:checked').val();
    if (String(v) === '1') { $('#remarksId').show(); } else { $('#remarksId').hide(); }
  }
</script>

<table id="scrutinyListTable" class="table table-bordered table-striped table-responsive">
  <thead>
  <tr>
    <th class="text-center align-middle">GSTIN</th>
    <th class="text-center align-middle">Taxpayer Name</th>
    <th class="text-center align-middle">Jurisdiction</th>
    <th class="text-center align-middle">Period</th>
    <th class="text-center align-middle">Reporting Date<br>(DD-MM-YYYY)</th>
    <th class="text-center align-middle">Indicative Value(₹)</th>
    <th class="text-center align-middle">Currently With</th>
    <th class="text-center align-middle">Case ID</th>
    <th class="text-center align-middle">Case Stage</th>
    <th class="text-center align-middle">Case Stage ARN</th>
    <th class="text-center align-middle">Amount(₹)</th>
    <th class="text-center align-middle">Recovery Stage</th>
    <th class="text-center align-middle">Recovery Stage ARN</th>
    <th class="text-center align-middle">Recovery Via DRC03(₹)</th>
    <th class="text-center align-middle">Supporting Document</th>
  </tr>
  </thead>
  <tbody>
  <c:forEach items="${reviewCasesList}" var="mstScrutinyCase">
    <tr>
      <td>${mstScrutinyCase.id.GSTIN}</td>
      <td>${mstScrutinyCase.taxpayerName}</td>
      <td>${mstScrutinyCase.locationDetails.locationName}</td>
      <td>${mstScrutinyCase.id.period}</td>
      <td><fmt:formatDate value="${mstScrutinyCase.id.caseReportingDate}" pattern="dd-MM-yyyy"/></td>
      <td>${mstScrutinyCase.indicativeTaxValue}</td>
      <td>${mstScrutinyCase.currentlyAssignedTo}</td>
      <td>${mstScrutinyCase.caseId}</td>
      <td>${mstScrutinyCase.caseStage.name}</td>
      <td>${mstScrutinyCase.caseStageArn}</td>
      <td>${mstScrutinyCase.amountRecovered}</td>
      <td>${mstScrutinyCase.recoveryStage.name}</td>
      <td>${mstScrutinyCase.recoveryStageArn}</td>
      <td>${mstScrutinyCase.recoveryByDRC03}</td>
      <td>
        <a href="/scrutiny_ru/downloadUploadedPdfFile?fileName=${mstScrutinyCase.scrutinyExtensionNoDocument.extensionFileName}">
          <button type="button" class="btn btn-primary"><i class="fa fa-download"></i></button>
        </a>
      </td>
    </tr>
  </c:forEach>
  </tbody>
</table>

<div class="modal fade" id="updateSummaryViewModal" tabindex="-1" role="dialog" aria-labelledby="updateSummaryViewTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-lg" role="document" >
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="updateSummaryViewModalTitle">Update Case</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      </div>
      <div class="modal-body" id="updateSummaryViewBody"></div>
    </div>
  </div>
</div>

<div class="modal fade" id="closeCaseModal" tabindex="-1" role="dialog" aria-labelledby="closeCaseModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <form method="POST" action="fo_close_cases" name="closeCaseForm">
        <div class="modal-header">
          <h5 class="modal-title" id="confirmationModalTitle">Do you want to proceed ahead with closure of the case?</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <div class="modal-body">
          <p>I hereby undertake that;</p>
          <p><i class="fa fa-check"></i> All the points raised by EIU pertaining to this Taxpayer have been dealt carefully in the light of the provisions of the GST laws</p>
          <p><i class="fa fa-check"></i> Applicable Tax has been levied as per the provisions of GST acts/rules 2017</p>
          <p><i class="fa fa-check"></i> Applicable Interest has been levied as per the provisions of GST acts/rules 2017</p>
          <p><i class="fa fa-check"></i> Applicable Penalty has been levied as per the provisions of GST acts/rules 2017</p>
          <p><input type="checkbox" id="mycheckbox" name="checkbox"> I hereby declare that above information is true and correct to the best of my knowledge</p>
        </div>

        <input type="hidden" id="gstno" name="gstno">
        <input type="hidden" id="date" name="date">
        <input type="hidden" id="period" name="period">

        <div class="modal-footer">
          <div id="checked" style="display:none">
            <button type="submit" class="btn btn-primary" id="okayBtn">Okay</button>
          </div>
          <button type="button" class="btn btn-secondary" data-dismiss="modal" id="closeBtn">Cancel</button>
        </div>
      </form>
    </div>
  </div>
</div>

<div class="modal fade" id="updateCaseidModal" tabindex="-1" role="dialog" aria-labelledby="updateCaseidModal" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="modalTitle">Update Case ID</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
      </div>

      <form method="POST" name="updatecaseidForm" id="updatecaseidForm" action="update_caseid" enctype="multipart/form-data">
        <div class="modal-body">
          <input type="hidden" id="gstnocaseid" name="gstnocaseid">
          <input type="hidden" id="datecaseid" name="datecaseid">
          <input type="hidden" id="periodcaseid" name="periodcaseid">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

          <div class="row">
            <div class="col-md-12">
              <div class="form-group">
                <label>Old Case ID</label>
                <input class="form-control" type="text" id="caseid" readonly/>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="col-md-12">
              <div class="form-group">
                <label>New Case ID Proposed <span style="color:red">*</span></label>
                <input class="form-control" maxlength="15" pattern="[A-Za-z0-9]{15}" id="newcaseid" name="caseid" title="Please enter 15-digits alphanumeric Case Id" required/>
              </div>
            </div>
          </div>

          <div class="form-group mb-1">
            <label class="col-form-label">Remarks <span style="color:red">*</span></label>
          </div>
          <div class="btn-group btn-group-vertical" role="group" data-toggle="buttons" style="width:100%;">
            <c:forEach items="${listRemarks}" var="item">
              <label class="btn btn-outline-custom">
                <input type="radio" name="remarks" id="${item.id}" onclick="showHideOtherRemark()" value="${item.id}" required> ${item.name}
              </label>
            </c:forEach>
          </div>
          <textarea class="form-control" id="remarksId" name="otherRemarks" style="display:none" placeholder="Remarks"></textarea>

          <div class="form-group mb-1">
            <label class="col-form-label">Upload File <span style="color:red">*</span>
              <span>(Only pdf files of size up to 10MB)</span>
            </label>
          </div>
          <input type="file" id="filePath" name="filePath" accept=".pdf" required/>
        </div>

        <div class="modal-footer">
          <button type="submit" class="btn btn-primary" id="transferCaseBtn">Submit</button>
        </div>
      </form>
    </div>
  </div>
</div>

<script>
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });

  // Disable bfcache back/forward
  $(document).ready(function () {
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = function (evt) { if (evt.persisted) disableBack(); };
  });

  // actions
  function view(gst, date, period){
    $.ajax({
      url:'/checkLoginStatus', method:'get', async:false,
      success:function(result){
        if(result==='true'){
          $("#updateSummaryViewBody").load('/fo/view_case/id?gst='+gst+'&date='+date+'&period='+period, function(resp, status){
            if(status==='success'){ $("#updateSummaryViewModal").modal('show'); }
          });
        } else if (result==='false'){ window.location.reload(); }
      }
    });
  }

  function closeCase(gstno, date, period){
    $("#gstno").val(gstno);
    $("#date").val(date);
    $("#period").val(period);
    $("#closeCaseModal").modal('show');
  }

  function updateCaseId(gstno, date, period, caseid){
    $("#gstnocaseid").val(gstno);
    $("#datecaseid").val(date);
    $("#periodcaseid").val(period);
    $("#caseid").val(caseid);
    $("#updateCaseidModal").modal('show');
  }

  $(function () {
    $('#mycheckbox').on('change', function(){ $('#checked').toggle(this.checked); });

    $("#okayBtn").on("click", function(){ $("#closeCaseModal").modal('hide'); });
    $("#closeBtn").on("click", function(){ $("#closeCaseModal").modal('hide'); });

    // Update Case ID form submit with robust checks
    let submitting = false;
    $('#updatecaseidForm').on('submit', function (oEvent) {
      oEvent.preventDefault();
      if (submitting) return;
      const oldCaseId = ($('#caseid').val() || '').trim();
      const newCaseId = ($('#newcaseid').val() || '').trim();

      $.confirm({
        title: 'Confirm!',
        content: 'Do you want to proceed ahead to request case id updation?',
        buttons: {
          submit: function () {
            const input = document.getElementById('filePath');
            const file = input.files && input.files[0] ? input.files[0] : null;

            if (!file) { $.alert('Please upload pdf file'); return; }

            // size <= 10MB and type/name looks like pdf
            const tooBig = file.size > 10 * 1024 * 1024;
            const isPdf = (file.type === 'application/pdf') || /\.pdf$/i.test(file.name);
            if (!isPdf) { $.alert('Please upload only pdf file'); input.value=''; return; }
            if (tooBig) { $.alert('Please upload max 10MB file'); input.value=''; return; }

            // if "Others" selected, remarks required
            const remarkChoice = String($('input[name="remarks"]:checked').val() || '');
            if (remarkChoice === '1') {
              const otherTxt = ($('#remarksId').val() || '').trim();
              if (!otherTxt) { $.alert('Please enter Others remarks'); return; }
            }

            if (oldCaseId === newCaseId) {
              $.alert('New Case ID can not be same as Old Case ID');
              $('#newcaseid').val('').focus();
              return;
            }

            submitting = true;
            oEvent.currentTarget.submit();
          },
          close: function(){ $.alert('Canceled!'); }
        }
      });
    });

    // DataTables
    try{
      const dt = $("#scrutinyListTable").DataTable({
        responsive:false, lengthChange:false, autoWidth:true, scrollX:false,
        buttons:["excel","pdf","print"]
      });
      dt.buttons().container().appendTo('#scrutinyListTable_wrapper .col-md-6:eq(0)');
    }catch(e){ /* DT may not be present on some views */ }
  });
</script>
