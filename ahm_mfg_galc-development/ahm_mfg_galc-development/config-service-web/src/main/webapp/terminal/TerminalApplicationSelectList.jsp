<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.honda.galc.entity.conf.*" %>
<%@ page import="java.util.*" %>
<html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>Terminals Application List</TITLE>
<script language="javascript">
function doGetTerminalAppInfo() {
	var selectChk = document.all("selectedItem");
	var selectedIndex = -1;
	var nodeId = null;
	var nodeName = null;
	//get the checked item
	if (selectChk.length != null) {
		for(i=0;i<selectChk.length;i++) {
			if(selectChk[i].checked) {
				selectedIndex = i;
				break;
			}
		}
		//no item selected
		if (selectedIndex<0)
			return;
		
		nodeId = document.all('nodeID' + selectedIndex).firstChild.data;
		nodeName = document.all('nodeName' + selectedIndex).firstChild.data;
	}else{
		if (selectChk !=null && selectChk.checked) {
			nodeId = document.all('nodeID0').innerText;
			nodeName = document.all('nodeName0').innerText;
		}
	}
	window.opener.document.all("nodeName").value = nodeId;
	window.opener.document.all("displayText").value = nodeName;
	window.close();
}
function doCloseCurrWindow() {
	window.close();
}
function doLoad() {
	setTimeout(function() { 
		window.opener.document.getElementById("nodeInfo").style.visibility = "visible"; } , 500);
}

</script>
</HEAD>

<BODY class="settingspage" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="doLoad();">
<%
List<ApplicationByTerminal> applicationList = (List<ApplicationByTerminal>)request.getAttribute("terminalApplicationList");
%>
<TABLE border="1" cellpadding="0" cellspacing="0" width="100%">
	<TBODY>
		<TR>
			<TH width="20">&nbsp;</TH>
			<TH>ApplicationID</TH>
			<TH>ApplicationName</TH>
			<TH>ApplicationType</TH>
			<TH>Description</TH>
			<TH>ScreenID</TH>
		</TR>
		<%
		if (applicationList!=null && applicationList.size()>0) {

			int applicationIndex = 0;
			for(ApplicationByTerminal applicationByTerminal : applicationList) {
				Application item = applicationByTerminal.getApplication();
			
		%>
			<TR>
				<TD><input type="radio" name="selectedItem" value="<%=applicationIndex %>"></TD>
				<TD id="nodeID<%=applicationIndex %>"><%=item.getApplicationId() %> </TD>
				<TD id="nodeName<%=applicationIndex %>"><%=item.getApplicationName() %> </TD>
				<TD><%=item.getApplicationType().getTypeString() %> </TD>
				<TD><%=item.getApplicationDescription()%> </TD>
				<TD><%=item.getScreenId()%> </TD>
			</TR>
		<%
			applicationIndex++;
			}
		} 
		%>
	</TBODY>
</TABLE>
<table width="100%">
	<tbody>
		<TR>
			<TD colspan="6" align="center">
				<input type="button" name="operation" value="Apply" onclick="doGetTerminalAppInfo()"/> 
				&nbsp;&nbsp;
				<input type="button" name="operation" value="Cancel" onclick="doCloseCurrWindow()"/>
			</TD>
		</TR>
	</tbody>
</table>
</BODY>
</html>
