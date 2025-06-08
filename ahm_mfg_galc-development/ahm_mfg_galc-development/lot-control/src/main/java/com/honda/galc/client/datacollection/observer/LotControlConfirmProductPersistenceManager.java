package com.honda.galc.client.datacollection.observer;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.logging.Logger;

public class LotControlConfirmProductPersistenceManager extends
	LotControlPersistenceManagerExt {

	public LotControlConfirmProductPersistenceManager(ClientContext context) {
		super(context);
	}

	//Save nothing for confirming purpose
	@Override
	public void saveCompleteData(ProcessProduct state) {
		// only update expected in GAl135TBX if needed
		saveExpectedProduct(state);
		Logger.getLogger().info("Confirm part done");
		return;
	}

}
