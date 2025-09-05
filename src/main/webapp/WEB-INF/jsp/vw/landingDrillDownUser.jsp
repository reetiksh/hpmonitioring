<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Cases List</title>

  <!-- AdminLTE 4 / Bootstrap 5 core styles -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css"/>
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css"/>

  <!-- DataTables (keep dataset/logic intact) -->
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css"/>
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css"/>
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css"/>
</head>
<body class="layout-fixed bg-body-tertiary">
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <div class="row">
          <div class="col-12">
            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title mb-0">Cases</h3>
              </div>

              <div class="card-body">
                <div class="table-responsive">
                  <table id="example" class="table table-bordered table-striped">
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
                              <a href="/vw/downloadUploadedFile?fileName=${object.fileName}">
                                <button type="button" class="btn btn-primary">
                                  <i class="fas fa-download"></i>
                                </button>
                              </a>
                            </c:if>
                          </td>
                        </tr>
                      </c:forEach>
                    </tbody>
                  </table>
                </div>
              </div><!-- /.card-body -->
            </div>
          </div>
        </div>

      </div><!-- /.container-fluid -->
    </div><!-- /.app-content -->
  </main>

  <!-- Scripts (AdminLTE 4 baseline + DataTables). Keeping logic unchanged -->
  <script src="/static/plugins/jquery/jquery.min.js"></script>
  <script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script src="/static/dist/js/adminlte.min.js"></script>

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

  <script>
    $(document).ready(function () {
      $('#example').DataTable({
        dom: 'Bfrtip',
        buttons: ['excel', 'pdf', 'print'],
        initComplete: function () {
          console.log('DataTable initialized');
        }
      });
    });
  </script>
</body>
</html>
