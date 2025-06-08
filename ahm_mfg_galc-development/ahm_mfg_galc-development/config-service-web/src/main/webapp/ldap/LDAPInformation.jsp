<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>User Configuration</TITLE>
<script language="javascript">
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
		agnGroup.add(newGroup, null);	// standard complient
	}
	catch (ex) {
		agnGroup.add(newGroup);	// MS IE
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
	//validate
   	var userID = document.getElementById("userID").value;
   	var userName = document.getElementById("userName").value;
	var passwd = document.getElementById("passwd").value;
	var conPwd = document.getElementById("cnfPwd").value;
	if(userID == "") {
		alert("Please enter a user ID.");
		return false;
	}
	if(userName == "") {
		alert("Please enter a user name.");
		return false;
	}
	if ( passwd != conPwd ) {
		alert("Wrong Confirm Password");
		return false
	}
	var agnGroup = document.getElementById("assignedSecurityGroup");
	if (agnGroup.length > 0) {
		for (i=0; i<agnGroup.length;i++) {
			agnGroup.options[i].selected = true;
		}
	}
	return true;
}

function confirmDeletion()
{
   var userID = document.getElementById("userID").value;
   result =  window.prompt("To confirm the deletion of this user, enter the user ID below.","");
   
   if (result == userID)
   {
      document.getElementById("deleteConfirmed").value = "true";
      return true;
   }
   else
   {
       alert("User deletion not confirmed properly");
   }
   
   return false;
}

function refreshUserList() {
	if (document.all('freshUserList').value == 'true') {
		//refresh the user list
		var userListFrame = top.frames['lDAPListFrame'].frames['userListIFrame'];
		userListFrame.location.reload(true);
	}
}
</script>
</HEAD>

<BODY class="settingspage" onload="refreshUserList()">
<html:form action="/lDAPSettings">
<html:hidden property="createFlag" />
<html:hidden property="freshUserList" />
<html:hidden property="oldPassword" />
<html:hidden property="deleteConfirmed" styleId="deleteConfirmed"/>
<bean:define id="strEditor" property="editor" name="lDAPForm"/>
<%
Boolean isEditor = (Boolean)pageContext.getAttribute("strEditor");
boolean editorFlag = !isEditor.booleanValue();
boolean showDetail = false;

boolean readonlyID = false;
String readonlyClass = "";
%>
<logic:notEmpty property="userID" name="lDAPForm">
<bean:define id="strOpt" property="operation" name="lDAPForm"/>
<%
String operaiton = (String)pageContext.getAttribute("strOpt");
if (operaiton != null && operaiton.equalsIgnoreCase("query")) {
	readonlyID = true;
	readonlyClass = "readonly";
}
%>
</logic:notEmpty>
<logic:notEmpty property="userID" name="lDAPForm">
<%showDetail=true; %>
</logic:notEmpty>
<logic:equal property="operation" value="Create" name="lDAPForm">
<%showDetail=true; %>
</logic:equal>
<%if (showDetail) {%>
	<TABLE border="0">
		<TBODY>
				<!-- User information operate panel -->
				<logic:notEmpty property="operation" name="lDAPForm">
			<TR>
				<TD valign="top" align="left">


					<table border="0">
						<tbody>
							<tr>
								<th align="left" class="settingstext">User ID</th>
								<td><html:text property="userID" styleId="userID" name="lDAPForm" readonly="<%= readonlyID %>" styleClass="<%= readonlyClass %>"/></td>
							</tr>
							<tr>
								<th align="left" class="settingstext">User Name</th>
								<td><html:text property="userName" styleId="userName" name="lDAPForm" /></td>
							</tr>
							<tr>
								<th align="left" class="settingstext">Password</th>
								<td><html:password property="passwd" styleId="passwd" name="lDAPForm" /></td>
							</tr>
							<tr>
								<th align="left" class="settingstext">Confirm Password</th>
								<td><html:password property="cnfPwd" styleId="cnfPwd" name="lDAPForm" /></td>
							</tr>
							<tr>
								<th align="left" class="settingstext">Password Updated</th>
								<td><html:text property="pwdUpdatedDate" name="lDAPForm"
									readonly="true" /></td>
							</tr>
							<tr>
								<th align="left" class="settingstext">Expire days</th>
								<td><html:text property="expireDays" name="lDAPForm" /></td>
							</tr>
						</tbody>
					</table>


					</td>
					<td align="right">
						<a href="#" class="settingspagelink" onclick="window.open('securityGroupSettings.do?initializeFrame=true','groupwindow','width=750,height=600,resizable=yes,status=yes')">[Security Groups]</a>
					</td>
					</tr>
					<tr>
					<td colspan="2">


					<TABLE border="0">
						<TBODY>
							<TR>
								<TD valign="top" align="center">
								<FIELDSET class="settingstext" style="width: 260">
									<LEGEND class="settingstext">Assigned Security Group</LEGEND>
									<html:select property="assignedSecurityGroup" styleId="assignedSecurityGroup" multiple="true" size="12" style="width: 260">
									<logic:notEmpty property="userSecurityGroups" name="lDAPForm">
										<bean:define id="securityGroupItems" property="userSecurityGroups" name="lDAPForm"></bean:define>
										<html:options collection="securityGroupItems" property="securityGroupId" labelProperty="groupName"/>
									</logic:notEmpty>
									</html:select>
								</FIELDSET>
								</TD>
								<TD align="center"><div align="center">
									<input type="button" value="&nbsp;<<&nbsp;" style="width: 60;" onclick="addGroup()">
									<P></P>
									<input type="button" value="&nbsp;>>&nbsp;" style="width: 60;" onclick="removeGroup()">
									</div>
								</TD>
								<TD valign="top" align="center">
								<FIELDSET class="settingstext" style="width: 260">
									<LEGEND class="settingstext">Group List</LEGEND>
									<html:select property="selectedSecurityGroup" styleId="selectedSecurityGroup" size="12" style="width: 260">
									<logic:notEmpty property="securityGroups" name="lDAPForm">
										<bean:define id="securityGroupItems" property="securityGroups" name="lDAPForm"></bean:define>
										<html:options collection="securityGroupItems" property="securityGroup" labelProperty="groupName"/>
									</logic:notEmpty>
									</html:select>
								</FIELDSET>
								</TD>
							</TR>
							<TR>
								<TD colspan="3" align="center">
									<html:submit property="operation" value="Apply" onclick="return applyChanges()" disabled="<%=editorFlag%>"></html:submit>&nbsp;&nbsp;
									<html:submit property="operation" value="Delete" disabled="<%=editorFlag%>" onclick="return confirmDeletion();"/>&nbsp;&nbsp;
									<html:submit property="operation" value="Cancel" />
								</TD>
							</TR>
						</TBODY>
					</TABLE>

				</TD>
			</TR>

				</logic:notEmpty>
				<!-- End of User information operate panel -->
		</TBODY>
	</TABLE>
<%} %>
</html:form>
<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
   <bean:write name="msg"/>
</html:messages>
<html:errors/>
</BODY>
</html:html>
