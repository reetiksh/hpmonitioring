<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HPGST | Log in</title>

	<!-- <link rel="stylesheet" href="/static/dist/css/googleFront/googleFrontFamilySourceSansPro.css"> -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/plugins/icheck-bootstrap/icheck-bootstrap.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="/static/dist/css/sweetalert2.min.css">
  
	<script src="/static/plugins/jquery/jquery.min.js"></script>
	<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
	<script src="/static/dist/js/adminlte.min.js"></script>
	<script src="/static/dist/js/countdownTimer.min.js"></script>
	<script src="/static/dist/js/sweetalert2.all.min.js"></script>
  
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

  .password-container {
            position: relative;
        }

	.toggle-password {
		position: absolute;
		right: 5px;
		top: 50%;
		transform: translateY(-50%);
		cursor: pointer;
	}

	.toggle-password-retype {
		position: absolute;
		right: 5px;
		top: 50%;
		transform: translateY(-50%);
		cursor: pointer;
	}

	
	
  
  </style>
  
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
$(document).ready(function () {

  /*   $("#forgotUsername").click(function (event) {
    	event.preventDefault(); 
        $("#loginDiv").slideUp(300, function () {
            $("#loginDiv").hide();
            $("#forgotDiv").slideDown(300);
            $("#forgotDiv").show(); 
        });
    });

    $("#forgotPassword").click(function (event) {
    	event.preventDefault(); 
        $("#loginDiv").slideUp(300, function () {
            $("#loginDiv").hide();
            $("#forgotDiv").slideDown(300);
            $("#forgotDiv").show(); 
        });
    }); */
    
    
	$('#loginIdOtp').hide();
	$('#passwordLabel').hide();
	$('#usernameEmailIdDiv').hide();
      $('#usernameDobDiv').hide();
    
    $("#backToLoginBtn").click(function (event) {
    	window.location.href = $(location).attr('protocol')+"//"+$(location).attr('host')+'/login';
    });

    function hideOrShowChildForValidateButton(status){

        if(status=='hide'){
        	$("#otpDiv").hide();
	   		 $("#validateOtpDiv").hide();
	   		 $("#passwordReEnterDiv").hide();
        }else{
        	$("#otpDiv").hide();
	   		 $("#validateOtpDiv").hide();
	   		 $("#passwordReEnterDiv").hide();
        }
    }

    function userOrPasswordChk(){
    	$("#forgotInput").val('');
   	 	$("#forgotInput").prop('disabled', false);
		$("#validateBtn").prop('disabled', false);
		$("#validateBtn").show();
    }
		$('#UsernameChk').change(function(){
		$('#validtateUserNameBtnDiv').css('display', 'block');
		$('#validateBtnDiv').css('display', 'none');
		$('#messageDiv').css('display', 'none');
		
		$('#forgotDbDIv').hide();
		$('#usernameEmailIdDiv').show();
		$('#usernameDobDiv').show();
		$('#passwordUsernameDiv').hide();
      
		 userOrPasswordChk();
		 hideOrShowChildForValidateButton('hide');
		 
         if($(this).prop('checked') === true){
            $("#passwordChk").prop('checked', false);
            $("#forgotInput").attr('placeholder', 'Enter Grn No.');
            
         }else{
        	 $("#passwordChk").prop('checked', true);
        	 $("#forgotInput").attr('placeholder', 'Enter Username');
        }
		clearFields();
     });

     $('#passwordChk').change(function(){
		clearFields();
		$('#validtateUserNameBtnDiv').css('display', 'none');
		$('#validateBtnDiv').css('display', 'block');
     $('#passwordUsernameDiv').show();
         $('#forgotDbDIv').show();
    	 userOrPasswordChk();
		 hideOrShowChildForValidateButton('hide');
		  $('#usernameEmailIdDiv').hide();
      $('#usernameDobDiv').hide();
		 
         if($(this).prop('checked') === true){
            $("#UsernameChk").prop('checked', false);
            $("#forgotInput").attr('placeholder', 'Enter Username');
         }else{
        	 $("#UsernameChk").prop('checked', true);
        	 $("#forgotInput").attr('placeholder', 'Enter Grn No.');
         }
     });

     $("#ConfirmPassword1").on('keyup', function(){
     $("#messageDiv").css("display", "block");
     	
   	    var password = $("#Password").val();
   	    var confirmPassword = $("#ConfirmPassword").val();
   	    
   	    if (password != confirmPassword)
   	        $("#messageDiv").html("Password do not match. Please enter same password.").css("color","red");
   	    else
   	        $("#messageDiv").html("Password match !").css("color","green");
   	   });
});

	function counter(){
		$('#example').countdownTimer({
	    	   seconds: 5,
	    	   loop:false,
	    	   callback:function(){ 
					$("#example").text("");
	    		   $("#regeneratePassBtn").prop('disabled', false);
				}
	   	 });
	}

	function validate(){
		$('#messageDiv').css('display', 'block');
		var usernameSelected =  $('#UsernameChk').prop('checked');
	    var passwordSelected =  $('#passwordChk').prop('checked');
	    var username = $("#forgotInput").val();
	    var userdob = $("#forgotDOB").val();
		
	    
		<!-- var input = $("#forgotInput").val();-->
		

		if((username === '') && userdob === ''){
			 $("#messageDiv").html("Please enter login id and date of birth !").css("color","red");
			 return;
		}

		if(passwordSelected && (username === '')){
			 $("#messageDiv").html("Please enter  login id !").css("color","red");
			 return;
		}
		if(userdob === ''){
			 $("#messageDiv").html("Please enter date of birth !").css("color","red");
			 return;
		}
		
		if(usernameSelected && (username === '')){
			 $("#messageDiv").html("Please enter the GRN number !").css("color","red");
			 return;
		}
		
		var host = $(location).attr('protocol')+"//"+$(location).attr('host');
		$.get(host+'/validateUsernameAndDob?usernameSelected='+usernameSelected+'&passwordSelected=' + passwordSelected + '&username=' + username +'&userdob=' + userdob, function(data, status){
		

			if(status){
			var jsonData = JSON.parse(data);
				if(jsonData.message == 'SUCCESS'){
					$("#loginTitle").text("Enter OTP");
					$("#messageDiv").html('');
					$("#forgotInput").prop('disabled', true);
					$("#validateBtn").hide();
					$("#otpDiv").show();
					$("#otp").val('');
					$("#regenerateBtn").prop('disabled', true);
					$("#validateOtpDiv").show();
					$('#passwordUsernameDiv').hide();
					$('#forgotDbDIv').hide();
					$('#userPassChkDiv').hide();
					$('#clearFields').hide();
					$('#newPassword_error').hide();
					$('#loginIdOtp').show();
					counter();
				}
				else{
					if(jsonData.message == 'InActiveUser'){
						 $("#messageDiv").html(jsonData.inActiveUserMessage).css("color","red");
					}else if(jsonData.message == 'Locked'){
						$("#messageDiv").html(jsonData.systemLockedForDuration).css("color","red");
					}
					else{
						$("#messageDiv").html(jsonData.noUserPresent).css("color","red");
					}
				}
			}
		}).fail(function() {
		   console.log("failed");
		});
	 }

 
	 function validateUserName(){
		$('#messageDiv').css('display', 'block');
		var usernameSelected =  $('#UsernameChk').prop('checked');
	    var passwordSelected =  $('#passwordChk').prop('checked');
		var userEmailId = $("#usernameEmailId").val();
	    var userDob = $("#usernameDob").val();

		var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
		

		if((userEmailId === '') && userDob === ''){
			 $("#messageDiv").html("Please provide the email id and date of birth !").css("color","red");
			 return;
		}

		if (!emailRegex.test(userEmailId)) {
			$("#messageDiv").html("Please enter a valid email address. !").css("color","red");
			return;
		}
		

		if(userEmailId === ''){
			 $("#messageDiv").html("Please provide the email id !").css("color","red");
			 return;
		}

		if(userDob === ''){
			 $("#messageDiv").html("Please provide date of birth !").css("color","red");
			 return;
		}


		var host = $(location).attr('protocol')+"//"+$(location).attr('host');
		$.get(host+'/validateEmailAndDob?usernameSelected=' +usernameSelected + '&passwordSelected=' + passwordSelected + '&userEmailId=' + userEmailId +'&userDob=' + userDob, function(data, status){
		

			if(status){
			var jsonData = JSON.parse(data);
				if(jsonData.message == 'SUCCESS'){
					$("#loginTitle").text("Enter OTP");
					$("#messageDiv").html('');
					$("#forgotInput").prop('disabled', true);
					$("#validateBtn").hide();
					$("#otpDiv").show();
					$("#otp").val('');
					$("#regenerateBtn").prop('disabled', true);
					$("#validateOtpDiv").show();
					$('#passwordUsernameDiv').hide();
					$('#forgotDbDIv').hide();
					$('#userPassChkDiv').hide();
					$('#clearFields').hide();
					$("#validtateUserNameBtn").hide();
					$("#usernameEmailIdDiv").hide();
					$("#usernameDobDiv").hide();
					$("#validateOtpBtn").hide();
					$("#validateUserNameOtpDiv").show();
					$("#loginIdOtp").show();
					
					counter();
				}
				else{
					if(jsonData.message == 'InActiveUser'){
						 $("#messageDiv").html(jsonData.inActiveUserMessage).css("color","red");
					}else if(jsonData.message == 'Locked'){
						$("#messageDiv").html(jsonData.systemLockedForDuration).css("color","red");
					}
					else if(jsonData.message == 'noEmailPresent'){
						$("#messageDiv").html(jsonData.noEmailPresent).css("color","red");
					}
					else{
						$("#messageDiv").html(jsonData.noUserPresent).css("color","red");
					}
				}
			}
		}).fail(function() {
		   console.log("failed");
		});

	 }
	
	 function validateOtp(){
	 
			var otp = $("#otp").val();
			 if(otp === ''){
				 $("#messageDiv").html("Please provide OTP !").css("color","red");
				 return;
			}
		 	var usernameSelected =  $('#UsernameChk').prop('checked');
			var passwordSelected =  $('#passwordChk').prop('checked');
			var input = $("#forgotInput").val();
			var forgotDOBOtp = $("#forgotDOB").val();
			
			var host = $(location).attr('protocol')+"//"+$(location).attr('host');
		
			$.get(host+'/validateOtp?usernameSelected='+usernameSelected+'&passwordSelected=' + passwordSelected+'&input=' + input + '&otp=' + otp + '&forgotDOBOtp=' + forgotDOBOtp, function(data, status){
			var jsonData = JSON.parse(data);
			
				if(status){
					if(jsonData.message == 'SUCCESS'){
						$("#passwordLabel").show();
						$("#loginTitle").text("Reset Password");
						$("#otp").prop('disabled', false);
						$("#otpDiv").hide();
						$("#regenerateBtn").text('Regenerate OTP');
						$("#regenerateBtn").prop('disabled', true);
						$("#validateOtpDiv").hide();
						$("#messageDiv").hide();
						$("#loginIdOtp").hide();
						
						
						
						
						if(passwordSelected){
						 $("#passwordReEnterDiv").show();
						}
						if(usernameSelected){
							
						}
					}else if(jsonData.message == 'otpExpire'){
						$("#messageDiv").html(jsonData.otpExpire).css("color","red");
					}
					else if(jsonData.remainingOtpAttempts == 0){
						$("#messageDiv").html(jsonData.message).css("color","red");
					}else if(jsonData.message == 'Locked'){
						$("#messageDiv").html(jsonData.systemLockedForDuration).css("color","red");
					}else if(jsonData.message == 'UnLocked'){
					 $("#messageDiv").html(jsonData.userInActiveAfterLocked).css("color","red");
					}else if(jsonData.message == 'InActive'){
						$("#messageDiv").html(jsonData.userInActiveAfterLocked).css("color","red");
					}
				
					else{
						 $("#messageDiv").html("Your account will be locked after " + jsonData.remainingOtpAttempts + " wrong attempt(s)!").css("color","red");
					}
				}
			});
	 }



