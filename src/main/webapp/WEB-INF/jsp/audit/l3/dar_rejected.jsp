<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<script>
  $(function () {
    bsCustomFileInput.init();
  });

  $(document).ready(function() {
    if('${nonEditable}'==='true'){
      $('input').attr('readonly', true);
      $("select").prop('disabled', true);
      $('button').attr('disabled', true);
    }

    var selectedOption = '${amdDocument.nilDar}';
    var link = '/l3/nil_dar_option?nilDar=' + selectedOption + "&caseId=" + '${amdDocument.caseId.caseId}';

    loadInputDiv(link);
	});

  function loadInputDiv(link){
    $.ajax({url: '/checkLoginStatus',
				method: 'get',
				async: false,
				success: function(result){
					const myJSON = JSON.parse(result);
					if(result=='true'){
						$("#inputDiv").load(link, function(response, status, xhr){
							if(status == 'success'){
								// console.log("success");
							}else{
								alert("We are facing some internal error. Please refresh the page!");
							}
						});
					} else if(result=='false'){
						window.location.reload();
					}
				}         
			});
  }

  $('#nilDar').change(function() {
			var selectedOption = $(this).val();

      var link = '/l3/nil_dar_option?nilDar=' + selectedOption + "&caseId=" + '${amdDocument.caseId.caseId}';
      loadInputDiv(link);
  });

  $('form').on('submit', function(oEvent) {
    oEvent.preventDefault();
    $.confirm({
      title : 'Confirm!',
      content : 'Do you want to proceed ahead with resubmission of DAR details?',
      buttons : {
        submit : function() {
          oEvent.currentTarget.submit();
        },
        close : function() {
          $.alert('Canceled!');
        }
      }
    });
  });

  $(document).ready(function() {
		$("#workingDate").on("change", function() {
			var effectiveDate = $("#workingDate").val();
			var now = new Date();
      var previousStatusDate = new Date("${previousStatusDate}");
      now.setHours(23, 59, 59, 999);
			const givenDate = new Date(effectiveDate);
			
			if (givenDate > now) {
				$("#workingDate").val('');

				var x = document.getElementById("message1");
				if(x.style.display === "none"){
					$("#alertMessage1").html("Future date is not allowed").css("color","#fffff");
					x.style.display = "block";
					showDiv();
				}
			} else if (givenDate < previousStatusDate) {
        $("#workingDate").val('');

        var x = document.getElementById("message1");
				if(x.style.display === "none"){
					$("#alertMessage1").html("The entered date cannot be before " +  formatDate(previousStatusDate)).css("color","#fffff");
					x.style.display = "block";
					showDiv();
				}
      }
		});
	});

  function showDiv(){
		$("#message1").fadeTo(2000, 500).slideUp(500, function() {
			$("#message1").slideUp(500);
		});
	}

  function formatDate(date) {
    var day = String(date.getDate()).padStart(2, '0');
    var month = String(date.getMonth() + 1).padStart(2, '0');
    var year = date.getFullYear();
    return day + "-" + month + "-" + year;
  }
</script>
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
<script type="text/javascript">
  $('.selectpicker').selectpicker();
</script>

<div class="card-body"  style="background-color: #f1f1f1; border-radius: 3px; box-shadow: 2px 2px 4px 1px #8888884d;">
  <p class="h5">DAR</p>
  <hr>
  <form method="POST" name="caseUpdationActionForm" id="caseUpdationActionForm" action="/l3/update_audit_case_details" enctype="multipart/form-data" >
    <div class="row">
      <div class="col-lg-12">
        <div class="row">
          <div class="col-lg-12 alert alert-danger alert-dismissible fade show" role="alert" id="message1" style="max-height: 500px; overflow-y: auto; display: none;">
            <span id="alertMessage1"></span>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="col-lg-6">
            <div class="form-group row">
              <label for="workingDate" class="col-form-label col-sm-3">Date<span style="color: red;"> *</span></label>
              <div class="col-sm-4">
                <c:if test="${nonEditable eq 'true'}">
                  <c:set var="isoDate"><fmt:formatDate value='${amdDocument.actionDate}' pattern='yyyy-MM-dd'/></c:set>
                </c:if>
                <input type="date" class="form-control" id="workingDate" name="workingDate" value="${isoDate}" required/>
              </div>
            </div>
          </div>
          <div class="col-lg-6">
            <div class="form-group">
              <label>Nil DAR<span style="color: red;"> *</span><span id="category_alert"></span></label>
              <select id="nilDar" name="nilDar" class="selectpicker col-md-4" data-live-search="true" title="Please Select Yes/No" required>
                <c:forEach items="${nilDARdddValues}" var="value">
                  <c:choose>
                      <c:when test="${value eq amdDocument.nilDar}">
                          <option value="${value}" selected="selected">${value}</option>
                      </c:when>
                      <c:when test="${value eq 'YES' and amdDocument.nilDar eq 'NO'}">
                          <option value="${value}" disabled>${value}</option>
                      </c:when>
                      <c:otherwise>
                          <option value="${value}">${value}</option>
                      </c:otherwise>
                  </c:choose>
              </c:forEach>
              </select>
            </div>
          </div>
          <div class="col-lg-12" id="inputDiv"></div>
        </div>
      </div>
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
      <input type="hidden" name="caseId" id="caseId" value="${caseId}" />
      <input type="hidden" name="updateStatusId" id="updateStatusId" value="${activeStatusId}" />
    </div>
    
    <hr>
    <div class="row">
      <div class="col-lg-12">
        <div class="float-right">
            <button type="submit" class="btn btn-primary">Update</button>
        </div>
      </div>
    </div>
  </form>
</div>