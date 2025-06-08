package com.honda.galc.service.tracking;

import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ProductHistory;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class TrackingServiceImpl implements TrackingService {
	
	@SuppressWarnings("unchecked")
	static Map<ProductType, Class<? extends ProductTrackerBase>> classMap = 
		new HashMap<ProductType, Class<? extends ProductTrackerBase>>();
	
	static {
		classMap.put(ProductType.ENGINE, EngineTracker.class);
		classMap.put(ProductType.FRAME, FrameTracker.class);
		classMap.put(ProductType.KNUCKLE, SubProductTracker.class);
		classMap.put(ProductType.BLOCK, BlockTracker.class);
		classMap.put(ProductType.HEAD, HeadTracker.class);
		classMap.put(ProductType.IPU, SubProductTracker.class);
		classMap.put(ProductType.PLASTICS, MBPNProductTracker.class);
		classMap.put(ProductType.WELD, SubProductTracker.class);
		classMap.put(ProductType.MBPN, MBPNProductTracker.class);
		classMap.put(ProductType.BUMPER, SubProductTracker.class);
		classMap.put(ProductType.CONROD, ConrodTracker.class);
		classMap.put(ProductType.CRANKSHAFT, CrankshaftTracker.class);
		classMap.put(ProductType.MISSION, MissionTracker.class);
		classMap.put(ProductType.MCASE, CaseTracker.class);
		classMap.put(ProductType.TCCASE, CaseTracker.class);
		classMap.put(ProductType.FIPUCASE, CaseTracker.class);
		classMap.put(ProductType.RIPUCASE, CaseTracker.class);
		classMap.put(ProductType.MPDR, SubProductTracker.class);
		classMap.put(ProductType.MPDN, SubProductTracker.class);
		classMap.put(ProductType.PSDR, SubProductTracker.class);
		classMap.put(ProductType.PSDN, SubProductTracker.class);
		classMap.put(ProductType.IPU_MBPN, MBPNProductTracker.class);
		classMap.put(ProductType.TDU, MBPNProductTracker.class);
		classMap.put(ProductType.BMP_MBPN, MBPNProductTracker.class);
		classMap.put(ProductType.SUBFRAME, MBPNProductTracker.class);
		classMap.put(ProductType.KNU_MBPN, MBPNProductTracker.class);
		classMap.put(ProductType.MBPN_PART, MBPNProductTracker.class);			
	}
	
	@Transactional
	public void track(BaseProduct product, String processPointId) {
		Tracker<BaseProduct> productTracker = getProductTracker(product.getProductType());
		
		if(productTracker != null)
			productTracker.track(product, processPointId);
		else
			getLogger(processPointId).warn("Failed to find tracker for:"  + product.getProductType());
	}

	@Transactional
	public void track(BaseProduct product, String processPointId, String deviceId) {
		Tracker<BaseProduct> productTracker = getProductTracker(product.getProductType());
		
		if(productTracker != null)
			productTracker.track(product, processPointId, deviceId);
		else
			getLogger(processPointId).warn("Failed to find tracker for:"  + product.getProductType());
	}
	@Transactional
	public void track(ProductType productType, String productId, String processPointId) {
		Tracker<BaseProduct> productTracker = getProductTracker(productType);
		
		if(productTracker != null)
			productTracker.track(productId, processPointId);
		else
			getLogger(processPointId).warn("Failed to find tracker for:"  + productType);
	}
	

	@Transactional
	public void track(ProductType productType, String productId, String processPointId, String deviceId) {
		Tracker<BaseProduct> productTracker = getProductTracker(productType);
		
		if(productTracker != null)
			productTracker.track(productId, processPointId,deviceId);
		else
			getLogger(processPointId).warn("Failed to find tracker for:"  + productType);
	}
	@Transactional
	public void track(ProductType productType, ProductHistory productHistory) {
		Tracker<BaseProduct> productTracker = getProductTracker(productType);
		
		if(productTracker != null) {
			productTracker.track(productHistory);
		} else {
			getLogger(productHistory.getProcessPointId()).warn("Failed to find tracker for:"  + productType);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Class<? extends ProductTrackerBase> getServiceClass(ProductType productType) {
		return classMap.get(productType);
	}

	@SuppressWarnings("unchecked")
	private Tracker<BaseProduct> getProductTracker(ProductType productType) {
		
		try {
			return ServiceFactory.getService(getServiceClass(productType));
		} catch (Exception e) {
			Logger.getLogger().error("Failed to get tracker. unspported product type:" + productType.toString());
		}
		
		return null;
	}
	
	private Logger getLogger(String processPointId) {
		return Logger.getLogger(PropertyService.getLoggerName(processPointId));
	}
}
