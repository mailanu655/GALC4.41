
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet" type="text/css">
<TITLE>User Config</TITLE>
</HEAD>
<FRAMESET cols="30%,70%" frameborder="yes" border="2" class="settingspage">
	<html:frame frameName="lDAPListFrame" action="/lDAPListFrame"></html:frame>
	<html:frame frameName="lDAPSettingsFrame" action="/lDAPSettings"></html:frame>	
</FRAMESET>

</html:html>
