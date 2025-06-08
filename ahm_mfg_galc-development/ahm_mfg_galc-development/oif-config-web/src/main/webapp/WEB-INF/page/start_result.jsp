<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<html:html>
<head>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/galc.css" />
<title>OIF Started</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body background="${pageContext.request.contextPath}/pics/background.jpg">
<h2>OIF Started</h2>
<p><a href="./start_req.do">Back</a></p>
<html:messages id="msg" message="true">
	<b><bean:write name="msg" /></b>
</html:messages>
<html:errors />
</body>
</html:html>
