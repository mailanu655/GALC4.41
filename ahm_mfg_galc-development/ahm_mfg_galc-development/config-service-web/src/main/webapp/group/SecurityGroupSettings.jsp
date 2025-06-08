
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.SecurityGroupForm" %>
<%@ page session="false" %>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>SecurityGroupSettings.jsp</TITLE>
<script>

function show(sObjectID) {
		// USED TO SHOW AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		
		document.getElementById(sObjectID).style.display='';
}
		
function hide(sObjectID) {
			
		// USED TO HIDE AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		document.getElementById(sObjectID).style.display='none';
}

function verifyInput() {
	var groupId = document.getElementById("securityGroupForm").groupID.value;

	if(groupId == "") {
		alert("Please enter a group ID.");
		return false;
	}

	var groupName = document.getElementById("securityGroupForm").groupName.value;
	if(groupName == "") {
		alert("Please enter a group Name.");
		return false;
	}
	return true;
}

function deleteConfirmation() {
 
    var groupid = document.getElementById("securityGroupForm").groupID.value;
    
    var result = prompt("To delete the group, enter the group ID in the field below:","");
    
    if (result != groupid)
    {
       alert("Group will not be deleted");
       
       return false;
    }
    
    document.getElementById("securityGroupForm").deleteConfirm.value="true";
    return true;

}

function refreshGroupList() {

   parent.frames['resultiframe'].window.document.securityGroupSearchForm.submit();
}

</script>
</HEAD>
<%
    SecurityGroupForm securityGroupForm = (SecurityGroupForm)request.getAttribute("securityGroupForm");
    
    boolean readonlyID = false;
    String readonlyClass = "";
    
    if (securityGroupForm.isExistingGroup())
    {
      readonlyID = true;
      readonlyClass = "readonly";
      
    }
    
    boolean disableApply = false;
    boolean disableDelete = false;
    boolean disableCancel = false;
    
    if (!securityGroupForm.isEditor())
    {
       disableApply = true;
       disableDelete = true;
       
    }
    
    if (securityGroupForm.isDeleteConfirm())
    {
       // Must have already deleted
       disableApply = true;
       disableDelete = true;       
       disableCancel = true;
    }
    
    if (!securityGroupForm.isExistingGroup())
    {
       disableDelete = true;
       disableCancel = true;
    }
    
    // Determine if we want to refresh the parent list
    String onloadcmd ="";
    
    if (securityGroupForm.isRefreshList())
    {
       onloadcmd="refreshGroupList()";
    }
 %>
<BODY class="settingspage" onload="<%=onloadcmd %>" >
<html:form styleId="securityGroupForm" action="/securityGroupSettings" name="securityGroupForm"   scope="request" type="com.honda.global.galc.system.config.web.forms.SecurityGroupForm">
    <html:hidden property="initializePage" />
    <html:hidden property="existingGroup" />
    <html:hidden property="deleteConfirm"/>
	<TABLE border="0">
		<TBODY>
			<TR>
				<TH class="settingstext" align="left">Group ID</TH>
				<TD align="left"><html:text property="groupID" readonly="<%= readonlyID %>" styleClass="<%= readonlyClass %>" size="32" maxlength="255"/></TD>
				<td>&nbsp;</td>
			</TR>		
			<TR>
				<TH class="settingstext" align="left">Group Name</TH>
				<TD align="left"><html:text property="groupName" size="48" maxlength="64"/></TD>
				<td>&nbsp;</td>
			</TR>
			<TR>
				<TH class="settingstext" align="left">Description</TH>
				<TD align="left"><html:text property="displayText" size="48" maxlength="64" readonly="true" styleClass="readonly"/></TD>
				<td>&nbsp;</td>
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
				<TD colspan="3"><html:submit property="apply" value="Apply" disabled="<%= disableApply %>" onclick="return verifyInput()" />&nbsp;&nbsp;
				                <html:submit property="delete" value="Delete" disabled="<%= disableDelete %>" onclick="return deleteConfirmation();" />&nbsp;&nbsp;
				                <html:submit property="cancel" value="Cancel" disabled="<%= disableCancel %>"/>&nbsp;&nbsp;
				</TD>
			</TR>
		</TBODY>
	</TABLE>
</html:form>
</BODY>
</html:html>
