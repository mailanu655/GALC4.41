
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<html:html>
<HEAD>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.SiteForm" %>
<%@ page session="false" %>	
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>Site Information.jsp</TITLE>
<script>
function confirmDeletion()
{
   
   
   siteID = document.getElementById("siteForm").name.value;
   
   
   result =  window.prompt("To confirm the deletion of this site, enter the Site Name below.","");
   
   if (result == siteID)
   {
      document.getElementById("siteForm").confirmDelete.value = "true";
      
      return true;
   }
   else
   {
       alert("Site deletion not confirmed properly");
   }
   
   return false;
}

function refreshTree()  {

    parent.treepane.location="<c:url value="/buildProcessTree.do"/>";
 }

</script>
</HEAD>
<%

 // Get the flag that determines if certain fields are read-only
 
 boolean existingFlag = true;

 
 SiteForm siteform = (SiteForm)request.getAttribute("siteForm");
 
 if (siteform.isCreateFlag())
 {
     existingFlag = false;
 }
 
 boolean disableDelete = false;
 boolean disableApply = false;
 boolean disablePlant = false;
 
 if (siteform != null)
 {
     disableDelete = !siteform.isEditor();
     disableApply = !siteform.isEditor();
     disablePlant = !siteform.isPlantEditor();
 }

 // Determine the style class to apply to possible readonly fields
 String readonlyclass = "";
 if (existingFlag)
 {
    readonlyclass = "readonly";
 }
 
 String onloadcmd = "";
 if (siteform.isRefreshTree())
 {
     onloadcmd = "refreshTree();";
 }
 %>
<BODY class="settingspage" onload="<%= onloadcmd %>">
<H1 class="settingsheader">Site Information Config</H1>
<html:form styleId="siteForm" name="siteForm" type="com.honda.galc.system.config.web.forms.SiteForm" scope="request" action="/getSiteInformation">
    <html:hidden property="confirmDelete" />
    <html:hidden property="createFlag"/>
	<TABLE border="0">
		<TBODY>
			<TR>
				<TH align="left" class="settingstext">Site Name</TH>
				<TD><html:text property="name"  readonly="<%= existingFlag %>" size="16" maxlength="16" styleClass="<%=readonlyclass %>"/></TD>
			</TR>
			<TR>
				<TH align="left" class="settingstext">Description</TH>
				<TD><html:text property="description" maxlength="128" size="60"/></TD>
			</TR>
			<TR>
				<TD colspan="2"><html:submit property="apply" value="Apply" disabled="<%= disableApply %>" />&nbsp;&nbsp;&nbsp;
				    <html:submit property="delete" value="Delete" disabled="<%= disableDelete %>"  onclick="return confirmDeletion();" />&nbsp;&nbsp;&nbsp;
				    <html:submit property="cancel" value="Cancel" /></TD>			
			</TR>
		</TBODY>
	</TABLE>
</html:form>

<P><BR>
<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE"><bean:write name="msg"/></html:messages>
</P>
<html:errors/>

<%
 if (existingFlag && siteform.isPlantEditor() && !siteform.isConfirmDelete()) {

 %>

<html:form name="siteForm" type="com.honda.galc.system.config.web.forms.SiteForm" scope="request" action="/plantSettings">	
	<input type="hidden" name="createFlag" value="true">
	<input type="hidden" name="siteName" value="<%= siteform.getName() %>">
	<html:submit value="Create new Plant for this site."></html:submit>
</html:form>
<% } %>
</BODY>
</html:html>
