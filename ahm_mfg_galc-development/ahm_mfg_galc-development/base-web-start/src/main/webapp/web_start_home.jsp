<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>Webstart Home</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="GENERATOR" content="Rational Software Architect">

<%@ page import="java.util.*"%>
<%@ page import="com.honda.galc.entity.conf.WebStartClient"%>
<%@ page import="com.honda.galc.entity.conf.WebStartBuild"%>
<%@ page import="com.honda.galc.webstart.WebStartConstants"%>

<%
	List clients = (List) session.getAttribute(WebStartConstants.CLIENTS);
	List builds = (List) session.getAttribute(WebStartConstants.BUILDS);
	String defaultBuildId = (String) session.getAttribute(WebStartConstants.BUILD_ID);
	String lineId = (String) session.getAttribute(WebStartConstants.LINE_ID);
	if(clients == null || builds == null || lineId == null) {
    	response.sendRedirect(WebStartConstants.Action.HOME.action()); 
    	return;
	}
	if(defaultBuildId == null) {
		defaultBuildId = "(Not Defined)";
	}
%>

<meta http-equiv="Refresh" content="6000;url=HOME.act" />
<meta http-equiv="cache-control" content="No-Cache" />
<jsp:include flush="true" page="header.jsp"></jsp:include>
</head>
<body>
<br>
<br>
<center>
<TABLE  width="860" border="0" cellpadding="0">
	<tr>
		<td align="left" width="50%">
		<h3>GALC Line: <%=lineId%></h3>
		</td>
		<td align="right" width="50%">
		<h3>Default Build: <%=defaultBuildId%></h3>
		</td>
	</tr>
</TABLE>
</center>
<br>
<br>
<center>

<table class="line" border="1" cellpadding="3" width="80%">
  <thead>
	 <tr>
			<th class='line' width="25%">Hostname</th>
			<th class='line' width="55%">Terminal Name</th>
			<th class='line' width="20%">Target Build</th>
	 </tr>
  </thead>
  <%
	Iterator iter1 = clients.iterator();
	WebStartClient client = null;
  %>
  <%
	while (iter1.hasNext()) {
	  client = (WebStartClient) iter1.next();
  %>
  <tr>
		<td width="25%"><%=client.getIpAddress()%></td>
		<td width="55%"><%=client.getHostName()%></td>
			<td width="20%">&nbsp;<%=client.getBuildId()%></td>
  </tr>
  <%
	}
  %>
</table>
<br>
<br>
<br>
<table class="line" border="1"  cellpadding="3" width="80%">
	<thead class="fixedHeader" id="fixedHeader">
	<tr>
		<th class='line' width="50%">Build ID</th>
		<th class='line' width="50%">Build Date</th>
	</tr>
</thead>
   <%
		Iterator iter2 = builds.iterator();
		WebStartBuild aBuild = null;
		while (iter2.hasNext()) {
			aBuild = (WebStartBuild) iter2.next();
	        if(aBuild.getBuildId().length() > 0 && aBuild.getDescription().length() > 0) { 
	%>
	<tr>
		<td width="50%">&nbsp;<%=aBuild.isDefaultBuild() ? "<b>" : ""%>
			  <%=aBuild.getBuildId()%> <%=aBuild.isDefaultBuild() ? "&nbsp;&nbsp;(Default)</b>" : ""%></td>
		<td width="50%" align="center">&nbsp;<%=aBuild.getBuildDate().toString()%></td>
    </tr>
	<%
			}
		}
	%>
</table>
</center>

</body>
</html>
