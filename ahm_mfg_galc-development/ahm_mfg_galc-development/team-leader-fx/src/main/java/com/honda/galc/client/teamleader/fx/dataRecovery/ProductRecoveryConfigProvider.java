package com.honda.galc.client.teamleader.fx.dataRecovery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProductRecoveryConfigProvider</code> is ...
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD> L&T Infotech</TD>
 * <TD>Jul 13, 2017</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author  L&T Infotech
 */
public class ProductRecoveryConfigProvider {

	private static Gson gson = new Gson();

	/**
	 * Used by TL to create recovery panels
	 * 
	 * @param componentId
	 * @return
	 */
	public static List<ProductRecoveryConfig> createProductRecoveryConfigs(String componentId) {
		DataRecoveryPropertyBean property = getProperty(componentId);
		Map<String, String> partDefinitionMap = property.getRecoveryPart() == null ? new HashMap<String, String>() : property.getRecoveryPart();
		Map<String, String> configPropertyMap = property.getRecoveryConfig() == null ? new HashMap<String, String>() : property.getRecoveryConfig();
		return createProductRecoveryConfigs(configPropertyMap, partDefinitionMap);
	}

	/**
	 * Used by product clients (currenlty QICS) to create single recovery panel
	 * 
	 * @param processPoint
	 * @param productType
	 * @param modelCode
	 * @return
	 */
	public static ProductRecoveryConfig createProductRecoveryConfig(ProcessPoint processPoint, ProductType productType, String modelCode) {
		List<ProductRecoveryConfig> configs = createProductRecoveryConfigs(processPoint.getProcessPointId());
		Map<String, ProductRecoveryConfig> map = new HashMap<String, ProductRecoveryConfig>();
		for (ProductRecoveryConfig config : configs) {
			map.put(config.getConfigId(), config);
		}
		if (map.containsKey(modelCode)) {
			return map.get(modelCode);
		} else if (map.containsKey("*")) {
			return map.get("*");
		}
		for (String configId : map.keySet()) {
			ProductRecoveryConfig recoveryConfig = ProductRecoveryConfig.getConfig(configId);
			if (recoveryConfig != null) {
				return recoveryConfig;
			}
		}
		return null;
	}

	protected static DataRecoveryPropertyBean getProperty(String componentId) {
		DataRecoveryPropertyBean property = null;
		property = PropertyService.getPropertyBean(DataRecoveryPropertyBean.class, componentId);
		return property;
	}

	public static Gson getGson() {
		return gson;
	}

	// === utility === //
	public static List<ProductRecoveryConfig> createProductRecoveryConfigs(Map<String, String> configPropertyMap, Map<String, String> partDefinitionMap) {
		List<ProductRecoveryConfig> configs = new ArrayList<ProductRecoveryConfig>();
		replace(partDefinitionMap, partDefinitionMap);
		replace(configPropertyMap, partDefinitionMap);
		for (String key : configPropertyMap.keySet()) {
			ProductRecoveryConfig config = ProductRecoveryConfig.getConfig(key);
			if (config == null) {
				String string = configPropertyMap.get(key);
				try {
					config = getGson().fromJson(string, ProductRecoveryConfig.class);
				} catch (Exception e) {
					System.err.println(string);
					e.printStackTrace();
					throw new TaskException("Failed to create Recovery Config :\n" + string + "\n",e);
				}
			}
			if (config != null) {
				if (StringUtils.isBlank(config.getConfigId())) {
					config.setConfigId(key);
				}
				configs.add(config);
			}
		}
		return configs;
	}

	protected static void replace(Map<String, String> map, Map<String, String> replaceWithMap) {
		if (map == null || replaceWithMap == null) {
			return;
		}
		for (String replaceKey : replaceWithMap.keySet()) {
			String replaceValue = StringUtils.trimToEmpty(replaceWithMap.get(replaceKey));
			for (String key : map.keySet()) {
				String value = StringUtils.trimToEmpty(map.get(key));
				String replaceToken = String.format("{%s}", replaceKey);
				if (value.indexOf(replaceToken) != -1) {
					value = value.replace(replaceToken, replaceValue);
					map.put(key, value);
				}
			}
		}
	}
}
