
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.ApplicationTaskForm" %>
<%@ page import="com.honda.galc.entity.conf.*" %>
<%@ page import="java.util.*" %>
<%@ page session="false" %>
<% response.setHeader("Pragma","no-cache"); %>
<% response.setHeader("Cache-control", "no-cache"); %>
<% response.setHeader("Expires", "0"); %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM Software Development Platform">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/Master.css" rel="stylesheet"
	type="text/css">
<%
    // Get reference to the TaskSpecData list
    ApplicationTaskForm appTaskForm = (ApplicationTaskForm)request.getAttribute("applicationTaskForm");
    
    List<TaskSpec> taskSpecList = appTaskForm.getTaskSpecList();
    
    if (taskSpecList == null)
    {
       taskSpecList = new java.util.ArrayList<TaskSpec>(0);
    }
    
    String jndiName = "";
    String stateful = "false";
    String taskName = appTaskForm.getTaskName();
 %>
	
<SCRIPT>
   
   var jndiArray = null;
   var statefulArray = null;
   
   function initArray() {
      jndiArray = new Array(<%= taskSpecList.size() %>);
      statefulArray = new Array(<%= taskSpecList.size() %>);
      
      <%
      int idx = 0; 
      
      for(TaskSpec taskSpecData : taskSpecList) {
         
         if (taskSpecData.getTaskName().equals(taskName))
         {
            jndiName = taskSpecData.getJndiName();
            
            if (taskSpecData.getStatefullSessionBeanFlag() > 0)
            {
               stateful = "true";
            }
         }
         %>
      jndiArray[<%= idx %>] = "<%= taskSpecData.getJndiName() %>";
      statefulArray[<%= idx %>] = "<%= taskSpecData.getStatefullSessionBeanFlag() > 0 %>";
         <%
         idx++;
      }
      %>
   }
   
   function applySettings()
   {
      taskName = document.applicationTaskForm.taskName.value;
      commitFlag = "false";
      
      if (document.applicationTaskForm.commitFlag.checked == true)
      {
          commitFlag = "true";        
      }
      
      beginFlag = "false";
      if (document.applicationTaskForm.beginFlag.checked == true)
      {
          beginFlag = "true";
      }
      
      inputEventFlag = "false";
      if (document.applicationTaskForm.inputEventFlag.checked == true)
      {
         inputEventFlag = "true";
      }
   
      taskArgs = document.applicationTaskForm.taskArg.value;
      if (taskArgs == null)
      {
         taskArgs = "";
      }
      
      sequenceNo = document.applicationTaskForm.sequenceNo.value;
      
      applicationID = document.applicationTaskForm.applicationID.value;
      
      operation = document.applicationTaskForm.operation.value;
      
      jndiName = document.applicationTaskForm.jndiName.value;
      
      statefulFlag = document.applicationTaskForm.statefulFlag.value;
      
      if (window.opener && !window.opener.closed)
      {
         window.opener.updateTaskList(operation, sequenceNo, inputEventFlag, taskName, beginFlag,commitFlag, taskArgs, jndiName, statefulFlag);
      }
      else
      {
         alert("Unable to access parent window");
      }
   }
   
   function taskSelectionChange() {
      
      var cb = null;
      var jndief = null;
      
      cb = document.getElementById("taskcb");
      jndief = document.getElementById("jndief");
      statefulfield = document.getElementById("taskStatefulFlag");
      
      idx  = cb.options.selectedIndex;
      jndief.value = jndiArray[idx];
      statefulfield.value = statefulArray[idx];
      
      
      
   }
</SCRIPT>
<TITLE>Application Task Configuration</TITLE>
</HEAD>
<BODY class="settingspage" onload="initArray()">
<html:form name="applicationTaskForm" type="com.honda.global.galc.system.config.web.forms.ApplicationTaskForm"  scope="request" action="/applicationTaskSettings">
    <html:hidden property="sequenceNo" />
    <html:hidden property="operation"/>
    <html:hidden property="applicationID"/>
    <input id="taskStatefulFlag" type="hidden" name="statefulFlag" value="<%= stateful %>" />
              
	<TABLE border="0">
		<TBODY>
		    <tr>
		       <TH align="left" class="settingstext">Task Name</TH>
				<TD><html:select property="taskName" onchange="taskSelectionChange()" styleId="taskcb">
<% 
                    for(TaskSpec taskSpecData : taskSpecList) {
%>
			         <html:option value="<%= taskSpecData.getTaskName() %>"><%= taskSpecData.getTaskName() %></html:option>
<%
                    }
 %>				
                   </html:select>
				</TD>
		    </tr>
		    <tr>
		       <th align="left" class="settingstext">JNDI</th>
		       <td>
				<INPUT id="jndief" type="text" name="jndiName" size="80" maxlength="120"
					readonly value="<%= jndiName %>">
		       </td>
			</tr>
		    <TR>
				<TH align="left" class="settingstext">Task Arguments</TH>
				<TD><html:text property="taskArg" size="80" maxlength="128">60</html:text></TD>
			</TR>
			<TR>
			    <TD colspan="2">
			    <table width="100%">
			    <tbody>
			    <tr>
			    <td>
			    <FIELDSET class="settingstext"><LEGEND class="settingstext">Transaction Begin/Commit</LEGEND>
			    <TABLE>
			    <TBODY>
			    <TR>			   	
				<TD><html:checkbox property="beginFlag" >Begin</html:checkbox></TD>
				<TD><html:checkbox property="commitFlag" >Commit</html:checkbox></TD>
				</TR>
				</TBODY>
				</TABLE>
				</FIELDSET>
				</td>
				<TD align="right"><html:checkbox property="inputEventFlag" >Input Event</html:checkbox></TD>
				</tr>
				</tbody>
				</table>
				</TD>
			</TR>
						
			<TR>
				<TD><BUTTON onclick="applySettings();return false;">Save</BUTTON></TD>
			</TR>
		</TBODY>
	</TABLE>
</html:form>
</BODY>
</html:html>
