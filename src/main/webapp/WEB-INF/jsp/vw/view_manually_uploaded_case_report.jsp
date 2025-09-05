<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST | Dashboard</title>

  <!-- styles (kept as in your page) -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/dist/ionicons-2.0.1/css/ionicons.min.css">
  <link rel="stylesheet" href="/static/plugins/tempusdominus-bootstrap-4/css/tempusdominus-bootstrap-4.min.css">
  <link rel="stylesheet" href="/static/plugins/icheck-bootstrap/icheck-bootstrap.min.css">
  <link rel="stylesheet" href="/static/plugins/jqvmap/jqvmap.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="/static/plugins/overlayScrollbars/css/OverlayScrollbars.min.css">
  <link rel="stylesheet" href="/static/plugins/daterangepicker/daterangepicker.css">
  <link rel="stylesheet" href="/static/plugins/summernote/summernote-bs4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- header / sidebar (unchanged includes) -->
  <jsp:include page="../layout/header.jsp" />
  <jsp:include page="../layout/sidebar.jsp" />

  <!-- ====================== MAIN ====================== -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- page header -->
        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6">
              <h1 class="m-0">Manually uploaded Old Case Report</h1>
            </div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/vw/dashboard">Home</a></li>
                <li class="breadcrumb-item"><a href="/vw/mis">MIS</a></li>
                <li class="breadcrumb-item active">Report</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- content -->
        <section class="content">
          <div class="container-fluid">

            <%-- SEARCH FORM (commented in original; left intact) --%>
            <%-- 
            <form method="POST" action="submit_manually_uploaded_case_report" modelAttribute="workFlowModel">
              <div class="row">
                <div class="col-md-6">
                  <div class="form-group">
                    <label>View<span style="color: red;"> *</span></label>
                    <select id="category" name="category" class="form-select" required>
                      <option value="">Please Select</option>
                      <option value="EZ" <c:if test="${location eq 'EZ'}">selected</c:if>>Enforcement Zone-wise</option>
                      <option value="C"  <c:if test="${location eq 'C'}">selected</c:if>>Circle-wise</option>
                    </select>
                  </div>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <div class="col-md-6">
                  <div class="text-center">
                    <button type="submit" class="btn btn-primary my-3">Search</button>
                  </div>
                </div>
              </div>
            </form>
            --%>

            <br>

            <!-- modal -->
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

            <!-- table -->
            <div class="row">
              <div class="col-md-12">
                <table id="example1" class="table table-bordered table-striped">
                  <thead>
                    <tr>
                      <th style="text-align: center; vertical-align: middle;">Jurisdiction</th>
                      <th style="text-align: center; vertical-align: middle;">Total Indicative Value(₹)</th>
                      <th style="text-align: center; vertical-align: middle;">Total Case</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach items="${oldCaseList}" var="object">
                      <tr>
                        <td><c:out value="${object[0]}" /></td>
                        <td><c:out value="${object[2]}" /></td>
                        <td>
                          <i class="fa fa-eye" aria-hidden="true" style="cursor:pointer;color:#4682b4"
                             onclick="drillDown('${object[1]}')">${object[3]}</i>
                        </td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>
              </div>
            </div>

          </div>
        </section>

      </div><!-- /.container-fluid -->
    </div><!-- /.app-content -->
  </main>

  <!-- footer include (unchanged) -->
  <jsp:include page="../layout/footer.jsp" />
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div><!-- /.app-wrapper -->

<!-- overlay for mobile sidebar -->
<div class="sidebar-overlay" data-lte-dismiss="sidebar"></div>

<!-- scripts (kept; only minimal tweaks for BS5/AdminLTE4 usage) -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/jquery-ui/jquery-ui.min.js"></script>
<script> $.widget.bridge('uibutton', $.ui.button) </script>
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
<script src="/static/dist/js/googleCharts/googleChartsLoader.js"></script>
<script src="/static/dist/js/pages/dashboard.js"></script>

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
<script src="/static/dist/js/bootstrap-select.min.js"></script>

<script type="text/javascript">
  function drillDown(location) {
    var link = 'landingDrillDownOldCaseList?location=' + location;

    $.ajax({
      url: '/checkLoginStatus',
      method: 'get',
      async: false,
      success: function(result){
        const myJSON = JSON.parse(result);
        if (result == 'true'){
          $('#exampleModal').load(link, function(response, status, xhr){
            if (status == 'success'){
              var modalEl = document.getElementById('modal1');
              new bootstrap.Modal(modalEl).show();
            } else {
              console.log("failed");
            }
          });
        } else if (result == 'false'){
          window.location.reload();
        }
      }
    });
  }
</script>

<script type="text/javascript">
  document.addEventListener('contextmenu', function(e){ e.preventDefault(); });
  document.addEventListener('keydown', function(e){
    if (e.ctrlKey && e.key === 'u') { e.preventDefault(); }
  });
</script>

<script>
  $("#foCaseStageList").DataTable({
    "responsive": true,
    "lengthChange": false,
    "autoWidth": false
  });

  $(function() {
    $("#example1").DataTable({
      "responsive": true,
      "lengthChange": false,
      "autoWidth": true,
      "buttons": ["excel","pdf","print"]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');

    $('#example2').DataTable({
      "paging": true,
      "lengthChange": false,
      "searching": false,
      "ordering": true,
      "info": true,
      "autoWidth": false,
      "responsive": true
    });
  });

  function checkForm(){
    alert("dashboard");
    $("#category").prop("required", true);
    $("#financialyear").prop("required", false);
    $("#view").prop("required", true);
  }
</script>
</body>
</html>
