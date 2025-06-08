
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.entity.conf.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE></TITLE>
</HEAD>
<%
   String divID = (String)request.getAttribute("divID");
   String appId = (String)request.getAttribute("appId");
   List<Terminal> terminalList = (List<Terminal>)request.getAttribute("terminalList");
   
   boolean showAll = false;
   if (divID == null) divID = "<null>";
   
   if (divID.equals("*")) showAll = true;
   if (terminalList == null) terminalList = new ArrayList<Terminal>(0);
   
 %>
<BODY class="listpage">
<% if (showAll) { %>
    <h1 class="listheader">All Terminals</h1>
<% } else if (StringUtils.isNotBlank(appId)){ %>
    <h1 class="listheader">Terminals associated with application : <%= appId %></h1>
    <h6><html:link action="/terminalList">[Show all terminals]</html:link></h6>
<% } else { %>
    <h1 class="listheader">Terminal for <%= divID %></h1>
    <h6><html:link action="/terminalList">[Show all terminals]</html:link></h6>
<% } %>
<table class="listtable" border="1">
<THEAD class="listtablehead">
<TR>
<% if (showAll || StringUtils.isNotBlank(appId) ) { %>
<th>Division ID</th>
<th>IP Address</th>
<th>Port</th>
<% } %>
<th>Host Name</th>
<th>Terminal Description</th>
</TR>
</THEAD>
<tbody>
<%
    int count = 0;
    String lineclass = "oddrow";
    
    for(Terminal terminal : terminalList) {

       count++;
       
       if (count % 2 == 0)
       {
          lineclass = "evenrow";
       }
       else
       {
          lineclass = "oddrow";
       }
       
       
       request.setAttribute("hostName", terminal.getHostName());
       
       String description = "&nbsp;";
       
       if (terminal.getTerminalDescription() != null && terminal.getTerminalDescription().length() > 0)
       {
          description = terminal.getTerminalDescription();
       }
    	
 %>
 <tr class="<%= lineclass %>">
 <%
  if (showAll || StringUtils.isNotBlank(appId)) {
  %>
<td><%= terminal.getDivisionId() %></td>
<td><%= terminal.getIpAddress() %></td>
<td><%= terminal.getPort() %></td>
  
  <% } %>
<td><html:link action="/terminalSettings" paramId="hostName" paramName="hostName" scope="request" styleClass="settingspagelink"><%= terminal.getHostName() %></html:link></td>  
<td><%= description %></td>  
</tr> 
 <% 
    }
 %>

</tbody>
</table>
</BODY>
</html:html>
