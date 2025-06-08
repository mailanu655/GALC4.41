
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.DivisionForm" %>
<%@ page session="false" %>

<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>Division Information Configuration</TITLE>
<SCRIPT>

function confirmDeletion()
{
   
   
   divID = document.getElementById("divForm").divisionID.value;
   
   
   result =  window.prompt("To confirm the deletion of this division, enter the Division ID below.","");
   
   if (result == divID)
   {
      document.getElementById("divForm").deleteConfirmed.value = "true";
      
      return true;
   }
   else
   {
       alert("Plant deletion not confirmed properly");
   }
   
   return false;
}

function checkExisting() {
	var existingFlag = document.getElementById("divForm").divisionID.readOnly;
	if (!existingFlag) {
		var selectedDivisionID = document.getElementById("divForm").divisionID.value;
		var selectedSiteName = document.getElementById("divForm").siteName.value;
		var selectedPlantName = document.getElementById("divForm").plantName.value;
		var url= "/ConfigService/checkExistingDivisionIdAjax.do?function=checkDivisionIdExists&divisionID=" + selectedDivisionID + "&siteName=" + selectedSiteName + "&plantName=" + selectedPlantName;
		try {
			// Firefox, Opera 8.0+, Safari
			xmlHttp = new XMLHttpRequest();
		} catch(e) {
			// Internet Explorer 6.0+
			try {
				xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
			} catch (e) {
				try {
					xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
				} catch (e) {
					alert("Your browser does not support AJAX!");
					return false;
				}
			}
		}
		xmlHttp.onreadystatechange = function() {
			if (xmlHttp.readyState == 4) {
				var exists = xmlHttp.responseText.trim();
				if (exists === "false") {
					document.getElementById("divForm").action = document.getElementById("divForm").action + "?apply=apply";
					document.getElementById("divForm").submit();
				} else {
					var parms = exists.split(",");
					if (confirm("Division " + parms[1] + " already exists at site " + parms[2] + " and plant " + parms[3] + ".\nDo you want to move division " + parms[1] + " to site " + parms[4] + " and plant " + parms[5] + "?")) {
						document.getElementById("divForm").action = document.getElementById("divForm").action + "?apply=apply";
						document.getElementById("divForm").submit();
					}
				}
			}
		}
		xmlHttp.open("POST", url, true);
		xmlHttp.send(null);
	} else {
		document.getElementById("divForm").action = document.getElementById("divForm").action + "?apply=apply";
		document.getElementById("divForm").submit();
	}
}

function refreshTree()  {

   parent.treepane.location="<c:url value="/buildProcessTree.do"/>";
}


</SCRIPT>
</HEAD>
<%

 // Get the flag that determines if certain fields are read-only
 String existingflagString = (String)request.getAttribute("existingflag");
 boolean existingFlag = true;
 if (existingflagString != null && existingflagString.equalsIgnoreCase("false"))
 {
    existingFlag = false;
 }
 
 DivisionForm divisionForm = (DivisionForm)request.getAttribute("divisionForm");
 
 String onloadcall = "";
 
 if (divisionForm == null)
 {
    // Avoid null pointers, create stub
    divisionForm = new DivisionForm();
 }
 
 if (divisionForm.isRefreshTree())
 {
    onloadcall = "refreshTree();";
 }
 
 // Determine the style class to apply to possible readonly fields
 String readonlyclass = "";
 if (existingFlag)
 {
    readonlyclass = "readonly";
 }
 
 boolean disableApply = false;
 boolean disableDelete = false;
 boolean disableTerminal = false;
 boolean disableDevice = false;
 boolean disableCancel = false;
 
 if (!divisionForm.isEditor() || divisionForm.isDeleteConfirmed())
 {
    disableApply = true;
    disableDelete = true;
  
 }
 
 if (divisionForm.isDeleteConfirmed())
 {
    disableCancel = true;
 }
 
 if (!divisionForm.isTerminalEditor())
 {
    disableTerminal = true;
 }
 
 if (!divisionForm.isDeviceEditor())
 {
    disableDevice = true;
 }
 %>
