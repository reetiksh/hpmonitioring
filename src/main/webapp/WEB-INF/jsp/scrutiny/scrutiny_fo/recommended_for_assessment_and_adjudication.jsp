<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta content="width=device-width, initial-scale=1" name="viewport">
  <title>ASMT-10 Issuance</title>

  <!-- AdminLTE 4 / Bootstrap 5 -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">

  <!-- Optional plugins already used in your app -->
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">

  <style>
    :root{ --hp-sidebar-width: 210px; --hp-header-h: 56px; }
    .app-sidebar{ width: var(--hp-sidebar-width) !important; }
    .app-header{ height: var(--hp-header-h); }
    .app-main{ margin-left: 0 !important; transition: margin-left .2s ease; }
    body.sidebar-collapse .app-main{ margin-left: 0 !important; }
    @media (max-width: 991.98px){ .app-main{ margin-left: 0 !important; } }

    /* Make any container inside app-content full width */
    .app-content > .container,
    .app-content > .container-fluid{ max-width:100% !important; width:100% !important; }
  </style>
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- Header / Sidebar (kept via includes) -->
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <!-- ====================== MAIN ====================== -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page header -->
        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6"><h1 class="m-0">ASMT-10 Issuance</h1></div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/fo/dashboard">Home</a></li>
                <li class="breadcrumb-item active">ASMT-10 Issuance</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- Card -->
        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title mb-0">Issue / Update ASMT-10</h3>
          </div>

          <div class="card-body" style="background-color:#f1f1f1;border-radius:3px;box-shadow:2px 2px 4px 1px #8888884d;">

            <!-- Keep your existing tagline placeholders (IDs unchanged) -->
            <div id="caseIdTagLine" class="text-danger mb-2" style="display:none;">Please fill caseId.</div>
            <div id="caseStageArnTagLine" class="text-danger mb-2" style="display:none;">Please fill case stage arn.</div>
            <div id="demandTagLine" class="text-danger mb-2" style="display:none;">Please fill amount.</div>
            <div id="demandCommaSeperatedTagLine" class="text-danger mb-2" style="display:none;">Please fill amount without comma seperated.</div>
            <div id="recoveryStageTagLine" class="text-danger mb-2" style="display:none;">Please select recovery stage.</div>
            <div id="recoveryByDRC3TagLine" class="text-danger mb-2" style="display:none;">Please fill recovery by drc03.</div>
            <div id="recoveryByDRC3CommaSeperatedTagLine" class="text-danger mb-2" style="display:none;">Please fill recovery by drc03 without comma seperated.</div>
            <div id="asmtTenFileAttachTagLine" class="text-danger mb-2" style="display:none;">Please attach the file.</div>
            <div id="asmtTenfileMaxSizeTagLine" class="text-danger mb-2" style="display:none;">Maximum allowable file size is 10 MB.</div>
            <div id="recoveryStageArnTagLine" class="text-danger mb-2" style="display:none;">Please provide recovery stage arn/reference no.</div>

            <form method="POST" name="asmtTenForm" id="asmtTenForm" action="submit_asmt_ten" enctype="multipart/form-data">
              <h4><b><u>ASMT-10 Issuance:</u></b></h4>
              <br>

              <div class="row g-3">
                <div class="col-md-3">
                  <label for="caseId" class="form-label">Case Id <span class="text-danger">*</span></label>
                  <input class="form-control" id="caseId" name="caseId"
                         placeholder="Please enter Case Id" maxlength="15"
                         pattern="[A-Za-z0-9]{15}"
                         title="Please enter 15-digits alphanumeric Case Id" />
                </div>

                <div class="col-md-3">
                  <label for="caseStage" class="form-label">Case Stage <span class="text-danger">*</span></label>
                  <input class="form-control" id="caseStage" name="caseStage"
                         value="ASMT-10 Issued" title="ASMT-10 Issued" readonly />
                </div>

                <div class="col-md-3">
                  <label for="caseStageArn" class="form-label">Case Stage ARN/Reference no <span class="text-danger">*</span></label>
                  <input class="form-control" id="caseStageArn" name="caseStageArn"
                         placeholder="Please enter Case Stage ARN" maxlength="15"
                         pattern="[A-Za-z0-9]{15}" value="${submittedData.caseStageArn}"
                         title="Please enter 15-digits alphanumeric Case Stage ARN" />
                </div>

                <div class="col-md-3">
                  <label for="demand" class="form-label">Amount(₹) <span class="text-danger">*</span></label>
                  <input class="form-control" type="text" maxlength="11" id="demand" name="demand"
                         onkeypress="return event.charCode >= 48 && event.charCode <= 57"
                         value="${submittedData.demand}" title="Please enter Amount" />
                </div>

                <!-- Consolidated error banner (optional; kept hidden until used) -->
                <div class="col-12">
                  <div id="message1" class="alert alert-danger alert-dismissible fade show" role="alert" style="max-height:500px; overflow-y:auto; display:none;">
                    <span id="alertMessage1"></span>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                  </div>
                </div>

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
              </div>

              <!-- Recovery section -->
              <div class="row g-3 mt-1">
                <div class="col-md-3">
                  <label for="recoveryStage" class="form-label">Recovery Stage <span class="text-danger">*</span></label>
                  <select class="form-select selectpicker recoveryStage" id="recoveryStage" name="recoveryStage"
                          data-live-search="true" title="Please select Recovery Stage">
                    <c:forEach items="${recoveryStageList}" var="soloRecovery">
                      <c:choose>
                        <c:when test="${soloRecovery.id eq mstScrutinyCases.recoveryStage.id}">
                          <option value="${soloRecovery.id}" selected="selected">${soloRecovery.name}</option>
                        </c:when>
                        <c:otherwise>
                          <option value="${soloRecovery.id}">${soloRecovery.name}</option>
                        </c:otherwise>
                      </c:choose>
                    </c:forEach>
                  </select>
                </div>

                <div class="col-md-3">
                  <label for="recoveryByDRC3" class="form-label">Recovery Via DRC03(₹) <span class="text-danger">*</span></label>
                  <input class="form-control" type="text" maxlength="11" id="recoveryByDRC3" name="recoveryByDRC3"
                         onkeypress="return event.charCode >= 48 && event.charCode <= 57"
                         value="${submittedData.recoveryByDRC3}" title="Please enter Recovery Via DRC03" />
                </div>

                <div class="col-md-3">
                  <label for="asmtTenUploadedFile" class="form-label">File Upload <span class="text-danger">*</span></label>
                  <span class="ms-1 small text-muted">(upload only pdf file with max file size of 10MB)</span>
                  <input type="file" class="form-control" id="asmtTenUploadedFile" name="asmtTenUploadedFile" accept=".pdf,application/pdf">
                </div>

                <c:if test="${not empty submittedData.recoveryStageArnStr}">
                  <c:set var="strlst" value="${submittedData.recoveryStageArnStr}"/>
                  <c:set var="lst" value="${fn:split(strlst,',')}"/>
                </c:if>

                <input type="hidden" id="arnLength" value="${fn:length(lst)}" />
                <input type="hidden" id="asmtGstin" name="asmtGstin" value="" readonly />
                <input type="hidden" id="asmtPeriod" name="asmtPeriod" value="" readonly />
                <input type="hidden" id="asmtCaseReportingDate" name="asmtCaseReportingDate" value="" readonly />

                <div class="col-md-3">
                  <label class="form-label">Recovery Stage ARN/Reference no <span class="text-danger">*</span></label>
                  <table id="myTable" class="table order-list mb-0">
                    <thead></thead>
                    <tbody>
                      <c:if test="${fn:length(lst) > 0}">
                        <c:forEach items="${lst}" varStatus="loop" var="lst">
                          <tr>
                            <td>
                              <!-- KEEP original name/indexing -->
                              <input type="text"
                                     class="form-control recoveryclass"
                                     id="recoveryStageArn"
                                     name="recoveryStageArn[${loop.index}]"
                                     value="${fn:trim(lst)}"
                                     placeholder="Please enter Recovery Stage ARN"
                                     maxlength="15"
                                     pattern="[A-Za-z0-9]{15}"
                                     title="Please enter 15-digits alphanumeric Recovery Stage ARN"
                                     required />
                            </td>
                            <td>
                              <button type="button" class="ibtnDel btn btn-danger btn-sm">Delete</button>
                            </td>
                          </tr>
                        </c:forEach>
                      </c:if>
                    </tbody>
                    <tfoot>
                      <tr>
                        <td colspan="5" class="text-start">
                          <button type="button" class="btn btn-primary btn-sm" id="addrow">Add Row</button>
                        </td>
                      </tr>
                    </tfoot>
                  </table>
                </div>
              </div>

              <hr>
              <div class="d-flex justify-content-end">
                <button type="button" class="btn btn-primary" id="submitBtn" onclick="submitAsmtTenIssuance();">Submit</button>
              </div>
            </form>
          </div>
        </div>

      </div><!-- /.container-fluid -->
    </div><!-- /.app-content -->
  </main>

  <!-- Footer -->
  <jsp:include page="../layout/footer.jsp"/>

