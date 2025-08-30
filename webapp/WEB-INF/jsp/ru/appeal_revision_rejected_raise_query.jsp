<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
.btn-outline-custom {
  color: #495057;
  border: 1px solid #ced4da;
  text-align:left;
}

.popuprq {
  display: none;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  padding: 20px;
  background-color: #fff;
  border: 1px solid #ccc;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 999;
}
.overlayrq {
  display: none;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 998;
}
</style>
<head>
  <!-- <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script> -->
  <script src="/static/dist/js/jquery-3.6.4.min.js"></script>
</head>
<div class="modal fade" id="raisequeryModal" tabindex="-1" role="dialog" aria-labelledby="raisequeryModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
    
       <div class="modal-header">
        <h5 class="modal-title" id="raiseQueryModalTitle"><b>Raise Query</b></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">    
          <span aria-hidden="true">&times;</span>
        </button>
       </div>
	    <div class="popuprq" id="myPopuprq">
        <p>Are You Sure To Raise Query For The Following Case ?</p>
        <button onclick="onOkrq()">OK</button>
        <button onclick="onCancelrq()">Cancel</button>
	    </div>
      <div class="modal-body">
        <form method="POST" id="verifierRaiseQueryRemarksDetails" action="appeal_revision_raise_query_case">
          <input type="hidden" id="gstinnorq" name="gstinno" >
          <input type="hidden" id="circlerq" name="circle" >
          <input type="hidden" id="reportingdaterq" name="reportingdate" >
          <input type="hidden" id="periodrq" name="period" >
          <input type="hidden" id="remarksRaiseQuery" name="remarksRaiseQuery" >
          <input type="hidden" id="otherRemarksTextValue" name="otherRemarksTextValue" >
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
          <div id="selectRaiseQueryRemarksValueTagLine" style="color: red;display: none;">Please Enter the Remarks !</div>
          <div id="selectRaiseQueryRemarksTagLine" style="color: red;display: none;">Please enter remarks.</div>      
		      <div class="form-group" style="margin-bottom: 0rem;">
            <label for="message-text" class="col-form-label">Remarks
            	<span style="color: red;"> *</span>
            </label>
          </div>
		      <div class="btn-group btn-group-vertical" role="group" data-toggle="buttons" style="width: 100%;">
            <c:forEach items="${raiseQueryRemarks}" var="object">
              <label class="btn btn-outline-custom" style="font-weight: 400;">
                <input type="radio" name="remarkOptions" id="${object.name}" onclick="showHideOtherRemark()" value="${object.id}" > ${object.name}
              </label>
        	  </c:forEach>
        	</div>
        	<textarea class="form-control" id="remarksRaiseQueryId" name="otherRemarks" style="display: none" placeholder="Remarks"></textarea>	
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="transferCaseBtn" onclick="submitVerifierRaiseQueryRemarksForm()">Submit</button>
      </div>
    </div>
  </div>
</div>
<script>
  function showHideOtherRemark(){
    $('#selectRaiseQueryRemarksTagLine').css('display', 'none');
    $('#selectRaiseQueryRemarksValueTagLine').css('display', 'none');
  
  
    var selectedValue = $('input[name="remarkOptions"]:checked').val();
    $("#remarksRaiseQuery").val(selectedValue);
      if(selectedValue == 1){
        $("#remarksRaiseQueryId").show();
      }else{
        $("#remarksRaiseQueryId").hide();
      }
  }

  function openPopuprq() {
      $.confirm({
					title : 'Confirm!',
					content : 'Do you want to proceed ahead with raising a query ?',
					buttons : {
						submit : function() {
              $('#raisequeryModal').modal('hide');
              
              document.getElementById("commonBoostrapAlertFail").style.display = "block";
              document.getElementById("raisedQueryTagLine").style.display = "block";
              setTimeout(function() {
              document.getElementById("verifierRaiseQueryRemarksDetails").submit();
      }, 300);
						},
						close : function() {
							$.alert('Canceled!');
						}
					}
				});
  }
  
  function closePopuprq() {
      $("#myPopuprq").hide();
  }
  
  function onOkrq() {
  
      document.getElementById("commonBoostrapAlertFail").style.display = "block";
  
      document.getElementById("raisedQueryTagLine").style.display = "block";
      setTimeout(function() {
        document.getElementById("verifierRaiseQueryRemarksDetails").submit();
      }, 500);
      
      $('#raisequeryModal').modal('hide');
      var popupContainer = document.getElementById('raisequeryModal');
      if (popupContainer) {
          popupContainer.style.display = 'none';
      }
      closePopuprq();
  }
  
  function onCancelrq() {
      closePopuprq();
  }
      
  function submitVerifierRaiseQueryRemarksForm(){
    
    var otherRemarksValue = $('#remarksRaiseQueryId').val();
    var selectedValue = $('input[name="remarkOptions"]:checked').val();
    $("#otherRemarksTextValue").val(otherRemarksValue);
    
  
    if((selectedValue == 1) && (otherRemarksValue == '')){
        $('#selectRaiseQueryRemarksValueTagLine').css('display', 'block');
        return;
      }
  
    
      if(selectedValue === undefined){
        $('#myPopuprq').hide();
        $('#selectRaiseQueryRemarksTagLine').css('display', 'block');
      }
      else{
        openPopuprq();
      }
  
    <!-- $("#verifierRaiseQueryRemarksDetails").submit();-->
  }
  
  </script>






