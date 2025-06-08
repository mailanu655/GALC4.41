<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>logout</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="GENERATOR" content="Rational Software Architect">
</head>
<body>
<jsp:include flush="true" page="header.jsp"></jsp:include>
	<br><br><br>
	<center>
	<form name="logout" action="ibm_security_logout" method="post">
		<input type="hidden" name="logoutExitPage" value="login.html">
		<input type="submit" name="logout" value="Logout">
		<input type="button" value="Cancel" onclick="history.back();">
	</form> 
	</center>
</body>
</html>
