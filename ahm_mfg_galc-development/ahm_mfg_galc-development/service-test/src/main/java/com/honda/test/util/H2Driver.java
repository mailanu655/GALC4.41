package com.honda.test.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.h2.Driver;
import org.h2.message.DbException;
import org.h2.upgrade.DbUpgrade;

public class H2Driver extends Driver{
	
	 
	
	@Override
    public Connection connect(String url, Properties info) throws SQLException {
        try {
            if (info == null) {
                info = new Properties();
            }
            if (!acceptsURL(url)) {
                return null;
            }
            Connection c = DbUpgrade.connectOrUpgrade(url, info);
            if (c != null) {
                return c;
            }
            return new H2JdbcConnection(url, info);
        } catch (Exception e) {
            throw DbException.toSQLException(e);
        }
    }

}
