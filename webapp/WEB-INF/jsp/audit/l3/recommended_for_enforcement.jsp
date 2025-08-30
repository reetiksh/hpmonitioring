<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<script>
  $(function () {
    bsCustomFileInput.init();
  });

  $(document).ready(function() {
    var workingDate = '${amdDocument.actionDate}';
    var pdfFile = '${amdDocument.actionFilePath}';
    var updateButton = document.querySelector(".btn.btn-primary");
    var pdfFileDiv = document.getElementById("pdfFileDiv");
    var divElement = document.getElementById("pdfFileDownloadDiv");
    var spanElement = document.getElementById("pdfFileName");

    if(workingDate.length > 0) {
      $("#workingDate").prop("readonly", true);
      updateButton.disabled = true;
    }

    if(pdfFile.length > 0) {
      pdfFileDiv.style.display = "none";
      divElement.style.display = "block";
      spanElement.textContent = pdfFile;
    }
  });

  $('form').on('submit', function(oEvent) {
    oEvent.preventDefault();

    var pdfFile = $("#pdfFile").val();
    if(!VerifyUploadSizeIsOK("pdfFile", 10485760)){
      var x = document.getElementById("message1");
      if(x.style.display === "none"){
        $("#alertMessage1").html("Please upload pdf file upto 10MB").css("color","#fffff");
        x.style.display = "block";
        showDiv();
      }
      return;
    }
    
    $.confirm({
      title : 'Confirm!',
      content : 'Do you want to proceed ahead with submission of audit plan details?',
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

  function VerifyUploadSizeIsOK(UploadFieldID, MaxSizeInBytes){
	  var fld = document.getElementById(UploadFieldID);
	  if( fld.files && fld.files.length == 1 && fld.files[0].size > MaxSizeInBytes ){
			return false;
	  }
	  return true;
	}

  $(document).ready(function() {
		$("#workingDate").on("change", function() {
			var effectiveDate = $("#workingDate").val();
			var now = new Date();
      var previousStatusDate = new Date("${previousStatusDate}");
      now.setHours(23, 59, 59, 999);
			const givenDate = new Date(effectiveDate);
			
			if (givenDate > now) {
				$("#workingDate").val('');

				var x = document.getElementById("message1");
				if(x.style.display === "none"){
					$("#alertMessage1").html("Future date is not allowed").css("color","#fffff");
					x.style.display = "block";
					showDiv();
				}
			} else if (givenDate < previousStatusDate) {
        $("#workingDate").val('');

        var x = document.getElementById("message1");
				if(x.style.display === "none"){
					$("#alertMessage1").html("The entered date cannot be before " +  formatDate(previousStatusDate)).css("color","#fffff");
					x.style.display = "block";
					showDiv();
				}
      }
		});
	});

  function showDiv(){
		$("#message1").fadeTo(2000, 500).slideUp(500, function() {
			$("#message1").slideUp(500);
		});
	}
  
  function formatDate(date) {
    var day = String(date.getDate()).padStart(2, '0');
    var month = String(date.getMonth() + 1).padStart(2, '0');
    var year = date.getFullYear();
    return day + "-" + month + "-" + year;
  }
</script>
<div class="card-body"  style="background-color: #f1f1f1; border-radius: 3px; box-shadow: 2px 2px 4px 1px #8888884d;">
  <p class="h5">Recommended for Enforcement</p>
  <hr>
  <div class="col-12" style="text-align: center;">
    <img class="animation__shake" src="/static/image/under_development.png" alt="pdf" height="150" width="150"><br>
    <span style="font-size:35px;color:rgb(97, 97, 97)">This feature will be available shortly</span>
  </div>
</div>