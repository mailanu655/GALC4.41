<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.GpcsForm" %>
<%@ page import="com.honda.galc.dto.GpcsDto" %>

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

var siteWithPlantNameList;
var plantWithDivisionNameList;

function show(sObjectID) {
		document.getElementById(sObjectID).style.display='';
}
		
function hide(sObjectID) {
		document.getElementById(sObjectID).style.display='none';
}

function setButton(actionType) {
	
	if(actionType == 'view') {
		hide('subsection');
		
	} else if(actionType == 'add') {
		show('subsection');
		show('add');
		hide('delete');
		hide('update');
		show('cancel');
		
	} else if(actionType == 'update') {
		show('subsection');
		hide('add');
		hide('delete');
		show('update');
		show('cancel');
		
	} else if(actionType == 'delete') {
		show('subsection');
		hide('add');
		show('delete');
		hide('update');
		show('cancel');
	}
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

function setDefaultView(plantname,siteNameWithPlant,plantWithDivisionMap) {
	
	siteWithPlantNameList = siteNameWithPlant;
	plantWithDivisionNameList = plantWithDivisionMap;
	hide('subsection');
	document.getElementById("radioView").selected=true;
	
}

 function resetDropDown() {
	var selectedPlantNameVar = document.getElementsByName("selectedPlantName")[0];
	selectedPlantNameVar.innerHTML = "";
	
	var selectedPlantNameVar = document.getElementsByName("selectedDivision")[0];	
	selectedPlantNameVar.innerHTML = "";
} 

function populatePlant(siteName,gridSiteName) {
	
	resetDropDown();
	
	var siteNameSelected;
	if(gridSiteName !=null || gridSiteName!=undefined) {
		siteNameSelected = gridSiteName.trim();
	} else {
		siteNameSelected = siteName.selectedOptions[0].innerText.trim();
	}
	
	var localSiteWithPlantNameList = siteWithPlantNameList;
	
	localSiteWithPlantNameList= localSiteWithPlantNameList.replace(/\{/g,"");
	localSiteWithPlantNameList= localSiteWithPlantNameList.replace(/\}/g,"");
	localSiteWithPlantNameList= localSiteWithPlantNameList.replace(/\[/g,"");
	localSiteWithPlantNameList= localSiteWithPlantNameList.replace(/\],/g,"#");
	localSiteWithPlantNameList= localSiteWithPlantNameList.replace(/\]/g,"");
	var plantNameArray = localSiteWithPlantNameList.split("#");
	
	for(var count=0;count<plantNameArray.length;count++) {
		var singlePlantValue = plantNameArray[count].split("=");
		
		if(singlePlantValue[0].trim().toLowerCase() == siteNameSelected.toLowerCase()) {
			
			var selectedPlantNameVar = document.getElementsByName("selectedPlantName")[0];
	
			selectedPlantNameVar.innerHTML = "";
			
			var optionEmpty = document.createElement("option");
			optionEmpty.text = "";
			optionEmpty.value="";
			document.getElementsByName("selectedPlantName")[0].appendChild(optionEmpty);
			
			singlePlantValue[1].split(",").forEach( function(item) { 
				   var option = document.createElement("option");
				   option.text = item;
				   option.value=item;
				   document.getElementsByName("selectedPlantName")[0].appendChild(option);
			});

			break;
		}		
	}	
}

function populateDivision(plantName,gridPlantName) {

	var plantNameSelected;
	if(gridPlantName !=null || gridPlantName!=undefined) {
		plantNameSelected = gridPlantName.trim();
	} else {
		plantNameSelected = plantName.selectedOptions[0].innerText.trim();
	}
	
	
	var localSiteWithPlantNameList = plantWithDivisionNameList;
	
	localSiteWithPlantNameList= localSiteWithPlantNameList.replace(/\{/g,"");
	localSiteWithPlantNameList= localSiteWithPlantNameList.replace(/\}/g,"");
	localSiteWithPlantNameList= localSiteWithPlantNameList.replace(/\[/g,"");
	localSiteWithPlantNameList= localSiteWithPlantNameList.replace(/\],/g,"#");
	localSiteWithPlantNameList= localSiteWithPlantNameList.replace(/\]/g,"");
	var plantNameArray = localSiteWithPlantNameList.split("#");
	
	for(var count=0;count<plantNameArray.length;count++) {
		var singlePlantValue = plantNameArray[count].split("=");
		
		if(singlePlantValue[0].trim().toLowerCase() == plantNameSelected.toLowerCase()) {
			
			var selectedPlantNameVar = document.getElementsByName("selectedDivision")[0];
			
			selectedPlantNameVar.innerHTML = "";
			singlePlantValue[1].split(",").forEach( function(item) { 
				   var option = document.createElement("option");
				   option.text = item;
				   option.value=item;
				   document.getElementsByName("selectedDivision")[0].appendChild(option);
			});
			
			break;
		}		
	}	
	
}


