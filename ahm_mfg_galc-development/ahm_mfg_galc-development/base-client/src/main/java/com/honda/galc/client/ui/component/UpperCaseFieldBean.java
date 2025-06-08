package com.honda.galc.client.ui.component;

import javax.swing.text.*;

import com.honda.galc.common.logging.Logger;
/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * TextField which converts lower case characters to upper case characters when they are displayed.
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
 * <TD>Y.Kawada</TD>
 * <TD>(Nov/02/2001 10:04:35)</TD>
 * <TD>1.0.1</TD>
 * <TD>(none)</TD>
 * <TD>Modify Javadoc</TD>
 * </TR>
 * </TABLE>
 * @see LengthFieldBean
 * @author M.Satoh
 * @version 1.0
 */

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class UpperCaseFieldBean extends LengthFieldBean {

	private static final long serialVersionUID = 1L;
	
	public UpperCaseFieldBean() {
		super();
	}
	
	public UpperCaseFieldBean(ITextFieldRender fieldRender) {
		super(fieldRender);
	}

	protected class UpperCaseDocument extends LengthControlDocument {

		private static final long serialVersionUID = 1L;

		public void insertString(int offs,String str,AttributeSet a) throws BadLocationException{
			try{
				super.insertString(offs,str.toUpperCase(),a);
			}
			catch(BadLocationException e){
				Logger.getLogger().warn(e, this.getClass().getSimpleName() + "::insertString() exception.");
			}
		}
	}
	
	protected Document createDefaultModel() {
		return new UpperCaseDocument();
	}
	
	/**
	 * Set the specified text to the text field, after converting lower case characters to upper case characters if the text contains lower case characters.
	 * @param t - new string to be set to the text field.  
	 * @exception IndexOutOfBoundsException - There is some disintegrity within the specified string.
	 */
	public void setText(String t) {
		String text;
		text = t == null ? null: t.toUpperCase();
		try{
			super.setText(text);

		}catch(IndexOutOfBoundsException e){
			Logger.getLogger().warn(e, this.getClass().getSimpleName() + "::setText() exception.");
		}
	}
}
