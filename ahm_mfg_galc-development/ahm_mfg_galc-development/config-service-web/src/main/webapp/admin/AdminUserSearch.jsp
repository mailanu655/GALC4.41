
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
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>AdminUserSearch.jsp</TITLE>

<script>
function resizeframe() {

  tr = document.getElementById("triframe");
  fr = document.getElementById("resultiframe");
  
  //alert(tr.offsetHeight);
  fr.style.height = tr.offsetHeight - 10;
}

function createUser() {
   newurl = "<%=request.getContextPath() %>/adminUserSettings.do?existingUser=false";
   
   parent.frames['adminUserSettingsFrame'].location = newurl;
}
</script>
</HEAD>
<%

 %>
<BODY class="settingspage">
<html:form target="findResults" action="/adminUsersSearch" scope="request" name="adminUserSearchForm" type="com.honda.galc.system.config.web.forms.AdminUserSearchForm">
	<html:hidden property="initializePage" value="false" />
	<TABLE border="0" height="100%">
		<TBODY>
			<TR>
				<TH class="settingstext" align="left">User </TH>
				<TD><html:text property="userMask" value="*" size="16" maxlength="16"/></TD>
			
				<TD><html:submit property="find" value="Find" /></TD>
			
			</TR>
			<tr id="triframe">
			<td colspan="3">
			  <IFRAME id="resultiframe" name="findResults"  width="100%" align="top" marginheight="0" marginwidth="0" onload="resizeframe();"></IFRAME>
			</td>
			</tr>
		</TBODY>
	</TABLE>
</html:form>
</BODY>
</html:html>
