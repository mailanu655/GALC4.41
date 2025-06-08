/**
 * 
 */
package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.dto.RequiredLetPartSpecDetailsDto;
import com.honda.galc.entity.product.LetPartCheckSpec;
import com.honda.galc.entity.product.LetPartCheckSpecId;
import com.honda.galc.service.IDaoService;

/**
 * @author VF031824
 *
 */
public interface LetPartCheckSpecDao extends IDaoService<LetPartCheckSpec, LetPartCheckSpecId> {

	public List<LetPartCheckSpec> findAllActiveSpecsByProductType(String productType);
	
	public List<String> findAllActivePartNamesByProductType(String productType);
	
	public List<LetPartCheckSpec> findAllActiveByPartNameSpecAndParamType(String partName, String partSpec, int paramType);
	
	public List<RequiredLetPartSpecDetailsDto> findAllActiveByProductSpecCode(String productSpecCode, String ProductId, String productType, int paramType);
	
}
