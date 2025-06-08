package com.honda.galc.client.ui.component;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * This class Extends PlainDocument.
 * This method restrict that a user inputs a character except for the number.
 * <h4>Usage and Example</h4>
 * 
 * <h4>Special Notes</h4>
 * 
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
 * <TD>M.Hayasibe</TD>
 * <TD>(2001/03/17 13:11:30)</TD>
 * <TD>0.1</TD><TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author M.Hayasibe
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class NumericDocument extends javax.swing.text.PlainDocument {
	
	private static final long serialVersionUID = 1L;
	private java.lang.String validValues = "1234567890";
	private int limit;
    /**
     * Constructor
     */
    public NumericDocument() {
        super();
        limit = 256;
    }
    /**
     * Constructor
     * 
     * @param limit int
     */
    public NumericDocument(int limit) {
        super();
        this.limit = limit;
    }
    /**
     * Costructor
     *
     * @param c javax.swing.text.AbstractDocument.Content
     */
    protected NumericDocument(javax.swing.text.AbstractDocument.Content c) {
        super(c);
    }
	/**
	 * This method restrict that a user inputs a character except for the number.
	 * 
	 * @param offset int
	 * @param str java.lang.String
	 * @param a javax.swing.text.AttributeSet
	 */
	public void insertString(
		int offset, 
		String str, 
		javax.swing.text.AttributeSet a) {

		for (int i = 0; i < str.length(); i++) {
			if (validValues.indexOf(str.charAt(i)) == -1) {
				return;
			}
		}
		if (getLength() + str.length() > limit) {
			return;
		}
		try {
			super.insertString(offset, str, a);
		}
		catch (javax.swing.text.BadLocationException e) {

		}
	}
}
