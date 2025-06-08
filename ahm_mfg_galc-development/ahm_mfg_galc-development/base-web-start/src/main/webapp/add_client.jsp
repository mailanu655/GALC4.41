<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.webstart.WebStartConstants" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>Add Client</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="GENERATOR" content="Rational Software Architect">
<script type="text/javascript" src="js/jquery-1.11.2.min.js"></script>
 <c:if test="${empty builds || empty terminals}">
	<c:redirect url="ADD_CLIENT.act"/>
 </c:if>
 <script language="javascript">
 	
 	var defaultIpAddress = "<%=WebStartConstants.DEFAULT_IP_ADDRESS%>";	
	
	function checkClientForm() {
		var missing = "";

		if(document.getElementById("build_id").options[document.getElementById("build_id").selectedIndex].value == "") {
			missing += "\n  - Target Build";
		}
		
		if(document.getElementById("ip_address").value == "") {
			missing += "\n  - IP Address";
		}
		
		if(document.getElementById("host_name").value == "") {
			missing += "\n  - Host Name";
		}
		
		if(missing != "") {
			alert("Following fields are empty: \n" + missing);
			return false;
	    }
		
		var obj1 = document.getElementById("feature_type");
		var featureType = obj1.options[obj1.selectedIndex].value;
	    var obj2 = document.getElementById("feature_id");
		var featureId = obj2.options[obj2.selectedIndex].value;
	    if((featureType != "NONE") && (featureId == "NONE"))
		{
		 alert("Please select a Feature Id");
		 return false;
		}
		
		var phoneNo = document.getElementById("phone_extension").value;
  		if(isNaN(phoneNo)){  
     		  alert("Please enter valid phone number");  
     		   return false;
       	 }  
     	
	    
    	return true;
	}

    function clickDefault() {
    	document.getElementById("ip_address").value = defaultIpAddress;
    }
    
    function clearDefault() {
    	document.getElementById("default_ip").checked = false;
    }
    
    
    function changedFeaturedType(selectedFeatureType) {
 
 		var selectedFeatureType = selectedFeatureType.value;
 
  		if (document.getElementById('feature_id').options.length != 0) 
 			{ 
 				 document.getElementById('feature_id').options.length = 0;
 			}
 
  		var url= "/BaseWebStart/AjaxRequestServlet?feature_type="+selectedFeatureType;
 
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
		      	 	var select = document.getElementById('feature_id');
			      	var featureIdValue = xmlHttp.responseText.split("|||"); 
			      	 for(counter=1;counter<featureIdValue.length-1;counter++) {
					
	 					var value = featureIdValue[counter];
 					
	 					var opt = document.createElement('option');
	 					opt.value = value;
    				    opt.innerHTML = value;
    					select.appendChild(opt); 
	 					 
	 					
			      	} 
			      	
				} else{
					//alert("Ajax request does not succeed");
				}
			}
			xmlHttp.open("GET", url, true);
			xmlHttp.send(null);
 }
    
</script>
 
</head>

<body>
<jsp:include flush="false" page="header.jsp"></jsp:include>
	<br>
	<br>
	<center>
	<h2>Configure Webstart Client</h2>
	<br>
	<form id="Form" action="<%=WebStartConstants.Action.ADD_CLIENT.action()%>" method="Post" onSubmit="return checkClientForm();">
	<TABLE width="90%" border="0" cellpadding="15">
		<TBODY>
		<TR>
			<TD>
			  <table class="none" border="0" cellpadding="10">
			    <tr> 
			    	<th width="40%" align="right">Hostname / IP Address:</th>
			      	<td>
				        <input type="text" id="ip_address" name="ip_address" onclick="clearDefault();">
				        <input id="default_ip" type="checkbox" onclick="clickDefault();"><b>Default</b>
			      	</td>
			    </tr>
			    <tr> 
			    	<th  width="40%" align="right">Terminal Name:</th>
			      	<td>
				        <select id="host_name" name="host_name">
				        	<c:forEach var="term" items="${terminals}">
			          			<option value='<c:out value="${term.hostName}"/>'><c:out value="${term.hostName}"/></option> 
				        	</c:forEach>
				        	<c:remove var="terminals"/>
			        	</select>
			      	</td>
			    </tr>
			    
			    <tr>
			    	<th  width="40%" align="right">Target Build:</th>
			    	<td>
			        	<select id="build_id" name="build_id">
				        	<c:forEach var="build" items="${builds}">
			          			<option value='<c:out value="${build.buildId}"/>'><c:out value="${build.buildId} - ${build.description}"/></option> 
				        	</c:forEach>
				        	<c:remove var="builds"/>
			        	</select>
			      	</td>
			    </tr>
			    <tr> 
			    	<th  width="40%" align="right">Asset Number:</th>
			      	<td>
				        <input type="text" id="asset_number" name="asset_number" onclick="clearDefault();">
			      	</td>
			    </tr>
			    <tr> 
			    	<th  width="40%" align="right">Column Location:</th>
			      	<td>
				        <input type="text" id="column_location" name="column_location" onclick="clearDefault();">
			      	</td>
			    </tr>
			    <tr>
			    	<th width="40%" align="right">Feature Type:</th>
			    	<td>
			    		<select id="feature_type" name="feature_type" onchange="changedFeaturedType(this)">
			    			 <c:forEach var="featuretype" items="${featuretypes}">
			          			<option value='<c:out value="${featuretype}"/>'><c:out value="${featuretype}"/></option> 
				        	</c:forEach> 
				        	
			    		</select>
			    	</td>
			    </tr>
			    
			    <tr>
			    	<th width="40%" align="right">Feature Id:</th>
			    	<td>
			    		<select id="feature_id" name="feature_id">
			    			<option value="NONE">NONE</option>
			    		</select>
			    	</td>
			    </tr>
			    
			    			    
			    <tr> 
			    	<th  width="40%" align="right">Phone Extension:</th>
			      	<td>
				        <input type="text" id="phone_extension" name="phone_extension" onclick="clearDefault();">
			      	</td>
			    </tr>

			  </table>
			</TD>
		</TR>
		<TR>
			<TD>
			    <input type="submit" value="Save" class="smalltext">
			    <input type="button" value="Cancel" class="smalltext" onclick="history.back();">
			</TD>
		</TR>
		</TBODY>
	</TABLE>
	</form>
	</center>
</body>
</html>

