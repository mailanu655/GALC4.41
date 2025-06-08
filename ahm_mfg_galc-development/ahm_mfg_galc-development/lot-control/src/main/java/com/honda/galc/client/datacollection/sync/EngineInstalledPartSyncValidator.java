package com.honda.galc.client.datacollection.sync;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.entity.product.ProductSpec;

public class EngineInstalledPartSyncValidator extends
		InstalledPartSyncValidator {

	public EngineInstalledPartSyncValidator(ClientContext context) {
		super(context);
	}

	@Override
	protected ProductSpec getProductSpec() {
		for(EngineSpec engineSpec : context.getEngineSpecs()) {
			if(engineSpec.getProductSpecCode().equals(productSpecCode)) 
				return engineSpec;
		}
		
		throw new TaskException("Invalid product spec code:" + productSpecCode, this.getClass().getSimpleName() );
	}

}
