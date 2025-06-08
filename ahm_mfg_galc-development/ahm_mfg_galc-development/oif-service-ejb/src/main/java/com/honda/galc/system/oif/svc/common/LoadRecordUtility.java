package com.honda.galc.system.oif.svc.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.honda.galc.system.oif.svc.common.OifFieldDefinition.FieldType;

/**
 * <h3>LoadRecordUtility</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * A utilitty to manipulate DB records issuing SQL over JDBC<br>
 * SQL is generated base on field definitions placed in properties<br>
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update Date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Apr 10, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@???</TD>
 * <TD>Initial Version</TD>
 * </TR>
 * </TABLE>
 */
public class LoadRecordUtility extends OifServiceObject {

	private static final String SELECT_WORD = "SELECT";

	private static final String SQL_CLAUSE_END = ")";

	private static final String SQL_STMT_END = "";

	private static final String SQL_WHERE = "WHERE";

	private static final String SQL_VALUES = "VALUES (";

	private static final String SQL_INSERT_INTO = "INSERT INTO ";

	private static final String SQL_UPDATE = "UPDATE ";

	private static final String SQL_SELECT_COUNT_FROM = SELECT_WORD + " COUNT(*) FROM ";

	private static final String SQL_SPACE = " ";

	private static final String SQL_AND = " AND ";

	private static final String SQL_ASSIGN = " = ";

	private static final String SQL_CLAUSE_SEPARATOR_W_SPACE = ", ";

	private static final String SQL_PARAM = "?";

	private String table;
	
	private String select;
	
	private String update;
	
	private boolean noUpdate = false;
	
	private String insert;

	protected Map<String, OifFieldDefinition> oifFieldDefinition;
	
	protected Map<Integer, OifFieldDefinition> selParams;
	protected Map<Integer, OifFieldDefinition> updParams;
	protected Map<Integer, OifFieldDefinition> insParams;

	private String fieldDefId;
	
	/**
	 * Constructor
	 * 
	 * @param parentHandler - parent service handler
	 * @param fldDefCompId - field definition component ID - for Logging
	 * @param dbTable - database table
	 * @param options TODO
	 * @param pFieldDefProps - field definition properties
	 */
	public LoadRecordUtility(IOifService parentHandler, String fldDefCompId, 
			String dbTable, String options, Properties pFieldDefProps) {
		super(parentHandler);
		this.table = dbTable;
		this.fieldDefId = fldDefCompId;
		this.initRecordDefinition(pFieldDefProps);
		if(options != null) {
			if(options.equalsIgnoreCase("nu")) { // no update
				noUpdate = true;
			}
		}
	}
	
	/**
	 * Initializes field definition with given properties
	 * 
	 * @param fieldDefProps - field definition properties
	 */
	private void initRecordDefinition(Properties fieldDefProps) {
		final String logMethod = "initRecordDefinitions(fieldDefProps)";
		
		if(fieldDefProps == null || fieldDefProps.isEmpty()) {
			logError("HCMOIFE0332", logMethod, "Empty record defintion properties for " 
					+ this.fieldDefId + " of table " + this.table);
			return;
		}
		
		Set<Entry<Object, Object>> elements = fieldDefProps.entrySet();
		
		this.oifFieldDefinition = new HashMap<String, OifFieldDefinition>();
		
		for (Entry<Object, Object> entry : elements) {
			
			String col = entry.getKey().toString();
			String def = entry.getValue().toString();
			
			OifFieldDefinition rd = OifFieldDefinition.parseFormat(col, def);
			
			if (rd != null) {
				this.oifFieldDefinition.put(col, rd);
			} else {
				logError(MSG_GEN_ERR_ID, logMethod, "Cannot parse field definition for: " + col);
			}
			
		}
	}

	/**
	 * Getter for SQL insert statement
	 * 
	 * @return SQL insert statement
	 */
	public String getInsert() {
		return insert;
	}
	
	/**
	 * Getter for SQL select statement
	 * 
	 * @return SQL select statement
	 */
	public String getSelect() {
		return select;
	}
	
