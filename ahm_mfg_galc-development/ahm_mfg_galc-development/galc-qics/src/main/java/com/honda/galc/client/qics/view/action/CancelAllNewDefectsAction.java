package com.honda.galc.client.qics.view.action;

import java.awt.event.ActionEvent;
import java.util.List;

import com.honda.galc.client.qics.view.screen.DefectRepairPanel;
import com.honda.galc.client.qics.view.screen.QicsPanel;
import com.honda.galc.entity.qics.DefectResult;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>CancelAllNewDefectsAction</code> is ...
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
public class CancelAllNewDefectsAction extends AbstractPanelAction {
	private static final long serialVersionUID = 1L;

	public CancelAllNewDefectsAction(QicsPanel qicsPanel) {
		super(qicsPanel);
	}

	@Override
	protected void execute(ActionEvent e) {
		List<DefectResult> list = getQicsController().getProductModel().getNewDefects();
		if (list == null || list.isEmpty()) {
			return;
		}
		getQicsController().getProductModel().clearNewDefects();
		getQicsPanel().resetDefectTable();
		getQicsFrame().getLogger().info("All new Defects voided");
	}

	@Override
	protected DefectRepairPanel getQicsPanel() {
		return (DefectRepairPanel) super.getQicsPanel();
	}
}
