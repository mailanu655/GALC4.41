<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>

<%@ page import="com.honda.galc.entity.conf.Terminal" %>
<%@ page import="com.honda.galc.webstart.WebStartConstants" %>

<html>
<head>
<title>Terminals</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="GENERATOR" content="Rational Software Architect">

<%
  List terminals = (List) session.getAttribute(WebStartConstants.TERMINALS);

  if(terminals == null) {
  	terminals = new ArrayList();
    response.sendRedirect(WebStartConstants.Action.LIST_TERMINALS.action());
    return;
  }
  
 %>
 

</head>
<body>
<jsp:include flush="false" page="header.jsp"></jsp:include>
	<br>
	<br>
	<center>		
		<h2>Terminals</h2>
		<br>
		<table class="line" border="1" cellpadding="3"  width="90%" > 
	  		<thead class="fixedHeader" id="fixedHeader">
		    	<tr>
		      		<th class="line">Terminal Name</th>
		      		<th class="line"><span style="width: 120px">Hostname / IP Address</span></th>
		      		<th class="line"><span style="width: 50px">Port</span></th>
		      		<th class="line"><span style="width: 500px">Description</span></th>
		      		<th class="line"><span style="width: 90px">Process Point ID</span></th>
		    	</tr>  
	  		</thead>
			  		
	  		<%
	      		Iterator iter = terminals.iterator();
	      		Terminal aTerminal = null;
	      		while(iter.hasNext()) {
		       		aTerminal = (Terminal)iter.next();
	    	%>
		    	<tr>
		      		<td><%=aTerminal.getHostName()%></td>
		      		<td>&nbsp;<%= aTerminal.getIpAddress() %></td>
		      		<td>&nbsp;<%= aTerminal.getPort() %></td>
		      		<td>&nbsp;<%= aTerminal.getTerminalDescription() %></td>
		      		<td>&nbsp;<%= aTerminal.getLocatedProcessPointId() %></td>
		    	</tr>
	    	<%
	      		}
      		%>
		</table>
	</center>

</body>
</html>
