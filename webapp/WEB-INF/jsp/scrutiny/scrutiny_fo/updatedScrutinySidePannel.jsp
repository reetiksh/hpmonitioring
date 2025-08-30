<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


  <c:forEach items="${ScrutinyInitiatedSidePanelList}" var="status">
    <%-- <c:if test="${status.id eq (auditCaseDetails.action.id + 1)}"> --%>
      <!-- <a class="list-group-item list-group-item-action" id="list-profile-list" data-toggle="list" href="#list-profile" role="tab">${status.status}</a> -->
      <c:choose>
    <c:when test="${status.id eq '6'}">
        <button type="button" value="${status.id}" class="list-group-item list-group-item-success" style="border-radius: 0px 150px 150px 0px; margin-bottom: 5px; width: 250px;" onclick="displayDynamicSidePannelView('${status.id}')">
            ${status.name} 
        </button>
    </c:when>
    <c:otherwise>
        <button type="button" value="${status.id}" class="list-group-item list-group-item-info" style="border-radius: 0px 150px 150px 0px; margin-bottom: 5px; width: 250px;text-align: center;" onclick="displayDynamicSidePannelView('${status.id}')">
            ${status.name}
        </button>
    </c:otherwise>
</c:choose>

    
    
    <%-- <c:if test="${status.id gt (auditCaseDetails.action.id + 1)}"> --%>
      <%-- <div class="list-group-item list-group-item-danger" id="list-profile-list" role="tab" style="border-radius: 0px 150px 150px 0px; margin-bottom: 5px;">${status.status}</div> --%>
    <%-- </c:if> --%>
  </c:forEach>