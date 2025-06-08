package com.honda.galc.service.productcheck;

import java.util.List;
import java.util.Map;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.ProductCheckService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductCheckUtil;

/** * * 
* @version 0.1 
* @author Dmitri Kouznetsov
* @since Sep 1, 2020
*/

public class ProductCheckServiceImpl implements ProductCheckService {
	
	public String check(String productType, String productId, String processPointId, String productCheckType,
			List<String> installedPartNames) {
		ProcessPoint processPoint = this.getProcessPoint(processPointId);
		if (processPoint == null) return null;
		
		BaseProduct product = this.getProduct(productType, productId, processPointId);
		if (product == null) return null;
		
		Object result = new ProductCheckUtil().check(product, processPoint, installedPartNames, productCheckType);
		return ProductCheckUtil.formatTxt(result, ":");
	}

	public String check(String productType, String productId, String processPointId, String productCheckType,
			String deviceId) {
		ProcessPoint processPoint = this.getProcessPoint(processPointId);
		if (processPoint == null) return null;
		
		BaseProduct product = this.getProduct(productType, productId, processPointId);
		if (product == null) return null;
		
		Device device = ServiceFactory.getDao(DeviceDao.class).findByKey(deviceId);
		if (device == null) {
			Logger.getLogger(PropertyService.getLoggerName(processPointId)).error("Device " + deviceId + " not found.");
			return null;
		}
		Object result = new ProductCheckUtil().check(product, processPoint, productCheckType, device);
		return ProductCheckUtil.formatTxt(result, ":");
	}
	
	public String check(String productType, String productId, String processPointId, String productCheckType) {	
		ProcessPoint processPoint = this.getProcessPoint(processPointId);
		if (processPoint == null) return null;
		
		BaseProduct product = this.getProduct(productType, productId, processPointId);
		if (product == null) return null;
		
		Object result = new ProductCheckUtil().check(product, processPoint, productCheckType);
		return ProductCheckUtil.formatTxt(result, ":");
	}
	
	public String check(String productType, String productId, String processPointId, List<String> productCheckTypeIds) {	
		ProcessPoint processPoint = this.getProcessPoint(processPointId);
		if (processPoint == null) return null;
		
		BaseProduct product = this.getProduct(productType, productId, processPointId);
		if (product == null) return null;
		
		Map<String, Object> results = ProductCheckUtil.check(product, processPoint, productCheckTypeIds.toArray(new String[0]));
		return ProductCheckUtil.formatTxt(results);
	}
	
	public String check(String productType, String productId, String processPointId, List<String> installedPartNames, String[] productCheckTypeIds) {
		ProcessPoint processPoint = this.getProcessPoint(processPointId);
		if (processPoint == null) return null;
		
		BaseProduct product = this.getProduct(productType, productId, processPointId);
		if (product == null) return null;
		
		Map<String, Object> results = ProductCheckUtil.check(product, processPoint, installedPartNames, productCheckTypeIds);
		return ProductCheckUtil.formatTxt(results);
	}
	
	public String check(String productType, String productId, String processPointId, String[] productCheckTypeIds, List<String> partNames) {
		ProcessPoint processPoint = this.getProcessPoint(processPointId);
		if (processPoint == null) return null;
		
		BaseProduct product = this.getProduct(productType, productId, processPointId);
		if (product == null) return null;
		
		Map<String, Object> results = ProductCheckUtil.check(product, processPoint, productCheckTypeIds, partNames);
		return ProductCheckUtil.formatTxt(results);
	}
	
	private ProcessPoint getProcessPoint(String processPointId){
		ProcessPoint processPoint = ServiceFactory.getDao(ProcessPointDao.class).findById(processPointId);
		if (processPoint == null) Logger.getLogger().error("Process point " + processPointId + " not found.");
		return processPoint;
	}
	
	private BaseProduct getProduct(String productType, String productId, String processPointId) {
		if (productType == null) {
			this.getLogger(processPointId).error("Product type " + productType + " not found.");
			return null;
		}
		
		BaseProduct product = ProductTypeUtil.findProduct(productType, productId);
		if (product == null) {
			this.getLogger(processPointId).error("Product " + productType + "[" + productId + "] not found.");
			return null;
		}
		
		return product;
	}
	
	private Logger getLogger(String processPointId) {
		return Logger.getLogger(PropertyService.getLoggerName(processPointId));
	}
}
