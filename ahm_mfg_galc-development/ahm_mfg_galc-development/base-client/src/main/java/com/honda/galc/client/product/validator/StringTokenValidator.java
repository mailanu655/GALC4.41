package com.honda.galc.client.product.validator;

import java.text.MessageFormat;

import com.honda.galc.client.product.command.Command;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>StringTokenValidator</code> is ... .
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
public class StringTokenValidator extends AbstractValidator {

	private int startIx;
	private int endIx;

	private String tokenName;

	private Command validator;

	public StringTokenValidator(String tokenName, int startIx, int endIx, Command validator) {
		this.tokenName = tokenName;
		this.startIx = startIx;
		this.endIx = endIx;
		this.validator = validator;

		if (getStartIx() < 0) {
			setStartIx(0);
		}
		if (getEndIx() < 0) {
			setEndIx(0);
		}
		setDetailedMessageTemplate("{1}");
	}

	public boolean execute(String value) {
		if (value == null) {
			return false;
		}
		if (value.length() < getEndIx() + 1) {
			return false;
		}
		String token = value.substring(getStartIx(), getEndIx() + 1);
		return getValidator().execute(token);
	}

	@Override
	public String getMessage(String propertyName) {
		String msg1 = getValidator().getDetailedMessage(getTokenNameMessage());
		String msg = MessageFormat.format(getMessageTemplate(), propertyName, msg1);
		return msg;
	}

	public String getMessage() {
		return getValidator().getMessage(getTokenNameMessage());
	}

	protected int getStartIx() {
		return startIx;
	}

	protected int getEndIx() {
		return endIx;
	}

	protected void setStartIx(int startIx) {
		this.startIx = startIx;
	}

	protected void setEndIx(int endIx) {
		this.endIx = endIx;
	}

	protected String getTokenName() {
		return tokenName;
	}

	protected String getTokenNameMessage() {
		String msg = null;
		if (getStartIx() == getEndIx()) {
			msg = String.format("%s ix[%s]", getTokenName(), getStartIx() + 1);
		} else {
			msg = String.format("%s ix[%s, %s]", getTokenName(), getStartIx() + 1, getEndIx() + 1);
		}
		return msg;
	}

	protected Command getValidator() {
		return validator;
	}
}
