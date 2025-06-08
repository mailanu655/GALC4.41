<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="com.honda.galc.entity.conf.WebStartClient" %>
<%@ page import="com.honda.galc.entity.conf.WebStartBuild" %>
<%@ page import="com.honda.galc.webstart.WebStartConstants" %>
<%@ page import="com.honda.galc.webstart.WebStartBuildUtility" %>
<html>
<head>
<title>Webstart Clients</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="GENERATOR" content="Rational Software Architect">
<script>

  function strStartsWith(str, prefix) {    
    return str.indexOf(prefix) === 0;
  }
   
  function strTrim(str) {
     return str.replace(/^\s+|\s+$/g, '') ;
  }
  
  function toggleValue(checkbox) {
	if (checkbox.checked) {
		checkbox.value = "true";
	} else {
		checkbox.value = "false";
	}
  }
  
  function selectAll() {
    var checkboxes = document.getElementsByClassName('shutdownCheckbox');
    for (var i = 0, n = checkboxes.length; i < n; i++) {
    	if (checkboxes[i].checked == false) {
    		checkboxes[i].checked = true;
    	}
    }
  }
  
  function deselectAll() {
    var checkboxes = document.getElementsByClassName('shutdownCheckbox');
    for (var i = 0, n = checkboxes.length; i < n; i++) {
    	if (checkboxes[i].checked == true) {
    		checkboxes[i].checked = false;
    	}
    }
  }
  
  function toggleAll() {
    var checkboxes = document.getElementsByClassName('shutdownCheckbox');
    for (var i = 0, n = checkboxes.length; i < n; i++) {
    	checkboxes[i].checked = !checkboxes[i].checked;
    }
  }
 
  function validateForm() {
	var shutdownCheckbox = document.getElementById("<%=WebStartConstants.SHUTDOWN_CHECKBOX%>");
	if (shutdownCheckbox.checked == true) {
    	if (confirm('Are you sure you want to shut down the selected terminals?')) {
    		return confirmShutdown(); 
    	}
    	return false;
    } else {
    	return true;
    }
  }

  function confirmShutdown() {
    var hostName = prompt('Please confirm the host name to proceed with shutting down terminals', '');
    if (hostName == null) {
    	return false;
    } else {
    	env = strTrim(hostName);
    	if (hostName != document.domain) {
    		alert('\'' + hostName + '\'' + ' does not match ' + document.domain);
    		return false;
    	}
    }
  }

</script>
<%
  List clients = (List) session.getAttribute(WebStartConstants.CLIENTS);
  if(clients == null ) {
    response.sendRedirect(WebStartConstants.Action.LIST_CLIENTS.action()); 
    return;
  }
 %>
 
<meta http-equiv="Refresh" content="6000;url=LIST_CLIENTS.act"/>
<meta http-equiv="cache-control" content="No-Cache"/>

</head>

