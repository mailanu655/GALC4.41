<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html:html>
<head>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/galc.css" />
<c:set var="propLink"
	value="/ConfigService/propertySettings.do?loadComponent=Load&loadComponentID=" />
<c:set var="propTarget" value='target="PropertiesWindow"' />
<title>OIF - Configuration</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
<div align="center">
<h2>OIF Configuration</h2>
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

<table border="1" bordercolor="black" cellpadding="10" cellspacing="0">
	<tbody>
		<tr>
			<th>OIF Manual Distribution Start Menu</th>
			<td><a <c:out value="${propTarget}"/>
				href='<c:out value="${propLink}"/>OIF_START_DISTRIBUTION_MENU'><b>OIF_START_DISTRIBUTION_MENU</b></a></td>
		</tr>
		<c:if test="${!empty displayOifNotifications}">
			<tr>
				<th>OIF Notifications</th>
				<td><a <c:out value="${propTarget}"/>
					href='<c:out value="${propLink}"/>OIF_NOTIFICATION_PROPERTIES'><b>OIF_NOTIFICATION_PROPERTIES</b></a></td>
			</tr>
		</c:if>
	</tbody>
</table>
<BR/>
<table border="1" bordercolor="black" cellpadding="10" cellspacing="0">
	<tbody>
		<tr>
			<th>Interface</th>
			<th>Ditribution Parameters</th>
		</tr>
		<c:forEach var="oif" items="${oifConfigs}">
			<tr>
				<td><a <c:out value="${propTarget}"/>
					href='<c:out value="${propLink}"/><c:out value="${oif.interfaceName}"/>'>
				<b><c:out value="${oif.interfaceName}" /></b></a></td>
				<td><a <c:out value="${propTarget}"/>
					href='<c:out value="${propLink}"/><c:out value="${oif.distributionName}"/>'>
				<b><c:out value="${oif.distributionName}" /></b></a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<h4><a href="distribution.do">Back</a></h4>
<h4><a href="index.do">Main</a></h4>
</div>

</body>
</html:html>
