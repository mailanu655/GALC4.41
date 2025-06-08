
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.ApplicationSearchForm"  %>
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
<TITLE>ApplicationSearchResults.jsp</TITLE>

<script>
function displayApplication(applicationID, rowid) {

   newurl = "<%=request.getContextPath() %>/applicationSettings.do?applicationID="+applicationID;
   
   top.frames['appSettingsFrame'].location = newurl;
   
   document.getElementById(rowid).style.backgroundColor = '#cccccc';
   
   var tbody = document.getElementById("appstablebody");
   
   var rows = tbody.getElementsByTagName("tr"); 
   
   for(i = 0; i < rows.length; i++){           
      
      
      if (rows[i].id != rowid)
      {
        rows[i].style.backgroundColor = "white";
      }
   }
   
   
   
   
   

}

</script>

<%
ApplicationSearchForm form = (ApplicationSearchForm)request.getAttribute("applicationSearchForm");
List<Application> applicationList = null;

if (form != null)
{
  applicationList = form.getApplicationList();
}

 %>
</HEAD>

<BODY>
<html:form action="/applicationSearch" name="applicationSearchForm" type="com.honda.global.galc.system.config.web.forms.ApplicationSearchForm" scope="request">
	<html:hidden property="applicationType" />
</html:form>
<%
  if (applicationList != null && applicationList.size() > 0) {
%>
<table border="1">
<THEAD>
  <tr>
    <th>Application ID</th>
    <th>Name</th>
  </tr>
</THEAD>

<tbody id="appstablebody">
<%
     int idx = 0;
     for(Application data : applicationList) {
        idx++;
        String rowid = "approw"+idx;
%>
<tr style="background-color: white" id="<%= rowid %>" onclick="displayApplication('<%=data.getApplicationId() %>','<%= rowid %>' );">
  
  <td><%= data.getApplicationId() %></td>
  <td><%= data.getApplicationName() == null ? "&nbsp;" : data.getApplicationName() %></td>
  
</tr>
<%
    
     }
     
%>
</tbody>
</table>
<%
  }
 %>
<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
   <bean:write name="msg"/>
</html:messages>
<html:errors />
</BODY>
</html:html>
