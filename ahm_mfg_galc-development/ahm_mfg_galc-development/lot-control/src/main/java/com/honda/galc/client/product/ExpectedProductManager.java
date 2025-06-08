package com.honda.galc.client.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.observer.AbstractExpectedProductManager;
import com.honda.galc.client.datacollection.observer.IExpectedProductManager;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.service.ServiceFactory;

 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class ExpectedProductManager extends AbstractExpectedProductManager
implements IExpectedProductManager{

	public ExpectedProductManager(ClientContext context) {
		super(context);
	}

	@Override
	public String getNextExpectedProductId(String productId) {
		InProcessProductDao inProcessProductDao = ServiceFactory.getDao(InProcessProductDao.class);
		if (StringUtils.isBlank(productId) && !context.getProperty().isCheckExpectedProductFromPreviousLine()) {
			productId = expectedProdId.getLastProcessedProduct();
		}
		InProcessProduct inProcessProduct = inProcessProductDao.findByKey(productId);
		if (inProcessProduct == null)
			return null;

		String nextProductId = inProcessProduct.getNextProductId();

		if (nextProductId != null) {
			return nextProductId;
		} else if (context.getProperty().isCheckExpectedProductFromPreviousLine()) {
			return getNextProductIdFromPreviousLine(inProcessProduct);
		} else
			return "";
	}

	public List<String> getIncomingProducts(DataCollectionState state) {
		String productId = (state instanceof ProcessProduct) ? null : state.getProductId(); 
		productId= StringUtils.isEmpty(productId)? getNextExpectedProductId(state.getExpectedProductId()) : getNextExpectedProductId(productId);
		
		if(!StringUtils.isEmpty(productId)) {
			InProcessProductDao inProcessProductDao = ServiceFactory.getDao(InProcessProductDao.class);
			return inProcessProductDao.findIncomingExpectedProductIds(productId);
		} else {
			return new ArrayList<String>();
		}
	}

	public void getExpectedProductIdFromServer(ProcessProduct state) {
		super.getExpectedProductIdFromServer(state);
	}

	public void updateProductSequence(ProcessProduct state) {
		// TODO Auto-generated method stub
		
	}

	public boolean isProductIdAheadOfExpectedProductId(String expectedProductId, String productId) {
		try {
			InProcessProductDao inProcessProductDao = ServiceFactory.getDao(  InProcessProductDao.class);
			return inProcessProductDao.isProductIdAheadOfExpectedProductId(expectedProductId,productId);
		} catch (Exception e) {
			Logger.getLogger().warn(e, "Unable to execute isProductIdAheadOfExpectedProductId.");
			return false;
		}	
	}
	
	public String getNextProductIdFromPreviousLine(InProcessProduct inProcessProduct) {	
		ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findById(context.getProcessPointId());
		InProcessProduct nextInProcessProduct =  ServiceFactory.getDao(InProcessProductDao.class).findNextProductSeqByPlantName(processPoint.getProcessPointId(),processPoint.getPlantName());				
		if(nextInProcessProduct!=null)
		{
			return nextInProcessProduct.getProductId();
		}
		return null;
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
