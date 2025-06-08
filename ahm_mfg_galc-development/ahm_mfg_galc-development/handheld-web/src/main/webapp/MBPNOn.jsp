<%@page import="com.honda.galc.handheld.forms.MBPNOnForm" %>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@include file="Handheld.jsp" %>
<html>
	<head>
		<link rel="stylesheet" href="Handheld.css">
		<meta charset="UTF-8">
		<title>GALC - MBPN ON</title>
	</head>
	<body onload="handlePageLoad()">
		<h1 id="label">MBPN On</h1>
	<%
		MBPNOnForm mbpnOnForm = (MBPNOnForm)request.getSession().getAttribute("mbpnOnForm");
		if (mbpnOnForm == null)
			mbpnOnForm = new MBPNOnForm();
	%>		

		<html:form
			onreset="handlePageLoad()"
			styleId="mainForm"
			action="/mbpnOnAction"
			name="mbpnOnForm"
			type="com.honda.galc.handheld.forms.MBPNOnForm">
			<html:hidden property="sessionTimedOut" styleId="sessionTimedOutField"/>
			<html:hidden property="mbpnProductIdMasks" styleId="mbpnProductIdMasksHiddenField"/>
			<table>
				<tbody>
					<tr>
						<td style="font-weight: bold">MBPN TYPE:</td>
					</tr>
					<tr>
						<td>
							<html:select property="selectedMBPNPartName" name="mbpnOnForm" styleId="mbpnPartNameDropdownList" onchange="handleMBPNPartNameChanged(this.value)" onclick="setTimeoutDuration()">
								<html:option value=""></html:option>
								<html:optionsCollection property="mbpnOnProcessPoints" label="displayName" value="processPointId"/>
							</html:select>
						</td>
					</tr>
					<tr>
						<td style="font-weight: bold">PRODUCT ID:</td>
					</tr>
					<tr>
						<td>
							<html:text property="productId" name="mbpnOnForm" styleId="productIdField" onchange="handleProductIdChanged(this.value)" onclick="setTimeoutDuration()"/>

<!-- NOTE: The hidden field and checkbox must go together or else the boolean only goes back to the server when "true" -->							
							<html:hidden property="specCodeFromProductId" styleId="specCodeFromIdHiddenField"/>
							<input
								type="checkbox"
								id="specCodeFromProductIdCheckbox"
								onclick="handleSpecCodeFromProductIdCheckboxChanged(this)"
								<logic:equal name="mbpnOnForm" property="specCodeFromProductId" value="true">checked</logic:equal>
							/>
