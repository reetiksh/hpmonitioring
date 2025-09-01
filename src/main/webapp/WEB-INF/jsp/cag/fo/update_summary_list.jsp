<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Update Status List</title>

  <!-- <link rel="stylesheet" href="/static/dist/css/googleFront/googleFrontFamilySourceSansPro.css"> -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
  
  <style>
.modal-lg, .modal-xl {
    max-width: 1854px;
}

.table-responsive {
 overflow: scroll;
 max-height: 800px;
 }


.btn-outline-custom {
  color: #495057;
  border: 1px solid #ced4da;
  text-align:left;
}


</style>

  
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
<jsp:include page="../../layout/header.jsp"/>
<jsp:include page="../../layout/sidebar.jsp"/>
<jsp:include page="../../layout/confirmation_popup.jsp"/>
<div class="content-wrapper">
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Update Status</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a>Home</a></li>
              <li class="breadcrumb-item active">Update Status</li>
            </ol>
          </div>
        </div>
      </div>
    </section>
    <section class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-12">
            <div class="card-primary">
              <div class="card-header">
                <h3 class="card-title">Update Status</h3>
              </div>
              <div class="card-body">
                <c:if test="${not empty closeclasemessage}">
                  <div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
                    <strong>${closeclasemessage}</strong>
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                      <span aria-hidden="true">&times;</span>
                    </button>
                  </div>
                </c:if>
                <c:if test="${not empty message}">
                  <div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
                    <strong>${message}</strong>
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                      <span aria-hidden="true">&times;</span>
                    </button>
                  </div>
                </c:if>
                <div class="row">
                  
                </div>
                <div id ="loader" style="display:none; text-align:center;">
                  <i class="fa fa-spinner fa-spin" style="font-size:52px;color:#007bff;"></i>
                </div>
                <div id="dataListDiv">
                
				<div class="row">
					<table id="dataListTable" class="table table-bordered table-striped">
						<thead>
							<tr>
								<th style="text-align: center; vertical-align: middle;">GSTIN</th>
								<th style="text-align: center; vertical-align: middle;">Taxpayer Name</th>
								<th style="text-align: center; vertical-align: middle;">Jurisdiction</th>
								<th style="text-align: center; vertical-align: middle;">Period</th>
								<th style="text-align: center; vertical-align: middle;">Reporting Date<br>(DD-MM-YYYY)</th>
								<th style="text-align: center; vertical-align: middle;">Parameter</th>
								<th style="text-align: center; vertical-align: middle;">Indicative Value(₹)</th>
								<th style="text-align: center; vertical-align: middle;">Action</th>
							</tr>
						</thead>
						<tbody>
						    <c:forEach items="${listofCases}" var="item">             
							<tr>
								<td>${item.GSTIN_ID}</td>
								<td>${item.taxpayerName}</td>
								<td>${item.circle}</td>
								<td>${item.period_ID}</td>
								<td><fmt:formatDate value="${item.date}"  pattern="dd-MM-yyyy" /></td>
								<td>${item.parameter}</td>
								<td>${item.indicativeTaxValue}</td>
								<td>
								
								    <c:if test="${item.reason == 'deactive'}">
									<button type="button" class="btn btn-primary viewbtn" onclick="view('${item.GSTIN_ID}' , '${item.caseReportingDate_ID}' , '${item.period_ID}', '${item.parameter}')" disabled >Update</button>
									</c:if>
									<c:if test="${item.reason == 'active'}">
									<button type="button" class="btn btn-primary viewbtn" onclick="view('${item.GSTIN_ID}' , '${item.caseReportingDate_ID}' , '${item.period_ID}', '${item.parameter}')" >Update</button>
									</c:if>
									
									<c:if test="${item.caseIdUpdated == 'deactive'}">
									<button type="button" class="btn btn-primary"  onclick="closeCase('${item.GSTIN_ID}' , '${item.caseReportingDate_ID}' , '${item.period_ID}' , '${item.parameter}')" disabled >Close</button>
									</c:if>
									<c:if test="${item.caseIdUpdated == 'active'}">
									<button type="button" class="btn btn-primary"  onclick="closeCase('${item.GSTIN_ID}' , '${item.caseReportingDate_ID}' , '${item.period_ID}' , '${item.parameter}')" >Close</button>
									</c:if>
									
								</td>
							</tr>
							</c:forEach>
						</tbody>
						
					</table>
				</div>
				
				<div class="modal fade" id="updateSummaryViewModal" tabindex="-1" role="dialog" aria-labelledby="updateSummaryViewTitle" aria-hidden="true" >
				  <div class="modal-dialog modal-dialog-centered modal-lg" role="document" >
				    <div class="modal-content">
				      <div class="modal-header">
				        <h5 class="modal-title" id="updateSummaryViewModalTitle">Update Case </h5>
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close">    
				          <span aria-hidden="true">&times;</span>
				        </button>
				      </div>
				      
				      <div class="modal-body" id="updateSummaryViewBody" >
				       
				      </div>
				      
				    </div>
				  </div>
				</div>
				
				<div class="modal fade" id="closeCaseModal" tabindex="-1" role="dialog" aria-labelledby="closeCaseModalTitle" aria-hidden="true">
				  <div class="modal-dialog modal-dialog-centered" role="document">
				  
				    <div class="modal-content">
				     <form method="POST" action="close_cases" id="closeCaseForm" name="closeCaseForm"  enctype="multipart/form-data" >
				      <div class="modal-header">
				        <h5 class="modal-title" id="confirmationModalTitle">Do you want to proceed ahead with closure of the case?</h5>
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
				          <span aria-hidden="true">&times;</span>
				        </button>
				      </div>
				      
				      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				      
				      <div class="modal-body">
					    
					  <div class="row">
		
						<div class="col-md-6">
								<div class="form-group">
									<label for="excelFile">File Upload <span style="color: red">*</span></label><span> (upload only pdf file with max file size of 10MB ) </span>
				                    <input class="form-control" type="file" id="uploadedFile"  name="uploadedFile" accept=".pdf"  required>
								</div>
						</div>
						
						<div class="col-md-6">
							<div class="form-group">
							<label for="">Remarks <span style="color: red">*</span></label>
							<br>
							<br>
							<input type="text" class="form-control" id="otherRemarks" name="otherRemarks" required />
							</div>
						</div>
						   
						</div>   
					    
					  </div>
				      
				      <input type="hidden" id="gstno" name="gstno" >
				      <input type="hidden" id="date" name="date" >
				      <input type="hidden" id="period" name="period" >
				      <input type="hidden" id="parameter" name="parameter" >
				      
				      <div class="modal-footer">
				        <button type="submit" class="btn btn-primary" id="okayBtn" >Okay</button>
				        <button type="button" class="btn btn-secondary" data-dismiss="modal" id="closeBtn" >Cancel</button>
				      </div>
				      </form>
				    </div>
				  </div>
				</div>
                
                
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
  <jsp:include page="../../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark">
  </aside>
