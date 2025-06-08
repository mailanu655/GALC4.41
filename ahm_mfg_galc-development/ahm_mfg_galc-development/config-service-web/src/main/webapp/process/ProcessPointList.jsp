
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
   String lineID = (String)request.getAttribute("lineID");
   List<ProcessPoint> processPointList = (List<ProcessPoint>)request.getAttribute("processPointList");
   
   boolean showAll = false;
   if (lineID == null)
   {
       lineID = "<null>";
   }
   
   if (lineID.equals("*"))
   {
      showAll = true;
   }
   if (processPointList == null)
   {
      processPointList = new java.util.ArrayList(0);
   }
   
 %>
<BODY class="listpage">
<% if (showAll) { %>
<h1 class="listheader">All Process Points</h1>
<% } else { %>
<h1 class="listheader">Process Points for <%= lineID %></h1>
<h6><html:link action="/processPointList">[Show all process points]</html:link></h6>
<% } %>
<table class="listtable" border="1">
<THEAD class="listtablehead">
<TR>
<% if (showAll) { %>
<th>Plant Name</th>
<th>Division ID</th>
<th>Division Name</th>
<th>Line ID</th>
<th>Line Name</th>
<% } %>
<TH>Process Point ID</TH>
<th>Process Point Name</th>
<th>Process Point Description</th>
</TR>
</THEAD>
<tbody>
<%
    int count = 0;
    String lineclass = "oddrow";
    for(ProcessPoint data : processPointList) {
       count++;
       
       if (count % 2 == 0)
          lineclass = "evenrow";
       else
          lineclass = "oddrow";
       
       
       request.setAttribute("processPointID", data.getProcessPointId());
    
 %>
 <tr class="<%= lineclass %>">
 <%
  if (showAll) {
  %>
<td><%= data.getPlantName() %></td>
<td><%= data.getDivisionId() %></td>
<td><%= data.getDivisionName() %></td>
<td><%= data.getLineId() %></td>
<td><%= data.getLineName() %></td>
  
  <% } %>
<td><html:link action="/processPointSettings" paramId="processPointID" paramName="processPointID" scope="request" styleClass="settingspagelink"><%= data.getProcessPointId() %></html:link></td>  
<td><%= data.getProcessPointName() %></td>  
<td><%= data.getProcessPointDescription() %></td>  
</tr> 
 <% 
    }
 %>

</tbody>
</table>
</BODY>
</html:html>
