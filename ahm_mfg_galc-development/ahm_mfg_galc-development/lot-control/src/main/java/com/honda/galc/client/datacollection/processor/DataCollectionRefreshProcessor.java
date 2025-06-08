package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.ProductId;

public class DataCollectionRefreshProcessor extends RefreshProcessor{

	public DataCollectionRefreshProcessor(ClientContext context) {
		super(context);
		this.context = context;
	}

	@Override
	public synchronized boolean execute(ProductId productId) {
			Logger.getLogger().debug("DataCollectionRefreshProcessor : Enter execute()");
		
		try {
			getController().getFsm().messageSentOk();
			Logger.getLogger().debug("DataCollectionRefreshProcessor : Exit execute()");
		} catch(Exception ex) {
			ex.printStackTrace();
			getController().getFsm().messageSentNg();
			return false;
		}
		return true;
	}
}
