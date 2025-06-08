
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@page import="com.honda.galc.system.config.web.forms.PropertySettingsForm"%>
<%@ page import="com.honda.galc.entity.conf.*" %>
<%@ page import="com.honda.galc.dto.ProcessPointGroupDto" %>
<%@ page import="com.honda.galc.entity.enumtype.ProcessPointType" %>
<%@ page import="java.util.*" %>
<%@ page import="java.lang.*" %>
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
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE></TITLE>
<script type="text/javascript" src="common/common.js"></script>
<script>

function changedFeaturedType(selectedFeatureType) {
 
 var selectedFeatureType = selectedFeatureType.value;
 
  if (document.getElementById('featureId').options.length != 0) 
 { 
  document.getElementById('featureId').options.length = 0;
 }
 
  var url= "/ConfigService/populateFeatureIdAjax.do?function=populateFeatureId&featureType="+selectedFeatureType;
 
 	try {
		    	// Firefox, Opera 8.0+, Safari 
		        xmlHttp=new XMLHttpRequest(); 
		  	} catch(e) {
		     // Internet Explorer 6.0+
				try { 
					xmlHttp=new ActiveXObject("Msxml2.XMLHTTP"); 
				} catch (e) {
					try {  
					      xmlHttp=new ActiveXObject("Microsoft.XMLHTTP"); 
					} catch (e) {  
					      alert("Your browser does not support AJAX!"); 
				      return false; 
					}
				}
			}
 
			xmlHttp.onreadystatechange=function() {
				if(xmlHttp.readyState==4)	{
			      	var ser = xmlHttp.responseText;
		      	 	var select = document.getElementById('featureId');
			      	var value = xmlHttp.responseText.split("|||");
			      	
			      	for(counter=1;counter<value.length-1;counter++) {
					
	 					var v1 = value[counter];
	 					//alert(v1);
	 					
	 					var opt = document.createElement('option');
	 					opt.value = v1;
    				    opt.innerHTML = v1;
    					select.appendChild(opt); 
	 					 
	 					
			      	}
			      	
				} else{
					//alert("Ajax request does not succeed");
				}
			}
			xmlHttp.open("POST", url, true);
			xmlHttp.send(null);
}

function confirmTrackingChange() {
  
   var currentPoint = document.getElementById("ppForm").currentLineEntryPoint.value;
   
   if (currentPoint != "")
   {
       if (document.getElementById("trackingCB").checked == true)
       {
           alert("Selecting tracking will change the Lines entry point. Only one tracking point per line is allowed.");
       }
   }
}

function displayMessage(colid, messageText) {
    
    var msgcol = document.getElementById(colid);
    
    if (msgcol != null)
    {
      msgcol.innerHTML=messageText;
    }
}

function confirmAppply()
{
var selectedFeatureType = document.getElementById('featureType').value;
var selectedFeatureId = document.getElementById('featureId').value;
	if((selectedFeatureType != "NONE") && (selectedFeatureId == "NONE"))
	{
	 alert("Please select a Feature Id");
	 return false;
	}

	return true;
}

function confirmDelete()
{
   
   
   ppID = document.getElementById("ppForm").processPointID.value;
   
   
   result =  window.prompt("To confirm the deletion of this process point, enter the Process Point ID below.","");
   
   if (result == ppID)
   {
      document.getElementById("ppForm").deleteConfirmed.value = "true";
      
      return true;
   }
   else
   {
       alert("Process point deletion not confirmed properly");
   }
   
   return false;
}

