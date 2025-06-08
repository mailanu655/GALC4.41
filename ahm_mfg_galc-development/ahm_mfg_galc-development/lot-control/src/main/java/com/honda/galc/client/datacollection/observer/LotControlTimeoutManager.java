package com.honda.galc.client.datacollection.observer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.property.LotControlPropertyBean;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.net.Request;

/**
 * 
 * <h3>LotControlTimeoutManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>Manager that trigger the UI to refresh if the lot control process is timeout </p>
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
 * @author YX
 * 2014.05.22
 *
 */

public class LotControlTimeoutManager extends DataCollectionObserverBase implements ITimeoutObserver {
	private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private ClientContext context;
	private LotControlPropertyBean propertyBean;
	private ScheduledThreadPoolExecutor timerPool;
	private ScheduledFuture<?> timeoutTask = null;
	private boolean isTimeoutTaskRun = false;
	
	public LotControlTimeoutManager(ClientContext context) {
		super();
		this.context = context;
		this.propertyBean = this.context.getProperty();
		this.timerPool = new ScheduledThreadPoolExecutor(1);
	}
	
	/**
	 * Start to time the lot control process
	 * @param state current state
	 */
	public void startTimer(DataCollectionState state) {
		String productId = state.getProductId();
		int interval = propertyBean.getLotProcessingTimeout();
		if(interval == -1) {
			Logger.getLogger().info("Timeout Observer is disabled, ignore the notification.");
			return;
		}
		Logger.getLogger().info("Start Lot Control Processing timer for Product Id " + productId + "at " + df.format(new Date()));
		timeoutTask = timerPool.schedule(new TimeoutTask(productId), interval, TimeUnit.SECONDS);
	}

	/**
	 * Restart timing the lot control process
	 * @param state current state
	 */
	public void restartTimer(DataCollectionState state) {
		if(!isTimeoutTaskRun){
			Logger.getLogger().info("Receive message to restart Timeout Task for Product Id" + state.getProductId());
			if (timeoutTask != null) timeoutTask.cancel(true);
			startTimer(state);
		}
	}

	/**
	 * Stop the timer of the current process
	 * @param state current state
	 */
	public void stopTimer(DataCollectionState state) {
		if(!isTimeoutTaskRun){
			Logger.getLogger().info("Receive message to cancel Timeout Task for Product Id" + state.getProductId());
			stopTimeoutTask();
		}
	}
	
	/**
	 * Stop the timer and the timer pool when the application exits
	 */
	@Override
	public void cleanUp() {
		stopTimeoutTask();
		timerPool.shutdown();
	}
	
	/**
	 * Stop the timeout task
	 */
	private void stopTimeoutTask() {
		if(timeoutTask!=null){
			if(!timeoutTask.isDone() && !timeoutTask.isCancelled()) {
				timeoutTask.cancel(true);
			}
			timeoutTask=null;
		}
	}
	
	/**
	 * inner class which will send request to refresh the UI and show the message
	 */
	private class TimeoutTask implements Runnable {
		private String productId;
		
		public TimeoutTask(String productId) {
			this.productId = productId;
		}
		
		public void run() {
			isTimeoutTaskRun =true;
			try{
				Logger.getLogger().info("Timeout for processing product " + this.productId + ", Send Refresh request to controller");
				DataCollectionController dcc = DataCollectionController.getInstance(context.getAppContext().getApplicationId());
				dcc.received(new Request("cancel"));
				dcc.received(new Request("error", new Object[]{new Message("Lot Control Processing Timeout")}, new Class[]{Message.class}));
			}finally{
				isTimeoutTaskRun=false;
			}
		}
	}

}
