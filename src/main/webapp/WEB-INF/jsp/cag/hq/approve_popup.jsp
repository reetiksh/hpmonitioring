<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
<script src="/static/dist/js/jquery-confirm.min.js"></script>
<style>
  .btn-outline-custom {
    color: #495057;
    border: 1px solid #ced4da;
    text-align:left;
}
</style>

<script>
		document.addEventListener('contextmenu', function(e) {
			e.preventDefault();
		});
		document.addEventListener('keydown', function(e) {
			if (e.ctrlKey && e.key === 'u') {
				e.preventDefault();
			}
		});
		document.addEventListener('keydown', function(e) {
		    if (e.key === 'F12') {
		        e.preventDefault();
		    }
		});
		 // Disable back and forward cache
		$(document).ready(function () {
		    function disableBack() {window.history.forward()}
		
		    window.onload = disableBack();
		    window.onpageshow = function (evt) {if (evt.persisted) disableBack()}
		});
		// Disable refresh
		document.onkeydown = function (e) {
		    if (e.key === 'F5' || (e.ctrlKey && e.key === 'r') || e.keyCode === 116) {
		        e.preventDefault();
		        
		    }
		};	
		
		
</script>


<div class="modal fade" id="confirmationApproveModal" tabindex="-1" role="dialog" aria-labelledby="confirmationTransferModalTitle" aria-hidden="true">
		  <div class="modal-dialog modal-dialog-centered" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="confirmationTransferModalTitle">Approve Case</h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      
		      <form method="POST" name="approveForm" id="approveForm" action="approve_transfer_cases"  >
		      
		         <input type="hidden" id="app_gstno" name="gstno" >
                 <input type="hidden" id="app_date" name="date" >
                 <input type="hidden" id="app_period" name="period" >
                 <input type="hidden" id="app_parameter" name="parameter" >
                 <input type="hidden" id="app_jurisdiction" name="caseAssignedTo" >
                 <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        
		         <div class="modal-footer">
		         <button type="submit" class="btn btn-primary" id="okBtn" >Yes</button>
		         <button type="button" class="btn btn-secondary" data-dismiss="modal" id="cancelBtn" >No</button>
		         </div>
		         
		      </form>
		         
    </div>
  </div>
</div>

