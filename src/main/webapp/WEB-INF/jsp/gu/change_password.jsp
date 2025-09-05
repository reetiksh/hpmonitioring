<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>HPGST | Log in</title>
    <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png"/>

    <!-- Bootstrap 5 CSS (required by AdminLTE 4) -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"/>

    <!-- Font Awesome (keep your local if needed) -->
    <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css"/>

    <!-- AdminLTE 4 CSS -->
    <link rel="stylesheet" href="/static/dist/css/adminlte.min.css"/>

    <link rel="stylesheet" href="/static/dist/css/sweetalert2.min.css"/>
    <style>
      :root { --hp-sidebar-width: 250px; }
      .app-sidebar { width: var(--hp-sidebar-width) !important; }
      @media (min-width: 992px) {
        body.sidebar-expand-lg:not(.sidebar-collapse) .app-wrapper .app-main { margin-left: var(--hp-sidebar-width); }
        body.sidebar-collapse .app-wrapper .app-main { margin-left: 0 !important; }
      }
      @media (max-width: 991.98px) {
        .app-wrapper .app-main { margin-left: 0 !important; }
        body.sidebar-open .app-wrapper .app-main { margin-left: 0 !important; }
      }
      .login-header-custom{
        background-color:#343a40;font-size:20px;color:#fff;text-align:center;padding:10px;
        box-shadow:0 0 1px rgba(0,0,0,.125),0 1px 3px rgba(0,0,0,.2);
        border-top-left-radius:10px;border-top-right-radius:10px;
      }
      .toggle-password { cursor: pointer; }
    </style>
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">
        <div class="row justify-content-center">
          <div class="col-md-4">
            <div class="login-box" id="loginDiv">
              <div class="login-header-custom">Change Password</div>
              <div class="card">
                <div class="card-body login-card-body">
                  <div id="messageDiv" style="color:red;padding-bottom:5px;text-align:center;">${messageDiv}</div>

                  <form id="loginForm">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                    <div class="text-center mb-3">
                      <i class="fa fa-user-circle" style="font-size:60px;"></i>
                      <h5 class="mt-2 mb-0">${UserLoginName}</h5>
                      <p class="mb-0" style="font-size:12px;">${designationAcronym}</p>
                    </div>

                    <div id="passwordModule">
                      <label for="oldPassword">Old Password<span style="color:red;"> *</span> <span id="oldPassword_error"></span></label>
                      <div class="input-group mb-3" id="inputDiv1" style="border-radius:5px;">
                        <input type="password" id="oldPassword" name="oldPassword" class="form-control" maxlength="15" placeholder="Please Enter Old Password">
                        <span class="input-group-text">
                          <i id="toggleIconOldPass" class="toggle-password fas fa-eye-slash" onclick="togglePasswordVisibility('oldPass')"></i>
                        </span>
                      </div>

                      <label for="newPassword">New Password<span style="color:red;"> *</span> <span id="newPassword_error"></span></label>
                      <div class="input-group mb-3" id="inputDiv2" style="border-radius:5px;" title="Password Should be 8-15 char long[A-Z, a-z, 0-9 and [@, ., #, $, !, %, *, ?, &]]">
                        <input type="password" id="newPassword" name="newPassword" class="form-control" maxlength="15" placeholder="Please Enter New Password">
                        <span class="input-group-text">
                          <i id="toggleIconNewPass" class="toggle-password fas fa-eye-slash" onclick="togglePasswordVisibility('newPass')"></i>
                        </span>
                      </div>

                      <label for="ReEnterPassword">Re-Enter Password<span style="color:red;"> *</span> <span id="reEnterNewPasswordError"></span></label>
                      <div class="input-group mb-3" id="inputDiv3" style="border-radius:5px;">
                        <input type="password" id="reEnterNewPassword" name="reEnterNewPassword" class="form-control" maxlength="15" placeholder="Please Re-Enter New Password">
                        <span class="input-group-text">
                          <i id="toggleIconReNewPass" class="toggle-password fas fa-eye-slash" onclick="togglePasswordVisibility('reNewPass')"></i>
                        </span>
                      </div>

                      <input type="hidden" id="otpEncd" name="otpEncd">
                      <input type="hidden" id="otpTime" name="otpTime">

                      <div class="row g-2 justify-content-center">
                        <div class="col-4">
                          <button type="button" class="btn btn-primary w-100" onclick="submitForm();">Submit</button>
                        </div>
                        <div class="col-4">
                          <button type="button" class="btn btn-primary w-100" onclick="goBack('${returnToUrl}');">Back</button>
                        </div>
                      </div>
                    </div>

                    <div id="otpModule" style="display:none;">
                      <label for="otp">One Time Password<span style="color:red;"> *</span> <span id="otp_error"></span></label>
                      <div class="input-group mb-3">
                        <input type="text" onKeyPress="if(this.value.length==6) return false;" id="otp" name="otp" class="form-control" placeholder="Enter OTP">
                        <button type="button" class="btn btn-primary" id="regenerateBtn" onclick="submitForm();" style="padding:0.25rem 0.85rem">
                          <span id="example1"></span><span id="example"></span>
                        </button>
                      </div>
                    </div>

                  </form>
                </div>
              </div>

            </div>
          </div>
        </div>
      </div>
    </div>
  </main>

  <jsp:include page="../layout/footer.jsp"/>
</div>

