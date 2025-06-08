<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.webstart.WebStartConstants" %>
<html>
<head>
<title>Webstart</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="GENERATOR" content="Rational Software Architect">

<%
	StringBuilder serverurl = new StringBuilder();
	serverurl.append(request.isSecure() ? "https://" : "http://");
	serverurl.append(request.getServerName());
	if (request.getServerPort() != (request.isSecure() ? 443 : 80)) {
    	serverurl.append(':');
    	serverurl.append(request.getServerPort());
	}

	String client = request.getRemoteHost();
  
	if (client == null || client.equals(request.getRemoteAddr())) {
		client = WebStartConstants.HOST_NAME;
	}
	serverurl.append("/BaseWebStart/webstart.jnlp");
%>
  <LINK rel="stylesheet" href="css/default.css" type="text/css">
</head>
<body>
<jsp:include flush="true" page="header.jsp"></jsp:include>

	<center>
	<br>
	<img border="0" src="pics/galcwebstart.jpg"><br><br>
	<br>
	<table width=90% cellpadding=0>
		<tr align="center">
    		<td>
    			<a href="<%=serverurl%>"
					onmouseover="document.images['myBut'].src='pics/startButOver.gif'"
					onmouseout="document.images['myBut'].src='pics/startBut.gif'"
					onmousedown="document.images['myBut'].src='pics/startButDown.gif'"
					onmouseup="document.images['myBut'].src='pics/startButOver.gif'"><img
					name="myBut" src="pics/startBut.gif" border="0"></a>
			</td>
		</tr>
	</table>
	</center>

</body>
</html>
