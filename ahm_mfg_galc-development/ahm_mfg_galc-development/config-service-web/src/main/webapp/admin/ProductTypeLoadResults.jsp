
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="false" %>
<%
	response.setHeader("Pragma","no-cache");
%>
<%
	response.setHeader("Cache-control", "no-cache");
%>
<%
	response.setHeader("Expires", "0");
%>	
<%@ page import="com.honda.galc.system.config.web.forms.ProductTypeLoadForm" %>
<%@ page import="com.honda.galc.entity.product.*" %>
<%@ page import="java.util.*" %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>Product Type Load Results</TITLE>
<SCRIPT>

function displayProductType(productType, rowid) {

newurl = "<%=request.getContextPath()%>/productType.do?existingProductType=true&productType="+productType;
   
   parent.frames['settingsIFrame'].location = newurl;
   
   document.getElementById(rowid).style.backgroundColor = '#cccccc';
   
   var tbody = document.getElementById("typeTablebody");
   
   var rows = tbody.getElementsByTagName("tr"); 
   
   for(i = 0; i < rows.length; i++){           
      
      
      if (rows[i].id != rowid)
      {
        rows[i].style.backgroundColor = "white";
      }
   }
   

}

</SCRIPT>
</HEAD>
<%
	ProductTypeLoadForm searchForm = (ProductTypeLoadForm)request.getAttribute("productTypeLoadForm");
  
  if (searchForm == null)
  {
     searchForm = new ProductTypeLoadForm();
  }
%>
<BODY class="settingspage">
<html:form action="/productTypeLoad" />
<table border="1">
<THEAD>
  <tr> 
    <th>Product Type</th><th>Product Type Label </th>
  </tr>
</THEAD>
<tbody id="typeTablebody">
<%
   List<ProductTypeData> types = searchForm.getProductTypes();
   
   int idx=0;
   for(ProductTypeData typeData : types) {
      String rowid = "userrow"+idx;
 %> 
<tr style="background-color: white" id="<%= rowid %>" onclick="displayProductType('<%=typeData.getProductTypeName() %>','<%= rowid %>' )">
<td><%= typeData.getProductTypeName() %></td>
<td><%= typeData.getProductTypeLabel() %></td>
</tr>    
 <%
     idx++;
   }
 %>
 </tbody>
 </table>
</BODY>
</html:html>
