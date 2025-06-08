<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<%@page import="com.honda.galc.system.config.web.forms.PropertySettingsForm"%>
<html:html>
<head>


<script language="javascript">

function verifyInput() {
	var aValue = document.getElementById("componentID").value;
	if(aValue == "") {
		alert("Please enter the component ID.");
		return false;
	}

	aValue = document.getElementById("propertyKey").value;
	if(aValue == "") {
		alert("Please enter the property key.");
		return false;
	}

	return true;
}

function confirmDeleteProperty() {

	myform = window.document.forms['propertySettingsForm'];
	
	propertyID = myform.propertyKey.value;
	
	confirmResult = window.confirm("Are you sure you want to delete "+propertyID+"?");
	
	if (confirmResult == true) {
	   myform.confirmDelete.value = 'true';
	}
	
	return confirmResult;
}

function promptRenameProperty() {
	myform = window.document.forms['propertySettingsForm'];
	propertyKey = myform.propertyKey.value;
	if (propertyKey == "") {
		alert("Please select a property from the table below.");
		return false;
	}
	renamePropertyKey = window.prompt("Please enter the new name for property " + propertyKey + " in the field below.", propertyKey);
	if (renamePropertyKey == null) {
		return false;
	}
	if (renamePropertyKey.trim() == "") {
		alert("The property key must not be blank.");
		return false;
	}
	myform.renamePropertyKey.value = renamePropertyKey;
	return true;
}

function confirmDeleteAllProperties() {

	myform = window.document.forms['propertySettingsForm'];
	
	componentID = myform.componentID.value;
	
	confirmResult = window.prompt("To confirm that you want to delete all properties for "+componentID+ 
	                               " you must enter the component ID in the field below.");
	
	if (confirmResult == componentID) {
	   myform.confirmDeleteAll.value = 'true';
	   cleanEntry();
	   return true;
	}
	
	return false;
}

function confirmMergeProperties() {

	myform = window.document.forms['propertySettingsForm'];
	
	componentID = myform.componentID.value;
	
	mergeComponentID = myform.referenceComponentID.value;
	
	confirmResult = window.confirm("Are you sure you want to merge properties from " + mergeComponentID + " into " + componentID + "?");
	
	if (confirmResult == true) {
	   myform.confirmMerge.value = 'true';
	}
	
	return confirmResult;
}

function cleanEntry() {
	   document.getElementById("propertyKey").value = "";
	   document.getElementById("propertyValue").value = "";
	   document.getElementById("description").value = "";
	   return true;
}

function confirmRefreshProperties() {

	myform = window.document.forms['propertySettingsForm'];
	
	componentID = myform.componentID.value;
	
	confirmResult = window.confirm("Are you sure you want to refresh properties for " + componentID + "?\n"
									+"All server instances will be updated. However other servers in cluster might stay out of sync!!!");
	
	return confirmResult;
}


</script>
<title>Property Settings</title>
</head>
<%
  PropertySettingsForm form = (PropertySettingsForm)request.getAttribute("propertySettingsForm");
  if (form == null)
  {
     form = new PropertySettingsForm();
     
  }
  
  String onloadcmd="";
  boolean readonlyID = false;
  boolean disableApply = false;
  boolean disableDelete = false;
  boolean disableRename = false;
  boolean disableRefresh = false;
  String showWarningColor = (String)session.getAttribute("DISPLAY_PRODUCTION_WARNING_COLOR");
  
  String iframeurl = "";
  
  String readonlyClass = "";
  if (form.isStaticComponentID()) {
  	
  	  readonlyID = true;
  	  readonlyClass="readonly";
  	  onloadcmd="loadProperties('"+form.getComponentID()+"')";
  
  	  iframeurl = request.getContextPath() +"/propertyList.do?componentID="+form.getComponentID();
  }

  if (!form.isEditor()) {
     
     disableApply = true;
     disableDelete = true;
     disableRename = true;
  }

  String componentTypeString = "Component";
  boolean showChooseAnotherComponent = false;
  if (form.getComponentType() ==  PropertySettingsForm.COMPONENT_TYPE_TERMINAL) {
     componentTypeString = "Terminal";
  }
  else if (form.getComponentType() ==  PropertySettingsForm.COMPONENT_TYPE_APPLICATION) {
  
     componentTypeString = "Application";
  }
  else {
  
      // Generic component type
      showChooseAnotherComponent = true;
  }
 %>
