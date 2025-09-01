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
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
 
  
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
  <div class="preloader flex-column justify-content-center align-items-center">
    <img class="animation__shake" src="/static/dist/img/AdminLTELogo.png" alt="AdminLTELogo" height="60" width="60">
  </div>
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar1.jsp"/>
  <div class="content-wrapper">
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Dashboard</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item">View</a></li>
              <li class="breadcrumb-item "><a href="/vw/dashboard">Dashboard</li>
            </ol>
          </div>
        </div>
      </div>
    </section>
    <section class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-12">
              
              									<div class="row">
						<div class="col-lg-3 col-6">
							<div class="small-box bg-info">
								<div class="inner">
									<h3>${dashBoardTotalCases}<sup style="font-size: 20px"></sup>
									</h3>
									<p>Total Cases</p>
								</div>
								<div class="icon">
									<i class="ion ion-bag"></i>
								</div>
								<a href="#" class="small-box-footer"> &nbsp</a>
							</div>
						</div>
						<div class="col-lg-3 col-6">
							<div class="small-box bg-success">
								<div class="inner">
									<h3>${dashBoardTotalAcknowledgedCases}<sup
											style="font-size: 20px"></sup>
									</h3>
									<p>Cases Acknowledged</p>
								</div>
								<div class="icon">
									<i class="ion ion-stats-bars"></i>
								</div>
								<a href="#" class="small-box-footer"> &nbsp</a>
							</div>
						</div>
						<div class="col-lg-3 col-6">
							<div class="small-box bg-primary">
								<div class="inner">
									<h3>
										${dashBoardTotalInitiatedCases}<sup style="font-size: 20px"></sup>
									</h3>
									<p>Cases Initiated</p>
								</div>
								<div class="icon">
									<i class="ion ion-stats-bars"></i>
								</div>
								<a href="#" class="small-box-footer"> &nbsp</a>
							</div>
						</div>
						<div class="col-lg-3 col-6">
							<div class="small-box bg-warning">
								<div class="inner">
									<h3>${dashBoardTotalCasesClosedByFo}<sup style="font-size: 20px">
									</h3>
									<p>Cases Closed by FO</p>
								</div>
								<div class="icon">
									<i class="ion ion-person-add"></i>
								</div>
								<a href="#" class="small-box-footer">&nbsp</a>
							</div>
						</div>
						<div class="col-lg-3 col-6">
							<div class="small-box bg-danger">
								<div class="inner">
									<h3>${dashBoardTotalSuspectedIndicativeAmount}<supstyle="font-size: 20px">
									</h3>
									<p>Total Suspected Indicative Amount</p>
								</div>
								<div class="icon">
									<i class="ion ion-pie-graph"></i>
								</div>
								<a href="#" class="small-box-footer"> &nbsp</a>
							</div>
						</div>
						<div class="col-lg-3 col-6">
							<div class="small-box bg-dark">
								<div class="inner">
									<h3>${dashBoardTotalAmount}<sup style="font-size: 20px">
									</h3>
									<p>Total Amount</p>
								</div>
								<div class="icon">
									<i class="ion ion-pie-graph"></i>
								</div>
								<a href="#" class="small-box-footer"> &nbsp</a>
							</div>
						</div>
						<div class="col-lg-3 col-6">
							<div class="small-box bg-secondary">
								<div class="inner">
									<h3>${dashBoardTotalDemand}<sup style="font-size: 20px">
									</h3>
									<p>Total Demand</p>
								</div>
								<div class="icon">
									<i class="ion ion-pie-graph"></i>
								</div>
								<a href="#" class="small-box-footer"> &nbsp </a>
							</div>
						</div>
						<div class="col-lg-3 col-6">
							<div class="small-box bg-light">
								<div class="inner">
									<h3>${dashBoardTotalRecovery}<sup style="font-size: 20px">
									</h3>
									<p>Total Recovery</p>
								</div>
								<div class="icon">
									<i class="ion ion-pie-graph"></i>
								</div>
								<a href="#" class="small-box-footer"> &nbsp </a>
							</div>
						</div>
					</div>
					<div>
					<div class="row" >
			<div class="col-lg-4" >
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
						<div class="row">
						<div class="col-md-6">
							<div class="form-group">
								<label>Type<span style="color: red;"> *</span></label>
								<form action="dashboard" id="dashboard">
									<select id="view" name="view" class="custom-select" required
										onchange="submitForm()">
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
<!-- Modal -->
					<div class="modal fade" id="modal1" tabindex="-1"
						aria-labelledby="exampleModalLabel" aria-hidden="true">
						<div class="modal-dialog" style="max-width: 2000px;">
							<div class="modal-content">
								<div class="modal-header">
									<h5 class="modal-title" id="exampleModalLabel">Detailed View</h5>
									<button type="button" class="close" data-dismiss="modal"
										aria-label="Close">
										<span aria-hidden="true">&times;</span>
									</button>
								</div>
								<div class="modal-body" id="exampleModal"></div>
								<div class="modal-footer">
									<button type="button" class="btn btn-secondary"
										data-dismiss="modal">Close</button>
									<!-- <button type="button" class="btn btn-primary">Save
										changes</button> -->
								</div>
							</div>
						</div>
					</div>
              <div class="card-body">
              				<c:if test="${not empty districtWiseCounts}">
					<div class="row">
						<table id="example1"
							class="table table-bordered table-striped table-responsive">
							<h2>District Wise FO Count</h2>
							<thead style="background-color: #C0C0C0">
								<tr>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">District Name</th>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">No. of FOs</th>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">No. of Cases</th>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Suspected Indicative Tax Value</th>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Demand Created</th>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Amount Recovered</th>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Yet to be Acknowledge</th>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Yet to be Initiated</th>
									<th style="text-align: center; vertical-align: middle;"
										colspan="7">Case Stage</th>
								</tr>
								<tr>
									<th style="text-align: center; vertical-align: middle;">DRC-01A
										issued</th>
									<th style="text-align: center; vertical-align: middle;">ASMT-10
										issued</th>
									<th style="text-align: center; vertical-align: middle;">DRC01
										issued</th>
									<th style="text-align: center; vertical-align: middle;">Demand
										Created via DRC07</th>
									<th style="text-align: center; vertical-align: middle;">Case
										Dropped</th>
									<th style="text-align: center; vertical-align: middle;">Partial
										Voluntary Payment Remaining Demand</th>
									<th style="text-align: center; vertical-align: middle;">Demand
										Created However Discharged via DRC-03</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${districtWiseCounts}"
									var="districtWiseCounts">
									<tr>
										<td align="center"><i class="fa fa-eye"
											aria-hidden="true" style="cursor: pointer; color: #4682b4"
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
										<td><strong>${districtWiseCounts[0]}</strong>
										</td>
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
                <%-- <c:if test="${!empty categoryTotals}">                  <table id="example1" class="table table-bordered table-striped">
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
                          <td style="text-align: center; vertical-align: middle;"><a href="/hq/review_cases_list?category=${total.category}"><c:out value="${total.category}" /></a></td>
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
                  <div class="col-12" style="text-align: center;">
                    <i class="fa fa-info-circle" style="font-size:100px;color:rgb(97, 97, 97)" aria-hidden="true"></i><br>
                    <span style="font-size:35px;color:rgb(97, 97, 97)">No Review Case List Available</span>
                  </div>
                </c:if> --%>
                <c:if test="${not empty foWiseCounts}">
					<div class="row">
					  <div>
						<table id="example2"
							class="table table-bordered table-striped table-responsive">
							<h2>Field Officer List</h2>
							<thead style="background-color: #C0C0C0">
								<tr>
								<th style="text-align: center; vertical-align: middle;"
										rowspan="2">SL No</th>
										<th style="text-align: center; vertical-align: middle;"
										rowspan="2"> Name</th>
