
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
<%
  request.setAttribute("initializePage",new Boolean(true));
 %>
</HEAD>
<FRAMESET cols="30%,70%" frameborder="yes" border="2" class="settingspage" framespacing="" >
	<html:frame marginheight="2" marginwidth="2"  action="/applicationSearch" paramId="initializePage" paramScope="request" paramName="initializePage"></html:frame>
	<html:frame marginheight="2" marginwidth="2"  frameName="appSettingsFrame" action="/applicationSettings" paramId="initializePage" paramScope="request" paramName="initializePage"></html:frame>	
</FRAMESET>

</html:html>
