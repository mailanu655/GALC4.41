package com.honda.galc.client.teamleader.hold.qsr.put.file;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.teamleader.hold.InputPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.entity.conf.Division;

/**
 * 
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ImportFileInputPanel</code> is ... .
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
public class ImportFileInputPanel extends InputPanel {

	private static final long serialVersionUID = 1L;

	private JLabel fileLabel;
	private JTextField fileNameInput;
	private JButton browseButton;
	private JButton importButton;

	public ImportFileInputPanel(ImportFilePanel parentPanel) {
		super(parentPanel);
	}

	@Override
	protected void initView() {
		super.initView();
		this.fileLabel = createFileLabel();
		this.fileNameInput = createFileNameInput();
		this.browseButton = createBrowseButton();
		this.importButton = createImportButton();

		remove(getDepartmentElement());
		remove(getProductTypeElement());
		setLayout(new MigLayout("insets 3", "[fill]20[fill]10[][grow,fill][120!,fill][120!,fill]", ""));
		add(getDepartmentElement(), "width 200::, height 30");
		add(getProductTypeElement(), "width 200::, height 30");
		add(getFileLabel());
		add(getFileNameInput(), "height 30");
		add(getBrowseButton(), "height 40");
		add(getImportButton(), "height 40");

	}

	protected JLabel createFileLabel() {
		JLabel element = new JLabel("File", JLabel.RIGHT);
		element.setFont(Fonts.DIALOG_BOLD_14);
		return element;
	}

	public JTextField createFileNameInput() {
		JTextField element = new JTextField();
		element.setFont(Fonts.DIALOG_BOLD_14);
		element.setEditable(false);
		return element;
	}

	public JButton createBrowseButton() {
		JButton button = new JButton();
		button.setEnabled(false);
		button.setFont(Fonts.DIALOG_BOLD_16);
		button.setText("Browse");
		button.setMnemonic(KeyEvent.VK_B);
		return button;
	}

	public JButton createImportButton() {
		JButton button = new JButton();
		button.setEnabled(false);
		button.setFont(Fonts.DIALOG_BOLD_16);
		button.setText("Upload");
		button.setMnemonic(KeyEvent.VK_U);
		return button;
	}

	// === mappings === //
	@Override
	protected void mapActions() {
		super.mapActions();
		getProductTypeComboBox().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getParentPanel().getProductPanel().removeData();
				getFileNameInput().setText("");
				Division division = (Division) getDepartmentComboBox().getSelectedItem();
				boolean enableBrowse = division != null;
				boolean enableUpload = false;
				getBrowseButton().setEnabled(enableBrowse);
				getImportButton().setEnabled(enableUpload);
			}
		});

	}

	// === get/set === //
	public JButton getBrowseButton() {
		return browseButton;
	}

	public JLabel getFileLabel() {
		return fileLabel;
	}

	public JTextField getFileNameInput() {
		return fileNameInput;
	}

	public JButton getImportButton() {
		return importButton;
	}

	public JButton getCommandButton() {
		return null;
	}
}