</div>
<script src="/static/plugins/jquery/jquery.min.js"></script>
<script	src="/static/dist/js/jquery-confirm.min.js"></script>
<script src="/static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="/static/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="/static/plugins/datatables-bs4/js/dataTables.bootstrap4.min.js"></script>
<script src="/static/plugins/datatables-responsive/js/dataTables.responsive.min.js"></script>
<script src="/static/plugins/datatables-responsive/js/responsive.bootstrap4.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/dataTables.buttons.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.bootstrap4.min.js"></script>
<script src="/static/plugins/jszip/jszip.min.js"></script>
<script src="/static/plugins/pdfmake/pdfmake.min.js"></script>
<script src="/static/plugins/pdfmake/vfs_fonts.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.html5.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.print.min.js"></script>
<script src="/static/plugins/datatables-buttons/js/buttons.colVis.min.js"></script>
<script src="/static/dist/js/bootstrap-select.min.js"></script>
<script src="/static/dist/js/adminlte.min.js"></script>
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
  $(function () {
      $("#categor1").on('change', function(){

      var selectedValue =	$(this).val();
      
      $.ajax({url: '/checkLoginStatus',
              method: 'get',
              async: false,
                  success: function(result){
                      const myJSON = JSON.parse(result);
                    if(result=='true'){
                    $("#dataListDiv").empty();
                  $("#loader").show();

                  setTimeout(function(){$("#dataListDiv").load($(location).attr('protocol')+"//"+
                      $(location).attr('host')+'/fo/update_summary_data_list', 
                      function(response, status, xhr){

                    $("#loader").hide();
                    
                    if(status == 'success'){
                      console.log("success");
                    }else{
                      console.log("failed");
                    }
                  });},1000);
                    }
                    else if(result=='false'){
                      window.location.reload();
                    }
                    }
              });
      });
  });
