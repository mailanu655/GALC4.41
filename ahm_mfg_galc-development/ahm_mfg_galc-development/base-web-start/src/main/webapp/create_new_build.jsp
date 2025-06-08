<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>Configure New Build</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="GENERATOR" content="Rational Software Architect">
<script language="javascript" src="scripts/date-picker.js"></script>
<script language="javascript">
  var urls = new Array();
  var descriptions = new Array();
  var defBuildId = "";

	var i = 0;
    <c:forEach var="build" items="${builds}">urls[i] = "<c:out value='${build.url}'/>";descriptions[i++] = "<c:out value='${build.description}'/>";</c:forEach>
  
  function populateFromDefault() {
      var selectObject = document.getElementById("ref_build");
      defBuildId = document.getElementById("build_id").value = selectObject.options[selectObject.options.selectedIndex].value;
	  document.getElementById('build_description').value = descriptions[selectObject.options.selectedIndex];
	  document.getElementById('jar_url').value = urls[selectObject.options.selectedIndex];
  }
  
  function checkForm() {
    if (document.getElementById('build_id').value == "") {
        alert('Build ID cannot be empty.');
        return false;
    }
    if (document.getElementById('build_id').value == defBuildId) {
        alert('Build ID equals to already defined one.');
        return false;
    }
    if (document.getElementById('build_id').value == 'DEFAULT') {
        alert('Build ID is reserved. Please use a different name.');
        return false;
    }
	if (document.getElementById('build_description').value == "") {
        alert('Please enter a description for the build.');
        return false;
    }
    if (document.getElementById('jar_url').value == "") {
        alert('Please enter a URL for the build.');
        return false;
    }
    if (document.getElementById('build_date').value == "") {
        alert('Please enter a date for the build.');
        return false;
    }
    return true;
}

</script>

</head>
<body>
	<jsp:include flush="true" page="header.jsp"></jsp:include>

	<form id="Form" action="ADD_BUILD.act" method="Post" onSubmit="return checkForm();">

	<center>
	<br>
	<br>
	<h2>Configure New Build</h2>
	
	<table width="90%" border="0" cellpadding="5">
		<tbody>
		<tr>
			<td>
			<table class="edit">
			<br>
				<tr>
					<th align="right">Build ID</th>
					<td><input type="text" id="build_id" name="build_id" size="20" maxlength="20"></td>
				</tr>

				<tr>
					<th align="right">Description</th>
					<td><input type="text" id="build_description" name="build_description" size="60" maxlength="60"></td>
				</tr>

				<tr>
					<th align="right">JAR URL</th>
					<td><input type="text" id="jar_url" name="jar_url" size="120" maxlength="120"></td>
				</tr>

				<tr>
					<th valign="bottom" align="right">Build Date</th>
					<td><input type="text" id="build_date" name="build_date" value="" size="10" maxlength="10">
					<a href="javascript:show_calendar('forms[0].build_date');"><img src="pics/calendar.gif" width="24" height="24" border="0"></a></td>
				</tr>

			</table>
			</td>
		</tr>
		<tr>
			<td>
				<br>
				<input type="submit" value="Save" class="smalltext">
				<input type="button" value="Cancel" class="smalltext" onclick="history.back();">
				<input type="reset" value="Clear Fields" class="smalltext">
				<input type="button" value="Fill In From: " class="smalltext" onclick="populateFromDefault();">
			    <select id="ref_build" name="ref_build">
		        	<c:forEach var="build" items="${builds}">
	          			<option value='<c:out value="${build.buildId}"/>'><c:out value="${build.buildId} - ${build.description}"/></option> 
		        	</c:forEach>
		        	<c:remove var="builds"/>
	        	</select>
				
			</td>
		</tr>

		</tbody>
	</table>
	</center>
</form>
</body>
</html>
