package com.honda.galc.client.datacollection.processor;


import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.entity.product.EngineSpec;
/**
 * <h3>EngineSnProcessor</h3>
 * <h4>
 * Engine Serial Number processor - used to verify Engine Serial Number from both of
 * device and gui client input. A sub class of Product Id processor.
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
public class EngineSnProcessor extends ProductIdProcessor {

	public EngineSnProcessor(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
	}

	public EngineSpec findProductSpec(String productSpecCode) {
		for(EngineSpec engineSpec : context.getEngineSpecs()) {
			if(engineSpec.getProductSpecCode().equals(productSpecCode)) 
				return engineSpec;
		}
		
		throw new TaskException("Invalid product spec code:" + productSpecCode );
	}
	
	@Override
	public String getProductSpecCode(String productId) {
		return getProductSpecCode(productId, context.getEngineSpecs());
	}
		

	
}
