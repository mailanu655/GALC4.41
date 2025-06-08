package com.honda.galc.client.product.validator;

import java.text.MessageFormat;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>RegExpValidator</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
public class RegExpValidator extends AbstractValidator {

	private String pattern;

	public RegExpValidator(String pattern) {
		this.pattern = pattern;
		setDetailedMessageTemplate("{0} must match: {1}");
	}

	public boolean execute(String value) {
		if (value == null || getPattern() == null) {
			return false;
		}
		return value.matches(getPattern());
	}

	// === get/set === //
	protected String getPattern() {
		return pattern;
	}

	@Override
	public String getMessage(String propertyName) {
		return MessageFormat.format(getMessageTemplate(), propertyName, getPattern());
	}

	@Override
	public String getDetailedMessage(String propertyName) {
		return MessageFormat.format(getDetailedMessageTemplate(), propertyName, getPattern());
	}
}
