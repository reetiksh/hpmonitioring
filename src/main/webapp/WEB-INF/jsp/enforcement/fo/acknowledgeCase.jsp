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
	// $('form').on('submit', function (oEvent) {
	// 	oEvent.preventDefault();
	// 	submitForApproval('${caseDetails.GSTIN}', '${caseDetails.period}', $("#caseReportingDate").val(), $(this).find(':submit').filter(':focus').val(), $("#caseId").val());
	// });

	$('form').on('submit', function(oEvent) {
		oEvent.preventDefault();
		$.confirm({
			title : 'Confirm!',
			content : 'Do you want to proceed ahead with submission of the case?',
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
</script>
<div class="card-body">
	<form method="POST" id="uploadForm" name="uploadForm" action="acknowledgement" enctype="multipart/form-data">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		<div class="row">
			<div class="col-md-12">
				<div class="row">
					<div class="col-md-12">
						<label class="sr-only" for="GSTIN">GSTIN</label>
						<div class="input-group mb-2">
							<div class="input-group-prepend">
								<div class="input-group-text" style="width: 140px; font-weight: bold;">GSTIN</div>
							</div>
							<input type="text" class="form-control" id="GSTIN" name="GSTIN" placeholder="GSTIN" value="${caseDetails.GSTIN}" readonly>
						</div>
					</div>
					<div class="col-md-12">
						<label class="sr-only" for="period">Period</label>
						<div class="input-group mb-2">
							<div class="input-group-prepend">
								<div class="input-group-text" style="width: 140px; font-weight: bold;">Period</div>
							</div>
							<input type="text" class="form-control" id="period" name="period" placeholder="Period" value="${caseDetails.period}" readonly>
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
							<input type="Date" class="form-control" id="caseReportingDate" name="caseReportingDate" placeholder="Case Reporting Date" value="${isoDate}" readonly>
						</div>
					</div>
					<div class="col-md-12">
						<label class="sr-only" for="caseType">Case Type</label>
						<div class="input-group mb-2">
							<div class="input-group-prepend">
								<div class="input-group-text" style="width: 140px; font-weight: bold;">Case Type</div>
							</div>
							<select class="custom-select" id="caseType" name="caseType" required>
								<option value="" disabled selected>Please select</option>
								<option value="enforcement">Enforcement</option>
								<option value="scrutiny">Scrutiny</option>
							</select>
						</div>
					</div>
				</div>
			</div>
		</div>
		<hr>
		<div class="row">
				<div class="col-md-12">
					<div class=" float-right">
						<button type="submit" class="btn btn-success" title="Submit"><i class="fas fa-check"></i></button>
					</div>
				</div>
			</div>
		</div>
	</form>
</div>