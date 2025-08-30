<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="en">
<head>

<link rel="stylesheet"
	href="/static/plugins/fontawesome-free/css/all.min.css">
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700&display=fallback">
<link rel="stylesheet"
	href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
<link rel="stylesheet"
	href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
<link rel="stylesheet"
	href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
<link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
<link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
<!-- <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet" /> -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" />
  <link rel="stylesheet" href="/static/plugins/datatables-bs4/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-responsive/css/responsive.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/plugins/datatables-buttons/css/buttons.bootstrap4.min.css">
  <link rel="stylesheet" href="/static/dist/css/adminlte.min.css">
  <link rel="stylesheet" href="/static/dist/css/jquery-confirm.min.css">
  
<style>  

th {
    position: relative;
    padding-right: 20px; /* Space for the arrow */
}
th:after {
    content: '\f0dc'; /* Font Awesome's fa-sort icon */
    font-family: 'Font Awesome 5 Free';
    position: absolute;
    right: 5px;
    top: 50%;
    transform: translateY(-50%);
    font-weight: 900;
}


th.sort-asc:after {
    content: '\f0de'; /* fa-sort-up */
}
th.sort-desc:after {
    content: '\f0dd'; /* fa-sort-down */
}

</style>  
  
<style>
@import
	url('https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css')
	;

*, *:after, *:before {
	-webkit-box-sizing: border-box;
	-moz-box-sizing: border-box;
	box-sizing: border-box;
}

.clearfix:before, .clearfix:after {
	content: " ";
	display: table;
}

.clearfix:after {
	clear: both;
}

body {
	font-family: sans-serif;
	background: #f6f9fa;
}

h1 {
	color: #3a3c87;
	text-align: center;
	font-size: 30px;
}

h4 {
	color: #0d4a80;
	text-align: left;
}

a {
	color: green;
	text-decoration: none;
	outline: none;
	font-style: italic; /* Add italic style */
	
}

/*Fun begins*/
.tab_container {
	width: 90%;
	margin: 0 auto;
	padding-top: 10px;
	position: relative;
}

input, section {
	clear: both;
	padding-top: 10px;
	display: none;
}

.custom-tab-label {
	font-weight: 700;
	font-size: 18px;
	display: block;
	float: left;
	width: 20%;
	padding: 1.5em;
	color: #757575;
	cursor: pointer;
	text-decoration: none;
	text-align: center;
 	background: linear-gradient(to top, white, #e5e9f1);
 }

#tab1:checked ~ #content1, #tab2:checked ~ #content2, #tab3:checked ~
	#content3, #tab4:checked ~ #content4, #tab5:checked ~ #content5 {
	display: block;
	padding: 20px;
	background: #fff;
	color: #999;
	border-bottom: 2px solid #f0f0f0;
}

.tab_container .tab-content p, .tab_container .tab-content h3 {
	-webkit-animation: fadeInScale 0.7s ease-in-out;
	-moz-animation: fadeInScale 0.7s ease-in-out;
	animation: fadeInScale 0.7s ease-in-out;
}

.tab_container .tab-content h3 {
	text-align: center;
}

.tab_container [id^="tab"]:checked+label {
	background: #fff;
	box-shadow: inset 0 3px #0CE;
}

.tab_container [id^="tab"]:checked+label .fa {
	color: #0CE;
}

label .fa {
	font-size: 1.3em;
	margin: 0 0.4em 0 0;
}

/*Media query*/
@media only screen and (max-width: 900px) {
	label span {
		display: none;
	}
	.tab_container {
		width: 98%;
	}
}

/*Content Animation*/
@
keyframes fadeInScale { 0% {
	transform: scale(0.9);
	opacity: 0;
}

100




%
{
transform




:




scale


(




1




)


;
opacity




:




1


;
}
}
.no_wrap {
	text-align: center;
	color: #0ce;
}

.link {
	text-align: center;
}

.red-heading {
	color: #0173b9;
}
</style>

<style>
footer {
	background-color: #222;
	color: #fff;
	font-size: 14px;
	bottom: 0;
	position: fixed;
	left: 0;
	right: 0;
	text-align: center;
	z-index: 999;
}

footer p {
	margin: 10px 0;
}

footer i {
	color: red;
}

a:hover {
	color: blue;
}

footer a {
	color: #3c97bf;
	text-decoration: none;
}

