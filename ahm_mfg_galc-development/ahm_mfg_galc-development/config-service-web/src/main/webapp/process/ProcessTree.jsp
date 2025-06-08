
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<HEAD>
<%@ page import="com.honda.galc.entity.conf.*" %>
<%@ page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="false" %>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>

<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>Process Tree</TITLE>

<script>
		function show(sObjectID) {
		// USED TO SHOW AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		
		   
			document.getElementById(sObjectID).style.display='';
		}
		
		function hide(sObjectID) {
			
		// USED TO HIDE AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
			document.getElementById(sObjectID).style.display='none';
		}
		
		function toggleVisibility(sObjectID) {
		// USED TO TOGGLE VISIBILITY OF AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		
		    if (document.getElementById(sObjectID) == null)
		    {
		       return;
		    }
		    
			if(document.getElementById(sObjectID).style.display=='none') {
				show(sObjectID);
				
				return "show";
			} else {
				hide(sObjectID);
				
				return "hide";
			}
		}
		
		function toggleTable(idnumber) {
		   var result = toggleVisibility("table"+idnumber);
		   
		   if (result == "show")
		   {
		       document.getElementById("updownimg"+idnumber).src = "process/branchdown.gif";
		   }
		   else
		   {
               document.getElementById("updownimg"+idnumber).src = "process/branchup.gif";		   
		   }
		}
</script>
</HEAD>

<BODY class="settingspage">
<%
  int idcount = 1;
  List<Site> siteList = (List<Site>)request.getSession(false).getAttribute("SITE_NODES");
  
  if (siteList == null)
  {
     siteList = new ArrayList<Site>(0);
  }
  
