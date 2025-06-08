package com.honda.galc.client.teamleader.hold.qsr.release.defect;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import com.honda.galc.client.teamleader.hold.qsr.reason.AssociateInfoPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LimitedLengthPlainDocument;

public class DefectReleaseInputPanel extends JPanel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final String REASON_SELECTED = "REASON_SELECTED";
	private static final int ELEMENT_HEIGHT = 25;
	
	private JLabel reasonLabel;
	private JComboBox reasonInput;
	private AssociateInfoPanel associateInfoPanel;

	private JLabel commentLabel;
	private JTextField commentInput;

	private JButton cancelButton;
	private JButton submitButton;


	public DefectReleaseInputPanel() {
		init();
	}

	protected void init() {
		reasonLabel = createReasonLabel();
		reasonInput = createReasonInput();

		associateInfoPanel = createAssociateInfoPanel();

		commentLabel = createCommentLabel();
		commentInput = createCommentInput();

		cancelButton = createCancelButton();
		submitButton = createSubmitButton();

		setLayout(null);
		setSize(985, 4 * ELEMENT_HEIGHT + 35);
		setBorder(BorderFactory.createEtchedBorder());

		add(getReasonLabel());
		add(getReasonInput());

		add(getAssociateInfoPanel());
		
		add(getCommentLabel());
		add(getCommentInput());
		
		add(getCancelButton());
		add(getSubmitButton());
	}

	protected JLabel createReasonLabel() {
		JLabel element = new JLabel("Scrap");
		element.setSize(80, ELEMENT_HEIGHT);
		element.setLocation(10, 10);
		element.setFont(getLabelFont());
		return element;
	}

	protected JComboBox createReasonInput() {
		JComboBox element = new JComboBox();
		Component base = getReasonLabel();
		element.setActionCommand(REASON_SELECTED);
		element.setSize(480, ELEMENT_HEIGHT);
		element.setLocation(base.getX() + base.getWidth(), base.getY());
		element.setFont(getInputFont());
		element.setEditable(true);
		Document document = new LimitedLengthPlainDocument(80 - (5 + 1));
		JTextComponent editor = (JTextComponent) element.getEditor().getEditorComponent();
		editor.setDocument(document);
		element.setSelectedIndex(-1);
		return element;
	}
	
	private AssociateInfoPanel createAssociateInfoPanel() {
		associateInfoPanel = new AssociateInfoPanel();
		Component base = getReasonLabel();
		associateInfoPanel.setSize(800, ELEMENT_HEIGHT);

		associateInfoPanel.setLocation(base.getX(), base.getY() + base.getHeight() + 10);
		return associateInfoPanel;
	}

	protected JLabel createCommentLabel() {
		JLabel element = new JLabel("Comment");
		Component base = getAssociateInfoPanel();
		element.setSize(80, ELEMENT_HEIGHT);
		element.setLocation(base.getX(), base.getY() + base.getHeight() + 10);
		element.setFont(getLabelFont());
		return element;
	}

	protected JTextField createCommentInput() {
		JTextField element = new JTextField();
		Component base = getCommentLabel();
		element.setSize(555, ELEMENT_HEIGHT);
		element.setLocation(base.getX() + base.getWidth(), base.getY());
		element.setFont(getInputFont());
		Document document = new LimitedLengthPlainDocument(80);
		element.setDocument(document);
		element.setEnabled(false);
		return element;
	}

	protected JButton createCancelButton() {
		JButton element = new JButton();
		Component base = getCommentInput();
		element.setSize(160, 2 * ELEMENT_HEIGHT);
		element.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		element.setFont(getButtontFont());
		element.setText("Cancel");
		element.setMnemonic(KeyEvent.VK_C);
		return element;
	}

	protected JButton createSubmitButton() {
		JButton element = new JButton();
		Component base = getCancelButton();
		element.setSize(base.getWidth(), 2 * ELEMENT_HEIGHT);
		element.setLocation(base.getX() + base.getWidth() + 5, base.getY());
		element.setFont(getButtontFont());
		element.setText("Submit");
		element.setMnemonic(KeyEvent.VK_S);
		element.setEnabled(false);
		return element;
	}
	
	public JLabel getReasonLabel() {
		return reasonLabel;
	}
	
	public AssociateInfoPanel getAssociateInfoPanel() {
		return associateInfoPanel;
	}
	
	public JTextField getAssociateNameInput() {
		return getAssociateInfoPanel().getAssociateNameInput();
	}
	
	public JTextField getAssociateIdInput() {
		return getAssociateInfoPanel().getAssociateIdInput();
	}
	
	public JLabel getAssociateNameLabel() {
		return getAssociateInfoPanel().getAssociateNameLabel();
	}
	
	public JTextField getPhoneInput() {
		return getAssociateInfoPanel().getPhoneInput();
	}
	
	public JComboBox getReasonInput() {
		return reasonInput;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public JButton getSubmitButton() {
		return submitButton;
	}

	public JTextField getCommentInput() {
		return commentInput;
	}

	public JLabel getCommentLabel() {
		return commentLabel;
	}
	
	protected Font getLabelFont() {
		return Fonts.DIALOG_BOLD_12;
	}

	protected Font getInputFont() {
		return Fonts.DIALOG_BOLD_12;
	}

	protected Font getButtontFont() {
		return Fonts.DIALOG_BOLD_16;
	}
	
	protected int getElementHeight() {
		return ELEMENT_HEIGHT;
	}
}
