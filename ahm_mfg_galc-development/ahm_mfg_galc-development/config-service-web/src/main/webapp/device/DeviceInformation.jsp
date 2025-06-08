
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="false" %>
<%@ page import="com.honda.galc.system.config.web.forms.DeviceForm" %>
<%@page import="com.honda.galc.system.config.web.forms.PropertySettingsForm"%>
<%@ page import="com.honda.galc.entity.conf.*" %>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet" type="text/css">
<TITLE>Device Information Configuration</TITLE>
<SCRIPT language="javascript">
function openDeviceDataFormat(ddfname) {
	var ddfurl = "<%=request.getContextPath()%>/deviceDataFormatSettings.do";
	if (ddfname=='clientDDF') {
		ddfurl = ddfurl + "?clientID=" + document.getElementById("clientID").value;
	}
	if (ddfname=='replyDDF') {
		ddfurl = ddfurl + "?clientID=" + document.getElementById("replyClientID").value;
	}
	devicewindow = window.open(ddfurl,'devicedataformatwindow','width=1050,height=560,resizable=yes,status=yes');
	
	devicewindow.focus();
	
}
/* This function is not used - buttons were obsoleted at some point */
function updateDDFButtonLabel() {
	if (document.all("clientID").value == "") {
		document.all("clientDDF").value="New Device Data Format";
	}else{
		document.all("clientDDF").value="Modify Device Data Format";
	}
	if (document.all("replyClientID").value == "") {
		document.all("replyDDF").value="Create Device Data Format";
	}else{
		document.all("replyDDF").value="Modify Device Data Format";
	}
}

