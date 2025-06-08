package com.honda.galc.client.product;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.service.ServiceFactory;

import static com.honda.galc.service.ServiceFactory.getDao;

 /**
  * 
  * 
  * <h3>ExpectedProductManager Class description</h3>
  * <p> ExpectedProductManager description </p>
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
  * Mar 6, 2014
  *
  *
  */
public class ExpectedProductManager extends AbstractExpectedProductManager implements IExpectedProductManager{


	public ExpectedProductManager(ProductModel model, Logger logger) {
		super(model, logger);
	}

	@Override
	public String getNextExpectedProductId(String productId) {
		InProcessProduct inProcessProduct = getDao(InProcessProductDao.class).findByKey(productId);
		if(inProcessProduct == null) return null;

		String nextProductId = inProcessProduct.getNextProductId();
		return inProcessProduct.getNextProductId() != null ? 
				nextProductId :  getNextProductIdFromPreviousLine(inProcessProduct);
	}

	public List<String> getIncomingProducts() {
			//BCC 9/23/11: method was returning null, added call to class above, then added to list.
			List<String> expectedProductID = new ArrayList<String>();
			String nextProductId = getNextExpectedProductId(this.expectedProd.getProductId());
			expectedProductID.add(nextProductId);
		return expectedProductID;
	}

	public boolean isProductIdAheadOfExpectedProductId(String expectedProductId, String productId) {
		try {
			return getDao(InProcessProductDao.class).isProductIdAheadOfExpectedProductId(expectedProductId,productId);
		} catch (Exception e) {
			getLogger().warn(e, "Unable to execute isProductIdAheadOfExpectedProductId.");
			return false;
		}	
	}
	
	public String getNextProductIdFromPreviousLine(InProcessProduct inProcessProduct) {	
		ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findById(model.getProcessPointId());
		InProcessProduct nextInProcessProduct =  ServiceFactory.getDao(InProcessProductDao.class).findNextProductSeqByPlantName(processPoint.getProcessPointId(),processPoint.getPlantName());				
		if(nextInProcessProduct!=null)
		{
			return nextInProcessProduct.getProductId();
		}
		return null;
	}

	@Override
	public void updateProductSequence() {
		
	}

}