function confirmDeleteApplication()
{
   
   
   appID = document.getElementById("appForm").applicationID.value;
   
   result = confirm("Are you sure you want to delete the application "+appID);
   
   if (result)
   {
      document.getElementById("appForm").deleteApplicationConfirmed.value = "true";
      
      return true;
   }
   
   return false;
}


		function showProcessPoint() 
		{
		   
		   normalText('ApplicationTab');
		   normalText('BroadcastTab');
		   boldText('ProcessPointTab');
		   hide('ApplicationSettings');
		   hide('BroadcastSettings');
		   show('ProcessPointSettings');
		}

		function showApplication() {
		   normalText('BroadcastTab');
		   normalText('ProcessPointTab');
		   boldText('ApplicationTab');
		   hide('BroadcastSettings');
		   hide('ProcessPointSettings');
 		   show('ApplicationSettings');
		}

		function showBroadcast() {
		   normalText('ApplicationTab');
		   normalText('ProcessPointTab');
		   boldText('BroadcastTab');
		   hide('ApplicationSettings');
		   hide('ProcessPointSettings');
		   show('BroadcastSettings');
		}
		
		
		var taskPopupWindow = null;
		var taskSpecPopupWindow = null;
		
	    function launchApplicationTaskWindow(operation)
	    {
	        
	        
	        applicationID = document.getElementById("appSettingsAppliationID").value;

			sequenceNo = 0;
	        if (operation == "add")
	        {
	           // determine the number of rows in the table  
	           // using the radio buttons
	           taskseq = document.getElementById("appForm").tasksequence;
	           if (taskseq == null)
	           {
	              rowcount = 0;
	           }
	           else
	           {
	              rowcount = taskseq.length;
	           }
	           
	           
	                     
	           sequenceNo = rowcount;
	           
	            popupurl = "applicationTaskSettings.do?applicationID="+applicationID+"&operation="+operation+"&sequenceNo="+sequenceNo;
	           
	        }
	        else
	        {
	            rblist = document.getElementById("appForm").tasksequence;
	        	rowcount = rblist.length;
	        	
	        	for (i=0; i < rowcount; i++)
	        	{
	        	   
	        	   if (rblist[i].checked == true)
	        	   {
	        	      sequenceNo = i;
	        	      break;
	        	   }
	        	}
	        	
	        	fieldIndex = sequenceNo+1;
	        	
	        	taskName = document.getElementsByName("taskName"+fieldIndex)[0].value;
	        	beginFlag = document.getElementsByName("beginFlag"+fieldIndex)[0].value;
	        	commitFlag = document.getElementsByName("commitFlag"+fieldIndex)[0].value;
	        	inputFlag = document.getElementsByName("inputFlag"+fieldIndex)[0].value;
	        	taskArgs=document.getElementsByName("taskArgs"+fieldIndex)[0].value;
	        	
	        	 popupurl = "applicationTaskSettings.do?applicationID="+applicationID+"&operation="+operation+"&sequenceNo="+sequenceNo+
	        	            "&taskName="+taskName+"&beginFlag="+beginFlag+"&commitFlag="+commitFlag+"&inputEventFlag="+inputFlag+
	        	            "&taskArg="+taskArgs;
	        
	        }	        

	        
	        taskPopupWindow = window.open(popupurl,"apptaskwin","width=600,height=400,status=yes,resizable=yes",true);
	        taskPopupWindow.focus();
	        

	        
	        return false;
	    }
	    
	    function launchTaskSpecWindow()
	    {
	       taskSpecPopupWindow = window.open("taskSpecificationSettings.do","taskspecwin","width=600,height=300,status=yes,resizable=yes",true);
	       taskSpecPopupWindow.focus();
	       
	       return false;
	    }
	    
	    function closeTaskSpecWindow()
	    {
	       taskSpecPopupWindow.window.close();
	       taskSpecPopupWindow = null;
	    }
	    
	    // This method is called from the Application Task window
	    function updateTaskList(operation, sequenceNo, inputFlag, taskName, beginFlag,commitFlag, taskArgs, jndiName, statefulFlag)
	    {
	        taskPopupWindow.window.close();
	        taskPopupWindow = null;
	        
	    	beginCommit = "&nbsp;";
	    	taskArgsDisplay = "&nbsp;";
	    	
	    	inputChecked = "";
	    	
	    	if (inputFlag == "true")
	    	{
	    	   inputChecked = "checked";
	    	}
	    	
	    	
	    	
	    	if (beginFlag == "true" && commitFlag == "true")
	    	{
	    	   beginCommit = "Begin/Commit";
	    	}
	    	else if (beginFlag == "true")
	    	{
	    	   beginCommit = "Begin";
	    	}
	    	else if (commitFlag == "true")
	    	{
	    	   beginCommit = "Commit";
	    	}
	    	
	    	if (taskArgs == null)
	    	{
	    	   taskArgs = "";
	    	}
	    	
	    	if (taskArgs != "")
	    	{
	    	   taskArgsDisplay = taskArgs;
	    	}
	    	
	        if (operation == "add")
	        {
	           // Make sure the sequence Number is correct
	           taskseq = document.getElementsByName("tasksequence");
	           if (taskseq == null)
	           {
	              rowcount = 0;
	           }
	           else
	           {
	              rowcount = taskseq.length;
	           }
	           
	           // Add one to the row count to get new sequence number.
	           sequenceNo = rowcount + 1;
	           
	           
	           
	           // Build out the new table entries
	           var tablebody = document.getElementById("tasklisttablebody");
	           
	           newrow = document.createElement("tr");
	           newrow.id = "trtask"+sequenceNo;
      
      
      		   // Create sequence radio button
      		   newcol = document.createElement("td");
      		   newcol.id = "tdtaskrb"+sequenceNo;
      		   newcol.innerHTML="<INPUT type=\"radio\" name=\"tasksequence\" value=\""+sequenceNo+"\" >"+sequenceNo;
      		   newrow.appendChild(newcol);
      
      		   // Input flag
      		   newcol = document.createElement("td");
      		   newcol.id = "tdtaskinput"+sequenceNo;
               hiddenfield ="<input type=\"hidden\" name=\"inputFlag"+sequenceNo+"\" value=\""+inputFlag+"\">";
               checkfield = "<INPUT type=\"checkbox\" name=\"inputbox"+sequenceNo+"\" "+ inputChecked+ " disabled>";
               newcol.innerHTML = checkfield+hiddenfield;
               newrow.appendChild(newcol);
               
               // task name
               newcol = document.createElement("td");
               newcol.id = "tdtaskname"+sequenceNo;
               hiddenfield="<input type=\"hidden\" name=\"taskName"+sequenceNo+"\" value=\""+taskName+"\">";
               hiddenfield2="<input type=\"hidden\" name=\"jndiName"+sequenceNo+"\" value=\""+jndiName+"\">";
               hiddenfield3="<input type=\"hidden\" name=\"statefulFlag"+sequenceNo+"\" value=\""+statefulFlag+"\">";
               newcol.innerHTML = taskName+hiddenfield+hiddenfield2+hiddenfield3;
               newrow.appendChild(newcol);
               
               // begin commit
               newcol = document.createElement("td");
               newcol.id = "tdtaskbegincommit"+sequenceNo;
               hiddenfield1 ="<input type=\"hidden\" name=\"beginFlag"+sequenceNo+"\" value=\""+beginFlag+"\">";
               hiddenfield2 ="<input type=\"hidden\" name=\"commitFlag"+sequenceNo+"\" value=\""+commitFlag+"\">";
               newcol.innerHTML = beginCommit+hiddenfield1+hiddenfield2;
               newrow.appendChild(newcol);
               
               // Argument
               newcol = document.createElement("td");
               newcol.id = "tdtaskargs"+sequenceNo;
               hiddenfield="<input type=\"hidden\" name=\"taskArgs"+sequenceNo+"\" value=\""+taskArgs+"\">";
               newcol.innerHTML = taskArgsDisplay+hiddenfield;
               newrow.appendChild(newcol);
               
               // Add row to table
               tablebody.appendChild(newrow);
            
               displayMessage("applicationmsgcol", "Changes are not saved until the Apply button is pressed.");	  
	        }
	        else if (operation == "modify")
	        {
	        
	           index = parseInt(sequenceNo)+1;
	           
	           
	           // Input flag
	           col = document.getElementById("tdtaskinput"+index);
	        	
      		   hiddenfield ="<input type=\"hidden\" name=\"inputFlag"+index+"\" value=\""+inputFlag+"\">";
               checkfield = "<INPUT type=\"checkbox\" name=\"inputbox"+index+"\" " +inputChecked + " disabled>";
               col.innerHTML = checkfield+hiddenfield;
               
               // task name
               col = document.getElementById("tdtaskname"+index);
               hiddenfield="<input type=\"hidden\" name=\"taskName"+index+"\" value=\""+taskName+"\">";
               hiddenfield2="<input type=\"hidden\" name=\"jndiName"+index+"\" value=\""+jndiName+"\">";
               hiddenfield3="<input type=\"hidden\" name=\"statefulFlag"+index+"\" value=\""+statefulFlag+"\">";               
               col.innerHTML = taskName+hiddenfield+hiddenfield2+hiddenfield3;
               
               
               // begin commit
               col = document.getElementById("tdtaskbegincommit"+index);
               hiddenfield1 ="<input type=\"hidden\" name=\"beginFlag"+index+"\" value=\""+beginFlag+"\">";
               hiddenfield2 ="<input type=\"hidden\" name=\"commitFlag"+index+"\" value=\""+commitFlag+"\">";
               col.innerHTML = beginCommit+hiddenfield1+hiddenfield2;
               
               // Argument
               col = document.getElementById("tdtaskargs"+index);
               hiddenfield="<input type=\"hidden\" name=\"taskArgs"+index+"\" value=\""+taskArgs+"\">";
               col.innerHTML = taskArgsDisplay+hiddenfield;
               
               displayMessage("applicationmsgcol", "Changes are not saved until the Apply button is pressed.");	  	        	
	        }
	        
	    }
	    
	    function processUp()
	    {
	            sequenceNo = 0;
	            rblist = document.getElementById("appForm").tasksequence;
	            if (rblist != null)
	            {
	        	  rowcount = rblist.length;
	        	
	        	  for (i=0; i < rowcount; i++)
	        	  {
	        	   
	        	   if (rblist[i].checked == true)
	        	   {
	        	      sequenceNo = i;
	        	      break;
	        	   }
	        	  }
	        	  
	        	  if (sequenceNo == 0)
	        	  {
	        	     // No way to go up
	        	     return;
	        	  }
	        	  
	        	  // field name indexes are 1 based
	        	  row1index = sequenceNo+1;
	        	  row2index = sequenceNo;
	        	  
	        	  switchRows(row1index, row2index);
	        	  
	        	  // change the selection state
	        	  rblist[sequenceNo].checked = false;
	        	  rblist[sequenceNo-1].checked = true;	        
	        	  
	        	  displayMessage("applicationmsgcol", "Changes are not saved until the Apply button is pressed.");	  
	        	}
	    
	    }
	    
	    function processDown() {
	            sequenceNo = 0;
	            rblist = document.getElementById("appForm").tasksequence;
	            if (rblist != null)
	            {
	        	  rowcount = rblist.length;
	        	
	        	  for (i=0; i < rowcount; i++)
	        	  {
	        	   
	        	   if (rblist[i].checked == true)
	        	   {
	        	      sequenceNo = i;
	        	      break;
	        	   }
	        	  }
	        	  
	        	  if (sequenceNo == (rowcount -1))
	        	  {
	        	     // No way to go down
	        	     return;
	        	  }
	        	  
	        	  // field name indexes are 1 based
	        	  row1index = sequenceNo+1;
	        	  row2index = sequenceNo+2;
	        	  
	        	  switchRows(row1index, row2index);
	        	  
	        	  // change the selection state
	        	  rblist[sequenceNo].checked = false;
	        	  rblist[sequenceNo+1].checked = true;
	        	  
	        	  displayMessage("applicationmsgcol", "Changes are not saved until the Apply button is pressed.");	  
	        	}
	    
	    }
	    
	    function processRemove()
	    {
	       tablebody = document.getElementById("tasklisttablebody");
           sequenceNo = 0;
	       rblist = document.getElementById("appForm").tasksequence;
	       if (rblist != null && rblist.length > 0)
	       {
	          rowcount = rblist.length;
	          
	          //alert(rblist.length);
	        	
	          for (i=0; i < rowcount; i++)
	          {
	        	   
	        	   if (rblist[i].checked == true)
	        	   {
	        	      sequenceNo = i;
	        	      break;
	        	   }
	        	
	          }
	          	  
	          if (sequenceNo < (rowcount - 1))
	          {
	        	     // Shift the data upwords moving the row to be deleted
	        	     // to the end.
	        	     for (j = sequenceNo+1; j < rowcount; j++)
	        	     {
	        	        switchRows(j,j+1);
	        	     }
	          }
	        	  
	          // Now remove last child of table
	          trow = document.getElementById("trtask"+rowcount);
	          if (trow != null)
	          {
	        	     tablebody.removeChild(trow);
	          }
	        	  
	          // Reset the selection
	          rblist = document.getElementById("appForm").tasksequence;
	          if (rblist != null)
	          {
	        	    rowcount = rblist.length;
	        	
	        	    for (i=0; i < rowcount; i++)
	        	    {
	        	       if (i == 0)
	        	       {
	        	          rblist[i].checked = true;
	        	       }
	        	       else
	        	       {
	        	          rblist[i].checked = false;
	        	       }
	        	    }
	           }
	        	  
	       	      
	          displayMessage("applicationmsgcol", "Changes are not saved until the Apply button is pressed.");	  
	       }
	       else
	       {
	          // Need to handle single row case where array length is undefined in some cases
	          // Now remove last child of table
	          trow = document.getElementById("trtask1");
	          if (trow != null)
	          {
	        	     tablebody.removeChild(trow);
	        	     displayMessage("applicationmsgcol", "Changes are not saved until the Apply button is pressed.");	  
	          }
	       }	    
	    }
	    
	    function switchRows(row1index, row2index)
	    {
	        	row1taskName = document.getElementsByName("taskName"+row1index)[0].value;
	        	row1beginFlag = document.getElementsByName("beginFlag"+row1index)[0].value;
	        	row1commitFlag = document.getElementsByName("commitFlag"+row1index)[0].value;
	        	row1inputFlag = document.getElementsByName("inputFlag"+row1index)[0].value;
	        	row1taskArgs=document.getElementsByName("taskArgs"+row1index)[0].value;
	        	row1jndiName = document.getElementsByName("jndiName"+row1index)[0].value;
	        	row1statefulFlag = document.getElementsByName("statefulFlag"+row1index)[0].value;

	        	row2taskName = document.getElementsByName("taskName"+row2index)[0].value;
	        	row2beginFlag = document.getElementsByName("beginFlag"+row2index)[0].value;
	        	row2commitFlag = document.getElementsByName("commitFlag"+row2index)[0].value;
	        	row2inputFlag = document.getElementsByName("inputFlag"+row2index)[0].value;
	        	row2taskArgs=document.getElementsByName("taskArgs"+row2index)[0].value;
	        	row2jndiName = document.getElementsByName("jndiName"+row2index)[0].value;
	        	row2statefulFlag = document.getElementsByName("statefulFlag"+row2index)[0].value;
	        	
	        	
	        	modifyTaskRow(row1index, row2inputFlag, row2taskName, row2beginFlag, row2commitFlag, row2taskArgs, row2jndiName, row2statefulFlag);
	        	modifyTaskRow(row2index, row1inputFlag, row1taskName, row1beginFlag, row1commitFlag, row1taskArgs, row1jndiName, row1statefulFlag);
	        	
	    
	    }
	    
	    function modifyTaskRow(index, inputFlag, taskName, beginFlag, commitFlag, taskArgs, jndiName, statefulFlag)
	    {
	    	beginCommit = "&nbsp;";
	    	taskArgsDisplay = "&nbsp;";
	    	
	    	inputChecked  = "";
	    	
	    	if (inputFlag == "true")
	    	{
	    	   inputChecked = "checked";
	    	}
	    	
	    	
	    	if (beginFlag == "true" && commitFlag == "true")
	    	{
	    	   beginCommit = "Begin/Commit";
	    	}
	    	else if (beginFlag == "true")
	    	{
	    	   beginCommit = "Begin";
	    	}
	    	else if (commitFlag == "true")
	    	{
	    	   beginCommit = "Commit";
	    	}
	    	
	    	if (taskArgs == null)
	    	{
	    	   taskArgs = "";
	    	}
	    	
	    	if (taskArgs != "")
	    	{
	    	   taskArgsDisplay = taskArgs;
	    	}

	           // Input flag
	           col = document.getElementById("tdtaskinput"+index);
	        	
      		   hiddenfield ="<input type=\"hidden\" name=\"inputFlag"+index+"\" value=\""+inputFlag+"\">";
               checkfield = "<INPUT type=\"checkbox\" name=\"inputbox"+index+"\" "+ inputChecked + " disabled>";
               col.innerHTML = checkfield+hiddenfield;
               
               // task name
               col = document.getElementById("tdtaskname"+index);
               hiddenfield="<input type=\"hidden\" name=\"taskName"+index+"\" value=\""+taskName+"\">";
               hiddenfield2="<input type=\"hidden\" name=\"jndiName"+index+"\" value=\""+jndiName+"\">";
               hiddenfield3="<input type=\"hidden\" name=\"statefulFlag"+index+"\" value=\""+statefulFlag+"\">";               
              
               col.innerHTML = taskName+hiddenfield+hiddenfield2+hiddenfield3;
               
               
               // begin commit
               col = document.getElementById("tdtaskbegincommit"+index);
               hiddenfield1 ="<input type=\"hidden\" name=\"beginFlag"+index+"\" value=\""+beginFlag+"\">";
               hiddenfield2 ="<input type=\"hidden\" name=\"commitFlag"+index+"\" value=\""+commitFlag+"\">";
               col.innerHTML = beginCommit+hiddenfield1+hiddenfield2;
               
               // Argument
               col = document.getElementById("tdtaskargs"+index);
               hiddenfield="<input type=\"hidden\" name=\"taskArgs"+index+"\" value=\""+taskArgs+"\">";
               col.innerHTML = taskArgsDisplay+hiddenfield;
	    
	    
	    }

