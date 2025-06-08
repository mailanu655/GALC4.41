package com.honda.galc.client.teamleader.qi.productRecovery;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.entity.conf.Application;

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

 * <TD>1.0</TD>
 * <TD>GY 20150119</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 * @author L&T Infotech
 * Aug 28, 2017
 */

public class ManualLtCtrResultEnterViewManager extends ManualLtCtrResultEnterViewManagerBase {

	public ManualLtCtrResultEnterViewManager(ApplicationContext applicationContext,Application application) {
		super(applicationContext, application);
	}

	@Override
	protected void loadHeadLessResult() {
		super.loadHeadLessResult();
	}

	@Override
	protected void loadHeadedResult() {
		super.loadHeadedResult();
	}
}
