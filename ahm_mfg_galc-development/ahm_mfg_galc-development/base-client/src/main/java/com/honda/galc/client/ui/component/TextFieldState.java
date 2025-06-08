package com.honda.galc.client.ui.component;

import java.awt.Color;

import javax.swing.JTextField;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>TextFieldState</code> is ...
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
 * <TD>Karol Wozniak</TD>
 * <TD>Apr 7, 2009</TD>
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
public enum TextFieldState {

	DEFAULT(true, Color.WHITE, Color.BLACK),
	DISABLED(false, Color.LIGHT_GRAY, Color.GRAY),

	EDIT(true, Color.BLUE, Color.WHITE), 
	READ_ONLY(false, Color.GREEN, Color.BLACK),
  	EDITABLE(true, Color.GREEN, Color.BLACK),
	ERROR(true, Color.RED, Color.BLACK), 
	ERROR_READ_ONLY(false, Color.RED, Color.BLACK);
	
	private boolean enabled;
	private Color backgroundColor;
	private Color foregroundColor;

	private TextFieldState() {

	}

	private TextFieldState(boolean enabled, Color backgroundColor, Color foregroundColor) {
		this.enabled = enabled;
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
	}

	// === get/set === //
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	protected boolean isEnabled() {
		return enabled;
	}

	public Color getForegroundColor() {
		return foregroundColor;
	}

	public void setState(JTextField field) {
		if (field == null) {
			return;
		}

		field.setEnabled(isEnabled());
		field.setRequestFocusEnabled(isEnabled());
		field.putClientProperty("state", this);

		field.setBackground(getBackgroundColor());
		field.setCaretColor(getForegroundColor());
		field.setForeground(getForegroundColor());
		field.setDisabledTextColor(getForegroundColor());
		if(field instanceof UpperCaseFieldBean)
			((UpperCaseFieldBean)field).setColor(getBackgroundColor());

		if (!field.isEditable()) {
			if (field.isEnabled()) {
				field.setEnabled(false);
			}
			if (this.equals(EDIT)) {
				field.setBackground(DISABLED.getBackgroundColor());
			}
		}
	}

	public boolean isInState(JTextField field) {
		if (field == null) {
			return false;
		}
		return this.equals(field.getClientProperty("state"));
	}
}
