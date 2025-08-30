
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.Date" %>
<script>
  $(document).ready(function () {
    const inspectionRequired = '${inspectionRequired}';
    const checkbox = document.getElementById("inspectionRequired");
    if (inspectionRequired === 'true') {
      checkbox.checked = true;
    } else {
      checkbox.checked = false;
    }

    const checkboxLock = '${checkboxLock}';
    if (checkboxLock === 'lock') {
        checkbox.disabled = true; // Prevent user modification
    }

  $('#inspectionRequired').on('change', function () {
    const value = $(this).is(':checked') ? 'true' : 'false';
    console.log("value:", value);

    const hidden_form1 = document.createElement('form'); 
		hidden_form1.method = 'post';
		hidden_form1.action = "/enforcement_fo/update_enforcement_case"; 
			
		const hidden_input1 = document.createElement('input'); 
		hidden_input1.type = 'hidden'; 
		hidden_input1.name = 'GSTIN';  
		hidden_input1.value = '${enforcementCaseDetails.id.GSTIN}'; 

		const hidden_input2 = document.createElement('input'); 
		hidden_input2.type = 'hidden'; 
		hidden_input2.name = 'period'; 
		hidden_input2.value = '${enforcementCaseDetails.id.period}'; 

		const hidden_input3 = document.createElement('input'); 
		hidden_input3.type = 'hidden'; 
		hidden_input3.name = 'caseReportingDate'; 
		hidden_input3.value = document.getElementById("fomattedCaseReportingDate").value;

		const hidden_input4 = document.createElement('input'); 
		hidden_input4.type = 'hidden'; 
		hidden_input4.name = 'inspectionRequired'; 
		hidden_input4.value = value; 

		const hidden_input5 = document.createElement('input'); 
		hidden_input5.type = 'hidden'; 
		hidden_input5.name = '${_csrf.parameterName}'; 
		hidden_input5.value = '${_csrf.token}'; 

		hidden_form1.appendChild(hidden_input1); 
		hidden_form1.appendChild(hidden_input2);
		hidden_form1.appendChild(hidden_input3); 
		hidden_form1.appendChild(hidden_input4);
		hidden_form1.appendChild(hidden_input5); 
		document.body.appendChild(hidden_form1); 
    hidden_form1.submit();
  });
});
</script>
<div class="card-body" style="background-color: #bedd9d; border-radius: 3px; margin-bottom: 10px;  box-shadow: 2px 2px 4px 1px #abbb97;">
  <div class="row">
   
    <hr>
     <div class="form-check form-switch col-md-12">
      <label class="form-check-label col-md-2" for="inspectionRequired" style="font-weight: bold;">
        Inspection is required?
      </label>
      <input class="form-check-input" type="checkbox" id="inspectionRequired">
    </div>
    <div class="col-md-2">
      <label>GSTIN:</label>
      <span>${enforcementCaseDetails.id.GSTIN}</span>
    </div>
    <div class="col-md-2">
      <label>Period:</label>
      <span>${enforcementCaseDetails.id.period}</span>
    </div>
    <div class="col-md-3">
      <label>Case Reporting Date:</label>
      <span><fmt:formatDate value="${enforcementCaseDetails.id.caseReportingDate}" pattern="dd-MM-yyyy" /></span>
      <c:set var="isoDate"><fmt:formatDate value='${enforcementCaseDetails.id.caseReportingDate}' pattern='yyyy-MM-dd'/></c:set>
      <input type="hidden" class="form-control" id="fomattedCaseReportingDate" name="fomattedCaseReportingDate" value="${isoDate}"/>
    </div>
    <div class="col-md-3">
      <label>Indicative Tax Value ( <i class="fas fa-rupee-sign"></i> ):</label>
      <span><fmt:formatNumber value="${enforcementCaseDetails.indicativeTaxValue}" pattern="#,##,##0" /></span>
    </div>
    <div class="col-md-2">
      <label>Taxpayer Name:</label>
      <span>${enforcementCaseDetails.taxpayerName}</span>
    </div>
    <div class="col-md-2">
      <label>Category:</label>
      <span>${enforcementCaseDetails.category.name}</span>
    </div>
    <div class="col-md-6">
      <label>Parameter(s):</label>
      <span>${enforcementCaseDetails.parametersNameList}</span>
    </div>
  </div>
</div>