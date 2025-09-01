<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>HPGST | Log in</title>

    <link rel="stylesheet" href="static\dist\css\googleFront\googleFrontFamilySourceSansPro.css">
    <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
    <link rel="stylesheet" href="/static/plugins/icheck-bootstrap/icheck-bootstrap.min.css">
    <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">

    <script src="/static/plugins/jquery/jquery.min.js"></script>
    <script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="/static/dist/js/adminlte.min.js"></script>
    <script src="/static/dist/js/countdownTimer.min.js"></script>
    <link rel="stylesheet" href="/static/dist/css/sweetalert2.min.css">
  
  <style type="text/css">
  
  .login-header-custom{
  	background-color: #343a40; 
  	font-size:20px;
  	color: #fff; 
  	text-align:center;
  	padding:10px;
  	box-shadow: 0 0 1px rgba(0,0,0,.125), 0 1px 3px rgba(0,0,0,.2);
  	border-top-left-radius: 10px; 
  	border-top-right-radius: 10px;
  }
  .toggle-password {
		position: absolute;
		right: 5px;
		top: 50%;
		transform: translateY(-50%);
		cursor: pointer;
	}
  
  </style>
</head>
<body class="hold-transition login-page">
<div class="login-box" id="loginDiv">
  <div class="login-header-custom" >
   Change Password
  </div>
  <div class="card">
    <div class="card-body login-card-body">
 	    <div id="messageDiv" style="color: red;padding-bottom:5px;text-align:center;">${messageDiv}</div>
      <form id="loginForm" >
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <div class="text-center">
            <i class="fa fa-user-circle"  style="font-size:60px;"></i>
            <h5>${UserLoginName}</h5>
            <p style="font-size:12px;">${designationAcronym}</p>
            <%-- <p style="font-size:12px;">${userIdToChangePassword}</p> --%>
            
        </div>
        <div id="passwordModule">
            <label for="oldPassword">Old Password<span style="color: red;"> *</span> <span id="oldPassword_error"></span></label>
            <div class="input-group mb-3" id="inputDiv1" style="border-radius: 5px;">
                <input type="password" id="oldPassword" name="oldPassword" class="form-control" maxlength="15" placeholder="Please Enter Old Password">
                <div class="input-group-append">
                    <div class="input-group-text">
                        <i id="toggleIconOldPass" class="toggle-password fas fa-eye-slash" onclick="togglePasswordVisibility('oldPass')"></i>
                    </div>
                </div>
            </div>
            <label for="newPassword">New Password<span style="color: red;"> *</span> <span id="newPassword_error"></span></label>
            <div class="input-group mb-3" id="inputDiv2" style="border-radius: 5px;" title="Password Should be 8-15 char long[A-Z, a-z, 0-9 and [@, ., #, $, !, %, *, ?, &]]">
                <input type="password" id="newPassword" name="newPassword" class="form-control" maxlength="15" placeholder="Please Enter New Password">
                <div class="input-group-append">
                    <div class="input-group-text">
                        <i id="toggleIconNewPass" class="toggle-password fas fa-eye-slash" onclick="togglePasswordVisibility('newPass')"></i>
                    </div>
                </div>
            </div>
            <label for="ReEnterPassword">Re-Enter Password<span style="color: red;"> *</span> <span id="reEnterNewPasswordError"></span></label>
            <div class="input-group mb-3" id="inputDiv3" style="border-radius: 5px;">
                <input type="password" id="reEnterNewPassword" name="reEnterNewPassword" class="form-control" maxlength="15" placeholder="Please Re-Enter New Password">
                <div class="input-group-append">
                    <div class="input-group-text">
                        <i id="toggleIconReNewPass" class="toggle-password fas fa-eye-slash" onclick="togglePasswordVisibility('reNewPass')"></i>
                    </div>
                </div>
            </div>
            <input type="hidden" id="otpEncd" name="otpEncd">
            <input type="hidden" id="otpTime" name="otpTime">
            <div class="col-12 d-flex justify-content-center">
                <div class="col-4">
                    <button type="button" class="btn btn-primary btn-block" onclick="submitForm();">Submit</button>
                </div>
                
                 <div class="col-4">
                    <button type="button" class="btn btn-primary btn-block" onclick="goBack('${returnToUrl}');">Back</button>
                </div> 
                
            </div>
            
            <!-- <div class="col-12 d-flex justify-content-center">
                <div class="col-4">
                    <button type="button" class="btn btn-primary btn-block">Back</button>
                </div>
            </div> -->
            
        </div>
        <div id="otpModule" style="display: none;">
            <label for="otp">One Time Password<span style="color: red;"> *</span> <span id="otp_error"></span></label>
            <div class="input-group mb-3">
                <input type="text" onKeyPress="if(this.value.length==6) return false;" id="otp" name="otp" class="form-control" placeholder="Enter OTP">
                <div class="input-group-append">
                    <div class="input-group-text" style="padding:0px 2px;">
                        <button type="button" class="btn btn-primary" style="padding:0.25rem 0.85rem" id="regenerateBtn" onclick="submitForm();" ><span id="example1"></span><span id="example"></span></button>
                    </div>
                </div>
            </div>
            <div class="col-12 d-flex justify-content-center">
                <div class="col-4">
                    <button type="button" class="btn btn-primary btn-block" onclick="submitOtp();">Submit</button>
                </div>
            </div>
        </div>
      </form>
    </div>
  </div>
