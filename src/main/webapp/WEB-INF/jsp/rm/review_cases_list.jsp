<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Acknowledge Case List</title>

  <!-- AdminLTE 4 / Bootstrap 5 / Font Awesome -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.5.2/css/all.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/css/adminlte.min.css" rel="stylesheet"/>

  <!-- DataTables (Bootstrap 5 build + Buttons + Responsive) -->
  <link href="https://cdn.datatables.net/v/bs5/dt-2.0.8/r-3.0.2/b-3.0.2/datatables.min.css" rel="stylesheet"/>

  <!-- bootstrap-select (BS5-compatible) -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.14.0-beta3/dist/css/bootstrap-select.min.css"/>

  <!-- jquery-confirm (kept) -->
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css"/>

  <style>
    .nav-tabs .nav-item.show .nav-link, .nav-tabs .nav-link.active {
      background-color: #0d6efd;
      color: #fff;
    }
    .table-responsive { overflow: scroll; max-height: 800px; }
  </style>
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/confirmation_popup.jsp"/>
  <jsp:include page="../fo/transfer_popup.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Header -->
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Review Meeting Comments</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-end">
              <li class="breadcrumb-item"><a>Home</a></li>
              <li class="breadcrumb-item active">Review Cases List View</li>
            </ol>
          </div>
        </div>

        <!-- Card -->
        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title">Review Cases List View</h3>
          </div>

          <div class="card-body">
            <c:if test="${not empty message}">
              <div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
                <strong>${message}</strong>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
              </div>
            </c:if>

            <div class="card-body">
              <div class="row">
                <div class="col-md-12">
                  <div class="form-group">

                    <!-- Tabs (BS5 uses data-bs-toggle; legacy compat shim below keeps data-toggle working too) -->
                    <ul class="nav nav-tabs" role="tablist">
                      <li class="nav-item">
                        <a class="nav-link active" data-bs-toggle="tab" data-toggle="tab" href="#home">Category wise Review Meeting</a>
                      </li>
                      <li class="nav-item">
                        <a class="nav-link" data-bs-toggle="tab" data-toggle="tab" href="#menu1">Detailed Enforcement Case wise Review Meeting</a>
                      </li>
                    </ul>

                  </div>
                </div>
              </div>

              <form method="POST" action="update_category_remarks" id="category_remarks" name="category_remarks" enctype="multipart/form-data">
                <div class="container-fluid">
                  <div class="row">
                    <div class="col-sm-12">
                      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                      <div class="tab-content">

                        <!-- Tab 1 -->
                        <div id="home" class="tab-pane fade show active">
                          <br/>
                          <div class="row g-3">
                            <div class="col-md-4">
                              <div class="form-group">
                                <label for="reviewMeetingDateId">Review Meeting Date</label>
                                <input type="date" class="form-control" id="reviewMeetingDateId" onchange="reviewMeetingsDate(this.value)" name="reviewMeetingDate" required/>
                              </div>
                            </div>
                            <div class="col-md-4">
                              <div class="form-group">
                                <label for="meetingDocumentId">MoM Document</label>
                                <input type="file" class="form-control" id="meetingDocumentId" name="meetingDocument" required/>
                              </div>
                            </div>
                          </div>

                          <br/>
                          <table id="examaple" class="table table-bordered table-striped">
                            <thead>
                              <tr>
                                <th style="text-align:center; width: 15%; vertical-align: middle;">Category</th>
                                <th style="text-align:center; width: 20%; vertical-align: middle;">Previous Review Meeting Comments</th>
                                <th style="text-align:center; width: 30%; vertical-align: middle;">Review Meeting Comments</th>
                              </tr>
                            </thead>
                            <tbody>
                              <c:forEach items="${list}" var="obj">
                                <tr>
                                  <td>${obj.category}</td>
                                  <td>
                                    <textarea id="categoryoldremarks" name="categoryoldremarks" style="width:100%;background-color:#e9ecef;" readonly>${obj.remarks}</textarea>
                                  </td>
                                  <td>
                                    <textarea id="categoryremarks" name="categoryremarks" style="width:100%;"></textarea>
                                  </td>
                                </tr>
                              </c:forEach>
                            </tbody>
                          </table>
                          <br/>
                        </div>

                        <!-- Tab 2 -->
                        <div id="menu1" class="tab-pane fade"><br/>
                          <table id="example1" class="table table-bordered table-striped table-responsive w-100">
                            <thead>
                              <tr>
                                <th style="text-align:center; width:7%; vertical-align: middle;">GSTIN</th>
                                <th style="text-align:center; width:7%; vertical-align: middle;">Taxpayer Name</th>
                                <th style="text-align:center; width:7%; vertical-align: middle;">Jurisdiction</th>
                                <th style="text-align:center; width:7%; vertical-align: middle;">Category</th>
                                <th style="text-align:center; width:7%; vertical-align: middle;">Reporting Date</th>
                                <th style="text-align:center; width:7%; vertical-align: middle;">Indicative Value(₹)</th>
                                <th style="text-align:center; width:7%; vertical-align: middle;">Action Status</th>
                                <th style="text-align:center; width:7%; vertical-align: middle;">Case Stage</th>
                                <th style="text-align:center; width:7%; vertical-align: middle;">Assigned To</th>
                                <th style="text-align:center; width:7%; vertical-align: middle;">Amount(₹)</th>
                                <th style="text-align:center; width:7%; vertical-align: middle;">Recovery Stage</th>
                                <th style="text-align:center; width:8%; vertical-align: middle;">Recovery by DRC-3(₹)</th>
                                <th style="text-align:center; width:7%; vertical-align: middle;">Recovery Against Demand(₹)</th>
                                <th style="text-align:center; width:7%; vertical-align: middle;">Previous Review Meeting Comments</th>
                                <th style="text-align:center; width:13%; vertical-align: middle;">Review Meeting Comments</th>
                              </tr>
                            </thead>
                            <tbody>
                              <c:forEach items="${gstlist}" var="obj">
                                <tr>
                                  <td>${obj.GSTIN_ID}</td>
                                  <input type="hidden" name="gstin" value="${obj.GSTIN_ID}">
                                  <td>${obj.taxpayerName}</td>
                                  <td>${obj.circle}</td>
                                  <td>${obj.category}</td>
                                  <input type="hidden" name="catogy" value="${obj.categoryId}">
                                  <td><fmt:formatDate value="${obj.date}" pattern="dd-MM-yyyy"/></td>
                                  <input type="hidden" name="caseReportingDateId" value="${obj.caseReportingDate_ID}">
                                  <td>${obj.indicativeTaxVal}</td>
                                  <input type="hidden" name="period" value="${obj.period_ID}">
                                  <td>${obj.actionStatusName}</td>
                                  <td>${obj.caseStageName}</td>
                                  <td>
                                    <c:choose>
                                      <c:when test="${obj.assignedTo eq 'HQ'}">Head Office</c:when>
                                      <c:when test="${obj.assignedTo eq 'FO'}">Field Office</c:when>
                                      <c:when test="${obj.assignedTo eq 'RU'}">Reviewer</c:when>
                                      <c:when test="${obj.assignedTo eq 'AP'}">Approver</c:when>
                                    </c:choose>
                                  </td>
                                  <td>${obj.demand}</td>
                                  <td>${obj.recoveryStageName}</td>
                                  <td>${obj.recoveryByDRC3}</td>
                                  <td>${obj.recoveryAgainstDemand}</td>
                                  <td><textarea name="oldremarks" style="width:100%;background-color:#e9ecef;" readonly>${obj.remarks}</textarea></td>
                                  <td><textarea name="remarks" style="width:100%;"></textarea></td>
                                </tr>
                              </c:forEach>
                            </tbody>
                          </table>
                          <br/><br/>
                        </div>

                      </div>
                    </div>
                  </div>
                </div>

                <div class="row">
                  <div class="col-md-12">
                    <div class="form-group">
                      <button type="submit" class="btn btn-primary" id="submitBtn" style="float:right;">Submit</button>
                    </div>
                  </div>
                </div>
              </form>

            </div>
          </div>
        </div>

      </div>
    </div>
  </main>

  <jsp:include page="../layout/footer.jsp"/>

