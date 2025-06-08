
package com.honda.galc.dto;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.qi.QiDefectResult;

/**
 * 
 * <h3>DefectMapDto Class description</h3>
 * <p>
 * DefectMapDto
 * </p>
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
 * 
 * </TABLE>
 * 
 * @author LnTInfotech<br>
 * Feb 13, 2017
 * 
 */

public class DefectMapDto implements IDto {

private static final long serialVersionUID = 1L;
	
	@DtoTag(name = "SITE_NAME")
	private String siteName;
	
	@DtoTag(name = "PLANT")
	private String plantDescription;
	
	@DtoTag(name = "PRODUCT_TYPE")
	private String productType;
	
	@DtoTag(outputName = "LOCAL_DEFECT_COMBINATION_ID")
	private Integer localDefectCombinationId;
	
	@DtoTag(outputName = "ENTRY_SITE")
	private String entrySite;
	
	@DtoTag(outputName = "ENTRY_DEPARTMENT")
	private String entryDepartment;
	
	@DtoTag(outputName = "ENTRY_PROD_LINE_NO")
	private String entryArea;
	
	@DtoTag(outputName = "CREATE_USER")
	private String associateId;
	
	@DtoTag(outputName = "EXTERNAL_SYSTEM_NAME")
	private String externalSystemName;
	
	@DtoTag(outputName = "EXTERNAL_SYSTEM_KEY")
	private Long externalSystemKey = 0L;
	
	@DtoTag(outputName = "EXTERNAL_PART_CODE")
	private String externalPartCode;
	
	@DtoTag(outputName = "EXTERNAL_DEFECT_CODE")
	private String externalDefectCode;
	
	@DtoTag(outputName = "PRODUCT_ID")
	private String productId;
	
	@DtoTag(outputName = "APPLICATION_ID")
	private String processPointId;
	
	@DtoTag(outputName = "ENTRY_SCREEN")
	private String entryScreen;
	
	@DtoTag(outputName = "ORIGINAL_DEFECT_STATUS")
	private String originalDefectStatus;
	
	@DtoTag(outputName = "CURRENT_DEFECT_STATUS")
	private String currentDefectStatus;
	
	@DtoTag(outputName = "WRITE_UP_DEPARTMENT")
	private String writeupDepartment;
	
	@DtoTag(outputName = "IMAGE_NAME")
	private String imageName;
	
	@DtoTag(outputName = "POINT_X")
	private String xAxis;
	
	@DtoTag(outputName = "POINT_Y")
	private String yAxis;
	
	@DtoTag(outputName = "FULL_PART_DESC")
	private String fullPartDesc;
	
	@DtoTag(outputName = "TEXT_ENTRY_MENU")
	private String textEntryMenu;
	
	@DtoTag(outputName = "ENTRY_MODEL")
	private String entryModel;
	
	@DtoTag(outputName = "EXTERNAL_SYSTEM_DEFECT_MAP_ID")
	private Integer externalSystemMapId;
	
	@DtoTag(outputName = "INSPECTION_PART_NAME")
	private String inspectionPartName; 

	@DtoTag(outputName = "INSPECTION_PART_LOCATION_NAME")
	private String inspectionPartLocationName;

	@DtoTag(outputName = "INSPECTION_PART_LOCATION2_NAME")
	private String inspectionPartLocation2Name; 

	@DtoTag(outputName = "INSPECTION_PART2_NAME")
	private String inspectionPart2Name; 

	@DtoTag(outputName = "INSPECTION_PART2_LOCATION_NAME")
	private String inspectionPart2LocationName; 

	@DtoTag(outputName = "INSPECTION_PART2_LOCATION2_NAME")
	private String inspectionPart2Location2Name; 

	@DtoTag(outputName = "INSPECTION_PART3_NAME")
	private String inspectionPart3Name;

	@DtoTag(outputName = "IQS_VERSION")
	private String iqsVersion;

	@DtoTag(outputName = "IQS_CATEGORY")
	private String iqsCategory;

	@DtoTag(outputName = "DEFECT_TYPE_NAME")
	private String defectTypeName; 

	@DtoTag(outputName = "DEFECT_TYPE_NAME2")
	private String defectTypeName2;

	@DtoTag(outputName = "THEME_NAME")
	private String themeName;

	@DtoTag(outputName = "REPORTABLE")
	private short reportable;

	@DtoTag(outputName = "RESPONSIBLE_SITE")
	private String responsibleSite; 

	@DtoTag(outputName = "RESPONSIBLE_PLANT")
	private String responsiblePlant; 

	@DtoTag(outputName = "RESPONSIBLE_DEPT")
	private String responsibleDept; 
	
	@DtoTag(outputName = "RESPONSIBLE_LEVEL_NAME")
	private String responsibleLevelName;
		
	@DtoTag(outputName = "LEVEL")
	private String level;
	
	@DtoTag(outputName = "LEVEL_TWO")
	private String levelTwo;
	
	@DtoTag(outputName = "LEVEL_THREE")
	private String levelThree;
	
	@DtoTag(outputName = "PDDA_RESPONSIBILITY_ID")
	private String pddaResponsibilityId; 

