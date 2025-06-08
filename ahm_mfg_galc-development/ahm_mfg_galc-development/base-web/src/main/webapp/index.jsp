<%@ page import="com.honda.galc.buildlevel.BuildLevelUtility" %>
<html>
<head><title>GALC</title></head>
<body>
<style> 
  body {
    font-family: Arial, Helvetica, sans-serif;
    font-size: 12px;
    background: #E6E6E6;
  }
  #header {
     margin-left: 55px ;
  }
  #panel {
     margin: 55px ;
     border:3px solid gray;
     padding:10px;
  }
</style>
<div id="header">
   <h1>GALC</h1>
</div>
<div id="panel">
  <h2>Build</h2>
  <h3>Version:&nbsp;&nbsp;<%= BuildLevelUtility.getBuildComment()%></h3>
  <h3>Date:&nbsp;&nbsp;<%= BuildLevelUtility.getBuildTimestamp()%></h3>
</div>
<div id="panel">
  <h2>Links</h2>
  <ul>
    <li><a href="/BaseWeb/status.jsp">Application Status</a></li>
    <li><a href="/BaseWeb/HttpServiceHandler">HTTP Service Handler</a></li>
    <li><a href="/BaseWebStart">Web Start</a></li>
    <li><a href="/ConfigService">Configurator</a></li>
    <li><a href="/BaseWeb/RefreshProperties">Refresh Properties</a></li>
    <li><a href="/oifConfig">OIF Configuration</a></li>
  </ul>
</div>
</body>
</html>
