<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.honda.galc.system.config.web.forms.ComponentStatusListForm"%>
<%@ page import="com.honda.galc.entity.conf.*" %>
<%@ page import="java.util.*" %>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<title>Component Status List</title>
<script>
function showAlert(key,val,desc,newstat) {
   alert(key+" "+val+" "+desc+" "+newstat);
}

function fillFields(key,desc) {
   
   myform = parent.window.document.forms['componentStatusSettingsForm'];
   
   myform.statusKey.value = key;
   myform.statusValue.value = document.getElementById(key).firstChild.data;
   myform.description.value = desc;
   
   var newstat = 'true';
   
   if (newstat == 'true')
   {
      myform.statusKey.readOnly = false;
      myform.statusKey.styleClass="";
   }
   else
   {
      myform.statusKey.readOnly = true;
      myform.statusKey.styleClass="readonly";
   }
}
</script>
</head>
<body class="settingspage">
<%
ComponentStatusListForm componentStatusListForm = (ComponentStatusListForm)request.getAttribute("componentStatusListForm");
if (componentStatusListForm == null) {
	componentStatusListForm = new ComponentStatusListForm();

}

List<ComponentStatus> componentStatusList = componentStatusListForm.getComponentStatusDataList();

 %>


<table border="1">
<tbody>
<%
int idx = 0;
String rowclass = "";
for(ComponentStatus data : componentStatusList) {
   idx++;
   
   if (idx % 2 == 0) {
      rowclass = "evenrow";
   }
   else {
      rowclass = "oddrow";
   }
   
   String displayValue = data.getStatusValue();
   if (displayValue == null || displayValue.length() == 0) {
      displayValue = "&nbsp";
   }
   
   String displayDescription = data.getDescription();
   if (displayDescription == null || displayDescription.length() == 0) {
      displayDescription="&nbsp;";
   }
%>
<tr class="<%=rowclass %>" id="row_<%= data.getId().getStatusKey() %>" >
<td><a class="settingspagelink" onclick="fillFields('<%= data.getId().getStatusKey() %>','<%= data.getDescription() %>'); return false;"><%= data.getId().getStatusKey() %></a></td>
<td><%= displayValue %><p id="<%= data.getId().getStatusKey() %>" style="display: none;"><%= data.getStatusValue() %></p></td>
<td><%= displayDescription %></td>
</tr>
<%
}
 %>
</tbody>
</table>
</body>
</html:html>