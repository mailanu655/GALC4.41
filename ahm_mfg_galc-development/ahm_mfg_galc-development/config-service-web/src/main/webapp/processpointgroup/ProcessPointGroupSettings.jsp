
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<head>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.ProcessPointGroupSettingsForm"  %>
<%@ page import="com.honda.galc.entity.conf.*" %>
<%@ page session="false" %>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>	
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Content-Style-Type" content="text/css">
<link href="theme/Master.css" rel="stylesheet" type="text/css">
<title>Process Point Group Settings</title>
<script>

function initFrames() {
	refreshProcessPoints();
}

function refreshProcessPoints() {
	filterProcessPoints(this.frames['availableProcessPointList'].document.availableProcessPointListForm);
	filterProcessPoints(this.frames['assignedProcessPointList'].document.assignedProcessPointListForm);
}

function filterProcessPoints(listForm) {
	var selectedCode = getSelectedCategoryCode();
	var tbody = parent.frames['regionalProcessPointGroupList'].document.getElementById('pgtable');
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
		listForm.submit();
	}
}

function getSelectedCategoryCode() {
	var tbody = parent.frames['regionalCodeList'].document.getElementById('rctable');
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

function addProcessPoint() {
	var selectedCode = getSelectedCategoryCode();
	var settingsForm = document.getElementById("ProcessPointGroupSettings");
	settingsForm.reset();
		
	var tbody = parent.frames['regionalProcessPointGroupList'].document.getElementById('pgtable');
	var selectedRow = getSelectedRow(tbody);
	if(selectedCode != null && selectedRow != null) {
		settingsForm.categoryCode.value = selectedCode;
		var cols = selectedRow.getElementsByTagName("td");
		for(j = 0; j < cols.length; j++) {
			if(cols[j].id == "value") {
				settingsForm.processPointGroupName.value = cols[j].innerHTML;
			} else if(cols[j].id == "site") {
				settingsForm.site.value = cols[j].innerHTML;
			}
		}
		var ppid = getSelectedProcessPointId(this.frames['availableProcessPointList'].document.getElementById('pptable'));
		if(ppid != null) {
			settingsForm.processPointId.value = ppid;		
			settingsForm.operation.value = "ADD";
			settingsForm.submit();		
		}
	} else {
		alert("Please select a Regional Process Point Group");
	}
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

function removeProcessPoint() {
	var selectedCode = getSelectedCategoryCode();
	var settingsForm = document.getElementById("ProcessPointGroupSettings");
	settingsForm.reset();

	var tbody = parent.frames['regionalProcessPointGroupList'].document.getElementById('pgtable');
	var selectedRow = getSelectedRow(tbody);
	if(selectedCode != null && selectedRow != null) {
		settingsForm.categoryCode.value = selectedCode;
		var cols = selectedRow.getElementsByTagName("td");
		for(j = 0; j < cols.length; j++) {
			if(cols[j].id == "value") {
				settingsForm.processPointGroupName.value = cols[j].innerHTML;
			} else if(cols[j].id == "site") {
				settingsForm.site.value = cols[j].innerHTML;
			}
		}

		var ppid = getSelectedProcessPointId(this.frames['assignedProcessPointList'].document.getElementById('pptable'));
		if(ppid != null) {
			settingsForm.processPointId.value = ppid;		
			settingsForm.operation.value = "DELETE";
			settingsForm.submit();		
		}
	}
}

function getSelectedProcessPointId(tbody) {
	var selectedRow = getSelectedRow(tbody);
	if(selectedRow != null)
	var rows = tbody.getElementsByTagName("tr"); 
	for(i = 0; i < rows.length; i++) {    
   		if(rows[i].style.backgroundColor == '#cccccc' || rows[i].style.backgroundColor == 'rgb(204, 204, 204)') {
   			var cols = rows[i].getElementsByTagName("td");
   			for(j = 0; j < cols.length; j++) {
   				if(cols[j].id == "processPointId") {
   					return cols[j].innerHTML;
   				}
   			}
   		}
	}
	return null;
}
</script>

</head>

<body onload="initFrames()">
<html:form styleId="ProcessPointGroupSettings" action="/processPointGroupSettings" name="processPointGroupSettingsForm" type="com.honda.galc.system.config.web.forms.ProcessPointGroupSettingsForm" scope="request">
	<html:hidden property="site" value="" />
	<html:hidden property="categoryCode" value="" />
	<html:hidden property="processPointGroupName" value="" />
	<html:hidden property="processPointId" value="" />
	<html:hidden property="operation" value="" />
	<table>
		<tbody>
			<tr>
			   <td>
					<iframe id="availableProcessPointList" src="processpointgroup/AvailableProcessPointList.jsp" name="availableProcessPointList" width="500" height="400" align="top" frameBorder="0" scrolling="no"></iframe>
			   </td>
			   <td align="center" class="settingstext">
					<input id="deleteButton" type="button" value="&nbsp;<< Update&nbsp;" disabled="true" style="width=80" onclick="removeProcessPoint()">
					<P/>
					<input id="addButton" type="button" value="&nbsp;Update >>&nbsp;" disabled="true" style="width=80" onclick="addProcessPoint()">
			   </td>			   
			   <td>
					<iframe id="assignedProcessPointList" src="processpointgroup/AssignedProcessPointList.jsp"  name="assignedProcessPointList" width="500" height="400" frameBorder="0" scrolling="no"></iframe>
			   </td>
			</tr>

			<tr>
				<td colspan="3">
			    	<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
				   		<bean:write name="msg"/>
				 	</html:messages>
				 	<html:errors />
			  	</td>
			</tr>
		</tbody>
	</table>
</html:form>
</body>
</html:html>
