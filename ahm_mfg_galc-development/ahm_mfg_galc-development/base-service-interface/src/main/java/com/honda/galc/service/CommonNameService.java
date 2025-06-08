package com.honda.galc.service;

import java.util.List;

import com.honda.galc.entity.product.InstalledPart;

public interface CommonNameService extends IService {
	
	public String getLatestPartSerialNumberByCommonName(String productId, String commonPartName);
	
	public List<InstalledPart> findAllInstalledPartByCommonNameList(String productId, List<String> commonPartNameList);
	
	public List<InstalledPart> findAllInstalledPartByCommonNameListAndProductType(String productId, List<String> commonPartNameList, String productType);
	
}
