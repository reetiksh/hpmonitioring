<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Cases for Random Verification</title>

  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
  <jsp:include page="../../layout/header.jsp"/>
  <jsp:include page="../../layout/sidebar.jsp"/>
  <jsp:include page="../scrutiny_ru/random_recommended_pop_up.jsp"/>

  <div class="content-wrapper">
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Cases for Random Verification</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="/ap/dashboard">Home</a></li>
              <li class="breadcrumb-item active">Cases for Random Verification</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-12">
            <div class="card-primary">
              <div class="card-header">
                <h3 class="card-title">Cases for Random Verification</h3>
              </div>

              <div class="card-body">
                <c:if test="${not empty message}">
                  <div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
                    <strong><c:out value="${message}"/></strong>
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                      <span aria-hidden="true">&times;</span>
                    </button>
                  </div>
                </c:if>

                <table id="example1" class="table table-bordered table-striped">
                  <thead>
                  <tr>
                    <th class="text-center align-middle">GSTIN</th>
                    <th class="text-center align-middle">Taxpayer Name</th>
                    <th class="text-center align-middle">Jurisdiction</th>
                    <th class="text-center align-middle">Period</th>
                    <th class="text-center align-middle">Reporting Date (DD-MM-YYYY)</th>
                    <th class="text-center align-middle">Indicative Value (₹)</th>
                    <th class="text-center align-middle">Case Category</th>
                    <th class="text-center align-middle">Amount (₹)</th>
                    <th class="text-center align-middle">Recovery Stage</th>
                    <th class="text-center align-middle">Recovery Stage ARN</th>
                    <th class="text-center align-middle">Recovery Via DRC03 (₹)</th>
                    <th class="text-center align-middle">Status</th>
                    <th class="text-center align-middle">Case File</th>
                    <th class="text-center align-middle">Action</th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:choose>
                    <c:when test="${not empty mstScrutinyCasesList}">
                      <c:forEach items="${mstScrutinyCasesList}" var="object">
                        <tr>
                          <td class="text-center align-middle"><c:out value="${object.id.GSTIN}"/></td>
                          <td class="text-center align-middle"><c:out value="${object.taxpayerName}"/></td>
                          <td class="text-center align-middle"><c:out value="${object.locationDetails.locationName}"/></td>
                          <td class="text-center align-middle"><c:out value="${object.id.period}"/></td>
                          <td class="text-center align-middle">
                            <fmt:formatDate value="${object.id.caseReportingDate}" pattern="dd-MM-yyyy"/>
                          </td>
                          <td class="text-center align-middle">
                            <fmt:formatNumber value="${object.indicativeTaxValue}" pattern="#,##,##0"/>
                          </td>
                          <td class="text-center align-middle"><c:out value="${object.category.name}"/></td>
                          <td class="text-center align-middle">
                            <fmt:formatNumber value="${object.amountRecovered}" pattern="#,##,##0"/>
                          </td>
                          <td class="text-center align-middle"><c:out value="${object.recoveryStage.name}"/></td>
                          <td class="text-center align-middle"><c:out value="${object.recoveryStageArn}"/></td>
                          <td class="text-center align-middle">
                            <fmt:formatNumber value="${object.recoveryByDRC03}" pattern="#,##,##0"/>
                          </td>
                          <td class="text-center align-middle"><c:out value="${object.actionDescription}"/></td>
                          <td class="text-center align-middle">
                            <c:if test="${object.filePath != null}">
                              <c:url var="dlUrl" value="/scrutiny_ru/downloadUploadedPdfFile">
                                <c:param name="fileName" value="${object.filePath}"/>
                              </c:url>
                              <a href="${dlUrl}">
                                <button type="button" class="btn btn-primary" title="Download Case File">
                                  <i class="fa fa-download"></i>
                                </button>
                              </a>
                            </c:if>
                          </td>
                          <td class="text-center align-middle">
                            <button type="button"
                                    class="btn btn-primary"
                                    title="Recommend for Random Verification"
                                    onclick="callRecommendedScrutinyCase('${object.id.GSTIN}','${object.id.caseReportingDate}','${object.id.period}')">
                              Recommend
                            </button>
                          </td>
                        </tr>
                      </c:forEach>
                    </c:when>
                    <c:otherwise>
                      <tr>
                        <td colspan="14" class="text-center text-muted">No cases available.</td>
                      </tr>
                    </c:otherwise>
                  </c:choose>
                  </tbody>
                </table>
              </div><!-- /.card-body -->
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>

  <jsp:include page="../../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<script src="/static/plugins/jquery/jquery.min.js"></script>
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
<script src="/static/dist/js/jquery-confirm.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<script>
  // Soft-dismiss success alert
  $(function(){
    $("#message").fadeTo(2000, 500).slideUp(500, function(){ $("#message").slideUp(500); });
  });

  // DataTables setup
  $(function () {
    $("#example1").DataTable({
      responsive: false,
      lengthChange: false,
      autoWidth: true,
      scrollX: true,
      buttons: ["excel","pdf","print"]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
  });

  // Open recommendation modal + seed hidden fields inside popup
  function callRecommendedScrutinyCase(gstin, reportingDate, period){
    $("#recommendScrutinyGstin").val(gstin);
    $("#recommendScrutinyPeriod").val(period);
    $("#recommendScrutinyCaseReportingDate").val(reportingDate);
    $("#randomRecommendScrutinyCaseModal").modal('show');
  }

  // (Optional) basic hardening similar to other pages
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = (e.key||'').toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
</script>
</body>
</html>