</script>

<script>

function formValidation(){
    
	var actionStatusId = $("#actionStatus").val();
	
    if(actionStatusId == 1){
    
    return true;
         
    }else{

        var fileName = document.querySelector('#uploadedFile').value;

        var extension = fileName.slice((fileName.lastIndexOf(".") - 1 >>> 0) + 2);
        
        var input = document.getElementById('uploadedFile');
        
     	if (input.files && input.files[0]) {
        
     	var maxAllowedSize = 10 * 1024 * 1024;

     	if(extension == 'pdf'){
             
        if(input.files[0].size > maxAllowedSize) {

        	alert('Please upload max 10MB file');
         	input.value = '';
         	return false;
        }else{
       
          return true;
 	    }

     	}else{

          alert("Please upload only pdf file");
          document.querySelector('#uploadedFile').value = '';
     	  return false;
          }

     	  }else{

     		alert("Please upload pdf file");
     		return false;
     	  } 
   
      }
        
}

</script>

<script>


	$(document).ready(function() {
		$("#message").fadeTo(2000, 500).slideUp(500, function() {
			$("#message").slideUp(500);
		});
	});

	
</script>


<script>

$('#closeCaseForm').on('submit', function(oEvent) {

    debugger;
	
	oEvent.preventDefault();
	
	$.confirm({
		title : 'Confirm!',
		content : 'Do you want to proceed ahead with closure of the case?',
		buttons : {
			submit : function() {

							var fileName = document.querySelector('#uploadedFile').value;

					        var extension = fileName.slice((fileName.lastIndexOf(".") - 1 >>> 0) + 2);
					        
					        var input = document.getElementById('uploadedFile');
					        
					     	if (input.files && input.files[0]) {
					        
					     	var maxAllowedSize = 10 * 1024 * 1024;
		                    
					     	if(extension == 'pdf'){
					             
						        if(input.files[0].size > maxAllowedSize) {

						        	$.alert('Please upload max 10MB file');
						         	input.value = '';
						        }else{

						        	oEvent.currentTarget.submit();
						 	    }

						    }else{

					     		 $.alert("Please upload pdf file");
					     	}

					     	}

							},
							close : function() {
								$.alert('Canceled!');
							}
				   }
			});
});


</script>


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
  function view(gst, date, period, parameter){

      const encodedString = encodeURIComponent(parameter);
	  
	  $.ajax({url: '/checkLoginStatus',
          method: 'get',
          async: false,
              success: function(result){
                  const myJSON = JSON.parse(result);
            	  if(result=='true'){
					  $("#updateSummaryViewBody").load('/cag_fo/view_cag_case/id?gst='+gst+'&date='+date+'&period='+period+'&parameter='+encodedString+'',
							  function(response, status, xhr){
				
						if(status == 'success'){
							 $("#updateSummaryViewModal").modal('show');
						}else{
							console.log("failed");
						}
					 });
					  
            	  }
               	  else if(result=='false'){
               		  window.location.reload();
               	  }
                   }
                 
             });
     }

 
  function closeCase(gstno , date, period, parameter){

    $("#gstno").val(gstno);
	$("#date").val(date);
	$("#period").val(period);
	$("#parameter").val(parameter);

    $("#closeCaseModal").modal('show');
    
  }
  
</script>

<script>

$(function () {

	  $('#mycheckbox').change(function(){
	    if(this.checked) {
	    	$("#checked").show();
	    } else {
	        $("#checked").hide();
	    }
	  });
    
	  
	  $("#updateSummaryClosedCaseBtn").on("click", function(){
	    $("#confirmationModal").modal('show');
	  });

	  
	  $("#okayBtn").on("click", function(){
	    console.log("save");
	    $("#closeCaseModal").modal('hide');
	  });
	  
	  $("#closeBtn").on("click", function(){
		  console.log("failed");
	    $("#closeCaseModal").modal('hide');
	  });
      
		  
	});  


	$('#closeCaseModal').on('hidden.bs.modal', function () {
	    
	    $('#closeCaseForm')[0].reset();
	    $('#uploadedFile').val('');
	    $('#otherRemarks').val('');
	});

</script>


</body>
</html>
