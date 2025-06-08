package com.honda.galc.client.codebroadcast;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class CodeBroadcastConfirmationField extends JButton {

	private static final long serialVersionUID = 1L;
	private CodeBroadcastCode code;
	private boolean confirmationEnabled;

	public CodeBroadcastConfirmationField() {
		super();
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (CodeBroadcastConfirmationField.this.confirmationEnabled)
					CodeBroadcastConfirmationField.this.code.setConfirmed(!CodeBroadcastConfirmationField.this.code.getConfirmed());
			}
		});
	}

	public CodeBroadcastCode getCode() {
		return this.code;
	}

	public void setCode(CodeBroadcastCode code) {
		this.code = code;
	}

	public boolean isConfirmationEnabled() {
		return this.confirmationEnabled;
	}

	public void setConfirmationEnabled(boolean confirmationEnabled) {
		this.confirmationEnabled = confirmationEnabled;
	}
}