</div><!-- /.app-wrapper -->

<!-- overlay for mobile sidebar (tap outside to close) -->
<div class="sidebar-overlay" data-lte-dismiss="sidebar"></div>

<!-- ====================== SCRIPTS ====================== -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<!-- Optionals used on this page -->
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  /* Keep content margin synced with real sidebar width so toggle works */
  function setSidebarVar(){
    const sb = document.querySelector('.app-sidebar');
    const collapsed = document.body.classList.contains('sidebar-collapse');
    const px = collapsed ? 0 : (sb ? sb.offsetWidth : 210);
    document.documentElement.style.setProperty('--hp-sidebar-width', px + 'px');
  }
  setSidebarVar();
  new MutationObserver(setSidebarVar).observe(document.body, {attributes:true, attributeFilter:['class']});
  window.addEventListener('resize', setSidebarVar);

  /* bootstrap-select */
  $(function(){ $('.selectpicker').selectpicker(); });

  /* Numeric-only with max length guard */
  function bindMoney($el, max=99999999999){ // up to 11 digits
    $el.on('input', function(){
      let v = this.value.replace(/\D+/g,'');
      v = v.replace(/^0+(?=\d)/,''); // trim leading zeros but keep single zero if empty
      if (v && Number(v) > max) v = String(max);
      this.value = v.slice(0, 11);
    });
  }

  /* Force uppercase A-Z/0-9 and length for IDs/ARNs (doesn't change field names/logic) */
  function forceUpperLen(sel, max){
    $(document).on('input', sel, function(){
      this.value = this.value.toUpperCase().replace(/[^A-Z0-9]/g,'').slice(0, max);
    });
  }

  /* Helpers to show/hide existing red taglines */
  function showTag(id, show){ const el = document.getElementById(id); if(el){ el.style.display = show ? 'block' : 'none'; } }

  $(function () {
    bindMoney($('#demand'));
    bindMoney($('#recoveryByDRC3'));

    forceUpperLen('#caseId', 15);
    forceUpperLen('#caseStageArn', 15);
    forceUpperLen('.recoveryclass', 15);

    const $form = $('#asmtTenForm');
    const $submitBtn = $('#submitBtn');

    // ===== Dynamic Recovery ARN rows (KEEP original indexed names) =====
    const $table = $('table.order-list');
    const $tbody = $table.find('tbody');
    let counter = Number($('#arnLength').val() || 0);

    // Ensure unique IDs for any server-rendered inputs (names remain indexed)
    $table.find('input.recoveryclass').each(function(idx){
      $(this).attr('id', 'recoveryStageArn_' + idx);
    });

    // Add row (new rows continue indexing)
    $('#addrow').on('click', function () {
      const idx = counter++;
      const row = `
        <tr>
          <td>
            <input type="text"
                   class="form-control recoveryclass"
                   id="recoveryStageArn_${idx}"
                   name="recoveryStageArn[${idx}]"
                   placeholder="Please enter Recovery Stage ARN"
                   maxlength="15"
                   pattern="[A-Za-z0-9]{15}"
                   title="Please enter 15-digits alphanumeric Recovery Stage ARN"
                   required />
          </td>
          <td>
            <button type="button" class="ibtnDel btn btn-danger btn-sm">Delete</button>
          </td>
        </tr>`;
      $tbody.append(row);
    });

    // Delete row
    $table.on('click', '.ibtnDel', function(){
      $(this).closest('tr').remove();
    });

    // ===== Submit + validation (keeps your logic & messages) =====
    window.submitAsmtTenIssuance = function () {
      // Hide all messages first
      [
        'caseIdTagLine','caseStageArnTagLine','demandTagLine','demandCommaSeperatedTagLine',
        'recoveryStageTagLine','recoveryByDRC3TagLine','recoveryByDRC3CommaSeperatedTagLine',
        'asmtTenFileAttachTagLine','asmtTenfileMaxSizeTagLine','recoveryStageArnTagLine'
      ].forEach(id => showTag(id, false));
      $('#message1').hide();
      $('#alertMessage1').empty();

      const errs = [];
      const caseId = $('#caseId').val().trim();
      const caseStageArn = $('#caseStageArn').val().trim();
      const demand = $('#demand').val().trim();
      const recoveryStage = $('#recoveryStage').val();
      const drc3 = $('#recoveryByDRC3').val().trim();
      const $file = $('#asmtTenUploadedFile');
      const file = $file[0]?.files?.[0] || null;

      let ok = true;

      if (!/^[A-Za-z0-9]{15}$/.test(caseId)){ showTag('caseIdTagLine', true); errs.push('Case Id must be 15 alphanumeric characters.'); ok = false; }
      if (!/^[A-Za-z0-9]{15}$/.test(caseStageArn)){ showTag('caseStageArnTagLine', true); errs.push('Case Stage ARN must be 15 alphanumeric characters.'); ok = false; }

      if (!demand){ showTag('demandTagLine', true); errs.push('Amount is required.'); ok = false; }
      if (/,/.test(demand)){ showTag('demandCommaSeperatedTagLine', true); errs.push('Amount must not contain commas.'); ok = false; }
      if (!/^\d+$/.test(demand)){ showTag('demandTagLine', true); ok = false; }

      if (!recoveryStage){ showTag('recoveryStageTagLine', true); errs.push('Recovery Stage is required.'); ok = false; }

      if (!drc3){ showTag('recoveryByDRC3TagLine', true); errs.push('Recovery via DRC-03 is required.'); ok = false; }
      if (/,/.test(drc3)){ showTag('recoveryByDRC3CommaSeperatedTagLine', true); errs.push('Recovery via DRC-03 must not contain commas.'); ok = false; }
      if (!/^\d+$/.test(drc3)){ showTag('recoveryByDRC3TagLine', true); ok = false; }

      // At least one ARN and all valid
      const arnInputs = $table.find('input.recoveryclass');
      if (!arnInputs.length){ showTag('recoveryStageArnTagLine', true); errs.push('At least one Recovery Stage ARN is required.'); ok = false; }
      arnInputs.each(function(){
        const v = $(this).val().trim();
        if (!/^[A-Za-z0-9]{15}$/.test(v)){ showTag('recoveryStageArnTagLine', true); ok = false; }
      });

      // File: must be PDF and <= 10MB
      if (!file){
        showTag('asmtTenFileAttachTagLine', true); errs.push('Please attach the ASMT-10 PDF.'); ok = false;
      } else {
        const isPdfExt = /\.pdf$/i.test(file.name);
        const isPdfMime = (file.type || '').toLowerCase() === 'application/pdf';
        if (!isPdfExt || !isPdfMime){ showTag('asmtTenFileAttachTagLine', true); errs.push('Only PDF files are allowed.'); ok = false; }
        if (file.size > 10 * 1024 * 1024){ showTag('asmtTenfileMaxSizeTagLine', true); errs.push('Maximum file size is 10MB.'); ok = false; }
      }

      if (!ok){
        if (errs.length){
          $('#alertMessage1').html(errs.map(e => '• ' + e).join('<br>'));
          $('#message1').show();
        }
        return;
      }

      // Prevent double submit
      $submitBtn.prop('disabled', true);
      $form.trigger('submit');
    };

    /* Hardening (same as other pages) */
    document.addEventListener('contextmenu', e => e.preventDefault());
    document.addEventListener('keydown', e => {
      const k = e.key.toLowerCase();
      if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
    });
  });
</script>
</body>
</html>
