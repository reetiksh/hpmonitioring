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
  <!-- <link rel="stylesheet" href="/static/dist/css/googleFront/googleFrontFamilySourceSansPro.css"> -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>
  <div class="content-wrapper">
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Update Enforcement Case</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="/hq/dashboard">Home</a></li>
              <li class="breadcrumb-item active">Update Enforcement Case</li>
            </ol>
          </div>
        </div>
      </div><!-- /.container-fluid -->
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-12">
            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title">Search for Enforcement Case</h3>
              </div>
              <form method="GET" id="searchEnforcementCases" name="searchEnforcementCases" action="<c:url value='/hq/search_enforcement_cases' />" enctype="multipart/form-data">
                <div class="card-body">
                    <div class="form-group col-md-12">
                      <div class="row">
                        <div class="col-md-4">
                          <div class="row">
                            <div class="col-md-12">
                              <label for="GSTIN">GSTIN  <span style="color: red;"> *</span><span id="GSTIN_alert"></span></label>
                            </div>
                            <div class="col-md-12">
                              <input type="text" class="form-control" id="GSTIN" name="GSTIN" placeholder="Please Enter GSTIN" required>
                            </div>
                          </div>
                        </div>
                        <div class="col-md-4">
                          <div class="row">
                            <div class="col-md-12">
                              <label for="period">Period <span id="period_alert"></span></label>
                            </div>
                            <div class="col-md-12">
                              <select id="period" name="period" class="selectpicker col-md-12" data-live-search="true" title="Please Select Period">
                                <c:forEach items="${periods}" var="period">
                                    <c:choose>
                                      <c:when test="${period eq selectPeriod}">
                                          <option value="${period}" selected="selected">${period}</option>
                                      </c:when>
                                      <c:otherwise>
                                          <option value="${period}">${period}</option>
                                      </c:otherwise>
                                    </c:choose>
                                  </c:forEach>
                              </select>
                            </div>
                          </div>
                        </div>
                        <div class="col-md-3">
                          <div class="row">
                            <div class="col-md-12">
                              <label for="caseReportingDate">Case Reporting Date <span id="caseReportingDate_alert"></span></label>
                            </div>
                            <div class="col-md-12">
                              <input type="date" class="form-control" id="caseReportingDate" name="caseReportingDate"  value="" placeholder="Please enter Case Reporting Date"/>
                            </div>
                          </div>
                        </div>
                        <div class="col-md-1" style="text-align: center;">
                          <br>
                          <button type="submit" class="btn btn-primary" style="margin-top: 8px;"><i class="fa fa-search" aria-hidden="true"></i></button>
                        </div>
                      </div>
                    </div>
                </div>
              </form>
            </div>
            <c:if test="${not empty successMessage}">
							<div class="col-12 alert alert-success alert-dismissible fade show" id="message"  role="alert" style="max-height: 500px; overflow-y: auto;">
                <strong>${successMessage}</strong><br>
								<button type="button" class="close" data-dismiss="alert" aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
							</div>
						</c:if>
            <c:if test="${not empty caseList}">
              <div class="card card-primary">
                <div class="card-header">
                  <h3 class="card-title">Enforcement Cases</h3>
                </div>
                  <div class="card-body">
                      <div class="form-group col-md-12">
                        <table id="example1" class="table table-bordered table-striped">
                          <thead>
                          <tr>
                            <th style="text-align: center; vertical-align: middle;">GSTIN</th>
                            <th style="text-align: center; vertical-align: middle;">Taxpayer Name</th>
                            <th style="text-align: center; vertical-align: middle;">Jurisdiction</th>
                            <th style="text-align: center; vertical-align: middle;">Category</th>
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
                            <th style="text-align: center; vertical-align: middle;">Action</th>
                          </tr>
                          </thead>
                          <tbody>
                            <c:forEach items="${caseList}" var="object">
                              <tr>
                                  <td><c:out value="${object.id.GSTIN}" /></td>
                                  <td><c:out value="${object.taxpayerName}" /></td>
                                  <td><c:out value="${object.locationDetails.locationName}"/></td>
                                  <td><c:out value="${object.category}"/></td>
                                  <td><c:out value="${object.id.period}" /></td>
                                  <td><c:out value="${object.extensionNo}" /></td>
                                  <td><fmt:formatDate value="${object.id.caseReportingDate}" pattern="dd-MM-yyyy" /></td>
                                  <td><fmt:formatNumber value="${object.indicativeTaxValue}" pattern="#,##,##0" /></td>
                                  <td><c:out value="${object.assignedTo}" /></td>
                                  <td><c:out value="${object.actionStatus.name}" /></td>
                                  <td><c:out value="${object.caseId}" /></td>
                                  <td><c:out value="${object.caseStage.name}" /></td>
                                  <td><c:out value="${object.caseStageArn}" /></td>
                                  <td><fmt:formatNumber value="${object.demand}" pattern="#,##,##0" /></td>
                                  <td><c:out value="${object.recoveryStage.name}" /></td>
                                  <td><c:out value="${object.recoveryStageArn}" /></td>
                                  <td><fmt:formatNumber value="${object.recoveryByDRC3}" pattern="#,##,##0" /></td>
                                  <td><fmt:formatNumber value="${object.recoveryAgainstDemand}" pattern="#,##,##0" /></td>
                                  <td><c:out value="${object.parameter}" /></td>
                                  <td style="text-align: center;"><a href="/hq/downloadFile?fileName=${object.extensionNoDocument.extensionFileName}"><button type="button" onclick="" class="btn btn-primary"><i class="fas fa-download"></i></button></a></td>
                                  <td style="text-align: center;">
                                    <c:if test="${object.assignedTo eq 'FO'}">
                                      <button type="button" title="Edit User" class="btn btn-info" onclick="viewEnforcementCases('${object.id.GSTIN}', '${object.id.period}', '${object.id.caseReportingDate}')"><i class="fas fa-edit nav-icon"></i></button>
                                    </c:if>
                                    <c:if test="${object.assignedTo ne 'FO'}">
                                      <button type="button" title="Edit User" class="btn btn-info" onclick="viewEnforcementCases('${object.id.GSTIN}', '${object.id.period}', '${object.id.caseReportingDate}')" disabled><i class="fas fa-edit nav-icon"></i></button>
                                    </c:if>
                                  </td>
                              </tr>
                            </c:forEach>
                          </tbody>
                        </table>
                      </div>
                  </div>
              </div>
            </c:if>
            <!-- /.card -->
          </div>
          <!-- /.col -->
        </div>
        <!-- /.row -->
      </div>
      <!-- /.container-fluid -->
    </section>
    <!-- /.content -->
  </div>
  <div class="modal fade" id="viewTransferRoleModal" tabindex="-1" role="dialog" aria-labelledby="viewTransferRoleModalTitle" aria-hidden="true" >
		<div class="modal-dialog modal-dialog-centered modal-lg" role="document" >
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="updateSummaryViewModalTitle">Update Enforcement Review Cases Details </h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">    
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body" id="viewTransferRole" >
				</div>
			</div>
		</div>
	</div>

  <!-- /.content-wrapper -->
  <jsp:include page="../layout/footer.jsp"/>


  <!-- Control Sidebar -->
  <aside class="control-sidebar control-sidebar-dark">
    <!-- Control sidebar content goes here -->
  </aside>
  <!-- /.control-sidebar -->
