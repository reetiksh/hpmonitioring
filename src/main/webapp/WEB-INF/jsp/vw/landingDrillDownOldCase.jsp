<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="row">
  <div class="col-12">
    <div class="table-responsive">
      <table id="oldCasesTable" class="table table-bordered table-striped w-100">
        <thead>
          <tr>
            <th class="text-center align-middle">GSTIN</th>
            <th class="text-center align-middle">Reporting Date (DD-MM-YYYY)</th>
            <th class="text-center align-middle">Jurisdiction</th>
            <th class="text-center align-middle">Taxpayer Name</th>
            <th class="text-center align-middle">Indicative Value (₹)</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${oldCaseList}" var="object">
            <tr>
              <!-- object[3] = GSTIN -->
              <td class="text-center align-middle"><c:out value="${object[3]}"/></td>

              <!-- object[2] = date -->
              <td class="text-center align-middle">
                <fmt:formatDate value="${object[2]}" pattern="dd-MM-yyyy"/>
              </td>

              <!-- object[0] = jurisdiction -->
              <td class="text-center align-middle"><c:out value="${object[0]}"/></td>

              <!-- object[6] = taxpayer name -->
              <td class="text-center align-middle"><c:out value="${object[6]}"/></td>

              <!-- object[4] = indicative value -->
              <td class="text-center align-middle">
                <fmt:formatNumber value="${object[4]}" pattern="#,##,##0"/>
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </div>
</div>

<script>
  (function initOldCasesTable(){
    var $tbl = $('#oldCasesTable');

    // If this fragment is injected via AJAX, prevent double-initialization
    if ($.fn.dataTable.isDataTable($tbl)) {
      $tbl.DataTable().destroy();
    }

    $tbl.DataTable({
      dom: 'Bfrtip',
      buttons: ['excel', 'pdf', 'print'],
      responsive: false,
      autoWidth: true,
      scrollX: true
    });
  })();
</script>
