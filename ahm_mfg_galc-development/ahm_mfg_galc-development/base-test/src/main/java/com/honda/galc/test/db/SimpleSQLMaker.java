package com.honda.galc.test.db;

import java.util.Hashtable;
import java.util.Vector;

/**
 * <h3>This class maintains the parameter substitute data which is substituted to SQL skeleton and the using Vector parameter. </h3>
 * <h4>Description</h4>
 * It is SQL Creation Class.<br>
 * <br>
 * It implements SQL creation process interface. <br>
 * This class maintains the parameter substitute data which is substituted to SQL skeleton and the using Vector parameter. <br>
 * SQL skeleton defines SQL sentence and 2 types of parameters. One of them is character strings except BLOB which is a parameter enabling to express. Another one is a parameter for BLOB. Refer to the following examples.  <br>
 * Inquiry sentence SELECT * FROM TABLE_NAME WHERE INT_COLUMN > {0} AND CHAR_COLUMN = '{1}' AND BLOB_COLUMN = {2} <br>
 * Update sentence INSERT INTO TABLE_NAME (INT_CULUMN, CHAR_COLUMN, BLOB_COLUMN) VALUES( {2} , '{1}' , [0] )<br>
 * Parameter No. is in order of inputting in the substitute data input vector and it begins with 0.<br>
 * Also, data for BLOB maintaining to Vector should be byte[] format. <br>
 * <br> 
 * In this class, it is necessary to substitute data to the all parameter of SQL skeleton. <br>
 * When the substitute data is not setup or it is omission value (PqDbOmissionData) object, this class throws the exception. <br>
 * Also, when the substitute data is null, delete when ' exists before and after substitute place.<br>
 * <h4>Usage and Example</h4>
 * Show example in the following.<br>
 * <BR>
 * SELECT * FROM TABLE WHERE COL_A = {0} AND COL_B = '{1}' AND COL_C = {2}<BR>
 * Vector vec = new Vector();<BR>
 * vec.add( "5" );<BR>
 * vec.add( "BBB" );<BR>
 * vec.add( "20" );<BR>
 * In case of this,"SELECT * FROM TABLE WHERE COL_A = 5 AND COL_B = 'BBB' AND COL_C = 20" is executed.<BR>
 * <BR>
 * UPDATE TABLE SET COL_C = {0} WHERE COL_A = {2} AND COL_B = '{1}'<BR>
 * Vector vec = new Vector();<BR>
 * vec.add( "byte[] data" );<BR>
 * vec.add( "BBB" );<BR>
 * vec.add( "5" );<BR>
 * In case of this,"UPDATE TABLE SET COL_C = byte[] data WHERE COL_A = 5 AND COL_B = 'BBB'" is executed.<BR>
 * <BR>
 * INSERT INTO TABLE VALUES ( {0} , '{1}' , {2} )<BR>
 * Vector vec = new Vector();<BR>
 * vec.add( "5" );<BR>
 * vec.add( "BBB" );<BR>
 * vec.add( "byte[] data" );<BR>
 * vec.add( "CCC" );<BR>
 * In case of this, "INSERT INTO TABLE VALUES ( 5 , 'BBB' , "byte[]" data )" is executed.<BR>
 * <BR>
 * DELETE FROM TABLE WHERE COL_A = {1} AND COL_B = '{2}' AND COL_C = {3}<BR>
 * Vector vec = new Vector();<BR>
 * vec.add( "AAA" );<BR>
 * vec.add( "5" );<BR>
 * vec.add( "BBB" );<BR>
 * vec.add( "20" );<BR>
 * In case of this,"DELETE FROM TABLE WHERE COL_A = 5 AND COL_B = 'BBB' AND COL_C = 20" is executed.<BR>
 * <BR>
 * UPDATE TABLE SET COL_A = {0} , COL_B = '{1}' , COL_C = {2}<BR>
 * Vector vec = new Vector();<BR>
 * vec.add( null );<BR>
 * vec.add( null );<BR>
 * vec.add( null );<BR>
 * In case of this,"UPDATE TABLE SET COL_A = null , COL_B = null , COL_C = null" is executed.<BR>
 * <BR>
 * SELECT * FROM TABLE WHERE COL_A = {0} AND COL_B = '{1}' AND COL_C = {2}<BR>
 * Vector vec = new Vector();<BR>
 * vec.add( "5" );<BR>
 * vec.add( "BBB" );<BR>
 * In case of this, throw exception. <BR>
 * <h4>Special Notes</h4>
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
 * <TD>T.Shimode</TD>
 * <TD>(2001/02/06 14:51:09)</TD>
 * <TD>0.1</TD><TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * <TR>
 * <TD>M.Hayashibe</TD>
 * <TD>(2001/02/06 14:51:09)</TD>
 * <TD>hma-2001/08/02</TD><TD>VER_2001_08_02</TD>
 * <TD>Modify setSkeltonType() method</TD>
 * </TR>
 * <TR>
 * <TD>M.Hayashibe</TD>
 * <TD>(2001/11/02 14:00:00)</TD>
 * <TD></TD><TD>(none)</TD>
 * <TD>Revise Javadoc</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author T.Shimode
 */

