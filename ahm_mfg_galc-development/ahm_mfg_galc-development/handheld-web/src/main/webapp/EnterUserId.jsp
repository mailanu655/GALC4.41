<!DOCTYPE HTML><%@page language="java"
	contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
	<head>
		<link rel="stylesheet" href="Handheld.css">
		<meta charset="UTF-8">
		<title>GALC-Enter ID</title>
	</head>
	<body onload="handlePageLoad()">
		
		<html:form
			action="enterUserIdAction"
			name="enterUserIdForm"
			type="com.honda.galc.handheld.forms.EnterUserIdForm">
			<html:hidden property="division" styleId="divisionField"/>	
			<table>
				<tbody>
					<tr>
						<td><h3 id="enterIdLabel">Enter ID:</h3></td>
					</tr>
					<tr>
						<p id= "errorField" style="color:Red;" hidden="true">Access Denied</p>						
						<td><html:password property="userInput" onkeyup="handleUserIdFieldValueChanged()" maxlength="30" onfocus= "select()" styleId="userIdField" onblur="this.focus(); this.select()"/></td>
					</tr>
					<tr>
						<td>
							<input id="submit" type="submit" value="Submit" disabled style="margin-left: 30;">
						</td>
						<td>
							<input id="submit" type="button"  value="Cancel" onclick="handleCancelClicked()" style="margin-left: 30;">
						</td>
					</tr>
				</tbody>
			</table>
		</html:form>
	</body>
	<script src="Handheld.js"></script>
	<script>
		function handlePageLoad() {
			document.getElementById("userIdField").focus();
			document.getElementById("divisionField").value = "<%=request.getParameter("division")%>";
			document.getElementById("errorField").hidden = document.getElementById("userIdField").value.length == 0; 
		}
		function handleUserIdFieldValueChanged() {
			var field = document.getElementById("userIdField");
			document.getElementById("submit").disabled = field.value.length == 0;
		}
		
		function goodbye(e) {
			document.body.style.cursor = 'progress';
		}
		window.onbeforeunload=goodbye; 
		
		function handleCancelClicked() {
			window.history.back();
		}
	</script>
</html>