</script>
<script type="text/javascript" src="broadcast/BroadcastDestinations.js"></script>

</HEAD>

<%
      com.honda.galc.system.config.web.forms.ProcessPointForm ppf = 
      (com.honda.galc.system.config.web.forms.ProcessPointForm)request.getAttribute("processPointForm");

	
	
 // Get the flag that determines if certain fields are read-only
 boolean existingFlag = true;
 {
 	String existingflagString = (String)request.getAttribute("existingflag");
 	if (existingflagString != null && existingflagString.equalsIgnoreCase("false"))
 	{
 		existingFlag = false;
 	}
 }
 boolean existingApplicationFlag = true;
 {
 	String existingapplicationflagString = (String)request.getAttribute("existingapplicationflag");
 	if (existingapplicationflagString != null && existingapplicationflagString.equalsIgnoreCase("false"))
 	{
 		existingApplicationFlag = false;
 	}
 }
 String styleClass = "readOnly";
 String activePage = null;
 
 boolean showACLLink = true;
 
 boolean showPropertiesLink = true;

 activePage = ppf.getActivePage();
 if (activePage == null || activePage.length() == 0)
 {
        activePage = "ProcessPoint";
        ppf.setActivePage("ProcessPoint");
 }
	 
 if (!existingFlag)
 {
    styleClass = null;
    activePage = "ProcessPoint";
    ppf.setActivePage("ProcessPoint");
    
    showACLLink = false;
    showPropertiesLink = false;
 }
 
 boolean disableDeleteApplication = !ppf.isEditor();
 
 // If no screen ID, don't show ACL link
 if (ppf.getApplicationScreenID() == null || 
     ppf.getApplicationScreenID().length() == 0)
 {
    showACLLink = false;
 } 
 
 boolean disableEdit = !ppf.isEditor();
 
 
 boolean disableDelete = !ppf.isEditor();
 
 if (!existingFlag)
 {
     disableDelete = true;
 }
 if (!existingApplicationFlag)
 {
	 disableDeleteApplication = true;
 }
