package com.honda.galc.dto.qi;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.IDto;
import com.honda.galc.entity.enumtype.QiEntryModelVersioningStatus;
import com.honda.galc.qi.constant.QiConstant;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PdcRegionalAttributeMaintDto</code> is the Dto class for Regional Part Defect Combination.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>L&T Infotech</TD>
 * <TD>14/07/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
public class PdcRegionalAttributeMaintDto implements IDto {
	
	private static final long serialVersionUID = 1L;
	
	private Integer regionalDefectCombinationId;
	
	private short active;
	
	private String productKind;
	
	private Integer partLocationId;
	
	private String inspectionPartName;

	private String inspectionPartLocationName;

	private String inspectionPartLocation2Name;

	private String inspectionPart2Name;

	private String inspectionPart2LocationName;

	private String inspectionPart2Location2Name;

	private String inspectionPart3Name;
	
	private String defectTypeName;
	
	private String defectTypeName2;
	
	private short reportable;
	private short regionalReportable;
	
	private String themeName;
	
	private String iqsVersion;
	
	private String iqsCategory;
	
	private String iqsQuestion;
	
	private int iqsQuestionNo;
	
	private Integer iqsId;
	
	private String textEntryMenu;
	
	private Integer pddaResponsibilityId;
	
	private String reportableDefect;
	
	private String fullPartName;
	
	private String responsibility;
	
	private String defectName;
	
	private String localTheme;
	
	private String createUser;
	
	private String defectCategory;
	private String regionalDefectCategory;
	
	private String entrySite;
	
	private String entryPlant;
	
	private String repairArea;
	
	private String repairMethod;
	
	private short repairTime;
	
	private Integer totalTime;
	
    private short engineFiringFlag;
	
	private Integer responsibleLevelId;
	
	private Integer localAttributeId;
	
	private short isUsed;
	
	private String pddaInfo;
	
	private Date localPdcUpdateTimestamp;
	
	public String getFullPartName() {
		return fullPartName;
	}

	public void setFullPartName(String fullPartName) {
		this.fullPartName = fullPartName;
	}
	
	public Integer getRegionalDefectCombinationId() {
		return regionalDefectCombinationId;
	}

	public void setRegionalDefectCombinationId(Integer regionalDefectCombinationId) {
		this.regionalDefectCombinationId = regionalDefectCombinationId;
	}

	public String getInspectionPartLocation2Name() {
		return StringUtils.trimToEmpty(this.inspectionPartLocation2Name);
	}

	public void setInspectionPartLocation2Name(String inspectionPartLocation2Name) {
		this.inspectionPartLocation2Name = inspectionPartLocation2Name;
	}

	public String getInspectionPart2Name() {
		return StringUtils.trimToEmpty(this.inspectionPart2Name);
	}

	public void setInspectionPart2Name(String inspectionPart2Name) {
		this.inspectionPart2Name = inspectionPart2Name;
	}

	public String getInspectionPart2LocationName() {
		return StringUtils.trimToEmpty(this.inspectionPart2LocationName);
	}

	public void setInspectionPart2LocationName(String inspectionPart2LocationName) {
		this.inspectionPart2LocationName = inspectionPart2LocationName;
	}

	public String getInspectionPart2Location2Name() {
		return StringUtils.trimToEmpty(this.inspectionPart2Location2Name);
	}

	public void setInspectionPart2Location2Name(String inspectionPart2Location2Name) {
		this.inspectionPart2Location2Name = inspectionPart2Location2Name;
	}

	public String getInspectionPart3Name() {
		return StringUtils.trimToEmpty(this.inspectionPart3Name);
	}

	public void setInspectionPart3Name(String inspectionPart3Name) {
		this.inspectionPart3Name = inspectionPart3Name;
	}

	public String getInspectionPartName() {
		return StringUtils.trimToEmpty(this.inspectionPartName);
	}

	public void setInspectionPartName(String inspectionPartName) {
		this.inspectionPartName = inspectionPartName;
	}

	public String getInspectionPartLocationName() {
		return StringUtils.trimToEmpty(this.inspectionPartLocationName);
	}

	public void setInspectionPartLocationName(String inspectionPartLocationName) {
		this.inspectionPartLocationName = inspectionPartLocationName;
	}

