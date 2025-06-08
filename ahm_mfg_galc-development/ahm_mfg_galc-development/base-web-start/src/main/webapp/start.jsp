<%@ page language="java" contentType= "text/html" %>
<%@ page import="com.honda.galc.webstart.WebStartConstants" %>
<head>

<%
	StringBuilder aBuilder = new StringBuilder();
	aBuilder.append("http://");
	aBuilder.append(request.getServerName());
	if (request.getServerPort() != 80) {
    	aBuilder.append(':');
    	aBuilder.append(request.getServerPort());
	}
	aBuilder.append("/BaseWebStart/webstart.jnlp");
	String serverUrl = aBuilder.toString();
%>

<meta http-equiv=refresh content="0; url=<%=serverUrl%>"/>  

<TITLE>GALC Start Page</TITLE>
</head>

<body background=pics/background.jpg>

	<center>
	<br><br>
	<h1 align="center" style="font-size: 40px; font-family: sans-serif; color: #000000">GALC Web Start</h1><br><br><br>
	<img border="0" src="pics/galcwebstart.jpg"><br><br><br>
	<table width=90% cellpadding=0>
		<tr align="center">
    		<td>
    			<a href="<%=serverUrl%>"
					onmouseover="document.images['myBut'].src='pics/startButOver.gif'"
					onmouseout="document.images['myBut'].src='pics/startBut.gif'"
					onmousedown="document.images['myBut'].src='pics/startButDown.gif'"
					onmouseup="document.images['myBut'].src='pics/startButOver.gif'"><img
					name="myBut" src="pics/startBut.gif" border="0"></a>
			</td>
		</tr>
	</table>
	</center>
</body>
