<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="row">

<table id="example" class="table table-bordered table-striped">
                  <thead>
                  <tr>
                    <th style="text-align: center; vertical-align: middle;">GSTIN</th>
                    <th style="text-align: center; vertical-align: middle;">Taxpayer Name</th>
                    <th style="text-align: center; vertical-align: middle;">Jurisdiction</th>
                    <th style="text-align: center; vertical-align: middle;">Period</th>
                    <th style="text-align: center; vertical-align: middle;">Dispatch No.</th>
                    <th style="text-align: center; vertical-align: middle;">Reporting Date<br>(DD-MM-YYYY)</th>
                    <th style="text-align: center; vertical-align: middle;">Indicative Value(₹)</th>
                    <th style="text-align: center; vertical-align: middle;">Currently With</th>
                    <th style="text-align: center; vertical-align: middle;">Action Status</th>
                    <th style="text-align: center; vertical-align: middle;">Case ID</th>
                    <th style="text-align: center; vertical-align: middle;">Case Stage</th>
                    <th style="text-align: center; vertical-align: middle;">Case Stage ARN</th>
                    <th style="text-align: center; vertical-align: middle;">Amount(₹)</th>
                    <th style="text-align: center; vertical-align: middle;">Recovery Stage</th>
					<th style="text-align: center; vertical-align: middle;">Recovery Stage ARN</th>
                    <th style="text-align: center; vertical-align: middle;">Recovery Via DRC03(₹)</th>
                    <th style="text-align: center; vertical-align: middle;">Recovery Against Demand(₹)</th>
                    <c:if test="${category eq 'Self Detected Cases'}">
                    <th style="text-align: center; vertical-align: middle;">Reason</th>
                    </c:if>                    
                    <th style="text-align: center; vertical-align: middle;">Supporting File</th>
                  </tr>
                  </thead>
                  <tbody>
                      <c:forEach items="${caseList}" var="object">
                      <tr>
                          <td><c:out value="${object.id.GSTIN}" /></td>
                          <td><c:out value="${object.taxpayerName}" /></td>
                          <td><c:out value="${object.locationDetails.locationName}"/></td>
                          <td><c:out value="${object.id.period}" /></td>
                          <td><c:out value="${object.extensionNo}" /></td>
                          <td><fmt:formatDate value="${object.id.caseReportingDate}" pattern="dd-MM-yyyy" /></td>
                          <td><fmt:formatNumber value="${object.indicativeTaxValue}" pattern="#,##,##0" /></td>
                          <td><c:out value="${object.assignedTo}" /></td>
                          <td><c:out value="${object.actionStatus.name}" /></td>
                          <td><c:out value="${object.caseId}" /></td>
                          <td><c:out value="${object.caseStage.name}" /></td>
                          <td><c:out value="${object.caseStageArn}" /></td>
                          <td><fmt:formatNumber value="${object.demand}" pattern="#,##,##0" /></td>
                          <td><c:out value="${object.recoveryStage.name}" /></td>
                          <td><c:out value="${object.recoveryStageArn}" /></td>
                          <td><fmt:formatNumber value="${object.recoveryByDRC3}" pattern="#,##,##0" /></td>
                          <td><fmt:formatNumber value="${object.recoveryAgainstDemand}" pattern="#,##,##0" /></td>
                          <c:if test="${category eq 'Self Detected Cases'}">
                          <td><c:out value="${object.findingCategory}" /></td>
                          </c:if>
                          <td style="text-align: center;">
                          <c:if test="${object.fileName != null}">
                          <a href="/vw/downloadUploadedFile?fileName=${object.fileName}"><button type="button" onclick="" class="btn btn-primary"><i class="fas fa-download"></i></button></a>
                          </c:if>
                          </td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>						
						
</div>

<script>
        $(document).ready(function() {

            $('#example').DataTable({
                dom: 'Bfrtip',
                buttons: [
                    'excel', 'pdf', 'print'
                ],
                initComplete: function(settings, json) {
                    console.log('DataTable initialized');
                }
            });
            
        });
</script>



