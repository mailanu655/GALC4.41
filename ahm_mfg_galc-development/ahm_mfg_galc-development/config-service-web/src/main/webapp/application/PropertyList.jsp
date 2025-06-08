<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="com.honda.galc.system.config.web.forms.PropertyListForm"%>
<%@ page import="com.honda.galc.entity.conf.*" %>
<%@ page import="java.util.*" %>
<html:html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<title>Property List</title>
<script>
function showAlert(key,val,desc,newprop) {
   alert(key+" "+val+" "+desc+" "+newprop);
}

function fillFields(key,val,desc) {
   
   myform = parent.window.document.forms['propertySettingsForm'];
   
   myform.propertyKey.value = key;
   myform.propertyValue.value = val;
   myform.description.value = desc;
   
   var newprop = 'true';
   
   if (newprop == 'true')
   {
      myform.propertyKey.readOnly = false;
      myform.propertyKey.styleClass="";
   }
   else
   {
      myform.propertyKey.readOnly = true;
      myform.propertyKey.styleClass="readonly";
   }
}
</script>
</head>
<body class="settingspage">
<%
PropertyListForm propertyListForm = (PropertyListForm)request.getAttribute("propertyListForm");
if (propertyListForm == null) {
	propertyListForm = new PropertyListForm();

}

List<ComponentProperty> propertyList = propertyListForm.getPropertyDataList();

 %>


<table border="1">
<tbody>
<%
int idx = 0;
String rowclass = "";
for(ComponentProperty data : propertyList) {
   idx++;
   
   if (idx % 2 == 0) {
      rowclass = "evenrow";
   }
   else {
      rowclass = "oddrow";
   }
   
   String displayValue = data.getPropertyValue();
   if (displayValue == null || displayValue.length() == 0) {
      displayValue = "&nbsp";
   }
   
   String displayDescription = data.getDescription();
   if (displayDescription == null || displayDescription.length() == 0) {
      displayDescription="&nbsp;";
   }
%>
<tr class="<%=rowclass %>" id="row_<%= data.getId().getPropertyKey() %>" >
<td><a class="settingspagelink" onclick="fillFields('<%= data.getId().getPropertyKey().replace("\\","\\\\").replace("\'","\\\'").replace("\"","\\&quot;") %>','<%= data.getPropertyValue().replace("\\","\\\\").replace("\'","\\\'").replace("\"","\\&quot;") %>','<%= data.getDescription() == null ? null : data.getDescription().replace("\\","\\\\").replace("\'","\\\'").replace("\"","\\&quot;") %>'); return false;"><%= data.getId().getPropertyKey() %></a></td>
<td><%= displayValue %><p id="<%= data.getId().getPropertyKey() %>" style="display: none;"><%= data.getPropertyValue() %></p></td>
<td><%= displayDescription %></td>
</tr>
<%
}
 %>
</tbody>
</table>
</body>
</html:html>