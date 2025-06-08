package com.honda.galc.system.oif.svc.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.honda.galc.common.exception.OifServiceException;


/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * This is class to access to database.<br>
 * Access to database with JDBC.<br>
 * <h4>Usage and Example</h4>
 * Insert the usage and example here.
 * <h4>Special Notes</h4>
 * Insert the special notes here if any.
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Administrator</TD>
 * <TD>(2001/01/16 15:39:12)</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Administrator
 * 
 */
public class OifServiceDBUtility extends OifServiceObject {
	/**
	 * ID to identify the line number
	 */
	private String lineID;

	/**
	 * Connection for database instance
	 */
	private java.sql.Connection conn;

	/**
	 * Database name
	 */
	private java.lang.String datasourceUrl;

	/**
	 * DBUtil constructor comment
	 */
	public OifServiceDBUtility(IOifService aParentHandler, String aLineId, String url) {
		super(aParentHandler);
		this.lineID = aLineId;
		this.datasourceUrl = url;
	}

	/**
	 * Connection With the data base of the connection JDBC A resource is
	 * canceled
	 * <p>
	 * 
	 * @return Result of close
	 * @exception OifServiceException
	 *                Close of databae failed
	 */
	public boolean close(){
		final String logMethod = "close()";
		
		boolean res = false;
		try {
			// Connection The data base which an Connection holds at present is
			// closed
			if (isDebug()) {
				log(MSG_GEN_INFO_ID, logMethod, "Closing connection: line:" + lineID + ", URL: " + datasourceUrl);
			}			
			conn.close();

			res = true;
		} catch (SQLException e) {
			logError("HCMOIFE0321", logMethod, "SQLException caught in closeDB(): " + e);
			// object is formed and thrown.
			throw new OifServiceException("HCMOIFE0321", e);
		}

		return res;
	}

	/**
	 * All the changes done after the commit/rollback right before the front
	 * should be made effective, and a connection cancels all the databaselocks
	 * to hold at present.
	 * <p>
	 * 
	 * @return boolean
	 * @exception OifServiceException
	 *                Commit failed.
	 */
	public boolean commit() throws OifServiceException {
		final String logMethod = "commit()";
		
		boolean res = false;
		try {
			// Connection: The data base connected at present is closed
			conn.commit();
			
			res = true;
		} catch (SQLException e) {
			logError("HCMOIFE0322", logMethod, "SQLException caught in " + logMethod + ": " + e);

			// DBUtilitiyException object is formed and thrown
			throw new OifServiceException("HCMOIFE0322", e);
		}

		return res;
	}

	public String getLineID() {
		return lineID;
	}

	public boolean connect() throws OifServiceException {
		final String logMethod = "connect()";
		
		boolean res = false;
		conn = null;
		try {
			// url: It is connected to the data base which an URL shows
			this.conn = getConnection(this.datasourceUrl);
			
			res = true;
			
		} catch (Exception e) {
			// It save message that is error
			logError("HCMOIFE0323", logMethod, "Error connecting to database in " 
						+ logMethod + ": url: " 
					+ this.datasourceUrl + ". Exception: "
					+ e.getMessage());

			throw new OifServiceException("HCMOIFE0323", e);
		}

		return res;
	}

	/**
	 * @param datasourceUrl
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */
	protected Connection getConnection(String datasourceUrl) throws NamingException, SQLException {

		DataSource dataSource = (DataSource) new InitialContext().lookup(datasourceUrl);

		return dataSource.getConnection();
	}

	/**
	 * A connection to URL of the specified data base is done
	 * <p>
	 * 
	 * @return boolean
	 * @param url
	 *            Database url name
	 * @exception OifServiceException
	 *                An exception about the database occurred.
	 */
	public boolean connectDB(int isolationLevel) throws OifServiceException {
		final String logMethod = "connectDB()";
		
		boolean res = false;

		res = connect();

		try {
			switch (isolationLevel) {
			case 1: // read uncommitted
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				break;

			case 2: // read committed
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
				break;

			case 3: // repeatable read
				conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
				break;

			case 4: // repeatable read
				conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				break;

			default: // set default to read committed
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
				break;

			}
		} catch (Exception e) {
			// It save message that is error
			logError("HCMOIFE0324", logMethod, "Error setting transaction isolation in " 
					+ logMethod + ": url: " 
					+ this.datasourceUrl + ". Exception: "
					+ e.getMessage());

			throw new OifServiceException("HCMOIFE0324", e);
		}
		return res;
	}

