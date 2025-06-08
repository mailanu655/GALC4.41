<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<%@page import="com.honda.galc.system.config.web.forms.ComponentStatusSettingsForm"%>
<html:html>
<head>


<script language="javascript">

function verifyInput() {
	var aValue = document.getElementById("componentID").value;
	if(aValue == "") {
		alert("Please enter the component ID.");
		return false;
	}

	aValue = document.getElementById("statusKey").value;
	if(aValue == "") {
		alert("Please enter the status key.");
		return false;
	}
	
	aValue = document.getElementById("statusValue").value;
	if(aValue == "") {
		alert("Please enter the status value.");
		return false;
	}
	return true;
}

function confirmDeleteComponentStatus() {
	myform = window.document.forms['componentStatusSettingsForm'];
	componentStatusID = myform.statusKey.value;
	confirmResult = window.confirm("Are you sure you want to delete " + componentStatusID + "?");
	
	if (confirmResult == true) {
	   myform.confirmDelete.value = 'true';
	}
	return confirmResult;
}

function confirmDeleteAllComponentStatuses() {
	myform = window.document.forms['componentStatusSettingsForm'];
	componentID = myform.componentID.value;
	confirmResult = window.prompt("To confirm that you want to delete all statuses for " + componentID +
	                               " you must enter the component ID in the field below.");
	
	if (confirmResult == componentID) {
	   myform.confirmDeleteAll.value = 'true';
	   cleanEntry();
	   return true;
	}
	return false;
}

function cleanEntry() {
	   document.getElementById("statusKey").value = "";
	   document.getElementById("statusValue").value = "";
	   document.getElementById("description").value = "";
	   return true;
}


</script>
<title>Component Status Settings</title>
</head>
<%
  ComponentStatusSettingsForm form = (ComponentStatusSettingsForm)request.getAttribute("componentStatusSettingsForm");
  if (form == null) {
     form = new ComponentStatusSettingsForm();
  }
  
  String onloadcmd="";
  boolean readonlyID = false;
  boolean disableApply = false;
  boolean disableDelete = false;
  String showWarningColor = (String)session.getAttribute("DISPLAY_PRODUCTION_WARNING_COLOR");
  
  String iframeurl = "";
  
  String readonlyClass = "";
  if (form.isStaticComponentID()) {
  	
  	  readonlyID = true;
  	  readonlyClass="readonly";
  	  onloadcmd="loadComponentStatuses('"+form.getComponentID()+"')";
  
  	  iframeurl = request.getContextPath() + "/componentStatusList.do?componentID=" + form.getComponentID();
  }

  if (!form.isEditor()) {
     disableApply = true;
     disableDelete = true;
  }
 %>
<body class="settingspage" >
<%
if (form.getComponentID() == null) {
 %>
<H1 class="settingsheader" style="<%=showWarningColor%>">Status Configuration for New Component</H1> 
 <% }  else { %>
<H1 class="settingsheader" style="<%=showWarningColor%>">Status Configuration for Component <%= form.getComponentID() %></H1>
<% } %>

<table width="100%">
<tbody>
 <tr>
  <td>
	<html:form styleId="componentStatusSettingsForm" action="/componentStatusSettings" name="componentStatusSettingsForm" type="com.honda.global.galc.system.config.web.forms.ComponentStatusSettingsForm" scope="request" >
    <html:hidden styleId="staticComponentID" property="staticComponentID"/>
    <html:hidden styleId="confirmDeleteAll" property="confirmDeleteAll" value="false"/>
    <html:hidden styleId="confirmDelete" property="confirmDelete" value="false"/>
    <table width="100%">
    <tbody>
    <tr>
    <td>
		<table>
			<tbody>
				<tr>
					<th class="settingstext">Component ID:</th>
					<td><html:text styleId="componentID" property="componentID" styleClass="<%= readonlyClass %>"
						readonly="<%= readonlyID %>" size="64" maxlength="40"></html:text></td>
				</tr>
				<tr>
					<th class="settingstext">Status Key:</th>
					<td><html:text styleId="statusKey" property="statusKey" size="64"
						maxlength="64" /></td>
				</tr>
				<tr>
					<th class="settingstext">Status Value:</th>
					<td><html:text styleId="statusValue" property="statusValue" size="64"
							maxlength="1024" /></td>
				</tr>
				<tr>
					<th class="settingstext">Description:</th>
					<td><html:text styleId="description" property="description" size="64"
							maxlength="64" /></td>
				</tr>
				<tr>
				   <td colspan="2">
				    <html:submit property="apply" value="Apply" disabled="<%= disableApply %>" onclick="return verifyInput()"/>
   				    <html:submit property="delete" value="Delete" disabled="<%= disableDelete %>" onclick="return confirmDeleteComponentStatus();"/>
				   </td>
				</tr>
			</tbody>
		</table>

	
	</td>
	<td align="right" valign="top">
	  <FIELDSET class="settingstext"><LEGEND class="settingstext">Status Set Functions</LEGEND>
	  <table>
	     <tbody>
	     <tr>
	        <th class="settingstext">Edit status for: </th>
	        <td><html:select styleId="loadComponentID" property="loadComponentID">
	               <option label="[Create new component]">[Create new component]</option>
	               <html:options property="referenceComponents"></html:options>
	            </html:select>
	        </td>
	        <td><html:submit property="loadComponent" value="Load" onclick="return cleanEntry();"/> </td>
	        
	     </tr>
	     <% if (form.isEditor() && form.isStaticComponentID()) { %>
	     <tr>
	     <td colspan="3" align="center">
	     <html:submit property="deleteAll" value="Delete All Statuses for Component" disabled="<%= disableDelete %>" onclick="return confirmDeleteAllComponentStatuses();"></html:submit>
	     </td>
	     </tr>
	     <%} %>
	     </tbody>
	  </table>
	  </FIELDSET>
	</td>
	</tr>
	</tbody>
	</table>
	</html:form>
  </td>
  </tr>
  <tr>
  <td>
     <html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
       <bean:write name="msg"/>
     </html:messages>
     <html:errors/>
     <br>
  </td>
  </tr>
  <tr>
	<td>
	  <iframe id="componentstatuslistiframe" src="<%= iframeurl %>" align="top" width="100%" height="400" marginheight="0" marginwidth="0" scrolling="yes">
	  </iframe>
	  </td>
	</tr>
</tbody>
</table>
</body>
</html:html>