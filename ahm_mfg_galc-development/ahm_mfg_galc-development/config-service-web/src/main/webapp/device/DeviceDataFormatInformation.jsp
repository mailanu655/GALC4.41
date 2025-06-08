
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>Device Data Format Configuration</TITLE>
<style type="text/css">

.autocomplete {
  position: relative;
  display: inline-block;
}
.scroll {
  height: 220px;
  position: absolute;
  border: 1px solid #d4d4d4;
  border-bottom: none;
  border-top: none;
  z-index: 99;
  top: 100%;
  left: 0;
  right: 0;
  overflow-y: scroll;
  overflow-x: hidden;
}
.scroll div {
  padding: 3px;
  cursor: pointer;
  background-color: #fff; 
  border-bottom: 1px solid #d4d4d4; 
}
.scroll div:hover {
  background-color: #e9e9e9; 
}
.autocomplete-items {
  position: absolute;
  border: 1px solid #d4d4d4;
  border-bottom: none;
  border-top: none;
  z-index: 99;
  top: 100%;
  left: 0;
  right: 0;
}
.autocomplete-items div {
  padding: 3px;
  cursor: pointer;
  background-color: #fff; 
  border-bottom: 1px solid #d4d4d4; 
}
.autocomplete-items div:hover {
  background-color: #e9e9e9; 
}
.autocomplete-active {
  background-color: DodgerBlue !important; 
  color: #ffffff; 
}
</style>
<script language="javascript">
<%com.honda.galc.system.config.web.forms.DeviceDataFormatForm deviceForm = (com.honda.galc.system.config.web.forms.DeviceDataFormatForm)request.getAttribute("deviceDataFormatForm");
java.util.Map tagTypes = deviceForm.getDeviceTagTypes();
java.util.Map dataTypes = deviceForm.getDataTypes();
java.util.List<String> clientsList = (java.util.List<String>)request.getAttribute("clients");

//need assign sequence value to them
String strChkSeq = "<input type=\"radio\" name=\"selectedSeq\" ";
String strTxtSeq = "<input type=\"text\" name=\"sequenceNumber\" readonly=\"readonly\" style=\"width: 30\" ";

StringBuffer sb = new StringBuffer();
sb.append("<select name=\"tagType\" style=\"width: 130\">");
//set the options
java.util.Set keys = tagTypes.keySet();
java.util.Iterator it = keys.iterator();
while(it.hasNext()) {
	String key = (String)it.next();
	sb.append("<option value=" + key + ">" + tagTypes.get(key).toString() + "</option>");
}
sb.append("</select>");
String strComTagType = sb.toString();


StringBuffer sb1 = new StringBuffer();
sb1.append("<select name=\"dataType\" style=\"width: 100\">");
//set the options
java.util.Set dataTypeKeys = dataTypes.keySet();
java.util.Iterator it1 = dataTypeKeys.iterator();
while(it1.hasNext()) {
	String key = (String)it1.next();
	sb1.append("<option value=" + key + ">" + dataTypes.get(key).toString() + "</option>");
}
sb.append("</select>");
String strDataTagType = sb1.toString();

String strDCTag = "<input type=\"text\" name=\"tag\" size=\"40\" >";
String strTagValue = "<input type=\"text\" name=\"tagValue\" style=\"width: 220\" >";
String strOffset = "<input type=\"text\" name=\"offset\" style=\"width: 50\">";
String strLength = "<input type=\"text\" name=\"length\" style=\"width: 50\">";
String strTagName = "<input type=\"text\" name=\"tagName\" style=\"width: 220\" >";%>

