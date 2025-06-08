package com.honda.galc.service.broadcast;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.BroadcastDestinationId;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.BroadcastUtil;

/**
 * 
 * <h3>BroadcastServiceImpl Class description</h3>
 * <p> BroadcastServiceImpl description </p>
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
 * @author Jeffray Huang<br>
 * Aug 20, 2013
 *
 *
 */
public class BroadcastServiceImpl implements BroadcastService{
	
	@Autowired
    private BroadcastDestinationDao broadcastDestinationDao;
	
	
	public void broadcast(String processPointId, String productId) {
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.PRODUCT_ID, productId);
		broadcast(processPointId,dc);
	}
	
	public void broadcast(String processPointId, BaseProduct product) {
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.PRODUCT_ID, product.getProductId());
		dc.put(DataContainerTag.PRODUCT_TYPE,product.getProductType().toString());
		dc.put(DataContainerTag.PRODUCT,product);
		dc.put(DataContainerTag.PRODUCT_SPEC_CODE,product.getProductSpecCode());
		broadcast(processPointId,dc);
	}


	public void broadcast(String processPointId, BaseProduct product,DataContainer dc) {
		if(dc==null){
			dc = new DefaultDataContainer();
		}
		dc.put(DataContainerTag.PRODUCT_ID, product.getProductId());
		dc.put(DataContainerTag.PRODUCT_TYPE,product.getProductType().toString());
		dc.put(DataContainerTag.PRODUCT,product);
		dc.put(DataContainerTag.PRODUCT_SPEC_CODE,product.getProductSpecCode());
		broadcast(processPointId,dc);
	}

	public void broadcast(String processPointId, DataContainer dataContainer) {
		
		List<BroadcastDestination> broadcastDestinations = broadcastDestinationDao.findAllByProcessPointId(processPointId, true);
		if(broadcastDestinations.isEmpty()) {
			return;
		}
		Logger.getLogger(processPointId).info("Start Broadcast Services for process point " + processPointId);

		DataContainer tempData = new DefaultDataContainer();
		tempData.putAll(dataContainer);
		
		for(BroadcastDestination destination: broadcastDestinations) {
			if (BroadcastUtil.isBroadcast(destination, tempData)) {
				broadcast(destination,processPointId,dataContainer);
			} else {
				Logger.getLogger(processPointId).info("Conditional Broadcast to destination " + destination + " is not executed, condition : " + destination.getCondition());
			}
		}
	}
	
	public void broadcast(String processPointId, DataContainer dataContainer, CheckPoints checkPoint) {
		
		List<BroadcastDestination> broadcastDestinations = getBroadcastDestinationDao().findAllByProcessPointIdAndCheckPoint(processPointId, checkPoint);
		if(broadcastDestinations.isEmpty()) {
			return;
		}
		Logger.getLogger(processPointId).info("Start Broadcast Services for process point " + processPointId);

		DataContainer tempData = new DefaultDataContainer();
		tempData.putAll(dataContainer);
		
		for (BroadcastDestination destination : broadcastDestinations) {
			if (BroadcastUtil.isBroadcast(destination, tempData)) {
				broadcast(destination, processPointId, dataContainer);
			} else {
				Logger.getLogger(processPointId).info("Conditional Broadcast to destination " + destination + " is not executed, condition : " + destination.getCondition());
			}
		}
	}
	
	public void asynBroadcast(String processPointId, int seqno, String productId) {
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.PRODUCT_ID, productId);
		asynBroadcast(processPointId, seqno,dc);
	}
	
	public void asynBroadcast(String processPointId, int seqno,DataContainer dc) {
		BroadcastDestination destination = broadcastDestinationDao.findByKey(new BroadcastDestinationId(processPointId,seqno));
		AbstractBroadcast broadcast = getBroadcaster(destination, processPointId,dc);
		fireBroadcastTask(broadcast);
	}
	
	public DataContainer broadcast(String processPointId, int seqno, String productId) {
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.PRODUCT_ID, productId);
		
		return broadcast(processPointId, seqno,dc);
	}
	
	public DataContainer broadcast(String processPointId, int seqno,DataContainer dc) {
		BroadcastDestination destination = broadcastDestinationDao.findByKey(new BroadcastDestinationId(processPointId,seqno));
		AbstractBroadcast broadcast = getBroadcaster(destination, processPointId,dc);
		return broadcast.execute();
	}
	
	public void broadcast(BroadcastDestination destination, String processPointId, DataContainer dc) {
		AbstractBroadcast broadcast = getBroadcaster(destination, processPointId,dc);
		// This condition is added as per RGALCPROD-4139 to make broadcast service synchronous for printing build sheets
		if(PropertyService.getPropertyBoolean(processPointId, "SYNC_BROADCAST", false)){
			Logger.getLogger(processPointId).info("Synchronous  Fire off " + broadcast.getClass().getSimpleName() + " - " + destination);
			broadcast.execute();
			dc.putAll(broadcast.dc);
		} else {
			Logger.getLogger(processPointId).info("Asynchronous  Fire off " + broadcast.getClass().getSimpleName() + " - " + destination);
			// fire off the broadcast work in separate thread 
			fireBroadcastTask(broadcast);
		}
	}
	
	public void reBroadcast(String processPointId, DataContainer dataContainer) {
		
		List<BroadcastDestination> broadcastDestinations = broadcastDestinationDao.findAllByProcessPointId(processPointId, true);
		if(broadcastDestinations.isEmpty()) {
			return;
		}
		Logger.getLogger(processPointId).info("Start Broadcast Services for process point " + processPointId);
			
		for(BroadcastDestination destination: broadcastDestinations) {
			broadcast(destination,processPointId,dataContainer);
		}
	}
	
	public AbstractBroadcast getBroadcaster(BroadcastDestination destination, String processPointId,DataContainer dc){
		switch(destination.getDestinationType()){
			case Printer : 
				return new PrinterBroadcast(destination,processPointId,dc);
			case Equipment: 
				return new EquipmentBroadcast(destination,processPointId,dc);
			case External: 
				return new ExternalServiceBroadcast(destination,processPointId,dc);
			case Notification : 
				return new NotificationServiceBroadcast(destination,processPointId,dc);
			case MQ : 
				return new MqBroadcast(destination,processPointId,dc);
			case DEVICE_WISE : 
				return new HttpJsonBroadcast(destination,processPointId,dc);
			case SERVER_TASK : 
				return new ServerTaskBroadcast(destination, processPointId, dc);
			case FTP:
				return new FtpBroadcast(destination, processPointId, dc);
			case MQMANIFEST:
				return new MqManifestBroadcast(destination,processPointId,dc);
			case HTTP_PARAM:
				return new HttpBroadcast(destination,processPointId,dc);

			default:
		};
		return null;
	}
	
	private void fireBroadcastTask(AbstractBroadcast broadcast) {
		ThreadPoolTaskExecutor taskExecutor = null;
		try{
			taskExecutor = (ThreadPoolTaskExecutor)ApplicationContextProvider.getBean("taskExecutor");
			Logger.getLogger().debug("Pool Size " + taskExecutor.getPoolSize());
			taskExecutor.execute(new BroadcastTask(broadcast));
		} catch(Exception ex){
			ex.printStackTrace();
			Logger.getLogger().error("Exception occured in Broadcasting. " + ExceptionUtils.getStackTrace(ex));
			if (taskExecutor!=null){
				String  threadPoolInfo = "ThreadPoolExecutor Info" +
						" Pool Core: " + taskExecutor.getCorePoolSize() +
						", Pool Max: " + taskExecutor.getMaxPoolSize() + 
						", Pool Actual: " + taskExecutor.getPoolSize() +
						", Queue Remaining Capacity: " + taskExecutor.getThreadPoolExecutor().getQueue().remainingCapacity() ;
				Logger.getLogger().error(threadPoolInfo);
			}
		}
	}
	
	private class BroadcastTask implements Runnable {
		private AbstractBroadcast broadcast;
		public BroadcastTask(AbstractBroadcast broadcast) {
			this.broadcast = broadcast;
		}
		public void run() {
			broadcast.execute();			
		}
		
	}

	protected BroadcastDestinationDao getBroadcastDestinationDao() {
		return broadcastDestinationDao;
	}

	protected void setBroadcastDestinationDao(BroadcastDestinationDao broadcastDestinationDao) {
		this.broadcastDestinationDao = broadcastDestinationDao;
	}	
}
