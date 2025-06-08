package com.honda.galc.client.qics.view.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import com.honda.galc.client.qics.view.screen.MainPanel;
import com.honda.galc.client.qics.view.screen.QicsPanel;
import com.honda.galc.client.qics.view.screen.RepairInPanel;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Apr 17, 2014
 */

public class SubmitWithRepairTracking extends SubmitOffAction {

	private static final long serialVersionUID = 1L;

	public SubmitWithRepairTracking(QicsPanel qicsPanel) {
		super(qicsPanel);
		init();
	}

	protected void init() {
		putValue(Action.NAME, "Done");
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
	}

	@Override
	protected void execute(ActionEvent e) {	
		if (!(getQicsFrame().getMainPanel().getSelectedPanel() instanceof RepairInPanel)) {
			((MainPanel) super.getQicsPanel()).displayRepairInPanel();
		} else {
			String selectedItem = ((RepairInPanel) getQicsFrame().getMainPanel().getSelectedPanel()).getRepairLineComboBox().getSelectedItem().toString().trim();
			if (selectedItem.equals("Select")) {
				getQicsFrame().setErrorMessage("Please select the Repair Line and click Done");
			} else {
				super.execute(e);
				String selectedLineId = selectedItem.substring(selectedItem.indexOf("(") + 1, selectedItem.indexOf(")"));
				getQicsController().performRepairTracking(selectedLineId);
			}
		}
	}
}
