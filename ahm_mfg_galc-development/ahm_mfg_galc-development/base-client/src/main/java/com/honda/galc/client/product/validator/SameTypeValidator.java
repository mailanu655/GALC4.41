package com.honda.galc.client.product.validator;

import java.util.Collection;

import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.component.TextFieldState;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>SameTypeValidator</code> is ... .
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
 * @created Oct 20, 2014
 */
public class SameTypeValidator extends AbstractValidator {

	private Collection<JTextField> fields;

	enum Type {
		NUMERIC, ALPHA
	};

	public SameTypeValidator(Collection<JTextField> fields) {
		this.fields = fields;
		setDetailedMessageTemplate("all {0}(s) must be Alpha or all {0}(s) must be Numeric");
	}

	public boolean execute(String value) {
		Type type = getType();
		if (type == null) {
			if (StringUtils.isNumeric(value)) {
				return true;
			}
			if (StringUtils.isAlpha(value)) {
				return true;
			}
			return false;
		}

		if (Type.NUMERIC.equals(type)) {
			return StringUtils.isNumeric(value);
		}
		if (Type.ALPHA.equals(type)) {
			return StringUtils.isAlpha(value);
		}
		return false;
	}

	protected Type getType() {

		if (getFields() == null || getFields().isEmpty()) {
			return null;
		}
		for (JTextField field : getFields()) {
			if (TextFieldState.READ_ONLY.isInState(field)) {
				String str = StringUtils.trim(field.getText());
				if (StringUtils.isNumeric(str)) {
					return Type.NUMERIC;
				}
				if (StringUtils.isAlpha(str)) {
					return Type.ALPHA;
				}
			}
		}
		return null;
	}

	protected Collection<JTextField> getFields() {
		return fields;
	}
}
