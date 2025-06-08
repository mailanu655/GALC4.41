package com.honda.galc.client.teamleader.hold.qsr.reason;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.Document;

import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LimitedLengthPlainDocument;

public class AssociateInfoPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int ELEMENT_HEIGHT = 25;
	
	private JLabel associateIdLabel;
	private JTextField associateIdInput;

	private JLabel associateNameLabel;
	private JTextField associateNameInput;

	private JLabel phoneLabel;
	private JTextField phoneInput;

	public AssociateInfoPanel() {
		super();
		init();
	}

	protected void init() {
		setLayout(null);

		add(createAssociateIdLabel());
		add(createAssociateIdInput());
		add(createAssociateNameLabel());
		add(createAssociateNameInput());

		add(createPhoneLabel());
		add(createPhoneInput());
	}

	protected JLabel createAssociateIdLabel() {
		associateIdLabel = new JLabel("Associate ID");
		associateIdLabel.setSize(80, ELEMENT_HEIGHT);
		associateIdLabel.setFont(getLabelFont());
		return associateIdLabel;
	}

	protected JTextField createAssociateIdInput() {
		associateIdInput = new JTextField();
		Component base = getAssociateIdLabel();
		associateIdInput.setSize(120, ELEMENT_HEIGHT);
		associateIdInput.setLocation(base.getX() + base.getWidth(), base.getY());
		associateIdInput.setFont(getInputFont());
		Document document = new LimitedLengthPlainDocument(11);
		associateIdInput.setDocument(document);
		associateIdInput.setEditable(false);
		associateIdInput.setFocusable(false);
		return associateIdInput;
	}

	protected JLabel createAssociateNameLabel() {
		associateNameLabel = new JLabel("Name", JLabel.RIGHT);
		Component base = getAssociateIdInput();
		associateNameLabel.setSize(50, ELEMENT_HEIGHT);
		associateNameLabel.setLocation(base.getX() + base.getWidth(), base.getY());
		associateNameLabel.setFont(getLabelFont());
		return associateNameLabel;
	}

	protected JTextField createAssociateNameInput() {
		associateNameInput = new JTextField();
		Component base = getAssociateNameLabel();
		associateNameInput.setSize(300, ELEMENT_HEIGHT);
		associateNameInput.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		associateNameInput.setFont(getInputFont());
		Document document = new LimitedLengthPlainDocument(30);
		associateNameInput.setDocument(document);
		associateNameInput.setEnabled(false);
		return associateNameInput;
	}

	protected JLabel createPhoneLabel() {
		phoneLabel = new JLabel("Phone", JLabel.RIGHT);
		Component base = getAssociateNameInput();
		phoneLabel.setSize(80, ELEMENT_HEIGHT);
		phoneLabel.setLocation(base.getX() + base.getWidth(), base.getY());
		phoneLabel.setFont(getLabelFont());
		return phoneLabel;
	}

	protected JTextField createPhoneInput() {
		phoneInput = new JTextField();
		Component base = getPhoneLabel();
		phoneInput.setSize(135, ELEMENT_HEIGHT);
		phoneInput.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		phoneInput.setFont(getInputFont());
		Document document = new LimitedLengthPlainDocument(20);
		phoneInput.setDocument(document);
		phoneInput.setEnabled(false);
		return phoneInput;
	}

	public JTextField getAssociateIdInput() {
		return associateIdInput;
	}

	public JLabel getAssociateIdLabel() {
		return associateIdLabel;
	}

	public JTextField getAssociateNameInput() {
		return associateNameInput;
	}

	public JLabel getAssociateNameLabel() {
		return associateNameLabel;
	}

	public JTextField getPhoneInput() {
		return phoneInput;
	}

	public JLabel getPhoneLabel() {
		return phoneLabel;
	}

	protected Font getLabelFont() {
		return Fonts.DIALOG_BOLD_12;
	}

	protected Font getInputFont() {
		return Fonts.DIALOG_BOLD_12;
	}
}