function addDevice() {
	var oBody = document.all("deviceDataFormatItems");
	var oBodyRows = oBody.getElementsByTagName("tr");
	var iCount = oBodyRows.length;
	var iRecordID = iCount + 1;

	var oTr = document.createElement("tr");
	//create TD objects
	var oTdChkSeq = document.createElement("td");
	var oTdTxtSeq = document.createElement("td");
	var oTdComTagType = document.createElement("td");
	var oTdDataTagType = document.createElement("td");
	var oTdDCTag = document.createElement("td");
	var oTdTagValue = document.createElement("td");
	var oTdOffset = document.createElement("td");
	var oTdLength = document.createElement("td");
	var oTdTagName = document.createElement("td");
	//intial TD object inner html
	oTdChkSeq.innerHTML = '<%=strChkSeq%> value="' + iRecordID + '"/>';
	oTdTxtSeq.innerHTML = '<%=strTxtSeq%> value="' + iRecordID + '"/>';
	oTdComTagType.innerHTML = '<%=strComTagType%>';
	oTdDataTagType.innerHTML = '<%=strDataTagType%>';
	oTdDCTag.innerHTML = '<%=strDCTag%>';
	oTdTagValue.innerHTML = '<%=strTagValue%>';
	oTdOffset.innerHTML = '<%=strOffset%>';
	oTdLength.innerHTML = '<%=strLength%>';
	oTdTagName.innerHTML = '<%=strTagName%>';
	//attach TD objects to TR object
	oTr.appendChild(oTdChkSeq);
	oTr.appendChild(oTdTxtSeq);
	oTr.appendChild(oTdDCTag);
	oTr.appendChild(oTdComTagType);
	oTr.appendChild(oTdTagValue);
	oTr.appendChild(oTdOffset);
	oTr.appendChild(oTdLength);
	oTr.appendChild(oTdDataTagType);
	oTr.appendChild(oTdTagName);
	//attach TR object to Table
	oBody.appendChild(oTr);
}

function removeDevice() {
	var oBody = document.all("deviceDataFormatItems");
	var oBodyRows = oBody.getElementsByTagName("tr");
	var iCount = oBodyRows.length;
	
	var selectChk = document.all("selectedSeq");
	var selectedIndex = -1;
	//get the checked item
	for(i=0;i<selectChk.length;i++) {
		if(selectChk[i].checked) {
			selectedIndex = i;
			break;
		}
	}
	//no item selected
	if (selectedIndex<0)
		return;

	//reset the sequence number of the items after the selected item
	var changeIndex = selectedIndex;
	for(i=selectedIndex;i < iCount-1; i++) {
		changeIndex++;
		var newOTr = oBodyRows.item(changeIndex);
		var newOTrCells = newOTr.getElementsByTagName("td");
		var oldOTdChkSeq = newOTrCells.item(0);
		var oldOTdTxtSeq = newOTrCells.item(1);
		var newOTdChkSeq = document.createElement("td");
		var newOTdTxtSeq = document.createElement("td");
		newOTdChkSeq.innerHTML = '<%=strChkSeq%> value="' + changeIndex + '"/>';
		newOTdTxtSeq.innerHTML = '<%=strTxtSeq%> value="' + changeIndex + '"/>';
		newOTr.replaceChild(newOTdChkSeq, oldOTdChkSeq);
		newOTr.replaceChild(newOTdTxtSeq, oldOTdTxtSeq);
	}
	//remove selected item
	var oTr = oBodyRows.item(selectedIndex);
	oBody.removeChild(oTr);
}

function upDevice() {
	var selectChk = document.all("selectedSeq");
	var selectedIndex = -1;
	//get the checked item
	for(i=0;i<selectChk.length;i++) {
		if(selectChk[i].checked) {
			selectedIndex = i;
			break;
		}
	}
	//no item selected
	if (selectedIndex<=0)
		return;
	//alert(selectedIndex);
	replace(selectedIndex, -1);
}

function downDevice() {
	var selectChk = document.all("selectedSeq");
	var selectedIndex = -1;
	//get the checked item
	for(i=0;i<selectChk.length;i++) {
		if(selectChk[i].checked) {
			selectedIndex = i;
			break;
		}
	}
	//no item selected
	if (selectedIndex<0 || selectedIndex+1==selectChk.length)
		return;
	replace(selectedIndex, 1);
}

function replace(selectedIndex, offset) {

	var selectChk = document.all("selectedSeq");
	for(i=0;i<selectChk.length;i++) {
		if(i == selectedIndex+offset) {
			selectChk[i].checked = true;
			break;
		}
	}
	
	var oBody = document.all("deviceDataFormatItems");
	var oBodyRows = oBody.getElementsByTagName("tr");
	var iCount = oBodyRows.length;
	var selectedOTr = oBodyRows.item(selectedIndex);
	var oldOTr = oBodyRows.item(selectedIndex + offset);
	
	// swap rows data
	if(offset == -1) {
		oBody.insertBefore(selectedOTr, oldOTr);
	} else {
		oBody.insertBefore(oldOTr, selectedOTr);
	}
	
	var oldInputs = oldOTr.getElementsByTagName("input");
	var selInputs = selectedOTr.getElementsByTagName("input");
	// change selection
	selInputs.item(0).checked = true;
	var tmp = oldInputs.item(1).value;
	oldInputs.item(1).value = selInputs.item(1).value;
	selInputs.item(1).value = tmp;
}

