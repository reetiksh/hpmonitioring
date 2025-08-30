<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST | Create User</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png"/>

  <!-- AdminLTE 4 / Bootstrap 5 -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">

  <!-- Plugins -->
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="/static/plugins/select2/select2.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">

  <style>
    .select2-selection__rendered { line-height: 31px !important; }
    .select2-container .select2-selection--single { height: 36px !important; }
    .select2-selection__arrow { height: 34px !important; }
    div[title]:hover::after { background-color: red; }
  </style>
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- Header & Sidebar -->
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <!-- ====================== MAIN ====================== -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page header -->
        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6">
              <h1 class="m-0">Create User</h1>
            </div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/admin/dashboard">Home</a></li>
                <li class="breadcrumb-item active">Create User</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- Form card -->
        <div class="row">
          <div class="col-12">
            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title mb-0">Create User <i class="fas fa-user-plus nav-icon"></i></h3>
              </div>

              <form method="POST" id="userDetails" action="<c:url value='/admin/create_user' />" enctype="multipart/form-data">
                <div class="card-body">
                  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                  <div id="errorMessage" class="mb-2 text-danger"></div>

                  <div class="row g-3">
                    <div class="col-md-6">
                      <div class="form-group" title="Login Id should be 8-30 chars [A-Z, a-z, 0-9 and . or _]">
                        <label class="form-label">Login Id <span class="text-danger">*</span>
                          <span id="loginId_alert" class="ms-1"></span>
                        </label>
                        <input type="text" class="form-control" id="loginName" name="loginName"
                               value="<c:out value='${HqUploadForm.extensionNo}'/>"
                               onkeyup="validate_loginId()" placeholder="Please Enter Login Id" required>
                      </div>
                    </div>

                    <div class="col-md-6">
                      <label class="form-label">Mobile Number <span class="text-danger">*</span>
                        <span id="mobileNumber_error" class="ms-1"></span>
                      </label>
                      <input type="number" class="form-control" id="mobileNumber" name="mobileNumber"
                             value="<c:out value='${HqUploadForm.extensionNo}'/>"
                             onKeyPress="if(this.value.length==10) return false;"
                             onkeyup="validate_mobileNumber()"
                             placeholder="Please Enter Mobile Number" required>
                    </div>

                    <div class="col-md-6">
                      <label class="form-label">First Name <span class="text-danger">*</span>
                        <span id="firstName_error" class="ms-1"></span>
                      </label>
                      <input type="text" class="form-control" id="firstName" name="firstName"
                             value="<c:out value='${HqUploadForm.extensionNo}'/>"
                             maxlength="40" placeholder="Please Enter First Name"
                             onkeydown="return /[a-z]/i.test(event.key)" required>
                    </div>

                    <div class="col-md-6">
                      <label class="form-label">Middle Name <span id="middleName_error" class="ms-1"></span></label>
                      <input type="text" class="form-control" id="middleName" name="middleName"
                             value="<c:out value='${HqUploadForm.extensionNo}'/>"
                             maxlength="40" placeholder="Please Enter Middle Name"
                             onkeydown="return /[a-z]/i.test(event.key)">
                    </div>

                    <div class="col-md-6">
                      <label class="form-label">Last Name <span id="lastName_error" class="ms-1"></span></label>
                      <input type="text" class="form-control" id="lastName" name="lastName"
                             value="<c:out value='${HqUploadForm.extensionNo}'/>"
                             maxlength="40" placeholder="Please Enter Last Name"
                             onkeydown="return /[a-z]/i.test(event.key)">
                    </div>

                    <div class="col-md-6">
                      <label class="form-label">Email Id <span class="text-danger">*</span>
                        <span id="emailId_error" class="ms-1"></span>
                      </label>
                      <input type="email" class="form-control" id="emailId" name="emailId"
                             value="<c:out value='${HqUploadForm.extensionNo}'/>" maxlength="50"
                             placeholder="Please Enter Email id" onkeyup="validate_emailId()" required>
                    </div>

                    <div class="col-md-6">
                      <label class="form-label">Date Of Birth <span class="text-danger">*</span>
                        <span id="dob_error" class="ms-1"></span>
                      </label>
                      <input type="date" class="form-control" id="dob" name="dob"
                             value="<c:out value='${HqUploadForm.extensionNo}'/>" required>
                    </div>

                    <div class="col-md-6">
                      <label class="form-label">Designation <span class="text-danger">*</span>
                        <span id="designation_error" class="ms-1"></span>
                      </label>
                      <select class="form-control" id="designation" name="designation" required>
                        <option value="" disabled selected>Select Designation</option>
                        <c:forEach items="${designationList}" var="designation">
                          <option value="${designation.designationId}">
                            ${designation.designationName}
                          </option>
                        </c:forEach>
                      </select>
                    </div>
                  </div>

                  <div class="mt-3">
                    <c:forEach items="${HqUploadForm.excelErrors}" var="excelError">
                      <div class="text-danger"><c:out value="${excelError}" /></div>
                    </c:forEach>
                  </div>
                </div>

                <div class="card-footer d-flex gap-2">
                  <button type="button" class="btn btn-primary" onclick="submitForm()">Submit</button>
                  <button type="button" id="resetBtn" class="btn btn-secondary">Clear</button>
                </div>
              </form>
            </div>

            <!-- flash messages -->
            <c:if test="${not empty successMessage}">
              <div class="col-12 alert alert-success alert-dismissible fade show" id="message" role="alert">
                <strong>${successMessage}</strong><br>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
              </div>
            </c:if>
            <c:if test="${not empty errorMessage}">
              <div class="col-12 alert alert-danger alert-dismissible fade show" id="message" role="alert">
                <strong>${errorMessage}</strong><br>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
              </div>
            </c:if>

          </div>
        </div>

      </div>
    </div>
  </main>

  <jsp:include page="../layout/footer.jsp"/>
