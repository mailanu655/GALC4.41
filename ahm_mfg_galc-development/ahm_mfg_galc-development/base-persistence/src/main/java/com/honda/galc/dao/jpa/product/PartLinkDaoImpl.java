package com.honda.galc.dao.jpa.product;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.PartLinkDao;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.PartLink;
import com.honda.galc.entity.product.PartLinkId;
import com.honda.galc.service.Parameters;


public class PartLinkDaoImpl extends BaseDaoImpl<PartLink, PartLinkId> implements PartLinkDao  {
		
	private String FIND_BY_PARENT_PART_AND_SPEC_CODE = "select p.*  from galadm.PART_LINK_TBX as p"
												+ "  where p.PARENT_PART_NAME = ?1  AND  p.PRODUCT_SPEC_CODE like ?2";
	
	private String FIND_BY_ID = "select p.*  from galadm.PART_LINK_TBX as p  where p.PARENT_PART_NAME = ?1  AND  p.PRODUCT_SPEC_CODE like ?2 AND p.CHILD_PART_NAME=?3";

	private String FIND_ALL_BY_PARENT_PART_NAME = "select p.* from galadm.PART_LINK_TBX as p where p.PARENT_PART_NAME=?1";	
	
	public List<PartLink> findAllByProductSpecCode(String partName, String specCode) {
		Parameters params = Parameters.with("1", partName);
		params.put("2", specCode);
		return  findAllByNativeQuery(FIND_BY_PARENT_PART_AND_SPEC_CODE,params,PartLink.class);	
	}
	
	@Override
	public List<PartLink> findAllByParentPartNameAndProductSpecCode(String parentPartName, String specCode) {
		Parameters params = Parameters.with("1", parentPartName);
		params.put("2", specCode);
		return  findAllByNativeQuery(FIND_BY_PARENT_PART_AND_SPEC_CODE,params,PartLink.class);
	}

	@Override
	public List<PartLink> findAllById(String parentPartName, String specCode, String childPartName) {
		Parameters params = Parameters.with("1", parentPartName);
		params.put("2", specCode);
		params.put("3",childPartName );
		return findAllByNativeQuery(FIND_BY_ID,params,PartLink.class);
	}

	@Override
	public List<PartLink> findAllByParentPartName(String parentPartName) {
		Parameters params = Parameters.with("1", parentPartName);		
		return findAllByNativeQuery(FIND_ALL_BY_PARENT_PART_NAME,params,PartLink.class);
	}
}