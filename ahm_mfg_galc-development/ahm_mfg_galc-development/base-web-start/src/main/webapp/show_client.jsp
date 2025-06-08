<HTML>
<HEAD>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.honda.galc.entity.conf.WebStartClient" %>
<%@ page import="com.honda.galc.webstart.WebStartConstants" %>
<%
	WebStartClient client = (WebStartClient)session.getAttribute(WebStartConstants.WEBSTART_CLIENT);
	if(client == null) {
		response.sendRedirect(WebStartConstants.Action.LIST_CLIENTS.action());
		return;
	}

	String description = (client.getDescription() == null) ? "" : client.getDescription();
	String buildId = (client.getBuildId() == null) ? "" : client.getBuildId();
 %>
 
<LINK rel="stylesheet" href="theme/Table.css" type="text/css">

</HEAD>

<body>
<jsp:include flush="true" page="header.jsp"></jsp:include>

	<center>
	<br>
	<br>
	<h2>Webstart Client: <%=client.getHostName()%> </h2>
	<TABLE width="90%" border="0" cellpadding="5">
		<TBODY>
			<tr>
			      <th width="100" align="right">IP Address:</th>
			      &nbsp;<td> <%= client.getIpAddress()%></td>
			</tr>
			<tr>
			      <th width="100" align="right">Host Name:</th>
			      &nbsp;<td> <%= client.getHostName()%></td>
			</tr>
			<tr>
			      <th width="100" align="right">Description:</th>
			      &nbsp;<td> <%=description%></td>
			</tr>
			<tr>
			      <th width="100" align="right">Target Build:</th>
			      &nbsp;<td> <%=buildId%></td>
			</tr>
		</TBODY>
	</TABLE>
	
	<TABLE width="90%" border="0" cellpadding="5">
		<TBODY>
			<tr>
				<td>
					<br>
		      		<input type="button" value="Back" class="smalltext" onclick="history.back();">
				</td>
				<td>
				</td>
			</tr>
		</TBODY>
	</TABLE>
	</center>
</body>
</HTML>