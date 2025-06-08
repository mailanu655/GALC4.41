package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.RequiredPart;
import com.honda.galc.entity.product.RequiredPartId;
import com.honda.galc.service.IDaoService;

public interface RequiredPartDao extends IDaoService<RequiredPart, RequiredPartId>{
	
	/**
	 * Find missing required part for the process point
	 * 
	 * @param specCode
	 * @param processPointId
	 * @param type
	 * @param productId
	 * @param productSubId
	 * @return
	 */
	public List<String> findMissingRequiredParts(String specCode,
			String processPointId, ProductType type, String productId, String productSubId);

	public List<RequiredPart> findAllById(RequiredPartId id, String productType);

	public List<RequiredPart> findAllByProcessPoint(String processPointId);
	
	public List<RequiredPart> findAllByIdAndProductType(RequiredPartId id, String division, String productType);
	
	public List<RequiredPart> findAllByIdAndDefaultProductType(RequiredPartId id, String division);
	
	public List<RequiredPart> findAllByProcessPointAndProdSpec(String processPoint,BaseProductSpec specCode);
	
	public List<RequiredPart> findAllByProductSpecCode(String productSpecCode);
}
