<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Acknowledge/Transfer Case List</title>

<link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
<link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
<link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
<link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
<link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
<link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
<link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">
  <jsp:include page="../../layout/header.jsp" />
  <jsp:include page="../scrutiny_fo/transfer_popup.jsp" />
  <jsp:include page="../../layout/sidebar.jsp" />

  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <section class="content-header px-0">
          <div class="row mb-2">
            <div class="col-sm-6">
              <h1>Acknowledge/Transfer Cases</h1>
            </div>
            <div class="col-sm-6">
              <ol class="breadcrumb float-sm-end">
                <li class="breadcrumb-item"><a>Home</a></li>
                <li class="breadcrumb-item active">Acknowledge/Transfer Case</li>
              </ol>
            </div>
          </div>
        </section>

        <section class="content px-0">
          <div class="row">
            <div class="col-12">
              <div class="card card-primary">
                <div class="card-header">
                  <h3 class="card-title">Pending for Acknowledge/Transfer List</h3>
                </div>
                <div class="card-body">
                  <c:if test="${not empty message}">
                    <div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
                      <strong>${message}</strong>
                      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                  </c:if>

                  <c:if test="${not empty acknowlegdemessage}">
                    <div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
                      <strong>${acknowlegdemessage}</strong>
                      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                  </c:if>

                  <table id="example1" class="table table-bordered table-striped">
                    <thead>
                      <tr>
                        <th class="text-center align-middle">GSTIN</th>
                        <th class="text-center align-middle">Taxpayer Name</th>
                        <th class="text-center align-middle">Jurisdiction</th>
                        <th class="text-center align-middle">Case Category</th>
                        <th class="text-center align-middle">Period</th>
                        <th class="text-center align-middle">Reporting Date (DD-MM-YYYY)</th>
                        <th class="text-center align-middle">Indicative Value(₹)</th>
                        <th class="text-center align-middle">Parameter(s)</th>
                        <c:if test="${not remarksIsEmpty}">
                          <th class="text-center align-middle">Head Office Remarks</th>
                        </c:if>
                        <th class="text-center align-middle">Case File</th>
                        <th class="text-center align-middle" style="width:200px;">Action</th>
                      </tr>
                    </thead>
                    <tbody>
                      <c:forEach items="${mstScrutinyCasesList}" var="mstScrutinyCase">
                        <tr>
                          <td class="text-center align-middle">${mstScrutinyCase.id.GSTIN}</td>
                          <td class="text-center align-middle">${mstScrutinyCase.taxpayerName}</td>
                          <td class="text-center align-middle">${mstScrutinyCase.locationDetails.locationName}</td>
                          <td class="text-center align-middle">${mstScrutinyCase.category.name}</td>
                          <td class="text-center align-middle">${mstScrutinyCase.id.period}</td>
                          <td class="text-center align-middle"><fmt:formatDate value="${mstScrutinyCase.id.caseReportingDate}" pattern="dd-MM-yyyy" /></td>
                          <td class="text-center align-middle">${mstScrutinyCase.indicativeTaxValue}</td>
                          <td class="text-center align-middle">${mstScrutinyCase.allConcatParametersValue}</td>

                          <c:if test="${not remarksIsEmpty}">
                            <td class="text-center align-middle">
                              <c:forEach items="${mstScrutinyCase.hqRemarks}" var="remark">
                                <i class="fas fa-pen nav-icon fa-xs"></i> <c:out value="${remark}" /><br/>
                              </c:forEach>
                            </td>
                          </c:if>

                          <td class="text-center align-middle">
                            <c:if test="${mstScrutinyCase.scrutinyExtensionNoDocument.id != null}">
                              <a href="/scrutiny_fo/downloadUploadedPdfFile?fileName=${mstScrutinyCase.scrutinyExtensionNoDocument.extensionFileName}">
                                <button type="button" class="btn btn-primary">
                                  <i class="fa fa-download"></i>
                                </button>
                              </a>
                            </c:if>
                          </td>

                          <td class="text-center align-middle">
                            <c:if test="${mstScrutinyCase.acknowlegeByFoOrNot eq true}">
                              <button class="btn btn-primary acknowledgeBtn"
                                      data-gstnid="${mstScrutinyCase.id.GSTIN}"
                                      data-reportingdateid="${mstScrutinyCase.id.caseReportingDate}"
                                      data-periodid="${mstScrutinyCase.id.period}"
                                      disabled>Acknowledge</button>
                            </c:if>
                            <c:if test="${mstScrutinyCase.acknowlegeByFoOrNot eq false}">
                              <button class="btn btn-primary acknowledgeBtn"
                                      data-gstnid="${mstScrutinyCase.id.GSTIN}"
                                      data-reportingdateid="${mstScrutinyCase.id.caseReportingDate}"
                                      data-periodid="${mstScrutinyCase.id.period}">Acknowledge</button>
                            </c:if>

                            <button class="btn btn-primary transferBtn"
                                    onclick="transferBtn('${mstScrutinyCase.id.GSTIN}','${mstScrutinyCase.id.caseReportingDate}','${mstScrutinyCase.id.period}','${mstScrutinyCase.locationDetails.locationId}')">Transfer</button>
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
    </div>
  </main>

  <jsp:include page="../../layout/footer.jsp" />
