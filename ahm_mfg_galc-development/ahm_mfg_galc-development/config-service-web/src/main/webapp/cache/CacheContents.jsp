<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page import="com.honda.galc.entity.conf.*" %>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page session="false" %>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>Application Settings</TITLE>

<script type="text/javascript">
	var currentTab = "propertyTab";
	function boldText(sObjectID) {
	// USED TO BOLDEN THE TEXT OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		document.getElementById(sObjectID).style.background='#cccccc';
	}
	
	function normalText(sObjectID) {
	// USED TO BOLDEN THE TEXT OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		document.getElementById(sObjectID).style.background='#eeeeee';
	}
	
	function show(sObjectID) {
		// USED TO SHOW AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		
		document.getElementById(sObjectID).style.display='';
	}
	
	function hide(sObjectID) {
		
	// USED TO HIDE AN OBJECT IN THE DOM THAT WAS UNIQUELY IDENTIFIED WITH AN "ID" PROPERTY
		document.getElementById(sObjectID).style.display='none';
	}
	
	function highlight(tbodyName, rowid) {

	   var tbody = document.getElementById(tbodyName);
	   var rows = tbody.getElementsByTagName("tr"); 
	   
	   for(i = 0; i < rows.length; i++){           
	      if (rows[i].id != rowid){
	        rows[i].style.backgroundColor = "white";
	      }else rows[i].style.backgroundColor = '#cccccc';
	   }
	}
	
	function load(){
		if(currentTab =="propertyTab"){
			newurl = "<%=request.getContextPath() %>/cacheDisplayProperty.do?ITEM=";
		    id = "PropertyList";	
		}else {
			newurl = "<%=request.getContextPath() %>/cacheDisplayLotControlRule.do?ITEM=";
			id = "LotControlRuleList";
		}
		var cacheTag = document.getElementById(id);
		var rows = cacheTag.getElementsByTagName("input");
		var item = "";
		for(i = 0; i < rows.length; i++){           
	   		if(rows[i].checked)  item = rows[i].value;
		}
		parent.frames['refreshCachesFrame'].location = newurl + item;
   }
   
   function refresh(){
   		if(currentTab =="propertyTab"){
			newurl = "<%=request.getContextPath() %>/cacheRefresh.do?CACHE_NAME=PROPERTY&ITEM=";
		    id = "PropertyList";	
		}else {
			newurl = "<%=request.getContextPath() %>/cacheRefresh.do?CACHE_NAME=LOT_CONTROL_RULE&ITEM=";
			id = "LotControlRuleList";
		}
		var cacheTag = document.getElementById(id);
		var rows = cacheTag.getElementsByTagName("input");
		var item = "";
		for(i = 0; i < rows.length; i++){           
	   		if(rows[i].checked)  item = rows[i].value;
		}
		parent.frames['refreshCachesFrame'].location = newurl + item;
   }

	function showProperty() {
	   boldText('propertyTab');
	   normalText('lotControlRuleTab');
	   hide('LotControlRuleList');
	   show('PropertyList');
	   currentTab = "propertyTab";
	}

	function showLotControlRule() {
	   normalText('propertyTab');
	   boldText('lotControlRuleTab');
	   hide('PropertyList');
	   show('LotControlRuleList');
	   currentTab = "lotControlRuleTab";
	}
</script>
</HEAD>

<%
  String showWarningColor = (String)request.getAttribute("DISPLAY_PRODUCTION_WARNING_COLOR");
  List<String> componentIds = (List<String>)request.getAttribute("COMPONENT_IDS");
  List<String> processPointIds = (List<String>)request.getAttribute("PROCESS_POINT_IDS");
%>

<BODY class="settingspage">
<div border="1" rows="30,*">
		<table border="1" cellpadding="4">
		<tr>
		<td id="propertyTab" class="activetab"><span  onclick="showProperty();" style="<%=showWarningColor%>">Property Cache</span></td>
		<% 
		  boolean existingFlag = true;
		  String optionalTabStyle = null;
		  if  (existingFlag) { 
		      optionalTabStyle = "class=\"inactivetab\"";
		  }
		  else
		  {
		      optionalTabStyle = "style=\"display: none;\"";
		  }
		   
		%>
		<td id="lotControlRuleTab" <%= optionalTabStyle %>><span   onclick="showLotControlRule();" style="<%=showWarningColor%>">Lot Control Rule Cache</span></td>
		
		</tr>
		</table>

	<HR>
	<TABLE width="100%" border = "1" style=""border:1px solid black;">
	
		<TR>
			<tbody id="PropertyList">
			<TR>
				<TH width="20">&nbsp;</TH>
				<TH>Component ID</TH>
			</TR>

			<%
				int index=1;
				for(String item : componentIds) {
			 %>
				<TR onclick="highlight('propertyList','<%= index %>' )">
					<TD>
						<input type="radio" name="cacheItem" value="<%=item%>"/>
					</TD>
					<TD><%=item %></TD>
				</TR>
				<%index++;
			  }
			%>
			</tbody> 
			
		</TR>
		
		<TR>
			<tbody id="LotControlRuleList" style="display: none">
			<TR>
				<TH>Process Point Id</TH>
			</TR>
			<%
				index=1;
				for(String item : processPointIds) {
			 %>
			<TR>
				<TD>
					<input type="radio" name="cacheItem" value="<%=item%>"/>
				</TD>
				<TD><%=item %></TD>
			</TR>
			<%index++; 
			  }
			%>
			</tbody> 
		</TR>
	</TABLE>
	<br><br><br>
	<table>
		<TR>
			<TD align="center" height="40" valign="top">
				<input type="submit" name="operation" value="Refresh" onclick="refresh()"/>
			</TD>
			
			<TD align="center" height="40" valign="top">
				<input type="submit" name="operation" value="Load" onclick ="load()"/>
			</TD>
			
		</TR>
	
	</table>
</div>
</BODY>
</html:html>