	/**
	 * A designated file is inserted into Table 1. The preparation of the file
	 * of carrying out insertion 2. The practice of the executive fileThe
	 * practice of the executive file
	 * <p>
	 * 
	 * @return Result of SQL
	 * @param sql
	 *            SQL statement
	 * @exception OifServiceException
	 *                SQL failed
	 */
	public java.sql.ResultSet executeSelect(String sql) throws OifServiceException {
		final String logMethod = "executeSelect()";
		
		ResultSet resSet = null;
		try {
			// Statement object is made
			Statement stmt = conn.createStatement();
			// SQL is carried out, and resultSet object is acquired
			resSet = stmt.executeQuery(sql);
		} catch (Exception e) {
			logError("HCMOIFE0326", logMethod, "Exception caught in " + logMethod + ": " + e.getMessage());

			throw new OifServiceException("HCMOIFE0326", e);
		}

		return resSet;
	}

	/**
	 * The practice of specified SQL is done
	 * <p>
	 * 
	 * @return Update row number
	 * @param sql
	 *            SQL Statement
	 * @exception OifServiceException
	 *                SQL failed
	 */
	public int executeSQL(String sql) throws OifServiceException {
		final String logMethod = "executeSQL()";
		
		int res = 0;
		try {
			// Statement An object is made
			Statement stmt = conn.createStatement();
			// SQL is carried out, and a ResultSet object is acquired
			res = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			logError("HCMOIFE0327", logMethod, "SQLException caught in " + logMethod + ": " + e.getMessage());

			// DBUtilitiyException object is formed and thrown
			throw new OifServiceException("HCMOIFE0327", e);
		}

		return res;
	}

	public int[] getColumnLength(String tableName, String sql) throws OifServiceException {
		final String logMethod = "getColumnLength()";
		
		ResultSet resSet = null;
		int columnLen[] = null;
		boolean sqlFlg = false;
		try {
			if ((sql == null) && (tableName == null)) {
				// It save message that is error
				logError("HCMOIFE0328", logMethod, "Both sql and tableName are both null.");

				// DBUtilitiyException object is formed and thrown
				throw new OifServiceException("HCMOIFE0328");
			}

			if (sql == null) {
				// tableNameSQL: to acquire a ResultSet object from the
				// tableNameSQL is made
				sql = "SELECT * from " + tableName;
				sqlFlg = true;
			}

			// Statement object is made
			Statement stmt = conn.createStatement();
			
			// SQL is carried out, and a ResultSet object is acquired
			resSet = stmt.executeQuery(sql);
			ResultSetMetaData resSetMD = resSet.getMetaData();
			
			// Colum nnumber of the table is acquired
			int columnNum = resSetMD.getColumnCount();
			
			// The MAX length of each Column is acquired *It is usual maximum
			// length by the type char
			columnLen = new int[columnNum];

			// Changed by T.Shimode at 2001/05/11
			if (!sqlFlg) {
				for (int cnt = 1; cnt <= columnNum; cnt++) {
					columnLen[cnt - 1] = resSetMD.getColumnDisplaySize(cnt);
				}

			} else {
				for (int cnt = 1; cnt <= columnNum - 2; cnt++) {
					columnLen[cnt - 1] = resSetMD.getColumnDisplaySize(cnt);
				}
			}

		} catch (SQLException e) {
			// It save message that is error
			logError("HCMOIFE0328", logMethod, "SQLException caught: " + e);
			
			throw new OifServiceException("HCMOIFE0328", e);
		}
		return columnLen;
	}

