<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Acknowledge Case List</title>

  <!-- <link rel="stylesheet" href="/static/dist/css/googleFront/googleFrontFamilySourceSansPro.css"> -->
  <link rel="stylesheet" href="/static/plugins/fontawesome-free/css/all.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/dist/css/bootstrap-select.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
<style>
  .nav-tabs .nav-item.show .nav-link, .nav-tabs .nav-link.active{
      background-color: #007bff;
      color: #fff;
  }
  
  .table-responsive {
   overflow: scroll;
   max-height: 800px;
   
  }
</style>

</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">
  <jsp:include page="../layout/header.jsp"/>
  <jsp:include page="../layout/confirmation_popup.jsp"/>
  <jsp:include page="../fo/transfer_popup.jsp"/>
  <jsp:include page="../layout/sidebar.jsp"/>
  <div class="content-wrapper">
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Review Meeting Comments</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a >Home</a></li>
              <li class="breadcrumb-item active">Review Cases List View</li>
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
                <h3 class="card-title">Review Cases List View</h3>
              </div>
              <div class="card-body">
	            <c:if test="${not empty message}">
								<div class="alert alert-success alert-dismissible fade show" id="message" role="alert">
									<strong>${message}</strong>					
								</div>
							</c:if>
	            <div class="card-body">
								<div class="row"> 
									<div class="col-md-12">
										<div class="form-group">
											<ul class="nav nav-tabs" role="tablist">
													<li class="nav-item" >
														<a class="nav-link active" data-toggle="tab" href="#home" >Category wise Review Meeting</a>
													</li>
													<li class="nav-item">
														<a class="nav-link" data-toggle="tab" href="#menu1" >Detailed Enforcement Case wise Review Meeting</a>
													</li>
											</ul>
										</div>
									</div>
								</div>
				  			<form method="POST" action="update_category_remarks" id="category_remarks"  name="category_remarks"  enctype="multipart/form-data" >
									<div class="container-fluid">   
										<div class="row">
											<div class="col-sm-12">
				    						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> 
												<div class="tab-content">
													<div id="home" class="tab-pane active">
														<br>
														<div class="row">
															<div class="col-md-4">
																<div class="form-group">
																	<label for="casecategory">Review Meeting Date</label>
																	<input type="date" class="form-control" id="reviewMeetingDateId"  onchange="reviewMeetingsDate(this.value)"  name="reviewMeetingDate"  required />
																</div>
															</div>
															<div class="col-md-4">
																<div class="form-group">
																	<label for="casereportingdate">MoM Document </label> 
																	<input type="file" class="form-control" id="meetingDocumentId"  name="meetingDocument" required />
																</div>
															</div>
		                  			                      </div>
														<br>
	                					                <table id="examaple" class="table table-bordered table-striped">
															<thead>
																<tr>
																	<th style="text-align: center; width: 15%; vertical-align: middle;" >Category</th>
																	<th style="text-align: center; width: 20%; vertical-align: middle;" >Previous Review Meeting Comments</th>
																	<th style="text-align: center; width: 30%; vertical-align: middle;" >Review Meeting Comments</th>
																</tr>
															</thead>
															<tbody>
																<c:forEach items="${list}"  var="obj">
																
																<tr>
																	<td>${obj.category}</td>
																	<td><textarea id="categoryoldremarks" name="categoryoldremarks" style="width:100%;background-color:#e9ecef;" readonly> ${obj.remarks} </textarea></td>
																	<td><textarea id="categoryremarks" name="categoryremarks" style="width:100%;"></textarea></td>
																</tr>
																
																</c:forEach>
															</tbody>
			                 					</table>
			                					<br/>
				    							</div>
				    							<div id="menu1" class="tab-pane fade"><br>
				     								<table id="example" class="table table-bordered table-striped table-responsive">
															<thead>
																<tr>
																	<th style="text-align: center; width: 7%; vertical-align: middle;" >GSTIN</th>
																	<th style="text-align: center; width: 7%; vertical-align: middle;" >Taxpayer Name</th>
																	<th style="text-align: center; width: 7%; vertical-align: middle;" >Jurisdiction</th>
																	<th style="text-align: center; width: 7%; vertical-align: middle;" >Category</th>
																	<th style="text-align: center; width: 7%; vertical-align: middle;" >Reporting Date</th>
																	<th style="text-align: center; width: 7%; vertical-align: middle;" >Indicative Value(₹)</th>
																	<th style="text-align: center; width: 7%; vertical-align: middle;" >Action Status</th>
																	<th style="text-align: center; width: 7%; vertical-align: middle;" >Case Stage</th>
																	<th style="text-align: center; width: 7%; vertical-align: middle;" >Assigned To</th>
																	<th style="text-align: center; width: 7%; vertical-align: middle;" >Amount(₹)</th>
																	<th style="text-align: center; width: 7%; vertical-align: middle;" >Recovery Stage</th>
																	<th style="text-align: center; width: 8%; vertical-align: middle;" >Recovery by DRC-3(₹)</th>
																	<th style="text-align: center; width: 7%; vertical-align: middle;" >Recovery Against Demand(₹)</th>
																	<th style="text-align: center; width: 7%; vertical-align: middle;" >Previous Review Meeting Comments</th>
																	<th style="text-align: center; width: 13%; vertical-align: middle;" >Review Meeting Comments</th>
																</tr>
															</thead>
                   						                    <tbody>
																<c:forEach items="${gstlist}"  var="obj" >
																	<tr>
																		<td>${obj.GSTIN_ID}</td>
																		<input type="hidden" name="gstin" value="${obj.GSTIN_ID}">
																		<td>${obj.taxpayerName}</td>
																		<td>${obj.circle}</td>
																		<td>${obj.category}</td>
																		<input type="hidden" name="catogy" value="${obj.categoryId}">
																		<td><fmt:formatDate value="${obj.date}" pattern="dd-MM-yyyy" /></td>
																		<input type="hidden" name="caseReportingDateId" value="${obj.caseReportingDate_ID}">
																		<td>${obj.indicativeTaxVal}</td>
																		<input type="hidden" name="period" value="${obj.period_ID}">
																		<td>${obj.actionStatusName}</td>
																		<td>${obj.caseStageName}</td>
																		<td>
																			<c:choose>
																				<c:when  test = "${obj.assignedTo eq 'HQ'}">
																				Head Office
																				</c:when>
																				<c:when  test = "${obj.assignedTo eq 'FO'}">
																				Field Office
																				</c:when>
																				<c:when  test = "${obj.assignedTo eq 'RU'}">
																				Reviewer
																				</c:when>
																				<c:when  test = "${obj.assignedTo eq 'AP'}">
																				Approver
																				</c:when>
																			</c:choose>
																		</td>
																		<td>${obj.demand}</td>
																		<td>${obj.recoveryStageName}</td>
																		<td>${obj.recoveryByDRC3}</td>
																		<td>${obj.recoveryAgainstDemand}</td>
																		<td><textarea id="w3review" name="oldremarks" style="width:100%;background-color:#e9ecef;" readonly>${obj.remarks}</textarea></td>
																		<td><textarea id="w3review" name="remarks" style="width:100%;"></textarea></td>
																	</tr>
																</c:forEach>
                  						</tbody>
                						</table>
                 						<br>
                						<br>
                  				</div>
				  							</div>
				  						</div>
				  					</div>
				  				</div>
				  				<div class="row">
										<div class="col-md-12">
											<div class="form-group">
												<button type="submit" class="btn btn-primary" id="submitBtn" style="float : right;"  >Submit</button>
											</div>
										</div>
				  				</div>
				  			</form>
				  		</div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
  <jsp:include page="../layout/footer.jsp"/>
  <aside class="control-sidebar control-sidebar-dark">
  </aside>
