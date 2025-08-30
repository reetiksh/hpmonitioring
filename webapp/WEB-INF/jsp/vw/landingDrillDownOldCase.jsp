<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="row">

<div class="col-12" >

<table id="example" class="table table-bordered table-striped">
                  <thead>
                  <tr>
                    <th style="text-align: center; vertical-align: middle;">GSTIN</th>
                    <th style="text-align: center; vertical-align: middle;">Reporting Date<br>(DD-MM-YYYY)</th>
                    <th style="text-align: center; vertical-align: middle;">Jurisdiction</th>
                    <th style="text-align: center; vertical-align: middle;">Taxpayer Name</th>
                    <th style="text-align: center; vertical-align: middle;">Indicative Value(₹)</th>
                  </tr>
                  </thead>
                  <tbody>
                      <c:forEach items="${oldCaseList}" var="object">
                      <tr>
                          <td>${object[3]}</td>
                          <td><fmt:formatDate value="${object[2]}" pattern="dd-MM-yyyy" /></td>
                          <td>${object[0]}</td>
                          <td>${object[6]}</td>
                          <td><fmt:formatNumber value="${object[4]}" pattern="#,##,##0" /></td>
                      </tr>
                    </c:forEach>
                  </tbody>
                </table>
                
</div>                						
						
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