<body>
<jsp:include flush="true" page="header.jsp"></jsp:include>
	<br>
	<br>
	<center>		
	<h2>Webstart Clients</h2>
	<br>
	<form id="Form" name="updateClientsForm" action="UPDATE_CLIENTS.act" method="Post" onsubmit="return validateForm();">
				<table border="1" cellpadding="3" cellspacing="0" width="90%" class="line"> 
			  		<thead class="fixedHeader" id="fixedHeader">
			    	<tr>
			      		<th class='line' width="10">Hostname/ IPAddress</th>
			      		<th class='line' width="10%" >Terminal Name</th>
			      		<th class='line' width="30%">Description</th>
			      		<th class='line' width="10%">Target Build</th>
			      		<th class='line' width="10%">Asset Number</th>
			      		<th class='line' width="10%" >Column Location</th>
			      		<th class='line' width="5%">Shutdown Flag</th>
			      		<th class='line' width="5%" >Edit</th>
			      		<th class='line'  width="5%">Delete</th>
			    	</tr>  
			  		</thead>
			  		
			  		<%
			      		Iterator iter = clients.iterator();
			      		WebStartClient client = null;
			      		int cnt = 0;
			  		%>
					<tbody class="scrollContent">
			    	<%
			      		while(iter.hasNext()) {
			       		client = (WebStartClient)iter.next();
			       		cnt++;
			    	%>
			    	<tr>
			      		<td><b><%=client.getIpAddress()%></b></td>
			      		<td><a href="<%=WebStartConstants.Action.SHOW_CLIENT.action()%>?<%=WebStartConstants.IP_ADDRESS%>=<%=client.getIpAddress()%>"><%=client.getHostName()%></a></td>

			      		<td>&nbsp;<%=client.getDescription()%></td>
			      		<td>&nbsp;<%=client.getBuildId()%></td>
			      		<td>&nbsp;<%=client.getAssetNumber()%></td>
			      		<td>&nbsp;<%=client.getColumnLocation()%></td>
			      		<td align="center">
			      		<%
			      		if (client.isShutdownFlagOn()) {
			      		%>
			      			<input
			      				type="checkbox"
			      				id="<%=WebStartConstants.SHUTDOWN_FLAG%>_<%=client.getIpAddress()%>" 
			      				name="<%=WebStartConstants.SHUTDOWN_FLAG%>_<%=client.getIpAddress()%>" 
			      				class="shutdownCheckbox" 
			      		   		onclick="toggle(this);" 
			      		   		checked
			      		   	/>
			      		   	<input type="hidden" name="<%=WebStartConstants.SHUTDOWN_FLAG%>_<%=client.getIpAddress()%>" value="off"/>
			      		<%
			      		} else {
			      		%>
			      			<input
			      				type="checkbox"
			      				id="<%=WebStartConstants.SHUTDOWN_FLAG%>_<%=client.getIpAddress()%>" 
			      				name="<%=WebStartConstants.SHUTDOWN_FLAG%>_<%=client.getIpAddress()%>" 
			      				class="shutdownCheckbox" 
			      		   		onclick="toggle(this);"
			      		   	/>
			      		   	<input type="hidden" name="<%=WebStartConstants.SHUTDOWN_FLAG%>_<%=client.getIpAddress()%>" value="off"/>
			      		<%
			      		}
			      		%>
			      		</td>
			      		<td align="center">
			      			<a href="<%=WebStartConstants.Action.EDIT_CLIENT.action()%>?<%=WebStartConstants.IP_ADDRESS%>=<%=client.getIpAddress()%>"><img src="pics/edit.gif" border="0" title="Edit"></a>
						</td>
						<td align="center">
							<a href="<%=WebStartConstants.Action.DELETE_CLIENT.action()%>?<%=WebStartConstants.IP_ADDRESS%>=<%=client.getIpAddress()%>"><img src="pics/delete.gif" border="0" title="Delete"></a>
						</td>
			    	</tr>
			    	<%
			      		}
			      		if(cnt == 0) {
			        		out.print("<h3>No clients currently configured!</h3>");
			      		}
			    	%>
			    	</tbody>
				</table>

	<table border="0"  width="90%" cellpadding="5">
		<tr>
			<td>
				<br>
				<input type="button" value="Select All" class="smalltext" onclick="selectAll();">
				<input type="button" value="Deselect All" class="smalltext" onclick="deselectAll();">
				<input type="button" value="Toggle All" class="smalltext" onclick="toggleAll();">
			</td>
		</tr>
		<tr>
			<td>
				<br>
			    <input type="submit" value="Save" class="smalltext">
			    <input type="button" value="Cancel" class="smalltext" onclick="history.back();">
			    <label>&emsp;Shut Down:</label>
			    <input type="checkbox" id="<%=WebStartConstants.SHUTDOWN_CHECKBOX%>" name="<%=WebStartConstants.SHUTDOWN_CHECKBOX%>">
			</td>
		</tr>
	</table>
	</form>
</center>
</body>
</html>

