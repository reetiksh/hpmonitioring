<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Notification Example</title>
 <style>
        .notification {
            background-color: #f2f2f2;
            color: #333;
            text-align: center;
            padding: 10px;
        }
    </style>
</head>
<body>
	<div class="notification">
        <%-- Display notification message here --%>
        <%= request.getAttribute("notificationMessage") %>
    </div>
</body>
</html>