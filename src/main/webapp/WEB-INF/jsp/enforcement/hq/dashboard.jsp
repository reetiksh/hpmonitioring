<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST | Dashboard</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png"/>

  <!-- AdminLTE 4 / Bootstrap 5 core -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css"/>
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css"/>

  <!-- Optional plugins (keep only what you use) -->
  <link rel="stylesheet" href="/static/plugins/overlayScrollbars/css/OverlayScrollbars.min.css"/>
  <link rel="stylesheet" href="/static/plugins/daterangepicker/daterangepicker.css"/>
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css"/>
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- ====================== HEADER ====================== -->
  <header class="app-header navbar navbar-expand bg-dark border-bottom">
    <!-- Sidebar toggle -->
    <div class="navbar-nav">
      <a class="nav-link" data-lte-toggle="sidebar" href="#" role="button" aria-label="Toggle sidebar">
        <i class="fas fa-bars text-white"></i>
      </a>
    </div>

    <!-- Right navbar -->
    <ul class="navbar-nav ms-auto">
      <!-- User dropdown -->
      <li class="nav-item dropdown">
        <a class="nav-link" data-bs-toggle="dropdown" href="#"><i class="far fa-user text-white"></i></a>
        <div class="dropdown-menu dropdown-menu-end">
          <span class="dropdown-item dropdown-header">${UserLoginName}</span>
          <div class="dropdown-divider"></div>
          <a href="${commonUserDetails}" class="dropdown-item"><i class="fas fa-id-card me-2"></i>User Details</a>
          <div class="dropdown-divider"></div>
          <a href="${changeUserPassword}" class="dropdown-item"><i class="fas fa-key me-2"></i>Change Password</a>
          <div class="dropdown-divider"></div>
          <a href="/logout" class="dropdown-item dropdown-footer text-danger"><i class="fas fa-sign-out-alt me-2"></i>Logout</a>
        </div>
      </li>

      <!-- Fullscreen -->
      <li class="nav-item">
        <a class="nav-link" data-lte-toggle="fullscreen" href="#" role="button" aria-label="Fullscreen">
          <i class="fas fa-expand-arrows-alt text-white"></i>
        </a>
      </li>

      <!-- Notifications -->
      <li class="nav-item dropdown">
        <a class="nav-link" data-bs-toggle="dropdown" href="#">
          <i class="far fa-bell text-white"></i>
          <c:choose>
            <c:when test="${not empty unReadNotificationListCount}">
              <span class="badge bg-warning navbar-badge" id="notificationCount">${unReadNotificationListCount}</span>
            </c:when>
            <c:otherwise>
              <span class="badge bg-warning navbar-badge" id="notificationCount">0</span>
            </c:otherwise>
          </c:choose>
        </a>
        <div class="dropdown-menu dropdown-menu-lg dropdown-menu-end" style="max-height:300px; overflow-y:auto;">
          <span class="dropdown-item dropdown-header">Notifications</span>
          <div class="dropdown-divider"></div>
          <c:choose>
            <c:when test="${not empty loginedUserNotificationList}">
              <c:forEach items="${loginedUserNotificationList}" var="object">
                <button class="dropdown-item text-wrap" onclick="showHighlightedNotification('${object.description}');">
                  ${object.description}
                </button>
                <div class="dropdown-divider"></div>
              </c:forEach>
            </c:when>
            <c:otherwise>
              <span class="dropdown-item dropdown-header">No notifications available</span>
            </c:otherwise>
          </c:choose>
        </div>
      </li>
    </ul>
  </header>

  <!-- ====================== SIDEBAR ====================== -->
  <aside id="appSidebar"
         class="app-sidebar bg-dark text-white elevation-4"
         style="background-image:url(/static/dist/img/pattern_h.png); background-color:#15283c;">

    <!-- Brand (neutral container, not app-header) -->
    <div class="sidebar-brand d-flex align-items-center p-3 border-bottom">
      <a href="/" class="d-flex align-items-center text-decoration-none text-white">
        <img src="/static/files/hp_logo.png"
             alt="HP GST Logo"
             class="brand-image img-circle elevation-3 me-2"
             style="opacity:.8; width:50px; height:50px;">
        <span class="brand-text fw-bold">HP GST</span>
      </a>
    </div>

    <!-- (Optional) Home link / user -->
    <div class="p-3 pb-2 border-bottom">
      <a href="/welcome" class="text-white text-decoration-none d-inline-flex align-items-center">
        <i class="fas fa-home me-2"></i>
        <span>Home <c:out value="${user}"/></span>
      </a>
    </div>

    <!-- Sidebar Menu -->
    <div class="app-sidebar-menu p-2">
      <nav aria-label="Sidebar Navigation">
        <ul class="nav nav-pills flex-column" role="menu" data-accordion="false">
          <c:forEach items="${MenuList}" var="data">
            <li class="nav-item">
              <a href="/${data.userType}/${data.url}"
                 class="nav-link d-flex align-items-center <c:if test='${data.url == activeMenu}'>active</c:if>"
                 aria-current="<c:if test='${data.url == activeMenu}'>page</c:if>">
                <i class="nav-icon fas ${data.icon} me-2"></i>
                <span class="mb-0"><c:out value="${data.name}"/></span>
              </a>
            </li>
          </c:forEach>
        </ul>
      </nav>
    </div>
  </aside>

  <!-- ====================== MAIN CONTENT ====================== -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page Header -->
        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6"><h1 class="m-0">Dashboard</h1></div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="#">Home</a></li>
                <li class="breadcrumb-item active">Dashboard</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- Small Boxes -->
        <div class="row g-3">
          <div class="col-lg-3 col-6">
            <div class="small-box bg-primary text-white">
              <div class="inner">
                <h3>${totalEnforcementCases}</h3>
                <p>Count of Total Cases</p>
              </div>
              <div class="icon"><i class="fas fa-clipboard-list"></i></div>
              <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
            </div>
          </div>

          <div class="col-lg-3 col-6">
            <div class="small-box bg-warning text-dark">
              <div class="inner">
                <h3>${countOfActiontakenCases}</h3>
                <p>Total Action Taken On</p>
              </div>
              <div class="icon"><i class="fas fa-edit"></i></div>
              <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
            </div>
          </div>

          <div class="col-lg-3 col-6">
            <div class="small-box bg-info text-white">
              <div class="inner">
                <h3><i class="fas fa-rupee-sign"></i> ${totalSumOfIndicativeTaxValue}/-</h3>
                <p>Total Indicative Tax Value</p>
              </div>
              <div class="icon"><i class="fas fa-money-bill-wave"></i></div>
              <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
            </div>
          </div>

          <div class="col-lg-3 col-6">
            <div class="small-box bg-secondary text-white">
              <div class="inner">
                <h3>${totalEnforcementCasesInLast3Months}</h3>
                <p>Total Uploaded Cases in Last 3 Months</p>
              </div>
              <div class="icon"><i class="fas fa-chart-line"></i></div>
              <a href="#" class="small-box-footer">More info <i class="fas fa-arrow-circle-right"></i></a>
            </div>
          </div>
        </div>

        <!-- Filters -->
        <form method="GET" action="/enforcement_hq/dashboard" name="dashboard" id="dashboard" class="mt-3">
          <div class="row g-3">
            <div class="col-md-6">
              <label class="form-label">Parameter <span class="text-danger">*</span></label>
              <select id="category" name="category" class="form-select" required>
                <option value="-1">Please Select</option>
                <c:forEach items="${categories}" var="cat">
                  <option value="${cat[0]}" <c:if test='${cat[0] == category}'>selected</c:if>>${cat[1]}</option>
                </c:forEach>
              </select>
            </div>
            <div class="col-md-6">
              <label class="form-label">Financial Year</label>
              <select id="financialyear" name="financialyear" class="form-select">
                <option value="">Please Select</option>
                <c:forEach items="${financialyearlist}" var="financialyear">
                  <option value="${financialyear}" <c:if test='${financialyear == year}'>selected</c:if>>${financialyear}</option>
                </c:forEach>
              </select>
            </div>
          </div>
          <div class="row mt-3"><div class="col-12 text-center">
            <button type="submit" class="btn btn-primary">Submit</button>
          </div></div>
        </form>

        <!-- Dashboard Summary Table -->
        <div class="card mt-4">
          <div class="card-header"><h3 class="card-title">Dashboard Summary</h3></div>
          <div class="card-body">
            <div class="table-responsive">
              <table class="table table-bordered table-hover align-middle">
                <thead class="table-light">
                  <tr>
                    <th>Zone</th><th>Circle</th><th>Indicative Tax</th><th>Allotted</th>
                    <th>Completed</th><th>Not Acknowledged</th><th>Transferred</th>
                    <th>Acknowledged</th><th>Panchnama</th><th>Preliminary Report</th>
                    <th>Final Report</th><th>Adjudication</th><th>Show Cause</th>
                  </tr>
                </thead>
                <tbody>
                  <c:forEach var="row" items="${dashboardSummary}">
                    <tr>
                      <td>${row.zoneName}</td>
                      <td>${row.circleName}</td>
                      <td>${row.indicativeTaxValue}</td>
                      <td>${row.allottedCases}</td>
                      <td>${row.casesCompleted}</td>
                      <td>${row.notAcknowledged}</td>
                      <td>${row.transferToScrutiny}</td>
                      <td>${row.acknowledged}</td>
                      <td>${row.panchnama}</td>
                      <td>${row.preliminaryReport}</td>
                      <td>${row.finalReport}</td>
                      <td>${row.referToAdjudication}</td>
                      <td>${row.showCauseIssued}</td>
                    </tr>
                  </c:forEach>
                </tbody>
              </table>
            </div>
          </div>
        </div>

      </div><!-- /.container-fluid -->
    </div><!-- /.app-content -->
  </main>

  <!-- ====================== FOOTER ====================== -->
  <footer class="main-footer">
    <strong>Copyright &copy; 2023-2024 <a href="/">Govt of Himachal Pradesh</a>.</strong>
    All rights reserved.
    <div class="float-end d-none d-sm-inline"><!-- <b>Version</b> 4.0.0 --></div>
  </footer>

</div><!-- /.app-wrapper -->

<!-- ====================== SCRIPTS (order matters; load once) ====================== -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<!-- Optional scripts -->
<script src="/static/plugins/moment/moment.min.js"></script>
<script src="/static/plugins/daterangepicker/daterangepicker.js"></script>
<script src="/static/plugins/overlayScrollbars/js/jquery.overlayScrollbars.min.js"></script>
<script src="/static/plugins/chart.js/Chart.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  // Optional: notification modal helper
  function showHighlightedNotification(txt){
    const el = document.getElementById('notificationText');
    if (el) el.textContent = txt;
    const mEl = document.getElementById('notificationHightLightModel');
    if (mEl && window.bootstrap) new bootstrap.Modal(mEl).show();
  }

  // Basic hardening
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = e.key.toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });
</script>

<!-- Optional: notification modal markup -->
<div class="modal fade" id="notificationHightLightModel" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div>
        <div class="modal-header">
          <h5 class="modal-title">Notification</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body"><p id="notificationText"></p></div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
