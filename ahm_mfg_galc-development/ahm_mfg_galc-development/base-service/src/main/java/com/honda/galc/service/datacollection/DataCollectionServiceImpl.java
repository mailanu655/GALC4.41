package com.honda.galc.service.datacollection;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.data.TagNames;
import com.honda.galc.datacollection.LotControlRuleCache;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.service.DataCollectionService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ServiceUtil;

/**
 * 
 * <h3>DataCollectionServiceImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DataCollectionServiceImpl description </p>
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
 * @author Paul Chou
 * Mar 24, 2011
 *
 */

public class DataCollectionServiceImpl implements DataCollectionService{
	static Map<ProductType, Class<? extends ProductDataCollectorBase>> classMap = 
		new HashMap<ProductType, Class<? extends ProductDataCollectorBase>>();
	
	static {
		classMap.put(ProductType.ENGINE, ProductDataCollector.class);
		classMap.put(ProductType.FRAME, ProductDataCollector.class);
		classMap.put(ProductType.KNUCKLE, ProductDataCollector.class);
		classMap.put(ProductType.BLOCK, DiecastDataCollector.class);
		classMap.put(ProductType.HEAD, DiecastDataCollector.class);
		classMap.put(ProductType.MBPN, MbpnDataCollector.class);
		classMap.put(ProductType.CRANKSHAFT, DiecastDataCollector.class);
		classMap.put(ProductType.CONROD, DiecastDataCollector.class);
		classMap.put(ProductType.BUMPER, ProductDataCollector.class);
		classMap.put(ProductType.MISSION, ProductDataCollector.class);
		classMap.put(ProductType.MCASE, DiecastDataCollector.class);
		classMap.put(ProductType.TCCASE, DiecastDataCollector.class);
		classMap.put(ProductType.FIPUCASE, ProductDataCollector.class);
		classMap.put(ProductType.RIPUCASE, ProductDataCollector.class);
		classMap.put(ProductType.MPDR, ProductDataCollector.class);
		classMap.put(ProductType.MPDN, ProductDataCollector.class);
		classMap.put(ProductType.PSDR, ProductDataCollector.class);
		classMap.put(ProductType.PSDN, ProductDataCollector.class);
		classMap.put(ProductType.TDU, MbpnDataCollector.class);
		classMap.put(ProductType.IPU_MBPN, MbpnDataCollector.class);
		classMap.put(ProductType.BMP_MBPN, MbpnDataCollector.class);
		classMap.put(ProductType.SUBFRAME, MbpnDataCollector.class);
		classMap.put(ProductType.KNU_MBPN, MbpnDataCollector.class);
		classMap.put(ProductType.MBPN_PART, MbpnDataCollector.class);
	}
	
	HeadLessPropertyBean propertyBean;
	
	public DataCollectionServiceImpl() {
		super();
	}

	public Device execute(Device device) {
		try {
			propertyBean = PropertyService.getPropertyBean(HeadLessPropertyBean.class, device.getIoProcessPointId());
			return getDataCollector(getProductType(device)).execute(device);
		} catch (Exception e) {
			getLogger(getProcessPointId(device)).error(e, " Exception: DataCollector execute exception.");
			return null;
		}
	}

	public DataContainer execute(DataContainer data){
		try{
			propertyBean = PropertyService.getPropertyBean(HeadLessPropertyBean.class, (String)data.get(TagNames.PROCESS_POINT_ID.name()));
			return getDataCollector(getProductType(data)).execute(data);
		} catch(Exception e){

			getLogger(getProcessPointId(data)).error(e, " Exception: DataCollector execute exception.");
			return null;
		}
	}
	
	// this method is required for java reflection
	public DataContainer execute(DefaultDataContainer data){
		DataContainer dt = data;
		return execute(dt);
	}

	private String getProcessPointId(DataContainer data) {
		return (String)data.get(TagNames.PROCESS_POINT_ID.name());
	}


	private ProductType getProductType(DataContainer data) {
		
		return getProductType((String)data.get(TagNames.PRODUCT_TYPE.name()));
	}


	protected ProductType getProductType(Device device) {
		String type = (String)device.getInputValue(TagNames.PRODUCT_TYPE.name());
		return getProductType(type);

	}
	
	private Logger getLogger(String processPointId) {
		return Logger.getLogger(processPointId + (propertyBean == null ? "" :propertyBean.getNewLogSuffix()));
		
	}


	private String getProcessPointId(Device device) {
		return (String)device.getInputValue(TagNames.PROCESS_POINT_ID.name());
	}


	private ProductType getProductType(String type) {
		ProductType productType = null;
		if (StringUtils.isEmpty(type)) {
			type = propertyBean.getProductType();
		}
		
		if(!StringUtils.isEmpty(type))
			productType = ProductTypeCatalog.getProductType(type);
		
		if(productType == null)
			throw new TaskException("Invalid product type:" + type, this.getClass().getSimpleName());

		return productType;
	}


	private DataCollectionService getDataCollector(ProductType productType) {

		return getProductDataCollector(productType);
	}
	
	private DataCollector getProductDataCollector(ProductType productType) {
		
		try {
			return ServiceFactory.getService(getServiceClass(productType));
		} catch (Exception e) {
			Logger.getLogger().error("Failed to get tracker. unspported product type:" + productType.toString());
			throw new TaskException("Unsupported product type:" + productType.toString());
		}
		
	}


	private Class<? extends ProductDataCollectorBase> getServiceClass(ProductType productType) {
		return classMap.get(productType);
	}


	public void refreshLotControlRuleCache(String processPointId) {
		LotControlRuleCache.removeLotCtrolRules(processPointId);
		Logger.getLogger().info("Lot Control Rule for process point:", processPointId, 
				" was removed from cache. url:", HttpServiceProvider.url);
		
	}


	public String getShortServerName() {
		return ServiceUtil.getShortServerName();
	}

}
