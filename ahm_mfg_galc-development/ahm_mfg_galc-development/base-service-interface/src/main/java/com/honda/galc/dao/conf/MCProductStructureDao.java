/**
 * 
 */
package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.dao.product.BaseProductStructureDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.StructureUnitDetailsDto;
import com.honda.galc.entity.conf.MCProductStructure;
import com.honda.galc.entity.conf.MCProductStructureId;

/**
 * @author Subu Kathiresan
 * @date Feb 18, 2014
 */
public interface MCProductStructureDao 
	extends BaseProductStructureDao<MCProductStructure, MCProductStructureId>{
	
	public List<String> getAllUnmappedProducts(ProductType productType);
	
	public List<MCProductStructure> findAllProductForProdSpecCodeAndRevision(String prodSpecCode, long structureRevision);
	
	public List<String> getUnmappedMBPNProductSpecCodes();
	
	public List<String> getUnmappedEngineProductSpecCodes();
	
	public List<String> getUnmappedFrameProductSpecCodes();
	
	public List<MCProductStructure> findAllBy(String productId, String productSpecCode);
	
	public MCProductStructure findByKey(String productId, String divisionId, String productSpecCode);
	
	public List<StructureUnitDetailsDto> findAllByProductAndDivision(String productId, String divisionId, long structureRev);
	
	public List<MCProductStructure> findProductByStructureRevAndDivId(long structureRev, String divisionId);
	
	public List<MCProductStructure> findAllByProductIdDivisionAndSpecCode(List<String> products, String divisionId, String specCode);
	
	public List<MCProductStructure> findAllByProductIdAndDivisionId(String productId, String divisionId);
	
}
