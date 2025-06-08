package com.honda.galc.client.product.validator;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>LengthValidator</code> is ... .
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
public class LengthValidator extends AbstractValidator {

	private List<Integer> lengths;

	public LengthValidator(Integer... lengths) {
		this.lengths = new ArrayList<Integer>();
		if (lengths != null) {
			getLengths().addAll(Arrays.asList(lengths));
		}
		setDetailedMessageTemplate("{0} excpected length: {1}");
	}

	public boolean execute(String value) {
		if (value == null) {
			return false;
		}
		if (getLengths() == null || getLengths().isEmpty()) {
			return true;
		}
		if (getLengths().contains(value.length())) {
			return true;
		}
		return false;
	}

	@Override
	public String getMessage(String propertyName) {
		return MessageFormat.format(getMessageTemplate(), propertyName, getLengthsAsString());
	}

	protected List<Integer> getLengths() {
		return lengths;
	}

	protected String getLengthsAsString() {
		if (getLengths() == null || getLengths().size() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int l : getLengths()) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(l);
		}
		return sb.toString();
	}
}
