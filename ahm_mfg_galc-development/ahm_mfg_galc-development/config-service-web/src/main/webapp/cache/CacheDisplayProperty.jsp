<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page import="com.honda.galc.entity.conf.*" %>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
<TITLE>Application Settings</TITLE>

<script type="text/javascript">
	function boldText(sObjectID) {
	// USED TO BOLDEN THE TEXT OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		document.getElementById(sObjectID).style.background='#cccccc';
	}
	
	function normalText(sObjectID) {
	// USED TO BOLDEN THE TEXT OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		document.getElementById(sObjectID).style.background='#eeeeee';
	}
	
	function show(sObjectID) {
		// USED TO SHOW AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		
		document.getElementById(sObjectID).style.display='';
	}
	
	function hide(sObjectID) {
		
	// USED TO HIDE AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		document.getElementById(sObjectID).style.display='none';
	}
	
	function disableTab(index){
		normalText("tab"+index);
		hide("server"+index);
	}
	
	function enableTab(index){
		boldText("tab"+index);
		show("server"+index);
	}
	
	function clickTab(index) {
	   	var serverListTag = document.getElementById("serverTabs");
	   	var rows = serverListTag.getElementsByTagName("td");
	   	for(i =0; i<rows.length;i++){
	   		if(i == index) enableTab(i);
	   		else disableTab(i);
	   	}
	}
</script>
</HEAD>

<%
  
  Map<String,List<ComponentProperty>> propertyMap = (Map<String,List<ComponentProperty>>)request.getAttribute("COMPONENT_PROPERTY");

%>

<BODY class="settingspage" onload="clickTab(0)">
		<table border="1" cellpadding="4">
		<tr id="serverTabs">
		<%
			int index = 0;
			for(String item : propertyMap.keySet()) {
				String tabId = "tab"+index;
		%>
		<td id=<%=tabId%> class="activetab"><span  onclick="clickTab(<%=index%>);"><%=item%></span></td>
		<%
			 index++;
			 }
		%>
		</tr>
		</table>

	<%
			index =0;
			for(String serverIp : propertyMap.keySet()) {
				String serverId = "server"+index++;
	%>
	<DIV id=<%=serverId%> style="display: none;">
		<H2>Property Cache on server <%=serverIp %></H2> 
		<HR>
		<TABLE width="100%" border = "1" style=""border:1px solid black;">
			<TR style="display: none;">
				<TR style ="background-color: #cccccc">
					<TH>PROPERTY KEY</TH>
					<TH>PROPERTY VALUE</TH>
				</TR>
	
				<tbody>
				<%
					for(ComponentProperty property : propertyMap.get(serverIp)){
				 %>
					<TR>
						<TD>
							<%=property.getPropertyKey() %>
						</TD>
						<TD><%=property.getPropertyValue() %></TD>
					</TR>
				<%
					} 
				%>
				</tbody> 
			</TR>
		</TABLE>
	</DIV>
	<%
	  }
	%>
		
</BODY>
</html:html>
