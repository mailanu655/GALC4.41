
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="com.honda.galc.enumtype.ScreenAccessLevel"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.ACLForm" %>
<%@ page import="com.honda.galc.entity.conf.*" %>
<%@ page import="java.util.*" %>
<%@ page session="false" %>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>Screen ACL Information</TITLE>
</HEAD>
<%
  //Place the group list in the request
  ACLForm aclForm = (ACLForm)request.getAttribute("aclForm");
  
  if (aclForm == null)
  {
     aclForm = new ACLForm();
  }
  request.setAttribute("groups",aclForm.getGroups());
  
  boolean disableApply = !aclForm.isEditor();
  boolean disableRemove = !aclForm.isEditor();
 %>
<BODY class="settingspage">
<h1 class="settingsheader">Screen Access Control List</h1>
<html:form action="/ACLSettings" name="aclForm" type="com.honda.global.galc.system.config.web.forms.ACLForm" scope="request">

    <table width="100%">
    <tbody>
    <tr>
    <td align="left" valign="top">
    <fieldset class="settingstext">
    <LEGEND class="settingstext">Add a New ACL Entry</LEGEND>
	<TABLE border="0">
		<TBODY>
		    <TR>
				<TH class="settingstext" align="left">Application ID</TH>
				<TD><html:text property="applicationID" readonly="true" styleClass="readonly"/></TD>
			</TR>
			<TR>
				<TH class="settingstext" align="left">Application Name</TH>
				<TD><html:text property="applicationName" readonly="true" styleClass="readonly"/></TD>	
			</TR>			
			<TR>
				<TH class="settingstext" align="left">Screen ID</TH>
				<TD><html:text property="screenID" readonly="true" styleClass="readonly"/></TD>
			</TR>	
			<TR>
				<TH class="settingstext" align="left">Security Group</TH>
				<TD><html:select property="securityGrp">
				      <html:options collection="groups" property="securityGroup" labelProperty="displayText" />
				    </html:select>
				</TD>
			</TR>					
			<TR>
				<TH class="settingstext" align="left">Operation</TH>
				<TD><html:select property="operation" >
				       <html:option value="1">Full Access</html:option>
				      <html:option value="0">No Access</html:option>
				      <html:option value="2">Read Only</html:option>
				    </html:select>
				</TD>
			</TR>
			<TR>
				<TD colspan="2">
				   <html:submit property="apply" value="Apply" disabled="<%= disableApply %>"/> &nbsp;&nbsp;
				   <html:submit property="clear" value="Clear" />
				</TD>
			</TR>
		</TBODY>
	</TABLE>
	</fieldset>
	</td>
	<td align="right" valign="top">
<FIELDSET class="settingstext">
   <LEGEND class="settingstext">Existing ACL Entries for <%= aclForm.getScreenID() %></LEGEND>
   <table>
   <tbody>
   <tr>
   <td>
   <table border="1">
     <THEAD>
       <tr>
       <th>
       </th>
       <th>Security Group</th>
       <th>Group Name</th>
       <th>Operation</th>
       </tr>
     </THEAD>
     <tbody>
<%
  List<AccessControlEntry> aclEntries = aclForm.getAclData();
  
  List<SecurityGroup> groups = aclForm.getGroups();
  
  if (aclEntries.size() == 0)
  {
     disableRemove = true;
  }
  
  for(AccessControlEntry aclData : aclEntries) {
      
      String operationText = "No Access";
      
      if (aclData.getOperation() == ScreenAccessLevel.FULL_ACCESS.getId())
      {
         operationText = "Full Access";
      }
       else if (aclData.getOperation() == ScreenAccessLevel.READ_ONLY.getId())
      {
         operationText = "Read Only";
      }
      else if (aclData.getOperation() == ScreenAccessLevel.NO_ACCESS.getId())
      {
         operationText = "No Access";
      }
      
      String grpName = "&nbsp;";
      
      for(SecurityGroup sgdata : groups) {
         
         if (sgdata.getSecurityGroup().equals(aclData.getId().getSecurityGroup())) {
            grpName = sgdata.getGroupName();
         }
      }
      
  
 %>
 <tr>
   <td><html:radio property="removeSecurityGroup" value="<%= aclData.getId().getSecurityGroup() %>"><%= aclData.getId().getSecurityGroup() %></html:radio></td>
   <td><%= aclData.getId().getSecurityGroup() %></td>
   <td><%= grpName %></td>
   <td><%= operationText %></td>
 </tr>
 <%
  }
  %>     
     </tbody>
   </table>
   </td>
   </tr>
   <tr>
   <td><html:submit property="remove" value="Remove" disabled="<%= disableRemove %>"/></td>
   </tr>
   </tbody>
   </table>
</FIELDSET>

   </td>
   </tr>
   <tr>
     <td colspan="2">
        <html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE"><bean:write name="msg"/></html:messages>
	    <html:errors/>	
     </td>
   </tr>
   </tbody>
   </table>
	
</html:form>



</BODY>
</html:html>
