package com.honda.galc.dto.qi;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.lang.StringUtils;
import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;
import com.honda.galc.entity.qi.QiResponsibleLevel;
/**
 * 
 * <h3>QiDefectResultDto Class description</h3>
 * <p>
 * QiDefectResultDto
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
 *        Oct 03, 2016
 * 
 */
public class QiDefectResultDto implements IDto,Comparator<QiDefectResultDto> {
	
	private static final long serialVersionUID = 1L;
	
	@DtoTag(outputName = "APPLICATION_ID")
	private String applicationId;
	
	@DtoTag(outputName = "COUNT")
	private String count;
	
	@DtoTag(outputName ="REGIONAL_DEFECT_COMBINATION_ID")
	private int regionalDefectCombinationId;
	
	@DtoTag(outputName ="IMAGE_SECTION_ID")
	private int imageSectionId;
	
	@DtoTag(outputName ="PART_LOCATION_ID")
	private int partLocationId;
	
	@DtoTag(outputName ="RESPONSIBLE_LEVEL_ID")
	private int responsibleLevelId;
	
	@DtoTag(outputName ="ENTRY_SCREEN")
	private String entryScreen;
	
	@DtoTag(outputName ="TEXT_ENTRY_MENU")
	private String textEntryMenu;
	
	@DtoTag(outputName ="IMAGE_NAME")
	private String imageName;
	
	@DtoTag(outputName ="IMAGE_DATA")
	private byte[] imageData;
	
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

	@DtoTag(outputName = "DEFECT_TYPE_NAME")
	private String defectTypeName;

	@DtoTag(outputName = "DEFECT_TYPE_NAME2")
	private String defectTypeName2;
	
	@DtoTag(outputName ="IS_IMAGE")
	private short isImage;
	
	@DtoTag(outputName = "LEVEL_TWO")
	private String levelTwo;
	
	@DtoTag(outputName = "LEVEL_THREE")
	private String levelThree;
	
	@DtoTag(outputName = "IQS_VERSION")
	private String iqsVersion;
	
	@DtoTag(outputName = "IQS_CATEGORY")
	private String iqsCategory;
	
	@DtoTag(outputName = "IQS_QUESTION_NO")
	private int iqsQuestionNo;
	
	@DtoTag(outputName = "IQS_QUESTION")
	private String iqsQuestion;
	
	@DtoTag(outputName = "THEME_NAME")
	private String themeName;
	
	@DtoTag(outputName = "REPORTABLE")
	private short reportable;
	
	@DtoTag(outputName = "REPAIR_AREA_NAME")
	private String repairAreaName;
	
	@DtoTag(outputName = "REPAIR_METHOD")
	private String repairMethod;
	
	@DtoTag(outputName = "REPAIR_METHOD_TIME")
	private short repairMethodTime;
	
	@DtoTag(outputName = "LOCAL_THEME")
	private String localTheme;
	
	@DtoTag(outputName = "PROCESS_NUMBER")
	private String processNumber;
	
	@DtoTag(outputName = "PROCESS_NAME")
	private String processName;
	
	@DtoTag(outputName = "UNIT_NUMBER")
	private String unitNumber;
	
	@DtoTag(outputName = "UNIT_DESC")
	private String unitDesc;
	
	@DtoTag(outputName = "DEFECT_CATEGORY_NAME")
	private String defectCategoryName;
	
	@DtoTag(outputName = "PRODUCT_ID")
	private String productId;
	
	@DtoTag(outputName = "CREATE_USER")
	private String createUser;
	
	@DtoTag(outputName = "CREATE_TIME")
	private Timestamp createTime;
	
	@DtoTag(outputName = "DEFECT_RESULT_ID")
	private long defectResultId;
	
	@DtoTag(outputName = "INCIDENT_TITLE")
	private String incidentTitle;
	
	@DtoTag(outputName = "INCIDENT_DATE")
	private Timestamp incidentDate;
	
	@DtoTag(outputName = "ENGINE_FIRING_FLAG")
	private short engineFiringFlag;
	
