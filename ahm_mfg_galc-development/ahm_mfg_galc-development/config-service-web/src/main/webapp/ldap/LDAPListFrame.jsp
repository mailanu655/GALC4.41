
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
<TITLE></TITLE>
<script language="javascript">
function resizeframe() {

  tr = document.getElementById("triframe");
  fr = document.getElementById("userListIFrame");
  
  //alert(tr.offsetHeight);
  fr.style.height = tr.offsetHeight - 10;
}
</script>
</HEAD>
<BODY class="settingspage">
<html:form action="/lDAPSettings" target="lDAPSettingsFrame">
	<TABLE height="100%" border="0">
		<TBODY>
			<TR id="triframe"> 
				<td valign="top">
					<IFRAME src="<%=request.getContextPath()%>/lDAPList.do" name="userListIFrame" id="userListIFrame" onload="resizeframe()" width="100%" align="top" marginheight="0" marginwidth="0" ></IFRAME>
				</td>
			</TR>
			<TR>
				<TD align="left" height="40" valign="top">
					<input type="submit" name="operation" value="Create"/>
				</TD>
			</TR>
		</TBODY>
	</TABLE>
</BODY>
</html:form>
</html:html>
