<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style>
  .button {
    background-color: #04AA6D; /* Green */
    border: none;
    color: white;
    padding: 16px 0px 16px 0px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    margin: 4px 2px;
    transition-duration: 0.4s;
    cursor: pointer;
    width: 100%;
    border-radius: 20px 50px;
  }

  .button1 {
    background-color: #b7e3cf8a; 
    color: black; 
    border: 2px solid #04AA6D;
    box-shadow: 5px 5px 2px lightblue;
  }

  .button1:hover {
    background-color: #04AA6D;
    color: white;
    font-size: 18px;
    font-weight: bold;
    padding: 14px 0px 14px 0px;
  }

  .button2 {
    background-color: white; 
    color: black; 
    border: 2px solid #008CBA;
    box-shadow: 5px 5px 2px lightblue;
  }

  .button2:hover {
    background-color: #008CBA;
    color: white;
    font-size: 18px;
    font-weight: bold;
    padding: 14px 0px 14px 0px;
  }

  .button4 {
    background-color: rgb(206, 206, 206);
    color: black;
    border: 2px solid #f8c7c7;
    font-size: 18px;
    padding: 14px 0px 14px 0px;
    box-shadow: 5px 5px 2px rgb(230, 173, 173);
  }

  .button4:hover {
    background-color: #f7c2c2;
    font-size: 18px;
    font-weight: bold;
    padding: 14px 0px 14px 0px;
    }
    
</style>
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

<div class="row">
  <c:forEach items="${auditCaseStatusList}" var="status">
      <c:if test="${status.id eq (activeActionPannelId)}">
        <c:if test="${auditCaseDetails.assignTo eq 'L3'}">
          <div class="col-lg-2">
            <a href="javascript:void(0);" onclick="showActionPannelView('${auditCaseDetails.caseId}', '${status.id}')">
              <div class="button button2">${status.category}</div>
            </a>
          </div>
        </c:if>
        <c:if test="${auditCaseDetails.assignTo ne 'L3'}">
          <div class="col-lg-2">
            <a href="javascript:void(0);" onclick="showActionPannelView('${auditCaseDetails.caseId}', '${status.id}')">
              <div class="button button1">${status.category}</div>
            </a>
          </div>
        </c:if>
      </c:if>
      <c:if test="${status.id ne (activeActionPannelId)}">
        <c:if test="${status.sequence eq (auditCaseDetails.action.sequence + 1)}">
          <div class="col-lg-2">
            <a href="javascript:void(0);" onclick="showActionPannelView('${auditCaseDetails.caseId}', '${status.id}')">
              <div class="button button1">${status.category}</div>
            </a>
          </div>
        </c:if>
        <c:if test="${status.sequence lt (auditCaseDetails.action.sequence + 1)}">
          <div class="col-lg-2">
            <a href="javascript:void(0);" onclick="showActionPannelView('${auditCaseDetails.caseId}', '${status.id}')">
              <div class="button button1">${status.category}</div>
            </a>
          </div>
        </c:if>
        <c:if test="${status.sequence gt (auditCaseDetails.action.sequence + 1)}">
          <c:if test="${status.activationOrder lt (auditCaseDetails.action.sequence + 1)}">
            <div class="col-lg-2">
              <a href="javascript:void(0);" onclick="showActionPannelView('${auditCaseDetails.caseId}', '${status.id}')">
                <div class="button button2">${status.category}</div>
              </a>
            </div>
          </c:if>
          <c:if test="${status.activationOrder eq (auditCaseDetails.action.sequence + 1)}">
            <div class="col-lg-2">
              <a href="javascript:void(0);" onclick="showActionPannelView('${auditCaseDetails.caseId}', '${status.id}')">
                <div class="button button2">${status.category}</div>
              </a>
            </div>
          </c:if>
          <c:if test="${status.activationOrder gt (auditCaseDetails.action.sequence + 1)}">
            <div class="col-lg-2">
              <div class="button button4">${status.category}</div>
            </div>
          </c:if>
        </c:if>
      </c:if>
  </c:forEach>
</div>
<br>