<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>User Details</title>

  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png"/>

  <!-- AdminLTE 4 / Bootstrap 5 / Font Awesome 6 -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.5.2/css/all.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/css/adminlte.min.css" rel="stylesheet"/>

  <!-- DataTables (Bootstrap 5) -->
  <link href="https://cdn.datatables.net/v/bs5/dt-2.0.8/r-3.0.2/b-3.0.2/datatables.min.css" rel="stylesheet"/>

  <!-- jquery-confirm (unchanged logic) -->
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css"/>

  <style>
    :root { --hp-sidebar-width: 250px; }
    .app-sidebar { width: var(--hp-sidebar-width) !important; }
    @media (min-width: 992px) {
      body.sidebar-expand-lg:not(.sidebar-collapse) .app-wrapper .app-main {
        margin-left: var(--hp-sidebar-width);
      }
      body.sidebar-collapse .app-wrapper .app-main { margin-left: 0 !important; }
    }
    @media (max-width: 991.98px) {
      .app-wrapper .app-main { margin-left: 0 !important; }
      body.sidebar-open .app-wrapper .app-main { margin-left: 0 !important; }
    }
  </style>
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>User Details</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-end">
              <li class="breadcrumb-item"><a href="/welcome">Home</a></li>
              <li class="breadcrumb-item active">User Details</li>
            </ol>
          </div>
        </div>

        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title">User Details</h3>
          </div>

          <!-- alerts (unchanged ids/content) -->
          <div class="alert alert-success d-none" role="alert" id="commonBoostrapAlertSuccess">
            <div class="d-none" id="caseRecommendedTagLine">Case Recommended Successfully !</div>
          </div>
          <div class="alert alert-success d-none" role="alert" id="commonBoostrapAlertFail">
            <div class="d-none" id="raisedQueryTagLine">Query Raised Successfully !</div>
          </div>
          <div class="alert alert-success d-none" role="alert" id="caseAppealedAlertSuccess">
            <div class="d-none" id="caseAppealedTagLine">Case recommended for appeal successfully !</div>
          </div>
          <div class="alert alert-success d-none" role="alert" id="caseRevisionAlertSuccess">
            <div class="d-none" id="caseRevisionTagLine">Case recommended for revision successfully !</div>
          </div>

          <div class="card-body">
            <table id="example1" class="table table-bordered table-striped w-100">
              <thead>
              <tr>
                <th style="text-align: center; vertical-align: middle;">User Name</th>
                <th style="text-align: center; vertical-align: middle;">Mob. no.</th>
                <th style="text-align: center; vertical-align: middle;">Designation</th>
                <th style="text-align: center; vertical-align: middle;">Email Id</th>
                <th style="text-align: center; vertical-align: middle;">DOB</th>
                <th style="text-align: center; vertical-align: middle;">Assigned Roles</th>
                <th style="text-align: center; vertical-align: middle;">Working Locations</th>
              </tr>
              </thead>
              <tbody>
              <tr>
                <td><c:out value="${userProfileDetails.userName}" /></td>
                <td><c:out value="${userProfileDetails.mob}" /></td>
                <td><c:out value="${userProfileDetails.designation}" /></td>
                <td><c:out value="${userProfileDetails.emailId}" /></td>
                <td><fmt:formatDate value="${userProfileDetails.dob}" pattern="dd-MM-yyyy" /></td>
                <td><c:out value="${userProfileDetails.assignedRoles}" /></td>
                <td>
                  <c:forEach items="${userRoleMapWithLocations}" var="userRoleMapWithLocations">
                    <c:out value="${userRoleMapWithLocations}" /><br/>
                  </c:forEach>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>

      </div>
    </div>
  </main>

  <jsp:include page="../layout/footer.jsp"/>
</div>

<!-- Modal (converted to BS5 markup; ids/logic preserved) -->
<div class="modal fade" id="closeCaseModal" tabindex="-1" aria-labelledby="closeCaseModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div>
        <div class="modal-header">
          <h5 class="modal-title" id="confirmationModalTitle">Do you want to proceed ahead with case recommendation ?</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <div class="modal-body">
          <p>I hereby undertake that;</p>
          <p><i class="fa fa-check" aria-hidden="true"></i> All the points raised by EIU pertaining to this Taxpayer have been dealt carefully in the light of the provisions of the GST laws</p>
          <p><i class="fa fa-check" aria-hidden="true"></i> Applicable Tax has been levied as per the provisions of GST acts/rules 2017</p>
          <p><i class="fa fa-check" aria-hidden="true"></i> Applicable Interest has been levied as per the provisions of GST acts/rules 2017</p>
          <p><i class="fa fa-check" aria-hidden="true"></i> Applicable Penalty has been levied as per the provisions of GST acts/rules 2017</p>
          <p><input type="checkbox" id="mycheckbox" name="checkbox"> I hereby declare that above information is true and correct to the best of my knowledge</p>
        </div>

        <input type="hidden" id="gstno" name="gstno">
        <input type="hidden" id="date" name="date">
        <input type="hidden" id="period" name="period">

        <div class="modal-footer">
          <div id="checked" style="display:none">
            <button onclick="submitVerifierDeclaration()" class="btn btn-primary" id="okayBtn">Okay</button>
          </div>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" id="closeBtn">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- JS: jQuery -> Bootstrap 5 -> AdminLTE 4 -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/js/adminlte.min.js"></script>

<!-- DataTables (Bootstrap 5) -->
<script src="https://cdn.datatables.net/v/bs5/dt-2.0.8/r-3.0.2/b-3.0.2/jszip-3.10.1/pdfmake-0.2.7/vfs_fonts-0.1.0/datatables.min.js"></script>

<!-- jquery-confirm (kept) -->
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<!-- jQuery ↔ Bootstrap 5 Modal bridge (keeps $('#id').modal('show') working) -->
<script>
  (function ($) {
    $.fn.modal = function (action) {
      return this.each(function () {
        const instance = bootstrap.Modal.getOrCreateInstance(this);
        if (action === 'show') instance.show();
        else if (action === 'hide') instance.hide();
      });
    };
  })(jQuery);
</script>

<!-- Sidebar toggle for ALTE4 (kept your logic) -->
<script>
  (function () {
    const mqDesktop = window.matchMedia('(min-width: 992px)');
    function toggleSidebar() {
      if (mqDesktop.matches) {
        document.body.classList.toggle('sidebar-collapse');
      } else {
        document.body.classList.toggle('sidebar-open');
      }
    }
    document.addEventListener('click', function (e) {
      const btn = e.target.closest('[data-lte-toggle="sidebar"], [data-widget="pushmenu"]');
      if (!btn) return;
      e.preventDefault();
      toggleSidebar();
    });
  })();
</script>

<!-- DataTable init (dataset/logic unchanged) -->
<script>
  $(function () {
    $("#example1").DataTable({
      responsive: true,
      lengthChange: false,
      autoWidth: false,
      buttons: ["excel","pdf","print"]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
  });
</script>

<!-- Security/UX handlers (unchanged behavior) -->
<script>
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
  $(document).ready(function () {
    function disableBack() { window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = function (evt) { if (evt.persisted) disableBack(); }
  });
</script>
</body>
</html>
