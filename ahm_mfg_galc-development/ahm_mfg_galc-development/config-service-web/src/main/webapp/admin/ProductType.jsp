
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.ProductTypeForm" %>
<%@ page import ="com.honda.galc.data.ProductType" %>
<%@ page import ="java.util.*" %>
<%@ page session="false" %>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>Product Type Details</TITLE>
<script>

function show(sObjectID) {
		document.getElementById(sObjectID).style.display='';
}
		
function hide(sObjectID) {
		document.getElementById(sObjectID).style.display='none';
}

function deleteConfirmation() {
 
    var productType = document.getElementById("productTypeForm").productType.value;
    
    var result = prompt("To delete the product type, enter the Product Type in the field below:","");
    
    if (result != productType)
    {
       alert("Product Type will not be deleted");
       
       return false;
    }
    
    document.getElementById("productTypeForm").deleteConfirm.value="true";
    return true;

}

function refreshProductTypeList() {

   parent.frames['resultiframe'].window.document.productTypeLoadForm.submit();
}

</script>
</HEAD>
<%
    ProductTypeForm productTypeForm = (ProductTypeForm)request.getAttribute("productTypeForm");
    
    boolean readonlyID = false;
    String readonlyClass = "";
 
    
    if (productTypeForm.isExistingProductType())
    {
      readonlyID = true;
      readonlyClass = "readonly";
 
    }
    
    boolean disableApply = false;
    boolean disableDelete = false;
    boolean disableCancel = false;
    
    if (!productTypeForm.isEditor())
    {
       disableApply = true;
       disableDelete = true;
       
    }
    
    if (productTypeForm.isDeleteConfirm())
    {
       // Must have already deleted
       disableApply = true;
       disableDelete = true;       
       disableCancel = true;
    }
    
    if (!productTypeForm.isExistingProductType())
    {
       disableDelete = true;
       disableCancel = true;
    }
    
    // Determine if we want to refresh the parent list
    String onloadcmd ="";
    
    if (productTypeForm.isRefreshList())
    {
       onloadcmd="refreshProductTypeList()";
    }
    
   

 %>
<BODY class="settingspage" onload="<%=onloadcmd %>" >
<html:form styleId="productTypeForm" action="/productType" name="productTypeForm"   scope="request" type="com.honda.galc.system.config.web.forms.ProductTypeForm">
    <html:hidden property="initializePage" />
    <html:hidden property="existingProductType" />
    <html:hidden styleId="deleteConfirm" property="deleteConfirm"/>
	<TABLE border="0">
		<TBODY>
			<TR>
				<TH class="settingstext" align="left">Product Type</TH>
				<TD>
				    <bean:define id="productTypeList" property="productTypes" type="java.util.LinkedHashMap" name="productTypeForm" />
					<html:select property="productType">
						<html:options collection="productTypeList" property="key" labelProperty="value"/>
					</html:select>
				    
				</TD>
				<td>&nbsp</td>
			</TR>		
			<TR>
				<TH class="settingstext" align="left">Product Type Label</TH>
				<TD align="left"><html:text property="productTypeLabel" size="48" maxlength="64"/></TD>
				<td>&nbsp</td>
			</TR>
			<TR>
				<TH class="settingstext" align="left">Product Spec Code Format</TH>
				<TD align="left"> <html:text property="productSpecCodeFormat" size="48" maxlength="64"/></TD>
				<td>&nbsp</td>
			</TR>
			
			<TR>
				<TH class="settingstext" align="left">Product Spec Code Label</TH>
				<TD align="left"> <html:text property="productSpecCodeLabel" size="48" maxlength="64"/></TD>
				<td>&nbsp</td>
			</TR>
			
			<TR>
				<TH class="settingstext" align="left">Owner Product Type</TH>
				<TD>
				    <bean:define id="productTypeList" property="productTypes" type="java.util.LinkedHashMap" name="productTypeForm" />
					<html:select property="ownerProductType">
						<html:options collection="productTypeList" property="key" labelProperty="value"/>
					</html:select>
				    
				</TD>
				<td>&nbsp</td>
			</TR>
			
				<TR>
				<TH class="settingstext" align="left">Product Id Label</TH>
				<TD align="left"> <html:text property="productIdLabel" size="48" maxlength="64"/></TD>
				<td>&nbsp</td>
			</TR>
			
				<TR>
				<TH class="settingstext" align="left">Product Id Format</TH>
				<TD align="left"> <html:text property="productIdFormat" size="48" maxlength="64"/></TD>
				<td>&nbsp</td>
			</TR>
			
			<tr>
			  <td colspan="3">
			     <html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
				   <bean:write name="msg"/>
				 </html:messages>
				 <html:errors />
			  </td>
			</tr>
			
			<TR>
				<TD colspan="3"><html:submit property="apply" value="Apply" disabled="<%= disableApply %>"  onclick="applyChanges(); return true;" />&nbsp;&nbsp;
				                <html:submit property="delete" value="Delete" disabled="<%= disableDelete %>" onclick="return deleteConfirmation();" />&nbsp;&nbsp;
				                <html:submit property="cancel" value="Cancel" disabled="<%= disableCancel %>"/>&nbsp;&nbsp;
				</TD>
			</TR>
		</TBODY>
	</TABLE>
</html:form>
</BODY>
</html:html>
