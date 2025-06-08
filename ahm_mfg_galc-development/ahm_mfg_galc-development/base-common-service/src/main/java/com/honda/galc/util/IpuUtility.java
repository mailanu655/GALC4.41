package com.honda.galc.util;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.entity.product.BuildAttribute;

public class IpuUtility {
	private static final String ESU ="ENERGY_STORAGE_UNIT";
	private static final String DC_JUNCTION_BOARD = "DC_JUNCTION_BOARD";
	public static BuildAttributeDao buildAttributeDao;
	public static MbpnProductDao mbpnProductDao;
	
	public static BuildAttributeDao getBuildAttributeDao() {
		if(buildAttributeDao == null){
			buildAttributeDao = getDao(BuildAttributeDao.class);
		}
		return buildAttributeDao;
	}
	
	public static MbpnProductDao getMbpnProductDao() {
		if (mbpnProductDao == null) {
			mbpnProductDao = getDao(MbpnProductDao.class);
		}
		return mbpnProductDao;
	}
	
	public static String checkPrefix(String productSpecCode,String serialNumber){
		List<BuildAttribute> esuAttributes = null;
		List<BuildAttribute> dcJunctionAttributes = null;
		String[] validPrefixes = null;
		String prefixList = "";
		BuildAttribute attribute = null;
		
		//Skip if it is Mbpn Repair functionality
		if(getMbpnProductDao().findByKey(serialNumber) == null){
			esuAttributes = getBuildAttributeDao().findAllMatchBuildAttributes(ESU);
			dcJunctionAttributes = getBuildAttributeDao().findAllMatchBuildAttributes(DC_JUNCTION_BOARD);
			
			if(dcJunctionAttributes != null && !dcJunctionAttributes.isEmpty()){
				for(BuildAttribute dcJunctionAttribute : dcJunctionAttributes){
					if(CommonPartUtility.simpleVerification(productSpecCode, dcJunctionAttribute.getProductSpecCode())){
						attribute = dcJunctionAttribute;
						break;
					}
				}
			}
			//Energy storage unit spec code
			if(attribute == null && esuAttributes != null && !esuAttributes.isEmpty()){ 
				for(BuildAttribute esuAttribute : esuAttributes){
					if(CommonPartUtility.simpleVerification(productSpecCode, esuAttribute.getProductSpecCode())){
						attribute = esuAttribute;
						break;
					}
				}
			}
			if(attribute != null && StringUtils.isNotEmpty(attribute.getAttributeValue())){
				validPrefixes = StringUtils.split(attribute.getAttributeValue(), ",");
				 prefixList = attribute.getAttributeValue();
			}
			else{
				return "The given productSpecCode '"+productSpecCode+"' has no build attributes.";
			}
			if(validPrefixes!= null && !StringUtils.startsWithAny(serialNumber, validPrefixes)){
				return "The valid prefixes for '"+productSpecCode+"' are :"+prefixList;
			}
		}
		return null;
	}

}
