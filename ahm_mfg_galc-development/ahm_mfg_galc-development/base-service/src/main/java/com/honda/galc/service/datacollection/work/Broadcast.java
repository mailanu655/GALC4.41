package com.honda.galc.service.datacollection.work;

import static com.honda.galc.service.ServiceFactory.getService;

import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;

public class Broadcast extends CollectorWork{
	public Broadcast(HeadlessDataCollectionContext context)	 {
		super(context, null);
	}

	@Override
	void doWork() throws Exception {
		if(context.getProduct() == null){
			getService(BroadcastService.class).broadcast(context.getProcessPointId(), context.getProductId());
		}else 
			getService(BroadcastService.class).broadcast(context.getTrackingProcessPointId(),context.getProduct(),context.asDataContainer());

	}

}
