
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.AdminGroupSearchForm" %>
<%@ page import="com.honda.galc.entity.conf.*" %>
<%@ page import="java.util.*" %>
<%@ page session="false" %>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>Admin Groups SearchResults.jsp</TITLE>
<SCRIPT>

function displayGroup(groupid, rowid) {

newurl = "<%=request.getContextPath() %>/adminGroupSettings.do?existingGroup=true&groupID="+groupid;
   
   //parent.parent.frames['adminGroupSettingsFrame'].location = newurl;
   parent.frames['settingsIFrame'].location = newurl;
   
   document.getElementById(rowid).style.backgroundColor = '#cccccc';
   
   var tbody = document.getElementById("grouptablebody");
   
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
  AdminGroupSearchForm searchForm = (AdminGroupSearchForm)request.getAttribute("adminGroupSearchForm");
  
  if (searchForm == null)
  {
     searchForm = new AdminGroupSearchForm();
  }
 %>
<BODY class="settingspage">
<html:form action="/adminGroupsSearch" >
	    <html:hidden property="groupMask" />
	    <html:hidden property="find" value="find"/>
</html:form>
<table border="1">
<THEAD>
  <tr> 
    <th>Group ID</th><th>Description </th>
  </tr>
</THEAD>
<tbody id="grouptablebody">
<%
   List<AdminGroup>  groups = searchForm.getAdminGroups();
   
   int idx=0;
 	for(AdminGroup groupData : groups) {
      String rowid = "grouprow"+idx;
 %> 
<tr style="background-color: white" id="<%= rowid %>" onclick="displayGroup('<%=groupData.getGroupId() %>','<%= rowid %>' )">
<td><%= groupData.getGroupId() %></td>
<td><%= groupData.getGroupDesc() %></td>
</tr>    
 <%
     idx++;
   }
 %>
 </tbody>
 </table>
</BODY>
</html:html>