function validateUserNameOtp(){
	var otp = $("#otp").val();
			 if(otp === ''){
				 $("#messageDiv").html("Please provide OTP !").css("color","red");
				 return;
			}
			
		 	var usernameSelected =  $('#UsernameChk').prop('checked');
			var passwordSelected =  $('#passwordChk').prop('checked');
			var userEmailId = $("#usernameEmailId").val();
			var forgotDOBOtp = $("#usernameDob").val();
			
			var host = $(location).attr('protocol')+"//"+$(location).attr('host');
		
			$.get(host+'/validateUserNameOtp?usernameSelected='+usernameSelected+'&passwordSelected=' + passwordSelected+'&userEmailId=' + userEmailId + '&otp=' + otp + '&forgotDOBOtp=' + forgotDOBOtp, function(data, status){
			var jsonData = JSON.parse(data);
			
				if(status){
					if(jsonData.message == 'SUCCESS'){
					var popUpMessage = jsonData.popUpInfo;

					Swal.fire({
						icon: 'success',
						title: 'Success!',
						text: popUpMessage,
						}).then(() => {
						window.location.href = $(location).attr('protocol')+"//"+$(location).attr('host')+'/login';
					});
					}else if(jsonData.message == 'otpExpire' ){
						$("#messageDiv").html(jsonData.otpExpire).css("color","red");
					}
					else if(jsonData.remainingOtpAttempts == 0){
						$("#messageDiv").html(jsonData.message).css("color","red");
					}else if(jsonData.message == 'Locked'){
						$("#messageDiv").html(jsonData.systemLockedForDuration).css("color","red");
					}else if(jsonData.message == 'UnLocked'){
					 $("#messageDiv").html(jsonData.userInActiveAfterLocked).css("color","red");
					}else if(jsonData.message == 'InActive'){
						$("#messageDiv").html(jsonData.userInActiveAfterLocked).css("color","red");
					}
				
					else{
						 $("#messageDiv").html("Your account will be locked after " + jsonData.remainingOtpAttempts + " wrong attempt(s)!").css("color","red");
					}
				}
			});
}	 
	 
 
function resetPassword(){
	let passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@.#$!%*?&])[A-Za-z\d@.#$!%*?&]{8,15}$/;

	$("#messageDiv").css("display", "block");
	 var passwordOnSubmit = $("#Password").val();
	 var confirmPasswordOnSubmit = $("#ConfirmPassword").val(); 
     var username = $("#forgotInput").val();
     var userdob = $("#forgotDOB").val();
	 
	if(passwordOnSubmit == ''){
		$("#messageDiv").html("Kindly provide the password !").css("color","red");
		return;
	 }

	 if(confirmPasswordOnSubmit == ''){
		$("#messageDiv").html("Kindly provide the confirm password ").css("color","red");
		return;
	 } 

	

	 if(!passwordRegex.test(passwordOnSubmit)){
		$("#messageDiv").html("Enter a valid password (eg. Example@2024) !").css("color","red");
            $("#Password").val("");
            $("#ConfirmPassword").val("");
            return;
	}

     if(passwordOnSubmit == confirmPasswordOnSubmit){
     var host = $(location).attr('protocol')+"//"+$(location).attr('host');
			var csrfToken = $("meta[name='_csrf']").attr("content");

			$.ajaxSetup({
			headers: {
			'X-CSRF-TOKEN': '${_csrf.token}'
		}
		});
     
		$.post(host+'/saveResetPassword?username='+username + '&userdob=' + userdob + '&passwordOnSubmit=' + passwordOnSubmit, function(data, status){
		var jsonData = JSON.parse(data);
		
		if(status){
			if(jsonData.message == 'SUCCESS'){
				$("#otp").prop('disabled', false);
				$("#otpDiv").hide();
				$("#regenerateBtn").text('Regenerate OTP');
				$("#regenerateBtn").prop('disabled', true);
				$("#validateOtpDiv").hide();
				$("#resetPasswordBtn").prop("disabled", true);
				$("#Password").prop("disabled", true);
				$("#ConfirmPassword").prop("disabled", true);
				Swal.fire({
					icon: 'success',
					title: 'Success!',
					text: 'Password changed successfully. Please re-login.',
					}).then(() => {
					window.location.href = $(location).attr('protocol')+"//"+$(location).attr('host')+'/login';
				});	
			}
			else{
				 $("#messageDiv").html("Password Reset Fail Due To Connection Lost !").css("color","red");
			}
		}
	});
   }else{
   		$("#messageDiv").html("Password do not match.").css("color","red");
   }
}



