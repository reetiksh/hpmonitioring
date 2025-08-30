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
  <title>HP GST |Review Enforcement Case</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">
  <!-- <link rel="stylesheet" href="/static/dist/css/googleFront/googleFrontFamilySourceSansPro.css"> -->
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
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Worklogs of Enforcement Case</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="/hq/dashboard">Home</a></li>
              <li class="breadcrumb-item"><a href="/hq/view_enforcement_cases/search_enforcement_cases?GSTIN=${GST_IN}">Review Enforcement Cases</a></li>
              <li class="breadcrumb-item active">Worklogs of Enforcement Case</li>
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
                <h3 class="card-title">Worklogs of Enforcement Case</h3>
              </div>
              <div class="card-body">
                <table id="example1" class="table table-bordered table-striped">
                  <thead>
                    <tr>
                      <th style="text-align: center; vertical-align: middle;">GSTIN</th>
                      <th style="text-align: center; vertical-align: middle;">Taxpayer Name</th>
                      <th style="text-align: center; vertical-align: middle;">Jurisdiction</th>
                      <th style="text-align: center; vertical-align: middle;">Period</th>
                      <th style="text-align: center; vertical-align: middle;">Reporting Date<br>(DD-MM-YYYY)</th>
                      <th style="text-align: center; vertical-align: middle;">Indicative Value(₹)</th>
                      <th style="text-align: center; vertical-align: middle;">Parameters</th>
                      <th style="text-align: center; vertical-align: middle;">Action Status</th>
                      <th style="text-align: center; vertical-align: middle;">Action</th>
                      <th style="text-align: center; vertical-align: middle;">Case ID</th>
                      <th style="text-align: center; vertical-align: middle;">Case Stage</th>
                      <th style="text-align: center; vertical-align: middle;">Case Stage ARN</th>
                      <th style="text-align: center; vertical-align: middle;">Amount(₹)</th>
                      <th style="text-align: center; vertical-align: middle;">Recovery Stage</th>
                      <th style="text-align: center; vertical-align: middle;">Recovery Stage ARN</th>
                      <th style="text-align: center; vertical-align: middle;">Recovery Via DRC03(₹)</th>
                      <th style="text-align: center; vertical-align: middle;">Recovery Against Demand(₹)</th>
                      <th style="text-align: center; vertical-align: middle;">Category</th>
                      <th style="text-align: center; vertical-align: middle;">Review Meeting Comment</th>
                      <th style="text-align: center; vertical-align: middle;">Approver Remarks To Reject The Case</th>
                      <th style="text-align: center; vertical-align: middle;">Verifier Raise Query Remarks</th>
                      <th style="text-align: center; vertical-align: middle;">Assigned From Location</th>
                      <th style="text-align: center; vertical-align: middle;">Assign to User</th>
                      <th style="text-align: center; vertical-align: middle;">Assigned From User</th>
                      <th style="text-align: center; vertical-align: middle;">Other Remarks</th>
                      <th style="text-align: center; vertical-align: middle;">Transfer Remarks</th>
                      <th style="text-align: center; vertical-align: middle;">Suggested Jurisdiction</th>
                      <th style="text-align: center; vertical-align: middle;">Updating Date</th>
                      <th style="text-align: center; vertical-align: middle;">Assigned From</th>
                      <th style="text-align: center; vertical-align: middle;">Assigned To</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach items="${caseLogHistoriyList}" var="object">
                      <tr>
                          <td><c:out value="${object.GSTIN}" /></td>
                          <td><c:out value="${object.taxpayerName}" /></td>
                          <td><c:out value="${object.assignedToLocation}"/></td>
                          <td><c:out value="${object.period}" /></td>
                          <td><fmt:formatDate value="${object.caseReportingDate}" pattern="dd-MM-yyyy" /></td>
                          <td><fmt:formatNumber value="${object.indicativeTaxValue}" pattern="#,##,##0" /></td>
                          <td><c:out value="${object.parameter}" /></td>
                          <td><c:out value="${object.actionStatus}" /></td>
                          <td><c:out value="${object.action}" /></td>
                          <td><c:out value="${object.caseId}" /></td>
                          <td><c:out value="${object.caseStage}" /></td>
                          <td><c:out value="${object.caseStageArn}" /></td>
                          <td><fmt:formatNumber value="${object.demand}" pattern="#,##,##0" /></td>
                          <td><c:out value="${object.recoveryStage}" /></td>
                          <td><c:out value="${object.recoveryStageArn}" /></td>
                          <td><fmt:formatNumber value="${object.recoveryByDRC3}" pattern="#,##,##0" /></td>
                          <td><fmt:formatNumber value="${object.recoveryAgainstDemand}" pattern="#,##,##0" /></td>
                          <td><c:out value="${object.category}" /></td>
                          <td><c:out value="${object.reviewMeetingComment}" /></td>
                          <td><c:out value="${object.approverRemarksToRejectTheCase}" /></td>
                          <td><c:out value="${object.verifierRaiseQueryRemarks}" /></td>
                          <td><c:out value="${object.assignedFromLocation}" /></td>
                          <td><c:out value="${object.assigntoUser}" /></td>
                          <td><c:out value="${object.assignedFromUser}" /></td>
                          <td><c:out value="${object.otherRemarks}" /></td>
                          <td><c:out value="${object.transferRemarks}" /></td>
                          <td><c:out value="${object.suggestedJurisdiction}" /></td>
                          <td><fmt:formatDate value="${object.updatingDate}" pattern="dd-MM-yyyy HH:mm:ss" /></td>
                          <!-- <td><c:out value="${object.updatingDate}" /></td> -->
                          <td><c:out value="${object.assignedFrom}" /></td>
                          <td><c:out value="${object.assignedTo}" /></td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>
              </div>
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
      "order": [[26, 'desc']],
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
  new DataTable('#example', {
    scrollX: true
});
</script>

<script>
  $(function () {
			$('#example3').DataTable( {
        scrollX: true,
				dom: 'Blfrtip',
				buttons: [
					{
						extend: 'excelHtml5'
					},
					{
						extend: 'csvHtml5'
					},
					{
						extend: 'print'
					}
				]
			});		
		});
</script>
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
