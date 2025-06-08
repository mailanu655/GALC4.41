
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.SecurityGroupSearchForm" %>
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
<TITLE>Security Groups SearchResults</TITLE>
<SCRIPT>

function displayGroup(groupid, rowid) {

newurl = "<%=request.getContextPath() %>/securityGroupSettings.do?existingGroup=true&groupID="+groupid;
   
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
  SecurityGroupSearchForm searchForm = (SecurityGroupSearchForm)request.getAttribute("securityGroupSearchForm");
  
  if (searchForm == null)
  {
     searchForm = new SecurityGroupSearchForm();
  }
 %>
<BODY class="settingspage">
<html:form action="/securityGroupsSearch" >
	    <html:hidden property="groupMask" />
	    <html:hidden property="find" value="find"/>
</html:form>
<table border="1">
<THEAD>
  <tr> 
    <th>Group ID</th><th>Group Name</th><th>Description </th>
  </tr>
</THEAD>
<tbody id="grouptablebody">
<%
if(searchForm.getSecurityGroups() != null) {
   List<SecurityGroup> groups = searchForm.getSecurityGroups();
   int idx=0;
   for(SecurityGroup groupData : groups) {
      String rowid = "grouprow"+idx;
 %> 
<tr style="background-color: white" id="<%= rowid %>" onclick="displayGroup('<%=groupData.getSecurityGroup() %>','<%= rowid %>' )">
<td><%= groupData.getSecurityGroup() %></td>
<td><%= groupData.getGroupName() %></td>
<td><%= groupData.getDisplayText() %></td>
</tr>    
 <%
     idx++;
   }
 }
 %>
 </tbody>
 </table>
</BODY>
</html:html>
