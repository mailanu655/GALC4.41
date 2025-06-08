<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.honda.galc.common.logging.Logger"%>
<%@ page import="com.honda.galc.dao.conf.MetricGroupDao"%>
<%@ page import="com.honda.galc.dao.conf.MetricGroupTypeDao"%>
<%@ page import="com.honda.galc.dao.conf.MetricGroupTypeParamDao"%>
<%@ page import="com.honda.galc.entity.conf.MetricGroup"%>
<%@ page import="com.honda.galc.entity.conf.MetricGroupType"%>
<%@ page import="com.honda.galc.entity.conf.MetricGroupTypeParam"%>
<%@ page import="com.honda.galc.service.ServiceFactory"%>
<%@ page import="java.math.BigInteger"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.LinkedList"%>
<%@ page import="java.util.List"%>
<html>
<%
	response.setIntHeader("Refresh", 60);
%>
<head>
<title>Table Data Monitoring</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
<center>
<h2>Table Data Monitoring</h2>
<table width="75%" border="1" cellpadding="1" cellspacing="0">
	<%!
		private class Limits {
			private BigInteger failureLimit;
			private BigInteger warningLimit;
			
			private Limits(BigInteger failureLimit, BigInteger warningLimit) {
				this.failureLimit = failureLimit;
				this.warningLimit = warningLimit;
			}
		}
	%>

	<%
		long executionStartTime = System.currentTimeMillis();
	
		Date startTime;
		BigInteger failureLimit, warningLimit, tempBigInteger, timeRemaining;
		List<MetricGroup> failureMetricGroups = null;
		List<MetricGroup> warningMetricGroups = null;
		Map<BigInteger, String> metricGroupTypes = new HashMap<BigInteger, String>();
		MetricGroupTypeDao metricGroupTypeDao;
		
		try {						
			final String DISPLAY_TYPE = "TABLE_DATA";
			final String PARAM_NAME_FAIL_SUB = "FAILURE_THRESHOLD_MSEC";
			final String PARAM_NAME_WARN_SUB = "WARNING_THRESHOLD_MSEC";
	
			metricGroupTypeDao = ServiceFactory.getDao(MetricGroupTypeDao.class);

			Limits tempLimits;
			List<MetricGroup> metricGroups = ServiceFactory.getDao(MetricGroupDao.class).findAllActiveOrdered();
			List<MetricGroupTypeParam> metricGroupTypeParams;
			Map<BigInteger, Limits> metricGroupTypeParamLimits = new HashMap<BigInteger, Limits>();
			MetricGroupTypeParamDao metricGroupTypeParamDao = ServiceFactory.getDao(MetricGroupTypeParamDao.class);	
			String tempString1, tempString2;
			
			failureMetricGroups = new LinkedList<MetricGroup>();
			warningMetricGroups = new LinkedList<MetricGroup>();
			
			for (MetricGroup metricGroup : metricGroups) {
				if (DISPLAY_TYPE.equals(metricGroup.getDisplayType())) {
					tempBigInteger = metricGroup.getMetricGroupTypeId();
					if(!metricGroupTypeParamLimits.containsKey(tempBigInteger)) {
						metricGroupTypeParams = metricGroupTypeParamDao.findAllByMetricGroupTypeId(tempBigInteger);
						failureLimit = warningLimit = null;
						for(MetricGroupTypeParam metricGroupTypeParam : metricGroupTypeParams) {
							tempString1 = metricGroupTypeParam.getMetricGroupTypeParamName();
							tempString2 = metricGroupTypeParam.getMetricGroupTypeParamValue();
							if(tempString1 != null && tempString2 != null) {
								if(tempString1.contains(PARAM_NAME_WARN_SUB))
									warningLimit = new BigInteger(tempString2);
								else if(tempString1.contains(PARAM_NAME_FAIL_SUB))
									failureLimit = new BigInteger(tempString2);
							}
						}
						metricGroupTypeParamLimits.put(tempBigInteger, new Limits(failureLimit, warningLimit));
					}
					if(metricGroupTypeParamLimits.containsKey(tempBigInteger)) {						
						tempLimits = metricGroupTypeParamLimits.get(tempBigInteger);
						tempBigInteger = metricGroup.getTimeRemainingMsec();
						failureLimit = tempLimits.failureLimit;
						warningLimit = tempLimits.warningLimit;
												
						if(tempBigInteger != null) {
							if(failureLimit != null && tempBigInteger.compareTo(failureLimit) == -1)
								failureMetricGroups.add(metricGroup);
							else if(warningLimit != null && tempBigInteger.compareTo(warningLimit) == -1)
								warningMetricGroups.add(metricGroup);
						}
						
						tempBigInteger = metricGroup.getMetricGroupTypeId();
						if(!metricGroupTypes.containsKey(tempBigInteger))
							metricGroupTypes.put(tempBigInteger, metricGroupTypeDao.findByKey(tempBigInteger).getMetricGroupTypeName());
					}
				}
			}
		}
		catch (Exception e) {
			Logger.getLogger().error("JSP data initialization error");
		}
	%>
	<tr>
		<th width=5%>&nbsp;Status&nbsp;</th>
		<th width=25%>&nbsp;Type&nbsp;</th>
		<th width=35%>&nbsp;Event&nbsp;</th>
		<th width=5%>&nbsp;Priority&nbsp;</th>
		<th width=15%>&nbsp;Remaining&nbsp;</th>
		<th width=15%>&nbsp;Duration&nbsp;</th>
	</tr>
	<%
		for (MetricGroup failureMetricGroup : failureMetricGroups) {
			tempBigInteger = failureMetricGroup.getMetricGroupTypeId();
			startTime = failureMetricGroup.getStartTime();
			timeRemaining = failureMetricGroup.getTimeRemainingMsec();
	%>
	<tr align="center">
		<td><img src="pics/failure_status_indicator.gif" align="middle" border="0" title="FAILURE"></td>
		<td><%=tempBigInteger != null ? metricGroupTypes.get(tempBigInteger) : tempBigInteger%></td>
		<td><%=failureMetricGroup.getMetricGroupDescription()%></td>
		<td><%=failureMetricGroup.getPriority()%></td>
		<td><%=timeRemaining != null ? formatTime(timeRemaining.longValue()) : null%></td>
		<td><%=startTime != null ? formatTime(System.currentTimeMillis() - startTime.getTime()) : null%></td>
	</tr>
	<%
		}
	%>
	<%
		for (MetricGroup warningMetricGroup : warningMetricGroups) {
			tempBigInteger = warningMetricGroup.getMetricGroupTypeId();
			startTime = warningMetricGroup.getStartTime();
			timeRemaining = warningMetricGroup.getTimeRemainingMsec();
	%>
	<tr align="center">
		<td><img src="pics/warning_status_indicator.gif" align="middle" border="0" title="WARNING"></td>
		<td><%=tempBigInteger != null ? metricGroupTypes.get(tempBigInteger) : tempBigInteger%></td>
		<td><%=warningMetricGroup.getMetricGroupDescription()%></td>
		<td><%=warningMetricGroup.getPriority()%></td>
		<td><%=timeRemaining != null ? formatTime(timeRemaining.longValue()) : null%></td>
		<td><%=startTime != null ? formatTime(System.currentTimeMillis() - startTime.getTime()) : null%></td>
	</tr>
	<%
		}
	%>

	<%!
		String formatTime(long timeMsec) {
			final long MSEC_PER_DAY = 86400000;
			final long MSEC_PER_HR  = 3600000;
			final long MSEC_PER_MIN = 60000;
			final long MSEC_PER_SEC = 1000;
			
			String formattedTime = null;
	
			if (timeMsec < 0)
				formattedTime = "0d 0h 0m 0s";
			else {
				formattedTime = timeMsec / MSEC_PER_DAY + "d "
						+ (timeMsec %= MSEC_PER_DAY) / MSEC_PER_HR + "h "
						+ (timeMsec %= MSEC_PER_HR) / MSEC_PER_MIN + "m "
						+ (timeMsec %= MSEC_PER_MIN) / MSEC_PER_SEC + "s";
			}
	
			return formattedTime;
		}
	%>
</table>
<p>
	<%="Failures: " + failureMetricGroups.size()%><br/>
	<%="Warnings: " + warningMetricGroups.size()%><br/>
	<%="Load time: " + ((System.currentTimeMillis() - executionStartTime) / 1000) + " seconds"%>
</p>
</center>
</body>
</html>
