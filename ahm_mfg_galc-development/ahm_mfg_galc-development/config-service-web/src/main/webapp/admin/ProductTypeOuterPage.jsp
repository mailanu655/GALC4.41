
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>Product Type Configuration</TITLE>

<script>
function resizeframe() {

  tr = document.getElementById("triframe");
  fr = document.getElementById("resultiframe");
  
  //alert(tr.offsetHeight);
  fr.style.height = tr.offsetHeight - 10;
}

function resizeSettingsFrame() {

  tr = document.getElementById("trouter");
  fr = document.getElementById("settingsiframe");
  
  //alert(tr.offsetHeight);
  fr.style.height = tr.offsetHeight - 10;
}

function createProductType() {
   newurl = "<%=request.getContextPath() %>/productType.do?existingProdutType=false";
   
   window.frames['settingsIFrame'].location = newurl;
}

function submit(){
    document.forms[0].submit();
}

</script>
</HEAD>
<BODY class="settingspage" onload="submit()">
<h1 class="settingsheader">Product Type Configuration</h1>
<table width="100%" height="100%">
<tbody>
<tr id="trouter">
<td>
<html:form target="findResults" action="/productTypeLoad" scope="request" 
	name="productTypeLoadForm" style="height: 100%;"
	type="com.honda.galc.system.config.web.forms.ProductTypeLoadForm">
	<html:hidden property="initializePage" value="false" />
	<TABLE border="0" height="100%">
		<TBODY>
			<TR>
				<td><button onclick="createProductType(); return false;">New&nbsp;Type</button></TD>
			</TR>
			<tr id="triframe">
			<td colspan="4">
			  <IFRAME id="resultiframe" name="findResults"  width="100%" align="top" marginheight="0" marginwidth="0" onload="resizeframe();"></IFRAME>
			</td>
			</tr>
		</TBODY>
	</TABLE>
</html:form>
</td>
<td width="75%">
<IFRAME id="settingsiframe" src="<c:url value="/common/Blank.jsp"></c:url>" frameborder="0" name="settingsIFrame"  width="100%" align="top" marginheight="0" marginwidth="0" onload="resizeSettingsFrame();" ></IFRAME>
</td>
</tr>
</tbody>
</table>
</BODY>
</html:html>
