<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.entity.conf.WebStartBuild" %>
<%@ page import="com.honda.galc.webstart.WebStartConstants" %>

<html>
<head>
<title>create_new_build</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="GENERATOR" content="Rational Software Architect">

<%
	WebStartBuild aBuild = (WebStartBuild) session.getAttribute(WebStartConstants.WEBSTART_BUILD);
	if (aBuild == null) {
		response.sendRedirect(WebStartConstants.Action.LIST_BUILDS.action());
		return;
	}
%>

<script language="javascript" src="scripts/date-picker.js"></script>
<script language="javascript">
  function checkForm() {
    if (document.getElementById('jar_rul').value == "") {
        alert('Please enter a URL for the build.');
        return false;
    }
    if (document.getElementById('build_date').value == "") {
        alert('Please enter a date for the build.');
        return false;
    }
    return true;
  }
  
</script>

</head>

<body>
	<jsp:include flush="true" page="header.jsp"></jsp:include>

	<form id="Form" action="<%=WebStartConstants.Action.UPDATE_BUILD.action()%>" method="Post" onSubmit="return checkForm()">
	<input type="hidden" name="<%=WebStartConstants.BUILD_ID%>" value="<%=aBuild.getBuildId()%>">

	<center>
	<br>
	<br>
	<h2>GALC Build</h2>
	
	<table width="90%" border="0" cellpadding="5">
		<tbody>
		<tr>
			<td>
			<table border="0" cellpadding="10" class="edit">
			<br>
				<tr>
					<th align="right">Build ID:</th>
					<td><%=aBuild.getBuildId()%></td>
				</tr>

				<tr>
					<th align="right">Description:</th>
					<td><input type="text" name="<%=WebStartConstants.BUILD_DESCRIPTION%>" 
								size="60" maxlength="60" value="<%=aBuild.getDescription()%>"></td>
				</tr>

				<tr>
					<th align="right">JAR URL:</th>
					<td><input type="text" name="<%=WebStartConstants.JAR_URL%>" 
								size="120" maxlength="120" value="<%=aBuild.getUrl()%>"></td>
				</tr>

				<tr>
					<th valign="bottom" align="right">Build Date:</th>
					<td><input type="text" name="<%=WebStartConstants.BUILD_DATE%>" size="10" maxlength="10" 
								value="<%=aBuild.getBuildDate().toString()%>">
					<a href="javascript:show_calendar('forms[0].build_date')">
							<img src="pics/calendar.gif" width="24" height="24" border="0"></a></td>
				</tr>

			</table>
			</td>
		</tr>
		<tr>
			<td>
				<br>
				<input type="submit" value="Save" class="smalltext">
				<input type="button" value="Cancel" class="smalltext" onclick="history.back();">
			</td>
		</tr>

		</tbody>
	</table>
	</center>
</form>
</body>
</html>
