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
              <link rel="stylesheet" href="/static/plugins/select2/select2.min.css">
              <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
              <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
            </head>

            <body class="hold-transition sidebar-mini">
              <div class="wrapper">
                <jsp:include page="../../layout/header.jsp" />
                <jsp:include page="../../hq/transfer_popup.jsp" />
                <jsp:include page="../../layout/sidebar.jsp" />
                <div class="content-wrapper">
                  <section class="content-header">
                    <div class="container-fluid">
                      <div class="row mb-2">
                        <div class="col-sm-6">
                          <h1>Transfer Request</h1>
                        </div>
                        <div class="col-sm-6">
                          <ol class="breadcrumb float-sm-right">
                            <li class="breadcrumb-item"><a href="/hq/dashboard">Home</a></li>
                            <li class="breadcrumb-item active">Request For Transfer</li>
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
                              <h3 class="card-title">Transfer Request</h3>
                            </div>
                            <!-- /.card-header -->
                            <div class="card-body">
                              <c:if test="${not empty successMessage}">
                                <div class="col-12 alert alert-success alert-dismissible fade show" id="message"
                                  role="alert" style="max-height: 500px; overflow-y: auto;">
                                  <strong>${successMessage}</strong><br>
                                  <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                  </button>
                                </div>
                              </c:if>
                              <c:if test="${!empty hqTransferList}">
                                <table id="example1" class="table table-bordered table-striped">
                                  <thead>
                                    <tr>
                                      <th style="text-align: center; vertical-align: middle;">GSTIN</th>
                                      <th style="text-align: center; vertical-align: middle;">Taxpayer Name</th>
                                      <th style="text-align: center; vertical-align: middle;">Category</th>
                                      <th style="text-align: center; vertical-align: middle;">Reporting
                                        Date<br>(DD-MM-YYYY)</th>
                                      <th style="text-align: center; vertical-align: middle;">Case Period</th>
                                      <th style="text-align: center; vertical-align: middle;">Indicative Value(₹)</th>
                                      <th style="text-align: center; vertical-align: middle;">Case Assigned To</th>
                                      <th style="text-align: center; vertical-align: middle;">Suggested Jurisdiction
                                      </th>
                                      <th style="text-align: center; vertical-align: middle;">Requested on</th>
                                      <th style="text-align: center; vertical-align: middle;">Action</th>
                                    </tr>
                                  </thead>
                                  <tbody>
                                    <c:forEach items="${hqTransferList}" var="object">
                                      <tr>
                                        <td style="vertical-align: middle;">
                                          <c:out value="${object.GSTIN_ID}" />
                                        </td>
                                        <td style="vertical-align: middle;">
                                          <c:out value="${object.taxpayerName}" />
                                        </td>
                                        <td style="vertical-align: middle;">
                                          <c:out value="${object.category}" />
                                        </td>
                                        <td style="vertical-align: middle;">
                                          <fmt:formatDate value="${object.date}" pattern="dd-MM-yyyy" />
                                        </td>
                                        <td style="vertical-align: middle;">
                                          <c:out value="${object.period_ID}" />
                                        </td>
                                        <td style="vertical-align: middle;">
                                          <fmt:formatNumber value="${object.indicativeTaxValue}" pattern="#,##,##0" />
                                        </td>
                                        <td style="vertical-align: middle;">
                                          <c:out value="${object.caseStageName}" />
                                        </td>
                                        <td style="vertical-align: middle;">
                                          <c:out value="${object.actionStatusName}" />
                                        </td>
                                        <td style="vertical-align: middle;">
                                          <fmt:formatDate value="${object.caseUpdatedDate}" pattern="dd-MM-yyyy" />
                                        </td>
                                        <td style="text-align: center; vertical-align: middle;">
                                          <button type="button" class="btn btn-primary btn-block"
                                            onclick="approveCase('${object.GSTIN_ID}' , '${object.date}' , '${object.period_ID}' , '${object.parameter}' , '${object.caseStageArn}')">
                                            Approve </button>
                                          <button type="button" class="btn btn-danger btn-block"
                                            onclick="rejectCase('${object.GSTIN_ID}' , '${object.date}' , '${object.period_ID}' , '${object.parameter}' , '${object.caseStageArn}')">
                                            Reject </button>
                                          <c:if test="${not empty object.uploadedFileName}">
                                            <br>
                                            <a href="/hq/downloadFOFile?fileName=${object.uploadedFileName}">
                                              <button type="button" class="btn btn-primary btn-block">
                                                <i class="fas fa-download"></i>
                                              </button>
                                            </a>
                                          </c:if>
                                        </td>
                                      </tr>
                                    </c:forEach>
                                  </tbody>
                                </table>
                              </c:if>
                              <c:if test="${empty hqTransferList}">
                                <div class="col-12" style="text-align: center;">
                                  <i class="fa fa-info-circle" style="font-size:100px;color:rgb(97, 97, 97)"
                                    aria-hidden="true"></i><br>
                                  <span style="font-size:35px;color:rgb(97, 97, 97)">No Transfer Request
                                    Available</span>
                                </div>
                              </c:if>
                            </div>
                            <!-- /.card-body -->
                          </div>
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
                <!-- /.content-wrapper -->
                <jsp:include page="../../layout/footer.jsp" />
                <jsp:include page="../../cag/hq/approve_popup.jsp" />
                <jsp:include page="../../cag/hq/reject_popup.jsp" />

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
              <script src="/static/plugins/select2/select2.min.js"></script>
              <script src="/static/dist/js/jquery-confirm.min.js"></script>
              <script>
                document.addEventListener('contextmenu', function (e) {
                  e.preventDefault();
                });
                document.addEventListener('keydown', function (e) {
                  if (e.ctrlKey && e.key === 'u') {
                    e.preventDefault();
                  }
                });
                document.addEventListener('keydown', function (e) {
                  if (e.key === 'F12') {
                    e.preventDefault();
                  }
                });
                // Disable back and forward cache
                $(document).ready(function () {
                  function disableBack() { window.history.forward() }

                  window.onload = disableBack();
                  window.onpageshow = function (evt) { if (evt.persisted) disableBack() }
                });
                // Disable refresh
                document.onkeydown = function (e) {
                  if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) {
                    e.preventDefault();

                  }
                };

                $('select').select2();

                $(document).ready(function () {
                  $("#message").fadeTo(5000, 500).slideUp(500, function () {
                    $("#message").slideUp(500);
                  });
                });
              </script>
              <script>
                $(function () {
                  $('select').select2({
                    dropdownParent: $('#transferModal')
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
                    "responsive": true, "lengthChange": false, "autoWidth": false,
                    "buttons":
                      [
                        // "copy",
                        // "csv",
                        "excel",
                        "pdf",
                        "print",
                        // "colvis"
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

                function transferBtn(remark, suggestedJurisdictionId, assignedFromLocationId, gstIn, caseReportingDate, period) {

                  $("#remark").val(remark);
                  $("#assignedFromLocationId").val(assignedFromLocationId);
                  $("#gstIn").val(gstIn);
                  $("#caseReportingDate").val(caseReportingDate);
                  $("#period").val(period);

                  var selectValues = JSON.parse('${locatoinMap}');

                  // console.log(selectValues);
                  const selectElement = document.getElementById('locationId');

                  $("#locationId").val(suggestedJurisdictionId);

                  //Remove all previous options from select drop-down
                  $('#locationId').children().remove().end().append('<option disabled selected value="">Select Jurisdiction</option>');

                  $.each(selectValues, function (key, value) {
                    // console.log(key);
                    if (key != assignedFromLocationId) {
                      if (key == suggestedJurisdictionId) {
                        $('#locationId').append('<option selected value="' + key + '">' + selectValues[key] + '</option>');
                      } else {
                        $('#locationId').append('<option value="' + key + '">' + selectValues[key] + '</option>');
                      }
                    }
                  });

                  $("#transferModal").modal('show');

                }


                function approveCase(gstno, date, period, parameter, jurisdiction) {

                  $("#app_gstno").val(gstno);
                  $("#app_date").val(date);
                  $("#app_period").val(period);
                  $("#app_parameter").val(parameter);
                  $("#app_jurisdiction").val(jurisdiction);

                  $("#confirmationApproveModal").modal('show');

                }


                function rejectCase(gstno, date, period, parameter, jurisdiction) {

                  $("#gstno").val(gstno);
                  $("#date").val(date);
                  $("#period_id").val(period);
                  $("#parameter").val(parameter);
                  $("#jurisdiction").val(jurisdiction);

                  $("#raiseQueryModal").modal('show');

                }



                $('#rejectForm').on('submit', function (oEvent) {

                  oEvent.preventDefault();

                  $.confirm({
                    title: 'Confirm!',
                    content: 'Do you want to proceed ahead with reject the transfer case?',
                    buttons: {
                      submit: function () {

                        oEvent.currentTarget.submit();

                      },
                      close: function () {
                        $.alert('Canceled!');
                      }
                    }
                  });
                });


                $('#approveForm').on('submit', function (oEvent) {

                  oEvent.preventDefault();

                  $.confirm({
                    title: 'Confirm!',
                    content: 'Do you want to proceed ahead with approve the transfer case?',
                    buttons: {
                      submit: function () {

                        oEvent.currentTarget.submit();

                      },
                      close: function () {
                        $.alert('Canceled!');
                      }
                    }
                  });

                });



              </script>






            </body>

            </html>