function confirmDeletion()
{
   clientID = document.getElementById("clientID").value;
   
   result =  window.prompt("To confirm the deletion of this device, enter the client ID below.","");
   
   if (result == clientID)
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
</HEAD>
<%
 DeviceForm deviceForm = (DeviceForm)request.getAttribute("deviceForm");
 
 // Get the flag that determines if certain fields are read-only
 String existingflagString = (String)request.getAttribute("existingflag");
 boolean existingFlag = false;
 if (existingflagString != null && existingflagString.equalsIgnoreCase("true"))
 {
    existingFlag = true;
 }
 // Determine the style class to apply to possible readonly fields
 String readonlyclass = "";
 if (existingFlag)
 {
    readonlyclass = "readonly";
 }
 
 %>
<BODY class="settingspage">
<H1 class="settingsheader">Device Information Config</H1>
<table border="0" width="100%">
<tbody>
<tr>
<td align="left">
<html:form action="/deviceSettings" name="deviceForm" type="com.honda.global.galc.system.config.web.forms.DeviceForm" scope="request">
    <html:hidden property="createFlag" />
    <html:hidden property="divisionID" />
    <html:hidden property="deleteConfirmed" />
<bean:define id="strEditor" property="editor" name="deviceForm"/>
<%

Boolean isEditor = (Boolean)pageContext.getAttribute("strEditor");
boolean editorFlag = !isEditor.booleanValue();


boolean showFormats = true;

if (deviceForm.isCreateFlag() || deviceForm.isDeleteConfirmed())
{
  showFormats = false;
}
%>
	<TABLE border="0">
		<TBODY>
			<TR>
				<TH align="left" class="settingstext">Client ID *</TH>
				<TD>
					<html:text property="clientID" styleId="clientID" readonly="<%= existingFlag %>" styleClass="<%=readonlyclass %>" size="32" maxlength="32"/>
					&nbsp;
<!--			    		<%if (showFormats) { %>-->
<!--					<td>-->
<!--					<input type="button" name="clientDDF" value="Device Data Format" onclick="openDeviceDataFormat(this)"/>-->
<!--					</td>-->
<!--					<%} %>-->
				<% if (existingFlag) {
				 
				
				      String propertiesURL = 
				         request.getContextPath()+"/propertySettings.do?staticComponentID=true&componentID="+deviceForm.getClientID() +
								                              "&componentType="+PropertySettingsForm.COMPONENT_TYPE_APPLICATION;
				 %>
                 [<a class="settingspagelink" href="<%=propertiesURL %>">Device Properties</a>]
				 <%
				 }
				 %>
				 
				 </TD>
			
			</TR>
			<TR>
				<TH align="left" class="settingstext">Reply Client ID:</TH>
				<TD><html:text property="replyClientID" styleId="replyClientID" size="32" maxlength="32"/>&nbsp;</td>
				
<!--				<%if (showFormats) { %>-->
<!--				<td>-->
<!--				<input type="button" name="replyDDF" value="Device Data Format" onclick="openDeviceDataFormat(this)"/>-->
<!--				</td>-->
<!--				<%} %>-->
				
			</TR>
			<TR>
				<TH align="left" class="settingstext">Device Type</TH>
				<TD>
					<bean:define id="deviceTypeVOs" property="deviceTypeMap" type="java.util.TreeMap" name="deviceForm" />
					<html:select property="deviceType">
						<html:options collection="deviceTypeVOs" property="key" labelProperty="value"/>
					</html:select>
				</TD>
			</TR>			
			<TR>
				<TH align="left" class="settingstext">EIF IPAddress</TH>
				<TD><html:text property="eifIPAddress" /></TD>
			</TR>			
			<TR>
				<TH align="left" class="settingstext">EIF Port</TH>
				<TD><html:text property="eifPort" /></TD>
			</TR>			
			<TR>
				<TH align="left" class="settingstext">I/O Process Point ID</TH>
				<TD><html:text property="ioProcessPointID" /></TD>
			</TR>			
			<TR>
				<TH align="left" class="settingstext">Alias Name</TH>
				<TD><html:text property="aliasName" size="32" maxlength="64"/></TD>
			</TR>			
			<TR>
				<TH align="left" class="settingstext">Description</TH>
				<TD><html:text property="deviceDescription" size="32" maxlength="128"/></TD>
			</TR>		
			<tr>
				<th>&nbsp;</th>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>	
			
			<TR>
				<TD colspan="3" align="center">
					<html:submit property="apply" value="Apply" disabled="<%=editorFlag%>"/>&nbsp;&nbsp;
					<html:submit property="apply" value="Delete" disabled="<%=editorFlag%>" onclick="return confirmDeletion();"/>&nbsp;&nbsp;
					<html:submit property="cancel" value="Cancel" />
				</TD>	
			</TR>
			
		</TBODY>
	</TABLE>
  </td>
  <td valign="top">
     <FIELDSET class="settingstext"><LEGEND class="settingstext">Related configurations</LEGEND>
     <table >
        <tbody >
<% if (deviceForm.getClientID() != null && deviceForm.getClientID().length() > 0 ) { %>
          <tr>
            <td ><a href="" class="settingspagelink" onclick="openDeviceDataFormat('clientDDF'); return false;"/>Device Data Format for <%= deviceForm.getClientID() %></a></td>
          </tr>
<%} %>        
<% if (deviceForm.getReplyClientID() != null && deviceForm.getReplyClientID().length() > 0 ) { %>
          <tr>
            <td><a href="" class="settingspagelink" onclick="openDeviceDataFormat('replyDDF'); return false;"/>Device Data Format for <%= deviceForm.getReplyClientID() %></a></td>
          </tr>
<%} %>          
<% if (deviceForm.getIoProcessPointID() != null && deviceForm.getIoProcessPointID().length() > 0) { 
		 String ppLink = "processPointSettings.do?processPointID="+deviceForm.getIoProcessPointID();
%>     

			<tr>
			<td><a class="settingspagelink" href="<%= ppLink %>">Process Point Settings for <%= deviceForm.getIoProcessPointID() %></a>
			</td>
			</tr>
<% }  
%>
<% String opcLink = null; 
   if (deviceForm.getOpcConfigurationDataList() != null && deviceForm.getOpcConfigurationDataList().size() > 0) {
       if (deviceForm.getOpcConfigurationDataList().size() == 1) {
       
           OpcConfigEntry data = (OpcConfigEntry)deviceForm.getOpcConfigurationDataList().get(0);
           opcLink = "opcSettings.do?load=load&newInstance=false&id="+data.getId();
       }
       else
       {
       	   opcLink = "opcConfigurationList.do?fetchList=fetch&deviceID="+deviceForm.getClientID();
       }
   }
   
   if (opcLink != null) {
%>
   <tr>
      <td><a class="settingspagelink" href="<%= opcLink %>">OPC Settings for <%= deviceForm.getClientID() %></a></td>
   </tr>
<% } %>

        </tbody>
        
        
     </table>
     </FIELDSET>
  </td>
  </tr>
</tbody>
</table>
</html:form>
<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
   <bean:write name="msg"/>
</html:messages>
<html:errors/>
</BODY>
</html:html>
