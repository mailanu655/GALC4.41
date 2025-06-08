
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
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
<TITLE>Administrative Group Management</TITLE>

<script>
function resizeframe() {

  tr = document.getElementById("triframe");
  fr = document.getElementById("resultiframe");
  
  //alert(tr.offsetHeight);
  fr.style.height = tr.offsetHeight - 10;
}

function resizeSettingsFrame() {

  tr = document.getElementById("trouter");
  fr = document.getElementById("settingsiframe");
  
  //alert(tr.offsetHeight);
  fr.style.height = tr.offsetHeight - 10;
}

function createGroup() {
   newurl = "<%=request.getContextPath() %>/adminGroupSettings.do?existingGroup=false";
   
   window.frames['settingsIFrame'].location = newurl;
}
</script>
</HEAD>
<%

 %>
<BODY class="settingspage">
<h1 class="settingsheader">Administrative Groups Settings</h1>
<table width="100%" height="100%">
<tbody>
<tr id="trouter">
<td>
<html:form target="findResults" action="/adminGroupsSearch" scope="request" 
	name="adminGroupSearchForm" style="height: 100%;"
	type="com.honda.galc.system.config.web.forms.AdminGroupSearchForm">
	<html:hidden property="initializePage" value="false" />
	<TABLE border="0" height="100%">
		<TBODY>
			<TR>
				<TH class="settingstext" align="left">Group </TH>
				<TD><html:text property="groupMask" value="*" size="16" maxlength="16"/></TD>
			
				<TD><html:submit property="find" value="Find" /></td>
				<td><button onclick="createGroup(); return false;">New&nbsp;Group</button></TD>
				
			
			</TR>
			<tr id="triframe">
			<td colspan="4">
			  <IFRAME id="resultiframe" name="findResults"  width="100%" align="top" marginheight="0" marginwidth="0" onload="resizeframe();"></IFRAME>
			</td>
			</tr>
		</TBODY>
	</TABLE>
</html:form>
</td>
<td width="75%">
<IFRAME id="settingsiframe" src="<c:url value="/common/Blank.jsp"></c:url>" frameborder="0" name="settingsIFrame"  width="100%" align="top" marginheight="0" marginwidth="0" onload="resizeSettingsFrame();" ></IFRAME>
</td>
</tr>
</tbody>
</table>
</BODY>
</html:html>
