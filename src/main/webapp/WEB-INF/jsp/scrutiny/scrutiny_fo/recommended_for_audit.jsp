<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8"/>
  <meta content="width=device-width, initial-scale=1" name="viewport"/>
  <title>Recommend for Audit</title>

  <!-- AdminLTE / Bootstrap core -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css"/>
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css"/>

  <!-- Plugins -->
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css"/>
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css"/>

  <style>
    :root{ --hp-sidebar-width: 210px; --hp-header-h: 56px; }
    .app-sidebar{ width: var(--hp-sidebar-width) !important; }
    .app-header{ height: var(--hp-header-h); }
    .app-main{ margin-left: 0 !important; transition: margin-left .2s ease; }
    @media (min-width: 992px){ body.sidebar-expand-lg:not(.sidebar-collapse) .app-wrapper .app-main{ margin-left: var(--hp-sidebar-width); } }
    @media (max-width: 991.98px){ .app-main{ margin-left: 0 !important; } }
  </style>
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- Header / Sidebar -->
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <!-- ====================== MAIN ====================== -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6"><h1 class="m-0">Recommend for Audit</h1></div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/fo/dashboard">Home</a></li>
                <li class="breadcrumb-item active">Recommend for Audit</li>
              </ol>
            </div>
          </div>
        </div>

        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title mb-0">Enter Details</h3>
          </div>

          <div class="card-body" style="background:#f1f1f1;border-radius:3px;box-shadow:2px 2px 4px 1px #00000010;">

            <!-- inline taglines (kept same ids so backend JS, if any, still works) -->
            <div id="auditFileAttachTagLine" class="text-danger mb-2" style="display:none;">Please attach the file.</div>
            <div id="auditfileMaxSizeTagLine" class="text-danger mb-2" style="display:none;">Maximum allowable file size is 10 MB.</div>
            <div id="caseIdTagLine" class="text-danger mb-2" style="display:none;">Please fill caseId.</div>
            <div id="demandTagLine" class="text-danger mb-2" style="display:none;">Please fill amount.</div>

            <!-- consolidated alert (optional) -->
            <div id="message1" class="alert alert-danger alert-dismissible fade show" role="alert" style="display:none;max-height:500px;overflow-y:auto;">
              <span id="alertMessage1"></span>
              <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>

            <form method="POST" name="recommendedAuditForm" id="recommendedAuditForm" action="recommend_audit" enctype="multipart/form-data">
              <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

              <!-- Hidden context (prefill if available) -->
              <input type="hidden" id="asmtGstin" name="asmtGstin" value="${mstScrutinyCases.id.GSTIN}"/>
              <input type="hidden" id="asmtPeriod" name="asmtPeriod" value="${mstScrutinyCases.id.period}"/>
              <input type="hidden" id="asmtCaseReportingDate" name="asmtCaseReportingDate" value="${mstScrutinyCases.id.caseReportingDate}"/>

              <h4><b><u>Recommended for Audit:</u></b></h4>
              <br/>

              <div class="row g-3">
                <div class="col-md-4">
                  <label for="caseId" class="form-label">Case Id <span class="text-danger">*</span></label>
                  <input class="form-control" id="caseId" name="caseId"
                         placeholder="Please enter Case Id"
                         maxlength="15" pattern="[A-Za-z0-9]{15}"
                         title="Please enter 15-digits alphanumeric Case Id"/>
                </div>

                <div class="col-md-4">
                  <label for="demand" class="form-label">Amount (₹) <span class="text-danger">*</span></label>
                  <input class="form-control" id="demand" name="demand"
                         maxlength="11" inputmode="numeric"
                         onkeypress="return event.charCode >= 48 && event.charCode <= 57"
                         value="${submittedData.demand}" placeholder="Enter amount"/>
                </div>

                <div class="col-md-4">
                  <label for="recommendedForAuditFile" class="form-label">File Upload <span class="text-danger">*</span></label>
                  <span class="small text-muted ms-1">(PDF, max 10MB)</span>
                  <input class="form-control" type="file" id="recommendedForAuditFile" name="recommendedForAuditFile" accept=".pdf,application/pdf"/>
                  <c:if test="${submittedData.sum eq 'fileexist'}">
                    <a href="/fo/downloadUploadedFile?fileName=${submittedData.remarks}" class="btn btn-primary mt-2" title="Download existing file">
                      <i class="fa fa-download"></i>
                    </a>
                  </c:if>
                </div>
              </div>

              <hr/>
              <div class="d-flex justify-content-end">
                <button type="button" class="btn btn-primary" id="recommendBtn">Recommend</button>
              </div>
            </form>
          </div>
        </div>

      </div><!-- /.container-fluid -->
    </div><!-- /.app-content -->
  </main>

  <jsp:include page="../layout/footer.jsp"/>

