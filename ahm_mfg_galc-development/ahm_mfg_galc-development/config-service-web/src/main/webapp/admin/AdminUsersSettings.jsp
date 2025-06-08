
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.AdminUserForm" %>
<%@ page session="false" %>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>AdminUsersSettings.jsp</TITLE>
<script>

function show(sObjectID) {
		// USED TO SHOW AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		
		document.getElementById(sObjectID).style.display='';
}
		
function hide(sObjectID) {
			
		// USED TO HIDE AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		document.getElementById(sObjectID).style.display='none';
}

function processCheckbox() {
   if (document.getElementById("passwordcb").checked == true)
   {
      // Show the password fields
      show("passwordrow");
      show("confirmpasswordrow");
   }
   else
   {
      // Hide the password fields
      hide("passwordrow");
      hide("confirmpasswordrow");      
   }
}

function addGroup(){
	var selGroup = document.getElementById("selectedSecurityGroup");
	var groupName = selGroup.options[selGroup.selectedIndex].text;
	var groupValue = selGroup.options[selGroup.selectedIndex].value;
	var agnGroup = document.getElementById("assignedSecurityGroup");
	for (i=0; i<agnGroup.length;i++) 
	{
		if (agnGroup.options[i].text == groupName) {
			return;
		}
	}
	var newGroup = document.createElement('option');
	newGroup.text = groupName;
	newGroup.value = groupValue;
	try {
		agnGroup.add(newGroup, null);	// standards complient browser
	}
	catch(ex) {
		agnGroup.add(newGroup);		// MS IE 
	}
}
function removeGroup() {
	var agnGroup = document.getElementById("assignedSecurityGroup");
	if (agnGroup.selectedIndex >= 0) {
		var group = agnGroup.options[agnGroup.selectedIndex].text;
		agnGroup.remove(agnGroup.selectedIndex);
	}
}

function applyChanges() {

	var agnGroup = document.getElementById("assignedSecurityGroup");
	if (agnGroup.length > 0) {
		for (i=0; i<agnGroup.length;i++) {
			agnGroup.options[i].selected = true;
		}
	}
}

function deleteConfirmation() {
 
    var userid = document.getElementById("adminUserForm").userID.value;
    
    var result = prompt("To delete the user, enter the user ID in the field below:","");
    
    if (result != userid)
    {
       alert("User will not be deleted");
       
       return false;
    }
    
    document.getElementById("adminUserForm").deleteConfirm.value="true";
    return true;

}

function refreshUserList() {

   parent.frames['resultiframe'].window.document.adminUserSearchForm.submit();
}

</script>
</HEAD>
<%
    AdminUserForm adminUserForm = (AdminUserForm)request.getAttribute("adminUserForm");
    
    boolean readonlyID = false;
    String readonlyClass = "";
    
    String passwordRowStyle = "";
    
    
    if (adminUserForm.isExistingUser())
    {
      readonlyID = true;
      readonlyClass = "readonly";
      
      if (!adminUserForm.isChangePassword()) {
      		passwordRowStyle = "display: none";
      }
    }
    
    boolean disableApply = false;
    boolean disableDelete = false;
    boolean disableCancel = false;
    
    if (!adminUserForm.isEditor())
    {
       disableApply = true;
       disableDelete = true;
       
    }
    
    if (adminUserForm.isDeleteConfirm())
    {
       // Must have already deleted
       disableApply = true;
       disableDelete = true;       
       disableCancel = true;
    }
    
    if (!adminUserForm.isExistingUser())
    {
       disableDelete = true;
       disableCancel = true;
    }
    
    request.setAttribute("adminGroups",adminUserForm.getAllGroups());
    request.setAttribute("usersAdminGroups",adminUserForm.getUserGroups());
    
    
    // Determine if we want to refresh the parent list
    String onloadcmd ="";
    
    if (adminUserForm.isRefreshList())
    {
       onloadcmd="refreshUserList()";
    }
 %>
