<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST | Review Case Summary</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">

  <!-- AdminLTE 4 / Bootstrap 5 -->
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css"/>

  <!-- Font Awesome (unchanged) -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css"/>

  <!-- DataTables Bootstrap 5 (CDN; switch to your local paths if you have them) -->
  <link rel="stylesheet" href="https://cdn.datatables.net/v/bs5/dt-1.13.8/r-2.5.0/b-2.4.2/b-html5-2.4.2/b-print-2.4.2/datatables.min.css"/>

  <!-- jQuery Confirm -->
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css"/>
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">
  <!-- Header / Sidebar (kept as-is) -->
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page header -->
        <section class="content-header px-0">
          <div class="row mb-2">
            <div class="col-sm-6">
              <h1>Review Case Summary</h1>
            </div>
            <div class="col-sm-6">
              <ol class="breadcrumb float-sm-right">
                <li class="breadcrumb-item"><a href="/ru/dashboard">Home</a></li>
                <li class="breadcrumb-item active">Review Case Summary</li>
              </ol>
            </div>
          </div>
        </section>

        <!-- Card -->
        <section class="content px-0">
          <div class="row">
            <div class="col-12">
              <div class="card card-primary">
                <div class="card-header">
                  <h3 class="card-title mb-0">Review Case Summary List</h3>
                </div>
                <div class="card-body">
                  <c:if test="${!empty categoryTotals}">
                    <table id="example1" class="table table-bordered table-striped" style="width:100%;">
                      <thead>
                        <tr>
                          <th style="text-align:center;vertical-align:middle;">Category</th>
                          <th style="text-align:center;vertical-align:middle;">Total Cases</th>
                          <th style="text-align:center;vertical-align:middle;">Total Indicative Value(₹)</th>
                          <th style="text-align:center;vertical-align:middle;">Total Amount(₹)</th>
                          <th style="text-align:center;vertical-align:middle;">Total Demand(₹)</th>
                          <th style="text-align:center;vertical-align:middle;">Total Recovery(₹)</th>
                        </tr>
                      </thead>
                      <tbody>
                        <c:forEach items="${categoryTotals}" var="total">
                          <tr>
                            <td style="text-align:center;vertical-align:middle;">
                              <a href="/ru/view_list_of_case?category=${total.category}">
                                <c:out value="${total.category}"/>
                              </a>
                            </td>
                            <td style="text-align:center;vertical-align:middle;"><c:out value="${total.totalRows}"/></td>
                            <td style="text-align:center;vertical-align:middle;"><fmt:formatNumber value="${total.totalIndicativeTax}" pattern="#,##,##0"/></td>
                            <td style="text-align:center;vertical-align:middle;"><fmt:formatNumber value="${total.totalAmount}" pattern="#,##,##0"/></td>
                            <td style="text-align:center;vertical-align:middle;"><fmt:formatNumber value="${total.totalDemand}" pattern="#,##,##0"/></td>
                            <td style="text-align:center;vertical-align:middle;"><fmt:formatNumber value="${total.totalRecovery}" pattern="#,##,##0"/></td>
                          </tr>
                        </c:forEach>
                      </tbody>
                    </table>
                  </c:if>

                  <c:if test="${empty categoryTotals}">
                    <div class="col-12 text-center py-5">
                      <i class="fa fa-info-circle" style="font-size:100px;color:#616161" aria-hidden="true"></i><br>
                      <span style="font-size:35px;color:#616161">No Review Case List Available</span>
                    </div>
                  </c:if>
                </div>
              </div>
            </div>
          </div>
        </section>

      </div>
    </div>
  </main>

  <jsp:include page="../layout/footer.jsp"/>
</div>

<!-- Scripts -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<!-- DataTables (BS5 build + Buttons, Responsive) -->
<script src="https://cdn.datatables.net/v/bs5/dt-1.13.8/r-2.5.0/b-2.4.2/b-html5-2.4.2/b-print-2.4.2/jszip-3.10.1/pdfmake-0.2.7/vfs_fonts-0.1.0/datatables.min.js"></script>

<!-- jQuery Confirm -->
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  // Same UX guards you had
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    if ((e.ctrlKey && e.key === 'u') || e.key === 'F12' || e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) {
      e.preventDefault();
    }
  });
  (function () {
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack;
    window.onpageshow = function(evt){ if (evt.persisted) disableBack(); };
  })();

  // DataTables init (unchanged logic; BS5 styling)
  $(function () {
    const dt = $("#example1").DataTable({
      responsive: true,
      lengthChange: false,
      autoWidth: false,
      buttons: ["excel", "pdf", "print"]
    });
    dt.buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
  });
</script>
</body>
</html>
