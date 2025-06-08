package com.honda.test.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.util.StringUtils;


public class CreateTableUtil {
	public static String createTable(String tableName) {
		String sql ="";
		ClassLoader cl = CreateTableUtil.class.getClassLoader();
		try {
			InputStream is = cl.getResourceAsStream(tableName +".sql");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			try {
				 String line = null;
	             do {
	                line = reader.readLine();
	                if(sql.isEmpty()) {
	                	if(line != null && line.contains("CREATE TABLE")) {
	                		sql = line;
	                	}
	                }else {
	                	sql += replaceNotNull(line);
	                }
	                if(!sql.isEmpty()) {
	                	int c1 = StringUtils.countOccurrencesOf(sql, "(");
	                	int c2 = StringUtils.countOccurrencesOf(sql, ")");
	                	if(c1 == c2) {
	                		int c3 = sql.lastIndexOf(")");
	                		sql = sql.substring(0, c3+1);
	                		int i1 = sql.indexOf("\"");
	                		if(i1 >=0) {
	                			int i2 = sql.indexOf("\"", i1 + 1);
	                			String schema = sql.substring(i1 + 1, i2);
	                			sql = sql.replace(schema, StringUtils.trimAllWhitespace(schema));
	                			sql = sql.replace("WITH ", "");
	                		}
	                		return sql;
	                	}
	                	
	                }
	             }while (line != null);
	          }catch(IOException e) {
	        	
	          }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String replaceNotNull(String line) {
		if(line == null || line.length() == 0) return line;
		int index = line.indexOf("NOT NULL");
		if(index >= 0) {
			line = line.replace("NOT NULL", "");
			line = line.replace(",", " NOT NULL ,");
		}else {
			index = line.indexOf("NULL");
			if(index >= 0) {
				line = line.replace("NULL", "");
				line = line.replace(",", " NULL ,");
			}
		}
		
		return line;
		
	}
}