</div><!-- /.app-wrapper -->

<!-- ====================== SCRIPTS ====================== -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<!-- Plugins -->
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>
<!-- (Optional) bs-custom-file-input if you want native-like file label behavior -->
<script src="/static/plugins/bs-custom-file-input/bs-custom-file-input.min.js"></script>

<script>
  /* Sidebar width sync (if you use AdminLTE 4 sidebar toggle) */
  function setSidebarVar(){
    const sb = document.querySelector('.app-sidebar');
    const collapsed = document.body.classList.contains('sidebar-collapse');
    const px = collapsed ? 0 : (sb ? sb.offsetWidth : 210);
    document.documentElement.style.setProperty('--hp-sidebar-width', px + 'px');
  }
  setSidebarVar();
  new MutationObserver(setSidebarVar).observe(document.body, {attributes:true, attributeFilter:['class']});
  window.addEventListener('resize', setSidebarVar);

  /* Init plugins */
  $(function(){
    try { bsCustomFileInput.init(); } catch(e){}
    $('.selectpicker').selectpicker();
  });

  /* Helpers */
  function showTag(id, show){ const el = document.getElementById(id); if(el){ el.style.display = show ? 'block' : 'none'; } }
  function addError(msg){ const c = document.getElementById('message1'); const t = document.getElementById('alertMessage1'); if (t) t.innerHTML += (t.innerHTML?'<br>• ':'• ') + msg; if (c) c.style.display='block'; }

  /* Force uppercase A-Z0-9 for CaseId */
  document.addEventListener('input', function(e){
    const el = e.target;
    if (el && (el.id === 'caseId')) {
      el.value = el.value.toUpperCase().replace(/[^A-Z0-9]/g,'').slice(0, 15);
    }
  });

  /* Numeric only for amount */
  document.getElementById('demand')?.addEventListener('input', function(){
    this.value = this.value.replace(/\D+/g,'').slice(0, 11);
  });

  /* Confirm + validate + submit */
  document.getElementById('recommendBtn').addEventListener('click', function(){
    // reset messages
    ['auditFileAttachTagLine','auditfileMaxSizeTagLine','caseIdTagLine','demandTagLine'].forEach(i=>showTag(i,false));
    document.getElementById('message1').style.display='none';
    document.getElementById('alertMessage1').innerHTML='';

    const caseId = document.getElementById('caseId').value.trim();
    const demand = document.getElementById('demand').value.trim();
    const fileEl = document.getElementById('recommendedForAuditFile');
    const file = fileEl && fileEl.files && fileEl.files[0] ? fileEl.files[0] : null;

    let ok = true;

    if (!/^[A-Za-z0-9]{15}$/.test(caseId)) { showTag('caseIdTagLine', true); addError('Case Id must be 15 alphanumeric characters.'); ok = false; }
    if (!demand) { showTag('demandTagLine', true); addError('Amount is required.'); ok = false; }
    if (!/^\d+$/.test(demand)) { showTag('demandTagLine', true); addError('Amount must be numeric.'); ok = false; }

    if (!file){
      showTag('auditFileAttachTagLine', true);
      addError('Please attach a PDF file.');
      ok = false;
    } else {
      const isPdf = /\.pdf$/i.test(file.name) && (file.type || '').toLowerCase()==='application/pdf';
      if (!isPdf){ showTag('auditFileAttachTagLine', true); addError('Only PDF files are allowed.'); ok = false; }
      if (file.size > 10 * 1024 * 1024){ showTag('auditfileMaxSizeTagLine', true); addError('Maximum file size is 10MB.'); ok = false; }
    }

    if (!ok) return;

    $.confirm({
      title: 'Confirm!',
      content: 'Do you want to proceed ahead with recommendation of this case to Audit?',
      buttons: {
        submit: function () {
          document.getElementById('recommendedAuditForm').submit();
        },
        close: function () {
          $.alert('Canceled!');
        }
      }
    });
  });

  /* Optional hardening */
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = (e.key || '').toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
</script>
</body>
</html>
