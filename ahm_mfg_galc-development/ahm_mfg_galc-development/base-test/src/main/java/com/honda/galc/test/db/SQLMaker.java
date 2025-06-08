package com.honda.galc.test.db;

import java.util.Vector;

/**
 * <h3>Interface of convert SQL skelton into actual SQL</h3>
 * <h4>Description</h4>
 * If create a new convert SQL skelton class, that class need to implement this interface.<br>
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
 * <TR>
 * <TD>M.Hayashibe</TD>
 * <TD>(2001/11/02 14:00:00)</TD>
 * <TD>0.1</TD><TD>(none)</TD>
 * <TD>Revise Javadoc</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author T.Shimode
 */
public interface SQLMaker {
/**
 *  Deprecated, not in use.
 * <p>
 * @return Vector
 */
Vector<Object> getBlobParams();
/**
 * Gets actual SQL.
 * <p>
 * @return actual SQL
 */
String getSqlStatement();
/**
 * Select SQL or not.
 * <p>
 * @return booelan
 */
boolean isQuery();
/**
 * Convert SQL skelton into actual SQL.<br>
 * <p>
 * @param sqlSkelton SQL skelton
 * @param sqlParam  parameter of SQL skelton.
 * @exception IllegalSQLRequestException Thrown when fail to convert SQL skelton into actual SQL.
 */
void runMakeSql(String aSQLSkelton, Vector<Object> aSQLParam)
	throws IllegalSQLRequestException;
}