</div>
<!-- ./wrapper -->

<!-- jQuery -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<!-- Bootstrap 4 -->
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<!-- DataTables  & Plugins -->
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
<!-- Select drop-down -->
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>
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

$(document).ready(function() {
  $("#message").fadeTo(5000, 500).slideUp(500, function() {
    $("#message").slideUp(500);
  });
});
</script>

<!-- AdminLTE App -->
<script src="/static/dist/js/adminlte.min.js"></script>
<!-- AdminLTE for demo purposes -->
<!-- <script src="/static/dist/js/demo.js"></script> -->
<!-- Page specific script -->
<script>
  $(function () {
    $("#example1").DataTable({
      "responsive": false, "lengthChange": false, "autoWidth": true, "scrollX": true,
      "buttons": 
        [
          "excel",
          "pdf",
          "print", 
        ]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
    $('#example2').DataTable({
      "paging": true,
      "lengthChange": false,
      "searching": false,
      "ordering": true,
      "info": true,
      "autoWidth": false,
      "responsive": true,
    });
  });
</script>
<script>
  function viewEnforcementCases(GSTIN, period, caseReportingDate){
    var date = String(caseReportingDate);
    var caseReportingDateStr = date.substring(0, 11);

    var link = '/hq/view_enforcement_case_to_edit?GSTIN=' + GSTIN + '&period=' + period + '&caseReportingDateStr=' + caseReportingDateStr;

    $.ajax({url: '/checkLoginStatus',
			method: 'get',
			async: false,
			success: function(result){
				const myJSON = JSON.parse(result);
				if(result=='true'){
					$("#viewTransferRole").load(link, function(response, status, xhr){
						if(status == 'success'){
								$("#viewTransferRoleModal").modal('show');
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
<script>
	$('form').on('submit', function(oEvent) {
		oEvent.preventDefault();
		$.confirm({
			title : 'Confirm!',
			content : 'Do you want to proceed ahead to search the case(s)?',
			buttons : {
				submit : function() {
					oEvent.currentTarget.submit();
				},
				close : function() {
					$.alert('Canceled!');
				}
			}
		});
	}) 
</script>

</body>
</html>
