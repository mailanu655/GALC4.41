package com.honda.galc.client.datacollection.observer;

import java.util.List;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.service.ServiceFactory;

 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class IpuExpectedProductManager extends AbstractExpectedProductManager
implements IExpectedProductManager{

	public IpuExpectedProductManager(ClientContext context) {
		super(context);
	}

	@Override
	public String getNextExpectedProductId(String productId) {
		InProcessProductDao inProcessProductDao = ServiceFactory.getDao(InProcessProductDao.class);
		ProcessPointDao processPointDao = ServiceFactory.getDao(ProcessPointDao.class);
		ProcessPoint previousProcessPoint = processPointDao.findPreviousProcessPointByProcessPointId(context.getProcessPointId());
		String nextProductId = null;
		if (previousProcessPoint!=null){
			nextProductId = inProcessProductDao.findFirstUnconfirmed(context.getAppContext().getTerminal().getProcessPoint().getLineId(), previousProcessPoint.getProcessPointId(), context.getProcessPointId());
		}
		return nextProductId;
	}

	public List<String> getIncomingProducts(DataCollectionState state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getExpectedProductIdFromServer(ProcessProduct state) {
		ExpectedProductDao expectedProductDao = ServiceFactory.getDao(ExpectedProductDao.class);
		expectedProdId = expectedProductDao.findByKey(context.getProcessPointId());

		if (context.getProperty().isSaveNextProductAsExpectedProduct()) {
			state.setExpectedProductId(expectedProdId == null ? "" : expectedProdId.getProductId());
		} else {
			if(expectedProdId != null )
				state.setExpectedProductId(getNextExpectedProductId(expectedProdId.getProductId()));
			else 
				state.setExpectedProductId("");
		}
	}

	public void updateProductSequence(ProcessProduct state) {
		// TODO Auto-generated method stub
		
	}

	public boolean isProductIdAheadOfExpectedProductId(
			String expectedProductId, String ProductId) {
		// TODO Auto-generated method stub
		return false;
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
