package com.honda.galc.dto.qi;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * <h3>QiDefectResult Class description</h3>
 * <p>
 * QiDefectResult
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
 *        Nov 26, 2016
 * 
 */
/**
 * 
 *    
 * @author Vivek Bettada
 * @since Feb 8, 2019
 * Recent Defect is an aggregate defect result including a usage count
 */
public class QiRecentDefectDto implements IDto {
	
	private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName =  "DEFECTRESULTID")
	private long defectresultid; //if defectResultId, it will be converted to DEFECT_RESULT_ID
	
	@DtoTag(outputName =  "PRODUCT_ID")
	private String productId;
	
	@DtoTag(outputName =  "INSPECTION_PART_NAME")
	private String inspectionPartName;
	
    @DtoTag(outputName =  "INSPECTION_PART_LOCATION_NAME")
    private String inspectionPartLocationName;
    
	@DtoTag(outputName =  "INSPECTION_PART_LOCATION2_NAME")
	private String inspectionPartLocation2Name;
	
	@DtoTag(outputName =  "INSPECTION_PART2_NAME")
	private String inspectionPart2Name;
	
	@DtoTag(outputName =  "INSPECTION_PART2_LOCATION_NAME")
	private String inspectionPart2LocationName;
	
	@DtoTag(outputName =  "INSPECTION_PART2_LOCATION2_NAME")
	private String inspectionPart2Location2Name;
	
	@DtoTag(outputName =  "INSPECTION_PART3_NAME")
	private String inspectionPart3Name;
	
	@DtoTag(outputName =  "DEFECT_TYPE_NAME")
	private String defectTypeName;
	
	@DtoTag(outputName =  "DEFECT_TYPE_NAME2")
	private String defectTypeName2;
	
	@DtoTag(outputName =  "POINT_X")
	private int pointX;
	
	@DtoTag(outputName =  "POINT_Y")
	private int pointY;

	@DtoTag(outputName =  "IMAGE_NAME")
	private String imageName;
	
	@DtoTag(outputName =  "APPLICATION_ID")
	private String applicationId;
	
	@DtoTag(outputName =  "IQS_VERSION")
	private String iqsVersion;
	
	@DtoTag(outputName =  "IQS_CATEGORY_NAME")
	private String iqsCategoryName;
	
	@DtoTag(outputName =  "IQS_QUESTION_NO")
	private int iqsQuestionNo;
	
	@DtoTag(outputName =  "IQS_QUESTION")
	private String iqsQuestion;
	
	@DtoTag(outputName =  "THEME_NAME")
	private String themeName;
	
	@DtoTag(outputName =  "REPORTABLE")
	private short reportable;
	
	@DtoTag(outputName =  "RESPONSIBLE_SITE")
	private String responsibleSite;
	
	@DtoTag(outputName =  "RESPONSIBLE_PLANT")
	private String responsiblePlant;
	
	@DtoTag(outputName =  "RESPONSIBLE_DEPT")
	private String responsibleDept;
	
	@DtoTag(outputName =  "RESPONSIBLE_LEVEL3")
	private String responsibleLevel3;
	
	@DtoTag(outputName =  "RESPONSIBLE_LEVEL2")
	private String responsibleLevel2;
	
	@DtoTag(outputName =  "RESPONSIBLE_LEVEL1")
	private String responsibleLevel1;
	
	@DtoTag(outputName =  "PDDA_MODEL_YEAR")
	private float pddaModelYear;
	
	@DtoTag(outputName =  "PDDA_VEHICLE_MODEL_CODE")
	private String pddaVehicleModelCode;
	
	@DtoTag(outputName =  "PROCESS_NUMBER")
	private String processNo;
	
	@DtoTag(outputName =  "PROCESS_NAME")
	private String processName;
	
	@DtoTag(outputName =  "UNIT_NUMBER")
	private String unitNo;
	
	@DtoTag(outputName =  "UNIT_DESC")
	private String unitDesc;
	
	@DtoTag(outputName =  "RESPONSIBLE_ASSOCIATE")
	private String responsibleAssociate;
	
	@DtoTag(outputName =  "WRITE_UP_DEPARTMENT")
	private String writeUpDept;
	
	@DtoTag(outputName =  "ENTRY_SITE_NAME")
	private String entrySiteName;
	
	@DtoTag(outputName =  "ENTRY_PLANT_NAME")
	private String entryPlantName;
	
	@DtoTag(outputName =  "ENTRY_PROD_LINE_NO")
	private int entryProdLineNo;
	
	@DtoTag(outputName =  "ENTRY_DEPT")
	private String entryDept;
	
	@DtoTag(outputName =  "ACTUAL_TIMESTAMP")
	private Date actualTimestamp;
	
	@DtoTag(outputName =  "PRODUCTION_DATE")
	private Date productionDate;
	
	@DtoTag(outputName =  "SHIFT")
	private String shift;
	
	@DtoTag(outputName =  "TEAM")
	private String team;
	
	@DtoTag(outputName =  "PRODUCT_TYPE")
	private String productType;
	
	@DtoTag(outputName =  "PRODUCT_SPEC_CODE")
	private String productSpecCode;
	
	@DtoTag(outputName =  "BOM_MAIN_PART_NO")
	private String bomMainPartNo;
	
	@DtoTag(outputName =  "ORIGINAL_DEFECT_STATUS")
	private short originalDefectStatus;
	
	@DtoTag(outputName =  "CURRENT_DEFECT_STATUS")
	private short currentDefectStatus;
	
	@DtoTag(outputName =  "REPAIR_AREA")
	private String repairArea;
	
	@DtoTag(outputName =  "REPAIR_METHOD_NAME_PLAN")
	private String repairMethodNamePlan;
	
	@DtoTag(outputName =  "REPAIR_TIME_PLAN")
	private int repairTimePlan;
	
	@DtoTag(outputName =  "LOCAL_THEME")
	private String localTheme;
	
	@DtoTag(outputName =  "DELETED")
	private short deleted;
	
	@DtoTag(outputName =  "GDP_DEFECT")
	private short gdpDefect;
	
	@DtoTag(outputName =  "TRPU_DEFECT")
	private short trpuDefect;
	
	@DtoTag(outputName =  "TERMINAL_NAME")
	private String terminalName;

	
	@DtoTag(outputName =  "PRODUCTION_LOT")
	private String productionLot;
	
	@DtoTag(outputName =  "KD_LOT_NUMBER")
	private String kdLotNumber;
	
	@DtoTag(outputName =  "AF_ON_SEQUENCE_NUMBER")
	private int afOnSequenceNumber;
	
	@DtoTag(outputName =  "DEFECT_CATEGORY_NAME")
	private String defectCategoryName;
	
	@DtoTag(outputName =  "IS_REPAIR_RELATED")
	private short isRepairRelated;
	
	@DtoTag(outputName =  "GROUP_TIMESTAMP")
	private Timestamp groupTimestamp;
	
	@DtoTag(outputName =  "ENTRY_SCREEN")
	private String entryScreen;
	
	@DtoTag(outputName =  "ENGINE_FIRING_FLAG")
	private short engineFiringFlag;
	
	@DtoTag(outputName =  "INCIDENT_ID")
	private int incidentId;
	
	@DtoTag(outputName =  "ENTRY_MODEL")
	private String entryModel;
	
	@DtoTag(outputName =  "COMMENT")
	private String comment;
	
	@DtoTag(outputName =  "REASON_FOR_CHANGE")
	private String reasonForChange;
	
	@DtoTag(outputName =  "CORRECTION_REQUEST_BY")
	private String correctionRequestBy;
	
	@DtoTag(outputName =  "COUNT")
	private int count;
	
	@DtoTag(outputName = "DIVISION_ID")
	private String kickoutDivisionId;
	
	@DtoTag(outputName = "LINE_ID")
	private String kickoutLineId;
	
	@DtoTag(outputName = "PROCESS_POINT_ID")
	private String kickoutProcessPointId;
	
	@DtoTag(outputName = "PROCESS_POINT_NAME")
	private String kickoutProcessPointName;
	
	@DtoTag(outputName = "KICKOUT_ID")
	private long kickoutId;
	
	public QiRecentDefectDto() {
		super();
	}

	public String getPartDefectDesc() {
		return StringUtils.trimToEmpty(inspectionPartName+" " +
				   inspectionPartLocationName+" " +
				   inspectionPartLocation2Name+" " +
				   inspectionPart2Name+" " +
				   inspectionPart2LocationName+" " +
				   inspectionPart2Location2Name+" " +
				   inspectionPart3Name+" " +
				   defectTypeName+" " +
				   defectTypeName2).replaceAll("null", "").replaceAll("\\s+"," ");
	}
	
	public String getResponsibilityDesc() {
		return StringUtils.trimToEmpty(responsibleSite+" " +
				   responsiblePlant+" " +
				   responsibleDept+" " +
				   responsibleLevel3+" " +
				   responsibleLevel2+" " +
				   responsibleLevel1).replaceAll("null", "").replaceAll("\\s+"," ");
	}
	
	public Object getId() {
		return getDefectResultId();
	}

	/**
	 * @return the defectResultId
	 */
	public long getDefectResultId() {
		return defectresultid;
	}

	/**
	 * @param defectResultId the defectResultId to set
	 */
	public void setDefectResultId(long defectresultid) {
		this.defectresultid = defectresultid;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return StringUtils.trimToEmpty(productId);
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return the inspectionPartName
	 */
	public String getInspectionPartName() {
		return StringUtils.trimToEmpty(inspectionPartName);
	}

	/**
	 * @param inspectionPartName the inspectionPartName to set
	 */
	public void setInspectionPartName(String inspectionPartName) {
		this.inspectionPartName = inspectionPartName;
	}

	/**
	 * @return the inspectionPartLocationName
	 */
	public String getInspectionPartLocationName() {
		return StringUtils.trimToEmpty(inspectionPartLocationName);
	}

	/**
	 * @param inspectionPartLocationName the inspectionPartLocationName to set
	 */
	public void setInspectionPartLocationName(String inspectionPartLocationName) {
		this.inspectionPartLocationName = inspectionPartLocationName;
	}

	/**
	 * @return the inspectionPartLocation2Name
	 */
	public String getInspectionPartLocation2Name() {
		return StringUtils.trimToEmpty(inspectionPartLocation2Name);
	}

	/**
	 * @param inspectionPartLocation2Name the inspectionPartLocation2Name to set
	 */
	public void setInspectionPartLocation2Name(String inspectionPartLocation2Name) {
		this.inspectionPartLocation2Name = inspectionPartLocation2Name;
	}

	/**
	 * @return the inspectionPart2Name
	 */
	public String getInspectionPart2Name() {
		return StringUtils.trimToEmpty(inspectionPart2Name);
	}

	/**
	 * @param inspectionPart2Name the inspectionPart2Name to set
	 */
	public void setInspectionPart2Name(String inspectionPart2Name) {
		this.inspectionPart2Name = inspectionPart2Name;
	}

	/**
	 * @return the inspectionPart2LocationName
	 */
	public String getInspectionPart2LocationName() {
		return StringUtils.trimToEmpty(inspectionPart2LocationName);
	}

	/**
	 * @param inspectionPart2LocationName the inspectionPart2LocationName to set
	 */
	public void setInspectionPart2LocationName(String inspectionPart2LocationName) {
		this.inspectionPart2LocationName = inspectionPart2LocationName;
	}

	/**
	 * @return the inspectionPart2Location2Name
	 */
	public String getInspectionPart2Location2Name() {
		return StringUtils.trimToEmpty(inspectionPart2Location2Name);
	}

	/**
	 * @param inspectionPart2Location2Name the inspectionPart2Location2Name to set
	 */
	public void setInspectionPart2Location2Name(String inspectionPart2Location2Name) {
		this.inspectionPart2Location2Name = inspectionPart2Location2Name;
	}

	/**
	 * @return the inspectionPart3Name
	 */
	public String getInspectionPart3Name() {
		return StringUtils.trimToEmpty(inspectionPart3Name);
	}

	/**
	 * @param inspectionPart3Name the inspectionPart3Name to set
	 */
	public void setInspectionPart3Name(String inspectionPart3Name) {
		this.inspectionPart3Name = inspectionPart3Name;
	}

	/**
	 * @return the defectTypeName
	 */
	public String getDefectTypeName() {
		return StringUtils.trimToEmpty(defectTypeName);
	}

	/**
	 * @param defectTypeName the defectTypeName to set
	 */
	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}

	/**
	 * @return the defectTypeName2
	 */
	public String getDefectTypeName2() {
		return StringUtils.trimToEmpty(defectTypeName2);
	}

	/**
	 * @param defectTypeName2 the defectTypeName2 to set
	 */
	public void setDefectTypeName2(String defectTypeName2) {
		this.defectTypeName2 = defectTypeName2;
	}

	/**
	 * @return the pointX
	 */
	public int getPointX() {
		return pointX;
	}

	/**
	 * @param pointX the pointX to set
	 */
	public void setPointX(int pointX) {
		this.pointX = pointX;
	}

	/**
	 * @return the pointY
	 */
	public int getPointY() {
		return pointY;
	}

	/**
	 * @param pointY the pointY to set
	 */
	public void setPointY(int pointY) {
		this.pointY = pointY;
	}

	/**
	 * @return the imageName
	 */
	public String getImageName() {
		return StringUtils.trimToEmpty(imageName);
	}

	/**
	 * @param imageName the imageName to set
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	/**
	 * @return the applicationId
	 */
	public String getApplicationId() {
		return StringUtils.trimToEmpty(applicationId);
	}

	/**
	 * @param applicationId the applicationId to set
	 */
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * @return the iqsVersion
	 */
	public String getIqsVersion() {
		return StringUtils.trimToEmpty(iqsVersion);
	}

	/**
	 * @param iqsVersion the iqsVersion to set
	 */
	public void setIqsVersion(String iqsVersion) {
		this.iqsVersion = iqsVersion;
	}

	/**
	 * @return the iqsCategoryName
	 */
	public String getIqsCategoryName() {
		return StringUtils.trimToEmpty(iqsCategoryName);
	}

	/**
	 * @param iqsCategoryName the iqsCategoryName to set
	 */
	public void setIqsCategoryName(String iqsCategoryName) {
		this.iqsCategoryName = iqsCategoryName;
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
	 * @return the iqsUestion
	 */
	public String getIqsQuestion() {
		return StringUtils.trimToEmpty(iqsQuestion);
	}

	/**
	 * @param iqsUestion the iqsUestion to set
	 */
	public void setIqsQuestion(String iqsQuestion) {
		this.iqsQuestion = iqsQuestion;
	}

	/**
	 * @return the themeName
	 */
	public String getThemeName() {
		return StringUtils.trimToEmpty(themeName);
	}

	/**
	 * @param themeName the themeName to set
	 */
	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	/**
	 * @return the reportable
	 */
	public short getReportable() {
		return reportable;
	}

	/**
	 * @param reportable the reportable to set
	 */
	public void setReportable(short reportable) {
		this.reportable = reportable;
	}

	/**
	 * @return the responsibleSite
	 */
	public String getResponsibleSite() {
		return StringUtils.trimToEmpty(responsibleSite);
	}

	/**
	 * @param responsibleSite the responsibleSite to set
	 */
	public void setResponsibleSite(String responsibleSite) {
		this.responsibleSite = responsibleSite;
	}

	/**
	 * @return the responsibleDept
	 */
	public String getResponsibleDept() {
		return StringUtils.trimToEmpty(responsibleDept);
	}

	/**
	 * @param responsibleDept the responsibleDept to set
	 */
	public void setResponsibleDept(String responsibleDept) {
		this.responsibleDept = responsibleDept;
	}

	/**
	 * @return the responsibleLevel3
	 */
	public String getResponsibleLevel3() {
		return StringUtils.trimToEmpty(responsibleLevel3);
	}

	/**
	 * @param responsibleLevel3 the responsibleLevel3 to set
	 */
	public void setResponsibleLevel3(String responsibleLevel3) {
		this.responsibleLevel3 = responsibleLevel3;
	}

	/**
	 * @return the responsibleLevel2
	 */
	public String getResponsibleLevel2() {
		return StringUtils.trimToEmpty(responsibleLevel2);
	}

	/**
	 * @param responsibleLevel2 the responsibleLevel2 to set
	 */
	public void setResponsibleLevel2(String responsibleLevel2) {
		this.responsibleLevel2 = responsibleLevel2;
	}

	/**
	 * @return the responsibleLevel1
	 */
	public String getResponsibleLevel1() {
		return StringUtils.trimToEmpty(responsibleLevel1);
	}

	/**
	 * @param responsibleLevel1 the responsibleLevel1 to set
	 */
	public void setResponsibleLevel1(String responsibleLevel1) {
		this.responsibleLevel1 = responsibleLevel1;
	}

	public float getPddaModelYear() {
		return pddaModelYear;
	}

	public void setPddaModelYear(float pddaModelYear) {
		this.pddaModelYear = pddaModelYear;
	}

	public String getPddaVehicleModelCode() {
		return StringUtils.trimToEmpty(pddaVehicleModelCode);
	}

	public void setPddaVehicleModelCode(String pddaVehicleModelCode) {
		this.pddaVehicleModelCode = pddaVehicleModelCode;
	}

	/**
	 * @return the processNo
	 */
	public String getProcessNo() {
		return StringUtils.trimToEmpty(processNo);
	}

	/**
	 * @param processNo the processNo to set
	 */
	public void setProcessNo(String processNo) {
		this.processNo = processNo;
	}

	/**
	 * @return the processName
	 */
	public String getProcessName() {
		return StringUtils.trimToEmpty(processName);
	}

	/**
	 * @param processName the processName to set
	 */
	public void setProcessName(String processName) {
		this.processName = processName;
	}

	/**
	 * @return the unitNo
	 */
	public String getUnitNo() {
		return StringUtils.trimToEmpty(unitNo);
	}

	/**
	 * @param unitNo the unitNo to set
	 */
	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}

	/**
	 * @return the unitDesc
	 */
	public String getUnitDesc() {
		return StringUtils.trimToEmpty(unitDesc);
	}

	/**
	 * @param unitDesc the unitDesc to set
	 */
	public void setUnitDesc(String unitDesc) {
		this.unitDesc = unitDesc;
	}

	/**
	 * @return the responsibleAssociate
	 */
	public String getResponsibleAssociate() {
		return StringUtils.trimToEmpty(responsibleAssociate);
	}

	/**
	 * @param responsibleAssociate the responsibleAssociate to set
	 */
	public void setResponsibleAssociate(String responsibleAssociate) {
		this.responsibleAssociate = responsibleAssociate;
	}

	/**
	 * @return the writeUpDept
	 */
	public String getWriteUpDept() {
		return StringUtils.trimToEmpty(writeUpDept);
	}

	/**
	 * @param writeUpDept the writeUpDept to set
	 */
	public void setWriteUpDept(String writeUpDept) {
		this.writeUpDept = writeUpDept;
	}

	/**
	 * @return the entrySiteName
	 */
	public String getEntrySiteName() {
		return StringUtils.trimToEmpty(entrySiteName);
	}

	/**
	 * @param entrySiteName the entrySiteName to set
	 */
	public void setEntrySiteName(String entrySiteName) {
		this.entrySiteName = entrySiteName;
	}

	/**
	 * @return the entryPlantName
	 */
	public String getEntryPlantName() {
		return StringUtils.trimToEmpty(entryPlantName);
	}

	/**
	 * @param entryPlantName the entryPlantName to set
	 */
	public void setEntryPlantName(String entryPlantName) {
		this.entryPlantName = entryPlantName;
	}

	/**
	 * @return the entryProdLineNo
	 */
	public int getEntryProdLineNo() {
		return entryProdLineNo;
	}

	/**
	 * @param entryProdLineNo the entryProdLineNo to set
	 */
	public void setEntryProdLineNo(int entryProdLineNo) {
		this.entryProdLineNo = entryProdLineNo;
	}

	/**
	 * @return the entryTimestamp
	 */
	public Date getActualTimestamp() {
		return actualTimestamp;
	}

	public String getActualTimestampForDisplay() {
		return StringUtils.trimToEmpty(CommonUtil.formatDate(actualTimestamp));
	}

	/**
	 * @param entryTimestamp the entryTimestamp to set
	 */
	public void setActualTimestamp(Date actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	

	/**
	 * @return the shift
	 */
	public String getShift() {
		return StringUtils.trimToEmpty(shift);
	}

	/**
	 * @param shift the shift to set
	 */
	public void setShift(String shift) {
		this.shift = shift;
	}

	/**
	 * @return the team
	 */
	public String getTeam() {
		return StringUtils.trimToEmpty(team);
	}

	/**
	 * @param team the team to set
	 */
	public void setTeam(String team) {
		this.team = team;
	}

	/**
	 * @return the productType
	 */
	public String getProductType() {
		return StringUtils.trimToEmpty(productType);
	}

	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}

	/**
	 * @return the productSpecCode
	 */
	public String getProductSpecCode() {
		return StringUtils.trimToEmpty(productSpecCode);
	}

	/**
	 * @param productSpecCode the productSpecCode to set
	 */
	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	/**
	 * @return the bomMainPartNo
	 */
	public String getBomMainPartNo() {
		return StringUtils.trimToEmpty(bomMainPartNo);
	}

	/**
	 * @param bomMainPartNo the bomMainPartNo to set
	 */
	public void setBomMainPartNo(String bomMainPartNo) {
		this.bomMainPartNo = bomMainPartNo;
	}

	/**
	 * @return the originalDefectStatus
	 */
	public short getOriginalDefectStatus() {
		return originalDefectStatus;
	}
	
	/**
	 * @param originalDefectStatus the originalDefectStatus to set
	 */
	public void setOriginalDefectStatus(short originalDefectStatus) {
		this.originalDefectStatus = originalDefectStatus;
	}

	public void setDefectStatus(String selectedRadioBtn) {
		if(selectedRadioBtn!=null){
			if(selectedRadioBtn.equals(DefectStatus.REPAIRED.getName())) {
				this.originalDefectStatus = (short)DefectStatus.REPAIRED.getId();
				this.currentDefectStatus = (short)DefectStatus.FIXED.getId();
			} else if(selectedRadioBtn.equals(DefectStatus.NOT_REPAIRED.getName())) {
				this.originalDefectStatus = (short)DefectStatus.NOT_REPAIRED.getId();
				this.currentDefectStatus = (short)DefectStatus.NOT_FIXED.getId();
			} else if(selectedRadioBtn.equals(DefectStatus.NON_REPAIRABLE.getName())) {
				this.originalDefectStatus = (short)DefectStatus.NON_REPAIRABLE.getId();
				this.currentDefectStatus = (short)DefectStatus.NON_REPAIRABLE.getId();
			}
		}
	}

	/**
	 * @return the currentDefectStatus
	 */
	public short getCurrentDefectStatus() {
		return currentDefectStatus;
	}

	/**
	 * @param currentDefectStatus the currentDefectStatus to set
	 */
	public void setCurrentDefectStatus(short currentDefectStatus) {
		this.currentDefectStatus = currentDefectStatus;
	}

	/**
	 * @return the repairArea
	 */
	public String getRepairArea() {
		return StringUtils.trimToEmpty(repairArea);
	}

	/**
	 * @param repairArea the repairArea to set
	 */
	public void setRepairArea(String repairArea) {
		this.repairArea = repairArea;
	}

	/**
	 * @return the repairMethodNamePlan
	 */
	public String getRepairMethodNamePlan() {
		return StringUtils.trimToEmpty(repairMethodNamePlan);
	}

	/**
	 * @param repairMethodNamePlan the repairMethodNamePlan to set
	 */
	public void setRepairMethodNamePlan(String repairMethodNamePlan) {
		this.repairMethodNamePlan = repairMethodNamePlan;
	}

	/**
	 * @return the repairTimePlan
	 */
	public int getRepairTimePlan() {
		return repairTimePlan;
	}

	/**
	 * @param repairTimePlan the repairTimePlan to set
	 */
	public void setRepairTimePlan(int repairTimePlan) {
		this.repairTimePlan = repairTimePlan;
	}

	/**
	 * @return the localTheme
	 */
	public String getLocalTheme() {
		return StringUtils.trimToEmpty(localTheme);
	}

	/**
	 * @param localTheme the localTheme to set
	 */
	public void setLocalTheme(String localTheme) {
		this.localTheme = localTheme;
	}

	/**
	 * @return the deleted
	 */
	public short getDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(short deleted) {
		this.deleted = deleted;
	}

	/**
	 * @return the gdpDefect
	 */
	public short getGdpDefect() {
		return gdpDefect;
	}

	/**
	 * @param gdpDefect the gdpDefect to set
	 */
	public void setGdpDefect(short gdpDefect) {
		this.gdpDefect = gdpDefect;
	}

	/**
	 * @return the trpuDefect
	 */
	public short getTrpuDefect() {
		return trpuDefect;
	}

	/**
	 * @param trpuDefect the trpuDefect to set
	 */
	public void setTrpuDefect(short trpuDefect) {
		this.trpuDefect = trpuDefect;
	}

	/**
	 * @return the terminalName
	 */
	public String getTerminalName() {
		return StringUtils.trimToEmpty(terminalName);
	}

	/**
	 * @param terminalName the terminalName to set
	 */
	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}

	
	/**
	 * @return responsible plant name
	 */
	public String getResponsiblePlant() {
		return StringUtils.trimToEmpty(responsiblePlant);
	}
	
	/**
	 * @param used to responsiblePlant 
	 */
	public void setResponsiblePlant(String responsiblePlant) {
		this.responsiblePlant = responsiblePlant;
	}
	/**
	 * @return reponsible plant name
	 */
	public String getEntryDept() {
		return StringUtils.trimToEmpty(entryDept);
	}
	/**
	 * @param used to set entryDept
	 */
	public void setEntryDept(String entryDept) {
		this.entryDept = entryDept;
	}
	/**
	 * @return production lot
	 */
	public String getProductionLot() {
		return StringUtils.trimToEmpty(productionLot);
	}
	/**
	 * @param used to set productionLot
	 */
	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}
	/**
	 * @return kdLotNumber
	 */
	public String getKdLotNumber() {
		return StringUtils.trimToEmpty(kdLotNumber);
	}
	/**
	 * @param used to set kdLotNumber
	 */
	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}
	/**
	 * @return afOnSequenceNumber
	 */
	public int getAfOnSequenceNumber() {
		return afOnSequenceNumber;
	}
	/**
	 * @param used to set afOnSequenceNumber
	 */
	public void setAfOnSequenceNumber(int afOnSequenceNumber) {
		this.afOnSequenceNumber = afOnSequenceNumber;
	}
	/**
	 * @return defectCategoryName
	 */
	public String getDefectCategoryName() {
		return StringUtils.trimToEmpty(defectCategoryName);
	}
	/**
	 * @param used to set defectCategoryName
	 */
	public void setDefectCategoryName(String defectCategoryName) {
		this.defectCategoryName = defectCategoryName;
	}

	/**
	 * @return original defect status as String
	 */
	public String getOriginalStatus() {
		return DefectStatus.getType(originalDefectStatus).getName().toUpperCase();
	}
	
	/**
	 * @return current defect status as String
	 */
	public String getCurrentStatus() {
		return DefectStatus.getType(currentDefectStatus).getName().toUpperCase();
	}
	
	/**
	 * Checks if Defect is Not Repaired
	 * @return
	 */
	public boolean isDefectNotRepaired() {
		return this.currentDefectStatus == DefectStatus.NOT_FIXED.getId();
	}
	
	/**
	 * Checks if Defect is Non Repairable
	 * @return
	 */
	public boolean isDefectNotRepairable() {
		return this.currentDefectStatus == DefectStatus.NON_REPAIRABLE.getId();
	}
	
	public DefectStatus getDefectStatusType() {
		return DefectStatus.getType(this.originalDefectStatus);
	}
	
	public short getIsRepairRelated() {
		return isRepairRelated;
	}

	public void setIsRepairRelated(short isRepairRelated) {
		this.isRepairRelated = isRepairRelated;
	}

	public Timestamp getGroupTimestamp() {
		return groupTimestamp;
	}

	public void setGroupTimestamp(Timestamp groupTimestamp) {
		this.groupTimestamp = groupTimestamp;
	}
	
	

	public String getEntryScreen() {
		return StringUtils.trimToEmpty(entryScreen);
	}

	public void setEntryScreen(String entryScreen) {
		this.entryScreen = entryScreen;
	}
	

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public short getEngineFiringFlag() {
		return engineFiringFlag;
	}

	public void setEngineFiringFlag(short engineFiringFlag) {
		this.engineFiringFlag = engineFiringFlag;
	}

	public int getIncidentId() {
		return incidentId;
	}

	public void setIncidentId(int incidentId) {
		this.incidentId = incidentId;
	}
	
	public String getEntryModel() {
		return StringUtils.trimToEmpty(entryModel);
	}

	public void setEntryModel(String entryModel) {
		this.entryModel = entryModel;
	}

	public String getComment() {
		return StringUtils.trimToEmpty(comment);
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getReasonForChange() {
		return StringUtils.trimToEmpty(reasonForChange);
	}

	public void setReasonForChange(String reasonForChange) {
		this.reasonForChange = reasonForChange;
	}

	public String getCorrectionRequestBy() {
		return StringUtils.trimToEmpty(correctionRequestBy);
	}

	public void setCorrectionRequestBy(String correctionRequestBy) {
		this.correctionRequestBy = correctionRequestBy;
	}

	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public String getKickoutDivisionId() {
		return StringUtils.trim(this.kickoutDivisionId);
	}
	
	public void setKicoutDivisionId(String kickoutDivisionId) {
		this.kickoutDivisionId = kickoutDivisionId;
	}
	
	public String getKickoutLineId() {
		return StringUtils.trim(this.kickoutLineId);
	}
	
	public void setKickoutLineId(String kickoutLineId) {
		this.kickoutLineId = kickoutLineId;
	}
	
	public String getKickoutProcessPointId() {
		return StringUtils.trim(this.kickoutProcessPointId);
	}
	
	public void setKickoutProcessPointId(String kickoutProcessPointId) {
		this.kickoutProcessPointId = kickoutProcessPointId;
	}
	
	public String getKickoutProcessPointName() {
		return StringUtils.trim(this.kickoutProcessPointName);
	}
	
	public void setKickoutProcessPointName(String kickoutProcessPointName) {
		this.kickoutProcessPointName = kickoutProcessPointName;
	}
	
	public long getKickoutId() {
		return this.kickoutId;
	}
	
	public void setKickoutId(long kickoutId) {
		this.kickoutId = kickoutId;
	}
	
	public String getPartDefectDescWithDefectResultId() {
		return StringUtils.trimToEmpty("("+defectresultid+")"+inspectionPartName+" " +
				   inspectionPartLocationName+" " +
				   inspectionPartLocation2Name+" " +
				   inspectionPart2Name+" " +
				   inspectionPart2LocationName+" " +
				   inspectionPart2Location2Name+" " +
				   inspectionPart3Name+" " +
				   defectTypeName+" " +
				   defectTypeName2).replaceAll("null", "").replaceAll("\\s+"," ");
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result + afOnSequenceNumber;
		result = prime * result + ((applicationId == null) ? 0 : applicationId.hashCode());
		result = prime * result + ((bomMainPartNo == null) ? 0 : bomMainPartNo.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((correctionRequestBy == null) ? 0 : correctionRequestBy.hashCode());
		result = prime * result + currentDefectStatus;
		result = prime * result + ((defectCategoryName == null) ? 0 : defectCategoryName.hashCode());
		result = prime * result + (int) (defectresultid ^ (defectresultid >>> 32));
		result = prime * result + ((defectTypeName == null) ? 0 : defectTypeName.hashCode());
		result = prime * result + ((defectTypeName2 == null) ? 0 : defectTypeName2.hashCode());
		result = prime * result + deleted;
		result = prime * result + engineFiringFlag;
		result = prime * result + ((entryDept == null) ? 0 : entryDept.hashCode());
		result = prime * result + ((entryModel == null) ? 0 : entryModel.hashCode());
		result = prime * result + ((entryPlantName == null) ? 0 : entryPlantName.hashCode());
		result = prime * result + entryProdLineNo;
		result = prime * result + ((entryScreen == null) ? 0 : entryScreen.hashCode());
		result = prime * result + ((entrySiteName == null) ? 0 : entrySiteName.hashCode());
		result = prime * result + gdpDefect;
		result = prime * result + ((groupTimestamp == null) ? 0 : groupTimestamp.hashCode());
		result = prime * result + ((imageName == null) ? 0 : imageName.hashCode());
		result = prime * result + incidentId;
		result = prime * result
				+ ((inspectionPart2Location2Name == null) ? 0 : inspectionPart2Location2Name.hashCode());
		result = prime * result + ((inspectionPart2LocationName == null) ? 0 : inspectionPart2LocationName.hashCode());
		result = prime * result + ((inspectionPart2Name == null) ? 0 : inspectionPart2Name.hashCode());
		result = prime * result + ((inspectionPart3Name == null) ? 0 : inspectionPart3Name.hashCode());
		result = prime * result + ((inspectionPartLocation2Name == null) ? 0 : inspectionPartLocation2Name.hashCode());
		result = prime * result + ((inspectionPartLocationName == null) ? 0 : inspectionPartLocationName.hashCode());
		result = prime * result + ((inspectionPartName == null) ? 0 : inspectionPartName.hashCode());
		result = prime * result + ((iqsCategoryName == null) ? 0 : iqsCategoryName.hashCode());
		result = prime * result + ((iqsQuestion == null) ? 0 : iqsQuestion.hashCode());
		result = prime * result + iqsQuestionNo;
		result = prime * result + ((iqsVersion == null) ? 0 : iqsVersion.hashCode());
		result = prime * result + isRepairRelated;
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((localTheme == null) ? 0 : localTheme.hashCode());
		result = prime * result + originalDefectStatus;
		result = prime * result + Float.floatToIntBits(pddaModelYear);
		result = prime * result + ((pddaVehicleModelCode == null) ? 0 : pddaVehicleModelCode.hashCode());
		result = prime * result + pointX;
		result = prime * result + pointY;
		result = prime * result + ((processName == null) ? 0 : processName.hashCode());
		result = prime * result + ((processNo == null) ? 0 : processNo.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((productType == null) ? 0 : productType.hashCode());
		result = prime * result + ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime * result + ((productionLot == null) ? 0 : productionLot.hashCode());
		result = prime * result + ((reasonForChange == null) ? 0 : reasonForChange.hashCode());
		result = prime * result + ((repairArea == null) ? 0 : repairArea.hashCode());
		result = prime * result + ((repairMethodNamePlan == null) ? 0 : repairMethodNamePlan.hashCode());
		result = prime * result + repairTimePlan;
		result = prime * result + reportable;
		result = prime * result + ((responsibleAssociate == null) ? 0 : responsibleAssociate.hashCode());
		result = prime * result + ((responsibleDept == null) ? 0 : responsibleDept.hashCode());
		result = prime * result + ((responsibleLevel1 == null) ? 0 : responsibleLevel1.hashCode());
		result = prime * result + ((responsibleLevel2 == null) ? 0 : responsibleLevel2.hashCode());
		result = prime * result + ((responsibleLevel3 == null) ? 0 : responsibleLevel3.hashCode());
		result = prime * result + ((responsiblePlant == null) ? 0 : responsiblePlant.hashCode());
		result = prime * result + ((responsibleSite == null) ? 0 : responsibleSite.hashCode());
		result = prime * result + ((shift == null) ? 0 : shift.hashCode());
		result = prime * result + ((team == null) ? 0 : team.hashCode());
		result = prime * result + ((terminalName == null) ? 0 : terminalName.hashCode());
		result = prime * result + ((themeName == null) ? 0 : themeName.hashCode());
		result = prime * result + trpuDefect;
		result = prime * result + ((unitDesc == null) ? 0 : unitDesc.hashCode());
		result = prime * result + ((unitNo == null) ? 0 : unitNo.hashCode());
		result = prime * result + ((writeUpDept == null) ? 0 : writeUpDept.hashCode());
		result = prime * result + (int)kickoutId;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiRecentDefectDto other = (QiRecentDefectDto) obj;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (afOnSequenceNumber != other.afOnSequenceNumber)
			return false;
		if (applicationId == null) {
			if (other.applicationId != null)
				return false;
		} else if (!applicationId.equals(other.applicationId))
			return false;
		if (bomMainPartNo == null) {
			if (other.bomMainPartNo != null)
				return false;
		} else if (!bomMainPartNo.equals(other.bomMainPartNo))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (correctionRequestBy == null) {
			if (other.correctionRequestBy != null)
				return false;
		} else if (!correctionRequestBy.equals(other.correctionRequestBy))
			return false;
		if (currentDefectStatus != other.currentDefectStatus)
			return false;
		if (defectCategoryName == null) {
			if (other.defectCategoryName != null)
				return false;
		} else if (!defectCategoryName.equals(other.defectCategoryName))
			return false;
		if (defectresultid != other.defectresultid)
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
		if (deleted != other.deleted)
			return false;
		if (engineFiringFlag != other.engineFiringFlag)
			return false;
		if (entryDept == null) {
			if (other.entryDept != null)
				return false;
		} else if (!entryDept.equals(other.entryDept))
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
		if (entryProdLineNo != other.entryProdLineNo)
			return false;
		if (entryScreen == null) {
			if (other.entryScreen != null)
				return false;
		} else if (!entryScreen.equals(other.entryScreen))
			return false;
		if (entrySiteName == null) {
			if (other.entrySiteName != null)
				return false;
		} else if (!entrySiteName.equals(other.entrySiteName))
			return false;
		if (gdpDefect != other.gdpDefect)
			return false;
		if (groupTimestamp == null) {
			if (other.groupTimestamp != null)
				return false;
		} else if (!groupTimestamp.equals(other.groupTimestamp))
			return false;
		if (imageName == null) {
			if (other.imageName != null)
				return false;
		} else if (!imageName.equals(other.imageName))
			return false;
		if (incidentId != other.incidentId)
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
		if (inspectionPart2Name == null) {
			if (other.inspectionPart2Name != null)
				return false;
		} else if (!inspectionPart2Name.equals(other.inspectionPart2Name))
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
		if (iqsCategoryName == null) {
			if (other.iqsCategoryName != null)
				return false;
		} else if (!iqsCategoryName.equals(other.iqsCategoryName))
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
		if (isRepairRelated != other.isRepairRelated)
			return false;
		if (kdLotNumber == null) {
			if (other.kdLotNumber != null)
				return false;
		} else if (!kdLotNumber.equals(other.kdLotNumber))
			return false;
		if (localTheme == null) {
			if (other.localTheme != null)
				return false;
		} else if (!localTheme.equals(other.localTheme))
			return false;
		if (originalDefectStatus != other.originalDefectStatus)
			return false;
		if (Float.floatToIntBits(pddaModelYear) != Float.floatToIntBits(other.pddaModelYear))
			return false;
		if (pddaVehicleModelCode == null) {
			if (other.pddaVehicleModelCode != null)
				return false;
		} else if (!pddaVehicleModelCode.equals(other.pddaVehicleModelCode))
			return false;
		if (pointX != other.pointX)
			return false;
		if (pointY != other.pointY)
			return false;
		if (processName == null) {
			if (other.processName != null)
				return false;
		} else if (!processName.equals(other.processName))
			return false;
		if (processNo == null) {
			if (other.processNo != null)
				return false;
		} else if (!processNo.equals(other.processNo))
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
		if (productionDate == null) {
			if (other.productionDate != null)
				return false;
		} else if (!productionDate.equals(other.productionDate))
			return false;
		if (productionLot == null) {
			if (other.productionLot != null)
				return false;
		} else if (!productionLot.equals(other.productionLot))
			return false;
		if (reasonForChange == null) {
			if (other.reasonForChange != null)
				return false;
		} else if (!reasonForChange.equals(other.reasonForChange))
			return false;
		if (repairArea == null) {
			if (other.repairArea != null)
				return false;
		} else if (!repairArea.equals(other.repairArea))
			return false;
		if (repairMethodNamePlan == null) {
			if (other.repairMethodNamePlan != null)
				return false;
		} else if (!repairMethodNamePlan.equals(other.repairMethodNamePlan))
			return false;
		if (repairTimePlan != other.repairTimePlan)
			return false;
		if (reportable != other.reportable)
			return false;
		if (responsibleAssociate == null) {
			if (other.responsibleAssociate != null)
				return false;
		} else if (!responsibleAssociate.equals(other.responsibleAssociate))
			return false;
		if (responsibleDept == null) {
			if (other.responsibleDept != null)
				return false;
		} else if (!responsibleDept.equals(other.responsibleDept))
			return false;
		if (responsibleLevel1 == null) {
			if (other.responsibleLevel1 != null)
				return false;
		} else if (!responsibleLevel1.equals(other.responsibleLevel1))
			return false;
		if (responsibleLevel2 == null) {
			if (other.responsibleLevel2 != null)
				return false;
		} else if (!responsibleLevel2.equals(other.responsibleLevel2))
			return false;
		if (responsibleLevel3 == null) {
			if (other.responsibleLevel3 != null)
				return false;
		} else if (!responsibleLevel3.equals(other.responsibleLevel3))
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
		if (shift == null) {
			if (other.shift != null)
				return false;
		} else if (!shift.equals(other.shift))
			return false;
		if (team == null) {
			if (other.team != null)
				return false;
		} else if (!team.equals(other.team))
			return false;
		if (terminalName == null) {
			if (other.terminalName != null)
				return false;
		} else if (!terminalName.equals(other.terminalName))
			return false;
		if (themeName == null) {
			if (other.themeName != null)
				return false;
		} else if (!themeName.equals(other.themeName))
			return false;
		if (trpuDefect != other.trpuDefect)
			return false;
		if (unitDesc == null) {
			if (other.unitDesc != null)
				return false;
		} else if (!unitDesc.equals(other.unitDesc))
			return false;
		if (unitNo == null) {
			if (other.unitNo != null)
				return false;
		} else if (!unitNo.equals(other.unitNo))
			return false;
		if (writeUpDept == null) {
			if (other.writeUpDept != null)
				return false;
		} else if (!writeUpDept.equals(other.writeUpDept))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QiRecentDefectDto [defectresultid=" + defectresultid + ", productId=" + productId
				+ ", inspectionPartName=" + inspectionPartName + ", inspectionPartLocationName="
				+ inspectionPartLocationName + ", inspectionPartLocation2Name=" + inspectionPartLocation2Name
				+ ", inspectionPart2Name=" + inspectionPart2Name + ", inspectionPart2LocationName="
				+ inspectionPart2LocationName + ", inspectionPart2Location2Name=" + inspectionPart2Location2Name
				+ ", inspectionPart3Name=" + inspectionPart3Name + ", defectTypeName=" + defectTypeName
				+ ", defectTypeName2=" + defectTypeName2 + ", pointX=" + pointX + ", pointY=" + pointY + ", imageName="
				+ imageName + ", applicationId=" + applicationId + ", iqsVersion=" + iqsVersion + ", iqsCategoryName="
				+ iqsCategoryName + ", iqsQuestionNo=" + iqsQuestionNo + ", iqsQuestion=" + iqsQuestion + ", themeName="
				+ themeName + ", reportable=" + reportable + ", responsibleSite=" + responsibleSite
				+ ", responsiblePlant=" + responsiblePlant + ", responsibleDept=" + responsibleDept
				+ ", responsibleLevel3=" + responsibleLevel3 + ", responsibleLevel2=" + responsibleLevel2
				+ ", responsibleLevel1=" + responsibleLevel1 + ", pddaModelYear=" + pddaModelYear
				+ ", pddaVehicleModelCode=" + pddaVehicleModelCode + ", processNo=" + processNo + ", processName="
				+ processName + ", unitNo=" + unitNo + ", unitDesc=" + unitDesc + ", responsibleAssociate="
				+ responsibleAssociate + ", writeUpDept=" + writeUpDept + ", entrySiteName=" + entrySiteName
				+ ", entryPlantName=" + entryPlantName + ", entryProdLineNo=" + entryProdLineNo + ", entryDept="
				+ entryDept + ", actualTimestamp=" + actualTimestamp + ", productionDate=" + productionDate + ", shift="
				+ shift + ", team=" + team + ", productType=" + productType + ", productSpecCode=" + productSpecCode
				+ ", bomMainPartNo=" + bomMainPartNo + ", originalDefectStatus=" + originalDefectStatus
				+ ", currentDefectStatus=" + currentDefectStatus + ", repairArea=" + repairArea
				+ ", repairMethodNamePlan=" + repairMethodNamePlan + ", repairTimePlan=" + repairTimePlan
				+ ", localTheme=" + localTheme + ", deleted=" + deleted + ", gdpDefect=" + gdpDefect + ", trpuDefect="
				+ trpuDefect + ", terminalName=" + terminalName + ", productionLot=" + productionLot + ", kdLotNumber="
				+ kdLotNumber + ", afOnSequenceNumber=" + afOnSequenceNumber + ", defectCategoryName="
				+ defectCategoryName + ", isRepairRelated=" + isRepairRelated + ", groupTimestamp=" + groupTimestamp
				+ ", entryScreen=" + entryScreen + ", engineFiringFlag=" + engineFiringFlag + ", incidentId="
				+ incidentId + ", entryModel=" + entryModel + ", comment=" + comment + ", reasonForChange="
				+ reasonForChange + ", correctionRequestBy=" + correctionRequestBy + "kickoutId=" + kickoutId + "]";
	}
}