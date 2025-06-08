<%@ page language="java" session="false" import="javax.naming.InitialContext,javax.sql.DataSource,java.sql.*" %>

<%
    response.setHeader("Cache-Control","no-store"); //HTTP 1.1
    response.setHeader("Pragma","no-cache"); //HTTP 1.0
    response.setDateHeader ("Expires", 0); //prevents caching at the proxy server

    String SQL_STR = "SELECT * FROM GAL117TBX WITH UR";
    String hostname = "*UNKNOWN*";
    String wcStatus = "successful";
    String dbStatus = "failed";
    String jmsStatus = "failed";
    String recs = "-1";
    long recsTime = -1;

    InitialContext ic = new InitialContext();
    String errmsg = "NONE";

    ///////////////////////////////////////
    // Get the current server hostname
    ///////////////////////////////////////
    try {
        hostname =  java.net.InetAddress.getLocalHost().getHostName();
    } catch (Exception e) { }

    ///////////////////////////////////////
    // Test connection to database
    ///////////////////////////////////////
    DataSource ds = null;
    Connection con = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
        long startTime = (new Date()).getTime();
        ds = (DataSource) ic.lookup("jdbc/galdb-ds5");
        con = ds.getConnection();
        stmt = con.createStatement();
        rs = stmt.executeQuery(SQL_STR);
        while(rs.next()) {
            dbStatus = "successful";
            recs = rs.getString(1);
            recsTime = ((new Date()).getTime()) - startTime;
        }
    } catch (Throwable e) {
        e.printStackTrace();
        errmsg = e.getMessage();
        dbStatus = "failed";
    } finally {
        if( rs != null ) { try{ rs.close();} catch (Throwable e) { } }
        if( stmt != null ) { try{ stmt.close();} catch (Throwable e) { } }
        if( con != null ) { try{ con.close();} catch (Throwable e) { } }
    }

    String status = "failed";
    if(wcStatus.indexOf("successful")>-1 
    && dbStatus.indexOf("successful")>-1)
	{
        status = "successful";
    }
    
    String dbStatusAll="failed";    
    if (dbStatus.indexOf("successful")>-1 )
	{
		dbStatusAll="successful";
	}

%>

<%@page import="java.util.Date"%>
<html>
<head>
  <title>GALC Application Status</title>
  <META HTTP-EQUIV="Refresh" CONTENT="10" URL=".">
</head>
<body>
    <h2>GALC Status: <%=status%></h2>
    <li><b>Web Container:</b>  <%= wcStatus %>
    <li><b>Database Access:</b> <%= dbStatusAll %>
    <li><b>Database Record (PROD):</b>  SQL: <%= SQL_STR %> -> [FIELD:SITE] -> <%= recs %> -> [QUERY TIME]: <%= recsTime %>
    <li><b>Hostname:</b> <%= hostname%>
    <li><b>Date:</b> <%= new java.util.Date() %>
<hr>
    Errors:  <%=errmsg%>
</body>
</html>
