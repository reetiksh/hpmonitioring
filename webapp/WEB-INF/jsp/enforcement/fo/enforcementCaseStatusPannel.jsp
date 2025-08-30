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

<c:forEach items="${enforcementCaseStatusList}" var="status">
  <c:if test="${status.usedIn eq 'update'}">
    <c:if test="${status.id eq (activeActionPannelId)}">
      <a href="javascript:void(0);" onclick="showActionPannelView('${auditCaseDetails.caseId}', '${status.id}')">
        <div class="list-group-item list-group-item-info" style="border-radius: 0px 150px 150px 0px; margin-bottom: 5px;">${status.status}</div>
      </a>
    </c:if>
    <c:if test="${status.id ne (activeActionPannelId)}">
      <c:if test="${status.id lt activeActionPannelId}">
        <a href="javascript:void(0);" onclick="showActionPannelView('${auditCaseDetails.caseId}', '${status.id}')">
          <div class="list-group-item list-group-item-success" role="tab" style="border-radius: 0px 150px 150px 0px; margin-bottom: 5px;">${status.status}</div>
        </a>
      </c:if>
      <c:if test="${status.id gt activeActionPannelId}">
        <div class="list-group-item list-group-item-danger" role="tab" style="border-radius: 0px 150px 150px 0px; margin-bottom: 5px;">${status.status}</div>
      </c:if>
    </c:if>
  </c:if>
</c:forEach>