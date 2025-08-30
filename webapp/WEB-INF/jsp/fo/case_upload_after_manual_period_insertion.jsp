<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Period-wise Breakup of Old Detailed Enforcement Cases</title>

<!-- <link rel="stylesheet" href="/static/dist/css/googleFront/googleFrontFamilySourceSansPro.css"> -->
<link rel="stylesheet"
	href="/static/plugins/fontawesome-free/css/all.min.css">
<link rel="stylesheet"
	href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
<link rel="stylesheet"
	href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
<link rel="stylesheet"
	href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
<link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
<link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
<link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
</head>

<style>
.modal-lg, .modal-xl {
	max-width: 800px;
}

.table-responsive {
	overflow: scroll;
}

/* Style for all select elements */
select {
	width: 100%; /* Set the width as needed */
	padding: 8px; /* Add padding */
	border: 1px solid #ccc; /* Add border */
	border-radius: 4px; /* Add border radius */
	box-sizing: border-box;
	/* Ensure padding and border are included in width */
}

/* Style for the option elements */
select option {
	background-color: #f2f2f2; /* Set background color */
	color: #000; /* Set text color */
}

/* Style for the hover effect on options */
select option:hover {
	background-color: #ddd; /* Change background color on hover */
}
</style>

<div class="modal fade" id="uploadOldCasesManuallyModal" tabindex="-1"
	role="dialog" aria-labelledby="uploadOldCasesManuallyTitle"
	aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered modal-lg"
		role="document">
		<div class="modal-content">

			<div class="modal-header">
				<h5 class="modal-title" id="raiseQueryModalTitle">
					<b>Update Period/Indicative Value</b>
				</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<!-- <div class="popuprq" id="myPopuprq">
        <p>Are You Sure To Raise Query For The Following Case ?</p>
        <button onclick="onOkrq()">OK</button>
        <button onclick="onCancelrq()">Cancel</button>
	    </div> -->
			<div class="modal-body">
				<form method="POST" id="manualOldCasesDetails"
					action="save_cases_with_manual_period_insertion">

					<input type="hidden" id="workinglocationid" name="workinglocationid">
					<input type="hidden" id="oldCaseId" name="oldCaseId">  
					<input type="hidden" id="periodIndicativeJson" name="periodIndicativeJson"> 
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
					<div id="selectUniquePeriod" style="color: red;display: none;">Please select unique period  !</div>
					<div id="selectIndicativeTaxValue" style="color: red;display: none;">Please fill indicative tax value  !</div>
					<div id="selectPeriodAndIndicativeTaxValue" style="color: red;display: none;">Please select the period and indicative tax value by click on add row button !</div>

					<div class="row">
						<div class="col-md-6">
							<div class="form-group">
								<label for="excelFile">GSTIN</label>
								<!-- <input class="form-control" type="file" id="appealRevisionFile"  name="appealRevisionFile" accept=".pdf"  > -->
								<input class="form-control" id="gstinOldCases"
									name="gstinOldCases" value="" readonly />
							</div>
						</div>

						<div class="col-md-6">
							<div class="form-group">
								<label for="excelFile">Taxpayer Name</label>
								<!-- <input class="form-control" type="file" id="appealRevisionFile"  name="appealRevisionFile" accept=".pdf"  > -->
								<input class="form-control" id="taxPayerNameOldCases"
									name="taxPayerNameOldCases" value="" readonly />
							</div>
						</div>

						<div class="col-md-6">
							<div class="form-group">
								<label for="excelFile">Jurisdiction</label>
								<!-- <input class="form-control" type="file" id="appealRevisionFile"  name="appealRevisionFile" accept=".pdf"  > -->
								<input class="form-control" id="jurisdictionOldCases"
									name="jurisdictionOldCases" value="" readonly />
							</div>
						</div>

						<div class="col-md-6">
							<div class="form-group">
								<label for="excelFile">Reporting Date (DD-MM-YYYY)</label>
								<!-- <input class="form-control" type="file" id="appealRevisionFile"  name="appealRevisionFile" accept=".pdf"  > -->
								<input class="form-control" id="reportingDateOldCases"
									name="reportingDateOldCases" value="" readonly />
							</div>
						</div>
						
						<div class="col-md-6">
							<div class="form-group">
								<label for="excelFile">Indicative Value</label>
								<input class="form-control" id="indicativeTaxValueOldCases"
									name="indicativeTaxValueOldCases" value="" readonly />
							</div>
						</div>




					</div>

					<%-- <table id="manual_period_indicative_value_table">
		            <tr>
		                <td>
		                    <select name="dropdown[]">
             <c:forEach items="${periods}" var="period">
					<option value="${period}">${period}</option>
			</c:forEach>
		              </select>
		          </td>
		          <td><input type="text" name="textbox[]" onkeypress="return isNumberKey(event)"></td>
		      </tr>
		  </table> --%>

				<table id="manual_period_indicative_value_table"
						class=" table order-list">
						<thead>
						</thead>
						<tbody>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="5" style="text-align: left;"><input
									type="button" class="btn btn-primary" id="addrow"
									value="Add Row" /></td>
							</tr>
							<tr>
							</tr>
						</tfoot>
					</table>
			</form>
			
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-primary" id="transferCaseBtn"
					onclick="submitVerifierRaiseQueryRemarksForm()">Submit</button>
			</div>
		</div>
	</div>
