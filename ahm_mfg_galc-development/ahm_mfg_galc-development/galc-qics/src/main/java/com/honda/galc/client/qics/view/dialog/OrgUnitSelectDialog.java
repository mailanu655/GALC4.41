package com.honda.galc.client.qics.view.dialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.honda.galc.client.qics.controller.QicsController;
import com.honda.galc.client.qics.view.fragments.OrgUnitDataPanel;
import com.honda.galc.client.ui.component.Fonts;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Popup dialog that lets user select department, line, and zone.
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
public class OrgUnitSelectDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = null;
	private OrgUnitDataPanel orgUnitDataPanel = null;
	private JButton okButton = null;
	private JButton cancelButton = null;

	private QicsController qicsController;

	public OrgUnitSelectDialog(JDialog owner, QicsController qicsController, String title) {
		super(owner, title, true);
		setQicsController(qicsController);
		initialize();
		getOrgUnitDataPanel().loadDepartments(qicsController.getDepartments());
	}
	
	public OrgUnitSelectDialog(JFrame owner, QicsController qicsController, String title) {
		super(owner, title, true);
		setQicsController(qicsController);
		initialize();
		getOrgUnitDataPanel().loadDepartments(qicsController.getDepartments());

	}


	public void setSelectedDepartment(String department) {
		getOrgUnitDataPanel().setSelectedDepartment(department);
	}

	public void setSelectedLine(String line) {
		getOrgUnitDataPanel().setSelectedLine(line);
	}

	public void setSelectedZone(String zone) {
		getOrgUnitDataPanel().setSelectedZone(zone);
	}

	/**
	 * Gets the selected department name.
	 * Use Department Name instead of ID
	 * @return the selected department name
	 */
	public String getSelectedDepartmentName() {
		return getOrgUnitDataPanel().getSelectedDepartmentName();
	}
	
	public String getSelectedDepartment() {
		return getOrgUnitDataPanel().getSelectedDepartment();
	}

	public String getSelectedLine() {
		return getOrgUnitDataPanel().getSelectedLine();
	}
	
	/**
	 * Gets the selected line name.
	 * Use Line Name instead of ID
	 * @return the selected line name
	 */
	public String getSelectedLineName() {
		return getOrgUnitDataPanel().getSelectedLineName();
	}

	public String getSelectedZone() {
		return getOrgUnitDataPanel().getSelectedZone();
	}

	/**
	 * Gets the selected Zone name.
	 * Use Zone Name instead of ID
	 * @return the selected zone name
	 */
	public String getSelectedZoneName() {
		return getOrgUnitDataPanel().getSelectedZoneName();
	}
	
	// end of public api

	private void initialize() {
		setSize(380, 220);
		setContentPane(getContentPanel());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		mapActions();
		getRootPane().setDefaultButton(getCancelButton());
	}

	private JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.add(getOrgUnitDataPanel(), null);
			contentPanel.add(getOkButton(), null);
			contentPanel.add(getCancelButton(), null);
			getOrgUnitDataPanel().setEnabled(true);
		}
		return contentPanel;
	}

	protected OrgUnitDataPanel getOrgUnitDataPanel() {
		if (orgUnitDataPanel == null) {
			orgUnitDataPanel = new OrgUnitDataPanel();
		}
		return orgUnitDataPanel;
	}

	public JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("Ok");
			okButton.setFont(Fonts.DIALOG_PLAIN_18);
			okButton.setMnemonic(KeyEvent.VK_O);
			okButton.setPreferredSize(new Dimension(90, 25));
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
		}
		return cancelButton;
	}

	// ==== actions ===//
	private void onOkButton() {
		// dispose();
	}

	private void onCancelButton() {
		dispose();
	}

	// === action mappings ===//
	private void mapActions() {
		getOkButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OrgUnitSelectDialog.this.onOkButton();
			}
		});

		getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OrgUnitSelectDialog.this.onCancelButton();
			}
		});

}

	public QicsController getQicsController() {
		return qicsController;
	}

	public void setQicsController(QicsController qicsController) {
		this.qicsController = qicsController;
	}
} 
