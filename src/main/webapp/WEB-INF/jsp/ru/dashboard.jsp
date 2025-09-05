<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST | Dashboard</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png"/>

  <!-- AdminLTE 4 / Bootstrap 5 (CDN for guaranteed versions) -->
  <link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.5.2/css/all.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/css/adminlte.min.css" rel="stylesheet"/>

  <!-- Optional plugins (only if you use them) -->
  <link href="https://cdn.jsdelivr.net/npm/overlayscrollbars@2.7.3/styles/overlayscrollbars.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" rel="stylesheet"/>
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

    <!-- Brand -->
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
                <h3>${verificationTotalCases}</h3>
                <p>Total Cases received for verifications</p>
              </div>
              <div class="icon"><i class="fas fa-clipboard-list"></i></div>
              <a href="#" class="small-box-footer">&nbsp;</a>
            </div>
          </div>

          <div class="col-lg-3 col-6">
            <div class="small-box bg-success text-white">
              <div class="inner">
                <h3>${pendingVerificationTotalCases}</h3>
                <p>Cases Pending for Verification</p>
              </div>
              <div class="icon"><i class="fas fa-tasks"></i></div>
              <a href="#" class="small-box-footer">&nbsp;</a>
            </div>
          </div>

          <div class="col-lg-3 col-6">
            <div class="small-box bg-info text-white">
              <div class="inner">
                <h3>${dashBoardTotalCases}</h3>
                <p>Total Cases</p>
              </div>
              <div class="icon"><i class="fas fa-list"></i></div>
              <a href="#" class="small-box-footer">&nbsp;</a>
            </div>
          </div>

          <div class="col-lg-3 col-6">
            <div class="small-box bg-success text-white">
              <div class="inner">
                <h3>${dashBoardTotalAcknowledgedCases}</h3>
                <p>Cases Acknowledged</p>
              </div>
              <div class="icon"><i class="fas fa-check-circle"></i></div>
              <a href="#" class="small-box-footer">&nbsp;</a>
            </div>
          </div>

          <div class="col-lg-3 col-6">
            <div class="small-box bg-primary text-white">
              <div class="inner">
                <h3>${dashBoardTotalInitiatedCases}</h3>
                <p>Cases Initiated</p>
              </div>
              <div class="icon"><i class="fas fa-play"></i></div>
              <a href="#" class="small-box-footer">&nbsp;</a>
            </div>
          </div>

          <div class="col-lg-3 col-6">
            <div class="small-box bg-warning">
              <div class="inner">
                <h3>${dashBoardTotalCasesClosedByFo}</h3>
                <p>Cases Closed by FO</p>
              </div>
              <div class="icon"><i class="fas fa-folder-minus"></i></div>
              <a href="#" class="small-box-footer">&nbsp;</a>
            </div>
          </div>

          <div class="col-lg-3 col-6">
            <div class="small-box bg-danger text-white">
              <div class="inner">
                <h3>${dashBoardTotalSuspectedIndicativeAmount}</h3>
                <p>Total Suspected Indicative Amount</p>
              </div>
              <div class="icon"><i class="fas fa-rupee-sign"></i></div>
              <a href="#" class="small-box-footer">&nbsp;</a>
            </div>
          </div>

          <div class="col-lg-3 col-6">
            <div class="small-box bg-dark text-white">
              <div class="inner">
                <h3>${dashBoardTotalAmount}</h3>
                <p>Total Amount</p>
              </div>
              <div class="icon"><i class="fas fa-database"></i></div>
              <a href="#" class="small-box-footer">&nbsp;</a>
            </div>
          </div>

          <div class="col-lg-3 col-6">
            <div class="small-box bg-secondary text-white">
              <div class="inner">
                <h3>${dashBoardTotalDemand}</h3>
                <p>Total Demand</p>
              </div>
              <div class="icon"><i class="fas fa-file-invoice-dollar"></i></div>
              <a href="#" class="small-box-footer">&nbsp;</a>
            </div>
          </div>

          <div class="col-lg-3 col-6">
            <div class="small-box bg-light">
              <div class="inner">
                <h3>${dashBoardTotalRecovery}</h3>
                <p>Total Recovery</p>
              </div>
              <div class="icon"><i class="fas fa-hand-holding-usd"></i></div>
              <a href="#" class="small-box-footer">&nbsp;</a>
            </div>
          </div>
        </div>

        <!-- Filters -->
        <form method="GET" action="dashboard" name="dashboard" id="dashboard" class="mt-2">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
          <div class="row g-3">
            <div class="col-md-6">
              <label class="form-label">Category <span class="text-danger">*</span></label>
              <select id="category" name="category" class="form-select" required>
                <option value="">Please Select</option>
                <c:forEach items="${categories}" var="categories">
                  <option value="${categories.id}" <c:if test="${categories.id eq category}">selected</c:if>>
                    ${categories.name}
                  </option>
                </c:forEach>
              </select>
            </div>

            <div class="col-md-6">
              <label class="form-label">Financial Year</label>
              <select id="financialyear" name="financialyear" class="form-select">
                <option value="">Please Select</option>
                <c:forEach items="${financialyear}" var="financialyear">
                  <option value="${financialyear}" <c:if test="${financialyear == year}">selected</c:if>>
                    ${financialyear}
                  </option>
                </c:forEach>
              </select>
            </div>

            <div class="col-md-6">
              <label class="form-label">View <span class="text-danger">*</span></label>
              <select id="view" name="view" class="form-select" required>
                <option value="">Please Select</option>
                <option value="circleWise" <c:if test="${viewtype eq 'circleWise'}">selected</c:if>>Circle-wise</option>
                <%-- <option value="zoneWise"  <c:if test="${viewtype eq 'zoneWise'}">selected</c:if>>Zone-wise</option> --%>
              </select>
            </div>

            <div class="col-md-6">
              <label class="form-label">Parameter</label>
              <select id="parameter" name="parameter" class="form-select">
                <option value="0">Select All Parameter</option>
                <c:forEach items="${parameterList}" var="parameter">
                  <c:choose>
                    <c:when test="${parameter.id eq parameterId}">
                      <option value="${parameter.id}" selected="selected">${parameter.paramName}</option>
                    </c:when>
                    <c:otherwise>
                      <option value="${parameter.id}">${parameter.paramName}</option>
                    </c:otherwise>
                  </c:choose>
                </c:forEach>
              </select>
            </div>

            <div class="col-12 text-center">
              <button type="submit" class="btn btn-primary">Submit</button>
            </div>
          </div>
        </form>

        <!-- ================== Circle-wise Table ================== -->
        <c:if test="${not empty viewtype && viewtype == 'circleWise'}">
          <div class="card mt-4">
            <div class="card-header"><h3 class="card-title">Circle-wise Summary</h3></div>
            <div class="card-body">
              <div class="table-responsive">
                <table id="circleTable" class="table table-bordered table-hover align-middle">
                  <thead class="table-light">
                  <tr>
                    <th rowspan="2" class="text-center align-middle">Circle</th>
                    <th rowspan="2" class="text-center align-middle">No. of Cases</th>
                    <th rowspan="2" class="text-center align-middle">Suspected Indicative Tax Value (₹)</th>
                    <th rowspan="2" class="text-center align-middle">Demand Created (₹)</th>
                    <th rowspan="2" class="text-center align-middle">Amount Recovered (₹)</th>
                    <th rowspan="2" class="text-center align-middle">Yet to be Acknowledge</th>
                    <th rowspan="2" class="text-center align-middle">Yet to be Initiated</th>
                    <th colspan="7" class="text-center align-middle">Case Stage</th>
                  </tr>
                  <tr>
                    <th class="text-center align-middle">DRC-01A issued</th>
                    <th class="text-center align-middle">ASMT-10 issued</th>
                    <th class="text-center align-middle">DRC01 issued</th>
                    <th class="text-center align-middle">Demand Created via DRC07</th>
                    <th class="text-center align-middle">Case Dropped</th>
                    <th class="text-center align-middle">Partial Voluntary Payment Remaining Demand</th>
                    <th class="text-center align-middle">Demand Created However Discharged via DRC-03</th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:set var="totalnoOfCase" value="${0}" />
                  <c:set var="totalindicativeValue" value="${0}" />
                  <c:set var="totaldemand" value="${0}" />
                  <c:set var="totalrecoveredAmount" value="${0}" />
                  <c:set var="totaldrc01aissued" value="${0}" />
                  <c:set var="totalamst10issued" value="${0}" />
                  <c:set var="totaldrc01issued" value="${0}" />
                  <c:set var="totalnotAcknowledege" value="${0}" />
                  <c:set var="totaldemandByDrc07" value="${0}" />
                  <c:set var="totalcaseDroped" value="${0}" />
                  <c:set var="totalnotApplicable" value="${0}" />
                  <c:set var="totalpartialVoluentryDemand" value="${0}" />
                  <c:set var="totaldemandDischageByDrc03" value="${0}" />

                  <c:forEach items="${circleList}" var="circleList">
                    <tr>
                      <td>${circleList.juristiction}</td>
                      <td>${circleList.noOfCase}</td>

                      <c:set var="totalnoOfCase" value="${totalnoOfCase + circleList.noOfCase}" />
                      <c:set var="totalindicativeValue" value="${totalindicativeValue + circleList.indicativeValue}" />
                      <c:set var="totaldemand" value="${totaldemand + circleList.demand}" />
                      <c:set var="totalrecoveredAmount" value="${totalrecoveredAmount + circleList.recoveredAmount}" />
                      <c:set var="totaldrc01aissued" value="${totaldrc01aissued + circleList.drc01aissued}" />
                      <c:set var="totalamst10issued" value="${totalamst10issued + circleList.amst10issued}" />
                      <c:set var="totaldrc01issued" value="${totaldrc01issued + circleList.drc01issued}" />
                      <c:set var="totalnotAcknowledege" value="${totalnotAcknowledege + circleList.notAcknowledege}" />
                      <c:set var="totaldemandByDrc07" value="${totaldemandByDrc07 + circleList.demandByDrc07}" />
                      <c:set var="totalcaseDroped" value="${totalcaseDroped + circleList.caseDroped}" />
                      <c:set var="totalnotApplicable" value="${totalnotApplicable + circleList.notApplicable}" />
                      <c:set var="totalpartialVoluentryDemand" value="${totalpartialVoluentryDemand + circleList.partialVoluentryDemand}" />
                      <c:set var="totaldemandDischageByDrc03" value="${totaldemandDischageByDrc03 + circleList.demandDischageByDrc03}" />

                      <td>${circleList.indicativeValue}</td>
                      <td>${circleList.demand}</td>
                      <td>${circleList.recoveredAmount}</td>
                      <td>${circleList.notAcknowledege}</td>
                      <td>${circleList.notApplicable}</td>

                      <td>${circleList.drc01aissued}</td>
                      <td>${circleList.amst10issued}</td>
                      <td>${circleList.drc01issued}</td>
                      <td>${circleList.demandByDrc07}</td>
                      <td>${circleList.caseDroped}</td>
                      <td>${circleList.partialVoluentryDemand}</td>
                      <td>${circleList.demandDischageByDrc03}</td>
                    </tr>
                  </c:forEach>
                  </tbody>

                  <tfoot>
                  <tr>
                    <th>Grand Total</th>
                    <th>${totalnoOfCase}</th>
                    <th>${totalindicativeValue}</th>
                    <th>${totaldemand}</th>
                    <th>${totalrecoveredAmount}</th>
                    <th>${totalnotAcknowledege}</th>
                    <th>${totalnotApplicable}</th>
                    <th>${totaldrc01aissued}</th>
                    <th>${totalamst10issued}</th>
                    <th>${totaldrc01issued}</th>
                    <th>${totaldemandByDrc07}</th>
                    <th>${totalcaseDroped}</th>
                    <th>${totalpartialVoluentryDemand}</th>
                    <th>${totaldemandDischageByDrc03}</th>
                  </tr>
                  </tfoot>
                </table>
              </div>
            </div>
          </div>
        </c:if>

        <!-- ================== Zone-wise Table ================== -->
        <c:if test="${not empty viewtype && viewtype == 'zoneWise'}">
          <div class="card mt-4">
            <div class="card-header"><h3 class="card-title">Zone-wise Summary</h3></div>
            <div class="card-body">
              <div class="table-responsive">
                <table id="zoneTable" class="table table-bordered table-hover align-middle">
                  <thead class="table-light">
                  <tr>
                    <th rowspan="2" class="text-center align-middle">Zone</th>
                    <th rowspan="2" class="text-center align-middle">No. of Cases</th>
                    <th rowspan="2" class="text-center align-middle">Suspected Indicative Tax Value (₹)</th>
                    <th rowspan="2" class="text-center align-middle">Demand Created (₹)</th>
                    <th rowspan="2" class="text-center align-middle">Amount Recovered (₹)</th>
                    <th rowspan="2" class="text-center align-middle">Yet to be Acknowledge</th>
                    <th rowspan="2" class="text-center align-middle">Yet to be Initiated</th>
                    <th colspan="7" class="text-center align-middle">Case Stage</th>
                  </tr>
                  <tr>
                    <th class="text-center align-middle">DRC-01A issued</th>
                    <th class="text-center align-middle">ASMT-10 issued</th>
                    <th class="text-center align-middle">DRC01 issued</th>
                    <th class="text-center align-middle">Demand Created via DRC07</th>
                    <th class="text-center align-middle">Case Dropped</th>
                    <th class="text-center align-middle">Partial Voluntary Payment Remaining Demand</th>
                    <th class="text-center align-middle">Demand Created However Discharged via DRC-03</th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:set var="totalnoOfCaseZone" value="${0}" />
                  <c:set var="totalindicativeValueZone" value="${0}" />
                  <c:set var="totaldemandZone" value="${0}" />
                  <c:set var="totalrecoveredAmountZone" value="${0}" />
                  <c:set var="totaldrc01aissuedZone" value="${0}" />
                  <c:set var="totalamst10issuedZone" value="${0}" />
                  <c:set var="totaldrc01issuedZone" value="${0}" />
                  <c:set var="totalnotAcknowledegeZone" value="${0}" />
                  <c:set var="totaldemandByDrc07Zone" value="${0}" />
                  <c:set var="totalcaseDropedZone" value="${0}" />
                  <c:set var="totalnotApplicableZone" value="${0}" />
                  <c:set var="totalpartialVoluentryDemandZone" value="${0}" />
                  <c:set var="totaldemandDischageByDrc03Zone" value="${0}" />

                  <c:forEach items="${zoneList}" var="zoneList">
                    <tr>
                      <td>${zoneList.juristiction}</td>
                      <td>${zoneList.noOfCase}</td>

                      <c:set var="totalnoOfCaseZone" value="${totalnoOfCaseZone + zoneList.noOfCase}" />
                      <c:set var="totalindicativeValueZone" value="${totalindicativeValueZone + zoneList.indicativeValue}" />
                      <c:set var="totaldemandZone" value="${totaldemandZone + zoneList.demand}" />
                      <c:set var="totalrecoveredAmountZone" value="${totalrecoveredAmountZone + zoneList.recoveredAmount}" />
                      <c:set var="totaldrc01aissuedZone" value="${totaldrc01aissuedZone + zoneList.drc01aissued}" />
                      <c:set var="totalamst10issuedZone" value="${totalamst10issuedZone + zoneList.amst10issued}" />
                      <c:set var="totaldrc01issuedZone" value="${totaldrc01issuedZone + zoneList.drc01issued}" />
                      <c:set var="totalnotAcknowledegeZone" value="${totalnotAcknowledegeZone + zoneList.notAcknowledege}" />
                      <c:set var="totaldemandByDrc07Zone" value="${totaldemandByDrc07Zone + zoneList.demandByDrc07}" />
                      <c:set var="totalcaseDropedZone" value="${totalcaseDropedZone + zoneList.caseDroped}" />
                      <c:set var="totalnotApplicableZone" value="${totalnotApplicableZone + zoneList.notApplicable}" />
                      <c:set var="totalpartialVoluentryDemandZone" value="${totalpartialVoluentryDemandZone + zoneList.partialVoluentryDemand}" />
                      <c:set var="totaldemandDischageByDrc03Zone" value="${totaldemandDischageByDrc03Zone + zoneList.demandDischageByDrc03}" />

                      <td>${zoneList.indicativeValue}</td>
                      <td>${zoneList.demand}</td>
                      <td>${zoneList.recoveredAmount}</td>
                      <td>${zoneList.notAcknowledege}</td>
                      <td>${zoneList.notApplicable}</td>
                      <td>${zoneList.drc01aissued}</td>
                      <td>${zoneList.amst10issued}</td>
                      <td>${zoneList.drc01issued}</td>
                      <td>${zoneList.demandByDrc07}</td>
                      <td>${zoneList.caseDroped}</td>
                      <td>${zoneList.partialVoluentryDemand}</td>
                      <td>${zoneList.demandDischageByDrc03}</td>
                    </tr>
                  </c:forEach>
                  </tbody>

                  <tfoot>
                  <tr>
                    <th>Grand Total</th>
                    <th>${totalnoOfCaseZone}</th>
                    <th>${totalindicativeValueZone}</th>
                    <th>${totaldemandZone}</th>
                    <th>${totalrecoveredAmountZone}</th>
                    <th>${totalnotAcknowledegeZone}</th>
                    <th>${totalnotApplicableZone}</th>
                    <th>${totaldrc01aissuedZone}</th>
                    <th>${totalamst10issuedZone}</th>
                    <th>${totaldrc01issuedZone}</th>
                    <th>${totaldemandByDrc07Zone}</th>
                    <th>${totalcaseDropedZone}</th>
                    <th>${totalpartialVoluentryDemandZone}</th>
                    <th>${totaldemandDischageByDrc03Zone}</th>
                  </tr>
                  </tfoot>
                </table>
              </div>
            </div>
          </div>
        </c:if>

      </div><!-- /.container-fluid -->
    </div><!-- /.app-content -->
  </main>

  <!-- ====================== FOOTER ====================== -->
  <footer class="main-footer">
    <strong>Copyright &copy; 2023-2024 <a href="/">Govt of Himachal Pradesh</a>.</strong>
    All rights reserved.
    <div class="float-end d-none d-sm-inline"></div>
  </footer>