</div>

<!-- Acknowledge Modal (BS5 markup) -->
<div class="modal fade" id="confirmationModal" tabindex="-1" aria-labelledby="confirmationModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <form method="POST" action="fo_acknowledge_cases" name="acknowledgeForm">
        <div class="modal-header">
          <h5 class="modal-title" id="confirmationModalTitle">Are you sure want to acknowledge ?</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <input type="hidden" id="gstno_acknowledge" name="gstno">
        <input type="hidden" id="date_acknowledge" name="date">
        <input type="hidden" id="period_acknowledge" name="period">

        <div class="modal-footer">
          <button type="submit" class="btn btn-primary" id="okayBtn">Okay</button>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" id="cancelBtn">Cancel</button>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- JS: jQuery -> Bootstrap 5 -> AdminLTE 4 -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/js/adminlte.min.js"></script>

<!-- DataTables bundle (DT + Responsive + Buttons + JSZip + pdfmake) -->
<script src="https://cdn.datatables.net/v/bs5/dt-2.0.8/r-3.0.2/b-3.0.2/jszip-3.10.1/pdfmake-0.2.7/vfs_fonts-0.1.0/datatables.min.js"></script>

<!-- bootstrap-select (BS5) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.14.0-beta3/dist/js/bootstrap-select.min.js"></script>

