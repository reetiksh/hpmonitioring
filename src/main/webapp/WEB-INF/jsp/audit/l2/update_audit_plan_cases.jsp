<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

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
			content : 'Do you want to proceed ahead with ' + action +' of audit plan?',
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
    document.getElementById("buttonAction").value = action;
  }

	// $(document).ready(function() {
	// 	$("#workingDate").on("change", function() {
	// 		var effectiveDate = $("#workingDate").val();
	// 		var now = new Date();
	// 		now.setHours(23, 59, 59, 999);
	// 		const givenDate = new Date(effectiveDate);
			
	// 		if (givenDate > now) {
	// 			$("#workingDate").val('');

	// 			var x = document.getElementById("message1");
	// 			if(x.style.display === "none"){
	// 				$("#alertMessage1").html("Future date is not allowed").css("color","#fffff");
	// 				x.style.display = "block";
	// 				showDiv();
	// 			}
	// 		}
	// 	});
	// });

	$(document).ready(function () {
		$("#workingDate").on("change", function () {
			var effectiveDate = $("#workingDate").val();
			var now = new Date();
			var previousStatusDate = new Date("${previousStatusDate}");
			now.setHours(23, 59, 59, 999);
			const givenDate = new Date(effectiveDate);

			if (givenDate > now) {
				$("#workingDate").val('');

				var x = document.getElementById("message1");
				if (x.style.display === "none") {
					$("#alertMessage1").html("Future date is not allowed").css("color", "#fffff");
					x.style.display = "block";
					showDiv();
				}
			} else if (givenDate < previousStatusDate) {
				$("#workingDate").val('');

				var x = document.getElementById("message1");
				if (x.style.display === "none") {
					$("#alertMessage1").html("The entered date cannot be before " + formatDate(previousStatusDate)).css("color", "#fffff");
					x.style.display = "block";
					showDiv();
				}
			}
		});
	});

	function formatDate(date) {
		var day = String(date.getDate()).padStart(2, '0');
		var month = String(date.getMonth() + 1).padStart(2, '0');
		var year = date.getFullYear();
		return day + "-" + month + "-" + year;
	}

	function showDiv(){
		$("#message1").fadeTo(2000, 500).slideUp(500, function() {
			$("#message1").slideUp(500);
		});
	}
</script>
	<div class="card-body">
		<form method="POST" name="caseAssignmentAction" id="caseAssignmentAction" action="/l2/update_audit_plan_cases" enctype="multipart/form-data" >
			<div class="row">
				<div class="col-md-12">
						<div class="row">
							<div class="col-md-12">
								<div class="row">
									<div class="col-md-3">
										<label for="caseId">Case Id</label>
										<div class="form-group">
											<input type="text" class="form-control" id="caseId" name="caseId" value="${caseId}" readonly/>
										</div>
									</div>
									<div class="col-md-9">
										<div class="form-group">
											<label for="pdfFileDownloadDiv">Supporting Document: <span style="color: red;"> *</span></label>
											<div id="pdfFileDownloadDiv">
												<a href="/l2/downloadFile?fileName=${documentObject.actionFilePath}">
													<span id="pdfFileName"><img class="animation__shake" src="/static/image/PDF_file_icon.png" alt="pdf" height="40" width="34">  ${documentObject.actionFilePath}</span>
												</a>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="workingDate">Date <span style="color: red;"> *</span></label>
									<input type="date" class="form-control" id="workingDate" name="workingDate" required/>
								</div>
							</div>
              <div class="col-md-9">
								<div class="form-group">
									<label for="comment" title="Remarks should be within 100 letter">Remarks:</label>
									<textarea type="text" class="form-control" id="comment" name="comment" maxlength="100" placeholder="Please enter your remarks" title="Please enter your remarks within 100 letter"></textarea>
								</div>
							</div>
						</div>
				</div>
				<div class="col-lg-12 alert alert-danger alert-dismissible fade show" role="alert" id="message1" style="max-height: 500px; overflow-y: auto; display: none;">
					<span id="alertMessage1"></span>
					<button type="button" class="close" data-dismiss="alert" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <input type="hidden" id="buttonAction" name="buttonAction" value="" />
			</div>
			<hr>
			<div class="row">
				<div class="col-md-12">
					<div class="float-right">
							<button type="submit" onclick="submitForm('approval')" class="btn btn-primary">Approve</button>
              <button type="submit" onclick="submitForm('rejection')" class="btn btn-danger">Reject</button>
					</div>
				</div>
			</div>
		</form>
	</div>


