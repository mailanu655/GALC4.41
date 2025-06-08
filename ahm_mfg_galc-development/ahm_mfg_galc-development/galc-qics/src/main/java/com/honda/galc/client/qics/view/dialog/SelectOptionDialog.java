package com.honda.galc.client.qics.view.dialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.util.ValidatorUtils;


/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Generic OK/Cancel popup dialog with editable combo box.
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 */
public class SelectOptionDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel;
	private JComboBox dataComboBox;
	private JButton okButton;
	private JButton cancelButton;
	private int inputLength;
	
	private boolean cancelled = true;
	private Object returnValue;
	
	public SelectOptionDialog(JFrame parent, String title, boolean editable) {
		super(parent, title, true);
		initialize(editable);
	}

	protected void initialize(boolean editable) {
		setSize(220, 130);
		setContentPane(getContentPanel());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		mapActions();

		getDataComboBox().setEditable(editable);
		getRootPane().setDefaultButton(getCancelButton());
	}

	protected JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.setLayout(null);
			contentPanel.add(getDataComboBox());
			contentPanel.add(getOkButton());
			contentPanel.add(getCancelButton());
		}
		return contentPanel;
	}

	protected JComboBox getDataComboBox() {
		if (dataComboBox == null) {
			dataComboBox = new JComboBox();
			dataComboBox.setSize(190, 30);
			dataComboBox.setLocation(10, 20);
			dataComboBox.setFont(Fonts.DIALOG_PLAIN_18);
		}
		return dataComboBox;
	}

	public JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("Ok");
			okButton.setFont(Fonts.DIALOG_PLAIN_18);
			okButton.setMnemonic(KeyEvent.VK_O);
			okButton.setPreferredSize(new Dimension(90, 25));
			okButton.setSize(90, 30);
			okButton.setLocation(getDataComboBox().getX(), getDataComboBox().getY() + +getDataComboBox().getHeight() + 10);
		}
		return okButton;
	}

	protected JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.setPreferredSize(new Dimension(90, 25));
			cancelButton.setMnemonic(KeyEvent.VK_C);
			cancelButton.setFont(Fonts.DIALOG_PLAIN_18);
			cancelButton.setSize(90, 30);
			cancelButton.setLocation(getOkButton().getX() + getOkButton().getWidth() + 10, getOkButton().getY());
		}
		return cancelButton;
	}

	// ==== actions ===//
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

	// === action mappings ===//
	protected void mapActions() {
		
		getDataComboBox().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setReturnValue(getDataComboBox().getSelectedItem());
			}
		});
		
		getOkButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SelectOptionDialog.this.onOkButton();
			}
		});

		getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SelectOptionDialog.this.onCancelButton();
			}
		});
	}

	// === get/set === //
	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}
	
	public void setReturnValue() {
		Object returnValue = getDataComboBox().getSelectedItem();
		setReturnValue(returnValue);
	}
	
	public Object getReturnValue() {
		return returnValue;
	}
	
	public int getInputLength() {
		return inputLength;
	}

	public void setInputLength(int numberLength) {
		this.inputLength = numberLength;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	protected List<String> validate(Object value) {
		//implement in subclass
		return new ArrayList<String>();
	}
}
