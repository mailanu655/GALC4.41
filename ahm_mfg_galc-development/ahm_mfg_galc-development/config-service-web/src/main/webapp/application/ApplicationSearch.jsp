
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page session="false" %>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>ApplicationSearch.jsp</TITLE>
<script>
var enterKeyCode = 13;
var searchTextMinLength = 2;

function resizeframe() {

  tr = document.getElementById("triframe");
  fr = document.getElementById("resultiframe");
  
  //alert(tr.offsetHeight);
  fr.style.height = tr.offsetHeight - 10;
}

function onApplicationTypeChange(){
	applicationSearchForm.elements.searchText.style.display = "inline-block";
	applicationSearchForm.elements.searchText.value = '';
	if(applicationSearchForm.elements.applicationType.value === "-1"){
		applicationSearchForm.elements.searchText.style.display = "none";
	}
	submitForm();
}

function searchTextKeyUp(event){
	if(event.keyCode === enterKeyCode || event.key.toLowerCase() === 'enter'){
		var searchTextLength = applicationSearchForm.elements["searchText"].value.length;

		if(searchTextLength > 0 && searchTextLength < searchTextMinLength){
			alert("You must enter at least " + searchTextMinLength + " char(s) to search");
		}else{
			submitForm();			
		}		
		
		//Avoid to perform a default struts form submit on which a new application will be created
		event.preventDefault();
		return false;
	}
}

function submitForm(){
	applicationSearchForm.submit();
}

function createApplication() {
   newurl = "<%=request.getContextPath() %>/applicationSettings.do?createFlag=true&initializePage=true";
   
   parent.frames['appSettingsFrame'].location = newurl;
}
</script>
</HEAD>

<BODY class="settingspage" onResize="resizeframe();">
<html:form target="findResults" action="/applicationSearch" name="applicationSearchForm" type="com.honda.global.galc.system.config.web.forms.ApplicationSearchForm" scope="request">
	<TABLE height="100%" cellspacing="1">
		<TBODY>
			<TR> 
				<TD>
			    	<html:select property="applicationType" onchange="javascript:onApplicationTypeChange();">
			        	<html:option value="-1">Select an option</html:option>
			        	<html:optionsCollection name="applicationSearchForm" property="applicationTypes" label="typeString" value="id" />
			      	</html:select>				    
			    	&nbsp;<html:text property="searchText" onkeypress="javascript:searchTextKeyUp(event);" style="display: none;"></html:text>
			    </TD>
			</TR>			
			<tr id="triframe">
			  <td valign="top"  >
			    <IFRAME id="resultiframe" name="findResults"  width="100%" align="top" marginheight="0" marginwidth="0" onload="resizeframe();"></IFRAME>
			  </td>
			</tr>
            <tr height="35">
              <td>
                 <button onclick="createApplication(); return false;">Create</button>
              </td>
            </tr>
		</TBODY>
	</TABLE>
</html:form>
</BODY>
</html:html>