%>
<BODY class="settingspage" onload="show<%=activePage %>()">
<table border="1" cellpadding="4">
<tr>
<td id="ProcessPointTab" class="activetab"><span onclick="showProcessPoint();">Process Point Config</span></td>
<% 
  String optionalTabStyle = null;
  if  (existingFlag) { 
      optionalTabStyle = "class=\"inactivetab\"";
  }
  else
  {
      optionalTabStyle = "style=\"display: none;\"";
  }
   
%>
<td id="ApplicationTab" <%= optionalTabStyle %>><span  onclick="showApplication();">Application Config</span></td>
<td id="BroadcastTab" <%= optionalTabStyle %>><span   onclick="showBroadcast();">Broadcast Config</span></td>

</tr>
<tbody>
</table>
<HR>
<TABLE width="100%">
<TBODY>
<TR id="ProcessPointSettings" style="display: none;">
<TD>
<h1 class="settingsheader">Process Point Information Config</h1>
<table width="100%" cellpadding="0" cellspacing="0">
<tbody>
<tr>
<td>
<html:form styleId="ppForm" action="/processPointSettings" name="processPointForm" type="com.honda.global.galc.system.config.web.forms.ProcessPointForm" scope="request">
    <html:hidden property="createFlag" />
    <html:hidden property="lineID" />
    <html:hidden property="siteName" />
    <html:hidden property="plantName" />
    <html:hidden property="divisionID" />
    <html:hidden property="divisionName" />
    <html:hidden property="lineName" />
    <html:hidden property="deleteConfirmed" />
    <html:hidden property="currentLineEntryPoint" />
    <html:hidden property="trackingPointFlag" />
    <input type="hidden" name="activePage" value="ProcessPoint" >
	<TABLE border="0">
		<TBODY>
			<TR>
				<TH class="settingstext" align="left">Process Point ID</TH>
				<TD><html:text property="processPointID" maxlength="16" readonly="<%=existingFlag%>" styleClass="<%=styleClass%>"/></TD>
			</TR>		
			<TR>
				<TH class="settingstext" align="left">Process Point Name</TH>
				<TD><html:text property="processPointName" maxlength="32" /></TD>
			</TR>			
			<TR>
				<TH class="settingstext" align="left">Process Point Type</TH>
				<TD><html:select property="processPointType" >