#messages {
	animation: 1s fadeIn;
	animation-fill-mode: forwards;
	visibility: hidden;
}

@
keyframes fadeIn { 50% {
	visibility: hidden;
}
60
%
{
visibility
:
visible;
opacity
:
0.5;
}
100
%
{
visibility
:
visible;
opacity
:
1;
}
}
</style>
<style>
#messages {
	animation: 1s fadeIn;
	animation-fill-mode: forwards;
	visibility: hidden;
}

@keyframes fadeIn {
	50% {
		visibility: hidden;
	}
	60% {
		visibility: visible;
		opacity: 0.5;
	}
	100% {
		visibility: visible;
		opacity: 1;
	}
}

</style>
 <style>
        @keyframes blink {
            0% {
                opacity: 1;
            }
            50% {
                opacity: 0;
            }
            100% {
                opacity: 1;
            }
        }
    </style>
    <style>
@keyframes colorChange {
    0% { color: #AF4D04; }
    50% { color: #e57373; }
    100% { color: #AF4D04; }
}
.button-link {
    font-size: 13px;
    padding: 10px 20px;
    background-color: #90a4b9;
    color: white;
    text-decoration: none;
    border-radius: 5px;
    display: inline-block;
}

</style>
</head>
<body>

	
	<div class="text-center">
	<br>
	<h1>Government of Himachal Pradesh</h1>
					<img src="static/image/logo_HP.png" style="width: 100px;"
						alt="logo">
					<p class="no_wrap">
			<br> 
				
<a href="/login" class="button-link"><i class="fa-solid fa-house"></i>
    Click Here To Go Back To Login Page
</a>
				
		</p>
				</div>
	<div class="tab_container">
		<c:set var="selectedTab" value="${selectedTab}" />
		<%-- <input id="tab1" type="radio" name="tabs"
			<c:if test="${selectedTab == 'tab1'}">checked</c:if>> <label
			for="tab1"><i class="fas fa-question-circle"></i><span>
				FAQs</span></label> <input id="tab2" type="radio" name="tabs"
			<c:if test="${selectedTab == 'tab2'}">checked</c:if>> <label
			for="tab2"><i class="fas fa-flag"></i><span> Acts &
				Rules</span></label> <input id="tab3" type="radio" name="tabs"
			<c:if test="${selectedTab == 'tab3'}">checked</c:if>> <label
			for="tab3"><i class="fas fa-exclamation-circle"></i><span>
				Notifications</span></label> <input id="tab5" type="radio" name="tabs"
			<c:if test="${selectedTab == 'tab5'}">checked</c:if>> <label
			for="tab5"><i class="fas fa-file-alt"></i><span>
				Circulars</span></label> <input id="tab4" type="radio" name="tabs"
			<c:if test="${selectedTab == 'tab4'}">checked</c:if>> <label
			for="tab4"><i class="fas fa-link"></i><span> Other
				Websites</span></label>
 --%>
 <input id="tab1" type="radio" name="tabs" <c:if test="${selectedTab == 'tab1'}">checked</c:if>> 
<label for="tab1" class="custom-tab-label"><i class="fas fa-question-circle"></i><span> FAQs</span></label> 

<input id="tab2" type="radio" name="tabs" <c:if test="${selectedTab == 'tab2'}">checked</c:if>> 
<label for="tab2" class="custom-tab-label"><i class="fas fa-flag"></i><span> Acts & Rules</span></label> 

<input id="tab3" type="radio" name="tabs" <c:if test="${selectedTab == 'tab3'}">checked</c:if>> 
<label for="tab3" class="custom-tab-label"><i class="fas fa-exclamation-circle"></i><span> Notifications</span></label> 

<input id="tab5" type="radio" name="tabs" <c:if test="${selectedTab == 'tab5'}">checked</c:if>> 
<label for="tab5" class="custom-tab-label"><i class="fas fa-file-alt"></i><span> Circulars</span></label> 

<input id="tab4" type="radio" name="tabs" <c:if test="${selectedTab == 'tab4'}">checked</c:if>> 
<label for="tab4" class="custom-tab-label"><i class="fas fa-link"></i><span> Other Useful Website Links</span></label>
 

		<section id="content1" class="tab-content">
			<h3>The section will be available soon!</h3>
			
		</section>

		<section id="content2" class="tab-content">
			<h3 class="red-heading">Himachal Pradesh Goods and Services Tax
				Provisions</h3>
			<br> <br>
			<form action="exploreMore" name="explore" id="explore1">
				<input type="hidden" id="selectedTab" name="selectedTab"
					value="tab2">

				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<h4>
								Select Act<span style="color: red;"> *</span>
							</h4>
							<select id="level" name="level" class="custom-select"
								onchange="submitForm1()">
								<option value="">Please Select</option>
								<c:forEach items="${levellist}" var="lvl">
									<option value="${lvl}"
										<c:if test="${lvl == level}">selected</c:if>>${lvl}</option>
								</c:forEach>
							</select>
						</div>
					</div>

				</div>
			</form>
			<c:if test="${not empty actsList}">
				<table class="table table-striped table-bordered" id="myTable1"
					style="color: black;">

					<thead class="thead-dark">
						<tr class="header">
							<th scope="col">Sl No</th>
							<th scope="col">Year</th>
							<th scope="col">Type</th>
							<th scope="col">Act</th>
							<th scope="col">File</th>
							<th scope="col">Upload Date</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${actsList}" var="circular">
							<tr>
								<th scope="row">${circular.id}</th>
								<td>${circular.year}</td>
								<td>${circular.type}</td>
								<td>${circular.level}</td>
								<td><a href="/downloadFile?fileName=${circular.fileName}&category=${circular.category}&year=${circular.year}" target="_blank"><i
										class="fas fa-file-pdf"></i> ${circular.fileName}</a></td>
								
								<td>${circular.uploadDate}</td>
										
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:if>
		</section>
<section id="content3" class="tab-content">
<form action="exploreMore" name="explore2" id="explore2">
<input type="hidden" id="selectedTab" name="selectedTab"
					value="tab3">

    <div class="relative col-md-12" style="display: flex;">
        <div class="relative col-md-4" id="notifications">
            <div class="p-4" style="background: linear-gradient(to bottom, white, #e5e9f1) !important;  border-radius: 15px; border: 2px solid #d3d3d3;"">
                <button type="button" class="inline-flex items-center gap-x-1 text-sm font-semibold leading-6 text-gray-900" aria-expanded="false">
                    <i class="fa-solid fa-bell fa-shake"></i> <span>Notifications</span>
                    <i class="fa-solid fa-chevron-down text-gray-600 group-hover:text-indigo-600 animate-bounce"></i>
                </button>
                <div id="messages">
                    <c:forEach items="${topNotilist}" var="notification">
                    <div class="group relative flex gap-x-6 rounded-lg p-4 hover:bg-gray-200">
                        <div class="mt-1 flex h-11 w-11 flex-none items-center justify-center rounded-lg bg-gray-50 group-hover:bg-white">
                            
                        </div>
                        <div>
                        <div>
                             <div style="color:red; animation: blink 2s infinite; font-size:12px; font-weight: bold;">
                             <i class="fa-solid fa-file-pdf" style="color:red; font-size:20px;"></i>
                                New PDF!!  </div>
                                 <span style="color:#034f84; font-size:11px;">uploaded on ${notification.uploadDate}</span>
                                <span class="absolute inset-0"></span>    
                           </div>
<a href="/downloadFile?fileName=${notification.fileName}&category=${notification.category}&year=${notification.year}" target="_blank" style="
    color: #034f84;
    animation: colorChange 3s infinite;
    -webkit-animation: colorChange 3s infinite; ">
    ${notification.fileName}
</a>                        </div>
                    </div>
                    </c:forEach>
                </div>
                <div class="grid grid-cols-2 divide-x divide-gray-900/5 bg-gray-100">
                    <a href="#" class="flex items-center justify-center gap-x-2.5 p-3 font-semibold text-gray-900 hover:bg-blue-200" id="toggleButton">
                        <i class="fa-solid fa-broom"></i> Clear
                    </a>
                    <a href="#" class="flex items-center justify-center gap-x-2.5 p-3 font-semibold text-gray-900 hover:bg-blue-200" id="closeButton">
                        <i class="fa-regular fa-circle-xmark"></i> Close
                    </a>
                </div>
            </div>
        </div>
        <div class="relative col-md-8">
         <div class="col-md-12" style="display: flex;">
        	<div class="col-md-6">
						<div class="form-group">
							<h4>
								Select Financial Year<span style="color: red;"> *</span>
							</h4>
							<select id="financialyearNoti" name="financialyearNoti"
								class="custom-select">
								<option value="">Please Select</option>
								<c:forEach items="${yearlist}" var="year">
									<option value="${year}"
										<c:if test="${year == financialyearNoti}">selected</c:if>>${year}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
							<h4>
								Select Type<span style="color: red;"> *</span>
							</h4>
							<select id="type" name="type"
								class="custom-select">
								<option value="">Please Select</option>
								<c:forEach items="${typelist}" var="year">
									<option value="${year}"
										<c:if test="${year == type}">selected</c:if>>${year}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					</div>
					<div class="row">
							<div class="col-md-12">
								<div class="text-center">
									<button type="button" class="btn btn-primary"
											onclick="submitNotiForm()">Submit</button>
								</div>
							</div>
						</div>
						<br>

            <c:if test="${not empty notiList}">
				<table class="table table-striped table-bordered" id="myTable3"
					style="color: black;">

					<thead class="thead-dark">
						<tr class="header">
							<th scope="col">Sl No</th>
							<th scope="col">Type</th>
							<th scope="col">File</th>
							<th scope="col">Upload Date</th>
							
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${notiList}" var="circular">
							<tr>
								<th scope="row">${circular.id}</th>
								<td>${circular.type}</td>
								<td><a href="/downloadFile?fileName=${circular.fileName}&category=${circular.category}&year=${circular.year}" target="_blank"><i
										class="fas fa-file-pdf"></i> ${circular.fileName}</a></td>	
																	<td>${circular.uploadDate}</td>
								
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:if>
        </div>
    </div>
    </form>
</section>

		<section id="content4" class="tab-content">
			<input type="hidden" id="selectedTab" name="selectedTab" value="tab4">
			<table class="table table-striped table-bordered"
				style="color: black;">
				<thead class="thead-dark">
					<tr>
						<th scope="col">Sl No</th>
						<th scope="col">Website Name</th>
						<th scope="col">Website Link</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<th scope="row">1</th>
						<td>Goods and Services Tax,India</td>
						<td><a href="https://www.gst.gov.in/" target="_blank"><i
								class="fas fa-hand-pointer"></i> Click Here</a></td>
					</tr>
					<tr>
						<th scope="row">2</th>
						<td>E-WayBill System</td>
						<td><a href="https://ewaybillgst.gov.in/" target="_blank"><i
								class="fas fa-hand-pointer"></i> Click Here</a></td>
					</tr>
					<tr>
						<th scope="row">3</th>
						<td>HP VAT</td>
						<td><a href="https://www.hptax.gov.in/" target="_blank"><i
								class="fas fa-hand-pointer"></i> Click Here</a></td>
					</tr>
					<tr>
						<th scope="row">4</th>
						<td>HP Excise E-Governance</td>
						<td><a href="https://egovef.hptax.gov.in/" target="_blank"><i
								class="fas fa-hand-pointer"></i> Click Here</a></td>
					</tr>
					<tr>
						<th scope="row">5</th>
						<td>Government of HP </td>
						<td><a href="https://himachal.nic.in/en-IN/" target="_blank"><i
								class="fas fa-hand-pointer"></i> Click Here</a></td>
					</tr>
					<tr>
						<th scope="row">6</th>
						<td>HP Finance Department</td>
						<td><a href="https://himachal.nic.in/index.php?lang=1&dpt_id=1" target="_blank"><i
								class="fas fa-hand-pointer"></i> Click Here</a></td>
					</tr>
				</tbody>
			</table>
		</section>

		<section id="content5" class="tab-content">
			<form action="exploreMore" name="explore" id="explore">
				<input type="hidden" id="selectedTab" name="selectedTab"
					value="tab5">

				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<h4>
								Select Financial Year<span style="color: red;"> *</span>
							</h4>
							<select id="financialyear" name="financialyear"
								class="custom-select" onchange="submitForm()">
								<option value="">Please Select</option>
								<c:forEach items="${yearlist}" var="year">
									<option value="${year}"
										<c:if test="${year == financialyear}">selected</c:if>>${year}</option>
								</c:forEach>
							</select>
						</div>
					</div>

					<c:if test="${not empty circularList}">
						<div class="col-md-6">
							<div class="form-group">
								<h4>Search File By Name</h4>
								<input type="text" class="form-control" id="myInput"
									placeholder="Search..." onkeyup="myFunction()">
							</div>
						</div>
					</c:if>

				</div>
				<div>
<c:if test="${not empty circularList}">
					<table class="table table-striped table-bordered" id="myTable"
						style="color: black;">

						<thead class="thead-dark">
							<tr class="header">
								<th scope="col">Sl No</th>
								<th scope="col">Year</th>
								<th scope="col">File Name</th>
								<th scope="col">Upload Date</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${circularList}" var="circular">
								<tr>
									<th scope="row">${circular.id}</th>
									<th scope="row">${circular.year}</th>
<td><a href="/downloadFile?fileName=${circular.fileName}&category=${circular.category}&year=${circular.year}" target="_blank"><i
										class="fas fa-file-pdf"></i> ${circular.fileName}</a></td>									<td>${circular.uploadDate}</td>
									
								</tr>
							</c:forEach>
						</tbody>
					</table>
					</c:if>
				</div>

				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" />
			</form>
		</section>
</div>

		<p>
		</p>
		<p>
		</p>
		<br>
		<footer>
			<p>
				<strong>Copyright &copy; 2023-2024 <a href="/">Govt of
						Himachal Pradesh</a>.
				</strong> All rights reserved.
			</p>
		</footer> 
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
<script src="/static/dist/js/adminlte.min.js"></script>
<script src="/static/dist/js/jquery-confirm.min.js"></script>
		<script type="text/javascript">

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
			$(document).ready(function() {
				function disableBack() {
					window.history.forward()
				}
	
				window.onload = disableBack();
				window.onpageshow = function(evt) {
					if (evt.persisted)
						disableBack()
				}
			});
			// Disable refresh
			document.onkeydown = function(e) {
				if (e.key === 'F5' || (e.ctrlKey && e.key === 'r')
						|| e.keyCode === 116) {
					e.preventDefault();
	
				}
			};
		
			function submitForm() {
				document.forms["explore"].submit();
			}
			
		</script>
		<script type="text/javascript">
			function submitForm2() {
				document.forms["explore2"].submit();
			}
		</script>
		 <script type="text/javascript">
		function submitForm1() {
			document.forms["explore1"].submit();
		}
	</script>

<script>
    $(document).ready(function() {
        $('#myTable3').DataTable();
    });
    $(document).ready(function() {
        $('#myTable').DataTable();
    });
</script>

	 <script>
		function myFunction() {
			var input, filter, table, tr, td, i, txtValue;
			input = document.getElementById("myInput");
			filter = input.value.toUpperCase();
			table = document.getElementById("myTable");
			console.log("jhi");
			tr = table.getElementsByTagName("tr");
			for (i = 0; i < tr.length; i++) {
				td = tr[i].getElementsByTagName("td")[0];
				if (td) {
					txtValue = td.textContent || td.innerText;
					if (txtValue.toUpperCase().indexOf(filter) > -1) {
						tr[i].style.display = "";
					} else {
						tr[i].style.display = "none";
					}
				}
			}
		}
	</script> <script>
		// Get references to the button and the notifications div
		const closeButton = document.getElementById("closeButton");
		const toggleButton = document.getElementById("toggleButton");
		const notifications = document.getElementById("notifications");
		const messages = document.getElementById("messages");

		// Add event listener to the button
		closeButton.addEventListener("click", function() {
			// Toggle the visibility of the notifications div
			if (notifications.style.display === "none") {
				notifications.style.display = "block";
				closeButton.textContent = "Hide Notifications";
			} else {
				notifications.style.display = "none";
				closeButton.textContent = "Show Notifications";
			}
		});

		toggleButton.addEventListener("click", function() {
			// Toggle the visibility of the notifications div
			if (messages.style.display === "none") {
				messages.style.display = "block";
				toggleButton.textContent = "Clear";
			} else {
				messages.style.display = "none";
				toggleButton.textContent = "Undo";
			}
		});
		
	</script>
	
<script>
    function submitNotiForm() {
       
        var financialYear = document.getElementById("financialyearNoti").value;
        var type = document.getElementById("type").value;
      
        if (type === "" || financialYear === "") {
            alert("Please select all required fields");
        } else {
         document.getElementById("explore2").submit();

        }
    }
</script>
</body>
</html>
