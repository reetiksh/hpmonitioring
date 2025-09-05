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
  <title>HP GST |Review Case List</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">

  <!-- AdminLTE 4 / Bootstrap 5 / Font Awesome 6 -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.5.2/css/all.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/css/adminlte.min.css" rel="stylesheet"/>

  <!-- DataTables (Bootstrap 5 build + Responsive + Buttons) -->
  <link href="https://cdn.datatables.net/v/bs5/dt-2.0.8/r-3.0.2/b-3.0.2/datatables.min.css" rel="stylesheet"/>

  <!-- jquery-confirm (kept for your existing usage) -->
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page header -->
        <div class="row mb-2 align-items-center">
          <div class="col-sm-6">
            <h1 class="m-0">
              <a href="review_summary_list?categoryId=${categoryId}" class="btn btn-primary me-2">
                <i class="fas fa-arrow-left nav-icon"></i>
              </a>
              Review Case Summary
            </h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-end">
              <li class="breadcrumb-item"><a href="/hq/dashboard">Home</a></li>
              <li class="breadcrumb-item"><a href="/hq/review_summary_list">Review Status</a></li>
              <li class="breadcrumb-item active">Review Case Summary</li>
            </ol>
          </div>
        </div>

        <!-- Card -->
        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title">Review Cases List</h3>
          </div>
          <div class="card-body">
            <table id="example1" class="table table-bordered table-striped w-100">
              <thead>
                <tr>
                  <th style="text-align: center; vertical-align: middle;">GSTIN</th>
                  <th style="text-align: center; vertical-align: middle;">Taxpayer Name</th>
                  <th style="text-align: center; vertical-align: middle;">Jurisdiction</th>
                  <th style="text-align: center; vertical-align: middle;">Period</th>
                  <th style="text-align: center; vertical-align: middle;">Dispatch No.</th>
                  <th style="text-align: center; vertical-align: middle;">Reporting Date<br>(DD-MM-YYYY)</th>
                  <th style="text-align: center; vertical-align: middle;">Indicative Value(₹)</th>
                  <th style="text-align: center; vertical-align: middle;">Currently With</th>
                  <th style="text-align: center; vertical-align: middle;">Action Status</th>
                  <th style="text-align: center; vertical-align: middle;">Case ID</th>
                  <th style="text-align: center; vertical-align: middle;">Case Stage</th>
                  <th style="text-align: center; vertical-align: middle;">Case Stage ARN</th>
                  <th style="text-align: center; vertical-align: middle;">Amount(₹)</th>
                  <th style="text-align: center; vertical-align: middle;">Recovery Stage</th>
                  <th style="text-align: center; vertical-align: middle;">Recovery Stage ARN</th>
                  <th style="text-align: center; vertical-align: middle;">Recovery Via DRC03(₹)</th>
                  <th style="text-align: center; vertical-align: middle;">Recovery Against Demand(₹)</th>
                  <th style="text-align: center; vertical-align: middle;">Parameters</th>
                  <th style="text-align: center; vertical-align: middle;">Supporting File</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach items="${caseList}" var="object">
                  <tr>
                    <td><c:out value="${object[0]}" /></td>
                    <td><c:out value="${object[1]}" /></td>
                    <td><c:out value="${object[2]}"/></td>
                    <td><c:out value="${object[3]}" /></td>
                    <td><c:out value="${object[4]}" /></td>
                    <td><c:out value="${object[5]}" /></td>
                    <td><c:out value="${object[6]}" /></td>
                    <td><c:out value="${object[7]}" /></td>
                    <td><c:out value="${object[8]}" /></td>
                    <td><c:out value="${object[9]}" /></td>
                    <td><c:out value="${object[10]}" /></td>
                    <td><c:out value="${object[11]}" /></td>
                    <td><c:out value="${object[12]}" /></td>
                    <td><c:out value="${object[13]}" /></td>
                    <td><c:out value="${object[14]}" /></td>
                    <td><c:out value="${object[15]}" /></td>
                    <td><c:out value="${object[16]}" /></td>
                    <td><c:out value="${object[17]}" /></td>
                    <td style="text-align: center;">
                      <c:if test="${not empty object[18]}">
                        <a href="/hq/downloadFile?fileName=${object[18]}">
                          <button type="button" class="btn btn-primary">
                            <i class="fas fa-download"></i>
                          </button>
                        </a>
                      </c:if>
                      <c:if test="${empty object[18]}">
                        <button type="button" class="btn btn-primary" disabled>
                          <i class="fas fa-download"></i>
                        </button>
                      </c:if>
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </div>

        <input type="hidden" id="category" value="${category}"/>

      </div>
    </div>
  </main>

  <jsp:include page="../layout/footer.jsp"/>
</div>

<!-- Scripts: jQuery -> Bootstrap 5 -> AdminLTE 4 -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/js/adminlte.min.js"></script>

<!-- DataTables bundle (DT + Responsive + Buttons + JSZip + pdfmake) -->
<script src="https://cdn.datatables.net/v/bs5/dt-2.0.8/r-3.0.2/b-3.0.2/jszip-3.10.1/pdfmake-0.2.7/vfs_fonts-0.1.0/datatables.min.js"></script>

<!-- jquery-confirm (kept) -->
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  // Keep your original UX/security handlers
  document.addEventListener('contextmenu', function(e) { e.preventDefault(); });
  document.addEventListener('keydown', function(e) {
    if (e.ctrlKey && e.key === 'u') e.preventDefault();
  });
  document.addEventListener('keydown', function(e) {
    if (e.key === 'F12') e.preventDefault();
  });
  $(document).ready(function () {
    function disableBack() {window.history.forward()}
    window.onload = disableBack();
    window.onpageshow = function (evt) {if (evt.persisted) disableBack()}
  });
  document.onkeydown = function (e) {
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) e.preventDefault();
  };
</script>

<script>
  // DataTable init (logic preserved)
  $(function () {
    $("#example1").DataTable({
      responsive: false,
      lengthChange: false,
      autoWidth: true,
      scrollX: true,
      buttons: [
        "excel",
        "pdf",
        "print"
      ]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
  });
</script>

<!-- Your extra script block (left unchanged) -->
<script>
  var tableBody = document.getElementById("tableBody");
  var data = '${caseList}';
  data.forEach(function(obj) {
    var row = document.createElement("tr");
    var idCell = document.createElement("td");
    idCell.textContent = obj.id.GSTIN;
    row.appendChild(idCell);

    var textCell = document.createElement("td");
    var maxLength = 20;
    textCell.textContent = obj.caseStage.substring(0, maxLength) + (obj.text.length > maxLength ? '...' : '');
    row.appendChild(textCell);
    tableBody.appendChild(row);
  });
</script>
</body>
</html>
