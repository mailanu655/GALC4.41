package com.honda.galc.dao.product;


import java.util.List;

import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.BuildAttributeId;
import com.honda.galc.service.IDaoService;
import com.honda.galc.util.KeyValue;

public interface BuildAttributeDao extends IDaoService<BuildAttribute, BuildAttributeId> {
	
	public List<BuildAttribute> findAllByAttribute(String attribute);
	
	public BuildAttribute findfirstByAttributeAndValue(String attribute, String Value);
	
	public void updateDescription(String attribute, String value, String attributeDescription);
	
	public BuildAttribute findById(String attribute, String productSpecCode);
	
	public List<KeyValue<String, String>> findAllDistinctAttributes();
	
	/**
	 * find all by matching productSpecCode and attribute
	 * productSpecCode can be a wild card like "9SVA%" and attibute can be null
	 * @param productionSpecCode
	 * @param attribute
	 * @return
	 */
	public List<BuildAttribute> findAllMatchId(String productSpecCode, String attribute);
	
	/**
	 * Find all by exact productSpecCode
	 */
	public List<BuildAttribute> findAllByProductSpecCode(String productSpecCode);

	/**
	 * Find all matching build attributes 
	 * attributePrefix could be in the format "ABC%" 
	 * @param attributePrefix
	 * @return
	 */
	public List<BuildAttribute> findAllMatchBuildAttributes(String attributePrefix);
	
	public List<String> findByBuildAttributeValue(String attributeValue);
	
	/**
	 * Find all build attributes that have matching PrintForm formids 
	 * @return
	 */
	public List<BuildAttribute> findAllMatchPrintAttributes();
	
	

	/**
	 * Find all build attributes for a given product type
	 * @param productType
	 * @return
	 */
	public List<KeyValue<String, String>> findAllDistinctAttributes(
			String productType);

	/**
	 * Find all build attribute names for a given product type
	 * @param productType
	 * @return
	 */
	public List<String> findAllDistinctAttributeNames(
			String productType);

	/**
	 * find all by matching productSpecCode, attribute and product type
	 * productSpecCode can be a wild card like "9SVA%" and attribute can be null
	 * @param specCode
	 * @param attribute
	 * @param productType
	 * @return
	 */
	public List<BuildAttribute> findAllMatchId(String specCode,
			String attribute, String productType);

	/**
	 * Find all by matching productSpecCode, attribute, product type and attribute group.<br>
	 * productSpecCode can be a wild card like "9SVA%"; any parameters may be null.<br>
	 * @param specCode
	 * @param attribute
	 * @param productType
	 * @param attributeGroup
	 * @return
	 */
	public List<BuildAttribute> findAllMatchIdAndGroup(String specCode,
			String attribute, String productType, String attributeGroup);

	/**
	 * update Sub Id, attribute value and description
	 * @param buildAttribute
	 */
	public void updateBuildAttribute(BuildAttribute buildAttribute, String value, String subId, String description, String userName);
	
	/**
	 * 
	 * @param specCode
	 * @param attribute
	 * @param productType
	 * @return
	 */
	public long count(String specCode, String attribute, String productType);
	
	/**
	 * 
	 * @param specCode
	 * @param attribute
	 * @param productType
	 * @param attributeGroup
	 * @return
	 */
	public long count(String specCode, String attribute, String productType, String attributeGroup);

	public List<String> findAtrributeValueAndSpecForProductSpecAndServiceLot(String serviceLotCombinations,
			String productSpecCode);
	
	public List<Object[]> findAllParentChildDetails(String productSpecCode, String attributeValue);
	
	public List<String> findAllDistinctAttributeNames();
}
