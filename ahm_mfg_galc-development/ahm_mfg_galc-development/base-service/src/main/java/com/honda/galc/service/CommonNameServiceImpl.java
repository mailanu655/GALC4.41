package com.honda.galc.service;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.entity.product.InstalledPart;

public class CommonNameServiceImpl implements CommonNameService {

	@Override
	public String getLatestPartSerialNumberByCommonName(String productId, String commonPartName) {
		String partSerialNo = null;
		List<InstalledPart> installedPartList = ServiceFactory.getDao(InstalledPartDao.class).findAllInstalledPartByCommonName(productId, commonPartName);
		if(installedPartList != null && !installedPartList.isEmpty()) {
			partSerialNo = installedPartList.get(0).getPartSerialNumber();
		} else {
			String partName = StringUtils.trimToEmpty(commonPartName);
			partSerialNo = getDao(InstalledPartDao.class).getLatestPartSerialNumber(productId, StringUtils.trimToEmpty(partName));
		}
		return partSerialNo;
	}
	
	@Override
	public List<InstalledPart> findAllInstalledPartByCommonNameList(String productId, List<String> commonPartNameList) {
		List<InstalledPart> installedPartList = ServiceFactory.getDao(InstalledPartDao.class).findAllInstalledPartByCommonNameList(productId, commonPartNameList, StringUtils.EMPTY);
		if(installedPartList == null || installedPartList.isEmpty()) {
			installedPartList = ServiceFactory.getDao(InstalledPartDao.class).findAllByProductIdAndPartNames(productId, commonPartNameList);
		}
		return installedPartList;
	}
	
	@Override
	public List<InstalledPart> findAllInstalledPartByCommonNameListAndProductType(String productId, List<String> commonPartNameList, String productType) {
		List<InstalledPart> installedPartList = ServiceFactory.getDao(InstalledPartDao.class).findAllInstalledPartByCommonNameList(productId, commonPartNameList, productType);
		if(installedPartList == null || installedPartList.isEmpty()) {
			installedPartList = ServiceFactory.getDao(InstalledPartDao.class).findAllEngineByEngineProductIdAndPartNames(productType, productId, commonPartNameList);
		}
		return installedPartList;
	}
}
