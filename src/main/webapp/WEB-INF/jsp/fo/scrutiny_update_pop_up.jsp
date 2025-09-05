<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>HP GST | Update Scrutiny Case</title>
  <link rel="icon" type="image/x-icon" href="/static/files/hp_logo.png">

  <!-- AdminLTE / Bootstrap / Plugins -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">

  <style>
    .btn-outline-custom{color:#495057;border:1px solid #ced4da;text-align:left;}
    .class{border:1px solid #000;padding:10px;}
  </style>
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
  <!-- (Optional) preloader -->
  <div class="preloader flex-column justify-content-center align-items-center">
    <img class="animation__shake" src="/static/dist/img/AdminLTELogo.png" alt="AdminLTELogo" height="60" width="60">
  </div>

  <!-- Header / Sidebar -->
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>

  <div class="content-wrapper">
    <!-- Page header -->
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6"><h1>Update Scrutiny Case</h1></div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="/fo/dashboard">Home</a></li>
              <li class="breadcrumb-item active">Update Scrutiny Case</li>
            </ol>
          </div>
        </div>
      </div>
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="container-fluid">

        <!-- ==== YOUR ORIGINAL CONTENT STARTS HERE (unchanged logic) ==== -->

        <style>
          .btn-outline-custom {
            color: #495057;
            border: 1px solid #ced4da;
            text-align:left;
          }
          .class {
            border: 1px solid black;
            padding : 10px;
          }
        </style>

        <script>
          document.addEventListener('contextmenu', function(e) { e.preventDefault(); });
          document.addEventListener('keydown', function(e) { if (e.ctrlKey && e.key === 'u') { e.preventDefault(); } });
          document.addEventListener('keydown', function(e) { if (e.key === 'F12') { e.preventDefault(); } });

          // Disable back and forward cache
          $(document).ready(function () {
            var actionId = ${submittedData.actionStatus};
            var caseStage = ${submittedData.caseStage};
            var recoveryId = $("#recoveryStage").val();

            if(actionId == 2 || actionId == 3){
              $("#span1").show();
              $("#span4").show();
              $("#span5").show();
              $("#span6").show();

              $("#recoveryStage").prop("required", true);
              $("#caseStage").prop("required", true);
              $("#demand").prop("required", true);
              $("#uploadedFile").prop("required", true);
              $("#recoveryByDRC3").prop("required", true);
              $("#recoveryAgainstDemand").prop("required", true);

              $("#caseId").prop("required", true);
              $("#caseStageArn").prop("required", true);
              $("#recovery").prop("required", true);
            } else {
              $("#recoveryStage").prop("required", false);
              $("#caseStage").prop("required", false);
              $("#demand").prop("required", false);
              $("#uploadedFile").prop("required", false);
              $("#recoveryByDRC3").prop("required", false);
              $("#recoveryAgainstDemand").prop("required", false);

              $("#caseId").prop("required", false);
              $("#caseStageArn").prop("required", false);
              $("#recovery").prop("required", false);
            }

            $.ajax({
              url: '/fo/get_case_stage_by_actionid',
              method: 'get',
              async: false,
              data: {actionId : actionId},
              success: function(result){
                $('#caseStage').empty();
                $('#caseStage').append('<option data-tokens="casestage" value="" disabled selected>Select</option>');
                var option = null;
                $.each(result, function(key, value){
                  var selected = key == ${submittedData.caseStage} ? 'selected' : '';
                  option = '<option data-tokens="" value='+key+' '+selected+'>'+value+'</option>';
                  $('#caseStage').append(option);
                });
              }
            });

            if(recoveryId == 1){
              $("#recoveryByDRC3").prop('readonly', true);
              $("#recoveryAgainstDemand").prop('readonly', true);
            } else if(recoveryId == 4){
              $("#recoveryByDRC3").prop('readonly', true);
              $("#recoveryAgainstDemand").prop('readonly', true);
            } else if(recoveryId == 2 && caseStage == 5){
              $("#recoveryByDRC3").prop('readonly', true);
              $("#recoveryAgainstDemand").prop('readonly', false);
            } else if(recoveryId == 2 && caseStage == 6){
              $("#recoveryByDRC3").prop('readonly', false);
              $("#recoveryAgainstDemand").prop('readonly', true);
            } else if(recoveryId == 3 && caseStage == 5){
              $("#recoveryByDRC3").prop('readonly', true);
              $("#recoveryAgainstDemand").prop('readonly', true);
            } else if(recoveryId == 3 && caseStage == 6){
              $("#recoveryByDRC3").prop('readonly', true);
              $("#recoveryAgainstDemand").prop('readonly', true);
            } else {
              $("#recoveryByDRC3").prop('readonly', false);
              $("#recoveryAgainstDemand").prop('readonly', false);
            }

            onAction();

            function disableBack() { window.history.forward(); }
            window.onload = disableBack();
            window.onpageshow = function (evt) { if (evt.persisted) disableBack(); }

            function validateCaseStage() {
              var caseStage = document.getElementById("caseStage").value;
              if (caseStage === "" || caseStage === "0") {
                document.getElementById("caseStageError").style.display = "block";
                return false;
              } else {
                document.getElementById("caseStageError").style.display = "none";
                return true;
              }
            }
          });

          // Disable refresh
          document.onkeydown = function (e) {
            if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) { e.preventDefault(); }
          };

          $(document).ready(function () {
            var counter = $("#arnLength").val();

            $("#addrow").on("click", function () {
              var newRow = $("<tr>");
              var cols = "";
              cols += '<td><input type="text" class="form-control recoveryclass" id="recoveryStageArn" name="recoveryStageArn[' + counter + ']" placeholder="Please enter Recovery Stage ARN" maxlength="15" pattern="[A-Za-z0-9]{15}" title="Please enter 15-digits alphanumeric Recovery Stage ARN" required /></td>';
              cols += '<td><input type="button" class="ibtnDel btn btn-md btn-danger" value="Delete"></td>';
              newRow.append(cols);
              $("table.order-list").append(newRow);
              counter++;
            });

            $("table.order-list").on("click", ".ibtnDel", function () {
              $(this).closest("tr").remove();
            });
          });

          function calculateRow(row){ var price = +row.find('input[name^="price"]').val(); }
          function calculateGrandTotal() {
            var grandTotal = 0;
            $("table.order-list").find('input[name^="price"]').each(function () { grandTotal += +$(this).val(); });
            $("#grandtotal").text(grandTotal.toFixed(2));
          }
        </script>

        <script>
          $(function(){ $('.selectpicker').selectpicker(); });

          function caseStageChange(){
            var caseStage = $("#caseStage").val();
            var recoveryId = $("#recoveryStage").val();

            if(recoveryId == 1){
              $("#recoveryByDRC3").prop('readonly', true);
              $("#recoveryAgainstDemand").prop('readonly', true);
              $("#recoveryByDRC3").val(0); $("#recoveryAgainstDemand").val(0);
            } else if(recoveryId == 4){
              $("#recoveryByDRC3").prop('readonly', true);
              $("#recoveryAgainstDemand").prop('readonly', true);
              $("#recoveryByDRC3").val(0); $("#recoveryAgainstDemand").val(0);
            } else if(recoveryId == 2 && caseStage == 5){
              $("#recoveryByDRC3").prop('readonly', true);
              $("#recoveryAgainstDemand").prop('readonly', false);
              $("#recoveryByDRC3").val(0); $("#recoveryAgainstDemand").val('');
            } else if(recoveryId == 2 && caseStage == 6){
              $("#recoveryByDRC3").prop('readonly', false);
              $("#recoveryAgainstDemand").prop('readonly', true);
              $("#recoveryByDRC3").val(''); $("#recoveryAgainstDemand").val(0);
            } else if(recoveryId == 3 && caseStage == 5){
              $("#recoveryByDRC3").prop('readonly', true);
              $("#recoveryAgainstDemand").prop('readonly', true);
              $("#recoveryByDRC3").val(0);
              $("#recoveryAgainstDemand").val($("#demand").val());
            } else if(recoveryId == 3 && caseStage == 6){
              $("#recoveryByDRC3").prop('readonly', true);
              $("#recoveryAgainstDemand").prop('readonly', true);
              $("#recoveryByDRC3").val($("#demand").val());
              $("#recoveryAgainstDemand").val(0);
            } else {
              $("#recoveryByDRC3").prop('readonly', false);
              $("#recoveryAgainstDemand").prop('readonly', false);
              $("#recoveryByDRC3").val('');
              $("#recoveryAgainstDemand").val('');
            }

            $('#caseStageArn').val('');
            $('#demand').val('');
            $('#recoveryStage').val('0').change();
          }

          function amountChange(){
            var amount = $("#demand").val();
            var recoveryId = $("#recoveryStage").val();
            var caseStage = $("#caseStage").val();

            if(recoveryId == 3 && caseStage == 5){
              $("#recoveryAgainstDemand").prop('readonly', true).val(amount);
            } else if(recoveryId == 3 && caseStage == 6){
              $("#recoveryByDRC3").prop('readonly', true).val(amount);
            }
          }

          function notRecovery(){
            var recoveryId = $("#recoveryStage").val();
            var caseStage = $("#caseStage").val();

            if(recoveryId == 1){
              $("#recoveryByDRC3").prop('readonly', true);
              $("#recoveryAgainstDemand").prop('readonly', true);
              $("#recoveryByDRC3").val(0); $("#recoveryAgainstDemand").val(0);
            } else if(recoveryId == 4){
              $("#recoveryByDRC3").prop('readonly', true);
              $("#recoveryAgainstDemand").prop('readonly', true);
              $("#recoveryByDRC3").val(0); $("#recoveryAgainstDemand").val(0);
            } else if(recoveryId == 2 && caseStage == 5){
              $("#recoveryByDRC3").prop('readonly', true);
              $("#recoveryAgainstDemand").prop('readonly', false);
              $("#recoveryByDRC3").val(0); $("#recoveryAgainstDemand").val('');
            } else if(recoveryId == 2 && caseStage == 6){
              $("#recoveryByDRC3").prop('readonly', false);
              $("#recoveryAgainstDemand").prop('readonly', true);
              $("#recoveryByDRC3").val(''); $("#recoveryAgainstDemand").val(0);
            } else if(recoveryId == 3 && caseStage == 5){
              $("#recoveryByDRC3").prop('readonly', true);
              $("#recoveryAgainstDemand").prop('readonly', true);
              $("#recoveryByDRC3").val(0);
              $("#recoveryAgainstDemand").val($("#demand").val());
            } else if(recoveryId == 3 && caseStage == 6){
              $("#recoveryByDRC3").prop('readonly', true);
              $("#recoveryAgainstDemand").prop('readonly', true);
              $("#recoveryByDRC3").val($("#demand").val());
              $("#recoveryAgainstDemand").val(0);
            } else {
              $("#recoveryByDRC3").prop('readonly', false);
              $("#recoveryAgainstDemand").prop('readonly', false);
              $("#recoveryByDRC3").val('');
              $("#recoveryAgainstDemand").val('');
            }
          }

          function yetToInitiated(){
            var actionStatusId = $("#actionStatus").val();
            var actionStatus = ${submittedData.actionStatus};

            if(actionStatusId < actionStatus){
              alert("Action not allowed: You can not select previous action status/stage");
              $("#actionStatus").val(actionStatus);
            } else {
              if(actionStatusId == 2 || actionStatusId == 3){
                $("#span1,#span4,#span5,#span6").show();
                $("#recoveryStage,#caseStage,#demand,#uploadedFile,#recoveryByDRC3,#recoveryAgainstDemand").prop("required", true);
                $("#caseId,#caseStageArn,#recovery").prop("required", true);
              } else {
                $("#span1,#span4,#span5,#span6").hide();
                $("#recoveryStage,#caseStage,#demand,#uploadedFile,#recoveryByDRC3,#recoveryAgainstDemand").prop("required", false);
                $("#caseId,#caseStageArn,#recovery").prop("required", false);
              }
            }
          }
        </script>

        <script>
          $('#caseUpdateForm').on('submit', function(oEvent) {
            document.getElementById("caseStageError").style.display = "none";
            var caseStage = document.getElementById("caseStage").value;
            if (caseStage === "2") {
              document.getElementById("caseStageError").style.display = "block";
              return false;
            }

            oEvent.preventDefault();

            $.confirm({
              title : 'Confirm!',
              content : 'Do you want to proceed ahead with updating case details?',
              buttons : {
                submit : function() {
                  var actionStatusId = $("#actionStatus").val();

                  if(actionStatusId == 1){
                    oEvent.currentTarget.submit();
                  } else {
                    var recoveryStage = $("#recoveryStage").val();
                    var recoveryStageArn = $("#recoveryStageArn").val();

                    if(recoveryStage == 2 || recoveryStage == 3){
                      if (typeof recoveryStageArn === 'undefined' || recoveryStageArn === null) {
                        $.alert("Please add Recovery Stage ARN/Reference no");
                      } else {
                        var fileName = document.querySelector('#uploadedFile').value;
                        var extension = fileName.slice((fileName.lastIndexOf(".") - 1 >>> 0) + 2);
                        var input = document.getElementById('uploadedFile');

                        if (input.files && input.files[0]) {
                          var maxAllowedSize = 10 * 1024 * 1024;
                          if(extension == 'pdf'){
                            if(input.files[0].size > maxAllowedSize) {
                              $.alert('Please upload max 10MB file');
                              input.value = '';
                            } else {
                              oEvent.currentTarget.submit();
                            }
                          } else {
                            $.alert("Please upload only pdf file");
                            document.querySelector('#uploadedFile').value = '';
                          }
                        } else {
                          $.alert("Please upload pdf file");
                        }
                      }
                    } else {
                      var fileName2 = document.querySelector('#uploadedFile').value;
                      var extension2 = fileName2.slice((fileName2.lastIndexOf(".") - 1 >>> 0) + 2);
                      var input2 = document.getElementById('uploadedFile');

                      if (input2.files && input2.files[0]) {
                        var maxAllowedSize2 = 10 * 1024 * 1024;
                        if(extension2 == 'pdf'){
                          if(input2.files[0].size > maxAllowedSize2) {
                            $.alert('Please upload max 10MB file');
                            input2.value = '';
                          } else {
                            oEvent.currentTarget.submit();
                          }
                        } else {
                          $.alert("Please upload only pdf file");
                          document.querySelector('#uploadedFile').value = '';
                        }
                      } else {
                        $.alert("Please upload pdf file");
                      }
                    }
                  }
                },
                close : function() { $.alert('Canceled!'); }
              }
            });
          });

          function onAction() {
            var actionId = $("#actionStatus").val();
            var actionStatus = ${submittedData.actionStatus};
            var caseStage = ${submittedData.caseStage};

            if(actionId < actionStatus){
              alert("Action not allowed: You can not select previous action status/stage");
              $("#actionStatus").val(actionStatus);
            } else {
              $.ajax({
                url: '/fo/get_case_stage_by_actionid',
                method: 'get',
                async: false,
                data: {actionId : actionId},
                success: function(result){
                  $('#caseStage').empty();
                  $('#caseStage').append('<option data-tokens="casestage" value="" disabled selected>Select</option>');
                  var option = null;
                  $.each(result, function(key, value){
                    if (key == caseStage) {
                      option = '<option data-tokens="" value="' + key + '" selected>' + value + '</option>';
                    } else {
                      option = '<option data-tokens="" value='+key+'>'+value+'</option>';
                    }
                    $('#caseStage').append(option);
                  });
                }
              });
            }
          }
        </script>

        <span id="caseStageError" style="color: red; display: none; padding-left: 20px;">Please select different case stage.</span>

        <div class="card card-primary">
          <div class="card-body">
            <form method="POST" action="fo_update_scrutiny_cases" name="caseUpdateForm" id="caseUpdateForm" enctype="multipart/form-data">
              <div class="row">
                <div class="col-md-3">
                  <div class="form-group">
                    <label for="gstin">GSTIN</label>
                    <input class="form-control" id="gstin" name="GSTIN_ID" value="${viewItem.GSTIN_ID}" readonly />
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <label for="taxpayername">Taxpayer Name</label>
                    <input class="form-control" id="taxpayerName" name="taxpayerName" value="${viewItem.taxpayerName}" readonly />
                  </div>
                </div>

                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                <div class="col-md-3">
                  <div class="form-group">
                    <label for="circle">Jurisdiction</label>
                    <input class="form-control" id="circle" name="circle" value="${viewItem.circle}" readonly />
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <label for="casecategory">Case Category</label>
                    <input class="form-control" id="caseCategory" value="${viewItem.category}" name="category" readonly />
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-md-3">
                  <div class="form-group">
                    <label for="casereportingdate">Period</label>
                    <input class="form-control" value="${viewItem.period_ID}" readonly />
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <label for="casereportingdate">Reporting Date(DD-MM-YYYY)</label>
                    <input class="form-control" value="<fmt:formatDate value='${viewItem.date}' pattern='dd-MM-yyyy' />" readonly />
                  </div>
                </div>

                <input type="hidden" class="form-control" id="caseReportingDate" value="${viewItem.caseReportingDate_ID}" name="caseReportingDate_ID" />
                <input type="hidden" name="period_ID" value="${viewItem.period_ID}" >

                <div class="col-md-3">
                  <div class="form-group">
                    <label for="suspectedIndicative">Indicative Value(₹)</label>
                    <input class="form-control" id="susIndicativeTax" value="${viewItem.indicativeTaxValue}" name="indicativeTaxValue" readonly />
                  </div>
                </div>
                <div class="col-md-3">
                  <div class="form-group">
                    <label for="message-text">Review Comments</label>
                    <textarea class="form-control" id="remarksId" name="remarksId" readonly>${viewItem.remarks}</textarea>
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-md-3">
                  <div class="form-group">
                    <label for="actionstatus">Action Status <span style="color:red">*</span></label>
                    <select class="form-control selectpicker" id="actionStatus" name="actionStatus"
                            onchange="yetToInitiated(),onAction()" data-live-search="true" title="Please select Action Status" required>
                      <option data-tokens="actionstatus" value="0" disabled>Select</option>
                      <c:forEach items="${listActionStatus}" var="categories">
                        <option data-tokens="" value="${categories.id}"
                          <c:if test="${categories.id == 1}">disabled</c:if>
                          <c:if test="${categories.id == submittedData.actionStatus}">selected</c:if>>
                          ${categories.name}
                        </option>
                      </c:forEach>
                    </select>
                  </div>
                </div>

                <div class="col-md-3" id="span4">
                  <div class="form-group">
                    <label for="caseId">Case Id <span style="color:red">*</span></label>
                    <c:choose>
                      <c:when test="${viewItem.caseIdUpdated eq 'yes'}">
                        <input class="form-control" value="${viewItem.caseId}" readonly />
                      </c:when>
                      <c:when test="${viewItem.caseIdUpdated eq 'no'}">
                        <input class="form-control" id="caseId" name="caseId"
                               placeholder="${viewItem.scrutinyCaseId != null ? viewItem.scrutinyCaseId : 'Please enter Case Id'}"
                               maxlength="15" pattern="[A-Za-z0-9]{15}" title="Please enter 15-digits alphanumeric Case Id" />
                      </c:when>
                    </c:choose>
                  </div>
                </div>

                <div class="col-md-3" id="span5">
                  <div class="form-group">
                    <label for="caseStage">Case Stage <span style="color:red">*</span></label>
                    <select class="form-control" id="caseStage" name="caseStage" onchange="caseStageChange()" title="Please select Case Stage"></select>
                  </div>
                </div>

                <div class="col-md-3" id="span6">
                  <div class="form-group">
                    <label for="caseStageARN">Case Stage ARN/Reference no <span style="color:red">*</span></label>
                    <input class="form-control" id="caseStageArn" name="caseStageArn"
                           placeholder="Please enter Case Stage ARN" maxlength="15" pattern="[A-Za-z0-9]{15}"
                           value="${submittedData.caseStageArn}"
                           title="Please enter 15-digits alphanumeric Case Stage ARN" />
                  </div>
                </div>
              </div>

              <!-- <!-- <span id="span1"> --> -->

              <div class="row">
                <div class="col-md-3" id="span2">
                  <div class="form-group">
                    <label for="demand">Amount(₹) <span style="color:red">*</span></label>
                    <input class="form-control" type="text" maxlength="11" id="demand" name="demand"
                           onchange="amountChange()" onkeypress='return event.charCode >= 48 && event.charCode <= 57'
                           value="${submittedData.demand}" title="Please enter Amount" />
                  </div>
                </div>

                <div class="col-md-3">
                  <div class="form-group">
                    <label for="recoveryStage">Recovery Stage <span style="color:red">*</span></label>
                    <select class="form-control selectpicker recoveryStage" id="recoveryStage" name="recoveryStage"
                            data-live-search="true" onchange="notRecovery()" title="Please select Recovery Stage">
                      <option data-tokens="recoverystage" value="0" disabled>Select</option>
                      <c:forEach items="${listRecovery}" var="categories">
                        <option data-tokens="" value="${categories.id}"
                          <c:if test="${categories.id == '4'}">disabled</c:if>
                          <c:if test="${categories.id == submittedData.recoveryStage}">selected</c:if>>
                          ${categories.name}
                        </option>
                      </c:forEach>
                    </select>
                  </div>
                </div>

                <div class="col-md-3">
                  <div class="form-group">
                    <label for="recoveryByDRC">Recovery Via DRC03(₹) <span style="color:red">*</span></label>
                    <input class="form-control" type="text" maxlength="11" id="recoveryByDRC3" name="recoveryByDRC3"
                           onkeypress='return event.charCode >= 48 && event.charCode <= 57'
                           value="${submittedData.recoveryByDRC3}" title="Please enter Recovery Via DRC03" />
                  </div>
                </div>

                <div class="col-md-3">
                  <div class="form-group">
                    <label for="recovery">Recovery Against Demand(₹) <span style="color:red">*</span></label>
                    <input class="form-control" type="text" maxlength="11" id="recoveryAgainstDemand" name="recoveryAgainstDemand"
                           onkeypress='return event.charCode >= 48 && event.charCode <= 57'
                           value="${submittedData.recoveryAgainstDemand}" title="Please enter Recovery Against Demand" />
                  </div>
                </div>

                <div class="col-md-3">
                  <div class="form-group">
                    <label for="excelFile">File Upload <span style="color:red">*</span></label>
                    <span> (upload only pdf file with max file size of 10MB ) </span>
                    <input class="form-control" type="file" id="uploadedFile" name="uploadedFile" accept=".pdf">
                    <c:if test="${submittedData.sum eq 'fileexist'}">
                      <a href="/fo/downloadUploadedFile?fileName=${submittedData.remarks}">
                        <button type="button" class="btn btn-primary"><i class="fa fa-download"></i></button>
                      </a>
                    </c:if>
                  </div>
                </div>
              </div>

              <div class="row">
                <c:if test="${not empty submittedData.recoveryStageArnStr}">
                  <c:set var="strlst" value="${submittedData.recoveryStageArnStr}"/>
                  <c:set var="lst" value="${fn:split(strlst,',')}"/>
                </c:if>
                <input type="hidden" id="arnLength" value="${fn:length(lst)}">

                <div class="col-md-3">
                  <div class="form-group">
                    <label for="recoveryByDRC">Recovery Stage ARN/Reference no <span style="color:red">*</span></label>
                    <table id="myTable" class="table order-list">
                      <thead></thead>
                      <tbody></tbody>
                      <tfoot>
                        <c:if test="${fn:length(lst) > 0}">
                          <c:forEach items="${lst}" varStatus="loop" var="lst">
                            <tr>
                              <td>
                                <input type="text" class="form-control recoveryclass" value="${fn:trim(lst)}"
                                       id="recoveryStageArn" name="recoveryStageArn[${loop.index}]"
                                       placeholder="Please enter Recovery Stage ARN"
                                       maxlength="15" pattern="[A-Za-z0-9]{15}"
                                       title="Please enter 15-digits alphanumeric Recovery Stage ARN" required />
                              </td>
                              <td><input type="button" class="ibtnDel btn btn-md btn-danger" value="Delete"></td>
                            </tr>
                          </c:forEach>
                        </c:if>
                        <tr>
                          <td colspan="5" style="text-align:left;">
                            <input type="button" class="btn btn-primary" id="addrow" value="Add Row" />
                          </td>
                        </tr>
                      </tfoot>
                    </table>
                  </div>
                </div>
              </div>

              <!-- (closing of commented span to keep DOM balanced) -->
              <!-- </span> -->

              <hr>

              <div class="row">
                <div class="col-md-12">
                  <div class="form-group float-right">
                    <button type="submit" class="btn btn-primary" id="submitCase">Submit</button>
                  </div>
                </div>
              </div>
            </form>
          </div>
        </div>

        <!-- ==== YOUR ORIGINAL CONTENT ENDS HERE ==== -->

      </div>
    </section>
  </div>

  <jsp:include page="../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark"></aside>
</div>

<!-- Scripts -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>
</body>
</html>
