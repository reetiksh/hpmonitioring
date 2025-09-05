<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST |Review Case Summary</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">

  <!-- Fonts / Icons -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">

  <!-- AdminLTE 4 / Bootstrap 5 -->
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">

  <!-- DataTables (Bootstrap 5 builds) -->
  <link rel="stylesheet" href="/static/plugins/datatables-bs5/css/dataTables.bootstrap5.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap5.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap5.min.css">

  <!-- Optional -->
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
</head>
<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">
  <div class="preloader flex-column justify-content-center align-items-center">
    <img class="animation__shake" src="/static/dist/img/AdminLTELogo.png" alt="AdminLTELogo" height="60" width="60">
  </div>

  <!-- keep existing includes exactly as-is -->
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
              <h1 class="m-0">Review Case Summary</h1>
            </div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/vw/dashboard">Home</a></li>
                <li class="breadcrumb-item active">Review Case Summary</li>
              </ol>
            </div>
          </div>
        </div>

        <section class="content">
          <div class="row">
            <div class="col-12">
              <div class="card card-primary">
                <div class="card-header">
                  <h3 class="card-title mb-0">Review Case Summary List</h3>
                </div>
                <div class="card-body">

                  <div class="row">
                    <div class="col-md-6 form-group">
                      <label>Category<span style="color: red;"> *</span></label>
                      <select id="category" name="category" class="selectpicker col-md-8" data-live-search="true" title="Please Select Category">
                        <c:forEach items="${categories}" var="category">
                          <c:choose>
                            <c:when test="${category.id eq categoryId}">
                              <option value="${category.id}" selected="selected">${category.name}</option>
                            </c:when>
                            <c:otherwise>
                              <option value="${category.id}">${category.name}</option>
                            </c:otherwise>
                          </c:choose>
                        </c:forEach>
                      </select>
                    </div>

                    <c:if test="${!empty parameters}">
                      <div class="col-md-6 form-group">
                        <label>Parameter<span style="color: red;"> *</span></label>
                        <select id="parameter" name="parameter" class="selectpicker col-md-8" data-live-search="true" title="Please Select Paramater">
                          <option value="0">Select All Parameter</option>
                          <c:forEach items="${parameters}" var="parameter">
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
                    </c:if>
                  </div>

                  <c:if test="${!empty categoryTotals}">
                    <div class="table-responsive">
                      <table id="example1" class="table table-bordered table-striped w-100">
                        <thead>
                        <tr>
                          <th style="text-align: center; vertical-align: middle;">Category</th>
                          <th style="text-align: center; vertical-align: middle;">Parameter</th>
                          <th style="text-align: center; vertical-align: middle;">Total Cases</th>
                          <th style="text-align: center; vertical-align: middle;">Total Indicative Value(₹)</th>
                          <th style="text-align: center; vertical-align: middle;">Total Amount(₹)</th>
                          <th style="text-align: center; vertical-align: middle;">Total Demand(₹)</th>
                          <th style="text-align: center; vertical-align: middle;">Total Recovery(₹)</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${categoryTotals}" var="total">
                          <tr>
                            <td style="text-align: center; vertical-align: middle;">
                              <a href="/vw/view_list_of_case?category=${total[0]}&parameterId=${total[7]}"><c:out value="${total[0]}" /></a>
                            </td>
                            <td style="text-align: center; vertical-align: middle;"><c:out value="${total[1]}" /></td>
                            <td style="text-align: center; vertical-align: middle;"><c:out value="${total[2]}" /></td>
                            <td style="text-align: center; vertical-align: middle;"><fmt:formatNumber value="${total[3]}" pattern="#,##,##0"/></td>
                            <td style="text-align: center; vertical-align: middle;"><fmt:formatNumber value="${total[4]}" pattern="#,##,##0"/></td>
                            <td style="text-align: center; vertical-align: middle;"><fmt:formatNumber value="${total[6]}" pattern="#,##,##0"/></td>
                            <td style="text-align: center; vertical-align: middle;"><fmt:formatNumber value="${total[5]}" pattern="#,##,##0"/></td>
                          </tr>
                        </c:forEach>
                        </tbody>
                      </table>
                    </div>
                  </c:if>

                  <c:if test="${empty categoryTotals}">
                    <div class="col-12 text-center">
                      <i class="fa fa-info-circle" style="font-size:100px;color:rgb(97, 97, 97)" aria-hidden="true"></i><br>
                      <span style="font-size:35px;color:rgb(97, 97, 97)">No Review Case List Available</span>
                    </div>
                  </c:if>

                </div>
              </div>
            </div>
          </div>

        </section>
      </div>
    </div>
  </main>

  <jsp:include page="../layout/footer.jsp"/>