<BODY class="settingspage" onload="<%=onloadcmd %>" >
<html:form styleId="adminUserForm" action="/adminUserSettings" name="adminUserForm"   scope="request" type="com.honda.galc.system.config.web.forms.AdminUserForm">
    <html:hidden property="initializePage" />
    <html:hidden property="existingUser" />
    <html:hidden styleId="deleteConfirm" property="deleteConfirm"/>
	<TABLE border="0">
		<TBODY>
			<TR>
				<TH class="settingstext" align="left">User ID</TH>
				<TD align="left"><html:text property="userID" readonly="<%= readonlyID %>" styleClass="<%= readonlyClass %>" size="32" maxlength="32"/></TD>
				<td>&nbsp</td>
			</TR>		
			<TR>
				<TH class="settingstext" align="left">Display Name</TH>
				<TD align="left"><html:text property="displayName" size="48" maxlength="64"/></TD>
				<td>&nbsp</td>
			</TR>
			<TR>
				<TH class="settingstext" align="left">Description</TH>
				<TD align="left"> <html:text property="description" size="48" maxlength="64"/></TD>
				<td>&nbsp</td>
			</TR>
			<tr>
			   <td colspan="3">
			   <FIELDSET class="settingstext">
			   <LEGEND class="settingstext">Groups</LEGEND>
			   
			   <table>
			   <tbody>
			   <tr>
			    <th class="settingsText">Assigned Groups</th>
			    <td></td>
			    <th class="settingsText">Available Groups</th>
			   </tr>
			   <tr>
			   <td valign="top" align="center">

			        
					<html:select property="assignedGroups" multiple="true" size="6" style="width: 250" styleId="assignedSecurityGroup">
					  <html:options collection="usersAdminGroups" property="groupId" labelProperty="groupDisplayText" />
					</html:select>
					

			   </td>
			   <TD align="center" class="settingstext">
					<input type="button" value="&nbsp;<<&nbsp;" style="width=100" onclick="addGroup()">
					<P/>
					<input type="button" value="&nbsp;>>&nbsp;" style="width=100" onclick="removeGroup()">
			   </TD>			   
			   <td valign="top" align="center">
			        <html:select property="availableGroups" size="6" style="width: 250" styleId="selectedSecurityGroup">
			           <html:options collection="adminGroups" property="groupId" labelProperty="displayText" />
			        </html:select>			        
			   </td>
			   </tr>
			   </tbody>
			   </table>
			   </FIELDSET>
			   </td>
			</tr>
			<% if (adminUserForm.isExistingUser()) { %>
			<TR>	
				<TD colspan="3"><html:checkbox property="changePassword" styleId="passwordcb" onclick="processCheckbox();" >Change password</html:checkbox></TD>
			</TR>
			<% } %>						
			<TR id="passwordrow" style="<%= passwordRowStyle %>">
				<TH class="settingstext" align="left">Password</TH>
				<TD align="left"><html:password property="textPassword" /></TD>
				<td>&nbsp</td>
			</TR>
			<TR id="confirmpasswordrow" style="<%= passwordRowStyle %>">
				<TH class="settingstext" align="left">Confirm Password</TH>
				<TD align="left"><html:password property="confirmPassword" /></TD>
				<td>&nbsp</td>
			</TR>
			<tr>
			  <td colspan="3">
			     <html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
				   <bean:write name="msg"/>
				 </html:messages>
				 <html:errors />
			  </td>
			</tr>
			<TR>
				<TD colspan="3"><html:submit property="apply" value="Apply" disabled="<%= disableApply %>"  onclick="applyChanges(); return true;" />&nbsp;&nbsp;
				                <html:submit property="delete" value="Delete" disabled="<%= disableDelete %>" onclick="return deleteConfirmation();" />&nbsp;&nbsp;
				                <html:submit property="cancel" value="Cancel" disabled="<%= disableCancel %>"/>&nbsp;&nbsp;
				</TD>
			</TR>
		</TBODY>
	</TABLE>
</html:form>
</BODY>
</html:html>
