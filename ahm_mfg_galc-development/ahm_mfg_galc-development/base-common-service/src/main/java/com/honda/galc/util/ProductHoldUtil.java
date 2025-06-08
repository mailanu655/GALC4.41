package com.honda.galc.util;

import java.util.Map;

import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.property.ProductHoldPropertyBean;
import com.honda.galc.service.property.PropertyService;

public class ProductHoldUtil {
	
	public static String getDefaultHoldAccessType(String processPointId) {
		ProductHoldPropertyBean propertyBean = getProductHoldPropertyBean(processPointId);
		return propertyBean.getDefaultHoldAccessType();
	}
	
	public static String getDefaultAccessTypeByHoldType(String processPointId, HoldResultType holdType) {
		ProductHoldPropertyBean propertyBean = getProductHoldPropertyBean(processPointId);
		Map<String,String> accessTypeMap = propertyBean.getDefaultAccessTypeByHoldMap(String.class);
		String holdAccessType = 
				accessTypeMap != null && accessTypeMap.containsKey(holdType.toString()) ? 
				accessTypeMap.get(holdType.toString()) : getDefaultHoldAccessType(processPointId);
		return holdAccessType;
	}
	
	private static ProductHoldPropertyBean getProductHoldPropertyBean(String processPointId) {
		return PropertyService.getPropertyBean(ProductHoldPropertyBean.class, processPointId);
	}
}