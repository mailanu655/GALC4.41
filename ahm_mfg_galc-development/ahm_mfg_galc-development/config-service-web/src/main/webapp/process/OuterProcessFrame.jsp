<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<HTML>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="true" %>	
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>	
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
	<script type="text/javascript" src="js/jquery-1.11.2.min.js"></script>
<%
   String assemblyLineID =   (String)session.getAttribute("ASSEMBLY_LINE_ID");
   if (assemblyLineID == null)
   {
      assemblyLineID = "Unknown";
   }
 %>
<TITLE>GALC Configurator for System: <%= assemblyLineID %></TITLE>
</HEAD>
<FRAMESET class="settingspage" border="1" rows="30,*" class="settingspage" bordercolor="black" framespacing="0" >
	<frame class="settingspage" src="process/NavigationLinks.jsp" name="navpane" marginheight="0" marginwidth="0"/>
	<frame class= "settingspage" src="process/ProcessFrame.jsp" name="contentpane" ></frame>
</FRAMESET>
<body>
</BODY>
</HTML>