</div>

<!-- overlay for mobile sidebar (tap outside to close) -->
<div class="sidebar-overlay" data-lte-dismiss="sidebar"></div>

<!-- Scripts -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<!-- DataTables (Bootstrap 5 builds) -->
<script src="/static/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="/static/plugins/datatables-bs5/js/dataTables.bootstrap5.min.js"></script>
<script src="/static/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="/static/plugins/datatables-responsive/js/responsive.bootstrap5.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/dataTables.buttons.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.bootstrap5.min.js"></script>
<script src="/static/plugins/jszip/jszip.min.js"></script>
<script src="/static/plugins/pdfmake/pdfmake.min.js"></script>
<script src="/static/plugins/pdfmake/vfs_fonts.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.html5.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.print.min.js"></script>

<!-- Optional -->
<script src="/static/dist/js/jquery-confirm.min.js"></script>
<script src="/static/dist/js/bootstrap-select.min.js"></script>

<script>
document.addEventListener('contextmenu', function(e){ e.preventDefault(); });
document.addEventListener('keydown', function(e){
  const k = e.key.toLowerCase();
  if ((e.ctrlKey && k === 'u') || k === 'f12') e.preventDefault();
});
$(document).ready(function(){
  function disableBack(){ window.history.forward(); }
  window.onload = disableBack();
  window.onpageshow = function(evt){ if (evt.persisted) disableBack(); }
});
document.onkeydown = function(e){
  const k = e.key.toLowerCase();
  if (k === 'f5' || (e.ctrlKey && k === 'r') || e.keyCode === 116) e.preventDefault();
};

// Keep content margin synced with real sidebar width so toggle works (ALTE4)
(function setSidebarVar(){
  const sb = document.querySelector('.app-sidebar');
  const collapsed = document.body.classList.contains('sidebar-collapse');
  const px = collapsed ? 0 : (sb ? sb.offsetWidth : 210);
  document.documentElement.style.setProperty('--hp-sidebar-width', px + 'px');
  new MutationObserver(() => {
    const c = document.body.classList.contains('sidebar-collapse');
    const v = c ? 0 : (sb ? sb.offsetWidth : 210);
    document.documentElement.style.setProperty('--hp-sidebar-width', v + 'px');
  }).observe(document.body, {attributes:true, attributeFilter:['class']});
  window.addEventListener('resize', () => {
    const v = document.body.classList.contains('sidebar-collapse') ? 0 : (sb ? sb.offsetWidth : 210);
    document.documentElement.style.setProperty('--hp-sidebar-width', v + 'px');
  });
})();

// DataTables
$(function () {
  $("#example1").DataTable({
    responsive: true,
    lengthChange: false,
    autoWidth: true,
    buttons: ["excel","pdf","print"]
  }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');

  $('#example2').DataTable({
    paging: true,
    lengthChange: false,
    searching: false,
    ordering: true,
    info: true,
    autoWidth: true,
    responsive: true
  });
});

// Filters (preserve existing logic)
$(document).ready(function () {
  $("#category").change(function(){
    var categoryId = $(this).val();
    window.location.href = '/vw/review_summary_list?categoryId=' + categoryId;
  });
  $("#parameter").change(function(){
    var parameterId = $(this).val();
    var categoryId = $("#category").val();
    window.location.href = '/vw/review_summary_list?parameterId=' + parameterId + '&categoryId=' + categoryId;
  });
});
</script>
</body>
</html>
