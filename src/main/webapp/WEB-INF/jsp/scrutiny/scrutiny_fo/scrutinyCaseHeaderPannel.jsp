<%@ page contentType="text/html;charset=UTF-8" language="java"%> <!-- for ₹ symbol -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.Date" %>
<div class="card-body" style="background-color: #bedd9d; border-radius: 3px; margin-bottom: 10px;  box-shadow: 2px 2px 4px 1px #abbb97;">
  <div class="row">
    <div class="col-md-2">
      <label>GSTIN:</label>
      <span>${mstScrutinyCases.id.GSTIN}</span>
    </div>
    <div class="col-md-2">
      <label>Period:</label>
      <span>${mstScrutinyCases.id.period}</span>
    </div>
    <div class="col-md-3">
      <label>Case Reporting Date:</label>
      <span><fmt:formatDate value="${mstScrutinyCases.id.caseReportingDate}" pattern="dd-MM-yyyy" /></span>
    </div>
    <div class="col-md-3">
      <label>Indicative Tax Value(₹):</label>
      <span><fmt:formatNumber value="${mstScrutinyCases.indicativeTaxValue}" pattern="#,##,##0" /></span>
    </div>
    <div class="col-md-2">
      <label>Taxpayer Name:</label>
      <span>${mstScrutinyCases.taxpayerName}</span>
    </div>
    <div class="col-md-2">
      <label>Category:</label>
      <span>${mstScrutinyCases.category.name}</span>
    </div>
     <div class="col-md-6">
      <label>Parameter(s):</label>
      <%-- <c:forEach items="${parameterList}" var="parameter"> --%>
        <span><td>${mstScrutinyCases.allConcatParametersValue}</td> </span>
      <%-- </c:forEach> --%>
    </div> 
  </div>
</div>