
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
	function showHideOtherRemark(){
		var selectedValue = $('input[name="remarkOptions"]:checked').val();
		$("#remarksId").val('');
		
		if(selectedValue === 'other'){
			$("#remarksId").show();
		}else{
			$("#remarksId").hide();
		}
	}
	
</script>

<div class="modal fade" id="transferModal" tabindex="-1" role="dialog" aria-labelledby="transferModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-body">
        <form>
          <div class="form-group">
            <label for="recipient-name" class="col-form-label">Action: <span style="color: red;"></span></label>
             <input type="text" name="action" value="Recommend" readonly >
          </div>
          <div class="form-group" style="margin-bottom: 0rem;">
            <label for="message-text" class="col-form-label">Remarks<span style="color: red;"> *</span>
            </label>
          </div>
          <div class="btn-group btn-group-vertical" role="group" data-toggle="buttons" style="width: 100%;">
			  <label class="btn btn-outline-custom" style="font-weight: 100;">
			    <textarea class="form-control" id="remarksId" name="remarksId" placeholder="Remarks"></textarea>
			  </label>
			</div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="transferCaseBtn" >Submit</button>
      </div>
    </div>
  </div>
</div>

