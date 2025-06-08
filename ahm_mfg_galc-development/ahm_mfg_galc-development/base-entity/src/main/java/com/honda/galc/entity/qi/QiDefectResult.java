package com.honda.galc.entity.qi;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.qi.QiRecentDefectDto;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.CreateUserAuditEntry;
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
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */
@Entity
@Table(name = "QI_DEFECT_RESULT_TBX")
public class QiDefectResult extends CreateUserAuditEntry implements Cloneable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "DEFECTRESULTID", nullable=false)
	private long defectResultId;
	
	@Column(name = "PRODUCT_ID", nullable=false)
	private String productId;
	
	@Column(name = "INSPECTION_PART_NAME")
	private String inspectionPartName;
	
    @Column(name = "INSPECTION_PART_LOCATION_NAME")
    private String inspectionPartLocationName;
    
	@Column(name = "INSPECTION_PART_LOCATION2_NAME")
	private String inspectionPartLocation2Name;
	
	@Column(name = "INSPECTION_PART2_NAME")
	private String inspectionPart2Name;
	
	@Column(name = "INSPECTION_PART2_LOCATION_NAME")
	private String inspectionPart2LocationName;
	
	@Column(name = "INSPECTION_PART2_LOCATION2_NAME")
	private String inspectionPart2Location2Name;
	
	@Column(name = "INSPECTION_PART3_NAME")
	private String inspectionPart3Name;
	
	@Column(name = "DEFECT_TYPE_NAME")
	private String defectTypeName;
	
	@Column(name = "DEFECT_TYPE_NAME2")
	private String defectTypeName2;
	
	@Column(name = "POINT_X")
	private int pointX;
	
	@Column(name = "POINT_Y")
	private int pointY;

	@Column(name = "IMAGE_NAME")
	private String imageName;
	
	@Column(name = "APPLICATION_ID", nullable=false)
	private String applicationId;
	
	@Column(name = "IQS_VERSION")
	private String iqsVersion;
	
	@Column(name = "IQS_CATEGORY_NAME")
	private String iqsCategoryName;
	
	@Column(name = "IQS_QUESTION_NO")
	private int iqsQuestionNo;
	
	@Column(name = "IQS_QUESTION")
	private String iqsQuestion;
	
	@Column(name = "THEME_NAME")
	private String themeName;
	
	@Column(name = "REPORTABLE")
	private short reportable;
	
	@Column(name = "RESPONSIBLE_SITE")
	private String responsibleSite;
	
	@Column(name = "RESPONSIBLE_PLANT")
	private String responsiblePlant;
	
	@Column(name = "RESPONSIBLE_DEPT")
	private String responsibleDept;
	
	@Column(name = "RESPONSIBLE_LEVEL3")
	private String responsibleLevel3;
	
	@Column(name = "RESPONSIBLE_LEVEL2")
	private String responsibleLevel2;
	
	@Column(name = "RESPONSIBLE_LEVEL1")
	private String responsibleLevel1;
	
	@Column(name = "PDDA_MODEL_YEAR")
	private float pddaModelYear;
	
	@Column(name = "PDDA_VEHICLE_MODEL_CODE")
	private String pddaVehicleModelCode;
	
	@Column(name = "PROCESS_NUMBER")
	private String processNo;
	
	@Column(name = "PROCESS_NAME")
	private String processName;
	
	@Column(name = "UNIT_NUMBER")
	private String unitNo;
	
	@Column(name = "UNIT_DESC")
	private String unitDesc;
	
	@Column(name = "RESPONSIBLE_ASSOCIATE")
	private String responsibleAssociate;
	
	@Column(name = "WRITE_UP_DEPARTMENT")
	private String writeUpDept;
	
	@Column(name = "ENTRY_SITE_NAME")
	private String entrySiteName;
	
	@Column(name = "ENTRY_PLANT_NAME")
	private String entryPlantName;
	
	@Column(name = "ENTRY_PROD_LINE_NO")
	private int entryProdLineNo;
	
	@Column(name = "ENTRY_DEPT")
	private String entryDept;
	
	@Column(name = "ACTUAL_TIMESTAMP")
	private Date actualTimestamp;
	
	@Column(name = "PRODUCTION_DATE")
	private Date productionDate;
	
	@Column(name = "SHIFT")
	private String shift;
	
	@Column(name = "TEAM")
	private String team;
	
	@Column(name = "PRODUCT_TYPE")
	private String productType;
	
	@Column(name = "PRODUCT_SPEC_CODE")
	private String productSpecCode;
	
	@Column(name = "BOM_MAIN_PART_NO")
	private String bomMainPartNo;
	
	@Column(name = "ORIGINAL_DEFECT_STATUS")
	private short originalDefectStatus;
	
	@Column(name = "CURRENT_DEFECT_STATUS")
	private short currentDefectStatus;
	
	@Column(name = "REPAIR_AREA")
	private String repairArea;
	
	@Column(name = "REPAIR_METHOD_NAME_PLAN")
	private String repairMethodNamePlan;
	
	@Column(name = "REPAIR_TIME_PLAN")
	private int repairTimePlan;
	
	@Column(name = "LOCAL_THEME")
	private String localTheme;
	
	@Column(name = "DELETED")
	private short deleted;
	
	@Column(name = "GDP_DEFECT")
	private short gdpDefect;
	
	@Column(name = "TRPU_DEFECT")
	private short trpuDefect;
	
	@Column(name = "TERMINAL_NAME")
	private String terminalName;

	@Column(name = "PRODUCTION_LOT")
	private String productionLot;
	
	@Column(name = "KD_LOT_NUMBER")
	private String kdLotNumber;
	
	@Column(name = "AF_ON_SEQUENCE_NUMBER")
	private int afOnSequenceNumber;
	
	@Column(name = "DEFECT_CATEGORY_NAME")
	private String defectCategoryName;
	
	@Column(name = "IS_REPAIR_RELATED")
	private short isRepairRelated;
	
	@Column(name = "GROUP_TIMESTAMP")
	private Timestamp groupTimestamp;
	
	@Column(name = "ENTRY_SCREEN")
	private String entryScreen;
	
	@Column(name = "ENGINE_FIRING_FLAG")
	private short engineFiringFlag;
	
	@Column(name = "INCIDENT_ID")
	private int incidentId;
	
	@Column(name = "ENTRY_MODEL")
	private String entryModel;
	
	@Column(name = "COMMENT")
	private String comment;
	
	@Column(name = "REASON_FOR_CHANGE")
	private String reasonForChange;
	
	@Column(name = "CORRECTION_REQUEST_BY")
	private String correctionRequestBy;
	
	@Column(name ="DEFECT_TRANSACTION_GROUP_ID")
	private long defectTransactionGroupId;
	
	@Column(name = "KICKOUT_ID")
	private long kickoutId;
	
	@Transient
	private String kickoutDivisionId;
	
	@Transient
	private String kickoutLineId;
	
	@Transient
	private String kickoutProcessPointId;
	
	@Transient
	private String kickoutProcessPointName;
	
	@Transient
	private QiResponsibleLevel defaultResponsibleLevel1;
	
	@Transient
	private double iqsScore = 0;
	
	@Transient
	private short iqsAuditAction = 0;
	
	@Transient
	private List<QiDefectResultImage> defectResultImages;

	public QiResponsibleLevel getDefaultResponsibleLevel1() {
		return defaultResponsibleLevel1;
	}

	public void setDefaultResponsibleLevel1(QiResponsibleLevel defaultResponsibleLevel1) {
		this.defaultResponsibleLevel1 = defaultResponsibleLevel1;
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
		return defectResultId;
	}

	/**
	 * @param defectResultId the defectResultId to set
	 */
	public void setDefectResultId(long defectResultId) {
		this.defectResultId = defectResultId;
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
	
	public boolean isDefectFixed() {
		return this.currentDefectStatus == DefectStatus.FIXED.getId();
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

	public Long getDefectTransactionGroupId() {
		return this.defectTransactionGroupId;
	}

	public void setDefectTransactionGroupId(Long defectTransactionGroupId) {
		this.defectTransactionGroupId = defectTransactionGroupId;
	}
	
	public long getKickoutId() {
		return this.kickoutId;
	}
	
	public void setKickoutId(long kickoutId) {
		this.kickoutId = kickoutId;
	}
	
	public String getKickoutDivisionId() {
		return StringUtils.trim(this.kickoutDivisionId);
	}
	
	public void setKickoutDivisionId(String kickoutDivisionId) {
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
	
	public void setKickoutProcessPointId(String kickoutProcessPointName) {
		this.kickoutProcessPointId = kickoutProcessPointName;
	}
	
	public String getKickoutProcessPointName() {
		return StringUtils.trim(this.kickoutProcessPointName);
	}
	
	public void setKickoutProcessPointName(String kickoutProcessPointName) {
		this.kickoutProcessPointName = kickoutProcessPointName;
	}

	public double getIqsScore() {
		return iqsScore;
	}

	public void setIqsScore(double iqsScore) {
		this.iqsScore = iqsScore;
	}

	public short getIqsAuditAction() {
		return iqsAuditAction;
	}

	public void setIqsAuditAction(short iqsAuditAction) {
		this.iqsAuditAction = iqsAuditAction;
	}

	public List<QiDefectResultImage> getDefectResultImages() {
		if(defectResultImages == null) {
			defectResultImages = new ArrayList<QiDefectResultImage>();
		}
		return defectResultImages;
	}

	public void setDefectResultImages(List<QiDefectResultImage> defectResultImages) {
		this.defectResultImages = defectResultImages;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result + afOnSequenceNumber;
		result = prime * result
				+ ((applicationId == null) ? 0 : applicationId.hashCode());
		result = prime * result
				+ ((bomMainPartNo == null) ? 0 : bomMainPartNo.hashCode());
		result = prime * result + currentDefectStatus;
		result = prime * result + ((productionDate == null) ? 0 : productionDate.hashCode());
		result = prime
				* result
				+ ((defectCategoryName == null) ? 0 : defectCategoryName
						.hashCode());
		result = prime * result + (int)defectResultId;
		result = prime * result + (int)defectTransactionGroupId;
		result = prime * result
				+ ((defectTypeName == null) ? 0 : defectTypeName.hashCode());
		result = prime * result
				+ ((defectTypeName2 == null) ? 0 : defectTypeName2.hashCode());
		result = prime * result + deleted;
		result = prime * result
				+ ((entryDept == null) ? 0 : entryDept.hashCode());
		result = prime * result
				+ ((entryPlantName == null) ? 0 : entryPlantName.hashCode());
		result = prime * result + entryProdLineNo;
		result = prime * result
				+ ((entrySiteName == null) ? 0 : entrySiteName.hashCode());
		result = prime * result + gdpDefect;
		result = prime * result
				+ ((imageName == null) ? 0 : imageName.hashCode());
		result = prime
				* result
				+ ((inspectionPart2Location2Name == null) ? 0
						: inspectionPart2Location2Name.hashCode());
		result = prime
				* result
				+ ((inspectionPart2LocationName == null) ? 0
						: inspectionPart2LocationName.hashCode());
		result = prime
				* result
				+ ((inspectionPart2Name == null) ? 0 : inspectionPart2Name
						.hashCode());
		result = prime
				* result
				+ ((inspectionPart3Name == null) ? 0 : inspectionPart3Name
						.hashCode());
		result = prime
				* result
				+ ((inspectionPartLocation2Name == null) ? 0
						: inspectionPartLocation2Name.hashCode());
		result = prime
				* result
				+ ((inspectionPartLocationName == null) ? 0
						: inspectionPartLocationName.hashCode());
		result = prime
				* result
				+ ((inspectionPartName == null) ? 0 : inspectionPartName
						.hashCode());
		result = prime * result
				+ ((iqsCategoryName == null) ? 0 : iqsCategoryName.hashCode());
		result = prime * result
				+ ((iqsQuestion == null) ? 0 : iqsQuestion.hashCode());
		result = prime * result + iqsQuestionNo;
		result = prime * result
				+ ((iqsVersion == null) ? 0 : iqsVersion.hashCode());
		result = prime * result
				+ ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + originalDefectStatus;
		result = prime * result + pointX;
		result = prime * result + pointY;
		result = prime * result
				+ ((processName == null) ? 0 : processName.hashCode());
		result = prime * result
				+ ((processNo == null) ? 0 : processNo.hashCode());
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		result = prime * result
				+ ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result
				+ ((productType == null) ? 0 : productType.hashCode());
		result = prime * result
				+ ((productionLot == null) ? 0 : productionLot.hashCode());
		result = prime * result
				+ ((repairArea == null) ? 0 : repairArea.hashCode());
		result = prime
				* result
				+ ((repairMethodNamePlan == null) ? 0 : repairMethodNamePlan
						.hashCode());
		result = prime * result + repairTimePlan;
		result = prime * result + reportable;
		result = prime
				* result
				+ ((responsibleAssociate == null) ? 0 : responsibleAssociate
						.hashCode());
		result = prime * result
				+ ((responsibleDept == null) ? 0 : responsibleDept.hashCode());
		result = prime
				* result
				+ ((responsibleLevel1 == null) ? 0 : responsibleLevel1
						.hashCode());
		result = prime
				* result
				+ ((responsibleLevel2 == null) ? 0 : responsibleLevel2
						.hashCode());
		result = prime
				* result
				+ ((responsibleLevel3 == null) ? 0 : responsibleLevel3
						.hashCode());
		result = prime
				* result
				+ ((responsiblePlant == null) ? 0 : responsiblePlant.hashCode());
		result = prime * result
				+ ((responsibleSite == null) ? 0 : responsibleSite.hashCode());
		result = prime * result + ((shift == null) ? 0 : shift.hashCode());
		result = prime * result + ((team == null) ? 0 : team.hashCode());
		result = prime
				* result
				+ ((localTheme == null) ? 0 : localTheme
						.hashCode());
		result = prime * result
				+ ((terminalName == null) ? 0 : terminalName.hashCode());
		result = prime * result
				+ ((themeName == null) ? 0 : themeName.hashCode());
		result = prime * result + trpuDefect;
		result = prime * result
				+ ((unitDesc == null) ? 0 : unitDesc.hashCode());
		result = prime * result + ((unitNo == null) ? 0 : unitNo.hashCode());
		result = prime * result
				+ ((writeUpDept == null) ? 0 : writeUpDept.hashCode());
		result = prime * result + ((entryModel == null) ? 0 : entryModel.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + (int)pddaModelYear;
		result = prime * result + ((pddaVehicleModelCode == null) ? 0 : pddaVehicleModelCode.hashCode());
		result = prime * result + (int)kickoutId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiDefectResult other = (QiDefectResult) obj;
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
		if (imageName == null) {
			if (other.imageName != null)
				return false;
		} else if (!imageName.equals(other.imageName))
			return false;
		if (inspectionPart2Location2Name == null) {
			if (other.inspectionPart2Location2Name != null)
				return false;
		} else if (!inspectionPart2Location2Name
				.equals(other.inspectionPart2Location2Name))
			return false;
		if (inspectionPart2LocationName == null) {
			if (other.inspectionPart2LocationName != null)
				return false;
		} else if (!inspectionPart2LocationName
				.equals(other.inspectionPart2LocationName))
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
		} else if (!inspectionPartLocation2Name
				.equals(other.inspectionPartLocation2Name))
			return false;
		if (inspectionPartLocationName == null) {
			if (other.inspectionPartLocationName != null)
				return false;
		} else if (!inspectionPartLocationName
				.equals(other.inspectionPartLocationName))
			return false;
		if (inspectionPartName == null) {
			if (other.inspectionPartName != null)
				return false;
		} else if (!inspectionPartName.equals(other.inspectionPartName))
			return false;
		if (pointX != other.pointX)
			return false;
		if (pointY != other.pointY)
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (entryModel == null) {
			if (other.entryModel != null)
				return false;
		} else if (!entryModel.equals(other.entryModel))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (localTheme == null) {
			if (other.localTheme != null)
				return false;
		} else if (!localTheme.equals(other.localTheme))
			return false;
		if (pddaModelYear != other.pddaModelYear)
			return false;
		if (pddaVehicleModelCode == null) {
			if (other.pddaVehicleModelCode != null)
				return false;
		} else if (!pddaVehicleModelCode.equals(other.pddaVehicleModelCode))
			return false;
		return true;
	}
	public QiDefectResult() {
		super();
	}

	public QiDefectResult(QiRepairResultDto repairResultDto) {
		this.productId = repairResultDto.getProductId();
		this.inspectionPartName = repairResultDto.getInspectionPartName();
		this.inspectionPartLocationName =  repairResultDto.getInspectionPartLocationName();
		this.inspectionPartLocation2Name =  repairResultDto.getInspectionPartLocation2Name();
		this.inspectionPart2Name = repairResultDto.getInspectionPart2Name();
		this.inspectionPart2LocationName = repairResultDto.getInspectionPart2LocationName();
		this.inspectionPart2Location2Name = repairResultDto.getInspectionPart2Location2Name();
		this.inspectionPart3Name = repairResultDto.getInspectionPart3Name();
		this.defectTypeName = repairResultDto.getDefectTypeName();
		this.defectTypeName2 = repairResultDto.getDefectTypeName2();
		this.pointX = repairResultDto.getPointX();
		this.pointY = repairResultDto.getPointY();
		this.imageName = repairResultDto.getImageName();
		this.applicationId = repairResultDto.getApplicationId();
		this.responsibleDept = repairResultDto.getResponsibleDept();
		this.currentDefectStatus = repairResultDto.getCurrentDefectStatus();
		this.defectCategoryName = repairResultDto.getDefectCategoryName();
		this.entryDept = repairResultDto.getEntryDept();
		this.isRepairRelated = 1;
		this.defectTransactionGroupId = repairResultDto.getDefectTransactionGroupId();
		this.kickoutId = repairResultDto.getKickoutId();
		this.kickoutDivisionId = repairResultDto.getKickoutDivisionId();
		this.kickoutLineId = repairResultDto.getKickoutLineId();
		this.kickoutProcessPointId = repairResultDto.getKickoutProcessPointId();
		this.kickoutProcessPointName = repairResultDto.getKickoutProcessPointName();
	}
	
	public QiDefectResult(QiRepairResult qiRepairResult) {
		this.defectResultId = qiRepairResult.getDefectResultId();
		this.productId = qiRepairResult.getProductId();
		this.inspectionPartName = qiRepairResult.getInspectionPartName();
		this.inspectionPartLocationName =  qiRepairResult.getInspectionPartLocationName();
		this.inspectionPartLocation2Name =  qiRepairResult.getInspectionPartLocation2Name();
		this.inspectionPart2Name = qiRepairResult.getInspectionPart2Name();
		this.inspectionPart2LocationName = qiRepairResult.getInspectionPart2LocationName();
		this.inspectionPart2Location2Name = qiRepairResult.getInspectionPart2Location2Name();
		this.inspectionPart3Name = qiRepairResult.getInspectionPart3Name();
		this.defectTypeName = qiRepairResult.getDefectTypeName();
		this.defectTypeName2 = qiRepairResult.getDefectTypeName2();
		this.pointX = qiRepairResult.getPointX();
		this.pointY = qiRepairResult.getPointY();
		this.imageName = qiRepairResult.getImageName();
		this.applicationId = qiRepairResult.getApplicationId();
		this.iqsVersion = qiRepairResult.getIqsVersion();
		this.iqsCategoryName = qiRepairResult.getIqsCategoryName();
		this.iqsQuestionNo = qiRepairResult.getIqsQuestionNo();
		this.iqsQuestion = qiRepairResult.getIqsQuestion();
		this.themeName = qiRepairResult.getThemeName();
		this.reportable = qiRepairResult.getReportable();
		this.responsibleSite = qiRepairResult.getResponsibleSite();
		this.responsiblePlant = qiRepairResult.getResponsiblePlant();
		this.responsibleDept = qiRepairResult.getResponsibleDept();
		this.responsibleLevel3 = qiRepairResult.getResponsibleLevel3();
		this.responsibleLevel2 = qiRepairResult.getResponsibleLevel2();
		this.responsibleLevel1 = qiRepairResult.getResponsibleLevel1();
		this.pddaModelYear = qiRepairResult.getPddaModelYear();
		this.pddaVehicleModelCode = qiRepairResult.getPddaVehicleModelCode();
		this.processNo = qiRepairResult.getProcessNo();
		this.processName = qiRepairResult.getProcessName();
		this.unitNo = qiRepairResult.getUnitNo();
		this.unitDesc = qiRepairResult.getUnitDesc();
		this.responsibleAssociate = qiRepairResult.getResponsibleAssociate();
		this.writeUpDept = qiRepairResult.getWriteUpDept();
		this.entrySiteName = qiRepairResult.getEntrySiteName();
		this.entryPlantName = qiRepairResult.getEntryPlantName();
		this.entryProdLineNo = qiRepairResult.getEntryProdLineNo();
		this.entryDept = qiRepairResult.getEntryDept();
		this.actualTimestamp = qiRepairResult.getActualTimestamp();
		this.productionDate = qiRepairResult.getProductionDate();
		this.shift = qiRepairResult.getShift();
		this.team = qiRepairResult.getTeam();
		this.productType = qiRepairResult.getProductType();
		this.productSpecCode = qiRepairResult.getProductSpecCode();
		this.bomMainPartNo = qiRepairResult.getBomMainPartNo();
		this.originalDefectStatus = qiRepairResult.getOriginalDefectStatus();
		this.currentDefectStatus = qiRepairResult.getCurrentDefectStatus();
		this.repairArea = qiRepairResult.getRepairArea();
		this.repairMethodNamePlan = qiRepairResult.getRepairMethodNamePlan();
		this.repairTimePlan = qiRepairResult.getRepairTimePlan();
		this.localTheme = qiRepairResult.getLocalTheme();
		this.deleted = qiRepairResult.getDeleted();
		this.gdpDefect = qiRepairResult.getGdpDefect();
		this.trpuDefect = qiRepairResult.getTrpuDefect();
		this.terminalName = qiRepairResult.getTerminalName();
		this.productionLot = qiRepairResult.getProductionLot();
		this.kdLotNumber = qiRepairResult.getKdLotNumber();
		this.afOnSequenceNumber = qiRepairResult.getAfOnSequenceNumber();
		this.defectCategoryName = qiRepairResult.getDefectCategoryName();
		this.isRepairRelated = qiRepairResult.getIsRepairRelated();
		this.groupTimestamp = qiRepairResult.getGroupTimestamp();
		this.entryScreen = qiRepairResult.getEntryScreen();
		this.engineFiringFlag = qiRepairResult.getEngineFiringFlag();
		this.incidentId = qiRepairResult.getIncidentId();
		this.entryModel = qiRepairResult.getEntryModel();
		this.comment = qiRepairResult.getComment();
		this.defectTransactionGroupId = qiRepairResult.getDefectTransactionGroupId();
	}
	
	public static QiDefectResult createInstance(QiRecentDefectDto qiRecentDefectDto) {
		QiDefectResult defectResult = new QiDefectResult();
		defectResult.inspectionPartName = qiRecentDefectDto.getInspectionPartName();
		defectResult.inspectionPartLocationName =  qiRecentDefectDto.getInspectionPartLocationName();
		defectResult.inspectionPartLocation2Name =  qiRecentDefectDto.getInspectionPartLocation2Name();
		defectResult.inspectionPart2Name = qiRecentDefectDto.getInspectionPart2Name();
		defectResult.inspectionPart2LocationName = qiRecentDefectDto.getInspectionPart2LocationName();
		defectResult.inspectionPart2Location2Name = qiRecentDefectDto.getInspectionPart2Location2Name();
		defectResult.inspectionPart3Name = qiRecentDefectDto.getInspectionPart3Name();
		defectResult.defectTypeName = qiRecentDefectDto.getDefectTypeName();
		defectResult.defectTypeName2 = qiRecentDefectDto.getDefectTypeName2();
		defectResult.pointX = qiRecentDefectDto.getPointX();
		defectResult.pointY = qiRecentDefectDto.getPointY();
		defectResult.imageName = qiRecentDefectDto.getImageName();
		defectResult.applicationId = qiRecentDefectDto.getApplicationId();
		defectResult.iqsVersion = qiRecentDefectDto.getIqsVersion();
		defectResult.iqsCategoryName = qiRecentDefectDto.getIqsCategoryName();
		defectResult.iqsQuestionNo = qiRecentDefectDto.getIqsQuestionNo();
		defectResult.iqsQuestion = qiRecentDefectDto.getIqsQuestion();
		defectResult.themeName = qiRecentDefectDto.getThemeName();
		defectResult.reportable = qiRecentDefectDto.getReportable();
		defectResult.responsibleSite = qiRecentDefectDto.getResponsibleSite();
		defectResult.responsiblePlant = qiRecentDefectDto.getResponsiblePlant();
		defectResult.responsibleDept = qiRecentDefectDto.getResponsibleDept();
		defectResult.responsibleLevel3 = qiRecentDefectDto.getResponsibleLevel3();
		defectResult.responsibleLevel2 = qiRecentDefectDto.getResponsibleLevel2();
		defectResult.responsibleLevel1 = qiRecentDefectDto.getResponsibleLevel1();
		defectResult.pddaModelYear = qiRecentDefectDto.getPddaModelYear();
		defectResult.pddaVehicleModelCode = qiRecentDefectDto.getPddaVehicleModelCode();
		defectResult.processNo = qiRecentDefectDto.getProcessNo();
		defectResult.processName = qiRecentDefectDto.getProcessName();
		defectResult.unitNo = qiRecentDefectDto.getUnitNo();
		defectResult.unitDesc = qiRecentDefectDto.getUnitDesc();
		defectResult.responsibleAssociate = qiRecentDefectDto.getResponsibleAssociate();
		defectResult.writeUpDept = qiRecentDefectDto.getWriteUpDept();
		defectResult.entrySiteName = qiRecentDefectDto.getEntrySiteName();
		defectResult.entryPlantName = qiRecentDefectDto.getEntryPlantName();
		defectResult.entryProdLineNo = qiRecentDefectDto.getEntryProdLineNo();
		defectResult.entryDept = qiRecentDefectDto.getEntryDept();
		defectResult.shift = qiRecentDefectDto.getShift();
		defectResult.team = qiRecentDefectDto.getTeam();
		defectResult.productType = qiRecentDefectDto.getProductType();
		defectResult.bomMainPartNo = qiRecentDefectDto.getBomMainPartNo();
		defectResult.originalDefectStatus = qiRecentDefectDto.getOriginalDefectStatus();
		defectResult.currentDefectStatus = qiRecentDefectDto.getCurrentDefectStatus();
		defectResult.repairArea = qiRecentDefectDto.getRepairArea();
		defectResult.repairMethodNamePlan = qiRecentDefectDto.getRepairMethodNamePlan();
		defectResult.repairTimePlan = qiRecentDefectDto.getRepairTimePlan();
		defectResult.localTheme = qiRecentDefectDto.getLocalTheme();
		defectResult.deleted = qiRecentDefectDto.getDeleted();
		defectResult.gdpDefect = qiRecentDefectDto.getGdpDefect();
		defectResult.trpuDefect = qiRecentDefectDto.getTrpuDefect();
		defectResult.terminalName = qiRecentDefectDto.getTerminalName();
		defectResult.defectCategoryName = qiRecentDefectDto.getDefectCategoryName();
		defectResult.isRepairRelated = qiRecentDefectDto.getIsRepairRelated();
		defectResult.groupTimestamp = qiRecentDefectDto.getGroupTimestamp();
		defectResult.entryScreen = qiRecentDefectDto.getEntryScreen();
		defectResult.engineFiringFlag = qiRecentDefectDto.getEngineFiringFlag();
		defectResult.incidentId = qiRecentDefectDto.getIncidentId();
		defectResult.entryModel = qiRecentDefectDto.getEntryModel();
		defectResult.comment = qiRecentDefectDto.getComment();
		defectResult.kickoutDivisionId = qiRecentDefectDto.getKickoutDivisionId();
		defectResult.kickoutLineId = qiRecentDefectDto.getKickoutLineId();
		defectResult.kickoutProcessPointId = qiRecentDefectDto.getKickoutProcessPointId();
		defectResult.kickoutId = qiRecentDefectDto.getKickoutId();
		defectResult.kickoutProcessPointName = qiRecentDefectDto.getKickoutProcessPointName();
		return defectResult;
	}

	public String getPartDefectDescWithDefectResultId() {
		return StringUtils.trimToEmpty("("+defectResultId+")"+inspectionPartName+" " +
				   inspectionPartLocationName+" " +
				   inspectionPartLocation2Name+" " +
				   inspectionPart2Name+" " +
				   inspectionPart2LocationName+" " +
				   inspectionPart2Location2Name+" " +
				   inspectionPart3Name+" " +
				   defectTypeName+" " +
				   defectTypeName2).replaceAll("null", "").replaceAll("\\s+"," ");
	}
	
	@Override
	public String toString() {
		return toString(getId(),getProductId(),
				getInspectionPartName(),
				getInspectionPartLocationName(),
				getInspectionPartLocation2Name(),
				getInspectionPart2Name(),
				getInspectionPart2LocationName(),
				getInspectionPart2Location2Name(),
				getInspectionPart3Name(),
				getDefectTypeName(),
				getDefectTypeName2(),
				getPointX(),
				getPointY(),
				getImageName(),
				getApplicationId(),
				getResponsibleDept(),
				getCurrentDefectStatus(),
				getDefectCategoryName(),
				getEntryDept()
				);
	}
	
	@Override
	public QiDefectResult clone() throws CloneNotSupportedException {
		QiDefectResult cloned = (QiDefectResult) super.clone();
		return cloned;
	}
	
	public String getPartDefectDescDetail() {
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
}
