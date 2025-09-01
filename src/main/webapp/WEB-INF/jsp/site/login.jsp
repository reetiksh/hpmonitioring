<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login</title>

  <!-- Styles -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/plugins/icheck-bootstrap/icheck-bootstrap.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/dist/css/jquery-confirm.min.css">

  <!-- Scripts -->
  <script src="${pageContext.request.contextPath}/static/plugins/jquery/jquery.min.js"></script>
  <script src="${pageContext.request.contextPath}/static/dist/js/jquery-confirm.min.js"></script>
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

  <style>
    @import url('https://fonts.googleapis.com/css?family=Montserrat:400,800');

    * {
      box-sizing: border-box;
    }

    body {
      background-image: url('${pageContext.request.contextPath}/static/image/SHIMLA_NEW_1.jpg');
      background-size: cover;
      background-repeat: no-repeat;
      background-attachment: fixed;
      background-position: center;
      margin: 0;
      padding: 0;
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      align-items: center;
      overflow-x: hidden;
    }

    .container-flex {
      display: flex;
      flex: 1;
      flex-wrap: wrap;
      justify-content: center;
      align-items: stretch;
      width: 100%;
      max-width: 1000px;
      gap: 20px;
      margin-top: 40px;
    }

    .equal-box {
      flex: 1;
      min-height: 550px;
      display: flex;
      flex-direction: column;
      justify-content: center;
    }

    .container.login-box,
    .container.overlay-box {
      background-color: #fff;
      border-radius: 10px;
      box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25),
                  0 10px 10px rgba(0, 0, 0, 0.22);
      padding: 20px;
      width: 100%;
      max-width: 480px;
      box-sizing: border-box;
    }

    .form-container {
      width: 100%;
    }

    form {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 12px;
    }

    input {
      width: 100%;
      padding: 12px 15px;
      border-radius: 50px;
      border: 1px solid #455f73;
      background-color: #eee;
    }

    button {
      border-radius: 20px;
      border: 1px solid #FF4B2B;
      background-color: #FF4B2B;
      color: #fff;
      font-weight: bold;
      padding: 12px 45px;
      letter-spacing: 1px;
      text-transform: uppercase;
      cursor: pointer;
    }

    .signin-heading {
      font-size: 24px;
      color: #455f73;
      text-align: center;
    }

    .captcha-section {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 10px;
    }

    .captcha-section img {
      max-width: 100%;
    }

    .overlay {
      background: linear-gradient(to left, #6489a4, #0e3145e6);
      padding: 20px;
      color: #fcfcfc;
      border-radius: 8px;
      flex: 1;
    }

   .overlay-panel {
  display: flex;
  flex-direction: column;
  align-items: stretch;
  justify-content: center;
  height: 100%;
  gap: 10px;
  padding: 30px 20px;
}

.overlay-panel h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #f9ed8e;
  animation: color-change 2s infinite, bounce 1s infinite;
}

.faq {
  display: flex;
  align-items: center;
  padding: 15px;
  background-color: rgba(255, 255, 255, 0.12);
  border-radius: 10px;
  font-size: 16px;
  transition: background-color 0.3s;
}

.faq:hover {
  background-color: rgba(255, 255, 255, 0.25);
}

.faq a {
  color: #fff;
  font-weight: bold;
  font-size: 16px;
  text-decoration: none;
}

