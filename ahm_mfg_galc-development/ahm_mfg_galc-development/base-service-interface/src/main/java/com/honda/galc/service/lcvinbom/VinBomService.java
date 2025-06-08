package com.honda.galc.service.lcvinbom;

import java.util.Date;
import java.util.List;

import com.honda.galc.dto.lcvinbom.DcmsDto;
import com.honda.galc.dto.lcvinbom.LetCategoryCodeDto;
import com.honda.galc.dto.lcvinbom.ModelPartLotDto;
import com.honda.galc.dto.lcvinbom.ModelPartLotFilterDto;
import com.honda.galc.dto.lcvinbom.ModelPartLotRowDto;
import com.honda.galc.dto.lcvinbom.PartsByProductDto;
import com.honda.galc.dto.lcvinbom.PmqaDto;
import com.honda.galc.dto.lcvinbom.SystemrelationshipDto;
import com.honda.galc.dto.lcvinbom.VinBomDesignChangeDto;
import com.honda.galc.dto.lcvinbom.VinBomPartSetDto;
import com.honda.galc.dto.lcvinbom.VinPartDto;
import com.honda.galc.dto.lcvinbom.VinPartFilterDto;
import com.honda.galc.entity.lcvinbom.ModelPart;
import com.honda.galc.entity.lcvinbom.ModelPartApproval;
import com.honda.galc.entity.lcvinbom.VinBomPart;
import com.honda.galc.entity.lcvinbom.VinPart;
import com.honda.galc.entity.lcvinbom.VinPartApproval;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.service.IService;

public interface VinBomService extends IService {
	void updateBeamPartData(String plantLocCode, String division);
	List<DcmsDto> retrieveDesignChange(String plantLocCode, String designChangeNumber);
	List<VinPartDto> getVinPartAndStatus();
	void approveVinPartChange(long vinPartApprovalId, String approveAssociateNumber);
	VinBomDesignChangeDto[] retrieveDesignChangeByEffectiveDate(String plantLocCode, Date effectiveDate);
	void updateCategoryInspections(LetCategoryCodeDto letCategoryCodeDto, String userId);
	void removeLetCategory(long categoryCodeId, String userId);
	List<ModelPartLotDto> getAvailableLotAssignments(boolean pendingOnly);
	List<Object[]> getLines(String plantCode);
	List<PreProductionLot> getProductionLotsByProdDateAndLine(Date productionDate, String lineNo);
	void setInterchangableInactive(long modelPartId,String userId);
	List<VinPartApproval> findAllPendingVinPartApprovals();
	boolean validateRuleSelection(String dcClass, int reflash, int interchangable, int scrapParts);
	void deleteModelPartAssignment(long modelPartId, String planCode, String startingProductionLot, String userId);
	List<ModelPartApproval> getPendingModelPartApprovals();
	void approveModelPartChange(long modelPartApprovalId, String approveAssociateNumber);
	void denyModelPartChange(long modelPartApprovalId, String approveAssociateNumber);
	void saveModelPartApproval(ModelPartApproval modelPartApproval, String userId);
	void saveVinPartApproval(VinPartApproval vinPartApproval, String userId);
	List<LetCategoryCodeDto> getInspectionsByCategoryName(String categoryName);
	PmqaDto retrievePmqaData(String productId);
	void vinPartAssignment(String productSpecCode, String productId, String productionLot, String processPointId);
	void immediateDesignChange(ModelPart modelPart);
	List<VinBomPart> findDistinctPartNumberBySystemName(String letSystemName, String productId);
	void removeVinPart(String productId, String letSystemName, String dcPartNumber,String userId);
	
	PartsByProductDto getPartsByProductForSystem(String productId, String systemName);
	
	void saveMultiModelPartApproval(List<ModelPartApproval> modelPartApprovalList, String userId);
    void saveModelLotAndModelPartApproval(List<ModelPartLotRowDto> modelPartLotDtoList, String selectedPlanCode, String selectedProdLot, String userId);
    void approveMultiModelPartChange(List<Long> modelPartApprovalIdList, String approveAssociateNumber);
	void denyMultiModelPartChange(List<Long> modelPartApprovalIdList, String approveAssociateNumber);
	
	List<ModelPartLotDto> filterLotAssignments(String dcNumber, String dcPartNumber, String systemName, String model, String active);
	List<VinPartDto> filterVinParts(String productId, String productionLot, String partNumber, String systemName );
	List<VinPartFilterDto> getVinPartFilters();
	List<ModelPartLotFilterDto> getLotAssignmentFilters();
	List<ModelPart> createVinBomRules(VinBomPartSetDto vinBomPartSet);
	List<VinPartDto> filterVinPartsByCriteria(String partNumber, String productSpecCode, String systemName);
	void saveMultiVinPartApproval(List<VinPartApproval> vinPartApprovalList, String userId);
	void saveMultiVinPart(List<VinPart> vinPartApprovalList, String userId);
	void removeMultiVinPart(String partNumber, String productSpecCode, String systemName);
	void approveMultiVinPartChange(List<Long> vinPartApprovalIdList, String approveAssociateNumber);
	public void denyMultiVinPartChanges(List<Long> vinPartApprovalIdList, String approveAssociateNumber);
	public void saveSystemRelationship(List<SystemrelationshipDto> systemRelationshipDto);
	void deleteSystemRelationship(List<SystemrelationshipDto> systemRelationshipDtoList);
	
}
