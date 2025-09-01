  <header class="app-header navbar navbar-expand bg-dark border-bottom">
    <!-- Sidebar toggle (left) -->
    <div class="navbar-nav">
      <a class="nav-link" data-lte-toggle="sidebar" href="#" role="button" aria-label="Toggle sidebar">
        <i class="fas fa-bars text-white"></i>
      </a>
    </div>

    <!-- Brand (center-left) -->
    <div class="navbar-brand ms-2 text-white fw-bold d-none d-sm-inline">
      <img src="/static/files/hp_logo.png" alt="HP GST" style="height:28px;width:28px;border-radius:50%;margin-right:.5rem;vertical-align:middle;">
      HP GST
    </div>

    <!-- Right navbar -->
    <ul class="navbar-nav ms-auto">
      <!-- (sample) icons on the right for parity with your layout -->
      <li class="nav-item"><a class="nav-link text-white" href="#"><i class="far fa-user"></i></a></li>
      <li class="nav-item dropdown">
        <a class="nav-link text-white" data-bs-toggle="dropdown" href="#"><i class="far fa-bell"></i></a>
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
