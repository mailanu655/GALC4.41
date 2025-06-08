
package com.honda.galc.client.datacollection.view.action;

import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.view.ViewManagerBase;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.PartSerialNumber;

/**
 * <h3>PartSerialNumberInputAction</h3>
 * <h4>
 * Part serial number input.
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Aug.19, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
public class PartSerialNumberInputAction extends BaseDataCollectionAction {
	private static final long serialVersionUID = 7377412250749352703L;
	String currentPartSn;
	JTextField currentPartSnTextField;

	public PartSerialNumberInputAction(ClientContext context, String name) {
		super(context, name);
	}

	public void actionPerformed(ActionEvent e) {
		currentPartSnTextField = (JTextField)e.getSource(); 
		currentPartSn = currentPartSnTextField.getText();
		if (context.getCurrentViewManager().getUniqueScanType(currentPartSn) != ViewManagerBase.UniqueScanType.NONE) {
			Logger.getLogger().info("Unique scan:" + currentPartSn + " is received from Terminal.");
		} else {
			Logger.getLogger().info("Part SN:" + currentPartSn + " is received from Terminal.");
			confirmPartSerialNumber(currentPartSn);
		}
	}

	protected void confirmPartSerialNumber(String partSn) {
		runInSeparateThread(new PartSerialNumber(partSn));
	}

}