<!-- jquery-confirm -->
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  // Original UX/security handlers (unchanged)
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    if ((e.ctrlKey && e.key === 'u') || e.key === 'F12') e.preventDefault();
  });
  $(document).ready(function () {
    function disableBack(){ window.history.forward() }
    window.onload = disableBack();
    window.onpageshow = evt => { if (evt.persisted) disableBack() };
  });
  document.onkeydown = function (e) {
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) e.preventDefault();
  };
</script>

<script>
  // Bootstrap 5 shims: keep your existing jQuery modal/tab triggers and legacy data-dismiss working
  (function($){
    // Shim $.fn.modal('show'|'hide')
    if (!$.fn.modal) {
      $.fn.modal = function(action){
        return this.each(function(){
          const inst = bootstrap.Modal.getOrCreateInstance(this);
          if (action === 'show') inst.show();
          else if (action === 'hide') inst.hide();
        });
      };
    }
    // Enable legacy data-toggle="tab"
    $(document).on('click','[data-toggle="tab"]', function(e){
      e.preventDefault();
      const tab = new bootstrap.Tab(this);
      tab.show();
    });
    // Make legacy data-dismiss="modal" buttons close BS5 modals
    document.addEventListener('click', function(e){
      const btn = e.target.closest('[data-dismiss="modal"]');
      if (!btn) return;
      e.preventDefault();
      const modalEl = btn.closest('.modal');
      if (modalEl) bootstrap.Modal.getOrCreateInstance(modalEl).hide();
    });
  })(jQuery);
</script>

<script>
  // Flash message behavior
  $(function() {
    $("#message").fadeTo(2000, 500).slideUp(500, function() { $("#message").slideUp(500); });
  });

  // DataTables init (logic preserved)
  $(function () {
    $("#example1").DataTable({
      responsive: true,
      lengthChange: false,
      autoWidth: false,
      buttons: ["excel","pdf","print"]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');

    // If you ever use #example2 again:
    $('#example2').DataTable({
      paging: true,
      lengthChange: false,
      searching: false,
      ordering: true,
      info: true,
      autoWidth: false,
      responsive: true
    });
  });

  // Modal open helpers (unchanged)
  function acknowledge(gstno , date , period){
    $("#gstno_acknowledge").val(gstno);
    $("#date_acknowledge").val(date);
    $("#period_acknowledge").val(period);
    $("#confirmationModal").modal('show');
  }
  function transferBtn(gstno , date , period){
    $("#gstno").val(gstno);
    $("#date").val(date);
    $("#period").val(period);
    $("#transferModal").modal('show');
  }

  // Form confirm dialog (unchanged)
  $('form').on('submit', function(oEvent) {
    oEvent.preventDefault();
    $.confirm({
      title : 'Confirm!',
      content : 'Do you want to proceed ahead with submission of review meeting comments?',
      buttons : {
        submit : function() { oEvent.currentTarget.submit(); },
        close  : function() { $.alert('Canceled!'); }
      }
    });
  });

  // Date guard (unchanged)
  function reviewMeetingsDate(val){
    var currentDate = new Date();
    var timestamp = Date.parse(val);
    var dateObject = new Date(timestamp);
    if(dateObject > currentDate){
      alert("Review Meeting date can not be greater than current date");
      $("#reviewMeetingDateId").val("");
    }
  }
  window.reviewMeetingsDate = reviewMeetingsDate; // keep global for inline onchange
</script>

</body>
</html>
