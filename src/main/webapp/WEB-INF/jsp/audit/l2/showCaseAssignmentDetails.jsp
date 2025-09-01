<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<style>
  .btn-outline-custom {
    color: #495057;
    border: 1px solid #ced4da;
    text-align:left;
	}
	#div1{
		background-color: #b3b3b342;
		border: 1px solid rgb(212, 212, 212);
    padding: 10px;
    border-radius: 3px;
	}
</style>


<script>
	document.addEventListener('contextmenu', function(e) {
		e.preventDefault();
	});
	document.addEventListener('keydown', function(e) {
		if (e.ctrlKey && e.key === 'u') {
			e.preventDefault();
		}
	});
	document.addEventListener('keydown', function(e) {
        if (e.key === 'F12') {
            e.preventDefault();
        }
    });
	$(document).ready(function () {
	    function disableBack() {window.history.forward()}
	
	    window.onload = disableBack();
	    window.onpageshow = function (evt) {if (evt.persisted) disableBack()}
	});

	// Disable refresh
	document.onkeydown = function (e) {
			if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) {
					e.preventDefault();
			}
	};
	
	$(function() {
		$('.selectpicker').selectpicker();
	});
</script>


<script>
	$('form').on('submit', function(oEvent) {
		oEvent.preventDefault();
		$.confirm({
			title : 'Confirm!',
			content : 'Do you want to proceed ahead with assignment of this case?',
			buttons : {
				submit : function() {
					oEvent.currentTarget.submit();
				},
				close : function() {
					$.alert('Canceled!');
				}
			}
		});
	}) 

	$(document).ready(function() {
		$("#workingDate").on("change", function() {
			var effectiveDate = $("#workingDate").val();
			var now = new Date();
			var previousStatusDate = new Date("${previousStatusDate}");
			now.setHours(23, 59, 59, 999);
			const givenDate = new Date(effectiveDate);
			
			if (givenDate > now) {
				// Clear the input field
				$("#workingDate").val('');

				var x = document.getElementById("message1");
				if(x.style.display === "none"){
					$("#alertMessage1").html("Future date is not allowed").css("color","#fffff");
					x.style.display = "block";
					showDiv();
				}
			} else if (givenDate < previousStatusDate) {
				$("#workingDate1").val('');

				var x = document.getElementById("message1");
				if (x.style.display === "none") {
					$("#alertMessage1").html("The entered date cannot be before " + formatDate(previousStatusDate)).css("color", "#fffff");
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

	<div class="card-body">
		<form method="POST" name="caseAssignmentAction" id="caseAssignmentAction" action="/l2/case_assignment" enctype="multipart/form-data" >
			<div class="row">
				<div class="col-md-12">
						<div class="row">
							<div class="col-md-12">
								<div class="row">
									<div class="col-md-2">
										<label for="caseId">Case Id</label>
									</div>
									<div class="col-md-3">
										<div class="form-group">
											<input type="text" class="form-control" id="caseId" name="caseId" value="${caseId}" readonly/>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="workingDate">Date <span style="color: red;"> *</span><span id="date_alert"></span></label>
									<input type="date" class="form-control" id="workingDate" name="workingDate" required/>
								</div>
							</div>
							<div class="col-lg-9">
								<div class="form-group">
									<label for="teamLeadUser">Team Lead: <span style="color: red;"> *</span></label>
									<select class="selectpicker form-control" data-live-search="true" id="teamLeadUser" name="teamLeadUser" required>
										<c:choose>
											<c:when test="${not empty userList}">
												<option value="" selected disabled>Please select team lead user</option>
												<c:forEach items="${userList}" var="urm">
													<option value="${urm.userDetails.userId}" data-subtext="${urm.userDetails.designation.designationAcronym}">${urm.userDetails.firstName} ${urm.userDetails.middleName} ${urm.userDetails.lastName} [${urm.userDetails.loginName}] ${urm.userDetails.designation.designationAcronym}</option>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<option value="" disabled selected>No team lead found for this jurisdiction</option>
											</c:otherwise>
										</c:choose>
									</select>
								</div>
							</div>
						</div>
				</div>
				<div class="col-12 alert alert-danger alert-dismissible fade show" role="alert" id="message1" style="max-height: 500px; overflow-y: auto; display: none;">
					<span id="alertMessage1"></span>
					<button type="button" class="close" data-dismiss="alert" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<!-- <c:if test="${not empty uploadDataError}"> -->
					<div class="col-12 alert alert-danger alert-dismissible fade show" id="messageOutput"  role="alert" style="max-height: 500px; overflow-y: auto;">
						<c:forEach items="${uploadDataError}" var="row">
							<strong>${row}</strong><br>
						</c:forEach>
						<span id="example1"></span>
						<button type="button" class="close" data-dismiss="alert" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>
				<!-- </c:if> -->
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				<input type="hidden" name="transferId" id="transferId" value="${transferRole.id}" />
			</div>
			<hr>
			<div class="row">
				<div class="col-md-12">
					<div class="float-right">
							<button type="submit" class="btn btn-primary">Assign</button>
					</div>
				</div>
			</div>
		</form>
	</div>


