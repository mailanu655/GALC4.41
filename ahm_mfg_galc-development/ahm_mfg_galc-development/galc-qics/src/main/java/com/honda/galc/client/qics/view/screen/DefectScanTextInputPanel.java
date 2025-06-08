package com.honda.galc.client.qics.view.screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;






public class DefectScanTextInputPanel extends JPanel {


	private static final long serialVersionUID = 1L;
	public UpperCaseFieldBean statusTextField = null;
	public UpperCaseFieldBean defectTextField = null;
	public UpperCaseFieldBean entryTextField = null;




	public DefectScanTextInputPanel() {
		super();
		initialize(770, 165);
	}

	public DefectScanTextInputPanel(int width, int height) {
		super();
		initialize(width, height);
	}


	private void initialize(int width, int height) {

		GridBagLayout gridBagLayout = new GridBagLayout();
		setLayout(gridBagLayout);
		setSize(width, height);
		setBackground(new Color(204,204,204));
		add(getStatusTextField(), getDefaultTextFieldContraints());
		add(getDefectTextField(), getDefaultTextFieldContraints());
		add(getEntryTextField(), getDefaultTextFieldContraints());
		entryTextField.requestFocusInWindow();
	}

	public UpperCaseFieldBean getStatusTextField() {
		if (statusTextField == null) {
			statusTextField = new UpperCaseFieldBean();
			statusTextField.setText("OUTSTANDING");
			statusTextField.setPreferredSize(new Dimension(300,30));
			statusTextField.setBackground(Color.green);
			statusTextField.setFont(Fonts.DIALOG_PLAIN_24);
			statusTextField.setEditable(false);
			statusTextField.setEnabled(true);
			statusTextField.setFocusable(false);
			statusTextField.setRequestFocusEnabled(false);

		}
		return statusTextField;
	}

	public UpperCaseFieldBean getDefectTextField() {
		if (defectTextField == null) {
			defectTextField = new UpperCaseFieldBean();
			defectTextField.setText("");
			defectTextField.setPreferredSize(new Dimension(300,30));
			defectTextField.setBackground(Color.green);
			defectTextField.setFont(Fonts.DIALOG_PLAIN_24);
			defectTextField.setEditable(false);
			defectTextField.setEnabled(true);
			defectTextField.setFocusable(false);
			defectTextField.setRequestFocusEnabled(false);

		}
		return defectTextField;
	}


	public UpperCaseFieldBean getEntryTextField() {
		if (entryTextField == null) {
			entryTextField = new UpperCaseFieldBean();
			entryTextField.setText("");
			entryTextField.setPreferredSize(new Dimension(0,30));
			entryTextField.setBackground(Color.blue);
			entryTextField.setFont(Fonts.DIALOG_PLAIN_24);
			entryTextField.setEditable(true);
			entryTextField.setEnabled(true);
			entryTextField.setFocusable(true);
			entryTextField.setRequestFocusEnabled(true);
			entryTextField.setFixedLength(false);
		}
		return entryTextField;
	}

	protected GridBagConstraints getDefaultTextFieldContraints() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 5, 5);				
		return constraints;
	}

	public void setStatusTextField(UpperCaseFieldBean statusTextField) {
		this.statusTextField = statusTextField;
	}

	public void setDefectTextField(UpperCaseFieldBean defectTextField) {
		this.defectTextField = defectTextField;
	}

	public void setEntryTextField(UpperCaseFieldBean entryTextField) {
		this.entryTextField = entryTextField;
	}


	public void resetPanel() {
		getStatusTextField().setBackground(Color.green);
		getStatusTextField().setText("OUTSTANDING");
		getStatusTextField().setFocusable(false);
		getStatusTextField().setEditable(false);
		getStatusTextField().setEnabled(true);
		getStatusTextField().setRequestFocusEnabled(false);
		getDefectTextField().setBackground(Color.blue);
		getDefectTextField().setText("");
		getDefectTextField().setFocusable(false);
		getDefectTextField().setEditable(false);
		getDefectTextField().setEnabled(true);
		getDefectTextField().setRequestFocusEnabled(false);
		getEntryTextField().setBackground(Color.blue);
		getEntryTextField().setText("");
		getEntryTextField().setFocusable(true);
		getEntryTextField().setEditable(true);
		getEntryTextField().setEnabled(true);
		getEntryTextField().setRequestFocusEnabled(true);
		getEntryTextField().requestFocusInWindow();	
		getEntryTextField().grabFocus();
	}
	public void resetEntryTextField() {
		getEntryTextField().setEnabled(true);
		getEntryTextField().setEditable(true);
		getEntryTextField().setColor(Color.blue);
		getEntryTextField().setBackground(Color.blue);
		getEntryTextField().setText("");
		getEntryTextField().grabFocus();
	}


}