function cloneDeviceDataFormat() {
	var panel = document.getElementById('deviceControlPanel');
	if (panel.style.visibility == 'hidden') {
		panel.style.visibility = 'visible';
		document.all("clone").value = 'clone';
	}else{
		panel.style.visibility = 'hidden';
		document.all("clone").value = '';
	}
}

function validate() {
	var txtClientID = document.getElementById("clientID");
	var txtNewClientID = document.getElementById("newClientID");
	var panel = document.getElementById("deviceControlPanel");
	if (txtClientID.value == "") {
		alert("Please enter Client ID.");
		txtClientID.focus();
		return false;
	}
	if (panel.style.visibility == "visible" && txtNewClientID.value == "") {
		alert("Please enter New Client ID.");
		txtNewClientID.focus();
		return false;
	}
}
function doRemoveDeviceDataFormat() {
	var txtClientID = document.getElementById("clientID");
	if (txtClientID.value == "") {
		return false;
	}else{
	   result =  window.prompt("To confirm the deletion of this, enter the client ID below.","");
	   if (result == txtClientID.value)
	   {
	      document.getElementById("deleteConfirmed").value = "true";
	      return true;
	   }
	   else
	   {
	       alert("Device Data Format deletion not confirmed properly");
	   }
	   return false;
	}
}

function loadAttributes(obj) {
		obj.form.submit();
}

function loadPP(){
	var array =  [<% if(clientsList != null && clientsList.size() > 0 ){ for(String client : clientsList) { %>"<%= client.trim() %>",<% } } %>];
	var input = document.getElementById("clientID");
	input.setAttribute( "autocomplete", "off" );
	autocomplete(input, array);
}

function autocomplete(inp, arr) 
{  
	  var currentFocus;

	  inp.addEventListener("input", function(e)	{
		  var a, b, i, tot = 0, val = this.value;

		  closeAllLists();
		  if (!val) { 		 
		  	showAllList(this);
		  	return false;
		  }
		  currentFocus = -1;

		  a = document.createElement("DIV");
		  a.setAttribute("id", this.id + "autocomplete-list");
		  a.setAttribute("class", "autocomplete-items");
		  
		  this.parentNode.appendChild(a);

		  for (i = 0; i < arr.length; i++){			
			if( arr[i].toUpperCase().indexOf(val.toUpperCase()) !== -1 ){
			  tot = tot+1;
			  b = document.createElement("DIV");
			  b.innerHTML =  addMatchedWords(arr[i], val);
			  b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";			
			  addClickListenerToElement(b);	
			  a.appendChild(b);
			}
		  }
		  if(tot>9) a.setAttribute("class", "scroll"); 		  
	  });
	  
	  inp.addEventListener("focus", function(e)	  {
		 closeAllLists();
		 if (!this.value) 	showAllList(this);
		 return false;
	  });
	  
	  inp.addEventListener("keydown", function(e) {
		 var x = document.getElementById(this.id + "autocomplete-list");
		 var y = x;
		 if (x) x = x.getElementsByTagName("div");
		 if (e.keyCode == 40) {
		 	if(y){
				currentFocus++;
				addActive(x);
			}
			else showAllList(this);			
		 }
		 else if (e.keyCode == 38){ 
			currentFocus--;
			addActive(x);
		 }
		 else if (e.keyCode == 13){
			e.preventDefault();
			if (currentFocus > -1) 
				if (x) x[currentFocus].click();
		 }
		 else if (e.keyCode == 27) closeAllLists();
		 
	  });
	  
	  function addMatchedWords(word, text){
	  	var iniPos =  word.toUpperCase().indexOf(text.toUpperCase());		
		if (iniPos!==-1)
		word = word.replace(word.substr(iniPos, text.length),"<strong>" + word.substr(iniPos, text.length)+ "</strong>");
		return word;
	  }
	  
	  function addActive(x){
		if (!x) return false;
		removeActive(x);
		if (!currentFocus) currentFocus = 0;
		if (currentFocus >= x.length) currentFocus = 0;
		if (currentFocus < 0) currentFocus = (x.length - 1);		
		x[currentFocus].classList.add("autocomplete-active");
		scroll(document.getElementById("clientIDautocomplete-list"),x[currentFocus]);
	  }
	  
	  function removeActive(x) {    
		for (var i = 0; i < x.length; i++) {
		  x[i].classList.remove("autocomplete-active");
		}
	  }
	  
	  function closeAllLists(elmnt){
		var x = document.getElementsByClassName("autocomplete-items");
		if(!x.length>0) x = document.getElementsByClassName("scroll");
		for (var i = 0; i < x.length; i++) 
		{
		  if (elmnt != x[i] && elmnt != inp)  x[i].parentNode.removeChild(x[i]);		  
		}
	  }
	  
	  function showAllList(obj){
		  var a, b;
		  closeAllLists();
		  a = document.createElement("DIV");		
		  a.setAttribute("id", obj.id + "autocomplete-list");
		  a.setAttribute("class", "scroll");
		  obj.parentNode.appendChild(a);
		  for (i = 0; i < arr.length; i++) {
			  b = document.createElement("DIV");			
			  b.innerHTML += arr[i] + "<input type='hidden' value='" + arr[i] + "'>";			
			  addClickListenerToElement(b);			  
			  a.appendChild(b);			
		  }	  
	  }
	  
	  function addClickListenerToElement(elmnt) {
		  elmnt.addEventListener("click", function(e) {		
			  inp.value = (this.getElementsByTagName("input")[0].value).trim();			
			  closeAllLists();
			  document.getElementsByTagName("form")[0].submit();
		  });	  	
	  }
	  
	  function scroll(wrapper, elmnt) {
  		if( (elmnt.offsetTop > wrapper.offsetHeight-25) || elmnt.offsetTop === 0)
  			wrapper.scrollTop = elmnt.offsetTop; 	  	
  		else wrapper.scrollTop = elmnt.offsetTop - wrapper.offsetHeight; 
	  }
	  
	  document.addEventListener("click", function (e) {
		  closeAllLists(e.target);
	  });
}



