<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>
<html:html>
<head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/galc.css"/>
	<title>Reschedule Status</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="GENERATOR" content="Rational Software Architect">
</head>
<body>
<div align="center">
	<h1>Reschedule is completed</h1>

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
            <li style="color:#EE0000;font-weight:bold"><bean:write name="msg" /></li>
         </html:messages>
      </ul>
   	</logic:messagesPresent>
   
	<p><a style="font-style:bold;" href="./schedule.do">Back to OIF Schedule</a></p>
	<p><a style="font-style:bold;" href="./">Back to Main</a></p>
</div>
</body>
</html:html>
