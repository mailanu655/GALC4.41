<%@ page language="java" contentType= "application/x-java-jnlp-file" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.honda.galc.webstart.WebStartConstants" %>
<%@ page import="com.honda.galc.service.property.PropertyService" %>
<%@ page import="com.honda.galc.webstart.WsClientConfig" %>
<%@ page import="com.honda.galc.entity.conf.WebStartClient" %>
<%@ page import="com.honda.galc.entity.conf.WebStartBuild" %>
<%@ page import="com.honda.galc.webstart.WebStartConfiguration" %>
<%
/*
* This file generates a dynamic jnlp file
*/	
	String hostName = request.getServletPath().replace("/","").replace(".host", "");
	boolean isLinux = request.getHeader("User-Agent").indexOf("JNLP") > -1
				|| request.getHeader("User-Agent").indexOf("Linux") > -1;
	PropertyService.refreshComponentProperties(hostName);
	WsClientConfig clientConfig = WebStartConfiguration.getInstance().getClientConfig(hostName, isLinux);
	//System.out.println(clientConfig);
	WebStartClient aClient = clientConfig.getWsClient();
	WebStartBuild targetBuild = clientConfig.getBuild();		

    String port = request.getServerPort()+"";
    String baseUrlWithPort = null;
    if (port!=null && port.length()>0){
        baseUrlWithPort = "http://" + request.getServerName() + ":"+ request.getServerPort();
    }else{
    	baseUrlWithPort = "http://" + request.getServerName();
    }
    String baseUrlWithOutPort = "http://" + request.getServerName();
    
%>
<?xml version="1.0" encoding="utf-8"?>

<%@page import="java.util.ArrayList"%><jnlp spec="1.0+" codebase="<%=baseUrlWithPort%>/BaseWebStart/" >
	<information>
		<title>GALC Client</title>
    	<vendor><%=targetBuild.getBuildId()%> By Honda.</vendor>
    	<homepage href="<%=baseUrlWithPort%>/BaseWebStart" />
    	<description>GALC Client <%=targetBuild.getBuildId()%></description>
        <icon href="pics/galc5.jpg"/>
        <icon kind="splash" href="pics/galcwebstart.jpg"/>
        <shortcut online="false" install="false">
        </shortcut>
        <offline-allowed/>
    </information>

    <security>
    	<j2ee-application-client-permissions />
    	<all-permissions />
    </security>
    
    <update check="always" policy="always"/>
    
    <resources>
      	<j2se version="1.5+" initial-heap-size="<%=clientConfig.getInitialHeapSize()%>" max-heap-size="<%=clientConfig.getMaxHeapSize()%>"/>
	   	<c:forEach var="jar" items="<%=clientConfig.getJars()%>">
    		<jar href='<%=targetBuild.getUrl()%><c:out value="/${jar}"/>'/>
    	</c:forEach>
    	<c:forEach var="nativeJar" items="<%=clientConfig.getNativeJars()%>">
    		<nativelib href='<%=targetBuild.getUrl()%><c:out value="/${nativeJar}"/>'/>
    	</c:forEach>
    </resources>

    <application-desc main-class="<%=clientConfig.getMainClass()%>">
    	<c:forEach var="arg" items="<%=clientConfig.getArgs()%>">
    		<argument><c:out value="${arg}"/></argument>
    	</c:forEach>
   </application-desc>
</jnlp>

