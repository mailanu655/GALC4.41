package com.honda.galc.client.datacollection.sync;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.ProductSpec;

public class FrameInstalledPartSyncValidator extends InstalledPartSyncValidator {

	public FrameInstalledPartSyncValidator(ClientContext context) {
		super(context);
	}


	@Override
	protected ProductSpec getProductSpec() {
		
		FrameSpec frameSpec = context.getFrameSpec(productSpecCode);
		if(frameSpec == null) {
			throw new TaskException("Invalid product spec code:" + productSpecCode, this.getClass().getSimpleName() );
		}else return frameSpec;
	}

}
