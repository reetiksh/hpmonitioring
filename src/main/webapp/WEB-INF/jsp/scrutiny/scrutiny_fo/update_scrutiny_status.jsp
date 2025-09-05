<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Update Scrutiny Status</title>

  <!-- AdminLTE 4 / Bootstrap 5 -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css" />
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css" />

  <!-- DataTables (Bootstrap 5 builds) -->
  <link rel="stylesheet" href="/static/plugins/datatables-bs5/css/dataTables.bootstrap5.min.css" />
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap5.min.css" />
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap5.min.css" />

  <!-- Optional -->
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css" />
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css" />
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">

<div class="app-wrapper">
  <!-- Header / Sidebar -->
  <jsp:include page="../../layout/header.jsp" />
  <jsp:include page="../scrutiny_fo/scrutiny_proceeding_dropped.jsp" />
  <jsp:include page="../../layout/sidebar.jsp" />

  <!-- ====================== MAIN ====================== -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page header -->
        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6">
              <h1 class="m-0">Update Scrutiny Status</h1>
            </div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a>Home</a></li>
                <li class="breadcrumb-item active">Update Scrutiny Status</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- Card -->
        <div class="row">
          <div class="col-12">
            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title mb-0">Update Scrutiny Status</h3>
              </div>

              <div class="card-body">

                <!-- Flash messages (unchanged logic) -->
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
                        <th class="text-center">ASMT-10 Issued Or Not ?</th>
                        <th class="text-center">Indicative Value(₹)</th>
                        <th class="text-center">Parameter(s)</th>
                        <th class="text-center">Action</th>
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
                          <td class="text-center">
                            <fmt:formatDate value="${mstScrutinyCase.id.caseReportingDate}" pattern="dd-MM-yyyy" />
                          </td>
                          <td class="text-center">Issued</td>
                          <td class="text-center">${mstScrutinyCase.indicativeTaxValue}</td>
                          <td class="text-center">${mstScrutinyCase.allConcatParametersValue}</td>
                          <td class="text-center">
                            <button class="btn btn-sm btn-primary"
                              onclick="updateScrutinyStatus('${mstScrutinyCase.id.GSTIN}','${mstScrutinyCase.id.caseReportingDate}','${mstScrutinyCase.id.period}')"
                              title="Update scrutiny status">
                              <i class="fas fa-edit" aria-hidden="true"></i>
                              <span class="visually-hidden">Update scrutiny status</span>
                            </button>
                          </td>
                        </tr>
                      </c:forEach>
                    </tbody>
                  </table>
                </div><!-- /.table-responsive -->

              </div><!-- /.card-body -->
            </div>
          </div>
        </div>

      </div><!-- /.container-fluid -->
    </div><!-- /.app-content -->
  </main>

  <jsp:include page="../../layout/footer.jsp" />
</div><!-- /.app-wrapper -->

<!-- ====================== SCRIPTS (order matters) ====================== -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
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

<!-- Optional -->
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  // DataTables init (keeps logic/dataset intact)
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

  // Safer URL building (keeps routing/params intact)
  function updateScrutinyStatus(gstin, reportingDate, period){
    const params = new URLSearchParams({
      gstin: gstin,
      reportingDate: reportingDate,
      period: period
    });
    window.location.href = '/scrutiny_fo/scrutiny_status_updated?' + params.toString();
  }

  // Existing hardening (unchanged)
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

  // Fade out alert
  $(function () {
    $("#message").each(function () {
      $(this).fadeTo(2000, 0.5).slideUp(500, () => $(this).remove());
    });
  });
</script>
</body>
</html>
