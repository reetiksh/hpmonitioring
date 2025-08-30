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
		var action = document.getElementById("buttonAction").value;
		$.confirm({
			title : 'Confirm!',
			content : 'Do you want to proceed ahead with ' + action +' of recommendation?',
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
  function submitForm(action) {
		if(action == 'rejection'){
			document.getElementById('comment_span').style.display = 'block';
			document.getElementById('comment').setAttribute('required', 'required');
		} else {
			document.getElementById('comment_span').style.display = 'none';
			document.getElementById('comment').removeAttribute('required');
		}
    document.getElementById("buttonAction").value = action;
  }

	$(document).ready(function() {
		$("#workingDate").on("change", function() {
			var effectiveDate = $("#workingDate").val();
			var now = new Date();
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
			}
		});
	});

	function showDiv(){
		$("#message1").fadeTo(2000, 500).slideUp(500, function() {
			$("#message1").slideUp(500);
		});
	}
</script>

	<div class="card-body">
		<form method="POST" name="caseAssignmentAction" id="caseAssignmentAction" action="/l2/recommended_for_other_module" enctype="multipart/form-data" >
			<div class="row">
				<div class="col-md-12">
						<div class="row">
							<div class="col-md-5">
								<div class="row">
									<div class="col-md-4">
										<label for="caseId">Case Id</label>
									</div>
									<div class="col-md-7">
										<div class="form-group">
											<input type="text" class="form-control" id="caseId" name="caseId" value="${caseId}" readonly/>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-7">
								<div class="row">
									<div class="col-md-3">
										<label for="jurisdiction">Jurisdiction</label>
									</div>
									<div class="col-md-9">
										<div class="form-group">
											<input type="text" class="form-control" id="jurisdictionName" name="jurisdictionName" value="${jurisdictionName}" readonly/>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label for="module">Recommended Module <span style="color: red;"> *</span><span id="date_alert"></span></label>
									<select class="selectpicker form-control" data-live-search="true" id="module" name="module" title="Please select module" required>
										<option value="${recommendedModule}" selected>${recommendedModule}</option>
									</select>
								</div>
							</div>
							<div class="col-lg-8">
								<div class="form-group">
									<label for="teamLeadUser">Field Officer List: <span style="color: red;"> *</span></label>
									<select class="selectpicker form-control" data-live-search="true" id="teamLeadUser" name="teamLeadUser" required>
										<c:choose>
											<c:when test="${not empty foUserList}">
												<option value="" selected disabled>Please select field officer</option>
												<c:forEach items="${foUserList}" var="foUser">
													<c:choose>
														<c:when test="${foUser.userId eq foUserId}">
															<option value="${foUser.userId}" data-subtext="${foUser.designation}" selected>${foUser.userName} [${foUser.loginId}] ${foUser.designation}</option>
														</c:when>
														<c:otherwise>
															<option value="${foUser.userId}" data-subtext="${foUser.designation}">${foUser.userName} [${foUser.loginId}] ${foUser.designation}</option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<option value="" disabled selected>No field officer found for this jurisdiction</option>
											</c:otherwise>
										</c:choose>
									</select>
								</div>
							</div>
							<div class="col-lg-12">
								<div class="form-group">
									<label for="pdfFile">Supporting Document for ${recommendationStatus}</label>
									<div id="pdfFileDownloadDiv">
										<a href="/l2/downloadFile?fileName=${recommendationStatusDocument}">
											<img class="animation__shake" src="/static/image/PDF_file_icon.png" alt="pdf" height="40" width="34">  <span>${recommendationStatusDocument}</span>
										</a>
									</div>
								</div>
							</div>
							<div class="col-lg-12">
								<div class="form-group">
									<label for="pdfFile">Supporting Document for Closure Report Issue</label>
									<div id="pdfFileDownloadDiv">
										<a href="/l2/downloadFile?fileName=${closureReportIssueDocument}">
											<img class="animation__shake" src="/static/image/PDF_file_icon.png" alt="pdf" height="40" width="34">  <span>${closureReportIssueDocument}</span>
										</a>
									</div>
								</div>
							</div>
							<div class="col-lg-12">
								<div class="form-group">
									<label for="comment">Remarks<span id="comment_span" style="color: red; float: right; display: none;">&nbsp;*</span></label>
									<input type="text" class="form-control" id="comment" name="comment" maxlength="100" placeholder="Please enter your remarks" required/>
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
        <input type="hidden" id="buttonAction" name="buttonAction" value="" />
			</div>
			<hr>
			<div class="row">
				<div class="col-md-12">
					<div class="float-right">
							<button type="submit" onclick="submitForm('approval')" class="btn btn-primary">Assign</button>
              <button type="submit" onclick="submitForm('rejection')" class="btn btn-danger">Reject</button>
					</div>
				</div>
			</div>
		</form>
	</div>


