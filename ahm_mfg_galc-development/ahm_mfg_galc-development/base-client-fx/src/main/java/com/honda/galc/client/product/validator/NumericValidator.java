package com.honda.galc.client.product.validator;

import org.apache.commons.lang.StringUtils;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>NumericValidator</code> is ... .
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
public class NumericValidator extends AbstractValidator {

	public NumericValidator() {
		setDetailedMessageTemplate("{0} must be numeric");
	}

	public boolean execute(String value) {
		return StringUtils.isNumeric((String) value);
	}
}
