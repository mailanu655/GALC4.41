package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.device.ipuqatester.model.IpuQaTestResult;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;

/**
 * @author Gangadhararao Gadde
 * @author Subu Kathiresan
 * @Date Apr 27, 2012
 *
 */
public class IpuQaTesterInstalledPartProcessor extends PartSerialNumberProcessor {

	public IpuQaTesterInstalledPartProcessor(ClientContext context) {
		super(context);
	}
	
	public synchronized boolean execute(IpuQaTestResult testResult) {
		InstalledPart installedPart = testResult.getInstalledPart();
		try {
			if (installedPart.getInstalledPartStatus() == InstalledPartStatus.MISSING) {
				getController().getFsm().partSnMissing(installedPart);
			} else {
				getController().getFsm().partSnOk(installedPart);
			}
			
			Logger.getLogger().debug("PartSerialNumberProcessor:: Exit confirmPartSerialNumber ok");
			return true;

		} catch (Exception e) {
			getController().getFsm().partSnNg(installedPart, PART_SN_MESSAGE_ID, "An Exception occured");
			Logger.getLogger().error(e.getMessage());
			return false;
		}
	}
}
