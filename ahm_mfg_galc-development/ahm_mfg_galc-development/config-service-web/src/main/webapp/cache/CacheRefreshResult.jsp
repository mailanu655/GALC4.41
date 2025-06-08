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

<%
  
  String cacheName = (String)request.getAttribute("CACHE_NAME");
  String item = (String)request.getAttribute("ITEM");
  Map<String,String> messageMap = (Map<String,String>)request.getAttribute("MESSAGES");

%>

<BODY class="settingspage">
		<H2> Refresh "<%=cacheName%>" Cache, Item : "<%=item%>"</H2>
		<HR><BR>
		<TABLE border="1" cellpadding="4">
		<TR>
			<TH>SERVER IP</TH>
			<TH>REFRESH RESULT</TH>
		</TR>
		<%
			for(String key : messageMap.keySet()) {
				String msg = messageMap.get(key);
		%>
		<TR>
			<TD><%=key%> </TD>
			<TD><%=msg%> </TD>
		</TR>
		<%
			 }
		%>
		</TABLE>
</BODY>
</html:html>
