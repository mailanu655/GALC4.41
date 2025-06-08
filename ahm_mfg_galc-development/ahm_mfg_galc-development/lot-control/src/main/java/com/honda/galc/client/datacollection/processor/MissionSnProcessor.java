package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.entity.product.MissionSpec;

/**
 * <h3>Class description</h3>
 * Mission ID processor.
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
 * <TD>Nov. 19, 2014</TD>
 * <TD>1.0</TD>
 * <TD>GY 20141119</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */
public class MissionSnProcessor extends ProductIdProcessor {

	public MissionSnProcessor(ClientContext context) {
		super(context);
	}

	@Override
	protected MissionSpec findProductSpec(String productSpecCode) {
		for(MissionSpec missionSpec : context.getMissionSpecs()) {
			if(missionSpec.getProductSpecCode().equals(productSpecCode)) 
				return missionSpec;
		}
		
		throw new TaskException("Invalid product spec code:" + productSpecCode );
	}

	@Override
	public String getProductSpecCode(String productId) {
		return getProductSpecCode(productId, context.getMissionSpecs());
	}

}
