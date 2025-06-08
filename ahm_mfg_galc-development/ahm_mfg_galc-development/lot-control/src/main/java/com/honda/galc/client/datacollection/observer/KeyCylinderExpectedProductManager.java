package com.honda.galc.client.datacollection.observer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.service.ServiceFactory;

public class KeyCylinderExpectedProductManager extends AbstractExpectedProductManager
implements IExpectedProductManager{

	public KeyCylinderExpectedProductManager(ClientContext context) {
		super(context);
		
	}

	@Override
	public String getNextExpectedProductId(String productId) {
		
		String nextProductId = "";
		InProcessProductDao inProcessProductDao = ServiceFactory.getDao(InProcessProductDao.class);
		InProcessProduct inProcessProduct = inProcessProductDao.findByKey(productId);
		if(inProcessProduct == null || !(inProcessProduct.getLineId().equalsIgnoreCase(getAfOnLineId()))){
			
			context.setManualRefresh(true);
			return nextProductId;
		}
		if((StringUtils.isEmpty(inProcessProduct.getNextProductId())) || inProcessProduct.getNextProductId()== null){
			
			context.setManualRefresh(true);
		}
		else
			nextProductId = inProcessProduct.getNextProductId();
		return nextProductId;
	}

	public List<String> getIncomingProducts(DataCollectionState state) {
		
		List<String> expectedProductID = new ArrayList<String>();
		String nextProductId = getNextExpectedProductId(this.expectedProdId.getLastProcessedProduct());
		expectedProductID.add(nextProductId);
	return expectedProductID;
	}

	public boolean isProductIdAheadOfExpectedProductId(
			String expectedProductId, String productId) {
		
		try {
			InProcessProductDao inProcessProductDao = ServiceFactory.getDao(  InProcessProductDao.class);
			return inProcessProductDao.isProductIdAheadOfExpectedProductId(expectedProductId,productId);
		} catch (Exception e) {
			Logger.getLogger().warn(e, "Unable to execute isProductIdAheadOfExpectedProductId.");
			return false;
		}	
	}
	

	public void updateProductSequence(ProcessProduct state) {
		
		
	}
	@Override
	public void saveNextExpectedProduct(ProcessProduct state) {

		String nextProductId = null;
		if(context.getProperty().isExpectedProductSequenceResetAllowed()){
			nextProductId = getNextExpectedProductId(state.getProductId());
		}else{
			nextProductId = getNextExpectedProductId(state.getExpectedProductId());
		}

			saveNextExpectedProduct(nextProductId,state.getProductId());
	
		state.setExpectedProductId(nextProductId);
		Logger.getLogger().info("Save Next Expected Product:", nextProductId);
	}
	
	protected void getExpectedProductIdFromServer(ProcessProduct state) {
		ExpectedProductDao expectedProductDao = ServiceFactory.getDao(ExpectedProductDao.class);
		expectedProdId = expectedProductDao.findByKey(context.getProcessPointId());
		
		if (context.getProperty().isSaveNextProductAsExpectedProduct()) {
			if(expectedProdId == null){
				state.setExpectedProductId("");
				context.setManualRefresh(true);
			}
			else
			state.setExpectedProductId(expectedProdId.getProductId());
		} else {
			if(expectedProdId != null)
				state.setExpectedProductId(getNextExpectedProductId(expectedProdId.getLastProcessedProduct()));
			else {
				state.setExpectedProductId("");
				context.setManualRefresh(true);
			}
		}
		
		Logger.getLogger().info("Next expected product Id from server:", (expectedProdId == null ? "" : expectedProdId.getProductId()));
	}
	
	private String getAfOnLineId() {
		ProcessPoint afOnPp = ServiceFactory.getDao(ProcessPointDao.class).findByKey(getFrameLinePropertyBean().getAfOnProcessPointId());
		return StringUtils.trimToEmpty(afOnPp.getLineId());
		

		
	}
	
	public String findPreviousProductId(String productId) {
		String previousProductId = null;
		InProcessProduct previousInProcessProduct = context.getDbManager().findInProcessProductByNextProductId(productId);
		if (previousInProcessProduct != null) {
			previousProductId = previousInProcessProduct.getProductId();
		}
		return previousProductId;
	}
	
	public boolean isInSequenceProduct(String productId) {
		InProcessProduct inProcessProduct = context.getDbManager().findInProcessProductByKey(productId);
		return (inProcessProduct != null);
	}
}
