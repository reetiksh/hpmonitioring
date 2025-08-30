<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
<script>
	document.addEventListener('contextmenu', function (e) {
		e.preventDefault();
	});
	document.addEventListener('keydown', function (e) {
		if (e.ctrlKey && e.key === 'u') {
			e.preventDefault();
		}
	});
	document.addEventListener('keydown', function (e) {
		if (e.key === 'F12') {
			e.preventDefault();
		}
	});
	$(document).ready(function () {
		function disableBack() { window.history.forward() }

		window.onload = disableBack();
		window.onpageshow = function (evt) { if (evt.persisted) disableBack() }
	});

	// Disable refresh
	document.onkeydown = function (e) {
		if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) {
			e.preventDefault();
		}
	};

	$('.selectpicker').selectpicker();
</script>
<script>
	$('form').on('submit', function (oEvent) {
		oEvent.preventDefault();
		submitForApproval('${caseDetails.GSTIN}', '${caseDetails.period}', $("#caseReportingDate").val(), $(this).find(':submit').filter(':focus').val(), $("#assignToEmployee").val());
	});
</script>
<div class="card-body">
	<form method="POST" name="caseAssignmentAction" id="caseAssignmentAction" action=""
		enctype="multipart/form-data">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		<div class="row">
			<div class="col-md-12">
				<div class="row">
					<div class="col-md-12">
						<label class="sr-only" for="GSTIN">GSTIN</label>
						<div class="input-group mb-2">
							<div class="input-group-prepend">
								<div class="input-group-text" style="width: 200px; font-weight: bold;">GSTIN</div>
							</div>
							<input type="text" class="form-control" id="GSTIN" placeholder="GSTIN"
								value="${caseDetails.GSTIN}" readonly>
						</div>
					</div>
					<div class="col-md-12">
						<label class="sr-only" for="period">Period</label>
						<div class="input-group mb-2">
							<div class="input-group-prepend">
								<div class="input-group-text" style="width: 200px; font-weight: bold;">Period</div>
							</div>
							<input type="text" class="form-control" id="period" placeholder="Period"
								value="${caseDetails.period}" readonly>
						</div>
					</div>
					<div class="col-md-12">
						<label class="sr-only" for="caseReportingDate">Case Reporting Date</label>
						<div class="input-group mb-2">
							<div class="input-group-prepend">
								<div class="input-group-text" style="width: 200px; font-weight: bold;">Reporting Date</div>
							</div>
							<c:set var="isoDate">
								<fmt:formatDate value='${caseDetails.caseReportingDate}' pattern='yyyy-MM-dd' />
							</c:set>
							<input type="Date" class="form-control" id="caseReportingDate" placeholder="Case Reporting Date"
								value="${isoDate}" readonly>
						</div>
					</div>

					<div class="col-md-12">
						<label class="sr-only" for="assignToEmployee">Case Id</label>
						<div class="input-group mb-2">
							<div class="input-group-prepend">
								<div class="input-group-text" style="width: 200px; font-weight: bold;">Assign to Field officer</div>
							</div>
							<select class="form-control selectpicker" data-live-search="true" id="assignToEmployee" name="assignToEmployee" title="Select officer's name" required>
								<option value="" disabled selected>Select Field Officer</option>
								<c:forEach var="officer" items="${userList}">
									<c:choose>
										<c:when test="${assignedOfficer eq officer.userId}">
												<option value="${officer.userId}" data-subtext="${officer.loginId}" selected="selected">${officer.userName} [${officer.designation}]</option>
										</c:when>
										<c:otherwise>
											<option value="${officer.userId}" data-subtext="${officer.loginId}">${officer.userName} [${officer.designation}]</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- </div> -->
		<hr>
		<div class="row">
			<div class="col-md-9">
				<h4 title="Is permission required from the Supervisory Officer?">Assign case or not ?</h4>
			</div>
			<div class="col-md-3">
				<div class=" float-right">
					<button type="submit" id="submit_button" value="yes" class="btn btn-success" title="Yes"><i
							class="fas fa-check"></i></button>
					<button type="submit" id="submit_button" value="no" class="btn btn-danger" title="No"><i
							class="fa fa-times"></i></button>
				</div>
			</div>
		</div>
	</form>
</div>