<!-- END NOTE -->
							<html:hidden property="clearMBPNProductSpecCode" styleId="clearMBPNProductSpecCodeHiddenField"/>
						</td>
					</tr>
					<tr>
						<td style="font-weight: bold">MBPN PRODUCT SPEC CODE:</td>
					</tr>
					<tr>
						<td>
							<html:select property="mbpnProductSpecCode" name="mbpnOnForm" styleId="MbpnProductSpecCodesDropdownList" onchange="handleMbpnProductSpecCodeChanged(this.value)" onclick="setTimeoutDuration()">
								<html:option value=""></html:option>
								<html:optionsCollection property="mbpnProductSpecCodes" label="productSpecCode" value="productSpecCode"/>
							</html:select>
						</td>
					</tr>
					<tr>
						<td style="font-weight: bold">
							MBPN PRODUCT ID:
							<p id= "errorField" style="color:Red;" hidden="true">Error Text</p>
						</td>
					</tr>
						<td>
								<html:text property="mbpnProductId" name="mbpnOnForm" styleId="mbpnProductIdField" onchange="handleMbpnProductIdChanged(this.value)" onclick="setTimeoutDuration()"/>
						</td>
					</tr>
				</tbody>
			</table>
			<p>
			<html:hidden property="cancelRequested" styleId="cancelRequestHiddenField"/>
			<html:button  property="cancelRequested" value="<%=HandheldConstants.CANCEL%>" onclick="handleCancelButtonClicked()" style="margin-left: 30;"/>
		</html:form>
	</body>
	<script src="Handheld.js"></script>
	<script>
		function getErrorField() {
			return document.getElementById("errorField"); 
		}
		
		function handleMbpnProductSpecCodeChanged(specCode) {
			getClearMBPNProductSpecCodeHiddenField().value = "false";
			getMbpnProductIdField().value = "";
			submitForm();
		}
		
		function getClearMBPNProductSpecCodeHiddenField() {
			return document.getElementById("clearMBPNProductSpecCodeHiddenField");
		}
		
		function handleMbpnProductIdChanged(userInput) {
			getErrorField().hidden = true;
			if (validateInput(userInput, getMaskListString())) {
				return submitForm();
			}

			getErrorField().innerHTML = getMaskListString();
			getErrorField().hidden = false;
			getMbpnProductIdField().focus();
			getMbpnProductIdField().select();
		}
		
		function getMaskListString() {
			return document.getElementById("mbpnProductIdMasksHiddenField").value;
		}
		
		function handleCancelButtonClicked() {
			document.getElementById("cancelRequestHiddenField").value = "<%=HandheldConstants.CANCEL%>";
			submitForm();
		}

		function getProductIdField() {
			return document.getElementById("productIdField");
		}

		function getMbpnProductIdField() {
			return document.getElementById("mbpnProductIdField");
		}
		
		function getProductSpecCode() {
			return document.getElementById("SelectedMBPNProductSpecCodeLabel").innerHTML;
		}
		
		function getUseProductIdCheckbox() {
			return document.getElementById("specCodeFromProductIdCheckbox");
		}

		function isMbpnPartNameSelected() {
			return getDropDownSelectionFrom("mbpnPartNameDropdownList") != "";
		}
		
		function getMbpnPartNameDropdownList() {
			return document.getElementById("mbpnPartNameDropdownList");
		}

		function handleMBPNPartNameChanged(mbpnPartName) {
			clearWidgetsForNewRequest();
			submitForm();
		}
		
		function clearWidgetsForNewRequest() {
			getProductIdField.value = "";
			getSpecCodeFromIdHiddenField().value = "true";
			getMbpnProductIdField().value = "";
			getUseProductIdCheckbox().value = "true"
			getMbpnProductSpecCodesDropdownList().value = "";
		}
		
		function isProductIdFieldEmpty() {
			return getProductIdField().value == "";
		}
		
		function handleProductIdChanged(productId) {
			if(isProductIdFieldEmpty()) {
				getProductIdField().focus();
				return;
			}
			submitForm();				
		}
		
		function getMbpnProductSpecCodesDropdownList() {
			return document.getElementById("MbpnProductSpecCodesDropdownList");
		}
		
		function getSpecCodeFromIdHiddenField() {
			return document.getElementById("specCodeFromIdHiddenField");
		}
		
		function handleSpecCodeFromProductIdCheckboxChanged(checkbox) {
			getSpecCodeFromIdHiddenField().value = checkbox.checked;
			getMbpnProductIdField().value = "";
			getMbpnProductSpecCodesDropdownList().value = "";
			
			if (!checkbox.checked) {
				getProductIdField().value = "";
				getClearMBPNProductSpecCodeHiddenField().value = "true";
			}
			submitForm();
		}
		
		function isInvalidParentProductId() {
			return getProductIdField().value == "" || <%=((MBPNOnForm)(request.getSession().getAttribute("mbpnOnForm"))).isInvalidParentProductId()%>;
		}
		
		function shouldUseProductId() {
			return getSpecCodeFromIdHiddenField().value == "true";
		}
		
		function handlePageLoad() {
			setTimeoutFromSession(<%=request.getSession().getAttribute(HandheldConstants.TIMEOUT_INTERVAL)%>);
			adjustWidgetStates();
		}
		
		function adjustWidgetStates() {
			getUseProductIdCheckbox().disabled = !isMbpnPartNameSelected();
			getProductIdField().disabled = !isMbpnPartNameSelected() || !shouldUseProductId();
			getMbpnProductSpecCodesDropdownList().disabled = shouldUseProductId();

			if (shouldUseProductId()) { 
				if (isInvalidParentProductId()) {
					getProductIdField().select();
					getProductIdField().focus();
				}
				getMbpnProductSpecCodesDropdownList().style.color = 'blue';
			} else {
				getProductIdField().value = "";
				getMbpnProductSpecCodesDropdownList().style.color = 'black';
			}

			if (getMbpnProductSpecCodesDropdownList().value == "") {
				getMbpnProductIdField().disabled = true;
				getMbpnProductIdField().value = "";
			} else {
				getMbpnProductIdField().disabled = false;
				getMbpnProductIdField().focus();
				getMbpnProductIdField().select();
			}

			if (!isMbpnPartNameSelected()) {
				getMbpnPartNameDropdownList().focus();
			}
			
			if (getMbpnProductIdField().value == "") {
				getErrorField().hidden = true;
			}
		}
	</script>
</html>