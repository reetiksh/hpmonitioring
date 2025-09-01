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

  <!-- Plugins -->
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css"/>
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css"/>
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css"/>
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css"/>
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css"/>

  <style>
    /* Keep normal layout margin; only remove it when collapsed so there’s no gap */
    .sidebar-collapse .app-main { margin-left: 0 !important; }
    @media (max-width: 991.98px) { .sidebar-open .app-main { margin-left: 0 !important; } }

    .bootstrap-select .dropdown-menu { max-height: 350px; }
  </style>
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- Preloader (optional) -->
  <div class="preloader flex-column justify-content-center align-items-center">
    <img class="animation__shake" src="/static/dist/img/AdminLTELogo.png" alt="AdminLTELogo" height="60" width="60"/>
  </div>

  <!-- Imports (use your shared header & sidebar) -->
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <!-- ====================== MAIN ====================== -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page Header -->
        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6">
              <h1 class="m-0">
                <button class="btn btn-secondary" onclick="backToSearch()">
                  <i class="fas fa-arrow-left nav-icon"></i>
                </button>
                Update/Delete User Role
              </h1>
            </div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/admin/dashboard">Home</a></li>
                <li class="breadcrumb-item"><a href="/admin/update_user_role">Update/Delete User Role</a></li>
                <li class="breadcrumb-item active">Details</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- Officer details -->
        <div class="row">
          <div class="col-12">
            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title">Officer Details <i class="fas fa-user nav-icon"></i></h3>
              </div>
              <div class="card-body">
                <div class="row g-3">
                  <div class="col-md-6">
                    <div class="form-group">
                      <label>
                        <h1 class="mb-1">
                          <c:out value='${userDetails.firstName}'/>
                          <c:out value=' ${userDetails.middleName}'/>
                          <c:out value=' ${userDetails.lastName}'/>
                        </h1>
                      </label><br/>
                      <label>Designation: <c:out value='${userDetails.designation.designationName}'/></label><br/>
                      <label>Login Id: <c:out value='${userDetails.loginName}'/></label>
                      <input type="hidden" id="loginName" name="loginName" value="<c:out value='${userDetails.loginName}'/>"/>
                    </div>
                  </div>
                  <div class="col-md-6">
                    <label>Contact Details:</label><br/>
                    <c:if test="${userDetails.mobileNumber.length() > 0}">
                      <i class="fas fa-mobile"></i><c:out value=" +91 ${userDetails.mobileNumber}"/>
                    </c:if>
                    <c:if test="${userDetails.emailId.length() > 0}">
                      <br/><i class="fas fa-envelope"></i><c:out value=" ${userDetails.emailId}"/>
                    </c:if>
                    <c:if test="${userDetails.officePhone.length() > 0}">
                      <br/><i class="fas fa-phone"></i><c:out value=" ${userDetails.officePhone}"/>
                    </c:if>
                  </div>
                </div>
              </div>
              <div class="card-footer">
                <button type="button" class="btn btn-success" id="addRole">
                  <i class="fas fa-plus nav-icon"></i> Add Role <i class="fas fa-tag nav-icon"></i>
                </button>
              </div>
            </div>

            <c:if test="${not empty successMessage}">
              <div class="alert alert-success" role="alert">${successMessage}</div>
            </c:if>
          </div>

          <!-- Add role block -->
          <div class="col-12" id="addRoleDiv" style="display:none;">
            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title">Add User Role <i class="fas fa-user-tag nav-icon"></i></h3>
              </div>
              <div class="card-body">
                <div class="row g-3 align-items-end">
                  <div class="col-md-3" id="div1">
                    <label class="form-label">Role</label>
                    <select id="userRole" name="userRole" class="form-control selectpicker"
                            data-live-search="true" title="Please Select Role" multiple>
                      <c:choose>
                        <c:when test="${!empty allUserRole}">
                          <c:forEach items="${allUserRole}" var="userRole">
                            <option value="${userRole.id}">${userRole.roleName}</option>
                          </c:forEach>
                        </c:when>
                        <c:otherwise>
                          <option value="" disabled>No Role Found</option>
                        </c:otherwise>
                      </c:choose>
                    </select>
                  </div>

                  <div class="col-md-7" id="div2">
                    <label class="form-label">Location</label>
                    <select id="locationId" name="locationId" class="form-control selectpicker"
                            data-live-search="true" title="Select Location" data-dropup-auto="false" multiple>
                      <c:choose>
                        <c:when test="${!empty locationMapping}">
                          <optgroup label="State">
                            <c:forEach items="${stateMap}" var="object">
                              <option value="${object.key}">${object.value}</option>
                            </c:forEach>
                          </optgroup>
                          <optgroup label="Zone">
                            <c:forEach items="${zoneMap}" var="object">
                              <option value="${object.key}">${object.value}</option>
                            </c:forEach>
                          </optgroup>
                          <optgroup label="Circle">
                            <c:forEach items="${circleMap}" var="object">
                              <option value="${object.key}">${object.value}</option>
                            </c:forEach>
                          </optgroup>
                        </c:when>
                        <c:otherwise>
                          <option value="" disabled>No Location Found</option>
                        </c:otherwise>
                      </c:choose>
                    </select>
                  </div>

                  <div class="col-md-2 d-grid">
                    <button type="button" class="btn btn-primary" onclick="addRoles()">
                      <i class="fas fa-check nav-icon"></i> Submit
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Errors -->
          <c:if test="${not empty errorMessage}">
            <div class="col-12 alert alert-danger alert-dismissible fade show" id="message"
                 role="alert" style="max-height:500px;overflow-y:auto;">
              <c:forEach items="${errorMessage}" var="row">
                <strong>${row}</strong><br/>
              </c:forEach>
              <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
          </c:if>

          <!-- Existing roles -->
          <c:if test="${userExistingRole.size() gt 0}">
            <div class="col-12">
              <div class="card card-primary">
                <div class="card-header">
                  <h3 class="card-title">Officer Roles <i class="fas fa-user-tag nav-icon"></i></h3>
                </div>
                <div class="card-body">
                  <table id="example1" class="table table-bordered table-striped">
                    <thead>
                    <tr>
                      <th>Role Assigned</th>
                      <th>Effected Location</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${userExistingRole}" var="role">
                      <tr>
                        <td>
                          <button type="button" class="btn btn-danger"
                                  onclick="deleteRoleLocation('Na','${role.key}')">
                            <i class="fas fa-trash nav-icon"></i>
                          </button>
                          &nbsp;<c:out value="${role.key}"/>
                        </td>
                        <td>
                          <c:forEach items="${role.value}" var="locationMap">
                            <button type="button" class="btn btn-sm"
                                    onclick="deleteRoleLocation('${locationMap.key}','${role.key}')">
                              <i class="fa fa-trash" style="color:red;"></i>
                            </button>
                            <c:out value="${locationMap.value}"/><br/>
                          </c:forEach>
                        </td>
                      </tr>
                    </c:forEach>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </c:if>
        </div>

      </div>
    </div>
  </main>

  <!-- Footer -->
  <jsp:include page="../layout/footer.jsp"/>

