
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="true" %>	
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>	
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="<c:url value="/theme/Master.css" ></c:url>" rel="stylesheet" type="text/css">
<TITLE></TITLE>
<%
   String assemblyLineID =   (String)session.getAttribute("ASSEMBLY_LINE_ID");
   if (assemblyLineID == null)
   {
      assemblyLineID = "Unknown";
   }
   
   String showWarning = (String)session.getAttribute("DISPLAY_PRODUCTION_WARNING");
   String showWarningColor = (String)session.getAttribute("DISPLAY_PRODUCTION_WARNING_COLOR");
   String onloadValue="";
   
   if (showWarning != null)
   {
      session.removeAttribute("DISPLAY_PRODUCTION_WARNING");
      
      if (Boolean.parseBoolean(showWarning))
      {
         onloadValue="showProductionWarning()";
      }
   }
 %>
<script>
   var userwindow = null;
   
   var securitygroupswindow = null;
   
   var devicewindow = null;
   
   var printwindow = null;
   
   var appwindow = null;
   
   var opcwindow = null;
   
   var propwindow = null;
   
   function closeWindows() {
     
     if (userwindow != null && !userwindow.closed)
     {
         userwindow.window.close();
     }
     
     if (securitygroupswindow != null && !securitygroupswindow.closed)
     {
         securitygroupswindow.window.close();
     }

     if (devicewindow != null && !devicewindow.closed)
     {
         devicewindow.window.close();
     }
     
     if (printwindow != null && !printwindow.closed)
     {
         printwindow.window.close();
     }

     if (appwindow != null && !appwindow.closed)
     {
         appwindow.window.close();
     }

     if (opcwindow != null && !opcwindow.closed)
     {
         opcwindow.window.close();
     }
     
     if (propwindow != null && !propwindow.closed)
     {
         propwindow.window.close();
     }       
        
     return false;
   }
   
   function showProductionWarning() {
   		alert("You are now accessing a GALC production system. Please confirm that this system (Line ID: <%= assemblyLineID %>) is " +
   		      "the system you were planning to access.");
   		      
   		return false;
   }
</script>
</HEAD>
 
<BODY class="settingspage" onload="<%=onloadValue %>">
<table width="100%" cellpadding="0" cellspacing="0" style="<%=showWarningColor%>">
<TBODY>
<tr>
<td align="left">
	<table>
		<tbody>
			<tr>
				<td>
					<html:link styleClass="settingslink"
						onclick="userwindow = window.open('','userwindow','width=1000,height=560,resizable=yes,status=yes');userwindow.focus();"
						target="userwindow" action="/lDAPConfiguration">Users</html:link>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<td>
					<html:link styleClass="settingslink"
						onclick="securitygroupswindow = window.open('','securitygroupswindow','width=750,height=600,resizable=yes,status=yes');securitygroupswindow.focus();"
						target="securitygroupswindow" action="/securityGroupSettings.do?initializeFrame=true">Security Groups</html:link>
				</td>				
				<td>&nbsp;|&nbsp;</td>
				<td>
					<html:link styleClass="settingslink"
						onclick="devicewindow = window.open('','devicedataformatwindow','width=1050,height=560,resizable=yes,status=yes');devicewindow.focus();"
						target="devicedataformatwindow"
						action="/deviceDataFormatSettings">Device Data Format</html:link>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<td>
					<html:link styleClass="settingslink"
						onclick="printwindow = window.open('','printattributeformatwindow','width=860,height=630,resizable=yes,status=yes');printwindow.focus();"
						target="printattributeformatwindow"
						action="/printAttributeFormatSettings">Print Attribute Format</html:link>
				</td>		
				<td>&nbsp;|&nbsp;</td>
				<td>
					<html:link styleClass="settingslink"
						onclick="appwindow = window.open('','applicationWindow','width=1100,height=550,resizable=yes,status=yes');appwindow.focus();"
						target="applicationWindow"
						action="/applicationSearchAndConfigure">Application</html:link>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<td>
					<html:link styleClass="settingslink"
						onclick="propwindow = window.open('','propWindow','width=1000,height=700,resizable=yes,status=yes');propwindow.focus();"
						target="propWindow" action="/propertySettings">Properties</html:link>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<td>
					<html:link styleClass="settingslink"
						onclick="componentstatuswindow = window.open('','componentStatusWindow','width=1000,height=700,resizable=yes,status=yes');componentstatuswindow.focus();"
						target="componentStatusWindow"
						action="/componentStatusSettings">Component Status</html:link>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<td>
					<html:link styleClass="settingslink"
						onclick="propwindow = window.open('','propWindow','width=1000,height=700,resizable=yes,status=yes');propwindow.focus();"
						target="propWindow" action="/cacheConfiguration">Caches</html:link>
				</td>
				<td>&nbsp;|&nbsp;</td>
				<td>
					<html:link styleClass="settingslink"
						onclick="propwindow = window.open('','propWindow','width=1200,height=800,resizable=yes,status=yes');propwindow.focus();"
						target="propWindow" action="/processPointGroup">Process Point Groups</html:link>
				</td>
			</tr>	
		</tbody>
	</table>
</td>
<td align="right">
<table>
<TBODY>
<tr>
 <th class="systeminfo">User:</th>
 <td class="systeminfo"><%= request.getUserPrincipal().getName() %></td>
 <td class="systeminfo">&nbsp;|&nbsp;</td>	
 <th class="systeminfo">System:</th>

 <td class="systeminfo"><%= assemblyLineID %></td>
 <td class="systeminfo">&nbsp;|&nbsp;</td>	
 <td valign="middle"><a class="logout" onclick="closeWindows();" href="<c:url value="/ibm_security_logout?logoutExitPage=initialize.do" />" target="_top">Logout</a></td>
</tr>
</TBODY>
</table>
</td>
</tr>
</TBODY>
</table>
</BODY>
</html:html>
