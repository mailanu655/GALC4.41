<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<HTML>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="../theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>GALC Configurator</TITLE>
</HEAD>

<FRAMESET frameborder="yes" border="2" cols="20%,80%" class="settingspage">
	<html:frame page="/buildProcessTree.do" frameName="treepane"></html:frame>
	<html:frame action="/getSiteInformation" frameName="editpane"></html:frame>
</FRAMESET>
<body>
</BODY>
</HTML>
