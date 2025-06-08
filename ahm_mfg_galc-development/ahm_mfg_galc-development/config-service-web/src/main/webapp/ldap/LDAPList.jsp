
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.entity.conf.*" %>
<%@ page import="java.util.*" %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE></TITLE>
<script language="javascript">
function displayApplication(aTr) {

   aTr.style.backgroundColor = '#cccccc';
   
   var tbody = document.getElementById("userListBody");
   
   var rows = tbody.getElementsByTagName("tr"); 
   for(i = 0; i < rows.length; i++){           
      if (rows[i] != aTr)
      {
        rows[i].style.backgroundColor = "white";
      }
   }
   
   var userCell = aTr.getElementsByTagName("td").item(0);
   var userId = userCell.firstChild.data;
   newurl = '<%=request.getContextPath()%>/lDAPSettings.do?operation=query&userID=' + userId;
   top.frames['lDAPSettingsFrame'].location = newurl;
}

function highlightSelectedUser() {
	var detailFrame = top.frames['lDAPSettingsFrame'];
	
	if (detailFrame.document.all('userID') != null) {
	
       var selectUser = detailFrame.document.all('userID').value;
	   var tbody = document.getElementById("userListBody");
	   var rows = tbody.getElementsByTagName("tr"); 
	   for(i = 0; i < rows.length; i++){           
	      if (rows[i].childNodes[0].innerText == selectUser)
	      {
	        rows[i].style.backgroundColor = '#cccccc';
	      }
	   }
   }
}
</script>
</HEAD>

<BODY onload="highlightSelectedUser()">
<table border="1" width="100%">
	<THEAD>
		<TR>
			<TH>User ID</TH>
			<TH>User Name</TH>
		</TR>
	</THEAD>
	<tbody id="userListBody">
		<%
		
		List<User> users = (List<User>)request.getAttribute("lDAPList");
		
		for(User user : users){

		%>
				<tr style="background-color: white" onclick="displayApplication(this);" >
					<td><%=user.getUserId() %></td>
					<td><%=user.getUserName() %></td>
				</tr>
		<%
		}
		%> 
	</tbody>
</table>
</BODY>
</html:html>
