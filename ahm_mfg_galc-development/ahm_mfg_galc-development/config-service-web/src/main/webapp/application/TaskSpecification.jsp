
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.system.config.web.forms.TaskSpecificationForm" %>
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
<TITLE>Task Specification</TITLE>
<%

  boolean findResultsMode = false;
  List<TaskSpec> taskSpecList = null;
  String onloadCommand = "";
  
  TaskSpecificationForm taskSpecForm = (TaskSpecificationForm)request.getAttribute("taskSpecificationForm");
  
  
  if (taskSpecForm != null)
  {
  	  taskSpecList = taskSpecForm.getTaskSpecList();
  	  
  	  if (taskSpecList != null && !taskSpecList.isEmpty())
  	  {
  	      findResultsMode = true;
  	  }
  }
  
 if (findResultsMode) { 
    onloadCommand = "loadArrays();";  
 %>
<SCRIPT>

   var jndiArray = null;
   var descriptionArray = null;
   var statefulArray = null;
   
   function loadArrays() {
      jndiArray = new Array(<%= taskSpecList.size() %>);
      descriptionArray = new Array(<%= taskSpecList.size() %>);
      statefulArray = new Array(<%= taskSpecList.size() %>);
      
      <%
      
      	int idx = 0;
      	for(TaskSpec taskSpecData : taskSpecList) { 
         
      %>
      	jndiArray[<%= idx %>] = "<%= taskSpecData.getJndiName() %>";
      	statefulArray[<%= idx %>] = "<%= taskSpecData.getStatefullSessionBeanFlag() > 0 %>";
      	descriptionArray[<%= idx %>] = "<%= taskSpecData.getTaskSpecDescription() %>";
       <%
         	idx++;
      	}
      %>
   }
   
   function doSelectionChange()
   {
      var cb = null;
      var jndief = null;
      
      cb = document.getElementById("taskcb");
      jndief = document.getElementById("jndief");
      statefulfield = document.getElementById("statefulCheckbox");
      descriptionef = document.getElementById("descriptionef");
      
      idx  = cb.options.selectedIndex;
      jndief.value = jndiArray[idx];
      descriptionef.value = descriptionArray[idx];
      
      if (statefulArray[idx] == "true")
      {
        statefulfield.checked = true;
      }
      else
      {
        statefulfield.checked = false;
      }
   
   }
   
   

</SCRIPT>
 <%
 }
 %>
 <script>
    function closeTaskSpecWindow() {
       if (window.opener && !window.opener.closed)
       {
          window.opener.closeTaskSpecWindow();
       }
       else
       {
          window.close();
       }
    }
 </script>
</HEAD>

<BODY class="settingspage" onload="<%= onloadCommand %>">
<html:form name="taskSpecificationForm"  type="com.honda.global.galc.system.config.web.forms.TaskSpecificationForm" action="/taskSpecificationSettings" scope="request">
	<TABLE border="0">
		<TBODY>
		
			<TR>
				<TH align="left" class="settingstext">Task Name</TH>
				<% if (!findResultsMode) { %>
				<TD><html:text property="taskName" maxlength="32" size="64"/></TD>
				<%  } else  { %>
				<td><html:select property="taskName" styleId="taskcb" onchange="doSelectionChange();">
				        <html:optionsCollection property="taskSpecList" label="taskName" value="taskName" />
				    </html:select></td>
				
				<%  } /* end if displaying results */%>
				
			</TR>
			<TR>
				<TH align="left" class="settingstext">JNDI Name</TH>
				<TD><html:text property="jndiName" styleId="jndief" maxlength="128" size="64"/></TD>
			</TR>
			<TR>
				<TH align="left" class="settingstext">Description</TH>
				<TD><html:text property="taskDescription" size="64" maxlength="128" styleId="descriptionef"/></TD>
			</TR>
			<TR>				
				<TD colspan="2" align="left" class="settingstext"><html:checkbox property="statefulSessionBean" styleId="statefulCheckbox" >Stateful Session Bean</html:checkbox></TD>
			</TR>
			<tr>
			  <td colspan="2">
				<html:messages id="msg" name="org.apache.struts.action.ACTION_MESSAGE">
				   <bean:write name="msg"/>
				</html:messages>
				<html:errors />			  
			  </td>
			</tr>				
			<TR>
				<TD colspan="2" align="center">
				     <html:submit property="apply" value="Apply" />&nbsp;&nbsp;&nbsp;
				     <% if (!findResultsMode)  {%>
				     <html:submit property="findTask" value="Find Task" />&nbsp;&nbsp;&nbsp;
				     <% } %>
				     <html:submit property="delete" value="Delete" />&nbsp;&nbsp;&nbsp;
				     <button onclick="closeTaskSpecWindow();return false;" >Cancel</button>
			    </TD>
		    </TR>
		</TBODY>
	</TABLE>
</html:form>
</BODY>
</html:html>
