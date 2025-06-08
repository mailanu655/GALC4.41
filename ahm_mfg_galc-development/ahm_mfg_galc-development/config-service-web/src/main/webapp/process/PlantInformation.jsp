
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
<LINK href="<c:url value="/theme/Master.css" />" rel="stylesheet"
	type="text/css">
<TITLE>Plant Information</TITLE>
<SCRIPT>

function confirmDeletion()
{
   
   
   plantID = document.getElementById("plantForm").plantName.value;
   
   
   result =  window.prompt("To confirm the deletion of this plant, enter the Plant Name below.","");
   
   if (result == plantID)
   {
      document.getElementById("plantForm").deleteConfirmed.value = "true";
      
      return true;
   }
   else
   {
       alert("Plant deletion not confirmed properly");
   }
   
   return false;
}

function refreshTree()  {

   parent.treepane.location="<c:url value="/buildProcessTree.do"/>";
}


</SCRIPT>
</HEAD>

<%
    com.honda.galc.system.config.web.forms.PlantForm plantForm = 
    (com.honda.galc.system.config.web.forms.PlantForm)request.getAttribute("plantForm");

 
 boolean disableApply = !plantForm.isEditor();
 boolean disableDelete = !plantForm.isEditor();

 // Get the flag that determines if certain fields are read-only
 String existingflagString = (String)request.getAttribute("existingflag");
 boolean existingFlag = true;
 if (existingflagString != null && existingflagString.equalsIgnoreCase("false"))
 {
    existingFlag = false;
    
    disableDelete = true;
 }
 
 // If the plant was just deleted, disable the buttons
 if (plantForm.isDeleteConfirmed())
 {
   disableDelete = true;
   disableApply = true;
   
 }
 
 String onloadcall = "";
 if (plantForm.isRefreshTree())
 {  
    onloadcall = "refreshTree();";
 }
 
 // Determine the style class to apply to possible readonly fields
 String readonlyclass = "";
 if (existingFlag)
 {
    readonlyclass = "readonly";
 }
%>
<BODY class="settingspage" onload="<%= onloadcall %>">
<H1 class="settingsheader">Plant Information Config</H1>
<html:form styleId="plantForm" action="/plantSettings" name="plantForm" type="com.honda.global.galc.system.config.web.forms.PlantForm" scope="request">
    <html:hidden property="createFlag" />
    <html:hidden property="siteName" />
    <html:hidden property="deleteConfirmed" />
	<TABLE border="0">	
		<TBODY>
			<TR>
				<TH align="left" class="settingstext">Plant name:</TH>
				<TD><html:text property="plantName" readonly="<%= existingFlag %>" styleClass="<%=readonlyclass %>" size="16" maxlength="16" /></TD>
			</TR>
			<TR>
				<TH align="left" class="settingstext">Plant description</TH>
				<TD><html:text property="plantDescription" maxlength="128" size="60"/></TD>
			</TR>
			<TR>
				<TD colspan="2"><html:submit property="apply" value="Apply" disabled="<%= disableApply %>" />&nbsp;&nbsp;&nbsp;
				                <html:submit property="delete" value="Delete" disabled="<%= disableDelete %>" onclick="return confirmDeletion();" />&nbsp;&nbsp;&nbsp;
				<html:submit property="cancel" value="Cancel" /></TD>	
			</TR>
		</TBODY>
	</TABLE>
</html:form>
<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
   <bean:write name="msg"/>
</html:messages>
<html:errors/>

<%
 if (existingFlag && plantForm.isDivisionEditor() && !plantForm.isDeleteConfirmed()) {
 %>

<html:form scope="request" action="/divisionSettings">	
    <input type="hidden" name="apply" value="apply">
	<input type="hidden" name="createFlag" value="true">
	<input type="hidden" name="plantName" value="<%= plantForm.getPlantName() %>">
	
	<input type="hidden" name="siteName" value="<%= plantForm.getSiteName() %>">
	<html:submit value="Create new Division"></html:submit>
</html:form>
<% } %>

</BODY>
</html:html>
