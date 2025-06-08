package com.honda.galc.client.product.validator;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>DateValidator</code> is ... .
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
public class DateValidator extends AbstractValidator {

	private String[] patterns;
	private List<DateFormat> formats;

	public DateValidator(String... patterns) {
		this.patterns = patterns;
		this.formats = new ArrayList<DateFormat>();
		if (patterns == null) {
			return;
		}
		for (String pattern : getPatterns()) {
			if (pattern != null) {
				DateFormat df = new SimpleDateFormat(pattern.trim());
				getFormats().add(df);
			}
		}
		setDetailedMessageTemplate("{0} excpected date pattern: {1}");
	}

	public DateValidator(boolean lenient, String... patterns) {
		this(patterns);
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
				df.parse(value);
				return true;
			} catch (ParseException e) {
			}
		}
		return false;
	}

	@Override
	public String getDetailedMessage(String propertyName) {
		return MessageFormat.format(getDetailedMessageTemplate(), propertyName, getPatternsAsString());
	}

	protected String getPatternsAsString() {
		if (getPatterns() == null || getPatterns().length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String str : getPatterns()) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(str);
		}
		return sb.toString();
	}

	public String[] getPatterns() {
		return patterns;
	}

	public List<DateFormat> getFormats() {
		return formats;
	}
}
