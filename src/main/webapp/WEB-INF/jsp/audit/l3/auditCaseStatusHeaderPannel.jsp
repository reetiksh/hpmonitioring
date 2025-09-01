
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.Date" %>
<div class="card-body" style="background-color: #bedd9d; border-radius: 3px; margin-bottom: 10px;  box-shadow: 2px 2px 4px 1px #abbb97;">
  <div class="row">
    <div class="col-md-2">
      <label>Case Id:</label>
      <span>${auditCaseDetails.caseId}</span>
    </div>
    <div class="col-md-2">
      <label>GSTIN:</label>
      <span>${auditCaseDetails.GSTIN}</span>
    </div>
    <div class="col-md-2">
      <label>Period:</label>
      <span>${auditCaseDetails.period}</span>
    </div>
    <div class="col-md-3">
      <label>Case Reporting Date:</label>
      <span><fmt:formatDate value="${auditCaseDetails.caseReportingDate}" pattern="dd-MM-yyyy" /></span>
    </div>
    <div class="col-md-3">
      <label>Indicative Tax Value ( <i class="fas fa-rupee-sign"></i> ):</label>
      <span><fmt:formatNumber value="${auditCaseDetails.indicativeTaxValue}" pattern="#,##,##0" /></span>
    </div>
    <div class="col-md-2">
      <label>Taxpayer Name:</label>
      <span>${auditCaseDetails.taxpayerName}</span>
    </div>
    <div class="col-md-2">
      <label>Category:</label>
      <span>${auditCaseDetails.category.name}</span>
    </div>
    <div class="col-md-6">
      <label>Parameter(s):</label>
      <c:forEach items="${parameterList}" var="parameter">
        <span>${parameter.paramName}, </span>
      </c:forEach>
    </div>
  </div>
</div>