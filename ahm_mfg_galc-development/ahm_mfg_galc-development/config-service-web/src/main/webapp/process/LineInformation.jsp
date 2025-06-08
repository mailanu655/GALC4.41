
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="false" %>
<%
response.setHeader("Pragma","no-cache");
%>
<%
response.setHeader("Cache-control", "no-cache");
%>
<%
response.setHeader("Expires", "0");
%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>LineInformation.jsp</TITLE>
<script>
function confirmDeletion()
{
   
   
   zoneID = document.getElementById("lineForm").lineID.value;
   
   
   result =  window.prompt("To confirm the deletion of this line, enter the Line ID below.","");
   
   if (result == zoneID)
   {
      document.getElementById("lineForm").deleteConfirmed.value = "true";
      
      return true;
   }
   else
   {
       alert("Line deletion not confirmed properly");
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
 String existingflagString = (String)request.getAttribute("existingflag");
 boolean existingFlag = true;
 if (existingflagString != null && existingflagString.equalsIgnoreCase("false"))
 {
    existingFlag = false;
 }
 com.honda.galc.system.config.web.forms.LineForm lineForm = 
   (com.honda.galc.system.config.web.forms.LineForm)request.getAttribute("lineForm");
   
 // Determine the style class to apply to possible readonly fields
 String readonlyclass = "";
 if (existingFlag)
 {
    readonlyclass = "readonly";
 }   
 
 String onloadCmd = "";
 
 if (lineForm.isRefreshTree())
 {
    onloadCmd = "refreshTree();";
 }
 
 boolean disableApply = false;
 boolean disableDelete = false;
 boolean disableCancel = false;
 
 if (!lineForm.isEditor())
 {
     disableDelete = true;
     disableApply = true;
 }
 
 if (lineForm.isDeleteConfirmed())
 {
     disableDelete = true;
     disableApply = true;    
     disableCancel = true;
 }
 
 if (lineForm.isCreateFlag())
 {
    disableDelete = true;
    disableCancel = true;
 }
%>
<BODY class="settingspage" onload="<%= onloadCmd %>" >
<H1 class="settingsheader">Line Information Config</H1>
<table border="0" width="100%">
<tbody>
<tr>
<td align="left">
<html:form styleId="lineForm" action="/lineSettings"  name="lineForm" type="com.honda.global.galc.system.config.web.forms.LineForm" scope="request">
    <html:hidden property="createFlag" />
    <html:hidden property="siteName" />
    <html:hidden property="plantName" />
    <html:hidden property="divisionID" />
    <html:hidden property="divisionName" />
    <html:hidden property="deleteConfirmed" value="false" />
	<html:hidden property="processPointCount" />
    <TABLE border="0">
		<TBODY>
		  <tr>
		  <td>
		  <table border="0"> 
		  <tbody>  
			<TR>
				<TH align="left" class="settingstext">Line ID</TH>
				<TD><html:text property="lineID" size="20" maxlength="16" readonly="<%=existingFlag%>" styleClass="<%=readonlyclass%>" /></TD>
			</TR>
			<TR>
				<TH align="left" class="settingstext">Line Name</TH>
				<TD><html:text property="lineName" maxlength="32"/></TD>
			</TR>			
			<tr>
			   <th align="left" class="settingstext">Line type</th>
			   <td><html:select property="lineType" >
			         <html:option value="0">LINE</html:option>
					<html:option value="1">AREA</html:option>
				</html:select>
			   </td>
			</tr>
			<TR>
				<TH align="left" class="settingstext">Description</TH>
				<TD><html:text property="lineDescription" maxlength="128" size="50"/></TD>
			</TR>
			<TR>
				<TH align="left" class="settingstext">EntryProcessPointID</TH>
				<TD><html:text property="entryProcessPointID" styleClass="readonly" readonly="true" maxlength="16"/></TD>
			</TR>						
			<TR>
				<TH align="left" class="settingstext">Previous Line ID</TH>
				<TD><html:text property="previousLinesText" size="50"/></TD>
			</TR>
			<TR>
				<TH align="left" class="settingstext">Sequence No</TH>
				<TD><html:text property="sequenceNo" size="3"/></TD>
			</TR>
		</tbody>
		</table>
		</td>
		</tr>
		<tr>
		<td>
		<FIELDSET class="settingstext"><LEGEND class="settingstext">Line Inventory Information</LEGEND>
		<table>
		  <tbody>
		    <tr>
		        <TH align="left" class="settingstext">Minimum </TH>
				<TD><html:text property="minInventory" size="6" maxlength="4"/></TD>
				<TH align="left" class="settingstext">&nbsp;&nbsp;&lt; Standard </TH>
				<TD><html:text property="stdInventory" size="6" maxlength="4"/></TD>

				<TH>&nbsp;&nbsp;&lt;Maximum</TH>
				<TD><html:text property="maxInventory" size="6" maxlength="4"/></TD>
			</TR>				
		    
		  </tbody>
		</table>
		</FIELDSET>
		</td>
		</tr>
							

		<TR>
		<br/>
		<TR>
		    <TD>
		       <DIV>
			      <P> <%=lineForm.getMissingGpcsDataMessage()%> </P>
			   </DIV>
			</TD>
		</TR>	
				<TD colspan="2" align="center">
				     <html:submit property="apply" value="Apply" disabled="<%= disableApply  %>" />&nbsp;&nbsp;&nbsp;
				     <html:submit property="delete" value="Delete" disabled="<%= disableDelete %>" onclick="return confirmDeletion();" />&nbsp;&nbsp;&nbsp;
				     <html:submit property="cancel" value="Cancel" disabled="<%= disableCancel %>" />
			    </TD>
		   </TR>
		</TBODY>
	</TABLE>
</html:form>
<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
   <bean:write name="msg"/>
</html:messages>
<html:errors />

<%
  if (existingFlag && !lineForm.isDeleteConfirmed())
  {
 %>
<table border = 0 >
<tbody>
<tr>
<td>
<html:form scope="request" action="/processPointSettings">	
    <input type="hidden" name="apply" value="apply">
	<input type="hidden" name="createFlag" value="true">
	
	<input type="hidden" name="lineID" value="<%= lineForm.getLineID() %>">
	<html:submit value="Create new Process Point"></html:submit>
</html:form>
</td>
</tr>
</tbody>
</table>
<% } %>
</td>
<td align="right" valign="top">
<FIELDSET class="settingstext"><LEGEND class="settingstext">Related configurations</LEGEND>
<table border="0" width="100%">
<tbody>
<tr><td><html:link action="/processPointList" paramId="lineID" paramName="lineForm" paramProperty="lineID" styleClass="settingspagelink">Process Points (<%= lineForm.getProcessPointCount() %>)</html:link></td></tr>
<tr><td><html:link action="/gpcs" paramId="lineID" paramName="lineForm" paramProperty="lineID" styleClass="settingspagelink">GPCS</html:link></td></tr>
</tbody>
</table>
</FIELDSET>
</td>
</tr>
</tbody>
</table>
</BODY>
</html:html>
