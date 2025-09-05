<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Cases for Mandatory Verification</title>

  <!-- AdminLTE / Bootstrap 4 / Plugins -->
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
  <!-- Contains the modal & form fields recommendScrutinyGstin/recommendScrutinyPeriod/recommendScrutinyCaseReportingDate -->
  <jsp:include page="../scrutiny_ru/recommended_pop_up.jsp"/>

  <div class="content-wrapper">
    <!-- Header -->
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Cases for Mandatory Verification</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="/ap/dashboard">Home</a></li>
              <li class="breadcrumb-item active">Cases for Mandatory Verification</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <!-- Main -->
    <section class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-12">

            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title mb-0">Cases for Mandatory Verification</h3>
              </div>

              <div class="card-body">
                <!-- Flash message -->
                <c:if test="${not empty message}">
                  <div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
                    <strong><c:out value="${message}"/></strong>
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                      <span aria-hidden="true">&times;</span>
                    </button>
                  </div>
                </c:if>

                <div class="table-responsive">
                  <table id="example1" class="table table-bordered table-striped w-100">
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
                        <th class="text-center align-middle">Recovery Via DRC-03 (₹)</th>
                        <th class="text-center align-middle">Case File</th>
                        <th class="text-center align-middle">Action</th>
                      </tr>
                    </thead>
                    <tbody>
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
                          <td class="text-center align-middle">
                            <c:if test="${object.filePath != null}">
                              <a class="btn btn-sm btn-primary"
                                 href="/scrutiny_ru/downloadUploadedPdfFile?fileName=${object.filePath}"
                                 title="Download case file">
                                <i class="fa fa-download"></i>
                              </a>
                            </c:if>
                          </td>
                          <td class="text-center align-middle">
                            <!-- Safer: pass data via attributes instead of inline JS params -->
                            <button type="button"
                                    class="btn btn-sm btn-primary js-recommend"
                                    data-gstin="${fn:escapeXml(object.id.GSTIN)}"
                                    data-period="${fn:escapeXml(object.id.period)}"
                                    data-reportingdate="${fn:escapeXml(object.id.caseReportingDate)}">
                              Recommend
                            </button>
                          </td>
                        </tr>
                      </c:forEach>
                    </tbody>
                  </table>
                </div>

              </div><!-- /card-body -->
            </div><!-- /card -->

          </div>
        </div>
      </div>
    </section>
  </div>

  <jsp:include page="../../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- Scripts -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- DataTables -->
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
  // Flash message fade out
  $(function () {
    $("#message").fadeTo(2000, 0.5).slideUp(500, function () {
      $(this).remove();
    });
  });

  // DataTable
  $(function () {
    const dt = $("#example1").DataTable({
      responsive: false,
      lengthChange: false,
      autoWidth: true,
      scrollX: true,
      buttons: ["excel", "pdf", "print"]
    });
    dt.buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
  });

  // Event delegation: open Recommend modal and hydrate fields
  $(document).on('click', '.js-recommend', function () {
    const gstin = $(this).data('gstin') || '';
    const period = $(this).data('period') || '';
    const reportingDate = $(this).data('reportingdate') || '';

    $("#recommendScrutinyGstin").val(gstin);
    $("#recommendScrutinyPeriod").val(period);
    $("#recommendScrutinyCaseReportingDate").val(reportingDate);

    $("#recommendScrutinyCaseModal").modal('show');
  });
</script>
</body>
</html>
