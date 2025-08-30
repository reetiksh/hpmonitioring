<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
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

  $('.selectpicker').selectpicker();
</script>
<div class="col-md-8" style="margin-bottom: 10px;">
  <label>Status<span style="color: red;"> *</span></label>
  <select name="activeActionPannelId" id="activeActionPannelId" class="selectpicker col-md-6" data-live-search="true" title="Please Select Status">
    <c:forEach items="${auditCaseStatusList}" var="status">
      <c:choose>
        <c:when test="${status.id eq paraCategory}">
          <option value="${status.id}" selected="selected">${status.category}</option>
        </c:when>
        <c:otherwise>
          <option value="${status.id}">${status.category}</option>
        </c:otherwise>
      </c:choose>
    </c:forEach>
  </select>
</div>