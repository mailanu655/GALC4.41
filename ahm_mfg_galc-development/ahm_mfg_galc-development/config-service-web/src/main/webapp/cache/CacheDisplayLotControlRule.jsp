<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page import="com.honda.galc.entity.conf.*" %>
<%@ page import="com.honda.galc.entity.product.*" %>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
<TITLE>Application Settings</TITLE>

<script type="text/javascript">
	function boldText(sObjectID) {
	// USED TO BOLDEN THE TEXT OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		document.getElementById(sObjectID).style.background='#cccccc';
	}
	
	function normalText(sObjectID) {
	// USED TO BOLDEN THE TEXT OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		document.getElementById(sObjectID).style.background='#eeeeee';
	}
	
	function show(sObjectID) {
		// USED TO SHOW AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		
		document.getElementById(sObjectID).style.display='';
	}
	
	function hide(sObjectID) {
		
	// USED TO HIDE AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		document.getElementById(sObjectID).style.display='none';
	}
	
	function disableTab(index){
		normalText("tab"+index);
		hide("server"+index);
	}
	
	function enableTab(index){
		boldText("tab"+index);
		show("server"+index);
	}
	
	function clickTab(index) {
	   	var serverListTag = document.getElementById("serverTabs");
	   	var rows = serverListTag.getElementsByTagName("td");
	   	for(i =0; i<rows.length;i++){
	   		if(i == index) enableTab(i);
	   		else disableTab(i);
	   	}
	}
</script>
</HEAD>

<%
  
  Map<String,List<LotControlRule>> lotControlRuleMap = (Map<String,List<LotControlRule>>)request.getAttribute("LOT_CONTROL_RULE");

%>

<BODY class="settingspage" onload="clickTab(0)">
	<table border="1" cellpadding="4">
	<tr id="serverTabs">
	<%
		int index =0;
		for(String item : lotControlRuleMap.keySet()) {
			String tabId = "tab"+index;
	%>
	<td id=<%=tabId%> class="activetab"><span  onclick="clickTab(<%=index%>);"><%=item%></span></td>
	<%
		index++;
		}
	%>
	</tr>
	</table>
	<%
			index =0;
			for(String serverIp : lotControlRuleMap.keySet()) {
				String serverId = "server"+index++;
	%>
		<div id=<%=serverId%>>
		<H2>Lot Control Rule Cache on server <%=serverIp %></H2> 
		<HR>
		<H3>Lot Control Rule</H3>
		<TABLE width="100%" border = "1" style=""border:1px solid black;">
		
			<TR style="display: none;">
				<TR style="background-color: #cccccc">
					<TH>YMTO</TH>
					<TH>SEQ#</TH>
					<TH>PART</TH>
					<TH>SUB ID</TH>
					<TH>VERIFY</TH>
					<TH>SCAN</TH>
					<TH>UNIQUE</TH>
					<TH>INSTALL TIME</TH>
					<TH>INSTRUCTION CODE</TH>
					<TH>DEVICE ID</TH>
					<TH>STRATEGY</TH>
				</TR>
	
				<tbody id="propertyList">
				<%
					for(LotControlRule rule : lotControlRuleMap.get(serverIp)){
				%>
					<TR>
						<TD><%=rule.getId().getYMTO() %></TD>
						<TD><%=rule.getSequenceNumber() %></TD>
						<TD><%=rule.getId().getPartName() %></TD>
						<TD><%=rule.getSubId() %></TD>
						<TD><%=rule.isVerify() %></TD>
						<TD><%=rule.getSerialNumberScanType() %></TD>
						<TD><%=rule.isUnique() %></TD>
						<TD><%=rule.getExpectedInstallTime() %></TD>
						<TD><%=rule.getInstructionCode() %></TD>
						<TD><%=rule.getDeviceId() %></TD>
						<TD><%=rule.getStrategy() %></TD>
					</TR>
				<% 
					} 
				%>
				</tbody> 
			</TR>
		</TABLE>
		<BR>
		<H2>Part Spec</H2>
		<TABLE width="100%" border = "1" style=""border:1px solid black;">
		
			<TR id="PartSpec" style="display: none;">
				<TR style="background-color: #cccccc">
					<TH>MTOC</TH>
					<TH>PART NAME</TH>
					<TH>PART ID</TH>
					<TH>DESCRIPTION</TH>
					<TH>PART MASK</TH>
					<TH>MAX ATTEMPTS</TH>
					<TH>MEASUREMENT COUNT</TH>
					<TH>SN SCAN COUNT</TH>
					<TH>COMMENT</TH>
				</TR>
	
				<tbody id="propertyList">
				 <%
					for(LotControlRule rule : lotControlRuleMap.get(serverIp)){
						List<PartByProductSpecCode> partSpecCodes = rule.getPartByProductSpecs();
						for(PartByProductSpecCode partSpecCode :partSpecCodes){
							PartSpec partSpec = partSpecCode.getPartSpec();
				 %>
					<TR>
						<TD><%=partSpecCode.getId().getYMTO() %></TD>
						<TD><%=partSpec.getId().getPartName() %></TD>
						<TD><%=partSpec.getId().getPartId() %></TD>
						<TD><%=partSpec.getPartDescription() %></TD>
						<TD><%=partSpec.getPartSerialNumberMask() %></TD>
						<TD><%=partSpec.getPartMaxAttempts() %></TD>
						<TD><%=partSpec.getMeasurementCount() %></TD>
						<TD><%=partSpec.getScanCount() %></TD>
						<TD><%=partSpec.getComment() %></TD>
					</TR>
				<% 
						}
					} 
				%>
				</tbody> 
				
			</TR>
			
		</TABLE>
		<BR>
		<H2>Measurement Spec</H2>
		<TABLE width="100%" border = "1" style=""border:1px solid black;">
		
			<TR id="MeasurementSpec" style="display: none;">
				<TR style="background-color: #cccccc">
					<TH>PART NAME</TH>
					<TH>PART ID</TH>
					<TH>SEQ #</TH>
					<TH>MIN LIMIT</TH>
					<TH>MAX LIMIT</TH>
					<TH>MAX ATTEMPTS</TH>
					<TH>COMMENT</TH>
				</TR>
	
				<tbody>
				 <%
					for(LotControlRule rule : lotControlRuleMap.get(serverIp)){
						List<PartByProductSpecCode> partSpecCodes = rule.getPartByProductSpecs();
						for(PartByProductSpecCode partSpecCode :partSpecCodes){
							PartSpec partSpec = partSpecCode.getPartSpec();
							List<MeasurementSpec> measurementSpecs = partSpec.getMeasurementSpecs();
							for(MeasurementSpec spec :measurementSpecs){
				 %>
					<TR>
						<TD><%=spec.getId().getPartName() %></TD>
						<TD><%=spec.getId().getPartId() %></TD>
						<TD><%=spec.getId().getMeasurementSeqNum() %></TD>
						<TD><%=spec.getMinimumLimit() %></TD>
						<TD><%=spec.getMaximumLimit() %></TD>
						<TD><%=spec.getMaxAttempts() %></TD>
					</TR>
				<%
				  			}
				  		}
				  	}
				%>
				</tbody> 
				
			</TR>
		</TABLE>
		</div>
	<%
		} 
	%>
</BODY>
</html:html>
