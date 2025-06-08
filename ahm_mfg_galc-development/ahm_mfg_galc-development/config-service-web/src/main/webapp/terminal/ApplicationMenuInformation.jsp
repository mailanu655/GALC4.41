
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.AppMenuForm" %>
<%@ page import="com.honda.galc.entity.conf.*" %>
<%@ page import="java.util.*" %><% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>Application Menu Information</TITLE>
<SCRIPT language="javascript">
function openTerminalApplicationList() {
	var ddfurl = "<%=request.getContextPath()%>/terminalApplicationList.do";
	ddfurl = ddfurl + "?terminalHostName=" + document.all("clientID").value;
	window.open(ddfurl,'terminalapplicationwindow','width=750,height=500,scrollbars=yes,resizable=yes,status=yes');
	
}

function confirmDeletion() {

   
   nn = document.getElementById("menuForm").nodeNumber.value;
   
   result = confirm("Are you sure you want to delete node "+nn);
   
   if (result)
   {
      document.getElementById("menuForm").confirmDelete.value = "true";
   }
   
   return result;
}
</script>


<%
  AppMenuForm formData = (AppMenuForm)request.getAttribute("appMenuForm");
  
  List<ApplicationMenuEntry> childNodesList = null;
  List<ApplicationMenuEntry> allNodeList = null;
  
  ApplicationMenuEntry currentParent = null; 
  
  boolean editorFlag = false;
  boolean deleteDisableFlag = false;
  
  if (formData != null)
  {
     editorFlag = !formData.isEditor();
     
     if (formData.isEditor() && formData.isDeletable())
     {
        deleteDisableFlag = true;
     }
     
     currentParent = formData.getCurrentParent();
     childNodesList= formData.getCurrentChildList();
     allNodeList = formData.getApplicationMenuDataNodes();
     
  }
  
 %>
</HEAD>

<BODY class="settingspage">

<% if (formData != null) { %>
<h1 class="settingsheader">Application Menu Information for Terminal <%= formData.getClientID() %></h1>
<%} %>

<% if (currentParent != null) { %>

<table>
<tbody>
<tr>
<td colspan="2">
<table border="1">
<THEAD>
  <tr>
  <th colspan="5">Current Node</th>
  </tr>
  <tr>
    <th>Display Text</th>
    <th>Node Name</th>
    <th>Node Number</th>
    <th>Parent Node<br>Number</th>
  </tr>
</THEAD>
<tbody>
  <tr>
    <td><%= currentParent.getApplicationName() %></td>
    <td><%= currentParent.getNodeName() %></td>
    <td><%= currentParent.getId().getNodeNumber() %></td>
    <td><%= currentParent.getParentNodeNumber() %></td>
    <td>
    <%
      if (currentParent.getId().getNodeNumber() != 0 && currentParent.getApplication() != null) {
      %>
    <a class="settingspagelink" href="<%=  "appMenuSetting.do?clientID="+currentParent.getId().getClientId()+"&currentDisplayParent="+currentParent.getId().getNodeNumber()+"&modifyNode="+currentParent.getId().getNodeNumber() %>">Settings...</a>
    &nbsp;&nbsp;
    <a class="settingspagelink" href="<%=  "appMenuSetting.do?clientID="+currentParent.getId().getClientId()+"&currentDisplayParent="+currentParent.getParentNodeNumber() %>">View parent...</a>
        &nbsp;
     <% } 
    
    if (currentParent.getApplication() == null) {
    %>
    <a class="settingspagelink" href="<%=  "appMenuSetting.do?newChild=true&clientID="+currentParent.getId().getClientId()+"&currentDisplayParent="+currentParent.getId().getNodeNumber() %>" >Add child...</a>
      <%
      }
     %>
     <td>
  </tr> 
</tbody>  
</table>
</td>
<td>&nbsp;</td>
</tr>
<tr>
<td>&nbsp;</td>
<td>
<table border="1">
  <THEAD>
  <tr>
  <th colspan="5">Children</th>
  </tr>
  <tr>
    <th>Display Text</th>
    <th>Node Name</th>
    <th>Node Number</th>
    <th>Parent Node Number</th>
    <th></th>
  </tr>
  </THEAD>
  <tbody>
  <%
     for(ApplicationMenuEntry node : childNodesList) {
         List<ApplicationMenuEntry> subNodes = new ArrayList<ApplicationMenuEntry>();
         for(ApplicationMenuEntry subNode : allNodeList) {
         	if(node.getId().getNodeNumber()== subNode.getParentNodeNumber()) subNodes.add(subNode);
         };
  %>
     <tr>
    <td><%= node.getApplicationName() %></td>
    <td><%= node.getNodeName() %></td>
    <td><%= node.getId().getNodeNumber() %></td>
    <td><%= node.getParentNodeNumber() %></td>     
    <td>
    <a href="<%=  "appMenuSetting.do?clientID="+node.getId().getClientId()+"&currentDisplayParent="+currentParent.getId().getNodeNumber()+"&modifyNode="+node.getId().getNodeNumber() %>">Settings...</a>
    &nbsp;&nbsp;
    <%if(!node.isApplication()) { %>
    <a href="<%=  "appMenuSetting.do?clientID="+node.getId().getClientId()+"&currentDisplayParent="+node.getId().getNodeNumber() %>">View children...</a>
    <% } %>
    </td>
     </tr>     
  <% 
     }
   %>
  </tbody>
</table>
</td>
</tr>
</tbody>
</table>
<% } %>
<html:form styleId="menuForm"  name="appMenuForm" type="com.honda.global.galc.system.config.web.forms.AppMenuForm" scope = "request" action="/appMenuSetting">
<html:hidden property="modifyNode" name="appMenuForm"/> 
<html:hidden property="currentDisplayParent" name="appMenuForm"/> 
<html:hidden property="createFlag" name="appMenuForm"/>
<html:hidden property="confirmDelete" name="appMenuForm"/>
<%
boolean showPanel = formData.isShowSetting(); 
String showStyle = null;
if (showPanel) {
	showStyle = "visible";
}else{
	showStyle = "hidden";
}

