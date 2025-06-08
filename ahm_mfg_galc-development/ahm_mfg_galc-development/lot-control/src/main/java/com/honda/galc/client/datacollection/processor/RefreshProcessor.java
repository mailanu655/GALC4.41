/**
 * 
 */
package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.dataformat.ProductId;

/**
 * @author Subu Kathiresan
 * @Date May 1, 2012
 *
 */
public class RefreshProcessor extends ProcessorBase 
	implements IProductIdProcessor {

	public RefreshProcessor(ClientContext context) {
		super(context);
		init();
	}

	public void init() {
		Logger.getLogger().debug("RefreshProcessor : Enter init()");
		Logger.getLogger().debug("RefreshProcessor : Exit init()");
	}

	public void registerDeviceListener(DeviceListener listener) {
	}

	public boolean execute(ProductId productId) {
		Logger.getLogger().debug("RefreshProcessor : Enter execute()");
		Logger.getLogger().debug("RefreshProcessor : Exit execute()");
		return true;
	}
}
