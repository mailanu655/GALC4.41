<!DOCTYPE HTML>
<%@page import="com.honda.galc.handheld.data.HandheldConstants"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@include file="Handheld.jsp" %>
<html>
	<head>
		<link rel="stylesheet" href="Handheld.css">
		<meta charset="UTF-8">
		<title>GALC - Choose Process</title>
	</head>
	<body onload="handlePageLoad()">
		<h1 id="label">GALC</h1>

		<html:form
			onreset="handlePageLoad()"
			styleId="mainForm"
			action="/chooseProcessAction"
			name="chooseProcessForm"
			type="com.honda.galc.handheld.forms.ChooseProcessForm">
			<html:hidden property="sessionTimedOut" styleId="sessionTimedOutField" />
			<table>
				<tbody>
					<tr>
						<td><h3>Division:</h3></td>
					</tr>
					<tr>
						<td>
							<html:select property="selectedDivision" name="chooseProcessForm" styleId="divisionDropdownList" onchange="handleDivisionChanged(this.value)" onclick="setTimeoutDuration()">
								<html:option value=""></html:option>
								<html:options property="availableDivisions"/>
							</html:select>
						</td>
					</tr>
					<tr>
						<td><h3>Zone:</h3></td>
					</tr>
					<tr>
						<td>
							<html:select property="selectedZone" name="chooseProcessForm" styleId="zoneDropdownList" onchange="handleZoneChanged(this.value)" onclick="setTimeoutDuration()">
								<html:option value=""></html:option>
								<html:options property="availableZones"/>
							</html:select>
						</td>
					</tr>
					<tr>
						<td><h3>Process:</h3></td>
					</tr>
					<tr>
						<td>
								<html:select property="selectedProcessPointId" styleId="processDropdownList" onclick="setTimeoutDuration()">
									<html:optionsCollection  name="chooseProcessForm" property="processesForSelectedZone" label="processPointName" value="processPointId"/>
								</html:select>
						</td>
					</tr>
				</tbody>
			</table>
			<p>
			<html:hidden property="mbpnOnRequseted" styleId="mbpnOnRequestHiddenField"/>
			<input id="btnSubmit" type="submit" value="Submit" disabled = "true">
			<input id="btnMBPNOn" type="button" value="MBPN ON" onclick="handleMBPNOnButtonClicked()">
		</html:form>
	</body>
	<script src="Handheld.js"></script>
	<script>
		function handleMBPNOnButtonClicked() {
			document.getElementById("mbpnOnRequestHiddenField").value = "true";
			submitForm();
		}
		
		function handleDivisionChanged(departmentSelection) {
			var
				disabled = getDropDownSelectionFrom("divisionDropdownList") == "0",
				dropdownList = document.getElementById("zoneDropdownList");
			dropdownList.disabled = disabled;
			handleZoneChanged();
		}
		
		function handleZoneChanged(zoneSelection) {
			var
				disabled = getDropDownSelectionFrom("zoneDropdownList") == "0",
				dropdownList = document.getElementById("processDropdownList");
			dropdownList.disabled = disabled;
			handleProcessChanged();
			submitForm();
		}

		function handleProcessChanged() {
			var 
				selectedProcess = getDropDownSelectionFrom("processDropdownList");
				disabled = selectedProcess == null || selectedProcess == "0";
			document.getElementById("btnSubmit").disabled = disabled;
		}
		
		function handlePageLoad() {
			setTimeoutFromSession(<%=request.getSession().getAttribute(HandheldConstants.TIMEOUT_INTERVAL)%>);
			var divisionOptions =document.getElementById("divisionDropdownList").options; 
			if (divisionOptions.length == 1 && divisionOptions[0].value == "") {
				submitForm();
				return;
			}
			if (getDropDownSelectionFrom("divisionDropdownList") == "") {
				document.getElementById("divisionDropdownList").focus();
			} else if (getDropDownSelectionFrom("zoneDropdownList") == "") {
				document.getElementById("zoneDropdownList").focus();
			} else {
				document.getElementById("processDropdownList").focus();
				handleProcessChanged();
			}
		}
	</script>
</html>