	@DtoTag(outputName = "INCIDENT_TYPE")
	private String incidentType;
	
	@DtoTag(outputName = "INCIDENT_ID")
	private int incidentId;
	
	@DtoTag(outputName = "LOCAL_DEFECT_COMBINATION_ID")
	private int localDefectCombinationId;
	
	@DtoTag(outputName = "MODEL_YEAR")
	private float modelYear;
	
	@DtoTag(outputName = "VEHICLE_MODEL_CODE")
	private String vehicleModelCode;
	
	private QiResponsibleLevel defaultResponsibeLevel1;
	
	private String mcSerialNumber;
	private String dcSerialNumber;	
	
	public String getMcSerialNumber() {
		return mcSerialNumber;
	}

	public void setMcSerialNumber(String mcSerialNumber) {
		this.mcSerialNumber = mcSerialNumber;
	}

	public String getDcSerialNumber() {
		return dcSerialNumber;
	}

	public void setDcSerialNumber(String dcSerialNumber) {
		this.dcSerialNumber = dcSerialNumber;
	}

	public QiResponsibleLevel getDefaultResponsibeLevel1() {
		return defaultResponsibeLevel1;
	}

	public void setDefaultResponsibeLevel1(QiResponsibleLevel defaultResponsibeLevel1) {
		this.defaultResponsibeLevel1 = defaultResponsibeLevel1;
	}

	public QiDefectResultDto() {
		super();
	}

