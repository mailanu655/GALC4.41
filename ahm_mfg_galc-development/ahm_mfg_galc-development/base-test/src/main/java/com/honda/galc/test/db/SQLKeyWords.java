package com.honda.galc.test.db;

/**
 * <h3>Literals of used by com.honda.global.galc.common.database package</h3>
 * <h4>Description</h4>
 * <h4>Usage and Example</h4>
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
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author T.Shimode
 */

public class SQLKeyWords {
	/**
	 * NULL String
	 */
	public static final String KEY_NULL = "NULL";

	/**
	 * Header String of SELECT Sentence
	 */
	public static final String KEY_SELECT = "SELECT ";

	/**
	 * Header String of INSERT Sentence
	 */
	public static final String KEY_INSERT = "INSERT ";

	/**
	 * Header String of UPDATE Sentence
	 */
	public static final String KEY_UPDATE = "UPDATE ";

	/**
	 * Header String of DELETE Sentence
	 */
	public static final String KEY_DELETE = "DELETE ";

	/**
	 * Header String of WITH Sentence
	 */
	public static final String KEY_WITH = "WITH ";

	/**
	 * Header String of free SQL Sentence
	 */
	public static final String KEY_FREE_SQL = "FREE_SQL ";
	
	/**
	 * Analysis key word " WHERE "
	 */
	public static final String KEY_WHERE = " WHERE ";

	/**
	 * Analysis key word  " AND "
	 */
	public static final String KEY_AND = " AND ";

	/**
	 * Analysis key word  " OR "
	 */
	public static final String KEY_OR = " OR ";

	/**
	 * Analysis key word  " ( "
	 */
	public static final String KEY_KAKKO1 = " ( ";

	/**
	 * Analysis key word  " ) "
	 */
	public static final String KEY_KAKKO2 = " ) ";

	/**
	 * Analysis key word  " ORDER "
	 */
	public static final String KEY_ORDER = " ORDER ";

	/**
	 * Analysis key word  " GROUP "
	 */
	public static final String KEY_GROUP = " GROUP ";

	/**
	 * Analysis key word  " HAVING "
	 */
	public static final String KEY_HAVING = " HAVING ";

	/**
	 * Analysis key word  " SET "
	 */
	public static final String KEY_SET = " SET ";

	/**
	 * Analysis key word  " VALUES "
	 */
	public static final String KEY_VALUES = " VALUES ";

	/**
	 * Analysis key word  " , "
	 */
	public static final String KEY_COMMA = " , ";

	/**
	 * Analysis key word 
	 */
	public static final String KEY_SHOURYAKU = " %OMISSION% ";

	/**
	 * Analysis key word , " { "
	 */
	public static final String KEY_PARAM_STRING_KAKKO1 = "{";

	/**
	 * Analysis key word , " } "
	 */
	public static final String KEY_PARAM_STRING_KAKKO2 = "}";

	/**
	 * Analysis key word (byte[]) " [ "
	 */
	public static final String KEY_PARAM_BYTES_KAKKO1 = "[";

	/**
	 * Analysis key word (byte[]) " ] "
	 */
	public static final String KEY_PARAM_BYTES_KAKKO2 = "]";

	/**
	 * Analysis key word , " ' "
	 */
	public static final String KEY_PARAM_SINGLE_QUOTETION = "'";
/**
 * Constractor
 */
public SQLKeyWords() {
    super();
}
}
