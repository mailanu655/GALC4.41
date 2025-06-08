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
<c:set var="propLink"
	value="/ConfigService/propertySettings.do?loadComponent=Load&loadComponentID=" />
<c:set var="propTarget" value='target="PropertiesWindow"' />
<title>OIF - Schedule</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body background="${pageContext.request.contextPath}/pics/background.jpg">
<div align="center">
<h2>OIF Schedule</h2>
<table border="0" bordercolor="black" cellpadding="10" cellspacing="0">
	<tbody>
		<tr>
			<th><a href="./reschedule_events.do">Reschedule OIF Events</a></th>
		</tr>
		<tr>
			<th><a <c:out value="${propTarget}"/>
				href='<c:out value="${propLink}"/>OIF_DISTRIBUTION_SCHEDULE'>Configure OIF Distribution 
			Schedule</a></th>
		</tr>
		<tr>
			<th><a href="./event_status.do">Events Status</a></th>
		</tr>
	</tbody>
</table>
<h4><a href="index.do">Back</a></h4>
</div>
</body>
</html:html>
