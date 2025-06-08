<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="java.util.List" %>
<%@page import="com.honda.galc.handheld.forms.ChooseProcessForm" %>
<%@page import="com.honda.galc.entity.product.LotControlRule" %>
<%@page import="com.honda.galc.entity.product.BaseProduct" %>
<%@page import="com.honda.galc.handheld.data.BuildResultBean" %>
<%@page import="com.honda.galc.handheld.data.BuildResultContainer" %>
<%@page import="com.honda.galc.util.CommonPartUtility" %>
<%@page import="com.honda.galc.handheld.data.HandheldConstants"%>
<%@include file="Handheld.jsp" %>
<html>
	<head>
		<link rel="stylesheet" href="Handheld.css">
		<meta charset="UTF-8" name="viewport" content="width=device-width, height=device-height, initial-scale=1">
		<script src="Handheld.js"></script>
		<title>GALC-Scan Parts</title>
	</head>
	
	<script>
		var partWidgets = [];
		function getActiveFields() {
			var result = [];
			for(var i=0; i<partWidgets.length; i++) {
				var eachWidgetList = partWidgets[i];
				if (eachWidgetList.checkbox != null && eachWidgetList.checkbox.checked)
					result.push(eachWidgetList.field);
			}
			return result;
		}

		function isTorqueOnly() {
			return !hasScanFields() && hasTorqueCheckboxes();
		}
		
		function isScanOnly() {
			return hasScanFields() && !hasTorqueCheckboxes();
		}
		
		function isScanAndTorque() {
			return hasScanFields() && hasTorqueCheckboxes();
		}
		
		function hasTorqueCheckboxes() {
			return getTorqueCheckboxes().length > 0;
		}
		
		function isReadyToSubmitTorques() {
			if (!hasTorqueCheckboxes())
				return true;
			
			var list = getTorqueCheckboxes();
			for(var i=0; i<list.length; i++) {
				var eachCheckbox = list[i];
				if (eachCheckbox.checked)
					return true;
			}
			return false;
		}
		
		function isReadyToSubmit() {
			if (isTorqueOnly())
				if (isReadyToSubmitTorques())
					return true;
			
			if(isScanOnly())
				if (areAllActiveFieldsValid() && hasAtLeastOneScanFieldEnabled())
					return true;
			
			if (isScanAndTorque()) {
				if (hasAtLeastOneScanFieldEnabled())
					if (areAllActiveFieldsValid())
						return true;
				
				if (!hasAtLeastOneScanFieldEnabled())
					if (isReadyToSubmitTorques())
						return true;
			}
			return false;
		}

		function getTorqueCheckboxes() {
			var result = [];
			for(var i=0; i<partWidgets.length; i++) {
				var eachWidgetList = partWidgets[i];
				if (eachWidgetList.torqueCheckbox != null)
					result.push(eachWidgetList.torqueCheckbox);
			}
			return result;
		}

		function handlePageLoad() {
			setTimeoutFromSession(<%=request.getSession().getAttribute(HandheldConstants.TIMEOUT_INTERVAL)%>);
			document.getElementById("0field").focus();
		}

		function findWidgetsAssociatedWith(aWidget) {
			for (var i=0; i< partWidgets.length; i++) {
				var eachWidgetList = partWidgets[i];
				if (eachWidgetList.name == aWidget || eachWidgetList.field == aWidget || eachWidgetList.masksField == aWidget || eachWidgetList.checkbox == aWidget) {
					return eachWidgetList;
				}
			}
			return null;
		}
		
		function findWidgetOfType(aWidget, type) {
				var widgetList = findWidgetsAssociatedWith(aWidget);
				if (type == "name") {
					return widgetList.name;
				} else if (type == "field") {
					return widgetList.field;
				} else if (type == "masksField") {
					return widgetList.masksField;
				} else if (type == "checkbox") {
					return widgetList.checkbox;
				} else if (type == "scanType") {
					return widgetList.scanType;
				} else if (type == "minLength") {
					return widgetList.minLength;
				}
 				return null;
		}
		
		function toggleSubmitButtonEnabling() {
			document.getElementById("submitButton").disabled = !isReadyToSubmit();
		}
		
		function handleCheckboxValueChanged(checkbox) {
			var field = findWidgetOfType(checkbox,"field"); 
			field.disabled = !checkbox.checked;
			field.value = "";
			toggleSubmitButtonEnabling();
		}

		function handleKeyDown(event, field) {
			if(isEnterKey(event))
				handleValueChanged(field);
		}
		
		function isEnterKey(evt) {
			if (!evt) {
				// grab IE event object
				Evt = window.event;
			} else if (!evt.keyCode) {
				// grab NN4 event info
				Evt.keyCode = evt.which;
			}
			return (evt.keyCode == 13);
		}
		
		function doesFieldHaveMinLength(field) {
			return !(getMinFieldLength(field) == -1 || isNaN(getMinFieldLength(field)));			
		}
		
		function getMinFieldLength(field) {
			return findWidgetOfType(field, "minLength").value;
		}
		
		function handleValueChanged(field) {
			if (!findWidgetOfType(field, "checkbox").value) {
				return;
			}
			var errorField = document.getElementById("errorField");
			var maskTag = findWidgetOfType(field, "masksField");
			var testResult = isFieldValueValid(field);
			if (!testResult) {
				field.focus();
				field.select();
			} else {
				handleValidValueIn(field);
			}
			var scanTypeField = findWidgetOfType(field, "scanType");
			errorField.innerHTML = testResult
			? ""
			: ("Input: " + field.value + " Mask: " + 
					(scanTypeField.value == "Cert"
							? "Cert Label Scan"
							: maskTag.value) + 
								(doesFieldHaveMinLength(field)
										? " Min: " + getMinFieldLength(field)
										: ""));
			errorField.hidden = false;
		}

		function isFieldValueMinLengthValid(field) {
			return !doesFieldHaveMinLength(field) 
				|| field.value.length >= parseInt(getMinFieldLength(field));   
		}

		function isFieldValueValid(field) {
			var maskTag = findWidgetOfType(field, "masksField");
			return isFieldValueMinLengthValid(field) 
				&& validateInput(field.value, maskTag.value);
		}

		function areAllActiveFieldsValid() {
			var activeFields = getActiveFields();
			for (var i=0;i< activeFields.length;i++) {
				var eachActiveField = activeFields[i];
				if (!isFieldValueValid(eachActiveField))
					return false;
			}
			return true;
		}

		function hasScanFields() {
			for(var i=0; i<partWidgets.length; i++) {
				var eachWidgetList = partWidgets[i];
				if (eachWidgetList.field != null)
					return true;
			}
			return false;
		}
		
		function hasAtLeastOneScanFieldEnabled() {
			for(var i=0; i<partWidgets.length; i++) {
				var eachWidgetList = partWidgets[i];
				if (eachWidgetList.checkbox != null && eachWidgetList.checkbox.checked)
					return true;
			}
			return false;
		}
		
		function handleValidValueIn(field) {
			var canSubmit = isReadyToSubmit();
			toggleSubmitButtonEnabling();
			
			if(canSubmit) {
				if (!hasTorqueCheckboxes())
					submitForm();
				else
					field.focus();
					field.select();
			} else {
				var nextField = getActiveFieldAfter(field);
				if (nextField != null) {
					field.blur();
					nextField.focus();
				}
			}
		}

		function getFieldIndex(aField) {
			var widgetList = findWidgetsAssociatedWith(aField);
			return partWidgets.indexOf(widgetList);
		}

		function getActiveFieldAfter(aField) {
			var activeFields = getActiveFields();
			if (activeFields.length == 1)
				return null;
			var fieldIndex = activeFields.indexOf(aField);
			return (activeFields[(fieldIndex == activeFields.length - 1)?0:fieldIndex + 1]);
		}
		
		function handleCancelButtonClicked() {
			document.getElementById("cancelRequestHiddenField").value = "cancel";
			submitForm();
		}
		
		function handleSubmitButtonClicked() {
			var flag = isReadyToSubmit();
			if(flag) {
				document.getElementById("submitButtonHiddenField").value = "true";
				submitForm();
			} else {
				toggleSubmitButtonEnabling();
				alert("One or more parts do not match their part mask.\n Please rescan and resubmit");
			}
		}
		
		function preventDefault(event) {
			try {
				event.preventDefault();
			} catch (error) {
				//do nothing probably IE
			}
		}

		function handleSubmitRequested(event) {
			var isValidSubmitRequest = !hasTorqueCheckboxes() 
				|| (hasTorqueCheckboxes() && document.getElementById("submitButtonHiddenField").value == "true");
			
			if (!isValidSubmitRequest) {
				document.getElementById("submitButtonHiddenField").value = "false";
				preventDefault(event);
				return false;
			}
			
			if (!areAllActiveFieldsValid()) {
				preventDefault(event);
			}

			document.getElementById("submitButtonHiddenField").value = "false";
			return areAllActiveFieldsValid();
		}
		
	</script>
	
	<h3 id="label">GALC</h3>
	<body onload="handlePageLoad()">
	<%
			ChooseProcessForm chooseProcessForm = (ChooseProcessForm)request.getSession().getAttribute("chooseProcessForm");
			BuildResultContainer buildResultContainerBean = (BuildResultContainer)request.getSession().getAttribute("buildResultContainerBean");
	%>

	<h3 id="stationName"><%= chooseProcessForm.getSelectedProcess().getProcessPointName() %></h3>
	<h3 id="productIdLabel"><%= ((BaseProduct)request.getSession().getAttribute(HandheldConstants.PRODUCT)).getProductId() %></h3>
	<h3 id="mtocLabel"><%= ((BaseProduct)request.getSession().getAttribute(HandheldConstants.PRODUCT)).getProductSpecCode()%></h3>

	<html:form
		styleId="mainForm"
		action="/scanPartsAction"
		name="scanPartsForm"
		type="com.honda.galc.handheld.forms.ScanPartsForm"
		onsubmit="handleSubmitRequested(event)" >
		<html:hidden property="sessionTimedOut" styleId="sessionTimedOutField" />
		<p id= "errorField" style="color:Red;" hidden="true">Error Text</p>
	
		<div style= "height: 80%; max-height: 340px; width: 95%; overflow: scroll; border-style: solid; border-color: black" onclick="setTimeoutDuration()">
		<table id="dataEntryTable" border="1" width="100%" align="left" cellpadding="3">
		<logic:iterate name="buildResultContainerBean" scope="session" property="buildResults" id="eachResult" indexId="index" >
			<tr>
				<td>
					<logic:equal value="true" name="eachResult" property="serialNumberRule">
						<html:hidden name="eachResult" property="partMasks" styleId='<%=index + "masks"%>'/>
						<html:hidden name="eachResult" property="scanType" styleId='<%=index + "scanType"%>'/>
						<html:hidden name="eachResult" property="minLength" styleId='<%=index + "minLength"%>'/>
						
							<bean:write name="eachResult" property="rule.partName.windowLabel"/>
							<br>
							<logic:equal value="Cert" name="eachResult" property="scanType">
								<html:password name="eachResult" property="result" indexed="true"  onkeydown="handleKeyDown(event,this)" onfocus="this.select();" styleId='<%=index + "field"%>'/>
							</logic:equal>
							<logic:notEqual value="Cert" name="eachResult" property="scanType">
								<html:text name="eachResult" property="result" indexed="true"  onkeydown="handleKeyDown(event,this)" onfocus="this.select();" styleId='<%=index + "field"%>'/>
							</logic:notEqual>
							<html:checkbox name="eachResult" property="required" indexed="true" onchange="handleCheckboxValueChanged(this)" tabindex="-1" styleId='<%=index + "checkbox"%>'/>
					</logic:equal>
					<logic:equal value="true" name="eachResult" property="torqueDataCollector">
						<logic:equal value="true" name="eachResult" property="serialNumberRule">
							<br>
							<br>
						</logic:equal>
							<html:checkbox name="eachResult" property="updateTorquesRequested" indexed="true" onchange="toggleSubmitButtonEnabling()" tabindex="-1" styleId='<%=index + "torqueCheckbox"%>'/>
							<bean:write name="eachResult" property="partName"/>
					</logic:equal>	
				</td>
			</tr>
			<script>
				var partName = '<%=index%>';
					partWidgets.push(
						{name: partName,
						field: document.getElementById(partName+"field"),
						masksField: document.getElementById(partName + "masks"),
						checkbox: document.getElementById(partName + "checkbox"),
						scanType: document.getElementById(partName + "scanType"),
						minLength: document.getElementById(partName + "minLength"),
						torqueCheckbox: document.getElementById(partName + "torqueCheckbox")});
			</script>
		</logic:iterate>
	</table>
	</div>	
		<p>
		<html:hidden property="cancelRequested" styleId="cancelRequestHiddenField"/>
		<html:button property="cancelRequested" value="<%=HandheldConstants.CANCEL%>" onclick="handleCancelButtonClicked()" style="margin-left: 30;"/>
		
		<input type="hidden" value="false" id="submitButtonHiddenField"/>
		<html:button property="" value="submit" onclick="handleSubmitButtonClicked()" styleId="submitButton" disabled="true"/>
	</html:form>
</body>
</html>