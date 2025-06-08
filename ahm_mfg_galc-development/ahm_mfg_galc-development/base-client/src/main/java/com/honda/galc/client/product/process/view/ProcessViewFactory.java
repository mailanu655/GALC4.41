package com.honda.galc.client.product.process.view;

import java.awt.Component;

import javax.swing.JPanel;

import com.honda.galc.client.product.process.engine.bearing.pick.view.BearingPickPanel;
import com.honda.galc.client.product.process.engine.bearing.select.view.BearingSelectPanel;
import com.honda.galc.client.product.view.fragments.InfoPanel;
import com.honda.galc.client.ui.MainWindow;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProcessViewFactory</code> is ... .
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
public class ProcessViewFactory {

	public static Component create(String name, MainWindow window) {
		JPanel panel = null;
		if (name != null) {
			name = name.trim();
		}
		if ("BEARING_PICK".equals(name)) {
			panel = new BearingPickPanel(window);
		} else if ("BEARING_SELECT".equals(name)) {
			panel = new BearingSelectPanel(window);
		} else if ("INFO".equals(name)) {
			panel = new InfoPanel();
		} else {
			panel = new JPanel();
		}
		return panel;
	}
}
