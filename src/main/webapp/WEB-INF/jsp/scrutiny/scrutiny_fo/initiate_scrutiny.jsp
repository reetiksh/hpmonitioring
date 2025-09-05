<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Initiate Scrutiny</title>

  <!-- AdminLTE 4 / Bootstrap 5 -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">

  <!-- DataTables (Bootstrap 5 builds) -->
  <link rel="stylesheet" href="/static/plugins/datatables-bs5/css/dataTables.bootstrap5.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap5.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap5.min.css">

  <!-- Optional plugins (kept for existing logic) -->
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <jsp:include page="../../layout/header.jsp" />
  <!-- keep existing includes -->
  <jsp:include page="../scrutiny_fo/scrutiny_proceeding_dropped.jsp" />
  <jsp:include page="../../layout/sidebar.jsp" />

  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Header -->
        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6"><h1 class="m-0">Initiate Scrutiny</h1></div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a>Home</a></li>
                <li class="breadcrumb-item active">Initiate Scrutiny</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- Card -->
        <div class="row">
          <div class="col-12">
            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title mb-0">Initiate Scrutiny</h3>
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

                <div class="table-responsive">
                  <table id="example1" class="table table-bordered table-striped align-middle w-100">
                    <thead class="table-light">
                      <tr>
                        <th class="text-center">GSTIN</th>
                        <th class="text-center">Taxpayer Name</th>
                        <th class="text-center">Jurisdiction</th>
                        <th class="text-center">Case Category</th>
                        <th class="text-center">Period</th>
                        <th class="text-center">Reporting Date (DD-MM-YYYY)</th>
                        <th class="text-center">Indicative Value(₹)</th>
                        <th class="text-center">Parameter(s)</th>
                        <c:if test="${not remarksIsEmpty}">
                          <th class="text-center">Verifier Remarks</th>
                        </c:if>
                        <th class="text-center">Case File</th>
                        <th class="text-center">ASMT-10 Required?</th>
                      </tr>
                    </thead>
                    <tbody>
                      <c:forEach items="${mstScrutinyCasesList}" var="mstScrutinyCase">
                        <tr>
                          <td class="text-center">${mstScrutinyCase.id.GSTIN}</td>
                          <td class="text-center">${mstScrutinyCase.taxpayerName}</td>
                          <td class="text-center">${mstScrutinyCase.locationDetails.locationName}</td>
                          <td class="text-center">${mstScrutinyCase.category.name}</td>
                          <td class="text-center">${mstScrutinyCase.id.period}</td>
                          <td class="text-center"><fmt:formatDate value="${mstScrutinyCase.id.caseReportingDate}" pattern="dd-MM-yyyy" /></td>
                          <td class="text-center">${mstScrutinyCase.indicativeTaxValue}</td>
                          <td class="text-center">${mstScrutinyCase.allConcatParametersValue}</td>

                          <c:choose>
                            <c:when test="${not remarksIsEmpty}">
                              <td class="text-center">
                                <c:forEach items="${mstScrutinyCase.verifierRemarks}" var="remark">
                                  <i class="fas fa-pen nav-icon fa-xs"></i> <c:out value="${remark}" /><br/>
                                </c:forEach>
                              </td>
                            </c:when>
                          </c:choose>

                          <td class="text-center">
                            <c:if test="${mstScrutinyCase.scrutinyExtensionNoDocument.id != null}">
                              <a href="/scrutiny_fo/downloadUploadedPdfFile?fileName=${mstScrutinyCase.scrutinyExtensionNoDocument.extensionFileName}" class="btn btn-sm btn-primary" title="Download">
                                <i class="fa fa-download"></i>
                              </a>
                            </c:if>
                          </td>

                          <td class="text-center">
                            <button class="btn btn-primary"
                              onclick="scrutinyInitiated('${mstScrutinyCase.id.GSTIN}','${mstScrutinyCase.id.caseReportingDate}','${mstScrutinyCase.id.period}')">Yes</button>
                            <button class="btn btn-primary"
                              onclick="scrutinyProceedingDropped('${mstScrutinyCase.id.GSTIN}','${mstScrutinyCase.id.caseReportingDate}','${mstScrutinyCase.id.period}','${mstScrutinyCase.taxpayerName}','${mstScrutinyCase.locationDetails.locationName}','${mstScrutinyCase.category.name}','${mstScrutinyCase.indicativeTaxValue}')">No</button>
                          </td>
                        </tr>
                      </c:forEach>
                    </tbody>
                  </table>
                </div>

              </div><!-- /.card-body -->
            </div>
          </div>
        </div>

      </div><!-- /.container-fluid -->
    </div><!-- /.app-content -->
  </main>

  <jsp:include page="../../layout/footer.jsp" />
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div><!-- /.app-wrapper -->

