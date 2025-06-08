package com.honda.galc.client.ui.component;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>DecimalDocument</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Karol Wozniak</TD>
 * <TD>May 7, 2009</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class DecimalDocument extends LimitedLengthPlainDocument {

	private static final long serialVersionUID = 1L;

	public DecimalDocument() {
		super();
	}

	public DecimalDocument(int maxLength) {
		super(maxLength);
	}

	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		try {
			String text = getText(0, getLength()) + str;
			if (str.trim().equalsIgnoreCase("d") || str.trim().equalsIgnoreCase("f")) {
				return;
			}
			Double.parseDouble(text);
			super.insertString(offs, str, a);
		} catch (NumberFormatException e) {
			//
		}
	}
}
