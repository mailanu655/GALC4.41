<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.BroadcastDestinationForm" %>
<%@ page import="com.honda.galc.entity.enumtype.DestinationType" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page session="false" %>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>Broadcast Destination</TITLE>
<script>
	var $destinationType;
	var $destinationId;
	var $requestId;
	var $arguments;
	var $externalDevice;
	var $form;
	var $findAll;
	var $findInDivision;

	window.onload = function()
	{
		$destinationType = document.getElementById('destinationType');
		$destinationId = document.getElementById('destinationID');
		$requestId = document.getElementById('requestID');
		$arguments = document.getElementById('argument');
		$externalDevice = document.getElementById('externalDeviceID');
		$form = document.broadcastDestinationForm;
		$findAll = document.getElementById('findAll');
		$findInDivision = document.getElementById('findInDivision');	
	}
	
	//Events	
	function destionationTypeSelectionChange(){		
		fnFindAll();
	}
	
	function fnFindAll(){
		$findAll.value = 1;
		$findInDivision.value = 0;
		submitForm();
	}
	
	function fnFindInDivision(){
		$findAll.value = 0;
		$findInDivision.value = 1;
		submitForm();
	}
	
	function destionationSelectionChange(){
		if($destinationType.options[$destinationType.selectedIndex].text == '<%= DestinationType.External %>'){
			submitForm();
		};
	}

	function submitForm(){
		$form.submit();
	}
	

	function doCancel() {
		if (window.opener && !window.opener.closed) {
			window.opener.closeBroadcastDestinationWindow();
			return;
		}
		window.close();
	}

	function doOK() {
		var destinationTypeText = $destinationType.options[$destinationType.selectedIndex].text;   
				
		if ($form.destinationID && trimIt($form.destinationID.value) === '') {
			alert(document.getElementById('destinationLbl').innerText + ' is required');
			$form.destinationID.focus();
			return false;
  		}
	  	
	  		
		if ($form.requestID && trimIt($form.requestID.value) === '') {
			alert(document.getElementById('requestLbl').innerText + ' is required');
			$form.requestID.focus();
			return false;
  		}
  		
  		if(document.getElementById('argumentRequired') && trimIt($form.argument.value) === ''){
  			alert(document.getElementById('argumentLbl').innerText + ' is required');
			$form.argument.focus();
			return false;
  		}

        condition = trimIt($form.condition.value);
        conditionType = trimIt($form.conditionType.value);
        if(condition !== ''  &&  conditionType === ''){
            alert('Condition Type is required when Condition is provided');
            $form.conditionType.focus();
            return false;
        }
        if (condition === '' && conditionType !== '') {
            conditionType = '';
        }
        
	   	if (window.opener && !window.opener.closed) {	   
			sequenceNo = $form.sequenceNo.value;
			operation = $form.operation.value;
			processPointID = $form.processPointID.value;
			destinationType = $form.destinationType.value;
			destinationID = $form.destinationID.value;
			requestID = ($form.requestID) ? $form.requestID.value : '';
			arguments = $form.argument.value;
			autoEnabled = $form.autoEnabled.checked ? 'true' : 'false';
			checkPoint = $form.checkPoint ? $form.checkPoint.value : '' ;
	        window.opener.applyBroadcastDestinationSettings(operation, processPointID, sequenceNo, destinationType, destinationID, requestID, arguments, autoEnabled, checkPoint, condition, conditionType);
	    }
	    else
	    {
	       window.close();
	    }
	}

	//Utils methods
	function trimIt(aString) {
		// RETURNS INPUT ARGUMENT WITHOUT ANY LEADING OR TRAILING SPACES
		return (aString.replace(/^ +/, '')).replace(/ +$/, '');
	}

	function show(sObjectID) {
		// USED TO SHOW AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY

		document.getElementById(sObjectID).style.display = '';
	}

	function hide(sObjectID) {
		// USED TO HIDE AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY

		document.getElementById(sObjectID).style.display = 'none';
	}

	function toggleVisibility(sObjectID) {
		// USED TO TOGGLE VISIBILITY OF AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY

		if (document.getElementById(sObjectID) == null) {
			return;
		}

		if (document.getElementById(sObjectID).style.display == 'none') {
			show(sObjectID);

			return "show";
		} else {
			hide(sObjectID);

			return "hide";
		}
	}
