<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.honda.galc.entity.conf.*" %>
<%@ page import="java.util.*" %>

<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE></TITLE>
</HEAD>

<BODY class="settingspage" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<html:form action="terminalApplicationItemList">
<input type="hidden" name="applicationType" id="applicationType">
<input type="hidden" name="applicationFilter" id="applicationFilter">
<%
	List<Application> applicationList = (List<Application>)request.getAttribute("applicationList");
	String applicationType = (String)request.getAttribute("applicationType");
	if (applicationType == null) {
		applicationType = "0";
	}
%>
<TABLE border="1" cellpadding="0" cellspacing="0" width="100%">
	<TBODY id="appItems">
		<TR>
			<TD width="20">&nbsp;</TD>
			<%if (applicationType.equalsIgnoreCase("1")) {%>
			<TH>ProcessPointID</TH>
			<TH>ProcessPointName</TH>
			<%}else{ %>
			<TH>ApplicationID</TH>
			<TH>ApplicationName</TH>
			<%} %>
			<TH>Description</TH>
			<TH>ScreenID</TH>
			<th style="display:none;"></th>
		</TR>
		<%
		if (applicationList!=null && applicationList.size()>0) {
			int applicationIndex = 0;
			for(Application item : applicationList) {
		%>
			<TR id="appItem<%=applicationIndex %>">
				<TD><input type="radio" name="selectedItem" id="selectedItem" value="<%=item.getApplicationId() %>"></TD>
				<TD><%=item.getApplicationId() %></TD>
				<%
				String applicationName = "&nbsp;";
				if (item.getApplicationName() != null && item.getApplicationName().length()>0) 
					applicationName = item.getApplicationName();
				%>
				<TD><%=applicationName %></TD>
				<%
				String applicationDesc = "&nbsp;";
				if (item.getApplicationDescription() != null && item.getApplicationDescription().length()>0) 
					applicationDesc = item.getApplicationDescription();
				%>
				<TD><%=applicationDesc %></TD>
				<%
				String screenID = "&nbsp;";
				if (item.getScreenId() != null && item.getScreenId().length()>0) 
					screenID = item.getScreenId();
				%>
				<TD><%=screenID %></TD>
				<td style="display:none;"><%=item.getApplicationTypeId() %></td>
			</TR>
		<%
			applicationIndex++;
			}
		} 
		%>
	</TBODY>
</TABLE>
</BODY>
</html:form>
</html:html>
