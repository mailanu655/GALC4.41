package com.honda.galc.client.datacollection.observer;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.common.datacollection.data.StatusMessage;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public abstract class AbstractExpectedProductManager {
	
	protected ExpectedProduct expectedProdId;
	protected ClientContext context;

	public AbstractExpectedProductManager(ClientContext context) {
		super();
		this.context = context;
	}
	
	public abstract String getNextExpectedProductId(String productId);

	public void getExpectedProductId(ProcessProduct state) {
		if(needToGetExpectProdId(state))
		{
			try {
				getNextExpectedProductIdForState(state);
			}catch (ServiceTimeoutException ste){
				setOffLine();
				Logger.getLogger().info(ste, "Server OffLine detected.");
				state.setExpectedProductId(null);
			}catch (Exception e){
				state.setExpectedProductId(null);
				Logger.getLogger().error(e, "Exception to get expected product id.");
				throw new TaskException(e.getMessage());
			}
		}
	}
	
	protected void getNextExpectedProductIdForState(ProcessProduct state) {
		if(InstalledPartCache.getInstance().getLastIndex() > 0) {
			getNextProductIdBasedOnLocalCache(state);
		} else {
			getExpectedProductIdFromServer(state);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void getNextProductIdBasedOnLocalCache(ProcessProduct state) {
		List<InstalledPart> installedParts = InstalledPartCache.getInstance().get(InstalledPartCache.getInstance().getLastIndex(), List.class);
		if(installedParts == null || installedParts.get(0) == null) return;
		String lastProductId = installedParts.get(0).getId().getProductId();
		String nextExpectedProductId = getNextExpectedProductId(lastProductId);
		if(!StringUtils.isEmpty(nextExpectedProductId))
			state.setExpectedProductId(nextExpectedProductId);
		Logger.getLogger().info("Next expect product from Local Cache:", nextExpectedProductId);
	}
	
	/**
	 * Get expected product Id at client start up e.g. state.expectedProductId is null and
	 * is client is configured to check expected product Id. 
	 * 
	 * Not get expected product Id when server is off line.
	 * 
	 * @param state
	 * @return
	 */
	private boolean needToGetExpectProdId(ProcessProduct state) {
		Logger.getLogger().debug("Persistencemanager::needToGetExpectProdId(): onLine:" + context.isOnLine() + " expPrdId:" + state.getExpectedProductId() );
		return context.isCheckExpectedProductId() && context.isOnLine() && state.getExpectedProductId() == null;
	}

	protected void getExpectedProductIdFromServer(ProcessProduct state) {
		ExpectedProductDao expectedProductDao = ServiceFactory.getDao(ExpectedProductDao.class);
		expectedProdId = expectedProductDao.findByKey(context.getProcessPointId());
		
		if (context.getProperty().isSaveNextProductAsExpectedProduct()) {
			state.setExpectedProductId(expectedProdId == null ? "" : expectedProdId.getProductId());
		} else {
			if(expectedProdId != null && !StringUtils.isEmpty(expectedProdId.getProductId()))
				state.setExpectedProductId(getNextExpectedProductId(expectedProdId.getProductId()));
			else 
				state.setExpectedProductId("");
		}
		
		Logger.getLogger().info("Next expected product Id from server:", (expectedProdId == null ? "" : expectedProdId.getProductId()));
	}

	public void saveNextExpectedProduct(ProcessProduct state) {

		if(StringUtils.isEmpty(state.getExpectedProductId())) return;

		String nextProductId = null;
		if(context.getProperty().isProductPartSerialNumber()){
			nextProductId = getNextExpectedProductId(state.getProductId());
			if(nextProductId == null){
				nextProductId = getNextExpectedProductId(state.getExpectedProductId());
			}
		} else {
			if(context.getProperty().isExpectedProductSequenceResetAllowed()){
				nextProductId = getNextExpectedProductId(state.getProductId());
			}else{
				nextProductId = getNextExpectedProductId(state.getExpectedProductId());
			}
		}
		if(context.getProperty().isSaveNextProductAsExpectedProduct()){
			saveNextExpectedProduct(nextProductId, state.getProductId());
		} else {
			saveNextExpectedProduct(state.getExpectedProductId(),state.getProductId());
		}
		state.setExpectedProductId(nextProductId);
	}
	
	public void saveNextExpectedProduct(String nextProductId, String lastProcessedProductId) {
		if(DataCollectionController.getInstance(context.getAppContext().getApplicationId()).getState().getStateBean().isResetNextExpected()){
			ExpectedProduct expectedProduct = new ExpectedProduct();
			expectedProduct.setProductId(nextProductId == null ? "" : nextProductId);
			expectedProduct.setProcessPointId(context.getProcessPointId());
			expectedProduct.setLastProcessedProduct(lastProcessedProductId);
			ExpectedProductDao expectedProductDao = ServiceFactory.getDao(ExpectedProductDao.class);
			expectedProductDao.save(expectedProduct);
			Logger.getLogger().info("Save Next Expected Product:", nextProductId);
		}
	}

	public void saveNextExpectedProduct(String nextProductId) {
		saveNextExpectedProduct(nextProductId, "");
	}
	
	protected void setOffLine() {
		EventBus.publish(new StatusMessage(StatusMessage.SERVER_ON_LINE, false));
		context.setOffline();
	}
	
	public void updatePreProductionLot() {}
	
	protected FrameLinePropertyBean getFrameLinePropertyBean() {
		return PropertyService.getPropertyBean(FrameLinePropertyBean.class);
	}
}