	public String getDefectTypeName() {
		return StringUtils.trimToEmpty(this.defectTypeName);
	}

	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}

	public String getDefectTypeName2() {
		return StringUtils.trimToEmpty(this.defectTypeName2);
	}

	public void setDefectTypeName2(String defectTypeName2) {
		this.defectTypeName2 = defectTypeName2;
	}

	public short getReportable() {
		return reportable ;
	}

	public void setReportable(short reportable) {
		this.reportable = reportable;
	}

	public String getThemeName() {
		return StringUtils.trimToEmpty(this.themeName);
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	public String getIqsVersion() {
		return StringUtils.trimToEmpty(this.iqsVersion);
	}
	
	public void setIqsVersion(String iqsVersion) {
		this.iqsVersion = iqsVersion;
	}
	
	public String getIqsCategory() {
		return StringUtils.trimToEmpty(this.iqsCategory);
	}
	
	public void setIqsCategory(String iqsCategory) {
		this.iqsCategory = iqsCategory;
	}
	
	public String getIqsQuestion() {
		return StringUtils.trimToEmpty(this.iqsQuestion);
	}
	
	public void setIqsQuestion(String iqsQuestion) {
		this.iqsQuestion = iqsQuestion;
	}

	public int getIqsQuestionNo() {
		return iqsQuestionNo;
	}

	public void setIqsQuestionNo(int iqsQuestionNo) {
		this.iqsQuestionNo = iqsQuestionNo;
	}

	public String getReportableDefect() {
		return reportable == 0 ? "Yes" : "No";
	}

	public void setReportableDefect(String reportableDefect) {
		this.reportableDefect = reportableDefect;
	}
	public short getRegionalReportable() {
		return regionalReportable;
	}

	public void setRegionalReportable(short regionalReportable) {
		this.regionalReportable = regionalReportable;
	}

	public short getActive() {
		return active;
	}
	public void setActive(short active) {
		this.active = active;
	}
	public Integer getPartLocationId() {
		return partLocationId;
	}
	public void setPartLocationId(Integer partLocationId) {
		this.partLocationId = partLocationId;
	}
	public String getProductKind() {
		return StringUtils.trimToEmpty(this.productKind);
	}
	public void setProductKind(String productKind) {
		this.productKind = productKind;
	}
	public Integer getIqsId() {
		return iqsId;
	}
	public void setIqsId(Integer iqsId) {
		this.iqsId = iqsId;
	}
	public String getTextEntryMenu() {
		return StringUtils.trimToEmpty(this.textEntryMenu);
	}
	public void setTextEntryMenu(String textEntryMenu) {
		this.textEntryMenu = textEntryMenu;
	}
	public Integer getPddaResponsibilityId() {
		return pddaResponsibilityId;
	}
	public void setPddaResponsibilityId(Integer pddaResponsibilityId) {
		this.pddaResponsibilityId = pddaResponsibilityId;
	}
	public String getResponsibility() {
		return StringUtils.trimToEmpty(responsibility);
	}
	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}
	public String getDefectName() {
		defectName=StringUtils.trimToEmpty(defectName);
		if(defectName.endsWith("-")){
			defectName =defectName.replace("-","");
		}
		return defectName;
	}
	
	public void setDefectName(String defectName) {
		this.defectName = defectName;
	}
	public String getLocalTheme() {
		return StringUtils.trimToEmpty(localTheme);
	}
	public void setLocalTheme(String localTheme) {
		this.localTheme = localTheme;
	}
	public String getCreateUser() {
		return StringUtils.trimToEmpty(createUser);
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	public boolean isDefectTypeSymptom() {
		return getDefectCategory().equals(QiConstant.SYMPTOM);
	}

	public String getDefectCategory() {
		return StringUtils.trimToEmpty(defectCategory);
	}

	public void setDefectCategory(String defectCategory) {
		this.defectCategory = defectCategory;
	}

	public String getRegionalDefectCategory() {
		return regionalDefectCategory;
	}

	public void setRegionalDefectCategory(String regionalDefectCategory) {
		this.regionalDefectCategory = regionalDefectCategory;
	}

	public String getEntrySite() {
		return StringUtils.trimToEmpty(entrySite);
	}

	public void setEntrySite(String entrySite) {
		this.entrySite = entrySite;
	}

	public String getEntryPlant() {
		return StringUtils.trimToEmpty(entryPlant);
	}

	public void setEntryPlant(String entryPlant) {
		this.entryPlant = entryPlant;
	}

	public String getRepairArea() {
		return StringUtils.trimToEmpty(repairArea);
	}

	public void setRepairArea(String repairArea) {
		this.repairArea = repairArea;
	}

	public String getRepairMethod() {
		return StringUtils.trimToEmpty(repairMethod);
	}

	public void setRepairMethod(String repairMethod) {
		this.repairMethod = repairMethod;
	}

	public short getRepairTime() {
		return repairTime;
	}

	public void setRepairTime(short repairTime) {
		this.repairTime = repairTime;
	}
	
	public Integer getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(Integer totalTime) {
		this.totalTime = totalTime;
	}

	public short getEngineFiringFlag() {
		return engineFiringFlag;
	}

	public void setEngineFiringFlag(short engineFiringFlag) {
		this.engineFiringFlag = engineFiringFlag;
	}
	
	public Integer getResponsibleLevelId() {
		return responsibleLevelId;
	}

	public void setResponsibleLevelId(Integer responsibleLevelId) {
		this.responsibleLevelId = responsibleLevelId;
	}
	
	public String getPddaInfo() {
		return pddaInfo;
	}
	public void setPddaInfo(String pddaInfo) {
		this.pddaInfo = pddaInfo;
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
	
	public Integer getLocalAttributeId() {
		return localAttributeId;
	}
	public void setLocalAttributeId(Integer localAttributeId) {
		this.localAttributeId = localAttributeId;
	}
	public short getIsUsed() {
		return isUsed;
	}
	
	public void setIsUsed(short isUsed) {
		this.isUsed = isUsed;
	}
	
	public String getIsUsedVersionData() {
		return QiEntryModelVersioningStatus.getType(getIsUsed()).getName();
	}
	
	public Date getLocalPdcUpdateTimestamp() {
		return localPdcUpdateTimestamp;
	}

	public void setLocalPdcUpdateTimestamp(Date localPdcUpdateTimestamp) {
		this.localPdcUpdateTimestamp = localPdcUpdateTimestamp;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result + ((createUser == null) ? 0 : createUser.hashCode());
		result = prime * result + ((defectCategory == null) ? 0 : defectCategory.hashCode());
		result = prime * result + ((defectName == null) ? 0 : defectName.hashCode());
		result = prime * result + ((defectTypeName == null) ? 0 : defectTypeName.hashCode());
		result = prime * result + ((defectTypeName2 == null) ? 0 : defectTypeName2.hashCode());
		result = prime * result + engineFiringFlag;
		result = prime * result + ((entryPlant == null) ? 0 : entryPlant.hashCode());
		result = prime * result + ((entrySite == null) ? 0 : entrySite.hashCode());
		result = prime * result + ((fullPartName == null) ? 0 : fullPartName.hashCode());
		result = prime * result
				+ ((inspectionPart2Location2Name == null) ? 0 : inspectionPart2Location2Name.hashCode());
		result = prime * result + ((inspectionPart2LocationName == null) ? 0 : inspectionPart2LocationName.hashCode());
		result = prime * result + ((inspectionPart2Name == null) ? 0 : inspectionPart2Name.hashCode());
		result = prime * result + ((inspectionPart3Name == null) ? 0 : inspectionPart3Name.hashCode());
		result = prime * result + ((inspectionPartLocation2Name == null) ? 0 : inspectionPartLocation2Name.hashCode());
		result = prime * result + ((inspectionPartLocationName == null) ? 0 : inspectionPartLocationName.hashCode());
		result = prime * result + ((inspectionPartName == null) ? 0 : inspectionPartName.hashCode());
		result = prime * result + ((iqsCategory == null) ? 0 : iqsCategory.hashCode());
		result = prime * result + ((iqsId == null) ? 0 : iqsId.hashCode());
		result = prime * result + ((iqsQuestion == null) ? 0 : iqsQuestion.hashCode());
		result = prime * result + iqsQuestionNo;
		result = prime * result + ((iqsVersion == null) ? 0 : iqsVersion.hashCode());
		result = prime * result + isUsed;
		result = prime * result + ((localAttributeId == null) ? 0 : localAttributeId.hashCode());
		result = prime * result + ((localTheme == null) ? 0 : localTheme.hashCode());
		result = prime * result + ((partLocationId == null) ? 0 : partLocationId.hashCode());
		result = prime * result + ((pddaInfo == null) ? 0 : pddaInfo.hashCode());
		result = prime * result + ((pddaResponsibilityId == null) ? 0 : pddaResponsibilityId.hashCode());
		result = prime * result + ((productKind == null) ? 0 : productKind.hashCode());
		result = prime * result + ((regionalDefectCombinationId == null) ? 0 : regionalDefectCombinationId.hashCode());
		result = prime * result + ((repairArea == null) ? 0 : repairArea.hashCode());
		result = prime * result + ((repairMethod == null) ? 0 : repairMethod.hashCode());
		result = prime * result + repairTime;
		result = prime * result + totalTime;
		result = prime * result + reportable;
		result = prime * result + ((reportableDefect == null) ? 0 : reportableDefect.hashCode());
		result = prime * result + ((responsibility == null) ? 0 : responsibility.hashCode());
		result = prime * result + ((responsibleLevelId == null) ? 0 : responsibleLevelId.hashCode());
		result = prime * result + ((textEntryMenu == null) ? 0 : textEntryMenu.hashCode());
		result = prime * result + ((themeName == null) ? 0 : themeName.hashCode());
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
		PdcRegionalAttributeMaintDto other = (PdcRegionalAttributeMaintDto) obj;
		if (active != other.active)
			return false;
		if (createUser == null) {
			if (other.createUser != null)
				return false;
		} else if (!createUser.equals(other.createUser))
			return false;
		if (defectCategory == null) {
			if (other.defectCategory != null)
				return false;
		} else if (!defectCategory.equals(other.defectCategory))
			return false;
		if (defectName == null) {
			if (other.defectName != null)
				return false;
		} else if (!defectName.equals(other.defectName))
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
		if (engineFiringFlag != other.engineFiringFlag)
			return false;
		if (entryPlant == null) {
			if (other.entryPlant != null)
				return false;
		} else if (!entryPlant.equals(other.entryPlant))
			return false;
		if (entrySite == null) {
			if (other.entrySite != null)
				return false;
		} else if (!entrySite.equals(other.entrySite))
			return false;
		if (fullPartName == null) {
			if (other.fullPartName != null)
				return false;
		} else if (!fullPartName.equals(other.fullPartName))
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
		if (iqsCategory == null) {
			if (other.iqsCategory != null)
				return false;
		} else if (!iqsCategory.equals(other.iqsCategory))
			return false;
		if (iqsId == null) {
			if (other.iqsId != null)
				return false;
		} else if (!iqsId.equals(other.iqsId))
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
		if (isUsed != other.isUsed)
			return false;
		if (localAttributeId == null) {
			if (other.localAttributeId != null)
				return false;
		} else if (!localAttributeId.equals(other.localAttributeId))
			return false;
		if (localTheme == null) {
			if (other.localTheme != null)
				return false;
		} else if (!localTheme.equals(other.localTheme))
			return false;
		if (partLocationId == null) {
			if (other.partLocationId != null)
				return false;
		} else if (!partLocationId.equals(other.partLocationId))
			return false;
		if (pddaInfo == null) {
			if (other.pddaInfo != null)
				return false;
		} else if (!pddaInfo.equals(other.pddaInfo))
			return false;
		if (pddaResponsibilityId == null) {
			if (other.pddaResponsibilityId != null)
				return false;
		} else if (!pddaResponsibilityId.equals(other.pddaResponsibilityId))
			return false;
		if (productKind == null) {
			if (other.productKind != null)
				return false;
		} else if (!productKind.equals(other.productKind))
			return false;
		if (regionalDefectCombinationId == null) {
			if (other.regionalDefectCombinationId != null)
				return false;
		} else if (!regionalDefectCombinationId.equals(other.regionalDefectCombinationId))
			return false;
		if (repairArea == null) {
			if (other.repairArea != null)
				return false;
		} else if (!repairArea.equals(other.repairArea))
			return false;
		if (repairMethod == null) {
			if (other.repairMethod != null)
				return false;
		} else if (!repairMethod.equals(other.repairMethod))
			return false;
		if (repairTime != other.repairTime)
			return false;
		if(totalTime != other.totalTime)
			return false;
		if (reportable != other.reportable)
			return false;
		if (reportableDefect == null) {
			if (other.reportableDefect != null)
				return false;
		} else if (!reportableDefect.equals(other.reportableDefect))
			return false;
		if (responsibility == null) {
			if (other.responsibility != null)
				return false;
		} else if (!responsibility.equals(other.responsibility))
			return false;
		if (responsibleLevelId == null) {
			if (other.responsibleLevelId != null)
				return false;
		} else if (!responsibleLevelId.equals(other.responsibleLevelId))
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
		return true;
	}
	/**
	 * To set value in DTO
	 * @param qiDtoList
	 * @param list
	 */
	public void setLocalAttributeDtoList(List<PdcRegionalAttributeMaintDto> qiLocalDtoList, List<Object[]> queryResultlist) {
		PdcRegionalAttributeMaintDto dto;
		if(queryResultlist!=null && !queryResultlist.isEmpty()){
			for(Object[] obj : queryResultlist){
				dto = new PdcRegionalAttributeMaintDto();
				dto.setRegionalDefectCombinationId(Integer.parseInt(obj[0].toString()));
				dto.setDefectTypeName(obj[1].toString());
				dto.setDefectTypeName2(obj[2]== null ? StringUtils.EMPTY :obj[2].toString());
				dto.setReportable(obj[3]== null ? 0 :(short)Integer.parseInt(obj[3].toString()));
				dto.setThemeName(obj[4]== null ? StringUtils.EMPTY :obj[4].toString());
				dto.setIqsVersion(obj[5]== null ? StringUtils.EMPTY :obj[5].toString());
				dto.setIqsCategory(obj[6]== null ? StringUtils.EMPTY :obj[6].toString());
				dto.setIqsQuestion(obj[7]== null ? StringUtils.EMPTY :obj[7].toString());
				dto.setIqsQuestionNo(obj[8]== null ? 0 :Integer.parseInt(obj[8].toString()));
				dto.setPartLocationId(Integer.parseInt(obj[9].toString()));
				dto.setActive(obj[10]== null ? 0 :(short)Integer.parseInt(obj[10].toString()));
				dto.setProductKind(obj[11]== null ? StringUtils.EMPTY :obj[11].toString());
				dto.setIqsId(obj[12]== null ? 0 :Integer.parseInt(obj[12].toString()));
				dto.setTextEntryMenu(obj[13]== null ? StringUtils.EMPTY :obj[13].toString());
				dto.setPddaResponsibilityId(obj[14]== null ? 0 :Integer.parseInt(obj[14].toString()));
				dto.setFullPartName(obj[15].toString());
				dto.setDefectName(obj[16]== null ? StringUtils.EMPTY :obj[16].toString());
				dto.setResponsibility(obj[17]== null ? StringUtils.EMPTY :obj[17].toString());
				dto.setLocalTheme(obj[18]== null ? StringUtils.EMPTY :obj[18].toString());
				dto.setPddaInfo(obj[19]== null ? StringUtils.EMPTY :obj[19].toString());
				dto.setIsUsed(obj[20]== null ? 0 :(short)Integer.parseInt(obj[20].toString()));
				dto.setDefectCategory(obj[21]== null ? StringUtils.EMPTY :obj[21].toString());
				dto.setEntrySite(obj[22]== null ? StringUtils.EMPTY :obj[22].toString());
				dto.setEntryPlant(obj[23]== null ? StringUtils.EMPTY :obj[23].toString());
				dto.setRepairArea(obj[24]== null ? StringUtils.EMPTY :obj[24].toString());
				dto.setRepairMethod(obj[25]== null ? StringUtils.EMPTY :obj[25].toString());
				dto.setRepairTime(obj[26]== null ? 0 :(short)Integer.parseInt(obj[26].toString()));
				dto.setEngineFiringFlag(obj[27]== null ? 0 :(short)Integer.parseInt(obj[27].toString()));
				dto.setResponsibleLevelId(obj[28]== null ? 0 :Integer.parseInt(obj[28].toString()));
				dto.setLocalAttributeId(obj[29]== null ? 0 :Integer.parseInt(obj[29].toString()));
				
				dto.setInspectionPartName(obj[30]== null ? StringUtils.EMPTY :obj[30].toString());
				dto.setInspectionPartLocationName(obj[31]== null ? StringUtils.EMPTY :obj[31].toString());
				dto.setInspectionPartLocation2Name(obj[32]== null ? StringUtils.EMPTY :obj[32].toString());
				dto.setInspectionPart2Name(obj[33]== null ? StringUtils.EMPTY :obj[33].toString());
				dto.setInspectionPart2LocationName(obj[34]== null ? StringUtils.EMPTY :obj[34].toString());
				dto.setInspectionPart3Name(obj[35]== null ? StringUtils.EMPTY :obj[35].toString());
				dto.setLocalPdcUpdateTimestamp(obj[36]== null ? null : (Date)obj[36]);
				dto.setRegionalReportable(obj[37]== null ? 0 :(short)Integer.parseInt(obj[37].toString()));
				dto.setRegionalDefectCategory(obj[38]== null ? StringUtils.EMPTY :obj[38].toString());
				dto.setTotalTime(obj[39]== null ? 0 :Integer.parseInt(obj[39].toString()));
				qiLocalDtoList.add(dto);
			}
		}
	}
	
}
