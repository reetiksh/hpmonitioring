<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Random Verification Of Cases</title>

  <!-- Keep fonts & icons -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css"/>

  <!-- DataTables Bootstrap 5 styles (AdminLTE 4 uses BS5) -->
  <link rel="stylesheet" href="/static/plugins/datatables-bs5/css/dataTables.bootstrap5.min.css"/>
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap5.min.css"/>
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap5.min.css"/>

  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css"/>
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css"/>
</head>
<body class="layout-fixed sidebar-expand-lg">
<div class="wrapper">
  <jsp:include page="../../layout/header.jsp"/>
  <jsp:include page="../../layout/sidebar.jsp"/>
  <jsp:include page="../scrutiny_hq/random_recommended_pop_up.jsp"/>

  <div class="content-wrapper">
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Random Verification Of Cases</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="/ap/dashboard">Home</a></li>
              <li class="breadcrumb-item active">Random Verification Of Cases</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-12">
            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title">Random Verification Of Cases</h3>
              </div>

              <div class="card-body">
                <c:if test="${not empty message}">
                  <div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
                    <strong>${message}</strong>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                  </div>
                </c:if>

                <table id="example1" class="table table-bordered table-striped">
                  <thead>
                    <tr>
                      <th style="text-align: center; vertical-align: middle;">GSTIN</th>
                      <th style="text-align: center; vertical-align: middle;">Taxpayer Name</th>
                      <th style="text-align: center; vertical-align: middle;">Jurisdiction</th>
                      <th style="text-align: center; vertical-align: middle;">Period</th>
                      <th style="text-align: center; vertical-align: middle;">Reporting Date (DD-MM-YYYY)</th>
                      <th style="text-align: center; vertical-align: middle;">Indicative Value(₹)</th>
                      <th style="text-align: center; vertical-align: middle;">Case Category</th>
                      <th style="text-align: center; vertical-align: middle;">Amount(₹)</th>
                      <th style="text-align: center; vertical-align: middle;">Recovery Stage</th>
                      <th style="text-align: center; vertical-align: middle;">Recovery Stage ARN</th>
                      <th style="text-align: center; vertical-align: middle;">Recovery Via DRC03(₹)</th>
                      <th style="text-align: center; vertical-align: middle;">Status</th>
                      <th style="text-align: center; vertical-align: middle;">Case File</th>
                      <th style="text-align: center; vertical-align: middle;">Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach items="${mstScrutinyCasesList}" var="object" varStatus="loop">
                      <tr>
                        <td style="text-align: center; vertical-align: middle;"><c:out value="${object.id.GSTIN}" /></td>
                        <td style="text-align: center; vertical-align: middle;"><c:out value="${object.taxpayerName}" /></td>
                        <td style="text-align: center; vertical-align: middle;"><c:out value="${object.locationDetails.locationName}" /></td>
                        <td style="text-align: center; vertical-align: middle;"><c:out value="${object.id.period}" /></td>
                        <td style="text-align: center; vertical-align: middle;"><fmt:formatDate value="${object.id.caseReportingDate}" pattern="dd-MM-yyyy" /></td>
                        <td style="text-align: center; vertical-align: middle;"><c:out value="${object.indicativeTaxValue}" /></td>
                        <td style="text-align: center; vertical-align: middle;"><c:out value="${object.category.name}" /></td>
                        <td style="text-align: center; vertical-align: middle;"><c:out value="${object.amountRecovered}" /></td>
                        <td style="text-align: center; vertical-align: middle;"><c:out value="${object.recoveryStage.name}" /></td>
                        <td style="text-align: center; vertical-align: middle;"><c:out value="${object.recoveryStageArn}" /></td>
                        <td style="text-align: center; vertical-align: middle;"><c:out value="${object.recoveryByDRC03}" /></td>
                        <td style="text-align: center; vertical-align: middle;"><c:out value="${object.actionDescription}" /></td>

                        <td style="text-align: center; vertical-align: middle;">
                          <c:if test="${object.filePath != null}">
                            <a href="/scrutiny_hq/downloadUploadedPdfFile?fileName=${object.filePath}">
                              <button type="button" class="btn btn-primary">
                                <i class="fa fa-download"></i>
                              </button>
                            </a>
                          </c:if>
                        </td>

                        <td style="text-align: center; vertical-align: middle;">
                          <button type="button" onclick="callRecommendedScrutinyCase('${object.id.GSTIN}','${object.id.caseReportingDate}','${object.id.period}');" class="btn btn-primary" id="appealRevisioinRejectBtn">Recommend</button>
                        </td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>
              </div>

            </div>
          </div>
        </div>
      </div>
    </section>
  </div>

  <jsp:include page="../../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- Core -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- DataTables + Bootstrap 5 -->
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

<script src="/static/dist/js/jquery-confirm.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<script>
$(document).ready(function() {
  $("#message").fadeTo(2000, 500).slideUp(500, function() { $("#message").slideUp(500); });

  $("#example1").DataTable({
    responsive: false,
    lengthChange: false,
    autoWidth: true,
    scrollX: true,
    buttons: ["excel","pdf","print"]
  }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
});
</script>

<script>
  function callRecommendedScrutinyCase(gstin,reportingDate,period){
    $("#recommendScrutinyGstin").val(gstin);
    $("#recommendScrutinyPeriod").val(period);
    $("#recommendScrutinyCaseReportingDate").val(reportingDate);
    $("#randomRecommendScrutinyCaseModal").modal('show');
  }

  // Keep your existing prevention/disable logic unchanged
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => { if (e.ctrlKey && e.key === 'u') e.preventDefault(); });
  document.addEventListener('keydown', e => { if (e.key === 'F12') e.preventDefault(); });
  $(function(){
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = e => { if (e.persisted) disableBack(); }
  });
  document.onkeydown = function (e) {
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) e.preventDefault();
  };
</script>
</body>
</html>
