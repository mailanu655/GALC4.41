<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<HTML>
<HEAD>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>
<%@ page 
session="false"
language="java"
contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"
%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>Server Information</TITLE>
</HEAD>
<%

String errorMessage = (String)request.getAttribute("jmxstatus.errorMessage");
String resultMessage = (String)request.getAttribute("jmxstatus.resultMessage");
String serverName = (String)request.getAttribute("jmxstatus.serverName");
if (serverName == null)
{
  serverName = "unknown";
}
String nodeName = (String)request.getAttribute("jmxstatus.nodeName");
if (nodeName == null)
{
  nodeName = "unknown";
}

String cellName = (String)request.getAttribute("jmxstatus.cellName");
if (cellName == null)
{
  cellName = "unknown";
}

String processName = (String)request.getAttribute("jmxstatus.processName");
if (processName == null)
{
   processName = "unknown";
}


%>
<BODY>
<%
if (errorMessage != null && errorMessage.length() > 0)
{
%>
<H3>Error</H3>
<h4><%= errorMessage %></h4>
<%
}
%>
<%
if (resultMessage != null && resultMessage.length() > 0)
{
%>
<H3>Server Information</H3>
<h4><%= resultMessage %></h4>
<%
}
%>
<table>
<tr>
<td valign="top">
<TABLE border="1">
	<TBODY>
		<TR>
			<TH colspan="2">WebSphere Information</TH></TR>
				<TR>
					<TH>Property</TH><TH>Value</TH>
					
				</TR>
				<TR>
			<TD>Cell</TD>
			<TD><%= cellName %></TD>
		</TR>
		<TR>
			<TD>Node</TD>
			<TD><%= nodeName %></TD>
		</TR>
		<TR>
			<TD>Server</TD>
			<TD><%= serverName %></TD>
		</TR>
		<TR>
			<TD>Process</TD>
			<TD><%= processName %></TD>
		</TR>
	</TBODY>
</TABLE>
</td>

</tr>
</table>

<P><BR>
</P>

</BODY>
</HTML>
