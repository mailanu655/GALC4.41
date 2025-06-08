package com.honda.galc.client.qics.view.screen;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.TextFieldState;





public class IPPInputPanel extends JPanel {


	private static final long serialVersionUID = 1L;
	private JLabel ippTagLabel;
	private JTextField ippTagInputElement;
	
	
	public IPPInputPanel() {
		initialize(770, 165);
	}

	public IPPInputPanel(int width, int height) {
		initialize(width, height);
	}

	protected void initialize(int width, int height) {
		setLayout(null);
		setSize(width, height);
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		ippTagLabel = createIppTagLabel();
		add(getIppTagLabel());
		ippTagInputElement = createIppTagInputElement();
		add(getIppTagInputElement());
	}


	
	protected JLabel createIppTagLabel() {
		JLabel label = new JLabel("IPP Tag Number", JLabel.LEFT);
		label.setFont(Fonts.DIALOG_PLAIN_18);
		label.setForeground(Color.black);
		label.setSize(175, 60);
		label.setLocation(10,15);
		return label;
	}

	
	public JTextField createIppTagInputElement() {
		JTextField textField = new JTextField();
		textField.setText("");
		TextFieldState.EDIT.setState(textField);
		textField.setFont(Fonts.DIALOG_PLAIN_36);
		textField.setAlignmentX(Component.LEFT_ALIGNMENT);
		textField.setEditable(true);
		textField.setRequestFocusEnabled(true);
		textField.setName("ippTagInputElement");
		textField.setLocation(getIppTagLabel().getX() + getIppTagLabel().getWidth(), getIppTagLabel().getY());
		int width = 400;
		textField.setSize(width,50);
		textField.setForeground(Color.WHITE);
		textField.setBackground(Color.BLUE);
		return textField;
	}

	public JTextField getIppTagInputElement() {
		return ippTagInputElement;
	}

	public void setIppTagInputElement(JTextField ippTagInputElement) {
		this.ippTagInputElement = ippTagInputElement;
	}

	public JLabel getIppTagLabel() {
		return ippTagLabel;
	}

	public void setIppTagLabel(JLabel ippTagLabel) {
		this.ippTagLabel = ippTagLabel;
	}

	
}
