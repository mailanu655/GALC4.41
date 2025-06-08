<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<head>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.AssignedProcessPointListForm"  %>
<%@ page import="com.honda.galc.entity.conf.*" %>
<%@ page import="java.util.*" %>
<%@ page session="false" %>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Content-Style-Type" content="text/css"> 
<link href="theme/Master.css" rel="stylesheet" type="text/css">
<title>AssignedProcessPointList.jsp</title>

<script>
var enterKeyCode = 13;

function searchTextKeyUp(event){
	if(event.keyCode === enterKeyCode || event.key.toLowerCase() === 'enter'){
		var listForm = document.assignedProcessPointListForm;

		var selectedCode = getSelectedCategoryCode();
		var tbody = parent.parent.frames['regionalProcessPointGroupList'].document.getElementById('pgtable');
		var selectedRow = getSelectedRow(tbody);
		if(selectedCode != null && selectedRow != null) {
			listForm.categoryCode.value = selectedCode;
    		var cols = selectedRow.getElementsByTagName("td");
    		for(j = 0; j < cols.length; j++) {
    			if(cols[j].id == "value") {
    				listForm.processPointGroupName.value = cols[j].innerHTML;
    			} else if(cols[j].id == "site") {
    				listForm.site.value = cols[j].innerHTML;
    			}
    		}
		}
	}
}

function getSelectedCategoryCode() {
	var tbody = parent.parent.frames['regionalCodeList'].document.getElementById('rctable');
	var selectedRow = getSelectedRow(tbody);
	if(selectedRow != null) {
   		var cols = selectedRow.getElementsByTagName("td");
   		for(j = 0; j < cols.length; j++) {
   			if(cols[j].id == "regionalValue") {
   				return cols[j].innerHTML;
   			}
   		}
	}
	return null;
}

function getSelectedRow(tbody) {
	var rows = tbody.getElementsByTagName("tr"); 
	for(i = 0; i < rows.length; i++) {    
   		if(rows[i].style.backgroundColor == '#cccccc' || rows[i].style.backgroundColor == 'rgb(204, 204, 204)') {
   			return rows[i];
   		}
	}
	return null;
}

function processPointSelected(rowid) {

   document.getElementById(rowid).style.backgroundColor = '#cccccc';
   
   var tbody = document.getElementById("pptable");
   var rows = tbody.getElementsByTagName("tr"); 
   
	for(i = 0; i < rows.length; i++) {           
    	if(rows[i].id != rowid) {
      		rows[i].style.backgroundColor = "#eeeeee";
      	}
   	}
   	var button = parent.document.getElementById('deleteButton');
   	button.disabled=false;
}
</script>

<%
	AssignedProcessPointListForm ppForm = (AssignedProcessPointListForm) request.getAttribute("assignedProcessPointListForm");
	List<ProcessPoint> processPointList = null;

	if(ppForm != null) {
		processPointList = ppForm.getProcessPointList();
	}
%>

</head>

<body class="settingspage">
<html:form styleId="AssignedProcessPointList" action="/assignedProcessPointList" name="assignedProcessPointListForm" type="com.honda.galc.system.config.web.forms.AssignedProcessPointListForm" scope="request">
	<html:hidden property="site" value="" />
	<html:hidden property="categoryCode" value="" />
	<html:hidden property="processPointGroupName" value="" />

	<table id="resulttable" align="top" width="100%" cellspacing="1">
		<tbody>
			<tr> 
				<td>
					<b>Assigned Process Points</b>
			    </td>
			    <td style="text-align:right;">
			    	Filter:  <html:text property="searchText" onkeypress="javascript:searchTextKeyUp(event);"></html:text>
			    </td>
			</tr>			
			<tr id="triframe">
				<td colspan="2">
					<div style="width: 480px; height: 350px; overflow: auto">
					<table border="1" style="width: 100%;">
						<thead>
							<tr>
    							<th>Process Point ID</th>
    							<th>Name</th>
    							<th>Type</th>
    							<th>Description</th>
  							</tr>
						</thead>

						<%
  							if (processPointList != null && processPointList.size() > 0) {
						%>
							<tbody id="pptable">
							<%
     							int idx = 0;
     							for(ProcessPoint pp : processPointList) {
        							idx++;
        							String rowid = "approw"+idx;
							%>
								<tr style="background-color: #eeeeee" id="<%= rowid %>" onclick="processPointSelected('<%= rowid %>');">
  									<td id="processPointId" style="width:200;"><%= pp.getProcessPointId() %></td>
  									<td><%= pp.getProcessPointName() %></td>
  									<td><%= pp.getProcessPointType() %></td>
  									<td><%= pp.getProcessPointDescription() == null ? "&nbsp;" : pp.getProcessPointDescription() %></td>
								</tr>
							<%
								}
							%>
							</tbody>
						<%
							}
						%>			
					</table>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</html:form>
</body>
</html:html>
