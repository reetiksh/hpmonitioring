<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
</script>
<script>
	$('form').on('submit', function (oEvent) {
		oEvent.preventDefault();
		submitForApproval('${caseDetails.GSTIN}', '${caseDetails.period}', $("#caseReportingDate").val(), $(this).find(':submit').filter(':focus').val(), $("#caseId").val());
	});
</script>
<div class="card-body">
	<form method="POST" name="caseAssignmentAction" id="caseAssignmentAction" action=""
		enctype="multipart/form-data">
		<div class="row">
			<div class="col-md-12">
				<div class="row">
					<div class="col-md-12">
						<label class="sr-only" for="GSTIN">GSTIN</label>
						<div class="input-group mb-2">
							<div class="input-group-prepend">
								<div class="input-group-text" style="width: 140px; font-weight: bold;">GSTIN</div>
							</div>
							<input type="text" class="form-control" id="GSTIN" placeholder="GSTIN" value="${caseDetails.GSTIN}" readonly>
						</div>
					</div>
					<div class="col-md-12">
						<label class="sr-only" for="period">Period</label>
						<div class="input-group mb-2">
							<div class="input-group-prepend">
								<div class="input-group-text" style="width: 140px; font-weight: bold;">Period</div>
							</div>
							<input type="text" class="form-control" id="period" placeholder="Period" value="${caseDetails.period}" readonly>
						</div>
					</div>
					<div class="col-md-12">
						<label class="sr-only" for="caseReportingDate">Case Reporting Date</label>
						<div class="input-group mb-2">
							<div class="input-group-prepend">
								<div class="input-group-text" style="width: 140px; font-weight: bold;">Reporting Date</div>
							</div>
							<c:set var="isoDate">
								<fmt:formatDate value='${caseDetails.caseReportingDate}' pattern='yyyy-MM-dd' />
							</c:set>
							<input type="Date" class="form-control" id="caseReportingDate" placeholder="Case Reporting Date" value="${isoDate}" readonly>
						</div>
					</div>
					<div class="col-md-12">
						<label class="sr-only" for="caseReportingDate">Case Id</label>
						<div class="input-group mb-2">
							<div class="input-group-prepend">
								<div class="input-group-text" style="width: 140px; font-weight: bold;">Case Id</div>
							</div>
							<input type="text" class="form-control" id="caseId" minlength="15" maxlength="15"
								title="Case ID must be 15 digit alphanumeric (letters and numbers only)" pattern="[a-zA-Z0-9]+" 
								placeholder="Please Enter Case Id" required />
								<div class="invalid-feedback">
									Please Enter valid case id.
								</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<hr>
		<div class="row">
				<div class="col-md-9">
					<h4 title="Is permission required from the Supervisory Officer?">Permission Required?</h4>
				</div>
				<div class="col-md-3">
					<div class=" float-right">
						<button type="submit" id="submit_button" value="yes" class="btn btn-success" title="Yes"><i class="fas fa-check"></i></button>
						<button type="submit" id="submit_button" value="no" class="btn btn-danger"  title="No"><i class="fa fa-times"></i></button>
					</div>
				</div>
			</div>
		</div>
	</form>
</div>