</script>
</HEAD>

<BODY class="settingspage" onload="return loadPP()">
<html:form action="/deviceDataFormatSettings">
<input type="hidden" name="clone"/>
<html:hidden property="deleteConfirmed" styleId="deleteConfirmed"/>
<bean:define id="strEditor" property="editor" name="deviceDataFormatForm"/>
<%
Boolean isEditor = (Boolean)pageContext.getAttribute("strEditor");
boolean editorFlag = !isEditor.booleanValue();

String showWarningColor = (String)session.getAttribute("DISPLAY_PRODUCTION_WARNING_COLOR");

//boolean existingFlag = false;
//String readonlyclass = "";
%>
	<TABLE border="0">
		<TBODY >
			<TR>
				<TH align="left" class="settingstext" style="<%=showWarningColor%>">Device Data Format Config</TH>
			</TR>
			<TR>
				<TD>
				<TABLE border="0" cellpadding="0" cellspacing="0">
					<TBODY>
						<TR>
							<TH align="right" class="settingstext" width="150">ClientID *</TH>
							<TD class="autocomplete"><html:text property="clientID" name="deviceDataFormatForm" styleId="clientID" style="width: 200"/></TD>
							<TD><html:submit property="operation" value="Find"  style="width: 100"/></TD>
							<TD><html:submit property="operation" value="Remove" disabled="<%=editorFlag%>" onclick="return doRemoveDeviceDataFormat()"  style="width: 100"/></TD>
							<TD><input type="button" value="Clone" onclick="cloneDeviceDataFormat()" style="width: 100"></TD>
						</TR>
						<TR id="deviceControlPanel" style="visibility: hidden;">
							<TH align="right" class="settingstext">New ClientID *</TH>
							<TD><input type="text" name="newClientID" id="newClientID" style="width: 200"></TD>
							<TD>&nbsp;</TD>
							<TD>&nbsp;</TD>
							<TD>&nbsp;</TD>
						</TR>
					</TBODY>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD>&nbsp;</TD>
			</TR>
			<TR>
				<TD>
				<TABLE border="0"  height="350" width="900">
					<TBODY>
						<TR>
							<TD valign="top" align="left">
							<!-- Device Data Format Information -->
							<DIV style="height: 350; width: 900; overflow:auto;">
							<TABLE id="deviceTable" border="1" cellpadding="0" cellspacing="0" width="100%">
								<THEAD>
									<TR>
										<TH align="left" width="20">&nbsp;</TH>
										<TH align="left">Seq</TH>
										<TH align="left">DC Tag</TH>
										<TH align="left">Tag Type</TH>
										<TH align="left">Tag Value</TH>
										<TH align="left">Offset</TH>
										<TH align="left">Length</TH>
										<TH align="left">Data Type</TH>
										<TH align="left">Tag Name</TH>
										
									</TR>
								</THEAD>
								<TBODY id="deviceDataFormatItems">
								<logic:notEmpty property="data" name="deviceDataFormatForm">
								<logic:iterate id="item" property="data" name="deviceDataFormatForm">
									<TR>
										<bean:define id="itemSeq" name="item" property="sequenceNumber"/>
										<%
										String itemStrSeq = ((Integer)pageContext.getAttribute("itemSeq")).toString();
										%>
										<TD align="left"><input type="radio" name="selectedSeq" value="<%=itemStrSeq %>"></TD>
										<TD align="left"><html:text property="sequenceNumber" name="item" style="width: 30" readonly="true"/></TD>
										<TD align="left"><html:text property="tag" name="item" size="40"/></TD>
										<TD align="left">
											<bean:define id="tagTypeVOs" property="deviceTagTypes" type="java.util.LinkedHashMap" name="deviceDataFormatForm" />
											<html:select styleId="attributeType" property="tagType" name="item" style="width: 130" onchange="loadAttributes(this);">
												<html:options collection="tagTypeVOs" property="key" labelProperty="value"/>
											</html:select>
										</TD>
										<TD align="left">
											<c:choose>
												<c:when test="${not empty item.attributeMethods}">
													<bean:define id="attributeMethods" property="attributeMethods" type="java.util.TreeMap" name="item" />
													<html:select styleId="attributeType" property="tagValue" name="item" style="width: 220">
														<html:options collection="attributeMethods" property="key" labelProperty="value"/>
													</html:select>
												</c:when>
												<c:otherwise>
													<html:text property="tagValue" name="item" style="width: 220"/>
												</c:otherwise>
											</c:choose>
										</TD>
										<TD align="left"><html:text property="offset" name="item" style="width: 50" /></TD>
										<TD align="left"><html:text property="length" name="item" style="width: 50" /></TD>
										<TD align="left">
										<bean:define id="dataTypeVOs" property="dataTypes" type="java.util.HashMap" name="deviceDataFormatForm" />
											<html:select property="dataType" name="item" style="width: 100">
												<html:options collection="dataTypeVOs" property="key" labelProperty="value"/>
											</html:select>
										</TD>
										<TD><html:text property="tagName" name="item" style="width: 220" /></TD>
									</TR>
								</logic:iterate>
								</logic:notEmpty>
								</TBODY>
							</TABLE>
							</DIV>
							</TD>

							<TD valign="top" align="left">
							<!-- operation buttons -->
							<TABLE border="0">
								<TBODY>
									<TR>
										<TD><input type="button" value="Add" style="width: 100" onclick="addDevice()"></TD>
									</TR>
									<TR>
										<TD><input type="button" value="Delete" style="width: 100" onclick="removeDevice()"></TD>
									</TR>
									<TR>
										<TD><input type="button" value="Up" style="width: 100" onclick="upDevice()"></TD>
									</TR>
									<TR>
										<TD><input type="button" value="Down" style="width: 100" onclick="downDevice()"></TD>
									</TR>
								</TBODY>
							</TABLE>
							</TD>
						</TR>
					</TBODY>
				</TABLE>
				</TD>
			</TR>
			<TR>
			   <TD colspan="2">&nbsp;
				<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
				   <bean:write name="msg"/>
				</html:messages>
				<html:errors/>
			   </TD>
			</TR>
			<TR>
				<TD align="center">
				<html:submit property="operation" value="Apply" disabled="<%=editorFlag%>"  style="width: 100" onclick="return validate()"/>&nbsp;
				<html:submit property="operation"  style="width: 100" value="Cancel"/>
				</TD>
			</TR>
		</TBODY>
	</TABLE>
</html:form>
</BODY>
</html:html>
