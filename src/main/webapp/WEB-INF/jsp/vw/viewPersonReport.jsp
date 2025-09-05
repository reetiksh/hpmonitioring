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

  <!-- AdminLTE 4 (Bootstrap 5) -->
  <link href="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/css/adminlte.min.css" rel="stylesheet">
  <!-- Font Awesome 6 -->
  <link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6/css/all.min.css" rel="stylesheet">
  <!-- DataTables (Bootstrap 5 bundle) -->
  <link href="https://cdn.datatables.net/v/bs5/dt-2.0.7/b-3.0.2/r-3.0.2/datatables.min.css" rel="stylesheet"/>
  <!-- (Optional) jQuery Confirm (kept) -->
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">

  <style>
    .table thead th, .table tbody td { text-align:center; vertical-align:middle; }
  </style>
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- Preloader (kept) -->
  <div class="preloader flex-column justify-content-center align-items-center">
    <img class="animation__shake" src="/static/dist/img/AdminLTELogo.png" alt="AdminLTELogo" height="60" width="60">
  </div>

  <!-- Header & Sidebar includes (unchanged except layout class updates handled by AdminLTE 4) -->
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar1.jsp"/>

  <main class="app-main">
    <!-- Content Header -->
    <div class="app-content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1 class="m-0">Dashboard</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-end">
              <li class="breadcrumb-item">View</li>
              <li class="breadcrumb-item"><a href="/vw/dashboard">Dashboard</a></li>
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

            <!-- Small boxes -->
            <div class="row">
              <div class="col-lg-3 col-6">
                <div class="small-box bg-info">
                  <div class="inner">
                    <h3>${dashBoardTotalCases}<sup style="font-size: 20px"></sup></h3>
                    <p>Total Cases</p>
                  </div>
                  <div class="icon">
                    <i class="fa-solid fa-bag-shopping"></i>
                  </div>
                  <a href="#" class="small-box-footer">&nbsp;</a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-success">
                  <div class="inner">
                    <h3>${dashBoardTotalAcknowledgedCases}<sup style="font-size: 20px"></sup></h3>
                    <p>Cases Acknowledged</p>
                  </div>
                  <div class="icon">
                    <i class="fa-solid fa-chart-line"></i>
                  </div>
                  <a href="#" class="small-box-footer">&nbsp;</a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-primary">
                  <div class="inner">
                    <h3>${dashBoardTotalInitiatedCases}<sup style="font-size: 20px"></sup></h3>
                    <p>Cases Initiated</p>
                  </div>
                  <div class="icon">
                    <i class="fa-solid fa-chart-line"></i>
                  </div>
                  <a href="#" class="small-box-footer">&nbsp;</a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-warning">
                  <div class="inner">
                    <h3>${dashBoardTotalCasesClosedByFo}<sup style="font-size: 20px"></sup></h3>
                    <p>Cases Closed by FO</p>
                  </div>
                  <div class="icon">
                    <i class="fa-solid fa-user-plus"></i>
                  </div>
                  <a href="#" class="small-box-footer">&nbsp;</a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-danger">
                  <div class="inner">
                    <h3>${dashBoardTotalSuspectedIndicativeAmount}<supstyle="font-size: 20px"></h3>
                    <p>Total Suspected Indicative Amount</p>
                  </div>
                  <div class="icon">
                    <i class="fa-solid fa-chart-pie"></i>
                  </div>
                  <a href="#" class="small-box-footer">&nbsp;</a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-dark">
                  <div class="inner">
                    <h3>${dashBoardTotalAmount}<sup style="font-size: 20px"></h3>
                    <p>Total Amount</p>
                  </div>
                  <div class="icon">
                    <i class="fa-solid fa-chart-pie"></i>
                  </div>
                  <a href="#" class="small-box-footer">&nbsp;</a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-secondary">
                  <div class="inner">
                    <h3>${dashBoardTotalDemand}<sup style="font-size: 20px"></h3>
                    <p>Total Demand</p>
                  </div>
                  <div class="icon">
                    <i class="fa-solid fa-chart-pie"></i>
                  </div>
                  <a href="#" class="small-box-footer">&nbsp;</a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-light">
                  <div class="inner">
                    <h3>${dashBoardTotalRecovery}<sup style="font-size: 20px"></h3>
                    <p>Total Recovery</p>
                  </div>
                  <div class="icon">
                    <i class="fa-solid fa-chart-pie"></i>
                  </div>
                  <a href="#" class="small-box-footer">&nbsp;</a>
                </div>
              </div>
            </div>

            <!-- Charts row -->
            <div>
              <div class="row">
                <div class="col-lg-4">
                  <div id="pieChart1" style="height: 400px;"></div>
                </div>
                <div class="col-lg-4">
                  <div id="pieChart2" style="height: 400px;"></div>
                </div>
                <div class="col-lg-4">
                  <div id="pieChart3" style="height: 400px;"></div>
                </div>
              </div>
            </div>

            <!-- Type selector -->
            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label>Type<span style="color: red;"> *</span></label>
                  <form action="dashboard" id="dashboard">
                    <select id="view" name="view" class="form-select" required onchange="submitForm()">
                      <option value="">Please Select</option>
                      <c:choose>
                        <c:when test="${view eq 'officerWise'}">
                          <option value="officerWise" selected>Officer-Wise</option>
                          <option value="districtWise">District-wise</option>
                        </c:when>
                        <c:when test="${view eq 'districtWise'}">
                          <option value="officerWise">Officer-Wise</option>
                          <option value="districtWise" selected>District-wise</option>
                        </c:when>
                        <c:otherwise>
                          <option value="officerWise">Officer-Wise</option>
                          <option value="districtWise" selected>District-wise</option>
                        </c:otherwise>
                      </c:choose>
                    </select>
                  </form>
                </div>
              </div>
            </div>

            <!-- Modal (Bootstrap 5 markup) -->
            <div class="modal fade" id="modal1" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
              <div class="modal-dialog" style="max-width: 2000px;">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Detailed View</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body" id="exampleModal"></div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                  </div>
                </div>
              </div>
            </div>

            <div class="card card-primary card-outline">
              <div class="card-body">

                <!-- District wise -->
                <c:if test="${not empty districtWiseCounts}">
                  <div class="row">
                    <table id="example1" class="table table-bordered table-striped table-responsive w-100">
                      <h2>District Wise FO Count</h2>
                      <thead style="background-color: #C0C0C0">
                        <tr>
                          <th rowspan="2">District Name</th>
                          <th rowspan="2">No. of FOs</th>
                          <th rowspan="2">No. of Cases</th>
                          <th rowspan="2">Suspected Indicative Tax Value</th>
                          <th rowspan="2">Demand Created</th>
                          <th rowspan="2">Amount Recovered</th>
                          <th rowspan="2">Yet to be Acknowledge</th>
                          <th rowspan="2">Yet to be Initiated</th>
                          <th colspan="7">Case Stage</th>
                        </tr>
                        <tr>
                          <th>DRC-01A issued</th>
                          <th>ASMT-10 issued</th>
                          <th>DRC01 issued</th>
                          <th>Demand Created via DRC07</th>
                          <th>Case Dropped</th>
                          <th>Partial Voluntary Payment Remaining Demand</th>
                          <th>Demand Created However Discharged via DRC-03</th>
                        </tr>
                      </thead>
                      <tbody>
                        <c:forEach items="${districtWiseCounts}" var="districtWiseCounts">
                          <tr>
                            <td align="center">
                              <i class="fa fa-eye" aria-hidden="true" style="cursor: pointer; color: #4682b4"
                                 onclick="drillDown('${districtWiseCounts[0]}')">${districtWiseCounts[0]}</i>
                            </td>
                            <td>${districtWiseCounts[1]}</td>
                            <td>${districtWiseCounts[2]}</td>
                            <td>${districtWiseCounts[3]}</td>
                            <td>${districtWiseCounts[4]}</td>
                            <td>${districtWiseCounts[5]}</td>
                            <td>${districtWiseCounts[6]}</td>
                            <td>${districtWiseCounts[7]}</td>
                            <td>${districtWiseCounts[8]}</td>
                            <td>${districtWiseCounts[9]}</td>
                            <td>${districtWiseCounts[10]}</td>
                            <td>${districtWiseCounts[11]}</td>
                            <td>${districtWiseCounts[12]}</td>
                            <td>${districtWiseCounts[13]}</td>
                            <td>${districtWiseCounts[14]}</td>
                          </tr>
                        </c:forEach>
                        <c:forEach items="${totalCounts}" var="districtWiseCounts">
                          <tr>
                            <td><strong>Total Count</strong></td>
                            <td><strong>${districtWiseCounts[0]}</strong></td>
                            <td><strong>${districtWiseCounts[1]}</strong></td>
                            <td><strong>${districtWiseCounts[2]}</strong></td>
                            <td><strong>${districtWiseCounts[3]}</strong></td>
                            <td><strong>${districtWiseCounts[4]}</strong></td>
                            <td><strong>${districtWiseCounts[5]}</strong></td>
                            <td><strong>${districtWiseCounts[6]}</strong></td>
                            <td><strong>${districtWiseCounts[7]}</strong></td>
                            <td><strong>${districtWiseCounts[8]}</strong></td>
                            <td><strong>${districtWiseCounts[9]}</strong></td>
                            <td><strong>${districtWiseCounts[10]}</strong></td>
                            <td><strong>${districtWiseCounts[11]}</strong></td>
                            <td><strong>${districtWiseCounts[12]}</strong></td>
                            <td><strong>${districtWiseCounts[13]}</strong></td>
                          </tr>
                        </c:forEach>
                      </tbody>
                    </table>
                  </div>
                </c:if>

                <!-- FO list -->
                <c:if test="${not empty foWiseCounts}">
                  <div class="row">
                    <div class="col-12">
                      <table id="example2" class="table table-bordered table-striped table-responsive w-100">
                        <h2>Field Officer List</h2>
                        <thead style="background-color: #C0C0C0">
                          <tr>
                            <th rowspan="2">SL No</th>
                            <th rowspan="2">Name</th>
                            <th rowspan="2">Login Name</th>
                            <th rowspan="2">Email Id</th>
                            <th rowspan="2">Mobile No</th>
                            <th rowspan="2">District Name</th>
                            <th rowspan="2">Circle Name</th>
                            <th rowspan="2">No. of Cases</th>
                            <th rowspan="2">Suspected Indicative Tax Value</th>
                            <th rowspan="2">Demand Created</th>
                            <th rowspan="2">Amount Recovered</th>
                            <th rowspan="2">Yet to be Acknowledge</th>
                            <th rowspan="2">Yet to be Initiated</th>
                            <th colspan="7">Case Stage</th>
                          </tr>
                          <tr>
                            <th>DRC-01A issued</th>
                            <th>ASMT-10 issued</th>
                            <th>DRC01 issued</th>
                            <th>Demand Created via DRC07</th>
                            <th>Case Dropped</th>
                            <th>Partial Voluntary Payment Remaining Demand</th>
                            <th>Demand Created However Discharged via DRC-03</th>
                          </tr>
                        </thead>
                        <tbody>
                          <c:forEach items="${foWiseCounts}" var="districtWiseCounts">
                            <tr>
                              <td>${districtWiseCounts[0]}</td>
                              <td>${districtWiseCounts[1]} ${districtWiseCounts[2]}</td>
                              <td>${districtWiseCounts[3]}</td>
                              <td>${districtWiseCounts[4]}</td>
                              <td>${districtWiseCounts[5]}</td>
                              <td>${districtWiseCounts[6]}</td>
                              <td>${districtWiseCounts[7]}</td>
                              <td>${districtWiseCounts[9]}</td>
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

