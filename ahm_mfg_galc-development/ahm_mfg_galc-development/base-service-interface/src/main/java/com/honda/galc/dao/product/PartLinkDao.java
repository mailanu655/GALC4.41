package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.PartLink;
import com.honda.galc.entity.product.PartLinkId;
import com.honda.galc.service.IDaoService;

public interface  PartLinkDao extends IDaoService<PartLink, PartLinkId> {

	List<PartLink> findAllByParentPartNameAndProductSpecCode(String parentPartName, String specCode);

	List<PartLink> findAllById(String parentPartName, String specCode, String childPartName);

	List<PartLink> findAllByParentPartName(String partName);
	
}