<% 
                    for(ProcessPointType ppType : ProcessPointType.values()) {
%>      
                    	var selectedOpt = <%= (ppType.getId() == ppf.getProcessPointType()) ? "selected" : ""%>
			         	<html:option value="<%= Integer.toString(ppType.getId())%>" ${selectedOpt}><%= ppType.getName() %></html:option>
<%
                    }
%>				
                   </html:select>
				</TD>
			</TR>
			<TR>
				<TH class="settingstext" align="left">Description</TH>
				<TD><html:text property="processPointDescription" size="50" maxlength="128"/></TD>
			</TR>
			<TR>
				<TH class="settingstext" align="left">Sequence Number</TH>
				<TD><html:text property="sequenceNo" maxlength="10" /></TD>
			</TR>
			<tr>
			<td colspan="2">
			<FIELDSET class="settingstext">
			    <LEGEND class="settingstext">Feature information</LEGEND>
			    <table>
			      <tbody>
			        <tr>
			        	<TH class="settingstext" align="left">Feature Type</TH>
	      		        <TD><html:select property="featureType" styleId="featureType" onchange="changedFeaturedType(this)">
	      		        <option label="NONE">NONE</option>
	      		        	<html:options property="featureTypeList"></html:options>
	      		        	</html:select>
	      		        </TD>	
			        </tr>
			       <tr>
			        	<TH class="settingstext" align="left">Feature ID</TH>
	      		        <TD><html:select property="featureId" styleId="featureId">
	      		        	<option label="NONE">NONE</option>
	      		        	<% if (ppf.getFeatureIdList()!=null) { %> 
     								<html:options property="featureIdList"></html:options>

  							 <% }%>
	      		        	</html:select>
	      		        </TD>	
			        </tr>
			      </tbody>
			    </table>			    
			</FIELDSET>
			</td>
			</tr>
			<tr>
			<td colspan="2">
			<FIELDSET class="settingstext">
			    <LEGEND class="settingstext">Passing counter information</LEGEND>
			    <table>
			      <tbody>
			        <tr>
				      <TD><html:checkbox property="passingCountFlag" >Passing counting point</html:checkbox></TD>
			        </tr>
			      </tbody>
			    </table>
			    
			</FIELDSET>
			</td>
			</tr>
			<tr>
			<td colspan="2">
			<FIELDSET class="settingstext">
			    <LEGEND class="settingstext">Recovery information</LEGEND>
			    <table>
			      <tbody>
			        <tr>
			        <TD align="left"><html:checkbox property="recoveryPointFlag" >Recovery Point Flag</html:checkbox>&nbsp;&nbsp;&nbsp;</TD>
			        <td align="right" class="settingstext">&nbsp;&nbsp;Backfill Process Point ID</td>
			        <TD><html:text property="backfillProcessPointID" /></TD>
			        </tr>
			      </tbody>
			    </table>
			    
			</FIELDSET>
			</td>
			</tr>			
			<tr>
			<td colspan="2">
			<FIELDSET class="settingstext">
			    <LEGEND class="settingstext">Expected product information</LEGEND>
			    <table>
			      <tbody>
			        <tr>
	      		        <TD align="left"><html:checkbox property="expectedProduct" >Expected Product</html:checkbox></TD>	
			        </tr>
			      </tbody>
			    </table>			    
			</FIELDSET>
			</td>
			</tr>		
			<tr>
			<td colspan="3">
				<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE"><bean:write name="msg"/></html:messages>

				<html:errors/>			
			</td>			
			</tr>	
		    <TR>
				<TD colspan="3"><html:submit property="apply" value="Apply"  disabled="<%= disableEdit %>" onclick = "return confirmAppply();"/>&nbsp;&nbsp;&nbsp;
				   <html:submit property="delete" value="Delete" disabled="<%= disableDelete %>" onclick="return confirmDelete();" />&nbsp;&nbsp;&nbsp;
				   <html:submit property="cancel" value="Cancel" />	
				   
				   <P> <%=ppf.getMissingGpcsDataMessage()%> </P>
				   
				</TD>   
			</TR>
		</TBODY>
	</TABLE>
