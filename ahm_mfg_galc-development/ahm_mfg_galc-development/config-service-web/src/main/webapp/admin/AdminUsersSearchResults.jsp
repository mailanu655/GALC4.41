
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="false" %>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>	
<%@ page import="com.honda.galc.system.config.web.forms.AdminUserSearchForm" %>
<%@ page import="com.honda.galc.entity.conf.*" %>
<%@ page import="java.util.*" %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>AdminUsersSearchResults.jsp</TITLE>
<SCRIPT>

function displayUser(userid, rowid) {

newurl = "<%=request.getContextPath() %>/adminUserSettings.do?existingUser=true&userID="+userid;
   
   //parent.parent.frames['adminUserSettingsFrame'].location = newurl;
   parent.frames['settingsIFrame'].location = newurl;
   
   document.getElementById(rowid).style.backgroundColor = '#cccccc';
   
   var tbody = document.getElementById("usertablebody");
   
   var rows = tbody.getElementsByTagName("tr"); 
   
   for(i = 0; i < rows.length; i++){           
      
      
      if (rows[i].id != rowid)
      {
        rows[i].style.backgroundColor = "white";
      }
   }
   

}

</SCRIPT>
</HEAD>
<%
  AdminUserSearchForm searchForm = (AdminUserSearchForm)request.getAttribute("adminUserSearchForm");
  
  if (searchForm == null)
  {
     searchForm = new AdminUserSearchForm();
  }
 %>
<BODY class="settingspage">
<html:form action="/adminUsersSearch" >
	    <html:hidden property="userMask" />
	    <html:hidden property="find" value="find"/>
</html:form>
<table border="1">
<THEAD>
  <tr> 
    <th>User ID</th><th>Description </th>
  </tr>
</THEAD>
<tbody id="usertablebody">
<%
   List<AdminUser> users = searchForm.getAdminUsers();
   
   int idx=0;
   for(AdminUser userData : users) {
      String rowid = "userrow"+idx;
 %> 
<tr style="background-color: white" id="<%= rowid %>" onclick="displayUser('<%=userData.getUserId() %>','<%= rowid %>' )">
<td><%= userData.getUserId() %></td>
<td><%= userData.getUserDesc() %></td>
</tr>    
 <%
     idx++;
   }
 %>
 </tbody>
 </table>
</BODY>
</html:html>
