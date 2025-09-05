<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST | Review Case List</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">

  <!-- CSS -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">

  <style>
    .dt-buttons .btn { margin-right:.25rem; }
  </style>
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <div class="content-wrapper">
    <!-- Header -->
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2 align-items-center">
          <div class="col-sm-6">
            <h1 class="m-0">
              <a href="review_category_case?categoryId=${categoryId}" class="btn btn-primary me-2">
                <i class="fas fa-arrow-left"></i>
              </a>
              Review Case Summary
            </h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right mb-0">
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
        <div class="card card-primary">
          <div class="card-header"><h3 class="card-title mb-0">Review Cases List</h3></div>
          <div class="card-body">
            <table id="example1" class="table table-bordered table-striped w-100">
              <thead>
                <tr>
                  <th class="text-center align-middle">GSTIN</th>
                  <th class="text-center align-middle">Taxpayer Name</th>
                  <th class="text-center align-middle">Jurisdiction</th>
                  <th class="text-center align-middle">Period</th>
                  <th class="text-center align-middle">Dispatch No.</th>
                  <th class="text-center align-middle">Reporting Date<br>(DD-MM-YYYY)</th>
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
                    <td><c:out value="${object[0]}"/></td>
                    <td><c:out value="${object[1]}"/></td>
                    <td><c:out value="${object[2]}"/></td>
                    <td><c:out value="${object[3]}"/></td>
                    <td><c:out value="${object[4]}"/></td>
                    <td><c:out value="${object[5]}"/></td>
                    <td class="text-end"><fmt:formatNumber value="${object[6]}" pattern="#,##,##0"/></td>
                    <td><c:out value="${object[7]}"/></td>
                    <td><c:out value="${object[8]}"/></td>
                    <td><c:out value="${object[9]}"/></td>
                    <td><c:out value="${object[10]}"/></td>
                    <td><c:out value="${object[11]}"/></td>
                    <td class="text-end"><fmt:formatNumber value="${object[12]}" pattern="#,##,##0"/></td>
                    <td><c:out value="${object[13]}"/></td>
                    <td><c:out value="${object[14]}"/></td>
                    <td class="text-end"><fmt:formatNumber value="${object[15]}" pattern="#,##,##0"/></td>
                    <td class="text-end"><fmt:formatNumber value="${object[16]}" pattern="#,##,##0"/></td>
                    <td><c:out value="${object[17]}"/></td>
                    <td class="text-center">
                      <c:choose>
                        <c:when test="${not empty object[18]}">
                          <a class="btn btn-primary btn-sm" href="/fo/downloadUploadedPdfFile?fileName=${object[18]}">
                            <i class="fas fa-download"></i>
                          </a>
                        </c:when>
                        <c:otherwise>
                          <button type="button" class="btn btn-primary btn-sm" disabled>
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

      <input type="hidden" id="category" value="${category}"/>
    </section>
  </div>

  <jsp:include page="../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- JS -->
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
  // Hardening (matches your pattern)
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key;
    if ((e.ctrlKey && k === 'u') || k === 'F12') e.preventDefault();
  });
  // Disable refresh
  document.onkeydown = function (e) {
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) e.preventDefault();
  };
  // Prevent bfcache back
  $(function(){
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = function(evt){ if (evt.persisted) disableBack(); };
  });

  // DataTable
  $(function () {
    $("#example1").DataTable({
      responsive: false,
      lengthChange: false,
      autoWidth: true,
      scrollX: true,
      buttons: ["excel", "pdf", "print"]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
  });
</script>
</body>
</html>
