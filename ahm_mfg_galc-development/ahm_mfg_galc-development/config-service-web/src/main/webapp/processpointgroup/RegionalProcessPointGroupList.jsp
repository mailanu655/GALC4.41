
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<head>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.RegionalProcessPointGroupListForm"  %>
<%@ page import="com.honda.galc.dto.*" %>
<%@ page import="java.util.*" %>
<%@ page session="false" %>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Content-Style-Type" content="text/css">
<link href="theme/Master.css" rel="stylesheet" type="text/css">
<title>RegionalProcessPointGroupList.jsp</title>

<script>
var enterKeyCode = 13;

function searchTextKeyUp(event){
	if(event.keyCode === enterKeyCode || event.key.toLowerCase() === 'enter'){
		var gForm = document.regionalProcessPointGroupListForm;
		var tbody = parent.frames['regionalCodeList'].document.getElementById('rctable');
		var rows = tbody.getElementsByTagName("tr"); 
		for(i = 0; i < rows.length; i++) {    
    		if(rows[i].style.backgroundColor == '#cccccc' || rows[i].style.backgroundColor == 'rgb(204, 204, 204)') {
    			var cols = rows[i].getElementsByTagName("td");
    			for(j = 0; j < cols.length; j++) {
    				if(cols[j].id == "regionalValue") {
    					gForm.categoryCode.value = cols[j].innerHTML;
    				}
    			}
    		}
   		}
	}
}

function regionalGroupSelected(rowid) {
   document.getElementById(rowid).style.backgroundColor = '#cccccc';
   
   var tbody = document.getElementById("pgtable");
   var rows = tbody.getElementsByTagName("tr"); 
   
	for(i = 0; i < rows.length; i++) {           
    	if(rows[i].id != rowid) {
      		rows[i].style.backgroundColor = "#eeeeee";
      	}
   	}
	resetProcessPointLists();   	
}

function resetProcessPointLists() {
	parent.frames['processPointGroupSettings'].document.processPointGroupSettingsForm.submit();
}

</script>

<%
	RegionalProcessPointGroupListForm groupForm = (RegionalProcessPointGroupListForm) request.getAttribute("regionalProcessPointGroupListForm");
	List<RegionalProcessPointGroupDto> regionalGroups = null;

	if(groupForm != null) {
  		regionalGroups = groupForm.getRegionalGroups();
	}
%>

</head>

<body class="settingspage" onload="resetProcessPointLists()">
<html:form styleId="RegionalProcessPointGroupList" action="/regionalProcessPointGroupList" name="regionalProcessPointGroupListForm" type="com.honda.galc.system.config.web.forms.RegionalProcessPointGroupListForm" scope="request">
	<html:hidden property="categoryCode" value="" />
	<table>
		<tbody>
			<tr> 
				<td>
			    	<b>Regional Groups</b>
			    </td>
			    <td style="text-align:right;">
			    	Filter: <html:text property="searchText" onkeypress="javascript:searchTextKeyUp(event);"></html:text>
			    </td>
			</tr>	
			<tr>		
				<td colspan="2" width="100%">
					<div style="width: 500px; height: 210px; overflow: auto;">
					<table border="1" style="width: 100%; empty-cells: show;">
						<thead>
  							<tr>
    							<th>Process Group</th>
								<th>Site</th>
    							<th>Name</th>
    							<th>Abbreviation</th>
    							<th>Description</th>
  							</tr>
						</thead>
						<%
  							if (regionalGroups != null && regionalGroups.size() > 0) {
						%>

							<tbody id="pgtable">
							<%
     							int idx = 0;
     							for(RegionalProcessPointGroupDto group : regionalGroups) {
        							idx++;
        							String rowid = "approw"+idx;
							%>
								<tr style="background-color: #eeeeee" id="<%= rowid %>" onclick="regionalGroupSelected('<%= rowid %>' );">
  
  									<td id="value"><%= group.getRegionalValue() %></td>
  									<td id="site"><%= group.getSite() %></td>
  									<td id="name"><%= group.getRegionalValueName() %></td>
  									<td id="abbr"><%= group.getRegionalValueAbbr() %></td>
  									<td id="desc"><%=group.getRegionalValueDesc() %></td>
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