</html:form>
</td>
<td valign="top">
<FIELDSET class="settingstext">
	<LEGEND class="settingstext">Related Settings</LEGEND>
	<table>
	  <tbody>
	     <tr><td><a class="settingspagelink" href="lineSettings.do?lineID=<%= ppf.getLineID() %>">Line Settings for <%= ppf.getLineID() %></a></td></tr>
	     <tr><td><html:link action="/gpcs"  styleClass="settingspagelink">GPCS</html:link></td></tr>
	     <% 
	        String deviceLink = null;
	        String opcLink = null;
	        String termLink = null;
	        if (ppf.getRelatedDeviceList() != null && ppf.getRelatedDeviceList().size() > 0) { 
	        
	            if (ppf.getRelatedDeviceList().size() == 1) {
	               Device data = (Device)ppf.getRelatedDeviceList().get(0);
	               deviceLink = "deviceSettings.do?clientID="+data.getClientId();
	            }
	            else {
	               deviceLink = "deviceList.do?processPointID="+ppf.getProcessPointID();
	            }
	        
	        } 
	        
	        if (ppf.getRelatedOPCList() != null && ppf.getRelatedOPCList().size() > 0) {
	           
	           if (ppf.getRelatedOPCList().size() == 1) {
	              OpcConfigEntry data= (OpcConfigEntry)ppf.getRelatedOPCList().get(0);
	              
	              opcLink = "opcSettings.do?load=load&newInstance=false&id="+data.getId();
	           }
	           else {
	           
	              opcLink = "opcConfigurationList.do?fetchList=fetch&processPointID="+ppf.getProcessPointID();
	           }
	        }
	        
	        if (ppf.getTerminals() != null && ppf.getTerminals().size() > 0) {
                if (ppf.getTerminals().size() == 1) {
                  Terminal terminal = ppf.getTerminals().get(0);
                  termLink = "terminalSettings.do?hostName=" + terminal.getHostName();
                } else {
                  termLink = "terminalList.do?appId=" + ppf.getApplicationID();
                }	        
	        }
	     %>
	     <%if (deviceLink != null) { %>
		   <tr>
      		<td><a class="settingspagelink" href="<%= deviceLink %>">Device References</a></td>
   		  </tr>
		<% } %>
		<%if (opcLink != null) { %>
		   <tr>
      		<td><a class="settingspagelink" href="<%= opcLink %>">OPC References</a></td>
   		  </tr>
		<% } %>
        <%if (termLink != null) { %>
           <tr>
            <td><a class="settingspagelink" href="<%= termLink %>">Terminal References</a></td>
          </tr>
        <% } %>		
	  </tbody>
	</table>