%>
<table cellpadding="0" cellspacing="0"> 
<tbody>
<tr>
<td>
<table>
<tbody>
<tr><td onclick="toggleTable('<%= idcount %>');"><IMG id="<%= "updownimg"+idcount %>" border="0" src="process/branchup.gif" width="17" height="17"></td><td>System Settings</td></tr>
<tr>
<td></td>
<td>
<table cellpadding="2" id="<%= "table"+idcount %>"  style="display: none"> 
<% idcount++; %>
<tr><td><A class="processtree" href="getSiteInformation.do?createFlag=true" target="editpane">New Site</A></td></tr>
<tr><td><a class="processtree" target="editpane" href="adminUserSettings.do?initializeFrame=true">Administrative Users</a></td></tr>
<tr><td><a class="processtree" target="editpane" href="adminGroupSettings.do?initializeFrame=true">Administrative Groups</a></td></tr>
<tr><td><a class="processtree" target="editpane" href="gpcs.do?initializeFrame=true">GPCS</a></td></tr>
<tr><td><a class="processtree" target="editpane" href="productType.do?initializeFrame=true">Product Type</a></td></tr>
</table>
</td>
</tr>
</tbody>
</table>
</td>
</tr>
<tr>
<td>
<table>
<tbody>
<tr>
<td><IMG border="0" src="process/opcnode.gif" width="17" height="17" /></td>
<td align="left"><a class="processtree" target="editpane" href="opcConfigurationList.do">OPC</a></td>
</tr>
</tbody>
</table>
</td>
</tr>
<tr>
<td>
<table><!--  site table -->
<TBODY>
<% 
  
  for(Site site : siteList) {
	  idcount++;
      String siteName = site.getSiteName();
%>
<tr><td onclick="toggleTable('<%= idcount %>');"><IMG id="<%= "updownimg"+idcount %>" border="0" src="process/branchup.gif" width="17" height="17"></td><td><A class="processtree" href="<%=  "getSiteInformation.do?name="+siteName %>" target="editpane">SITE - <%= site.getSiteName() %></a></td></tr>
<tr>
<td></td>
<td>
<%-- Plant Data --%>
<table cellpadding="2" id="<%= "table"+idcount %>"  style="display: none">  <!--  begin plant table -->
<tbody> 
<%
      for(Plant plant : site.getPlants()) {
      
      	idcount++;
         String plantName = plant.getPlantName();
 %>
 <tr>
     <td onclick="toggleTable('<%= idcount %>');"><IMG id="<%= "updownimg"+idcount %>" border="0" src="process/branchup.gif" width="17" height="17"></td>
     <td><a class="processtree" href="<%=  "plantSettings.do?siteName="+siteName+"&plantName="+plantName %>" target="editpane">PLANT - <%= plantName %></a></td>
 </tr>
 <tr>
 <td></td>
 <td>
 <%-- Divison Data --%>
 <table id="<%=  "table"+idcount %>" style="display: none">
 <tbody>
 <%
		 for(Division division : plant.getDivisions()) {
			 idcount++;
             String divisionID = division.getDivisionId();
             
             
             
%>
<tr>
    <td onclick="toggleTable('<%= idcount %>');"><IMG id="<%= "updownimg"+idcount %>" border="0" src="process/branchup.gif" width="17" height="17"></td>
    <td><a class="processtree" href="<%= "divisionSettings.do?divisionID="+divisionID %>" target="editpane" >DIV - <%=  division.getDivisionName()%></a></td>
</tr>
<tr>
<td></td>
<td> 
<%-- Line Data --%>
<table id="<%= "table"+idcount %>"  style="display: none">
<tbody>
<%
 if (division.getTerminalCount() > 0)
 {
%>
<tr><td colspan="2"><a class="processtree" href="<%= "terminalList.do?divisionID="+divisionID %>" target="editpane" >Terminals (<%= division.getTerminalCount() %> )</a></td></tr>
<%
 }
 %>
<%
 if (division.getDeviceCount() > 0)
 {
%>
<tr><td colspan="2"><a class="processtree" href="<%= "deviceList.do?divisionID="+divisionID %>" target="editpane" >Devices (<%= division.getDeviceCount() %> )</a></td></tr>
<%
 }
 %> 
<%
             for(Line line : division.getLines()) {
                 
                 idcount++;
                 String lineName = line.getLineName();
                 
                 String lineID = line.getLineId();
                 
                
                 
%>
<tr><TD  onclick="toggleTable('<%= idcount %>');"><IMG  id="<%= "updownimg"+idcount %>"  border="0" src="process/branchup.gif" width="17" height="17"></TD><td><a class="processtree" href="<%= "lineSettings.do?lineID="+lineID %>" target="editpane">LINE - <%= lineName %></a></td></tr>
<%                  

				  if (line.getProcessPointListCount() > 0)
				  {
%>
<tr>
<td></td>
<td>
<table id="<%= "table"+idcount %>" style="display: none">
<tbody>
<tr>
<td><a class="processtree" href="<%= "processPointList.do?lineID="+lineID %>" target="editpane">Process Points (<%= line.getProcessPointListCount() %>)</a></td>
</tr>
</tbody>
</table>
</td>
</tr>
<% 
				  }
             } /* end lines */
%>

<% 
             for(Zone zone : division.getZones()){
%>
<tr>
<td></td>
<td><a class="processtree" target="editpane" href="<%= "zoneSettings.do?existingZone=true&zoneID="+zone.getZoneId() %>">ZONE - <%= zone.getZoneName() %></a></td>
</tr>
<%
             } /* end zones */
 %>



</tbody>
</table> <!--  end zone/line table -->
<%-- end of line data --%>
</td></tr>

<% 
         }
%>

 </tbody>
 </table> <!--  end division table -->
 <%-- End division data --%>
 </td>
 <tr>

 <%        
      }  /* end plant */
 %>
</tbody>
</table>  <!--  end plant table -->
<%-- end plant data --%>
</td>
</tr>
<% 
  }  /*  end while siteIterator */
%>
</TBODY>
</table>  <!--  end site table -->
</td>
</tr>
</tbody>
</table>
</BODY>

<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
   <bean:write name="msg"/>
</html:messages>
<html:errors/>

</html:html>
