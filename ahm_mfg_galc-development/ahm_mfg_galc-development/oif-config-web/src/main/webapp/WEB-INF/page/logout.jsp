<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/galc.css" />
<title>OIF - Main</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="GENERATOR" content="Rational Software Architect">
</head>
<body background="${pageContext.request.contextPath}/pics/background.jpg";>
<h2 align="center">Other GALC Interfaces</h2>

<div align="center"><br>
<br>
<br>
<h3>Press Logout button to continue</h3>
<br>
<center>
<form name="logout" action="ibm_security_logout" method="post"><input
	type="hidden" name="logoutExitPage" value="login.html"> <input
	type="submit" name="logout" value="Logout"> <input
	type="button" value="Cancel" onclick="history.back();"></form>
</center>
</div>

</body>
</html>
