<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>

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
</script>
<script>
	$('#updateTransferRoleAction').on('submit', function(oEvent) {
		oEvent.preventDefault();
		$.confirm({
			title : 'Confirm!',
			content : 'Do you want to proceed ahead with recommending this case for MCM?',
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

	$(document).ready(function () {
			$("#workingDate1").on("change", function () {
				var effectiveDate = $("#workingDate1").val();
				var now = new Date();
				var previousStatusDate = new Date("${previousStatusDate}");
				now.setHours(23, 59, 59, 999);
				const givenDate = new Date(effectiveDate);

				if (givenDate > now) {
					$("#workingDate1").val('');

					var x = document.getElementById("message");
					if (x.style.display === "none") {
						$("#alertMessage").html("Future date is not allowed").css("color", "#fffff");
						x.style.display = "block";
						showDiv();
					}
				} else if (givenDate < previousStatusDate) {
					$("#workingDate1").val('');

					var x = document.getElementById("message");
					if (x.style.display === "none") {
						$("#alertMessage").html("The entered date cannot be before " + formatDate(previousStatusDate)).css("color", "#fffff");
						x.style.display = "block";
						showDiv();
					}
				}
			});
		});

		function showDiv() {
			$("#message").fadeTo(2000, 500).slideUp(500, function () {
				$("#message").slideUp(500);
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
		<form method="POST" name="updateTransferRoleAction" id="updateTransferRoleAction" action="/l2/recomandedToMcm" enctype="multipart/form-data" >
			<div class="row">
				<div class="col-md-12 row">
					<div class="col-md-12">
						<div class="form-group">
							<div class="row">
								<div class="col-md-6">
									<div class="row">
										<div class="col-md-4">
											<label for="caseId">Case id : </label>
										</div>
										<input type="text" class="col-md-7 form-control" id="caseId" name="caseId" value="${caseId}" readonly/>
									</div>
								</div>
								<div class="col-md-6">
									<div class="row">
										<div class="col-md-4">
											<label for="caseId">Date : <span style="color: red;"> *</span></label>
										</div>
										<input type="Date" class="col-md-7 form-control" id="workingDate1" name="workingDate" required/>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="col-md-12">
						<div class="form-group">
							<label for="remarks">Remarks <span style="color: red;"> *</span></label>
							<input type="text" class="form-control" id="comment" name="comment" placeholder="Please enter your remarks" maxlength="100" title="Please enter your remarks with in 100 letters" required/>
						</div>
					</div>
				</div>
				<div class="col-12 alert alert-danger alert-dismissible fade show" role="alert" id="message" style="max-height: 500px; overflow-y: auto; display: none;">
					<span id="alertMessage"></span>
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
				<input type="hidden" name="updateStatusId" id="updateStatusId" value="${updateStatusId}" />
			</div>
			<hr>
			<div class="row">
				<div class="col-md-12">
					<div class="float-right">
							<button type="submit" class="btn btn-primary">Recommend to MCM</button>
					</div>
				</div>
			</div>
		</form>
	</div>


