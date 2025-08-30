<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="row">

<div class="col-md-6">
	<div class="form-group">
		<label for="gstin">
		<c:if test="${action == 'linked'}" >
		Linked Cases
		</c:if>
		<c:if test="${action == 'notrequired'}" >
		Action Not Required Case
		</c:if>
		<c:if test="${action == 'initiated'}" >
		Being Initiated case
		</c:if>
		</label> 
	</div>
</div>

</div>

<div class="row">

             <c:if test="${action == 'notrequired'}" >
             <table id="example" class="table table-bordered table-striped">
                  <thead>
                  <tr>
                    <th style="text-align: center; vertical-align: middle;">GSTIN</th>
                    <th style="text-align: center; vertical-align: middle;">Reporting Date<br>(DD-MM-YYYY)</th>
                    <th style="text-align: center; vertical-align: middle;">Period</th>
                    <th style="text-align: center; vertical-align: middle;">Parameter</th>
                    <th style="text-align: center; vertical-align: middle;">Taxpayer Name</th>
                    <th style="text-align: center; vertical-align: middle;">Jurisdiction</th>
                    <th style="text-align: center; vertical-align: middle;">Dispatch No.</th>
                    <th style="text-align: center; vertical-align: middle;">Indicative Value(₹)</th>
                     <th style="text-align: center; vertical-align: middle;">Assigned From</th>
                    <th style="text-align: center; vertical-align: middle;">Assigned To</th>
                    <th style="text-align: center; vertical-align: middle;">Reason</th>
                    <th style="text-align: center; vertical-align: middle;">Remarks</th>
                    <th style="text-align: center; vertical-align: middle;">Recovery Via DRC03(₹)</th>                 
                    <th style="text-align: center; vertical-align: middle;">Supporting File</th>
                  </tr>
                  </thead>
                  <tbody>
                      <c:forEach items="${notrequireCagCase}" var="object">
                      <tr>
                          <td><c:out value="${object.GSTIN_ID}" /></td>
                          <td><fmt:formatDate value="${object.date}" pattern="dd-MM-yyyy" /></td>
                          <td><c:out value="${object.period_ID}" /></td>
                          <td><c:out value="${object.parameter}" /></td>
                          <td><c:out value="${object.taxpayerName}" /></td>
                          <td><c:out value="${object.circle}"/></td>
                          <td><c:out value="${object.extensionNo}" /></td>
                          <td>${object.indicativeTaxVal}</td>
                          <td><c:out value="${object.assignedFrom}" /></td>
                          <td><c:out value="${object.assignedTo}" /></td>
                          <td><c:out value="${object.reason}" /></td>
                          <td><c:out value="${object.remarks}" /></td>
                          <td><fmt:formatNumber value="${object.recoveryByDRC3}" pattern="#,##,##0" /></td>
                          <td style="text-align: center;">
                          <c:if test="${object.uploadedFileName != null}">
                          <a href="/vw/downloadUploadedFile?fileName=${object.uploadedFileName}"><button type="button" onclick="" class="btn btn-primary"><i class="fas fa-download"></i></button></a>
                          </c:if>
                          </td>
                      </tr>
                    </c:forEach>
			  </tbody>
			</table>	
			</c:if>
			
			
			<c:if test="${action == 'linked'}" >
             <table id="example" class="table table-bordered table-striped">
                  <thead>
                  <tr>
                    <th style="text-align: center; vertical-align: middle;">GSTIN</th>
                    <th style="text-align: center; vertical-align: middle;">Taxpayer Name</th>
                    <th style="text-align: center; vertical-align: middle;">Jurisdiction</th>
                    <th style="text-align: center; vertical-align: middle;">Period</th>
                    <th style="text-align: center; vertical-align: middle;">Reporting Date<br>(DD-MM-YYYY)</th>
                    <th style="text-align: center; vertical-align: middle;">Indicative Value(₹)</th>
                    <th style="text-align: center; vertical-align: middle;">Assign From</th>
                    <th style="text-align: center; vertical-align: middle;">Assign To</th>
                    <th style="text-align: center; vertical-align: middle;">Action Status</th>
                    <th style="text-align: center; vertical-align: middle;">Case ID</th>
                    <th style="text-align: center; vertical-align: middle;">Case Stage</th>
                    <th style="text-align: center; vertical-align: middle;">Amount(₹)</th>
                    <th style="text-align: center; vertical-align: middle;">Recovery Stage</th>
                    <th style="text-align: center; vertical-align: middle;">Recovery Via DRC03(₹)</th>
                    <th style="text-align: center; vertical-align: middle;">Recovery Against Demand(₹)</th>
                    <th style="text-align: center; vertical-align: middle;">Module</th>
                  </tr>
                  </thead>
                  <tbody>
                      <c:forEach items="${linkedCagCase}" var="object">
                      <tr>
                          <td><c:out value="${object.GSTIN_ID}" /></td>
                          <td><c:out value="${object.taxpayerName}" /></td>
                          <td><c:out value="${object.circle}"/></td>
                          <td><c:out value="${object.period_ID}" /></td>
                          <td><fmt:formatDate value="${object.date}" pattern="dd-MM-yyyy" /></td>
                          <td><fmt:formatNumber value="${object.indicativeTaxVal}" pattern="#,##,##0" /></td>
                          <td><c:out value="${object.assignedFrom}" /></td>
                          <td><c:out value="${object.assignedTo}" /></td>
                          <td><c:out value="${object.actionStatusName}" /></td>
                          <td><c:out value="${object.caseId}" /></td>
                          <td><c:out value="${object.caseStageName}" /></td>
                          <td><fmt:formatNumber value="${object.demand}" pattern="#,##,##0" /></td>
                          <td><c:out value="${object.recoveryStageName}" /></td>
                          <td><fmt:formatNumber value="${object.recoveryByDRC3}" pattern="#,##,##0" /></td>
                          <td><fmt:formatNumber value="${object.recoveryAgainstDemand}" pattern="#,##,##0" /></td>
                          <td><c:out value="${object.module}" /></td>
                      </tr>
                    </c:forEach>
			  </tbody>
			</table>	
			</c:if>
			
			
			<c:if test="${action == 'initiated'}" >
             <table id="example" class="table table-bordered table-striped">
                  <thead>
                  <tr>
                    <th style="text-align: center; vertical-align: middle;">GSTIN</th>
                    <th style="text-align: center; vertical-align: middle;">Taxpayer Name</th>
                    <th style="text-align: center; vertical-align: middle;">Jurisdiction</th>
                    <th style="text-align: center; vertical-align: middle;">Period</th>
                    <th style="text-align: center; vertical-align: middle;">Reporting Date<br>(DD-MM-YYYY)</th>
                    <th style="text-align: center; vertical-align: middle;">Indicative Value(₹)</th>
                    <th style="text-align: center; vertical-align: middle;">Assign From</th>
                    <th style="text-align: center; vertical-align: middle;">Assign To</th>
                    <th style="text-align: center; vertical-align: middle;">Action Status</th>
                    <th style="text-align: center; vertical-align: middle;">Case ID</th>
                    <th style="text-align: center; vertical-align: middle;">Case Stage</th>
                    <th style="text-align: center; vertical-align: middle;">Amount(₹)</th>
                    <th style="text-align: center; vertical-align: middle;">Recovery Stage</th>
                    <th style="text-align: center; vertical-align: middle;">Recovery Via DRC03(₹)</th>
                    <th style="text-align: center; vertical-align: middle;">Recovery Against Demand(₹)</th>
                    <th style="text-align: center; vertical-align: middle;">Module</th>
                  </tr>
                  </thead>
                  <tbody>
                      <c:forEach items="${initiatedCagCase}" var="object">
                      <tr>
                          <td><c:out value="${object.GSTIN_ID}" /></td>
                          <td><c:out value="${object.taxpayerName}" /></td>
                          <td><c:out value="${object.circle}"/></td>
                          <td><c:out value="${object.period_ID}" /></td>
                          <td><fmt:formatDate value="${object.date}" pattern="dd-MM-yyyy" /></td>
                          <td><fmt:formatNumber value="${object.indicativeTaxVal}" pattern="#,##,##0" /></td>
                          <td><c:out value="${object.assignedFrom}" /></td>
                          <td><c:out value="${object.assignedTo}" /></td>
                          <td><c:out value="${object.actionStatusName}" /></td>
                          <td><c:out value="${object.caseId}" /></td>
                          <td><c:out value="${object.caseStageName}" /></td>
                          <td><fmt:formatNumber value="${object.demand}" pattern="#,##,##0" /></td>
                          <td><c:out value="${object.recoveryStageName}" /></td>
                          <td><fmt:formatNumber value="${object.recoveryByDRC3}" pattern="#,##,##0" /></td>
                          <td><fmt:formatNumber value="${object.recoveryAgainstDemand}" pattern="#,##,##0" /></td>
                          <td><c:out value="${object.module}" /></td>
                      </tr>
                    </c:forEach>
			  </tbody>
			</table>	
			</c:if>					
				
						
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



