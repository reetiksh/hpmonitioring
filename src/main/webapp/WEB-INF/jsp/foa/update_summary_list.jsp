<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Update Status List</title>

  <!-- AdminLTE 4 / Bootstrap 5 / Font Awesome -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.5.2/css/all.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/css/adminlte.min.css" rel="stylesheet">

  <!-- DataTables (Bootstrap 5 integration) -->
  <link href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css" rel="stylesheet">
  <link href="https://cdn.datatables.net/responsive/2.5.0/css/responsive.bootstrap5.min.css" rel="stylesheet">
  <link href="https://cdn.datatables.net/buttons/2.4.1/css/buttons.bootstrap5.min.css" rel="stylesheet">

  <!-- Plugins you already use -->
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
</head>

<body class="layout-fixed sidebar-expand-lg">
<div class="wrapper">
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>
  <jsp:include page="../layout/confirmation_popup.jsp"/>

  <div class="content-wrapper">
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Update Status</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-end">
              <li class="breadcrumb-item"><a>Home</a></li>
              <li class="breadcrumb-item active">Update Status</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <section class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-12">
            <div class="card-primary">
              <div class="card-header">
                <h3 class="card-title">Update Status</h3>
              </div>

              <div class="card-body card">
                <c:if test="${not empty closeclasemessage}">
                  <div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
                    <strong>${closeclasemessage}</strong>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                  </div>
                </c:if>

                <c:if test="${not empty message}">
                  <div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
                    <strong>${message}</strong>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                  </div>
                </c:if>

                <div class="row">
                  <div class="col-md-6 form-group">
                    <label class="col-md-2">Category<span style="color: red;"> *</span></label>
                    <select id="category" name="category" class="selectpicker col-md-8"
                            data-live-search="true" title="Please Select Category">
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
                </div>

                <div id="loader" style="display:none; text-align:center;">
                  <i class="fa fa-spinner fa-spin" style="font-size:52px;color:#007bff;"></i>
                </div>

                <div id="dataListDiv"></div>

              </div><!-- /.card-body -->
            </div><!-- /.card-primary -->
          </div>
        </div>
      </div>
    </section>
  </div>

  <jsp:include page="../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- Scripts: jQuery -> Bootstrap 5 -> AdminLTE 4 -> DataTables -> other plugins -->
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0/dist/js/adminlte.min.js"></script>

<!-- DataTables (Bootstrap 5) -->
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>
<script src="https://cdn.datatables.net/responsive/2.5.0/js/dataTables.responsive.min.js"></script>
<script src="https://cdn.datatables.net/responsive/2.5.0/js/responsive.bootstrap5.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.1/js/dataTables.buttons.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.1/js/buttons.bootstrap5.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/jszip@3.10.1/dist/jszip.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/pdfmake@0.2.7/build/pdfmake.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/pdfmake@0.2.7/build/vfs_fonts.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.1/js/buttons.html5.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.1/js/buttons.print.min.js"></script>
<script src="https://cdn.datatables.net/buttons/2.4.1/js/buttons.colVis.min.js"></script>

<!-- Your existing plugins -->
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>

<!-- jQuery ↔ Bootstrap 5 modal bridge (keeps $('#id').modal('show') working) -->
<script>
  (function ($) {
    $.fn.modal = function (action) {
      return this.each(function () {
        const instance = bootstrap.Modal.getOrCreateInstance(this);
        if (action === 'show') instance.show();
        else if (action === 'hide') instance.hide();
      });
    };
  })(jQuery);
</script>

<script>
document.addEventListener('contextmenu', function(e) { e.preventDefault(); });
document.addEventListener('keydown', function(e) { if (e.ctrlKey && e.key === 'u') { e.preventDefault(); }});
document.addEventListener('keydown', function(e) { if (e.key === 'F12') { e.preventDefault(); }});

// Disable back and forward cache (same logic kept)
$(document).ready(function () {
  function disableBack() { window.history.forward(); }
  window.onload = disableBack();
  window.onpageshow = function (evt) { if (evt.persisted) disableBack(); }
});

// Disable refresh (same logic kept)
document.onkeydown = function (e) {
  if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) {
    e.preventDefault();
  }
};

$(function () {
  $("#categor1").on('change', function(){
    var selectedValue = $(this).val();
    $.ajax({
      url: '/checkLoginStatus',
      method: 'get',
      async: false,
      success: function(result){
        const myJSON = JSON.parse(result);
        if(result=='true'){
          $("#dataListDiv").empty();
          $("#loader").show();
          setTimeout(function(){
            $("#dataListDiv").load($(location).attr('protocol')+"//"+$(location).attr('host')+'/foa/update_summary_data_list',
              function(response, status, xhr){
                $("#loader").hide();
                if(status == 'success'){ console.log("success"); }
                else { console.log("failed"); }
              });
          },1000);
        } else if(result=='false'){
          window.location.reload();
        }
      }
    });
  });
});
</script>

<script>
function formValidation(){
  var actionStatusId = $("#actionStatus").val();
  if(actionStatusId == 1){
    return true;
  }else{
    var fileName = document.querySelector('#uploadedFile').value;
    var extension = fileName.slice((fileName.lastIndexOf(".") - 1 >>> 0) + 2);
    var input = document.getElementById('uploadedFile');
    if (input.files && input.files[0]) {
      var maxAllowedSize = 10 * 1024 * 1024;
      if(extension == 'pdf'){
        if(input.files[0].size > maxAllowedSize) {
          alert('Please upload max 10MB file');
          input.value = '';
          return false;
        } else {
          return true;
        }
      } else {
        alert("Please upload only pdf file");
        document.querySelector('#uploadedFile').value = '';
        return false;
      }
    } else {
      alert("Please upload pdf file");
      return false;
    }
  }
}
</script>

<script>
$(document).ready(function() {
  $("#message").fadeTo(2000, 500).slideUp(500, function() {
    $("#message").slideUp(500);
  });
});

$(function() {
  $("#category").on('change',function() {
    var selectedValue = $(this).val();
    if(selectedValue == '13'){
      $.ajax({
        url: '/checkLoginStatus',
        method: 'get',
        async: false,
        success: function(result){
          const myJSON = JSON.parse(result);
          if(result=='true') {
            $("#dataListDiv").empty();
            $("#loader").show();
            setTimeout(function() {
              $("#dataListDiv").load('/foa/get_scrutiny_data_list?id=' + selectedValue, function(response, status, xhr) {
                $("#loader").hide();
                if (status == 'success') { console.log("success"); }
                else { console.log("failed"); }
              });
            }, 1000);
          } else if(result=='false'){
            window.location.reload();
          }
        }
      });
    } else {
      $.ajax({
        url: '/checkLoginStatus',
        method: 'get',
        async: false,
        success: function(result){
          const myJSON = JSON.parse(result);
          if(result=='true'){
            $("#dataListDiv").empty();
            $("#loader").show();
            setTimeout(function() {
              $("#dataListDiv").load('/foa/update_summary_data_list?id=' + selectedValue, function(response, status, xhr) {
                $("#loader").hide();
                if (status == 'success') { console.log("success"); }
                else { console.log("failed"); }
              });
            }, 1000);
          } else if(result=='false'){
            window.location.reload();
          }
        }
      });
    }
  });
});
</script>
</body>
</html>
