<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard</title>
    <!-- Include your CSS and JavaScript files -->
    <link rel="stylesheet" href="/static/css/styles.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<aside class="main-sidebar sidebar-dark-primary elevation-4" style="background-image: url(/static/dist/img/pattern_h.png); background-color: #15283c;">
    <a href="index3.html" class="brand-link" style="height: 90px; background-color: #007bff; pointer-events: none;">
        <img src="/static/files/hp_logo.png" alt="AdminLTE Logo" class="brand-image img-circle elevation-3" style="opacity: .8; width: 70px; max-height: 70px;">
        <p></p>
        <span class="brand-text font-weight-dark">HP GST</span>
    </a>
    <div class="sidebar">
        <div class="user-panel mt-3 pb-3 mb-3 d-flex">
            <div class="image">
            </div>
            <div class="info">
                <%-- <a href="#" class="d-block">Welcome ${user}</a> --%>
                <a href="/welcome" class="d-block">Home ${user}</a>
            </div>
        </div>
        <nav class="mt-2">
    <ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false">
        <li class="nav-item">
            <a href="/vw/dashboard" class="nav-link">
                <i class="nav-icon fas fa-tachometer-alt"></i>
                <p>
                    Dashboard
                </p>
            </a>
            <!-- Submenu for Dashboard (if any) -->

        </li>
        <li class="nav-item ">
            <a href="#" class="nav-link">
                <i class="nav-icon fas fa-chart-line"></i>
                <p>
                    MIS
                    <i class="right fas fa-angle-left"></i>
                </p>
            </a>
            <!-- Submenu for MIS -->
            <ul class="nav nav-treeview">
                <li class="nav-item">
                    <a href="/vw/viewWiseReport" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>Case Pendency Status Report</p>
                    </a>
                </li>
                <!-- <li class="nav-item">
                    <a href="#" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>Report 2</p>
                    </a>
                </li>
                <li class="nav-item">
                    <a href="#" class="nav-link">
                        <i class="far fa-circle nav-icon"></i>
                        <p>Report 3</p>
                    </a>
                </li> -->
            </ul>
        </li>
    </ul>
</nav>
    </div>
</aside>

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

   
</script>
 <script>
    // jQuery to handle dropdown behavior
    $(document).ready(function() {
        // Toggle dropdown arrows
        $('.nav-item.has-treeview > a').click(function(e) {
            e.preventDefault();
            $(this).parent().toggleClass('menu-open');
        });
    });
</script>

</body>
</html>
