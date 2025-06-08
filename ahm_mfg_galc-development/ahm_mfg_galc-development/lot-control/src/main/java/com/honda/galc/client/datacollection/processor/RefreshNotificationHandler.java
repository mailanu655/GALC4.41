package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.net.Request;
import com.honda.galc.notification.service.INotificationService;

public class RefreshNotificationHandler implements INotificationService{
	
	public RefreshNotificationHandler() {
		super();
	}
	
	public void execute() {
		try {
			if(DataCollectionController.getInstance().getState().getProductId()==null) {	
				Object[] array = new Object[] {new Boolean(false)};
			    runInSeparateThread(new Request("cancel", array));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void runInSeparateThread(final Object request){
		Thread t = new Thread(){
			public void run() {
				DataCollectionController.getInstance().received(request);
			}
		};
		
		t.start();
	}
}
