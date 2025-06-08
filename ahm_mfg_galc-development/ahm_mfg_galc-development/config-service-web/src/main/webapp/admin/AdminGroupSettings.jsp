
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.AdminGroupForm" %>
<%@ page session="false" %>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>AdminGroupSettings.jsp</TITLE>
<script>

function show(sObjectID) {
		// USED TO SHOW AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		
		document.getElementById(sObjectID).style.display='';
}
		
function hide(sObjectID) {
			
		// USED TO HIDE AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		document.getElementById(sObjectID).style.display='none';
}



function deleteConfirmation() {
 
    var groupid = document.getElementById("adminGroupForm").groupID.value;
    
    var result = prompt("To delete the group, enter the group ID in the field below:","");
    
    if (result != groupid)
    {
       alert("Group will not be deleted");
       
       return false;
    }
    
    document.getElementById("adminGroupForm").deleteConfirm.value="true";
    return true;

}

function refreshGroupList() {

   parent.frames['resultiframe'].window.document.adminGroupSearchForm.submit();
}

</script>
</HEAD>
<%
    AdminGroupForm adminGroupForm = (AdminGroupForm)request.getAttribute("adminGroupForm");
    
    boolean readonlyID = false;
    String readonlyClass = "";
    
    String passwordRowStyle = "";
    
    
    if (adminGroupForm.isExistingGroup())
    {
      readonlyID = true;
      readonlyClass = "readonly";
      
    }
    
    boolean disableApply = false;
    boolean disableDelete = false;
    boolean disableCancel = false;
    
    if (!adminGroupForm.isEditor())
    {
       disableApply = true;
       disableDelete = true;
       
    }
    
    if (adminGroupForm.isDeleteConfirm())
    {
       // Must have already deleted
       disableApply = true;
       disableDelete = true;       
       disableCancel = true;
    }
    
    if (!adminGroupForm.isExistingGroup())
    {
       disableDelete = true;
       disableCancel = true;
    }
    
    
    
    // Determine if we want to refresh the parent list
    String onloadcmd ="";
    
    if (adminGroupForm.isRefreshList())
    {
       onloadcmd="refreshGroupList()";
    }
 %>
<BODY class="settingspage" onload="<%=onloadcmd %>" >
<html:form styleId="adminGroupForm" action="/adminGroupSettings" name="adminGroupForm"   scope="request" type="com.honda.galc.system.config.web.forms.AdminGroupForm">
    <html:hidden property="initializePage" />
    <html:hidden property="existingGroup" />
    <html:hidden property="deleteConfirm"/>
	<TABLE border="0">
		<TBODY>
			<TR>
				<TH class="settingstext" align="left">Group ID</TH>
				<TD align="left"><html:text property="groupID" readonly="<%= readonlyID %>" styleClass="<%= readonlyClass %>" size="32" maxlength="32"/></TD>
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
			     <html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
				   <bean:write name="msg"/>
				 </html:messages>
				 <html:errors />
			  </td>
			</tr>
			<TR>
				<TD colspan="3"><html:submit property="apply" value="Apply" disabled="<%= disableApply %>" />&nbsp;&nbsp;
				                <html:submit property="delete" value="Delete" disabled="<%= disableDelete %>" onclick="return deleteConfirmation();" />&nbsp;&nbsp;
				                <html:submit property="cancel" value="Cancel" disabled="<%= disableCancel %>"/>&nbsp;&nbsp;
				</TD>
			</TR>
		</TBODY>
	</TABLE>
</html:form>
</BODY>
</html:html>
