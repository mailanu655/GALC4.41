package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.device.dataformat.ProductId;
/**
 * 
 * <h3>EngineSnProcessorEx</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> EngineSnProcessorEx description </p>
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
 *
 * </TABLE>
 *   
 * @author Paul Chou
 * Jul 16, 2010
 *
 */
public class EngineSnProcessorEx extends EngineSnProcessor {

	public EngineSnProcessorEx(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
	}

	@Override
	public synchronized boolean execute(ProductId productId) {
		boolean result =  super.execute(productId);
		
		//doRequiredPartCheck(); done in product Id check
		
		return result;
	}

}
