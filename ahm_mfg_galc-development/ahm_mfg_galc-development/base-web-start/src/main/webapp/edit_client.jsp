<HTML>
<HEAD>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.honda.galc.webstart.WebStartConstants" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script language="javascript">

 	var defaultIpAddress = "<%=WebStartConstants.DEFAULT_IP_ADDRESS%>";
	
	function checkUpdateClientForm() {
	    var obj = document.getElementById("build_id");
	    var buildId = obj.options[obj.selectedIndex].value;
	    var oldBuildId = document.getElementById("old_build_id").value;

		// get buildId from old value
	    if(buildId == "" || buildId == "null") {
	    	  obj.value = buildId = oldBuildId;
	    }
		
	    var obj2 = document.getElementById("host_name");
	    var hostName = obj2.options[obj2.selectedIndex].value;
	    var oldHostName = document.getElementById("old_host_name").value;

		// get buildId from old value
	    if(hostName == "" || hostName == "null") {
	    	  obj2.value = hostName = oldHostName;
	    }
		
		var obj3 = document.getElementById("feature_type");
		var featureType = obj3.options[obj3.selectedIndex].value;
	    var oldFeatureType = document.getElementById("old_feature_type").value;
	    
	    // get featureType from old value
	    if(featureType == "" || featureType == "null") {
	    	  obj3.value = featureType = oldFeatureType;
	    }
		
		var obj4 = document.getElementById("feature_id");
		var featureId = obj4.options[obj4.selectedIndex].value;
	    var oldFeatureId = document.getElementById("old_feature_id").value;
	    
	    // get featureId from old value
	    if(featureId == "" || featureId == "null") {
	    	  obj4.value = featureId = oldFeatureId;
	    }
	    
	   
	   
		
		
		var ipAddress = document.getElementById("ip_address").value
		var oldIpAddress = document.getElementById("old_ip_address").value
		
		var assetNumber = document.getElementById("asset_number").value
		var oldAssetNumber = document.getElementById("old_asset_number").value
		
		var columnLocation = document.getElementById("column_location").value
		var oldColumnLocation = document.getElementById("old_column_location").value
		
		var oldShutdownFlag = document.getElementById("old_shutdown_flag").value
		var shutdownFlag = document.getElementById("shutdown_flag").value
		
		var oldPhoneExtension = document.getElementById("old_phone_extension").value
		var phoneExtension = document.getElementById("phone_extension").value
		
		//var hostName = document.getElementById("host_name").value
		//var oldHostName = document.getElementById("old_host_name").value
		// alert("buildId: " + buildId + ", oldBuildId: " + oldBuildId + ", ipAddress: " + oldIpAddress + ", ipAddress: " + oldIpAddress)
		
	if (buildId == oldBuildId && ipAddress == oldIpAddress
				&& assetNumber == oldAssetNumber
				&& columnLocation == oldColumnLocation
				&& hostName == oldHostName && shutdownFlag == oldShutdownFlag
				&& featureType == oldFeatureType && featureId == oldFeatureId && oldPhoneExtension == phoneExtension) {
			alert("There are no changes - there is nothing to save");
			return false;
		}

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

		if (document.getElementById('feature_id').options.length != 0) {
			document.getElementById('feature_id').options.length = 0;
		}

		var url = "/BaseWebStart/AjaxRequestServlet?feature_type="
				+ selectedFeatureType;

		try {
			// Firefox, Opera 8.0+, Safari 
			xmlHttp = new XMLHttpRequest();
		} catch (e) {
			// Internet Explorer 6.0+
			try {
				xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
			} catch (e) {
				try {
					xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
				} catch (e) {
					alert("Your browser does not support AJAX!");
					return false;
				}
			}
		}

		xmlHttp.onreadystatechange = function() {
			if (xmlHttp.readyState == 4) {
				var ser = xmlHttp.responseText;
				var select = document.getElementById('feature_id');
				var featureIdValue = xmlHttp.responseText.split("|||");
				for (counter = 1; counter < featureIdValue.length - 1; counter++) {

					var value = featureIdValue[counter];

					var opt = document.createElement('option');
					opt.value = value;
					opt.innerHTML = value;
					select.appendChild(opt);

				}

			} else {
				//alert("Ajax request does not succeed");
			}
		}
		xmlHttp.open("GET", url, true);
		xmlHttp.send(null);
	}
</script>
 <c:if test="${empty builds || empty terminals}}">
	<c:redirect url="EDIT_CLIENT.act?host_name=${webstart_client.hostName}"/>
 </c:if>

</HEAD>