<!-- ====================== SCRIPTS ====================== -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<!-- DataTables (Bootstrap 5 builds) -->
<script src="/static/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="/static/plugins/datatables-bs5/js/dataTables.bootstrap5.min.js"></script>
<script src="/static/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="/static/plugins/datatables-responsive/js/responsive.bootstrap5.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/dataTables.buttons.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.bootstrap5.min.js"></script>
<script src="/static/plugins/jszip/jszip.min.js"></script>
<script src="/static/plugins/pdfmake/pdfmake.min.js"></script>
<script src="/static/plugins/pdfmake/vfs_fonts.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.html5.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.print.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.colVis.min.js"></script>

<!-- Optional (kept for existing flows) -->
<script src="/static/dist/js/bootstrap-select.min.js"></script>

<script>
  // ---- Bootstrap 5 modal helpers (replace old jQuery .modal calls) ----
  function showModal(id){ const el=document.getElementById(id); if(!el) return; bootstrap.Modal.getOrCreateInstance(el).show(); }
  function hideModal(id){ const el=document.getElementById(id); if(!el) return; const m=bootstrap.Modal.getInstance(el); if(m) m.hide(); }

  // Hardening (unchanged)
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
  (function disableBFCache(){
    function f(){ window.history.forward(); }
    window.onload = f;
    window.onpageshow = evt => { if (evt.persisted) f(); };
  })();

  // Flash fade
  $(function(){ $("#message").fadeTo(2000, 500).slideUp(500, () => $("#message").remove()); });

  // DataTables init (dataset/logic unchanged)
  $(function () {
    const dt = $("#example1").DataTable({
      responsive: false,
      lengthChange: false,
      autoWidth: true,
      scrollX: true,
      buttons: ["excel","pdf","print"]
    });
    dt.buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
  });

  // Existing handlers (kept), swapped to BS5 modal helpers where needed
  $(document).ready(function(){
    $(document).on('click', '.acknowledgeBtn', function(){
      var gstno = $(this).data('gstnid');
      var date  = $(this).data('reportingdateid');
      var period= $(this).data('periodid');
      var row   = $(this).closest('tr');
      $.confirm({
        title:'Confirm!',
        content:'Do you want to proceed ahead with the acknowledgement of case?',
        buttons:{
          submit:function(){
            $.ajax({
              url:'/scrutiny_fo/acknowledged_case',
              method:'post',
              data:{gstno:gstno,date:date,period:period},
              beforeSend:function(xhr){ xhr.setRequestHeader('${_csrf.headerName}','${_csrf.token}'); },
              success:function(result){
                $.alert(result);
                row.find('.acknowledgeBtn').prop('disabled', true);
              }
            });
          },
          close:function(){ $.alert('Canceled!'); }
        }
      });
    });
  });

  function acknowledge(gstno, date, period, event){
    var button = event.target;
    button.disabled = true;
    $.confirm({
      title:'Confirm!',
      content:'Do you want to proceed ahead with the acknowledgement of case?',
      buttons:{
        submit:function(){
          $.ajax({
            url:'/fo/fo_acknowledge_cases',
            method:'post',
            data:{gstno:gstno,date:date,period:period},
            beforeSend:function(xhr){ xhr.setRequestHeader('${_csrf.headerName}','${_csrf.token}'); },
            success:function(result){ $.alert(result); }
          });
        },
        close:function(){ $.alert('Canceled!'); }
      }
    });
  }

  function scrutinyInitiated(gstin,reportingDate,period){
    var url = '/scrutiny_fo/scrutiny_initiated?gstin=' + encodeURIComponent(gstin)
            + '&reportingDate=' + encodeURIComponent(reportingDate)
            + '&period=' + encodeURIComponent(period);
    window.location.href = url;
  }

  function scrutinyProceedingDropped(gstno, date, period, taxpayerName, locationName, category, indicativeTaxValue) {
    $("#gstin").val(gstno);
    $("#casereportingdate").val(formatTheDate(date));
    $("#period").val(period);
    $("#taxpayerName").val(taxpayerName);
    $("#circle").val(locationName);
    $("#caseCategory").val(category);
    $("#susIndicativeTax").val(indicativeTaxValue);
    showModal('scrutinyProceedingDroppedModal');
  }

  // Transfer submit flow (unchanged logic) – only modal show/hide adapted
  $('#transferCaseBtn').on('click', function(oEvent){
    oEvent.preventDefault();
    var gstno = $("#gstno").val();
    var date = $("#date").val();
    var period = $("#period").val();
    var caseAssignedTo = $("#caseAssignedTo").val();
    var otherRemarks = $("#remarksId").val();
    var remarkOptions = $('input[name="remarkOptions"]:checked').val();

    $.confirm({
      title:'Confirm!',
      content:'Do you want to proceed ahead with transfer of this case?',
      buttons:{
        submit:function(){
          var remarkOptions = $('input[name="remarkOptions"]:checked').val();
          var otherRemarks = $("#remarksId").val().trim();
          var caseAssign = $("#caseAssignedTo").val();

          if(caseAssign == 'NC'){
            var fileName = document.querySelector('#uploadedFile').value;
            var extension = fileName.slice((fileName.lastIndexOf(".") - 1 >>> 0) + 2);
            var input = document.getElementById('uploadedFile');

            if (input.files && input.files[0]) {
              var maxAllowedSize = 10 * 1024 * 1024;
              if(extension == 'pdf'){
                if(input.files[0].size > maxAllowedSize){
                  $.alert('Please upload max 10MB file');
                  input.value = '';
                } else {
                  if (remarkOptions == 2) {
                    var formData = new FormData();
                    formData.append('uploadedFile', $('#uploadedFile')[0].files[0]);
                    formData.append('gstno', gstno);
                    formData.append('date', date);
                    formData.append('period', period);
                    formData.append('caseAssignedTo', caseAssignedTo);
                    formData.append('remarkOptions', remarkOptions);

                    $.ajax({
                      url:'/fo/fo_transfer_cases',
                      method:'post',
                      data: formData,
                      processData:false,
                      contentType:false,
                      beforeSend:function(xhr){ xhr.setRequestHeader('${_csrf.headerName}','${_csrf.token}'); },
                      success:function(result){
                        hideModal('scrutinyProceedingDroppedModal');
                        $.alert(result);
                      }
                    });
                  } else {
                    alert("Please choose correct Remarks");
                  }
                }
              } else {
                $.alert("Please upload only pdf file");
                document.querySelector('#uploadedFile').value = '';
              }
            } else {
              $.alert("Please upload pdf file");
            }

          } else {
            if (remarkOptions == 1) {
              if (otherRemarks != '') {
                $.ajax({
                  url:'/fo/fo_transfer_cases',
                  method:'post',
                  data:{ gstno:gstno, date:date, period:period, caseAssignedTo:caseAssignedTo, otherRemarks:otherRemarks, remarkOptions:remarkOptions },
                  beforeSend:function(xhr){ xhr.setRequestHeader('${_csrf.headerName}','${_csrf.token}'); },
                  success:function(result){
                    hideModal('scrutinyProceedingDroppedModal');
                    $.alert(result);
                  }
                });
              } else {
                $.alert('Please enter Others remarks');
              }
            } else {
              $.ajax({
                url:'/fo/fo_transfer_cases',
                method:'post',
                data:{ gstno:gstno, date:date, period:period, caseAssignedTo:caseAssignedTo, remarkOptions:remarkOptions },
                beforeSend:function(xhr){ xhr.setRequestHeader('${_csrf.headerName}','${_csrf.token}'); },
                success:function(result){
                  hideModal('scrutinyProceedingDroppedModal');
                  $.alert(result);
                }
              });
            }
          }
        },
        close:function(){ $.alert('Canceled!'); }
      }
    });
  });

  // Reset form on close
  document.getElementById('scrutinyProceedingDroppedModal')?.addEventListener('hidden.bs.modal', function(){
    $('#TransferForm')[0]?.reset();
    $('#caseAssignedTo').val('');
    $('#caseAssignedTo').selectpicker('val','');
  });

  // Legacy buttons using old IDs -> mapped to BS5 helpers
  $(function(){
    $("#transferBtn").on("click", function(){ showModal('scrutinyProceedingDroppedModal'); });
    $("#okayBtn").on("click", function(){ hideModal('confirmationModal'); });
    $("#cancelBtn").on("click", function(){ hideModal('confirmationModal'); });
  });

</script>
</body>
</html>
