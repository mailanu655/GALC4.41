
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
</HEAD>

<%
   String divID = (String)request.getAttribute("divID");
   String processPointID = (String)request.getAttribute("processPointID");
   List<Device> deviceList = (List<Device>)request.getAttribute("deviceList");
   
   boolean showAll = false;
   boolean showPP = false;
   if (divID == null)
   {
       divID = "<null>";
   }
   
   if (divID.equals("*"))
   {
      showAll = true;
   }
   if (deviceList == null)
   {
      deviceList = new ArrayList<Device>(0);
   }
   
   if (processPointID != null && processPointID.length() > 0) {
      showPP = true;
   }
   
 %>
<BODY class="listpage">
<% if (showPP) { %>
<h1 class="listheader">Devices that reference Process Point <%= processPointID %></h1>

<% } else if (showAll) { %>
<h1 class="listheader">All Devices</h1>
<% } else { %>
<h1 class="listheader">Device for <%= divID %></h1>
<h6><html:link action="/deviceList">[Show all devices]</html:link></h6>
<% } %>
<table class="listtable" border="1">
<THEAD class="listtablehead">
<TR>
<% if (showPP) { %>
<th>DIV</th>
<% } %>
<th>Client ID</th>
<% if (showAll) { %>
<th>Reply Client ID</th>
<th>EIF IP Address</th>
<th>EIF Port</th>
<% } %>
<th>Device Type</th>
<th>Device Description</th>
</TR>
</THEAD>
<tbody>
<%
    int count = 0;
    String lineclass = "oddrow";
    for(Device device : deviceList) {
       count++;
       if (count % 2 == 0) lineclass = "evenrow";
       else lineclass = "oddrow";
       
       request.setAttribute("clientID", device.getClientId());
       
       String description = "&nbsp;";
       
       if (device.getDeviceDescription() != null && device.getDeviceDescription().length() >0)
         description = device.getDeviceDescription();
    	
 %>
 <tr class="<%= lineclass %>">
 <% if (showPP) { %>
<td><%= device.getDivisionId() %></td>
<% } %>
<td><html:link action="/deviceSettings" paramId="clientID" paramName="clientID" scope="request" styleClass="settingspagelink"><%= device.getClientId() %></html:link></td>  
 <%
  if (showAll) {
  %>
<td><%= (device.getReplyClientId() != null && device.getReplyClientId().length() >0) ? device.getReplyClientId() : "&nbsp;" %></td>
<td><%= device.getEifIpAddress() %></td>
<td><%= device.getEifPort() %></td>
  <% } %>
<td><%= (device.getDeviceType() != null) ? device.getDeviceType().toString() : "&nbsp;" %></td>
<td><%= description %></td>  
</tr> 
 <% 
    }
 %>

</tbody>
</table>
</BODY>
</html:html>
