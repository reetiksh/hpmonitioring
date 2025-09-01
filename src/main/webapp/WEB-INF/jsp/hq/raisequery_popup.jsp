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

<script>

$('#recoveryForm').on('submit', function(oEvent) {

    debugger;
	
	oEvent.preventDefault();
	
	$.confirm({
		title : 'Confirm!',
		content : 'Do you want to proceed ahead with Raise Query?',
		buttons : {
			submit : function() {
			    
			//oEvent.currentTarget.submit();
			          
			},
			close : function() {
				$.alert('Canceled!');
			}
		}
	});
});


</script>


<div class="modal fade" id="raiseQueryModal" tabindex="-1" role="dialog" aria-labelledby="raiseQueryModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="raiseQueryModalTitle">Raise Query</h5>
        <button type="button" class="close"  data-dismiss="modal" aria-label="Close">    
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      
      <form method="POST" name="recoveryForm" id="recoveryForm" action="raise_query_recovery_cases"  >
      <div class="modal-body">
       
          <input type="hidden" id="gstno" name="gstno" >
          <input type="hidden" id="date" name="date" >
          <input type="hidden" id="period" name="period" >
          
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
          
          <div class="form-group" style="margin-bottom: 0rem;">
            <label for="message-text" class="col-form-label">Remarks<span style="color: red;"> *</span></label>
          </div>
          <textarea class="form-control" id="remarksId" name="otherRemarks" placeholder="Remarks" required></textarea>
            
      </div>
      
      <div class="modal-footer">
         <button type="submit" class="btn btn-danger" id="raiseQueryCaseBtn" >Raise Query</button>
      </div>
      </form>
      
    </div>
  </div>
</div>
