package com.honda.galc.client.datacollection.observer;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.logging.Logger;
/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * SR27623 Persistence manager for confirming only with nothing to save
 * <h4>Usage and Example</h4>
 *
 * <h4>Special Notes</h4>
 * 
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
 * <TD>YX</TD>
 * <TD>2013.07.02</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * <TR>
 * <TD>YX</TD>
 * <TD>2013.12.12</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>add expected VIN support</TD>
 * </TR>
 * </TABLE>
 * @see
 * @version 0.1
 * @author YX
 */
public class LotControlConfirmPersistenceManager extends
		LotControlFramePersistenceManager {

	public LotControlConfirmPersistenceManager(ClientContext context) {
		super(context);
	}

	//Save nothing for confirming purpose
	@Override
	public void saveCompleteData(ProcessProduct state) {
		// only update expected VIN in GAl135TBX if needed
		saveExpectedProduct(state);
		Logger.getLogger().info("Confirm part done");
		return;
	}

}
