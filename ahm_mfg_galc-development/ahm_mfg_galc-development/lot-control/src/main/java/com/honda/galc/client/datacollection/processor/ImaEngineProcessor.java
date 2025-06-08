package com.honda.galc.client.datacollection.processor;

import java.io.IOException;
import java.util.List;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Product;
import com.honda.galc.service.ServiceFactory;

public class ImaEngineProcessor extends EngineSnProcessor{

	public ImaEngineProcessor(ClientContext lotControlClientContext) {
		super(lotControlClientContext);
		
	}

	protected void confirmProductId(ProductId productId)
	throws SystemException, TaskException, IOException
	{
		checkProductIdNotNull(productId); 
		product.setProductId(productId.getProductId());  
//		checkProductIdLength(productId.getProductId());
		product.setProductId(parseEngineSerialNumber(productId.getProductId()));  
		
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
	
	private String parseEngineSerialNumber(String serialNumber) {
		
		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		List<InstalledPart> installedPart = installedPartDao.findAllByPartNameAndSerialNumber("IMA MOTOR", serialNumber);
		if(installedPart.size()==0)
			handleException("No engine installed part record exists for this motor");
		return installedPart.get(0).getProductId();
		
	}
	
	protected boolean confirmProdIdOnServer() throws Exception{
		Logger.getLogger().debug("ProductIdProcessor:Enter invoke on server.");

		Product aproduct = (Product) getProductFromServer(); 
		product.setSubId(aproduct.getSubId());
		product.setKdLotNumber(aproduct.getKdLotNumber());
					
		//must done before productIdOk
		loadLotControlRule();

		validateProduct(aproduct);
		
		doRequiredPartCheck();

		Logger.getLogger().debug("ProductIdProcessor:Return invoke on server.");
		return true;
	}
	/** NALC-1574-MAX_PRODUCT_SN_LENGTH is set to 17 ( I cannot add both 17 and length 11)*/
	@Deprecated
	protected void checkProductIdLength(String productId){
		if(productId.length() != property.getMaxProductSnLength()){
			String msg = "Invalid Product Id: " + product.getProductId() + ", length invalid.";
			handleException(msg);
		}

	}

	protected void validateProduct(Product aproduct) {
		if(property.isCheckProcessedProduct()) {
			checkProcessProduct();
		}
	}

	private void checkProcessProduct() {
		InstalledPartDao installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		List<InstalledPart> installedParts = installedPartDao.findAllByProductId(product.getProductId());
		
		for(InstalledPart part : installedParts){	
			if(part.getInstalledPartStatus() == InstalledPartStatus.REMOVED) continue;
				if(product.getProductId().equals(part.getId().getProductId()) && isProcessedPart(part.getId().getPartName()))
					if (!MessageDialog.confirm(context.getFrame(),"The product id entered is already processed.\nDo you want to proceed this product id?"))
						handleException("Product:" + product.getProductId() + " already processed!"); 
		}
	}
	
	protected boolean isProcessedPart(String partName) {
		List<LotControlRule> lotControlRules = state.getLotControlRules();
		for(int i = 0; i < lotControlRules.size(); i++)
		{
			LotControlRule r = lotControlRules.get(i);
			if(partName.equals(r.getPartName().getPartName()))
					return true;
		}
		return false;
	}
	
}

