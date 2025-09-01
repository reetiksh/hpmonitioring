<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Annexure VII Report</title>
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
  <jsp:include page="../layout/header.jsp" />
  <jsp:include page="../layout/sidebar.jsp" />
  <div class="content-wrapper">
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Annexure VII</h1>
          </div>
        </div>
      </div>
    </section>
    <section class="content">
      <div class="container-fluid">
        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title">Audit Status by Zone and Circle</h3>
          </div>
          <div class="card-body">
            <table id="annexureTable" class="table table-bordered table-striped" style="width:100%">
              <thead>
                  <tr>
  					<th rowspan="2">Zone</th>
  					<th rowspan="2">Circle</th>
  					<th rowspan="2">Allotted Cases</th>
  					<th rowspan="2">Audit Cases Completed</th>
  					<th rowspan="2">Pending at Desk Review Stage</th>
  					<th rowspan="2">Pending at Audit Plan Approval Stage</th>
  					<th rowspan="2">Pending at Books Examination Stage</th>
  					<th rowspan="2">Pending at DAR Completion Stage</th>
   					<th rowspan="2">Pending at FAR Preparation Stage</th>
 				 </tr>
              </thead>
              <tbody>
                <c:forEach var="row" items="${reportData}">
                  <tr>
                    <td><c:out value="${row.zoneName}"/></td>
                    <td><c:out value="${row.circleName}"/></td>
                    <td><c:out value="${row.allottedCases}"/></td>
                    <td><c:out value="${row.auditCasesCompleted}"/></td>
                    <td><c:out value="${row.pendingDeskReview}"/></td>
                    <td><c:out value="${row.pendingApprovalAuditPlan}"/></td>
                    <td><c:out value="${row.pendingExaminationBooks}"/></td>
                    <td><c:out value="${row.pendingDAR}"/></td>
                    <td><c:out value="${row.pendingFAR}"/></td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </section>
  </div>
  <jsp:include page="../layout/footer.jsp" />
</div>
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="/static/plugins/datatables-bs4/js/dataTables.bootstrap4.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/dataTables.buttons.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.bootstrap4.min.js"></script>
<script src="/static/plugins/jszip/jszip.min.js"></script>
<script src="/static/plugins/pdfmake/pdfmake.min.js"></script>
<script src="/static/plugins/pdfmake/vfs_fonts.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.html5.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.print.min.js"></script>
<script>
  $(function () {
    $("#annexureTable").DataTable({
      "responsive": true,
      "lengthChange": false,
      "autoWidth": false,
      "ordering": false,
      "scrollX": true,
      "buttons": ["excel", "pdf", "print"]
    }).buttons().container().appendTo('#annexureTable_wrapper .col-md-6:eq(0)');
  });
</script>
</body>
</html>