</div>
<div class="modal fade" id="confirmationModal" tabindex="-1" role="dialog" aria-labelledby="confirmationModalTitle" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
     <form method="POST" action="fo_acknowledge_cases" name="acknowledgeForm" >
      <div class="modal-header">
        <h5 class="modal-title" id="confirmationModalTitle">Are you sure want to acknowledge ?</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <input type="hidden" id="gstno_acknowledge" name="gstno" >
      <input type="hidden" id="date_acknowledge" name="date" >
      <input type="hidden" id="period_acknowledge" name="period" >
      <div class="modal-footer">
        <button type="submit" class="btn btn-primary" id="okayBtn" >Okay</button>
        <button type="button" class="btn btn-secondary" data-dismiss="modal" id="cancelBtn" >Cancel</button>
      </div>
      </form>
    </div>
  </div>
</div>
<script src="/static/plugins/jquery/jquery.min.js"></script>
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
<script src="/static/dist/js/jquery-confirm.min.js"></script>
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
  function acknowledge(gstno , date , period){
	   $("#gstno_acknowledge").val(gstno);
	   $("#date_acknowledge").val(date);
	   $("#period_acknowledge").val(period);
	   $("#confirmationModal").modal('show');
  }
  function transferBtn(gstno , date , period){
	  $("#gstno").val(gstno);
	  $("#date").val(date);
	  $("#period").val(period);
	  $("#transferModal").modal('show');
  }
</script>
<script>
  $(document).ready(function() {
		$("#message").fadeTo(2000, 500).slideUp(500, function() {
			$("#message").slideUp(500);
		});
  });
  $(function () {
    $("#example1").DataTable({
      "responsive": true, "lengthChange": false, "autoWidth": false,
      "buttons": 
				[
					"excel",
					"pdf",
					"print", 
				]
    }).buttons().container().appendTo('#example1_wrapper .col-md-6:eq(0)');
    $('#example2').DataTable({
      "paging": true,
      "lengthChange": false,
      "searching": false,
      "ordering": true,
      "info": true,
      "autoWidth": false,
      "responsive": true,
    });
  });

  $(function () {
	  $("#transferBtn").on("click", function(){
	    $("#transferModal").modal('show');
	  });
	
	  $("#okayBtn").on("click", function(){
	    console.log("save");
	    $("#confirmationModal").modal('hide');
	  });
	  
	  $("#cancelBtn").on("click", function(){
		  console.log("failed");
	    $("#confirmationModal").modal('hide');
	  });
		  
	});

  $('form').on('submit', function(oEvent) {
		oEvent.preventDefault();
		$.confirm({
			title : 'Confirm!',
			content : 'Do you want to proceed ahead with submission of review meeting comments?',
			buttons : {
				submit : function() {

				    oEvent.currentTarget.submit();	

				},
				close : function() {
					$.alert('Canceled!');
				}
			}
		});
	});
</script>

<script>

function reviewMeetingsDate(val){

    var currentDate = new Date();

    var timestamp = Date.parse(val);
    
    var dateObject = new Date(timestamp);
    
    if(dateObject > currentDate){

       alert("Review Meeting date can not be greater than current date");
       
       $("#reviewMeetingDateId").val("");
    
    }    
  
}



</script>

</body>
</html>
