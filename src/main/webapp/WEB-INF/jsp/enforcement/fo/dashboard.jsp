<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>HP GST | Dashboard</title>
    <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">
    <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
    <link rel="stylesheet" href="/static/dist/ionicons-2.0.1/css/ionicons.min.css">
    <link rel="stylesheet" href="/static/plugins/tempusdominus-bootstrap-4/css/tempusdominus-bootstrap-4.min.css">
    <link rel="stylesheet" href="/static/plugins/icheck-bootstrap/icheck-bootstrap.min.css">
    <link rel="stylesheet" href="/static/plugins/jqvmap/jqvmap.min.css">
    <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
    <link rel="stylesheet" href="/static/plugins/overlayScrollbars/css/OverlayScrollbars.min.css">
    <link rel="stylesheet" href="/static/plugins/daterangepicker/daterangepicker.css">
    <link rel="stylesheet" href="/static/plugins/summernote/summernote-bs4.min.css">
    <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
  </head>

  <body class="hold-transition sidebar-mini layout-fixed">
    <div class="wrapper">
      <div class="preloader flex-column justify-content-center align-items-center">
        <img class="animation__shake" src="/static/dist/img/AdminLTELogo.png" alt="AdminLTELogo" height="60" width="60">
      </div>
      <jsp:include page="../../layout/header.jsp"/>
      <jsp:include page="../../layout/sidebar.jsp"/>
      <div class="content-wrapper">
        <div class="content-header">
          <div class="container-fluid">
            <div class="row mb-2">
              <div class="col-sm-6">
                <h1 class="m-0">Dashboard</h1>
              </div>
              <div class="col-sm-6">
                <ol class="breadcrumb float-sm-right">
                  <li class="breadcrumb-item"><a href="#">Home</a></li>
                  <li class="breadcrumb-item active">Dashboard</li>
                </ol>
              </div>
            </div>
          </div>
        </div>
        <section class="content">
          <div class="container-fluid">
            <!-- Small boxes (Stat box) -->
            <div class="row">
              <div class="col-lg-3 col-6">
                <div class="small-box bg-primary">
                  <div class="inner">
                    <h3>${totalEnforcementCases}</h3>
                    <p>Count of Total Cases</p>
                  </div>
                  <div class="icon">
                    <i class="ion ion-clipboard"></i>
                  </div>
                  <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-warning">
                  <div class="inner">
                    <h3>${countOfActiontakenCases}</h3>
                    <p>Total Action Taken On</p>
                  </div>
                  <div class="icon">
                    <i class="ion ion-edit"></i>
                  </div>
                  <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-info">
                  <div class="inner">
                    <h3><i class="fas fa-rupee-sign"></i> ${totalSumOfIndicativeTaxValue}/-</h3>
                    <p>Total Indicative tax value</p>
                  </div>
                  <div class="icon">
                    <i class="ion ion-cash"></i>
                  </div>
                  <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-secondary">
                  <div class="inner">
                    <h3>${totalEnforcementCasesInLast3Months}</h3>
                    <p>Total Uploaded Cases in Last 3 Months</p>
                  </div>
                  <div class="icon">
                    <i class="ion ion-stats-bars"></i>
                  </div>
                  <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-success">
                  <div class="inner">
                    <h3>${countOfCasesActionByUser}</h3>
                    <p>Total Cases Action Taken by You</p>
                  </div>
                  <div class="icon">
                    <i class="ion ion-ribbon-b"></i>
                  </div>
                  <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
              <div class="col-lg-3 col-6">
                <div class="small-box bg-danger">
                  <div class="inner">
                    <h3>${countOfPendingCases}</h3>
                    <p>Pending Cases with You</p>
                  </div>
                  <div class="icon">
                    <i class="ion ion-bookmark"></i>
                  </div>
                  <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
                </div>
              </div>
            </div>