	/**
	 * Getter for SQL update statement
	 * 
	 * @return SQL update statement
	 */
	public String getUpdate() {
		return update;
	}

	/**
	 * Obtain PreparedStatement select statement
	 * 
	 * @param conn - JDBC connection
	 * @param records - records
	 * @param paramIx - position in records
	 * @return - prepared statement ready to execute
	 * 
	 * @throws SQLException
	 * 
	 */
	public PreparedStatement getSelectStatement(Connection conn, OifRecordValues records, int paramIx) throws SQLException {
		return getStatement(conn, records, paramIx, this.select, this.selParams);		
	}
	
	/**
	 * Obtain PreparedStatement update statement
	 * 
	 * @param conn - JDBC connection
	 * @param records - records
	 * @param paramIx - position in records
	 * @return - prepared statement ready to execute
	 * 
	 * @throws SQLException
	 * 
	 */
	public PreparedStatement getUpdateStatement(Connection conn, OifRecordValues records, int paramIx) throws SQLException {
		return getStatement(conn, records, paramIx, this.update, this.updParams);		
	}
	
	/**
	 * Obtain PreparedStatement insert statement
	 * 
	 * @param conn - JDBC connection
	 * @param records - records
	 * @param paramIx - position in records
	 * @return - prepared statement ready to execute
	 * 
	 * @throws SQLException
	 * 
	 */
	public PreparedStatement getInsertStatement(Connection conn, OifRecordValues records, int paramIx) throws SQLException {
		return getStatement(conn, records, paramIx, this.insert, this.insParams);		
	}
	
	/**
	 * Obtain PreparedStatement statement for a given SQL string
	 * 
	 * @param conn - JDBC connection
	 * @param records - records
	 * @param paramIx - position in records
	 * @param sql - given SQL statement string
	 * @param paramMap - parameter field map
	 * @return - prepared statement ready to execute
	 * @throws SQLException
	 */
	protected PreparedStatement getStatement(Connection conn, OifRecordValues records, 
			int paramIx, String sql, Map<Integer, OifFieldDefinition>paramMap) throws SQLException {
		final String logMethod = "getStatement(conn,params,sql,paramMap)";
		
		if(isDebug()) {
			log(MSG_GEN_INFO_ID, logMethod, "Preparing: " + sql);			
		}
		
		List<Object> params = records.getValue(paramIx);
		
		// Get statement
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		// Set parameters
		for (Map.Entry<Integer, OifFieldDefinition> recDefEntry : paramMap.entrySet()) {
			OifFieldDefinition recDef = recDefEntry.getValue();
			int paramIndex = recDef.getPosition() - 1;
			
			if(paramIndex >= 0 && paramIndex < params.size()) {
				
				Object param = params.get(paramIndex);
				int position = recDefEntry.getKey();
				if(isDebug()) {
					log(MSG_GEN_INFO_ID, logMethod, "Set object: " + position + "("+ recDef.getFieldName() + ")<-" + param);			
				}
				
				if (param != null) {
					
					// Trim if it's a String
					if(param instanceof String) {
						param = ((String)param).trim();
					}
					
					// Clip String if it's too long
					if(recDef.isStringTooLong(param)) {
						param = param.toString().substring(0, recDef.getFieldLength());
					}
					
					try {
						
						stmt.setObject(position, param);
						
					} catch (SQLException e) {
						logError(MSG_GEN_ERR_ID, logMethod, "SQL: " + sql 
								+ ": Enable to set parameter[" + position + "<- " + paramIndex + "(" + records.getName(paramIndex)+ ")" + "]: \"" 
								+ param + "\" (Class: " + param.getClass().getSimpleName() + ")");
						throw e;
					}					
					
				} else {
					
					setNullField(stmt, position, recDef);

				}
								
			} else {
				
				logError("HCMOIFE0327", logMethod, "Index is out of bounds: " 
						+ (paramIndex+1) + " [1.." + params.size() + "]");
				
			}
		}
		
		return stmt;
	}