</FIELDSET>

	<%
		List<ProcessPointGroupDto> groups = ppf.getProcessPointGroups();
  		if (groups != null && groups.size() > 0) {
	%>
	<FIELDSET class="settingstext">
		<LEGEND class="settingstext">In Process Point Groupings</LEGEND>
		<table aligh="top" border="1" style="width: 100%;">
			<thead>
  				<tr>
    				<th>Flag</th>
    				<th>Process Group</th>
  				</tr>
			</thead>
				<tbody id="pgtable">
					<%
		  				for(ProcessPointGroupDto dto : groups) {
					%>
					<tr>
  						<td><%= dto.getRegionalValueName() %></td>
  						<td><%= dto.getProcessPointGroupName() %></td>
					</tr>
					<%
						}
					 %>
				</tbody>
		</table>
	</FIELDSET>
	<%
		}
	%>			

</td>
</tr>
</tbody>
</table>
</TD>
</TR>
<TR id="ApplicationSettings" style="display: none;">
<TD>
<h1 class="settingsheader">Application Information Config</h1>
<html:form styleId="appForm" action="/processPointSettings" name="processPointForm" type="com.honda.global.galc.system.config.web.forms.ProcessPointForm" scope="request">
    <input type="hidden" name="activePage" value="Application" >
    <html:hidden property="deleteApplicationConfirmed" />
    <html:hidden property="processPointName"/>
    <html:hidden property="processPointID"/>
    <html:hidden property="processPointDescription"/>
	<TABLE width="100%">
		<TBODY>
			
			<TR>
				<TH class="settingstext" align="left">Process Point At</TH>
				<TD><html:text property="applicationID" readonly="true" styleClass="readonly" styleId="appSettingsAppliationID"/>
				<% if (showPropertiesLink) {
				 
				
				      String propertiesURL = 
				         request.getContextPath()+"/propertySettings.do?staticComponentID=true&componentID="+ppf.getProcessPointID() +
								                              "&componentType="+PropertySettingsForm.COMPONENT_TYPE_APPLICATION;
				 %>
                 [<a class="settingspagelink" href="<%=propertiesURL %>">Application Properties</a>]
				 <%
				 }
				 %>
				 </TD>
			</TR>
			<TR>
				<TH class="settingstext" align="left">Screen ID</TH>
				<TD><html:text property="applicationScreenID" />
<%
if (showACLLink) {
   String aclurl = "/ACLSettings.do?init=true&applicationID="+ppf.getProcessPointID();
%>
&nbsp;&nbsp;&nbsp;<html:link page="<%= aclurl %>" target="aclwindow" styleClass="settingspagelink" onclick="window.open('','aclwindow','width=750,height=560,resizable=yes,status=yes')">[ACL Settings]</html:link>
<%
}
 %>
				
				</TD>
				<TD align="left"><html:checkbox property="applicationTerminalApplicationFlag" >Terminal Application Flag</html:checkbox></TD>
				
			</TR>
			<tr>
			   <th class="settingstext" align="left">Application Type</th>
			   <td>
			      <html:select property="applicationType" >
			         <html:optionsCollection name="processPointForm" property="applicationTypes" label="typeString" value="id" />
			      </html:select>
			   </td>
			</tr>
			<TR>
				<TH class="settingstext" align="left">Screen Class</TH>
				<TD colspan="2"><html:text property="applicationScreenClass" size="60" maxlength="128"/></TD>
			</TR>
			<TR>
				<TH class="settingstext" align="left">Window Title</TH>
				<TD colspan="2"><html:text property="applicationWindowTitle" size="60" maxlength="32"/></TD>
			</TR>
			<TR>
			   <TD>Preload index</TD>
			
			   <td colspan="2">
			     <table border="0" cellpadding="0" cellspacing="0">
			     <tbody>
			       <TR>

				   <TD><html:text property="applicationPreload" /></TD>
				   <TD>&nbsp;&nbsp;&nbsp;<html:checkbox property="applicationSessionRequired" >Session Required</html:checkbox></TD>
  				   <TD><html:checkbox property="applicationPersistentSession" >Persistent Session</html:checkbox></TD>
				   
				   </TR>
				 </tbody>
				 </table>
			   </td>
			</TR>
			<tr>
			  <td colspan="3">
			  <table width="100%">
			    <tbody >
			      <tr>
			        <td valign="top">
			           <input type="hidden" id="taskCount" name="taskCount" value="<%= ppf.getTaskList().size() %>">
			           <table width="100%" border="2">
			             <THEAD>
			                <tr>
			                <th width="5%">Seq</th>
			                <th width="15%">Input Event</th>
			                <th>TaskName</th>
			                <th width="15%">Begin/Commit</th>
			                <th>Argument</th>
			                </tr>
			             </THEAD>
			             <TBODY id="tasklisttablebody">
