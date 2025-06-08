package com.honda.galc.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.HiddenTransitionExclusionStrategy;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BlockBuildResult;
import com.honda.galc.entity.product.BlockHistory;
import com.honda.galc.entity.product.ConrodBuildResult;
import com.honda.galc.entity.product.CrankshaftBuildResult;
import com.honda.galc.entity.product.HeadBuildResult;
import com.honda.galc.entity.product.HeadHistory;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;


/**
 * 
 * <h3>ProductManifestDataUtil Class description</h3>
 * <p> ProductManifestDataUtil description </p>
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
 * @author Paul Chou<br>
 * Oct.5, 2020
 *
 *
 */
public class ProductManifestDataUtil {
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS z";
	
	public enum ManifestDataType {	
		INSTALLEDPART("InstalledPart", (new TypeToken<ArrayList<InstalledPart>>(){}).getType()), 
		MEASUREMENT("Measurement", (new TypeToken<ArrayList<Measurement>>(){}).getType()), 
		HEADBUILDRESULT("HeadBuildResult", (new TypeToken<ArrayList<HeadBuildResult>>(){}).getType()), 
		BLOCKBUILDRESULT("BlockBuildResult", (new TypeToken<ArrayList<BlockBuildResult>>(){}).getType()),
		CRANKSHAFTBUILDRESULT("CrankshaftBuildResult", (new TypeToken<ArrayList<CrankshaftBuildResult>>(){}).getType()),
		CONRODBUILDRESULT("ConrodBuildResult", (new TypeToken<ArrayList<ConrodBuildResult>>(){}).getType()),
		DEFECTRESULT("DefectResult",(new TypeToken<ArrayList<DefectResult>>(){}).getType()),
		PRODUCTHISTORY("ProductResult",(new TypeToken<ArrayList<ProductResult>>(){}).getType()),
		BLOCKHISTORY("BlockHistory",(new TypeToken<ArrayList<BlockHistory>>(){}).getType()),
		HEADHISTORY("HeadHistory",(new TypeToken<ArrayList<HeadHistory>>(){}).getType()),
		PRODUCTIONLOT("ProductionLot",(new TypeToken<ArrayList<ProductionLot>>(){}).getType()),
		PREPRODUCTIONLOT("PreProductionLot",(new TypeToken<ArrayList<PreProductionLot>>(){}).getType());
		

		String name;
		Type type;

		private ManifestDataType(String name, Type type){
			this.name = name;
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public static ManifestDataType getManifestDataType(String clz){
			for(ManifestDataType item : ManifestDataType.values())
				if(item.equals(clz) || item.getName().equals(clz))
					return item;

			return null;
		}


		public Type getJsonDataType() {
			return type;
		}

	};
	
	public static Gson getGson() {
		GsonBuilder builder = new GsonBuilder();
		builder.setExclusionStrategies( new HiddenTransitionExclusionStrategy() );
		builder.setDateFormat(DATE_FORMAT);

		return builder.create();
	}
	
	public static String toJson(BaseProduct product) {
		return getGson().toJson(product);		
	}

	public static String toJson(List<?> entityList) {
		return getGson().toJson(entityList);
	}
	
	public static List<? extends AuditEntry> fromJson(String entity, String gsonStr) {
		ManifestDataType mtype = ManifestDataType.getManifestDataType(entity);
		return getGson().fromJson(gsonStr, mtype.getJsonDataType());
	}
	
	public static BaseProduct fromJson(String jsonStr, ProductType prodType){
		ProductTypeUtil util = ProductTypeUtil.getTypeUtil(prodType);
		return getGson().fromJson(jsonStr, util.getProductClass());
	}
	
	public static void save(BaseProduct product) {
		ServiceFactory.getService(GenericDaoService.class).saveEntity(product);
	}
	
	public static void saveAll(List<? extends AuditEntry> entities) {
		ServiceFactory.getService(GenericDaoService.class).saveAllEntity(entities);
	}

	public static String findEntity(DeviceFormat df, DataContainer dc, String productType) {
		return ManifestDataHelper.getInstance().findEntity(df, dc, productType);
	}

