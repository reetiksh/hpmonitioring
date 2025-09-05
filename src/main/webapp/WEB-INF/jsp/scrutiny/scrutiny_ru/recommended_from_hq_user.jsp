<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>HP GST | Recommended From HQ User</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />

  <!-- AdminLTE 4 + Bootstrap 5 + FontAwesome -->
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6/css/all.min.css">

  <!-- DataTables (Bootstrap 5) + Buttons -->
  <link rel="stylesheet" href="https://cdn.datatables.net/v/bs5/dt-2.0.7/b-3.0.2/r-3.0.2/datatables.min.css"/>

  <style>
    .table th, .table td { vertical-align: middle; text-align: center; }
  </style>
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
  <div class="app-wrapper">
    <!-- Header -->
    <nav class="app-header navbar navbar-expand bg-body">
      <div class="container-fluid">
        <a class="navbar-brand d-flex align-items-center gap-2" href="/ap/dashboard">
          <img src="/static/files/hp_logo.png" alt="HP GST" width="28" height="28" class="rounded"/>
          <span class="fw-semibold">HP GST</span>
        </a>
        <ul class="navbar-nav ms-auto">
          <li class="nav-item">
            <a class="nav-link" href="/ap/dashboard" title="Home">
              <i class="fa-solid fa-house"></i>
            </a>
          </li>
        </ul>
      </div>
    </nav>

    <!-- Sidebar (minimal) -->
    <aside class="app-sidebar bg-body-secondary shadow" id="sidebar">
      <div class="sidebar-brand">
        <a href="/ap/dashboard" class="brand-link">
          <img src="/static/files/hp_logo.png" alt="HP GST" class="brand-image opacity-75 shadow" />
          <span class="brand-text fw-bold">AP</span>
        </a>
      </div>
      <div class="sidebar-wrapper">
        <nav class="mt-2">
          <ul class="nav nav-pills nav-sidebar flex-column" data-lte-toggle="treeview" role="menu">
            <li class="nav-item">
              <a href="/ap/dashboard" class="nav-link">
                <i class="nav-icon fa-solid fa-gauge"></i><p>Dashboard</p>
              </a>
            </li>
            <li class="nav-item">
              <a href="#" class="nav-link active">
                <i class="nav-icon fa-solid fa-clipboard-check"></i><p>Recommended From HQ User</p>
              </a>
            </li>
          </ul>
        </nav>
      </div>
    </aside>

    <!-- Main -->
    <main class="app-main">
      <div class="app-content pt-3 px-3">
        <div class="container-fluid">

          <!-- Page header -->
          <div class="row mb-2">
            <div class="col-sm-6">
              <h1 class="h3 mb-0">Recommended From HQ User</h1>
            </div>
            <div class="col-sm-6">
              <ol class="breadcrumb float-sm-end">
                <li class="breadcrumb-item"><a href="/ap/dashboard">Home</a></li>
                <li class="breadcrumb-item active" aria-current="page">Recommended From HQ User</li>
              </ol>
            </div>
          </div>

          <!-- Flash message -->
          <c:if test="${not empty message}">
            <div class="alert alert-success alert-dismissible fade show" role="alert" id="message">
              <strong>${message}</strong>
              <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
          </c:if>

          <!-- Card -->
          <div class="card card-primary card-outline">
            <div class="card-header">
              <h3 class="card-title mb-0">Recommended From HQ User</h3>
            </div>
            <div class="card-body">
              <table id="example1" class="table table-bordered table-striped w-100">
                <thead class="table-light">
                  <tr>
                    <th>GSTIN</th>
                    <th>Taxpayer Name</th>
                    <th>Jurisdiction</th>
                    <th>Period</th>
                    <th>Reporting Date (DD-MM-YYYY)</th>
                    <th>Indicative Value(₹)</th>
                    <th>Case Category</th>
                    <th>Amount(₹)</th>
                    <th>Recovery Stage</th>
                    <th>Recovery Stage ARN</th>
                    <th>Recovery Via DRC03(₹)</th>
                    <th>HQ Remarks</th>
                    <th>Case File</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach items="${mstScrutinyCasesList}" var="object" varStatus="loop">
                    <tr>
                      <td><c:out value="${object.id.GSTIN}" /></td>
                      <td><c:out value="${object.taxpayerName}" /></td>
                      <td><c:out value="${object.locationDetails.locationName}" /></td>
                      <td><c:out value="${object.id.period}" /></td>
                      <td><fmt:formatDate value="${object.id.caseReportingDate}" pattern="dd-MM-yyyy" /></td>
                      <td><c:out value="${object.indicativeTaxValue}" /></td>
                      <td><c:out value="${object.category.name}" /></td>
                      <td><c:out value="${object.amountRecovered}" /></td>
                      <td><c:out value="${object.recoveryStage.name}" /></td>
                      <td><c:out value="${object.recoveryStageArn}" /></td>
                      <td><c:out value="${object.recoveryByDRC03}" /></td>
                      <td class="text-start">
                        <c:forEach items="${object.hqRemarks}" var="remark">
                          <i class="fas fa-pen fa-xs me-1"></i><c:out value="${remark}" /><br/>
                        </c:forEach>
                      </td>
                      <td>
                        <c:if test="${object.filePath != null}">
                          <a class="btn btn-primary btn-sm" href="/scrutiny_ru/downloadUploadedPdfFile?fileName=${object.filePath}" title="Download">
                            <i class="fa fa-download"></i>
                          </a>
                        </c:if>
                      </td>
                      <td>
                        <button type="button"
                                class="btn btn-primary btn-sm"
                                onclick="callRecommendedScrutinyCase('${object.id.GSTIN}','${object.id.caseReportingDate}','${object.id.period}')">
                          Recommend
                        </button>
                      </td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
          </div>

        </div>
      </div>

      <footer class="app-footer">
        <div class="float-end d-none d-sm-inline">AdminLTE 4</div>
        <strong>HP GST</strong> &copy; <script>document.write(new Date().getFullYear())</script>.
      </footer>
    </main>
  </div>

  <!-- Recommend Modal (BS5) -->
  <div class="modal fade" id="randomRecommendScrutinyCaseModal" tabindex="-1" aria-labelledby="recommendModalTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
      <form method="POST" id="recommendCaseForScrutinyDetails" action="random_recommend_for_scrutiny" class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="recommendModalTitle"><b>Recommend to Verifier for Mandatory Scrutiny</b></h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <input type="hidden" id="recommendScrutinyGstin" name="recommendScrutinyGstin">
        <input type="hidden" id="recommendScrutinyPeriod" name="recommendScrutinyPeriod">
        <input type="hidden" id="recommendScrutinyCaseReportingDate" name="recommendScrutinyCaseReportingDate">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

        <div class="modal-body">
          <div id="appealRevisionRejectRemarksMissingTagLine" class="text-danger mb-2" style="display:none;">Please enter remarks.</div>
          <label for="appealRevisionRejectRemarksValue" class="form-label">Remarks <span class="text-danger">*</span></label>
          <textarea class="form-control" id="appealRevisionRejectRemarksValue" name="recommendScrutinyRemark" placeholder="Remarks" rows="4"></textarea>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" onclick="submitAppealRevisionRejectDetails()">Submit</button>
        </div>
      </form>
    </div>
  </div>

  <!-- Core JS -->
  <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/js/adminlte.min.js"></script>

  <!-- DataTables + Buttons (BS5) -->
  <script src="https://cdn.datatables.net/v/bs5/dt-2.0.7/b-3.0.2/r-3.0.2/datatables.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/jszip@3.10.1/dist/jszip.min.js"></script>

  <script>
    // Fade flash message
    $(function () {
      $("#message").fadeTo(2000, 500).slideUp(500, function () {
        $("#message").slideUp(500);
      });

      // DataTable init
      const tbl = $('#example1');
      if (tbl.length) {
        tbl.DataTable({
          responsive: false,
          lengthChange: false,
          autoWidth: true,
          scrollX: true,
          buttons: ["excel", "print"]
        }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
      }
    });

    // Open modal with values (Bootstrap 5)
    function callRecommendedScrutinyCase(gstin, reportingDate, period) {
      document.getElementById('recommendScrutinyGstin').value = gstin;
      document.getElementById('recommendScrutinyPeriod').value = period;
      document.getElementById('recommendScrutinyCaseReportingDate').value = reportingDate;
      const modalEl = document.getElementById('randomRecommendScrutinyCaseModal');
      const modal = new bootstrap.Modal(modalEl);
      modal.show();
    }

    // Submit recommend (with validation + confirm)
    function submitAppealRevisionRejectDetails(){
      const remarks = document.getElementById("appealRevisionRejectRemarksValue").value.trim();
      const warn = document.getElementById("appealRevisionRejectRemarksMissingTagLine");
      if (!remarks) {
        warn.style.display = "block";
        return;
      }
      warn.style.display = "none";

      // Simple confirm
      if (confirm("Do you want to proceed ahead with recommending this case for mandatory scrutiny?")) {
        document.getElementById("recommendCaseForScrutinyDetails").submit();
      }
    }
  </script>
</body>
</html>
