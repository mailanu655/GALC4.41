<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html:html>
<head>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/galc.css">
<title>OIF - Manual Distribution Start</title>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/date-picker.js">
</script>
<script type="text/javascript">
	
	var warnings = new Array();
	var isTsFlags = new Array();
	var i = 0;

    <c:forEach var="item" items="${oifMenuOptions}">
		warnings[i] = "<c:out value='${item.warning}'/>";
		isTsFlags[i++] = "<c:out value='${item.startTsRequired}'/>";
 	</c:forEach>
	
	function initOifItems() {
		setOifItem(document.getElementById("taskName"));
	}
	
	function confirmOifLaunch() {
	    var oifName = document.getElementById("taskName");
	    var index = oifName.options.selectedIndex;
	 	if(oifName.value == "") {
	 		alert("Interface not selected.");
 	       	return false;
       	}
	 	var flag = isTsFlags[index];
	 	if(flag == "true") {
	 	   status = validateTimestamp(document.getElementById("startTimeStamp").value);
	 	   if(status == "false" || !status) {
	 	       return false;
	 	   }
	 	}
	    var message = warnings[index] + "\n Are you sure?";
	    return confirm(message);
	}
	
	function validateTimestamp(value) {
	    var dateFormat = "yyyy-mm-dd";
	    var timeFormat = "hh:mm:ss";
	    
	    var isMatch = value.search("^20\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d$");
	    
		if(isMatch == -1) {
			alert("Time stamp should be specified in this format: " + dateFormat + " " + timeFormat );
			return false;
		}
		return true;
	}
	
	function setOifItem(self) {
	    var index = self.options.selectedIndex;
	 	var flag = isTsFlags[index];
	 	if(flag == 'true') {
			document.getElementById("startTimeStampInputHdr").style.visibility = "visible";
			document.getElementById("startTimeStampInputRow").style.visibility = "visible";
		} else {
			document.getElementById("startTimeStampInputHdr").style.visibility = "hidden";
			document.getElementById("startTimeStampInputRow").style.visibility = "hidden";
		}
		document.getElementById("warning").innerHTML = warnings[index];
	}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="GENERATOR" content="Rational Software Architect">
</head>
<body class="tundra"; style="font-size:medium;"
	background="${pageContext.request.contextPath}/pics/background.jpg";
	onload="initOifItems();">
	<div align="center">
	<h2>OIF Manual Distribution Start</h2>
	<html:form styleId="startFrom" action="start_distribution"
		onsubmit="return confirmOifLaunch();">
		<br>
		<table>
			<th>Interface</th>
			<th id="startTimeStampInputHdr">Start Time Stamp</th>
			<tr>
				<td><html:select styleId="taskName" property="interfaceName"
					onchange="setOifItem(this);">
					<html:optionsCollection name="oifMenuOptions" />
				</html:select></td>
				<td id="startTimeStampInputRow"><html:text
					styleId="startTimeStamp" property="startTimeStamp" /><a
					href="javascript:show_calendar('forms[0].startTimeStamp');"><img
					src="${pageContext.request.contextPath}/pics/calendar.gif"
					style="width:24; height:22; vertical-align:top;" border="0"></a></td>
			</tr>
			<tr>
				<td>
				<div id="warning" style="font-size: small"></div>
				<br>
				<input type="submit" name="Submit" value="Submit"></td>
				<td>&nbsp;</td>
			</tr>
		</table>
		<br>
	
	</html:form>
	<h4><a href="distribution.do">Back</a></h4>
	<h4><a href="index.do">Main</a></h4>
	</div>
</body>
</html:html>
