package com.honda.galc.service;

import java.util.List;
import com.honda.galc.service.IService;


public interface ProductCheckService extends IService{
	
	public String check(String productType, String productId, String processPointId, String productCheckType, List<String> installedPartNames);
	
	public String check(String productType, String ProductId, String processPointId, String productCheckType, String deviceId);
	
	public String check(String productType, String ProductId, String processPointId, String productCheckType);
	
	public String check(String productType, String productId, String processPointId, List<String> productCheckTypeIds);
	
	public String check(String productType, String productId, String processPointId, List<String> installedPartNames, String[] productCheckTypeIds);
	
	public String check(String productType, String productId, String processPointId, String[] productCheckTypeIds, List<String> partNames);
}