<form method="GET" action="/enforcement_fo/dashboard" name="dashboard" id="dashboard" modelAttribute="dashboard">

	<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label>Parameter<span style="color: red;"> *</span></label> <select id="category"
										name="category" class="custom-select" required>
										<option value="-1">Please Select</option>
										<c:forEach items="${categories}" var="cat">
    									<option value="${cat[0]}" <c:if test="${cat[0] == category}">selected</c:if>>${cat[1]}</option>
										</c:forEach>
									</select>
								</div>
							</div>

							<div class="col-md-6">
								<div class="form-group">
									<label>Financial Year</label> <select id="financialyear" name="financialyear"
										class="custom-select">
										<option value="">Please Select</option>
										<c:forEach items="${financialyearlist}" var="financialyear">
											<option value="${financialyear}" <c:if test="${financialyear == year}">selected</c:if>
												>${financialyear}</option>
										</c:forEach>
									</select>
								</div>
							</div>

						</div>
							<div class="row">
							<div class="col-md-12">
								<div class="text-center">
									<button type="submit" class="btn btn-primary">Submit</button>
								</div>
							</div>
						</div>
</form>
            <!-- Dashboard Summary Table -->
            <div class="card mt-4">
              <div class="card-header">
                <h3 class="card-title">Dashboard Summary</h3>
              </div>
              <div class="card-body">
                <div class="table-responsive">
                  <table class="table table-bordered table-hover">
                    <thead>
                      <tr>
                        <th>Zone</th>
                        <th>Circle</th>
                        <th>Indicative Tax</th>
                        <th>Allotted</th>
                        <th>Completed</th>
                        <th>Not Acknowledged</th>
                        <th>Transferred</th>
                        <th>Acknowledged</th>
                        <th>Panchnama</th>
                        <th>Preliminary Report</th>
                        <th>Final Report</th>
                        <th>Adjudication</th>
                        <th>Show Cause</th>
                      </tr>
                    </thead>
                    <tbody>
                      <c:forEach var="row" items="${dashboardSummary}">
                        <tr>
                          <td>${row.zoneName}</td>
                          <td>${row.circleName}</td>
                          <td>${row.indicativeTaxValue}</td>
                          <td>${row.allottedCases}</td>
                          <td>${row.casesCompleted}</td>
                          <td>${row.notAcknowledged}</td>
                          <td>${row.transferToScrutiny}</td>
                          <td>${row.acknowledged}</td>
                          <td>${row.panchnama}</td>
                          <td>${row.preliminaryReport}</td>
                          <td>${row.finalReport}</td>
                          <td>${row.referToAdjudication}</td>
                          <td>${row.showCauseIssued}</td>
                        </tr>
                      </c:forEach>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>

          </div>
        </section>
      </div>
      <jsp:include page="../../layout/footer.jsp"/>
      <aside class="control-sidebar control-sidebar-dark"></aside>
    </div>

    <script src="/static/plugins/jquery/jquery.min.js"></script>
    <script src="/static/plugins/jquery-ui/jquery-ui.min.js"></script>
    <script>
      $.widget.bridge('uibutton', $.ui.button)
    </script>  
    <script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="/static/plugins/chart.js/Chart.min.js"></script>
    <script src="/static/plugins/sparklines/sparkline.js"></script>
    <script src="/static/plugins/jqvmap/jquery.vmap.min.js"></script>
    <script src="/static/plugins/jqvmap/maps/jquery.vmap.usa.js"></script>
    <script src="/static/plugins/jquery-knob/jquery.knob.min.js"></script>
    <script src="/static/plugins/moment/moment.min.js"></script>
    <script src="/static/plugins/daterangepicker/daterangepicker.js"></script>
    <script src="/static/plugins/tempusdominus-bootstrap-4/js/tempusdominus-bootstrap-4.min.js"></script>
    <script src="/static/plugins/summernote/summernote-bs4.min.js"></script>
    <script src="/static/plugins/overlayScrollbars/js/jquery.overlayScrollbars.min.js"></script>
    <script src="/static/dist/js/adminlte.js"></script>
    <script src="/static/dist/js/pages/dashboard.js"></script>
    <script src="/static/dist/js/jquery-confirm.min.js"></script>
    <script>
    function myFunction(){
      alert("Inside MyFunction!");
      document.forms["form1"].submit();
    }
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
	$(document).ready(function () {
	    function disableBack() {window.history.forward()}
	    window.onload = disableBack();
	    window.onpageshow = function (evt) {if (evt.persisted) disableBack()}
	});
	document.onkeydown = function (e) {
	    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) {
	        e.preventDefault();
	    }
	};
    </script>
  </body>
</html>
