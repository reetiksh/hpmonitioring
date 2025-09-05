<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST | Review Case List</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">

  <!-- AdminLTE + DataTables -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
</head>

<body class="hold-transition sidebar-mini">
<div class="wrapper">
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <div class="content-wrapper">
    <!-- Header -->
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6 d-flex align-items-center gap-2">
            <h1 class="m-0">
              <a href="review_summary_list?categoryId=${categoryId}" class="btn btn-primary">
                <i class="fas fa-arrow-left nav-icon"></i>
              </a>
              Review Case Summary
            </h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="/fo/dashboard">Home</a></li>
              <li class="breadcrumb-item"><a href="/fo/review_category_case">Review Status</a></li>
              <li class="breadcrumb-item active">Review Case Summary</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <!-- Body -->
    <section class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-12">

            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title">Review Cases List</h3>
              </div>

              <div class="card-body">
                <div class="table-responsive">
                  <table id="example1" class="table table-bordered table-striped w-100">
                    <thead>
                      <tr>
                        <th class="text-center align-middle">GSTIN</th>
                        <th class="text-center align-middle">Taxpayer Name</th>
                        <th class="text-center align-middle">Jurisdiction</th>
                        <th class="text-center align-middle">Period</th>
                        <th class="text-center align-middle">Dispatch No.</th>
                        <th class="text-center align-middle">Reporting Date (DD-MM-YYYY)</th>
                        <th class="text-center align-middle">Indicative Value (₹)</th>
                        <th class="text-center align-middle">Currently With</th>
                        <th class="text-center align-middle">Action Status</th>
                        <th class="text-center align-middle">Case ID</th>
                        <th class="text-center align-middle">Case Stage</th>
                        <th class="text-center align-middle">Case Stage ARN</th>
                        <th class="text-center align-middle">Amount (₹)</th>
                        <th class="text-center align-middle">Recovery Stage</th>
                        <th class="text-center align-middle">Recovery Stage ARN</th>
                        <th class="text-center align-middle">Recovery Via DRC03 (₹)</th>
                        <th class="text-center align-middle">Recovery Against Demand (₹)</th>
                        <th class="text-center align-middle">Parameters</th>
                        <th class="text-center align-middle">Supporting File</th>
                      </tr>
                    </thead>

                    <tbody>
                      <c:forEach items="${caseList}" var="object">
                        <tr>
                          <!-- object[0]..[18] as per backend order -->
                          <td class="text-center align-middle"><c:out value="${object[0]}"/></td>
                          <td class="text-center align-middle"><c:out value="${object[1]}"/></td>
                          <td class="text-center align-middle"><c:out value="${object[2]}"/></td>
                          <td class="text-center align-middle"><c:out value="${object[3]}"/></td>
                          <td class="text-center align-middle"><c:out value="${object[4]}"/></td>

                          <!-- Reporting Date: if it's a Date object, format; if a String already formatted, just output -->
                          <td class="text-center align-middle">
                            <c:choose>
                              <c:when test="${object[5] instanceof java.util.Date}">
                                <fmt:formatDate value="${object[5]}" pattern="dd-MM-yyyy"/>
                              </c:when>
                              <c:otherwise>
                                <c:out value="${object[5]}"/>
                              </c:otherwise>
                            </c:choose>
                          </td>

                          <!-- Indicative Value -->
                          <td class="text-center align-middle">
                            <fmt:formatNumber value="${object[6]}" pattern="#,##,##0"/>
                          </td>

                          <td class="text-center align-middle"><c:out value="${object[7]}"/></td>
                          <td class="text-center align-middle"><c:out value="${object[8]}"/></td>
                          <td class="text-center align-middle"><c:out value="${object[9]}"/></td>
                          <td class="text-center align-middle"><c:out value="${object[10]}"/></td>

                          <!-- Amount -->
                          <td class="text-center align-middle">
                            <fmt:formatNumber value="${object[11]}" pattern="#,##,##0"/>
                          </td>

                          <td class="text-center align-middle"><c:out value="${object[12]}"/></td>
                          <td class="text-center align-middle"><c:out value="${object[13]}"/></td>

                          <!-- Recovery Via DRC03 -->
                          <td class="text-center align-middle">
                            <fmt:formatNumber value="${object[14]}" pattern="#,##,##0"/>
                          </td>

                          <!-- Recovery Against Demand -->
                          <td class="text-center align-middle">
                            <fmt:formatNumber value="${object[15]}" pattern="#,##,##0"/>
                          </td>

                          <!-- Parameters -->
                          <td class="text-center align-middle"><c:out value="${object[16]}"/></td>

                          <!-- Maybe extra param? Keeping [17] as in your source -->
                          <td class="text-center align-middle"><c:out value="${object[17]}"/></td>

                          <!-- File -->
                          <td class="text-center align-middle">
                            <c:choose>
                              <c:when test="${not empty object[18]}">
                                <a href="/fo/downloadUploadedPdfFile?fileName=${object[18]}" class="btn btn-primary btn-sm" title="Download">
                                  <i class="fas fa-download"></i>
                                </a>
                              </c:when>
                              <c:otherwise>
                                <button type="button" class="btn btn-primary btn-sm" disabled title="No file">
                                  <i class="fas fa-download"></i>
                                </button>
                              </c:otherwise>
                            </c:choose>
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
      </div>

      <input type="hidden" id="category" value="${category}"/>
    </section>
  </div>

  <jsp:include page="../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- Scripts -->
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
<script src="/static/dist/js/adminlte.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  // Optional hardening (same behavior you had)
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

  // DataTable
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
</script>
</body>
</html>