function resetUserNamePassword(){
	$("#messageDiv").css("display", "block");
	 var passwordOnSubmit = $("#Password").val();
	 var confirmPasswordOnSubmit = $("#ConfirmPassword").val(); 
     var userEmailId = $("#usernameEmailId").val();
     var userdob = $("#usernameDob").val();
	 
	if(passwordOnSubmit == ''){
		$("#messageDiv").html(" Kindly provide the password !").css("color","red");
		return;
	 }
	 
     if(passwordOnSubmit == confirmPasswordOnSubmit){
     var host = $(location).attr('protocol')+"//"+$(location).attr('host');
     
		$.post(host+'/saveUserNameResetPassword?userEmailId='+userEmailId + '&userdob=' + userdob + '&passwordOnSubmit=' + passwordOnSubmit, function(data, status){
		var jsonData = JSON.parse(data);
		
		if(status){
			if(jsonData.message == 'SUCCESS'){
				$("#otp").prop('disabled', false);
				$("#otpDiv").hide();
				$("#regenerateBtn").text('Regenerate OTP');
				$("#regenerateBtn").prop('disabled', true);
				$("#validateOtpDiv").hide();
				
				$("#resetUserNamePasswordBtn").prop("disabled", true);
				$("#messageDiv").html("Password Changed Successfully !").css("color","green");
			}
			else{
				 $("#messageDiv").html("Password Reset Fail Due To Connection Lost !").css("color","red");
			}
		}
	});
   }else{
   		$("#messageDiv").html("Password do not match.").css("color","red");
   }
}

