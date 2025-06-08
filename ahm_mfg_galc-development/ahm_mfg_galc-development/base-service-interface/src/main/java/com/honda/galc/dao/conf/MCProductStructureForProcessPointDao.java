/**
 * 
 */
package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.dao.product.BaseProductStructureDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.StructureUnitDetailsDto;
import com.honda.galc.entity.conf.MCProductStructureForProcessPoint;
import com.honda.galc.entity.conf.MCProductStructureForProcessPointId;

/**
 * @author Fredrick Yessaian
 * @Date Apr 01 2016
 */
public interface MCProductStructureForProcessPointDao extends 
	BaseProductStructureDao<MCProductStructureForProcessPoint, MCProductStructureForProcessPointId> {
	
	public int getUnmappedProductCount(ProductType productType);
	
	public List<MCProductStructureForProcessPoint> findAllBy(String productId, String productSpecCode, String processPoint);

	public MCProductStructureForProcessPoint findByKey(MCProductStructureForProcessPointId mcProductStructureForProcessPointId);
	
	public MCProductStructureForProcessPoint findByKey(String productId, String processPointId, String productSpecCode);
	
	public List<MCProductStructureForProcessPoint> findAllByProductIdDivisionAndSpecCode(List<String> products, String divisionId, String specCode, String processPoint);
	
	public List<StructureUnitDetailsDto> findAllByProductIdDivisionAndProcessPoint(String productId, String divisionId, String processPoint);
	
}
