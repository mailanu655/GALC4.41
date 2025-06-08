package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.BuildAttributeByBom;
import com.honda.galc.entity.product.BuildAttributeByBomId;
import com.honda.galc.service.IDaoService;

public interface BuildAttributeByBomDao extends IDaoService<BuildAttributeByBom, BuildAttributeByBomId> {

	public List<BuildAttributeByBom> findAllByAttribute(String attribute);

	public long getCountOfAttribute(String attribute);

	public BuildAttributeByBom findfirstByAttributeAndValue(String attribute, String value);

	public List<BuildAttributeByBom> findByFilter(String attributeGroup, String system, String modelGroup, String attribute, String partNoPrefix);

	public List<String> findAllAttributeForAttributeGroupAndModelGroup(String attributeGroup, String modelGroup);
	
	public List<Object[]> findAllAttributeForPartNoAndModelYear(String modelYearCode,String partNo,String system);

}
