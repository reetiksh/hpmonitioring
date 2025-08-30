<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script>
  function showActionPannelView(caseId, activeActionPannelId){
    // console.log("Case Id: " + caseId + " | Active Pannel : " + activeActionPannelId);

    var link = '/l3/update_audit_case_details/activePannel?caseId=' + caseId + '&activeActionPannelId=' + activeActionPannelId;

    $.ajax({url: '/checkLoginStatus',
      method: 'get',
      async: false,
      success: function(result){
        const myJSON = JSON.parse(result);
        if(result=='true'){
          $("#actionPannelDiv").load(link, function(response, status, xhr){
            if(status == 'success'){
              console.log("success");	
              // $("#showAssignmentModal").modal('show');
            }else{
              console.log("failed");
            }
          });
        } else if(result=='false'){
          window.location.reload();
        }
      }         
    });
  }
</script>

<c:forEach items="${auditCaseStatusList}" var="status">
  <c:if test="${status.id eq (activeActionPannelId)}">
    <c:if test="${auditCaseDetails.assignTo eq 'L3'}">
      <a href="javascript:void(0);" onclick="showActionPannelView('${auditCaseDetails.caseId}', '${status.id}')">
        <div class="list-group-item list-group-item-info" style="border-radius: 0px 150px 150px 0px; margin-bottom: 5px;">${status.category}</div>
      </a>
    </c:if>
    <c:if test="${auditCaseDetails.assignTo ne 'L3'}">
      <a href="javascript:void(0);" onclick="showActionPannelView('${auditCaseDetails.caseId}', '${status.id}')">
        <div class="list-group-item list-group-item-success" role="tab" style="border-radius: 0px 150px 150px 0px; margin-bottom: 5px;">${status.category}</div>
      </a>
    </c:if>
  </c:if>
  <c:if test="${status.id ne (activeActionPannelId)}">
    <c:if test="${status.sequence eq (auditCaseDetails.action.sequence + 1)}">
      <a href="javascript:void(0);" onclick="showActionPannelView('${auditCaseDetails.caseId}', '${status.id}')">
        <div class="list-group-item list-group-item-success" role="tab" style="border-radius: 0px 150px 150px 0px; margin-bottom: 5px;">${status.category}</div>
      </a>
    </c:if>
    <c:if test="${status.sequence lt (auditCaseDetails.action.sequence + 1)}">
      <a href="javascript:void(0);" onclick="showActionPannelView('${auditCaseDetails.caseId}', '${status.id}')">
        <div class="list-group-item list-group-item-success" role="tab" style="border-radius: 0px 150px 150px 0px; margin-bottom: 5px;">${status.category}</div>
      </a>
    </c:if>
    <c:if test="${status.sequence gt (auditCaseDetails.action.sequence + 1)}">
      <c:if test="${status.activationOrder lt (auditCaseDetails.action.sequence + 1)}">
        <a href="javascript:void(0);" onclick="showActionPannelView('${auditCaseDetails.caseId}', '${status.id}')">
          <div class="list-group-item list-group-item-info" role="tab" style="border-radius: 0px 150px 150px 0px; margin-bottom: 5px;">${status.category}</div>
        </a>
      </c:if>
      <c:if test="${status.activationOrder eq (auditCaseDetails.action.sequence + 1)}">
        <a href="javascript:void(0);" onclick="showActionPannelView('${auditCaseDetails.caseId}', '${status.id}')">
          <div class="list-group-item list-group-item-info" role="tab" style="border-radius: 0px 150px 150px 0px; margin-bottom: 5px;">${status.category}</div>
        </a>
      </c:if>
      <c:if test="${status.activationOrder gt (auditCaseDetails.action.sequence + 1)}">
        <div class="list-group-item list-group-item-danger" role="tab" style="border-radius: 0px 150px 150px 0px; margin-bottom: 5px;">${status.category}</div>
      </c:if>
    </c:if>
  </c:if>
</c:forEach>