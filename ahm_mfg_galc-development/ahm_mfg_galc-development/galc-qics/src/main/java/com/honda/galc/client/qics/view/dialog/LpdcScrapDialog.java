package com.honda.galc.client.qics.view.dialog;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import com.honda.galc.client.qics.property.QicsPropertyBean;
import com.honda.galc.client.qics.validator.QicsValidator;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LimitedLengthPlainDocument;
import com.honda.galc.client.ui.component.UpperCaseDocument;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.util.ValidatorUtils;

/**
 * 
 * @author is011586
 * 
 */

public class LpdcScrapDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private JComboBox comment;
	private JComboBox reason;
	private JComboBox associateIdField;
	private JLabel associateNumberLabel;
	private JLabel messageLabel;
	private JLabel reasonLabel;
	private JLabel commentLabel;
	private JButton okButton;
	private JButton cancelButton;
	private ExceptionalOut returnValue;
	private String message;
	private boolean cancelled = true;
	private int inputLength;

	public LpdcScrapDialog(QicsFrame frame, String title, String message) {
		super(frame, title, true);
		this.message = message;
		getMessageLabel().setText(this.message);
		initialize();
		initFromLpdc(frame);
	}

	protected void initialize() {
		setInputLength(QicsValidator.MAX_INPUT_ASSOCIATE_NUMBER_LENGTH);
		setContentPane(getContentPanel());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(500, 220);
		mapActions();
	}

	protected void initFromLpdc(QicsFrame frame) {
		QicsPropertyBean propertyBean = frame.getQicsController().getQicsPropertyBean();
		setDefectList(propertyBean.getDefectList());
		setLocationList(propertyBean.getLocationList());
	}

	protected void setDefectList(String[] defectNames) {
		getComment().setModel(setComboBoxModel(defectNames));

		if (getComment().getComponentCount() > 0) {
			getComment().setSelectedIndex(0);
		}
	}

	protected void setLocationList(String[] locationNames) {
		getReason().setModel(setComboBoxModel(locationNames));

		if (getReason().getComponentCount() > 0) {
			getReason().setSelectedIndex(0);
		}
	}

	protected ComboBoxModel setComboBoxModel(String[] data) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < data.length; i++) {
			list.add(data[i]);
		}
		ComboBoxModel model = new DefaultComboBoxModel(new Vector<String>(list));
		return model;
	}

	public void setReturnValue() {
		ExceptionalOut scrap = new ExceptionalOut();
		scrap.setAssociateNo(getAssociateIdField().getSelectedItem().toString());

		String defectName = "";
		String location = "";

		// Defect is put in the EXCEPTIONAL_OUT_REASON_STRING column
		if (getComment().getSelectedItem() != null) {
			defectName = getComment().getSelectedItem().toString();
		}

		// Location is put in the EXCEPTIONAL_OUT_COMMENT column
		if (getReason().getSelectedItem() != null) {
			location = getReason().getSelectedItem().toString();
		}

		scrap.setExceptionalOutReasonString(defectName);
		scrap.setLocation(location);
		setReturnValue(scrap);
	}

	protected JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.setLayout(new GridBagLayout());

			int row = 0;
			int col = 0;

			GridBagConstraints constraints = null;
			constraints = new GridBagConstraints();
			constraints.gridx = col;
			constraints.gridy = row;
			constraints.insets = new Insets(getMargin(), getMargin(), getSpacing(), getSpacing());
			constraints.anchor = GridBagConstraints.WEST;
			constraints.gridwidth = 2;
			contentPanel.add(getMessageLabel(), constraints);

			// new row
			col = 0;
			row++;
			constraints = new GridBagConstraints();
			constraints.gridx = col;
			constraints.gridy = row;
			constraints.insets = new Insets(getSpacing(), getMargin(), getSpacing(), getSpacing());
			constraints.anchor = GridBagConstraints.WEST;
			contentPanel.add(getCommentLabel(), constraints);

			col++;
			constraints = new GridBagConstraints();
			constraints.gridx = col;
			constraints.gridy = row;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.weightx = 1;
			constraints.insets = new Insets(getMargin(), getSpacing(), getSpacing(), getMargin());
			contentPanel.add(getComment(), constraints);

			// new row
			col = 0;
			row++;
			constraints = new GridBagConstraints();
			constraints.gridx = col;
			constraints.gridy = row;
			constraints.insets = new Insets(getSpacing(), getMargin(), getSpacing(), getSpacing());
			constraints.anchor = GridBagConstraints.WEST;
			contentPanel.add(getReasonLabel(), constraints);

			col++;
			constraints = new GridBagConstraints();
			constraints.gridx = col;
			constraints.gridy = row;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.insets = new Insets(getSpacing(), getSpacing(), getSpacing(), getMargin());
			constraints.gridwidth = 3;
			contentPanel.add(getReason(), constraints);

			// new row
			col = 0;
			row++;
			constraints = new GridBagConstraints();
			constraints.gridx = col;
			constraints.gridy = row;
			constraints.insets = new Insets(getSpacing(), getMargin(), getMargin(), getSpacing());
			constraints.anchor = GridBagConstraints.WEST;
			contentPanel.add(getAssociateNumberLabel(), constraints);

			JPanel p = new JPanel();
			p.setLayout(new GridLayout(1, 3, getSpacing(), getSpacing()));
			p.add(getAssociateIdField());
			p.add(getCancelButton());
			p.add(getOkButton());

			col++;
			constraints = new GridBagConstraints();
			constraints.gridx = col;
			constraints.gridy = row;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.insets = new Insets(getSpacing(), getSpacing(), getMargin(), getMargin());
			contentPanel.add(p, constraints);
		}
		return contentPanel;
	}

	protected int getMargin() {
		return 15;
	}

	protected int getSpacing() {
		return 5;
	}

	protected Font getLabelFont() {
		return Fonts.DIALOG_BOLD_16;
	}

	protected Font getInputFont() {
		return Fonts.DIALOG_PLAIN_18;
	}

	public JLabel getCommentLabel() {
		if (commentLabel == null) {
			commentLabel = new JLabel("Defect: ");
			commentLabel.setFont(getLabelFont());
		}
		return commentLabel;
	}

	public JLabel getReasonLabel() {
		if (reasonLabel == null) {
			reasonLabel = new JLabel("Location: ");
			reasonLabel.setFont(getLabelFont());
		}
		return reasonLabel;
	}

	public JComboBox getReason() {
		if (reason == null) {
			reason = new JComboBox();
			JTextComponent editor = (JTextComponent) reason.getEditor().getEditorComponent();
			editor.setDocument(new LimitedLengthPlainDocument(64));
			reason.setFont(getInputFont());
		}
		return reason;
	}

	public JComboBox getComment() {
		if (comment == null) {
			comment = new JComboBox();
			JTextComponent editor = (JTextComponent) comment.getEditor().getEditorComponent();
			editor.setDocument(new LimitedLengthPlainDocument(64));
			comment.setFont(getInputFont());
		}
		return comment;
	}

	public JLabel getAssociateNumberLabel() {
		if (associateNumberLabel == null) {
			associateNumberLabel = new JLabel("Associate#: ");
			associateNumberLabel.setFont(getLabelFont());
		}
		return associateNumberLabel;
	}

	protected JComboBox getAssociateIdField() {
		if (associateIdField == null) {
			associateIdField = new JComboBox();
			associateIdField.setName("associateId");
			associateIdField.setBounds(10, 20, 190, 30);
			associateIdField.setFont(Fonts.DIALOG_PLAIN_18);
			JTextComponent editor = (JTextComponent) associateIdField.getEditor().getEditorComponent();
			editor.setDocument(new UpperCaseDocument(8));
			associateIdField.setEditable(true);
		}
		return associateIdField;
	}

	public int getInputLength() {
		return inputLength;
	}

	public void setInputLength(int numberLength) {
		this.inputLength = numberLength;
	}

	public JLabel getMessageLabel() {
		if (messageLabel == null) {
			messageLabel = new JLabel(message);
			messageLabel.setFont(getLabelFont());
		}
		return messageLabel;
	}

	// protected void setEditable(boolean editable) {
	// this.editable = editable;
	// }

	public JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("Ok");
			okButton.setFont(Fonts.DIALOG_PLAIN_18);
			okButton.setMnemonic(KeyEvent.VK_O);
			okButton.setPreferredSize(new Dimension(90, 50));
			okButton.setSize(90, 50);
			okButton.setLocation(10, 10);
		}
		return okButton;
	}

	protected JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.setPreferredSize(new Dimension(90, 50));
			cancelButton.setMnemonic(KeyEvent.VK_C);
			cancelButton.setFont(Fonts.DIALOG_PLAIN_18);
			cancelButton.setSize(90, 50);
			cancelButton.setLocation(getOkButton().getX() + getOkButton().getWidth() + 10, getOkButton().getY());
		}
		return cancelButton;
	}

	protected void mapActions() {

		getOkButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (getAssociateIdField().getSelectedItem() != null) {
					onOkButton();
				}
			}
		});

		getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCancelButton();
			}
		});
	}

	public void onOkButton() {
		setReturnValue();
		Object value = getReturnValue();
		List<String> messages = validate(value);
		if (messages.isEmpty()) {
			dispose();
			setCancelled(false);
		} else {
			JOptionPane.showMessageDialog(this, ValidatorUtils.formatMessages(messages));
		}
	}

	protected void onCancelButton() {
		setCancelled(true);
		dispose();
	}
	
	public void loadComboBox(Object[] items) {
		ComboBoxModel model = null;
		if (items == null) {
			model = new DefaultComboBoxModel();
		} else {
			model = new DefaultComboBoxModel(items);
		}
		getAssociateIdField().setModel(model);
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	protected List<String> validate(Object value) {
		return new ArrayList<String>();
	}

	public void setReturnValue(ExceptionalOut returnValue) {
		this.returnValue = returnValue;
	}

	public ExceptionalOut getReturnValue() {
		return returnValue;
	}
}