<body class="settingspage" >
<%
if (form.getComponentID() == null) {
 %>
<H1 class="settingsheader" style="<%=showWarningColor%>">Properties Configuration for New Component</H1> 
 <% }  else { %>
<H1 class="settingsheader" style="<%=showWarningColor%>">Properties Configuration for <%= componentTypeString %> <%= form.getComponentID() %></H1>
<% } %>

<table width="100%">
<tbody>
 <tr>
  <td>
	<html:form styleId="propertySettingsForm" action="/propertySettings" name="propertySettingsForm" type="com.honda.global.galc.system.config.web.forms.PropertySettingsForm" scope="request" >
    <html:hidden styleId="staticComponentID" property="staticComponentID"/>
    <html:hidden styleId="componentType" property="componentType"/>
    <html:hidden styleId="confirmDeleteAll" property="confirmDeleteAll" value="false"/>
    <html:hidden styleId="confirmDelete" property="confirmDelete" value="false"/>
    <html:hidden styleId="renamePropertyKey" property="renamePropertyKey"/>
    <html:hidden styleId="confirmMerge" property="confirmMerge" value="false"/>
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
					<th class="settingstext">Property Key:</th>
					<td><html:text styleId="propertyKey" property="propertyKey" size="64"
						maxlength="64" /></td>
				</tr>
				<tr>
					<th class="settingstext">Property Value:</th>
					<td><html:text styleId="propertyValue" property="propertyValue" size="64"
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
   				    <html:submit property="delete" value="Delete" disabled="<%= disableDelete %>" onclick="return confirmDeleteProperty();"/>
   				    <html:submit property="rename" value="Rename" disabled="<%= disableRename %>" onclick="return promptRenameProperty();"/>
				    <html:submit property="refresh" value="Refresh Properties" disabled="<%= disableRefresh %>" onclick="return confirmRefreshProperties();"/>
				   </td>
				</tr>
			</tbody>
		</table>

	
	</td>
	<td align="right" valign="top">
	  <FIELDSET class="settingstext"><LEGEND class="settingstext">Property Set Functions</LEGEND>
	  <table>
	     <tbody>
	     <% if (showChooseAnotherComponent) {%> 
	     <tr>
	        <th class="settingstext">Edit properties for: </th>
	        <td><html:select styleId="loadComponentID" property="loadComponentID">
	               <option label="[Create new component]">[Create new component]</option>
	               <html:options property="referenceComponents"></html:options>
	            </html:select>
	        </td>
	        <td><html:submit property="loadComponent" value="Load" onclick="return cleanEntry();"/> </td>
	        
	     </tr>
	     <% } %>
	     <% if (form.isEditor() && form.isStaticComponentID()) { %>
	     <tr>
	        <th class="settingstext">Merge properties from: </th>
	        <td><html:select styleId="referenceComponentID" property="referenceComponentID">
	               <html:options property="referenceComponents"></html:options>
	            </html:select>
	        </td>
	        <td><html:submit property="loadFromTemplate" value="Merge" onclick="return confirmMergeProperties()"/> </td>
	        
	     </tr>
	     <tr>
	       <td>&nbsp;</td>
	     </tr>
	     
	     <tr>
	     <td colspan="3" align="center">
	     <html:submit property="deleteAll" value="Delete All Properties" disabled="<%= disableDelete %>" onclick="return confirmDeleteAllProperties();"></html:submit>
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
	  <iframe id="propertylistiframe" src="<%= iframeurl %>" align="top" width="100%" height="400" marginheight="0" marginwidth="0" scrolling="yes">
	  </iframe>
	  </td>
	</tr>
</tbody>
</table>
</body>
</html:html>