function regenerateOtp(){
	var userdob ="";
	var username = "";
	
	username = $("#forgotInput").val();
	userdob = $("#forgotDOB").val();
	var usernameSelected =  $('#UsernameChk').prop('checked');
	var passwordSelected =  $('#passwordChk').prop('checked');


	if(userdob == ''){
		userdob = $("#usernameDob").val();
	}
	if(username == ''){
		username = $("#usernameEmailId").val();
	}


	
	var host = $(location).attr('protocol')+"//"+$(location).attr('host');
	$.get(host+'/regenerateOtp/?username=' + username + '&userdob=' + userdob + '&usernameSelected=' +usernameSelected + '&passwordSelected=' +passwordSelected, function(data, status){
		var jsonData = JSON.parse(data);
			if(status){
				if(jsonData.message == 'SUCCESS'){
					
				}
			else{
					 $("#messageDiv").html("Your account will be locked after " + jsonData.remainingOtpAttempts + " wrong attempt(s)!").css("color","red");
				}
			}
		});
}

function clearFields() {
	var textInput = document.getElementById("forgotInput");
	var dateInput = document.getElementById("forgotDOB");
	var userNameEmailId = document.getElementById("usernameEmailId");
	var usernameDob = document.getElementById("usernameDob");
	
	textInput.value = "";
	dateInput.value = "";
	userNameEmailId.value = "";
	usernameDob.value = "";
}

