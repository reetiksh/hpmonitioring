<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Update Status List</title>

  <!-- CSS -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>
  <jsp:include page="../layout/confirmation_popup.jsp"/>

  <div class="content-wrapper">
    <!-- Page header -->
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Recovery Raised Query Status</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a>Home</a></li>
              <li class="breadcrumb-item active">Recovery Raised Query Status</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <!-- Content -->
    <section class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-12">
            <div class="card-primary">
              <div class="card-header">
                <h3 class="card-title">Recovery Raised Query Status</h3>
              </div>

              <div class="card-body">
                <!-- Alerts -->
                <c:if test="${not empty closeclasemessage}">
                  <div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
                    <strong>${closeclasemessage}</strong>
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                      <span aria-hidden="true">&times;</span>
                    </button>
                  </div>
                </c:if>

                <c:if test="${not empty message}">
                  <div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
                    <strong>${message}</strong>
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                      <span aria-hidden="true">&times;</span>
                    </button>
                  </div>
                </c:if>

                <!-- Filters -->
                <div class="row">
                  <div class="col-md-6">
                    <div class="form-group">
                      <label>Category<span style="color: red;"> *</span></label>
                      <select id="category" name="category" class="custom-select">
                        <option value="">Please Select</option>
                        <c:forEach items="${categories}" var="categories">
                          <option value="${categories.id}">${categories.name}</option>
                        </c:forEach>
                      </select>
                    </div>
                    <c:if test="${formResult.hasFieldErrors('category')}">
                      <span class="text-danger">${formResult.getFieldError('category').defaultMessage}</span>
                    </c:if>
                  </div>
                </div>

                <!-- Loader -->
                <div id="loader" style="display:none; text-align:center;">
                  <i class="fa fa-spinner fa-spin" style="font-size:52px;color:#007bff;"></i>
                </div>

                <!-- Dynamic list -->
                <div id="dataListDiv"></div>

              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>

  <jsp:include page="../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- JS -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="/static/plugins/datatables-bs4/js/dataTables.bootstrap4.min.js"></script>
<script src="/static/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="/static/plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/dataTables.buttons.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.bootstrap4.min.js"></script>
<script src="/static/plugins/jszip/jszip.min.js"></script>
<script src="/static/plugins/pdfmake/pdfmake.min.js"></script>
<script src="/static/plugins/pdfmake/vfs_fonts.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.html5.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.print.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.colVis.min.js"></script>
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<script>
  // Context menu / key blocking (unchanged)
  document.addEventListener('contextmenu', function(e) { e.preventDefault(); });
  document.addEventListener('keydown', function(e) {
    if (e.ctrlKey && e.key === 'u') { e.preventDefault(); }
    if (e.key === 'F12') { e.preventDefault(); }
  });
  $(document).ready(function () {
    function disableBack(){ window.history.forward(); }
    window.onload = disableBack();
    window.onpageshow = function (evt){ if (evt.persisted) disableBack(); }
  });
  document.onkeydown = function (e) {
    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) {
      e.preventDefault();
    }
  };

  // Flash message fade
  $(document).ready(function() {
    $("#message").fadeTo(2000, 500).slideUp(500, function() {
      $("#message").slideUp(500);
    });
  });

  // Category change -> load summary list (with login check)
  $(function () {
    $("#category").on('change', function () {
      var selectedValue = $(this).val();

      $.ajax({
        url: '/checkLoginStatus',
        method: 'get',
        async: false,
        success: function(result){
          // original compares plain 'true'/'false'
          if (result === 'true') {
            $("#dataListDiv").empty();
            $("#loader").show();

            setTimeout(function(){
              $("#dataListDiv").load('/fo/update_reverted_approver_recovery_data_list/' + selectedValue + '/', function(response, status){
                $("#loader").hide();
                if (status === 'success') {
                  console.log('success');
                } else {
                  console.log('failed');
                }
              });
            }, 1000);

          } else if (result === 'false') {
            window.location.reload();
          }
        }
      });
    });
  });

  // Form validation (unchanged logic)
  function formValidation() {
    var actionStatusId = $("#actionStatus").val();

    if (actionStatusId == 1) {
      return true;
    } else {
      var fileInput = document.getElementById('uploadedFile');
      var fileName  = fileInput ? fileInput.value : '';
      var extension = fileName ? fileName.slice((fileName.lastIndexOf(".") - 1 >>> 0) + 2) : '';

      if (fileInput && fileInput.files && fileInput.files[0]) {
        var maxAllowedSize = 10 * 1024 * 1024; // 10MB

        if (extension == 'pdf') {
          if (fileInput.files[0].size > maxAllowedSize) {
            alert('Please upload max 10MB file');
            fileInput.value = '';
            return false;
          } else {
            return true;
          }
        } else {
          alert("Please upload only pdf file");
          if (fileInput) fileInput.value = '';
          return false;
        }
      } else {
        alert("Please upload pdf file");
        return false;
      }
    }
  }
</script>
</body>
</html>
