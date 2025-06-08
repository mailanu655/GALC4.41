<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/galc.css" />
<c:set var="propLink"
	value="/ConfigService/propertySettings.do?loadComponent=Load&loadComponentID=" />
<c:set var="propTarget" value='target="PropertiesWindow"' />
<title>OIF - Main</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="GENERATOR" content="Rational Software Architect">
</head>
<body background="${pageContext.request.contextPath}/pics/background.jpg";>
	<div align="center">
	<logic:messagesPresent message="true">
		<ul id="messages">
			<html:messages id="msg" message="true">
				<li><bean:write name="msg" /></li>
			</html:messages>
		</ul>
	</logic:messagesPresent> 
	<logic:messagesPresent>
		<ul id="errors">
			<html:messages id="msg">
				<li style="color: #EE0000; font-weight: bold"><bean:write name="msg" /></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>
	<h2 align="center">Other GALC Interfaces</h2>
	<h3>Access to OIF is disabled.</h3><br/>
	<table border="0" bordercolor="black" cellpadding="10" cellspacing="0">
		<tbody>
			<tr>
				<th><a <c:out value="${propTarget}"/>
					href='<c:out value="${propLink}"/>EVENT_SCHEDULE'>Configure OIF Schedule</a>
				</th>
			</tr>
			<tr><th>
				<br/><form name="logout" action="ibm_security_logout" method="post">
					<input type="hidden" name="logoutExitPage" value="login.html"> 
					<input type="submit" name="logout" value="Logout">
				</form>
			</th></tr>
		</tbody>
	</table>
		
	</div>
</body>
</html>
