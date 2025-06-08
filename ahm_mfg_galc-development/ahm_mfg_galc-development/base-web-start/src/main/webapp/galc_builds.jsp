<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="com.honda.galc.entity.conf.WebStartBuild" %>
<%@ page import="com.honda.galc.webstart.WebStartConstants" %>

<html>
<head>
<title>Builds</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="GENERATOR" content="Rational Software Architect">

<%
	List builds = (List)session.getAttribute(WebStartConstants.BUILDS);
	session.removeAttribute(WebStartConstants.BUILDS);
	
	if (builds == null) {
		response.sendRedirect(WebStartConstants.Action.LIST_BUILDS.action());
		return;
	}
 %>

<script language="javascript" src="scripts/date-picker.js"></script>
<script language="javascript">
  function checkForm() {
    return true;
  }
  
</script>
<meta http-equiv="Refresh" content="300;url=LIST_BUILDS.act"/>

</head>
<body>
<jsp:include flush="false" page="header.jsp"></jsp:include>

	<center>
	<br>
	<br>
	<h2>Builds</h2>
	<br>
	
			<table class="line" border='1' cellpadding="3"  width="90%" >
				<thead class="fixedHeader" id="fixedHeader">
					<tr>
						<th class='line'>Build ID</th>
						<th class='line'>Description</th>
						<th class='line'>Build Date</th>
			      		<th class='line'><span style="width: 50px">Edit</span></th>
			      		<th class='line'><span style="width: 50px">Delete</span></th>
					</tr>
				</thead>
				<tbody class="scrollContent">
					<%	Iterator iter = builds.iterator();
               			WebStartBuild aBuild = null;
	          			while(iter.hasNext()) { 
	            			aBuild = (WebStartBuild) iter.next();
	            			if(aBuild.getBuildId().length() > 0 && aBuild.getDescription().length() > 0) { 
	        		%>
						<tr>
							<td>&nbsp;<%=aBuild.getBuildId()%></td>
							<td>&nbsp;<%=aBuild.getDescription()%></td>
							<td align="center">&nbsp;<%= aBuild.getBuildDate().toString() %></td>
				      		<td align="center">
			      				<a href="<%=WebStartConstants.Action.EDIT_BUILD.action()%>?<%=WebStartConstants.BUILD_ID%>=<%=aBuild.getBuildId()%>"><img src="pics/edit.gif" border="0" title="Edit"></a>
							</td>
							<td align="center">
								<a href="<%=WebStartConstants.Action.DELETE_BUILD.action()%>?<%=WebStartConstants.BUILD_ID%>=<%=aBuild.getBuildId()%>"><img src="pics/delete.gif" border="0" title="Delete"></a>
							</td>
							
						</tr>
					<%
							}
	          			} 
	        		%>
				</tbody>
			</table>
	</center>
</body>
</html>