</div>
<script src="/static/dist/js/sweetalert2.all.min.js"></script>
<script>
document.addEventListener('contextmenu', function(e) {
	e.preventDefault();
});
document.addEventListener('keydown', function(e) {
	if (e.ctrlKey && e.key === 'u') {
		e.preventDefault();
	}
});
document.addEventListener('keydown', function(e) {
    if (e.key === 'F12') {
        e.preventDefault();
    }
});
 // Disable back and forward cache
$(document).ready(function () {
    function disableBack() {window.history.forward()}

    window.onload = disableBack();
    window.onpageshow = function (evt) {if (evt.persisted) disableBack()}
});
// Disable refresh
document.onkeydown = function (e) {
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) {
        e.preventDefault();
        
    }
};
    function togglePasswordVisibility(passOrRetypePass) {
        if(passOrRetypePass == 'oldPass'){
            var passwordInput = document.getElementById("oldPassword");
            var icon = document.getElementById("toggleIconOldPass");
        } else if(passOrRetypePass == 'newPass'){
            var passwordInput = document.getElementById("newPassword");
            var icon = document.getElementById("toggleIconNewPass");
        } else if(passOrRetypePass == 'reNewPass'){
            var passwordInput = document.getElementById("reEnterNewPassword");
            var icon = document.getElementById("toggleIconReNewPass");
        }
        passwordInput.type = passwordInput.type === "password" ? "text" : "password";
        icon.classList.toggle("fa-eye");
        icon.classList.toggle("fa-eye-slash");
    }
</script>
<script>

