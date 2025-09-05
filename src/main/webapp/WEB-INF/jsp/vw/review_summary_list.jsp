<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST |Review Case Summary</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">

  <!-- AdminLTE 4 (Bootstrap 5) + Font Awesome 6 -->
  <link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6/css/all.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/css/adminlte.min.css" rel="stylesheet">

  <!-- DataTables (Bootstrap 5) + Buttons -->
  <link href="https://cdn.datatables.net/v/bs5/dt-2.0.7/b-3.0.2/r-3.0.2/datatables.min.css" rel="stylesheet"/>

  <!-- (Optional) Keep local styles you already use -->
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">

  <style>
    .table thead th, .table tbody td { text-align:center; vertical-align:middle; }
  </style>
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- (Optional) Preloader kept as-is (visual only) -->
  <div class="preloader flex-column justify-content-center align-items-center">
    <img class="animation__shake" src="/static/dist/img/AdminLTELogo.png" alt="AdminLTELogo" height="60" width="60">
  </div>

  <!-- Header & Sidebar includes (unchanged) -->
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <main class="app-main">
    <!-- Content Header -->
    <div class="app-content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1 class="m-0">Review Case Summary</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-end">
              <li class="breadcrumb-item"><a href="/vw/dashboard">Home</a></li>
              <li class="breadcrumb-item active" aria-current="page">Review Case Summary</li>
            </ol>
          </div>
        </div>
      </div>
    </div>

    <!-- Main content -->
    <div class="app-content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-12">

            <div class="card card-primary card-outline">
              <div class="card-header">
                <h3 class="card-title mb-0">Review Case Summary List</h3>
              </div>

              <div class="card-body">
                <c:if test="${!empty categoryTotals}">
                  <table id="example1" class="table table-bordered table-striped w-100">
                    <thead>
                      <tr>
                        <th>Category</th>
                        <th>Total Cases</th>
                        <th>Total Indicative Value(₹)</th>
                        <th>Total Amount(₹)</th>
                        <th>Total Demand(₹)</th>
                        <th>Total Recovery(₹)</th>
                      </tr>
                    </thead>
                    <tbody>
                      <c:forEach items="${categoryTotals}" var="total">
                        <tr>
                          <td><a href="/vw/view_list_of_case?category=${total.category}"><c:out value="${total.category}" /></a></td>
                          <td><c:out value="${total.totalRows}" /></td>
                          <td><fmt:formatNumber value="${total.totalIndicativeTax}" pattern="#,##,##0"/></td>
                          <td><fmt:formatNumber value="${total.totalAmount}" pattern="#,##,##0"/></td>
                          <td><fmt:formatNumber value="${total.totalDemand}" pattern="#,##,##0"/></td>
                          <td><fmt:formatNumber value="${total.totalRecovery}" pattern="#,##,##0"/></td>
                        </tr>
                      </c:forEach>
                    </tbody>
                  </table>
                </c:if>

                <c:if test="${empty categoryTotals}">
                  <div class="col-12 text-center">
                    <i class="fa fa-info-circle" style="font-size:100px;color:rgb(97, 97, 97)" aria-hidden="true"></i><br>
                    <span style="font-size:35px;color:rgb(97, 97, 97)">No Review Case List Available</span>
                  </div>
                </c:if>
              </div>
            </div>

          </div>
        </div>
      </div>
    </div>

    <jsp:include page="../layout/footer.jsp"/>
  </main>

  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- Core JS (AdminLTE 4 requires Bootstrap 5) -->
<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/js/adminlte.min.js"></script>

<!-- DataTables (Bootstrap 5) + Buttons -->
<script src="https://cdn.datatables.net/v/bs5/dt-2.0.7/b-3.0.2/r-3.0.2/datatables.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/jszip@3.10.1/dist/jszip.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/pdfmake@0.2.7/build/pdfmake.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/pdfmake@0.2.7/build/vfs_fonts.js"></script>

<!-- (Optional) Keep local scripts you already use -->
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  // Keep existing lock-down behaviors (unchanged)
  document.addEventListener('contextmenu', function(e) { e.preventDefault(); });
  document.addEventListener('keydown', function(e) { if (e.ctrlKey && e.key === 'u') { e.preventDefault(); } });
  document.addEventListener('keydown', function(e) { if (e.key === 'F12') { e.preventDefault(); } });

  // Disable back/forward cache (unchanged)
  $(document).ready(function () {
    function disableBack() { window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = function (evt) { if (evt.persisted) disableBack(); }
  });

  // Disable refresh (unchanged)
  document.onkeydown = function (e) {
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) {
      e.preventDefault();
    }
  };

  // DataTables init (logic unchanged)
  $(function () {
    $("#example1").DataTable({
      responsive: true,
      lengthChange: false,
      autoWidth: false,
      buttons: ["excel","pdf","print"]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');

    $('#example2').DataTable({
      paging: true,
      lengthChange: false,
      searching: false,
      ordering: true,
      info: true,
      autoWidth: false,
      responsive: true
    });
  });
</script>
</body>
</html>
