<%@ page import="com.honda.mfg.stamp.conveyor.domain.Carrier" %>
<%@ page import="com.honda.mfg.stamp.conveyor.domain.Lane" %>
<%@ page import="com.honda.mfg.stamp.conveyor.manager.StorageState" %>
<%@ page import="com.honda.mfg.stamp.storage.web.StorageStateController" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="sstateController" class="com.honda.mfg.stamp.storage.web.StorageStateController"></jsp:useBean> %>
<html>
<head><title>Current Storage State</title></head>
<%
    String uri = "n/a";
    String contextPath = request.getContextPath();
    Object obj = request.getParameter("refresh");
    if (obj != null) {
        StorageStateController.refreshStorageState();
        uri = request.getRequestURI();
        if (uri != null && uri.indexOf("?") > -1) {
            uri = uri.substring(0, uri.indexOf("?"));
        }
        response.sendRedirect(uri);
    }
    StorageState state = sstateController.getStorageState();
%>
<body>
Page Load Date: <%=(new Date())%><br>
<a href="<%=contextPath%>">Return to main menu</a><br>
<a href="?refresh=true">Refresh Storage State</a><br>
LEGEND:  CarrierNumber/Quantity/Die/Buffer<br><br>
<%
    for (Lane lane : state.queryForLanes(null)) {
        int currentCapacity = lane.getCurrentCarrierCount();
        int maxCapacity = lane.getMaxCapacity();
%>
<%=lane.getLaneName()%> &nbsp; <%=currentCapacity%>/<%=maxCapacity%>
<%
    for (Carrier carrier : lane.getCarriers()) {
%>
<li>Carrier: <%=carrier.getCarrierNumber()%>/<%=carrier.getQuantity()%>/<%=carrier.getDie().getId()%>/<%=carrier.getBuffer()==null?"0":carrier.getBuffer()%>
        <%
    }
%>
    <br>
        <%
    }
%>

    FORM = <%=request.getParameter("form")%>
    <br>
</body>
</html>