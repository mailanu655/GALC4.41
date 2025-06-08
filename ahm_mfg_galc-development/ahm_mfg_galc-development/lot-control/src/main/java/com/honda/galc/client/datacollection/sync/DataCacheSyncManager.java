package com.honda.galc.client.datacollection.sync;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bushe.swing.event.EventBus;

import com.honda.galc.client.common.datacollection.data.StatusMessage;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.observer.InstalledPartCache;
import com.honda.galc.client.datacollection.property.DataSyncProperty;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.common.exception.ServiceException;
import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.util.LotControlPartUtil;
import com.honda.galc.util.ProductResultUtil;
/**
 * 
 * <h3>DataCacheSyncManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DataCacheSyncManager description </p>
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
 * Mar 25, 2010
 *
 */
public class DataCacheSyncManager implements Runnable{
	private TerminalPropertyBean property;
	List<InstalledPart> partList = new ArrayList<InstalledPart>();;
	List<Integer> partKeys = new ArrayList<Integer>();
	private ClientContext context;
	private InstalledPartSyncValidator validator;

	public DataCacheSyncManager(ClientContext context) {
		super();
		this.context = context;
		
		init();
	}

	private void init() {
		this.property = context.getProperty();
		
		createValidator();
	}

	private void createValidator() {
		if(property.getProductType().equals(ProductType.ENGINE.toString()))
			validator = new EngineInstalledPartSyncValidator(context);
		else
			validator = new FrameInstalledPartSyncValidator(context);
		
	}

	/**
	 * Interval to trying save data on server when server is available/not available 
	 * @return
	 */
	private int getSyncInterval() {
		return property.getOnlineSyncInterval();
	}

	private synchronized void saveInstalledPartsOnServer() {
		Logger.getLogger().info("---Sync Manager Save Data On Server:" + partKeys.size() + " " + getProductIds());
		
		ProductResultUtil.saveAll(context.getProcessPointId(), partList);
		
		Logger.getLogger().debug("[SyncMgr] Saved InstalledParts:" + getInstalledPartsDetails(partList));
	}

	private List<String> getProductIds() {
		List<String> list = new ArrayList<String>();
		for(InstalledPart part: partList){
			 if(!list.contains(part.getId().getProductId()))
					 list.add(part.getId().getProductId());
		}
		return list;
	}

	public static String getInstalledPartsDetails(List<InstalledPart> plist) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < plist.size(); i++){
			sb.append(System.getProperty("line.separator"));
			sb.append(plist.get(i).toString());
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private void removeSavedPartsFromCache() {
		try {
			Logger.getLogger().info("[SyncMgr] delete InstalledParts from cache:" );
			for (Integer k : partKeys) {
				
				//Log every installed part before removal
				List<InstalledPart> installedParts = getCache().get(k, List.class);
				Logger.getLogger().info(getInstalledPartsDetails(installedParts));
				getCache().remove(k);
			}
			
			if(getCache().getSize() == 0) getCache().resetLastIndex();
			getCache().flush();
		} catch (Exception e) {
			Logger.getLogger().error(e, this.getClass().getSimpleName() + " Exception");
		}

		//Clean part/key list and ready to save more parts
		cleanup();

	}

	private void cleanup() {
		partList.clear();
		partKeys.clear();
	}

	@SuppressWarnings("unchecked")
	private void preparePartsToSaveOnServer() {
		if(partList.size() == 0 && partKeys.size() == 0){
			List<Integer> keys = getCache().getKeys();
			Collections.sort(keys);
			
			for(int i = 0; i < property.getSyncDataSize() && i < keys.size(); i++){
				Integer partKey = keys.get(i);
				partKeys.add(partKey);
				List<InstalledPart> installedParts = getCache().get(partKey, List.class);

				validator.validateInstalledPart(installedParts);
				LotControlPartUtil.filterOutExcludePart(installedParts, context.getProperty().getExcludePartsToSave());
				
				partList.addAll(installedParts);
			}
		} else {
			Logger.getLogger().warn("Unexpected data may restored - need to check database" +
					" to make sure no duplicate data. keys:" + partKeys.toString());
		}
	}

	
	public void run() {
		try {
			int syncInterval = getSyncInterval() * 1000;
			delay(syncInterval);

			while(true){

				Logger.getLogger().debug("--Sync Manager: " + getCache().getSize());

				if (getCache().getSize() > 0 && context.isOnlineMode()) 
					saveCacheDataOnServer();

				delay(syncInterval);

			}
		} catch (Exception e) {
			Logger.getLogger().emergency(e, "Error, Contact IS. Cache Synchronization failed. " + this.getClass().getSimpleName() + " " + e.getClass().getSimpleName());
		}
	}

	private void delay(int syncInterval) {
		try {
			Thread.sleep(syncInterval);
		} catch (Exception e) {
			Logger.getLogger().error(e, this.getClass().getSimpleName() + " Exception");
		}
		
	}

	private void saveCacheDataOnServer() {
		try {
			
			preparePartsToSaveOnServer();
			
			saveInstalledPartsOnServer();

			//Remove from cache only after save to server success
			removeSavedPartsFromCache();

		} catch (ServiceTimeoutException ste) {
			handleServerOffLineException(ste);
		} catch (ServiceInvocationException sie) {
			//ServiceInvocationException: Failed to receive reply ... due to java.net.ConnectException: Connection refused: connect
			//so handle server off line
			handleServerOffLineException(sie);
		} catch (Throwable t) {
			Logger.getLogger().error(t, this.getClass().getSimpleName() + " : Unexpected exception.");
		}
	}

	private void handleServerOffLineException(ServiceException e) {
		
		setOffLine();
		
		//Failed to save data on server, so clean up key/part list
		cleanup();
		Logger.getLogger().debug(e, this.getClass().getSimpleName() + " : detected server off line.");
	}


	private void setOffLine() {
		EventBus.publish(new StatusMessage(StatusMessage.SERVER_ON_LINE, false));
		context.setOffline();
	}

	public DataSyncProperty getProperty() {
		return property;
	}
	
	
	public InstalledPartCache getCache(){
		return InstalledPartCache.getInstance();
	}
	

}
