<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<head>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.RegionalCodeListForm"  %>
<%@ page import="com.honda.galc.entity.conf.*" %>
<%@ page import="java.util.*" %>
<%@ page session="false" %>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Content-Style-Type" content="text/css">
<link href="theme/Master.css" rel="stylesheet"
	type="text/css">
<title>RegionalCodeList.jsp</title>

<script>

function regionalCodeSelected(codeValue, rowid) {
   document.getElementById(rowid).style.backgroundColor = '#cccccc';
   
   var tbody = document.getElementById("rctable");
   var rows = tbody.getElementsByTagName("tr"); 
   
	for(i = 0; i < rows.length; i++) {           
    	if(rows[i].id != rowid) {
      		rows[i].style.backgroundColor = "#eeeeee";
      	}
   	}
	filterGroups(codeValue);
}

function filterGroups(codeValue) {
	var groupForm = parent.frames['regionalProcessPointGroupList'].document.regionalProcessPointGroupListForm;
	groupForm.searchText.value = "";
	groupForm.categoryCode.value = codeValue;
	groupForm.submit();
}

function resetGroups() {
	filterGroups("");
}

</script>

<%
	RegionalCodeListForm codeForm = (RegionalCodeListForm) request.getAttribute("regionalCodeListForm");
	List<RegionalCode> regionalCodes = null;

	if(codeForm != null) {
  		regionalCodes = codeForm.getRegionalCodes();
	}
%>

</head>

<body class="settingspage" onload="resetGroups()">
<html:form action="/regionalCodeList" name="regionalCodeListForm" type="com.honda.galc.system.config.web.forms.RegionalCodeListForm" scope="request">
	
	<table border="0">
		<tbody>
			<tr> 
				<td align="center">
			    	<b>Regional Flag Values</b>
			    </td>
			    <td style="text-align:right;">
			    	Filter: <html:text property="searchText"></html:text>
			    </td>
			</tr>			
			<tr>
				<td colspan="2" align="center">
					<div style="width: 500px; height: 210px; overflow: auto;">
					<table border="1" style="width: 100%; empty-cells: show;">
						<thead>
  							<tr>
    							<th>Value</th>
    							<th>Name</th>
    							<th>Abbr</th>
    							<th>Description</th>
  							</tr>
						</thead>

						<%
							if (regionalCodes != null && regionalCodes.size() > 0) {
						%>
							<tbody id="rctable">
							<%
     							int idx = 0;
     							for(RegionalCode code : regionalCodes) {
        						idx++;
        						String rowid = "approw"+idx;
							%>
								<tr style="background-color: #eeeeee" id="<%= rowid %>" onclick="regionalCodeSelected('<%=code.getId().getRegionalValue() %>','<%= rowid %>' );">
  
  									<td id="regionalValue"><%= code.getId().getRegionalValue() %></td>
  									<td id="regionalValueName"><%= code.getRegionalValueName() %></td>
  									<td id="regionalValueAbbr"><%= code.getRegionalValueAbbr() %></td>
  									<td id="regionalValueDesc"><%= code.getRegionalValueDesc() %></td>
  
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