</div>

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
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<script>
  // Hardening (unchanged)
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key;
    if ((e.ctrlKey && k === 'u') || k === 'F12' || k === 'F5' || (e.ctrlKey && k === 'r') || e.keyCode === 116) e.preventDefault();
  });
  $(function(){ function disableBack(){window.history.forward()} window.onload=disableBack; window.onpageshow=function(evt){if(evt.persisted)disableBack()} });

  // DataTable (unchanged)
  $(function () {
    $("#example1").DataTable({
      responsive: true,
      lengthChange: false,
      autoWidth: false,
      buttons: ["excel","pdf","print"]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
  });

  // Helper for BS5 modal show/hide
  function showBsModal(id){
    const el = document.getElementById(id);
    if (!el) return;
    (bootstrap.Modal.getInstance(el) || new bootstrap.Modal(el)).show();
  }
  function hideBsModal(id){
    const el = document.getElementById(id);
    if (!el) return;
    const inst = bootstrap.Modal.getInstance(el);
    if (inst) inst.hide();
  }

  // Acknowledge via AJAX (unchanged logic)
  $(document).on('click', '.acknowledgeBtn', function(){
    const gstno = $(this).data('gstnid');
    const date  = $(this).data('reportingdateid');
    const period= $(this).data('periodid');
    const row   = $(this).closest('tr');
    $.confirm({
      title: 'Confirm!',
      content: 'Do you want to proceed ahead with the acknowledgement of case?',
      buttons: {
        submit: function(){
          $.ajax({
            url: '/scrutiny_fo/acknowledged_case',
            method: 'post',
            data: { gstno, date, period },
            beforeSend: function(xhr){ xhr.setRequestHeader('${_csrf.headerName}','${_csrf.token}'); },
            success: function(result){
              $.alert(result);
              row.find('.acknowledgeBtn').prop('disabled', true);
            }
          });
        },
        close: function(){ $.alert('Canceled!'); }
      }
    });
  });

  // Old direct acknowledge() kept for compatibility (unchanged logic; uses BS5 hide)
  function acknowledge(gstno, date, period, event){
    const button = event.target; button.disabled = true;
    $.confirm({
      title: 'Confirm!',
      content: 'Do you want to proceed ahead with the acknowledgement of case?',
      buttons: {
        submit: function(){
          $.ajax({
            url: '/fo/fo_acknowledge_cases',
            method: 'post',
            data: { gstno, date, period },
            beforeSend: function(xhr){ xhr.setRequestHeader('${_csrf.headerName}','${_csrf.token}'); },
            success: function(result){ $.alert(result); }
          });
        },
        close: function(){ $.alert('Canceled!'); }
      }
    });
  }

  // Transfer modal open (uses BS5 API)
  function transferBtn(gstno, date, period, jurisdiction){
    $("#gstno").val(gstno);
    $("#date").val(date);
    $("#period").val(period);
    $("#jurisdiction").val(jurisdiction);
    showBsModal('transferModal');
  }

  // Submit transfer (unchanged logic; just hides with BS5)
  $('#transferCaseBtn').on('click', function(oEvent){
    oEvent.preventDefault();
    const gstno = $("#gstno").val();
    const date = $("#date").val();
    const period = $("#period").val();
    const caseAssignedTo = $("#caseAssignedTo").val();
    const otherRemarks = $("#remarksId").val();
    const remarkOptions = $('input[name="remarkOptions"]:checked').val();

    if(remarkOptions === undefined){ $.alert('Please select remarks.'); return; }

    $.confirm({
      title: 'Confirm!',
      content: 'Do you want to proceed ahead with transfer of this case?',
      buttons: {
        submit: function(){
          const caseAssign = $("#caseAssignedTo").val();
          if(caseAssign === 'NC'){
            const fileInput = document.getElementById('uploadedFile');
            const fileName = (fileInput && fileInput.value) ? fileInput.value : '';
            const ext = fileName.slice((fileName.lastIndexOf(".") - 1 >>> 0) + 2);
            if(fileInput && fileInput.files && fileInput.files[0]){
              const maxSize = 10 * 1024 * 1024;
              if(ext === 'pdf'){
                if(fileInput.files[0].size > maxSize){
                  $.alert('Please upload max 10MB file'); fileInput.value=''; return;
                }else{
                  if(remarkOptions == 2){
                    const formData = new FormData();
                    formData.append('uploadedFile', fileInput.files[0]);
                    formData.append('gstno', gstno);
                    formData.append('date', date);
                    formData.append('period', period);
                    formData.append('caseAssignedTo', caseAssignedTo);
                    formData.append('remarkOptions', remarkOptions);
                    $.ajax({
                      url: '/scrutiny_fo/request_for_transfer',
                      method: 'post',
                      data: formData,
                      processData: false,
                      contentType: false,
                      beforeSend: function(xhr){ xhr.setRequestHeader('${_csrf.headerName}','${_csrf.token}'); },
                      success: function(result){ hideBsModal('transferModal'); $.alert(result); }
                    });
                  }else{
                    alert("Please choose correct Remarks");
                  }
                }
              }else{
                $.alert("Please upload only pdf file"); fileInput.value='';
              }
            }else{
              $.alert("Please upload pdf file");
            }
          }else{
            if (remarkOptions == 1){
              if ((otherRemarks || '').trim() !== ''){
                $.ajax({
                  url: '/scrutiny_fo/request_for_transfer',
                  method: 'post',
                  data: { gstno, date, period, caseAssignedTo, otherRemarks, remarkOptions },
                  beforeSend: function(xhr){ xhr.setRequestHeader('${_csrf.headerName}','${_csrf.token}'); },
                  success: function(result){ hideBsModal('transferModal'); $.alert(result); }
                });
              } else {
                $.alert('Please enter Others remarks');
              }
            } else {
              $.ajax({
                url: '/scrutiny_fo/request_for_transfer',
                method: 'post',
                data: { gstno, date, period, caseAssignedTo, remarkOptions },
                beforeSend: function(xhr){ xhr.setRequestHeader('${_csrf.headerName}','${_csrf.token}'); },
                success: function(result){ hideBsModal('transferModal'); $.alert(result); }
              });
            }
          }
        },
        close: function(){ $.alert('Canceled!'); }
      }
    });
  });

  // Reset transfer form when hidden (event name stays the same in BS5)
  $('#transferModal').on('hidden.bs.modal', function(){
    $('#TransferForm')[0].reset();
    $('#caseAssignedTo').val('');
    $('#caseAssignedTo').selectpicker('val','');
  });

  // Auto-hide alert
  $(function(){
    $("#message").fadeTo(2000, 500).slideUp(500, function(){ $("#message").slideUp(500); });
  });
</script>
</body>
</html>
