@CHARSET "UTF-8";

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="sitenav" type="com.ibm.etools.siteedit.sitelib.core.SiteNavBean" scope="request"/>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<title>horizontal-text</title>
<link rel="stylesheet" type="text/css" href="theme/horizontal-text.css">
</head>
<body>
<div><b> [ </b>
<c:forEach var="item" items="${sitenav.items}" begin="0" step="1" varStatus="status">
 <c:choose>
  <c:when test="${item.group}">
   <c:if test="${!status.first}">
    <b> || </b>
   </c:if>
   <span class="htext_item_group"><c:out value='${item.label}' escapeXml='false'/></span>
  </c:when>
  <c:otherwise>
   <c:if test="${!status.first}">
    <b> | </b>
   </c:if>
   <c:out value='<a href="${item.href}" class="htext_item_normal">${item.label}</a>' escapeXml='false'/>
  </c:otherwise>
 </c:choose>
</c:forEach>
<b> ] </b></div>
</body>
</html>
