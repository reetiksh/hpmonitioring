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
	$(document).ready(function() {
		$('#location').change(function() {
				var selectedLocation = $(this).val();
				$.ajax({
						url: '/hq/get_all_assigned_users', // Change this to your actual endpoint
						method: 'GET', // Change this to the appropriate HTTP method
						data: { location: selectedLocation }, // Pass the selected location as data
						success: function(response) {
                // Assuming response is an array of objects with 'id' and 'name' properties
								var $assignedEmployeeDiv = $('#assignToEmployeeDiv');
                var $assignedEmployee = $('#assignToEmployee');
                $assignedEmployee.empty(); // Clear existing options
								$assignedEmployee.append('<option value="" disabled selected>Select officer name</option>');
                $.each(response, function(index, employee) {
                    $assignedEmployee.append($('<option>', {
                        value: employee.userId,
                        text: employee.userName + " (" + employee.designation + ") : " + employee.loginId
                    }));
                });
								$assignedEmployeeDiv.show();
            },
						error: function(xhr, status, error) {
								console.error('Error:', error);
								// Handle error if needed
						}
				});
		});
	});

	function dropdownChanged() {
		var x = document.getElementById("otherRemarkDiv");
		var selectElement = document.getElementById("remark");
		var flag = false;

		for (var i = 0; i < selectElement.options.length; i++) {
			if (selectElement.options[i].selected && selectElement.options[i].value === "Others") {
				flag = true;
			}
		}

		if(flag == true){
			document.getElementById("otherRemarks").required = true;
			if(x.style.display === "none"){
				x.style.display = "block";
			}
		} else {
			document.getElementById("otherRemarks").required = false;
			x.style.display = "none";
		}

	}
</script>

<script>
	$('form').on('submit', function(oEvent) {
		oEvent.preventDefault();
		$.confirm({
			title : 'Confirm!',
			content : 'Do you want to proceed ahead to update the case(s)?',
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
		<form method="POST" name="updateTransferRoleAction" id="updateTransferRoleAction" action="/hq/update_enforcement_case" enctype="multipart/form-data" >
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<div class="row">
							<div class="col-md-4">
								<div class="form-group">
									<label for="GSTIN">GSTIN</label>
									<input type="text" class="form-control" id="GSTIN" name="GSTIN" value="${enforcementReviewCase.id.GSTIN}" readonly/>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label for="period">Period</label>
									<input type="text" class="form-control" id="period" name="period" value="${enforcementReviewCase.id.period}" readonly/>
								</div>
							</div>
							<div class="col-md-4">
								<div class="form-group">
									<label for="caseReportingDate">Case Reporting Date</label>
									<c:set var="isoDate"><fmt:formatDate value='${enforcementReviewCase.id.caseReportingDate}' pattern='yyyy-MM-dd'/></c:set>
									<input type="date" class="form-control" id="caseReportingDate" name="caseReportingDate" value="${isoDate}" readonly/>
								</div>
							</div>
							<div class="col-md-6">
								<div class="form-group">
									<label for="location">Working Location <span style="color: red;"> *</span></label>
									<select class="form-control selectpicker" data-live-search="true" id="location" name="location" title="Select working location">
										<c:forEach items="${locationList}" var="location">
											<c:if test="${location.locationId eq enforcementReviewCase.locationDetails.locationId}">
												<option value="${location.locationId}" data-subtext="${user.designation.designationAcronym}" selected>${location.locationName} [${location.locationType}]</option>
											</c:if>
											<c:if test="${location.locationId ne enforcementReviewCase.locationDetails.locationId}">
												<option value="${location.locationId}" data-subtext="${user.designation.designationAcronym}">${location.locationName} [${location.locationType}]</option>
											</c:if>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="col-md-6" id="assignToEmployeeDiv" style="display:none;">
								<div class="col-md-12">
									<div class="form-group">
										<label for="assignToEmployee">Assign to Field officer <span style="color: red;"> *</span></label>
										<select class="form-control" id="assignToEmployee" name="assignToEmployee" title="Select officer's name" required>
										</select>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label for="remark">Remarks <span style="color: red;"> *</span><span id="remark_alert"></span></label>
											<select id="remark" name="remark" class="selectpicker col-md-12" data-live-search="true" onchange="dropdownChanged()" title="Please Select Remarks" multiple required>
												<c:forEach items="${remarks}" var="remark">
													<option value="${remark.name}">${remark.name}</option>
												</c:forEach>
											</select>
										</div>
									</div>
									<div class="col-md-6" style="display: none;" id="otherRemarkDiv">
										<div class="form-group">
											<label for="otherRemarks">Other Remarks <span style="color: red;"> *</span><span id="otherRemarks_alert"></span></label>
											<input type="text" class="form-control" id="otherRemarks" name="otherRemarks" placeholder="Please Enter Your Remarks"/>
										</div>
									</div>
								</div>
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
							<button type="submit" class="btn btn-primary">Submit</button>
					</div>
				</div>
			</div>
		</form>
	</div>


