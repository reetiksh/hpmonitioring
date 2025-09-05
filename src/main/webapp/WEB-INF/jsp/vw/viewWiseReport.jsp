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
  <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">

  <!-- DataTables (Bootstrap 5 skins) -->
  <link rel="stylesheet" href="/static/plugins/datatables-bs5/css/dataTables.bootstrap5.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap5.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap5.min.css">

  <!-- AdminLTE 4 -->
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">

  <style>
    /* make font color black for all table elements */
    table, th, td { color: black; }

    .nav-item .nav-link.selected {
      background-color: #007bff;
      color: white;
    }
    .table-responsive {
      overflow-x: auto;
      -webkit-overflow-scrolling: touch;
    }
    /* Modal container */
    .modal { width: 100%; height: 100%; overflow: auto; }
    /* Modal content */
    .modal-content { width: 90%; max-height: 90vh; overflow: auto; }
  </style>
</head>
<body class="hold-transition sidebar-mini">
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
            <h1>Case Pendency Status Report</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="/vw/dashboard">Home</a></li>
              <li class="breadcrumb-item"><a href="/vw/mis">MIS</a></li>
              <li class="breadcrumb-item">Report</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">

        <form action="viewWiseReport" id="viewWiseReport">
          <div class="row">
            <div class="col-md-3">
              <div class="form-group">
                <label>Type<span style="color: red;"> *</span></label>
                <select id="view" name="view" class="form-select" required>
                  <option value="">Please Select</option>
                  <c:choose>
                    <c:when test="${view eq 'officerWise'}">
                      <option value="officerWise" selected>Officer-Wise</option>
                      <option value="zoneWise">Zone-wise</option>
                    </c:when>
                    <c:when test="${view eq 'zoneWise'}">
                      <option value="officerWise">Officer-Wise</option>
                      <option value="zoneWise" selected>Zone-wise</option>
                    </c:when>
                    <c:otherwise>
                      <option value="officerWise">Officer-Wise</option>
                      <option value="zoneWise" selected>Zone-wise</option>
                    </c:otherwise>
                  </c:choose>
                </select>
              </div>
            </div>

            <div class="col-md-3">
              <div class="form-group">
                <label>Financial Year</label>
                <select id="financialyear" name="financialyear" class="form-select">
                  <option value="">Please Select</option>
                  <c:forEach items="${financialyear}" var="financialyear">
                    <option value="${financialyear}" <c:if test="${financialyear == year}">selected</c:if>>${financialyear}</option>
                  </c:forEach>
                </select>
              </div>
            </div>

            <div class="col-md-3">
              <div class="form-group">
                <label>Category</label>
                <select id="category" name="category" class="form-select">
                  <option value="0">Please Select</option>
                  <c:forEach items="${categories}" var="categories">
                    <option value="${categories.id}" <c:if test="${categories.id == category}">selected</c:if>>${categories.name}</option>
                  </c:forEach>
                </select>
              </div>
            </div>

            <div class="col-md-3">
              <div style="margin-top:8%;text-align:center;">
                <button class="btn btn-primary" onclick="submitForm()">Search</button>
              </div>
            </div>
          </div>
        </form>

        <!-- Modal -->
        <div class="modal fade" id="modal1" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
          <div class="modal-dialog" style="max-width: 2000px;">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Detailed View</h5>
                <!-- BS5 close button -->
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
              </div>
              <div class="modal-body" id="exampleModal"></div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
              </div>
            </div>
          </div>
        </div>

        <div class="card-body">
          <c:if test="${not empty zoneWiseCounts}">
            <div class="">
              <table id="example1" class="table table-bordered table-striped table-container">
                <h2>Zone Wise FO Count</h2>
                <thead style="background-color: #C0C0C0">
                  <tr>
                    <th style="text-align: center; vertical-align: middle;" rowspan="2">Zone Name</th>
                    <th style="text-align: center; vertical-align: middle;" rowspan="2">No. of FOs</th>
                    <th style="text-align: center; vertical-align: middle;" rowspan="2">No. of Cases</th>
                    <c:if test="${isYearSelected}">
                      <th style="text-align: center; vertical-align: middle;" rowspan="2">Financial Year</th>
                    </c:if>
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
                  <c:forEach items="${zoneWiseCounts}" var="zoneWiseCounts">
                    <tr>
                      <td align="center">
                        <i class="fa fa-eye" aria-hidden="true" style="cursor: pointer; color: #4682b4"
                           onclick="drillDown('${zoneWiseCounts[15]}')">${zoneWiseCounts[0]}</i>
                      </td>
                      <td>${zoneWiseCounts[1]}</td>
                      <td>${zoneWiseCounts[2]}</td>
                      <c:if test="${isYearSelected}">
                        <td>${zoneWiseCounts[16]}</td>
                      </c:if>
                      <td>${zoneWiseCounts[3]}</td>
                      <td>${zoneWiseCounts[4]}</td>
                      <td>${zoneWiseCounts[5]}</td>
                      <td>${zoneWiseCounts[6]}</td>
                      <td>${zoneWiseCounts[7]}</td>
                      <td>${zoneWiseCounts[8]}</td>
                      <td>${zoneWiseCounts[9]}</td>
                      <td>${zoneWiseCounts[10]}</td>
                      <td>${zoneWiseCounts[11]}</td>
                      <td>${zoneWiseCounts[12]}</td>
                      <td>${zoneWiseCounts[13]}</td>
                      <td>${zoneWiseCounts[14]}</td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
          </c:if>

          <c:if test="${not empty foWiseCounts}">
            <div class="table-container">
              <table id="example1" class="table table-bordered table-striped table-responsive ">
                <h2>Field Officer List</h2>
                <thead style="background-color: #C0C0C0">
                  <tr>
                    <th style="text-align: center; vertical-align: middle;width: 7%;" rowspan="2"> Name</th>
                    <th style="text-align: center; vertical-align: middle;" rowspan="2">Zone Name</th>
                    <th style="text-align: center; vertical-align: middle;" rowspan="2">Circle Name</th>
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
                  <c:forEach items="${foWiseCounts}" var="districtWiseCounts">
                    <tr>
                      <td>${districtWiseCounts[1]} ${districtWiseCounts[2]}</td>
                      <td>${districtWiseCounts[6]}</td>
                      <td>${districtWiseCounts[7]}</td>
                      <td align="center">
                        <i class="fa fa-eye" aria-hidden="true" style="cursor: pointer; color: #4682b4"
                           onclick="drillDownPopup('${districtWiseCounts[23]}','${districtWiseCounts[22]}','${districtWiseCounts[24]}','${districtWiseCounts[25]}')">${districtWiseCounts[9]}</i>
                      </td>
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
          </c:if>
        </div>

      </div>
    </section>
  </div>

  <jsp:include page="../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<script src="/static/plugins/jquery/jquery.min.js"></script>

