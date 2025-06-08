package com.honda.galc.client.product.validator;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PassedDateValidator</code> is ... .
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
 * @created Apr 22, 2013
 */
public class PassedDateValidator extends DateValidator {

	private Date date;
	private boolean inclusive;

	public PassedDateValidator(Date date, boolean inclusive, String... patterns) {
		super(patterns);
		this.inclusive = inclusive;
		this.date = date;
		if (isInclusive()) {
			setDetailedMessageTemplate("{0} must be before or on: {1}");
		} else {
			setDetailedMessageTemplate("{0} must be before: {1}");
		}
	}

	public PassedDateValidator(boolean lenient, Date date, boolean inclusive, String... patterns) {
		this(date, inclusive, patterns);
		for (DateFormat df : getFormats()) {
			df.setLenient(lenient);
		}
	}

	public boolean execute(String value) {
		if (getFormats() == null || getFormats().isEmpty()) {
			return false;
		}
		for (DateFormat df : getFormats()) {
			try {
				Date d = df.parse(value);
				if (isInclusive() && d.equals(getDate()) || d.before(getDate())) {
					return true;
				} else {
					return false;
				}

			} catch (ParseException e) {
			}
		}
		return false;
	}

	@Override
	public String getDetailedMessage(String propertyName) {
		return MessageFormat.format(getDetailedMessageTemplate(), propertyName, getDate());
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isInclusive() {
		return inclusive;
	}
}