function displayProductType(idx,rowid,selectedSiteName,selectedPlantName,selectDivisionName,plantCode,processLocation,lineNo,plantNameList) {
	
	 document.getElementById(rowid).style.backgroundColor = '#cccccc';
	   
	   var tbody = document.getElementById("typeTablebody");
	   
	   var rows = tbody.getElementsByTagName("tr"); 
	   
	   for(i = 0; i < rows.length; i++){           
	      
	      
	      if (rows[i].id != rowid)
	      {
	        rows[i].style.backgroundColor = "white";
	      }
	   }
	   
		
	   	var selectedSiteNameVar = document.getElementsByName("selectedSiteName")[0];
	   	var options = selectedSiteNameVar.getElementsByTagName("option");
	   	var i =0;
	   	var siteNameindexValue =0;
	   	
	   	for(i=0;i<options.length;i++) {
	   		if(options[i].textContent.trim()==selectedSiteName.trim()) {
	   			siteNameindexValue = i;
	   			break;
	   		}
	   	}
	   
	   	populatePlant(null,selectedSiteName.trim());
		document.getElementsByName("selectedSiteName")[0].selectedIndex = siteNameindexValue;
		
		var selectedPlantNameVar = document.getElementsByName("selectedPlantName")[0];
		
	   	var options = selectedPlantNameVar.getElementsByTagName("option");
	   	var i =0;
	   	var plantNameindexValue =0;
	   	
	   	for(i=0;i<options.length;i++) {
	   		if(options[i].textContent.trim()==selectedPlantName.trim()) {
	   			plantNameindexValue = i;
	   			break;
	   		}
	   	}
	   
	   	populateDivision(null,selectedPlantName.trim());
		document.getElementsByName("selectedPlantName")[0].selectedIndex = plantNameindexValue;
		
		var selectedDivisionNameVar = document.getElementsByName("selectedDivision")[0];
	   	var options = selectedDivisionNameVar.getElementsByTagName("option");
	   	var i =0;
	   	var plantDivisionindexValue =0;
	   	
	   	for(i=0;i<options.length;i++) {
	   		if(options[i].textContent.trim()==selectDivisionName.trim()) {
	   			plantDivisionindexValue = i;
	   			break;
	   		}
	   	}
	   
		document.getElementsByName("selectedDivision")[0].selectedIndex = plantDivisionindexValue;
				
		document.getElementsByName("gPCSPlantCode")[0].value=plantCode;
		document.getElementsByName("gPCSProcessLocation")[0].value=processLocation;
		document.getElementsByName("gPCSLineNo")[0].value=lineNo;
	}


</script>
</HEAD>
<%
	GpcsForm productTypeForm = (GpcsForm) request.getAttribute("gpcsForm");

	Map<String,List<String>> siteWithPlantMap = productTypeForm.getSiteWithPlantNameList();
	Map<String,List<String>> plantWithDivisionMap = productTypeForm.getPlantWithDivisionNameList();
	
    
    boolean readonlyID = false;
    String readonlyClass = "";
    
    String onloadcmd="refreshProductTypeList()";
    boolean disableView = false;
    boolean disableAdd = false;
    boolean disableUpdate = false;
    boolean disableDelete = false;
    
    Map<String,String> plantNameMap = productTypeForm.getPlantNameMap();
 
 %>

<BODY class="settingspage" onload="setDefaultView('<%= plantNameMap%>','<%= siteWithPlantMap %>','<%= plantWithDivisionMap%>')" >
<html:form styleId="gPCSForm" action="/gpcs" name="gpcsForm" scope="request" type="com.honda.galc.system.config.web.forms.GpcsForm">
    <html:hidden property="initializePage" />
    <html:hidden property="existingGPCS" />
    <html:hidden styleId="deleteConfirm" property="deleteConfirm"/>
    
<TD colspan="3">

<!--  <label class ="radio">
<input type= "radio" name = "View" checked>

html:radio property="view"  -->
<TH class="settingstext" align="left" >View:</TH>
<input type= "radio"  name= "view" styleId="radioView" value = "View" onclick="setButton('view')" checked/>  &nbsp;&nbsp; 

<TH class="settingstext" align="left" >Add:</TH>
<input type= "radio" name="view"   value="Add" onclick="setButton('add')"/>&nbsp;&nbsp;

<TH class="settingstext" align="left">Update:</TH>
<input type= "radio" name="view"  value="Update" onclick="setButton('update')"/> &nbsp;&nbsp;

<TH class="settingstext" align="left">Delete:</TH>
<input type= "radio" name="view"   value="Delete" onclick="setButton('delete')"/>&nbsp;&nbsp; 





</TD>
   <BR></BR>
    <table border="1">
