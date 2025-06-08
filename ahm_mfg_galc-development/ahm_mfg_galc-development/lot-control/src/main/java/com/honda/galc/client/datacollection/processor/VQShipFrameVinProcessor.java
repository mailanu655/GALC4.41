package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.ProductId;

public class VQShipFrameVinProcessor extends VQShipFrameVinBaseProcessor {

	public VQShipFrameVinProcessor(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
	}

	@Override
	public synchronized boolean execute(ProductId productId) {

		Logger.getLogger().debug("VQShipFrameVinProcessor : Enter execute method");
		try {
			if (super.processFrameVin(productId)) {
				return (super.execute(productId));
			}

		} catch (Exception e) {
			Logger.getLogger().error(e, e.getMessage());
			getController().getFsm().error(new Message("MSG01", e.getCause().toString()));
		}

		Logger.getLogger().debug("VQShipFrameVinProcessor : Exit execute method");
		return false;
	}

	
}
