<%@ page language="java" contentType= "application/x-java-jnlp-file" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.honda.galc.webstart.WebStartConstants" %>
<%
/*
* This file generates a dynamic jnlp file
*/
	response.setDateHeader("Last-Modified", 
		((java.util.Date)session.getAttribute(WebStartConstants.LAST_MODIFIED)).getTime());
	response.setIntHeader("Expires", -1);
%>
<?xml version="1.0" encoding="utf-8"?>
<jnlp spec="1.0+" codebase="<c:out value='${codebase}'/>" href="<c:out value='${codebase}webstart.jnlp'/>">
	<information>
		<title>GALC Client</title>
    	<vendor><c:out value='${target.buildId}'/> By Honda.</vendor>
    	<homepage href="<c:out value='${codebase}index.html'/>" />
    	<description>GALC Client (<c:out value='${target.buildId}'/>)</description>
    <icon href="pics/galc5.jpg"/>
    <icon kind="splash" href="pics/galcwebstart.jpg"/>
    <offline-allowed/>
    </information>

    <security>
    	<j2ee-application-client-permissions />
    	<all-permissions />
    </security>
    
    <update check="always" policy="always"/>
    
    <resources>
    	<j2se version="1.5+" initial-heap-size="${initial_heap_size}" max-heap-size="${max_heap_size}"/>
    	<c:forEach var="jar" items="${jar_files}">
    		<jar href='<c:out value="${target.url}/${jar}"/>'/>
    	</c:forEach>
    	<c:forEach var="nativeJar" items="${native_jar_files}">
    		<nativelib href='<c:out value="${target.url}/${jar}"/>'/>
    	</c:forEach>
    </resources>

    <application-desc main-class='<c:out value="${main_class}"/>'>
		   <c:forEach var="arg" items="${jnlp_args}">
		   		<argument><c:out value="${arg}"/></argument> 
		   </c:forEach>
    </application-desc>
</jnlp>

