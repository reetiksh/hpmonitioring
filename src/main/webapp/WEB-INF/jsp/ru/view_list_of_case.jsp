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
            <h1>Review Case Summary</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="/ru/dashboard">Home</a></li>
              <li class="breadcrumb-item"><a href="/ru/review_category_case">Review Status</a></li>
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
            <div class="card-primary">
              <div class="card-header">
                <h3 class="card-title">Review Cases List ${category} </h3>
              </div>
              <div class="card-body">
                <table id="example1" class="table table-bordered table-striped">
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
                    <c:if test="${category eq 'Self Detected Cases'}">
                    <th style="text-align: center; vertical-align: middle;">Reason</th>
                    </c:if>                    
                    <th style="text-align: center; vertical-align: middle;">Supporting File</th>
                  </tr>
                  </thead>
                  <tbody>
                    <c:forEach items="${caseList}" var="object">
                      <tr>
                          <td><c:out value="${object.id.GSTIN}" /></td>
                          <td><c:out value="${object.taxpayerName}" /></td>
                          <td><c:out value="${object.locationDetails.locationName}"/></td>
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
                          <c:if test="${category eq 'Self Detected Cases'}">
                          <td><c:out value="${object.findingCategory}" /></td>
                          </c:if>
                          <td style="text-align: center;">
                          <c:if test="${object.fileName != null}">
                          <a href="/ru/downloadFile?fileName=${object.fileName}"><button type="button" onclick="" class="btn btn-primary"><i class="fas fa-download"></i></button></a>
                          </c:if>
                          </td>
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
