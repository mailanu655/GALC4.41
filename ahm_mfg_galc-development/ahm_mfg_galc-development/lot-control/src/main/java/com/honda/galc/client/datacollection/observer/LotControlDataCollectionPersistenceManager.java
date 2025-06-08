package com.honda.galc.client.datacollection.observer;

import java.util.List;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.entity.product.InstalledPart;

public class LotControlDataCollectionPersistenceManager extends LotControlFramePersistenceManager{
	
	public LotControlDataCollectionPersistenceManager(ClientContext context) {
		super(context);
		
	}

	@Override
	protected <T extends DataCollectionState> List<InstalledPart> prepareForSave(T state) {
		ProductBean product = state.getProduct();
				
		return product.getPartList();
	}
	
}