</div><!-- /.app-wrapper -->

<!-- =============== SCRIPTS (order matters) =============== -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<!-- Plugins -->
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script src="/static/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="/static/plugins/datatables-bs4/js/dataTables.bootstrap4.min.js"></script>
<script src="/static/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="/static/plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/dataTables.buttons.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.bootstrap4.min.js"></script>
<script src="/static/plugins/jszip/jszip.min.js"></script>
script src="/static/plugins/pdfmake/pdfmake.min.js"></script>
<script src="/static/plugins/pdfmake/vfs_fonts.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.html5.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.print.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.colVis.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  // DataTables
  $(function () {
    $("#example1").DataTable({
      responsive: true,
      lengthChange: false,
      autoWidth: false,
      buttons: ["excel","pdf","print"]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
  });

  // Toggle add role block
  document.getElementById("addRole").addEventListener("click", function(){
    const x = document.getElementById("addRoleDiv");
    x.style.display = (x.style.display === "none" || !x.style.display) ? "block" : "none";
  });

  // Back to search (preserves category & inputText)
  function backToSearch(){
    const form = document.createElement('form');
    form.method = 'get';
    form.action = "/admin/update_user_role/search_user_deatils";

    const q1 = document.createElement('input');
    q1.type = 'hidden'; q1.name = 'category'; q1.value = '${category}';

    const q2 = document.createElement('input');
    q2.type = 'hidden'; q2.name = 'inputText'; q2.value = '${inputText}';

    form.appendChild(q1); form.appendChild(q2);
    document.body.appendChild(form);
    form.submit();
  }

  // Add roles
  function addRoles(){
    let userRoles = "";
    $('#userRole :selected').each(function(_, sel){
      userRoles += (userRoles ? "," : "") + $(sel).val();
    });

    let locationIds = "";
    $('#locationId :selected').each(function(_, sel){
      locationIds += (locationIds ? "," : "") + $(sel).val();
    });

    if(!userRoles){
      $("#div1").css("box-shadow","2px 3px 5px red");
      return;
    }
    if(!locationIds){
      $("#div1").css("box-shadow","2px 3px 5px transparent");
      $("#div2").css("box-shadow","2px 3px 5px red");
      return;
    }

    const form = document.createElement('form');
    form.method = 'post';
    form.action = "/admin/update_user_role";

    const add = (n,v) => { const i=document.createElement('input'); i.type='hidden'; i.name=n; i.value=v; form.appendChild(i); };

    add('userRoles', userRoles);
    add('locationIds', locationIds);
    add('userId', '${userDetails.userId}');
    add('category', '${category}');
    add('inputText', '${inputText}');
    add('${_csrf.parameterName}', '${_csrf.token}');
    document.body.appendChild(form);

    $.confirm({
      title: 'Confirm!',
      content: 'Do you want to proceed ahead with role(s) assignment?',
      buttons: {
        submit: function(){ form.submit(); },
        close: function(){ $.alert('Canceled!'); }
      }
    });
  }

  // Delete role / location
  function deleteRoleLocation(roleMappingId, roleName){
    const form = document.createElement('form');
    form.method = 'post';
    form.action = "/admin/detele_user_role";

    const add = (n,v) => { const i=document.createElement('input'); i.type='hidden'; i.name=n; i.value=v; form.appendChild(i); };

    add('userId', '${userDetails.userId}');
    add('roleMappingId', roleMappingId);
    add('roleName', roleName);
    add('category', '${category}');
    add('inputText', '${inputText}');
    add('${_csrf.parameterName}', '${_csrf.token}');
    document.body.appendChild(form);

    $.confirm({
      title: 'Confirm!',
      content: 'Do you want to proceed ahead with role/location deletion?',
      buttons: {
        submit: function(){ form.submit(); },
        close: function(){ $.alert('Canceled!'); }
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