</div><!-- /.app-wrapper -->

<!-- ====================== SCRIPTS (order matters) ====================== -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/plugins/bs-custom-file-input/bs-custom-file-input.min.js"></script>
<script src="/static/plugins/select2/select2.min.js"></script>
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<script>
  // Flash fade
  $(function() {
    $("#message").fadeTo(5000, 0.5).slideUp(500, function(){ $(this).remove(); });
  });

  // Hardening
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
  (function disableBFCache(){
    function f(){ window.history.forward(); }
    window.onload = f;
    window.onpageshow = evt => { if (evt.persisted) f(); };
  })();

  // Plugins
  $(function(){ bsCustomFileInput.init(); });
  $('select').select2();

  // ===== Validation & submission =====
  function submitForm(){
    const MobileRegex = /^\d{10}$/;
    const EmailRegex  = /^[\w.-]+@([\w-]+\.)+[\w-]{2,4}$/;
    const loginNameRegex = /^(?=.*[a-zA-Z])(?=.*[._])[A-Za-z0-9._]{8,30}$/;

    const loginName1 = $("#loginName").val();
    const emailId1   = $("#emailId").val();

    clear_all_error();
    validate_loginId_OnSubmition();
    validate_emailId_OnSubmition();

    const loginName    = $("#loginName").val();
    const mobileNumber = $("#mobileNumber").val();
    const firstName    = $("#firstName").val();
    const middleName   = $("#middleName").val();
    const lastName     = $("#lastName").val();
    const emailId      = $("#emailId").val();
    const designation  = $("#designation").val();
    const dob          = $("#dob").val();

    if (loginName1 !== loginName) return;
    if (emailId1   !== emailId)   return;

    if (!loginName) {
      $("#loginId_alert").html("Please enter a login Id !").css("color","red"); return;
    }
    if (!loginNameRegex.test(loginName) || loginName.length > 30) {
      $("#loginId_alert").html("Please enter a valid login Id !").css("color","red"); return;
    }

    if (!mobileNumber || mobileNumber.length !== 10 || !MobileRegex.test(mobileNumber) ||
        /^(000+|111+|222+|333+|444+|555+|666+|777+|888+|999+)$/.test(mobileNumber)) {
      $("#mobileNumber_error").html("Please enter a valid mobile number !").css("color","red");
      $("#mobileNumber").val(''); return;
    }

    if (!firstName) { $("#firstName_error").html("Please enter first name !").css("color","red"); return; }
    if (firstName.length > 40) { $("#firstName_error").html("Please enter valid first name !").css("color","red"); return; }
    if (middleName.length > 40) { $("#middleName_error").html("Please enter valid middle name !").css("color","red"); return; }
    if (lastName.length > 40) { $("#lastName_error").html("Please enter valid last name !").css("color","red"); return; }

    if (!emailId || emailId.length > 50 || !EmailRegex.test(emailId)) {
      $("#emailId_error").html("Please enter a valid email id !").css("color","red");
      $("#emailId").val(''); return;
    }

    if (!dob) { $("#dob_error").html("Please enter a valid Date of Birth !").css("color","red"); return; }

    if (!designation || designation.length > 40) {
      $("#designation_error").html("Please select designation").css("color","red"); return;
    }

    $("#userDetails").submit();
  }

  function clear_all_error(){
    $("#firstName_error, #middleName_error, #lastName_error, #mobileNumber_error, #emailId_error, #dob_error, #designation_error").html("");
  }

  function validate_loginId(){
    const loginName = $("#loginName").val();
    $.ajax({
      url: "/admin/checkNewLoginId?loginName="+encodeURIComponent(loginName),
      success: function(result){
        if (result === 'true') {
          $("#loginId_alert").html("☒ Login Id '"+ loginName +"' already exists. Please enter a new login id.")
                              .css("color","red");
        } else if (loginName.length>0) {
          $("#loginId_alert").html("");
        }
      }
    });
  }

  function validate_loginId_OnSubmition(){
    const loginName = $("#loginName").val();
    $.ajax({
      url: "/admin/checkNewLoginId?loginName="+encodeURIComponent(loginName),
      async: false,
      success: function(result){
        if (result === 'true') {
          $("#loginId_alert").html("☒ Login Id '"+ loginName +"' already exists. Please enter a new login id.")
                              .css("color","red");
          $("#loginName").val('');
        }
      }
    });
  }

  function validate_emailId(){
    const emailId = $("#emailId").val();
    $.ajax({
      url: "/admin/checkNewEmailId?emailId="+encodeURIComponent(emailId),
      success: function(result){
        if (result === 'true') {
          $("#emailId_error").html("☒ Email Id '"+ emailId +"' already used").css("color","red");
        } else {
          $("#emailId_error").html("");
        }
      }
    });
  }

  function validate_emailId_OnSubmition(){
    const emailId = $("#emailId").val();
    $.ajax({
      url: "/admin/checkNewEmailId?emailId="+encodeURIComponent(emailId),
      async: false,
      success: function(result){
        if (result === 'true') {
          $("#emailId_error").html("☒ Email Id '"+ emailId +"' already used").css("color","red");
          $("#emailId").val('');
        }
      }
    });
  }

  function validate_mobileNumber(){
    const mobileNumber = $("#mobileNumber").val();
    if (mobileNumber.length > 10) {
      $("#mobileNumber_error").html("Please enter a valid mobile number !").css("color","red");
      return;
    } else {
      $("#mobileNumber_error").html("");
    }
    if (mobileNumber.length === 10) {
      $.ajax({
        url: "/admin/checkNewMobile?mobileNumber="+encodeURIComponent(mobileNumber),
        success: function(result){
          if (result === 'true') {
            $("#mobileNumber_error").html("☒ Mobile number '"+ mobileNumber +"' already used").css("color","red");
            $("#mobileNumber").val('');
          } else {
            $("#mobileNumber_error").html("");
          }
        }
      });
    }
  }

  // Confirm on form submit
  $('form').on('submit', function(e) {
    e.preventDefault();
    $.confirm({
      title : 'Confirm!',
      content : 'Do you want to proceed ahead with user creation?',
      buttons : {
        submit : function() { e.currentTarget.submit(); },
        close  : function() { $.alert('Canceled!'); }
      }
    });
  });

  // Confirm on reset
  $(function () {
    $("#resetBtn").on("click", function() {
      $.confirm({
        title : 'Confirm!',
        content : 'Are you sure you want to clear the form!',
        buttons : {
          submit : function() {
            clear_all_error();
            $("#userDetails").trigger("reset");
          },
          close  : function() { $.alert('Canceled!'); }
        }
      });
    });
  });
</script>
</body>
</html>
