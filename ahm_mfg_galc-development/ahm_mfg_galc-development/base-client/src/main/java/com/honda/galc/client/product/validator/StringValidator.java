package com.honda.galc.client.product.validator;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>StringValidator</code> is ... .
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
public class StringValidator extends AbstractValidator {

	private List<String> values;

	public StringValidator(String values) {
		this.values = new ArrayList<String>();
		if (values == null || StringUtils.isBlank(values)) {
			return;
		}
		String[] ar = values.split(",");
		for (String str : ar) {
			if (StringUtils.isBlank(str)) {
				continue;
			}
			str = str.trim();
			getValues().add(str);
		}
		setDetailedMessageTemplate("{0} expected value: {1}");
	}

	public StringValidator(String[] values) {
		this.values = new ArrayList<String>();
		if (values == null || values.length == 0) {
			return;
		}
		for (String str : values) {
			if (StringUtils.isBlank(str)) {
				continue;
			}
			str = str.trim();
			getValues().add(str);
		}
		setDetailedMessageTemplate("{0} expected value: {1}");
	}
	
	public boolean execute(String value) {
		if (getValues() == null || getValues().isEmpty()) {
			return true;
		}
		if (getValues().contains(value)) {
			return true;
		}
		return false;
	}

	@Override
	public String getMessage(String propertyName) {
		return MessageFormat.format(getMessageTemplate(), propertyName, getValuesAsString());
	}

	@Override
	public String getDetailedMessage(String propertyName) {
		return MessageFormat.format(getDetailedMessageTemplate(), propertyName, getValuesAsString());
	}

	protected String getValuesAsString() {
		if (getValues() == null || getValues().size() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (String str : getValues()) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(str);
		}
		return sb.toString();
	}

	protected List<String> getValues() {
		return values;
	}
}
