package com.honda.galc.client.ui.component;

import java.awt.Color;

import javax.swing.JComboBox;
import javax.swing.JTextField;

public enum ComboBoxState {

	EXPANDED(true, true, false,  Color.GREEN, Color.BLACK),
	COLLAPSED(true, true, false, Color.GREEN, Color.BLACK),
	DISABLED(false, true, true, Color.LIGHT_GRAY, Color.GRAY);
	
	private boolean enabled;
	private boolean editable;
	private boolean focusable;
	private Color backgroundColor;
	private Color foregroundColor;

	private ComboBoxState() {}

	private ComboBoxState(boolean enabled, boolean editable, boolean focusable, Color backgroundColor, Color foregroundColor) {
		this.enabled = enabled;
		this.editable = editable;
		this.focusable = focusable;
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
	}

	// === get/set === //
	public Color getBackgroundColor() {
		return this.backgroundColor;
	}

	protected boolean isEnabled() {
		return this.enabled;
	}
	
	protected boolean isEditable() {
		return this.editable;
	}
	
	protected boolean isFocusable() {
		return this.focusable;
	}

	public Color getForegroundColor() {
		return this.foregroundColor;
	}

	public void setState(JComboBox comboBox) {
		if (comboBox == null) {
			return;
		}

		comboBox.setEnabled(isEnabled());
		comboBox.setEditable(isEditable());
		comboBox.setFocusable(isFocusable());
		comboBox.setRequestFocusEnabled(isEnabled());
		comboBox.putClientProperty("state", this);
		((JTextField)comboBox.getEditor().getEditorComponent()).setBackground(getBackgroundColor());
		((JTextField)comboBox.getEditor().getEditorComponent()).setForeground(getForegroundColor());
	}
}