boolean disableNumber = true;
String classNumber = "readonly";
if (formData.getCreateFlag() != null && formData.getCreateFlag().equalsIgnoreCase("true"))
{
  disableNumber = false;
  
  deleteDisableFlag = true;
  
  classNumber = "";
}

%>
	<TABLE id="nodeInfo" border="0" style="visibility: <%=showStyle %>;">
		<TBODY>
			<TR>
				<TH class="settingstext" align="left" onclick="return func_1(this, event);">Client ID</TH>
				<TD><html:text property="clientID" readonly="true" styleClass="readonly"/></TD>
			</TR>
			<TR>
				<TH class="settingstext" align="left">Node Number</TH>
				<TD><html:text property="nodeNumber" readonly="<%= disableNumber %>" styleClass="<%= classNumber %>"/></TD>
			</TR>
			<TR>
				<TH class="settingstext" align="left">Parent Node Number</TH>
				<TD><html:text property="parentNodeNumber" readonly="true" styleClass="readonly"/></TD>
			</TR>
			<TR>
				<TH class="settingstext" align="left">Display Text</TH>
				<TD><html:text property="displayText" readonly="true" styleClass="readonly" /></TD>
			</TR>
			<TR>
				<TH class="settingstext" align="left">Node Name</TH>
				<TD><html:text property="nodeName" /></TD>
				<td><button onclick="return openTerminalApplicationList()">Select from Terminals Application List...</button></td>
			</TR>
			<TR>
                 <TD align="center" colspan="2">
					<html:submit property="apply" value="Apply" disabled="<%=editorFlag%>"></html:submit>&nbsp;&nbsp;&nbsp;
					<html:submit property="delete" value="Delete" disabled="<%=deleteDisableFlag%>"  onclick="return confirmDeletion();"/>&nbsp;&nbsp;&nbsp;
					<html:submit property="operation" value="Cancel" />
				</TD>
			</TR>
			<tr>
			 <td colspan="2">
				<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
				   <bean:write name="msg"/>
				</html:messages>
				<html:errors/>			 
			 </td>
			</tr>
		</TBODY>
	</TABLE>
	
<%
if (!showPanel) {
%>
<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
 <bean:write name="msg"/>
 </html:messages>
 <html:errors/>			 

<%
}
 %>	
</html:form>
</BODY>
</html:html>
