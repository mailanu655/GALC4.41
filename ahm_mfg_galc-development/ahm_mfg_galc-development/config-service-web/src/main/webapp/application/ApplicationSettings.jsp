
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@page import="com.honda.galc.system.config.web.forms.PropertySettingsForm"%>
<%@ page import="com.honda.galc.entity.conf.*" %>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.ApplicationForm" %>
<%@ page session="false" %>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<TITLE>Application Settings</TITLE>
<script>

function displayMessage(colid, messageText) {
    
    var msgcol = document.getElementById(colid);
    
    if (msgcol != null)
    {
      msgcol.innerHTML=messageText;
    }
}

		function confirmDelete()
		{
		   
		   
		   appID = document.getElementById("appForm").applicationID.value;
		   
		   result = confirm("Are you sure you want to delete the application "+appID);
		   
		   if (result)
		   {
		      document.getElementById("appForm").deleteConfirmed.value = "true";
		      
		      return true;
		   }
		   
		   return false;
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
	    
	    function verifyInput() 
	    {
	    	var applicationId = document.getElementById("applicationID").value;
	    	if(applicationId == "") {
	    		alert("Please enter the Application ID");
	    		return false;
	    	}
	    	var applicationName = document.getElementById("applicationName").value;
	    	if(applicationName == "") {
	    		alert("Please enter the Application Name");
	    		return false;
	    	}
	    	return true;
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
	        	
	        	// @JM200803 - Fix problems with JNDI name and stateful flag
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
<script type="text/javascript" src="common/common.js"></script>
<script type="text/javascript">
		function showApplication() {
		   normalText('BroadcastTab');
		   boldText('ApplicationTab');
		   hide('BroadcastSettings');
 		   show('ApplicationSettings');
		}

		function showBroadcast() {
		   normalText('ApplicationTab');
		   boldText('BroadcastTab');
		   hide('ApplicationSettings');
		   show('BroadcastSettings');
		}
</script>
<script type="text/javascript" src="broadcast/BroadcastDestinations.js"></script>
</HEAD>
<%
  ApplicationForm applicationForm = (ApplicationForm)request.getAttribute("applicationForm");
  
  if (applicationForm == null)
  {
     // Create a stub
     applicationForm = new ApplicationForm();
  }
  
  boolean disableEdit = !applicationForm.isEditor();
  
  boolean disableDelete = !applicationForm.isEditor();
  
  boolean applicationIDReadOnly = true;
  
  boolean showACLLink = true;
  
  boolean showPropertiesLink = true;

  String activePage = null;
  
  if (applicationForm.isCreateFlag())
  {
    applicationIDReadOnly = false;
    disableDelete = true;
    showACLLink = false;
    showPropertiesLink = false;
  }
  
  activePage = applicationForm.getActivePage();
  if (activePage == null || activePage.length() == 0)
  {
        activePage = "Application";
        applicationForm.setActivePage("Application");
  }
	 
  
  String readonlyClass = "";
  
  
  if (!applicationForm.isCreateFlag())
  {
     // ID field is read-only
     readonlyClass = "readonly";
  
     // Disable whole form if not create and no data to display
     if (applicationForm.getApplicationID() == null ||
         applicationForm.getApplicationID().length() == 0)
     {
        disableEdit = true;
        disableDelete = true;
     }
     
     // If no screen ID, don't show ACL link
     if (applicationForm.getScreenID() == null || 
         applicationForm.getScreenID().length() == 0)
     {
        showACLLink = false;
       
     }
  }
  
 %>

<BODY class="settingspage" onload="show<%=activePage %>()">
<table border="1" cellpadding="4">
<tr>
<td id="ApplicationTab" class="activetab"><span  onclick="showApplication();">Application Config</span></td>
<% 
  boolean existingFlag = true;
  String optionalTabStyle = null;
  if  (existingFlag) { 
      optionalTabStyle = "class=\"inactivetab\"";
  }
  else
  {
      optionalTabStyle = "style=\"display: none;\"";
  }
   
  String showWarningColor = (String)request.getAttribute("DISPLAY_PRODUCTION_WARNING_COLOR");
%>
<td id="BroadcastTab" <%= optionalTabStyle %>><span   onclick="showBroadcast();">Broadcast Config</span></td>

</tr>
<tbody>
</table>

<HR>
<TABLE width="100%">
<TBODY>
<TR id="ApplicationSettings" style="display: none;">
<TD>
<h1 class="settingsheader" style="<%=showWarningColor%>" >Application Information Config</h1>

<html:form styleId="appForm" action="/applicationSettings" name="applicationForm" type="com.honda.global.galc.system.config.web.forms.ApplicationForm" scope="request">
    <input type="hidden" name="activePage" value="Application" >
    <html:hidden property="createFlag"/>
    <html:hidden property="deleteConfirmed" />
	<TABLE width="100%">
		<TBODY>
			
			<TR>
				<TH class="settingstext" align="left">Application ID:</TH>
				<TD><html:text property="applicationID" size="32" maxlength="16" readonly="<%= applicationIDReadOnly %>" styleClass="<%= readonlyClass %>" styleId="appSettingsAppliationID"/>
				<% if (showPropertiesLink) {
				 
				
				      String propertiesURL = 
				         "/propertySettings.do?staticComponentID=true&componentID="+applicationForm.getApplicationID() +
								                              "&componentType="+PropertySettingsForm.COMPONENT_TYPE_APPLICATION;
				 %>
                 &nbsp;&nbsp;&nbsp;[<html:link styleClass="settingspagelink" page="<%=propertiesURL %>" target="propWindow" onclick="window.open('','propWindow','width=1000,height=700,resizable=yes,status=yes')">Application Properties</html:link>]
				 <%
				 }
				 %>
				
				</TD>
			</TR>
			<tr> 
				<TH class="settingstext" align="left">Application Name:</TH>
				<TD><html:text property="applicationName" size="32" maxlength="32"/></TD>			
			</tr>
			<tr>
			   <th class="settingstext" align="left">Application Type:</th>
			   <td>
			      <html:select property="applicationType" >
			         <html:optionsCollection name="applicationForm" property="applicationTypes" label="typeString" value="id" />
			      </html:select>
			   </td>
			</tr>
			<TR>
				<TH class="settingstext" align="left">Screen ID</TH>
				<TD><html:text property="screenID" />
<%
if (showACLLink) {
   String aclurl = "/ACLSettings.do?init=true&applicationID="+applicationForm.getApplicationID();
%>
&nbsp;&nbsp;&nbsp;<html:link page="<%= aclurl %>" target="aclwindow" styleClass="settingspagelink" onclick="window.open('','aclwindow','width=750,height=560,resizable=yes,status=yes')">[ACL Settings]</html:link>
<%
}
 %>
				</TD>
				
				
			</TR>
			<TR>
				<TH class="settingstext" align="left">Screen Class</TH>
				<TD colspan="2"><html:text property="screenClass" size="60" maxlength="128"/></TD>
			</TR>
			<TR>
				<TH class="settingstext" align="left">Window Title</TH>
				<TD colspan="2"><html:text property="windowTitle" size="60" maxlength="32"/></TD>
			</TR>
			<TR>
			   <TD>Preload index</TD>
			
			   <td colspan="2">
			     <table border="0" cellpadding="0" cellspacing="0">
			     <tbody>
			       <TR>

				   <TD><html:text property="preload" /></TD>
				   <TD>&nbsp;&nbsp;&nbsp;<html:checkbox property="sessionRequired" >Session Required</html:checkbox></TD>
  				   <TD><html:checkbox property="persistentSession" >Persistent Session</html:checkbox></TD>
				   
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
			           <input type="hidden" id="taskCount" name="taskCount" value="<%= applicationForm.getTaskList().size() %>">
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
      
      int seq=0;
      String checkedrb = "checked";
      String checkedinput = "checked";
      for(ApplicationTask taskData :applicationForm.getTaskList()) { 
             
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
			                 <td align="right"><button class="taskseqbutton" onclick="launchApplicationTaskWindow('add');return false;" >Add</button></td>			                 
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
				<TD colspan="3"><html:submit property="apply" value="Apply" disabled="<%= disableEdit %>" onclick="return verifyInput();"/>&nbsp;&nbsp;&nbsp;
				                <html:submit property="delete" value="Delete" disabled="<%= disableDelete %>" onclick="return confirmDelete();" />&nbsp;&nbsp;&nbsp;
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
<h1 class="settingsheader" style="<%=showWarningColor%>" >Broadcast Destination Config</h1>
</TD>
<tr>
<td>
<html:form styleId="broadcastForm" action="/applicationSettings" name="applicationForm" type="com.honda.global.galc.system.config.web.forms.ApplicationForm" scope="request"> 
	<input type="hidden" name="activePage" value="Broadcast" >
	<% request.setAttribute("disableEdit", Boolean.valueOf(disableEdit)); %>
	<c:set var="broadcastDestinationList" scope="request" value="${applicationForm.broadcastDestinationList}"/>
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
