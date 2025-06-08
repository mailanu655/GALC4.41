package com.honda.galc.client.product;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.service.ServiceFactory;

 /**
  * 
  * 
  * <h3>AbstractExpectedProductManager Class description</h3>
  * <p> AbstractExpectedProductManager description </p>
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
public abstract class AbstractExpectedProductManager implements IExpectedProductManager{
	
	protected ExpectedProduct expectedProd;
	
	protected ProductModel model;
	
	protected Logger logger;

	public AbstractExpectedProductManager(ProductModel model, Logger logger) {
		super();
		this.model = model;
		this.logger = logger;
	}
	
	public abstract String getNextExpectedProductId(String productId);

	
	public String getExpectedProductId() {
		ExpectedProductDao expectedProductDao = ServiceFactory.getDao(ExpectedProductDao.class);
		expectedProd = expectedProductDao.findByKey(model.getProcessPointId());
		return expectedProd == null ? null : expectedProd.getProductId();
	}

	public void saveNextExpectedProduct() {

		if(StringUtils.isEmpty(model.getExpectedProductId())) return;

		String nextProductId = null;
		if(model.getProperty().isExpectedProductSequenceResetAllowed()){
			nextProductId = getNextExpectedProductId(model.getProductId());
		}else{
			nextProductId = getNextExpectedProductId(model.getExpectedProductId());
		}
		if(model.getProperty().isSaveNextProductAsExpectedProduct()){
			saveNextExpectedProduct(nextProductId, model.getProductId());
		} else {
			saveNextExpectedProduct(model.getExpectedProductId(),model.getProductId());
		}
	}
	
	public void saveNextExpectedProduct(String nextProductId, String lastProcessedProductId) {
		
		ExpectedProduct expectedProduct = new ExpectedProduct();
		expectedProduct.setProductId(nextProductId == null ? "" : nextProductId);
		expectedProduct.setProcessPointId(model.getProcessPointId());
		expectedProduct.setLastProcessedProduct(lastProcessedProductId);
		ExpectedProductDao expectedProductDao = ServiceFactory.getDao(ExpectedProductDao.class);
		expectedProductDao.save(expectedProduct);
		getLogger().info("Save Next Expected Product:", nextProductId);
	}

	public void saveNextExpectedProduct(String nextProductId) {
		
		ExpectedProduct expectedProduct = new ExpectedProduct();
		expectedProduct.setProductId(nextProductId == null ? "" : nextProductId);
		expectedProduct.setProcessPointId(model.getProcessPointId());
		
		ExpectedProductDao expectedProductDao = ServiceFactory.getDao(ExpectedProductDao.class);
		expectedProductDao.save(expectedProduct);
		getLogger().info("Save Next Expected Product:", nextProductId);
	}
	
	public void updatePreProductionLot() {}
	
	protected Logger getLogger() {
		return logger;
	}
}
