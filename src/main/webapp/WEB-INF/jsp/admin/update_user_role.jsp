<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST | Update/Delete User Role</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png"/>

  <!-- AdminLTE 4 / Bootstrap 5 -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css"/>
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css"/>

  <!-- DataTables (keep your current paths) -->
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css"/>
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css"/>
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css"/>

  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css"/>

  <!-- Layout gap fix for AdminLTE 4 -->
  <style>
    :root { --app-sidebar-width: 250px; }
    .sidebar-expand-lg .app-main { margin-left:0px; transition: margin-left .3s; }
    .sidebar-collapse .app-main { margin-left: 0 !important; }
    @media (max-width: 991.98px) { .sidebar-open .app-main { margin-left: 0 !important; } }
  </style>
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- Header / Sidebar -->
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
              <h1 class="m-0">Update/Delete User Role</h1>
            </div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/admin/dashboard">Home</a></li>
                <li class="breadcrumb-item active">Update/Delete User Role</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- Card -->
        <div class="row">
          <div class="col-12">
            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title">
                  Update/Delete User Role <i class="fas fa-user-tag nav-icon"></i>
                </h3>
              </div>

              <div class="card-body">
                <!-- Search row -->
                <div class="row g-3 align-items-end">
                  <div class="col-lg-5">
                    <label class="form-label">Search Category <span class="text-danger">*</span></label>
                    <select id="category" name="category" class="form-control" title="Category">
                      <c:choose>
                        <c:when test="${!empty categories}">
                          <option value="" disabled selected>Please Select Category</option>
                          <c:forEach items="${categories}" var="categories">
                            <option value="${categories}">${categories}</option>
                          </c:forEach>
                        </c:when>
                        <c:otherwise>
                          <option value="" selected>No category Found</option>
                        </c:otherwise>
                      </c:choose>
                    </select>
                  </div>

                  <div class="col-lg-5">
                    <label class="form-label">Search Text</label>
                    <input type="text" class="form-control" id="inputText" placeholder="Please Enter Search Details">
                  </div>

                  <div class="col-lg-2">
                    <button type="button" class="btn btn-primary w-100" onclick="submitSearch()">
                      <i class="fa fa-search me-1" aria-hidden="true"></i> Search
                    </button>
                  </div>
                </div>

                <div class="mt-3">
                  <span id="error_message" class="text-danger"><c:out value='${error_message}'/></span>
                </div>

                <!-- Results -->
                <c:if test="${!empty userList}">
                  <div class="table-responsive mt-3">
                    <table id="example1" class="table table-bordered table-striped align-middle">
                      <thead>
                      <tr>
                        <th>Officer's Name</th>
                        <th>Designation</th>
                        <th>Login Id</th>
                        <th>Contact Details</th>
                        <th>Role Tag <i class="fas fa-tags"></i></th>
                        <th style="width:90px;">Action</th>
                      </tr>
                      </thead>
                      <tbody>
                      <c:forEach items="${userList}" var="object">
                        <tr>
                          <td>
                            <c:out value="${object.key.firstName}"/> <c:out value="${object.key.middleName}"/> <c:out value="${object.key.lastName}"/>
                          </td>
                          <td><c:out value="${object.key.designation.designationName}"/></td>
                          <td><c:out value="${object.key.loginName}"/></td>
                          <td>
                            <c:if test="${object.key.mobileNumber.length() > 0}">
                              <i class="fas fa-mobile"></i> <c:out value=" +91 ${object.key.mobileNumber}"/>
                            </c:if>
                            <c:if test="${object.key.emailId.length() > 0}">
                              <br><i class="fas fa-envelope"></i> <c:out value=" ${object.key.emailId}"/>
                            </c:if>
                            <c:if test="${object.key.officePhone.length() > 0}">
                              <br><i class="fas fa-phone"></i> <c:out value=" ${object.key.officePhone}"/>
                            </c:if>
                          </td>
                          <td>
                            <c:forEach items="${object.value}" var="roleMapObject">
                              <c:out value="${roleMapObject.key} : ${roleMapObject.value}"/><br/>
                            </c:forEach>
                          </td>
                          <td class="text-center">
                            <button type="button" class="btn btn-info"
                                    onclick="window.location.href='/admin/update_user_role?loginName=${object.key.loginName}&category=${category}&inputText=${inputText}'">
                              <i class="fas fa-edit nav-icon"></i>
                            </button>
                          </td>
                        </tr>
                      </c:forEach>
                      </tbody>
                    </table>
                  </div>
                </c:if>
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

<!-- ===== SCRIPTS (order) ===== -->
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

<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  $(function () {
    // DataTable
    $("#example1").DataTable({
      responsive: true,
      lengthChange: true,
      autoWidth: false,
      buttons: ["excel","pdf","print"]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
  });

  function submitSearch(){
    const category = $("#category").val();
    const inputText = $("#inputText").val();

    if(!category){
      $("#category").css("box-shadow","0 0 0 .25rem rgba(220,53,69,.25)");
      return;
    }
    if(!inputText){
      $("#category").css("box-shadow","none");
      $("#inputText").css("box-shadow","0 0 0 .25rem rgba(220,53,69,.25)");
      return;
    }

    const form = document.createElement('form');
    form.method = 'GET';
    form.action = '/admin/update_user_role/search_user_deatils';

    const in1 = document.createElement('input');
    in1.type = 'hidden'; in1.name = 'category'; in1.value = category;

    const in2 = document.createElement('input');
    in2.type = 'hidden'; in2.name = 'inputText'; in2.value = inputText;

    form.appendChild(in1); form.appendChild(in2);
    document.body.appendChild(form);
    form.submit();
  }

  // hardening
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