	/**
	 * Insert the method's description here. Creation date: (9/3/02 1:15:33 PM)
	 * 
	 * @return boolean
	 */
	public boolean isConnected() throws SQLException {
		if (conn != null && !conn.isClosed()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Insert the method's description here. Creation date: (7/12/02 11:15:38
	 * AM)
	 * 
	 * @return java.sql.PreparedStatement
	 * @param aStatement
	 *            java.lang.String
	 */
	public PreparedStatement prepareStatement(String aStatement) throws OifServiceException {
		// PreparedStatement
		try {
			return conn.prepareStatement(aStatement);
		} catch (SQLException vSQLException) {
			throw new OifServiceException("SQLException raised when creating preparestatement",
					vSQLException);
		}

	}

	/**
	 * All the changes done after the work right before the front should be
	 * invalidated. This connection cancels all the locks to hold at present.
	 * <p>
	 * 
	 * @return Rollback result
	 * @exception OifServiceException
	 *                Rollback failed
	 */
	public boolean rollback() throws OifServiceException {
		final String logMethod = "rollback()";
		
		boolean res = false;
		try {
			// The data base which a connection holds at present works rollback
			conn.rollback();
			res = true;
		} catch (SQLException e) {
			logError("HCMOIFE0329", logMethod, "Error: " + e);

			throw new OifServiceException("HCMOIFE0329", e);
		}

		return res;
	}

	public boolean exists(String selectString, PreparedStatement select, List<Object> record) throws OifServiceException {
		final String logMethod = "exists()";
		
		try {
			
			ResultSet resultSet = select.executeQuery();
			
			// the select should include count(*) column as a first column
			resultSet.next();
			
			int count = resultSet.getInt(1);
			
			if(isDebug()) {
				log(MSG_GEN_INFO_ID, logMethod, "Found " + count + " records");
			}
			
			return count > 0;
			
		} catch (SQLException e) {
			logError("HCMOIFE0330", logMethod, "Error: select: " + selectString + ", exception: " + e);

			throw new OifServiceException("HCMOIFE0330", e);
		}

	}


	public int executeModify(LoadRecordUtility loadUtil, OifRecordValues records, int i) 
					throws OifServiceException {
		final String logMethod = "executeModify()";
		String modifySql = "NOT DEFINED";
		int count = 0;
		PreparedStatement statement = null;
		try {
			// if select count(*) returns rows
			statement = loadUtil.getSelectStatement(this.conn, records, i);
			if (exists(loadUtil.getSelect(), statement, records.getValue(i))) {
				
				if(loadUtil.updateIfExists()) {
					modifySql = loadUtil.getUpdate();
					
					// run update
					closeStatement(statement); // clean up resource
					
					statement = loadUtil.getUpdateStatement(this.conn, records, i);
	
					count = statement.executeUpdate();
					log(MSG_GEN_INFO_ID, logMethod, "Record: " + records.recordToString(i) 
							+ " loaded with update count: " + count);
				} else {
					log(MSG_GEN_INFO_ID, logMethod, "Record: " + records.recordToString(i) 
							+ " udpate skipped: updateIfExists: " + loadUtil.updateIfExists());					
				}

			} else {
				modifySql = loadUtil.getInsert();
				// else - run insert
				closeStatement(statement); // clean up resource
				
				statement = loadUtil.getInsertStatement(this.conn, records, i);

				count = statement.executeUpdate();
				log(MSG_GEN_INFO_ID, logMethod, "Record: " + records.recordToString(i) 
						+ " loaded with insert count: " + count);
			}
		} catch (SQLException e) {
			logError("HCMOIFE0325", logMethod, "Error in executeModify(): record: " + records.recordToString(i) + 
					", stmt: " + modifySql + ": " + e);

			// OifServiceException An object is formed and thrown
			throw new OifServiceException("HCMOIFE0325", e);
		} finally {
			closeStatement(statement);			
		}
		return count;
	}

	/**
	 * @param statement
	 */
	public void closeStatement(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				// Do nothing
			}
		}
	}

	/**
	 * @param stmt
	 * @param rs
	 * @param isCommit TODO
	 * @throws OifServiceException 
	 */
	public void close(Statement stmt, ResultSet rs, boolean isCommit) throws OifServiceException {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// do nothing
			}
			rs =  null;
		}
		
		if(stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// do nothing
			}
			stmt =  null;
		}
		if (isCommit) {
			this.commit();
		} else {
			this.rollback();
		}
		this.close();
	}
}
