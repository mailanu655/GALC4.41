package com.honda.galc.client.ui.component;

import javax.swing.JPasswordField;


public class LabeledPasswordField extends LabeledComponent<JPasswordField> {

	private static final long serialVersionUID = 1L;

	
	public LabeledPasswordField(String labelName) {
		super(labelName, new JPasswordField());
		getComponent().setColumns(20);
		this.setInsets(10, 10, 10, 10);
	}
}
