<!DOCTYPE HTML>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@page import="com.honda.galc.handheld.forms.ChooseProcessForm" %>
<%@page import="com.honda.galc.handheld.forms.ScanProductIdForm"%>
<%@page import="com.honda.galc.handheld.data.HandheldConstants"%>
<%@include file="Handheld.jsp" %>
<html>
	<head>
		<link rel="stylesheet" href="Handheld.css">
		<meta charset="UTF-8">
		<title>GALC-Scan Product ID</title>
	</head>
		<h1 id="label">GALC</h1>
		<p>
	<%
		ChooseProcessForm chooseProcessForm = (ChooseProcessForm)request.getSession().getAttribute(HandheldConstants.CHOOSE_PROCESS_FORM);
		ScanProductIdForm scanProductIdForm = (ScanProductIdForm)request.getSession().getAttribute(HandheldConstants.SCAN_PRODUCT_ID_FORM);
		if (scanProductIdForm == null)
			scanProductIdForm = new ScanProductIdForm();
	%>		
		
		<h2 id="stationName" style="margin-left: 40"><%= chooseProcessForm.getSelectedProcess().getProcessPointName() %></h2>
	<body onload="handlePageLoad()">
	
		<html:form
			action="scanProductIdAction"
			name="scanProductIdForm"
			styleId="mainForm"
			type="com.honda.galc.handheld.forms.ScanProductIdForm">
		<html:hidden property="sessionTimedOut" styleId="sessionTimedOutField" />
		<table>
			<tbody>
				<tr><td><%= "Last Processed: " + (scanProductIdForm.getLastProcessedProductId())%></td></tr>
				<tr><td><h3>Scan Product ID</h3></td></tr>
				<tr><td><html:text property="productId" styleId= "productIdField" onfocus="select()" onclick="setTimeoutDuration()" onblur="this.focus()"/></td></tr>
			</tbody>
		</table>
		<html:hidden property="changeStationRequest" styleId="changeStationRequestHiddenField"/>
		<html:button property="changeStationRequest" value="Change Station" onclick="handleChangeStationButtonClicked()"/>
	</html:form>
</body>
<script src="Handheld.js"></script>
<script>
	function handlePageLoad() {
		setTimeoutFromSession(<%=request.getSession().getAttribute(HandheldConstants.TIMEOUT_INTERVAL)%>);
		document.getElementById("productIdField").focus();
		document.getElementById("productIdField").select();		 
	}
	
	function handleChangeStationButtonClicked() {
		document.getElementById("changeStationRequestHiddenField").value = "Change Station";
		document.forms[0].submit();
	}
</script>
</html>