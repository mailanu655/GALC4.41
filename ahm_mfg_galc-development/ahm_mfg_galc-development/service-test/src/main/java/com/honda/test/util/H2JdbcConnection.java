package com.honda.test.util;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.h2.engine.ConnectionInfo;
import org.h2.jdbc.JdbcConnection;

public class H2JdbcConnection extends JdbcConnection{

	public H2JdbcConnection(ConnectionInfo ci, boolean useBaseDir) throws SQLException {
		super(ci, useBaseDir);
	}
	
	public H2JdbcConnection(String url, Properties info) throws SQLException {
	        super(url,info);
	}
	
	@Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
		
		sql = processSql(sql);
		
		return super.prepareStatement(sql, resultSetType, resultSetConcurrency);
    
	}
	
	@Override
    public CallableStatement prepareCall(String sql) throws SQLException {
		sql = processSql(sql);
		return super.prepareCall(sql);
	}
	
	
	public String processSql(String sql) {
		sql = sql.replace("for read only", "");
		sql = sql.replaceAll("FOR READ ONLY", "");
		return sql;
	}
}
