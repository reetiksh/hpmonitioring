<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Review Cases</title>

  <!-- Styles -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">

  <style>
    .modal-lg, .modal-xl { max-width: 1400px; }
    .btn-outline-custom { color: #495057; border: 1px solid #ced4da; text-align: left; }
  </style>
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
  <jsp:include page="../../layout/header.jsp"/>
  <jsp:include page="../../layout/sidebar.jsp"/>
  <jsp:include page="../../layout/confirmation_popup.jsp"/>

  <div class="content-wrapper">
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6"><h1>Review Cases</h1></div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a>Home</a></li>
              <li class="breadcrumb-item active">Review Cases</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <div class="card card-primary">
          <div class="card-header"><h3 class="card-title">Review Cases</h3></div>
          <div class="card-body">

            <c:if test="${not empty closeclasemessage}">
              <div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
                <strong>${closeclasemessage}</strong>
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
            </c:if>
            <c:if test="${not empty message}">
              <div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
                <strong>${message}</strong>
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
            </c:if>

            <div class="table-responsive">
              <table id="scrutinyListTable" class="table table-bordered table-striped">
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
                    <td class="text-center">
                      <c:if test="${mstScrutinyCase.scrutinyExtensionNoDocument != null && mstScrutinyCase.scrutinyExtensionNoDocument.extensionFileName != null}">
                        <a href="/scrutiny_hq/downloadUploadedPdfFile?fileName=${fn:escapeXml(mstScrutinyCase.scrutinyExtensionNoDocument.extensionFileName)}">
                          <button type="button" class="btn btn-primary"><i class="fa fa-download"></i></button>
                        </a>
                      </c:if>
                    </td>
                  </tr>
                </c:forEach>
                </tbody>
              </table>
            </div>

          </div>
        </div>
      </div>
    </section>
  </div>

  <jsp:include page="../../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- ================= Modals ================= -->

<!-- Update case (content loads via AJAX) -->
<div class="modal fade" id="updateSummaryViewModal" tabindex="-1" role="dialog" aria-labelledby="updateSummaryViewTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="updateSummaryViewModalTitle">Update Case</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span>&times;</span></button>
      </div>
      <div class="modal-body" id="updateSummaryViewBody"></div>
    </div>
  </div>
</div>

<!-- Close case confirm -->
<div class="modal fade" id="closeCaseModal" tabindex="-1" role="dialog" aria-labelledby="closeCaseModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <form method="POST" action="fo_close_cases" name="closeCaseForm">
        <div class="modal-header">
          <h5 class="modal-title" id="confirmationModalTitle">Do you want to proceed ahead with closure of the case?</h5>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span>&times;</span></button>
        </div>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

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

<!-- Update Case ID -->
<div class="modal fade" id="updateCaseidModal" tabindex="-1" role="dialog" aria-labelledby="updateCaseidModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Update Case ID</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span>&times;</span></button>
      </div>

      <form method="POST" name="updatecaseidForm" id="updatecaseidForm" action="update_caseid" enctype="multipart/form-data">
        <div class="modal-body">
          <input type="hidden" id="gstnocaseid" name="gstnocaseid">
          <input type="hidden" id="datecaseid" name="datecaseid">
          <input type="hidden" id="periodcaseid" name="periodcaseid">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

          <div class="form-group">
            <label>Old Case ID</label>
            <input class="form-control" type="text" id="caseid" readonly>
          </div>

          <div class="form-group">
            <label>New Case ID Proposed <span class="text-danger">*</span></label>
            <input class="form-control" maxlength="15" pattern="[A-Za-z0-9]{15}" id="newcaseid" name="caseid" title="Please enter 15-digits alphanumeric Case Id" required>
          </div>

          <div class="form-group mb-1">
            <label class="col-form-label">Remarks <span class="text-danger">*</span></label>
          </div>
          <div class="btn-group btn-group-vertical w-100" role="group" data-toggle="buttons">
            <c:forEach items="${listRemarks}" var="item">
              <label class="btn btn-outline-custom">
                <input type="radio" name="remarks" id="${item.id}" value="${item.id}" onclick="showHideOtherRemark()" required> ${item.name}
              </label>
            </c:forEach>
          </div>
          <textarea class="form-control mt-2" id="remarksId" name="otherRemarks" style="display:none" placeholder="Remarks"></textarea>

          <div class="form-group mt-3 mb-1">
            <label>Upload File <span class="text-danger">*</span>
              <span class="text-muted">(Only PDF up to 10MB)</span>
            </label>
          </div>
          <input type="file" id="filePath" name="filePath" accept=".pdf,application/pdf" required>
        </div>

        <div class="modal-footer">
          <button type="submit" class="btn btn-primary" id="transferCaseBtn">Submit</button>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- Scripts -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="/static/plugins/datatables-bs4/js/dataTables.bootstrap4.min.js"></script>
<script src="/static/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="/static/plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/dataTables.buttons.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.bootstrap4.min.js"></script>
<script src="/static/plugins/jszip/jszip.min.js"></script>
<script src="/static/plugins/pdfmake/pdfmake.min.js"></script>
<script src="/static/plugins/pdfmake/vfs_fonts.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.html5.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.print.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.colVis.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<script>
  // Basic hardening
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = (e.key||'').toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
  $(function(){
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack;
    window.onpageshow = function(evt){ if (evt.persisted) disableBack(); };
    $("#message").fadeTo(2000, 500).slideUp(500, function(){ $("#message").slideUp(500); });
  });

  // Show/hide "Other" remarks textarea
  function showHideOtherRemark(){
    var selectedValue = $('input[name="remarks"]:checked').val();
    if (selectedValue == '1') { $("#remarksId").show(); } else { $("#remarksId").hide(); }
  }

  // Modal helpers
  function view(gst, date, period){
    $.ajax({
      url: '/checkLoginStatus',
      method: 'get',
      async: false,
      success: function(result){
        if (result === 'true'){
          const url = '/fo/view_case/id?gst=' + encodeURIComponent(gst)
                    + '&date=' + encodeURIComponent(date)
                    + '&period=' + encodeURIComponent(period);
          $("#updateSummaryViewBody").load(url, function(resp, status){
            if (status === 'success'){ $("#updateSummaryViewModal").modal('show'); }
          });
        } else if (result === 'false'){ window.location.reload(); }
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

  // Enable OK only when checkbox checked
  $(function(){
    $('#mycheckbox').on('change', function(){
      $("#checked").toggle(this.checked);
    });

    $("#okayBtn").on("click", function(){ $("#closeCaseModal").modal('hide'); });
    $("#closeBtn").on("click", function(){ $("#closeCaseModal").modal('hide'); });
  });

  // Update Case ID form submit + PDF validation
  $('#updatecaseidForm').on('submit', function(e){
    e.preventDefault();

    var oldCaseId = ($('#caseid').val()||'').trim();
    var newCaseId = ($('#newcaseid').val()||'').trim();

    $.confirm({
      title : 'Confirm!',
      content : 'Do you want to proceed ahead to request case id updation?',
      buttons : {
        submit : function() {
          var input = document.getElementById('filePath');
          if (!(input && input.files && input.files[0])){ $.alert("Please upload pdf file"); return; }
          var file = input.files[0];
          var name = (file.name||'').toLowerCase();
          if (!name.endsWith('.pdf')){ $.alert("Please upload only pdf file"); input.value=''; return; }
          if (file.size > 10 * 1024 * 1024){ $.alert('Please upload max 10MB file'); input.value=''; return; }
          if (oldCaseId === newCaseId){ $.alert("New Case ID cannot be same as Old Case ID"); $("#newcaseid").val(''); return; }
          e.currentTarget.submit();
        },
        close : function(){ $.alert('Canceled!'); }
      }
    });
  });

  // DataTable
  $(function () {
    $("#scrutinyListTable").DataTable({
      responsive: false,
      lengthChange: false,
      autoWidth: true,
      scrollX: true,
      buttons: ["excel","pdf","print"]
    }).buttons().container().appendTo('#scrutinyListTable_wrapper .col-md-6:eq(0)');
  });
</script>
</body>
</html>
