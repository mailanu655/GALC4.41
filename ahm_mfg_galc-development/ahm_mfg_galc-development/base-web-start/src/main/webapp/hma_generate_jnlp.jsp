<%@ page language="java" contentType= "application/x-java-jnlp-file" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.honda.galc.webstart.WebStartConstants" %>
<%
/*
* This file generates a dynamic jnlp file
*/
    long now = System.currentTimeMillis();
	response.setDateHeader("Last-Modified",now);
	response.setDateHeader("Expires",0);
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Pragma","no-cache");
%>
<?xml version="1.0" encoding="utf-8"?>
<jnlp spec="1.0+" codebase="<c:out value='${codebase}'/>" href="<c:out value='${codebase}${terminal_id}/${application}/Production.jnlp'/>">
	<information>
		<title>Lot Control</title>
    	<vendor>Lot Control Build - Build No. (<c:out value='${target.buildId}'/>)</vendor>
    	<homepage href="<c:out value='${codebase}index.html'/>" />
    	<description>Lot Control - Build No. (<c:out value='${target.buildId}'/>)</description>
    <icon kind='shortcut' href="pics/lc_032.png"/>
    <%-- icon kind="splash" href="pics/lc_splash.jpg"/ --%>

    <shortcut online="true">
        <desktop/>
       	<menu submenu="Startup"/>
    </shortcut>
    </information>

    <security>
    	<j2ee-application-client-permissions />
    	<all-permissions />
    </security>
 
    
    <resources>
    	<j2se version="1.5+" initial-heap-size="${initial_heap_size}" max-heap-size="${max_heap_size}"/>
	   	<c:forEach var="jar" items="${jar_files}">
    		<jar href='<c:out value="${target.url}/${jar}"/>'/>
    	</c:forEach>
    </resources>

    <application-desc main-class='<c:out value="${main_class}"/>'>
		   <c:forEach var="arg" items="${jnlp_args}">
		   		<argument><c:out value="${arg}"/></argument> 
		   </c:forEach>
    </application-desc>
</jnlp>