</script>
</HEAD>
<BODY class="settingspage">
	<html:form name="broadcastDestinationForm" action="/broadcastDestinationSettings" type="com.honda.global.galc.system.config.web.forms.BroadcastDestinationForm" scope="request">
	    <html:hidden property="sequenceNo" />
	    <html:hidden property="operation" />
	    <html:hidden property="processPointID"/>
	    <html:hidden property="findAll" styleId="findAll"/>
	    <html:hidden property="findInDivision" styleId="findInDivision"/>
		<TABLE border="0">
			<TBODY>
				<TR>
					<TH align="left" class="settingstext" nowrap>Destination Type</TH>
					<td>
						<html:select property="destinationTypeID" onchange="destionationTypeSelectionChange()" styleId="destinationType" >
						   <html:option value="0">Equipment</html:option>
						   <html:option value="1">Printer</html:option>
						   <html:option value="2">Terminal</html:option>
						   <html:option value="3">Application</html:option>
						   <html:option value="4">External</html:option>
						   <html:option value="5">Notification</html:option>
						   <html:option value="6">MQ</html:option>
						   <html:option value="7">DeviceWise</html:option>
						   <html:option value="9">Server Task</html:option>
						   <html:option value="10">FTP</html:option>
						   <html:option value="11">MQMANIFEST</html:option>
						</html:select>
					</td>
					<td nowrap>
						<button type="button" onclick="fnFindAll();">Find All</button>&nbsp;&nbsp;
					    <button type="button" onclick="fnFindInDivision();">Find in Division</button>					    	    
					</td>
				</TR>
				<TR>
					<TD colspan="3" style="color: red;">
						 <html:errors/>
					</TD>
				</TR>
				<TR>
					<TD colspan="2">
						<SPAN CLASS="requiredFieldLabel">*</SPAN> - Required Field
					</TD>
				</TR>
				<%-- All comparison with Enum destinationType was performed to strings due to struts do not support compare with another enum values --%>
				<TR>
					<c:choose>
						<c:when test="${broadcastDestinationForm.destinationType == 'Terminal'}">
							<c:set var="destinationLbl" value="Host Name"/>
						</c:when>
						<c:when test="${broadcastDestinationForm.destinationType == 'Application'}">
							<c:set var="destinationLbl" value="Process Point ID"/>
						</c:when>
						<c:when test="${broadcastDestinationForm.destinationType == 'External'}">
							<c:set var="destinationLbl" value="Service Name"/>
						</c:when>		
						<c:otherwise>
							<c:set var="destinationLbl" value="Client ID"/>
						</c:otherwise>
					</c:choose>
					<TH align="left" class="settingstext">
						<label id="destinationLbl"><c:out value="${destinationLbl}"/></label><SPAN CLASS="requiredFieldLabel">*</SPAN>
					</TH>
					<TD colspan="2">
						<c:set var="destinations" value="${broadcastDestinationForm.destinations}"/>
						<html:select property="destinationID" styleId="destinationID" style="width:210px;" onchange="destionationSelectionChange();"> 
				       		<html:options collection="destinations" property="value" labelProperty="label"/>
					    </html:select>
					</TD>
				</TR>
				<c:choose>
					<c:when test="${
						broadcastDestinationForm.destinationType == 'Printer' 
						|| broadcastDestinationForm.destinationType == 'External' 
						|| broadcastDestinationForm.destinationType == 'Notification' 
						|| broadcastDestinationForm.destinationType == 'FTP' 
					}">
						<TR>
							<c:choose>
								<c:when test="${broadcastDestinationForm.destinationType == 'External'}">
									<c:set var="requestLbl" value="Client ID"/>
								</c:when>	
								<c:otherwise>
									<c:set var="requestLbl" value="Form ID"/>
								</c:otherwise>	
							</c:choose>
							<TH align="left" valign="top" class="settingstext">
								<label id="requestLbl"><c:out value="${requestLbl}"/></label><SPAN CLASS="requiredFieldLabel">*</SPAN>				
							</TH>
							<TD colspan="2">
								<c:set var="requests" value="${broadcastDestinationForm.requests}"/>
								<html:select property="requestID" style="width:210px;" styleId="requestID" >
						       		<html:options collection="requests" property="value" labelProperty="label"/>
							    </html:select>
							</TD>
						</TR>		
					</c:when>								
				</c:choose>					
				<TR>
					<c:choose>
						<c:when test="${broadcastDestinationForm.destinationType == 'External'}">
							<c:set var="argumentLbl" value="Method Name"/>
							<c:set var="isArgRequired" value="true"/>
						</c:when>	
						<c:when test="${broadcastDestinationForm.destinationType == 'MQ'}">
							<c:set var="argumentLbl" value="Assembler Name"/>
							<c:set var="isArgRequired" value="true"/>
						</c:when>	
						<c:otherwise>
							<c:set var="argumentLbl" value="Argument"/>
							<c:set var="isArgRequired" value="false"/>
						</c:otherwise>	
					</c:choose>
					<TH align="left" valign="top" class="settingstext">
						<label id="argumentLbl"><c:out value="${argumentLbl}"/></label><c:if test="${isArgRequired == 'true'}"><SPAN CLASS="requiredFieldLabel" id="argumentRequired">*</SPAN></c:if>					
					</TH>
					<TD colspan="2">
						<c:choose>
							<c:when test="${not empty broadcastDestinationForm.arguments}">
								<c:set var="arguments" value="${broadcastDestinationForm.arguments}"/>
								<html:select property="argument" style="width:210px;" styleId="argument">
						       		<html:options collection="arguments" property="value" labelProperty="label"/>
							    </html:select>
							</c:when>	
							<c:otherwise>
								<html:text property="argument" styleId="argument" size="32" maxlength="32"/>
							</c:otherwise>	
						</c:choose>
					</TD>
				</TR>
				<tr>
					<TH align="left" class="settingstext" id="autoEnabledIdLabel">
						Auto Enabled
					</TH>
					<td colspan="2">
						<div id="autoEnabledDiv">
							<html:checkbox property="autoEnabled"></html:checkbox>
						</div>
					</td>
				</tr>
                <tr>
                    <TH align="left" class="settingstext" id="checkPoint">
                        Check Point
                    </TH>
                    <td colspan="2">
                        <div id="checkPointDiv">
                            <html:select property="checkPoint" style="width:257px;">
                                <html:options name="broadcastDestinationForm" property="checkPoints"  />    
                            </html:select>
                        </div>
                    </td>
                </tr>				
                <tr>
                    <TH align="left" class="settingstext" id="condition">
                        Condition
                    </TH>
                    <td colspan="2">
                        <div id="conditionDiv">
                            <html:text  property="condition" size="32" maxlength="250"/>
                        </div>
                    </td>
                </tr>
                <tr>
                    <TH align="left" class="settingstext" id="conditionType">
                        Condition Type
                    </TH>
                    <td colspan="2">
                        <div id="conditionTypeDiv">
                            <html:select property="conditionType" style="width:257px;">
                                <html:options name="broadcastDestinationForm" property="conditionTypes"  />    
                            </html:select>
                        </div>
                    </td>
                </tr>

				
				<TR>
					<TD colspan="3">
						<button type="button" id="okButton" onclick="doOK()">OK</button>&nbsp;&nbsp;
						<button type="button" onclick="doCancel();">Cancel</button>									
					</TD>
				</TR>
			</TBODY>
		</TABLE>
	</html:form>
</BODY>
</html:html>
