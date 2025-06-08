package com.honda.test.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;
import com.honda.test.common.ServletContainer;


public class DBUtils {
	
	public static final long ONE_DAY_IN_MILLISECS = 86400000;
	
	
	public static boolean readAndExecuteSqlFromFile(String filePath){
		return readAndExecuteSqlFromFile(filePath, "");
	}

	/**
	 * Reads sql queries one line at a time from the specified file and executes them
	 * 
	 * @param filePath
	 * @param vin
	 * @return
	 */
	public static boolean readAndExecuteSqlFromFile(String filePath, String vin) {
		BufferedReader br = null;
		InputStream is;
		ClassLoader cl = CreateTableUtil.class.getClassLoader();
		try {
			is = cl.getResourceAsStream(filePath);
			br = new BufferedReader(new InputStreamReader(is));
			
			String sqlQuery; 
			
			while((sqlQuery = br.readLine()) != null) { 						
				sqlQuery = sqlQuery.trim();
				if (sqlQuery.endsWith(";"))										
					sqlQuery = sqlQuery.substring(0, sqlQuery.length() -1);		// ignore semi-colons at the end of the sql statement
				
				if (!sqlQuery.equals("") && !sqlQuery.startsWith("--")) {		// ignore commented out sql statements
		        	sqlQuery = substituteVariables(vin, sqlQuery);
		        	getDataBaseBuildService().executeNativeUpdate(sqlQuery);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
			return false;
		} finally {
			try	{
				if(br != null) br.close();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return true;
	}
	
	public static DataBaseBuildService getDataBaseBuildService(){
		return ServiceFactory.getService(DataBaseBuildService.class);
	}
	
	

	/**
	 * substitutes all variables that start with '@'
	 * 
	 * @param vin
	 * @param sqlQuery
	 * @param conn
	 * @return
	 * @throws UnknownHostException
	 */
	private static String substituteVariables(String vin, String sqlQuery){
		Date tomorrow = new Date(new Date().getTime() + ONE_DAY_IN_MILLISECS);
    	return sqlQuery.replaceAll("@CURRENT_DAY@", StringUtil.dateToString(new Date(), "EEE").toUpperCase())
			.replaceAll("@CURRENT_DATE@", StringUtil.dateToString(new Date(), "yyyy-MM-dd"))
			.replaceAll("@TOMORROW_DAY@", StringUtil.dateToString(tomorrow, "EEE").toUpperCase())
			.replaceAll("@TOMORROW_DATE@", StringUtil.dateToString(tomorrow, "yyyy-MM-dd"))
			.replaceAll("@VIN@", vin);
	}
	
	public static String createTable(String tableName) {
		String sqlSuffix = (tableName.contains(".sql") || tableName.contains(".SQL"))? "" : ".sql";
		String sql ="";
		BufferedReader reader = null;
		InputStream is;
		
		ClassLoader cl = CreateTableUtil.class.getClassLoader();
		try {
			is = cl.getResourceAsStream(tableName +sqlSuffix);
			reader = new BufferedReader(new InputStreamReader(is));
			
			String line = null;
             do {
                line = reader.readLine();
                if(sql.isEmpty()) {
                	if(line != null && line.contains("CREATE TABLE")) {
                		sql = line;
               	}
                }else {
                	line = replaceNotNull(line);
                	sql += removeKeywords(line);
                }
                if(!sql.isEmpty()) {
                	int c1 = StringUtils.countOccurrencesOf(sql, "(");
                	int c2 = StringUtils.countOccurrencesOf(sql, ")");
                	if(c1 == c2 && c1!=0) {
                		int c3 = sql.lastIndexOf(")");
                		sql = sql.substring(0, c3+1);
                		int i1 = sql.indexOf("\"");
                		if(i1 >=0) {
                			int i2 = sql.indexOf("\"", i1 + 1);
                			String schema = sql.substring(i1 + 1, i2);
                			sql = sql.replace(schema, StringUtils.trimAllWhitespace(schema));
                		}
                		sql = sql.replace(" OFFSET ", " \"OFFSET\" ");
                		
  //              		sql = sql.replace("NOT NULL WITH DEFAULT","WITH DEFAULT NOT NULL");
                		sql = sql.replace("WITH ", "");
                    	
                		sql = sql.replace("NO CACHE", "");
                		sql = sql.replace("NO CYCLE", "");
                		sql = sql.replace("NO ORDER", "");
                		sql = sql.replace("OCTETS", "");
                		sql = sql.replace("LOGGED COMPACT", "");
                		sql = sql.replace("LOGGED NOT COMPACT", "");
                		sql = sql.replace("INLINE LENGTH 164","");
                		sql = sql.replace("XML", "VARCHAR(32768)");
                		return sql;
                	}
                	
                }
             }while (line != null);
	          
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try	{
				if(reader != null) reader.close();
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	private static String removeKeywords(String line) {
		if(line == null || line.isEmpty()) return line;
		
		String[] keywords = new String[] {
				"MINVALUE","MAXVALUE","CACHE","CYCLE","ORDER"
		};

		for(String item : keywords) {
			line = removeKeyword(line,item);
		}
			return line;
	}
	
	private static String removeKeyword(String line,String keyword)  {
		line = line.replace(", NO " + keyword + ",","");
		line = line.replace("NO " + keyword+",", "");
		line = line.replace("NO " + keyword, "");
	    
		if(!line.contains(keyword)) return line;
		
		String[] words = line.split(" ");
		int k = -1;
		for(int i = 0; i < words.length ; i++) {
			if(words[i].equalsIgnoreCase(keyword)) {
				k = i;
				break;
			}
		}
		
		if(k == -1) return line;
			
		line = line.replace(words[k], "");
		line = line.replace(words[k+1], "");
		return line;
	}
	
	private static String replaceNotNull(String line) {
		if(line == null || line.length() == 0) return line;
		
		if(!line.contains("DEFAULT")) return line;
		
		String value = null;
		if(line.contains("NOT NULL")) value = "NOT NULL";
		else if(line.contains("NULL")) value = "NULL";
		
		if(value == null) return line;
		
		line = line.replace(value, "");
		int index = line.indexOf("DEFAULT");
		String second = line.substring(index).trim();
		second = second.replace(",", " " +value +" ,");
		
		line = line.substring(0, index) + second;
		
			
		return line;
		
	}
	
	public static void loadConfig() {
		if(ApplicationContextProvider.getApplicationContext() != null) return;
		
		try {
			Enumeration<Driver> drivers = DriverManager.getDrivers();
			
			while(drivers.hasMoreElements()) {
				Driver driver = drivers.nextElement();
				if(driver instanceof org.h2.Driver) {
					DriverManager.deregisterDriver(driver);
				}
			}
		    DriverManager.registerDriver(new H2Driver());
		    drivers = DriverManager.getDrivers();
		     int i = 0;
		     i++;
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		ApplicationContextProvider.loadFromClassPathXml("applicationContext_h2.xml");

		String setDefaultSchema = "SET SCHEMA GALADM";
		
		DataBaseBuildService buildService = ServiceFactory.getService(DataBaseBuildService.class);
		buildService.executeNativeUpdate(setDefaultSchema);
		
		for(String tableName : getAllTableNames()) {
			String ddl = DBUtils.createTable(tableName);	
			System.out.println("tableName " + tableName + " ddl - " + ddl);
			buildService.executeNativeUpdate(ddl);
		}
		
		PropertyService.refreshComponentProperties("System_Info");
	}
	
	/*
	 * this starts httpServiceHandler and HttpDeviceHandler servlets
	 * so we can call dao / services through http calls from GALC clients
	 */
	public static void loadClientApplicationContext(int port) {
		ServletContainer.start(port);
		TestUtils.sleep(2000);

		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext_client.xml");
		HttpServiceProvider.setUrl("http://localhost:"+port+"/BaseWeb/HttpServiceHandler");
		ApplicationContextProvider.setClientApplicationContext(ctx);
	}
	
	public static void resetClientApplicationContext() {
		ServletContainer.stop();
		ApplicationContextProvider.setClientApplicationContext(null);
	}
	
	
	
	private static boolean exclude(String tableName) {
		String[] excludeList = {
				"GAL703TBXV.sql",
				"GAL704TBXV.sql",
				"GTS_LANE_CARRIER_TBX",
				"LETBPIDX1.sql",
				"LETTBSDAT1.sql",
				"LETTBSIDX1.sql",
				"LOT_TRACE_TBX.ddl",
				"MC_FOREIGN_KEYS.sql",
				"MC_TEST_SQL.sql",
				"MC_TEST_SQL_2SD.sql",
				"product_carrier.sql",
				"QI_GALC_TO_MDRS_TBV.sql",
				"QI_PRODUCT_TYPE_TBV.sql",
				"EQUIP_UNIT_FAULT.TBX.sql"
		};
		
		for (String name : excludeList) {
			if(tableName.equals(name)) return true;
		}
		
		return false;
	}
	
	private static List<String> getAllTableName1s() {
		List<String> tableNames = new ArrayList<String>();
		
		tableNames.addAll(DBTables.processTables.values());
		tableNames.addAll(DBTables.productTables.values());
		tableNames.addAll(DBTables.dataCollectionTables.values());
		tableNames.addAll(DBTables.trackingTables.values());
		tableNames.addAll(DBTables.engineShippingTables.values());
		tableNames.addAll(DBTables.gtsTables.values());
		return tableNames;
	}
	
	private static List<String> getAllTableNames() {
		List<String> tableNames = new ArrayList<>();
		ClassLoader cl = Thread.currentThread().getContextClassLoader(); 
		
		Enumeration<URL> urls = null;
		try {
			urls = cl.getResources("");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		URL url = null;
		while(urls.hasMoreElements()) {
			URL current = urls.nextElement();
			if(current.getPath().contains("REGIONAL")) {
				url = current;
				break;
			}
		}
		
		if(url != null) {
			try {
				File folder = new File(url.getPath().replace("01%20DDL/","01 DDL"));
				if(folder != null) {
					File[] files = folder.listFiles();
					
					for(File file : files) {
						if(!exclude(file.getName())) tableNames.add(file.getName());
					}
				}
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return tableNames;
	}
	
	public static void deleteAllTables() {
		 
		String DELETE_SQL = "DELETE FROM ";
		 
		 DataBaseBuildService buildService = ServiceFactory.getService(DataBaseBuildService.class);
			
		 for(String tableName : getAllTableNames()) {
			 tableName = tableName.replace(".sql", "");
			 tableName = tableName.replace(".SQL", "");
			 buildService.executeNativeUpdate(DELETE_SQL + tableName); 
		 }
		 
	}
	
	public static void resetData() {
	//	deleteAllTables();
		DataBaseBuildService buildService = ServiceFactory.getService(DataBaseBuildService.class);
		try {
			buildService.cleanAllData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PropertyService.reset();
		resetClientApplicationContext();
	}

	public static void loadConfigDb2() {
		ApplicationContextProvider.loadFromClassPathXml("applicationContext_db2.xml");

		String setDefaultSchema = "SET SCHEMA GALADM";
		DataBaseBuildService buildService = ServiceFactory.getService(DataBaseBuildService.class);
		buildService.executeNativeUpdate(setDefaultSchema);
		PropertyService.refreshComponentProperties("System_Info");
		
	}
	

}