	/**
	 * Set NULL JDBC parameter 
	 * 
	 * @param stmt - PreparedStatement
	 * @param position - positon in SQL parameteres
	 * @param fieldDef - field definition
	 * 
	 * @throws SQLException
	 */
	private void setNullField(PreparedStatement stmt, int position, 
				OifFieldDefinition fieldDef) throws SQLException {
		
		FieldType fieldType = fieldDef.getFieldType();
		switch(fieldType) {
			case C:
				stmt.setString(position, null);
				break;
										
			default:
				stmt.setNull(position, fieldType.getSqlType());
				break;
		}
	}

	/**
	* Build three SQL statements. The first one is for finding a current record. The second is for inserting a a new reord. The
	* last one is updating an existing record
	* <p>
	 * @param names - 'select' used to prepare list of records to load
	 * @param a string containing the record read from the input file
	 * @param a string containing the record definition
	 * @return an array containing three SELECT, INSERT and UPDATE SQL Statements
	*/
	protected void buildSQL(List<String> names) {
		final String logMethod = "buildSQL()";
		
		StringBuilder selectStatement = new StringBuilder(SQL_SELECT_COUNT_FROM).append(table).append(SQL_SPACE);
		StringBuilder updateStatement = new StringBuilder(SQL_UPDATE).append(this.table).append(" SET");
		StringBuilder insertStatement = new StringBuilder(SQL_INSERT_INTO).append(this.table).append(" (");
		StringBuilder insertValue = new StringBuilder(SQL_VALUES);
		StringBuilder whereClause = new StringBuilder(SQL_WHERE);
		
		this.selParams = new HashMap<Integer, OifFieldDefinition>();
		this.updParams = new HashMap<Integer, OifFieldDefinition>();
		this.insParams = new HashMap<Integer, OifFieldDefinition>();
		
		int keyFieldCount = 0;
		int updateFieldCount = 0;
		int insertFieldCount = 0;
		
		adjustRecordDefinitions(names);
		
		try {
			// parse the record definition, get field name, key field, field type, starting poistion, field length, alignment, field format
			for (OifFieldDefinition rd : this.oifFieldDefinition.values()) {

				// build the SQL Select and Update statements
				if (rd.isKeyField()) {
					
					keyFieldCount++;
					
					addToWhere(keyFieldCount, rd.getFieldName(), whereClause);
					
					// Update 'select' parameters map
					this.selParams.put(keyFieldCount, rd);
					
				} else {
					
					updateFieldCount++;
					
					addToUpdate(updateFieldCount, rd.getFieldName(), updateStatement);
					
					//	Update 'update' parameters map
					this.updParams.put(updateFieldCount, rd);
	
				}
				
				// build the SQL Insert statement
				insertFieldCount++;
				
				addToInsert(insertFieldCount, rd.getFieldName(), insertStatement, 
						insertValue);
				
				//	Update 'insert' parameters map
				this.insParams.put(insertFieldCount, rd);
				
			} // END FOR

			selectStatement.append(whereClause.toString());
			selectStatement.append(SQL_STMT_END);
			
			if (isDebug()) {
				log(MSG_GEN_INFO_ID, logMethod, "SELECT statement is " + selectStatement.toString());
			}
			
			insertStatement.append(SQL_CLAUSE_END).append(SQL_SPACE);
			insertStatement.append(insertValue.toString());
			insertStatement.append(SQL_CLAUSE_END).append(SQL_STMT_END);
	
			if (isDebug()) {
				log(MSG_GEN_INFO_ID, logMethod, "INSERT statement is " + insertStatement.toString());
			}
			
			updateStatement.append(SQL_SPACE).append(whereClause.toString());
			updateStatement.append(SQL_STMT_END);
			
			// Adjust 'update' parameter map with 'where' map
			for ( Map.Entry<Integer, OifFieldDefinition> entry : this.selParams.entrySet()) {
				this.updParams.put(entry.getKey() + updateFieldCount, entry.getValue());
			}
			
			if (isDebug()) {
				log(MSG_GEN_INFO_ID, logMethod, "UPDATE statement is " + updateStatement.toString());
			}

			this.select = selectStatement.toString();
			this.insert = insertStatement.toString();
			this.update = updateStatement.toString();
			
			//}
		} catch (Exception e) {
			logError("HCMOIFE0333", logMethod, "Exception in building sql statements. " + e.getMessage());
		}

	}