<THEAD>
  <tr> 
 <%--  <th>Site Name</th><th>Plant Name </th> <th>Division Id</th> <th>GPCS Process Location</th>  <th>GPCS Line No</th> <th>Plan Code</th>   --%>
    <th>Site Name</th><th>Plant Name </th> <th>Division Id</th> <th>Plant Code</th> <th>GPCS Process Location</th>  <th>GPCS Line No</th> <th>GPCS Plan Code</th>
    
  </tr>
</THEAD>
<tbody id="typeTablebody">
<%
if(productTypeForm!=null) {
	
	List<GpcsDto> gpscList = productTypeForm.getgPCSData();
	
   if(gpscList!=null && gpscList.size()>0) {
	   
   
   int idx=0;
   for(GpcsDto typeData : gpscList) {
      String rowid = "userrow"+idx;
 %> 
<tr style="background-color: white" id="<%= rowid %>" onclick="displayProductType('<%=idx%>','<%= rowid %>','<%= typeData.getSiteName() %>','<%= typeData.getPlantName() %>','<%= typeData.getDivisionIdAndName() %>','<%=typeData.getgPCSPlantCode()%>','<%=typeData.getgPCSProcessLocation()%>','<%=typeData.getgPCSLineNo()%>','<%=plantNameMap%>')">
<td><%= typeData.getSiteName() %></td>
<td><%= typeData.getPlantName() %></td>
<td><%= typeData.getDivisionIdAndName() %></td>
<td><%= typeData.getgPCSPlantCode() %></td>
<td><%= typeData.getgPCSProcessLocation() %></td>
<td><%= typeData.getgPCSLineNo() %></td>
<td><%= typeData.getPlanCode() %></td>
</tr>    

 <%
     idx++;
   }
   }
}
 %>

 </tbody>
 </table>			
<div id="subsection">
<div style="margin-top: 30px">
<span id = "site">
<span class="settingstext" align="left" white-space: nowrap style="font-weight: bold;
    font-size: 12pt;"> 

<bean:define id="siteNameList" property="siteNameMap" type="java.util.LinkedHashMap" name="gpcsForm" /> 
<font color ="black">Site: </font><html:select property="selectedSiteName"  onchange="populatePlant(this,null)">
 <option selected disabled></option>
<html:options collection="siteNameList" property="key" labelProperty="value" />
</span>
</html:select>				    

</span> 
                  
<span id = "plant">
<span class="settingstext" align="left" style="font-weight: bold;
    font-size: 12pt;> 
<bean:define id="plantNameList" property="plantNameMap" type="java.util.LinkedHashMap" name="gpcsForm" />
<font color ="black">Plant: </font><html:select property="selectedPlantName" onchange="populateDivision(this,null)">
<select id= "plant"></select>
</span>
</html:select>				    
</span>				
				 
<span id ="division">
<span class="settingstext" align="left" style="font-weight: bold;
    font-size: 12pt;>
<bean:define id="divisionNameList" property="divisionIdAndMap" type="java.util.LinkedHashMap" name="gpcsForm" />
<font color ="black">Division: </font><html:select property="selectedDivision"> 

 </span>
</html:select>				    
</span>				
</div>

				
<DIV id ="plantCode" >
<H4 class="settingstext" align="left">
<font color ="black">GPCS Plant Code: </font><TD align="center"> <html:text property="gPCSPlantCode" size="4" maxlength="4" style="text-transform:uppercase" onkeyup="this.value = this.value.toUpperCase()"/><font color = "grey" size = 2> 4 characters</font></TD>
<td>&nbsp</td>
</H4>
</DIV>

<DIV id ="processLocation">
<H4 class="settingstext" align="left">
<font color ="black">GPCS Process Location: </font><TD align="left"> <html:text property="gPCSProcessLocation" size="2" maxlength="2" style="text-transform:uppercase" onkeyup="this.value = this.value.toUpperCase()" /><font color = "grey" size = 2> 2 characters</font></TD>
<td>&nbsp</td>
</H4>
</DIV>

<DIV id ="lineNo">
<H4 class="settingstext" align="left">
<font color ="black">GPCS Line No: </font><TD align="left"> <html:text property="gPCSLineNo" size="2" maxlength="2" /><font color = "grey" size = 2> 2 Characters</font></TD>
<td>&nbsp</td>
</H4>
</DIV>

    
<DIV id= buttons>    
<html:submit property="buttonName"  value="Add" disabled="<%= disableAdd %>" styleId="add"  onclick="addChanges(); return true;" />&nbsp;&nbsp;
<html:submit property="buttonName"  value="Delete" disabled="<%= disableDelete %>" styleId="delete" onclick="return deleteConfirmation();" />&nbsp;&nbsp;
<html:submit property="buttonName"  value="Update" disabled="<%= disableUpdate %>" styleId="update" onclick="return updateConfirmation();" />&nbsp;&nbsp;
<html:submit property="buttonName"  value="Cancel"  onclick="return cancelConfirmation();" styleId="cancel"/>&nbsp;&nbsp;
 </DIV>
</div>
</html:form>

</BODY>
</html:html>