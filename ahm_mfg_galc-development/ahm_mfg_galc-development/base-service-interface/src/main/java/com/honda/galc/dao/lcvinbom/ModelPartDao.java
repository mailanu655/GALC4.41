package com.honda.galc.dao.lcvinbom;

import java.util.Date;
import java.util.List;

import com.honda.galc.dto.lcvinbom.ModelPartLotFilterDto;
import com.honda.galc.dto.lcvinbom.VinBomDesignChangeDto;
import com.honda.galc.entity.lcvinbom.ModelPart;
import com.honda.galc.service.IDaoService;
import com.honda.galc.entity.enumtype.VinBomActiveStatus;

public interface ModelPartDao extends IDaoService<ModelPart, Long> {

	public List<VinBomDesignChangeDto> getDesignChangeByPlantAndEffectiveDate(String plantCode,Date effectiveBeginDate);
	
	public List<String> getSystemNamesByPartNumber(String partNumber);
	
	public List<ModelPart> findAllActiveInterchangeble();
	  
	public ModelPart setInterchangableInactive(long modelPartId);
	
	public List<ModelPart> getInactivewithRules();
	
	public List<ModelPart> findAllBy(String productSpecWildcard, String letSystemName, String dcPartNumber);
	
	public List<ModelPart> filterRulesAlreadyAssigned(String dcNumber, String dcPartNumber, String productSpecWildcard, String letSystemName);
	
	public List<ModelPart> getApprovedInactiveByProductSpecAndSystemName(String productSpecCode, String systemName);
	
	public List<ModelPart> getActiveByProductSpecAndSystemName(String productSpecCode, String systemName);
	
	public List<ModelPart> getPendingInactivewithRules();

	public List<ModelPart> filterByCriteria(String dcNumber, String dcPartNumber, String systemName, List<String> models, List<String> active);

	public List<ModelPartLotFilterDto> findAllDistinctModelParts();

	public List<ModelPart> getInactiveByProductSpecAndSystemName(String productSpecCode, String systemName);
	
	public List<String> findAllDistinctYMTCode();
	
    public List<String> findAllDistinctModels();
    
    public List<ModelPart> filterModalPart(String systemName, String modelType, String ymtCode);

	List<ModelPart> getModelPartByYmt(String productSpecCode, String systemName);
	
	ModelPart setActiveState(long modelPartId, VinBomActiveStatus vinBomActiveStatus);
	}
