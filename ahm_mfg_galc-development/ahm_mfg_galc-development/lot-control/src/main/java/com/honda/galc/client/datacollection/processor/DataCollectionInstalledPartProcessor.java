package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.device.plc.DataCollectionInputData;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.InstalledPart;


public class DataCollectionInstalledPartProcessor extends PartSerialNumberProcessor {
	
	public DataCollectionInstalledPartProcessor(ClientContext context) {
		super(context);
		this.context = context;
	}
	
	public synchronized boolean execute(DataCollectionInputData result) {
		Logger.getLogger().debug("DataCollectionInstalledPartProcessor:: Enter confirmPartSerialNumber ");
		InstalledPart installedPart = result.getInstalledPart();
		try {
				getController().getFsm().partSnOk(installedPart);
			
			Logger.getLogger().debug("DataCollectionInstalledPartProcessor:: Exit confirmPartSerialNumber ok");
			return true;

		} catch (Exception e) {
			getController().getFsm().partSnNg(installedPart, PART_SN_MESSAGE_ID, "An Exception occured");
			Logger.getLogger().error(e.getMessage());
			return false;
		}
	}
}

	