<!-- 										<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Last Name</th> -->
										<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Login Name</th>
										<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Email Id</th>
										<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Mobile No</th>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">District Name</th>
										<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Circle Name</th>
									<!-- <th style="text-align: center; vertical-align: middle;"
										rowspan="2">User Count</th> -->
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">No. of Cases</th>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Suspected Indicative Tax Value</th>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Demand Created</th>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Amount Recovered</th>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Yet to be Acknowledge</th>
									<th style="text-align: center; vertical-align: middle;"
										rowspan="2">Yet to be Initiated</th>
									<th style="text-align: center; vertical-align: middle;"
										colspan="7">Case Stage</th>
								</tr>
								<tr>
									<th style="text-align: center; vertical-align: middle;">DRC-01A
										issued</th>
									<th style="text-align: center; vertical-align: middle;">ASMT-10
										issued</th>
									<th style="text-align: center; vertical-align: middle;">DRC01
										issued</th>
									<th style="text-align: center; vertical-align: middle;">Demand
										Created via DRC07</th>
									<th style="text-align: center; vertical-align: middle;">Case
										Dropped</th>
										<!-- <th style="text-align: center; vertical-align: middle;">NA</th> -->
									<th style="text-align: center; vertical-align: middle;">Partial
										Voluntary Payment Remaining Demand</th>
									<th style="text-align: center; vertical-align: middle;">Demand
										Created However Discharged via DRC-03</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${foWiseCounts}"
									var="districtWiseCounts">
									<tr>
										<td>${districtWiseCounts[0]}
										</td>
										<td>${districtWiseCounts[1]} ${districtWiseCounts[2]}</td>
