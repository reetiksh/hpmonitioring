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

	function showDiv(){
		$("#message1").fadeTo(2000, 500).slideUp(500, function() {
			$("#message1").slideUp(500);
		});
	}

	$(document).ready(function() {
		$("#messageOutput").fadeTo(10000, 500).slideUp(500, function() {
			$("#messageOutput").slideUp(500);
		});
	});
</script>

<script>
function submitUpdateTransferRoleAction() {
	
	// oEvent.preventDefault();
	var transferId = $("#transferId").val();
	var transferToUserId = $("#transferToUserId").val();
	var effectiveDate = $("#effectiveDate").val();

	var effectiveDate = $("#effectiveDate").val();
	var now = new Date();
	now.setHours(0, 0, 0, 0);
	const givenDate = new Date(effectiveDate);
	if(givenDate < now){
		var x = document.getElementById("message1");
		if(x.style.display === "none"){
			$("#alertMessage1").html("Please provide the valid effective date.").css("color","#fffff");
			x.style.display = "block";
			showDiv();
		}
		return;
	}
	
	$.confirm({
		title : 'Confirm!',
		content : 'Do you want to proceed ahead with updating transfer request?',
		buttons : {
			submit : function() {
				const url = '/admin/update_transfer_role_action?transferId='+transferId+'&transferToUserId='+transferToUserId+'&effectiveDate='+effectiveDate;
        const data = {
					transferId: transferId,
					transferToUserId: transferToUserId,
					effectiveDate: effectiveDate
        };

        fetch(url, {
					method: 'POST',
					headers: {
							'Content-Type': 'application/json',
							'X-CSRF-Token': '${_csrf.token}'
					},
					body: JSON.stringify(data)
        })
        .then(response => {
            if (response.ok) {
                return response.text();
            }
            throw new Error('Network response was not ok.');
        })
        .then(data => {
            const obj = JSON.parse(data);

						if(obj.dataInfo == 'error'){
							$("#alertMessage1").html(obj.uploadDataError).css("color","#fffff");
							var x = document.getElementById("message1");
							x.style.display = "block";
							showDiv();
						} else {
							Swal.fire({
								icon: 'success',
								title: 'Success!',
								text: obj.uploadDataInfo,
								}).then(() => {
									window.location.assign(obj.link);
							});	
						}

        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });


				// $.ajax({url: '/checkLoginStatus',
				// 	method: 'post',
				// 	async: false,
				// 	success: function(result){
				// 		const myJSON = JSON.parse(result);
				// 		if(result=='true'){
				// 			$("#viewTransferRole").load('/admin/update_transfer_role_action?transferId='+transferId+'&transferToUserId='+transferToUserId+'&effectiveDate='+effectiveDate, function(response, status, xhr){
				// 				if(status == 'success'){
				// 						$("#viewTransferRoleModal").modal('show');
				// 				}else{
				// 					console.log("failed");
				// 				}
				// 			});
				// 		} else if(result=='false'){
				// 			window.location.reload();
				// 		}
				// 	}         
				// });

			  // oEvent.currentTarget.submit();
			},
			close : function() {
				$.alert('Canceled!');
			}
		}
	});
}
</script>

	<div class="card-body">
	<form name="updateTransferRoleAction" id="updateTransferRoleAction" enctype="multipart/form-data" >
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label for="transferFromUserName">Transfer from : 
						<c:out value='${transferRole.userRoleMapping.userDetails.firstName}'/>
						<c:out value='${transferRole.userRoleMapping.userDetails.middleName}'/>
						<c:out value='${transferRole.userRoleMapping.userDetails.lastName}'/>
						<c:out value="${[transferRole.userRoleMapping.userDetails.loginName]}" />
						(<c:out value="${transferRole.userRoleMapping.userDetails.designation.designationAcronym}" />)
					</label> 
					<br>
					<br>
					<div id="div1">
						<label for="RoleLocation">Role - 
							<c:out value="${transferRole.userRoleMapping.userRole.roleName}" /> : 
							<c:if test = "${transferRole.userRoleMapping.circleDetails.circleId ne 'NA'}">
								<c:out value="${transferRole.userRoleMapping.circleDetails.circleName}" /> (Circle)
							</c:if>
							<c:if test = "${transferRole.userRoleMapping.zoneDetails.zoneId ne 'NA'}">
								<c:out value="${transferRole.userRoleMapping.zoneDetails.zoneName}" /> (Zone)
							</c:if>
							<c:if test = "${transferRole.userRoleMapping.stateDetails.stateId ne 'NA'}">
								<c:out value="${transferRole.userRoleMapping.stateDetails.stateName}" /> (State)
							</c:if>
						</label> 
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label for="effectiveDate">Effective Date <span style="color: red;"> *</span></label>
									<c:set var="isoDate"><fmt:formatDate value='${transferRole.actionDate}' pattern='yyyy-MM-dd'/></c:set>
									<input type="date" class="form-control" id="effectiveDate" name="effectiveDate" value="${isoDate}" required/>
								</div>
							</div>
							<div class="col-md-9">
								<div class="form-group">
									<label for="effectiveDate">Transfer To <span style="color: red;"> *</span></label>
									<select class="form-control selectpicker" data-live-search="true" id="transferToUserId" name="transferToUserId">
										<option value="" disabled selected>Select officer name</option>
										<c:forEach items="${userList}" var="user">
											<c:if test="${transferRole.transferToUser.userId eq user.userId}">
												<option value="${user.userId}" data-subtext="${user.designation.designationAcronym}" selected>${user.firstName} ${user.middleName} ${user.lastName} [${user.loginName}] ${user.designation.designationAcronym}</option>
											</c:if>
											<c:if test="${userId ne user.userId}">
												<c:if test="${transferRole.userRoleMapping.userDetails.userId ne user.userId}">
													<option value="${user.userId}" data-subtext="${user.designation.designationAcronym}">${user.firstName} ${user.middleName} ${user.lastName} [${user.loginName}]</option>
												</c:if>
											</c:if>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
					</div>
					<br>
					<br>
					<label for="transferFromUserName">Requested By : 
						<c:out value='${transferRole.requestedByUserId.firstName}'/>
						<c:out value='${transferRole.requestedByUserId.middleName}'/>
						<c:out value='${transferRole.requestedByUserId.lastName}'/>
						<c:out value="${[transferRole.requestedByUserId.loginName]}" />
						(<c:out value="${transferRole.requestedByUserId.designation.designationAcronym}" />)
					</label> 
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
					  <button type="button" onclick="submitUpdateTransferRoleAction()" class="btn btn-primary">Update</button>
				</div>
			</div>
		</div>
		</form>
	</div>