<body>
<jsp:include flush="true" page="header.jsp"></jsp:include>
	<br>
	<br>
	<center>
	<h2>Configure GALC Web Start Client</h2>
	<br>
	<form id="Form" action="UPDATE_CLIENT.act" method="Post" onSubmit="return checkUpdateClientForm();">
	<TABLE width="90%" border="0" cellpadding="15">
		<TBODY>
		<TR>
			<TD>
			  <table class="edit" border="1" cellpadding="10">
			  <th>&nbsp;</th>
			  <th>Current</th>
			  <th>New</th>
			    <tr> 
			    	<th width="120" align="right">Host Name / IP Address:</th>
			      	<td><input id="old_ip_address" name="old_ip_address" style="background-color: #dddddd;"
			      		value='<c:out value="${webstart_client.ipAddress}"/>' readonly></td>
			      	<td><input id="ip_address" name="ip_address" value='<c:out value="${webstart_client.ipAddress}"/>' onclick="clearDefault()">
			      	<input id="default_ip" type="checkbox" onclick="clickDefault();"><b>Default</b></td>
			    </tr>
			    <tr> 
			    	<th width="120" align="right">GALC Terminal Name</th>
			      	<td><input id="old_host_name" name="old_host_name"  style="background-color: #dddddd;"
			      		value='<c:out value="${webstart_client.hostName}"/>' readonly></td>
			      	<td>
				        <select id="host_name" name="host_name"">
			          		<option value="">(no change)</option> 
				        	<c:forEach var="term" items="${terminals}">
			          			<option value='<c:out value="${term.hostName}"/>'><c:out value="${term.hostName}"/></option> 
				        	</c:forEach>
				        	<c:remove var="terminals"/>
			        	</select>
			      	</td>
			    </tr>
			    
			    <tr> 
			    	<th width="120" align="right">Build ID:</th>
			      	<td><input id="old_build_id" name="old_build_id"  style="background-color: #dddddd;"
			      		value='<c:out value="${webstart_client.buildId}"/>' readonly></td>
			    	<td>
			        	<select id="build_id" name="build_id">
			          		<option value="">(no change)</option> 
				        	<c:forEach var="build" items="${builds}">
			          			<option value="<c:out value="${build.buildId}"/>"><c:out value="${build.buildId}"/></option> 
				        	</c:forEach>
				        	<c:remove var="builds"/>
			        	</select>
			      	</td>
			    </tr>
			    <tr> 
			    	<th width="120" align="right">Description:</th>
			      	<td colspan="2">&nbsp;<span id="description"><c:out value="${webstart_client.description}"/></span></td>
			    </tr>
			    <tr> 
			    	<th width="120" align="right">Asset Number:</th>
			      	<td><input id="old_asset_number" name="old_asset_number"  style="background-color: #dddddd;"
			      		value='<c:out value="${webstart_client.assetNumber}"/>' readonly></td>
			      	<td><input id="asset_number" name="asset_number" value='<c:out value="${webstart_client.assetNumber}"/>' onclick="clearDefault()">
			    </tr>
			    <tr> 
			    	<th width="120" align="right">Column Location:</th>
			      	<td><input id="old_column_location" name="old_column_location"  style="background-color: #dddddd;"
			      		value='<c:out value="${webstart_client.columnLocation}"/>' readonly></td>
			      	<td><input id="column_location" name="column_location" value='<c:out value="${webstart_client.columnLocation}"/>' onclick="clearDefault()">
			    </tr>
			    <tr> 
			    	<th width="120" align="right">Shutdown Flag:</th>
			      	<td><input id="old_shutdown_flag" name="old_shutdown_flag"  style="background-color: #dddddd;"
			      		value='<c:out value="${webstart_client.shutdownFlag}"/>' readonly></td>
			      	<td><input id="shutdown_flag" name="shutdown_flag" value='<c:out value="${webstart_client.shutdownFlag}"/>' onclick="clearDefault()">
			    </tr>
			    <tr> 
			    	<th width="120" align="right">Heartbeat Time:</th>
			      	<td colspan="2">&nbsp;<span id="heartBeatTimestamp"><c:out value="${webstart_client.heartBeatTimestamp}"/></span></td>
			    </tr>
			    <tr> 
			    	<th width="120" align="right">Feature Type:</th>
			      	<td><input id="old_feature_type" name="old_feature_type"  style="background-color: #dddddd;"
			      		value='<c:out value="${webstart_client.featureType}"/>' readonly></td>
			      	<td>
				        <select id="feature_type" name="feature_type" onchange="changedFeaturedType(this)">
				        <option value="">(no change)</option>
			    			 <c:forEach var="featuretype" items="${featuretypes}">
			          			<option value='<c:out value="${featuretype}"/>'><c:out value="${featuretype}"/></option> 
				        	</c:forEach> 
				        	
			    		</select>
			      	</td>
			    </tr>
			    <tr> 
			    	<th width="120" align="right">Feature Id:</th>
			      	<td><input id="old_feature_id" name="old_feature_id"  style="background-color: #dddddd;"
			      		value='<c:out value="${webstart_client.featureId}"/>' readonly></td>
			      	<td>
			      		<select id="feature_id" name="feature_id">
			    			<option value="">(no change)</option>
			    				<c:forEach var="featureid" items="${featureids}">
			          			<option value='<c:out value="${featureid}"/>'><c:out value="${featureid}"/></option> 
				        	</c:forEach> 
			    		</select>
			      	</td>
			    </tr>
			    
			     <tr> 
			    	<th width="120" align="right">Phone Extension:</th>
			      	<td><input id="old_phone_extension" name="old_phone_extension"  style="background-color: #dddddd;"
			      		value='<c:out value="${webstart_client.phoneExtension}"/>' readonly></td>
			      	<td><input id="phone_extension" name="phone_extension" value='<c:out value="${webstart_client.phoneExtension}"/>' onclick="clearDefault()">
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
</HTML>