<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


  <c:forEach items="${ScrutinyInitiatedSidePanelList}" var="status">
    <%-- <c:if test="${status.id eq (auditCaseDetails.action.id + 1)}"> --%>
      <!-- <a class="list-group-item list-group-item-action" id="list-profile-list" data-toggle="list" href="#list-profile" role="tab">${status.status}</a> -->
      <c:choose>
    <c:when test="${status.id eq '1'}">
        <button type="button" value="${status.id}" class="list-group-item list-group-item-info" style="border-radius: 0px 150px 150px 0px; margin-bottom: 5px; width: 250px; text-align: center;">
            ${status.name} 
        </button>
    </c:when>
    <c:otherwise>
        <button type="button" value="${status.id}" class="list-group-item list-group-item-danger" style="border-radius: 0px 150px 150px 0px; margin-bottom: 5px; width: 250px;">
            ${status.name}
        </button>
    </c:otherwise>
</c:choose>

    
    
    <%-- <c:if test="${status.id gt (auditCaseDetails.action.id + 1)}"> --%>
      <%-- <div class="list-group-item list-group-item-danger" id="list-profile-list" role="tab" style="border-radius: 0px 150px 150px 0px; margin-bottom: 5px;">${status.status}</div> --%>
    <%-- </c:if> --%>
  </c:forEach><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<style>
  /* pill look, right-side rounded */
  .step-pill {
    border-radius: 0 999px 999px 0;
    margin-bottom: .5rem;
    width: 250px;
    text-align: left;
  }
</style>

<c:set var="currentStep" value="${auditCaseDetails.action.id + 1}" />

<div class="list-group">
  <c:forEach items="${ScrutinyInitiatedSidePanelList}" var="status">
    <!-- coerce to numbers for safe comparisons -->
    <c:set var="statusIdNum" value="${status.id + 0}" />
    <c:set var="isCurrent" value="${statusIdNum == currentStep}" />
    <c:set var="isDone"    value="${statusIdNum <  currentStep}" />
    <c:set var="isFuture"  value="${statusIdNum >  currentStep}" />

    <button
      type="button"
      class="list-group-item list-group-item-action step-pill
             ${isCurrent ? 'active' : (isDone ? 'list-group-item-success' : 'list-group-item-secondary')}"
      data-step="${status.id}"
      ${isFuture ? 'disabled="disabled"' : ''}>

      <span>${status.name}</span>

      <c:if test="${isDone}">
        <i class="fas fa-check float-end mt-1"></i>
      </c:if>
    </button>
  </c:forEach>
</div>
