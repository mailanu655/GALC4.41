<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="com.honda.galc.system.config.web.forms.TerminalForm" %>
<%@ page import="com.honda.galc.system.config.web.forms.PropertySettingsForm"%>
<%@ page import="com.honda.galc.entity.conf.*" %>
<%@ page import="com.honda.galc.entity.enumtype.*" %>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>Terminal Information Configuration</TITLE>
<SCRIPT language="javascript">
function boldText(sObjectID) {
// USED TO BOLDEN THE TEXT OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
	document.getElementById(sObjectID).style.background='#cccccc';
}

function normalText(sObjectID) {
// USED TO BOLDEN THE TEXT OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
	document.getElementById(sObjectID).style.background='#dddddd';
}


function showProcessPoint() {
   clearMessages();	
   boldText('ProcessPointTab');
   normalText('PORDTab');
   normalText('TeamLeaderTab');
   normalText('QICSTab');
   submitApplcationForm(1);
}
function showPORD() {
   clearMessages();	
   boldText('PORDTab');
   normalText('ProcessPointTab');
   normalText('TeamLeaderTab');
   normalText('QICSTab');
   submitApplcationForm(2);
}
function showTeamLeader() {
   clearMessages();	
   boldText('TeamLeaderTab');
   normalText('PORDTab');
   normalText('ProcessPointTab');
   normalText('QICSTab');
   submitApplcationForm(3);
}
function showQICS() {
   clearMessages();	
   boldText('QICSTab');
   normalText('PORDTab');
   normalText('TeamLeaderTab');
   normalText('ProcessPointTab');
   submitApplcationForm(4);
}
function submitApplcationForm(paraAppType)
{
	var appDoc = window.frames['itemList'].document; 
	var appForm = appDoc.forms[0];
	var appType = appDoc.getElementById('applicationType');
	appType.value = paraAppType;
	var currType = document.getElementById('currentApplicationType');
	currType.value = paraAppType;
	var currFilter = document.getElementById('txtFilter');
	var appFilter = appDoc.getElementById('applicationFilter');
	appFilter.value = currFilter.value;
	appType.value =  currType.value;
	appForm.submit();
}
window.onload = function(e)
{	
	document.getElementById('txtFilter').onkeydown = function(evt)
	{
		if(evt.which === 13 || evt.keyCode === 13)
		{
		   	var appDoc = window.frames['itemList'].document; 
			var appForm = appDoc.forms[0];
			var appType = appDoc.getElementById('applicationType');
			var currType = document.getElementById('currentApplicationType');
			var currFilter = document.getElementById('txtFilter');
			var appFilter = appDoc.getElementById('applicationFilter');
			appFilter.value = currFilter.value;
			appType.value =  currType.value;
			appForm.submit();
			return false;
		}
		else{ return true; }
	}
}
function clearMessages() {
	var msgs = document.getElementById("messages_and_errors");
	if(msgs != null && msgs.firstChild != null) {
		msgs.firstChild.data = "";
	}
}
function doAddApplication() {
	clearMessages();	
	var appDoc = window.frames['itemList'].document; 
	var appType = document.getElementById('currentApplicationType');

	var appItemsContainer = appDoc.getElementById('appItems');
	var chkItems = appItemsContainer.getElementsByTagName('input');
	var selectedIndex = -1;
	
	if (chkItems == null)
		return false;
	//get the checked item
	for(i=0;i<chkItems.length;i++) {
		if(chkItems[i].checked) {
			selectedIndex = i;
			break;
		}
	}	
	
	//if no item is selected, ignore it.
	if (selectedIndex == -1)
		return false;
	
	var trItem = appDoc.getElementById("appItem" + selectedIndex);
	
	var trContents = trItem.getElementsByTagName('TD');
	
	
	var appItemID = trContents[1].firstChild.data;
	
	//check the item had been added or not
	var isExist = false;
	appItemsContainer = document.getElementById('applicationBody');
	var extApps = appItemsContainer.getElementsByTagName('input');
	if (extApps != null) {
		for(i=0;i<extApps.length;i++) {
			if(extApps[i].value == appItemID)
				isExist = true;
		}
	}
	//if added already, ignore it.
	if (isExist) 
		return false;
	
	var appItemName = trContents[2].firstChild.data;
	var appItemScreen = trContents[4].firstChild.data;
	var appTypeId = trContents[5].firstChild.data;
	var appTypes = new Object();
	<% for (ApplicationType at : ApplicationType.values()) { %>
		appTypes[<%=at.getId() %>] = '<%= at.getTypeString() %>';	
	<% } %>
	var appItemType = appTypes[appTypeId];
			
	var oBody = document.getElementById("applicationBody");
	var oTr = document.createElement("tr");
	var oTdChkSeq = document.createElement("td");
	var oTdMulSeq = document.createElement("td");
	oTdMulSeq.align="center";
	var oTdappType = document.createElement("td");
	var oTdappID = document.createElement("td");
	var oTdappName = document.createElement("td");
	var oTdscrID = document.createElement("td");
	
	var appCounter;
	if(extApps == null) {
		appCounter = 1;
	}else{
		appCounter = extApps.length + 1;
	}

	oTdChkSeq.innerHTML = "<input type=\"radio\" name=\"applicationItem\" value=\"" + appCounter + "\"><input type=\"hidden\" name=\"applicationID\" value=\"" + appItemID + "\">";
	oTdMulSeq.innerHTML = "<input type=\"checkbox\" onclick=\"doSelectDefault(this)\" name=\"defaultApplication\" value=\"" + appItemID + "\">";
	if (appItemType == " ")
		appItemType = "&nbsp;"
	oTdappType.innerHTML = appItemType;
	if (appItemID == " ")
		appItemID = "&nbsp;"
	oTdappID.innerHTML = appItemID;
	if (appItemName == " ")
		appItemName = "&nbsp;"
	oTdappName.innerHTML = appItemName;
	if (appItemScreen == " ")
		appItemScreen = "&nbsp;"
	oTdscrID.innerHTML = appItemScreen;
	
	oTr.appendChild(oTdChkSeq);
	oTr.appendChild(oTdMulSeq);
	oTr.appendChild(oTdappType);
	oTr.appendChild(oTdappID);
	oTr.appendChild(oTdappName);
	oTr.appendChild(oTdscrID);
	oBody.appendChild(oTr);
}
function doDeleteApplication() {
    clearMessages();	
	var oBody = document.getElementById("applicationBody");

	var chkItems = oBody.getElementsByTagName("input");
	var selectedIndex = -1;
	var chkIndex = -1;
	if (chkItems == null)
		return false;
		
	//get the checked item
	for(i=0; i<chkItems.length; i++) {
		if(chkItems[i].name == "applicationItem") {
			chkIndex ++;
			if(chkItems[i].checked) {
				selectedIndex = chkIndex;
				break;
			}
		}
	}	
	if (selectedIndex == -1)
		return false;
		
	oBody.deleteRow(selectedIndex + 1);
}
function doSelectDefault(curChk) {
	if (curChk.checked) {
		var defaultChks = document.all("defaultApplication");
		if (defaultChks.length != null && defaultChks.length>1 ) {
			for(i=0;i<defaultChks.length;i++) {
				defaultChks[i].checked = false;
			}
		}
		curChk.checked = true;
	}
}