<!-- jQuery (keep your local) -->
<script src="/static/plugins/jquery/jquery.min.js"></script>

<!-- Bootstrap 5 Bundle (required by AdminLTE 4) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- AdminLTE 4 -->
<script src="/static/dist/js/adminlte.min.js"></script>

<script src="/static/dist/js/countdownTimer.min.js"></script>
<script src="/static/dist/js/sweetalert2.all.min.js"></script>

<script>
  // Sidebar toggle compatible with AdminLTE 4 (Bootstrap 5)
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

<script>
  document.addEventListener('contextmenu', function(e){ e.preventDefault(); });
  document.addEventListener('keydown', function(e){ if(e.ctrlKey && e.key==='u'){ e.preventDefault(); }});
  document.addEventListener('keydown', function(e){ if(e.key==='F12'){ e.preventDefault(); }});
  $(document).ready(function(){
    function disableBack(){ window.history.forward() }
    window.onload = disableBack();
    window.onpageshow = function(evt){ if(evt.persisted) disableBack() }
  });
  document.onkeydown = function(e){
    if(e.key==='F5' || (e.ctrlKey && e.key==='r') || e.keyCode===116){ e.preventDefault(); }
  };

  function togglePasswordVisibility(which){
    var passwordInput, icon;
    if(which==='oldPass'){ passwordInput=document.getElementById("oldPassword"); icon=document.getElementById("toggleIconOldPass"); }
    else if(which==='newPass'){ passwordInput=document.getElementById("newPassword"); icon=document.getElementById("toggleIconNewPass"); }
    else if(which==='reNewPass'){ passwordInput=document.getElementById("reEnterNewPassword"); icon=document.getElementById("toggleIconReNewPass"); }
    passwordInput.type = passwordInput.type === "password" ? "text" : "password";
    icon.classList.toggle("fa-eye");
    icon.classList.toggle("fa-eye-slash");
  }
</script>

<script>
function clearAllErrors(){
  $("#oldPassword_error").html("");
  $("#newPassword_error").html("");
  $("#reEnterNewPassword_Error").html(""); // kept as-is
  $("#otp_error").html("");
}

function submitForm(){
  var passwordModule = document.getElementById("passwordModule");
  var otpModule = document.getElementById("otpModule");

  var oldPassword = $("#oldPassword").val();
  var newPassword = $("#newPassword").val();
  var reEnterNewPassword = $("#reEnterNewPassword").val();
  var flag = false;

  clearAllErrors();

  if(!oldPassword){ $("#inputDiv1").css("box-shadow","2px 3px 5px red"); flag = true; } else { $("#inputDiv1").css("box-shadow","2px 3px 5px transparent"); }
  if(!newPassword){ $("#inputDiv2").css("box-shadow","2px 3px 5px red"); flag = true; } else { $("#inputDiv2").css("box-shadow","2px 3px 5px transparent"); }
  if(!reEnterNewPassword){ $("#inputDiv3").css("box-shadow","2px 3px 5px red"); flag = true; } else { $("#inputDiv3").css("box-shadow","2px 3px 5px transparent"); }

  if(flag) return;

  let passwardRegex=/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@.#$!%*?&])[A-Za-z\d@.#$!%*?&]{8,15}$/;
  if(!passwardRegex.test(newPassword)){
    $("#newPassword_error").html("Please enter a valid password !").css("color","red");
    $("#newPassword").val(""); $("#reEnterNewPassword").val(""); return;
  }
  if(newPassword===oldPassword){
    $("#newPassword_error").html("Your old and new password are same. Please enter a different new password !").css("color","red");
    $("#newPassword").val(""); $("#reEnterNewPassword").val(""); return;
  }
  if(reEnterNewPassword!==newPassword){
    $("#reEnterNewPasswordError").html("New password does not match. Please enter same new password !").css("color","red");
    $("#reEnterNewPassword").val(""); return;
  }

  const url='/gu/reset_password';
  const data={ loginName:'${userIdToChangePassword}', oldPassword:oldPassword, newPassword:newPassword };

  fetch(url,{
    method:'POST',
    headers:{ 'Content-Type':'application/json','X-CSRF-Token':'${_csrf.token}' },
    body:JSON.stringify(data)
  })
  .then(r=>{ if(r.ok) return r.text(); throw new Error('Network response was not ok.'); })
  .then(data=>{
    const obj=JSON.parse(data);
    var len=Object.keys(obj).length;
    if(len>1){
      $("#otpEncd").val(obj.otpEncd);
      $("#otpTime").val(obj.otpTime);
      passwordModule.style.display="none";
      otpModule.style.display="block";
      $("#regenerateBtn").prop('disabled', true);
      $("#example1").text('Regenerate OTP in ');
      counter();
    }else{
      if(obj.error1=="pass_matched"){
        Swal.fire({ icon:'success', title:'Success!', text:'Password changed successfully. Please re-login.' })
            .then(()=>{ window.location.assign('/login'); });
      }else{
        $("#oldPassword").val("");
        $("#oldPassword_error").html(obj.error1).css("color","red");
      }
    }
  })
  .catch(err=>console.error(err));
}

document.getElementById("loginForm").addEventListener("keypress",function(event){
  if(event.key==="Enter"){ event.preventDefault(); submitForm(); }
});

function goBack(returnToUrl){ window.location.href = returnToUrl; }

// kept your (commented) OTP and counter logic as-is
</script>
</body>
</html>
