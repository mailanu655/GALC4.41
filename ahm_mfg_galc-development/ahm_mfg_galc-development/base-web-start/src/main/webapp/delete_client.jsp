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
 
</HEAD>

<body>
<jsp:include flush="true" page="header.jsp"></jsp:include>

	<center>
	<br>
	<br>
	<h2>GALC Web Start Client: <%=client.getHostName()%> </h2>
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
					<form id="Form" action="<%=WebStartConstants.Action.DELETE_CLIENT.action()%>" method="post">
					<input type="hidden" name="<%=WebStartConstants.IP_ADDRESS%>" value="<%=client.getIpAddress()%>">
			    	<input type="submit" value="Confirm Delete" class="smalltext">
			    	<input type="button" value="Cancel" class="smalltext" onclick="history.back();">
				</td>
				<td>
				</td>
			</tr>
		</TBODY>
	</TABLE>
	</center>
</body>
</HTML>