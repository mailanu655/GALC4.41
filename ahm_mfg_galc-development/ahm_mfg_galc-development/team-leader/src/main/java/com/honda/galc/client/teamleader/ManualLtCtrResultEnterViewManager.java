package com.honda.galc.client.teamleader;

import com.honda.galc.client.teamleader.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.client.ui.MainWindow;

/**
 * <h3>Class description</h3>
 * This class manages user interface for Manual Lot Control Repair.
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Jan 19, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150119</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */

public class ManualLtCtrResultEnterViewManager extends ManualLtCtrResultEnterViewManagerBase {

	public ManualLtCtrResultEnterViewManager(MainWindow mainWin, ManualLotControlRepairPropertyBean property) {
		super(mainWin.getApplicationContext(), property, mainWin.getApplication());
	}

	@Override
	protected void loadHeadLessResult() {
		super.loadHeadLessResult();
		if(hasComment() && !resetScreen) {
			showPartComment();
		}
	}

	@Override
	protected void loadHeadedResult() {
		super.loadHeadedResult();
		if(hasComment() && !resetScreen) {
			showPartComment();
		}
	}
}
