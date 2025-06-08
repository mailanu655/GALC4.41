
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
<TITLE>Print Attribute Format Configuration</TITLE>
<script language="javascript">
<%com.honda.galc.system.config.web.forms.PrintAttributeFormatForm printForm = (com.honda.galc.system.config.web.forms.PrintAttributeFormatForm)request.getAttribute("printAttributeFormatForm");
java.util.Map attributeTypes = printForm.getAttributeTypes();
java.util.Map requiredTypes = printForm.getRequiredTypes();

//need assign sequence value to them
String strChkSeq = "<input type=\"radio\" name=\"selectedSeq\" ";
String strTxtSeq = "<input type=\"text\" name=\"sequenceNumber\" readonly=\"readonly\" style=\"width: 24\" ";

StringBuffer sb = new StringBuffer();
sb.append("<select name=\"attributeTypeId\" style=\"width: 130\">");
//set the options
java.util.Set keys = attributeTypes.keySet();
java.util.Iterator it = keys.iterator();
while(it.hasNext()) {
	String key = (String)it.next();
	sb.append("<option value=" + key + ">" + attributeTypes.get(key).toString() + "</option>");
}
sb.append("</select>");
String strComAttributeType = sb.toString();

String strAttribute = "<input type=\"text\" name=\"attribute\" style=\"width: 170\">";
String strAttributeValue = "<input type=\"text\" name=\"attributeValue\" style=\"width: 220\">";
String strOffset = "<input type=\"text\" name=\"offset\" style=\"width: 50\">";
String strLength = "<input type=\"text\" name=\"length\" style=\"width: 50\">";


 sb = new StringBuffer();
sb.append("<select name=\"requiredTypeId\" style=\"width: 130\">");
 keys = requiredTypes.keySet();
 it = keys.iterator();
while(it.hasNext()) {
	String key = (String)it.next();
	sb.append("<option value=" + key + ">" + requiredTypes.get(key).toString() + "</option>");
}
sb.append("</select>");
String strRequiredType = sb.toString();

%>

function addPrintAttribute() {
	var oBody = document.getElementById("printAttributeFormatItems");
	var oBodyChildren = oBody.getElementsByTagName("tr");
	var iCount = 0;
	if(oBodyChildren.length > 0) {
		var oLastChild = oBodyChildren[oBodyChildren.length - 1];
		var iCount = parseInt(oLastChild.getElementsByTagName("input")[1].value);
	}
	var iRecordID = iCount + 1;
	

	var oTr = document.createElement("tr");
	//create TD objects
	var oTdChkSeq = document.createElement("td");
	var oTdTxtSeq = document.createElement("td");
	var oTdComAttributeType = document.createElement("td");
	var oTdAttribute = document.createElement("td");
	var oTdAttributeValue = document.createElement("td");
	var oTdOffset = document.createElement("td");
	var oTdLength = document.createElement("td");
	var oTdRequiredType = document.createElement("td");
	
	//intial TD object inner html
	oTdChkSeq.innerHTML = '<%=strChkSeq%> value="' + iRecordID + '"/>';
	oTdTxtSeq.innerHTML = '<%=strTxtSeq%> value="' + iRecordID + '"/>';
	oTdComAttributeType.innerHTML = '<%=strComAttributeType%>';
	oTdAttribute.innerHTML = '<%=strAttribute%>';
	oTdAttributeValue.innerHTML = '<%=strAttributeValue%>';
	oTdOffset.innerHTML = '<%=strOffset%>';
	oTdLength.innerHTML = '<%=strLength%>';
	oTdRequiredType.innerHTML = '<%=strRequiredType%>';
	
	//attach TD objects to TR object
	oTr.appendChild(oTdChkSeq);
	oTr.appendChild(oTdTxtSeq);
	oTr.appendChild(oTdAttribute);	oTr.appendChild(oTdComAttributeType);
	oTr.appendChild(oTdAttributeValue);
	oTr.appendChild(oTdOffset);
	oTr.appendChild(oTdLength);
	oTr.appendChild(oTdRequiredType);
	//attach TR object to Table
	oBody.appendChild(oTr);
}

