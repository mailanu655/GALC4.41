package com.honda.galc.client.qics.view.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.honda.galc.client.ClientMain;
import com.honda.galc.client.qics.view.dialog.OrgUnitSelectDialog;
import com.honda.galc.client.qics.view.screen.DefectRepairPanel;
import com.honda.galc.client.qics.view.screen.QicsPanel;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.entity.qics.DefectResult;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ChangeResponsibleAction</code> is ...
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
 * <TD>Karol Wozniak</TD>
 * <TD>Oct 21, 2008</TD>
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
public class ChangeResponsibleAction extends AbstractPanelAction {
	private static final long serialVersionUID = 1L;

	public ChangeResponsibleAction(QicsPanel qicsPanel) {
		super(qicsPanel);
	}

	@Override
	protected void execute(ActionEvent e) {
		
		// Provide access to Change Responsible Department functionality depending upon security group id
		String changeResponsibleGroup = getQicsController()
				.getQicsPropertyBean().getChangeResponsibleDeptGroup().trim();
		if (!changeResponsibleGroup.equals("*")) {
			if (changeResponsibleGroup == null
					|| changeResponsibleGroup.length() == 0
					|| !ClientMain.getInstance().getAccessControlManager()
							.isAuthorized(changeResponsibleGroup)) {
				MessageDialog
						.showError("You are not authorized to change Responsible Dept, Please contact TC.");
				return;
			}
		}
		final DefectResult repairData = getQicsPanel().getDefectPane().getSelectedItem();
		final OrgUnitSelectDialog responsibleDialog = new OrgUnitSelectDialog(getQicsPanel().getQicsFrame(), getQicsController(), "Change Responsible Data");
		responsibleDialog.setSelectedDepartment(repairData.getResponsibleDept());
		responsibleDialog.setSelectedLine(repairData.getResponsibleLine());
		responsibleDialog.setSelectedZone(repairData.getResponsibleZone());
		responsibleDialog.getOkButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Use Department, Line and Zone Name instead of ID
				String department = responsibleDialog.getSelectedDepartmentName();
				String line = responsibleDialog.getSelectedLineName();
				String zone = responsibleDialog.getSelectedZoneName();
				repairData.setResponsibleDept(department);
				repairData.setResponsibleLine(line);
				repairData.setResponsibleZone(zone);
				repairData.setChangeAtRepair(true);
				getQicsController().getProductModel().addUpdatedDefect(repairData);
				responsibleDialog.dispose();
			}
		});
		responsibleDialog.setLocationRelativeTo(getQicsPanel().getQicsFrame());
		responsibleDialog.setVisible(true);

		getQicsPanel().getDefectPane().clearSelection();
		getQicsPanel().getDefectPane().repaint();
	}

	@Override
	protected DefectRepairPanel getQicsPanel() {
		return (DefectRepairPanel) super.getQicsPanel();
	}
}
