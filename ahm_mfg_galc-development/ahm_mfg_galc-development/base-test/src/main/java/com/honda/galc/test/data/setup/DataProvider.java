/**
 * 
 */
package com.honda.galc.test.data.setup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Subu Kathiresan
 * @date Jan 26, 2012
 */
public class DataProvider {

	private static final long ONE_DAY_IN_MILLISECS = 86400000;
	private static final String dbDriver = "com.ibm.db2.jcc.DB2Driver";
	
	private String connectionUrl = "";
	private String dbUserId = "";
	private String encryptedDbPassword = "";
	private boolean logSql = true;
	
	public Connection conn = null;
	public Environment env = Environment.HMIN_SBX;
	
	static {
		try {
			Class.forName(dbDriver);		// load database driver
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
	}
	
	public DataProvider(Environment env, boolean logSql) {
		this.env = env;
		this.logSql = logSql;
		switch (env) {
		case LOCAL:
			//connectionUrl = "jdbc:db2://galchminline1dbsb.hmin.am.honda.com:60005/GALCDBSB";
			//dbUserId = "galcdbsb";
			//encryptedDbPassword = "VTCV2914rCEaZs/n4coLNw==";
			connectionUrl = "jdbc:db2://galchminline1dbdev.hmin.am.honda.com:60000/GALCDBDV";
			dbUserId = "hmigaldv";
			encryptedDbPassword = "IYpFMroyYIgaZs/n4coLNw==";
			break;
		case HMIN_SBX:
			connectionUrl = "jdbc:db2://galchminline1dbsb.hmin.am.honda.com:60005/GALCDBSB";
			dbUserId = "galcdbsb";
			encryptedDbPassword = "VTCV2914rCEaZs/n4coLNw==";
			break;
		case HMIN_DEV:
			connectionUrl = "jdbc:db2://galchminline1dbdev.hmin.am.honda.com:60000/GALCDBDV";
			dbUserId = "hmigaldv";
			encryptedDbPassword = "IYpFMroyYIgaZs/n4coLNw==";
			break;
		case HMIN_QA:
			connectionUrl = "jdbc:db2://galchminline1dbqa.hmin.am.honda.com:60010/GALCDBQA";
			dbUserId = "hmigalqa";
			encryptedDbPassword = "6LKKDWgiwYTukNgpqN0/Dw==";
			break;
		case HCL_QA:
			connectionUrl = "jdbc:db2://thcl1db1.hdm.am.honda.com:60004/QGAL1DB";
			dbUserId = "gal01";
			encryptedDbPassword = "IWCJahC/PNk=";
			break;	
		case HMA_LOCAL:
			connectionUrl = "jdbc:db2://dhma1db1:60012/GAL1DBQA";
			dbUserId = "galadm";
			encryptedDbPassword = "Dv5k62GAvBI=";
			break;	
		case HMA_DEV:
			connectionUrl = "jdbc:db2://dhma1db1.hma.am.honda.com:60000/GAL1DB";
			dbUserId = "galadm";
			encryptedDbPassword = "Dv5k62GAvBI=";
			break;		
		case HMA_QA:
			connectionUrl = "jdbc:db2://dhma1db1.hma.am.honda.com:60012/GAL1DBQA";
			dbUserId = "galadm";
			encryptedDbPassword = "Dv5k62GAvBI=";
			break;
		default:
			break;
		}
	}
	
	/**
	 * @return the database connection
	 */
	public Connection getConn() throws SQLException {
		if (conn == null || conn.isClosed()) {
			conn = DriverManager.getConnection(connectionUrl, dbUserId, DecryptString(encryptedDbPassword));
			conn.setAutoCommit(true);
		}
		return conn;
	}
	
	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public boolean readAndExecuteSqlFromFile(String filePath) {
		FileReader fr = null;
		BufferedReader br = null;
		
		try {
			try {
				fr = new FileReader(filePath);
			} catch(Exception ex) {
				System.out.println("Skipping SQL file execution: " + filePath + " Error: " + ex.getLocalizedMessage());
				return false;
			}
			br = new BufferedReader(fr); 
			String sqlQuery; 
			Date tomorrow = new Date(new Date().getTime() + ONE_DAY_IN_MILLISECS);
			
			while((sqlQuery = br.readLine()) != null) { 
				sqlQuery = sqlQuery.trim();
				if (!sqlQuery.equals("") && !sqlQuery.startsWith("--"))	{
					if (sqlQuery.endsWith(";")) {
						sqlQuery = sqlQuery.substring(0, sqlQuery.length() -1);
					}
		        	sqlQuery = sqlQuery.replaceAll("@CURRENT_DAY@", dateToString(new Date(), "EEE").toUpperCase())
		        						.replaceAll("@CURRENT_DATE@", dateToString(new Date(), "yyyy-MM-dd"))
		        						.replaceAll("@TOMORROW_DAY@", dateToString(tomorrow, "EEE").toUpperCase())
		        						.replaceAll("@TOMORROW_DATE@", dateToString(tomorrow, "yyyy-MM-dd"));
		        	
		        	Statement stmt = getConn().createStatement();
		        	try {
		        		stmt.executeUpdate(sqlQuery);
		        		if (logSql) {
		        			System.out.println("Executed: " + sqlQuery);
		        			System.out.println("Rows affected: " + stmt.getUpdateCount());
		        		}
		        		stmt.close();
		        	} catch(Exception ex) {
		        		System.out.println("unable to Execute: " + sqlQuery);
		        		ex.printStackTrace();
		        		break;
		        	} finally {
		        		try {
		        			getConn().commit();
		        		} catch(Exception ex) {}
		        	}
				}
			}
			System.out.println("SUCCESS: Data setup completed!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR  : Data setup incomplete!");
			return false;
		} finally {
			try {
				if (conn != null && !conn.isClosed())
					conn.close();
			} catch(Exception ex) {}
			try {
				fr.close();
			} catch(Exception ex) {}
			try {
				br.close();
			} catch(Exception ex) {}
		}
		return true;
	}
	
	/**
	 * 
	 * @param sql
	 * @return
	 */
	public int execGetCountSql(String sql) {
		try {
			sql = sql.trim();
			Statement stmt = getConn().createStatement();
		    ResultSet rs = stmt.executeQuery(sql);
		    int count = 0;
		    while (rs.next()) {
		    	count = rs.getInt(1);
		    }
		    stmt.close();
				
		    if (logSql) {
		    	System.out.println("SUCCESS: " + sql);
		    	System.out.println("ResultSet count: " +  count);
		    }
		    
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR  : " + sql);
		} finally {
			try {
				if (conn != null && !conn.isClosed())
					conn.close();
			} catch(Exception ex) {}
			
		}
		return 0;
	}
	
	public String execSelectSql(String sql) {
		String retVal = "";
		try {
			sql = sql.trim();
			Statement stmt = getConn().createStatement();
		    ResultSet rs = stmt.executeQuery(sql);
		    while (rs.next()) {
		    	retVal = rs.getString(1);
		    	break;
		    }
		    stmt.close();
				
		    if (logSql) {
		    	System.out.println("SUCCESS: " + sql);
		    	System.out.println("Result returned: " +  retVal);
		    }
			return retVal;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR  : " + sql);
		} finally {
			try {
				if (conn != null && !conn.isClosed())
					conn.close();
			} catch(Exception ex) {}
			
		}
		return retVal;
	}
	
	/**
	 * 
	 * @param strToDecrypt
	 * @return
	 */
	private static String DecryptString(String strToDecrypt) {
		StringEncrypter encrypter = null;
		try	{
			encrypter = new StringEncrypter("DES");
			return encrypter.decrypt(strToDecrypt);
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();		
		}
		return "Decryption error";
	}

	/**
	 * Converts a Date to String
	 *
	 * @param date 			The date to convert
	 * @param dateFormat	Date format to use for conversion
	 * @return 				A string representing the date
	 */
	public static String dateToString(Date date, String dateFormat)	{
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}
		
	public String getLinkedVin(String vin) {
		return execSelectSql("select next_product_id from galadm.gal176tbx where product_id = '" + vin + "'");
	}
	
	public int getLinkedListCount(String lineId) {
		return execGetCountSql("select count(*) from galadm.gal176tbx where line_id = '" + lineId + "'");
	}
	
	public int getTailCount(String lineId) {
		return execGetCountSql("select count(*) from galadm.gal176tbx where line_id = '" + lineId + "' and (next_product_id is null or next_product_id = '')");
	}
	
	public String getTailProductId(String lineId) {
		return execSelectSql("select product_id from galadm.gal176tbx where line_id = '" + lineId + "' and next_product_id is null for read only").trim();
	}
	
	public String getTrackingStatus(String productId) {
		return execSelectSql("select tracking_status from galadm.gal143tbx where product_id = '" + productId + "' for read only").trim();
	}
	
	public String getProductLastPassingPPId(String productId) {
		return execSelectSql("select last_passing_process_point_id from galadm.gal143tbx where product_id = '" + productId + "' for read only").trim();
	}
	
	public String getProductSeqLastPassingPPId(String productId) {
		return execSelectSql("select last_passing_process_point_id from galadm.gal176tbx where product_id = '" + productId + "' for read only").trim();
	}
	
	public String getAssociateNo(String productId, String ppId) {
		return execSelectSql("select associate_no from galadm.gal215tbx where product_id = '" + productId + "' and process_point_id = '" + ppId + "' order by actual_timestamp desc fetch first 1 rows only for read only").trim();
	}
	
	public String getApproverNo(String productId, String ppId) {
		return execSelectSql("select approver_no from galadm.gal215tbx where product_id = '" + productId + "' and process_point_id = '" + ppId + "' order by actual_timestamp desc fetch first 1 rows only for read only").trim();
	}
	
	public String getActualTimestamp(String productId, String ppId) {
		return execSelectSql("select actual_timestamp from galadm.gal215tbx where product_id = '" + productId + "' and process_point_id = '" + ppId + "' order by actual_timestamp desc fetch first 1 rows only for read only");
	}
}
