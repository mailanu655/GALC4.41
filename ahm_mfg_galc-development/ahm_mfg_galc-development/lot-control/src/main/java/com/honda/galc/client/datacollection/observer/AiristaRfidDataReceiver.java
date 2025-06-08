package com.honda.galc.client.datacollection.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.property.LotControlPropertyBean;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.AristaRfidData;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.enumtype.OperationMode;
import com.honda.galc.net.Request;
import com.honda.galc.service.ServiceFactory;


public class AiristaRfidDataReceiver extends RfidDataReceiver implements IRfidDataObserver {

	private LotControlPropertyBean propertyBean;
	private ScheduledThreadPoolExecutor timerPool;
	private ScheduledFuture<?> timeoutTask = null;
	private AristaRfidData rfidData;
		
	public AiristaRfidDataReceiver(ClientContext context){
		super();
		this.context = context;
		this.propertyBean = this.context.getProperty();
		registerDeviceListener();
	}

	protected List<IDeviceData> getDeviceDataList() {
		List<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new AristaRfidData());
		
		return list;
	}

	@Override
	public IDeviceData received(String clientId, IDeviceData deviceData) {
		if(context.getProperty().isScanlessEnabled() && !isInAuto()) {
			return deviceData;
		}
		if (deviceData instanceof AristaRfidData) {
			DataCollectionState state = DataCollectionController.getInstance(context.getAppContext().getApplicationId()).getState();
			rfidData = (AristaRfidData)deviceData;
		
			Logger.getLogger().info("Received Product Id " + rfidData.getProductId());
			
			if(state instanceof ProcessPart || state instanceof ProcessTorque){
				LotControlAudioManager.getInstance().playWarnSound();
				if(!state.getProductId().equalsIgnoreCase(rfidData.getProductId())){
					DataCollectionController.getInstance(context.getAppContext().getApplicationId()).addProductIdToQueue(rfidData.getProductId());
					int interval = propertyBean.getRfidTimeout();
					if(interval == -1) {
						Logger.getLogger().info("Timeout Observer is disabled, ignore the notification.");
						return deviceData;
					}
					
					runInSeparateThread(new Request("error", new Object[]{new Message("Starting Arista Rfid Processing timer")}, new Class[]{Message.class}), 0);
					Logger.getLogger().info("Start Arista Rfid Processing timer for Product Id " + rfidData.getProductId());
					runInSeparateThread(new Request("cancel"), interval);
				}else{
					Logger.getLogger().info("Received Product Id " + rfidData.getProductId()+", already processing productID - "+state.getProductId());
					rfidData = null;
				}
				
			}else{
				ProductId productId = new ProductId(rfidData.getProductId());
				runInSeparateThread(productId);
				rfidData = null;
			}
		}
		return deviceData;
	}
	
	@Override
	public void populateProductId(DataCollectionState state) {
		String nextProductId = DataCollectionController.getInstance(context.getAppContext().getApplicationId()).getNextProductIdFromQueue();
		if(StringUtils.isBlank(nextProductId)) {
			return;
		}else if(state.getProductId()== null){
			ProductId productId = new ProductId(nextProductId);
			runInSeparateThread(productId);
			Logger.getLogger().info("Populating Product Id " + nextProductId);
			rfidData = null;
		}else if(!state.getProductId().equalsIgnoreCase(nextProductId)){
			ProductId productId = new ProductId(nextProductId);
			runInSeparateThread(productId);
			Logger.getLogger().info("AutoPopulating Product Id " + nextProductId);
			rfidData = null;
		}
	}
	
	protected void runInSeparateThread(final Object request, long delay){
		timerPool = new ScheduledThreadPoolExecutor(1);
		Thread t = new Thread() {
			public void run() {
				DataCollectionController.getInstance(context.getAppContext().getApplicationId()).received(request);
			}
		};
		timeoutTask = timerPool.schedule(t, delay, TimeUnit.SECONDS);
				
	}
	
	@Override
	public void cleanUp() {
		if(timeoutTask!=null){
			if(!timeoutTask.isDone() && !timeoutTask.isCancelled()) {
				timeoutTask.cancel(true);
			}
			timeoutTask=null;
		}
		if(timerPool != null)timerPool.shutdown();
	}
	
	@Override
	public void stopTimer(DataCollectionState state) {
		cleanUp();
		populateProductId(state);
	}
	
	private boolean isInAuto() {
		ComponentStatus componentStatus = getComponentStatusScanless();
		String operationMode = componentStatus == null ? "N/A" : componentStatus.getStatusValue();
		return OperationMode.AUTO_MODE.getName().equalsIgnoreCase(operationMode);
	}
	
	private ComponentStatusDao getComponentStatusDao() {
		return ServiceFactory.getDao(ComponentStatusDao.class);
	}

	private ComponentStatus getComponentStatusScanless() {
		return getComponentStatusDao().findByKey(context.getProcessPointId(), "SCANLESS_OPERATION_MODE");
	}

}