function clearAllErrors(){
    $("#oldPassword_error").html("");
    $("#newPassword_error").html("");
    $("#reEnterNewPassword_Error").html("");
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
    

	if(oldPassword === '' || oldPassword == null){
        $("#inputDiv1").css("box-shadow","2px 3px 5px red");
		flag = true;
	} else {
        $("#inputDiv1").css("box-shadow","2px 3px 5px transparent");
    }
    if(newPassword === '' || newPassword == null){
        $("#inputDiv2").css("box-shadow","2px 3px 5px red");
		flag = true;
	} else {
        $("#inputDiv2").css("box-shadow","2px 3px 5px transparent");
    }
    if(reEnterNewPassword === '' || reEnterNewPassword == null){
        $("#inputDiv3").css("box-shadow","2px 3px 5px red");
		flag = true;
	} else {
        $("#inputDiv3").css("box-shadow","2px 3px 5px transparent");
    }

    

    if(flag!=true){
        let passwardRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@.#$!%*?&])[A-Za-z\d@.#$!%*?&]{8,15}$/;

        if(!passwardRegex.test(newPassword)){
            $("#newPassword_error").html("Please enter a valid password !").css("color","red");
            $("#newPassword").val("");
            $("#reEnterNewPassword").val("");
            return;
        }

        if(newPassword == oldPassword){
            $("#newPassword_error").html("Your old and new password are same. Please enter a different new password !").css("color","red");
            $("#newPassword").val("");
            $("#reEnterNewPassword").val("");
            return;
        }

        if(reEnterNewPassword != newPassword){
            $("#reEnterNewPasswordError").html("New password does not match. Please enter same new password !").css("color","red");
            $("#reEnterNewPassword").val("");
            return;
        }

        
        const url = '/gu/reset_password';
        const data = {
            loginName: '${userIdToChangePassword}',
            oldPassword: oldPassword,
            newPassword: newPassword
        };

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-Token': '${_csrf.token}'
            },
            body: JSON.stringify(data)
        })
        .then(response => {
            if (response.ok) {
                return response.text();
            }
            throw new Error('Network response was not ok.');
        })
        .then(data => {
        	debugger;
            const obj = JSON.parse(data);
            var len = Object.keys(obj).length;
            if(len>1){
                $("#otpEncd").val(obj.otpEncd);
                $("#otpTime").val(obj.otpTime);
                passwordModule.style.display = "none";
                otpModule.style.display = "block";
                $("#regenerateBtn").prop('disabled', true);
                $("#example1").text('Regenerate OTP in ');
                counter();
            } else{
            	if(obj.error1 == "pass_matched"){
            		Swal.fire({
    					icon: 'success',
    					title: 'Success!',
    					text: 'Password changed successfully. Please re-login.',
    					}).then(() => {
    					/* window.location.href = $(location).attr('protocol')+"//"+$(location).attr('host')+'/login'; */
    						window.location.assign('/login');
    				});
            	}
            	else{
            		 $("#oldPassword").val("");
                     $("#oldPassword_error").html(obj.error1).css("color","red");
            	}
            	
            	
                /* if(obj.error2==null){
                    $("#oldPassword").val("");
                    $("#oldPassword_error").html(obj.error1).css("color","red");
                } 
                else {
                	alert("hello");
                    window.location.assign('/login');
                } */
            }

        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
    }
}

  document.getElementById("loginForm").addEventListener("keypress",function(event){
    if(event.key === "Enter"){
      event.preventDefault();
      submitForm();
    }
  });

  function goBack(returnToUrl) {
	  window.location.href = returnToUrl;
	}
  
  /* function redirectToPage(redirectLocation){
	  debugger;
	  window.location.href = redirectLocation;
  } */

/* function submitOtp(){
    var otpEncd = $("#otpEncd").val();
    var otpTime = $("#otpTime").val();
    var newPassword = $("#newPassword").val();
    var oldPassword = $("#oldPassword").val();
    var loginName = '${loginName}';

    var otp = $("#otp").val();

    clearAllErrors();

    if(otp === '' || otp == null){
        $("#otp_error").html("Please enter OTP !").css("color","red");
		 return;
    }

    if(newPassword==='' || newPassword==null || loginName==='' || loginName==null){
        window.location.assign('/login');
    }

    const url = '/otpSubmissionAndResetPassword';

    const data = {
        loginName: '${loginName}',
        newPassword: newPassword,
        oldPassword: oldPassword,
        otp: otp,
        otpEncd: otpEncd,
        otpTime: otpTime
    };

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-Token': '${_csrf.token}'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (response.ok) {
            return response.text();
        }
        throw new Error('Network response was not ok.');
    })
    .then(data => {
        const obj = JSON.parse(data);
        var len = Object.keys(obj).length;
        
        console.log(obj);

        if(len>1){
            window.location.assign('/logout');
        } else{
            $("#otp_error").html(obj.error).css("color","red");
        }

    })
    .catch(error => {
        console.error('There was a problem with the fetch operation:', error);
    });
} */

    /* function counter(){
		$('#example').countdownTimer({
	    	   seconds: 5,
	    	   loop:false,
	    	   callback:function(){
	    		   $("#regenerateBtn").prop('disabled', false);
                   $("#example").text('');
                   $("#example1").text('Regenerate OTP');
	    	   }
	   	 });
	} */
</script>

</body>
</html>
