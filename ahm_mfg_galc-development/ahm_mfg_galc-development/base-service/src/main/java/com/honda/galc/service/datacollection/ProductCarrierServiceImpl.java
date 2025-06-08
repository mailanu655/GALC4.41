package com.honda.galc.service.datacollection;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.ProductCarrierDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.ProductCarrier;
import com.honda.galc.entity.product.ProductCarrierId;
import com.honda.galc.service.ProductCarrierService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ServiceUtil;

public class ProductCarrierServiceImpl extends IoServiceBase implements ProductCarrierService{
	
	@Override
	public DataContainer processData() {
		String carrierId=(String) getDevice().getInputValue(TagNames.CARRIER_ID.name());
		String productId=(String) getDevice().getInputValue(TagNames.PRODUCT_ID.name());		
		boolean aFlag = false;	
		try{
			String productKey=checkProductId(productId, getProductType());	
			checkCarrierId(carrierId);	
			
			boolean isAssociation = PropertyService.getPropertyBoolean(getDevice().getIoProcessPointId(), "IS_ASSOCIATION", true);
			if(isAssociation) associateProductCarrier(productKey,carrierId);
			else deassociateProductCarrier(productKey,carrierId);
			aFlag = true;
		} catch (TaskException te) {
			getLogger().error(te, "Exception when collect data for ", this.getClass().getSimpleName());
		} catch (Throwable e){
			getLogger().error(e, "Exception when collect data for ", this.getClass().getSimpleName());
		}
		return dataCollectionComplete(aFlag);
	}

	private void associateProductCarrier(String productId,String carrierId){
		ProductCarrierDao dao = getDao(ProductCarrierDao.class);
		ProductCarrier carrier = getProductCarrier(productId,carrierId);
		carrier.setProcessPointId(getProcessPointId());
		dao.save(carrier);		
		getLogger().info("The productId#carrierId ##" + productId + "/" + carrierId + " record is associated.");	
	}
	
	
	private void deassociateProductCarrier(String productId,String carrierId){
		ProductCarrierDao dao = getDao(ProductCarrierDao.class);
		List<ProductCarrier> productCarrierList = dao.findAll(productId,carrierId);		
		
		for(ProductCarrier productCarrier : productCarrierList)
			productCarrier.setOffTimestamp(new Timestamp(System.currentTimeMillis()));	
		if(!productCarrierList.isEmpty()) {
			dao.saveAll(productCarrierList);
			getLogger().info("The productId#carrierId ##" + productId + "/" + carrierId + " record is de-associated.");	
		}else {
			getLogger().error("The productId#carrierId ## " + productId + "/" + carrierId + " is invalid record");
		}
	}
	
	private ProductCarrier getProductCarrier(String productId,String carrierId) {
		ProductCarrier carrier = new ProductCarrier();
		ProductCarrierId carId = new ProductCarrierId(
				productId,
				carrierId, 				
				new Timestamp(System.currentTimeMillis()));
		carrier.setId(carId);	
		return carrier;
	}
	
	public String checkProductId(String productId, String productType){
		Map<Object, Object> context = new HashMap<Object, Object>();
		String productKey= ServiceUtil.validateProductId(productType, productId, context, productId).getProductId();
		checkContext(context);
		getLogger().info("The product " + productId + " is valid.");	
		return productKey;
	}
		
	public void checkCarrierId(String carrierId){
			if(StringUtils.isEmpty(carrierId)) {
				throw new TaskException("Carrier Id is null");
			} 
	}
}
