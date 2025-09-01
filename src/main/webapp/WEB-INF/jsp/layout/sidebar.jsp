<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

 <!-- ========== SIDEBAR (AdminLTE 4) ========== -->
  <aside class="app-sidebar bg-dark text-white elevation-4" style="background-image:url(/static/dist/img/pattern_h.png); background-color:#15283c;">
    <!-- Brand block inside sidebar -->
    <div class="sidebar-brand d-flex align-items-center p-3 border-bottom">
      <a href="/" class="d-flex align-items-center text-decoration-none text-white">
        <img src="/static/files/hp_logo.png" alt="HP GST Logo"
             class="brand-image img-circle elevation-3 me-2" style="opacity:.85; width:44px; height:44px;">
        <span class="brand-text fw-bold">HP GST</span>
      </a>
    </div>

    <!-- Menu -->
    <div class="app-sidebar-menu p-2">
      <nav>
        <ul class="nav nav-pills flex-column" role="menu">
          <li class="nav-item">
            <a href="/welcome" class="nav-link d-flex align-items-center">
              <i class="nav-icon fas fa-home me-2"></i>
              <span>Home <c:out value="${user}"/></span>
            </a>
          </li>

          <!-- Dynamic menu -->
          <c:forEach items="${MenuList}" var="data">
            <li class="nav-item">
              <a href="/${data.userType}/${data.url}"
                 class="nav-link d-flex align-items-center <c:if test='${data.url == activeMenu}'>active</c:if>">
                <i class="nav-icon fas ${data.icon} me-2"></i>
                <span><c:out value="${data.name}"/></span>
              </a>
            </li>
          </c:forEach>
        </ul>
      </nav>
    </div>
  </aside>