</div>


<body class="hold-transition sidebar-mini">
	<div class="wrapper">
		<jsp:include page="../layout/header.jsp" />
		<!-- <jsp:include page="../layout/confirmation_popup.jsp"/> -->
		<jsp:include page="../fo/transfer_popup.jsp" />
		<jsp:include page="../layout/sidebar.jsp" />
		<div class="content-wrapper">
			<section class="content-header">
				<div class="container-fluid">
					<div class="row mb-2">
						<div class="col-sm-6">
							<h1>Period-wise Breakup of Old Detailed Enforcement Cases</h1>
						</div>
						<div class="col-sm-6">
							<ol class="breadcrumb float-sm-right">
								<li class="breadcrumb-item"><a>Home</a></li>
								<li class="breadcrumb-item active">Period-wise Breakup of Old Detailed Enforcement Cases</li>
							</ol>
						</div>
					</div>
					
					
				</div>
			</section>
			<section class="content">
				<div class="container-fluid">
					<div class="row">
						<div class="col-12">
							<div class="card card-primary">
								<div class="card-header">
									<h3 class="card-title">Period-wise Breakup of Old Detailed Enforcement Cases</h3>
								</div>
								
								<div class="alert alert-success" role="alert" style="display: none;" id="uploadManualCasesBg">
              						<div style="display:none;" id="uploadManualCasesBgTitle">Manual Cases Uploaded Successfully !</div>
              					</div>
              					
								<div class="card-body">
									<c:if test="${not empty message}">
										<div class="alert alert-success alert-dismissible fade show"
											id="message" role="alert">
											<strong>${message}</strong>
											<button type="button" class="close" data-dismiss="alert"
												aria-label="Close">
												<span aria-hidden="true">&times;</span>
											</button>
										</div>
									</c:if>
									<c:if test="${not empty acknowlegdemessage}">
										<div class="alert alert-success alert-dismissible fade show"
											id="message" role="alert">
											<strong>${acknowlegdemessage}</strong>
											<button type="button" class="close" data-dismiss="alert"
												aria-label="Close">
												<span aria-hidden="true">&times;</span>
											</button>
										</div>
									</c:if>
									<table id="example1" class="table table-bordered table-striped">
										<thead>
											<tr>
												<th style="text-align: center; vertical-align: middle;">GSTIN</th>
												<th style="text-align: center; vertical-align: middle;">Taxpayer
													Name</th>
												<th style="text-align: center; vertical-align: middle;">Jurisdiction</th>
												<th style="text-align: center; vertical-align: middle;">Reporting
													Date (DD-MM-YYYY)</th>
												<th style="text-align: center; vertical-align: middle;">Indicative
													Value(₹)</th>
													<th style="text-align: center; vertical-align: middle;">Case Category</th>
												<th style="text-align: center; vertical-align: middle;">Period-wise Indicative Value (₹)</th>
												
												<!-- <th style="text-align: center; vertical-align: middle;">Action</th> -->
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${oldCasesUploadManuallyList}"
												var="oldCasesSolo">
												<tr>
													<td>${oldCasesSolo.gstin}</td>
													<td>${oldCasesSolo.taxpayerName}</td>
													<td>${oldCasesSolo.locationDetails.locationName}</td>
													<td><fmt:formatDate
															value="${oldCasesSolo.caseReportingDate}"
															pattern="dd-MM-yyyy" /></td>
													<td>${oldCasesSolo.indicativeTaxValue}</td>
													<td>Detailed Enforcement Old Cases</td>
													
													<%-- <td>
														<table id="manual_period_indicative_value_table">
												            <tr>
												                <td>
												                    <select name="dropdown[]">
												                        <c:forEach items="${periods}" var="period">
																			<option value="${period}">${period}</option>
																		</c:forEach>
												                    </select>
												                </td>
												                <td><input type="text" name="textbox[]" onkeypress="return isNumberKey(event)"></td>
												            </tr>
												        </table>
												        <input type="button" value="Add Row" onclick="addRow()">
        												<input type="button" value="Delete Row" onclick="deleteRow()">
														<input type="button" value="Collect Values" onclick="collectValues()">
													</td> --%>
													<td>
														<button type="button"
															onclick="submitManualCasesWithPeriods('${oldCasesSolo.gstin}','${oldCasesSolo.taxpayerName}','${oldCasesSolo.locationDetails.locationId}','${oldCasesSolo.locationDetails.locationName}','${oldCasesSolo.caseReportingDate}','${oldCasesSolo.id}','${oldCasesSolo.indicativeTaxValue}');"
															class="btn btn-primary" id="submitManualCasesUpload"
															style="margin-bottom: 4px">Submit</button>
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</section>
		</div>
		<jsp:include page="../layout/footer.jsp" />
		<aside class="control-sidebar control-sidebar-dark"></aside>
	</div>


	<%-- <form method="POST"
		id="submitManualCasesWithPeriodsAndIndicativeValues"
		action="submit_old_cases_manually">
		<input type="hidden" id="gstinOldCases" name="gstinOldCases">
		<input type="hidden" id="taxpayernameOldCases"
			name="taxpayernameOldCases"> <input type="hidden"
			id="workinglocationOldCases" name="workinglocationOldCases">
		<input type="hidden" id="reportingdateOldCases"
			name="reportingdateOldCases"> <input type="hidden"
			id="periodOldCases" name="periodOldCases"> <input
			type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	</form> --%>


	<%-- <div class="modal fade" id="confirmationModal" tabindex="-1"
		role="dialog" aria-labelledby="confirmationModalTitle"
		aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered" role="document">
			<div class="modal-content">
				<form method="POST" action="fo_acknowledge_cases"
					name="acknowledgeForm">
					<div class="modal-header">
						<h5 class="modal-title" id="confirmationModalTitle">Do you want to proceed ahead with the acknowledgement of case?</h5>
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
					</div>

					<input type="hidden" id="gstno_acknowledge" name="gstno"> <input
						type="hidden" id="date_acknowledge" name="date"> <input
						type="hidden" id="period_acknowledge" name="period"> <input
						type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

					<div class="modal-footer">
						<button type="submit" class="btn btn-primary" id="okayBtn">Okay</button>
						<button type="button" class="btn btn-secondary"
							data-dismiss="modal" id="cancelBtn">Cancel</button>
					</div>
				</form>
			</div>
		</div>
	</div> --%>

	<script src="/static/plugins/jquery/jquery.min.js"></script>
	<script src="/static/dist/js/jquery-confirm.min.js"></script>
	<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
	<script src="/static/plugins/datatables/jquery.dataTables.min.js"></script>
	<script
		src="/static/plugins/datatables-bs4/js/dataTables.bootstrap4.min.js"></script>
	<script
		src="/static/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
	<script
		src="/static/plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
	<script
		src="/static/plugins/datatables-buttons/js/dataTables.buttons.min.js"></script>
	<script
		src="/static/plugins/datatables-buttons/js/buttons.bootstrap4.min.js"></script>
	<script src="/static/plugins/jszip/jszip.min.js"></script>
	<script src="/static/plugins/pdfmake/pdfmake.min.js"></script>
	<script src="/static/plugins/pdfmake/vfs_fonts.js"></script>
	<script
		src="/static/plugins/datatables-buttons/js/buttons.html5.min.js"></script>
	<script
		src="/static/plugins/datatables-buttons/js/buttons.print.min.js"></script>
	<script
		src="/static/plugins/datatables-buttons/js/buttons.colVis.min.js"></script>
	<script src="/static/dist/js/bootstrap-select.min.js"></script>
	<script src="/static/dist/js/adminlte.min.js"></script>
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
	 // Disable back and forward cache
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
		$(document).ready(function() {
			$("#message").fadeTo(2000, 500).slideUp(500, function() {
				$("#message").slideUp(500);
			});
		});

		$(function() {
			$("#example1").DataTable({
				"responsive" : true,
				"lengthChange" : false,
				"autoWidth" : false,
				"buttons" : ["excel", "pdf", "print"]
			}).buttons().container().appendTo(
					'#example1_wrapper .col-md-6:eq(0)');
			$('#example2').DataTable({
				"paging" : true,
				"lengthChange" : false,
				"searching" : false,
				"ordering" : true,
				"info" : true,
				"autoWidth" : false,
				"responsive" : true,
			});
		});

		
		
	</script>


	<script>
        // Function to add a new row
        function addRow() {
            var table = document.getElementById("manual_period_indicative_value_table");
            var newRow = table.insertRow(table.rows.length);
            var cells = newRow.insertCell(0);
            /* cells.innerHTML = "<select name='dropdown[]'><option value='option1'>Option 1</option><option value='option2'>Option 2</option><option value='option3'>Option 3</option></select>"; */
            cells.innerHTML = "<select name='dropdown[]'>" + 
            "<c:forEach items='${periods}' var='period'>" +
            "<option value='${period}'>${period}</option>" +
            "</c:forEach>" +
            "</select>";
            cells = newRow.insertCell(1);
            cells.innerHTML = "<input type='text' name='textbox[]' onkeypress='return isNumberKey(event)'>";
        }

        // Function to delete a row
        function deleteRow() {
            var table = document.getElementById("manual_period_indicative_value_table");
            var rowCount = table.rows.length;

            if (rowCount > 1) {
                table.deleteRow(rowCount - 1);
            }
        }

        // Function to collect values
        function collectValues() {
            var dropdownValues = [];
            var textboxValues = [];
         // Create your map with values
            const myMap = new Map();

            var table = document.getElementById("manual_period_indicative_value_table");
            var rows = table.getElementsByTagName("tr");

            for (var i = 0; i < rows.length; i++) {
                var cells = rows[i].getElementsByTagName("td");
                dropdownValues.push(cells[0].getElementsByTagName("select")[0].value);
                textboxValues.push(cells[1].getElementsByTagName("input")[0].value);
                
                myMap.set(cells[0].getElementsByTagName("select")[0].value, cells[1].getElementsByTagName("input")[0].value);
                
            }

            console.log("Dropdown values:", dropdownValues);
            console.log("Textbox values:", textboxValues);
            console.log("map values:", myMap);

            // Here you can further process or send the collected values as needed.
        }
        
        // Function to validate input to accept only numbers
        function isNumberKey(evt) {
            var charCode = (evt.which) ? evt.which : event.keyCode;
            if (charCode > 31 && (charCode < 48 || charCode > 57))
                return false;
            return true;
        }
        
        
        function submitManualCasesWithPeriods(gstin,taxpayername,workinglocationid,workingloactionname,reportingdate,caseId,indicativeTaxValue){
			$("#gstinOldCases").val(gstin);
			$("#taxPayerNameOldCases").val(taxpayername);
			$("#jurisdictionOldCases").val(workingloactionname);
			$("#workinglocationid").val(workinglocationid);
			$("#reportingDateOldCases").val(formatDate(reportingdate));
			$("#indicativeTaxValueOldCases").val(indicativeTaxValue);
			$("#oldCaseId").val(caseId);
			$('#uploadOldCasesManuallyModal').modal('show');
		}
        
        $(document).ready(function () {
    	    var counter = 0;
    	
    	    $("#addrow").on("click", function () {
    	        var newRow = $("<tr>");
    	        var cols = "";
    			
    	        cols += '<td><select  name="periodForManualCase[' + counter + ']" >';
    	        <c:forEach items="${periods}" var="period">
    	            cols += '<option value="${period}">${period}</option>';
    	        </c:forEach>
    	        cols += '</select></td>';
    	        
    	        cols += '<td><input type="text" class="form-control"  id="indicativeTaxAmt" name="indicativeTaxAmt[' + counter + ']" placeholder="Indicative Tax Value" title="Please enter indicative tax amount" onkeypress="return isNumberKey(event)" required /></td>';
    	        cols += '<td><input type="button" class="ibtnDel btn btn-md btn-danger "  value="Delete"></td>';
    	        newRow.append(cols);
    	        $("table.order-list").append(newRow);
    	        counter++;
    	    });

    	    
    	
    	    $("table.order-list").on("click", ".ibtnDel", function (event) {
    	        $(this).closest("tr").remove();       
    	        counter -= 1
    	    });
    	
    	
    	});
        
        function formatDate(inputDate) {
            // Parsing the input date string into a JavaScript Date object
            var date = new Date(inputDate);

            // Extracting day, month, and year
            var day = date.getDate();
            var month = date.getMonth() + 1; // Month is zero-based
            var year = date.getFullYear();

            // Adding leading zeros if necessary
            if (day < 10) {
                day = '0' + day;
            }
            if (month < 10) {
                month = '0' + month;
            }

            // Combining into the desired format
            return day + '-' + month + '-' + year;
        }  
        
        
        
        
     // Function to collect values
        function submitVerifierRaiseQueryRemarksForm() {
    	 
        	$('#selectPeriodAndIndicativeTaxValue').css('display', 'none');
            $('#selectIndicativeTaxValue').css('display', 'none');
            $('#selectUniquePeriod').css('display', 'none')
    	 
        	var valuesMap = {};

        	// Select all 'select' elements
        	var selectElements = document.querySelectorAll('select[name^="periodForManualCase"]');
        	selectElements.forEach(function(selectElement) {
        	    // Extract the counter from the select element's name
        	    var counter = selectElement.name.match(/\[(.*?)\]/)[1];
        	    // Add the selected value to the valuesMap with the key as the counter
        	    valuesMap[counter] = {
        	        period: selectElement.value
        	    };
        	});

        	// Select all 'input' elements
        	var inputElements = document.querySelectorAll('input[name^="indicativeTaxAmt"]');
        	inputElements.forEach(function(inputElement) {
        	    // Extract the counter from the input field's name
        	    var counter = inputElement.name.match(/\[(.*?)\]/)[1];
        	    // Add the input value to the valuesMap with the corresponding counter as the key
        	    valuesMap[counter] = {
        	        ...valuesMap[counter], // Retain existing values, if any
        	        indicativeTaxAmt: inputElement.value
        	    };
        	});

        	var valuesMapJSON = JSON.stringify(valuesMap);
        	console.log(valuesMap);
        	
        	
        	
        	if (Object.keys(valuesMap).length === 0) {
        		$('#selectIndicativeTaxValue').css('display', 'none');
        		$('#selectPeriodAndIndicativeTaxValue').css('display', 'block');
        		return;
        	}
        	for (var key in valuesMap) {
        	    if (valuesMap.hasOwnProperty(key)) {
        	        var value = valuesMap[key];
        	        if(value.indicativeTaxAmt  === ""){
        	        	$('#selectPeriodAndIndicativeTaxValue').css('display', 'none');
        	        	$('#selectIndicativeTaxValue').css('display', 'block');
        	        	return;
        	        }
        	        
        	    }
        	}
        	

        	var encounteredPeriods = new Set();

        	for (var key in valuesMap) {
        	    if (valuesMap.hasOwnProperty(key)) {
        	        var value = valuesMap[key];
        	        if (encounteredPeriods.has(value.period)) {
        	            console.log("Duplicate period value found:", value.period);
        	            $('#selectPeriodAndIndicativeTaxValue').css('display', 'none');
        	            $('#selectIndicativeTaxValue').css('display', 'none');
        	            $('#selectUniquePeriod').css('display', 'block');
        	            return;
        	            // If you want to stop checking after finding the first duplicate, you can break out of the loop here
        	            // break;
        	        } else {
        	            encounteredPeriods.add(value.period);
        	        }
        	    }
        	}
        	
        	$("#periodIndicativeJson").val(valuesMapJSON);
    	 
         $.confirm({
					title : 'Confirm!',
					content : 'Do you want to proceed ahead with case submission ?',
					buttons : {
						submit : function() {
            $('#uploadOldCasesManuallyModal').modal('hide'); 
           
           document.getElementById("uploadManualCasesBg").style.display = "block";
           document.getElementById("uploadManualCasesBgTitle").style.display = "block"; 
           
           setTimeout(function() {
        	    document.getElementById("manualOldCasesDetails").submit(); 
   			}, 500);
						},
						close : function() {
							$.alert('Canceled!');
						}
					}
				});
    		}
        
        
        
    </script>
</body>
</html>