	@DtoTag(outputName = "ENTRY_SITE_NAME")
	private String entrySiteName;

	@DtoTag(outputName = "ENTRY_PLANT_NAME")
	private String entryPlantName; 
	
	@DtoTag(outputName = "PRODUCT_SPEC_CODE")
	private String productSpecCode; 
	
	@DtoTag(outputName = "KD_LOT_NUMBER")
	private String kdLotNumber;
	
	@DtoTag(outputName = "PRODUCTION_LOT")
	private String productionLot;

	@DtoTag(outputName = "DEFECT_CATEGORY_NAME")
	private String defectCategoryName;

	@DtoTag(outputName = "REPAIR_AREA_NAME")
	private String repairAreaName; 

	@DtoTag(outputName = "REPAIR_METHOD")
	private String repairMethod; 

	@DtoTag(outputName = "LOCAL_THEME")
	private String localTheme;
	
	@DtoTag(outputName = "IQS_QUESTION_NO")
	private int iqsQuestionNo;

	@DtoTag(outputName = "IQS_QUESTION")
	private String iqsQuestion;
	
	@DtoTag(outputName = "TERMINAL_NAME")
	private String terminalName;
	
	@DtoTag(outputName = "PROCESS_NUMBER")
	private String processNumber;
	
	@DtoTag(outputName = "PROCESS_NAME")
	private String processName;
	
	@DtoTag(outputName = "UNIT_NUMBER")
	private String unitNumber;
	
	@DtoTag(outputName = "UNIT_DESC")
	private String unitDesc;
	
	@DtoTag(outputName = "REPAIR_METHOD_TIME")
	private int repairMethodTime;

	@DtoTag(outputName = "ENGINE_FIRING_FLAG")
	private short engineFiringFlag;
	
	@DtoTag(outputName = "IS_QICS_REPAIR_REQD")
	private short isQicsRepairReqd;
	
	@DtoTag(outputName = "IS_EXT_SYS_REPAIR_REQD")
	private short isExtSysRepairReqd;
	
	@DtoTag(outputName = "OLD_IQS_CATEGORY_NAME")
	private String oldIqsCategoryName;
	
	@DtoTag(outputName = "OLD_IQS_ITEM_NAME")
	private String oldIqsItemName;
	
	@DtoTag(outputName = "IQS_ID")
	private int iqsId;
	
	@DtoTag(outputName = "REGIONAL_DEFECT_COMBINATION_ID") 
	private Integer regionalDefectCombinationId; 
	
	private boolean isLotControl;
	
	private boolean isReprocess;
	
	private String deviceId;
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@DtoTag(name = "UPDATE_TIMESTAMP", type = DtoType.IN)
	private Date updateTimestamp;
	
	public boolean isReprocess() {
		return isReprocess;
	}

	public void setReprocess(boolean isReprocess) {
		this.isReprocess = isReprocess;
	}


	private QiDefectResult qiDefectResult;
	
	public String getQicsRepairReqdStr() {
		return (this.isQicsRepairReqd == 0) ? "No" :"Yes";
	}
	
	public short getEngineFiringFlag() {
		return engineFiringFlag;
	}

	public void setEngineFiringFlag(short engineFiringFlag) {
		this.engineFiringFlag = engineFiringFlag;
	}
	
	/**
	 * @return the iqsQuestionNo
	 */
	public int getIqsQuestionNo() {
		return iqsQuestionNo;
	}

	/**
	 * @param iqsQuestionNo the iqsQuestionNo to set
	 */
	public void setIqsQuestionNo(int iqsQuestionNo) {
		this.iqsQuestionNo = iqsQuestionNo;
	}

	/**
	 * @return the iqsQuestion
	 */
	public String getIqsQuestion() {
		return  StringUtils.trimToEmpty(iqsQuestion);
	}

	/**
	 * @param iqsQuestion the iqsQuestion to set
	 */
	public void setIqsQuestion(String iqsQuestion) {
		this.iqsQuestion = iqsQuestion;
	}

