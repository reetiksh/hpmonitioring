<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Officer Details</title>

  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png"/>

  <!-- Keep existing assets; no logic changes -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css"/>
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css"/>
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css"/>
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css"/>
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css"/>

  <style>
    caption {
      caption-side: top;
      text-align: left;
      font-size: 1.5em;
      font-weight: bold;
      padding: 10px;
    }
  </style>
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- If you use shared layout, keep your includes -->
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <main class="app-main">
    <div class="app-content">
      <section class="content-header">
        <div class="container-fluid">
          <div class="row mb-2">
            <div class="col-sm-6">
              <h1>Officer Details</h1>
            </div>
            <div class="col-sm-6">
              <ol class="breadcrumb float-sm-right">
                <li class="breadcrumb-item"><a href="#">Home</a></li>
                <li class="breadcrumb-item active">Officer Details</li>
              </ol>
            </div>
          </div>
        </div>
      </section>

      <section class="content">
        <div class="container-fluid">
          <div class="card card-primary">
            <div class="card-header">
              <h3 class="card-title">Officer Details of Zone&nbsp;
                <c:out value="${circleWiseCounts[0][1]}"/></h3>
            </div>

            <div class="card-body">
              <div class="row">
                <div class="col-12">
                  <div class="table-responsive">
                    <table id="example" class="table table-bordered table-striped table-responsive display" style="width:100%">
                      <caption>
                        Officer Details of Zone ${circleWiseCounts[0][1]}
                      </caption>
                      <thead style="background-color: #C0C0C0">
                      <tr>
                        <th style="text-align: center; vertical-align: middle;" rowspan="2">Zone Name</th>
                        <th style="text-align: center; vertical-align: middle;" rowspan="2">Circle Name</th>
                        <th style="text-align: center; vertical-align: middle;" rowspan="2">Name</th>
                        <th style="text-align: center; vertical-align: middle;" rowspan="2">No. of Cases</th>
                        <th style="text-align: center; vertical-align: middle;" rowspan="2">Financial Year</th>
                        <th style="text-align: center; vertical-align: middle;" rowspan="2">Suspected Indicative Tax Value</th>
                        <th style="text-align: center; vertical-align: middle;" rowspan="2">Demand Created</th>
                        <th style="text-align: center; vertical-align: middle;" rowspan="2">Amount Recovered</th>
                        <th style="text-align: center; vertical-align: middle;" rowspan="2">Yet to be Acknowledge</th>
                        <th style="text-align: center; vertical-align: middle;" rowspan="2">Yet to be Initiated</th>
                        <th style="text-align: center; vertical-align: middle;" colspan="7">Case Stage</th>
                      </tr>
                      <tr>
                        <th style="text-align: center; vertical-align: middle;">DRC-01A issued</th>
                        <th style="text-align: center; vertical-align: middle;">ASMT-10 issued</th>
                        <th style="text-align: center; vertical-align: middle;">DRC01 issued</th>
                        <th style="text-align: center; vertical-align: middle;">Demand Created via DRC07</th>
                        <th style="text-align: center; vertical-align: middle;">Case Dropped</th>
                        <th style="text-align: center; vertical-align: middle;">Partial Voluntary Payment Remaining Demand</th>
                        <th style="text-align: center; vertical-align: middle;">Demand Created However Discharged via DRC-03</th>
                      </tr>
                      </thead>
                      <tbody>
                      <c:forEach items="${circleWiseCounts}" var="districtWiseCounts">
                        <tr>
                          <td>${districtWiseCounts[1]}</td>
                          <td>${districtWiseCounts[2]}</td>
                          <td>${districtWiseCounts[3]} ${districtWiseCounts[4]}</td>
                          <td>${districtWiseCounts[9]}</td>
                          <td>${districtWiseCounts[22]}</td>
                          <td>${districtWiseCounts[10]}</td>
                          <td>${districtWiseCounts[11]}</td>
                          <td>${districtWiseCounts[12]}</td>
                          <td>${districtWiseCounts[13]}</td>
                          <td>${districtWiseCounts[14]}</td>
                          <td>${districtWiseCounts[15]}</td>
                          <td>${districtWiseCounts[16]}</td>
                          <td>${districtWiseCounts[17]}</td>
                          <td>${districtWiseCounts[18]}</td>
                          <td>${districtWiseCounts[19]}</td>
                          <td>${districtWiseCounts[20]}</td>
                          <td>${districtWiseCounts[21]}</td>
                        </tr>
                      </c:forEach>
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div><!-- /.card-body -->
          </div><!-- /.card -->
        </div>
      </section>
    </div>
  </main>

  <jsp:include page="../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- Scripts (kept as-is) -->
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

<script src="/static/dist/js/adminlte.min.js"></script>

<script>
  $(document).ready(function () {
    $('#example').DataTable({
      dom: 'Bfrtip',
      buttons: ['excel', 'pdf', 'print'],
      initComplete: function (settings, json) {
        console.log('DataTable initialized');
      }
    });
  });
</script>
</body>
</html>
