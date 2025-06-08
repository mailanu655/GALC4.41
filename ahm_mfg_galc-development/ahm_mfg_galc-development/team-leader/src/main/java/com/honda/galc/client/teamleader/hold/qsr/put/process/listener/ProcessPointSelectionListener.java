package com.honda.galc.client.teamleader.hold.qsr.put.process.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.qsr.put.process.ProcessPanel;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProcessPointSelectionListener</code> is ...
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
 * <TD>Karol Wozniak</TD>
 * <TD>Jan 7, 2010</TD>
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
public class ProcessPointSelectionListener extends BaseListener<ProcessPanel> implements ActionListener {

	public ProcessPointSelectionListener(ProcessPanel parentPanel) {
		super(parentPanel);
	}

	@Override
	public void executeActionPerformed(ActionEvent e) {

		boolean enabled = false;
		if (getView().getInputPanel().getProcessPointComboBox().getSelectedItem() != null) {
			enabled = true;
		}
		getView().getProductPanel().removeData();
		getView().getInputPanel().resetInput();
		getView().getInputPanel().setInputEnabled(enabled);
		getView().getInputPanel().getCommandButton().setAction(getView().getSelectProductAction());
		getView().getInputPanel().getCommandButton().setEnabled(false);
	}
}
