package com.honda.galc.common;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * It is file exception class.<br>
 * This class  is thrown when fie process fails.<br>
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
 * <TD>(2001/01/16 15:42:50)</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR></TABLE>
 * @see 
 * @ver 0.1
 * @author Larry Karpov
 * Since 11/14/2013
 * 
 */
public class OIFParsingData extends Exception {
	
	private static final long serialVersionUID = 3602768476048016476L;

	/**
	 * Constructor
	 */
	public OIFParsingData() {
		super();
	}
	
	/**
	 * Constructor
	 * @param s MessageID
	 */
	public OIFParsingData(String s) {
		super(s);
	}

	/**
	 * Change to string 
	 * <p>
	 * @return Exception string
	 */
	public String toString() {
		return "OIFFileUtilityException";
	}

}
