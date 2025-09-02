<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
  <style>
    #apDataList td {
      text-align: center;
      vertical-align: middle;
    }
    #apDataList_filter {
      float: right
    }
  </style>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Review Case List</title>
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
  <div class="app-wrapper">
    <jsp:include page="../layout/header.jsp" />
    <jsp:include page="../layout/sidebar.jsp" />
    <jsp:include page="../ap/ap_case_approved_pop_up.jsp" />
    <jsp:include page="../ap/ap_case_rejected_pop_up.jsp" />
    <main class="app-main">
    <div class="app-content">
      <section class="content-header">
        <div class="container-fluid">
          <div class="row mb-2">
            <div class="col-sm-6">
              <h1>Approve/Revert Cases</h1>
            </div>
            <div class="col-sm-6">
              <ol class="breadcrumb float-sm-right">
                <li class="breadcrumb-item"><a href="/ap/dashboard">Home</a></li>
                <li class="breadcrumb-item active">Approve/Revert Cases</li>
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
                  <h3 class="card-title">Approve/Revert Cases</h3>
                </div>
                <div class="alert alert-success" role="alert" style="display: none;"
                  id="commonBoostrapAlertSuccess">
                  <div style="display:none;" id="caseApprovedTagLine">Case Approved Successfully !</div>
                </div>
                <div class="alert alert-success" role="alert" style="display: none;"
                  id="commonBoostrapAlertFail">
                  <div style="display:none;" id="caseRejectedTagLine">Case Reverted Successfully !</div>
                </div>
                <div class="card-body">
                  <table id="apDataList" class="table table-bordered table-striped">
                    <thead>
                      <tr>
                        <th style="text-align: center; vertical-align: middle;">GSTIN</th>
                        <th style="text-align: center; vertical-align: middle;">Taxpayer Name</th>
                        <th style="text-align: center; vertical-align: middle;">Jurisdiction</th>
                        <th style="text-align: center; vertical-align: middle;">Period</th>
                        <th style="text-align: center; vertical-align: middle;">Reporting Date (DD-MM-YYYY)</th>
                        <th style="text-align: center; vertical-align: middle;">Indicative Value(₹)</th>
                        <th style="text-align: center; vertical-align: middle;">Case Category</th>
                        <th style="text-align: center; vertical-align: middle;">Action Status</th>
                        <th style="text-align: center; vertical-align: middle;">Case Id</th>
                        <th style="text-align: center; vertical-align: middle;">Case Stage</th>
                        <th style="text-align: center; vertical-align: middle;">Case Stage ARN</th>
                        <th style="text-align: center; vertical-align: middle;">Amount(₹)</th>
                        <th style="text-align: center; vertical-align: middle;">Recovery Stage</th>
                        <th style="text-align: center; vertical-align: middle;">Recovery Stage ARN</th>
                        <th style="text-align: center; vertical-align: middle;">Recovery Via DRC03(₹)</th>
                        <th style="text-align: center; vertical-align: middle;">Recovery Against Demand(₹)</th>
                        <th style="text-align: center; vertical-align: middle;">Parameter</th>
                        <th style="text-align: center; vertical-align: middle;">Verifier Remarks</th>
                        <th style="text-align: center; vertical-align: middle;">Approver Remarks</th>
                        <th style="text-align: center; vertical-align: middle;">Supporting File</th>
                        <th style="text-align: center; vertical-align: middle;">Action</th>
                      </tr>
                    </thead>
                    <tbody></tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</main>
  <jsp:include page="../layout/footer.jsp" />
  </div>
  <div class="modal fade" id="closeCaseModal" tabindex="-1" role="dialog" aria-labelledby="closeCaseModalTitle"
    aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
      <div class="modal-content">
        <div>
          <div class="modal-header">
            <h5 class="modal-title" id="confirmationModalTitle">Do you want to proceed ahead with approval for
              closure of the case ?</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <p>I hereby undertake that;</p>
            <p><i class="fa fa-check" aria-hidden="true"></i> All the points raised by EIU pertaining to this
              Taxpayer have been dealt carefully in the light of the provisions of the GST laws</p>
            <p><i class="fa fa-check" aria-hidden="true"></i> Applicable Tax has been levied as per the
              provisions of GST acts/rules 2017</p>
            <p><i class="fa fa-check" aria-hidden="true"></i> Applicable Interest has been levied as per the
              provisions of GST acts/rules 2017</p>
            <p><i class="fa fa-check" aria-hidden="true"></i> Applicable Penalty has been levied as per the
              provisions of GST acts/rules 2017</p>
            <p><input type="checkbox" id="mycheckbox" name="checkbox"> I hereby declare that above information
              is true and correct to the best of my knowledge</p>
          </div>
          <input type="hidden" id="gstno" name="gstno">
          <input type="hidden" id="date" name="date">
          <input type="hidden" id="period" name="period">
          <div class="modal-footer">
            <div id="checked" style="display:none">
              <button onclick="submitApproverDeclaration()" class="btn btn-primary" id="okayBtn">Okay</button>
            </div>
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" id="closeBtn">Cancel</button>
          </div>
        </div>
      </div>
    </div>
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
  <script src="/static/dist/js/jquery-confirm.min.js"></script>
  <script src="/static/dist/js/adminlte.min.js"></script>
  <script>
    $(document).ready(function () {
      $('#apDataList').DataTable({

        "processing": true, // Enable processing indicator
        "serverSide": true, // Enable server-side processing
        "ordering": false, // Enable ordering/sorting
        "autoWidth": true,
        "scrollX": true,
        "dom": 'Bfrtip',
        "buttons": [
          {
            extend: 'excel',
            "action": function (e, dt, button, config) {
              window.location = '/ap/download_approve_reject_excel'; // Custom endpoint for exporting data to Excel
            }
          },
        ],
        "ajax": {
          "url": "/ap/approved_rejected_cases_ajax", // URL for server-side data
          "type": "GET", // HTTP method for AJAX request
          "data": function (d) {
            return {
              draw: d.draw,
              start: d.start,
              length: d.length,
              searchValue: d.search.value,
              actionStatus: '${actionStatus}'
            };
          }
        },
        "columns": [
          { "data": "id.gstin" },
          { "data": "taxpayerName" },
          { "data": "locationDetails.locationName" },
          { "data": "id.period" },
          {
            "data": "id.caseReportingDate",
            "render": function (data, type, row) {
              if (data) {
                var date = new Date(data);
                var day = ("0" + date.getDate()).slice(-2);
                var month = ("0" + (date.getMonth() + 1)).slice(-2);
                var year = date.getFullYear();
                return day + '-' + month + '-' + year;
              }
              return '';
            }

          },
          { "data": "indicativeTaxValue" },
          { "data": "category" },
          { "data": "actionStatus.name" },
          { "data": "caseId" },
          { "data": "caseStage.name" },
          { "data": "caseStageArn" },
          { "data": "demand" },
          { "data": "recoveryStage.name" },
          { "data": "recoveryStageArn" },
          { "data": "recoveryByDRC3" },
          { "data": "recoveryAgainstDemand" },
          { "data": "parameter" },
          {
            "data": "remark",
            "render": function (data, type, row) {
              if (data && data.length > 0) {
                return data.map((remark) =>
                  `<i class="fas fa-pen nav-icon fa-xs"></i> ${remark}<br>`).join('');
              } else {
                return "";
              }
            }
          },
          {
            "data": "apRemarks",
            "render": function (data, type, row) {
              if (data && data.length > 0) {
                // Generate HTML for each remark with an icon
                return data.map((remark) =>
                  `<i class="fas fa-pen nav-icon fa-xs"></i> ${remark}<br>`
                ).join('');
              } else {
                return ""; // Default text if remarks are empty
              }
            }
          },

          {
            "data": "fileName",
            "render": function (data, type, row) {
              if (data) {
                // Render a download button with a link
                return `<a href="/ap/downloadFile?fileName=` + data + `" target="_blank">
                <button type="button" class="btn btn-primary">
                    <i class="fas fa-download"></i>
                </button>
            </a>`;
              } else {
                return "No File"; // Fallback if fileName is not available
              }
            }
          },
          {
            "data": null,
            "render": function (data, type, row) {
              return `
                    <button type="button" 
                      onclick="handleButtonApproveClick(this);" 
                      class="btn btn-primary" 
                      id="recommendedBtn" 
                      style="margin-bottom: 4px">Approve</button>
                    <button type="button" 
                      onclick="handleButtonRejectClick(this);" 
                      class="btn btn-primary" 
                      id="raiseQueryBtn">Revert</button>
                `;
            }
          },
        ]
      });
      // Define the function to handle the approve button click and get row data
      window.handleButtonApproveClick = function (button) {
        debugger;
        // Get the row containing the clicked button
        var table = $(button).closest('table').DataTable();
        var row = $(button).closest('tr'); // This finds the row
        var rowData = table.row(row).data(); // Get the data for the row
        // For example, to get the 'gstin' of that row
        var gstin = rowData.id.gstin;
        var circle = rowData.locationDetails.locationName;
        var reportingDate = rowData.id.caseReportingDate;
        const date = new Date(reportingDate);
        // Extract the year, month, and day
        var year = date.getFullYear();
        var month = String(date.getMonth() + 1).padStart(2, '0'); // Month is zero-indexed, so add 1
        var day = String(date.getDate()).padStart(2, '0');
        // Format the date as yyyy-mm-dd
        var formattedDate = year + "-" + month + "-" + day
        var period = rowData.id.period
        callApprovedCase(gstin, circle, formattedDate, period);
      };
      // Define the function to handle the revert                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             button click and get row data
      window.handleButtonRejectClick = function (button) {
        debugger;
        // Get the row containing the clicked button
        var table = $(button).closest('table').DataTable();
        var row = $(button).closest('tr'); // This finds the row
        var rowData = table.row(row).data(); // Get the data for the row

        // For example, to get the 'gstin' of that row
        var gstin = rowData.id.gstin;
        var circle = rowData.locationDetails.locationName;
        var reportingDate = rowData.id.caseReportingDate;

        const date = new Date(reportingDate);

        // Extract the year, month, and day
        var year = date.getFullYear();
        var month = String(date.getMonth() + 1).padStart(2, '0'); // Month is zero-indexed, so add 1
        var day = String(date.getDate()).padStart(2, '0');

        // Format the date as yyyy-mm-dd
        var formattedDate = year + "-" + month + "-" + day
        var period = rowData.id.period
        callRejectedCase(gstin, circle, formattedDate, period);
      };
    });
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
  </script>

  <script>
    function callApprovedCase(gstin, circle, reportingDate, period) {
      debugger;
      $("#gstinnoap").val(gstin);
      $("#circleap").val(circle);
      $("#reportingdateap").val(reportingDate);
      $("#periodap").val(period);
      $("#actionStatusAp").val('${actionStatus}');

      $.confirm({
        title: 'Confirm!',
        content: 'Do you want to proceed ahead with approve of the case ?',
        buttons: {
          submit: function () {
            // oEvent.currentTarget.submit();

            // document.getElementById("raisequeryModal").style.display = "none";
            $('#raisequeryModal').modal('hide');
            document.getElementById("commonBoostrapAlertSuccess").style.display = "block";
            document.getElementById("caseApprovedTagLine").style.display = "block";
            setTimeout(function () {
              document.getElementById("approverRemarksDetails").submit();
            }, 300);
          },
          close: function () {
            $.alert('Canceled!');
          }
        }
      });
      //$("#closeCaseModal").modal('show');
    }

    function callRejectedCase(gstin, circle, reportingDate, period) {
      $("#gstinnoaprej").val(gstin);
      $("#circleaprej").val(circle);
      $("#reportingdateaprej").val(reportingDate);
      $("#periodaprej").val(period);
      $("#raisequeryModal").modal('show');
      $("#actionStatusRej").val('${actionStatus}');
    }
    $('#mycheckbox').change(function () {
      if (this.checked) {
        $("#checked").show();
      } else {
        $("#checked").hide();
      }
    });
  </script>
</body>
</html>