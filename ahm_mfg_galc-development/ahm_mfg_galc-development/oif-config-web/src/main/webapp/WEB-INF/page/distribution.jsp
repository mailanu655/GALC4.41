<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html:html>
<head>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/galc.css" />
<title>OIF - Main</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="GENERATOR" content="Rational Software Architect">
</head>
<body background="${pageContext.request.contextPath}/pics/background.jpg";>
<h2 align="center">Other GALC Interfaces - Distribution</h2>

<div align="center">
<table border="0" bordercolor="black" cellpadding="10" cellspacing="0">
	<tbody>
		<tr>
			<th><a href="distribution_configuration.do">Distribution Configuration</a></th>
		</tr>
		<tr>
			<th><a href="distribution_schedule.do">Distribution Schedule</a></th>
		</tr>
		<tr>
			<th><a href="start_req_distribution.do">Start Distribution Manually</a></th>
		</tr>
	</tbody>
</table>
<h4><a href="index.do">Back</a></h4>
</div>

</body>
</html:html>
