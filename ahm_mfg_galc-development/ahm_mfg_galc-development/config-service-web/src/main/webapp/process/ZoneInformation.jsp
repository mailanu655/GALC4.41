
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.ZoneForm" %>
<%@ page session="false" %>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>Zone Information</TITLE>
<script>

function confirmDeletion()
{
   
   
   zoneID = document.getElementById("zoneForm").zoneID.value;
   
   
   result =  window.prompt("To confirm the deletion of this zone, enter the Zone ID below.","");
   
   if (result == zoneID)
   {
      document.getElementById("zoneForm").deleteConfirmed.value = "true";
      
      return true;
   }
   else
   {
       alert("Zone deletion not confirmed properly");
   }
   
   return false;
}

function refreshTree()  {

   parent.treepane.location="<c:url value="/buildProcessTree.do"/>";
}
</script>
</HEAD>
<%
 ZoneForm zoneForm = (ZoneForm)request.getAttribute("zoneForm");
 if (zoneForm == null)
 {
    // Go with default to avoid null pointers
    zoneForm = new ZoneForm();
 }

 // Get the flag that determines if certain fields are read-only
 boolean existingFlag = zoneForm.isExistingZone();
 
   
 // Determine the style class to apply to possible readonly fields
 String readonlyclass = "";
 if (existingFlag)
 {
    readonlyclass = "readonly";
 }   

 boolean disableDelete = false;
 boolean disableApply = false;
 boolean disableCancel = false;

 if (!zoneForm.isExistingZone())
 {
    disableDelete = true;
 }

 if (zoneForm.isDeleteConfirmed())
 {
     disableDelete = true;
     disableApply = true;
     disableCancel = true;
 }
 
 if (!zoneForm.isEditor())
 {
     disableDelete = true;
     disableApply = true; 
 }
 
 String onloadCmd = "";
 if (zoneForm.isRefreshTree())
 {
    onloadCmd = "refreshTree();";
 }
 
 %>
<BODY class="settingspage" onload="<%= onloadCmd %>">
<H1 class="settingsheader">Zone Information Config</H1>
<table border="0" width="100%">
<tbody>
<tr>
<td align="left">
<html:form styleId="zoneForm" action="/zoneSettings"  name="zoneForm" type="com.honda.global.galc.system.config.web.forms.ZoneForm" scope="request">
    <html:hidden property="existingZone" />
    <html:hidden property="divisionID" />
    <html:hidden property="deleteConfirmed" />
	<TABLE border="0">
		<TBODY>
		  <tr>
		  <td>
		  <table border="0"> 
		  <tbody>  
			<TR>
				<TH align="left" class="settingstext">Zone ID</TH>
				<TD><html:text property="zoneID" readonly="<%= existingFlag %>" styleClass="<%=readonlyclass %>" size="20" maxlength="16" /></TD>
			</TR>
			<TR>
				<TH align="left" class="settingstext">Zone Name</TH>
				<TD><html:text property="zoneName" maxlength="32"/></TD>
			</TR>			
			<TR>
				<TH align="left" class="settingstext">Description</TH>
				<TD><html:text property="description" maxlength="128" size="50"/></TD>
			</TR>
		</tbody>
		</table>
		</td>
		</tr>
		<tr>
		  <td colspan="2">
			<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
			   <bean:write name="msg"/>
			</html:messages>
			<html:errors />		  
		  </td>
		</tr>

			<TR>
				<TD colspan="2" align="center">
				     <html:submit property="apply" value="Apply"  disabled="<%= disableApply %>"/>&nbsp;&nbsp;&nbsp;
				     <html:submit property="delete" value="Delete" disabled="<%= disableDelete %>"  onclick="return confirmDeletion();"/>&nbsp;&nbsp;&nbsp;
				     <html:submit property="cancel" value="Cancel" disabled="<%= disableCancel %>"/>
			    </TD>
		   </TR>
		</TBODY>
	</TABLE>
</html:form>

</BODY>
</html:html>