<%-- 										<td>${districtWiseCounts[2]}</td> --%>
										<td>${districtWiseCounts[3]}</td>
										<td>${districtWiseCounts[4]}</td>
										<td>${districtWiseCounts[5]}</td>
										<td>${districtWiseCounts[6]}</td>
										<td>${districtWiseCounts[7]}</td>
										<%-- <td>${districtWiseCounts[8]}</td> --%>
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
    </section>
  </div>
  <jsp:include page="../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark">
  </aside>
</div>
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
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

<script>
document.addEventListener('contextmenu', function(e) {
	e.preventDefault();
});
document.addEventListener('keydown', function(e) {
	if (e.ctrlKey && e.key === 'u') {
		e.preventDefault();
	}
});
document.addEventListener('keydown', function(e) {
    if (e.key === 'F12') {
        e.preventDefault();
    }
});
 // Disable back and forward cache
$(document).ready(function () {
    function disableBack() {window.history.forward()}

    window.onload = disableBack();
    window.onpageshow = function (evt) {if (evt.persisted) disableBack()}
});
// Disable refresh
document.onkeydown = function (e) {
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) {
        e.preventDefault();
        
    }
};
  $(function () {
    $("#example1").DataTable({
      "responsive": true, "lengthChange": false, "autoWidth": false,
      "buttons": 
        [
          "excel",
          "pdf",
          "print", 
        ]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
    
    $("#example2").DataTable({
        "responsive": true, "lengthChange": false, "autoWidth": true,
        "buttons": 
          [
            "excel",
            "pdf",
            "print", 
          ]
      }).buttons().container().appendTo('#example2_wrapper .col-md-6:eq(0)');
  });
</script>
<script>
		function submitForm() {
			document.forms["dashboard"].submit();
		}
	</script>
	<script type="text/javascript">

		function drillDown(distName) {
			var link = 'landingDrillDown?distName=' + distName ;
			
			 $.ajax({url: '/checkLoginStatus',
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
	<script type="text/javascript">
  document.addEventListener('contextmenu', function(e) {
		e.preventDefault();
	});
	document.addEventListener('keydown', function(e) {
		if (e.ctrlKey && e.key === 'u') {
			e.preventDefault();
		}
	});
    google.charts.load('current', { 'packages': ['corechart'] });
    google.charts.setOnLoadCallback(drawChart);
    function drawChart() {
      var data = google.visualization.arrayToDataTable(${ indicativeTaxValue });
      var options = {
        title: 'Category Wise Total Indicative Tax Value',
        pieHole: 0.4,
        legend: { position: 'bottom', maxLines: 3 },
        chartArea: {
            width: '100%',
            height: '75%'
        }
      };
      var chart = new google.visualization.PieChart(document.getElementById('pieChart1'));
      chart.draw(data, options);
    }
  </script>
	<script type="text/javascript">
    google.charts.load('current', { 'packages': ['corechart'] });
    google.charts.setOnLoadCallback(drawChart);
    function drawChart() {
      var data = google.visualization.arrayToDataTable(${ demandValue });
      var options = {
        title: 'Category Wise Total Demand',
        titleposition: { position: 'center' },
        pieHole: 0.4,
        legend: { position: 'bottom' },
        chartArea: {
            width: '90%',
            height: '75%'
        }
      };
      var chart = new google.visualization.PieChart(document.getElementById('pieChart2'));
      chart.draw(data, options);
    }
  </script>
	<script type="text/javascript">
    google.charts.load('current', { 'packages': ['corechart'] });
    google.charts.setOnLoadCallback(drawChart);
    function drawChart() {
      var data = google.visualization.arrayToDataTable(${ recoveryValue });
      var options = {
        title: 'Category Wise Total Recovery',
        pieHole: 0.4,
        legend: { position: 'bottom' },
        chartArea: {
            width: '90%',
            height: '75%'
        }
      };
      var chart = new google.visualization.PieChart(document.getElementById('pieChart3'));
      chart.draw(data, options);
    }
  </script>
</body>
</html>