	public String getApplicationId() {
		return StringUtils.trimToEmpty(applicationId);
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String getCount() {
		return StringUtils.trimToEmpty(count);
	}

	public void setCount(String count) {
		this.count = count;
	}

	public int getRegionalDefectCombinationId() {
		return regionalDefectCombinationId;
	}

	public void setRegionalDefectCombinationId(int regionalDefectCombinationId) {
		this.regionalDefectCombinationId = regionalDefectCombinationId;
	}

	public String getEntryScreen() {
		return StringUtils.trimToEmpty(entryScreen);
	}

	public void setEntryScreen(String entryScreen) {
		this.entryScreen = entryScreen;
	}

	public String getInspectionPartName() {
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

	public void setInspectionPart2Name(String inspectionPart2Name) {
		this.inspectionPart2Name = inspectionPart2Name;
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

	public short getIsImage() {
		return isImage;
	}

	public void setIsImage(short isImage) {
		this.isImage = isImage;
	}
	
	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public String getImageName() {
		return StringUtils.trimToEmpty(imageName);
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
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

	public int getIqsQuestionNo() {
		return iqsQuestionNo;
	}

	public void setIqsQuestionNo(int iqsQuestionNo) {
		this.iqsQuestionNo = iqsQuestionNo;
	}

	public String getIqsQuestion() {
		return StringUtils.trimToEmpty(iqsQuestion);
	}

	public void setIqsQuestion(String iqsQuestion) {
		this.iqsQuestion = iqsQuestion;
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

	public short getRepairMethodTime() {
		return repairMethodTime;
	}

	public void setRepairMethodTime(short repairMethodTime) {
		this.repairMethodTime = repairMethodTime;
	}

	public String getLocalTheme() {
		return StringUtils.trimToEmpty(localTheme);
	}

	public void setLocalTheme(String localTheme) {
		this.localTheme = localTheme;
	}

	public String getProcessNumber() {
		return StringUtils.trimToEmpty(processNumber);
	}

	public void setProcessNumber(String processNumber) {
		this.processNumber = processNumber;
	}

	public String getProcessName() {
		return StringUtils.trimToEmpty(processName);
	}

	public void setProcessName(String processName) {
		this.processName = processName;
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

	public String getDefectCategoryName() {
		return StringUtils.trimToEmpty(defectCategoryName);
	}

	public void setDefectCategoryName(String defectCategoryName) {
		this.defectCategoryName = defectCategoryName;
	}

	public int getResponsibleLevelId() {
		return responsibleLevelId;
	}

	public void setResponsibleLevelId(int responsibleLevelId) {
		this.responsibleLevelId = responsibleLevelId;
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
	
	

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((applicationId == null) ? 0 : applicationId.hashCode());
		result = prime * result + ((count == null) ? 0 : count.hashCode());
		result = prime * result
				+ ((createTime == null) ? 0 : createTime.hashCode());
		result = prime * result
				+ ((createUser == null) ? 0 : createUser.hashCode());
		result = prime
				* result
				+ ((defectCategoryName == null) ? 0 : defectCategoryName
						.hashCode());
		result = prime * result
				+ ((defectTypeName == null) ? 0 : defectTypeName.hashCode());
		result = prime * result
				+ ((defectTypeName2 == null) ? 0 : defectTypeName2.hashCode());
		result = prime * result
				+ ((entryScreen == null) ? 0 : entryScreen.hashCode());
		result = prime * result + Arrays.hashCode(imageData);
		result = prime * result
				+ ((imageName == null) ? 0 : imageName.hashCode());
		result = prime * result + imageSectionId;
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
				+ ((iqsCategory == null) ? 0 : iqsCategory.hashCode());
		result = prime * result
				+ ((iqsQuestion == null) ? 0 : iqsQuestion.hashCode());
		result = prime * result + iqsQuestionNo;
		result = prime * result
				+ ((iqsVersion == null) ? 0 : iqsVersion.hashCode());
		result = prime * result + isImage;
		result = prime * result
				+ ((levelThree == null) ? 0 : levelThree.hashCode());
		result = prime * result
				+ ((levelTwo == null) ? 0 : levelTwo.hashCode());
		result = prime * result + partLocationId;
		result = prime * result
				+ ((processName == null) ? 0 : processName.hashCode());
		result = prime * result
				+ ((processNumber == null) ? 0 : processNumber.hashCode());
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + regionalDefectCombinationId;
		result = prime * result
				+ ((repairAreaName == null) ? 0 : repairAreaName.hashCode());
		result = prime * result
				+ ((repairMethod == null) ? 0 : repairMethod.hashCode());
		result = prime * result + repairMethodTime;
		result = prime * result + reportable;
		result = prime * result + responsibleLevelId;
		result = prime
				* result
				+ ((localTheme == null) ? 0 : localTheme
						.hashCode());
		result = prime * result
				+ ((textEntryMenu == null) ? 0 : textEntryMenu.hashCode());
		result = prime * result
				+ ((themeName == null) ? 0 : themeName.hashCode());
		result = prime * result
				+ ((unitDesc == null) ? 0 : unitDesc.hashCode());
		result = prime * result
				+ ((unitNumber == null) ? 0 : unitNumber.hashCode());
		result = prime * result + localDefectCombinationId;
		result = prime * result + (int)modelYear;
		result = prime * result + ((vehicleModelCode == null) ? 0 : vehicleModelCode.hashCode());
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
		QiDefectResultDto other = (QiDefectResultDto) obj;
		if (applicationId == null) {
			if (other.applicationId != null)
				return false;
		} else if (!applicationId.equals(other.applicationId))
			return false;
		if (count == null) {
			if (other.count != null)
				return false;
		} else if (!count.equals(other.count))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
			return false;
		if (createUser == null) {
			if (other.createUser != null)
				return false;
		} else if (!createUser.equals(other.createUser))
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
		if (entryScreen == null) {
			if (other.entryScreen != null)
				return false;
		} else if (!entryScreen.equals(other.entryScreen))
			return false;
		if (!Arrays.equals(imageData, other.imageData))
			return false;
		if (imageName == null) {
			if (other.imageName != null)
				return false;
		} else if (!imageName.equals(other.imageName))
			return false;
		if (imageSectionId != other.imageSectionId)
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
		if (isImage != other.isImage)
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
		if (partLocationId != other.partLocationId)
			return false;
		if (processName == null) {
			if (other.processName != null)
				return false;
		} else if (!processName.equals(other.processName))
			return false;
		if (processNumber == null) {
			if (other.processNumber != null)
				return false;
		} else if (!processNumber.equals(other.processNumber))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (regionalDefectCombinationId != other.regionalDefectCombinationId)
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
		if (repairMethodTime != other.repairMethodTime)
			return false;
		if (reportable != other.reportable)
			return false;
		if (responsibleLevelId != other.responsibleLevelId)
			return false;
		if (localTheme == null) {
			if (other.localTheme != null)
				return false;
		} else if (!localTheme.equals(other.localTheme))
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
		if (unitDesc == null) {
			if (other.unitDesc != null)
				return false;
		} else if (!unitDesc.equals(other.unitDesc))
			return false;
		if (unitNumber == null) {
			if (other.unitNumber != null)
				return false;
		} else if (!unitNumber.equals(other.unitNumber))
			return false;
		if (localDefectCombinationId != other.localDefectCombinationId)
			return false;
		return true;
	}

	public int compare(QiDefectResultDto object1, QiDefectResultDto object2) {
		return object1.getPartDefectDesc().compareTo(object2.getPartDefectDesc());
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return the createUser
	 */
	public String getCreateUser() {
		return StringUtils.trimToEmpty(createUser);
	}

	/**
	 * @param createUser the createUser to set
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getTextEntryMenu() {
		return StringUtils.trimToEmpty(textEntryMenu);
	}

	public void setTextEntryMenu(String textEntryMenu) {
		this.textEntryMenu = textEntryMenu;
	}

	public int getImageSectionId() {
		return imageSectionId;
	}

	public void setImageSectionId(int imageSectionId) {
		this.imageSectionId = imageSectionId;
	}

	public int getPartLocationId() {
		return partLocationId;
	}

	public void setPartLocationId(int partLocationId) {
		this.partLocationId = partLocationId;
	}

	public long getDefectResultId() {
		return defectResultId;
	}

	public void setDefectResultId(long defectResultId) {
		this.defectResultId = defectResultId;
	}

	public String getIncidentTitle() {
		return incidentTitle;
	}

	public void setIncidentTitle(String incidentTitle) {
		this.incidentTitle = incidentTitle;
	}

	public void setIncidentDate(Timestamp incidentDate) {
		this.incidentDate = incidentDate;
	}

	public short getEngineFiringFlag() {
		return engineFiringFlag;
	}

	public void setEngineFiringFlag(short engineFiringFlag) {
		this.engineFiringFlag = engineFiringFlag;
	}

	public String getIncidentType() {
		return incidentType;
	}

	public void setIncidentType(String incidentType) {
		this.incidentType = incidentType;
	}

	public int getIncidentId() {
		return incidentId;
	}

	public void setIncidentId(int incidentId) {
		this.incidentId = incidentId;
	}
	
	public String getIncidentDate() {
		String format = StringUtils.EMPTY;
		if(incidentDate!=null){
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			format = formatter.format(incidentDate);
		}
		return format;
	}

	public int getLocalDefectCombinationId() {
		return localDefectCombinationId;
	}

	public void setLocalDefectCombinationId(int localDefectCombinationId) {
		this.localDefectCombinationId = localDefectCombinationId;
	}

	public float getModelYear() {
		return modelYear;
	}

	public void setModelYear(float modelYear) {
		this.modelYear = modelYear;
	}

	public String getVehicleModelCode() {
		return vehicleModelCode;
	}

	public void setVehicleModelCode(String vehicleModelCode) {
		this.vehicleModelCode = vehicleModelCode;
	}
	
	public String getCombinedDefectTypeName()  {
		String s1 = StringUtils.trimToEmpty(getDefectTypeName());
		String s2 = StringUtils.trimToEmpty(getDefectTypeName2());
		return combineTypeNames(s1, s2);
	}

	public static String combineTypeNames(String s1, String s2)  {
		StringBuilder sb = new StringBuilder();
		sb.append("");
		if(!StringUtils.isBlank(s1))  {
			sb.append(s1);
		}
		if(!StringUtils.isBlank(s2))  {
			sb.append(" - ").append(s2);
		}		
		return sb.toString();		
	}
}
