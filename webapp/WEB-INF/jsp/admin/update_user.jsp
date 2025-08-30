<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST | Update User</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png"/>

  <!-- AdminLTE 4 / Bootstrap 5 -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css"/>
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css"/>

  <!-- DataTables -->
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css"/>
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css"/>
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css"/>

  <!-- Extras -->
  <link rel="stylesheet" href="/static/dist/css/sweetalert2.min.css"/>
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css"/>

  <!-- Fix the left gap when sidebar is collapsed/open -->
  <style>
      :root{ --hp-sidebar-width: 210px; }
    .app-sidebar{ width: var(--hp-sidebar-width) !important; }
    .app-main{ margin-left: 0px !important; transition: margin-left .2s ease; }
    body.sidebar-collapse .app-main{ margin-left: 0 !important; }
    @media (max-width: 991.98px){ .app-main{ margin-left: 0 !important; } }
  </style>
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- Optional preloader -->
  <div class="preloader flex-column justify-content-center align-items-center">
    <img class="animation__shake" src="/static/dist/img/AdminLTELogo.png" alt="AdminLTELogo" height="60" width="60"/>
  </div>

  <!-- Imports (no hardcoding) -->
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <!-- ================= MAIN ================= -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page header -->
        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6">
              <h1 class="m-0">Update User</h1>
            </div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/admin/dashboard">Home</a></li>
                <li class="breadcrumb-item active">Update User</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- Users table -->
        <div class="row">
          <div class="col-12">
            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title">Update User</h3>
              </div>
              <div class="card-body">
                <table id="example1" class="table table-bordered table-striped align-middle">
                  <thead>
                    <tr>
                      <th>Officer's Name</th>
                      <th>Designation</th>
                      <th>Login Id</th>
                      <th>Mobile</th>
                      <th>Email</th>
                      <th>User Status</th>
                      <th style="width: 90px;">Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach items="${userList}" var="object">
                      <tr>
                        <td><c:out value="${object.firstName}"/> <c:out value="${object.middleName}"/> <c:out value="${object.lastName}"/></td>
                        <td><c:out value="${object.designation.designationName}"/></td>
                        <td><c:out value="${object.loginName}"/></td>
                        <td><c:out value="${object.mobileNumber}"/></td>
                        <td><c:out value="${object.emailId}"/></td>
                        <td><c:out value="${object.userStatus}"/></td>
                        <td class="text-center">
                          <button type="button" title="Edit User" class="btn btn-info" onclick="updateUser('${object.userId}')">
                            <i class="fas fa-edit nav-icon"></i>
                          </button>
                        </td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>
              </div>
            </div>

            <c:if test="${not empty successMessage}">
              <div class="alert alert-success" role="alert">
                ${successMessage}
              </div>
            </c:if>
          </div>
        </div>

      </div>
    </div>
  </main>

  <!-- Footer -->
  <jsp:include page="../layout/footer.jsp"/>

</div><!-- /.app-wrapper -->

<!-- ===== SCRIPTS (order matters) ===== -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<!-- DataTables -->
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

<!-- Extras -->
<script src="/static/dist/js/jquery-confirm.min.js"></script>
<script src="/static/dist/js/sweetalert2.all.min.js"></script>

<script>
  // DataTable
  $(function () {
    $("#example1").DataTable({
      responsive: true,
      lengthChange: false,
      autoWidth: false,
      buttons: ["excel","pdf","print"]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
  });

  // Edit action + CSRF
  function updateUser(userId) {
    const form = document.createElement('form');
    form.method = 'post';
    form.action = '/admin/update_user';

    const inUser = document.createElement('input');
    inUser.type = 'hidden'; inUser.name = 'userId'; inUser.value = userId;

    const inCsrf = document.createElement('input');
    inCsrf.type = 'hidden'; inCsrf.name = '${_csrf.parameterName}'; inCsrf.value = '${_csrf.token}';

    form.appendChild(inUser);
    form.appendChild(inCsrf);
    document.body.appendChild(form);

    $.confirm({
      title: 'Confirm!',
      content: 'Do you want to proceed ahead with user details updation?',
      buttons: {
        submit: function () { form.submit(); },
        close: function () { $.alert('Canceled!'); }
      }
    });
  }

  // Basic hardening
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
  $(document).ready(function () {
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack;
    window.onpageshow = evt => { if (evt.persisted) disableBack(); };
  });
</script>
</body>
</html>
