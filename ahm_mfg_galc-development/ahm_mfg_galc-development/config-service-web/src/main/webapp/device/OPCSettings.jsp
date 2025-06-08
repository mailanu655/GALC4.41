<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page import="com.honda.galc.system.config.web.forms.OpcSettingsForm" %>
<%@ page session="false" %>
<%@ page import="java.lang.String" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<title>OPC Configuration Settings</title>
<SCRIPT>

function openDeviceDataFormat(ddftype) {
   var ddfurl = "<%=request.getContextPath()%>/deviceDataFormatSettings.do";
	if (ddftype=='clientDDF') {
		ddfurl = ddfurl + "?clientID=" + document.getElementById("deviceID").value;
	}
	if (ddftype=='replyDDF') {
		ddfurl = ddfurl + "?clientID=" + document.getElementById("replyDeviceID").value;
	}
	window.open(ddfurl,'devicedataformatwindow','width=1050,height=600,resizable=yes,status=yes')
}

function confirmDeletion()
{
   instanceID = document.getElementById("opcInstanceName").value;
   
   result =  window.prompt("To confirm the deletion of this OPC Entry, enter the OPC Instance Name below.","");
   
   if (result == instanceID)
   {
      document.getElementById("deleteConfirmed").value = "true";
      
      return true;
   }
   else
   {
       alert("Device deletion not confirmed properly");
   }
   
   return false;
}
</SCRIPT>

</head>
<%
  OpcSettingsForm form = (OpcSettingsForm)request.getAttribute("opcSettingsForm");
  if (form == null)
  {
     form = new OpcSettingsForm();
  }
  
  boolean applyFlag = false;
  boolean deleteFlag = false;
  
  if (!form.isEditor()) {
  	applyFlag = true;
  	deleteFlag = true;  
  }
  
  if (form.isNewInstance()) {
   	 deleteFlag = true;
  }
  
  if (form.isDeleteConfirmed()) {
     applyFlag = true;
     deleteFlag = true;
  }
  

  
 %>
