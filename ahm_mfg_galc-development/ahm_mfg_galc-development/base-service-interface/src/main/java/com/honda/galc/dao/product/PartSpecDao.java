package com.honda.galc.dao.product;


import java.util.List;

import com.honda.galc.dto.ExtRequiredPartDto;
import com.honda.galc.dto.LetRequiredPartSpecsDto;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.PartSpecId;
import com.honda.galc.service.IDaoService;

public interface PartSpecDao extends IDaoService<PartSpec, PartSpecId> {
	
	public List<PartSpec> findAllByPartName(String partName);
	
	public List<PartSpec> findPartByLotCtrRule(LotControlRule rule);
	
	public List<PartSpec> findProdSpecs(String prodId, String processPointId);
	
	public List<PartSpec> findAllPartsWithPartNumberAndPartMark();
	
	public List<PartSpec> findAllWithPartNumberAndAssignedToProcessPoint();
	
	public PartSpec findValueWithPartNameAndPartID(String partName, String partID);
	
	public long findNumberOfPartsByImageId(int imageId);
	
	public List<PartSpec> findAllByProcessPoint(String processPointId);
	
	public List<ExtRequiredPartDto> findAllWithExtRequired();
	
	public List<LetRequiredPartSpecsDto> findAllWithLETRequired();
	
	public List<LetRequiredPartSpecsDto> findByPartNameWithLETRequired(String partName);
	
	public List<LetRequiredPartSpecsDto> findByPartNameWithLETRequiredByPartSpec(String partName, String partSpec);
	
	/**
	 * Retrieve all part names of the same part number
	 * @param partNumber
	 * @return
	 */
	public List<String> findAllPartNamesByPartNumberBase5(String partNumberBase5);
	

}
