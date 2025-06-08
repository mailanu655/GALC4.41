<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@page import="com.honda.galc.handheld.data.HandheldConstants"%>
<script type="text/javascript">
	var timeoutHandle, timeoutDuration;

	function submitSessionTimedOut() {
		document.getElementById("sessionTimedOutField").value = "<%=HandheldConstants.SESSION_TIMED_OUT%>";
		submitForm();	
		document.getElementById("sessionTimedOutField").value = "<%=HandheldConstants.SESSION_ACTIVE%>";
	}
	

	function submitForm() {
		document.forms["mainForm"].submit();
	}

	function setTimeoutFromSession(interval) {
		timeoutDuration = interval;
		setTimeoutDuration();
	}

	function setTimeoutDuration() {
		if (timeoutHandle != null)
			window.clearTimeout(timeoutHandle);
		timeoutHandle = window.setTimeout("submitSessionTimedOut()", (timeoutDuration * 1000) + 100);
	}

</script>