	/**
	 * Populate record column positions based on extraction SQL Select statement
	 * 
	 * @param columns - result column names
	 */
	protected void adjustRecordDefinitions(List<String> columns) {
		final String logMethod = "adjustRecordDefinitions(extractionSelect)";
		
		if (columns != null && columns.size() > 0) {
			// Find list of columns
						
			for (int i = 0; i < columns.size(); i++) {
				String columnName = columns.get(i);
				if (columnName.length() > 0) {
					
					//     Find record definition
					OifFieldDefinition fieldDef = this.oifFieldDefinition.get(columnName);
					
					// Check that record is found and position is not set explicitly
					if (fieldDef != null && !fieldDef.isPositionDefined()) {
						
						//	   Set column position (0-based)
						fieldDef.setPosition(i + 1);
					}
				}					
				
			}
		}
		
		// Validate record defintion
		for (OifFieldDefinition fd : this.oifFieldDefinition.values()) {
			
			if(!fd.isPositionDefined()) {
				
				logError(MSG_GEN_ERR_ID, 
						logMethod, this.fieldDefId + " - " + this.table 
						+ ": Field position is not defined for field: " + fd.getFieldName());
				
			}
		}
	}

	/**
	 * Add field to 'where' clause
	 * 
	 * @param keyFieldCount - field position
	 * @param fieldName - field name
	 * @param whereClause - 'where' clause
	 */
	private void addToWhere(int keyFieldCount, String fieldName, StringBuilder whereClause) {
		
			if (keyFieldCount <= 1) {
				whereClause.append(SQL_SPACE);
			} else {
				whereClause.append(SQL_AND);
			}
			
			whereClause.append(fieldName).append(SQL_ASSIGN).append(SQL_PARAM);			
	}

	/**
	 * Add field to 'update' clause
	 * 
	 * @param updateFieldCount - field position
	 * @param fieldName - field name
	 * @param update - 'update' clause
	 */
	private void addToUpdate(int updateFieldCount, String fieldName, StringBuilder update) {
		
			if (updateFieldCount <= 1) {
				update.append(SQL_SPACE);
			} else {
				update.append(SQL_CLAUSE_SEPARATOR_W_SPACE);
			}			
			update.append(fieldName);
			
			update.append(SQL_ASSIGN).append(SQL_PARAM);
	}

	/**
	 * Add field to 'insert' clause
	 * 
	 * @param insertFieldCount - field position
	 * @param fieldName - field name
	 * @param insertStatement - 'insert' clause
	 */
	private void addToInsert(int insertFieldCount, String fieldName, StringBuilder insertStatement, 
			StringBuilder insertValue) {
		
		if (insertFieldCount <= 1) {
			insertStatement.append(fieldName);
			
			insertValue.append(SQL_PARAM);
	
		} else {
			insertStatement.append(SQL_CLAUSE_SEPARATOR_W_SPACE);
			insertStatement.append(fieldName);
			
			insertValue.append(SQL_CLAUSE_SEPARATOR_W_SPACE);
			insertValue.append(SQL_PARAM);
		}
	}
	
	/**
	 * Returns SQL select columns based on the fact they follow "$COLUMN" format
	 * 
	 * @param selectSql
	 * @return
	 */
	public static List<String> getSelectColumns(String selectSql) {
		Pattern p = Pattern.compile("\\s+\\$(\\w+)(\\s*,\\s*|\\s+From)", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(selectSql);
		
		List<String> cols = new ArrayList<String>();
		while(m.find()) {
			cols.add(m.group(1));
		}
		
		return cols;
	}

	/**
	 * Getter to table name
	 * 
	 * @return the table name
	 */
	public String getTable() {
		return table;
	}

	public boolean updateIfExists() {
		return !noUpdate;
	}

	public void setNoUpdate(boolean noUpdate) {
		this.noUpdate = noUpdate;
	}

}
