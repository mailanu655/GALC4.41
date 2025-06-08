<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<HTML>
<HEAD>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE>GALC Web Start Error</TITLE>
</HEAD>
<BODY>
<jsp:include flush="true" page="header.jsp"></jsp:include>
	<br>
	<br>
	<center>
			<h2><%=session.getAttribute("ERROR")%></h2>
		<br>
		<br>
		<input type="button" value="Go Back" class="smalltext" onclick="history.back()">
	</center>
</BODY>
</HTML>