<!-- Bootstrap 5 bundle (required for data-bs-* APIs) -->
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- DataTables core + Bootstrap 5 adapters -->
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

<script src="/static/dist/js/adminlte.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

<script>
document.addEventListener('contextmenu', function(e) { e.preventDefault(); });
document.addEventListener('keydown', function(e) { if (e.ctrlKey && e.key === 'u') { e.preventDefault(); } });
document.addEventListener('keydown', function(e) { if (e.key === 'F12') { e.preventDefault(); } });

$(document).ready(function () {
  function disableBack() { window.history.forward(); }
  window.onload = disableBack();
  window.onpageshow = function (evt) { if (evt.persisted) disableBack(); }
});
document.onkeydown = function (e) {
  if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) { e.preventDefault(); }
};

$(function () {
  $("#example1").DataTable({
    "responsive": false, "lengthChange": false, "autoWidth": true, "scrollX": true,
    "buttons": ["excel","pdf","print"]
  }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');

  $("#example2").DataTable({
    "responsive": true, "lengthChange": false, "autoWidth": true,
    "buttons": ["excel","pdf","print"]
  }).buttons().container().appendTo('#example2_wrapper .col-md-6:eq(0)');
});
</script>

<script>
function submitForm() { document.forms["viewWiseReport"].submit(); }
</script>

<script type="text/javascript">
function drillDown(zoneName) {
  var category = $("#category").val();
  var year = $("#financialyear").val();
  if (year.trim() === "") { year = "0"; }
  var link = 'landingDrillDown?zoneName=' + zoneName + '&financialyear=' + year + '&category=' + category;

  $.ajax({
    url: '/checkLoginStatus',
    method: 'get',
    async: false,
    success: function(result){
      const myJSON = JSON.parse(result);
      if(result=='true'){
        $('#exampleModal').load(link, function(response, status, xhr){
          if(status == 'success'){
            $('#modal1').modal('show');
            console.log("showing");
          }else{
            console.log("failed");
          }
        });
      } else if(result=='false'){
        window.location.reload();
      }
    }
  });
}

function drillDownPopup(userId, year, circleId, categoryId) {
  var link = 'landingDrillDownUser?year=' + year + '&userId=' + userId + '&circleId=' + circleId + '&categoryId=' + categoryId;

  $.ajax({
    url: '/checkLoginStatus',
    method: 'get',
    async: false,
    success: function(result){
      const myJSON = JSON.parse(result);
      if(result=='true'){
        $('#exampleModal').load(link, function(response, status, xhr){
          if(status == 'success'){
            $('#modal1').modal('show');
            console.log("showing");
          }else{
            console.log("failed");
          }
        });
      } else if(result=='false'){
        window.location.reload();
      }
    }
  });
}
</script>
</body>
</html>
