package com.honda.galc.client.teamleader.bearing;

import java.awt.event.KeyEvent;

import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.entity.bearing.BearingType;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ConrodMatrixMaintenancePanel</code> is ... .
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
 * @created Dec 18, 2013
 */
public class ConrodMatrixMaintenancePanel extends BearingMatrixMaintenancePanel {

	private static final long serialVersionUID = 1L;

	public ConrodMatrixMaintenancePanel(TabbedMainWindow mainWindow) {
		super(BearingType.Conrod, "Conrod Matrix Setup", KeyEvent.VK_C, mainWindow);
	}

	@Override
	protected void initView() {
		super.initView();
		getJournalPositionComboBox().setEnabled(false);
		getBearingTypeComboBox().setEnabled(false);
	}

	// === ui model overrides === //
	@Override
	public String[] getColumnValues() {
		return getController().getProperties().getConrodBearingColumnValues();
	}

	@Override
	public String[] getRowValues() {
		return getController().getProperties().getConrodBearingRowValues();
	}

	@Override
	public String[] getBearingTypeOptionValues() {
		return getController().getProperties().getConrodBearingTypes();
	}
}
