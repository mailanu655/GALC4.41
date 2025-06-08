package com.honda.galc.client.datacollection.observer;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.notification.service.IProductPassedNotification;
import com.honda.galc.service.ServiceFactory;
/**
 * 
 * <h3>LotControlNotificationObserver</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlNotificationObserver description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Paul Chou
 * Mar 17, 2011
 *
 */
public class LotControlNotificationObserver extends DataCollectionObserverBase
implements INotificationObserver {
	
	protected ClientContext context;

	public LotControlNotificationObserver(ClientContext context) {
		this.context = context;
	}

	public void notify(ProcessProduct state) {
	    notifyInSeperateThread(state);
	}

	private void notifyInSeperateThread(final ProcessProduct state) {
		Thread t = new Thread(){
			public void run() {
				doNotification(state);
			}
		};

		t.start();
	}

	private void doNotification(ProcessProduct state) {
		if(!StringUtils.isEmpty(state.getProductId()) && state.getProduct().isValidProductId()){
			notify(context.getProcessPointId(), state.getProductId());
		} 
	}

	private void notify(String processPointId, String productId) {
		ServiceFactory.getNotificationService(IProductPassedNotification.class, processPointId).execute(
				processPointId, productId);
		
		Logger.getLogger().info("Notified product:", productId, " passed process point:", processPointId);
	}

}