</div><!-- /.app-wrapper -->

<!-- ====================== SCRIPTS (order matters) ====================== -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/js/adminlte.min.js"></script>

<!-- Optional scripts -->
<script src="https://cdn.jsdelivr.net/npm/moment@2.30.1/min/moment.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/daterangepicker@3.1/daterangepicker.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/overlayscrollbars@2.7.3/browser/overlayscrollbars.browser.es6.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<script>
  // Optional: notification modal helper
  function showHighlightedNotification(txt){
    const el = document.getElementById('notificationText');
    if (el) el.textContent = txt;
    const mEl = document.getElementById('notificationHightLightModel');
    if (mEl && window.bootstrap) new bootstrap.Modal(mEl).show();
  }

  // Hardening (unchanged behavior)
  document.addEventListener('contextmenu', e => e.preventDefault());
  document.addEventListener('keydown', e => {
    const k = (e.key || '').toLowerCase();
    if ((e.ctrlKey && k === 'u') || k === 'f12' || k === 'f5' || (e.ctrlKey && k === 'r')) e.preventDefault();
  });

  // (Optional) Shim for sidebar toggle on smaller screens if needed
  (function () {
    const mqDesktop = window.matchMedia('(min-width: 992px)');
    function toggleSidebar() {
      if (mqDesktop.matches) document.body.classList.toggle('sidebar-collapse');
      else document.body.classList.toggle('sidebar-open');
    }
    document.addEventListener('click', function (e) {
      const btn = e.target.closest('[data-lte-toggle="sidebar"]');
      if (!btn) return;
      e.preventDefault();
      toggleSidebar();
    });
  })();
</script>

<!-- Notification Modal -->
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
