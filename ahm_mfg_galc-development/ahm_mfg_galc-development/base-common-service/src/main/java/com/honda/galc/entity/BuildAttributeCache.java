package com.honda.galc.entity;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.data.MbpnDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.BuildAttributeId;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductSpecUtil;


public class BuildAttributeCache extends EntityCache<BuildAttribute,BuildAttributeId> {
	
	Map<String, List<BuildAttribute>> buildAttributeSpace = new HashMap<String, List<BuildAttribute>>();

	public BuildAttributeCache() {
		super(getDao(BuildAttributeDao.class));
	}
	
	public BuildAttributeCache(List<BuildAttribute> entityList,BuildAttributeDao dao) {
		super(entityList,dao);
	}
	
	public BuildAttributeCache(List<BuildAttribute> entityList) {
		super(entityList,null);
	}
	
	public BuildAttributeCache(String... attributes) {
		super((BuildAttributeDao) null);
		for(String attribute : attributes) {
			entityList.addAll(getDao(BuildAttributeDao.class).findAllByAttribute(attribute));
		}
	}
	
	public BuildAttribute findByKey(BuildAttributeId id) {
		
		List<BuildAttribute> buildAttributes =  findAllByAttribute(id);
		if(buildAttributes.isEmpty() && dao !=null) {
			List<BuildAttribute> tempBuildAttributes  = ((BuildAttributeDao) dao).findAllByAttribute(id.getAttribute());
			entityList.addAll(tempBuildAttributes);
			List<BuildAttribute> matchedList =	ProductSpecUtil.getMatchedList(id.getProductSpecCode(), tempBuildAttributes , BuildAttribute.class);
			if(!matchedList.isEmpty()) {
				
				return matchedList.get(matchedList.size() -1);
			}else {
				return null;
			}
	      
		}
        if(buildAttributes.isEmpty()) return null;
        else {
        	List<BuildAttribute> matchedList = ProductSpecUtil.getMatchedList(id.getProductSpecCode(), buildAttributes , BuildAttribute.class);
        	return matchedList.get(matchedList.size() -1);
        }
	
	}
	
	public BuildAttribute findById(String productSpecCode, String attribute, String subId) {
		 
		return findByKey(new BuildAttributeId(attribute,productSpecCode, subId));
		
	}
	
	public String findAttributeValue(String productSpecCode,String attribute, String subId) {
		BuildAttribute item = findById(productSpecCode,attribute, subId);
		return item == null ? "" : item.getAttributeValue();
	}
	
	public String findAttributeValue(String productSpecCode, String buildAttributeName, ProductType productType) {
		return findAttributeValue(productSpecCode, buildAttributeName,"",productType);
	}
	
	public String findAttributeValue(String productSpecCode,String attribute, String subId, ProductType productType) {
		
		if(ProductTypeUtil.isMbpnProduct(productType.toString())){
			return findMbpnAttributeValue(productSpecCode, attribute, subId);
		} else 
			return findAttributeValue(productSpecCode, attribute, subId);
	}

	public String findAttributeValue(String productSpecCode,String attribute, String subId, String productType) {

		if(ProductTypeUtil.isMbpnProduct(productType)){
			return findMbpnAttributeValue(productSpecCode, attribute, subId);
		} else 
			return findAttributeValue(productSpecCode, attribute, subId);
	}
	
	private String findMbpnAttributeValue(String productSpecCode, String attribute, String subId) {
		List<BuildAttribute> buildAttributes =  findAllByAttribute(new BuildAttributeId(attribute, productSpecCode, subId));
		if(buildAttributes.isEmpty() && dao !=null) {
			buildAttributes = ((BuildAttributeDao) dao).findAllMatchId(MbpnDef.MAIN_NO.getValue(productSpecCode) + "%", attribute);
			entityList.addAll(buildAttributes);
			buildAttributes =  findAllByAttribute(new BuildAttributeId(attribute, productSpecCode, subId));
		}
		
		if(buildAttributes.isEmpty()) return "";
		
		return buildAttributes.get(buildAttributes.size() -1).getAttributeValue();
	}

	public void loadAttribute(String attribute) {
		if(!containsAttribute(attribute))
			entityList.addAll(getDao(BuildAttributeDao.class).findAllByAttribute(attribute));
	}
	
	public boolean containsAttribute(String attribute) {
		for(BuildAttribute item : entityList) {
			if(item.getId().getAttribute().equals(attribute)) return true;
		}
		
		return false;
	}
	
	
	public List<BuildAttribute> findAllByAttribute(BuildAttributeId id) {
		if(!StringUtils.isEmpty(id.getSubId())){//1. has valid sub id
			List<BuildAttribute> resultList = findAttribute(id, getBuildAttributes(id.getSubId()));
			if(resultList == null || resultList.size() == 0)
				resultList = findAttribute(id, entityList);
			return resultList;
		   
		} else //2. sub id is null or blank
			return findAttribute(id, entityList);
	}

	private List<BuildAttribute> findAttribute(BuildAttributeId id, List<BuildAttribute> targetList) {
		List<BuildAttribute> results = new ArrayList<BuildAttribute>();
		for(BuildAttribute item : targetList) {
			if(item.getId().getAttribute().equals(id.getAttribute())) {
				results.add(item);
			}
		}
		
		return ProductSpecUtil.getMatchedList(id.getProductSpecCode(), results, BuildAttribute.class);
	}

	private List<BuildAttribute> getBuildAttributes(String subId) {
		if(!buildAttributeSpace.containsKey(subId)){
			List<BuildAttribute> list = new ArrayList<BuildAttribute>();
			for(BuildAttribute ba : entityList)
				if(subId.equals(ba.getId())) list.add(ba);
			
			buildAttributeSpace.put(subId, list);
		}
		return buildAttributeSpace.get(subId);
	}

	public String findAttributeValue(String productSpecCode, String attributeName) {
		return findAttributeValue(productSpecCode, attributeName, "");
	}

	public BuildAttribute findById(String productSpecCode, String attribute) {
		return findById(productSpecCode, attribute, "");
	}
}
