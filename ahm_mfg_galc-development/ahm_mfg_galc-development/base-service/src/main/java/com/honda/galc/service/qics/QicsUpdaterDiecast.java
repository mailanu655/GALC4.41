package com.honda.galc.service.qics;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.IdCreateDao;
import com.honda.galc.dao.product.ProductBuildResultDao;
import com.honda.galc.dto.qi.QiRepairDefectDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.QiExternalSystem;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.IdCreate;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.entity.qics.DefectResultId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.service.utils.QiHeadlessHelper;

/**
 * 
 * <h3>QicsUpdaterDiecast</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QicsUpdaterDiecast description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>May 11, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since May 11, 2011
 * @param <T>
 */

public abstract class QicsUpdaterDiecast<T extends DieCast> extends QicsUpdaterBase<T> {
	
	@Override
	protected List<DefectResult> createDefect(String processPointId, ProductBuildResult result) {
		List<DefectResult> list = new ArrayList<DefectResult>();
		DefectResult defect = new DefectResult();
		DefectResultId id = createDefectResultId(processPointId, result);
		id.setInspectionPartLocationName(getInspectionPartLocationName(result, ""));
		id.setDefectTypeName(getDefectTypeName(result, StringUtils.isEmpty(result.getResultValue()) ? 
				result.getInstalledPartStatus().toString() : result.getResultValue()));
		defect.setId(id);
		QiHeadlessHelper qiHelper = new QiHeadlessHelper(property);
		
		if(qiHelper.isSendToNAQics())  {
			IdCreate newId = ServiceFactory.getDao(IdCreateDao.class).incrementIdWithNewTransaction("BuildResult", "DEFECT_REF_ID");
			long defectRefId = newId.getCurrentId();
			result.setDefectRefId(defectRefId);
			defect.setDefectRefId(defectRefId);
			String errorCode = String.valueOf(InstalledPartStatus.NG.getId());
			defect.setErrorCode(errorCode);
		}
		defect.setDefectStatus(getDefectStatus(result));
		setDefectProperties(defect, result);
		defect.setQicsDefect(result.isQicsDefect());
		setPointXy(result, defect);
		
		setImageName(defect);
		
		list.add(defect);
		return list;
	}

	protected List<QiRepairDefectDto> buildRepairList (List<? extends ProductBuildResult> buildResults) {
		if(buildResults == null || buildResults.isEmpty())  return null;
		//get Installed parts that already have a defect, i.e., DEFECT_REF_ID is > 0
		ProductTypeUtil typeUtil = ProductTypeUtil.getTypeUtil(getProperty().getProductType());
		List<? extends ProductBuildResult> existing = null;
		ProductBuildResultDao<? extends ProductBuildResult,?> dao = ServiceFactory.getDao(typeUtil.getProductBuildResultDaoClass());
		existing = dao.findAllPartsWithDefect(productId);
		if(existing == null || existing.isEmpty())  return null;
		List<QiRepairDefectDto> repairList = new ArrayList<QiRepairDefectDto>();
		for(ProductBuildResult result : buildResults){
			if(result.getInstalledPartStatus() == InstalledPartStatus.OK){
				List<QiRepairDefectDto> repairListForBuildResult = getRepairDtoList(existing, result);
				if(repairListForBuildResult != null && !repairListForBuildResult.isEmpty())  {
					repairList.addAll(repairListForBuildResult);
				}
			}
		}

		if(isAllRepairedParts(existing)){
			product.setDefectStatusValue(DefectStatus.REPAIRED.getId());
			getLogger().info("product:", getProductId(), " is repaired.");
		}
		
		return repairList;
			
	}
	
	private boolean isAllRepairedParts(List<? extends ProductBuildResult> installedParts) {
		if(installedParts != null && installedParts.size() > 0){
			int count = 0;
			for(ProductBuildResult result : installedParts){
				if(result.getDefectStatus() != null && (result.getDefectStatus() == DefectStatus.REPAIRED.getId()))  {
					count++;
				}
			}
			
			return installedParts.size() == count;
		} 
		return false;
	}
	
	private List<QiRepairDefectDto> getRepairDtoList(List<? extends ProductBuildResult> existing, ProductBuildResult buildResult) {
		List<QiRepairDefectDto> dtoList = new ArrayList<QiRepairDefectDto>();
		for(ProductBuildResult iPart : existing){
			if(buildResult.getPartName().equals(iPart.getPartName()))
			{	
				QiRepairDefectDto qiRepair = new QiRepairDefectDto();
				qiRepair.setCurrentDefectStatus((short)DefectStatus.FIXED.getId());
				String associateId = "";
				if(!buildResult.getAssociateNo().equals(""))  {
					associateId = buildResult.getAssociateNo();
				} else if (! Boolean.parseBoolean(PropertyService.getProperty(this.processPointId, "INSTALLED_PART_ASSOCIATE_USING_USER_ID", 
						PropertyService.getProperty("Default_LotControl", "INSTALLED_PART_ASSOCIATE_USING_USER_ID", "True"))))  {
					associateId = this.processPointId;
				}
				qiRepair.setAssociateId(associateId);
				String updateReason = property.getRepairReason();
				qiRepair.setRepairReason(updateReason);
				
				iPart.setDefectStatus(DefectStatus.REPAIRED.getId());
				iPart.setAssociateNo(associateId);
				
				qiRepair.setExternalSystemKey(iPart.getDefectRefId());
				qiRepair.setExternalSystemName(QiExternalSystem.LOT_CONTROL.name());
				qiRepair.setFixDuplicates(property.isFixDuplicates());
				dtoList.add(qiRepair);
				
				getLogger().info("Qics repair product:", iPart.getProductId(), " partName:", 
						buildResult.getPartName(), "Defect ref id:" + iPart.getDefectRefId()," repaired.");
				
			}
		}
		return dtoList;
	}
	
}