function togglePasswordVisibility(passOrRetypePass) {
	
	if(passOrRetypePass == 'pass'){
		var passwordInput = document.getElementById("Password");
		var icon = document.getElementById("toggleIcon");

		passwordInput.type = passwordInput.type === "password" ? "text" : "password";

		icon.classList.toggle("fa-eye");
		icon.classList.toggle("fa-eye-slash");
	}
	if(passOrRetypePass == 'retypePass'){
		var passwordInput = document.getElementById("ConfirmPassword");
		var icon = document.getElementById("toggleIconRetypePass");

		passwordInput.type = passwordInput.type === "password" ? "text" : "password";

		icon.classList.toggle("fa-eye");
		icon.classList.toggle("fa-eye-slash");
	}
            
}

</script>

</head>
<body class="hold-transition login-page">
<div class="login-box" id="forgotDiv">
  <div class="login-header-custom" >
   <div id="loginTitle">Forgot Password/Login Id</div>
  </div>
  <div class="card">
    <div class="card-body login-card-body">
    <div id="messageDiv" style="color: red;padding-bottom:5px;text-align:center;"></div>
      <div class="input-group mb-3" id="userPassChkDiv">
      	 <div class="custom-control custom-switch">
		  <input type="checkbox" class="custom-control-input" id="passwordChk" checked>
		  <label class="custom-control-label" for="passwordChk">Password</label>
		</div>&nbsp;&nbsp;
         <div class="custom-control custom-switch">
		  <input type="checkbox" class="custom-control-input" id="UsernameChk" >
		  <label class="custom-control-label" for="UsernameChk">Login Id</label>
		</div> 
       </div>
       
        <div class="form-group" id="passwordUsernameDiv">
            <label for="username">
                <i class="fas fa-user"></i> Login Id <span style="color: red;"> *</span>
            </label>
            <input type="text" class="form-control" id="forgotInput" name="username" placeholder="Enter Login Id">
        </div>
        <div class="form-group" id="forgotDbDIv">
            <label for="dob">
                <i class="fas fa-calendar-alt"></i> Date of Birth: <span style="color: red;"> *</span>
            </label>
            <input type="date" class="form-control" id="forgotDOB" name="dob" required>
        </div>
        
        <div class="form-group" id="usernameEmailIdDiv">
	    	<label for="usernameEmailId">
		        <i class="fas fa-user"></i> Email Id : <span style="color: red;"> *</span>
		    </label>
		    <input type="text" class="form-control" id="usernameEmailId" name="usernameEmailId" required placeholder="Enter Email Id">
		</div>

        <div class="form-group" id="usernameDobDiv">
            <label for="usernameDob">
                <i class="fas fa-calendar-alt"></i> Date of Birth: <span style="color: red;"> *</span>
            </label>
            <input type="date" class="form-control" id="usernameDob" name="usernameDob" required>
        </div>
        
		  <label for="otp" id="loginIdOtp">One Time Password<span style="color: red;"> *</span> <span id="otp_error"></span></label>
          <div id="otpDiv" style="display: none;">
	          <div class="input-group mb-3" >
	          <input type="text" id="otp" class="form-control" placeholder="Enter OTP">
	          <div class="input-group-append">
	            <div class="input-group-text" style="padding:0px 2px;">
	               <button class="btn btn-primary" style="padding:0.25rem 0.85rem" id="regeneratePassBtn" disabled onclick="regenerateOtp()">Regenerate OTP(Sec) <span id="example"></span></button>
	            </div>
	          </div>
	        </div>
          </div>
		  
		  <label for="Password" id="passwordLabel">New Password<span style="color: red;"> *</span> <span id="newPassword_error"></span></label>
          <div id="passwordReEnterDiv" style="display:none;">
			<div class="input-group mb-3" >
			  <input type="password" id="Password" class="form-control" placeholder="Enter New Password" title="Password Should be 8-15 char long[A-Z, a-z, 0-9 and [@, $, . , #, !, %, *, ?, &, ^]]">
			  <div class="input-group-append">
	            <div class="input-group-text">
					<i id="toggleIcon" class="toggle-password fas fa-eye-slash" onclick="togglePasswordVisibility('pass')"></i>
	            </div>
	          </div>
	        </div>

			<label for="ConfirmPassword">Confirm New Password<span style="color: red;"> *</span> <span id="confirmPassword_error"></span></label>
	        <div class="input-group mb-3" >
	          <input type="password" id="ConfirmPassword" class="form-control" placeholder="Confirm Password" title="Password Should be 8-15 char long[A-Z, a-z, 0-9 and [@, $, . , #, !, %, *, ?, &, ^]]">
	          <div class="input-group-append">
	            <div class="input-group-text">
					<i id="toggleIconRetypePass" class="toggle-password-retype fas fa-eye-slash" onclick="togglePasswordVisibility('retypePass')"></i>
	            </div>
	          </div>
			</div>

	          <div class="input-group mb-3" id="resetPasswordBtnDiv">
		          <div class="col-4" style="padding-left:0px;padding-top:10px;">
			     	 <input class="btn btn-primary btn-block" id="resetPasswordBtn" type="button" value="Submit" onclick="resetPassword()" />
			      </div>
			  </div>

			  
			  <div class="input-group mb-3" id="resetUserNamePasswordDiv" style="display: none;">
				<div class="col-4" style="padding-left:0px;padding-top:1px;">
					<input class="btn btn-primary btn-block" id="resetUserNamePasswordBtn" type="button" value="Submit" onclick="resetUserNamePassword()" />
				</div>
			</div>


	        </div>

          </div>
	<div style="padding-left: 20px;">
      <div class="col-4" id="validateBtnDiv" style="padding-left:0px;float: left;">
     	 <input class="btn btn-primary btn-block" id="validateBtn" type="button" value="Validate" onclick="validate()" />
      </div>

	  <div class="col-4" id="validtateUserNameBtnDiv" style="padding-left:0px;float: left;display: none;">
		<input class="btn btn-primary btn-block" id="validtateUserNameBtn" type="button" value="Validate" onclick="validateUserName()" />
	</div>

	  

	  <div class="col-4" style="padding-left:0px;float: left;">
		<input class="btn btn-primary btn-block" id="clearFields" type="button" value="Clear" onclick="clearFields()" />
	  </div>
	  </div>


      <div class="col-6" style="display:none;padding-left:20px;" id="validateOtpDiv">
       	 <input class="btn btn-primary btn-block" id="validateOtpBtn" type="button" value="Submit OTP" onclick="validateOtp()" />
      </div>

	  
	  <div class="col-6" style="display:none;padding-left:20px;" id="validateUserNameOtpDiv">
		<input class="btn btn-primary btn-block" id="validateOtpUserNameBtn" type="button" value="Submit OTP" onclick="validateUserNameOtp()" />
  	 </div>



     <p></p>
      <p class="mb-1" style="text-align:right;padding-right: 10px;">
      	<a id="backToLoginBtn" style="cursor: pointer">Back To Login</a>
      </p>
    </div>
  </div>
</div>
</body>
</html>
