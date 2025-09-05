<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>HP GST | ASMT-10 Issuance</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />

  <!-- AdminLTE 4 / Bootstrap 5 -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css" />
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css" />

  <!-- Plugins (keep as used by page) -->
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css" />
  <!-- (Optional) If this page uses DataTables elsewhere, include its BS5 skins; not required for this form -->

  <style>
    .btn-outline-custom {
      color: #495057;
      border: 1px solid #ced4da;
      text-align: left;
    }
    /* Page card background (from original fragment) */
    .hp-card-bg {
      background-color: #f1f1f1;
      border-radius: 3px;
      box-shadow: 2px 2px 4px 1px #8888884d;
    }
  </style>
</head>

<body class="layout-fixed sidebar-expand-lg bg-body-tertiary">
<div class="app-wrapper">

  <!-- Header / Sidebar (AdminLTE 4 layout includes) -->
  <jsp:include page="../layout/header.jsp" />
  <jsp:include page="../layout/sidebar.jsp" />

  <!-- ====================== MAIN ====================== -->
  <main class="app-main">
    <div class="app-content">
      <div class="container-fluid">

        <!-- Page header -->
        <div class="content-header">
          <div class="row mb-2">
            <div class="col-sm-6">
              <h1 class="m-0">ASMT-10 Issuance</h1>
            </div>
            <div class="col-sm-6 d-flex justify-content-end">
              <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="/fo/dashboard">Home</a></li>
                <li class="breadcrumb-item active">ASMT-10 Issuance</li>
              </ol>
            </div>
          </div>
        </div>

        <!-- Card -->
        <div class="card card-primary">
          <div class="card-header">
            <h3 class="card-title mb-0">ASMT-10 Issuance</h3>
          </div>

          <div class="card-body hp-card-bg">

            <!-- ================== ORIGINAL FORM (UNCHANGED LOGIC/DATA) ================== -->
            <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
            <script src="/static/dist/js/bootstrap-select.min.js"></script>

            <div class="card-body" style="padding: 0;">
              <form method="POST" name="asmtTenForm" id="asmtTenForm" action="submit_asmt_ten" enctype="multipart/form-data">
                <h4><b><u>ASMT-10 Issuance:</u></b></h4><br>

                <div class="row">
                  <div class="col-md-3" id="span4">
                    <div class="form-group">
                      <label for="caseId">Case Id <span style="color: red">*</span></label>
                      <input class="form-control" id="caseId" name="caseId"
                             placeholder="Please enter Case Id" maxlength="15"
                             pattern="[A-Za-z0-9]{15}"
                             title="Please enter 15-digits alphanumeric Case Id" readonly />
                    </div>
                  </div>

                  <div class="col-md-3" id="span6">
                    <div class="form-group">
                      <label for="caseStageARN">Case Stage<span style="color: red">*</span></label>
                      <input class="form-control" id="caseStage" name="caseStage" placeholder="Please select Case Stage"  value="ASMT-10 Issued"  title="ASMT-10 Issued" readonly />
                    </div>
                  </div>

                  <div class="col-md-3" id="span6">
                    <div class="form-group">
                      <label for="caseStageARN">Case Stage ARN/Reference no<span style="color: red">*</span></label>
                      <input class="form-control" id="caseStageArn" name="caseStageArn" placeholder="Please enter Case Stage ARN" maxlength="15" pattern="[A-Za-z0-9]{15}"  value="${submittedData.caseStageArn}"  title="Please enter 15-digits alphanumeric Case Stage ARN" readonly/>
                    </div>
                  </div>

                  <div class="col-md-3" id="span2">
                    <div class="form-group">
                      <label for="demand">Amount(₹)<span style="color: red">*</span></label>
                      <input class="form-control" type="text" maxlength="11" id="demand" name="demand" onkeypress='return event.charCode >= 48 && event.charCode <= 57'  value="${submittedData.demand}"  title="Please enter Amount" readonly />
                    </div>
                  </div>

                  <div class="col-lg-12 alert alert-danger alert-dismissible fade show" role="alert" id="message1" style="max-height: 500px; overflow-y: auto; display: none;">
                    <span id="alertMessage1"></span>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                  </div>
                  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                </div>

                <!-- recoveryStageList -->
                <div class="row">

                  <div class="col-md-3">
                    <div class="form-group">
                      <label for="recoveryStage">Recovery Stage <span style="color: red">*</span></label>
                      <select class="form-control selectpicker recoveryStage" id="recoveryStage"
                              name="recoveryStage" data-live-search="true"
                              title="Please select Recovery Stage" disabled>
                        <c:forEach items="${recoveryStageList}" var="soloRecovery">
                          <c:choose>
                            <c:when test="${soloRecovery.id eq mstScrutinyCases.recoveryStage.id}">
                              <option value="${soloRecovery.id}" selected="selected" readonly>${soloRecovery.name}</option>
                            </c:when>
                            <c:otherwise>
                              <option value="${soloRecovery.id}">${soloRecovery.name}</option>
                            </c:otherwise>
                          </c:choose>
                        </c:forEach>
                      </select>
                    </div>
                  </div>

                  <div class="col-md-3">
                    <div class="form-group">
                      <label for="recoveryByDRC">Recovery Via DRC03(₹)<span style="color: red">*</span></label>
                      <input class="form-control" type="text" maxlength="11" id="recoveryByDRC3" name="recoveryByDRC3" onkeypress='return event.charCode >= 48 && event.charCode <= 57'  value="${submittedData.recoveryByDRC3}"  title="Please enter Recovery Via DRC03" readonly/>
                    </div>
                  </div>

                  <div class="col-md-3">
                    <div class="form-group">
                      <label for="downloadButton">Download<span style="color: red">*</span></label>
                      <span>(${amstTenFilePath})</span>
                      <div>
                        <a href="/scrutiny_fo/downloadUploadedPdfFile?fileName=${amstTenFilePath}">
                          <button type="button" class="btn btn-primary">
                            <i class="fa fa-download"></i>
                          </button>
                        </a>
                      </div>
                    </div>
                  </div>

                  <c:if test="${not empty submittedData.recoveryStageArnStr}">
                    <c:set var="strlst" value="${submittedData.recoveryStageArnStr}" />
                    <c:set var="lst" value="${fn:split(strlst,',')}" />
                  </c:if>

                  <input type="hidden" id="arnLength" value="${fn:length(lst)}" />
                  <input type="hidden" value="" id="asmtGstin" name="asmtGstin" readonly />
                  <input type="hidden" value="" id="asmtPeriod" name="asmtPeriod" readonly />
                  <input type="hidden" value="" id="asmtCaseReportingDate" name="asmtCaseReportingDate" readonly />

                  <div class="col-md-3">
                    <div class="form-group">
                      <label for="recoveryByDRC">Recovery Stage ARN/Reference no<span style="color: red">*</span></label>
                      <table id="myTable" class="table order-list">
                        <thead></thead>
                        <tbody></tbody>
                        <tfoot>
                          <c:if test="${fn:length(listOfArn) > 0}">
                            <c:forEach items="${listOfArn}" varStatus="loop" var="lst">
                              <tr>
                                <td>
                                  <input type="text" class="form-control recoveryclass"
                                         value="${fn:trim(lst)}"
                                         id="recoveryStageArn"
                                         name="recoveryStageArn[${loop.index}]"
                                         placeholder="Please enter Recovery Stage ARN"
                                         maxlength="15"
                                         pattern="[A-Za-z0-9]{15}"
                                         title="Please enter 15-digits alphanumeric Recovery Stage ARN"
                                         readonly />
                                </td>
                                <td><input type="button" class="ibtnDel btn btn-md btn-danger" value="Delete" disabled></td>
                              </tr>
                            </c:forEach>
                          </c:if>
                          <tr>
                            <td colspan="5" style="text-align: left;">
                              <input type="button" class="btn btn-primary" id="addrow" value="Add Row" disabled />
                            </td>
                          </tr>
                        </tfoot>
                      </table>
                    </div>
                  </div>

                </div>

                <hr />
                <div class="row">
                  <div class="col-lg-12">
                    <div class="float-end">
                      <button type="button" class="btn btn-primary" onclick="submitAsmtTenIssuance();" disabled>Submit</button>
                    </div>
                  </div>
                </div>

              </form>
            </div>
            <!-- ================== /ORIGINAL FORM ================== -->

          </div><!-- /.card-body -->
        </div><!-- /.card -->

      </div><!-- /.container-fluid -->
    </div><!-- /.app-content -->
  </main>

  <!-- Footer -->
  <jsp:include page="../layout/footer.jsp" />