<!-- Core JS -->
<script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/js/adminlte.min.js"></script>

<!-- DataTables + Buttons (Bootstrap 5) -->
<script src="https://cdn.datatables.net/v/bs5/dt-2.0.7/b-3.0.2/r-3.0.2/datatables.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/jszip@3.10.1/dist/jszip.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/pdfmake@0.2.7/build/pdfmake.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/pdfmake@0.2.7/build/vfs_fonts.js"></script>

<!-- (Optional) jQuery Confirm (kept) -->
<script src="/static/dist/js/jquery-confirm.min.js"></script>
<!-- Google Charts loader (kept) -->
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

<script>
  // Prevent default context/menu/keys (kept)
  document.addEventListener('contextmenu', function(e) { e.preventDefault(); });
  document.addEventListener('keydown', function(e) { if (e.ctrlKey && e.key === 'u') { e.preventDefault(); } });
  document.addEventListener('keydown', function(e) { if (e.key === 'F12') { e.preventDefault(); } });

  // Disable back/forward cache (kept)
  $(document).ready(function () {
    function disableBack() { window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = function (evt) { if (evt.persisted) disableBack(); }
  });

  // Disable refresh (kept)
  document.onkeydown = function (e) {
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) {
      e.preventDefault();
    }
  };

  // jQuery shim for Bootstrap 5 modal to preserve existing .modal('show'|'hide') calls
  (function ($) {
    if (!$.fn.modal) {
      $.fn.modal = function(action) {
        const el = this[0];
        if (!el) return this;
        let instance = bootstrap.Modal.getInstance(el) || new bootstrap.Modal(el);
        if (action === 'show') instance.show();
        else if (action === 'hide') instance.hide();
        return this;
      };
    }
  })(jQuery);

  // DataTables inits (logic unchanged)
  $(function () {
    $("#example1").DataTable({
      responsive: true,
      lengthChange: false,
      autoWidth: false,
      buttons: ["excel","pdf","print"]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');

    $("#example2").DataTable({
      responsive: true,
      lengthChange: false,
      autoWidth: true,
      buttons: ["excel","pdf","print"]
    }).buttons().container().appendTo('#example2_wrapper .col-md-6:eq(0)');
  });

  function submitForm() {
    document.forms["dashboard"].submit();
  }

  function drillDown(distName) {
    var link = 'landingDrillDown?distName=' + distName ;
    $.ajax({
      url: '/checkLoginStatus',
      method: 'get',
      async: false,
      success: function(result){
        const myJSON = JSON.parse(result);
        if(result=='true'){
          $('#exampleModal').load(link, function(response, status, xhr){
            if(status == 'success'){
              $('#modal1').modal('show'); // shim keeps logic intact
            }
          });
        } else if(result=='false'){
          window.location.reload();
        }
      }
    });
  }
</script>

<!-- Google Charts (logic unchanged) -->
<script type="text/javascript">
  document.addEventListener('contextmenu', function(e) { e.preventDefault(); });
  document.addEventListener('keydown', function(e) { if (e.ctrlKey && e.key === 'u') { e.preventDefault(); } });

  google.charts.load('current', { 'packages': ['corechart'] });
  google.charts.setOnLoadCallback(drawChart1);
  function drawChart1() {
    var data = google.visualization.arrayToDataTable(${ indicativeTaxValue });
    var options = {
      title: 'Category Wise Total Indicative Tax Value',
      pieHole: 0.4,
      legend: { position: 'bottom', maxLines: 3 },
      chartArea: { width: '100%', height: '75%' }
    };
    var chart = new google.visualization.PieChart(document.getElementById('pieChart1'));
    chart.draw(data, options);
  }
</script>
<script type="text/javascript">
  google.charts.load('current', { 'packages': ['corechart'] });
  google.charts.setOnLoadCallback(drawChart2);
  function drawChart2() {
    var data = google.visualization.arrayToDataTable(${ demandValue });
    var options = {
      title: 'Category Wise Total Demand',
      titleposition: { position: 'center' },
      pieHole: 0.4,
      legend: { position: 'bottom' },
      chartArea: { width: '90%', height: '75%' }
    };
    var chart = new google.visualization.PieChart(document.getElementById('pieChart2'));
    chart.draw(data, options);
  }
</script>
<script type="text/javascript">
  google.charts.load('current', { 'packages': ['corechart'] });
  google.charts.setOnLoadCallback(drawChart3);
  function drawChart3() {
    var data = google.visualization.arrayToDataTable(${ recoveryValue });
    var options = {
      title: 'Category Wise Total Recovery',
      pieHole: 0.4,
      legend: { position: 'bottom' },
      chartArea: { width: '90%', height: '75%' }
    };
    var chart = new google.visualization.PieChart(document.getElementById('pieChart3'));
    chart.draw(data, options);
  }
</script>
</body>
</html>
