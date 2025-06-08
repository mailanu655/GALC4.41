<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/galc.css" />
<title>Event Status</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
<jsp:useBean id="currentTime" class="java.util.Date" />
<c:set var="dateFmt" value="MMM-dd HH:mm:ss" />
<div align="center">
<h3>Scheduler: <c:out value="${scheduler}" /></h3>
<h4>Current Time: <fmt:formatDate value="${currentTime}"
	pattern="${dateFmt}" /></h4>
<table border="1" bordercolor="black" cellspacing="0" cellpadding="4"
	style="font-size: 12px;">
	<tr>
		<th>Event Processing Server</th>
		<th>Expire</th>
		<th>Disabled</th>
	</tr>
	<c:forEach var="lease" items="${leases}">
		<tr>
			<td><b><c:out value="${lease.owner}" /></b></td>
			<td><fmt:formatDate value="${lease.expire}" pattern="${dateFmt}" /></td>
			<td><c:out value="${lease.disabled}" /></td>
		</tr>
	</c:forEach>
</table>
<br>
<table border="1" bordercolor="black" cellspacing="0" cellpadding="4"
	style="font-size: 12px;">
	<tr>
		<th>ID</th>
		<th>Event</th>
		<th>Task</th>
		<th>Create Time</th>
		<th>Calendar</th>
		<th>Schedule</th>
		<th>Next Fire Time</th>
		<th>Status</th>
		<th>Avg Time (ms)</th>
	</tr>
	<c:forEach var="event" items="${events}">
		<tr>
			<td><c:out value="${event.taskId}" /></td>
			<td><c:out value="${event.name}" /></td>
			<td><c:out value='${eventTasks[event.name]}' /></td>
			<td><fmt:formatDate value="${event.createTime}"
				pattern="${dateFmt}" /></td>
			<td><c:out value="${event.userCalendarSpecifier}" /></td>
			<td><c:out value="${event.repeatInterval}" /></td>
			<td><fmt:formatDate value="${event.nextFireTime}"
				pattern="${dateFmt}" /></td>
			<c:choose>
				<c:when test="${currentTime gt event.nextFireTime}">
					<c:choose>
						<c:when
							test="${event.status eq 5 and statistics[event.name].avgExecutionTime +  event.nextFireTime.time > currentTime.time}">
							<c:set var="color" value="#7FFF00" />
						</c:when>
						<c:otherwise>
							<c:set var="color" value="#FF0000" />
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<c:set var="color" value="#FFFFFF" />
				</c:otherwise>
			</c:choose>
			<td bgcolor="${color}">${statusMap[event.status]}</td>
			<td align="right"
				title="Exec Times (ms):${statistics[event.name].executionTimes}">&nbsp;${statistics[event.name].avgExecutionTime}</td>
		</tr>
	</c:forEach>
</table>

<h4><a href="schedule.do">Back</a></h4>
</div>
</body>
</html>
