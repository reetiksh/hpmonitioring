<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST |Review Case Summary</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">

  <!-- AdminLTE 4 + Bootstrap 5 + Font Awesome 6 -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.5.2/css/all.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/css/adminlte.min.css" rel="stylesheet"/>

  <!-- DataTables (Bootstrap 5 build) + Buttons/Responsive -->
  <link href="https://cdn.datatables.net/v/bs5/dt-1.13.8/r-2.5.0/b-2.4.2/b-html5-2.4.2/b-print-2.4.2/datatables.min.css" rel="stylesheet"/>

  <!-- jQuery Confirm -->
  <link href="https://cdn.jsdelivr.net/npm/jquery-confirm@3.3.4/css/jquery-confirm.min.css" rel="stylesheet"/>
</head>
<body class="hold-transition layout-fixed">
<div class="wrapper">
  <div class="preloader flex-column justify-content-center align-items-center">
    <img class="animation__shake" src="/static/dist/img/AdminLTELogo.png" alt="AdminLTELogo" height="60" width="60">
  </div>

  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <div class="content-wrapper">
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Review Case Summary</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="/hq/dashboard">Home</a></li>
              <li class="breadcrumb-item active">Review Case Summary</li>
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
                <h3 class="card-title">Review Case Summary List</h3>
              </div>
              <div class="card-body">
                <c:if test="${!empty categoryTotals}">
                  <table id="example1" class="table table-bordered table-striped">
                    <thead>
                    <tr>
                      <th style="text-align: center; vertical-align: middle;">Category</th>
                      <th style="text-align: center; vertical-align: middle;">Total Cases</th>
                      <th style="text-align: center; vertical-align: middle;">Total Indicative Value(₹)</th>
                      <th style="text-align: center; vertical-align: middle;">Total Amount(₹)</th>
                      <th style="text-align: center; vertical-align: middle;">Total Demand(₹)</th>
                      <th style="text-align: center; vertical-align: middle;">Total Recovery(₹)</th>
                    </tr>
                    </thead>
                    <tbody>
                      <c:forEach items="${categoryTotals}" var="total">
                        <tr>
                          <td style="text-align: center; vertical-align: middle;">
                            <a href="/hq/review_cases_list?category=${total.category}">
                              <c:out value="${total.category}" />
                            </a>
                          </td>
                          <td style="text-align: center; vertical-align: middle;"><c:out value="${total.totalRows}" /></td>
                          <td style="text-align: center; vertical-align: middle;"><fmt:formatNumber value="${total.totalIndicativeTax}" pattern="#,##,##0"/></td>
                          <td style="text-align: center; vertical-align: middle;"><fmt:formatNumber value="${total.totalAmount}" pattern="#,##,##0"/></td>
                          <td style="text-align: center; vertical-align: middle;"><fmt:formatNumber value="${total.totalDemand}" pattern="#,##,##0"/></td>
                          <td style="text-align: center; vertical-align: middle;"><fmt:formatNumber value="${total.totalRecovery}" pattern="#,##,##0"/></td>
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
              </div><!-- /.card-body -->
            </div><!-- /.card -->
          </div>
        </div>
      </div>
    </section>
  </div>

  <jsp:include page="../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- JS: jQuery -> Bootstrap 5 -> AdminLTE 4 -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/js/adminlte.min.js"></script>

<!-- DataTables (Bootstrap 5 build) + extensions -->
<script src="https://cdn.datatables.net/v/bs5/dt-1.13.8/r-2.5.0/b-2.4.2/b-html5-2.4.2/b-print-2.4.2/datatables.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/jszip@3.10.1/dist/jszip.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/pdfmake@0.2.7/build/pdfmake.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/pdfmake@0.2.7/build/vfs_fonts.js"></script>

<!-- jQuery Confirm -->
<script src="https://cdn.jsdelivr.net/npm/jquery-confirm@3.3.4/js/jquery-confirm.min.js"></script>

<script>
document.addEventListener('contextmenu', function(e) { e.preventDefault(); });
document.addEventListener('keydown', function(e) { if (e.ctrlKey && e.key === 'u') { e.preventDefault(); } });
document.addEventListener('keydown', function(e) { if (e.key === 'F12') { e.preventDefault(); } });

// Disable back/forward cache
$(document).ready(function () {
  function disableBack() { window.history.forward() }
  window.onload = disableBack();
  window.onpageshow = function (evt) { if (evt.persisted) disableBack() }

  // Disable refresh
  document.onkeydown = function (e) {
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) {
      e.preventDefault();
    }
  };

  $("#example1").DataTable({
    responsive: true,
    lengthChange: false,
    autoWidth: false,
    buttons: ["excel", "pdf", "print"]
  }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');

  // Keep your secondary initializer in case you add #example2 later
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
