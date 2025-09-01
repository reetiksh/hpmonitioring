<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
		<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
			<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


				<style>
					.btn-outline-custom {
						color: #495057;
						border: 1px solid #ced4da;
						text-align: left;
					}

					.class {
						border: 1px solid black;
						padding: 10px;
					}
				</style>


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
					// Disable back and forward cache
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
					}

				</script>

				<script>


					$('#module').on('change', function () {

						debugger;
						var module = $(this).val();
						var parameter = $("#parameter").val();

						$.ajax({
							url: '/cag_fo/validate_parameter',
							method: 'get',
							async: false,
							data: {
								parameter: parameter,
								module: module
							},
							success: function (result) {

								if (result != '') {

									if (module === 'audit') {
										alert(result);
										$("#module").val("");
										$("#span7").hide();
										$("#cagCaseId").prop("required", false);
									} else {
										alert(result);
										$("#module").val("");
									}

								}

							},
							error: function (error) {
							}
						});

					});


				</script>


				<script>
					$(function () {
						$('.selectpicker').selectpicker();
					});


					function onChangeCategory() {

						var categoryId = $("#categoryListId").val();

						if (categoryId == 1) {

							$("#span").show();

							$("#span1").hide();

							$("#span2").hide();

							$("#span4").hide();

							$("#module").prop("required", false);

							$("#reason").prop("required", false);

						} else if (categoryId == 2) {

							var caseListLength = Number($("#caseListSize").val());

							if (caseListLength > 0) {

								$("#categoryListId").val("");
								alert("This case is already initiated. Please link it to the corresponding module by selecting Already Initiated");

							} else {

								$("#span").hide();

								$("#span1").show();

								$("#span2").hide();

								$("#span4").hide();

								$("#module").prop("required", true);

								$("#reason").prop("required", false);
							}

						} else if (categoryId == 3) {

							$("#span").hide();

							$("#span1").hide();

							$("#span2").show();

							$("#span4").show();

							$("#module").prop("required", false);

							$("#reason").prop("required", true);

						}

					}


					function onChangeModule() {

						var module = $("#module").val();

						if (module == 'audit') {

							$("#span7").show();

							$("#cagCaseId").prop("required", true);

						} else {

							$("#span7").hide();

							$("#cagCaseId").prop("required", false);

						}

					}


					function validateInput() {


					}

				</script>


				<script>

					$('#caseUpdateForm').on('submit', function (oEvent) {

						oEvent.preventDefault();

						$.confirm({
							title: 'Confirm!',
							content: 'Do you want to proceed ahead with updating case details?',
							buttons: {
								submit: function () {

									var checkedValues = [];
									var checkboxes = document.querySelectorAll('.cagcheckbox:checked');

									checkboxes.forEach(function (checkbox) {
										checkedValues.push(checkbox.value);
									});

									$("#caseList").val(checkedValues);

									var category = $("#categoryListId").val();

									var categoryId = Number(category);

									var fileName = document.querySelector('#uploadedFile').value;

									var extension = fileName.slice((fileName.lastIndexOf(".") - 1 >>> 0) + 2);

									var input = document.getElementById('uploadedFile');

									if (input.files && input.files[0]) {

										var maxAllowedSize = 10 * 1024 * 1024;

										if (extension == 'pdf') {

											if (input.files[0].size > maxAllowedSize) {

												$.alert('Please upload max 10MB file');
												input.value = '';
											} else {

												if (categoryId === 1) {

													if (checkedValues.length === 0) {

														alert("Please link any case");

													} else {

														oEvent.currentTarget.submit();
													}

												} else if (categoryId === 2) {

													var parameter = document.getElementById("cagCaseId").value;

													$.ajax({
														url: '/cag_fo/validate_caseid',
														method: 'get',
														async: false,
														data: {
															parameter: parameter,
														},
														success: function (result) {

															if (result != '') {

																alert(result);
																$("#cagCaseId").val("");
															} else {

																oEvent.currentTarget.submit();
															}

														},
														error: function (error) {
														}
													});

												} else if (categoryId === 3) {

													oEvent.currentTarget.submit();
												}
											}

										} else {

											$.alert("Please upload only pdf file");
											document.querySelector('#uploadedFile').value = '';
										}

									} else {

										$.alert("Please upload pdf file");
									}

								},
								close: function () {
									$.alert('Canceled!');
								}
							}
						});
					});
				</script>
				<div class="card-body">
					<form method="POST" action="update_cag_cases" name="caseUpdateForm" id="caseUpdateForm"
						enctype="multipart/form-data">
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label for="gstin">GSTIN </label>
									<input class="form-control" id="gstin" name="GSTIN_ID" value="${viewItem.id.GSTIN}" name="gstin"
										readonly />
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="taxpayername">Taxpayer Name </label>
									<input class="form-control" id="taxpayerName" name="taxpayerName" value="${viewItem.taxpayerName}"
										name="taxpayerName" readonly />
								</div>
							</div>
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
							<div class="col-md-3">
								<div class="form-group">
									<label for="circle">Jurisdiction</label>
									<input class="form-control" id="circle" name="circle" value="${viewItem.locationDetails.locationName}"
										name="circle" readonly />
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="casecategory">Case Category </label>
									<input class="form-control" id="caseCategory" value="${viewItem.category}" name="category" readonly />
								</div>
							</div>
						</div>
						<input type="hidden" name="recovery" value="${viewItem.id.GSTIN}" />
						<input type="hidden" name="caseReportingDate_ID" value="${date}" />
						<input type="hidden" name="period_ID" value="${viewItem.id.period}" />
						<input type="hidden" id="parameter" name="parameter" value="${viewItem.id.parameter}" />
						<div class="row">
							<div class="col-md-3">
								<div class="form-group">
									<label for="casereportingdate">Period</label>
									<input class="form-control" value="${viewItem.id.period}" readonly />
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="casereportingdate">Reporting Date(DD-MM-YYYY)</label>
									<input class="form-control"
										value="<fmt:formatDate value='${viewItem.id.caseReportingDate}' pattern='dd-MM-yyyy' />" readonly />
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="suspectedIndicative">Parameter</label>
									<input class="form-control" value="${viewItem.id.parameter}" readonly />
								</div>
							</div>
							<div class="col-md-3">
								<div class="form-group">
									<label for="suspectedIndicative">Indicative Value(₹)</label>
									<input class="form-control" value="${viewItem.indicativeTaxValue}" name="indicativeTaxValue"
										readonly />
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6">
								<div class="form-group">
									<label for="caseStage">Category <span style="color: red">*</span></label>
									<select class="form-control" id="categoryListId" name="categoryListId" onchange="onChangeCategory()"
										title="Please select Case Stage" required>
										<option data-tokens="" value="">Select</option>
										<c:forEach items="${categories}" var="categories">
											<option data-tokens="" value="${categories.id}" <c:if test="${categories.id == categorys}">
												selected </c:if> >${categories.name}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<input type="hidden" id="caseList" name="caseList" value="" />
							<div class="col-md-6" id="span1" style="display:none;">
								<div class="form-group">
									<label for="caseStage">Module<span style="color: red">*</span></label>
									<select class="form-control" id="module" name="module" onchange="onChangeModule()"
										title="Please select Case Stage">
										<option data-tokens="" value="">Select</option>
										<option data-tokens="" value="assessment" <c:if test="${modules == 'assessment'}"> selected </c:if>
											>Assessment & Adjudication</option>
										<option data-tokens="" value="scrutiny" <c:if test="${modules == 'scrutiny'}"> selected </c:if>
											>Scrutiny</option>
										<option data-tokens="" value="audit" <c:if test="${modules == 'audit'}"> selected </c:if> >Audit
										</option>
									</select>
								</div>
							</div>
							<div class="col-md-6" id="span2" style="display:none;">
								<div class="form-group">
									<label for="caseStage">Reason <span style="color: red">*</span></label>
									<select class="form-control" id="reason" name="reason" title="Please select Case Stage">
										<option data-tokens="" value="">Select</option>
										<c:forEach items="${reasons}" var="reasons">
											<option data-tokens="" value="${reasons.id}" <c:if test="${reasons.id == reason}"> selected
												</c:if> >${reasons.name}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>
						<input type="hidden" id="caseListSize" value="${fn:length(modulesCaselist)}" />
						<div class="row" id="span" style="display:none;">
							<table id="dataListTable" class="table table-bordered table-striped">
								<thead>
									<tr>
										<th style="text-align: center; vertical-align: middle;">Case ID</th>
										<th style="text-align: center; vertical-align: middle;">Reporting Date<br>(DD-MM-YYYY)</th>
										<th style="text-align: center; vertical-align: middle;">Indicative Value(₹)</th>
										<th style="text-align: center; vertical-align: middle;">Case Stage</th>
										<th style="text-align: center; vertical-align: middle;">Amount(₹)</th>
										<th style="text-align: center; vertical-align: middle;">Recovery Via DRC03(₹)</th>
										<th style="text-align: center; vertical-align: middle;">Recovery Against Demand(₹)</th>
										<th style="text-align: center; vertical-align: middle;">Module</th>
										<th style="text-align: center; vertical-align: middle;">Action</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${modulesCaselist}" var="item">
										<tr>
											<td>${item.caseId}</td>
											<td>
												<fmt:formatDate value="${item.date}" pattern="dd-MM-yyyy" />
											</td>
											<td>${item.indicativeTaxValue}</td>
											<td>${item.caseStageName}</td>
											<td>${item.demand}</td>
											<td>${item.recoveryByDRC3}</td>
											<td>${item.recoveryAgainstDemand}</td>
											<td>${item.module}</td>
											<td><input type="checkbox" class="cagcheckbox"
													value="${item.GSTIN_ID}/${item.period_ID}/${item.caseReportingDate_ID}/${item.caseId}/${item.module}">
											</td>
										</tr>
									</c:forEach>
								</tbody>

							</table>
						</div>


						<div class="row">

							<div class="col-md-6">
								<div class="form-group">
									<label for="excelFile">File Upload <span style="color: red">*</span></label><span> (upload only pdf
										file with max file size of 10MB ) </span>
									<input class="form-control" type="file" id="uploadedFile" name="uploadedFile" accept=".pdf" required>
								</div>
							</div>

							<div class="col-md-6">
								<div class="form-group">
									<label for="">Remarks <span style="color: red">*</span></label>
									<input type="text" class="form-control" name="remarks" required />
								</div>
							</div>

						</div>

						<div class="row">

							<div class="col-md-6" id="span4" style="display:none;">
								<div class="form-group">
									<label for=""> Recovery Via DRC03(₹)</label>
									<input type="text" class="form-control" name="recoveryByDRC3" />
								</div>
							</div>

							<div class="col-md-6" id="span7" style="display:none;">
								<div class="form-group">
									<label for=""> Case Id <span style="color: red">*</span></label>
									<input type="text" class="form-control" maxlength="15" pattern="[A-Za-z0-9]{15}"
										title="Please enter 15-digits alphanumeric Case Id" id="cagCaseId" name="cagCaseId" />
								</div>
							</div>

						</div>

						<hr>

						<div class="row">
							<div class="col-md-12">
								<div class="form-group float-right">
									<button type="submit" class="btn btn-primary" id="submitCase">Submit</button>
								</div>
							</div>
						</div>
					</form>

				</div>
