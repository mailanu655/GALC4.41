<!DOCTYPE HTML><%@page language="java" 	contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="com.honda.galc.handheld.data.HandheldConstants"%>
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
		<html:form action="/errorPageAction" styleId="mainForm">
			<html:hidden property="sessionTimedOut" styleId="sessionTimedOutField" />
			<table>
				<tbody>
					<tr>
						<td><h3>Error:</h3></td>
					</tr>
					<tr>
						<td>
							<%=session.getAttribute(HandheldConstants.PERSISTENCE_ERROR_MESSAGE) %>
						</td>
					</tr>
				</tbody>
			</table>
			<p>
			<input id="bntSubmit" type="submit" value="Submit" autofocus="autofocus">
		</html:form>
	</body>
	<script src="Handheld.js"></script>
	<script>
		function handlePageLoad() {
			setTimeoutFromSession(<%=request.getSession().getAttribute(HandheldConstants.TIMEOUT_INTERVAL)%>);
		}
	</script>
</html>