public class SimpleSQLMaker implements SQLMaker {
	/**
	 * SQL Skelton type
	 */
	protected String skeltonType = "";

	/**
	 * SQL data after creating 
	 */
	protected String sqlState = "";

	/**
	 * SQL data before creating
	 */
	protected String sqlStateBefore = "";

	/**
	 * Blob data after creating 
	 */
	protected Vector<Object> blobPara = null;

	/**
	 * Substiution data before creating.
	 */
	protected Vector<Object> paraBefore = null;

	/**
	 * Numbering list of Substiution data except Blob in working <br>
	 */
	protected Vector<Object> wkParaNotBlob = null;

	/**
	 * Number list of Blob data in working.
	 */
	protected Vector<Object> wkParaBlob = null;

	/**
	 * Numbering List of null Substiution in working.
	 */
	protected Vector<Object> wkParaNull = null;

	/**
	 * Numbering List of Omission Substiution Data in working 
	 */
	protected Vector<Object> wkParaOmission = null;

	/**
	 * Substiution data in working.
	 */
	protected Hashtable<Object,Object> wkParaHashtable = null;

	/**
	 * Omission Object
	 */
	protected OmissionData omissionData = new OmissionData();
	

/**
 * Constractor
 */
public SimpleSQLMaker() {
    super();
}
/**
 * Get Blob data after creating 
 * <p>
 * @return Blob data after creating 
 */
public Vector<Object> getBlobParams() {
    return blobPara;
}
/**
 * Get SQL after creating
 * <p>
 * @return SQL after creating
 */
public String getSqlStatement() {
    return sqlState;
}
/**
 * Initialize mathod
 * <p>
 * @param aSkelton SQL Skelton 
 * @param aParam parameter Substiution data
 */
 protected void init(String aSkelton, Vector<Object> aParam) {
	sqlStateBefore = aSkelton;
	paraBefore = aParam;
	sqlState = aSkelton;
	skeltonType = "";
	blobPara = new Vector<Object>();
	wkParaNotBlob = new Vector<Object>();
	wkParaBlob = new Vector<Object>();
	wkParaNull = new Vector<Object>();
	wkParaOmission = new Vector<Object>();
	wkParaHashtable = new Hashtable<Object,Object>();
}
/**
 * Check if Omission data does not exist in sql skelton.
 * <p>
 * @exception IllegalSQLRequestException  When Omission data exist.
 */
protected void isEssential() throws IllegalSQLRequestException {
	String sql = sqlState;

	if (sql.indexOf(SQLKeyWords.KEY_SHOURYAKU) >= 0) {
		IllegalSQLRequestException e = new IllegalSQLRequestException("sql.indexOf(SQLKeyWords.KEY_SHOURYAKU) >= 0");
		throw e;
	}
}
/**
 * Get query flag <br>
 * In case of query,return true
 * <p>
 * @return boolean query flag
 */
public boolean isQuery() {
    return skeltonType.equals(SQLKeyWords.KEY_SELECT);
}
/**
 * Analyze SQL skeleton and create the parameter list. <br>
 * Retrieve data corresponding to the parameter of SQL skeleton, <br>
 * and create list corresponding to each format. When substitute data is null, and except omission value, register it to the working substitute data by a key of parameter no. <br>
 * character strings list.<br>
 * <p>
 * @exception IllegalSQLRequestException When some exceptoin occured,throw this exception.
 */
protected void runAnalyzeSkelton() throws IllegalSQLRequestException {
	int paraSize = paraBefore.size();
	int wkIndexS = 0;
	int wkIndexE = 0;
	int wkParaIndex = 0;
	String wkParaStr = "";
	Object wkObj = null;

	String[][] paramKakko = new String[2][2];
	paramKakko[0][0] = SQLKeyWords.KEY_PARAM_STRING_KAKKO1;
	paramKakko[0][1] = SQLKeyWords.KEY_PARAM_STRING_KAKKO2;
	paramKakko[1][0] = SQLKeyWords.KEY_PARAM_BYTES_KAKKO1;
	paramKakko[1][1] = SQLKeyWords.KEY_PARAM_BYTES_KAKKO2;

	for (int y = 0; y < paramKakko.length; y++) {
		String[] wkKakko = paramKakko[y];
		wkIndexS = 0;
		wkIndexE = 0;
		for (;;) {
			wkIndexS = sqlState.indexOf(wkKakko[0], wkIndexE);
			if (wkIndexS < 0) {
				break;
			}

			wkIndexE = sqlState.indexOf(wkKakko[1], wkIndexS);
			wkParaStr = sqlState.substring(wkIndexS, wkIndexE + 1);
			wkParaIndex = 
				Integer.parseInt(wkParaStr.substring(1, wkParaStr.length() - 1).trim()); 
			wkObj = omissionData;

			if (wkParaIndex < paraSize) {
				wkObj = paraBefore.elementAt(wkParaIndex);
			}

			if (wkObj == null) {
				wkParaNull.add(wkParaStr);
			}
			else if (wkObj instanceof OmissionData) {
				wkParaOmission.add(wkParaStr);
			}
			else {
				if (wkKakko[0].equals(SQLKeyWords.KEY_PARAM_STRING_KAKKO1)) {
					if (!(wkObj instanceof String)) {

						IllegalSQLRequestException e = 
							new IllegalSQLRequestException("SQLParams includes not instanceof String"); 

					throw e;
					}
					wkParaNotBlob.add(wkParaStr);
				}
				else if (wkKakko[0].equals(SQLKeyWords.KEY_PARAM_BYTES_KAKKO1)) {
					if (!(wkObj instanceof byte[])) {

						IllegalSQLRequestException e = 
							new IllegalSQLRequestException("SQLParams includes not instanceof Byte[]"); 

						throw e;
					}
					wkParaBlob.add(wkParaStr);
				}
				wkParaHashtable.put(wkParaStr, wkObj);
			}
		}
	}

}
/**
 * Create SQL sentence from the specified SQL skeleton and parameter substitute data.<br>
 * <p>
 * @param aSkelton  SQL Skelton
 * @param aParam  Parameter substiution data
 * @exception IllegalSQLRequestException  When fails in creating SQL
 */
public void runMakeSql(String aSQLSkelton, Vector<Object> aSQLParam)
	throws IllegalSQLRequestException {
	
	try {
		init(aSQLSkelton, aSQLParam);
		setSkeltonType();
		runAnalyzeSkelton();
		setOmissionParam();
		setNullParam();
		setBlobParam();
		setNotBlobParam();
		isEssential();
	}
	catch (RuntimeException e) {
			throw e;
	}

}
/**
 * Set Blob data to SQL skelton.
 * <p>
 * @exception Exception When some exception occured.
 */
protected void setBlobParam() throws RuntimeException {
//System.out.println(getClass() + " # setNotBlobParam()" );
	String sql = sqlState;
	int wkIntS = 0;
	int wkIntE = 0;
	String wkSqlS = "";
	String wkSqlE = "";
	String wkParamStr = "";
	Object data = null;

	for ( int a = 0; a < wkParaBlob.size(); a++ ) {
		wkParamStr = (String)wkParaBlob.elementAt(a);

		wkIntS = sql.indexOf(wkParamStr);
		wkIntE = wkIntS + wkParamStr.length();
		wkSqlS = sql.substring(0, wkIntS);
		wkSqlE = sql.substring(wkIntE);

		sql = wkSqlS + "?" + wkSqlE;
		data = wkParaHashtable.get(wkParamStr);
		blobPara.addElement(data);
	}
	sqlState = sql;	
	
}
/**
 * Set data except Blob data to SQL skelton.
 * <p>
 * @exception Exception When some exception occured.
 */
protected void setNotBlobParam() throws RuntimeException {
//System.out.println(getClass() + " # setNotBlobParam()" );
	String sql = sqlState;
	int wkIntS = 0;
	int wkIntE = 0;
	String wkSqlS = "";
	String wkSqlE = "";
	String wkParamStr = "";
	String data = "";

	for ( int a = 0; a < wkParaNotBlob.size(); a++ ) {
		wkParamStr = (String)wkParaNotBlob.elementAt(a);

		wkIntS = sql.indexOf(wkParamStr);
		wkIntE = wkIntS + wkParamStr.length();
		wkSqlS = sql.substring(0, wkIntS);
		wkSqlE = sql.substring(wkIntE);

		data = (String)wkParaHashtable.get(wkParamStr);
		sql = wkSqlS + data + wkSqlE;
	}
	sqlState = sql;	
}
/**
 * Set null data to SQL skelton
 * <p>
 * @exception  Exception  When some exception occured
 */
protected void setNullParam() throws RuntimeException {
	String sql = sqlState;
	int wkIntS = 0;
	int wkIntE = 0;
	String wkSqlS = "";
	String wkSqlE = "";
	String wkParamStr = "";

	for ( int a = 0; a < wkParaNull.size(); a++ ) {
		wkParamStr = (String)wkParaNull.elementAt(a);
		wkIntS = sql.indexOf(wkParamStr);
		wkIntE = wkIntS + wkParamStr.length();
		wkSqlS = sql.substring(0, wkIntS);
		wkSqlE = sql.substring(wkIntE);

		if ( wkSqlS.trim().endsWith("'") ) {
			wkSqlS = wkSqlS.substring(0,wkSqlS.lastIndexOf("'"));
			wkSqlE = wkSqlE.substring(wkSqlE.indexOf("'") + 1 );
		}
		sql = wkSqlS + SQLKeyWords.KEY_NULL + wkSqlE;
	}
	sqlState = sql;	
}
/**
 * Set omission data to SQL skelton.
 * <p>
 * @exception Exception  When some exeption occured.
 */
protected void setOmissionParam() throws RuntimeException{
//System.out.println(" # setOmissionParam()");
	String sql = sqlState;
	int wkIntS = 0;
	int wkIntE = 0;
	String wkSqlS = "";
	String wkSqlE = "";
	String wkParamStr = "";

	for ( int a = 0; a < wkParaOmission.size(); a++ ) {
		wkParamStr = (String)wkParaOmission.elementAt(a);
		wkIntS = sql.indexOf(wkParamStr);
		wkIntE = wkIntS + wkParamStr.length();
		wkSqlS = sql.substring(0, wkIntS);
		wkSqlE = sql.substring(wkIntE);

		sql = wkSqlS + SQLKeyWords.KEY_SHOURYAKU + wkSqlE;
	}
	sqlState = sql;	
}
/**
 * Set SQL skelton type(SELECT or INSERT or UPDATE or ELETE ) <br>
 * When there is neither SELECT nor INSERT,UPDATE,DELETE, throw exception
 * <p>
 * @exception  IllegalSQLRequestException When Head of SQL is neither SELECT nor INSERT,UPDATE,DELETE 
 */
protected void setSkeltonType() throws IllegalSQLRequestException {
	// VER_2001_08_02
	// What is SQL type?
	sqlState = sqlState.trim();
		
	// add by T.Shimode at 2001/06/12
	String tempType = sqlState.toUpperCase();
	
	if (tempType.startsWith(SQLKeyWords.KEY_SELECT)) {
		skeltonType = SQLKeyWords.KEY_SELECT;
	}
	// bmarks 2CX 5/25/06 Added to handle Selects with TEMP tables
	else if (tempType.startsWith(SQLKeyWords.KEY_WITH)) {
		skeltonType = SQLKeyWords.KEY_SELECT;
	}
	else if (tempType.startsWith(SQLKeyWords.KEY_INSERT)) {
		skeltonType = SQLKeyWords.KEY_INSERT;
	}
	else if (tempType.startsWith(SQLKeyWords.KEY_DELETE)) {
		skeltonType = SQLKeyWords.KEY_DELETE;
	}
	else if (tempType.startsWith(SQLKeyWords.KEY_UPDATE)) {
		skeltonType = SQLKeyWords.KEY_UPDATE;
	}
	else {
		skeltonType = SQLKeyWords.KEY_FREE_SQL;

		/***** Update By M.Hayashibe 2001/7/27
		IllegalSQLRequestException exception = new IllegalSQLRequestException("SYSSQ005");
		Logger log = new Logger();
		LogRecord record = new LogRecord("SYSSQ005","SQLSkenton : " + sqlState);
		record.setSourceClassName(this.getClass().getName());
		record.setSourceMethodName("setSkeltonType()");
		log.logMessage(record);
		throw exception;
		*****/
	}
}
}
