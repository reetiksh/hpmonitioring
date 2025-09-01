<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Session Expired</title>
<link rel="stylesheet" href="static/dist/css/adminlte.min.css">
<style>
body {
	background-color: #f8f9fa;
}

.container {
	margin-top: 100px;
	text-align: center;
}
</style>
</head>
<body>
	<div class="container">
		<div class="alert alert-warning" role="alert">
			<h4 class="alert-heading">Session Expired</h4>
			<p>Your session has expired as you have logged in from
				another browser. Please log in again.</p>
			<hr>
			<p class="mb-0">
				<a href="/login" class="btn btn-primary">Log In</a>
			</p>
		</div>
	</div>
	<!-- Bootstrap JS (Optional) -->

	<script src="static/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
	<script src="static/dist/js/adminlte.min.js"></script>
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
</body>
</html>