.faq i {
  margin-right: 10px;
  font-size: 18px;
}


    @keyframes color-change {
      0% { color: #f0f4fc; }
      50% { color: #b5d2ec; }
      100% { color: #ffffff; }
    }

    @keyframes bounce {
      0%, 20%, 50%, 80%, 100% { transform: translateY(0); }
      40% { transform: translateY(-20px); }
      60% { transform: translateY(-10px); }
    }

    .faq {
      display: flex;
      align-items: center;
      padding: 8px 15px;
      background-color: rgba(255, 255, 255, 0.1);
      border-radius: 8px;
    }


    .footer {
      background-color: #222;
      color: #fff;
      text-align: center;
      font-size: 14px;
      width: 100%;
      padding: 10px;
      margin-top: 40px;
    }

    @media (max-width: 768px) {
      .container-flex {
        flex-direction: column;
        align-items: center;
      }

      .equal-box {
        min-height: auto;
      }

      .container.login-box,
      .container.overlay-box {
        max-width: 90%;
      }

      .overlay-panel {
        flex-direction: column;
        align-items: center;
      }
    }
  </style>
</head>
<body>

<div class="container-flex">
  <div class="container login-box equal-box">
    <div class="form-container">
      <form action="authenticate" method="post">
        <div style="text-align: center;">
          <img src="${pageContext.request.contextPath}/static/image/logo_HP.png" style="width: 130px;" alt="logo">
          <h4>Government of Himachal Pradesh</h4>
        </div>

        <c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION.message}">
          <div style="color: red; text-align: center;">
            <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />
          </div>
        </c:if>

        <h1 class="signin-heading">User Login</h1>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

        <label><i class="fas fa-user"></i> Login ID<span style="color:red;">*</span></label>
        <input type="text" id="userId" name="username" placeholder="Enter Login Id"
               onkeydown="return /[A-Za-z0-9._]/.test(event.key)" required>

        <label><i class="fas fa-lock"></i> Password<span style="color:red;">*</span></label>
        <input type="password" id="password" name="password" placeholder="Enter Password" required>

        <label><i class="fas fa-shield-alt"></i> Captcha<span style="color:red;">*</span></label>
        <input type="hidden" id="captchaCode" name="captchaCode" value="${captchaCode}">
        <div class="captcha-section">
          <input type="text" id="captcha" name="captcha" placeholder="Enter Captcha" required>
          <img id="captcha_Image" alt="Captcha Image" src="data:image/png;base64,${captchaImage}">
          <div><span class="fas fa-sync" onclick="refreshCaptcha()" style="cursor:pointer;"></span></div>
        </div>

        <a href="forgot" style="font-size: 13px; color: #000;">Forgot Login ID / Password?</a>

        <button type="submit"><i class="fas fa-sign-in-alt"></i> Sign In</button>
      </form>
    </div>
  </div>

  <div class="container overlay-box equal-box">
    <div class="overlay">
     <div class="overlay-panel overlay-right">
  <h2><i class="fas fa-chevron-circle-down"></i> Explore More Sections!</h2>

  <div class="faq"><i class="fas fa-question-circle"></i> <a href="exploreMore?selectedTab=tab1">FAQ</a></div>
  <div class="faq"><i class="fas fa-flag"></i> <a href="exploreMore?selectedTab=tab2">Acts & Rules</a></div>
  <div class="faq"><i class="fas fa-exclamation-circle"></i> <a href="exploreMore?selectedTab=tab3">Notifications</a></div>
  <div class="faq"><i class="fas fa-file-pdf"></i> <a href="exploreMore?selectedTab=tab5">Circulars</a></div>
  <div class="faq"><i class="fas fa-link"></i> <a href="exploreMore?selectedTab=tab4">Other Website Links</a></div>
</div>
     
    </div>
  </div>
</div>

<footer class="footer">
  &copy; 2023-2024 <a href="/" style="color:#3c97bf;">Govt of Himachal Pradesh</a>. All rights reserved.
</footer>

<script>
  $('form').on('submit', function (e) {
    e.preventDefault();
    var captcha = $("#captcha").val();
    var captchaCode = $("#captchaCode").val();
    $.ajax({
      url: 'checkCaptcha',
      method: 'get',
      async: false,
      data: {
        captcha: captcha,
        captchaCode: captchaCode
      },
      success: function (result) {
        if (result === 'true') {
          e.currentTarget.submit();
        } else {
          $.confirm({
            title: 'CAPTCHA error!',
            content: 'Captcha does not match! Please retry.',
            type: 'red',
            icon: 'fas fa-exclamation-triangle',
            animation: 'scale',
            closeAnimation: 'scale',
            boxWidth: '400px',
            useBootstrap: false
          });
          refreshCaptcha();
        }
      }
    });
  });

  function refreshCaptcha() {
    $.ajax({
      url: "captcha",
      success: function (result) {
        const myJSON = JSON.parse(result);
        $('#captcha_Image').attr("src", 'data:image/png;base64,' + myJSON.captchaImage);
        $("#captchaCode").val(myJSON.captchaCode);
        $("#captcha").val("");
      }
    });
  }

  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', function (e) {
    if ((e.ctrlKey && e.key === 'u') || e.key === 'F12' || e.key === 'F5' || (e.ctrlKey && e.key === 'r')) {
      e.preventDefault();
    }
  });
</script>

</body>
</html>
