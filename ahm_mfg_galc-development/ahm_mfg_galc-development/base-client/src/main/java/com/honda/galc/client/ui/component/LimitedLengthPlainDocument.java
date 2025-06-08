package com.honda.galc.client.ui.component;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Document that may limit the length of the text.
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
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
public class LimitedLengthPlainDocument extends PlainDocument {

	private static final long serialVersionUID = 1L;

	private int maxLength;
	
	public LimitedLengthPlainDocument() {
	}
	
	public LimitedLengthPlainDocument(int maxLength) {
		super();
		setMaxLength(maxLength);
	}
	
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		if (isLimitLength()) {
			str = checkLength(str);
			if (str.length() == 0) {
				return;
			}
		}
		super.insertString(offs, str, a);
	}

	protected String checkLength(String str) {
		if (getMaxLength() > 0) {
			int remainingCapacity = getMaxLength() - getLength();
			if (remainingCapacity > 0) {
				if (remainingCapacity < str.length()) {
					str = str.substring(0, remainingCapacity);
				}
			} else {
				return "";
			}
		}
		return str;
	}

	// === get/set === //
	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	protected boolean isLimitLength() {
		return true;
	}
}