<%
      
      List<ApplicationTask> taskList = ppf.getTaskList();
      java.util.Iterator taskIterator = taskList.iterator();
      int seq=0;
      String checkedrb = "checked";
      String checkedinput = "checked";
      for(ApplicationTask taskData : taskList) {
             
          String beginCommit = "&nbsp;";
          
          if (taskData.getBeginFlag() > 0 && taskData.getCommitFlag() > 0)
          {
             beginCommit = "Begin/Commit";
          }   
          else if (taskData.getBeginFlag() > 0)
          { 
             beginCommit = "Begin";
          }
          else if (taskData.getCommitFlag() > 0)
          {
             beginCommit = "Commit";
          }
          
          seq++;
          
          if (seq == 1)
          {
            checkedrb = "checked";
          }
          else
          {
             checkedrb = "";
          }
          
          if (taskData.getInputEventExistFlag() > 0)
          {
             checkedinput = "checked";
          }
          else
          {
             checkedinput = "";
          }
          
          String taskargs = taskData.getArgument();
          if (taskargs == null || taskargs.length() == 0)
          {
             taskargs = "&nbsp;";
             
          }
 %>
                         <TR id="<%= "trtask"+seq %>">
                            <td id="<%= "tdtaskrb"+seq %>"><INPUT type="radio" name="tasksequence" value="<%= seq %>" <%= checkedrb %> ><%=taskData.getSequenceNo() %></td>
                            <td id="<%= "tdtaskinput"+seq %>"><INPUT type="checkbox" name="inputbox<%=seq %>"	<%= checkedinput %> disabled>
                                <input type="hidden" name="<%= "inputFlag"+seq %>" value="<%= taskData.getInputEventExistFlag() %>">
                                 </td>
                            <td id="<%= "tdtaskname"+seq %>"><%= taskData.getId().getTaskName() %>
                                <input type="hidden" name="<%= "taskName"+seq %>" value="<%= taskData.getId().getTaskName() %>" />
                                <input type="hidden" name="<%= "jndiName"+seq %>" value="<%= taskData.getJndiName() %>" />
                                <input type="hidden" name="<%= "statefulFlag"+seq %>" value="<%= taskData.getStatefullSessionBeanFlag() %>" />
                            </td>
                            <td id="<%= "tdtaskbegincommit"+seq %>"><%= beginCommit %>
                                <input type="hidden" name="<%= "beginFlag"+seq %>" value="<%= taskData.getBeginFlag() %>">
                                <input type="hidden" name="<%= "commitFlag"+seq %>" value="<%= taskData.getCommitFlag() %>">
                                
                            </td>
                            <td id="<%= "tdtaskargs"+seq %>"><%= taskargs %><input type="hidden" name="<%= "taskArgs"+seq %>" value="<%= taskData.getArgument() %>">
                            </td>
                         </TR>     
 <%     
      } 

 %>			               
			             </TBODY>
			           </table>
			        </td>
			        <td align="right" width="90">
			           <table border="0">
			              <tbody>
			              <tr>
			                 <td align="right"><button class="taskseqbutton" onclick="launchApplicationTaskWindow('add');return false;">Add</button></td>			                 
			              </tr>
			              <tr>
			                 <td align="right"><button class="taskseqbutton"  onclick="processRemove();return false;">Delete</button></td>			                 
			              </tr>
			              <tr>
			                 <td align="right"><button class="taskseqbutton"  onclick="launchApplicationTaskWindow('modify');return false;">Modify</button></td>			                 
			              </tr>
			              <tr>
			                 <td align="right"><button class="taskseqbutton"  onclick="processUp();return false;">Up</button></td>			                 
			              </tr>
			              <tr>
			                 <td align="right"><button class="taskseqbutton" onclick="processDown();return false;">Down</button></td>			                 
			              </tr>
			              <tr>
			                 <td align="right"><button class="taskseqbutton" onclick="launchTaskSpecWindow();return false;">Task&nbsp;Spec</button></td>			                 
			              </tr>
			              </tbody>
			           </table>
			        </td>
			      </tr>
			    </tbody>
			  </table>
			  </td>
			</tr>			
			<tr>
			<td colspan="3" id="applicationmsgcol">
				<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE"><bean:write name="msg"/></html:messages>

				<html:errors/>			
			</td>
			</tr>
			 <TR>
				<TD colspan="3"><html:submit property="apply" value="Apply" disabled="<%= disableEdit %>" />&nbsp;&nbsp;&nbsp;
				                <html:submit property="deleteApplication" value="Delete" disabled="<%= disableDeleteApplication %>" onclick="return confirmDeleteApplication();" />&nbsp;&nbsp;&nbsp;
				                <html:submit property="cancel" value="Cancel" /></TD>	
			</TR>			
		</TBODY>
	</TABLE>
</html:form>

</TD>
</TR>
<TR id="BroadcastSettings" style="display: none;">
<td>
<table>
<tbody>
<tr>
<TD>
<h1 class="settingsheader">Broadcast Destination Config</h1>
</TD>
<tr>
<td>
<html:form styleId="broadcastForm" action="/processPointSettings" name="processPointForm" type="com.honda.global.galc.system.config.web.forms.ProcessPointForm" scope="request"> 
	<input type="hidden" name="activePage" value="Broadcast" >
	<% request.setAttribute("disableEdit", Boolean.valueOf(disableEdit)); %>
	<c:set var="broadcastDestinationList" scope="request" value="${processPointForm.broadcastDestinationList}"/>
	<jsp:include page="../broadcast/BroadcastDestinationsTile.jsp" flush="false"/>
</html:form>
</td>
</tr>
</tbody>
</table>
</TR>
</TBODY>
</TABLE>

</BODY>
</html:html>
