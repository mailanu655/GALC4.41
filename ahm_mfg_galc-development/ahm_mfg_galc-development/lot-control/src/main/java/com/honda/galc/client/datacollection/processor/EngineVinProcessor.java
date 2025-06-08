package com.honda.galc.client.datacollection.processor;

import java.io.IOException;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.service.ServiceFactory;

public class EngineVinProcessor extends FrameVinProcessor{

	public EngineVinProcessor(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
		
	}

	protected void confirmProductId(ProductId productId)
	throws SystemException, TaskException, IOException
	{
		checkProductIdNotNull(productId); 
		product.setProductId(productId.getProductId());  
//		checkProductIdLength(productId.getProductId());
		product.setProductId(getVinByEngine(productId.getProductId())); 
		if(context.isCheckExpectedProductId())
			checkExpectedProduct(productId);
		try {

			if(context.isOnLine())
				confirmProdIdOnServer();  
			else
				confirmProductIdOnLocalCache();

			Logger.getLogger().info("Product KD lot number:", product.getKdLotNumber());
			
		} catch (TaskException te) {
			throw te;
		} catch (ServiceTimeoutException se) {

		} catch (ServiceInvocationException sie) {
			Logger.getLogger().info("Engine"+ sie.getMessage());
		} catch (Exception e){
			throw new TaskException(e.getClass().toString(), this.getClass().getSimpleName());
		}
		
		product.setValidProductId(true);

	}
	
	private String getVinByEngine(String prodId){
		String vin= null;
		EngineDao engineDao = ServiceFactory.getDao(EngineDao.class);
		Engine engine = engineDao.findByKey(prodId);
		if(engine== null)
			handleException("Engine: " + product.getProductId() + " does not exist.");
		else{
			if(engine.getVin()== null || engine.getVin().length()== 0)
				handleException("VIN for this productId  " + product.getProductId() + ": does not exist.");
			else
			vin = engine.getVin().trim();
		}
		return vin; 
	}
	
	/** NALC-1574-MAX_PRODUCT_SN_LENGTH is set to 17 ( I cannot add both 17 and length 11)*/
	@Deprecated
	protected void checkProductIdLength(String productId){
		if(productId.length() != property.getMaxProductSnLength()){
			String msg = "Invalid Product Id: " + product.getProductId() + ", length invalid.";
			handleException(msg);
		}

	}


}


