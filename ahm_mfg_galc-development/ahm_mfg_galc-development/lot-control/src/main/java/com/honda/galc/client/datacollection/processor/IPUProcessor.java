package com.honda.galc.client.datacollection.processor;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.ServiceInvocationException;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ProductSpecCodeDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.product.Ipu;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductSpecCode;
import com.honda.galc.entity.product.ProductSpecCodeId;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.service.ServiceFactory;

public class IPUProcessor extends SubProductSnProcessor {

	public IPUProcessor(ClientContext context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	protected ProductSpec findProductSpec(String productSpec) {
		ProductSpecCodeId  productSpecCodeId = new ProductSpecCodeId();
		productSpecCodeId.setProductType(ProductType.IPU.toString());
		productSpecCodeId.setProductSpecCode(productSpec);
		ProductSpec prodSpec = getProductSpecCodeDao().findByKey(productSpecCodeId);
		if(prodSpec == null) prodSpec = createProductSpec(productSpec);
		return prodSpec;
	}
	
	private ProductSpec createProductSpec(String productSpec){
		ProductSpecCode productSpecCode = new ProductSpecCode();
		ProductSpecCodeId  productSpecCodeId = new ProductSpecCodeId();
		productSpecCodeId.setProductType(ProductType.IPU.toString());
		productSpecCodeId.setProductSpecCode(productSpec);
		productSpecCode.setId(productSpecCodeId);
		productSpecCode.setModelYearCode(productSpec.substring(0,1));
		productSpecCode.setModelTypeCode(productSpec.substring(4,7));
		productSpecCode.setModelCode(productSpec.substring(1,4));
		productSpecCode.setExtColorCode("*");
		productSpecCode.setModelOptionCode("*");
		productSpecCode.setIntColorCode("*");
		return getProductSpecCodeDao().save(productSpecCode);
	}
	
	private ProductSpecCodeDao getProductSpecCodeDao(){
		return ServiceFactory.getDao(ProductSpecCodeDao.class);
	}
	
	protected void confirmProductId(ProductId productId)
	throws SystemException, TaskException, IOException
	{
		checkProductIdNotNull(productId); 
		product.setProductId(productId.getProductId());
//		checkProductIdLength(productId.getProductId());

		product.setProductId(parseProductId(productId.getProductId()));
		product.setProductSpec(parseMTO(productId.getProductId()));
		
		try {

			if(context.isOnLine())
				confirmProdIdOnServer();
			else
				confirmProductIdOnLocalCache();

			Logger.getLogger().info("Product KD lot number:", product.getKdLotNumber());
			
		} catch (TaskException te) {
			throw te;
		} catch (ServiceTimeoutException se) {
//			handleServerOffLineException(se);
		} catch (ServiceInvocationException sie) {
//			handleServerOffLineException(sie);
		} catch (Exception e){
			throw new TaskException(e.getClass().toString(), this.getClass().getSimpleName());
		}
		
		product.setValidProductId(true);

	}
	
	private String parseProductId(String productId) {
		return StringUtils.substring(productId, 0,13);
	}
	
	private String parseMTO(String productId) {
		return StringUtils.substring(productId, 13,20);
	}
	
	protected boolean confirmProdIdOnServer() throws Exception{
		Logger.getLogger().debug("ProductIdProcessor:Enter invoke on server.");

		Product aproduct = getProductFromServer();
		product.setSubId(aproduct.getSubId());
		product.setKdLotNumber(aproduct.getKdLotNumber());
					
		//must done before productIdOk
		loadLotControlRule();

		validateProduct(aproduct);
		
		doRequiredPartCheck();

		Logger.getLogger().debug("ProductIdProcessor:Return invoke on server.");
		return true;
	}
	
	protected Product getProductFromServer() {
		Product aProduct = (Product) context.getDbManager().confirmProductOnServer(product.getProductId());
		if(aProduct == null) aProduct = createProduct(); 
			product.setProductSpec(aProduct.getProductSpecCode());

		return aProduct;
	}	
	
	private Product createProduct() {
		SubProduct ipu = new Ipu();
		ipu.setProductId(product.getProductId());
		ipu.setProductSpecCode(product.getProductSpec());
		return context.getDbManager().createProduct(ipu);
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