<BODY class="settingspage" onload="<%= onloadcall %>">
<H1 class="settingsheader">Division Information Config</H1>
<table border="0" width="100%">
<tbody>
<tr>
<td align="left">
<html:form styleId="divForm" action="/divisionSettings" name="divisionForm" type="com.honda.global.galc.system.config.web.forms.DivisionForm" scope="request">
    <html:hidden property="createFlag" />
    <html:hidden property="siteName" />
    <html:hidden property="plantName" />
    <html:hidden property="deleteConfirmed" />
	<TABLE border="0">
		<TBODY>
		<tr>
		<td>
		  <table border="0"> 
		  <tbody> 
			<TR>
				<TH align="left" class="settingstext">Division ID:</TH>
				<TD><html:text property="divisionID" maxlength="16" readonly="<%= existingFlag %>" styleClass="<%=readonlyclass %>"/></TD>
			</TR>
			<TR>
				<TH align="left" class="settingstext">Division Name:</TH>
				<TD><html:text property="divisionName" maxlength="32" readonly="<%= existingFlag %>" styleClass="<%=readonlyclass %>" /></TD>
			</TR>
			<TR>
				<TH align="left" class="settingstext">Division Description</TH>
				<TD><html:text property="divisionDescription" /></TD>
			</TR>	
			<TR>
				<TH align="left" class="settingstext">Sequence</TH>
				<TD width= "3" nowrap><html:text property="sequenceNumber" size="9" maxlength="9"/></TD>
			</TR>
		  </tbody>
		  </table>
		 </td>
         </tr>
			<br />
			<TR>
			   <TD>
	             <FIELDSET class="settingstext">
					<LEGEND class="settingstext">Division Relationship
						to GPCS</LEGEND>
					<table>
					 <tbody>
				       <TR>
							<TH align="left" class="settingstext">Process
								Location</TH>
							<TD><html:text property="processLocation" /></TD>
							<TH align="left" class="settingstext">&nbsp;&nbsp;
								Line No</TH>
							<TD><html:text property="lineNo" /></TD>
					   </TR>
				     </tbody>
			       </table>
		         </FIELDSET>
	            </TD>
             </TR>
     	<TR>
	      <br/>
			    <TD colspan="2">
					<html:button property="apply" value="Apply" disabled="<%= disableApply %>" onclick="checkExisting();"/>&nbsp;&nbsp;&nbsp;
					<html:submit property="delete" value="Delete" disabled="<%= disableDelete %>" onclick="return confirmDeletion();"/>&nbsp;&nbsp;&nbsp;
					<html:submit property="cancel" value="Cancel" disabled="<%= disableCancel %>"/>
					<P> <%=divisionForm.getMissingGpcsDataMessage()%> </P>
				</TD>	
			</TR>
		</TBODY>
	</TABLE>
</html:form>
<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
   <bean:write name="msg"/>
</html:messages>
<html:errors/>


<%
 if (existingFlag && !divisionForm.isDeleteConfirmed()) {
   
 %>


<table border = 0 >
<tbody>
<tr>
<td>
<html:form scope="request" action="/lineSettings">	
    <input type="hidden" name="apply" value="apply">
	<input type="hidden" name="createFlag" value="true">
	<input type="hidden" name="plantName" value="<%= divisionForm.getPlantName() %>">
	<input type="hidden" name="divisionID" value="<%= divisionForm.getDivisionID() %>">
	<input type="hidden" name="divisionName" value="<%= divisionForm.getDivisionName() %>">
	<input type="hidden" name="siteName" value="<%= divisionForm.getSiteName() %>">
	<html:submit value="Create new Line"></html:submit>
</html:form>
</td>
<td>
<html:form scope="request" action="/zoneSettings">	
	<input type="hidden" name="divisionID" value="<%= divisionForm.getDivisionID() %>">
	<input type="hidden" name="existingZone" value="false" />	
	<html:submit value="Create new Zone"></html:submit>
</html:form>
</td>

<td>
<html:form scope="request" action="/terminalSettings">	
    <input type="hidden" name="apply" value="apply">
	<input type="hidden" name="createFlag" value="true">
	<input type="hidden" name="plantName" value="<%= divisionForm.getPlantName() %>">
	<input type="hidden" name="divisionID" value="<%= divisionForm.getDivisionID() %>">
	<input type="hidden" name="siteName" value="<%= divisionForm.getSiteName() %>">
	<html:submit value="Create new Terminal" disabled="<%= disableTerminal %>"></html:submit>
</html:form>
</td>
<td>
<html:form scope="request" action="/deviceSettings">	
    <input type="hidden" name="apply" value="apply">
	<input type="hidden" name="createFlag" value="true">
	<input type="hidden" name="plantName" value="<%= divisionForm.getPlantName() %>">
	<input type="hidden" name="divisionID" value="<%= divisionForm.getDivisionID() %>">
	<input type="hidden" name="siteName" value="<%= divisionForm.getSiteName() %>">
	<html:submit value="Create new Device" disabled="<%= disableDevice %>"></html:submit>
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
<%
 if (existingFlag && !divisionForm.isDeleteConfirmed()) {
   
 %>

<tr><td><html:link action="/terminalList" paramId="divisionID" paramName="divisionForm" paramProperty="divisionID" styleClass="settingspagelink">Terminals</html:link></td></tr>
<tr><td><html:link action="/deviceList" paramId="divisionID" paramName="divisionForm" paramProperty="divisionID" styleClass="settingspagelink">Devices</html:link></td></tr>
<tr><td><html:link action="/gpcs" paramId="divisionID" paramName="divisionForm" paramProperty="divisionID" styleClass="settingspagelink">GPCS</html:link></td></tr>
<%} %>
</tbody>
</table>
</FIELDSET>
</td>
</tr>
</tbody>
</table>

</BODY>
</html:html>