</div><!-- /.app-wrapper -->

<!-- overlay for mobile “tap outside to close” -->
<div class="sidebar-overlay" data-lte-dismiss="sidebar"></div>

<!-- ====================== SCRIPTS ====================== -->
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>

<!-- Plugins used by this page -->
<script src="/static/plugins/bs-custom-file-input/bs-custom-file-input.min.js"></script>
<script src="/static/dist/js/bootstrap-select.min.js"></script>

<script>
  $(function () {
    if (window.bsCustomFileInput) { bsCustomFileInput.init(); }
    $('.selectpicker').selectpicker();
  });

  /************ add row for scrutiny proceeding dropped (unchanged) ****************/
  $(document).ready(function () {
    var counter = $("#arnLength").val();

    $("#addrow").on("click", function () {
      var newRow = $("<tr>");
      var cols  = "";
      cols += '<td><input type="text" class="form-control recoveryclass"  id="recoveryStageArn" name="recoveryStageArn[' + counter + ']" placeholder="Please enter Recovery Stage ARN" maxlength="15" pattern="[A-Za-z0-9]{15}" title="Please enter 15-digits alphanumeric Recovery Stage ARN" required /></td>';
      cols += '<td><input type="button" class="ibtnDel btn btn-md btn-danger "  value="Delete"></td>';
      newRow.append(cols);
      $("table.order-list").append(newRow);
      counter++;
    });

    $("table.order-list").on("click", ".ibtnDel", function () {
      $(this).closest("tr").remove();
    });
  });
  /************ end ****************/
</script>
</body>
</html>