function confirmDeletion()
{
   
   
   hostName = document.getElementById("hostName").value;
   
   
   result =  window.prompt("To confirm the deletion of this terminal, enter the host name below.","");
   
   if (result == hostName)
   {
      document.getElementById("deleteConfirmed").value = "true";
      
      return true;
   }
   else
   {
       alert("Terminal deletion not confirmed properly");
   }
   
   return false;
}

function displayApplication(applicationID) {
   newurl = "<%=request.getContextPath() %>/applicationSettings.do?applicationID="+applicationID;
   window.open(newurl, "Application Setting:"+applicationID, "height=600,width=900");
}

</SCRIPT>
</HEAD>

<BODY class="settingspage">
<html:form action="/terminalSettings">
<html:hidden property="divisionID" styleId="divisionID"/>
<html:hidden property="createFlag" styleId="createFlag"/>
<html:hidden property="deleteConfirmed" styleId="deleteConfirmed"/>
<bean:define id="strEditor" property="editor" name="terminalForm"/>
<%
TerminalForm terminalForm = (TerminalForm)request.getAttribute("terminalForm");
if (terminalForm == null)
{
   terminalForm = new TerminalForm();
}

String terminalID = terminalForm.getHostName();

Boolean isEditor = (Boolean)pageContext.getAttribute("strEditor");
boolean editorFlag = !isEditor.booleanValue();
String aHostName = (String)pageContext.getAttribute("strHostName");
boolean showDetail = false;

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
<logic:notEmpty property="hostName" name="terminalForm">
<%showDetail=true; %>
</logic:notEmpty>
<logic:equal property="createFlag" value="true" name="terminalForm">
<%showDetail=true; %>
</logic:equal>
<%if (showDetail) { %>
<H1 class="settingsheader">Terminal Information Config</H1>
<input type="hidden" name="currentApplicationType" id="currentApplicationType">
	<TABLE width="100%" border="0" cellpadding="0" cellspacing="0">
		<TBODY>
			<TR>
				<TD>

				<TABLE border="0" width="100%">
					<TBODY>
						<TR>
							<TH align="left" class="settingstext">Host Name *</TH>
							<TD><html:text property="hostName"  styleId="hostName" size="32" readonly="<%= existingFlag %>" styleClass="<%=readonlyclass %>"></html:text></TD>
						    <TH align="left" class="settingstext">Type</TH>
							<TD>
								<bean:define id="terminalTypes" name="terminalForm" property="terminalTypeMap" type="java.util.TreeMap"/>
								<html:select property="afTerminalFlag">
									<html:options collection="terminalTypes" property="key" labelProperty="value"/>
								</html:select>
				             </TD>
							<TD rowspan="4" valign="top" align="right">
							<FIELDSET class="settingstext"><LEGEND class="settingstext">Related configurations</LEGEND>
							<table border="0" width="100%">
							  <tbody>
								<tr><td><html:link action="/appMenuSetting" paramId="clientID" paramName="terminalForm" paramProperty="hostName" styleClass="settingspagelink">Terminal Application Menu</html:link></td></tr>
								
								<%
								if (existingFlag) {							
								    String ref= request.getContextPath()+"/propertySettings.do?staticComponentID=true&componentID="+terminalID +
								                              "&componentType="+PropertySettingsForm.COMPONENT_TYPE_TERMINAL;
								 %>
								<tr><td><a class="settingspagelink" href="<%=ref %>">Terminal Properties</a></td></tr>
								<% } %>
							  </tbody>
							</table>
							</FIELDSET>
							</TD>
						</TR>
						<TR>
							<TH align="left" class="settingstext">IP Address</TH>
							<TD><html:text property="ipAddress" maxlength="15"/><html:checkbox property="autoUpdateIpFlag">Auto Update</html:checkbox></TD>
							<TH align="left" class="settingstext">Port</TH>
							<TD><html:text property="port"/></TD></TR>
						<TR>
							<TH align="left" class="settingstext">Process Point ID</TH>
							<TD><html:text property="locatedProcessPointID" maxlength="16"/></TD>
							<TH align="left" class="settingstext">Router Port</TH>
							<TD><html:text property="routerPort" /></TD></TR>
						<TR>
							<TH align="left" class="settingstext">Description</TH>
							<TD colspan="3"><html:text property="terminalDescription" style="width=300" maxlength="128"/></TD></TR>
					</TBODY>
				</TABLE>

				</TD>
			</TR>
			<TR>
				<TD>
					<TABLE border="1">
						<TBODY>
							<TR>
								<TD><span style="background:#dddddd" id="ProcessPointTab" onclick="showProcessPoint();">Process Point</span></TD>
								<TD><span style="background:#dddddd" id="PORDTab" onclick="showPORD();">PORD Application</span></TD>
								<TD><span style="background:#dddddd" id="TeamLeaderTab" onclick="showTeamLeader();">Team Leader Application</span></TD>
								<TD><span style="background:#dddddd" id="QICSTab" onclick="showQICS();">QICS Station</span></TD>
							</TR>
						</TBODY>
					</TABLE>
				</TD>
			</TR>
			<TR>
				<TD>
					<IFRAME id="itemList" name="itemList" src="<%=request.getContextPath() %>/terminalApplicationItemList.do" width="100%"></IFRAME>
				</TD>
			</TR>
			<TR>
				<TD>
					<TABLE>
						<TBODY>
							<TR>
								<TD><input type="button" name="add" value="Add" onclick="doAddApplication()"/></TD>
								<TD><input type="button" name="delete" value="Delete" onclick="doDeleteApplication()"/></TD>
								<TD>
									<span style="padding-left:10px;">
										<input type="text" id="txtFilter" placeholder="Press Enter key for search... " size="35" maxlength="150" />																				
									</span>
								</TD>
							</TR>
						</TBODY>
					</TABLE>
				</TD>
			</TR>
			<TR>
				<TD>
				<DIV width="100%" style="height=150; overflow:auto;">
				<TABLE width="100%" border="1" cellpadding="0" cellspacing="0">
					<TBODY id="applicationBody">
						<TR>
							<TH width="20">&nbsp;</TH>
							<TH align="center">Default</TH>
							<TH>ApplictionType</TH>
							<TH>ApplicationID</TH>
							<TH>ApplicationName</TH>
							<TH>ScreenID</TH>
						</TR>
						<%
							int index=1;
							for(ApplicationByTerminal appByTerm : terminalForm.getApplicationByTerminalList()) {
						 %>
						<TR>
							<TD>
								<input type="radio" name="applicationItem" value="<%=index %>"/>
								<input type="hidden" name="applicationID" value="<%=appByTerm.getId().getApplicationId() %>"/>
							</TD>
							<TD align="center">
							<input type="checkbox" name="defaultApplication" onclick="doSelectDefault(this)" value="<%=appByTerm.getId().getApplicationId() %>" <%if (appByTerm.isDefaultApplication()) out.print("checked=\"checked\"");%> >
							</TD>
							<%
							String applicationType = appByTerm.getApplication().getApplicationType().getTypeString();
							ApplicationType appType = appByTerm.getApplication().getApplicationType();
							%>
							<TD><%=applicationType %></TD>
							<% if(appType.isProcessPointApplicationType()) { %>
							        <TD><a href="processPointSettings.do?processPointID=<%=appByTerm.getId().getApplicationId() %>"><%=appByTerm.getId().getApplicationId() %> </a></TD>
							<%} else if(applicationType.equals("Team Leader")) {  %>
									<TD><a href="javascript:displayApplication('<%=appByTerm.getId().getApplicationId() %>');"><%=appByTerm.getId().getApplicationId() %></a></TD>
							<%} else { %>
									<TD><%=appByTerm.getId().getApplicationId() %></TD>
							<%} %>
														
							<TD><%=appByTerm.getApplication().getApplicationName() %></TD>
							<TD><%=appByTerm.getApplication().getScreenId() %></TD>
						</TR>
						<%index++; 
						  }
						%>
					</TBODY>
				</TABLE>
				</DIV>
				</TD>
			</TR>
			<TR>
				<TD>&nbsp;</TD>
			</TR>
			<TR>
				<TD align="center">
					<html:submit property="operation" value="Apply" disabled="<%=editorFlag%>"></html:submit>
					<html:submit property="operation" value="Delete" disabled="<%=editorFlag%>" onclick="return confirmDeletion();"></html:submit>
					<html:submit property="operation" value="Cancel" />
				</TD>
			</TR>
		</TBODY>
	</TABLE>
<%} %>
</html:form>
<span id="messages_and_errors">
<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
   <bean:write name="msg"/>
</html:messages>
<html:errors/>
</span>
</BODY>
</html:html>