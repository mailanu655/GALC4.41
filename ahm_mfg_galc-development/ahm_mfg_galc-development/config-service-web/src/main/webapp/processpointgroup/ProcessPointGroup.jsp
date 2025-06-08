<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<head>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="false" %>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>	
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Content-Style-Type" content="text/css">
<link href="theme/Master.css" rel="stylesheet" type="text/css">
<title>Process Point Group</title>

<script>

function initFrames() {
	loadCodes();
	
	loadProcessPoints();
}

function loadCodes() {
   newurl = "<%=request.getContextPath()%>/regionalCodeList.do";
   this.frames['regionalCodeList'].location = newurl;
}

function loadProcessPoints() {
   newurl = "<%=request.getContextPath()%>/processPointGroupSettings.do";
   this.frames['processPointGroupSettings'].location = newurl;
}

</script>
</head>

<body onload="initFrames()">
	<table>
		<tbody>
			<tr>
				<td>
					<iframe id="regionalCodeList" name="regionalCodeList" width="520" height="250" align="center"  frameBorder="0" scrolling="no"></iframe>
				</td>

				<td style="width:50;">
				</td>

				<td>
					<iframe id="regionalProcessPointGroupList" src="processpointgroup/RegionalProcessPointGroupList.jsp" name="regionalProcessPointGroupList" width="520" height="250" align="center"  frameBorder="0" scrolling="no"></iframe>
				</td>
			</tr>
			
			<tr>
			   <td colspan="3">
					<iframe id="processPointGroupSettings" name="processPointGroupSettings" width="1100" height="450" align="center" frameBorder="0" scrolling="no"></iframe>
			   </td>
			</tr>

			<tr>
				<td colspan="3">
			    	<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
				   		<bean:write name="msg"/>
				 	</html:messages>
				 	<html:errors />
			  	</td>
			</tr>
		</tbody>
	</table>
</body>
</html:html>