	public static String getManifestDataJsonString(DataContainer dc, Device dev) {
		return getGson().toJson(getManifestDataContainer(dc, dev));
	}
	
	public static String findProductType(DataContainer dc) {
		String productType = dc.getString(DataContainerTag.PRODUCT_TYPE);
		if(StringUtils.isEmpty(productType)) {
			SystemPropertyBean bean;
			String ppId = dc.getString(DataContainerTag.PROCESS_POINT_ID);
			if (StringUtils.isEmpty(ppId))
				bean = PropertyService.getPropertyBean(SystemPropertyBean.class);
			else
				bean = PropertyService.getPropertyBean(SystemPropertyBean.class, StringUtils.trim(ppId));
			
			productType = bean.getProductType();
		}
		
		return productType;
	}

	@SuppressWarnings("unchecked")
	public static void saveManifestDataFromJsonString(String jsonMq, Logger logger) {
		try {
			Map<String, String> fromMq = ProductManifestDataUtil.getGson().fromJson(jsonMq, HashMap.class);
			String productType = fromMq.get(TagNames.PRODUCT_TYPE.name());
			fromMq.remove(TagNames.PRODUCT_TYPE.name());
			log(logger, "Manifest Data received product type:", productType);
			for (String key : fromMq.keySet()) {
				if (key.equals(DataContainerTag.Product)) {
					BaseProduct fromJson = ProductManifestDataUtil.fromJson(fromMq.get(DataContainerTag.Product),
							ProductType.valueOf(productType));
					ProductManifestDataUtil.save(fromJson);

					log(logger, "Manifest Data saved product:",	((fromJson.getId() == null) ? "null" : fromJson.getId().toString()));
				} else {
					if(StringUtils.isEmpty(fromMq.get(key)) || StringUtils.stripToEmpty(fromMq.get(key)).equals("[]")) {
						log(logger, "Manifest Data Warn:", "empty json payload received for: ", key);
						continue;
					}
					List<? extends AuditEntry> listFromJson = ProductManifestDataUtil.fromJson(key, fromMq.get(key));
					
					if(listFromJson == null || listFromJson.size() ==0) {
						log(logger,"Manifest Data no data from Json payload. json:", fromMq.get(key), " Key:", key);
						return;
					}
					
					ProductManifestDataUtil.saveAll(listFromJson);
					log(logger,"Manifest Data saved enities:" + listFromJson.get(0).getClass().getSimpleName());
				}

			} 
		} catch (Exception e) {
			if(logger != null)
				logger.error(e, "Manifest Data Exception.");
		}

	}

	private static void log(Logger logger, String... msg) {
		if (logger != null)
			logger.info(msg);
	}

	public static DataContainer getManifestDataContainer(DataContainer dc, Device dev) {
		Logger logger = getLogger(dc, dev);
		log(logger, "start Manifiest data:", dc.toString());
		DataContainer manifestDc = new DefaultDataContainer();
		String productType = findProductType(dc);
		log(logger, "productType:", productType);
		for(DeviceFormat df : dev.getDeviceDataFormats()) {
			if(df.getDeviceTagType() != DeviceTagType.ENTITY) continue;
			
			String entityStr = ProductManifestDataUtil.findEntity(df, dc, productType);
			if(!StringUtils.isEmpty(entityStr))
				manifestDc.put(df.getTag(), entityStr); 
		}

		manifestDc.put(TagNames.PRODUCT_TYPE.name(), dc.getString(TagNames.PRODUCT_TYPE.name()));
		log(logger, "Manifest Data Container:",manifestDc.toString());
		return manifestDc;
	}
	
	private static Logger getLogger(DataContainer data, Device device) {
		String loggerName = data.getString(TagNames.PROCESS_POINT_ID.name());
		if(StringUtils.isEmpty(loggerName)) {
			loggerName = device.getIoProcessPointId();
			
			if(StringUtils.isEmpty(loggerName))
				loggerName = device.getClientId();
		}
		

		return Logger.getLogger(loggerName);
	}


}