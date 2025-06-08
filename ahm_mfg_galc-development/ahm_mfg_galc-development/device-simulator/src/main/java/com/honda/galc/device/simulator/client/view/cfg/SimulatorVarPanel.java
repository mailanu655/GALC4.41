package com.honda.galc.device.simulator.client.view.cfg;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JPanel;

import com.honda.galc.device.simulator.client.view.data.ConfigValueObject;



/**
 * <h3>SimulatorVarPanel</h3>
 * <h4> abstract class for var panels </h4>
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
 * <TD>Sep. 23, 2008</TD>
 * <TD>Version 0.2</TD>
 * <TD></TD>
 * <TD>Initial version.</TD>
 * </TR>
 * </TABLE>
 */

public abstract class SimulatorVarPanel extends JPanel {
	private static final long serialVersionUID = 6805068785271060453L;

	public SimulatorVarPanel() {
		super();
	}

	protected abstract void fromValueObj(ConfigValueObject vo);
	protected abstract void saveToValueObj(ConfigValueObject vo);
	
	protected Font getTextFont() {
		return new Font("Dialog", Font.PLAIN, 12);
	}

	protected Dimension getComboBoxSize() {
		return new Dimension(150, 20);
	}
	
	protected GridBagConstraints getConstraint() {
		return getConstraint(0, 0, 1);
	}
	
	protected GridBagConstraints getConstraint(int gridx, int gridy, int gridwidth) {
		GridBagConstraints c = new GridBagConstraints();		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 0, 10);
		c.weightx = 0.3;
		
		c.gridx = gridx;
		c.gridy = gridy;
		c.gridwidth = gridwidth;		
		return c;
	}

}