<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.webstart.WebStartConstants" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>Default Build Setup</title>
<script type="text/javascript">
	var cells = new Array();
	var builds = new Array();
	var descriptions = new Array();
	var currentCell = "<c:out value='${current_cell_id}'/>";
	var i = 0;
    <c:forEach var="cell" items="${cell_builds}">
	 cells[i] = "<c:out value='${cell.key}'/>";
	 descriptions[i] = "<c:out value='${cell.value.envDescription}'/>";
	 builds[i++] = "<c:out value='${cell.value.defaultBuildId}'/>";
 	</c:forEach>
 	
 	function setCurrentDefBuild() {
 	    var cellId = document.getElementById("cell_id");
 		document.getElementById("currentDefBuild").value = builds[cellId.options.selectedIndex];
 		document.getElementById("description").value = descriptions[cellId.options.selectedIndex];
 	}
 	
 	function confirmDelete() {
 		var status = confirm("         Are you sure to delete \n\n       " 
 					+ document.getElementById("cell_id").value + 
 					"\n\n         cell configuration?");
 					
 		if(status == true) {
 			document.getElementById("actionCmd").value = "delete";
 		}
 		
		return status;
 	}
 	
 	function setNewCell() {
 	    var cellSelection = document.getElementById("cellSelection");
 	    var cellFlag = document.getElementById("is_new_cell");
 	    if(cellFlag.checked == true) {
 			cellSelection.innerHTML = "<input type=text name=cell_id id=cell_id value='"+ currentCell + "'>";
 		    document.getElementById("description").value = "";
 		    document.getElementById("currDefBuildArea").style.visibility = "hidden";
 			document.getElementById("deleteButton").style.visibility = "hidden";
 		} else {
 				var select =	"<select name=\'cell_id\' id=\'cell_id\' onchange=\'setCurrentDefBuild();\'>" +
						       <c:forEach var="cell" items="${cell_builds}">
				        			"<option value='<c:out value='${cell.key}'/>'>" +
				        				"<c:out value='${cell.key}'/> - <c:out value='${cell.value.envDescription}'/>- <c:out value='${cell.value.defaultBuildId}'/>" +
				        			"</option>" +
				        		</c:forEach>
			        		"</select>";       		
 		        cellSelection.innerHTML = select;
 		        setCurrentDefBuild();
 		        document.getElementById("currDefBuildArea").style.visibility = "visible";
 			    document.getElementById("deleteButton").style.visibility = "visible";
 		}
 	}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="GENERATOR" content="Rational Software Architect">
<%
	Object currentDefault = session.getAttribute(WebStartConstants.BUILD_ID);
	if(currentDefault == null) {
		currentDefault = "(Not Defined)";
	}

	if(session.getAttribute(WebStartConstants.CELL_BUILDS) == null) {
		response.sendRedirect(WebStartConstants.Action.SET_DEFAULT_BUILD.action());
		return;
	}
%>

</head>

<body onload="setCurrentDefBuild();">
<jsp:include flush="false" page="header.jsp"></jsp:include>
	<center>
	<h2>Default Build Configuration</h2>

	<form id="Form" action="<%=WebStartConstants.Action.SET_DEFAULT_BUILD.action()%>" method="Post">
	<TABLE width="90%" border="0" cellpadding="15">
		<TBODY>
		<TR>
			<TD> <div align="center">
				<table border="1" cellpadding="3"  class="line">
					<th class='line'>Cell</th>
					<th class='line' >Description</th>
					<th class='line' >Default Build</th>
					<c:forEach var="cell" items="${cell_builds}">
			    	<tr>
			    		<td><c:out value="${cell.key}"/></td>
			    		<td><c:out value="${cell.value.envDescription}"/></td>
			    		<td><c:out value="${cell.value.defaultBuildId}"/></td>
			    	</tr>
			    	</c:forEach>
			    </table>
			    </div>
			    <br>
			    <hr>
			
			  	<table border="0" cellpadding="10" class="edit">
			    	<tr>
			    		<th align="right">Cell:</th>
			    		<td id="cellSelection">
				    		<select name="cell_id" id="cell_id" onchange="setCurrentDefBuild();">
						       <c:forEach var="cell" items="${cell_builds}">
				        			<option value="<c:out value='${cell.key}'/>">
				        				<c:out value="${cell.key} [${cell.value.envDescription}] ${cell.value.defaultBuildId}"/>
				        			</option>
				        		</c:forEach>
			        		</select>        		
		        		</td>
		        		<th><input type="checkbox" name="is_new_cell" id="is_new_cell" value="true" onClick="return setNewCell();"/>Create New Cell</th>
			    		
			    	</tr>
			    	<tr id="currDefBuildArea">
			    		<th align="right">Current Default Build:</th>
			    			<td><input type="text" id="currentDefBuild" readonly></td>
			    	</tr>
			    	<tr>
			    		<th align="right">Default Build Description:</th>
			    			<td><input type="text" name="description" id="description"></td>
			    	</tr>
			    	<tr>
			      		<th align="right">New Default Build:</th>
			      		<td>
			      		<c:choose>
			      			<c:when test="${not empty builds}">
				        		<select name="<%=WebStartConstants.BUILD_ID%>">
					        		<c:forEach var="build" items="${builds}">
					        			<option value="<c:out value='${build.buildId}'/>">
					        				<c:out value="${build.buildId} - ${build.description}"/>
					        			</option>
					        		</c:forEach>
				        		</select>
			      			</c:when>
			      			<c:otherwise>
				      			<c:set var="saveDisabled" value="disabled"/>
				      			(No Builds Defined)</c:otherwise>
			      		</c:choose>
			      		</td>
			      		<c:remove var="cell_builds"/>
			    	</tr>
			    	<tr>
			    		<th align="right">
			    		<br>
					    	<input type="submit" value="Save" <c:out value='${saveDisabled}'/> class="smalltext">
					    	<input type="button" value="Cancel" class="smalltext" onclick="history.back();">
					    	<input type="hidden" name="actionCmd" id="actionCmd" value="update">
					    </th>
					    <td align="right">
					    <br>
					    	<input type="submit" name="deleteButton" id="deleteButton" value="Delete" class="smalltext" onclick="return confirmDelete();">
					    </td>
					</tr>
			  	</table>
			</TD>
		</TR>
		</TBODY>
	</TABLE>
	</form>
	</center>
</body>
</html>

