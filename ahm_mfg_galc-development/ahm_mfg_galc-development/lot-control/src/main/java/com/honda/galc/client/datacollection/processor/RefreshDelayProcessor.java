package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.net.Request;
/**
 * <h3>TorqueProcessor</h3>
 * <h4>
 * Torque collection processor - used to verify torque value from torque device
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
public class RefreshDelayProcessor extends RefreshProcessor {

	public RefreshDelayProcessor(ClientContext context) {
		super(context);
		init();
	}

	public void init() {
	}

	public boolean execute(ProductId productId) {
		Logger.getLogger().debug("RefreshDelayProcessor :: Enter execute");
		getController().received(new Request("cancel"));
		getController().received(productId);
		return true;
	}
}
