package com.honda.galc.client.datacollection.observer;

import com.honda.galc.client.datacollection.ClientContext;

public class LotControlKeyCylinderPersistenceManager extends LotControlFramePersistenceManager{
	public LotControlKeyCylinderPersistenceManager(ClientContext context) {
		super(context);
	}
	
	@Override
	public IExpectedProductManager getExpectedProductManger() {
		if(expectedProductManger == null)
			expectedProductManger = new KeyCylinderExpectedProductManager(context);
		
		return expectedProductManger;
	}
}
