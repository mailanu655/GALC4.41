package com.honda.galc.client.qics.view.action;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import com.honda.galc.client.qics.view.screen.DefectRepairPanel;
import com.honda.galc.client.qics.view.screen.QicsPanel;
import com.honda.galc.entity.qics.DefectResult;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>CancelSelectedNewDefectAction</code> is ...
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
public class CancelSelectedNewDefectAction extends AbstractPanelAction {
	private static final long serialVersionUID = 1L;

	public CancelSelectedNewDefectAction(QicsPanel qicsPanel) {
		super(qicsPanel);
	}

	@Override
	protected void execute(ActionEvent e) {
		DefectResult selectedValue = getQicsPanel().getDefectPane().getSelectedItem();
		if (selectedValue == null) {
			return;
		}
		getQicsController().getProductModel().removeDefect(selectedValue);
		getQicsPanel().resetDefectTable();		
	    getQicsFrame().getLogger().info("Defect "+selectedValue.getDefectTypeName()+" voided");
	}

	@Override
	protected DefectRepairPanel getQicsPanel() {
		return (DefectRepairPanel) super.getQicsPanel();
	}
}
