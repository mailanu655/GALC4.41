/**
 * 
 */
package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.dto.ExtRequiredPartSpecDto;
import com.honda.galc.entity.product.ExtRequiredPartSpec;
import com.honda.galc.entity.product.PartId;
import com.honda.galc.service.IDaoService;
/**
 * @author VF031824
 *
 */
public interface ExtRequiredPartSpecDao extends IDaoService<ExtRequiredPartSpec, PartId>{
	
	List<ExtRequiredPartSpecDto> findAllRequiredSpecs();
	
	List<ExtRequiredPartSpec> findAllActiveRequiredSpecsByProductType(String productType);
	
	List<ExtRequiredPartSpec> findAllActiveRequiredSpecsByPartName(String partName);
	
	List<ExtRequiredPartSpecDto> findAllRequiredSpecsByProductType(String productType);
	
	List<ExtRequiredPartSpecDto> findAllRequiredSpecsByProductTypeAndPartName(String productType, String partName);
	
	List<String> findDistinctActiveRequiredSpecsByProductType(String productType);
	
	List<ExtRequiredPartSpec> findAllActiveRequiredSpecsByPartNames(List<String> partNames);
	
	List<ExtRequiredPartSpecDto> findAllActiveRequiredSpecs(String partGroup);
	
	List<Object[]> findAllActiveByPartGroup(String partGroup);
	
}
