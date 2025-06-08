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
<TITLE>Cell Name</TITLE>
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
<%= cellName %>
</BODY>
</HTML>