<body class="settingspage"> 
<H1 class="settingsheader">OPC Configuration Settings</H1>
<table width="100%">
<tbody>
<tr>
<td align="left">
<html:form styleId="opcSettingsForm" action="/opcSettings"  name="opcSettingsForm" type="com.honda.global.galc.system.config.web.forms.OpcSettingsForm" scope="request">
	<html:hidden property="deleteConfirmed" styleId="deleteConfirmed" value="false"></html:hidden>
	<html:hidden property="newInstance"></html:hidden>
	<table>
		<tbody>
			<tr>
				<th class="settingstext" align="right">Record ID</th>
				<td><html:text property="id" maxlength="32" size="32"
					readonly="true" styleClass="readonly"></html:text></td>
			</tr>

			<tr>
				<th class="settingstext" align="right">OPC Instance name</th>
				<td><html:text property="opcInstanceName" styleId="opcInstanceName" maxlength="32"
					size="32"></html:text></td>
			</tr>
			<tr>
				<th class="settingstext" align="right">Data Ready Tag</th>
				<td colspan="2"><html:text property="dataReadyTag" maxlength="128"
					size="64"></html:text></td>
			</tr>
			<tr>
			   <th class="settingstext" align="right">Read source</th>
			   <td><html:select property="readSource">
			           <html:option value="1">Cache</html:option>
			           <html:option value="2">Device</html:option>
			       </html:select>
			   </td>
			</tr>
			<tr>
				<th class="settingstext" align="right">Enabled</th>
				<td><html:checkbox property="enabled"></html:checkbox></td>
			</tr>
			<tr>
				<th class="settingstext" align="right">Needs Listener</th>
				<td><html:checkbox property="needsListener"></html:checkbox></td>
			</tr>
			<tr>
				<th class="settingstext" align="right">Device ID</th>
				<% if (form.getDeviceList() != null && form.getDeviceList().size() > 0) { 
				
				%>
				<td>
				<html:select property="deviceID" styleId="deviceID">
				   <html:optionsCollection property="deviceList" label="clientId" value="clientId"  />
				</html:select>
				</td>
				 
				<%} else { %> 
				<td><html:text property="deviceID"  styleId="deviceID" size="32" maxlength="32"></html:text></td>
				<td>	
				<html:submit property="selectDevice" value="Select From List" disabled="<%=applyFlag%>" />
				</td>
				<% } %>
				<%-- 
				<% if (form.getDeviceID() != null && form.getDeviceID().length() > 0)  {%>
				<input type="button" name="clientDDF" value="Device Data Format" onclick="openDeviceDataFormat(this)"/>
				<% } %>
				--%>
				
			</tr>
			<tr>
				<th class="settingstext" align="right">Process Point ID</th>
				<td><html:text property="processPointID" size="32" maxlength="32" readonly="true" styleClass="readonly"></html:text></td>
			</tr>			
				
			<tr>
				<th class="settingstext" align="right">Reply Device ID</th>
				<td><html:text property="replyDeviceID" styleId="replyDeviceID" size="32" maxlength="32" readonly="true" styleClass="readonly"></html:text></td>
				<td>
				<%-- 
				<% if (form.getReplyDeviceID() != null && form.getReplyDeviceID().length() > 0)  {%>
				<input type="button" name="replyDDF" value="Device Data Format" onclick="openDeviceDataFormat(this)"/>
				<% } %>
				--%>
				</td>
			</tr>
			<tr>
				<th class="settingstext" align="right">Reply Data Ready Tag</th>
				<td colspan="2"><html:text property="replyDataReadyTag" maxlength="128"
					size="64"></html:text></td>
			</tr>
			<tr>
				<th class="settingstext" align="right">Process Complete Tag</th>
				<td colspan="2"><html:text property="processCompleteTag" maxlength="128"
					size="64"></html:text></td>
			</tr>			
			<tr>
				<th class="settingstext" align="right">Application Server Client Type</th>
				<td><html:select property="serverClientType">
					<html:option value="1">HTTPClient</html:option>
					<html:option value="2">Router Client</html:option>
					<html:option value="3">Embedded Client</html:option>
					<html:option value="4">HTTPServiceClient</html:option>
				</html:select></td>
			</tr>
			<tr>
				<th class="settingstext" align="right">Application Server Client Mode</th>
				<td><html:select property="serverClientMode">
					<html:option value="1">Stateless Transmit</html:option>
					<html:option value="2">Stateless Aysnc Transmit</html:option>
					<html:option value="3">Transmit</html:option>
					<html:option value="4">Transmit Async</html:option>
				</html:select></td>
			</tr>
			<tr>
				<th class="settingstext" align="right">Application Server URL</th>	
				<td colspan="2"><html:text property="serverURL" size="64" maxlength="256"></html:text></td>
			</tr>
			<tr>
				<th class="settingstext" align="right">Listener class</th>	
				<td colspan="2"><html:text property="listenerClass" size="64" maxlength="256"></html:text></td>
			</tr>								
			<tr>
			<td colspan="3">
			<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
   				<bean:write name="msg"/>
				</html:messages>
				<html:errors/>
			</td>
			</tr>
			<TR>
				<TD colspan="3" align="center">
				   <html:submit property="apply" value="Apply" disabled="<%=applyFlag%>" />&nbsp;&nbsp; 
				   <html:submit property="delete" value="Delete" disabled="<%=deleteFlag%>" onclick="return confirmDeletion();" />&nbsp;&nbsp; 
					<html:submit property="cancel" value="Cancel" /></TD>
			</TR>

		</tbody>
	</table>
</html:form>
</td>
<td align="left" valign="top">
<FIELDSET class="settingstext"><LEGEND class="settingstext">Related configurations</LEGEND>
<table>
<tbody>
<tr>
<td><html:link action="/opcConfigurationList" styleClass="settingspagelink">OPC List</html:link>
</td>
</tr>
<% if (form.getDeviceID() != null && form.getDeviceID().length() > 0)  { 
      String deviceLink = "deviceSettings.do?clientID="+form.getDeviceID();
%>
<tr>
<td><a class="settingspagelink" href="<%= deviceLink %>">Device Settings for <%= form.getDeviceID() %></a>
</td>
</tr>
<tr>
<td>
				<a href="" class="settingspagelink" onclick="openDeviceDataFormat('clientDDF'); return false;"/>Device Data Format for <%= form.getDeviceID() %></a>
</td></tr>
<% } %>
<% if (form.getReplyDeviceID() != null && form.getReplyDeviceID().length() > 0)  {%>
<tr>
<td>
				<a href="" class="settingspagelink" onclick="openDeviceDataFormat('replyDDF'); return false"/>Reply Device Data Format for <%= form.getReplyDeviceID() %></a>
</td></tr>
<% } %>				
<% if (form.getProcessPointID() != null && form.getProcessPointID().length() > 0) { 
      String ppLink = "processPointSettings.do?processPointID="+form.getProcessPointID();
%>
<tr>
<td><a class="settingspagelink" href="<%= ppLink %>">Process Point Settings for <%= form.getProcessPointID() %></a>
</td>
</tr>

<% } %>
</tbody>
</table>
</FIELDSET>
</td>
</tr>
</tbody>
</table>
</body>
</html>