	public String getSiteName() {
		return StringUtils.trimToEmpty(siteName);
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getPlantDescription() {
		return StringUtils.trimToEmpty(plantDescription);
	}

	public void setPlantDescription(String plantDescription) {
		this.plantDescription = plantDescription;
	}

	public String getProductType() {
		return StringUtils.trimToEmpty(productType);
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Integer getLocalDefectCombinationId() {
		return localDefectCombinationId;
	}

	public void setLocalDefectCombinationId(Integer localDefectCombinationId) {
		this.localDefectCombinationId = localDefectCombinationId;
	}

	public String getEntrySite() {
		return StringUtils.trimToEmpty(entrySite);
	}

	public void setEntrySite(String entrySite) {
		this.entrySite = entrySite;
	}

	public String getEntryDepartment() {
		return StringUtils.trimToEmpty(entryDepartment);
	}

	public void setEntryDepartment(String entryDepartment) {
		this.entryDepartment = entryDepartment;
	}

	public String getEntryArea() {
		return StringUtils.trimToEmpty(entryArea);
	}

	public void setEntryArea(String entryArea) {
		this.entryArea = entryArea;
	}

	public String getAssociateId() {
		return StringUtils.trimToEmpty(associateId);
	}

	public void setAssociateId(String associateId) {
		this.associateId = associateId;
	}

	public String getExternalSystemName() {
		return StringUtils.trimToEmpty(externalSystemName);
	}

	public void setExternalSystemName(String externalSystemName) {
		this.externalSystemName = externalSystemName;
	}

	public Long getExternalSystemKey() {
		return externalSystemKey;
	}

	public void setExternalSystemKey(Long externalSystemKey) {
		this.externalSystemKey = externalSystemKey;
	}

	public String getExternalPartCode() {
		return StringUtils.trimToEmpty(externalPartCode);
	}

	public void setExternalPartCode(String externalPartCode) {
		this.externalPartCode = externalPartCode;
	}

	public String getExternalDefectCode() {
		return StringUtils.trimToEmpty(externalDefectCode);
	}

	public void setExternalDefectCode(String externalDefectCode) {
		this.externalDefectCode = externalDefectCode;
	}

	public String getProductId() {
		return StringUtils.trimToEmpty(productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProcessPointId() {
		return StringUtils.trimToEmpty(processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getEntryScreen() {
		return StringUtils.trimToEmpty(entryScreen);
	}

	public void setEntryScreen(String entryScreen) {
		this.entryScreen = entryScreen;
	}

	public String getOriginalDefectStatus() {
		return StringUtils.trimToEmpty(originalDefectStatus);
	}

	public void setOriginalDefectStatus(String originalDefectStatus) {
		this.originalDefectStatus = originalDefectStatus;
	}

	public String getCurrentDefectStatus() {
		return StringUtils.trimToEmpty(currentDefectStatus);
	}

	public void setCurrentDefectStatus(String currentDefectStatus) {
		this.currentDefectStatus = currentDefectStatus;
	}

	public String getWriteupDepartment() {
		return StringUtils.trimToEmpty(writeupDepartment);
	}

	public void setWriteupDepartment(String writeupDepartment) {
		this.writeupDepartment = writeupDepartment;
	}

	public String getImageName() {
		return StringUtils.trimToEmpty(imageName);
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getxAxis() {
		return StringUtils.trimToEmpty(xAxis);
	}

	public void setxAxis(String xAxis) {
		this.xAxis = xAxis;
	}

	public String getyAxis() {
		return StringUtils.trimToEmpty( yAxis);
	}

	public void setyAxis(String yAxis) {
		this.yAxis = yAxis;
	}

	public String getFullPartDesc() {
		return StringUtils.trimToEmpty(fullPartDesc);
	}

	public void setFullPartDesc(String fullPartDesc) {
		this.fullPartDesc = fullPartDesc;
	}

	public String getTextEntryMenu() {
		return StringUtils.trimToEmpty(textEntryMenu);
	}

	public void setTextEntryMenu(String textEntryMenu) {
		this.textEntryMenu = textEntryMenu;
	}

	public String getEntryModel() {
		return StringUtils.trimToEmpty(entryModel);
	}

	public void setEntryModel(String entryModel) {
		this.entryModel = entryModel;
	}

	public Integer getExternalSystemMapId() {
		return externalSystemMapId;
	}

	public void setExternalSystemMapId(Integer externalSystemMapId) {
		this.externalSystemMapId = externalSystemMapId;
	}

	public String getInspection_part_name() {
		return StringUtils.trimToEmpty(inspectionPartName);
	}

	public void setInspectionPartName(String inspectionPartName) {
		this.inspectionPartName = inspectionPartName;
	}

	public String getInspectionPartLocationName() {
		return StringUtils.trimToEmpty(inspectionPartLocationName);
	}

	public void setInspectionPartLocationName(String inspectionPartLocationName) {
		this.inspectionPartLocationName = inspectionPartLocationName;
	}

	public String getInspectionPartLocation2Name() {
		return StringUtils.trimToEmpty(inspectionPartLocation2Name);
	}

	public void setInspectionPartLocation2Name(String inspectionPartLocation2Name) {
		this.inspectionPartLocation2Name = inspectionPartLocation2Name;
	}

	public String getInspectionPart2Name() {
		return StringUtils.trimToEmpty(inspectionPart2Name);
	}

	public void setInspectionPart2Name(String inspectionpart2name) {
		this.inspectionPart2Name = inspectionpart2name;
	}

	public String getInspectionPart2LocationName() {
		return StringUtils.trimToEmpty(inspectionPart2LocationName);
	}

	public void setInspectionPart2LocationName(String inspectionPart2LocationName) {
		this.inspectionPart2LocationName = inspectionPart2LocationName;
	}

	public String getInspectionPart2Location2Name() {
		return StringUtils.trimToEmpty(inspectionPart2Location2Name);
	}

	public void setInspectionPart2Location2Name(String inspectionPart2Location2Name) {
		this.inspectionPart2Location2Name = inspectionPart2Location2Name;
	}

	public String getInspectionPart3Name() {
		return StringUtils.trimToEmpty(inspectionPart3Name);
	}

	public void setInspectionPart3Name(String inspectionPart3Name) {
		this.inspectionPart3Name = inspectionPart3Name;
	}

	public String getIqsVersion() {
		return StringUtils.trimToEmpty(iqsVersion);
	}

	public void setIqsVersion(String iqsVersion) {
		this.iqsVersion = iqsVersion;
	}

	public String getIqsCategory() {
		return StringUtils.trimToEmpty(iqsCategory);
	}

	public void setIqsCategory(String iqsCategory) {
		this.iqsCategory = iqsCategory;
	}

	public String getDefectTypeName() {
		return StringUtils.trimToEmpty(defectTypeName);
	}

	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}

	public String getDefectTypeName2() {
		return StringUtils.trimToEmpty(defectTypeName2);
	}

	public void setDefectTypeName2(String defectTypeName2) {
		this.defectTypeName2 = defectTypeName2;
	}

	public String getThemeName() {
		return StringUtils.trimToEmpty(themeName);
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public short getReportable() {
		return reportable;
	}

	public void setReportable(short reportable) {
		this.reportable = reportable;
	}

	public String getResponsibleSite() {
		return StringUtils.trimToEmpty(responsibleSite);
	}

	public void setResponsibleSite(String responsibleSite) {
		this.responsibleSite = responsibleSite;
	}

	public String getResponsiblePlant() {
		return StringUtils.trimToEmpty(responsiblePlant);
	}

	public void setResponsiblePlant(String responsiblePlant) {
		this.responsiblePlant = responsiblePlant;
	}

	public String getResponsibleDept() {
		return StringUtils.trimToEmpty(responsibleDept);
	}

	public void setResponsibleDept(String responsibleDept) {
		this.responsibleDept = responsibleDept;
	}

	public String getResponsibleLevelName() {
		return StringUtils.trimToEmpty(responsibleLevelName);
	}

	public void setResponsibleLevelName(String responsibleLevelName) {
		this.responsibleLevelName = responsibleLevelName;
	}

	public String getLevel() {
		return StringUtils.trimToEmpty(level);
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLevelTwo() {
		return StringUtils.trimToEmpty(levelTwo);
	}

	public void setLevelTwo(String levelTwo) {
		this.levelTwo = levelTwo;
	}

	public String getLevelThree() {
		return StringUtils.trimToEmpty(levelThree);
	}

	public void setLevelThree(String levelThree) {
		this.levelThree = levelThree;
	}

	public String getPddaResponsibilityId() {
		return StringUtils.trimToEmpty( pddaResponsibilityId);
	}

	public void setPddaResponsibilityId(String pddaResponsibilityId) {
		this.pddaResponsibilityId = pddaResponsibilityId;
	}

	public String getEntrySiteName() {
		return StringUtils.trimToEmpty(entrySiteName);
	}

	public void setEntrySiteName(String entrySiteName) {
		this.entrySiteName = entrySiteName;
	}

	public String getEntryPlantName() {
		return StringUtils.trimToEmpty(entryPlantName);
	}

	public void setEntryPlantName(String entryPlantName) {
		this.entryPlantName = entryPlantName;
	}

	public String getProductSpecCode() {
		return StringUtils.trimToEmpty(productSpecCode);
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getKdLotNumber() {
		return StringUtils.trimToEmpty(kdLotNumber);
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	public String getProductionLot() {
		return StringUtils.trimToEmpty(productionLot);
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	public String getDefectCategoryName() {
		return StringUtils.trimToEmpty(defectCategoryName);
	}

	public void setDefectCategoryName(String defectCategoryName) {
		this.defectCategoryName = defectCategoryName;
	}

	public String getRepairAreaName() {
		return StringUtils.trimToEmpty(repairAreaName);
	}

	public void setRepairAreaName(String repairAreaName) {
		this.repairAreaName = repairAreaName;
	}

	public String getRepairMethod() {
		return StringUtils.trimToEmpty(repairMethod);
	}

	public void setRepairMethod(String repairMethod) {
		this.repairMethod = repairMethod;
	}

	public String getLocalTheme() {
		return StringUtils.trimToEmpty(localTheme);
	}

	public void setLocalTheme(String localTheme) {
		this.localTheme = localTheme;
	}

	public String getTerminalName() {
		return StringUtils.trimToEmpty(terminalName);
	}

	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	public String getInspectionPartName() {
		return StringUtils.trimToEmpty(inspectionPartName);
	}

	public String getProcessNumber() {
		return StringUtils.trimToEmpty(processNumber);
	}

	public void setProcessNumber(String processNumber) {
		this.processNumber = processNumber;
	}

	public String getUnitNumber() {
		return StringUtils.trimToEmpty(unitNumber);
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public String getUnitDesc() {
		return StringUtils.trimToEmpty(unitDesc);
	}

	public void setUnitDesc(String unitDesc) {
		this.unitDesc = unitDesc;
	}

	public int getRepairMethodTime() {
		return repairMethodTime;
	}

	public void setRepairMethodTime(int repairMethodTime) {
		this.repairMethodTime = repairMethodTime;
	}

	public String getProcessName() {
		return StringUtils.trimToEmpty(processName);
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getOldIqsCategoryName() {
		return StringUtils.trimToEmpty(oldIqsCategoryName);
	}

	public void setOldIqsCategoryName(String oldIqsCategoryName) {
		this.oldIqsCategoryName = oldIqsCategoryName;
	}

	public String getOldIqsItemName() {
		return StringUtils.trimToEmpty(oldIqsItemName);
	}

	public void setOldIqsItemName(String oldIqsItemName) {
		this.oldIqsItemName = oldIqsItemName;
	}

	public int getIqsId() {
		return iqsId;
	}

	public void setIqsId(int iqsId) {
		this.iqsId = iqsId;
	}
	
	public Integer getRegionalDefectCombinationId() {
		return regionalDefectCombinationId;
	}

	public void setRegionalDefectCombinationId(Integer regionalDefectCombinationId) {
		this.regionalDefectCombinationId = regionalDefectCombinationId;
	}

	public boolean isLotControl() {
		return isLotControl;
	}

	public void setLotControl(boolean isLotControl) {
		this.isLotControl = isLotControl;
	}
	public QiDefectResult getQiDefectResult() {
		return qiDefectResult;
	}

	public void setQiDefectResult(QiDefectResult qiDefectResult) {
		this.qiDefectResult = qiDefectResult;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	@Override
	public String toString() {
		return "QiExternalSystemDefectMapDto [siteName=" + siteName + ", plantDescription=" + plantDescription
				+ ", productType=" + productType + ", localDefectCombinationId=" + localDefectCombinationId
				+ ", entrySite=" + entrySite + ", entryDepartment=" + entryDepartment + ", entryArea=" + entryArea
				+ ", associateId=" + associateId + ", externalSystemName=" + externalSystemName + ", externalPartCode="
				+ externalPartCode + ", externalDefectCode=" + externalDefectCode + ", productId=" + productId
				+ ", processPointId=" + processPointId + ", entryScreen=" + entryScreen + ", repairStatus="
				+ originalDefectStatus + ", fixedStatus=" + currentDefectStatus + ", writeupDepartment=" + writeupDepartment
				+ ", imageName=" + imageName + ", xAxis=" + xAxis + ", yAxis=" + yAxis + ", fullPartDesc="
				+ fullPartDesc + ", textEntryMenu=" + textEntryMenu + ", entryModel=" + entryModel
				+ ", externalSystemMapId=" + externalSystemMapId + ", inspectionPartName=" + inspectionPartName
				+ ", inspectionPartLocationName=" + inspectionPartLocationName + ", inspectionPartLocation2Name="
				+ inspectionPartLocation2Name + ", inspectionPart2Name=" + inspectionPart2Name
				+ ", inspectionPart2LocationName=" + inspectionPart2LocationName + ", inspectionPart2Location2Name="
				+ inspectionPart2Location2Name + ", inspectionPart3Name=" + inspectionPart3Name + ", iqsVersion="
				+ iqsVersion + ", iqsCategory=" + iqsCategory + ", defectTypeName=" + defectTypeName
				+ ", defectTypeName2=" + defectTypeName2 + ", themeName=" + themeName + ", reportable=" + reportable + ", responsibleSite="
				+ responsibleSite + ", responsiblePlant=" + responsiblePlant + ", responsibleDept=" + responsibleDept
				+ ", responsibleLevelName=" + responsibleLevelName + ", level=" + level + ", levelTwo=" + levelTwo
				+ ", levelThree=" + levelThree + ", pddaResponsibilityId=" + pddaResponsibilityId + ", entrySiteName="
				+ entrySiteName + ", entryPlantName=" + entryPlantName + ", productSpecCode=" + productSpecCode
				+ ", kdLotNumber=" + kdLotNumber + ", productionLot=" + productionLot + ", defectCategoryName="
				+ defectCategoryName + ", repairAreaName=" + repairAreaName + ", repairMethod=" + repairMethod
				+ ", temporaryTracking=" + localTheme + ", iqsQuestionNo=" + iqsQuestionNo + ", iqsQuestion="
				+ iqsQuestion + ", terminalName=" + terminalName + ", regionalDefectCombinationId=" + regionalDefectCombinationId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((associateId == null) ? 0 : associateId.hashCode());
		result = prime * result + ((defectCategoryName == null) ? 0 : defectCategoryName.hashCode());
		result = prime * result + ((defectTypeName == null) ? 0 : defectTypeName.hashCode());
		result = prime * result + ((defectTypeName2 == null) ? 0 : defectTypeName2.hashCode());
		result = prime * result + ((entryArea == null) ? 0 : entryArea.hashCode());
		result = prime * result + ((entryDepartment == null) ? 0 : entryDepartment.hashCode());
		result = prime * result + ((entryModel == null) ? 0 : entryModel.hashCode());
		result = prime * result + ((entryPlantName == null) ? 0 : entryPlantName.hashCode());
		result = prime * result + ((entryScreen == null) ? 0 : entryScreen.hashCode());
		result = prime * result + ((entrySite == null) ? 0 : entrySite.hashCode());
		result = prime * result + ((entrySiteName == null) ? 0 : entrySiteName.hashCode());
		result = prime * result + ((externalDefectCode == null) ? 0 : externalDefectCode.hashCode());
		result = prime * result + ((externalPartCode == null) ? 0 : externalPartCode.hashCode());
		result = prime * result + ((externalSystemMapId == null) ? 0 : externalSystemMapId.hashCode());
		result = prime * result + ((externalSystemName == null) ? 0 : externalSystemName.hashCode());
		result = prime * result + ((currentDefectStatus == null) ? 0 : currentDefectStatus.hashCode());
		result = prime * result + ((fullPartDesc == null) ? 0 : fullPartDesc.hashCode());
		result = prime * result + ((imageName == null) ? 0 : imageName.hashCode());
		result = prime * result
				+ ((inspectionPart2Location2Name == null) ? 0 : inspectionPart2Location2Name.hashCode());
		result = prime * result + ((inspectionPart2LocationName == null) ? 0 : inspectionPart2LocationName.hashCode());
		result = prime * result + ((inspectionPart3Name == null) ? 0 : inspectionPart3Name.hashCode());
		result = prime * result + ((inspectionPartLocation2Name == null) ? 0 : inspectionPartLocation2Name.hashCode());
		result = prime * result + ((inspectionPartLocationName == null) ? 0 : inspectionPartLocationName.hashCode());
		result = prime * result + ((inspectionPartName == null) ? 0 : inspectionPartName.hashCode());
		result = prime * result + ((inspectionPart2Name == null) ? 0 : inspectionPart2Name.hashCode());
		result = prime * result + ((iqsCategory == null) ? 0 : iqsCategory.hashCode());
		result = prime * result + ((iqsQuestion == null) ? 0 : iqsQuestion.hashCode());
		result = prime * result + iqsQuestionNo;
		result = prime * result + ((iqsVersion == null) ? 0 : iqsVersion.hashCode());
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((levelThree == null) ? 0 : levelThree.hashCode());
		result = prime * result + ((levelTwo == null) ? 0 : levelTwo.hashCode());
		result = prime * result + ((localDefectCombinationId == null) ? 0 : localDefectCombinationId.hashCode());
		result = prime * result + ((pddaResponsibilityId == null) ? 0 : pddaResponsibilityId.hashCode());
		result = prime * result + ((plantDescription == null) ? 0 : plantDescription.hashCode());
		result = prime * result + ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((productType == null) ? 0 : productType.hashCode());
		result = prime * result + ((productionLot == null) ? 0 : productionLot.hashCode());
		result = prime * result + ((repairAreaName == null) ? 0 : repairAreaName.hashCode());
		result = prime * result + ((repairMethod == null) ? 0 : repairMethod.hashCode());
		result = prime * result + ((originalDefectStatus == null) ? 0 : originalDefectStatus.hashCode());
		result = prime * result + ((responsibleDept == null) ? 0 : responsibleDept.hashCode());
		result = prime * result + ((responsibleLevelName == null) ? 0 : responsibleLevelName.hashCode());
		result = prime * result + ((responsiblePlant == null) ? 0 : responsiblePlant.hashCode());
		result = prime * result + ((responsibleSite == null) ? 0 : responsibleSite.hashCode());
		result = prime * result + ((siteName == null) ? 0 : siteName.hashCode());
		result = prime * result + ((localTheme == null) ? 0 : localTheme.hashCode());
		result = prime * result + ((terminalName == null) ? 0 : terminalName.hashCode());
		result = prime * result + ((textEntryMenu == null) ? 0 : textEntryMenu.hashCode());
		result = prime * result + ((themeName == null) ? 0 : themeName.hashCode());
		result = prime * result + reportable;
		result = prime * result + ((writeupDepartment == null) ? 0 : writeupDepartment.hashCode());
		result = prime * result + ((xAxis == null) ? 0 : xAxis.hashCode());
		result = prime * result + ((yAxis == null) ? 0 : yAxis.hashCode());
		result = prime * result + ((regionalDefectCombinationId == null) ? 0 : regionalDefectCombinationId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefectMapDto other = (DefectMapDto) obj;
		if (associateId == null) {
			if (other.associateId != null)
				return false;
		} else if (!associateId.equals(other.associateId))
			return false;
		if (defectCategoryName == null) {
			if (other.defectCategoryName != null)
				return false;
		} else if (!defectCategoryName.equals(other.defectCategoryName))
			return false;
		if (defectTypeName == null) {
			if (other.defectTypeName != null)
				return false;
		} else if (!defectTypeName.equals(other.defectTypeName))
			return false;
		if (defectTypeName2 == null) {
			if (other.defectTypeName2 != null)
				return false;
		} else if (!defectTypeName2.equals(other.defectTypeName2))
			return false;
		if (entryArea == null) {
			if (other.entryArea != null)
				return false;
		} else if (!entryArea.equals(other.entryArea))
			return false;
		if (entryDepartment == null) {
			if (other.entryDepartment != null)
				return false;
		} else if (!entryDepartment.equals(other.entryDepartment))
			return false;
		if (entryModel == null) {
			if (other.entryModel != null)
				return false;
		} else if (!entryModel.equals(other.entryModel))
			return false;
		if (entryPlantName == null) {
			if (other.entryPlantName != null)
				return false;
		} else if (!entryPlantName.equals(other.entryPlantName))
			return false;
		if (entryScreen == null) {
			if (other.entryScreen != null)
				return false;
		} else if (!entryScreen.equals(other.entryScreen))
			return false;
		if (entrySite == null) {
			if (other.entrySite != null)
				return false;
		} else if (!entrySite.equals(other.entrySite))
			return false;
		if (entrySiteName == null) {
			if (other.entrySiteName != null)
				return false;
		} else if (!entrySiteName.equals(other.entrySiteName))
			return false;
		if (externalDefectCode == null) {
			if (other.externalDefectCode != null)
				return false;
		} else if (!externalDefectCode.equals(other.externalDefectCode))
			return false;
		if (externalPartCode == null) {
			if (other.externalPartCode != null)
				return false;
		} else if (!externalPartCode.equals(other.externalPartCode))
			return false;
		if (externalSystemMapId == null) {
			if (other.externalSystemMapId != null)
				return false;
		} else if (!externalSystemMapId.equals(other.externalSystemMapId))
			return false;
		if (externalSystemName == null) {
			if (other.externalSystemName != null)
				return false;
		} else if (!externalSystemName.equals(other.externalSystemName))
			return false;
		if (currentDefectStatus == null) {
			if (other.currentDefectStatus != null)
				return false;
		} else if (!currentDefectStatus.equals(other.currentDefectStatus))
			return false;
		if (fullPartDesc == null) {
			if (other.fullPartDesc != null)
				return false;
		} else if (!fullPartDesc.equals(other.fullPartDesc))
			return false;
		if (imageName == null) {
			if (other.imageName != null)
				return false;
		} else if (!imageName.equals(other.imageName))
			return false;
		if (inspectionPart2Location2Name == null) {
			if (other.inspectionPart2Location2Name != null)
				return false;
		} else if (!inspectionPart2Location2Name.equals(other.inspectionPart2Location2Name))
			return false;
		if (inspectionPart2LocationName == null) {
			if (other.inspectionPart2LocationName != null)
				return false;
		} else if (!inspectionPart2LocationName.equals(other.inspectionPart2LocationName))
			return false;
		if (inspectionPart3Name == null) {
			if (other.inspectionPart3Name != null)
				return false;
		} else if (!inspectionPart3Name.equals(other.inspectionPart3Name))
			return false;
		if (inspectionPartLocation2Name == null) {
			if (other.inspectionPartLocation2Name != null)
				return false;
		} else if (!inspectionPartLocation2Name.equals(other.inspectionPartLocation2Name))
			return false;
		if (inspectionPartLocationName == null) {
			if (other.inspectionPartLocationName != null)
				return false;
		} else if (!inspectionPartLocationName.equals(other.inspectionPartLocationName))
			return false;
		if (inspectionPartName == null) {
			if (other.inspectionPartName != null)
				return false;
		} else if (!inspectionPartName.equals(other.inspectionPartName))
			return false;
		if (inspectionPart2Name == null) {
			if (other.inspectionPart2Name != null)
				return false;
		} else if (!inspectionPart2Name.equals(other.inspectionPart2Name))
			return false;
		if (iqsCategory == null) {
			if (other.iqsCategory != null)
				return false;
		} else if (!iqsCategory.equals(other.iqsCategory))
			return false;
		if (iqsQuestion == null) {
			if (other.iqsQuestion != null)
				return false;
		} else if (!iqsQuestion.equals(other.iqsQuestion))
			return false;
		if (iqsQuestionNo != other.iqsQuestionNo)
			return false;
		if (iqsVersion == null) {
			if (other.iqsVersion != null)
				return false;
		} else if (!iqsVersion.equals(other.iqsVersion))
			return false;
		if (kdLotNumber == null) {
			if (other.kdLotNumber != null)
				return false;
		} else if (!kdLotNumber.equals(other.kdLotNumber))
			return false;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (levelThree == null) {
			if (other.levelThree != null)
				return false;
		} else if (!levelThree.equals(other.levelThree))
			return false;
		if (levelTwo == null) {
			if (other.levelTwo != null)
				return false;
		} else if (!levelTwo.equals(other.levelTwo))
			return false;
		if (localDefectCombinationId == null) {
			if (other.localDefectCombinationId != null)
				return false;
		} else if (!localDefectCombinationId.equals(other.localDefectCombinationId))
			return false;
		if (pddaResponsibilityId == null) {
			if (other.pddaResponsibilityId != null)
				return false;
		} else if (!pddaResponsibilityId.equals(other.pddaResponsibilityId))
			return false;
		if (plantDescription == null) {
			if (other.plantDescription != null)
				return false;
		} else if (!plantDescription.equals(other.plantDescription))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		if (productType == null) {
			if (other.productType != null)
				return false;
		} else if (!productType.equals(other.productType))
			return false;
		if (productionLot == null) {
			if (other.productionLot != null)
				return false;
		} else if (!productionLot.equals(other.productionLot))
			return false;
		if (repairAreaName == null) {
			if (other.repairAreaName != null)
				return false;
		} else if (!repairAreaName.equals(other.repairAreaName))
			return false;
		if (repairMethod == null) {
			if (other.repairMethod != null)
				return false;
		} else if (!repairMethod.equals(other.repairMethod))
			return false;
		if (originalDefectStatus == null) {
			if (other.originalDefectStatus != null)
				return false;
		} else if (!originalDefectStatus.equals(other.originalDefectStatus))
			return false;
		if (responsibleDept == null) {
			if (other.responsibleDept != null)
				return false;
		} else if (!responsibleDept.equals(other.responsibleDept))
			return false;
		if (responsibleLevelName == null) {
			if (other.responsibleLevelName != null)
				return false;
		} else if (!responsibleLevelName.equals(other.responsibleLevelName))
			return false;
		if (responsiblePlant == null) {
			if (other.responsiblePlant != null)
				return false;
		} else if (!responsiblePlant.equals(other.responsiblePlant))
			return false;
		if (responsibleSite == null) {
			if (other.responsibleSite != null)
				return false;
		} else if (!responsibleSite.equals(other.responsibleSite))
			return false;
		if (siteName == null) {
			if (other.siteName != null)
				return false;
		} else if (!siteName.equals(other.siteName))
			return false;
		if (localTheme == null) {
			if (other.localTheme != null)
				return false;
		} else if (!localTheme.equals(other.localTheme))
			return false;
		if (terminalName == null) {
			if (other.terminalName != null)
				return false;
		} else if (!terminalName.equals(other.terminalName))
			return false;
		if (textEntryMenu == null) {
			if (other.textEntryMenu != null)
				return false;
		} else if (!textEntryMenu.equals(other.textEntryMenu))
			return false;
		if (themeName == null) {
			if (other.themeName != null)
				return false;
		} else if (!themeName.equals(other.themeName))
			return false;
		if (reportable != other.reportable)
			return false;
		if (writeupDepartment == null) {
			if (other.writeupDepartment != null)
				return false;
		} else if (!writeupDepartment.equals(other.writeupDepartment))
			return false;
		if (xAxis == null) {
			if (other.xAxis != null)
				return false;
		} else if (!xAxis.equals(other.xAxis))
			return false;
		if (yAxis == null) {
			if (other.yAxis != null)
				return false;
		} else if (!yAxis.equals(other.yAxis))
			return false;
		if (regionalDefectCombinationId == null) { 
			if(other.regionalDefectCombinationId != null)
				return false;
		} else if(!regionalDefectCombinationId.equals(other.regionalDefectCombinationId))
			return false;
		return true;
	}
	
	
	
	public boolean validateDefectDataTypeAndLength() {

		if(!(StringUtils.length(this.getExternalSystemName()) <= 18))   
			return false;
		
		if(!(StringUtils.length(this.getExternalPartCode()) <= 64))
			return false;
			
		if(!(StringUtils.length(this.getExternalDefectCode())<= 64))
			return false;
		
		if( !(StringUtils.length(this.getProcessPointId()) <= 17))
			return false;
		
		if(StringUtils.isEmpty(this.getProductType()) || !StringUtils.isAlphaSpace(this.getProductType()) || !(StringUtils.length(this.getProductType()) <= 10))
			return false;
		
		if( !(StringUtils.length(this.getEntryDepartment()) <= 32))
			return false;
		
		if( !(StringUtils.length(this.getEntrySite()) <= 16))
			return false;
		
		if(!StringUtils.isNumeric(this.getOriginalDefectStatus()) || !(StringUtils.length(this.getOriginalDefectStatus()) <= 5))
			return false;
		
		if( !StringUtils.isNumeric(this.getCurrentDefectStatus()) || !(StringUtils.length(this.getCurrentDefectStatus())<=5))
			return false;
		
		if( !StringUtils.isAlphanumericSpace(this.getWriteupDepartment()) || !(StringUtils.length(this.getWriteupDepartment()) <= 32))
			return false;
		
		if( !StringUtils.isAlphanumericSpace(getImageName()) || !(StringUtils.length(getImageName()) <=20))
			return false;
		
		if( !StringUtils.isNumeric(this.getxAxis()) || !(StringUtils.length(getxAxis())<= 10))
			return false;
		
		if(!StringUtils.isNumeric(getyAxis()) || !(StringUtils.length(getyAxis()) <= 10))
			return false;
		
		if(!(StringUtils.length(getAssociateId()) <= 11))
			return false;

		return true;
	}

	public short getIsQicsRepairReqd() {
		return isQicsRepairReqd;
	}

	public void setIsQicsRepairReqd(short isQicsRepairReqd) {
		this.isQicsRepairReqd = isQicsRepairReqd;
	}

	public short getIsExtSysRepairReqd() {
		return isExtSysRepairReqd;
	}

	public void setIsExtSysRepairReqd(short isExtSysRepairReqd) {
		this.isExtSysRepairReqd = isExtSysRepairReqd;
	}
}
