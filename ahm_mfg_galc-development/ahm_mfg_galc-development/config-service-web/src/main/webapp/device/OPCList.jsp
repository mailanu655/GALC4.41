<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page import="com.honda.galc.system.config.web.forms.OpcListForm" %>
<%@ page session="false" %>
<%@ page import="com.honda.galc.entity.conf.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.lang.String" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<title>OPC Configuration Entries List</title>
</head>
<%
  OpcListForm form = (OpcListForm)request.getAttribute("opcListForm");
  if (form == null)
  {
     form = new OpcListForm();
  }
  
  List<OpcConfigEntry> dataList = form.getOpcConfigurationDataList();
 %>
<body class="settingspage"> 
<H1 class="settingsheader">OPC Configuration Entries List</H1>
<html:form styleId="opcListForm" action="/opcConfigurationList"  name="opcListForm" type="com.honda.global.galc.system.config.web.forms.OpcListForm" scope="request">
<table>
<tbody>
<tr>
<td class="settingstext">OPC Instance name</td>
<td><html:select property="instanceName">
<%
  List<String> instanceList = form.getInstanceNames();
  for(String instanceName : instanceList) {
 %>
<option value="<%= instanceName %>"><%= instanceName %></option>
 <%
  }
  %>
</html:select>
</td>

<td><html:submit property="fetchList" value="Fetch"></html:submit>
</td>
</tr>
</tbody>
</table>
</html:form>


<html:form action="/opcSettings">
<html:hidden property="newInstance" value="true"/>
<html:hidden property="load" value="load"/>
<html:hidden property="id" value="-1"/>
<table>
<tbody>
<tr>
<td><html:submit value="Create entry for new OPC Instance"></html:submit></td>
</tr>
</tbody>
</table>  
</html:form>

<%
 if (form.getInstanceName() != null && form.getInstanceName().length() > 0)
 {
 %>
<html:form action="/opcSettings">
<html:hidden property="newInstance" value="true"/>
<html:hidden property="load" value="load"/>
<html:hidden property="id" value="-1"/>
<html:hidden property="opcInstanceName" value="<%= form.getInstanceName() %>"/>
<table>
<tbody>
<tr>
<td><input type="submit" value="Create entry for instance <%= form.getInstanceName() %>"></td>
</tr>
</tbody>
</table>  
</html:form> 
<%} %>

<%
 if (dataList != null && dataList.size() > 0)
 {
 %>
 <table border="1">
 <thead>
 <tr>
   <td>&nbsp;</td>
   <td>Instance</td>
   <td>Data Ready Tag</td>
   <td>Device ID</td>
   <td>Process Point ID</td>
   <td>Enabled</td>
 </tr>
 </thead>
 <tbody>
     <% 
       
       int count = 0;
       String lineclass = "oddrow";
       for(OpcConfigEntry data : dataList) {
          count++;
       	  lineclass = (count % 2 == 0)? "evenrow" : "oddrow";
           
          String enabledValue = "Yes";
          
          if (data.getEnabled() <= 0) enabledValue = "No";
      %>
      <tr class="<%= lineclass %>">
         <td>
         <html:form action="/opcSettings">
		 <html:hidden property="newInstance" value="false"/>
		 <html:hidden property="load" value="load"/>
		 <input type="hidden" name="id" value="<%= Long.toString(data.getId()) %>" />
		 <html:submit value="Edit"></html:submit>
		 </html:form> 
         </td>
		 <td><%= data.getOpcInstanceName() %></td>
		 <td><%= data.getDataReadyTag() %></td>
		 <td><%= data.getDeviceId() %></td>
		 <td><%= data.getDeviceId() %></td>
		 <td><%= enabledValue %></td>
      </tr>
      <%
       }
       %>
 </tbody>
 </table>
<%} %>
</body>
</html>