function removePrintAttribute() {
	var oBody = document.all("printAttributeFormatItems");
	var oBodyChildren = oBody.getElementsByTagName("tr");
	var iCount = oBodyChildren.length;
	
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
		var newOTr = oBodyChildren.item(changeIndex);
		var newOTrChildren = newOTr.getElementsByTagName("td");
		var oldOTdChkSeq = newOTrChildren.item(0);
		var oldOTdTxtSeq = newOTrChildren.item(1);
		var newOTdChkSeq = document.createElement("td");
		var newOTdTxtSeq = document.createElement("td");
		newOTdChkSeq.innerHTML = '<%=strChkSeq%> value="' + changeIndex + '"/>';
		newOTdTxtSeq.innerHTML = '<%=strTxtSeq%> value="' + changeIndex + '"/>';
		newOTr.replaceChild(newOTdChkSeq, oldOTdChkSeq);
		newOTr.replaceChild(newOTdTxtSeq, oldOTdTxtSeq);
	}
	//remove selected item
	var oTr = oBodyChildren.item(selectedIndex);
	oBody.removeChild(oTr);
}

function upPrintAttribute() {
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

function downPrintAttribute() {
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
	
	var oBody = document.getElementById("printAttributeFormatItems");
	var oBodyChildren = oBody.getElementsByTagName("tr");
	var iCount = oBodyChildren.length;
	var selectedOTr = oBodyChildren.item(selectedIndex);
	var oldOTr = oBodyChildren.item(selectedIndex + offset);
	
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

function clonePrintDataFormat() {
	var panel = document.getElementById('printControlPanel');
	if (panel.style.visibility == "hidden") {
		panel.style.visibility = "visible";
		document.all("clone").value = 'clone';
	}else{
		panel.style.visibility = "hidden";
		document.all("clone").value = '';
	}
}

function validate() {
	var txtFormID = document.getElementById("formID");
	var txtNewFormID = document.getElementById("newFormID");
	var panel = document.getElementById('printControlPanel');
	if (txtFormID.value == "") {
		alert("Form ID is required.");
		txtFormID.focus();
		return false;
	}
	if (panel.style.display == 'block' && txtNewFormID.value == "") {
		alert("New Form ID is required.");
		txtNewFormID.focus();
		return false;
	}
}
function doRemovePrintAttributeFormat() {
	var txtFormID = document.getElementById("formID");
	if (txtFormID.value == "") {
		alert("Form ID is required.");
		return true;
	}else{
	   result =  window.prompt("To confirm the deletion of this, enter the form ID below.","");
	   if (result == txtFormID.value)
	   {
	      document.getElementById("deleteConfirmed").value = "true";
	      return true;
	   }
	   else
	   {
	       alert("Print Attribute Format deletion not confirmed properly");
	   }
	   return false;
	}
}

function loadAttributes(obj) {
		obj.form.submit();
}
</script>
</HEAD>

<BODY class="settingspage">
<html:form action="/printAttributeFormatSettings">
<input type="hidden" name="clone" id="clone"/>
<html:hidden property="deleteConfirmed" styleId="deleteConfirmed"/>
<bean:define id="strEditor" property="editor" name="printAttributeFormatForm"/>
<%
Boolean isEditor = (Boolean)pageContext.getAttribute("strEditor");
boolean editorFlag = !isEditor.booleanValue();
String showWarningColor = (String)session.getAttribute("DISPLAY_PRODUCTION_WARNING_COLOR");
%>
	<TABLE border="0">
		<TBODY>
			<TR>
				<TH align="left" class="settingstext" style="<%=showWarningColor%>">Print Attribute Format Config</TH>
			</TR>
			<TR>
				<TD>
				<TABLE border="0" cellpadding="0" cellspacing="0">
					<TBODY>
						<tr><td>&nbsp;</td><TD><html:submit property="operation" value="FindAll"  style="width: 100"/></TD></tr>
						<TR>
							<TH align="right" class="settingstext" width="150">FormID *</TH>
							<TD><c:choose>
							<c:when test="${not empty printAttributeFormatForm.forms}">
								<c:set var="forms" value="${printAttributeFormatForm.forms}"/>
								<html:select styleId="formID" property="formID" name="printAttributeFormatForm" onchange="loadAttributes(this);">
								       <html:options collection="forms" property="value" labelProperty="label"/>
							    </html:select>
							</c:when>
							<c:otherwise>
								<html:text styleId="formID" property="formID" name="printAttributeFormatForm" style="width: 200"/>
							</c:otherwise>
							</c:choose></TD>
							<TD><html:submit property="operation" value="Load"  style="width: 100"/></TD>
							<TD><html:submit property="operation" value="Remove" disabled="<%=editorFlag%>" onclick="return doRemovePrintAttributeFormat()"  style="width: 100"/></TD>
							<TD><input type="button" value="Clone" onclick="clonePrintDataFormat()" style="width: 100"></TD>
						</TR>
						<TR id="printControlPanel" style="visibility: hidden;">
							<TH align="right" class="settingstext">New FormID *</TH>
							<TD><input type="text" name="newFormID" style="width: 200"></TD>
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
				<TABLE border="0" height="420" width="1000">
					<TBODY>
						<TR>
							<TD valign="top" align="left">
							<!-- Print Attribute Format Information -->
							<DIV style="width: 835px; height: 400px; overflow: auto;">
							<TABLE id="deviceTable" border="1" cellpadding="0" cellspacing="0" width="100%">
								<THEAD>
									<TR>
										<TH align="left" width="6">&nbsp;</TH>
										<TH align="left">Seq</TH>
										<TH align="left">Attribute</TH>
										<TH align="left">Attribute Type</TH>
										<TH align="left">Attribute Value</TH>
										<TH align="left">Offset</TH>
										<TH align="left">Length</TH>
										<TH align="left">Required</TH>
									</TR>
								</THEAD>
								<TBODY id="printAttributeFormatItems">
								<logic:notEmpty property="data" name="printAttributeFormatForm">
								<logic:iterate id="item" property="data" name="printAttributeFormatForm">
									<TR>
										<TD align="left"><input type="radio" name="selectedSeq" value="<c:out value='${item.sequenceNumber}'/>"></TD>
										<TD align="left"><html:text property="sequenceNumber" name="item" style="width: 24" readonly="true"/></TD>
										<TD align="left"><html:text property="attribute" name="item" style="width: 170"/></TD>
										<TD align="left">
										<bean:define id="attributeVOs" property="attributeTypes" type="java.util.TreeMap" name="printAttributeFormatForm" />
											<html:select styleId="attributeType" property="attributeTypeId" name="item" style="width: 130" onchange="loadAttributes(this);">
												<html:options collection="attributeVOs" property="key" labelProperty="value"/>
											</html:select>
										</TD>
										<TD align="left">
											<c:choose>
												<c:when test="${not empty item.attributeMethods}">
													<bean:define id="attributeMethods" property="attributeMethods" type="java.util.TreeMap" name="item" />
													<html:select styleId="attributeType" property="attributeValue" name="item" style="width: 220">
														<html:options collection="attributeMethods" property="key" labelProperty="value"/>
													</html:select>
												</c:when>
												<c:otherwise>
													<html:text property="attributeValue" name="item" style="width: 220"/>
												</c:otherwise>
											</c:choose>
										</TD>
										<TD align="left"><html:text property="offset" name="item" style="width: 50" /></TD>
										<TD align="left"><html:text property="length" name="item" style="width: 50" />
										
										<TD align="left">
										<bean:define id="requiredVOs" property="requiredTypes" type="java.util.TreeMap" name="printAttributeFormatForm" />
											<html:select styleId="requiredType" property="requiredTypeId" name="item" style="width: 130" onchange="loadAttributes(this);">
												<html:options collection="requiredVOs" property="key" labelProperty="value"/>
											</html:select>
										</TD>

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
										<TD><input type="button" value="Add" style="width: 100" onclick="addPrintAttribute()"></TD>
									</TR>
									<TR>
										<TD><input type="button" value="Delete" style="width: 100" onclick="removePrintAttribute()"></TD>
									</TR>
									<TR>
										<TD><input type="button" value="Up" style="width: 100" onclick="upPrintAttribute()"></TD>
									</TR>
									<TR>
										<TD><input type="button" value="Down" style="width: 100" onclick="downPrintAttribute()"></TD>
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
				<TD align="center">
				<html:submit property="operation" value="Apply" disabled="<%=editorFlag%>" onclick="return validate()"/>&nbsp;
				<html:submit property="operation" value="Cancel" />
				</TD>
			</TR>
		</TBODY>
	</TABLE>
</html:form>
<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
   <bean:write name="msg"/>
</html:messages>
<html:errors/>
</BODY>
</html:html>
