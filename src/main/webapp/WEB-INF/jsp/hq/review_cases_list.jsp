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

  <!-- Font Awesome (unchanged) -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">

  <!-- AdminLTE 4 (Bootstrap 5 based) -->
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">

  <!-- DataTables Bootstrap 5 skins -->
  <link rel="stylesheet" href="/static/plugins/datatables-bs5/css/dataTables.bootstrap5.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap5.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap5.min.css">

  <!-- jQuery Confirm -->
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
</head>
<body class="hold-transition sidebar-mini layout-fixed">
<div class="wrapper">
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
            <ol class="breadcrumb float-sm-end">
              <li class="breadcrumb-item"><a href="/hq/dashboard">Home</a></li>
              <li class="breadcrumb-item"><a href="/hq/review_summary_list">Review Status</a></li>
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
                <h3 class="card-title mb-0">Review Cases List</h3>
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
                      <th style="text-align: center; vertical-align: middle;">Supporting File</th>
                    </tr>
                  </thead>
                  <tbody>
                  </tbody>
                </table>
              </div>
            </div> <!-- /.card -->
          </div>
        </div>
      </div>
      <input type="hidden" id="category" value="${category}"/>
    </section>
  </div>

  <jsp:include page="../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- Core: jQuery + Bootstrap 5 + AdminLTE 4 -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<!-- DataTables + Bootstrap 5 adapters -->
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

<!-- jQuery Confirm -->
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
// keep your security/UX handlers unchanged
document.addEventListener('contextmenu', e => e.preventDefault());
document.addEventListener('keydown', e => { if (e.ctrlKey && e.key === 'u') e.preventDefault(); });
document.addEventListener('keydown', e => { if (e.key === 'F12') e.preventDefault(); });

$(document).ready(function () {
  function disableBack(){ window.history.forward(); }
  window.onload = disableBack();
  window.onpageshow = function (evt) { if (evt.persisted) disableBack(); };
});
document.onkeydown = function (e) {
  if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) e.preventDefault();
};

// DataTables (server-side) – logic unchanged
$(function () {
  $("#example1").DataTable({
    processing: true,
    serverSide: true,
    ajax: {
      url: "/hq/review_cases_list_ajax",
      type: "GET",
      dataSrc: function(json) {
        if (json.redirect) {
          window.location.href = json.redirect;
          return [];
        }
        return json.data;
      },
      data: function (d) {
        d.category = $('#category').val();
      }
    },
    columns: [
      { data: "GSTIN" },
      { data: "taxpayerName" },
      { data: "locationName" },
      { data: "period" },
      { data: "extensionNo" },
      {
        data: "caseReportingDate",
        render: function(data) {
          if (!data) return '';
          var date = new Date(data);
          var day = ("0" + date.getDate()).slice(-2);
          var month = ("0" + (date.getMonth() + 1)).slice(-2);
          var year = date.getFullYear();
          return day + '-' + month + '-' + year;
        }
      },
      { data: "indicativeTaxValue" },
      { data: "assignedTo" },
      { data: "actionStatus" },
      { data: "caseId" },
      { data: "caseStage" },
      { data: "caseStageArn" },
      { data: "demand" },
      { data: "recoveryStage" },
      { data: "recoveryStageArn" },
      { data: "recoveryByDRC3" },
      { data: "recoveryAgainstDemand" },
      {
        data: "fileName",
        render: function (data) {
          return '<a href="/hq/downloadFile?fileName=' + data + '"><button type="button" class="btn btn-primary"><i class="fas fa-download"></i></button></a>';
        }
      }
    ],
    responsive: false,
    lengthChange: false,
    autoWidth: true,
    scrollX: true,
    dom: 'Bfrtip',
    buttons: [
      'excel', 'pdf', 'print'
    ]
  }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
});
</script>

<!-- (Unchanged) sample script using caseList; left as-is per your logic -->
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
