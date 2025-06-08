<HTML>
<HEAD>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.honda.galc.entity.conf.WebStartBuild" %>
<%@ page import="com.honda.galc.webstart.WebStartConstants" %>

<%
	WebStartBuild aBuild = (WebStartBuild) session.getAttribute(WebStartConstants.WEBSTART_BUILD);
	if (aBuild == null) {
		response.sendRedirect(WebStartConstants.Action.LIST_BUILDS.action());
		return;		
	}
 %>

</HEAD>

<body>
<jsp:include flush="true" page="header.jsp"></jsp:include>

	<form id="Form" action="<%=WebStartConstants.Action.DELETE_BUILD.action()%>" method="Post">
	<input type="hidden" name="build_id" value="<%=aBuild.getBuildId()%>">

	<center>
	<br>
	<br>
	<h2>Please confirm to delete following GALC build</h2>
	
	<table width="90%" border="0" cellpadding="5">
		<tbody>
		<tr>
			<td>
			<table border="0" cellpadding="10" class="edit">
				<tr>
					<th align="right">Build ID:</th>
					<td><%=aBuild.getBuildId()%></td>
				</tr>

				<tr>
					<th align="right">Description:</th>
					<td><%=aBuild.getDescription()%></td>
				</tr>

				<tr>
					<th align="right">JAR URL:</th>
					<td><%=aBuild.getUrl()%></td>
				</tr>

				<tr>
					<th valign="bottom" align="right">Build Date:</th>
					<td><%=aBuild.getBuildDate().toString()%></td>
				</tr>

			</table>
			</td>
		</tr>
		<tr>
			<td>
				<br>
				<input type="submit" value="Confirm Delete" class="smalltext">
				<input type="button" value="Cancel" class="smalltext" onclick="history.back();">
			</td>
		</tr>

		</tbody>
	</table>
	</center>
</form>
</body>
</HTML>