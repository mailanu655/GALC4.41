package com.honda.galc.client.product.validator;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>IntegerGreaterValidator</code> is ... .
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
 * @created Apr 23, 2013
 */
public class IntegerGreaterValidator extends AbstractValidator {

	private int lowValue;
	private boolean inclusive;

	public IntegerGreaterValidator(int lowValue) {
		this.lowValue = lowValue;
		setDetailedMessageTemplate("{0} must be greater than  {1}");
	}

	public IntegerGreaterValidator(int lowValue, boolean inclusive) {
		this(lowValue);
		this.inclusive = inclusive;
		if (isInclusive()) {
			setDetailedMessageTemplate("{0} must be greater than or equal {1}");
		}
	}

	public boolean execute(String value) {
		if (!StringUtils.isNumeric(value)) {
			setDetailedMessageTemplate("{0} must be numeric");
			return false;
		}
		int i = Integer.valueOf(value);
		if (isInclusive()) {
			return i >= getLowValue();
		} else {
			return i > getLowValue();
		}
	}

	@Override
	public String getDetailedMessage(String propertyName) {
		return MessageFormat.format(getDetailedMessageTemplate(), propertyName, getLowValue());
	}

	public int getLowValue() {
		return lowValue;
	}

	public boolean isInclusive() {
		return inclusive;
	}
}
